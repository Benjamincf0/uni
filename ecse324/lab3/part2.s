.section .vectors, "ax"
B _start // reset vector
B SERVICE_UND // undefined instruction vector
B SERVICE_SVC // software interrupt vector
B SERVICE_ABT_INST // aborted prefetch vector
B SERVICE_ABT_DATA // aborted data vector
.word 0 // unused vector
B SERVICE_IRQ // IRQ interrupt vector
B SERVICE_FIQ // FIQ interrupt vector

.text
.global _start
_start:
 /* Set up stack pointers for IRQ and SVC processor modes */
 MOV R1, #0b11010010 // interrupts masked, MODE = IRQ
 MSR CPSR_c, R1 // change to IRQ mode
 LDR SP, =0xFFFFFFFF - 3 // set IRQ stack to A9 on-chip memory
 /* Change to SVC (supervisor) mode with interrupts disabled */
 MOV R1, #0b11010011 // interrupts masked, MODE = SVC
 MSR CPSR, R1 // change to supervisor mode
 LDR SP, =0x3FFFFFFF - 3 // set SVC stack to top of DDR3 memory
 BL CONFIG_GIC // configure the ARM GIC
 // NOTE: write to the pushbutton KEY interrupt mask register
 // Or, you can call enable_PB_INT_ASM subroutine from previous task
 // to enable interrupt for ARM A9 private timer,
 // use ARM_TIM_config_ASM subroutine
 LDR R0, =0xFF200050 // pushbutton KEY base address
 MOV R1, #0xF // set interrupt mask bits
 STR R1, [R0, #0x8] // interrupt mask register (base + 8)

 // Set initial speed
 MOV R8, #2 // start at 1/4 second per move
 BL set_speed

 MOV R5, #0 // start at first character
 MOV R6, #1 // +1 = left, -1 = right
 MOV R7, #0 // 0 = moving, 1 = paused
 LDR A1, =MSG_C0FFEE
 MOV A2, #6
 BL display_hex // show C0FFEE

 // enable IRQ interrupts in the processor
 MOV R0, #0b01010011 // IRQ unmasked, MODE = SVC
 MSR CPSR_c, R0
IDLE:
 LDR A2, =PB_int_flag // button flag address
 LDR A1, [A2]
 MOV R9, A1 // keep button bits across driver calls
 CMP A1, #0
 BEQ check_timer // no button event => check timer.
 MOV A3, #0
 STR A3, [A2] // clear the button flag
 CMP R7, #0
 BNE check_pause // PB0-PB2 do nothing while paused
 TST R9, #0x04 // PB2 reverses direction
 RSBNE R6, R6, #0

 MOV R10, R8 // remember the old speed
 TST R9, #0x02 // PB1 makes it faster
 BEQ check_slower
 CMP R8, #4
 ADDLT R8, R8, #1
check_slower:
 TST R9, #0x01 // PB0 makes it slower
 BEQ apply_speed
 CMP R8, #0
 SUBGT R8, R8, #1
apply_speed:
 CMP R8, R10
 BLNE set_speed // update timer and LEDs only if speed changed

check_pause:
 TST R9, #0x08 // PB3 toggles pause
 BEQ check_timer
 EOR R7, R7, #1
 CMP R7, #0
 BNE pause_leds
 BL show_speed_LEDs // restore speed lights on resume
 B check_timer
pause_leds:
 MOV A1, #0
 BL write_LEDs_ASM // no lights while paused

check_timer:
 LDR A2, =tim_int_flag // timer flag address
 LDR A1, [A2] // did the timer tick?
 CMP A1, #0
 BEQ IDLE // if not => stay still.
 MOV A1, #0
 STR A1, [A2] // clear the flag
 CMP R7, #0 // check paused status
 BNE IDLE // stay still while paused
 ADD R5, R5, R6 // move one character in the current direction
 CMP R5, #6
 MOVEQ R5, #0 // wrap left
 CMP R5, #0
 MOVLT R5, #5 // wrap right
 LDR A1, =MSG_C0FFEE
 MOV A2, #6
 BL display_hex // show the shifted message
 B IDLE

set_speed:
 PUSH {LR}
 LDR A3, =SPEED_LOADS
 LDR A1, [A3, R8, LSL #2] // timer load for this speed
 MOV A2, #0x07 // run, reload, and interrupt
 BL ARM_TIM_config_ASM
 BL show_speed_LEDs
 POP {PC}

show_speed_LEDs:
 LDR A3, =SPEED_LEDS
 LDR A1, [A3, R8, LSL #2]
 B write_LEDs_ASM

CONFIG_GIC:
 PUSH {LR}
/* To configure the FPGA KEYS interrupt (ID 73):
* 1. set the target to cpu0 in the ICDIPTRn register
* 2. enable the interrupt in the ICDISERn register */
/* CONFIG_INTERRUPT (int_ID (R0), CPU_target (R1)); */
/* NOTE: you can configure different interrupts
 by passing their IDs to R0 and repeating the next 3 lines */
 MOV R0, #73 // KEY port (Interrupt ID = 73)
 MOV R1, #1 // this field is a bit-mask; bit 0 targets cpu0
 BL CONFIG_INTERRUPT
 MOV R0, #29 // A9 private timer (ID 29)
 MOV R1, #1 // this field is a bit-mask; bit 0 targets cpu0
 BL CONFIG_INTERRUPT
/* configure the GIC CPU Interface */
 LDR R0, =0xFFFEC100 // base address of CPU Interface
/* Set Interrupt Priority Mask Register (ICCPMR) */
 LDR R1, =0xFFFF // enable interrupts of all priorities levels
 STR R1, [R0, #0x04]
/* Set the enable bit in the CPU Interface Control Register (ICCICR).
* This allows interrupts to be forwarded to the CPU(s) */
 MOV R1, #1
 STR R1, [R0]
/* Set the enable bit in the Distributor Control Register (ICDDCR).
* This enables forwarding of interrupts to the CPU Interface(s) */
 LDR R0, =0xFFFED000
 STR R1, [R0]
 POP {PC} // weird way to return...

/*
* Configure registers in the GIC for an individual Interrupt ID
* We configure only the Interrupt Set Enable Registers (ICDISERn) and
* Interrupt Processor Target Registers (ICDIPTRn). The default (reset)
* values are used for other registers in the GIC
* Arguments: R0 = Interrupt ID, N
* R1 = CPU target
*/
CONFIG_INTERRUPT:
 PUSH {R4-R5, LR}
/* Configure Interrupt Set-Enable Registers (ICDISERn).
* reg_offset = (integer_div(N / 32) * 4
* value = 1 << (N mod 32) */
 LSR R4, R0, #3 // calculate reg_offset
 BIC R4, R4, #3 // R4 = reg_offset
 LDR R2, =0xFFFED100
 ADD R4, R2, R4 // R4 = address of ICDISER
 AND R2, R0, #0x1F // N mod 32
 MOV R5, #1 // enable
 LSL R2, R5, R2 // R2 = value
/* Using the register address in R4 and the value in R2 set the
* correct bit in the GIC register */
 LDR R3, [R4] // read current register value
 ORR R3, R3, R2 // set the enable bit
 STR R3, [R4] // store the new register value
/* Configure Interrupt Processor Targets Register (ICDIPTRn)
* reg_offset = integer_div(N / 4) * 4
* index = N mod 4 */
 BIC R4, R0, #3 // R4 = reg_offset
 LDR R2, =0xFFFED800
 ADD R4, R2, R4 // R4 = word address of ICDIPTR
 AND R2, R0, #0x3 // N mod 4
 ADD R4, R2, R4 // R4 = byte address in ICDIPTR
/* Using register address in R4 and the value in R2 write to
* (only) the appropriate byte */
 STRB R1, [R4]
 POP {R4-R5, PC}

/*--- Undefined instructions --------------------------------------*/
SERVICE_UND:
 B SERVICE_UND
/*--- Software interrupts ----------------------------------------*/
SERVICE_SVC:
 B SERVICE_SVC
/*--- Aborted data reads ------------------------------------------*/
SERVICE_ABT_DATA:
 B SERVICE_ABT_DATA
/*--- Aborted instruction fetch -----------------------------------*/
SERVICE_ABT_INST:
 B SERVICE_ABT_INST
/*--- IRQ ---------------------------------------------------------*/
SERVICE_IRQ:
 PUSH {R0-R7, LR}
/* Read the ICCIAR from the CPU Interface */
 LDR R4, =0xFFFEC100
 LDR R5, [R4, #0x0C] // read from ICCIAR
/* NOTE: Check which interrupt has occurred (check interrupt IDs)
 Then call the corresponding ISR
 If the ID is not recognized, branch to UNEXPECTED
 See the assembly example provided in the DE1-SoC Computer Manual
 on page 46 */
Tim_check:
 CMP R5, #29
 BNE Pushbutton_check
 BL ARM_TIM_ISR
 B EXIT_IRQ
Pushbutton_check:
 CMP R5, #73
UNEXPECTED:
 BNE UNEXPECTED // if not recognized, stop here
 BL KEY_ISR
EXIT_IRQ:
/* Write to the End of Interrupt Register (ICCEOIR) */
 STR R5, [R4, #0x10] // write to ICCEOIR
 POP {R0-R7, LR}
 SUBS PC, LR, #4
/*--- FIQ ---------------------------------------------------------*/
SERVICE_FIQ:
 B SERVICE_FIQ
 




// ----------------- Timer drivers -----------------
.equ tim_base_addr, 0xFFFEC600
.equ tim_ctrl_addr, 0xFFFEC608
.equ tim_status_addr, 0xFFFEC60C
ARM_TIM_config_ASM: # A1: ld, A2: ctrl
 ldr A3, =tim_base_addr
 str A1, [A3]
 str A2, [A3, #8]
 bx LR

ARM_TIM_read_INT_ASM:
 ldr A1, =tim_status_addr
 ldr A1, [A1]
 and A1, A1, #1
 bx LR

ARM_TIM_clear_INT_ASM:
 ldr A1, =tim_status_addr
 mov A2, #1
 str A2, [A1]
 bx LR


// Interrupt Service Routines (ISRs)
.data
tim_int_flag: .word 0
.text
ARM_TIM_ISR:
 PUSH {LR}
 bl ARM_TIM_clear_INT_ASM // clear F

 LDR A2, =tim_int_flag
 MOV A1, #1
 STR A1, [A2]

 POP {LR}
 BX LR

.data
PB_int_flag: .word 0
.text
KEY_ISR:
 PUSH {LR}
 BL read_PB_edgecp_ASM
 LDR A2, =PB_int_flag
 STR A1, [A2]
 BL PB_clear_edgecp_ASM
 POP {LR}
 BX LR


// Speed levels 0-4: slowest to fastest.
SPEED_LOADS:
 .word 200000000, 100000000, 50000000, 25000000, 12500000
SPEED_LEDS:
 .word 0x003, 0x00F, 0x03F, 0x0FF, 0x3FF

// part 1 stuff:
HEX_CODES:
 .byte 0b00111111, 0b00000110, 0b01011011, 0b01001111 // 0, 1, 2, 3
 .byte 0b01100110, 0b01101101, 0b01111101, 0b00000111 // 4, 5, 6, 7
 .byte 0b01111111, 0b01101111, 0b01110111, 0b01111100 // 8, 9, A, b
 .byte 0b00111001, 0b01011110, 0b01111001, 0b01110001 

.equ BLANK, 0xFF // blank display marker
MSG_C0FFEE:
 .byte 0xC, 0x0, 0xF, 0xF, 0xE, 0xE // one byte per character
.balign 4

display_hex: // A1: message, A2: length, R5: first character
 PUSH {R6, LR}
 MOV R6, R5 // current character index
 MOV A4, #0x20 // start at HEX5
 dloop:
  CMP A2, #0
  BEQ dloopEnd
  LDRB A3, [A1, R6] // get the current character
  PUSH {A1, A2, A4, LR}
  MOV A2, A3
  MOV A1, A4
  CMP A2, #BLANK // blank or digit?
  BNE display_digit
  BL HEX_clear_ASM
  B display_next
 display_digit:
  BL HEX_write_ASM
 display_next:
  POP {A1, A2, A4, LR}
  ADD R6, R6, #1
  CMP R6, #6
  MOVEQ R6, #0 // wrap to first character
  LSR A4, A4, #1 // move to next HEX display
  SUB A2, A2, #1
  B dloop
 dloopEnd:
 POP {R6, LR}
 BX LR
 
// HEX Display Driver
.equ SEG_ADDR1, 0xFF200020
.equ SEG_ADDR2, 0xFF200030
HEX_helper_ASM_:
 MOV A3, #1 // Filter...
 LDR A4, =SEG_ADDR1
 loop2:
 CMP A3, #0x40
 BEQ loopEnd

 CMP A3, #0x10
 LDREQ A4, =SEG_ADDR2

 TST A1, A3
 BEQ skip_store
 // store

 STRB A2, [A4]
 skip_store:
 ADD A4, A4, #1
 LSL A3, A3, #1
 B loop2
 loopEnd:
 BX LR

HEX_write_ASM:
 LDR A3, =HEX_CODES
 LDRB A2, [A3, A2]
 B HEX_helper_ASM_

HEX_flood_ASM:
 MOV A2, #0x7F
 B HEX_helper_ASM_

HEX_clear_ASM:
 MOV A2, #0x00
 B HEX_helper_ASM_

.equ LED_ADDR, 0xFF200000
write_LEDs_ASM:
 LDR A2, =LED_ADDR
 STR A1, [A2]
 BX LR

.equ PB_EDGE_ADDR, 0xFF20005C
read_PB_edgecp_ASM:
 LDR A2, =PB_EDGE_ADDR // load the address of push button state
 LDR A1, [A2] // read push button state
 BX LR

PB_clear_edgecp_ASM:
 PUSH {LR}
 BL read_PB_edgecp_ASM
 POP {LR}
 STR A1, [A2]
 BX LR

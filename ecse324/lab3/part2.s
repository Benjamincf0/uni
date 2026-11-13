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
 // enable IRQ interrupts in the processor
 MOV R0, #0b01010011 // IRQ unmasked, MODE = SVC
 MSR CPSR_c, R0
IDLE:
 B IDLE // This is where you write your main program task(s)



ldr A1, =50000000
mov A2, #0x03
bl ARM_TIM_config_ASM
mov r4, #0 // count


// display first
mov a2, r4
mov a1, #1
bl HEX_write_ASM

poll:
bl ARM_TIM_read_INT_ASM
TST A1, #1
beq poll // if not F, keep polling

bl ARM_TIM_clear_INT_ASM // clear F

add r4, r4, #1
and r4, r4, #0xf // wraps when it reaches 15
mov a2, r4
mov a1, #1
bl HEX_write_ASM

b poll

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
 POP {PC}

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


.data
PB_int_flag: .word 0
.text

KEY_ISR:
 PUSH {LR}
 BL read_PB_edgecp_ASM
 LDR A2, =PB_int_flag
 STR A1, [A2]
 BL PB_clear_edgecp_ASM
END_KEY_ISR:
 POP {LR}
 BX LR


// part 1 stuff:
HEX_CODES:
 .byte 0b00111111, 0b00000110, 0b01011011, 0b01001111 // 0, 1, 2, 3
 .byte 0b01100110, 0b01101101, 0b01111101, 0b00000111 // 4, 5, 6, 7
 .byte 0b01111111, 0b01101111, 0b01110111, 0b01111100 // 8, 9, A, b
 .byte 0b00111001, 0b01011110, 0b01111001, 0b01110001 
 
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

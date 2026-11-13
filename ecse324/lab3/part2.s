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

.section .vectors, "ax"
 B _start             // reset
 B SERVICE_UND        // undefined instruction
 B SERVICE_SVC        // supervisor call
 B SERVICE_ABT_INST   // instruction abort
 B SERVICE_ABT_DATA   // data abort
 .word 0              // unused
 B SERVICE_IRQ        // interrupt
 B SERVICE_FIQ        // fast interrupt

.text
.global _start
_start:
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

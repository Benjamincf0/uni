.global _start
_start:

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

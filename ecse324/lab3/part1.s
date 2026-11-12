.global _start
_start:
loop:
 BL read_slider_switches_ASM
 BL write_LEDs_ASM
 B loop


// Slider Switches Driver
// returns the state of slider switches in A1
// post- A1: slide switch state
.equ SW_ADDR, 0xFF200040
read_slider_switches_ASM:
 LDR A2, =SW_ADDR // load the address of slider switch state
 LDR A1, [A2] // read slider switch state
 BX LR
// LEDs Driver
// writes the state of LEDs (On/Off) in A1 to the LEDs' control register
// pre-- A1: data to write to LED state
.equ LED_ADDR, 0xFF200000
write_LEDs_ASM:
 LDR A2, =LED_ADDR // load the address of the LEDs' state
 STR A1, [A2] // update LED state with the contents of A1
 BX LR

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


// Push Button Driver
.equ PB_ADDR, 0xFF200050
read_PB_data_ASM:
 LDR A2, =PB_ADDR // load the address of push button state
 LDR A1, [A2] // read push button state
 BX LR

PB_data_is_pressed_ASM:
 MOV A2, A1 // save index.
 PUSH {LR, A2}
 BL read_PB_data_ASM // writes data into A1.
 POP {LR, A2}
 // MOV A3, #1 // bitmask
 // LSL A3, A3, A2 // mask for 1 at the ith button.
 TST A2, A1 // check the ith button is ON.
 MOVEQ A1, #0
 MOVNE A1, #1
 BX LR

.equ PB_EDGE_ADDR, 0xFF20005C
read_PB_edgecp_ASM:
 LDR A2, =PB_EDGE_ADDR // load the address of push button state
 LDR A1, [A2] // read push button state
 BX LR

PB_edgecp_is_pressed_ASM:
 MOV A2, A1 // save index.
 PUSH {LR, A2}
 BL read_PB_edgecp_ASM // writes data into A1.
 POP {LR, A2}
 TST A2, A1 // check the ith button is ON.
 MOVEQ A1, #0
 MOVNE A1, #1
 BX LR

PB_clear_edgecp_ASM:
 PUSH {LR}
 BL read_PB_edgecp_ASM
 POP {LR}
 STR A1, [A2]
 BX LR


.equ PB_INT_ADDR, 0xFF200058
enable_PB_INT_ASM:
 LDR A2, =PB_INT_ADDR // load the address
 LDR A3, [A2]
 ORR A3, A3, A1
 STR A3, [A2]
 BX LR

disable_PB_INT_ASM:
 LDR A2, =PB_INT_ADDR // load the address
 LDR A3, [A2]
 BIC A3, A3, A1 // A3 = A3 AND NOT A1
 STR A3, [A2]
 BX LR

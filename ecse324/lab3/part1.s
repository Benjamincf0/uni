.global _start
_start:
start:
 MVN R4, #0 // previous switch state (0xFFFFFFFF)
 MOV R5, #0 // rotate offset...
 MOV R6, #1 // 1 is left, -1 is right rotations
 MOV R7, #0 // count rotations up to 1023
loop:
 BL read_slider_switches_ASM
 CMP A1, R4
 MOVNE R5, #0 // a new message starts left-justified
 MOVNE R7, #0 // reset the count only when the switches change
 MOV R4, A1 // update prev state.

 BL read_PB_edgecp_ASM
 MOV R9, A1
 CMP R9, #0
 BLNE PB_clear_edgecp_ASM

 TST R9, #0x04 // check PB2
 RSBNE R6, R6, #0 // rev-subtract/toggle +1/-1
 TST R9, #0x08 // PB3 was pressed => rotation
 BEQ show_count
 // Only switch values 0, 1, 2 and 4 select a message.
 CMP R4, #4
 BHI show_count
 CMP R4, #3
 BEQ show_count // ignore rotate if the display is blank

 ADD R5, R5, R6 // increment/decrement rotate offset
 // wrap to keep in range.
 CMP R5, #6
 MOVEQ R5, #0 // 5 + 1 => 0
 CMP R5, #0
 MOVLT R5, #5 // 0 - 1 => 5

 LDR A1, =1023
 CMP R7, A1
 ADDLO R7, R7, #1 // keep 1023 once all ten LEDs are lit

show_count:
 MOV A1, R7
 BL write_LEDs_ASM
 MOV A1, R4 // recover the switch value for message selection

 CMP A1, #0x00
 LDREQ A1, =MSG_C0FFEE
 BEQ show_message
 CMP A1, #0x01
 LDREQ A1, =MSG_CAFE5
 BEQ show_message
 CMP A1, #0x02
 LDREQ A1, =MSG_CAb5
 BEQ show_message
 CMP A1, #0x04
 LDREQ A1, =MSG_ACE
 BEQ show_message
 LDR A1, =MSG_BLANK // else just blank all 6...

 show_message: // display_hex w 6 bytes...
  MOV A2, #6
  BL display_hex
 B loop

.equ BLANK, 0xFF // used in display_hex
MESSAGES:
MSG_C0FFEE:
 .byte 0xC, 0x0, 0xF, 0xF, 0xE, 0xE // 6 digits
MSG_CAFE5:
 .byte 0xC, 0xA, 0xF, 0xE, 0x5, BLANK
MSG_CAb5:
 .byte 0xC, 0xA, 0xB, 0x5, BLANK, BLANK
MSG_ACE:
 .byte 0xA, 0xC, 0xE, BLANK, BLANK, BLANK
MSG_BLANK:
 .byte BLANK, BLANK, BLANK, BLANK, BLANK, BLANK

.balign 4

display_hex: // A1: message base, A2: num bytes, R5: rotation offset (0-5)
 PUSH {R6, LR}
 MOV R6, R5 // local read index; leave the rotation offset unchanged
 MOV A4, #0x20
 dloop:
  CMP A2, #0
  BEQ dloopEnd
  LDRB A3, [A1, R6] // keep the base fixed and read at the current index
  PUSH {A1, A2, A4, LR}
  MOV A2, A3
  MOV A1, A4
  CMP A2, #BLANK // check if we have to clear
  BNE display_digit
  BL HEX_clear_ASM // clear hex
  B display_next
 display_digit:
  BL HEX_write_ASM
 display_next:
  POP {A1, A2, A4, LR}
  ADD R6, R6, #1
  CMP R6, #6
  MOVEQ R6, #0 // wrap to the start of the six-byte message
  LSR A4, A4, #1
  SUB A2, A2, #1
  B dloop
 dloopEnd:
 POP {R6, LR}
 BX LR


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

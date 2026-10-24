.global _start
Stuff: .byte 42, -7, 0, 127, -128, 5, -7, 99, -1, 5, 12 // test input
.space 1 // to align next word
N: .word 11 // number of elements in Stuff
Sorted: .space 11 // (optional) sorted output
.space 1 // to align next word
_start:
ldr A1, =Stuff // get the address of Stuff
ldr A2, N // get the number of things in Stuff
bl sort // go!
stop:
b stop
// In-place bubble sort.
// pre-- A1: Address of array of signed bytes to be sorted
// pre-- A2: Number of elements in array
// post- A1: Address of sorted array
sort:
    CMP A2, #1
    BXLE LR
    PUSH {R4}

    ADD A2, A1, A2
    SUB A2, A2, #1

bubblePass:
    MOV A3, A1
    MOV R4, #0 // Track swaps this pass.

bubbleLoop:
    CMP A3, A2
    BHS passDone
    LDRSB A4, [A3]
    LDRSB R12, [A3, #1]
    CMP A4, R12
    BLE nextPair

    STRB R12, [A3]
    STRB A4, [A3, #1]
    MOV R4, #1

nextPair:
    ADD A3, A3, #1
    B bubbleLoop

passDone:
    CMP R4, #0
    BEQ sortDone // Stop if nothing changed.
    SUB A2, A2, #1
    CMP A2, A1
    BHI bubblePass

sortDone:
    POP {R4}
    BX LR


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
// Recursive merge sort of signed bytes, in place using element shifts.
// pre-- A1: Address of array of signed bytes to be sorted
// pre-- A2: Number of elements in array
// post- A1: Address of sorted array
sort:
    CMP A2, #1
    BXLE LR
    PUSH {R4, LR}
    MOV R4, A1
    SUB A3, A2, #1              // right = length - 1 (inclusive)
    MOV A2, #0                 // left = 0
    BL mergeSort
    MOV A1, R4
    POP {R4, LR}
    BX LR

array .req R4
left  .req R5
right .req R6
mid   .req R7
i     .req R8
j     .req R9
k     .req R10
value .req R12

// mergeSort(array=A1, left=A2, right=A3), inclusive indices.
mergeSort:
    CMP A2, A3
    BXGE LR
    PUSH {R4-R10, LR}
    MOV array, A1
    MOV left, A2
    MOV right, A3

    SUB mid, right, left
    LSR mid, mid, #1
    ADD mid, left, mid          // left + (right - left) / 2

    MOV A3, mid
    BL mergeSort               // mergeSort(array, left, mid)

    MOV A1, array
    ADD A2, mid, #1
    MOV A3, right
    BL mergeSort               // mergeSort(array, mid + 1, right)

    MOV i, left
    ADD j, mid, #1
mergeLoop:
    CMP i, mid
    BGT mergeDone
    CMP j, right
    BGT mergeDone

    LDRSB R3, [array, i]
    LDRSB value, [array, j]
    CMP R3, value              // compare signed bytes
    BLE advanceLeft            // a[i] <= a[j]: already in order

    MOV k, j
shiftLoop:
    CMP k, i
    BLE insertValue
    ADD R0, array, k
    LDRB R3, [R0, #-1]         // a[k - 1]
    STRB R3, [R0]              // a[k] = a[k - 1]
    SUB k, k, #1
    B shiftLoop
insertValue:
    STRB value, [array, i]
    ADD mid, mid, #1           // left half has grown
    ADD j, j, #1
advanceLeft:
    ADD i, i, #1
    B mergeLoop

mergeDone:
    POP {R4-R10, LR}
    BX LR

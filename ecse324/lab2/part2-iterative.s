.global _start
N: .word 10 // input parameter n
P: .space 4 // Pell number result
_start:
ldr A1, N // get the input parameter
bl pell // go!
str A1, P // save the result
stop:
b stop
// Pell number calculation
// pre-- A1: Pell number index i to calculate, n >= 0
// post- A1: Pell number P = pell(n)
pell:
// your code starts here
n .req A1
n_1 .req A2
n_2 .req A3

CMP n, #2
BXLT LR

SUB n, n, #1
MOV n_1, #1
MOV n_2, #0

loop:
CMP n, #0
BEQ loopEnd

ADD A4, n_2, n_1, LSL #1
MOV n_2, n_1
MOV n_1, A4

SUB n, #1
B loop
loopEnd:
MOV A1, n_1
BX LR

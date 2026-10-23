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
n .req A3
CMP A1, #2
BXLT LR // if A1 < 2 -> return A1
PUSH {A2, n, LR}
MOV n, A1 // n <- A1
SUB n, n, #1 // n <- n-1

MOV A1, n // A1 <- n
BL pell // n <- pell(n-1)

LSL A2, A1, #1 // A2 <- 2*pell(n-1)


SUB n, n, #1 // n <- n-2

MOV A1, n // A1 <- n
BL pell // n <- pell(n-2)

ADD A1, A2, A1 // n <- 2*pell(n-1) + pell(n-2)
POP {A2, n, LR}
BX LR

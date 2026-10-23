.global _start
N: .word 12
Numbers: .short 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13
Primes: .space 24
_start:
// your code starts here
NLength .req R9
num .req R0
numStart .req R6
numPointer .req R4
pri .req R1
// priStart .req R7
// priPointer .req R5
prime .req R2
done .req R3

LDR numStart, =Numbers
LDR pri, =Primes
LDR NLength, N

// MOV num, #0
// pri is an output pointer, not an index; keep its Primes address.
MOV prime, #1 // Last prime
MOV done, #0 // False

outerLoop:
CMP done, #1
BEQ outerLoopEnd

outerLoopIter:
SUB num, prime, #1

innerLoop1:
CMP num, NLength
BGE innerLoop1End
LSL numPointer, num, #1 // 2 bytes per number.
ADD numPointer, numPointer, numStart
LDRH R8, [numPointer]
CMP R8, #0
BNE innerLoop1End
innerLoop1Iter:
ADD num, num, #1
B innerLoop1
innerLoop1End:

CMP num, NLength
MOVGE done, #1 // if num >= n
BGE innerLoop2End // if num  n => endif
// Now we in the else {}
LDRH prime, [numPointer]
STRH prime, [pri], #2

innerLoop2:
CMP num, NLength // while (num < n)
BGE innerLoop2End
innerLoop2Iter:
MOV R5, #0
LSL numPointer, num, #1 // 2 bytes per number.
ADD numPointer, numPointer, numStart
STRH R5, [numPointer]
ADD num, num, prime
B innerLoop2
innerLoop2End:
B outerLoop
outerLoopEnd:

end: B end

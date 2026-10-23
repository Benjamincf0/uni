.global _start
// int a[4] = {1, 2, 3, 4};
matrixA: .word 1, 2, 3, 4
lengthA: .word 4
// int b[8] = {2, 3, 5, 7, 11, 13, 17, 19};
matrixB: .word 2, 3, 5, 7, 11, 13, 17, 19
lengthB: .word 8
// we’ll save our results here
results: .space 8
// Summation
// Sum the integers in the given array
// pre-- A1: address of array
// pre-- A2: length of array
// post- A1: sum of elements
sum: // Params: A1=R0=array addr and return val ; A2=R1=len(arr) ;
// **you** push any registers used below onto the stack
PUSH {R2, R3, R4} // A3=R2=temp answer ; A4=R3=index ; R4=scratch register
// int answer = 0;
// for (int index=0; index<length; index++)
// **you** answer=0
MOV A3, #0 // temp answer
// **you** index=0
MOV A4, #0 // index
sumIter:
// **you** index<length?
CMP A4, A2
// **you** if not, branch to sumDone
BGE sumDone
// answer += array[index];
// **you** read the element at index in the array
LDR R4, [A1], #4
// **you** accumulate the answer
ADD A3, A3, R4
// **you** index++
ADD A4, A4, #1
B sumIter // repeat until done!
sumDone:
// **you** move the final answer into A1
MOV A1, A3
// **you** pop any registers pushed above
POP {R2, R3, R4}
BX LR // return!
_start:
// int a_s = sum((int *) a, 4); // 10
LDR A1, =matrixA // put the address of A in A1
LDR A2, lengthA // put the length of A in A2
BL sum // call the function
LDR V1, =results // put the address of Results in V1
STR A1, [V1] // save the answer (in A1) in result+0
// int b_s = sum((int *) b, 8); // 77
// **you** put the address of B in A1
LDR A1, =matrixB
// **you** put the length of B in A2
LDR A2, lengthB
// **you** call the function
BL sum
// **you** save the answer (in A1) in result+4
STR A1, [V1, #4]
inf:
B inf // infinite loop!

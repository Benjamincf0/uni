#include <stdio.h>

/* This program prints out the sizes of many datatypes in C.
 * The sizes of datatypes can depend on the processor architecture, operating system, and the most
 * crucially the compiler. */

int main()
{
    printf("char: %lu bytes\n", sizeof(char));
    printf("short: %lu bytes\n", sizeof(short));
    printf("int: %lu bytes\n", sizeof(int));
    printf("long: %lu bytes\n", sizeof(long));
    printf("long long: %lu bytes\n", sizeof(long long));
    printf("float: %lu bytes\n", sizeof(float));
    printf("double: %lu bytes\n", sizeof(double));
    printf("long double: %lu bytes\n", sizeof(long double));
}

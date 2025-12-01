#include <stdio.h>

int main()
{
    int n = 22;

    printf("n = %d\n", n);
    printf("n + 5 = %d\n", n+5);
    printf("n - 5 = %d\n", n-5);
    printf("n * 5 = %d\n", n*5);
    printf("n / 5 = %d\n", n/5);
    printf("n %% 5 = %d\n", n%5);

    n %= 5;
}

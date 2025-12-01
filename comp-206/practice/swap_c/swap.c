
#include <stdio.h>
#include <stdlib.h>


void swap(int *value1, int *value2) {
	int temp = *value2;
	*value2 = *value1;
	*value1 = temp;
}

int main(int argc, char *argv[])
{
	if (argc != 3) {
		puts("Wrong num args");
		exit(1);
	}
	int x = atoi(argv[1]);
	int y = atoi(argv[2]);

	swap(&x, &y);

	printf("%d %d\n", x, y); // y x
	return 0;
}

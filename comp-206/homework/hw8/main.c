#include "ringbuffer.h"

#include <stdio.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{	
	if (argc != 3) {
		fprintf(stderr, "Takes 2 inputs\n");
		exit(1);
	}

	char *end;
	int N = strtol(argv[1], &end, 10);
	if (*end != '\0' || end == argv[1]) {
		fprintf(stderr, "cant parse number\n");
		exit(1);
	}

	if (N < 0) {
		fprintf(stderr, "enter a positive numba");
		exit(1);
	}

	if (N == 0) {
		exit(0);
	}
	
	struct ringbuffer rb = rb_init(N);
	FILE *file = fopen(argv[2], "r");
	if (file == NULL) {
		fprintf(stderr, "uhh i cant read the file bruh");
		exit(3);
	}

	char line_buffer[256];

	while (fgets(line_buffer, sizeof(line_buffer), file) != NULL) {
		rb_push(&rb, line_buffer);
	}

	fclose(file);

	while(rb.size > 0) {
		rb_pop(&rb, line_buffer);
		fprintf(stdout, "%s", line_buffer);
	}
	rb_destroy(&rb);

	return 0;
}

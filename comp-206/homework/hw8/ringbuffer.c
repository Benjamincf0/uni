#include "ringbuffer.h"
#include <string.h>
#include <stdlib.h>

struct ringbuffer rb_init(int capacity)
{
	return (struct ringbuffer) {
		.lines = malloc(sizeof(struct line) * capacity),
		.capacity = capacity,
		.size = 0,
		.head = 0
	};
}

void rb_push(struct ringbuffer *rb, char const *line)
{
	strcpy(rb->lines[rb->head].buf, line);
	rb->head = (rb->head + 1) % rb->capacity;
	if (rb->size < rb->capacity) rb->size++;
}

int rb_pop(struct ringbuffer *rb, char *dst)
{
	if (rb->size == 0) return 0;
	int tail = (rb->head + rb->capacity - rb->size) % rb->capacity;
	strcpy(dst, rb->lines[tail].buf);
	rb->size--;

	return 1;
}

void rb_destroy(struct ringbuffer *rb)
{
	free(rb->lines);
	rb->lines = NULL;
}

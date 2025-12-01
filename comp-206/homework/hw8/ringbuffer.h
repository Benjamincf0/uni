/** Simple ringbuffer implementation holding lines of text. */

#define LINE_LENGTH 127

/** This struct wraps an array of chars, allowing us to pass whole buffers of
 * characters by value. */
struct line {
	char buf[LINE_LENGTH+1];
};

/** The ringbuffer itself. The array of lines will be malloc'd. */
struct ringbuffer {
	struct line *lines;
	int size; // 0 <= size <= capacity
	int capacity;
	int head; // index of where the next write will occur
};

/** Initializes an empty ringbuffer with the given capacity. */
struct ringbuffer rb_init(int capacity);

/** Copies the string into the slot at the end of the ringbuffer.
 * If the ringbuffer is full, the oldest item in it is overwritten. */
void rb_push(struct ringbuffer *rb, char const *line);

/** Removes an item from the start of the ringbuffer, copying its value into
 * the given buffer. The buffer must be large enough to store the data.
 * Returns 0 if the ringbuffer was empty. Returns nonzero otherwise. */
int rb_pop(struct ringbuffer *rb, char *dst);

/** Releases the memory associated to a ringbuffer. */
void rb_destroy(struct ringbuffer *rb);

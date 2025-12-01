#include "stringbuilder.h"
#include <stdlib.h>

struct string_builder sb_init(int capacity) {
	struct string_builder sb;
	sb.capacity = capacity;
	sb.size = 0;
	sb.buf = (char *)malloc(sizeof(char) * capacity);
	return sb;
}

void copy_string(char *dest, char *src, int n) {
	for (int i = 0; i < n; i++) {
		dest[i] = src[i];
	}
}

void sb_appendn(struct string_builder *sb, char const *buf, int len) {
	for (int i = 0; i < len; i++) {
		if (buf[i] == '\0') {
			break;
		}

		if (sb->size >= sb->capacity) {
			int new_capacity = 2 * sb->capacity;

			if (new_capacity == 0) {
				new_capacity = 16;
			}

			char *new_buf = (char *)malloc(sizeof(char) * new_capacity);

			copy_string(new_buf, sb->buf, sb->size);

			free(sb->buf);

			sb->buf = new_buf;
			sb->capacity = new_capacity;
		}

		sb->buf[sb->size] = buf[i];
		sb->size++;
	}
}

void sb_append(struct string_builder *sb, char const *buf) {
	for (int i = 0; buf[i] != '\0'; i++) {

		if (sb->size >= sb->capacity) {
			int new_capacity = 2 * sb->capacity;

			if (new_capacity == 0) {
				new_capacity = 16;
			}

			char *new_buf = (char *)malloc(sizeof(char) * new_capacity);

			copy_string(new_buf, sb->buf, sb->size);

			free(sb->buf);

			sb->buf = new_buf;
			sb->capacity = new_capacity;
		}

		sb->buf[sb->size] = buf[i];
		sb->size++;
	}
}

void sb_copy_to(struct string_builder const *sb, char *dst, int len) {
	int i;
	for (i = 0; i < len-1 && i < sb->size; i++) {
		dst[i] = sb->buf[i];
	}
	dst[i] = '\0';
}

char * sb_build(struct string_builder const *sb) {
	int len = (sb->size + 1);
	char *ret = (char *)malloc(sizeof(char) * len);
	sb_copy_to(sb, ret, len);
	return ret;
}


void sb_destroy(struct string_builder *sb) {
	free(sb->buf);
}

#include <stdio.h>
#include "stringbuilder.h"

int main() {
	printf("Initializing string builder 8");
	struct string_builder sb = sb_init(8);

	printf("Next, ill append 'ppap pppoopy'");
	sb_append(&sb, "ppap pppoopy");

	printf("appending 'hello world'");
	sb_append(&sb, "hello world");

	char *final_str = sb_build(&sb);
	printf("Final String: %s\n", final_str);
	printf("Final size %d , Final capacity %d\n", sb.size, sb.capacity);
	return 0;
}

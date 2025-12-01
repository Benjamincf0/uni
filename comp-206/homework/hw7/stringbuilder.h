struct string_builder {
	int capacity;
	int size;
	char *buf;
};

/** Initializes the stringbuilder, allocating an underlying array of the given
 * `capacity`. The initial size of the stringbuilder is zero. */
struct string_builder sb_init(int capacity);

/** Appends characters from `buf` to the end of the stringbuilder, until either
 * a NUL byte is reached or `len` many characters have been copied.
 * This function takes care of growing the underlying array if it runs out of
 * space during copying. This function allows copying from non-NUL-terminated
 * strings.*/
void sb_appendn(struct string_builder *sb, char const *buf, int len);

/** Appends characters from `buf` to the end of the stringbuilder, until a
 * NUL byte is reached. This function grows the underlying array when necessary.
 * Only NUL-terminated strings may be used with this function. */
void sb_append(struct string_builder *sb, char const *buf);

/** Copies the contents of the stringbuilder, plus a NUL byte, into the buffer
 * `dst`. We understand `len` as the size of the buffer `dst`, so at most
 * `len-1` characters are copied. */
void sb_copy_to(struct string_builder const *sb, char *dst, int len);

// THINK: why does the `buf` parameter of `sb_append` have `const` but the
// `buf` parameter of `sb_copy_to` does not?

/** Obtains a copy of the current contents of the stringbuilder as a malloc'd,
 * null-terminated string. It becomes the responsibility of the caller to call
 * `free` on that string. */
char * sb_build(struct string_builder const *sb);
// You should implement sb_build using sb_copy_to

/** Releases the memory held by the stringbuilder. */
void sb_destroy(struct string_builder *sb);

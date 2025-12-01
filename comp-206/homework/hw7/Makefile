# This specifies the C compiler to use. gcc is standard.
CC = gcc
# These are compiler flags. -Wall enables all warnings, -g adds debug info.
CFLAGS = -Wall -g

# The default target: this is what runs if you just type "make".
# It says that "sb_test" depends on two object files: main.o and stringbuilder.o.
all: sb_test

# Rule to link the object files into the final executable named "sb_test".
sb_test: main.o stringbuilder.o
	$(CC) $(CFLAGS) -o sb_test main.o stringbuilder.o

# Rule to compile main.c into main.o.
# It depends on main.c and stringbuilder.h. If either changes, this rule will run.
main.o: main.c stringbuilder.h
	$(CC) $(CFLAGS) -c main.c

# Rule to compile stringbuilder.c into stringbuilder.o.
# It depends on stringbuilder.c and stringbuilder.h.
stringbuilder.o: stringbuilder.c stringbuilder.h
	$(CC) $(CFLAGS) -c stringbuilder.c

# A "phony" target is one that is not a file. "clean" is a common one.
# It removes all the compiled files so you can start fresh.
.PHONY: clean
clean:
	rm -f sb_test main.o stringbuilder.o

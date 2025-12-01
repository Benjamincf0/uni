# COMP 206 - Fall 2025 - Homework 8

In this assignment, you'll essentially reimplement the `tail` program. This is
similar to the exercise in hw4, which was to implement a variation on `head`.
However, reading the _last_ `n` lines is trickier.

The purpose of the exercise is to:
- practice reading from files
- handling command-line arguments
- practice using control structures in C

Your program will be called `queue` (which is 'tail' in French).
You'll use it like this: `./queue N PATH`
This prints out the _last_ `N` list of the file identified by PATH.

The high-level idea of how to implement this program is:
- Read _every_ line of the given file, keeping track of the last `N` lines
  we've seen.
- When we hit end of file, then we print out those N lines.

## Keeping track of the last N lines

The idea is to use a data structure called a
[ringbuffer](https://en.wikipedia.org/wiki/Circular_buffer).
An implementation of it is given in `ringbuffer.c` with an interface for you to
use given in `ringbuffer.h`.

This data structure supports:
- efficiently adding items to the end,
- efficiently removing items from the start

If we try to add a new item to the end when the ringbuffer is full, we get a
'wrap-around' behaviour that overwrites the oldest element, at the start.

**Read ringbuffer.h and ringbuffer.c.** You should understand how this data
structure works.

This is exactly what we need to implement tail!
- Initialize the ringbuffer to capacity `N`.
- Add every line of the input file to the ringbuffer.
- When you reach end-of-file, print out every item from the ringbuffer.

For simplicity, we assume that lines have a maximum length, defined as a
constant in `ringbuffer.h`.

## Details

- If there are not enough command-line arguments, print a message to stderr and
  exit with code 1.
- If the first command-line argument fails to be parsed as a number, print a
  message to stderr and exit with code 1.
- The second command-line argument is a path. Open that file using `fopen`. If
  this fails, print a message to stderr and exit with code 3.
- Make sure to release all resources before the program exits:
    - Use `fclose` on the `FILE*`
    - Use `rb_destroy` to release the memory of the ringbuffer.

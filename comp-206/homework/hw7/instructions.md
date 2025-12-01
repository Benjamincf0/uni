 # Fall 2025 - COMP 206 - Assignment 7

Due date: 11 November 2025, 11:59pm.

This assignment is for you to practice using dynamic memory in preparation for
midterm 2.

The files to submit in this assignment are:
- `Makefile`
- `stringbuilder.c`
- `main.c`

(You'll also submit `stringbuilder.h`, but you shouldn't need to modify it at
all.)

## Using dynamic memory

Write your code for this question a file called `stringbuilder.c`. The purpose
of this exercise is for you to implement an extremely common and useful dynamic
data structure often called a _dynamic array._ You're already familiar with
this data structure: Python's `list` is a dynamic array, Java has it as the
`ArrayList` class, C++ calls it `vector` and Rust calls it `vec`. These kinds
of arrays can have elements added to them over time, and consequently "grow".

A dynamic array consists of three things:

1. A pointer to an underlying, fixed-size array that actually holds the data.
2. An integer called the _capacity,_ that is the total size of the fixed-size
   array.
3. An integer called the _size,_ that is the count of items currently stored
   inside the underlying array.

Suppose a dynamic array will hold items of some type `T`. Then it crucially
supports the following operation:

```c
void push(T const *item);
```

This function makes a copy of the given item, which is being passed by
reference via a pointer, into the underlying array, at index `size`. That's
because `size` is the count of items currently stored in the underlying array,
so treating it as an index gives us the "next spot" in the underlying array.
The size is then incremented.

However, what if `size == capacity`? In this case, there is no more room inside
the underlying array. **The big idea** is to allocate a _new underlying array
with **double** the capacity,_ copy all the items from the current underlying
array into the new one, and finally proceed to add the new item as before.

There is a library function `realloc` which does exactly that: it allocates a
new block, copies everything from the old block to the new block, and frees the
old block. **But do not use `realloc` at first.** First implement the logic
entirely using `malloc`, `free`, and loops. You can switch to `realloc` later.

**It is absolutely critical** that the size of the underlying array increase
**by a factor, not by a constant.** Different dynamic array implementations use
different factors, but the most common is a factor of 2. Exponentially growing
the size of the underlying array is what gives this data structure its
desirable performance characteristics.

In this exercise, the dynamic array you'll implement is actually of a specific
kind, called a _string builder._ This is a dynamic array holding `char`s, into
which we gradually copy in several smaller strings, to ultimately build up a
large string.

The prototypes of the functions you're expected to code are listed in
`stringbuilder.h`, together with explanations of what they do.

- **Implement these functions in stringbuilder.c**
- **Write a file `main.c` that includes stringbuilder.h,** initializes a
  stringbuilder, appends some strings to it: try both string literals and
  strings read from standard input.

A note about null-termination: you don't need to have a null-terminator on the
string while it's inside the builder, since the builder explicitly tracks the
size of the underlying array. You only _need_ to add a NUL byte on the end in
`sb_build`, which explicitly returns a null-terminated string that's a copy of
what was stored inside the builder.

Much of this assignment involves copying strings. There is a standard library
function `strcpy` for this **but you should implement your own string copying
function** to best practice and prepare for the upcoming midterm!

## Using a Makefile

In this assignment, you'll need to write a Makefile to compile your code.

It should perform _separate compilation._  That is, it separately compiles
`main.c` into `main.o`, `stringbuilder.c` into `stringbuilder.o`, and in a
final step links both `.o` files together into an executable `sb_test`.

This is a benefit as it allows only recompiling the parts of the program that
change.

To do this, define three _targets_ in the Makefile (one for each build
artifact: two .o files and one executable) and express the correct
dependencies for each target. The default target of the Makefile should be to
build the executable `sb_test`.

Use an AI tool of your choice to learn more about Makefiles and how to write
them, but **do not use the AI to write the Makefile for you.** For example, use
prompts like "how do I define a target in a makefile", "what is the default
target that is executed in a makefile", "explain separate compilation using
makefiles for a C program."

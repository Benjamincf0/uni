# Fall 2025 - COMP 206 - Assignment 5

- Soft due date: Monday, 27 October end-of-day
- **Submission instructions.** As before, you must push your repo and receive
  full marks from the autograder.

This assignment is for you:
- to practice using arrays and handling strings and pointers in C
- to learn about parsing strings
- to explore new parts of the standard library

Learning goals assessed in this assignment:

- Write robust system interactions in C.
- Use appropriate data structures and algorithms in C.
- Organize code in C for readability.
- Write bash scripts for automating development tasks.

Files to write in this assignment:

- `build` -- a build script to compile your program
- `minicalc.c` -- C source code of the mini calculator you'll develop

## Writing a build script

Write a bash script named `build` to do the following:

- Compile `minicalc.c` into an executable named `minicalc`.
- Use the `-Wall` flag to enable useful compiler warnings about your C program.
- Use the `-lm` flag to include the math library, which will be necessary for one of the operations
  of the mini calculator.
- Only when compilation succeeds: run `minicalc` many times across a variety of inputs to test it.
  Add to this test suite in tandem with the development of minicalc itself.
- When compilation fails, the build script must exit with code 1.

## Writing the mini calculator

Write your code for this program in a file named `minicalc.c`.

The program's input is its **command-line arguments** -- don't use scanf as that reads from
**stdin**. The command-line arguments will specify an operation to perform followed by some
operands.

Usage: `./minicalc OP ARGS...`

The exact count and nature of the ARGS will vary depending on the operation. The required
operations are the following:

1. Square root: `./minicalc sqrt A`
   - Input: `A` represents a floating-point number.
   - Output: the square root of `A`, formatted to two decimal places.
   - Use the `strtod` function to parse the string that minicalc receives into a `double`. See `man
     strtod` for a description of how to use the function.
   - Use the `sqrt` function of the standard library to compute the square root. See `man sqrt` to
     see what you will need to `#include`.

2. Greatest common divisor: `./minicalc gcd N...`
   - Input: one or more integers.
   - Output: the greatest common divisor of all those integers.
   - This isn't a course on algorithms, so use your favourite search engine to
     look up the Euclidean Algorithm for calculating the GCD of two integers.
   - Define a function `long gcd(long n1, long n2)` that computes the GCD of
     the two integers.
   - Use the `strtol` function to parse the strings representing integers into `long`s.
     See `man strtol`.
   - You must effect a **separation of concerns** in your program: the loop that parses the
     strings into integers must be separate from the loop that computes the GCD of all the
     integers.
     **Define the function** `int gcd_many(int len, long arr[])` to compute the GCD of all `long`s
     given as input as an array of size `len`. To implement `gcd_many`, make successive calls to 
     `gcd`, using the fact that the gcd of three integers n1, n2, n3 can be
     computed as `gcd(gcd(n1, n2), n3)`.

3. Anagram: `./minicalc anagram S1 S2`
   - Input: two strings.
   - Output: the string `true` or `false` depending on whether the input strings are anagrams.
   - Two strings are anagrams if the letters of one can be rearranged to form the other. For
     example, `binary` and `brainy` are anagrams.
   - To check whether two strings are anagrams, it suffices to see if they have the same *letter
     frequencies.* Use arrays to store the letter frequencies and then determine whether both
     arrays are equal using a loop.
   - Your program must validate that the strings S1 and S2 consist **only of
     lowercase letters** a-z.

### Input validation

A big part of this assignment is parsing and validating input.

- Any error messages your program produces must be sent to stderr.
  Use the `fprintf` function to accomplish this.
- Calling `./minicalc` with no arguments should print an error message and exit with code 1.
- Calling `./minicalc OP` with an unknown operation must print an error message and exit with code
  1.
- For each operation, you must check that the correct number of operands are given. If the count of
  given operands is incorrect, print an error message and exit with code 2.
    - `sqrt` requires exactly 1 operands
    - `gcd` requires at least 1 operand.
    - `anagram` requires exactly 2 operands.
- Each operation requires some parsing or validation on its operands. If the parsing or validation
  fails, print an error message and exit with code 3.
    - For `sqrt` the given operand must represent **in its entirety** a floating-point number.
      You will need to figure out how to use the `endptr` parameter of `strtod` to determine
      whether the *whole* string represents a floating-point number.
      Moreover, for `sqrt` the given number must be nonnegative.
    - For `gcd` the operands must represent integers in their entirety.
      Again, the `endptr` parameter of `strtol` is what you need to use to check this.
      Moreover, for `gcd`, all the input integers must be strictly greater than zero.
    - For `anagram`, the operands must consist only of lowercase English letters. Check this using
      a loop. Use the `islower` standard library function; see `man islower`.
- `minicalc` should exit with code 0 when it succeeds. (This would normally go without saying.)

## Getting started

The provided file `minicalc.c` contains some starter code, listing out the
functions you're expected to implement according to the description above.

Structure your program as follows:
- Use `main` to determine what operation is requested, then delegate all
  remaining work to the appropriate _handler function,_ whose name begins with
  `handle_`
- Each handler receives all the command-line arguments as input and should
  return an exit code back to `main`. `main` can return whatever exit code the
  handler returns. The handlers can then perform the appropriate validation and
  parsing logic according to the operation type.
- Each handler will call out to the appropriate helpers above.

This structure allows you to intelligently break up the program into logical
units, represented as functions, without deeply nesting the control flow within
`main`.

# Want extra practice?

Make a variation on minicalc that reads operations from stdin instead of from
the command-line arguments. 

- Use the code from the previous assignment to read lines from stdin.
- Use the standard library function `strtok` to break up a string by spaces, to
  populate an array of strings with a fixed size, e.g. 32.
- Now you have a count of 'words' and an array of words, which is how minicalc
  in this assignment was designed to operate.
- Adapt the code from hw5 minicalc to not exit when there's an error, but to
  just print it out so the user can try again interactively.

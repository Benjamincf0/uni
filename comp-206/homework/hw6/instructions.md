# Fall 2025 - COMP 206 - Assignment 5

- Soft due date: Monday, 3 November end-of-day
- **Submission instructions.** As before, you must push your repo and receive
  full marks from the autograder.

This assignment is for you:
- to continue practicing using arrays and handling strings and pointers in C
- to explore new parts of the standard library

# A REPL for minicalc

A common interface for programming languages is the read-eval-print-loop
(REPL). This is the interface you get when you run `python` by itself on the
command-line, for example. In a sense, it is also the interactive interface of
`bash`.

In a REPL:
- You are prompted for a line of input. (Read.)
- The interpreter parses and evaluates the input. (Eval.)
- It then prints the result. (Print.)
- And finally all these steps repeat forever. (Loop.)

**In this assignment, you'll develop a REPL-like interface for Minicalc.**

In short, you'll reuse your entire solution to Minicalc from hw5 and adjust it
so it takes input from stdin, as you briefly saw in hw4.

Here is an example session.
Lines beginning with `$` are the bash prompt. Lines beginning with `>` are the
minicalc prompt.

```bash
$ ./minicalc
> anagram brainy binary
true
> gcd 2 5 15
1
> gcd 5 10 a
error: invalid input to gcd
> sqrt -1
error: invalid input to sqrt
> sqrt 2 3
error: wrong count of inputs to sqrt
> anagram bruh
error: wrong count of inputs to anagram
> huh
error: unknown command huh
>
> exit
$
```

To implement this:
- Use the`fgets` library function from stdio.h to read a line from standard
  input, as seen in hw4. You may assume that lines will never exceed 127
  characters.
- Use the `strtok` (pronounced 'stir-toke' for "string token"; NOT
  'stir-to-K') to split the string read from stdin by spaces.
- Place the resulting separated strings into an array of strings and count how
  many strings you end up parsing.
- Now you have a setup similar to the way `main` is called by the OS in hw5:
  with a count of arguments `argc` and an array of argument values `argv`.

You'll need to adjust your minicalc code from hw5 so that rather than exiting
(with various status codes) in case of errors, it simply prints error messages
(to stderr as usual) and returns to the REPL so the user can try again.

**The error messages must be exactly as above.**
- They have the prefix `error:`
- They indicate the nature of the error: `invalid input` or `wrong count of
  inputs`
- They indicate the name of the operation: `to sqrt` or `to anagram`, etc.

As a special case, if the user inputs an empty string, no error should be
printed, but the prompt should just be printed again. This is shown at the end
of the sample session.

You'll also add an `exit` command that takes no arguments, and cause the
interactive minicalc to exit code 0.

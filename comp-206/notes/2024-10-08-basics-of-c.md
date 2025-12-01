# Basics of C

C is a strongly & statically-typed, procedural, imperative, compiled programming language.

- **strongly** & **statically**-typed: the compiler typechecks your program to ensure that e.g.
  arrays aren't passed as arguments to functions that expect integers.
- **procedural**: the basic unit of abstraction in C is to define _procedures_ aka functions.
- **imperative**: the program is expressed as a sequence of instructions to perform.
- **compiled**: the source code that you write is _not_ directly executed. Instead, it must be
  compiled into machine code, by running your source code through a _compiler._

Although it is possible to do object-oriented or even functional programming in C, there is no
built-in support from the language for this paradigms. Later in the course, we will discuss how to
do OOP in C at a basic level.

**Java** follows a similar typing discipline as C (it is also strongly and statically typed). It is
also imperative and is (somewhat) compiled.

**Python** is also imperative and strongly-typed, but its type system is _dynamic;_ there is no
compiler that typechecks (nor compiles) the program for you ahead of time. (Although this is
gradually changing with time -- now Python support type annotations and programs exist to check
these statically, i.e. without running the program.)

## Compiling C programs

Suppose we write this C program in a file called `helloworld.c`

```c
#include <stdio.h>

int main()
{
    printf("Hello world!\n");
    return 0;
}
```

We compile this program using the gcc compiler like this:

```bash
$ gcc -o helloworld helloworld.c
```

The general syntax is `gcc OPTIONS C_FILES...`

- The `-o` option sets the name of the generated executable file. When this is not specified, the
  default name is `a.out`.

We can then run the generated program: `./helloworld` prints out `Hello world!`

**EXERCISE:** write a bash script called `build_and_run`, to be run as `./build_and_run NAME` that
compiles the file `NAME.c` into an executable `NAME` and runs it **only if** compilation
succeeeded. (Of course, gcc exits with code 0 when compilation succeeds and nonzero otherwise.)

## General layout of C programs

A C source code file is generally layed out like this from top to bottom:

- `#include` directives
- global variables and constants
- function definitions
- definition of the `main` function

This layout is natural because C enforces that variables and functions be declared before they be
used. `main` is the so-called _entrypoint_ of the program, where execution starts, so it is defined
last.

(The justification for this enforcement of definition order comes sadly from hardware limitations
in the 1960s/1970s.)

## C compilation process

1. C has a _preprocessor_ which runs first to perform the effects of all `#` directives.
    - `#include` is the most common directive you will use, and it how C does "modules".
    - C does not have a true module system. Instead, `#include` textually pastes the contents of
      another file into the one in which the directive appears.
    - C also does not separate _namespaces._ It has one global namespace, so libraries must be
      careful not to define names which might have been defined in other libraries!
    - The output of the preprocessor is `.i` files (which we never usually see) in which no more
      preprocessor directives remain.
2. The compiler typechecks the program.
    - This is where you will get most of your compilation errors.
3. The compiler generates an _object file_ (`.o` file) for each `.c` file given as input.
    - Each `.c` file is compiled separately from the others.
    - In other words, C allows for _separate compilation_ which is desirable for a few reasons:
        1. If you edit `database.c` for example, then only that file needs to be recompiled, rather
           than the entire program.
        2. Compilation can be parallelized: each CPU core can be compiling a different `.c` file.
4. The _linker_ assembles all the `.o` files together into an executable binary file.
    - You can get errors at this stage too, since it's only at link-time that references to
      functions defined in other `.c` files ultimately get resolved. (Think back to separate
      compilation above.)

Compiling a library in C follows the same process, only that libraries will lack a `main` function
and will not be executable. On Linux, the extension for libraries are `.a` (for 'archive') and
`.so` (for 'shared object') depending on the kind of library.

## Basic C syntax

### Comments

A comment that extends to the end of the current line (called a line-comment) is written like:

```c
... // this is a line comment
```

A comment that goes up to a delimiter (called a block-comment) is written like:

```c
/* this is a block
comment
and can span
multiple lines
*/
```

### Statements and expressions

C's basic structure, within a function, is very similar to Java's and Python's, since they're all
_imperative_ languages.

* The program is expressed as a sequence of instructions called _statements._
    - A statement _does_ something. It has an _effect._
    - `int age;` is a statement that reserves some memory to store the value of the `age` variable.
    - `if (...) { ... } else { ... }` is a _compound statement_ made up of others.
    - Statements are terminated by a semicolon. Compound statements don't require a semicolon.
* Computations, on the other hand, are expressed as... _expressions._
    - An expression _computes_ something.
    - The computational process of turning an expression into a _value_ (i.e. an irreducible
      expression) is called _evaluation._
    - arithmetic such as `2 + 2` is an expression, calling a function is an expression, accessing a
      variable is an expression such as `x + 12` is an expression.

Also, any expression can be used as a statement.
That's why we can write `printf("hello world!\n");`
The function call itself as an expression, and we're using the evaluation of that expression as a
statement in its own right.
We don't call `printf` because it returns/computes something interesting, but because evaluating
that function call produces a useful _side-effect,_ namely printing something to the terminal.

### Variables and data types

Variables are declared like this: `TYPE NAME;`

They can also be initialized at the same time as they are declared: `TYPE NAME = EXPR;`

Here are a few basic types in C. Since C is a low-level language, we'll talk about how "wide" each
datatype is _bits._ More bits means a wider range of representable values.

* `char` (integer) is 8 bits wide (one byte) and owes its name to the ASCII encoding that was
  prevalent in the 1970s, enabling characters to be stored in just one byte.
    - Nowadays, handling multilingual text in applications uses a standard called Unicode, where
      characters (technically called "code points" in Unicode) are often represented using a
      variable-length scheme called UTF-8.
* `short` (integer) is 16 bits wide (2 bytes)
    - This type can also be written as `short int`
* `int` (integer) is 32 bits wide (4 bytes)
    - This is the typical integer type.
* `long` (integer) is 64 bits wide (8 bytes)
    - This type can also be written as `long int`
* `float` (real) is 32 bits wide and stores floating-point numbers.
* `double` (real) is 64 bits wide and stores floating-point numbers with more precision (double the
  precision) and in a larger range.

What's missing?

* `bool` -- C did not have a boolean type until version C99.
    - Integers are treated as truth-values with zero meaning false and nonzero meaning true.
    - You can do `#include <stdbool.h>` to use the type `bool` which is really just a numeric type
      allowing only the values 0 and 1.
* `string` -- C does not have a built-in string type!
    - This bears repeating: handling text is actually Very Difficult.
    - In this course, we'll assume all text is simply ASCII.
    - strings in C are represented as arrays of chars
    - We'll see lots of examples of working with strings when we talk about arrays and pointers.

#### Signedness

Each integer type can be specified as `signed` or `unsigned` to indicate whether it is
capable or incapable of storing negative numbers or not, respectively.

The floating-point types `float` and `double` are always signed, according to the specification
that they implement, called IEEE-754.
(You learn all about the gory details of IEEE-754 in COMP 273 and the intricacies of computing
accurately with these kinds of numbers in courses like COMP 350 or MATH 317. That is a very tricky
discipline called "numerical analysis".)

Examples:

```
unsigned int age = 22; // used unsigned because ages can't be negative!
char best_letter = 'j'; // single characters are written in single quotes
// the compiler replaces 'j' with the ASCII code for that letter
// in reality, `char` just holds numbers!
char also_best_letter = 106; // that happens to be the ascii code for j
```

The default signedness for all types (except `char`) is `signed`, so it's rare to see the `signed`
qualifier.
The default signedness of `char` is compiler-dependent. If you need a particular signedness on a
1-byte value, you need to specify.

#### Constants

Similar to signedness, each variable definition can be given an optional qualifier making the
definition _constant._ Constants cannot be modified after they are initialized.

Example:

```
float const GRAVITY = 9.8;
GRAVITY = 5;
// ^ error: cannot assign to variable 'GRAVITY' with const-qualified type 'const float'
```

The `const` modifier can appear either on the left or on the right of the type it modifies.

For reasons that will become clearer later on when we discuss pointers, it's preferable to place
the modifier on the _right._

**Best practice:** declare everything as const unless otherwise necessary.

### Operators

C has all the operators you'd expect:

- Arithmetic: `+`, `*`, `-`, `/`, `%`
- Logical: `&&`, `||`, `!`

All of these are used to form expressions.

**EXERCISE:** Write a program involving _all_ of these operators to confirm that you understand
what they do.

C also has "assignment" versions of all of the arithmetic operators above, e.g.

```c
n += 5;
// is equivalent to
n = n + 5;
```

C also has both pre- and post-increment and -decrement operators. These are **very tricky:**

```c
n = 5;
k = n++ + 6;
```

`n++` uses the _post-increment_ operator, so the value of `n` is _incremented_ **after** the
expression that contains the `n++` is evaluated.

Therefore, after executing the above two statements, `n` has the value `11` and `n` has the value
`6`.

In contrast,

```c
n = 5;
k = ++n + 6;
```

uses the _pre-increment_ operator, so the valu eof `n` is _incremented_ **before** the expression
that contains the `++n` is evaluated.

The binary logical operators `&&` and `||` are actually _control-flow_ mechanisms since they
exhibit the so-called "short-circuiting" behaviour:

```c
#include <stdlib.h>

int tricky()
{
    exit(1); // 'crash' the program
    return 0;
}

int main()
{
    return 0 && tricky();
}
```

What does this program exit with code 0 or with code 1?

- We want to evaluate `0 && tricky()`
- `0` is considered false. The AND `&&` operator evaluates to false if either side is false.
- Since the outcome of the AND (false) can be determined without evaluating the right-hand side,
  **the right-hand side is not evaluated.**
- Therefore, the program exits with code 0.

**EXERCISE:** construct a similar example to exhibit the short-circuiting behaviour of the `||` OR
operator.

Recall that C treats integers as booleans according to whether they're zero, so what does the `!`
(not) operator do?

- If `E` is an expression that evaluates to zero, then `! E` evaluates to `1`.
- If `E` is an expression that evaluates to a nonzero value, then `! E` evaluates to `0`.

**Trick:** `!! E` evaluates to `0` if `E` evaluates to zero; else, if `E` evaluates to nonzero,
then `!! E` evaluates to `1`.

### Control flow structures

The **if-statement** is just as in Java.

```c
if (expression) {
    // true branch
}
else {
    // false branch
}
```

Note that if the branch only has one statement, then you can omit the curly braces.
This is true even if the one-and-only statement in the branch is itself a compound statement.
Therefore, the `else if` syntax is a consequence of the above rule:

```c
if (expr 1) {
    // ...
}
else if (expr 2) {
    // ...
}
else {
    // ...
}
```

is equivalent to

```c
if (expr 1) {
    // ...
}
else {
    if (expr 2) {
        // ...
    }
    else {
        // ...
    }
}
```

The **while-loop** is the same as in Java, too.

```c
while (expr) {
    // ...
}
```

As a dual to the `while`-loop, which checks its condition at the beginning of the loop body, there
is the `do-while` loop, which checks its condition at the end of the loop body.

```c
do {
    // ...
} while (cond); // <- note the semicolon
```

Therfore, a `do-while` loop is guaranteed to be entered at least once.

The **for-loop** is not _completely_ the same as in Java.

```c
for (init; cond; step) {
    // ...
}
```

The _statement_ `init` is executed before the loop.
Then, the expression `cond` is evaluated as a condition to continue running the loop.
When control reaches the end of the loop's body, the `step` statement is executed and the `cond`
expression is evaluated again to determine whether to re-enter the loop or jump out of it.

I say that the C for-loop is not completely the same as in Java because Java supports a `foreach`
syntax `for (var x : collection)` to iterate over the elements of a collection.

**C does not have a foreach loop.**

Again similar to Java, C has a `switch` statement.

```c
switch(expr) {
    case N1:
        // ...
        break;
    case N2:
        // ...
        break;
    // ...
    default:
        // ...
}
```

When comparing the value of `expr` against a predetermined set of constant integers, the `switch`
statement can be more ergonomic. The `break` statement makes execution jump to the end of the
switch statement. Without it, execution will actually "fall through" into the next case.

### Defining functions

The general syntax is:

```c
RETURN_TYPE NAME(TYPE1 ARG1, TYPE2 ARG2, ...)
{
    // ...
}
```

which is again pretty much the same as Java's syntax.

Example:

```c
void greet(char *name, int age) // for now, pretend that `char *` means `string`
{
    printf("Hi %s, you are %d years old.");
}

int main()
{
    greet("Bob", 47);
    return 0;
}
```

What if we want to call a function _before_ we define it?
First of all, are there any situations where this is actually necessary?
Yes! Consider a pair of _mutually-recursive_ functions. Two functions call _each other_
recursively. This requires that they both know about each other, but we can't decide which one to
define first!

Admittedly, sets of mutually recursive functions are rare, but here's a (very silly) example that
decides if a number is even. It's based on the following mutually inductive definition of even and
odd numbers:

- `0` is an even number.
- `1` is an odd number.
- `n+1` is even when `n` is odd.
- `n+1` is odd when `n` is even.

```c
int is_even(unsigned int n)
{
    if (n == 0) return 1;
    else return is_odd(n-1);
}

int is_odd(unsigned int n)
{
    if (n == 0) return 0;
    else if (n == 1) return 1;
    else return is_even(n-1);
}
```

This program won't compile because `is_even` tries to call `is_odd` before it is declared.

The solution adopted in C, due to hardware/compiler limitations in the 1970s, is called _forwards
declaration._ The idea is to add a _declaration_ for `is_odd` _before_ the _definition_ of
`is_even`. The _declaration_ merely establishes a promise to _define_ the function later.

- _declaration_ says "hey I promise that this function will be _defined_ later"
- _definition_ says "here is the code for the function"

Solution:

```c
int is_odd(unsigned int n); // forwards declaration of is_odd promises to define it later.

int is_even(unsigned int n)
{
    if (n == 0) return 1;
    else return is_odd(n-1); // now the compiler can typecheck this call
}

int is_odd(unsigned int n) // and we finally make good on our promise to define the function
{
    if (n == 0) return 0;
    else if (n == 1) return 1;
    else return is_even(n-1);
}
```

**EXERCISE:** take that program, _delete_ the _definition_ of `is_odd`, but keep the declaration.
Try to compile the program. What kind of error message do you get?

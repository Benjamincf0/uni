# Bash scripting

Much of this comes from the [Advanced Bash Scripting Guide](https://tldp.org/LDP/abs/html/index.html).


Scripts (in general, not just bash scripts) in Unix-like operating systems such as Linux begin with
a special line, called the **shebang line**, short for "hash-bang", which are the first two
characters on that line:

```
#!/bin/bash
```

https://en.wikipedia.org/wiki/Shebang_(Unix)
https://en.wikipedia.org/wiki/File_format#Magic_number

This line is used by the OS to determine what program to use to run the script. For example, if you
wanted to make a Python script that you could run directory, e.g. `./my-awesome-script.py`, you
would use a shebang line like:

```
#!/usr/bin/python
```

(For historical reasons, some programs are in /bin and others are in /usr/bin. Some Linux
distributions merge the two, so /bin and /usr/bin actually go to the same place.)

**Comments** are written with a hash and continue to the end of the lin, like in Python.
This means that technically, the shebang is a comment. It just gets special treatment.

## Naming scripts

A somewhat common file extension for scripts is `.sh` or `.bash`, but this is unnecessary on Linux
-- Recall that on Linux, file extensions don't matter much.

Commonly, scripts simply have no file extension.

## Running a script

There are two ways.

1. Make the script executable with `chmod +x SCRIPT` then simply `./SCRIPT`.
2. Run the script via bash: `bash SCRIPT`
   This approach is more similar to what you might be used to in Python.

## Exit status

Any program, when it exits, gives a _status code_ (also called _exit status_) back to the OS to
signal whether the command succeeded or failed. Code zero `0` means success, and any other code
means failure. Commonly, code `1` is used for failure.

You can end a script by using the `exit` command, optionally specifying a status code, e.g. `exit
1`. The default code is `0`.

# Statements

In the bash script, you write commands just as if you were running them yourself on the
command-line:

```
echo 'Hello world!'
```

Any command you've seen before -- cp, mv, rm, rmdir, git, ls, cat, grep, cut, sort, uniq, etc. --
can be used in a script.

If that command produces output (on stdout/stderr), then since the process spawned to run the
command inherits the stdout/stderr file descriptors from the bash shell that's running the script,
we see the command's output on our terminal.

In case it's undesirable to see the output of a command run inside a script, there are a few ways
to suppress a command's output:

- Some commands come with flags to suppress their output, e.g. `grep -q` (the q stands for
  "quiet").
- Redirect the output into `/dev/null` -- this is a special file that doesn't actually store any
  data and just ignores anything we try to write to it, e.g. `ls > /dev/null 2>&1`.

## Defining variables

```bash
name="jake"
height=182
```

Why no spaces? Imagine I had this line instead.

```
name = "jake"
```

Bash would understand this as _running a program_ named "name" and giving it two arguments:
1. the string "="
2. the string "jake"

## Using variables

To get the value of a variable, put a `$` in front of its name:

```bash
echo "Hello, $name"
```

Inside double-quotes, $ has its special meaning (and it does a whole lot more that just read
variables, as we'll see below.) To suppress the special meaning of the $, you can put a backslash
in front **or use single-quotes** instead of double-quotes.

### Be careful of spaces!

Consider this example:

```bash
name="Jacob Errington"
mkdir $name
```

The second line is expanded by bash into: `mkdir Jacob Errington`, which invokes `mkdir` with _two_
arguments, thus creating two directories. That's unusual since `mkdir $name` looks like it will
only invoke `mkdir` with _one_ argument.

To resolve this, put double-quotes around the variable access:

```bash
mkdir "$name"
```

This expands into `mkdir "Jacob Errington"`, thus invoking `mkdir` with just one argument.

**Best practice:** always double-quote variable accesses when passing the value of a variable as an
argument to a command.

## Capturing the output of commands: command substitution

When we run a command in a script, its output ends up by default on our terminal. Often, however,
we want our script to take the output of a command and transform it in some way, or use it as (part
of) an argument to another command.

Crucially what we often want is to store the output of a command into a variable.

**Command substitution** is a syntax (involving the $) that enables a script to capture the output
of a command as a string.

```
$(COMMAND)
```

Bash evaluates this expression in the following way:
- Run `COMMAND` and store its output in memory (rather than showing it on the terminal)
- Replace the whole expression with the output of the command.

So suppose I have a file in my home directory called `todo.txt` and I want to read that file in a
script:

```bash
todo="$(cat ~/todo.txt)"
```

This stores the contents of the file `~/todo.txt` into the variable `todo` in my script.

Again, since the file could contain spaces, it's **best practice** to double-quote
command substitutions.

## Reading standard input: getting input from the user

When we run a script from the terminal, it inherits all the file descriptors of the shell that runs
the script. Therefore, the script's stdin is connected to the terminal.

Bash provides a builtin command called `read` that reads from stdin into a variable.

```bash
echo "How old are you?"
read -r age
echo "You are $age years old"
```

The command `read -r age` reads, by default, one line of standard input into the variable `age`.
The `read` command has many flags to affect its behaviour.

Since `read` is a command that is built in to bash, it does not really have its own man page. To
read the manual for `read`, see `man bash` then search (by pressing `/`) and type `^SHELL BUILTIN
COMMANDS`. You can then see all the builtin commands, their descriptions and their options. Scroll
for a bit to find `read`.

EXERCISE: Check the manual to see what exactly the `-r` flag does.

## Control flow

Control flow structures in Bash will seem a bit unusual compared to traditional programming
languages.

### If statement

```bash
if COMMAND ; then
    ...
[elif COMMAND ; then
    ...]
[else
    ...]
fi
```

The use of square brackets in the sample above indicates that those structures are optional. The
whole if-statement is termined by `fi`, which is "if" spelled backwards. A few other constructs in
bash use the keyword backwards as a terminator.

The if-statement _runs a given command_ and _if it succeeds_ (i.e. exits with code `0`), runs the
block of code in the `then`-branch.

In other words, the if-statement can be used to check if a command succeeds or not.

#### Another way to check if a command succeeds: the variable $?

Each time we run a command, either interactively in the terminal, or via a script, the shell
updates the variable $? with the exit status of the last command we ran.

In other words, the following two examples are essentially equivalent:

```bash
if COMMAND ; then
    ...
fi
```

vs

```bash
COMMAND
if [ $? = 0 ] ; then
    ...
fi
```

However, the first method is the preferred one, and is less susceptible to mistakes. The most
common mistake I see is something like:

```bash
COMMAND
echo "The exit status was $?"
if [ $? = 0 ] ; then
    ...
else
    ...
fi
```

This doesn't do what we'd expect, since the use of `echo` will update `$?` again! And since echo
exits with code 0 in this case, the if-statement will never end the else-branch.

### While loop

The while loop works the same way as the if statement:

```bash
while COMMAND ; do
    ...
done
```

While COMMAND exits with code 0, the body of the while-loop is executed.

#### Example: looping over lines from stdin

The `read` command gives exit status zero when stdin is still open and when it successfully reads a
line. Therefore we can use `read` as the COMMAND in a while loop to loop over lines of stdin.

```bash
while read -r line ; do
    echo "Here's a line: $line"
done
```

### Example: looping over lines of output from another command

By also using a pipe, we can feed the output of a command into such a while loop, enabling us to
loop over the lines of output produced by a program.

```bash
ls | while read -r file ; do
    if [ -d "$file" ] ; then
        echo "Is a directory: $file"
    else
        echo "Is not a directory: $file"
    fi
done
```

## Comparing strings and integers, etc.

Often, we want a condition in a while loop or an if statement to check if two variables are equal,
or if a variable equals a particular constant string.

There's a command for that! It's called `test`

```bash
if test "$arg" = "--help" ; then
    echo "This is a helpful message."
fi
```

The `test` program sees `=` and performs a string comparison. It exits with code 0 (success) if
the strings are equal and with a nonzero code if they're unequal.

Be careful, here it's crucial to put spaces around the `=`, since we want to pass _three_ arguments
to `test`:

1. The first string to consider.
2. The equals sign, to indicate a string comparison.
3. The second string to consider.

You can also use `test` in while-loops, of course.

To make the use of `test` look more like "traditional programming", it has an alias called `[`.
Indeed there is literally a file `/bin/[`. That's right, there is a program called "open square
bracket" and it's the exact same program as "test". The only difference is that it expects its last
argument to be "]".

So we can rewrite the above example into the completely equivalent:

```
if [ "$arg" = "--help" ] ; then
    echo "This is a helpful message."
fi
```

See `man test` to learn about all the different comparisons and checks you can use. For example,
`test` has flags for checking if a file exists at a given path, if it's a directory, if it's
executable, etc.

#### Example: checking if the user says yes or no

This program asks the user if they want to proceed before either doing the thing, or exiting.

```bash
read -p "Are you sure? [y/N] " -r answer
if [ "$answer" != "y" ] ; then
    exit 0
fi

# proceed to actually do the thing, whatever it is
```

### For loop

The for-loop in bash is a "Python-style" for-loop. That is, it traverses a sequence, assigning each
item along the way into a variable.

```bash
for f in * ; do
    echo "$f"
done
```

That program is a (crude) approximation of the `ls` program implemented in bash.

## Command-line arguments

When we run a script, we typically provide some arguments on the command-line, e.g.

```bash
$ ./SCRIPT arg1 arg2 ...
```

In the script's source code, we can refer to these with the numbered variables `$1`, `$2`, etc.
We can also refer to the count of given command-line arguments with the variable `$#`.

### Example: looping over command line arguments

This script prints each command-line argument on its own line.

```bash
while [ $# -gt 0 ] ; do
    echo "$1"
    shift
done
```

`shift` is a builtin command that "shifts" over all the numbered variables. That is, the value of
$2 moves into $1, the value of $3 moves into $2, and so on. The value of $1 is lost.

By using `shift`, we reduce the count of command-line argument by one, which is why `$#` eventually
reaches zero, ending the loop.

### Example: parsing command-line arguments 

Using a loop like this, you can write a simple command-line argument parser that handles

```bash
while [ $# -gt 0 ] ; do
    if [ "$1" = "-h" ] ; then
        echo "Helpful message."
        exit 0
    fi
    if [ "$1" = "-t" ] ; then
        TARGET_DIR="$2"
        shift
    elif [ "$1" = "-u" ] ; then
        TARGET_USER="$2"
        shift
    elif [ "$1" = "-v" ] ; then
        VERBOSE_MODE=1
    else
        echo "Unexpected command-line argument $1"
        exit 1
    fi
    shift
done
```

This hypothetical script accepts the following options. The options can be given in any order:

- `-h` to print a help message and quit
- `-t DIR` to set a variable for some kind of a target directory
- `-u USER` to set a username for something
- `-v` to enable verbose mode that would presumably print more messages during execution

Note the extra `shift` in the cases for the `-t` and `-u` options which take an argument following
the option.

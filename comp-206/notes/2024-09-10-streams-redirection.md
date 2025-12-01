# Standard Streams

- **Program**: Something you can run (ex: python code)
- **Process:** Running instance of the program (ex: open vim file)
	- each process is given a *process ID (PID)*
	- Each process has to be started by another process, besides *PID 1* which is started by the Unix kernel itself
	- Programs that can act as PID 1 are called **init systems**.

- Each opened file is associated to a *numeric ID* called a **file descriptor**
- **Ex:** Suppose process A spawns process B
	- A is the parent and B is the child process
	- Process B inherits all the open file descriptors of its parent

By default, each process has 3 file descriptors: 
	1. FD 0 : **standard input**, `stdin`
	2. FD 1 : **standard output**, `stdout`
	3. FD 2 : **standard error**, `stderr`

These are not associated necessarily with files, for example, `stdin` = `input()` in Python and `stdout` = `print()` in Python.

# Redirection

>**Recall**: Bash is to launch programs (create processes)

Here `stdin` comes from the terminal to the command, then if there's an error `sterr` will be sent back to the terminal, but if the process completed `stdout` will go to *output.txt*.

```bash
#redirect command stdout to a file
$ command > output.txt
#this is the same as
$ command 1> output.txt
```

To redirect `stderr`:
```bash
#redirect command stderr to a file
$ command 2> errors.txt
```

You can also stack redirections, and even to the same file.
```bash
$ command > output.txt 2> errors.txt
#this is the same as
$ command 2> errors.txt > output.txt
#to redirect both to the same file
$ command 2> errors.txt >&2
#this is the same as
$ command 2> errors.txt 1>&2
```

To redirect one file descriptor into another one, the syntax is `[N]>&M` where square brackets mean
"optional".
- `N` is the file descriptor to redirect. The default is `1`, i.e. stdout.
- `M` is the file descriptor to redirect into.

To redirect `stdin` from a file
```bash
$ command < input.txt
```

# Piping

Piping is related to redirection. Rather than send the output of a program into a file, or read the
input of a program from a file, piping _connects_ two programs together, so that the output of one
is fed as the input to another.

The syntax is: `A | B`
This runs `A` and `B` concurrently, connecting the stdout of `A` to the stdin of `B`.

This is similar to doing in Python something like `f(g(x))`, i.e. passing the output of one
function as the input to another function.
However, in Python, `g(x)` runs to completion, returns a value, and that value is then fed as input
to `f`. This is different from piping, where `A` and `B` are run _concurrently_ (at the same time).

You can imagine that the following is happening:
- Simultaneously: A and B both start running.
- A runs until it tries to produce some output, at which point it pauses and waits until B tries to
  read some input.
- B runs until it tries to read some input, at which point it pauses and waits until A produces
  some output.

In other words, `A` and `B` _synchronize_ at the points where `A` tries to write and `B` tries to
read.

When A is finished running, it closes the stdout file descriptor, which signals to B that there's
no more data to read. B will then also finish, and close its file descriptors too.

This does not mean that A and B are actually running in _parallel_ (truly at the same time). We
could imagine that on a single-core CPU that the execution looks like:

- Run B until it tries to read some input. Pause B.
- Run A until it produces some output. Pause A. Give that output to B and resume B.
- Repeat until A closes stdout.

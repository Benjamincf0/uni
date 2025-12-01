# COMP 206 - Fall 2025 - Homework 2

Learning goal developed and assessed in this homework:
- Write bash scripts to automate development tasks

Before getting started, see the file `starter-code`, which gives a quick tour
of the features of bash scripting that you will need to complete this
homework.

## Q1: Making backups of your homework

Recall that `rm` is irreversible on Linux. This is different from Windows or macOS, where
"deleting" a file actually places it into some kind of recycling/trash bin from where you can
restore the file.

This means that making backups is very important on Linux! It sure would be terrible if you
accidentally did `rm -rf ~/comp-206/homework`.

**Your task** is to create a script called `backup-hmk`, to be run from the `~/comp-206`
directory, which creates an archive of your `homework` directory, under a subdirectory called
"backups".

Example:

```bash
$ cd ~/comp-206
$ # from here, run the script that is in homework/hw2/backup-hmk
$ homework/hw2/backup-hmk
Created backup: backups/homework-2025-09-11T15:41:00.tar.gz
$ tar tvf backups/homework-2025-09-11T15:41:00.tar.gz
homework/
homework/hw1
homework/hw1/instructions.md
homework/hw1/copying-to-from-mimi.txt
homework/hw1/screenshot.png
...
```

* Your script must create the subdirectory `backups` if it does not exist already.
* Your script must name the archive according to the example.
    * That is, it must include the current date and time (a *timestamp*) from when the script is
      run.
    * The `date` command prints the current date and time, but in the wrong format!
    * Read the manual, `man date`, to see how to control the format of the timestamp.
    * To capture the output of the `date` command into a variable, look up "bash command
      substitution", or see here:
      https://unix.stackexchange.com/questions/440088/what-is-command-substitution-in-a-shell

To learn how to use the `tar` program for creating archives, see
[this guide](https://www.gnu.org/software/tar/manual/html_chapter/Tutorial.html)
written by the folks at GNU, which are the makers of the programs `ls`, `mkdir`, etc. that we use all the ime.

Take notes (in vim) along the way!

**Besides writing the script,** answer these short questions in a file called `using-tar.txt`
(in your `hw2` directory, of course!)

Answer each in at most two lines, giving an example and a brief explanation in words:

- Q1.1: how do you use tar to create an archive?
- Q1.2: how do you use tar to list the contents of an archive?
- Q1.3: how do you use tar to extract the entire contents of an archive?
- Q1.4: by default, where do the files get extracted to?
- Q1.5: what is the significance of the two extensions `.tar.gz`?
- Q1.6: what is a 'tarbomb'? Why could extracting one be dangerous?
    - Read this https://www.linfo.org/tarbomb.html and give a succinct answer.

Of course you could chatgpt all these questions, but then you wouldn't actually be learning
anything from this exercise. That will harm you when you are eventually tested on this material in
a midterm! So do your best to answer by looking things up on google, and in particular using the
GNU tar tutorial I linked above.

## Automating getting homework

To download this homework, you would have done something like...

```bash
$ cd ~/comp-206/homework
$ git clone fall2025-comp206:hw2.git
...
git outputs some message about the clone
...
$ cd hw2
$ ls
instructions.md starter-code
```

There are many assignments in this course, but the only thing that would
realistically change for downloading any particular assignment would be the
number. This is a perfect candidate for writing a script for automation.

**Your task** is to write a script called `get-homework` to automate this process.

Running `get-homework 1` performs a git clone of the hw1 repo into the hw1 subdirectory of
`homework`.

Here's a simple example session demonstrating how the script should work.

```bash
$ ls ~/comp-206/homework
$ # see, no assignments yet; ls produced no output
$ get-homework 1
$ ls ~/comp-206/homework
hw1
$ ls ~/comp-206/homework/hw1
instructions.md
$ get-homework 1
Homework 1 directory already exists. Overwrite? [y/n] y
$
```

Here's the list of features that your script needs to support.

1. **The script must not be senstive to the current working directory.**

   In other words, you could be in any directory when you run the script, and it
   will correctly download the homework into ~/comp-206/homework.
   For example, if I'm in the labs directory, the command-line for running get-homework
   might look like this:
   `jerrin1@teach-node-07:comp-206/labs$ ~/comp-206/homework/hw2/get-homework 1`
   See, I typed out an absolute path to run `get-homework`, because I was in a
   different directory from `hw2`. This should still work to clone the hw1 repo.

2. **The script runs `git clone` to download the requested homework into the
   right location.**
   
   `get-homework 1` runs `git clone fall2025-comp206:hw1.git`, and so on
   for `get-homework 2`, etc. By the way, to specify _where_ to put the cloned
   directory, check out `man git-clone`. There are extra parameters you can use
   to do this.

# Submission instructions

Commit and push your hw2 repo with files named exactly as below.

- `using-tar.txt`
- `get-homework`
- `backup-hmk`

The scripts `get-homework` and `backup-hmk` must have the execute permission
enabled.

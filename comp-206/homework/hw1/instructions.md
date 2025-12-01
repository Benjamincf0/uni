# COMP 206 - Fall 2025 - Homework 1

Instructors: Mona ElSaadawy & Jacob Errington

Soft due date: Monday, 13 September 2025
Hard due date: the date of midterm 1

## Prerequisites

You must have already set up your School of Computer Science (SoCS) account on
our server called `mimi`. On McGill wifi (or after connecting to the [McGill VPN](https://mcgill.service-now.com/itportal?id=kb_article_view&sysparm_article=KB0010687))
visit [this page](https://myaccount.cs.mcgill.ca/) to set up your SoCS account.

## Part 0: setting up your SSH key

`ssh` is a very versatile program that we use to log into a remote computer.
To log into a computer, we need to provide a username (to specify _as who_ we
would like to act as) and some kind of password (to prove that we have the
authority to act as that user.)

You're already used to ordinary passwords, that you type in when you want to
log in. But ssh has an alternative mechanism for providing a password. That
alternative mechanism is called an _ssh key._

An ssh key will have been emailed to you by IT. Download it to your computer.
Assuming your name is Jane Smith, the file you downloaded should be named
something like `jsmith13-key`.

**You will next need to transfer this key into your SoCS account.**

This will depend on what OS is running on your computer.  We won't give
detailed instructions on how to do this; you will need to do some research.

Our recommendation is to find a way to install the `rsync` program on your
computer. This is a command-line program for copying files via ssh.

- On Mac, I recommend getting Homebrew set up. Then you can use `brew install
  rsync`.
- On Windows, I recommend getting the Windows Subsystem for Linux set up. Then
  in that shell, you should be able to do `apt install rsync`.
- On Linux... well if you're running Linux on your computer already, I trust
  you to figure this out.

To learn how to use rsync, check out [this guide](https://www.digitalocean.com/community/tutorials/how-to-use-rsync-to-sync-local-and-remote-directories#using-rsync-to-sync-with-a-remote-system)

You can alternatively install on your computer a graphical program, such as
Cyberduck, Filezilla, or WinSCP.  (Just be careful with the Filezilla
installer: I think you need to uncheck a box during installation somewhere to
prevent it from also installing some kind of garbage alongside Filezilla.)

**Using whatever program you decided to go with,** copy the ssh key you
downloaded (e.g. `jsmith13-key`) into the home directory of your SoCS account.

Next, **log into mimi and do the following:**
- Run this command `mkdir -p .ssh`
  -- create a hidden directory called `.ssh` inside your home directory
- Run this command: `chmod 400 jsmith13-key`
  -- this makes the key read-only, as required by ssh.
- Run this command: `mv jsmith13-key .ssh`
  -- move the key into the `.ssh` directory
- Follow the instructions from the email you received from IT to create the
  file named `config` inside the `.ssh` directory.

Congratulations, you set up your ssh key. You will now be able to obtain and
submit the homework via git on mimi.

## Part 1: creating directories and getting homework instructions

- Create the directory `comp-206` in your home directory.
- Create directories `homework`, `labs`, and `notes` as subdirectories of
  `comp-206`.
- Enter the `homework` directory and clone the repository containing these
  instructions: `git clone fall2025-comp206:hw1.git`
- **If that command fails,** then your ssh key setup is probably incorrect.
  I recommend seeing an a TA or an instructor in office hours to sort
  that out, if you're stuck.
- **If the command succeeds,** then it will create a directory named `hw1`.

Check you did it right: `ls ~/comp-206/homework/hw1/.git` should succeed and
show a listing of some strange, internal git files.

Next, get the lecture notes, which we are also distributing via git:

- `cd ~/comp-206`
- `git clone https://gitlab.cs.mcgill.ca/jerrin/comp-206-notes.git notes`

Check you did it right: `ls ~/comp-206/notes/.git` should succeed and similarly
show a listing of strange, internal git files.

## Part 2: transferring files to and from mimi

- In the `hw1` directory, create a file called `copying-to-from-mimi.txt`.
- Use vim or nano to edit this file, and in it, answer the following questions:
    - Q1: What program you decided to use on your computer to do the copying
      to/from Mimi?
    - Q2: What did you do to install this program? (At most two sentences.)
    - Q3: In at most three sentences, describe how to use the program to copy a
      file from your computer onto mimi

Next:

- On Mimi, in your home directory, run `tree -F comp-206`.
  This will output a textual diagram of the directory structure you created in
  part 1.
- Take a screenshot of the diagram that was produced.
  Make sure to include in the screenshot the command prompt (the line where you
  ran `tree -F`), clearly showing your username.
- Copy the screenshot from your computer onto mimi, into the `hw1` directory,
  and rename it to `screenshot.png` (or `screenshot.jpg` if it happened to be
  in jpg format.)

# Submission instructions

To recap, your hw1 directory should contain at this point:

- `copying-to-from-mimi.txt`; and
- `screenshot.png` (or `screenshot.jpg`); and
- `instructions.md` (this file!)

Then,

- In the hw1 directory:
- Run `git add .` to prepare all changes in the repo to be committed.
- Run `git commit -m "completed assignment 1"`
- Run `git push` to upload your solution, which will run the autograder on it.
  The autograder will output a message saying `SCORE` telling you whether you
  passed the assignment or not.

If you need to revise your solution and resubmit it, repeat the three steps in
this section: `git add .`, `git commit -m "a message describing the changes"`,
`git push`.

**You can run the autograder via git push as many times as you like!**

(In practice when doing software development, running `git push` won't run an
autograder, but it will typically run some scripts to run a test suite on your
code, compile your code, and _deploy_ your program, i.e. make it accessible to
users!)

# Evaluation scheme

This assignment is graded simply on completion, and the main point of it is to
familiarize you with the mechanism by which you'll be doing and submitting your
homework throughout the semester.

Simply pushing with the required files present and of the correct types will
earn full marks.

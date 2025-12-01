# Permissions on files and directories in Unix-like systems

In class on 5 September, I showed you the command `ls -l` which lists the contents of a directory
in so-called "long" form. This form shows ownership properties and permissions of files and
directories. Here's what the output of `ls -l` in my home directory looks like:

```
total 316
-rwxr-xr-x  1 jerrin nogroup 15968 Mar 15 13:19 a.out
drwxr-xr-x  2 jerrin nogroup    33 Feb 16  2023 bin
drwxr-xr-x  4 jerrin nogroup     4 Sep  6 08:49 comp-206
drwxr-xr-x  2 jerrin nogroup    11 May 20 10:23 comp-206-grading
-rw-------  1 jerrin nogroup 18233 Dec 10  2015 comp-273.md
-rwxr-xr-x  1 jerrin nogroup    46 Sep  6 09:00 cs206
drwx------  2 jerrin nogroup     4 Sep  6  2016 Desktop
drwx------ 43 jerrin nogroup    51 Feb 16  2023 dotfiles
drwx------  2 jerrin nogroup     2 Aug 26 14:36 Downloads
-rw-r--r--  1 jerrin nogroup     0 Sep  3 15:51 dum
-rw-r--r--  1 jerrin nogroup     0 Sep  3 15:47 dum dum
-rw-r--r--  1 jerrin nogroup     0 Sep  3 15:45 dummy
drwxr-xr-x  3 jerrin nogroup     3 Oct 20  2023 fall2022
-rw-r--r--  1 jerrin nogroup 37476 Feb 23  2024 fall2023.tar.gz
drwxr-xr-x  2 jerrin nogroup     9 Sep 23  2022 hw1
-rw-r--r--  1 jerrin nogroup 20480 Aug 21 12:41 hw5-dist.tar.gz
-rwxr-xr-x  1 jerrin nogroup   456 Sep 22  2023 loc
drwxr-xr-x  2 jerrin nogroup     2 Sep  5 14:59 not-a-virus
drwxr-xr-x  2 jerrin nogroup     2 Sep  3 15:52 notes
drwx------  2 jerrin nogroup     3 Jul  5  2022 office365
drwxr-xr-x  2 jerrin nogroup     2 Aug 27 11:22 submissions
drwxr-xr-x  3 jerrin nogroup     4 Sep  5 08:17 treasure-hunt
-rw-r--r--  1 jerrin nogroup 20480 Sep  5 09:30 treasure-hunt.tar.gz
-rw-r--r--  1 jerrin nogroup   165 Sep  6 11:17 whos-online
```

We'll dissect what columns 1, 3, and 4 are about.

## Ownership: users and groups

You're already familiar with the concept of users. Each user has a name; e.g. mine is `jerrin`.

Each file or directory in a Unix-like OS such as Linux belongs to a particular user *and* to a
particular group. We can see from the `ls -l` output above that the files in my home directory
belong to the user `jerrin` and to the group `nogroup`. Yes, there is a group called `nogroup` on
mimi.

The user to which a file belongs is called the file's _owner._

A group is a collection of users. You can see what groups your user account belongs to by running
the `groups` command. When I run `groups`, the output is:

```
nogroup aspect-users cs206-tas cs302-admins
```

There are commands for modifying the ownership properties of filesystem entries:

- `chown USER PATH...` changes the owner of PATH to USER
- `chgrp GROUP PATH...` changes the group of PATH to GROUP

**Restrictions:**
- You cannot change the group of an entry to a group that you yourself do not belong to.
- You basically cannot change the owner of a file. `chown` is a command used by the system
  administrator, rather than by users.

Exercises / questions:
- Check the documentation for each of these commands to see what flag to use to recursively change
  the ownership properties inside a directory. (Spoiler, it's not `-r`.)
  You can see the docs for any command on mimi by running `man COMMAND` on mimi.
  `man` stands for "manual". The manual file is opened in the program `less`, which you can quit by
  pressing `q` and scroll around in using `j`, `k`, `u`, and `d` (or the arrow keys smh)
- `chown` is also capable of changing a file's group at the same time as changing its owner.
  Give a command that would change the user of file `A` to `bob` and its group to `devs`.

## Permissions

In addition to its ownership properties, each file has a set of permissions that control who can do
what to the file.

In the `ls -l` output above, the permissions are given in the first column. Consider for example
the permissions on the subdirection `submissions`. Here's the relevant line from the output above:

    drwxr-xr-x  2 jerrin nogroup     2 Aug 27 11:22 submissions

Let's dissect the permissions, `drwxr-xr-x`.
- The very first letter tells us what kind of entry this is. `d` means it's a directory, whereas
  just a dash `-` would mean it's a regular file.
- Next, `rwxr-xr-x` expresses what can be done to the entry by *three different classes of users*:
    - The first set of permissions, `rwx`, indicates that the _owner_ of the file can Read, Write
      and eXecute the entry.
    - The second set of permissions, `r-x`, indicates that users belonging to the file's _group_
      (the `nogroup` group in this case) can Read, and eXecute the entry, but not Write to it.
    - The third set of permissions, `r-x`, indicates that the same conditions apply to _anybody
      else._ That is, users other than the entry's owner *and* other than users belonging to the
      entry's group can read and execute the entry.

To summarize: a permissions string like `rwxrwxrwx` consists of three collections of three letters
each. The collections represent the permissions for the owner, the group members, and anybody else,
respectively. The letters within each group represent which permissions are enabled. A dash means
that permission is disabled.

Permissions on a file can be modified with the command `chmod` (for change _modes_).

usage: `chmod PERMS PATH...`

There are two ways to use chmod: the symbolic notation and the numeric notation.

**Symbolic:** expresses a change to the permissions using _letters_, e.g.
- `chmod +x foo` **adds** (+) the execute permission for each of the three classes of users (owner,
  group members, and anybody else)
- `chmod a+x foo` does the same thing as `chmod +x foo` -- `a` stands for "all"
- `chmod g-w foo` **removes** (-) the write permission from group members
- `chmod u+r foo` adds the read permission for the file's owner
- `chmod o-rwx foo` removes all three permissions for "others" -- `o` stands for "others".
- `chmod a=rx foo` **sets** the read=true, execute=true, and write=false for all classes of users

Multiple symbolic permissions changes can be expressed in one command by separating with commas:
- `chmod a=rw,u+x foo` sets permissions for all classes of users to read+write, then
  additionally adds the execute permission only for the file's owner.

**Numeric:** expresses all the permissions at once, across all three classes of users, e.g.
- `chmod 777 foo` enables all permissions for all classes of users
    - equivalent to `chmod a=rwx foo`.
- `chmod 644 foo` is equivalent to `chmod u=rw,g=r,o=r foo`

To see why these numbers have the effect they do, let's write them in **binary:**
- 7 is 111 in binary. Taking these as booleans (1 = true, 0 = false), we get:
    read=true, write=true execute=true
- Each of the digits in 777 therefore expresses the permissions for one of the classes of
  users.
- 6 is 110 in binary. Again, taken as booleans we get read=true, write=true, execute=false.
- 4 is 100 in binary. As booleans we get read=true, write=false, execute=false.
- So 644 means "owner can read & write, group members only read, anybody else only read".

This notation is also called **octal** because we're writing in base 8.

**Exercise:**
- Convert each symbolic permissions string into its numeric form, and explain which users have
  which permissions according to the string:
    - rw-rw-rw
    - r-x--x--x
    - -w-rwx-r--
- Convert each the following numeric permissions into a symbolic permissions string, and explain
  which users have which permissions:
    - 642
    - 441
    - 755
    - 400

## The interpretation of permissions depends on the entry's type

For a regular file, the read and write permissions are clear. The execute permission affects
whether can run the file as a program or not.
(Contrast: on Windows, whether a file is executable or not depends on its file extension, such as
`.exe`.)

Try it and see:
1. `touch foo` -- create an empty file
2. `./foo` -- try running it
    - get an error: `bash: permission denied: ./foo`
3. `chmod +x foo` -- enable the execute permission on foo
4. `./foo` -- try running it again
    - Nothing happens! That's because running an empty file does nothing.
    - But no error message this time.

For directories, the read, write, and execute permissions mean something slightly different.

- Read: reading a directory means the capability of _listing the entries_ in the directory.
    - In other words, you need the read permission on a directory to use `ls` on it.
- Write: the write permission grants the ability to _create new files_ in the directory.
- Execute: this capability is called "traverse" for directories, and having it means you can "go
  through" the directory to access its contents. This is best illustrated by an example.

Try it and see:
1. Create a directory and a file inside of it: `mkdir example; touch example/foo`
    - You can run multiple commands in the same line by separating them with a semicolon.
2. Remove the read permission from the directory: `chmod -r example`
3. Fail to list the contents of the directory: `ls example`
    - Error message: `"ls: cannot open directory 'example': Permission denied"`
4. But since `example` still has the execute permission: `cat example/foo` works!

So even though we can't list the contents of the directory, if we happen to know what entries are
present inside of it, we can still access those entries thanks to the `x` permission on the
directory.

Try some more:
- Remove the execute permission: `chmod -x example`
- Fail to access the file `foo`: `cat example/foo`
    - Error message: `cat: example/foo: Permission denied`

# Summary

- Each filesystem entry has _ownership properties:_
    - it belongs to one particular user and one particular group.
- The commands `chown` and `chgrp` modify ownership properties.
- This arrangement induces _three classes of users_ from the perspective of any entry:
    - the entry's owning user
    - users belonging to the entry's owning group
    - anybody else
- Each filesystem entry has _permissions_ that control who can do what to the entry:
    - The three capabilities are _read,_ _write,_ and _execute._
    - They are symbolized by the letter `r`, `w`, and `x`.
    - For each class of user, we have one boolean per capability, giving 9 total permissions.
    - The full set of permissions on a file can be written symbolically (e.g. `rwxr-xr-x`) or
      numerically (e.g. those permissions are `755`).
- Permissions can be changed using either of these notations with the `chmod` command.

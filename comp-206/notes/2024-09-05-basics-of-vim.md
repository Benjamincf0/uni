Basics of vim
=============

- When you start vim, you start in normal mode.
- This is the mode for moving around.
- To enter the mode for inserting text, press i.
- To exit insert mode and return to normal mode, press ESC.

- in normal mode:
	- press o to enter insert mode on a new line below the current one
	- press hjkl to move left down up right
	- press w to move forwards one word at a time
	- press b to move backwards one word at a time
	- press e to jump to the end of the (next) word
	- press $ to jump to the end of the line
	- press 0 to jump to the beginning of the line
	- press a to enter insert mode one char after the cursor
	- press A to enter insert mode at the end of the line

Investigate the following normal mode keys: p P O V v % c d x y

Important: in normal mode press u for undo, and ctrl+r for redo

- enter command mode by inputting a colon (shift + semicolon)
- in command mode:
	- `:write` (short form `:w`) saves the current file
	- `:write FILE` saves a _copy_ of the current file somewhere else
	- `:saveas FILE` saves a new copy somewhere else, and makes that the
	  active file, so subsequent :w will save to that file.
	- `:q` to quit. Fails if you have unsaved changes.
	- `:wq` save and quit.
	- `:q!` quit without saving.

Find-and-replace
----------------

Command mode also is how you can use vim's find-and-replace:
	:%s/text to find/text to replace with/

Breaking down this command:

	- % means to affect the entire file. If you leave it out, the
	  find-and-replace happens only on the current line
	- s means "substitute". Other letters are possible in this position,
	  e.g. `d` for delete: `:%d` deletes everything in the file
	- the substitute command then uses slashes to delimit the pattern to
	  find vs what text to replace with
	- after the final slash, you can include _options_, the most common of
	  which is `g`. This makes the replacement happen to each occurrence in
	  a line, vs the default of just the first occurrence in a line.

Practice using the find-and-replace by editing this file in vim and replacing
the word `file` everywhere with the word `document` (or something else entirely.)

The find-and-replace uses a syntax called _regular expressions_ (regex) to
select what text to find. I'll explain more about regex in a later lesson.
For now, know that ordinary text like "file" is also a valid regex, but be
aware that you might run into some issues if the strings you're searching for
include special characters.

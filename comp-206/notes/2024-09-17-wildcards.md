# Shell wildcards

Three kinds:

- star `*` -- match anything
- question mark `?` -- match any single character
- square brackets `[abc]` -- match any single character from the set "abc"
	- ranges can be specified using a dash, e.g. `[a-z]` any lowercase letter
	- multiple ranges `[a-zA-Z]`
	- you can negate the set with a bang: `[!a-z]` any character not a lowercase letter

Wildcards are interpreted by the SHELL. Not the program we're running.

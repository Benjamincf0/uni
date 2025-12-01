# Homework 3: CSV analysis with Bash

In this assignment, you'll be writing a script to process the given dataset `worldcities.csv`,
which contains some facts about cities around the world.

**Your task** is to create a script `analyze` to be used in the following way

* `./analyze count COUNTRY` -- counts the cities in COUNTRY
* `./analyze total COUNTRY` -- sums the population counts of each city in COUNTRY
* `./analyze average COUNTRY` -- computes the average population of the cities in COUNTRY

Your script will be run as above, and you should assume that `worldcities.csv` is in the same
directory as your script.

In this exercise, the main thing we're looking for is that your code is well organized into
functions:

* For each of the operations, define a function in your script: `total`, `average`, and `count`.
* As a helper to be used in each of these, define a function `rows-for` such that `rows-for
  COUNTRY` gives all the rows relating to the given country. Use `grep` for this.
* As a helper for the `total` operation, define a function `sum` that expects to receive numbers on
  stdin, one per line, and sums them all using a while loop and bash's arithmetic substitution
  `$((...))`.
* Computing an average requires calculating the sum, the count, and a division. Your `average`
  function ought to call your `total` and `count` functions.

So to summarize, your script should define 5 functions: `total`, `average`, `count`, `rows-for`, and `sum`.

Your script should be robust. It should exit with code 1 after printing an
error message that explains how to use the script properly when it is being
used incorrectly, e.g.:

* given no arguments, e.g. `./analyze`
* given an unknown/invalid operation, e.g. `./analyze foobar Canada`;
* no country is given as argument to a valid operation, e.g. `./analyze average`;

Your script should just output 0 in case it's asked to compute the average for a country that
doesn't exist / has zero cities / isn't in the CSV file.

**Follow-up** in a file called `all-averages` write a (short) bash script that produces this output
by using a pipeline + a while loop that runs your script with `./analyze average COUNTRY`.

```
Afghanistan 149666
Albania 41797
Algeria 84392
American Samoa 12576
Andorra 11095
Angola 209137
Anguilla 0
Antigua and Barbuda 22219
Argentina 98245
Armenia 53104
Aruba 28237
Australia 80415
Austria 43072
Azerbaijan 47275
Bahrain 73126
Bangladesh 457927
Barbados 110000
Belarus 74913
Belgium 29894
Belize 18007
...
```

That is, it's a (sorted) listing of all the countries with the average city population of that
country.

**It's normal for all-averages to take a while to run.** That's because it
reanalyses the entire worldcities.csv with each call to `analyze`. This is
meant to illustrate that bash scripts can be useful for something
quick-and-dirty, but not for something that should be performant!

## Remarks

The CSV file has some missing data. For instance, some cities do not have a recorded population.
They have an empty string instead. You can infer this to mean zero.

# Dynamic memory

Recall that local variables are stored on the _(call) stack._
This presents a problem when we want to create a function that should allocate a block of memory
that should outlive the function call. Consider the following (incorrect) program, seen in class,
and which can read in full in `c-programs/oct-22/bad.c`:

```c
int *read_numbers(int *n)
{
    printf("How many numbers? ");
    scanf("%d", n);
    int nums[*n]; // !!
    for (int i = 0; i < *n; i++) {
        scanf("%d", &nums[i]);
    }
    return nums; // !!
}
```

The lines marked `!!`, taken together, are problematic: the first allocates space on the stack for
an array of `n` integers, and the second returns a pointer to (the first element of) that array.
The size of the array, determined from the first `scanf`, is returned via the parameter `n`.

When the function returns, its stack frame will be released, and eventually reused to make a
different function call. That other function call will end up overwriting the memory that used to
be reserved for the array.
This is problematic because the returned pointer will continue pointing to the same spot in the
stack, where now, something else is living!

**The takeaway** is that we cannot return a pointer to a local variable, as its **lifetime** ends
when the function that created it returns.

One often applicable solution is to place the onus of allocating the memory onto the _caller:_ the
caller should allocate a block of the right size and pass a pointer in to the function. Sadly that
approach would not work here; only inside the function `read_numbers` does the necessary size (`n`)
become known.

**A more general solution** is to use _dynamic memory._ Memory blocks allocated in this way live
until they are explicitly released.
These memory blocks crucially live not on the stack, but instead in a space called _the heap._

## Allocating and releasing blocks

To allocate a block, use the function `void *malloc(size_t n)`.
Its input, of type `size_t`, is an unsigned integer representing a size in bytes. On Mimi, `size_t`
is equivalent to `unsigned long`.
The output of `malloc` is the unusual type `void *`, meaning "a pointer to something of an unknown
type." This is an alternate use of `void`, which up until now we have used to mean "does not return
anything" when used as the return type of a function.

We can replace the first of the problematic `!!` lines from the above program with the following to
begin fixing the program.

```c
int *num = malloc(n * sizeof(int))
```

When the function returns, the local variable `nums` (the pointer) is destroyed, but the block to
which it points does not! By returning `nums`, the pointer is copied out of `read_numbers`, back to
whatever function called `read_numbers`.

To release the allocated block of memory, use the function `void free(void *ptr)`, e.g.:

```c
int n;
int *data = read_numbers(&n);
free(data);
```

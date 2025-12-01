# Struct, union, and enum

These are three ways of creating new types in C. They all share similarities in their syntax, but
all serve different purposes.

## Enum

An `enum` (short for "enumeration") is a way of associating names to integers that usually function
as a kind of "tag".

```c
enum {
    STUDENT,
    INSTRUCTOR,
    ADMIN
};
```

This defines three constants `STUDENT`, `INSTRUCTOR`, and `ADMIN`, associated respectively to the
values `0`, `1`, and `2`.

You can give explicit values for the constants:

```c
enum {
    STD_MULTIPLIER = 1,
    BONUS_MULTIPLIER = 4,
};
```

Unless given an explicit value, the first enumerator has default value `0`; and unless given an
explicit value, a subsequent enumerator has value 1 greater than its predecessor.

These are just integers, so you can use them like this:

```c
int role = ADMIN;
int multiplier = BONUS_MULTIPLIER;
```

You can associate a name to an enum type, to help mitigate mixups.

```c
enum Role {
    STUDENT,
    INSTRUCTOR,
    ADMIN,
};

enum Role role = ADMIN;
```

Notice that we need to write `enum Role` as the name of the type when we want to declare a variable
of this type. To mitigate this annoyance, we can use `typedef`, to define a type synonym.

```c
typedef enum {
    STUDENT,
    INSTRUCTOR,
    ADMIN,
} Role;
```

This defines `Role` to stand for the (anonymous) type `enum { STUDENT, INSTRUCTOR, ADMIN }`.
With this setup, we could not write `enum Role`, but **only** `Role`.

To get both `enum Role` and `Role` available, we could write:

```c
typedef enum Role { ... } Role;
```

## Struct

A _structure_ is a fixed collection of values, called the _fields_ of the structure, that are
treated as a unit.

For example, we can define a structure to represent a coordinate in a 2D space.

```c
struct {
    float x;
    float y;
} point;

point.x = 32.5;
point.y = -12.78;
```

We use the `.` (dot) operator to access fields of a struct variable.

### Naming structures

It would be crazy to spell out `struct { float x; float y; }` every time to refer to this type.
Just as with `enum`, we can associate a type name directly, and using `typedef`.

```c
struct Point {
    float x;
    float y;
}; // defines the type `struct Point`.

struct Point point; // create a variable of that type.

// or using `typedef` to avoid saying `struct` over and over too

typedef struct {
    float x;
    float y;
} Point;

Point p;
```

### Memory layout, alignment, and sizes of structures

In terms of **memory layout,** the fields of the struct are layed out one after the other. The
memory layout is the same as if we had simply defined separate variables.

```c
Point p;
// ^ has the same memory layout than as if we had done:
float x;
float y;
```

**Important detail:** certain CPU architectures prefer or require that memory accesses be
_aligned._ A 32-bit CPU (conceptually) deals with data in units of 32 bits (4 bytes) at a time.
Such a CPU prefers (or requires) that memory addresses from which to read or at which to store data
be a **multiple of 4.**
You can read more about this phenomenon here: https://en.wikipedia.org/wiki/Data_structure_alignment

The upshot is that sometimes, the size of a structure will exceed the sum of the sizes of all its
fields. For example, on my M1 Mac, the size of the structure `struct { float x; char c; float y }`
is **12** -- it appears that this CPU prefers a 4-byte alignment. The field `x` is stored at offset
`0`; the field `c` is stored at offset `4`; and the field `y` is stored **at offset `8`.** The
compiler has added *three bytes of padding* after the `char c` to satisfy the alignment preference
of my CPU.

Structures can contains other compound data types, such as arrays, or even other structures.

```c
struct Name {
    char first[32];
    char last[32];
};

struct Employee {
    struct Name name;
    int age;
    enum {
        FINANCE,
        ENGINEERING,
        ACCOUNTING,
        MANAGEMENT,
    } department;
};
```

The size of a `struct Employee` will be (at least) 32 + 32 + 4 + 4 = 72 bytes.

### Passing structures by value and by reference

Whereas arrays "decay" into pointers when passed as arguments (rather than be copied, by value),
structures are passed **by value** even if they _contain_ arrays.

For example, consider this program that reads user input to initialize a `struct Employee`.

```c
struct Employee // <- return type is a struct
employee_init(int department)
{
    struct Employee e;
    e.department = department;
    printf("First name: ");
    scanf("%s", e.name.first); // array value is passed as pointer to scanf
    printf("Last name: ");
    scanf("%s", e.name.last); // same
    printf("Age: ");
    scanf("%d", &e.age); // explicitly take the address of e.age
    return e; // copies whole struct, including its embedded arrays, to the caller
}
```

Or alternatively, instead of creating a local variable and returning (a copy of) it, we can take a
reference to an existing `struct Employee` and initialize it thru the function.

```c
void employee_init(struct Employee *e, int department)
{
    (*e).department = department;
    // ^ follow the pointer `e` to the struct, then access the field with `.department`
    // SYNTAX SUGAR: equivalent to writing `e->department = department;`
    // The arrow syntax combines dereference and field access.
    printf("First name: ");
    scanf("%s", e->name.first);
    printf("Last name: ");
    scanf("%s", e->name.last);
    printf("Age: ");
    scanf("%d", &e->age);

    // nothing to return in this case!
}
```

### Example: dynamic array as a struct

A dynamic array is an array into which we add new items as they come, and which can resize itself
to accommodate more items when it runs out of space. In C, such a data structure will need to use
`malloc` to allocate an underlying, fixed-size array to hold the data. We must track the _capacity_
of this underlying array as well as the count of items actually stored in the underlying array.

Here's an implementation of a dynamic array of integers, using a struct to bundle the underlying
array together with its metadata (size and capacity.)

```c
struct IntArray {
    int *underlying; // pointer to underlying, fixed-size array obtained via malloc
    int size; // count of items actually stored in underlying array
    int capacity; // how many items can be stored at most in the underlying array
};

struct IntArray intarray_init(int cap)
{
    struct IntArray a;
    a.capacity = cap;
    a.size = 0; // initially no items are stored.
    a.underlying = malloc(sizeof(int) * cap); // but could store up to `cap` elements
    return a;
}

// EXERCISE: refactor `intarray_init` to take a `struct IntArray` by reference and initialize it.

void intarray_push(IntArray *arr, int item)
{
    if (arr->size == arr->capacity) {
        // ran out of space in underlying, so make it bigger
        arr->capacity *= 2; // double the capacity
        int *new_underlying = malloc(sizeof(int) * arr->capacity);
        // ^ new underlying has _double_ the capacity.
        // copy all existing items over:
        for (int i = 0; i < arr->size; i++) {
            new_underlying[i] = arr->underlying[i];
        }
        free(arr->underlying); // release old underlying array
        arr->underlying = new_underlying;

        // SEE ALSO: the stdlib function `realloc`, which accomplishes much of what we did manually
    }
    arr->underlying[arr->size++] = item;
    // ^ add the new item at the index arr->size, then increment the size
}

void intarray_free(IntArray *arr)
{
    free(arr->underlying);
    // at minimum, that call to `free` is required to release the memory

    // But we can do a bit more to mitigate potential programming errors that lead to security
    // issues. Security issues can arise if we:
    // - free the same block twice (double-free vulnerability)
    // - try to access the block after freeing it (use-after-free vulnerability)

    // This avoids both those issues, at least partially:
    arr->underlying = NULL;
    // Now trying to dereference (use) `underlying` will cause a crash (NULL pointer error)
    // and trying to `free(NULL)` is guaranteed by the standard library to be safe, doing nothing.

    // This helps, but isn't perfect as the old pointer value could have been copied somewhere else
    // in the program.
}
```

## Union

A `union` looks a lot like a struct, but rather than store each of its fields separately, it stores
them _overlapping._ 

```c
union int_or_float {
    int i;
    float f;
};
```

This union has a size of `4`, even though both `int` and `float` have size 4.

Consider this program:

```c
int_or_float hmmm;
hmmm.i = 12; // write to the int field
printf("%f\n", hmmm.f); // but read from the float field
```

The last field that is written to is considered "active", and trying to _read_ from an inactive
field is _undefined behaviour._
Therefore, the program above exhibits undefined behaviour, but in practice, what happens is that
the integer value will get _reinterpreted_ as a float, and that float will be printed out.

Just as an array isn't aware of its own size in C, a union isn't aware of which of its fields is
active. If you need to track that, you need to do it separately.

The smallest possible size a compiler might assign for a union would be the size of the union's
_largest_ field.

### Example: tagged union

A tagged union is simply a union together with a value that indicates which of its fields is
active. This is useful in data modelling, where a real-world phenomenon could be in exactly one of
a fixed set of different states.

For example, we can model a person in a university context as being either a student, an
instructor, or an administrator using the `enum Role` as the tag.

```c
struct Student
{
    float gpa;
    int n_enrolled_courses;
    struct Course enrolled_courses[16];
};

struct Instructor
{
    enum Rank {
        FACULTY_LECTURER,
        SENIOR_FACULTY_LECTURER,
        ASSISTANT_PROF,
        ASSOCIATE_PROF,
        FULL_PROF,
    } rank;
    int n_taught_courses;
    struct Course taught_courses[16];
};

struct Admin
{
    float salary;
    char department[64];
};

struct UniversityPerson
{
    // data common to all objects in the model:
    struct Name name;
    int age;
    // data specific to a certain role:
    union {
        struct Student as_student;
        struct Instructor as_instructor;
        struct Admin as_admin;
    } data;
    // and this enum indicates which role to consider
    // i.e. which of the union fields is active:
    enum Role role;
};
```

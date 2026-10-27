(* ---------------------------------------------------------------------------
   COMP 302 Midterm 2 Review
   --------------------------------------------------------------------------- *)

(* As you may have seen in the practice midterm and rubrics, 
   your midterm will have 4 parts: 
      PART 1: OOP and References
      PART 2: Lazy Programming
      PART 3: Backtracking
      PART 4: Induction
 *)


(* ---------------------------------------------------------------------------
   PART 1: OOP
   --------------------------------------------------------------------------- *)

(* Exercise 1.1:  A parking lot

   Note: this is a very long problem, we will probably only implement a couple
   of these functions
 
   make_lot capacity rate creates a parking lot with room for capacity cars.
   Every car is stored as a record with its plate and its arrival time, and
   leaving produces a record receipt (see the types below).
 
   - enter p : raises AlreadyParked if a car with plate p is already inside;
     raises LotFull if the lot has no room; otherwise the car comes in, arriving
     at the current time. If the lot becomes full because of this car, print
     "Lot is now full".
   - tick () : the clock advances by one hour. The clock starts at 0.
   - leave p : raises NotParked if no car with plate p is inside. Otherwise the
     car is removed and a receipt is returned: who = p, hours = the number of
     hours the car stayed, fee = hours * rate. The fee is added to the lot's
     revenue.
   - plates () : the plates of all cars currently inside.
   - owed () : the total of what all cars currently inside WOULD pay if they
     all left right now. Use List.fold_left.
   - revenue () : total fees collected from cars that have already left.

   Note: you may want to use HOF functions such as exists, filter, 
         or fold_left/fold_right
*)
 
exception LotFull
exception AlreadyParked
exception NotParked
 
type car = { 
    plate : string; 
    arrival : int 
}
type receipt = { 
    who : string; 
    hours : int; 
    fee : int 
}
 
type lot = {
  enter : string -> unit;
  leave : string -> receipt;
  tick : unit -> unit;
  plates : unit -> string list;
  owed : unit -> int;
  revenue : unit -> int;
}
 
let make_lot (capacity : int) (rate : int) : lot =
  failwith "TODO"




(* ---------------------------------------------------------------------------
   PART 2: Lazy Programming
   --------------------------------------------------------------------------- *)

(* There are three ways to represent a lazy sequence:
    Generator: Stateful. If the sequence is finite, it raises StopIteration 
               when finished.
    Stream: Stateless. Always infite. The tail is dealyed in a thunk.
    LList: Stateless. Like a stream but it can end (with Done).
*)

exception StopIteration
type 'a gen = {
    next : unit -> 'a 
}
 
type 'a thunk = Delay of (unit -> 'a)
let force (Delay f) = f ()
 
type 'a stream = { 
    hd : 'a; 
    tl : 'a stream thunk 
}
type 'a llist = 
    | Done 
    | Next of 'a * 'a llist thunk

(* Exercise 2.1: The triangular numbers

   You have probably seen this sequence before. It looks as follows:

       0, 1, 3, 6, 10, 15, 21, ...
 
   The k-th triangular number (starting at k = 0) is 0 + 1 + 2 + ... + k.

   Now we will express this sequence using the three lazytypes.
 
   (a) tri_gen () : an INFINITE gen that produces the whole sequence, one
       number per call to next.
   (b) tri_from k : the INFINITE stream of triangular numbers starting from the
       k-th one. So  tri_from 0  is the whole sequence.
   (c) tri_upto limit : an llist of all the triangular numbers that are at
       most limit, and then it ends.
   (d) tri_gen_upto limit : a gen that produces the same numbers as (c), 
       and then raises StopIteration.
*)
 
let tri_gen () : int gen =
  failwith "TODO"

let rec tri_from (k : int) : int stream =
  failwith "TODO"
 
let tri_upto (limit : int) : int llist =
  failwith "TODO"
 
let tri_gen_upto (limit : int) : int gen =
  failwith "TODO"


(* ---------------------------------------------------------------------------
   PART 3: Backtracking
   --------------------------------------------------------------------------- *)

(* Very important rule of thumb:
   If you are translating, your code should have the EXACT SAME format 
   We will go over how to reason through this. *)

(* There are THREE types of backtracking that you may have to translate between:
      Exceptions
      Options
      CPS 
*)

(* Exercise 3.1:

   Let's go through such a translation.
   
   Subset sum: given a list xs of integers and a target, find a sub-list of xs 
   (in the original order) whose elements add up to target. 

   Here is the Options version. Translate it to Exceptions and to CPS. *)

exception NoSolution

let rec subset_sum_opt (xs : int list) (target : int) : int list option =
  match xs with
  | [] -> if target = 0 then Some [] else None
  | x :: rest ->
      match subset_sum rest (target - x) with
       | Some s -> Some (x :: s)
       | None -> subset_sum rest target

let rec subset_sum_exn (xs : int list) (target : int) : int list =
  failwith "TODO"

let rec subset_sum_cps (xs : int list) (target : int)
    (success : int list -> 'r) (fail : unit -> 'r) : 'r =
  failwith "TODO"

(* A CPS function can be run as either of the other styles, just by choosing
   the continuations: *)
let subset_sum_via_cps xs t =
  subset_sum_cps xs t (fun s -> Some s) (fun () -> None)

let subset_sum_via_cps_exn xs t =
  subset_sum_cps xs t (fun s -> s) (fun () -> raise NoSolution)


(* Exercise 3.2:

   Let's do it again but this time starting from CPS. 
   I won't explain what the function does this time. You need to figure
   it out yourself. (Not that it matters much for a simple translation.)
 *)

type 'a tree = Empty | Node of 'a tree * 'a * 'a tree

let rec path_sum_cps (t : int tree) (n : int)
    (success : int list -> 'r) (fail : unit -> 'r) : 'r =
  match t with
  | Empty -> fail ()
  | Node (Empty, x, Empty) -> if x = n then success [x] else fail ()
  | Node (l, x, r) ->
      path_sum_cps l (n - x)
        (fun p -> success (x :: p))
        (fun () ->
           path_sum_cps r (n - x) (fun p -> success (x :: p)) fail)

let rec path_sum_opt (t : int tree) (n : int) : int list option =
  failwith "TODO"

let rec path_sum_exn (t : int tree) (n : int) : int list =
  failwith "TODO"



(* ---------------------------------------------------------------------------
   PART 4: Induction
   --------------------------------------------------------------------------- *)

(* 
I have put a lot more exercises in this section since you weren't really given 
any problems. We will only do a couple of these.

Exercise 4.1:
    Definitions:
    let rec traverse t =
        match t with 
            | Empty -> [] 
            | Node (l, x, r) -> x :: (traverse l @ traverse r)
    let rec traverse' t acc =
        match t with 
            | Empty -> acc 
            | Node (l, x, r) -> x :: traverse' l (traverse' r acc)

    Prove that these are equivalent. That is: traverse t @ acc = traverse' t acc

Exercise 4.2: 
    Definitions: 
    let rec map f l = 
        match l with 
            | [] -> [] 
            | x :: xs -> f x :: map f xs
    let rec map_cps f l return =
        match l with 
            | [] -> return [] 
            | x :: xs -> map_cps f xs (fun r -> return (f x :: r))

    Prove that these are equivalent. That is: map_cps f l return = return (map f l)


Exercise 4.3:
    Definitions:
    type nat = Z | S of nat
    let rec add n m = 
    match n with 
        | Z -> m 
        | S n' -> S (add n' m)

    Prove the associativity of add. That is: add (add a b) c = add a (add b c)


Exercise 4.4:
    Definitions:
    type 'a tree = Empty | Node of 'a tree * 'a * 'a tree
    let rec mirror t =
        match t with 
            | Empty -> Empty 
            | Node (l, x, r) -> Node (mirror r, x, mirror l)

    Prove that mirror (mirror t) = t


Exercise 4.5:
    Definitions:
    let rec len l = 
        match l with 
            | [] -> 0 
            | _ :: xs -> 1 + len xs
    let rec ins x l =
        match l with 
            | [] -> [x] 
            | y :: ys -> 
                if x <= y 
                then x :: y :: ys 
                else y :: ins x ys

    Prove: len (ins x l) = len l + 1

*)
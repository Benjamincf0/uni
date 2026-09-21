
(* Better implementations exist, but this is OK for our purposes. *)
(* `List.length` is the built-in list length function which works
  like the one you saw in class. *)
(* List.fold_left : ('b -> 'a -> 'b) -> 'b -> 'a list -> 'b 
behaves very much like fold_right seen in class (and List.fold_right exists too),
but the input function takes its arguments in the other order (folding the list
"from the left" instead of "from the right") and List.fold_left is tail-recursive.
In OCaml, it's usually better to fold_left than to fold_right for the tail-recursion. *)
let avg (xs : float list) : float =
  let len = List.length xs in
  let total = List.fold_left (+.) 0. xs in
  total /. float_of_int len


let average_on_pointed (f : 'a -> float) (xs : 'a list) : float =
  avg (List.map f xs)


(* Rewrite average_on_pointed without reference to `xs`.
   You should use `compose`. *)
let average_on_pointfree (f : 'a -> float) : 'a list -> float =
  compose avg (List.map f)
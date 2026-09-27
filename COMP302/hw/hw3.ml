(* Hi everyone. All of these problems are generally "one-liners" and have slick solutions. They're quite cute to think
   about but are certainly confusing without the appropriate time and experience that you devote towards reasoning about
   this style. Good luck! :-) *)

(* For example, if you wanted to use the encoding of five in your test cases, you could define: *)
let five : 'b church = fun s z -> s (s (s (s (s z))))
(* and use 'five' like a constant. You could also just use
   'fun z s -> s (s (s (s (s z))))' directly in the test cases too. *)

(* If you define a personal helper function like int_to_church, use it for your test cases, and see things break, you should
   suspect it and consider hard coding the input cases instead *)

(*---------------------------------------------------------------*)
(* QUESTION 1 *)

(* Question 1a: Church numeral to integer *)
(* TODO: Test cases *)
let to_int_tests : (int church * int) list = [
  (five, 5);
  ((fun s z -> z), 0);
  ((fun s z -> s z), 1);
]

(* TODO: Implement:
   Although the input n is of type int church, please do not be confused. This is due to typechecking reasons, and for
   your purposes, you could pretend n is of type 'b church just like in the other problems. *)
let to_int (n : int church) : int = n (fun x -> x+1) 0

(* Question 1b: Determine if a church numeral is zero *)
(* TODO: Test cases *)
let is_zero_tests : ('b church * bool) list = [
  (five, false);
  ((fun s z -> z), true);
]

(* TODO: Implement *)
let is_zero (n : 'b church) : bool = n (fun x -> false) true

(* Question 1c: Add two church numerals *)
(* TODO: Test cases *)
let add_tests : (('b church * 'b church) * 'b church) list = [
  ((five, (fun s z -> z)),five);
  ((five, (fun s z -> s z)),(fun s z -> s ((five s) z)));
]

(* TODO: Implement *)
let add (n1 : 'b church) (n2 : 'b church) : 'b church = fun s z -> z |> (n1 s) |> (n2 s)

(*---------------------------------------------------------------*)
(* QUESTION 2 *)

(* Question 2a: Multiply two church numerals *)
(* TODO: Test cases *)
let mult_tests : (('b church * 'b church) * 'b church) list = [
  ((five, one),five);
  (((fun s z -> z |> s |> s |> s), (fun s z -> z |> s |> s)), (fun s z -> s ((five s) z)));
]

(* TODO: Implement *)
let mult (n1 : 'b church) (n2 : 'b church) : 'b church = fun s z -> (n1 (n2 s)) z

(* Question 2b: Compute the power of a church numeral given an int as the power *)
(* TODO: Test cases *)
let int_pow_church_tests : ((int * 'b church) * int) list = [
  ((2, (fun s z -> z |> s |> s)), 4);
  ((5, zero), 1);
  ((0, one), 0);
  ((0, zero), 1);
  ((3, (fun s z -> z |> s |> s)), 9);
]

(* TODO: Implement *)
let int_pow_church (x : int) (n : 'b church) : int = n (fun a -> a*x) 1

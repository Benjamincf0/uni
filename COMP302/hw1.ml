(* Question 1: Manhattan Distance *)
(* TODO: Write a good set of tests for distance. *)
let distance_tests = [
  (
    ((0, 0), (0, 0)), (* input: two inputs, each a pair, so we have a pair of pairs *)
    0                 (* output: the distance between (0,0) and (0,0) is 0 *)
  );                    (* end each case with a semicolon *)
    (* Your test cases go here *)
  (((5, 5), (6, 6)), 2);
  (((-6, -7), (-4, -5)), 4);
]
;;

(* TODO: Correct this implementation so that it compiles and returns
         the correct answers.
*)
let distance (x1, y1) (x2, y2) = 
  abs (y2 - y1) + abs (x2 - x1) 



(* Question 2: Binomial *)
(* TODO: Write your own tests for the binomial function.
         See the provided test for how to write test cases.
         Remember that we assume that  n >= k >= 0; you should not write test cases where this assumption is violated.
*)
let binomial_tests = [
  (* Your test cases go here. Correct this incorrect test case for the function. *)
  ((0, 0), 1);
  ((2, 2), 1); 
  ((5, 2), 120/(2*(3*2)));
  ((3, 0), 1);
]

let rec factorial (tot: int) (n: int) : int =
  if n = 1 then tot
  else factorial (n*tot) (n-1)
       
let factorial n =
  if n = 0 then 1
  else factorial 1 n;;
(* TODO: Correct this implementation so that it compiles and returns
         the correct answers.
*)
let binomial n k =
  factorial n / (factorial k * factorial (n-k)) 

(* Question 3: Lucas Numbers *)

(* TODO: Write a good set of tests for lucas_tests. *)
let lucas_tests = [
  ((0), 2);
  ((1), 1);
]

(* TODO: Implement a tail-recursive helper lucas_helper. *)
let rec lucas_helper n a b=
  match n with
  | 0 -> a
  | _ -> lucas_helper (n-1) b (a+b)


(* TODO: Implement lucas by calling lucas_helper. *)
let lucas n = lucas_helper n 2 1

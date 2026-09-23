(* Question 1 *)

(* TODO: Write a good set of tests for {!q1a_nat_of_int}. *)
let q1a_nat_of_int_tests : (int * nat) list = [5, S (S ( S( S( S Z))))]

(* TODO:  Implement {!q1a_nat_of_int} using a tail-recursive helper. *)
let q1a_nat_of_int (n : int) : nat =
  let rec helper (n: int) acc = 
    match n with
    | 0 -> acc
    | _ -> helper (n-1) (S acc)
  in helper n Z

(* TODO: Write a good set of tests for {!q1b_int_of_nat}. *)
let q1b_int_of_nat_tests : (nat * int) list = [S (S ( S( S( S Z)))), 5]

(* TODO:  Implement {!q1b_int_of_nat} using a tail-recursive helper. *)
let q1b_int_of_nat (n : nat) : int =
  let rec helper (n: nat) num =
    match n with
    | Z -> num
    | S n_1 -> helper n_1 (num+1)
  in helper n 0

(* TODO: Write a good set of tests for {!q1c_add}. *)
let q1c_add_tests : ((nat * nat) * nat) list = [(S (S Z), S (S (S Z))), S (S ( S( S( S Z))))]

(* TODO: Implement {!q1c_add}. *)
let rec q1c_add (n : nat) (m : nat) : nat = 
  match n with
  | Z -> m
  | S e -> S (q1c_add e m)


(* Question 2 *)

(* TODO: Implement {!q2a_neg}. *)
let q2a_neg (e : exp) : exp = Times (Const (-1.), e)

(* TODO: Implement {!q2b_minus}. *)
let q2b_minus (e1 : exp) (e2 : exp) : exp = Plus (e1, q2a_neg e2)

(* TODO: Implement {!q2c_pow}. *)
let rec q2c_pow (e1 : exp) (p : nat) : exp = 
  match p with
  | Z -> Const 1.
  | S p1 -> Times (e1, q2c_pow e1 p1)


(* Question 3 *)

(* TODO: Write a good set of tests for {!eval}. *)
let eval_tests : ((exp * float) * float) list = [
  ((Plus (Const 2.0, Var), 3.0), 2.+.3.);
  ((Times (Const 5.0, Var), 3.0), 15.0);
  ((Div (Const 2.0, Var), 3.0), 2./.3.);
]

(* TODO: Implement {!eval}. *)
let rec eval (e : exp) (a : float) : float =
  match e with
  | Plus (c, b) -> (eval c a) +. (eval b a)
  | Times (c, b) -> (eval c a) *. (eval b a)
  | Div (c, b) -> (eval c a) /. (eval b a)
  | Const c -> c
  | Var -> a


(* Question 4 *)

(* TODO: Write a good set of tests for {!diff_tests}. *)
let diff_tests : (exp * exp) list = [
  (Plus (Times (Var, Var), Const 12.0), 
   Plus (Plus (Times (Const 1.0, Var), Times (Var, Const 1.0)), Const 0.0));
  (Div (Var, Var),
   Div (Plus (Times (Const 1.0, Var), Times (Const (-1.0), Times (Var, Const 1.0))), Times (Var, Var)));
  (Times (Const 32.0, Var), 
   Plus (Times (Const 0.0, Var), Times (Const 32.0, Const 1.0)));
]

(* TODO: Implement {!diff}. *)
let rec diff (e : exp) : exp =
  match e with
  | Plus (a, b) -> Plus ((diff a), (diff b))
  | Times (a, b) -> Plus (Times ((diff a), b), Times (a, (diff b)))
  | Div (a, b) -> Div (Plus (Times ((diff a), b), 
                             Times (Const (-1.0), 
                                    Times (a, (diff b))
                                   )
                            )
                      , Times (b, b))
  | Const a -> Const 0.0
  | Var -> Const 1.0
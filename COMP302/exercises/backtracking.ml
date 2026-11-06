(* Question 1 : partition an int list into two lists of equal sum *)
(* TODO: test cases *)
let partition_option_tests : (int list * (int list * int list) option) list = [
  ([], Some ([], []));
  ([1], None);
  ([1; 2], None);
  ([1; 1], Some ([1], [1]));
  ([2; 1; 1], Some ([2], [1; 1]));
  ([3; 1; 2], Some ([3], [1; 2]));
  ([1; 1; 4], None);
  ([-1; 1], Some ([-1; 1], []))
]

(* TODO: implement partition *)
let partition (ns : int list) : (int list * int list) =
  let rec go ns diff =
    match ns with
    | [] when diff = 0 -> ([],[])
    | [] -> raise NoPartitionFound
    | x::xs -> try
          let ys, zs = go xs (diff+x) in (x::ys,zs)
        with 
        | NoPartitionFound -> 
            let ys, zs = go xs (diff-x) in (ys,x::zs)
  in
  go ns 0

(* this function turns the output of partition into a type option *)
(* the tests you write will be tested against this function *)
(* this is meant to allow you to write success and failure tests *)  
let partition_option (ns : int list) : (int list * int list) option =
  try Some (partition ns) with
  | NoPartitionFound -> None


(* Question 2: find a list of int from distinct tuples that add up to tot *)
(*TODO: test cases *)
let choice_sum_option_tests : ((int * ((int * int) list)) * (int list) option) list = [
  ((0, []), Some []);
  ((5, [(5, 9)]), Some [5]);
  ((9, [(5, 9)]), Some [9]);
  ((7, [(5, 9)]), None);
  ((1, [(1, 1); (1, 1)]), None);
  ((6, [(3, 1); (3, 2)]), Some [3; 3]);
  ((10, [(1, 5); (1, 5)]), Some [5; 5]);
  ((100, [(1, 2); (3, 4)]), None)
]

(* TODO: implement choice_sum with CPS-style backtracking *)
let choice_sum (n : int) (tuples : (int * int) list) (return : int list -> 'r) (fail : unit -> 'r) : 'r =
  let rec go l acc return fail =
    match l with
    | [] when sum acc = n -> return acc
    | [] -> fail ()
    | x::xs -> go xs ((fst x)::acc) return (fun () -> go xs ((snd x)::acc) return fail)
  in
  go tuples [] return fail

(* this function turns choice_sum into a function that returns options *)
(* the tests you write will be tested against this function *)
(* this is meant to allow you to write success and failure tests *)
let choice_sum_option (n : int) (tuples : (int * int) list) : int list option =
  choice_sum n tuples (fun x -> Some x) (fun () -> None)

(* Question 3: find a subset of tuple that add up to a given tuple *)
(* TODO: test cases *)
let subset_sum_option_tests : (((int * int) * ((int * int) list)) * ((int * int) list option)) list = [
  (((0, 0), []), Some []);
  (((0, 0), [(1, 1)]), Some []);
  (((1, 2), [(1, 2)]), Some [(1, 2)]);
  (((2, 1), [(1, 2); (2, 1)]), Some [(2, 1)]);
  (((3, 3), [(1, 2); (2, 1)]), Some [(1, 2); (2, 1)]);
  (((3, 4), [(1, 2); (2, 1)]), None);
  (((5, 5), [(1, 1); (2, 2)]), None);
  (((1, 1), [(1, 1); (0, 0)]), Some [(1, 1); (0, 0)])
]

(* TODO: implement subset_sum_option using option-based backtracking *)
let rec subset_sum_option (target : int * int) (tuples : (int * int) list) : (int * int) list option =
  match tuples with
  | [] when target = (0,0) -> Some []
  | [] -> None
  | x::xs -> match subset_sum_option ((fst target)-(fst x),(snd target)-(snd x)) xs with
    | Some ys -> Some (x::ys)
    | None -> subset_sum_option target xs

(* Question 1 *)
(*----------------------------------------*)

(* TODO: Implement the function. *)
let find_sat_assignment_exc (formula : formula) : truth_assignment =
  let vars = collect_variables formula
  in
  let rec go vs acc =
    match vs with
    | [] -> if eval acc formula then acc else raise Unsatisfiable_formula
    | v::vs -> try go vs (Variable_map.add v true acc) with 
      | Unsatisfiable_formula -> go vs (Variable_map.add v false acc)
  in
  go vars Variable_map.empty

(* Question 2 *)
(*----------------------------------------*)

(* TODO: Implement the function. *)
let find_sat_assignment_cps (formula : formula)
    (return : truth_assignment -> 'r) (fail : unit -> 'r) : 'r = 
  let vars = collect_variables formula
  in
  let rec go vs acc fail =
    match vs with
    | [] -> if eval acc formula then return acc else fail ()
    | v::vs -> go vs (Variable_map.add v true acc) (fun () -> go vs (Variable_map.add v false acc) fail)
  in
  go vars Variable_map.empty fail

let satisfying_assignment_of formula =
  find_sat_assignment_cps formula (fun x -> Some x) (fun () -> None)
let is_satisfiable formula =
  find_sat_assignment_cps formula (fun _ -> true) (fun _ -> false)

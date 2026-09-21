
(* Implement safe_div which protects against division by zero. *)
let safe_div (n : int) (d : int) : int option =
  match d with
  | 0 -> None
  | _ -> Some (n / d)

(* Implement safe_div2, which does division twice using safe_div. *)
let safe_div2 (n : int) (d1 : int) (d2 : int) : int option =
  let r = safe_div n d1
  in
  match r with
  | None -> None
  | Some x -> safe_div x d2
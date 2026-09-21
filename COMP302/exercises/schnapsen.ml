let point_value = function
  | Num 10 -> 10
  | Num n  -> failwith ("rank " ^ string_of_int n ^ " does not exist in Schnapsen!")
  | Jack -> 2
  | Queen -> 3
  | King -> 4
  | Ace -> 11
  (* You need to add some more cases here. *)

(* You should use `match` to take apart the `card`s, but an if-else
   chain will probably be helpful for the actual logic. *)
let who_wins (trumps : suit) (lead : card) (follow : card) : winner =
  match (fst lead, fst follow) with
  | (a, b) when a == trumps && b != trumps -> Lead 
  | (a, b) when a != trumps && b == trumps -> Follow
  | (a, b) when a == b -> (
      match (point_value (snd lead), point_value (snd follow)) with
      | (a, b) when a > b -> Lead
      | (_, _) -> Follow )
  | (_, _) -> Lead
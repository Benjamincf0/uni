
let rec list_filter_gen (xs : 'a list) : ('a -> bool) -> 'a list =
  print_string "list_filter_gen\n";
  match xs with
  | [] -> (fun (_: 'a -> bool) -> [])
  | x :: xs ->
      let f_tail = list_filter_gen xs
      in
      fun predicate -> 
        match predicate(x) with
        | true -> x :: f_tail predicate
        | false -> f_tail predicate

let rec tree_map_gen (t : 'a tree) : ('a -> 'b) -> 'b tree =
  print_string "tree_map_gen\n";
  match t with
  | Empty -> fun _ -> Empty
  | Node (left_tree, node_val, right_tree) -> 
      let f_left = tree_map_gen left_tree; in
      let f_right = tree_map_gen right_tree; in 
      fun mapper_fun -> Node (f_left mapper_fun, mapper_fun node_val, f_right mapper_fun)
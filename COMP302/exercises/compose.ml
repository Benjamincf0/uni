let compose (f : 'b -> 'c) (g : 'a -> 'b) : 'a -> 'c =
  fun x -> f (g x)

let double x = 2 * x
let increment x = x + 1

(* 1a: double_add_one *)
let double_add_one = compose increment double

(* 1b: do_8_times *)
let do_8_times f x =
  let do_2_times x = (compose f f) x in
  let do_4_times x =  x |> do_2_times |> do_2_times
  in 
  compose do_4_times do_4_times x


(* 2: add_one_double *)
let add_one_double x = x |> increment |> double

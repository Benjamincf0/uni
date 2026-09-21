let perimeter (shape : shape) : float =
  match shape with
  | Square l -> 4.*.l
  | Circle r -> 2.*.pi*.r
  | Rectangle (a, b) -> 2. *. a +. 2. *. b

let area (shape : shape) : float =
  match shape with
  | Square l -> l*.l
  | Circle r -> r*.r*.pi
  | Rectangle (a, b) -> a*.b
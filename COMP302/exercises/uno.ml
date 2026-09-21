let rec sum_numbers p = 
  match p with
  | Empty -> 0
  | Extend (c, p) -> (
      match snd c with
      | Number i -> i + sum_numbers p
      | _ -> sum_numbers p
    )

let rec sum_numbers_tr p acc =
  match p with
  | Empty -> acc
  | Extend (c, p) -> (
      match snd c with
      | Number i -> sum_numbers_tr p (acc+i)
      | _ -> sum_numbers_tr p acc
    )

let rec damage p =
  match p with
  | Empty -> 0
  | Extend (c, p) -> (
      match snd c with
      | Plus2 -> 2 + damage p
      | _ -> 0
    )

let rec damage_tr p acc =
  match p with
  | Empty -> acc
  | Extend (c, p) -> (
      match snd c with
      | Plus2 -> damage_tr p (acc+2)
      | _ -> damage_tr Empty acc
    )

let rec draw n p =
  match (n, p) with
  | (0, p) -> (Empty, p)
  | (n, Extend (c, remaining)) -> 
      let (drawn, pile) = draw (n-1) remaining
      in
      (Extend (c, drawn), pile)
  | (n, Empty) -> (Empty, Empty)

let rec playable_on c p =
  match p with
  | Extend (c2, rest) when fst c = fst c2 || snd c = snd c2 
    -> Extend (c2, (playable_on c rest))
  | Extend (c2, rest) -> playable_on c rest
  | Empty -> Empty

let rec add p1 p2 =
  match p1 with
  | Empty -> p2
  | Extend (c, rest) -> Extend (c, add rest p2)
  

let rec find_first_of_color col p =
  match p with
  | Empty -> NoCard
  | Extend (c, remaining) -> (
      match fst c with
      | c_col when c_col = col -> YesCard c
      | _ -> find_first_of_color col remaining
    ) 
      
let find_last_of_color col p =
  let rec go col p last_card = 
    match p with
    | Empty -> last_card
    | Extend (c, rest) when fst c = col -> go col rest (YesCard c)
    | Extend (c, rest) -> go col rest last_card
  in
  go col p (NoCard)
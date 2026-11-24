exception NotImplemented

let parser_not_implemented =
  fun _ -> raise NotImplemented

(** Exercise 1: Parse a simple, non-recursive structure *)

type isb = int * string * bool

(** Exercise 2: Parse a CSV *)

type csv_item = Csv_int of int | Csv_string of string
type field = csv_item option
type row = (string * field) list
type csv = row list

(* Charater Stream *)
module CStream : sig
  type t

  val of_string : string -> t
  val is_done : t -> bool
  val uncons : t -> (char * t) option
  val take : int -> t -> (string * t) option
  val take_while : (char -> bool) -> t -> string * t
end = struct
  type t = { input : string; len : int; index : int }

  let of_string s = { input = s; len = String.length s; index = 0 }

  let is_done cs = cs.len = cs.index

  let uncons cs =
    if is_done cs
    then None
    else Some (cs.input.[cs.index], { cs with index = cs.index + 1 })

  let take l cs =
    let index = cs.index + l in
    if index <= cs.len
    then Some (String.sub cs.input cs.index l, { cs with index })
    else None

  let take_while p cs =
    let rec find_index i =
      if i >= cs.len || not (p cs.input.[i])
      then i
      else find_index (i + 1)
    in
    let index = find_index cs.index in
    (String.sub cs.input cs.index (index - cs.index), { cs with index })
end

(* Parser Primitives/Utilities *)
module Parser : sig
  type input
  type 'a output
  type 'a t = input -> 'a output
  exception InvalidKeyword

  val run : 'a t -> string -> 'a option

  val of_value : 'a -> 'a t
  (** read this as "and then" *)
  val ( |*> ) : 'a t -> ('a -> 'b t) -> 'b t
  (** read this as "before" *)
  val ( |>> ) : 'a t -> 'b t -> 'b t
  val fail : 'a t
  val map : ('a -> 'b) -> 'a t -> 'b t
  val map2 : ('a -> 'b -> 'c) -> 'a t -> 'b t -> 'c t
  val map3 : ('a -> 'b -> 'c -> 'd) -> 'a t -> 'b t -> 'c t -> 'd t
  val const_map : 'b -> 'a t -> 'b t
  val first_of_2 : 'a t -> 'a t -> 'a t
  val first_of : 'a t list -> 'a t
  val many : 'a t -> 'a list t
  val some : 'a t -> 'a list t
  val sep_by : 'a t -> 'b t -> 'a list t
  val sep_by_1 : 'a t -> 'b t -> 'a list t
  val optional : 'a t -> 'a option t
  val skip : 'a t -> unit t
  val no : 'a t -> unit t
  val between : unit t -> unit t -> 'a t -> 'a t

  val satisfy : (char -> bool) -> char t
  val satisfy_many : (char -> bool) -> string t
  val accept_char : char -> char t
  val accept_string : string -> string t

  val eof : unit t
  val spaces : unit t
  val lexeme : 'a t -> 'a t
  val symbol : string -> unit t
  val keyword_among : string list -> string -> unit t
  (** [identifier_except] takes a list of keywords and fails if the current identifier is one of the keywords *)
  val identifier_except : string list -> string t
  (** [digits] only allows "0" or a number character sequence not starting with 0s *)
  val digits : string t
  val int_digits : int t

  val prefix_op : 'a t -> 'b t -> ('a -> 'b -> 'b) -> 'b t
  val right_assoc_op : 'a t -> 'b t -> ('b -> 'a -> 'b -> 'b) -> 'b t
  val left_assoc_op : 'a t -> 'b t -> ('b -> 'a -> 'b -> 'b) -> 'b t
  val non_assoc_op : 'a t -> 'b t -> ('b -> 'a -> 'b -> 'b) -> 'b t
end = struct
  let is_digit = function '0' .. '9' -> true | _ -> false
  let is_nonzero_digit = function '1' .. '9' -> true | _ -> false
  let is_alpha = function 'a' .. 'z' | 'A' .. 'Z' -> true | _ -> false
  let is_alphanum c = is_alpha c || is_digit c
  let is_whitespace = function ' ' | '\t' | '\r' | '\n' -> true | _ -> false

  type input = CStream.t
  type 'a output = 'a option * CStream.t
  type 'a t = input -> 'a output
  exception InvalidKeyword

  let run p inp = CStream.of_string inp |> p |> fst

  let of_value x = fun is -> (Some x, is)

  let ( |*> ) p f = fun is0 ->
    match p is0 with
    | Some a, is1 -> f a is1
    | None, is1 -> (None, is1)

  let ( |>> ) p q = p |*> fun _ -> q

  let fail = fun is -> (None, is)

  let map f p = p |*> fun a -> of_value (f a)

  let map2 f p q = p |*> fun a -> map (f a) q

  let map3 f p q r = p |*> fun a -> map2 (f a) q r

  let const_map c p = map (fun _ -> c) p

  let first_of_2 p q = fun is ->
    match p is with
    | None, _ -> q is
    | result -> result

  let rec first_of ps =
    match ps with
    | [] -> fail
    | p :: ps -> first_of_2 p (first_of ps)

  let rec many p = first_of_2 (some p) (of_value [])
  and some p = p |*> fun x -> map (fun xs -> x :: xs) (many p)

  let sep_by_1 p sep = map2 (fun x xs -> x :: xs) p (many (sep |>> p))
  let sep_by p sep = first_of_2 (sep_by_1 p sep) (of_value [])

  let optional p = first_of_2 (map (fun a -> Some a) p) (of_value None)

  let skip p = map (fun _ -> ()) p

  let no p = fun is ->
    match p is with
    | Some a, _ -> (None, is)
    | None, _ -> (Some (), is)

  let between p1 p2 q = p1 |>> q |*> fun a -> const_map a p2

  let satisfy p = fun is0 ->
    match CStream.uncons is0 with
    | None -> (None, is0)
    | Some (c, is1) ->
       if p c
       then (Some c, is1)
       else (None, is0) (* doesn't consume a character for the unsatisfied case *)

  let satisfy_many p =
    fun is ->
    let str, rest = CStream.take_while p is in
    (Some str, rest)

  let accept_char c = satisfy (fun c' -> c = c')

  let accept_string s =
    let len = String.length s in
    fun is0 ->
    match CStream.take len is0 with
    | None -> (None, is0)
    | Some (s', is1) ->
       if s = s'
       then (Some s, is1)
       else (None, is0) (* doesn't consume a character for the unsatisfied case *)

  let eof = fun is ->
    if CStream.is_done is
    then (Some (), is)
    else (None, is)

  let spaces = skip (satisfy_many is_whitespace)

  (** [lexeme p] runs [p] and
      consumes all whitespaces after [p] if it succeeds.
      Otherwise, it backtracks. *)
  let lexeme p = first_of_2 p fail |*> fun a -> const_map a spaces

  let symbol s = lexeme (skip (accept_string s))

  let keyword_among keywords s =
    if List.mem s keywords
    then lexeme (accept_string s |>> no (satisfy is_alphanum))
    else raise InvalidKeyword

  let identifier_except keywords =
    lexeme
      (map2
         (fun c cs -> String.make 1 c ^ cs)
         (satisfy is_alpha)
         (satisfy_many is_alphanum)
       |*> fun s ->
           if List.mem s keywords
           then fail
           else of_value s)

  let digits =
    first_of_2
      (const_map "0" (accept_char '0' |>> no (satisfy is_digit)))
      (map2
        (fun c cs -> String.make 1 c ^ cs)
        (satisfy is_nonzero_digit)
        (satisfy_many is_digit))

  let int_digits = map (fun s -> int_of_string s) digits

  let prefix_op operator operand con =
    map2
      (function
       | Some op -> con op
       | None -> fun a -> a)
      (optional operator)
      operand

  let right_assoc_op operatorp operandp con =
    map2
      (fun a0 opbs -> List.fold_right (fun (op, b) f a -> con a op (f b)) opbs (fun a -> a) a0)
      operandp
      (many (map2 (fun op b -> (op, b)) operatorp operandp))

  let left_assoc_op operatorp operandp con =
    map2
      (List.fold_left (fun a (op, b) -> con a op b))
      operandp
      (many (map2 (fun op b -> (op, b)) operatorp operandp))

  let non_assoc_op operatorp operandp con =
    map2
      (fun a ->
        function
        | Some (op, b) -> con a op b
        | None -> a)
      operandp
      (optional (map2 (fun op b -> (op, b)) operatorp operandp))
end

type 'a parser = 'a Parser.t

(* Parse a newline character. *)
let newline = Parser.satisfy (( = ) '\n')
(* End of prelude *)
(* If you'd rather write `Parser.name` instead of `name`
   for the combinators we've given in the `Parser` module,
   you can remove this `open` statement. This would allow you to use the
   same name for something else without conflicts.
   In the project, that will probably be very useful. *)
open Parser

(* Exercise 1: parse a simple, non-recursive structure. *)

(* We HIGHLY recommend building up your parsers from small pieces,
   which will often require you to write small helper-parsers that
   we didn't ask for and don't test. Test them yourself!
   In this case, we will guide you towards our solution.
   Here are brief descriptions of the helpers we used: *)

(* In a couple of places, we need to check extra conditions.
   Use this given 'guard' combinator to perform such checks.
   See how easy it is to write your own combinators? *)
let guard (ok : bool) : unit parser = if ok then of_value () else fail
(* use like this: guard <condition> |>> ...keep parsing... *)

(* `int_digits` is defined in the prelude and parses an `int`. *)
(* `comma` parses a single ',' character and nothing else. *)
let comma : char parser = satisfy ((=) ',')

(* `text` parses all of the input up to the next ',' character or end of line,
   but not the ',' character. We used `satisfy_many` for this. 
   We also have to be sure that we parsed anything at all, but we've implemented
   this check for you as an example. *)
let allowed_in_text c = c <> ',' && c <> '\n'
let text : string parser =
  parser_not_implemented |*> fun result ->
    guard (String.length result > 0) |>
    const_map result

(* `bool` parses either the word "true" or the word "false"
   and returns the corresponding boolean value.
   To do that, we wrote two smaller helpers, for handling each case.
   Those parsers are implemented with `const_map` and `accept_string`. *)
let parse_true : bool parser =
  const_map (raise NotImplemented) (accept_string "true")
let parse_false : bool parser =
  parser_not_implemented
(* accepts either "true" or "false." Use `first_of_2` for this. *)
let bool : bool parser = parser_not_implemented

(* Now put everything together, to parse syntax like the following:
    55,some text,true
  That is, `run isb "55,some text,true"` should return `(55, "some text", true)` *)
(* Use |*> to assemble the pieces. *)
let isb = parser_not_implemented

(** DO NOT Change This Definition *)
let parse_isb = run (between (of_value ()) eof isb)

(* Exercise 2: Parse a CSV. *)

(* csv_int should parse an int, and wrap it up in a `Csv_int` constructor.
   You should use `map` to accomplish this. *)
let csv_int = parser_not_implemented
(* csv_string is like `text` above, but wraps it up in `Csv_string` constructor.
   You should use both `map` and `text` for this - don't reinvent the wheel! *)
let csv_string = parser_not_implemented
(* csv_item parses a CSV Item: either an int, or a string.
   Be careful to not parse "5" as `Csv_string "5"`! *)
let csv_item = parser_not_implemented

(* Each field in the CSV is actually optional. How to parse that?
   Why with the parser named `optional` of course! *)
let field = parser_not_implemented

(* Rows are a little bit more complicated. The _first_ row of a CSV is special,
   and consists of headers for each column. The result of parsing that will be
   a list of header names. How will we get that into our row parser?

   This is one of the strengths of writing parsers this way. We can write our own
   combinator that takes the list of header names and _constructs_ an appropriate
   row parser! *)
(* First, the header: this should be comma-separated `text`. For this, you should
   use the `sep_by_1` function from the Prelude, as well as your `text` and
   `comma` parsers. *)
let header = parser_not_implemented

(* Now, other rows: a comma-separated sequence of fields. Then you can combine
   the fields with the names from the header using `List.combine`.
   
   Once you have that working, also ensure that the number of fields matches the
   number of header names. Otherwise, bad input will crash your program! *)

let row header_names = parser_not_implemented

(* Finally, put it together into a CSV parser. A CSV is a header, followed
   by many newlines and rows. *)
let csv = parser_not_implemented

(** DO NOT Change This Definition *)
let parse_csv = run (between (of_value ()) eof csv)



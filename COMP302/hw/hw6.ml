let open_account (pass : password) : bank_account =
  (* TODO: create any helper variables and/or functions *)
  let balance = ref 0 in
  let password = ref pass in
  let wrong_attempts = ref 0 in
  let validate pw = match pw with
    | _ when !wrong_attempts >= 3 -> raise account_locked
    | _ when pw = !password -> wrong_attempts := 0
    | _ -> wrong_attempts := !wrong_attempts + 1;raise wrong_pass
  in
  let check_amt amt = if amt < 0 then raise negative_amount else ()
  in
  (* TODO: Implement deposit to add money to the account *)
  let deposit p amt = validate p; check_amt amt ; balance := !balance + amt
  in
  (* TODO: Implement show_balance for the account *)
  let show_balance p = validate p ; !balance in
  (* TODO: Implement withdraw money for the account *)
  let withdraw p amt = validate p; check_amt amt ; 
    if !balance < amt then raise not_enough_balance ;
    balance := !balance - amt in
  {
    deposit;
    show_balance;
    withdraw;
  }
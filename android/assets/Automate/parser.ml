(************************** Lexical Analyzer ***********************************)
(*
#load "dynlink.cma"
#load "camlp4o.cma"
*)
open ToXML
(*
type entree = string
type etat_name = string
type action = string

type etat = {
  nom : etat_name;
  transitions : (etat_name * entree * action) list
}

type automate = {
  etats : etat list;
  etatInitial : string;
  etatsFinals : string list;
  etatsBloquants : string list;
  nom1 : string;
}
*)
(*Code reutilisé du super professeur Michael Périn :) *)
let (one_letter: char Stream.t -> string) = parser
  | [< '( 'a'..'z' | 'A'..'Z' ) as c >] -> String.make 1 c

let (one_digit: char Stream.t -> string) = parser
  | [< '( '0'..'9') as c >] -> String.make 1 c

let rec (some_digits: char Stream.t -> string) = parser
  | [< s1 = one_digit ; s2 = some_digits >] -> s1^s2
  | [<>] -> ""

let (one_ident_symbol: char Stream.t -> string) = parser
  | [< s = one_letter >] -> s
  | [< s = one_digit >]  -> s
  | [< ''_' >] -> "_"

let rec (some_ident_symbols: char Stream.t -> string) = parser
  | [< s1 = one_ident_symbol ; s2 = some_ident_symbols >] -> s1 ^ s2
  | [<>] -> ""

let (one_symbol: char Stream.t -> string) = parser
  | [< '( '('|')'|'['|']'|'-'|'.'|'='|';'|',' ) as c >] -> String.make 1 c
 
let (one_ident: char Stream.t -> string) = parser
  | [< s1 = one_letter ; s2 = some_ident_symbols >] -> s1 ^ s2
  
let (one_space: char Stream.t -> string) = parser
  | [< '( ' ' | '\n' | '\t' as c) >] -> String.make 1 c

let (one_natural: char Stream.t -> string) = parser
  | [< s1 = one_digit ; s2 = some_digits >] ->s1 ^ s2

(* Si un nombre se finit par '.' alors c'est un float *)
let (aux_opt: char Stream.t -> string) = parser
  | [< x=one_digit; d=some_digits >] -> x ^ d
  | [< >] -> "0"

(* Lit un flot et renvoie un string *)
let (opt_decimal: char Stream.t -> string) = parser
  | [< ''.' ; d = aux_opt >] -> d
  | [<>] -> ""

let variable = 
  ["Etat";"Transition"; "ETATSUIVANT"; "ENTREE"; "SORTIE"; "ETAT_INITIAL"; "ETAT_FINAL"; "ETAT_BLOQUANT"; "AUTOMATE"]

exception Variable_inconnue of string


(* 2. THE LEXICAL ANALYZER: stream of chars -> token list *)
    
type token = 
  | S of string
  | V of string

let tk_of_list a = 
  if List.mem a variable then V a  
  else S a

let rec (one_number: char Stream.t -> token) = parser
  | [< '( '-') ; t = one_number >] -> 
	  (match t with
	  | S i -> S (i)
	  | S f -> S (f) 
      | _ -> failwith "ONE_NUMBER"
	  )
  | [< n = one_natural ; d = opt_decimal >] -> 
	  if d = "" 
	  then S (n)
	  else S  (n ^ "." ^ d)
  
let rec (to_list: 't Stream.t -> 't list) = fun stream ->
      match Stream.peek stream with
      |	None -> []
      |	Some t -> begin Stream.junk stream ; t::(to_list stream) end

let rec next_token = parser
  | [< '( ',' | ' ' | '=' | '(' | ')' | '[' | ']' | '\n' | '\t' ); tk = next_token >] -> tk
  | [< x = one_number >] -> Some ( x )
  | [< x = one_ident >] -> Some (tk_of_list (x))
  | [< >] -> None


(*Analyse un stream et renvoie sa liste de tokens correspondante*)
let (lexical_analyzer: char Stream.t -> token list) = 
  fun stream -> to_list (Stream.from (fun n -> next_token stream))


(************************** Lexical Analyzer ***********************************)
(*Lis chaque ligne du fichier et renvoie une liste de tokens*)
let rec string_inputs = function in_channel -> 
    try
        let line = input_line in_channel in
        let list_as_token = lexical_analyzer (Stream.of_string line) in 
        (list_as_token) @ string_inputs in_channel
    with
        End_of_file -> []

(*)
(*Ouvre la TABLE de correspondance Nom / Code des primitive de test et action*)
let getTable =
  let in_channel = open_in "TABLE" in
    let table = (string_inputs in_channel) in
    close_in in_channel;
    table
      
(*Remplace le nom du test ou de l'action par son code*)
let rec filter liste_transition  =
  let table = getTable in
  let rec search_correspondance table mot = match table with
    |[] ->failwith "ACTION OU TEST PAS DANS LA TABLE"
    |S moot :: S code :: tail ->if (moot=mot) then code else search_correspondance tail mot
  in
  match liste_transition with
  |[] -> []
  |(osef, test, action) :: tail -> let test_code=(search_correspondance table (test)) and action_code=(search_correspondance table (action)) in (osef,test_code, action_code) :: filter tail
*)

let rec parse_transition (l) = match l with
  |[]->([],[])
  |V "Transition" :: V "ETATSUIVANT" :: S etatSuiv :: V "ENTREE" :: S entree :: V "SORTIE" :: S sortie :: tail -> let (a,b) = parse_transition tail in ((etatSuiv, entree, sortie) :: a , b)
  |a::b -> ([], a::b)

let rec parse_etat l = match l with
  |[]-> ([],[])
  |V "Etat" :: S name :: tail -> let (a,b) = parse_transition tail in let (c,d)=parse_etat b in ({nom = name; transitions = (a)}::c, d)
  |a::b -> ([],a::b)

let parse_etat_init l = match l with
  |V "ETAT_INITIAL" :: S name :: tail -> (name,tail)
  |_ -> failwith "Erreur etat initial"
    
let rec parse_etat_final l = match l with
  |[] -> ([],[])
  |V "ETAT_FINAL" :: tail -> parse_etat_final tail
  |S etat :: tail -> let (a,b)=parse_etat_final tail in (etat::a,b)
  |a::b -> ([],a::b)

let rec parse_etat_bloquant l = match l with
  |[] -> ([],[])
  |V "ETAT_BLOQUANT" :: tail -> parse_etat_final tail
  |S etat :: tail -> let (a,b)=parse_etat_final tail in (etat::a,b)
  |a::b -> ([],a::b)

let parse_nom l = match l with
  |V "AUTOMATE" :: S name :: tail -> (name,tail)
  |_ -> failwith "Erreur etat initial"

let automate l =
  let (etaat, reste) = parse_etat l in
  let (etat_init, reste2) = parse_etat_init reste in
  let (etat_final, reste3) = parse_etat_final reste2 in
  let (etat_bloquant, reste4) = parse_etat_bloquant reste3 in
  let (nom, rest5) = parse_nom reste4 in
  {etats = etaat;
   etatInitial = etat_init;
   etatsFinals = etat_final;
   etatsBloquants = etat_bloquant;
   nom1 = nom
  }
    
(*Ouvre un fichier de nom donne en parametre et renvoie une structure de type automate*)
let make file_name =
    let in_channel = open_in file_name in
    let list_token = (string_inputs in_channel) in
    close_in in_channel;
    automate list_token
(*
let rec search_correspondance table mot = match table with
    |[] ->failwith "ACTION OU TEST PAS DANS LA TABLE"
    |S moot :: S code :: tail -> if (moot=mot) then code else search_correspondance tail mot 


let test = [("1", "NON_INTERSECTION", "AVANCER");
      ("2", "INTERSECTION", "DIRECTION_ALEATOIRE");
      ("3", "EST_MORT", "END_LIFE")]
  
let rec filter liste_transition  =
  let table = getTable in
  match liste_transition with
  |[] -> []
  |(osef, test, action) :: tail -> let test_code=(search_correspondance table (test)) and action_code=(search_correspondance table (action)) in (osef,test_code, action_code) :: filter tail

let _=getTable
let _=search_correspondance getTable "INTERSECTION"
let _=filter test
let _ = make "rama"
let _ = sear
*)

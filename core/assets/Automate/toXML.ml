

(* Types *)

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
(* Fonctions *)
let rec getTransitions (trans : (entree * etat_name * action) list): string =
  let getTransition transition =
  let (entry, nom_etat, act) = transition in
  " <Transition Entree= \""^ (nom_etat) ^ "\" Etat= \"" ^ (entry) ^ "\" Action = \"" ^ act ^ "\" > </Transition> \n" in

  match trans with
    |[] -> ""
    |a::tail -> getTransition a ^ getTransitions tail

let rec getEtats (e :etat list) : string =
  let getEtat (e : etat) : string =
  let {
    nom = name;
    transitions = trans
  }=e in
  "<Etat Nom= \"" ^ name ^ "\" > \n" ^  getTransitions trans ^ "</Etat> \n"
  in
  match e with
    |[] -> ""
    |head::tail -> getEtat head ^ getEtats tail

let rec getEtatsFinals (e :string list) : string =
  match e with
    |[] -> ""
    |head::tail -> "<Etat Nom= \"" ^ head ^ "\" > \n" ^ "</Etat> \n" ^ getEtatsFinals tail

let getAutomate (a : automate) : string =
  let  {

    etats = etat;
    etatInitial = etatInit;
    etatsFinals = etatEnd;
    etatsBloquants = etatBlo;
    nom1 = name;
  } = a in
  "<Automate ID= \""^ name ^"\" > \n" ^
    getEtats etat ^

    "<Etat_Initial> \n" ^
       getEtatsFinals ([etatInit]) ^
    "</Etat_Initial> \n" ^

    "<Etats_Finals> \n" ^
       getEtatsFinals etatEnd ^
    "</Etats_Finals> \n" ^

    "<Etats_Bloquants> \n" ^
       getEtatsFinals etatBlo ^
    "</Etats_Bloquants> \n" ^
 "</Automate> \n"

let entete = "<?xml version = \"1.0\" ?>\n"


let writeAutomate (a:automate) =
  let outChannel = open_out (a.nom1 ^".xml") in
  let string = getAutomate a in
  output outChannel (entete) 0 (String.length(entete));
  output outChannel (string) 0 (String.length(string));
  close_out outChannel



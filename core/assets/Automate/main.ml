open Parser
open ToXML

(*Verifie le nombre d'argument*)
let check_arg=
      if Array.length Sys.argv < 1 then (
              print_endline ("Ce programme prend 1 arguments.\n");
                  exit 1
                    )

(* Recupere le nom de fichier donner en agument*)          
let file_name=Sys.argv.(1)

(*On ouvre le fichier passé en argument du programme.*) 
let automate = Parser.make file_name

(*Ecrit dans le fichier, nommé après l'automate*)
let _ = ToXML.writeAutomate automate

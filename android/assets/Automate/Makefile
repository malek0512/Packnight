AUTOMATES = alex.auto princesse.auto fm_aleatoire_aveugle.auto fm_aleatoire.auto packnight.auto fm_berserk.auto fm_suiveur.auto rama.auto fm_immobile.auto ramasseur_pacgum.auto fm_lord.auto pa_fuit.auto fm_intercepteur.auto pa_en_detresse.auto

SUPPR = $(AUTOMATES:.auto=.xml)

all: main $(SUPPR)

# REGLES GENERIQUES #

%.xml: %.auto
	$(EXEC) $<

EXEC =./main.native 

main:
		ocamlbuild -pp "camlp4o.opt -unsafe" -libs graphics main.native	       

clean:
		ocamlbuild -clean
		rm -f *~ $(SUPPR) 

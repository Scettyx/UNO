# UNO per Metodologie di Programmazione (La Sapienza)
Questa è una versione del gioco di carte di UNO sviluppata in Java21 con JavaFX.
L'unica libreria "esterna" utilizzata è stata Gson.



# Authors
- Cosmin Florea       (M. 2241398)
- Massimo Giorgini    (M. 2234123)



# Instructions
Per l'avvio del progetto esistono due Classi principali da eseguire:
- "src.it.uniroma1.mdp.uno.view.MainApp"    da Eclipse.
- "src.it.uniroma1.mdp.uno.view.Launcher"   da VSCode/VSCodespace.

Per avviare una PARTITA NORMALE bisogna:
- Avviare la Classe Principale.
- Cliccare "INIZIA PARTITA".
- Selezionare "Partita Normale".
- Configurare manualmente "Modalità", regole, tipi di giocatori (o bot) e nomi.
- Cliccare "AVVIA PARTITA".
- Divertirsi.

Per salvare una PARTITA NORMALE bisogna:
- Avviare una PARTITA NORMALE.
- Cliccare "Salva" nel momento desiderato.
- Chiudere il gioco.

Per riavviare una PARTITA NORMALE (già salvata) bisogna:
- Avviare la Classe Principale.
- Cliccare "CARICA PARTITA".
- Divertirsi.

Per avviare una SIMULAZIONE TRA BOT bisogna:
- Avviare la Classe Principale.
- Cliccare "INIZIA PARTITA".
- Selezionare "Simulazione".
- Configurare manualmente regole, tipi di bot ed eventuali nomi.
- Cliccare "AVVIA SIMULAZIONE" tra le due modalità disponibili.
- Guardare.



# Limits
- I bot NON contestano l'eventuale mancata chiamata di Uno.
- I bot scelgono randomicamente il colore in seguito all'uso di una carta Wild.
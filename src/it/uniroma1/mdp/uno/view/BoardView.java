package it.uniroma1.mdp.uno.view;

import it.uniroma1.mdp.uno.model.game.GameEngine;
import it.uniroma1.mdp.uno.model.game.GameAction;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.deck.DiscardPile;
import it.uniroma1.mdp.uno.model.player.HumanPlayer;
import it.uniroma1.mdp.uno.model.player.Player;
import it.uniroma1.mdp.uno.model.player.Player.PlayerType;
import it.uniroma1.mdp.uno.model.player.Player.UNOState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
 */
public class BoardView extends BorderPane {

    private final GameEngine game;

    private HBox playerHandBox;
    private HBox centerAreaBox;
    private VBox opponentsBox;

    public BoardView(GameEngine game) {
        this.game = game;
        getStyleClass().add("table-bg");

        setupLayout();
        refreshBoard();
    }

    private void setupLayout() {
        playerHandBox = new HBox(-20);
        playerHandBox.setAlignment(Pos.CENTER);
        playerHandBox.setPadding(new Insets(20));
        setBottom(playerHandBox);

        centerAreaBox = new HBox(40);
        centerAreaBox.setAlignment(Pos.CENTER);
        setCenter(centerAreaBox);

        opponentsBox = new VBox(10);
        opponentsBox.setAlignment(Pos.TOP_CENTER);
        opponentsBox.setPadding(new Insets(20));
        setTop(opponentsBox);
    }

    public void refreshBoard() {

        // --- CONTROLLO VITTORIA GRAFICA ---
        if (game.getGameMode().getGameOver()) {
            showVictoryScreen();
            return; // Blocca tutto il resto e non disegna più il tavolo!
        }

        if (playerHandBox != null)
            playerHandBox.setDisable(false);

        if (centerAreaBox != null)
            centerAreaBox.setDisable(false);

        playerHandBox.getChildren().clear();
        centerAreaBox.getChildren().clear();
        opponentsBox.getChildren().clear();

        Player currentPlayer = game.getCurrentPlayer();

        // --- AZIONI AUTOMATICHE PER L'UMANO (DELAY 2 SECONDI) ---
        if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
            boolean hasPlayable = false;
            Card topDiscard = game.getDiscardPile().getTopCard();
            for (Card c : currentPlayer.getHand().getAllCardsCopy()) {
                if (c.isPlayableOn(topDiscard))
                    hasPlayable = true;
            }
            boolean canStack = false;
            if (game.getPendingDrawPenalty() > 0 && game.getRuleSet().getStackDrawCards()) {
                for (Card c : currentPlayer.getHand().getAllCardsCopy()) {
                    if (c.getType() == CardType.DRAW_TWO || c.getType() == CardType.WILD_DRAW_FOUR)
                        canStack = true;
                }
            }
                        // CASO 0: Challenge del Wild Draw Four
            if (game.getPendingDrawPenalty() >= 4 && topDiscard.getType() == it.uniroma1.mdp.uno.model.card.CardType.WILD_DRAW_FOUR && !currentPlayer.getIsChallenged()) {
                triggerChallengePhase((HumanPlayer) currentPlayer);
                return; // Ferma il caricamento della grafica del turno finche non risponde!
            }
            // CASO 1: L'umano riceve una penalità (+2 o +4) e non può difendersi.
            if (game.getPendingDrawPenalty() > 0 && !canStack) {
                centerAreaBox.setDisable(true);
                playerHandBox.setDisable(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(2));
                pt.setOnFinished(e -> {
                    // Inviamo una lista vuota: il GameEngine capirà che deve applicare la penalità!
                    game.processTurn(currentPlayer, new ArrayList<>());
                    finishHumanTurnAndRefresh(currentPlayer);
                });
                pt.play();
            }
            // CASO 2: L'umano inizia il turno ma ha zero carte giocabili. Pesca da solo!
            else if (game.getPendingDrawPenalty() == 0 && !hasPlayable && !currentPlayer.getHasDrawn()) {
                centerAreaBox.setDisable(true);
                playerHandBox.setDisable(true);
                PauseTransition pt = new PauseTransition(Duration.seconds(2));
                pt.setOnFinished(e -> {
                    game.drawIfNotPlayed(currentPlayer);
                    refreshBoard(); // Si sbloccherà dandogli la possibilità di giocare o passare
                });
                pt.play();
            }
        }

        if (currentPlayer.getPlayerType() == PlayerType.BOT) {

            game.punishUnsafePlayers();

            // Evita che il giocatore umano clicchi cose in preda al panico mentre il bot
            // pensa
            centerAreaBox.setDisable(true);
            playerHandBox.setDisable(true);
            // Timer di 1.5 secondi per dare "l'illusione" che stia pensando e far capire di
            // chi è il turno
            PauseTransition botTimer = new PauseTransition(Duration.seconds(1.5));
            botTimer.setOnFinished(e -> {
                List<Card> botPlay = currentPlayer.playTurn(game.getDiscardPile().getTopCard());
                // Se il bot non ha trovato nulla da giocare, deve pescare
                if (botPlay.isEmpty()) {
                    System.out.println(currentPlayer.getPlayerName() + " non ha carte. Pesca!");
                    Card drawn = game.drawIfNotPlayed(currentPlayer);
                    // Controlla se la carta appena pescata si può giocare come salvataggio in
                    // corner
                    if (drawn != null && drawn.isPlayableOn(game.getDiscardPile().getTopCard())) {
                        System.out.println(
                                "Fortuna! " + currentPlayer.getPlayerName() + " gioca la carta appena pescata.");
                        if (drawn.getType().isWild()) {
                            drawn.setChosenColor(it.uniroma1.mdp.uno.model.card.CardColor.getRandomColor());
                        }
                        botPlay.add(drawn);
                    }
                } else {
                    System.out.println(currentPlayer.getPlayerName() + " ha fatto la sua mossa.");
                }
                // Sblocca la grafica
                centerAreaBox.setDisable(false);
                playerHandBox.setDisable(false);

                // Processa il turno ed evoca la funzione in ricorsione per passare al prossimo
                game.processTurn(currentPlayer, botPlay);
                finishHumanTurnAndRefresh(currentPlayer);
            });
            botTimer.play();
        }

        // check per vedere se c'è qualcuno da punire per la mancata dichiarazione di
        // UNO
        if (currentPlayer.getUnoState() != UNOState.Unsafe && currentPlayer.getPlayerType() == PlayerType.HUMAN) {
            checkAndSpawnCallOutButton();
        }

        // --- BOTTONE PESCA ---
        Button drawPile = new Button("Pesca");
        drawPile.getStyleClass().add("menu-button");
        drawPile.setPrefSize(90, 90);
        drawPile.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 36; -fx-padding: 5");

        if (currentPlayer.getPlayerType() != PlayerType.HUMAN || currentPlayer.getHasDrawn()) {
            drawPile.setDisable(true);
        }

        drawPile.setOnAction(event -> {
            if (!drawPile.isDisabled()) {
                System.out.println("Il giocatore ha pescato una carta");
                HumanPlayer currentHumanPlayer = (HumanPlayer) currentPlayer;
                currentHumanPlayer.drawOnTurn(game);
                refreshBoard();
            }
        });

        // --- BOTTONE GIOCA CARTE ---
        Button playCardsBtn = new Button("Gioca");
        playCardsBtn.getStyleClass().add("menu-button");
        playCardsBtn.setPrefSize(90, 90);
        playCardsBtn.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 36; -fx-padding: 5");

        if (currentPlayer.getPlayerType() != PlayerType.HUMAN) {
            playCardsBtn.setDisable(true);
        } else {

        }

        playCardsBtn.setOnAction(event -> {
            HumanPlayer currentHumanPlayer = (HumanPlayer) currentPlayer;
            if (currentPlayer.getPlayerType() == PlayerType.HUMAN
                    && currentHumanPlayer.getSelectedCardsFromUI().size() > 0) {
                System.out.println("Il giocatore ha deciso di giocare le carte selezionate");
                // 1. Processa la mossa nel Model
                game.processTurn(currentHumanPlayer, currentHumanPlayer.playTurn(game.getDiscardPile().getTopCard()));

                // 2. Controlla quante carte sono rimaste
                int remainingCards = currentHumanPlayer.getHand().getAllCardsCopy().size();

                if (remainingCards == 1) {
                    // 3. Avvia la fase di emergenza (1 secondo), bloccando il passaggio del turno
                    triggerUnoPhase(currentHumanPlayer);
                } else {
                    // 4. Procedi normalmente al turno successivo
                    finishHumanTurnAndRefresh(currentPlayer);
                }
            }
        });
        // --- BOTTONE SALTA TURNO ---
        Button passTurnBtn = new Button("Passa");
        passTurnBtn.getStyleClass().add("menu-button");
        passTurnBtn.setPrefSize(90, 90);
        passTurnBtn.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 36; -fx-padding: 5");

        if (currentPlayer.getPlayerType() != PlayerType.HUMAN) {
            passTurnBtn.setDisable(true);
        }

        passTurnBtn.setOnAction(event -> {
            if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
                System.out.println("Il giocatore ha deciso di saltare il turno");
                List<Card> EmptyList = new ArrayList<>();
                game.processTurn(currentPlayer, EmptyList);
                finishHumanTurnAndRefresh(currentPlayer);
            }
        });

        // --- INFO DI GIOCO (Colore e Verso) ---
        VBox infoBox = new VBox(2); // Spazio tra i testi ridotto
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(2));
        infoBox.setPrefSize(90, 90);
        infoBox.setMaxSize(90, 90);
        infoBox.setMinSize(90, 90);
        infoBox.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-background-radius: 36; -fx-border-color: white; -fx-border-radius: 36; -fx-border-width: 1;");

        String colorName = game.getCurrentColor() != null ? game.getCurrentColor().name() : "N/A";
        String colorHex = "white";
        if (game.getCurrentColor() != null) {
            switch(game.getCurrentColor()) {
                case RED: colorHex = "#ff5555"; break;
                case BLUE: colorHex = "#5555ff"; break;
                case GREEN: colorHex = "#55ff55"; break;
                case YELLOW: colorHex = "#ffff55"; break;
                default: break;
            }
        }
        
        Label colorLabel = new Label("Colore:\n" + colorName);
        colorLabel.setAlignment(Pos.CENTER);
        colorLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        colorLabel.setStyle("-fx-text-fill: " + colorHex + "; -fx-font-size: 11px; -fx-font-weight: bold;");
        
        String dirText = game.getDirection() ? "Orario \u21BB" : "Anti \u21BA";
        Label dirLabel = new Label(dirText);
        dirLabel.setStyle("-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(colorLabel, dirLabel);

        // Layout Centrale
        Button saveBtn = new Button("Salva");
        saveBtn.getStyleClass().add("menu-button");
        saveBtn.setPrefSize(90, 90);
        saveBtn.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 36; -fx-padding: 5; -fx-background-color: #ff9900;");
        saveBtn.setOnAction(e -> {
            it.uniroma1.mdp.uno.save.SaveManager manager = new it.uniroma1.mdp.uno.save.SaveManager();
            if (manager.saveGame(game)) {
                saveBtn.setText("Fatto!");
            }
        });

        Card topDiscard = game.getDiscardPile().getTopCard();
        if (topDiscard != null) {
            CardView discardView = new CardView(topDiscard, true);
            centerAreaBox.getChildren().addAll(drawPile, playCardsBtn, discardView, passTurnBtn, saveBtn, infoBox);
        } else {
            centerAreaBox.getChildren().addAll(drawPile, playCardsBtn, passTurnBtn, saveBtn, infoBox);
        }

        // --- DISEGNA CARTE GIOCATORE ---
        if (currentPlayer != null) {
            for (Card card : currentPlayer.getHand().getAllCardsCopy()) {
                CardView cardView = new CardView(card, currentPlayer.getPlayerType() == PlayerType.HUMAN);

                if (currentPlayer.getHasDrawn() && !cardView.getCard().getDrawn()) {
                    cardView.setDisable(true);
                    cardView.setDisabledEffect(true);
                }

                if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
                    cardView.setOnMouseEntered(e -> cardView.setTranslateY(-15));
                    cardView.setOnMouseExited(e -> cardView.setTranslateY(0));
                } else {
                    cardView.setDisable(true);
                    cardView.setDisabledEffect(true);
                }

                if (!cardView.getCard().isPlayableOn(topDiscard)) {
                    cardView.setDisable(true);
                    cardView.setDisabledEffect(true);
                }

                cardView.setOnMouseClicked(e -> {
                    HumanPlayer currentHumanPlayer = (HumanPlayer) currentPlayer;
                    if (!cardView.isDisabled()) {
                        if (!cardView.getSelected()) {
                            if (currentHumanPlayer.getSelectedCardsFromUI().isEmpty()) {
                                currentHumanPlayer.getSelectedCardsFromUI().add(card);
                                cardView.setSelectedEffect(true);
                            }

                            if (game.getRuleSet().getNumberRush()) {
                                if (cardView.getCard().getType() == CardType.NUMBER
                                        && currentHumanPlayer.isSelectedCardsOnlyNumbers()) {
                                    currentHumanPlayer.getSelectedCardsFromUI().add(card);
                                    cardView.setSelectedEffect(true);
                                }
                            }
                        } else {
                            currentHumanPlayer.getSelectedCardsFromUI().remove(card);
                            cardView.setSelectedEffect(false);
                        }
                    }
                });

                playerHandBox.getChildren().add(cardView);
            }
        }

        // --- DISEGNA AVVERSARI ---
        Label turnInfo = new Label("Turno di: " + (currentPlayer != null ? currentPlayer.getPlayerName() : ""));
        turnInfo.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 22px; -fx-font-weight: bold;");
        opponentsBox.getChildren().add(turnInfo);

        HBox otherPlayersContainer = new HBox(40);
        otherPlayersContainer.setAlignment(Pos.CENTER);

        for (Player p : game.getPlayerList()) {
            if (p == currentPlayer)
                continue;

            VBox opponentBox = new VBox(5);
            opponentBox.setAlignment(Pos.CENTER);

            int cardCount = p.getHand().getAllCardsCopy().size();
            Label opponentName = new Label(p.getPlayerName() + " (" + cardCount + " carte)");
            opponentName.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

            HBox opponentCards = new HBox(-50);
            opponentCards.setAlignment(Pos.CENTER);

            for (Card card : p.getHand().getAllCardsCopy()) {
                CardView backCard = new CardView(card, false);
                backCard.setScaleX(0.6);
                backCard.setScaleY(0.6);
                opponentCards.getChildren().add(backCard);
            }

            opponentBox.getChildren().addAll(opponentName, opponentCards);
            otherPlayersContainer.getChildren().add(opponentBox);
        }

        opponentsBox.getChildren().add(otherPlayersContainer);

        // --- CREAZIONE STORICO IN ALTO A SINISTRA ---
        VBox historyContent = new VBox(3);
        historyContent.setPadding(new Insets(10));
        historyContent.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        
        Label histTitle = new Label(" STORICO MOSSE:");
        histTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        historyContent.getChildren().add(histTitle);

        List<GameAction> actions = game.getGameHistory().getAllActions();
        // Mostriamo TUTTE le mosse dall'inizio della partita
        for (int i = 0; i < actions.size(); i++) {
            Label move = new Label((i + 1) + ". " + actions.get(i).setActionDescription());
            move.setStyle("-fx-text-fill: lightgray; -fx-font-size: 12px;");
            historyContent.getChildren().add(move);
        }

        // Avvolgiamo la VBox in una ScrollPane
        javafx.scene.control.ScrollPane historyScroll = new javafx.scene.control.ScrollPane(historyContent);
        historyScroll.setPrefSize(250, 150); // Fissiamo dimensione
        historyScroll.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        historyScroll.setVbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED);
        historyScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        // Scroll automatico verso il basso ad ogni aggiornamento
        historyScroll.setVvalue(1.0); 

        // Impacchettiamo gli avversari (al centro) e lo storico (a sinistra) in una
        // TopBar
        BorderPane topBar = new BorderPane();
        topBar.setCenter(opponentsBox);
        topBar.setLeft(historyScroll);

        // --- CLASSIFICA IN ALTO A DESTRA (Solo per le partite a punti) ---
        if (game.getGameMode().getPointMatch()) {
            VBox scoreBox = new VBox(3);
            scoreBox.setPadding(new Insets(10));
            scoreBox.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-background-radius: 10;");
            
            Label scoreTitle = new Label(" PUNTI (Goal: " + game.getGameMode().getPointGoal() + "):");
            scoreTitle.setStyle("-fx-text-fill: gold; -fx-font-weight: bold;");
            scoreBox.getChildren().add(scoreTitle);
            
            for (Player p : game.getPlayerList()) {
                Label pScore = new Label("- " + p.getPlayerName() + ": " + p.getTotalScore() + " pt");
                // Mettiamo in grassetto il giocatore a cui tocca
                if (p == currentPlayer) {
                    pScore.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
                } else {
                    pScore.setStyle("-fx-text-fill: lightgray; -fx-font-size: 13px;");
                }
                scoreBox.getChildren().add(pScore);
            }
            
            topBar.setRight(scoreBox);
        }
        
        this.setTop(topBar); // Inserisce la barra in alto nel tavolo verde
    }

    // ==========================================
    // NUOVO: FASE DI CONTESTAZIONE (PUNIZIONE)
    // ==========================================

    private void checkAndSpawnCallOutButton() {
        // Controllo se esiste almeno un giocatore "unsafe"
        boolean isSomeoneUnsafe = false;

        for (Player p : game.getPlayerList()) {
            if (p.getHand().getAllCardsCopy().size() == 1 && p.getUnoState() == UNOState.Unsafe) {
                isSomeoneUnsafe = true;
                break;
            }
        }

        // Se qualcuno è da punire, mostriamo il bottone sulla SINISTRA
        if (isSomeoneUnsafe) {
            Button callOutBtn = new Button("CONTESTA!");
            callOutBtn.setStyle(
                    "-fx-background-color: #ff8800; -fx-text-fill: white; -fx-font-size: 28px; -fx-font-weight: bold; -fx-background-radius: 15; -fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 15; -fx-padding: 15 20; -fx-cursor: hand;");
            callOutBtn.setEffect(new DropShadow(15, Color.BLACK));

            VBox leftSideBox = new VBox(callOutBtn);
            leftSideBox.setAlignment(Pos.CENTER);
            leftSideBox.setPadding(new Insets(0, 0, 0, 50)); // Margine di 50px da sinistra

            this.setLeft(leftSideBox); // Lo piazziamo fisso a sinistra

            PauseTransition pt = new PauseTransition(Duration.seconds(3));

            callOutBtn.setOnAction(e -> {
                pt.stop();
                this.setLeft(null); // Rimuove il bottone
                System.out.println("Qualcuno è stato punito per non aver detto UNO!");

                game.punishUnsafePlayers();

                refreshBoard(); // Aggiorna per mostrare le nuove carte in mano al giocatore punito
            });

            pt.setOnFinished(e -> {
                System.out.println("Finestra di contestazione scaduta.");
                this.setLeft(null); // Rimuove il bottone

                // imposta lo stato di tutti i giocatori a Safe se finisce la finestra di tempo
                // per contestargli la mancata dichiarazione.
                for (Player p : game.getPlayerList()) {
                    game.setSafeUnoState(p);
                }
            });

            pt.play();
        }
    }

    // ==========================================
    // FASE DI EMERGENZA "UNO"
    // ==========================================

    /**
     * Mette in pausa il gioco per 1 secondo, aspettando che il giocatore clicchi il
     * bottone.
     */
    private void triggerUnoPhase(HumanPlayer humanPlayer) {
        // Disabilita temporaneamente i contenitori per evitare che l'utente clicchi
        // altre carte/bottoni
        centerAreaBox.setDisable(true);
        playerHandBox.setDisable(true);

        Button unoBtn = new Button("DICHIARA UNO!!");
        unoBtn.setStyle(
                "-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-size: 32px; -fx-font-weight: bold; -fx-background-radius: 15; -fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 15; -fx-padding: 15 30; -fx-cursor: hand;");
        unoBtn.setEffect(new DropShadow(20, Color.BLACK));

        // Creiamo un box dedicato per centrare il bottone verticalmente sul lato destro
        // dello schermo
        VBox rightSideBox = new VBox(unoBtn);
        rightSideBox.setAlignment(Pos.CENTER);
        rightSideBox.setPadding(new Insets(0, 50, 0, 0)); // Diamo 50px di margine dal bordo destro dello schermo

        // Piazziamo il box nella zona DESTRA (vuota) del BorderPane. Niente coordinate,
        // posizione fissa!
        this.setRight(rightSideBox);

        // Avviamo il timer di emergenza
        PauseTransition pt = new PauseTransition(Duration.seconds(1));

        // Se il giocatore clicca in tempo
        unoBtn.setOnAction(e -> {
            pt.stop(); // Ferma il countdown del timer
            System.out.println("L'UTENTE HA DICHIARATO UNO IN TEMPO!");
            concludeUnoPhase(humanPlayer);
        });

        // Se il timer scade
        pt.setOnFinished(e -> {
            System.out.println("Tempo scaduto! L'utente NON ha dichiarato UNO.");
            // (Model) Segna l'utente come UNSAFE
            game.setUnsafeUnoState(humanPlayer);
            concludeUnoPhase(humanPlayer);
        });

        pt.play();
    }

    /**
     * Rimuove il bottone, sblocca la UI e fa ripartire il normale loop del gioco.
     */
    private void concludeUnoPhase(Player previousPlayer) {
        // Svuotiamo la zona destra del BorderPane, facendo sparire il bottone
        this.setRight(null);

        // Sblocchiamo il tavolo e la mano
        centerAreaBox.setDisable(false);
        playerHandBox.setDisable(false);

        // Aggiorniamo la grafica per il turno successivo
        finishHumanTurnAndRefresh(previousPlayer);
    }

    /**
     * Gestisce la transizione visiva (Hotseat) per evitare che i giocatori umani
     * sbircino le carte l'uno dell'altro scambiandosi il posto al computer.
     */
        private void triggerChallengePhase(HumanPlayer humanPlayer) {
        centerAreaBox.setDisable(true);
        playerHandBox.setDisable(true);

        VBox challengeBox = new VBox(10);
        challengeBox.setAlignment(Pos.CENTER);
        challengeBox.setStyle("-fx-background-color: rgba(0,0,0,0.9); -fx-padding: 20; -fx-background-radius: 15; -fx-border-color: #ff0000; -fx-border-width: 3; -fx-border-radius: 15;");

        Label msg = new Label("Ti hanno tirato un +4!\nVuoi sfidare il giocatore precedente?");
        msg.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-text-alignment: center;");

        Button challengeBtn = new Button("SFIDA!");
        challengeBtn.setStyle("-fx-background-color: #ff0000; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;");
        
        Button acceptBtn = new Button("Accetta (Pesca/Impila)");
        acceptBtn.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");

        challengeBtn.setOnAction(e -> {
            humanPlayer.setIsChallenged(true); // Segna come scelta effettuata
            
            Player prev = game.getPreviousPlayer();
            boolean wasLegal = true;
            for (Card c : prev.getHand().getAllCardsCopy()) {
                // Se aveva il colore attivo PRIMA che giocasse il +4, allora la giocata era illegale
                if (c.getOriginalColor() == game.getPreviousColor() && c.getOriginalColor() != it.uniroma1.mdp.uno.model.card.CardColor.NONE) {
                    wasLegal = false;
                    break;
                }
            }

            if (!wasLegal) {
                System.out.println("Sfida VINTA! " + prev.getPlayerName() + " pesca 4 carte.");
                game.getDeck().drawCardRandom(prev.getHand(), 4);
                // Cancella la penalita per noi
                game.setPendingDrawPenalty(game.getPendingDrawPenalty() - 4);
            } else {
                System.out.println("Sfida PERSA! Peschi 6 carte!");
                game.setPendingDrawPenalty(game.getPendingDrawPenalty() + 2); // 4 normali + 2 di penalita extra
            }
            this.setRight(null); // Chiude il menu
            refreshBoard();
        });

        acceptBtn.setOnAction(e -> {
            humanPlayer.setIsChallenged(true);
            this.setRight(null);
            refreshBoard();
        });

        challengeBox.getChildren().addAll(msg, challengeBtn, acceptBtn);
        this.setRight(challengeBox);
    }

    private void finishHumanTurnAndRefresh(Player previousPlayer) {
        // Se il passaggio è tra DUE giocatori UMANI, oscura il tavolo
        if (previousPlayer != null && previousPlayer.getPlayerType() == PlayerType.HUMAN 
            && game.getCurrentPlayer().getPlayerType() == PlayerType.HUMAN) {
            
            playerHandBox.getChildren().clear();
            centerAreaBox.getChildren().clear();
            
            Label passLabel = new Label("Cambio Turno...");
            passLabel.setStyle("-fx-text-fill: orange; -fx-font-size: 35px; -fx-font-weight: bold;");
            centerAreaBox.getChildren().add(passLabel);

            PauseTransition pt = new PauseTransition(Duration.seconds(2));
            pt.setOnFinished(e -> refreshBoard());
            pt.play();
        } else {
            // Nessuna transizione se c'è di mezzo un bot
            refreshBoard();
        }
    }

    private void showVictoryScreen() {
        // Pulisce tutto il BorderPane
        this.getChildren().clear(); 
        Player winner = null;
        for (Player p : game.getPlayerList()) {
            if (p.getWonRound()) {
                winner = p;
                break;
            }
        }
        VBox victoryBox = new VBox(15);
        victoryBox.setAlignment(Pos.CENTER);
        Label title = new Label("FINE PARTITA!");
        title.setStyle("-fx-text-fill: gold; -fx-font-size: 55px; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, black, 10, 0, 0, 0);");
        Label subtitle = new Label(winner != null ? "Vince: " + winner.getPlayerName() + "!" : "Parità?");
        subtitle.setStyle("-fx-text-fill: white; -fx-font-size: 35px;");
        victoryBox.getChildren().addAll(title, subtitle);
        // Se è una partita a punti, stampiamo la Leaderboard!
        if (game.getGameMode().getPointMatch()) {
            Label scoreTitle = new Label("--- CLASSIFICA FINALE ---");
            scoreTitle.setStyle("-fx-text-fill: yellow; -fx-font-size: 25px; -fx-padding: 20 0 5 0;");
            victoryBox.getChildren().add(scoreTitle);
            
            for (Player p : game.getPlayerList()) {
                Label pScore = new Label(p.getPlayerName() + ": " + p.getTotalScore() + " pt");
                // Il vincitore ha il testo verde, gli altri bianco
                pScore.setStyle("-fx-text-fill: " + (p.getWonRound() ? "lightgreen" : "white") + "; -fx-font-size: 22px;");
                victoryBox.getChildren().add(pScore);
            }
        }
        Button historyBtn = new Button("Visualizza Storico Completo");
        historyBtn.setStyle("-fx-font-size: 18px; -fx-padding: 10 20; -fx-cursor: hand; -fx-background-color: #2196F3; -fx-text-fill: white;");
        historyBtn.setOnAction(e -> {
            VBox historyContent = new VBox(3);
            historyContent.setPadding(new Insets(10));
            historyContent.setStyle("-fx-background-color: #333;");
            
            List<GameAction> actions = game.getGameHistory().getAllActions();
            for (int i = 0; i < actions.size(); i++) {
                Label move = new Label((i + 1) + ". " + actions.get(i).setActionDescription());
                move.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                historyContent.getChildren().add(move);
            }

            javafx.scene.control.ScrollPane historyScroll = new javafx.scene.control.ScrollPane(historyContent);
            historyScroll.setFitToWidth(true);

            javafx.scene.Scene historyScene = new javafx.scene.Scene(historyScroll, 400, 500);
            javafx.stage.Stage historyStage = new javafx.stage.Stage();
            historyStage.setTitle("Storico Mosse della Partita");
            historyStage.setScene(historyScene);
            historyStage.show();
        });

        Button exitBtn = new Button("Chiudi Gioco");
        exitBtn.setStyle("-fx-font-size: 20px; -fx-padding: 10 20; -fx-cursor: hand;");
        exitBtn.setOnAction(e -> {
            System.out.println("Partita Terminata. Chiusura gioco...");
            // Classica mossa da studente: brutale exit(0) invece di tornare al menu principale
            System.exit(0); 
        });
        
        VBox.setMargin(historyBtn, new Insets(30, 0, 0, 0));
        VBox.setMargin(exitBtn, new Insets(10, 0, 0, 0)); // Spazio sopra al bottone
        victoryBox.getChildren().addAll(historyBtn, exitBtn);
        this.setCenter(victoryBox);
    }
}

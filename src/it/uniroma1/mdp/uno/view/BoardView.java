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
        playerHandBox.getChildren().clear();
        centerAreaBox.getChildren().clear();
        opponentsBox.getChildren().clear();

        Player currentPlayer = game.getCurrentPlayer();

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
                refreshBoard();
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
                    refreshBoard();
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
                refreshBoard();
            }
        });

        // Layout Centrale
        Card topDiscard = game.getDiscardPile().getTopCard();
        if (topDiscard != null) {
            CardView discardView = new CardView(topDiscard, true);
            centerAreaBox.getChildren().addAll(drawPile, playCardsBtn, discardView, passTurnBtn);
        } else {
            centerAreaBox.getChildren().addAll(drawPile, playCardsBtn, passTurnBtn);
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
        VBox historyBox = new VBox(3);
        historyBox.setPadding(new Insets(10));
        historyBox.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-background-radius: 10;");
        Label histTitle = new Label(" STORICO MOSSE:");
        histTitle.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        historyBox.getChildren().add(histTitle);
        
        List<GameAction> actions = game.getGameHistory().getAllActions();
        int start = Math.max(0, actions.size() - 5); // Mostra solo le ultime 5 mosse per non ingombrare
        for(int i = start; i < actions.size(); i++) {
            Label move = new Label("- " + actions.get(i).setActionDescription());
            move.setStyle("-fx-text-fill: lightgray; -fx-font-size: 13px;");
            historyBox.getChildren().add(move);
        }
        
        // Impacchettiamo gli avversari (al centro) e lo storico (a sinistra) in una TopBar
        BorderPane topBar = new BorderPane();
        topBar.setCenter(opponentsBox);
        topBar.setLeft(historyBox);
        
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
            concludeUnoPhase();
        });

        // Se il timer scade
        pt.setOnFinished(e -> {
            System.out.println("Tempo scaduto! L'utente NON ha dichiarato UNO.");
            // (Model) Segna l'utente come UNSAFE
            game.setUnsafeUnoState(humanPlayer);
            concludeUnoPhase();
        });

        pt.play();
    }

    /**
     * Rimuove il bottone, sblocca la UI e fa ripartire il normale loop del gioco.
     */
    private void concludeUnoPhase() {
        // Svuotiamo la zona destra del BorderPane, facendo sparire il bottone
        this.setRight(null);

        // Sblocchiamo il tavolo e la mano
        centerAreaBox.setDisable(false);
        playerHandBox.setDisable(false);

        // Aggiorniamo la grafica per il turno successivo
        refreshBoard();
    }
}
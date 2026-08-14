package it.uniroma1.mdp.uno.view;

import it.uniroma1.mdp.uno.model.game.GameEngine;
import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.deck.DiscardPile;
import it.uniroma1.mdp.uno.model.player.HumanPlayer;
import it.uniroma1.mdp.uno.model.player.Player;
import it.uniroma1.mdp.uno.model.player.Player.PlayerType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class BoardView extends BorderPane {

    private final GameEngine game;
    
    // Contenitori principali per le zone del tavolo
    private HBox playerHandBox;
    private HBox centerAreaBox;
    private VBox opponentsBox;

    public BoardView(GameEngine game) {
        this.game = game;
        
        // Mantiene lo stile "Casinò" impostato in MainApp
        getStyleClass().add("menu-bg"); 
        
        setupLayout();
        refreshBoard(); // Disegna lo stato iniziale della partita
    }

    /**
     * Inizializza le aree del BorderPane senza popolarle
     */
    private void setupLayout() {
        // 1. Area del giocatore (In basso)
        playerHandBox = new HBox(-20); // Spaziatura negativa per sovrapporre leggermente le carte a ventaglio
        playerHandBox.setAlignment(Pos.CENTER);
        playerHandBox.setPadding(new Insets(20));
        setBottom(playerHandBox);

        // 2. Area Centrale: Mazzo e Scarti
        centerAreaBox = new HBox(40);
        centerAreaBox.setAlignment(Pos.CENTER);
        setCenter(centerAreaBox);

        // 3. Area Avversari (In alto)
        opponentsBox = new VBox(10);
        opponentsBox.setAlignment(Pos.TOP_CENTER);
        opponentsBox.setPadding(new Insets(20));
        setTop(opponentsBox);
    }

    /**
     * Pulisce e ridisegna il tavolo in base allo stato attuale del GameEngine (Model)
     */
    public void refreshBoard() {
        // Pulisce le viste precedenti
        playerHandBox.getChildren().clear();
        centerAreaBox.getChildren().clear();
        opponentsBox.getChildren().clear();

        // Recuperiamo il giocatore che deve giocare in questo turno
        Player currentPlayer = game.getCurrentPlayer();
        
        // BOTTONE PESCA
        Button drawPile = new Button("Pesca"); 
        drawPile.getStyleClass().add("casino-button");
        drawPile.setPrefSize(90, 90); 
        drawPile.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 36; -fx-padding: 5");
        
        if (currentPlayer.getPlayerType() != PlayerType.HUMAN || currentPlayer.getHasDrawn()) {
            drawPile.setDisable(true);
        }
        
        drawPile.setOnAction(event -> {
            if (drawPile.isDisabled() == false) {
                System.out.println("Il giocatore ha pescato una carta");
                HumanPlayer currentHumanPlayer = (HumanPlayer) currentPlayer;
                currentHumanPlayer.drawOnTurn(game);
                refreshBoard();
            }
        });
        
        // BOTTONE GIOCA CARTE
        Button playCardsBtn = new Button("Gioca");
        playCardsBtn.getStyleClass().add("casino-button");
        playCardsBtn.setPrefSize(90, 90);
        playCardsBtn.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 36; -fx-padding: 5");
        
        // Disabilitato se non è il turno del giocatore umano
        if (currentPlayer.getPlayerType() != PlayerType.HUMAN) {
            playCardsBtn.setDisable(true);
        }
        
        playCardsBtn.setOnAction(event -> {
            if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
                System.out.println("Il giocatore ha deciso di giocare le carte selezionate");
                HumanPlayer currentHumanPlayer = (HumanPlayer) currentPlayer;
                game.processTurn(currentHumanPlayer, currentHumanPlayer.playTurn(game.getDiscardPile().getTopCard()));
                refreshBoard();
            }
        });
        
        // BOTTONE SALTA TURNO
        Button passTurnBtn = new Button("Passa");
        passTurnBtn.getStyleClass().add("casino-button");
        passTurnBtn.setPrefSize(90, 90);
        passTurnBtn.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-background-radius: 36; -fx-padding: 5");
        
        // Di default, disabilitiamo il bottone se non è il turno del giocatore umano.
        // Nel classico UNO, potresti volerlo abilitare SOLO se il giocatore ha già pescato (currentPlayer.getHasDrawn() == true) 
        // ma per ora lo lasciamo cliccabile per il turno umano.
        if (currentPlayer.getPlayerType() != PlayerType.HUMAN) {
            passTurnBtn.setDisable(true);
        }
        
        passTurnBtn.setOnAction(event -> {
            if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
                System.out.println("Il giocatore ha deciso di saltare il turno");
                game.nextTurn();
                refreshBoard();
            }
        });
        
        // Visualizza la prima carta degli scarti e aggiunge i bottoni all'area centrale
        Card topDiscard = game.getDiscardPile().getTopCard();
        if (topDiscard != null) {
            CardView discardView = new CardView(topDiscard, true);
            // Ordine: Pesca -> Gioca -> Carta Scartata -> Passa
            centerAreaBox.getChildren().addAll(drawPile, playCardsBtn, discardView, passTurnBtn);
        } else {
            centerAreaBox.getChildren().addAll(drawPile, playCardsBtn, passTurnBtn);
        }

        // -- DISEGNA IN BASSO SOLO LE CARTE DEL GIOCATORE CORRENTE --
        if (currentPlayer != null) {
            for (Card card : currentPlayer.getHand().getAllCardsCopy()) {
                // Le carte del giocatore di turno sono sempre scoperte (isFaceUp = true) se il giocatore è umano. Se è un bot sono coperte. 
                CardView cardView;
                if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
                    cardView = new CardView(card, true); 
                } else {
                    cardView = new CardView(card, false); 
                }
                
                // Dopo che un giocatore ha pescato una carta, può giocare solo quella carta. Impedisce al giocatore di giocare carte che non siano la carta pescata.
                if (currentPlayer.getHasDrawn() == true) {
                    if (cardView.getCard().getDrawn() != true) {
                        cardView.setDisable(true);
                        cardView.setDisabledEffect(true);
                    }
                }
                
                // Impedisce all'utente di interagire con le carte se è il turno di un bot
                if (currentPlayer.getPlayerType() == PlayerType.HUMAN) {
                    cardView.setOnMouseEntered(e -> cardView.setTranslateY(-15));
                    cardView.setOnMouseExited(e -> cardView.setTranslateY(0));
                } else {
                    cardView.setDisable(true);
                    cardView.setDisabledEffect(true);
                }
                
                // Impedisce all'utente di interagire con le carte che non possono essere giocate sulla carta in cima al discardPile
                if (cardView.getCard().isPlayableOn(topDiscard) != true) {
                    cardView.setDisable(true);
                    cardView.setDisabledEffect(true);
                }               
                
                cardView.setOnMouseClicked(e -> {
                    // Controlla che il giocatore di turno sia umano prima di far valere il click
                    HumanPlayer currentHumanPlayer = (HumanPlayer) currentPlayer;
                    if (cardView.isDisabled() == false) {
                        if (cardView.getSelected() == false) {
                            if (currentHumanPlayer.getSelectedCardsFromUI().size() == 0) {
                                currentHumanPlayer.getSelectedCardsFromUI().add(card);
                                cardView.setSelectedEffect(true);
                            }
                            
                            if (game.getRuleSet().getNumberRush() == true) {
                                if (cardView.getCard().getType() == CardType.NUMBER && currentHumanPlayer.isSelectedCardsOnlyNumbers() == true) {
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
        
        // -- DISEGNA IN ALTO TUTTI GLI ALTRI GIOCATORI IN ATTESA --
        Label turnInfo = new Label("Turno di: " + (currentPlayer != null ? currentPlayer.getPlayerName() : ""));
        turnInfo.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 22px; -fx-font-weight: bold;");
        opponentsBox.getChildren().add(turnInfo);

        HBox otherPlayersContainer = new HBox(40);
        otherPlayersContainer.setAlignment(Pos.CENTER);

        for (Player p : game.getPlayerList()) {
            // Se il giocatore nel ciclo è quello di turno, lo saltiamo perché sta già in basso
            if (p == currentPlayer) continue;

            // VBox per ogni singolo avversario in attesa
            VBox opponentBox = new VBox(5);
            opponentBox.setAlignment(Pos.CENTER);

            // Nome e numero carte
            int cardCount = p.getHand().getAllCardsCopy().size();
            Label opponentName = new Label(p.getPlayerName() + " (" + cardCount + " carte)");
            opponentName.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");

            // HBox per le carte (sovrapposte)
            HBox opponentCards = new HBox(-50); 
            opponentCards.setAlignment(Pos.CENTER);

            for (Card card : p.getHand().getAllCardsCopy()) {
                // Tutti i giocatori in attesa hanno le carte di dorso (isFaceUp = false)
                CardView backCard = new CardView(card, false);
                
                backCard.setScaleX(0.6);
                backCard.setScaleY(0.6);
                
                opponentCards.getChildren().add(backCard);
            }

            opponentBox.getChildren().addAll(opponentName, opponentCards);
            otherPlayersContainer.getChildren().add(opponentBox);
        }
        
        opponentsBox.getChildren().add(otherPlayersContainer);
    }
}
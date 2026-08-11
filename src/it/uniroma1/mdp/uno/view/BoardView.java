package it.uniroma1.mdp.uno.view;

import it.uniroma1.mdp.uno.model.game.GameEngine;
import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.player.Player;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

        // -- DISEGNA IL CENTRO TAVOLO --
        // Bottone per pescare (oppure potresti usare una CardView girata di dorso con un evento click)
        Button drawPile = new Button("PESCA"); 
        drawPile.getStyleClass().add("casino-button");
        drawPile.setPrefSize(80, 120);
        
        // Visualizza la prima carta degli scarti
        Card topDiscard = game.getDiscardPile().getTopCard();
        if (topDiscard != null) {
            CardView discardView = new CardView(topDiscard, true);
            centerAreaBox.getChildren().addAll(drawPile, discardView);
        } else {
            centerAreaBox.getChildren().add(drawPile);
        }

        // -- DISEGNA LA MANO DEL GIOCATORE UMANO SE E' IL SUO TURNO--
        Player current = game.getCurrentPlayer(); 
        if (current != null && current.getPlayerType() == Player.PlayerType.HUMAN) {
            for (Card card : current.getHand().getAllCardsCopy()) {
                CardView cardView = new CardView(card, true);
                
                // Effetto hover per alzare la carta quando ci passi sopra col mouse
                cardView.setOnMouseEntered(e -> cardView.setTranslateY(-15));
                cardView.setOnMouseExited(e -> cardView.setTranslateY(0));
                
                // LOGICA DI SCARTO (Placeholder)
                cardView.setOnMouseClicked(e -> {
                    System.out.println("Hai cliccato la carta: " + card.getOriginalColor() + " " + card.getPointsValue());
                    // Qui andrà richiamato il Controller per giocare la carta
                });
                
                playerHandBox.getChildren().add(cardView);
            }
        }
        
        // -- DISEGNA GLI AVVERSARI (Esempio base) --
        Label botsInfo = new Label("Turno di: " + game.getCurrentPlayer().getPlayerName());
        botsInfo.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        opponentsBox.getChildren().add(botsInfo);
    }
}
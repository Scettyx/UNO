package it.uniroma1.mdp.uno.view;

import it.uniroma1.mdp.uno.model.card.Card; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.net.URL;

public class CardView extends StackPane {

    private final Card card;
    private final boolean isFaceUp;
    
    // Dimensioni standard per le carte
    private static final double CARD_WIDTH = 80;
    private static final double CARD_HEIGHT = 120;

    public CardView(Card card, boolean isFaceUp) {
        this.card = card;
        this.isFaceUp = isFaceUp;
        
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        getStyleClass().add("card-view"); 
        
        render();
    }

    private void render() {
        getChildren().clear();
        
        // Ottiene il percorso corretto del file JPG
        String imagePath = determineImagePath();
        
        URL imageUrl = getClass().getResource(imagePath);
        
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm(), CARD_WIDTH, CARD_HEIGHT, true, true);
            ImageView imageView = new ImageView(image);
            
            getChildren().add(imageView);
        } else {
            System.err.println("ERRORE: Immagine non trovata al percorso -> " + imagePath);
            
        }
    }

    /**
     * Costruisce dinamicamente il nome del file in base allo stato della carta.
     */
    private String determineImagePath() {
        if (!isFaceUp) {
            return "/UNO/src/resources/images/carte_uno/Back.png"; 
        }
        
        // Estrae il nome dell'Enum (assicurati che i metodi getColor() e getValue() combacino con il tuo Model)
        String colorName = card.getOriginalColor().name(); 
        String valueName = Integer.toString(card.getPointsValue()); 
        
        // Restituisce la stringa formattata, es: "/cards/RED_9.png"
        return "/UNO/src/resources/images/carte_uno/" + colorName + "_" + valueName + ".jpg";
    }

    public Card getCard() {
        return card;
    }
}
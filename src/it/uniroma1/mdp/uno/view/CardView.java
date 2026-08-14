package it.uniroma1.mdp.uno.view;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;


import java.net.URL;


public class CardView extends StackPane {

    private final Card card;
    private final boolean isFaceUp;
    private boolean isSelected;
    
    // Dimensioni standard per le carte
    private static final double CARD_WIDTH = 100;
    private static final double CARD_HEIGHT = 140;

    public CardView(Card card, boolean isFaceUp) {
        this.card = card;
        this.isFaceUp = isFaceUp;
        
        setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        getStyleClass().add("card-view"); 
        
        render();
    }
    
    public boolean getSelected() {
    	return isSelected;
    }

    private void render() {
        getChildren().clear();
        
        setEffect(new DropShadow(20, Color.BLACK));
        
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
            return "/resources/images/carte_uno/Back.png"; 
        }
        
        // Estrae il nome dell'Enum
        String colorName = card.getOriginalColor().name(); 
        String valueName = "";
        if(card.getType() == CardType.NUMBER) {
        	valueName = Integer.toString(card.getPointsValue()); 
        } else {
        	valueName = card.getType().name();
        }
        
        // Restituisce la stringa formattata, es: "/images/carte_uno/RED_9.png"
        return "/resources/images/carte_uno/" + colorName + "_" + valueName + ".jpg";
    }

    public void setDisabledEffect(boolean disabled) {
        if (disabled) {
            ColorAdjust darkEffect = new ColorAdjust();
            darkEffect.setBrightness(-0.5); // Oscura l'immagine del 50%
            darkEffect.setSaturation(-0.5); // Desatura leggermente per dare un look "spento"
            this.setEffect(darkEffect);
        } else {
            this.setEffect(null); // Rimuove l'effetto
        }
    }
    
    public void setSelectedEffect(boolean selected) {
        if (selected) {
        	DropShadow shadow = new DropShadow();
            shadow.setColor(Color.YELLOW); // Colore dell'alone
            shadow.setRadius(15);         // Diffusione dell'alone
            shadow.setSpread(0.3);        // Intensità dell'alone
            this.setEffect(shadow);
            this.isSelected = true;
        } else {
            this.setEffect(null);
            this.isSelected = false; // Rimuove l'effetto
        }
    }
    
    
    public Card getCard() {
        return card;
    }
}
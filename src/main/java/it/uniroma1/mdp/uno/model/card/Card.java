package it.uniroma1.mdp.uno.model.card;

import java.util.Objects;

/**
 * @author Cosmin Florea    (M.2241398)
 */

public abstract class Card {
    private final CardType type;
    private final CardColor originalColor;
    private CardColor chosenColor;          // Colore da giocare dopo
    
    protected Card(CardType type, CardColor color) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(color);
        this.type = type;
        this.originalColor = color;
        this.chosenColor = null;
    }

    public CardType getType() { return type; }
    public CardColor getOriginalColor() { return originalColor; }

    // Da verificare
    public CardColor getActiveColor() {
        if (type.isWild() && chosenColor != null) {
            return chosenColor;
        } return originalColor;
    }

    public void setChosenColor(CardColor color) {
        if (!type.isWild()) {
            // Check sui cambio colore da finire
        }
        // Check sul colore da fare
        this.chosenColor = color;
    }
}
package it.uniroma1.mdp.uno.model.card;

import java.util.Objects;

/**
 * @author Cosmin Florea    (M.2241398)
 * @author Massimo Giorgini (M.2234123)
 */

public abstract class Card {
    protected final CardType type;
    protected final CardColor originalColor;
    private CardColor chosenColor;

    protected Card(CardType type, CardColor color) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(color);
        this.type = type;
        this.originalColor = color;
        this.chosenColor = null;
    }

    /**
     * Ritorna il tipo di una carta
     */
    public CardType getType() { return type; }

    /**
     * Ritorna il colore di una carta
     */
    public CardColor getOriginalColor() { return originalColor; }

    /**
     * Ritorna il colore attivo in gioco
     */
    public CardColor getActiveColor() {
        if (type.isWild() && chosenColor != null) {
            return chosenColor;
        } return null;
    }

    /**
     * Il metodo definisce il colore della prossima carta dopo una Wild
     */
    public void setChosenColor(CardColor color) {
        if (!type.isWild()) {
            throw new IllegalArgumentException("Solo le carte normali hanno un colore");
        }
        if (color == null || !color.isRealColor()) {
            throw new IllegalArgumentException("Va scelto un colore");
        }
        this.chosenColor = color;
    }

    /**
     * Annulla il colore per piazzare una carta Wild
     */
    public void resetChosenColor() {
        if (type.isWild()) { this.chosenColor = null; }
    }

    public abstract int getPointsValue();    // Restituirà il valore delle carte rimaste in mano alla fine

    /**
     * Verifica se si può giocare la carta scelta paragonandola 
     * alla carta messa in precedenza
     */
    public boolean isPlayableOn (Card topCard) {
        Objects.requireNonNull(topCard);
    	if (this.type.isWild()) { return true; }                                                // I vincoli non li stabiliamo qui
        if (this.originalColor == topCard.getActiveColor()) { return true; }                    // Stesso colore
        if (this.type == topCard.getType() && !topCard.getType().isWild()) { return true; }     // Stesso tipo, speciali inclusi
        return false;
    }

    @Override
    public String toString() {
        return type.name() + "-" + originalColor.name();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card other)) return false;
        return this.type == other.type && this.originalColor == other.originalColor;
    }

    // Non fare domande qua so solo che serve con equals 
    @Override
    public int hashCode() {
        return Objects.hash(type, originalColor);
    }
}
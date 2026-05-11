package it.uniroma1.mdp.uno.model.card;

import java.util.Objects;

/**
 * @author Cosmin Florea    (M.2241398)
 * @author Massimo Giorgini (M.2234123)
 */

public abstract class Card {
    private final CardType type;
    private final CardColor color;
    private final CardNumber number;
    private final int value;
    
    protected Card(CardType type, CardColor color, CardNumber number, int value) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(color);
        this.type = type;
        this.color = color;
        this.number = number;
        this.value = value;
    }
    /**
     * Ritorna il tipo di una carta
     */
    public CardType getType() { return type; }
    /**
     * Ritorna il colore di una carta
     */
    public CardColor getColor() { return color; }
    
    /**
     * Ritorna il valore di una carta
     */
    public int getValue() {return value;}
    
    // fratm ma a che serve sto metodo ?
    public CardColor getActiveColor() {
        if (type.isWild() && color != null) {
            return color;
        } return null;
    }
      
    /**
     * Verifica se si può giocare la carta scelta paragonandola 
     * alla carta messa in precedenza
     */
    public boolean canPlayOn (Card previousCard) {
    	if (color == previousCard.color ||
    		type.isWild() ||
    		number.getNumber() == previousCard.number.getNumber()) { return true;
    	} return false;
    }
}
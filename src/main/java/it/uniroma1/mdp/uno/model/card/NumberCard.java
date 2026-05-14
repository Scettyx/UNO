package it.uniroma1.mdp.uno.model.card;

import java.util.Objects;

/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
 * 
 *         Rappresenta le carte colorate numerate
 */
public class NumberCard extends Card {
    private final int value; // Massimo questi sono i tuoi amatissimi numeri finalmente ciao

    /**
     * Costruisce la carta azione
     * 
     * @param value tra 1 e 9
     * @param color non null o NONE
     * @throws IllegalArgumentException se la carta non ha colore o numero valido
     */
    public NumberCard(CardColor color, int value) {
        super(CardType.NUMBER, color);
        if (!color.isRealColor()) {
            throw new IllegalArgumentException("La carta deve avere un colore");
        }
        if (value > 9 || value < 0) {
            throw new IllegalArgumentException("La carta deve avere un vero numero");
        }
        this.value = value;
    }

    /**
     * Ritorna il valore della carta
     * 
     * @return intero tra 1 e 9
     */
    public int getValue() {
        return value;
    }

    /**
     * Ritorna il valore della carta in punti
     * 
     * @return intero tra 1 e 9
     */
    @Override
    public int getPointsValue() {
        return value;
    }

    /**
     * Verifica se la carta è giocabile sopra l'ultima appena giocata
     * 
     * @param topCard carta appena giocata/scartata
     * @return {@code true} se la carta può essere giocata
     */
    @Override
    public boolean isPlayableOn(Card topCard) {
        Objects.requireNonNull(topCard);
        if (this.getOriginalColor() == topCard.getActiveColor()) {
            return true;
        } // Stesso colore
        if (topCard instanceof NumberCard other && this.value == other.value) {
            return true;
        } // Stesso numero
        return false;
    }

    /**
     * @return stringa descrittiva della carta
     */
    @Override
    public String toString() {
        return "Num" + value + "-" + getOriginalColor().name();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj))
            return false;
        if (!(obj instanceof NumberCard other))
            return false;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
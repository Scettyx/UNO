package it.uniroma1.mdp.uno.model.card;

import java.util.Objects;

/**
 * @author Cosmin Florea (M.2241398)
 * @author Massimo Giorgini (M.2234123)
 * 
 *         Classe astratta per una generica carta
 *         Una volta definiti colore e tipo della carta, restano invariati
 *         definitivamente
 */
public abstract class Card {
    protected final CardType type;
    protected final CardColor originalColor;
    private CardColor chosenColor;

    /**
     * Costruttore utilizzabile solo dalle sottoclassi
     * 
     * @param type  tipo della carta non null
     * @param color colore della carta non null
     * @throws IllegalArgumentException se imposta tipo o colore null, le carte wild
     *                                  hanno colore NONE
     */
    protected Card(CardType type, CardColor color) {
        Objects.requireNonNull(type, "Il tipo della carta non puo essere null");
        Objects.requireNonNull(color, "Il colore della carta non puo essere null");
        this.type = type;
        this.originalColor = color;
        this.chosenColor = null;
    }

    /**
     * Ritorna il tipo della carta
     * 
     * @return il {@link CardType} della carta
     */
    public CardType getType() {
        return type;
    }

    /**
     * @return il colore della carta
     */
    public CardColor getOriginalColor() {
        return originalColor;
    }

    /**
     * @return il colore da giocare al turno successivo
     */
    public CardColor getActiveColor() {
        if (type.isWild() && chosenColor != null) {
            return chosenColor;
        }
        return null;
    }

    /**
     * Imposta il nuovo colore dopo una carta WILD o WILD_DRAW_FOUR
     * 
     * @param color che deve essere di uno dei quattro colori attivabili
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
     * Reimposta il colore di una carta di tipo WILD a null dopo esser stata giocata
     * necessaria in caso di ripescaggio dalle carte giocate
     */
    public void resetChosenColor() {
        if (type.isWild()) {
            this.chosenColor = null;
        }
    }

    /**
     * Metodo astratto per ritornare i punti delle carte rimaste in mano al termine
     * della partita
     * 
     * @return i punti delle carte rimaste in mano
     */
    public abstract int getPointsValue();

    /**
     * Verifica se la carta è giocabile sopra l'ultima appena giocata
     * 
     * @param topCard carta appena giocata/scartata
     * @return {@code true} se la carta può essere giocata
     */
    public boolean isPlayableOn(Card topCard) {
        Objects.requireNonNull(topCard);
        if (this.type.isWild()) {
            return true;
        } // I vincoli non li stabiliamo qui
        if (this.originalColor == topCard.getActiveColor()) {
            return true;
        } // Stesso colore
        if (this.type == topCard.getType() && !topCard.getType().isWild()) {
            return true;
        } // Stesso tipo, speciali inclusi
        return false;
    }

    /**
     * @return stringa descrittiva della carta
     */
    @Override
    public String toString() {
        return type.name() + "-" + originalColor.name();
    }

    /**
     * Confronto logico di due carte
     * 
     * @return {@code true} se le carte hanno lo stesso tipo e colore
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof Card other))
            return false;
        return this.type == other.type && this.originalColor == other.originalColor;
    }

    // Non fare domande qua so solo che serve con equals
    @Override
    public int hashCode() {
        return Objects.hash(type, originalColor);
    }
}
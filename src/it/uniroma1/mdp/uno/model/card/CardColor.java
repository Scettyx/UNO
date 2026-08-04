package it.uniroma1.mdp.uno.model.card;

/**
 * @author Cosmin Florea (M.2241398)
 * 
 *         Enumerazione per ogni tipo di colore delle carte
 */
public enum CardColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    NONE; // Per le carte speciali

    /**
     * Verifica che la carta abbia un colore diverso da NONE
     * 
     * @return {@code true} se RED, BLUE, GREEN o YELLOW
     */
    public boolean isRealColor() {
        return this != NONE;
    }
}
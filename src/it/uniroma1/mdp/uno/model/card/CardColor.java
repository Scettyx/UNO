package it.uniroma1.mdp.uno.model.card;

import java.util.Random;

/**
 * Enumerazione per ogni tipo di colore delle carte
 * 
 * @author Cosmin Florea (M.2241398)
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

    /**
     * Restituisce un colore scelto casualmente
     * 
     * @return un CardColor casuale
     */
    public static CardColor getRandomColor() {
        CardColor[] validColors = {
            RED,
            BLUE,
            GREEN,
            YELLOW
        };
        return validColors[new Random().nextInt(validColors.length)];
    }
}
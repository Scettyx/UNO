package it.uniroma1.mdp.uno.model.card;

/**
 * @author Cosmin Florea (M.2241398)
 * 
 *         Enumerazione per ogni tipo di carta
 */
public enum CardType {
    NUMBER,
    SKIP,
    REVERSE,
    DRAW_TWO,
    WILD,
    WILD_DRAW_FOUR;

    /**
     * Verifica se è una carta Wild
     * 
     * @return {@code true} se è WILD o WILD_DRAW_FOUR
     */
    public boolean isWild() {
        return this == WILD || this == WILD_DRAW_FOUR;
    }

    /**
     * Verifica se è una carta speciale colorata
     * 
     * @return {@code true} se è SKIP, REVERSE o DRAW_TWO
     */
    public boolean isColoredAction() {
        return this == SKIP || this == REVERSE || this == DRAW_TWO;
    }
}
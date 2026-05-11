package it.uniroma1.mdp.uno.model.card;

/**
 * @author Cosmin Florea    (M.2241398)
 */

public enum CardType {
    NUMBER,
    SKIP,
    REVERSE,
    DRAW_TWO,
    WILD,
    WILD_DRAW_FOUR;

    public boolean isWild() {
        return this == WILD || this == WILD_DRAW_FOUR;
    }

    public boolean isColoredAction() {
        return this == SKIP || this == REVERSE || this == DRAW_TWO;
    }
}
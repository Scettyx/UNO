package it.uniroma1.mdp.uno.model.card;

/**
 * @author Cosmin Florea    (M.2241398)
 */

/**
 * Enum per definire i colori delle carte
 */
public enum CardColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    NONE;       // Per le carte speciali

    // Metodo per le carte colorate (da aggiornare con javadoc)
    public boolean isRealColor() {
        return this != NONE;
    }
}
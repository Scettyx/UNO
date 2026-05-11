package it.uniroma1.mdp.uno.model.card;

import java.util.Objects;

/**
 * @author Cosmin Florea    (M.2241398)
 */

public class ActionCard extends Card {
    public static final int POINTS_VALUE = 20;

	public ActionCard(CardType type, CardColor color) {
		super(type, color);
		if (!type.isColoredAction()) {
			throw new IllegalArgumentException("La carta deve essere Reverse, DrawTwo o Skip");
		}
		if (!color.isRealColor()) {
			throw new IllegalArgumentException("La carta deve avere un colore");
		}
	}

	@Override
	public int getPointsValue() { return POINTS_VALUE; }

	@Override
	public boolean isPlayableOn (Card topCard) {
        Objects.requireNonNull(topCard);
        if (this.getOriginalColor() == topCard.getActiveColor()) { return true; }   // Stesso colore
        if (this.getType() == topCard.getType()) { return true; }                   // Stesso tipo
        return false;
    }

	@Override
    public String toString() {
        return getType().name() + "-" + getOriginalColor().name();
    }
}
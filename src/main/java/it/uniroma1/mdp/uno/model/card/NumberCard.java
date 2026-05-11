package it.uniroma1.mdp.uno.model.card;

import java.util.Objects;

/**
 * @author Massimo Giorgini	(M.2234123)
 * @author Cosmin Florea	(M.2241398)
 */

public class NumberCard extends Card {
	private final int value;	// Massimo questi sono i tuoi amatissimi numeri finalmente ciao

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

	public int getValue() { return value; }

	@Override
	public int getPointsValue() { return value; }

	@Override
	public boolean isPlayableOn (Card topCard) {
        Objects.requireNonNull(topCard);
        if (this.getOriginalColor() == topCard.getActiveColor()) { return true; }               // Stesso colore
        if (topCard instanceof NumberCard other && this.value == other.value) { return true; }	// Stesso numero
        return false;
    }

	@Override
    public String toString() {
        return "Num" + value + "-" + getOriginalColor().name();
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof NumberCard other)) return false;
        return this.value == other.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea	(M.2241398)
 */

public class WildCard extends Card{
	public static final int POINTS_VALUE = 50;	// Massimo assicurati che i punti sono giusti che io ho messo gli stessi tua

	public WildCard(CardType type) {
		super(type, CardColor.NONE);
		if (!type.isWild()) {
			throw new IllegalArgumentException("La carta deve essere Wild o WildFour");
		}
	}

	public boolean isWildFour() { return getType() == CardType.WILD_DRAW_FOUR; }

	@Override
	public int getPointsValue() { return POINTS_VALUE; }

	@Override
	public boolean isPlayableOn (Card topCard) { return true; }		// Da verificare se ci sono eccezioni

	@Override
    public String toString() {				// Me la devo rivedere
		String wildType = getType().name();
		CardColor activeColor = getActiveColor();
        if (activeColor.isRealColor()) {
			return wildType + ":" + activeColor;
		}
		return wildType;
    }
}
package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
 * 
 *         Rappresenta le carte WILD e WILD_DRAW_FOUR
 */
public class WildCard extends Card {
	public static final int POINTS_VALUE = 50; // Massimo assicurati che i punti sono giusti che io ho messo gli stessi
												// tua

	/**
	 * Costruisce la carta
	 * 
	 * @param type tra WILD e WILD_DRAW_FOUR
	 */
	public WildCard(CardType type) {
		super(type, CardColor.NONE);
		if (!type.isWild()) {
			throw new IllegalArgumentException("La carta deve essere Wild o WildFour");
		}
	}

	/**
	 * Verifica se la carta è di tipo WILD_DRAW_FOUR
	 * 
	 * @return {@code true} se è di tipo WILD_DRAW_FOUR
	 */
	public boolean isWildFour() {
		return getType() == CardType.WILD_DRAW_FOUR;
	}

	/**
	 * Ritorna sempre 50pt per questo tipo
	 * 
	 * @return 50
	 */
	@Override
	public int getPointsValue() {
		return POINTS_VALUE;
	}

	/**
	 * Verifica se la carta è giocabile sopra l'ultima appena giocata
	 * 
	 * @param topCard carta appena giocata/scartata
	 * @return sempre {@code true} (Da verificare se ci sono eccezioni)
	 */
	@Override
	public boolean isPlayableOn(Card topCard) {
		return true;
	}

	/**
	 * Ritorna tip e nuovo colore scelto
	 * 
	 * @return stringa descrittiva della carta
	 */
	@Override
	public String toString() { // Da rivedere
		String wildType = getType().name();
		CardColor activeColor = getActiveColor();
		if (activeColor.isRealColor()) {
			return wildType + ":" + activeColor;
		}
		return wildType;
	}
}
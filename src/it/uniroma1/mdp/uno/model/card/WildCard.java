package it.uniroma1.mdp.uno.model.card;

/**
 * Rappresenta le carte WILD e WILD_DRAW_FOUR
 * 
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
 */
public class WildCard extends Card {
	public static final int POINTS_VALUE = 50; // Massimo assicurati che i punti sono giusti che io ho messo gli stessi tua
	public boolean canCauseChallenge = false;
												

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
		if (this.getType() == CardType.WILD_DRAW_FOUR) {
			this.canCauseChallenge = true;
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
	 * Questo metodo serve a stabilire qualora questa carta, quando in cima alla discardPile, possa permettere a il giocatore che inizia il turno di lanciare la sfida
	 * del Wild Draw Four al giocatore precedente. Quando il valore di canCauseChallenge è "false" è perchè questa carta è già stata usata per lanciare una sfida.
	 * @param false se la carta non può lanciare una sfida, true se può farlo.
	 */
	public void setCanCauseChallenge(boolean value) {
		this.canCauseChallenge = value;
	}
	
	public boolean getCanCauseChallenge() {
		return this.canCauseChallenge;
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
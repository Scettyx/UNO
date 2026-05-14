package it.uniroma1.mdp.uno.model.card;

import java.util.Objects;

/**
 * @author Cosmin Florea (M.2241398)
 * 
 *         Rappresenta le carte colorate non numerate
 */
public class ActionCard extends Card {
	public static final int POINTS_VALUE = 20;

	/**
	 * Costruisce la carta azione
	 * 
	 * @param type  tra SKIP, REVERSE o DRAW_TWO
	 * @param color non null
	 * @throws IllegalArgumentException se la carta non è speciale o senza colore
	 */
	public ActionCard(CardType type, CardColor color) {
		super(type, color);
		if (!type.isColoredAction()) {
			throw new IllegalArgumentException("La carta deve essere Reverse, DrawTwo o Skip");
		}
		if (!color.isRealColor()) {
			throw new IllegalArgumentException("La carta deve avere un colore");
		}
	}

	/**
	 * Ritorna sempre 20pt per questo tipo
	 * 
	 * @return 20
	 */
	@Override
	public int getPointsValue() {
		return POINTS_VALUE;
	}

	/**
	 * Verifica se la carta è giocabile sopra l'ultima appena giocata
	 * 
	 * @param topCard carta appena giocata/scartata
	 * @return {@code true} se la carta può essere giocata
	 */
	@Override
	public boolean isPlayableOn(Card topCard) {
		Objects.requireNonNull(topCard);
		if (this.getOriginalColor() == topCard.getActiveColor()) {
			return true;
		} // Stesso colore
		if (this.getType() == topCard.getType()) {
			return true;
		} // Stesso tipo
		return false;
	}

	/**
	 * @return stringa descrittiva della carta
	 */
	@Override
	public String toString() {
		return getType().name() + "-" + getOriginalColor().name();
	}
}
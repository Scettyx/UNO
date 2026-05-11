package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class WildDrawFourCard extends Card {

	protected WildDrawFourCard(CardType type, CardColor color, CardNumber number, int value) {
		super(type.WILD_DRAW_FOUR, null, number.NONE, value);
	
	}

}

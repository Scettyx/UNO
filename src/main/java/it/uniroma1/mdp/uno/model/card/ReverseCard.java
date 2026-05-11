package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class ReverseCard extends Card {

	protected ReverseCard(CardType type, CardColor color, CardNumber number, int value) {
		super(type.REVERSE, color, number.NONE, 20);
	}

}

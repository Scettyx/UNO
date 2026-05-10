package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class ReverseCard extends Card {

	protected ReverseCard(CardType type, CardColor color, int number, int value) {
		super(type.REVERSE, color, number, 20);
	}

}

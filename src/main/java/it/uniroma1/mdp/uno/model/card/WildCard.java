package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class WildCard extends Card{

	protected WildCard(CardType type, CardColor color, CardNumber number, int value) {
		super(type.WILD, null, number.NONE, 50);
	}

}

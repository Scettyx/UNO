package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class WildCard extends Card{

	protected WildCard(CardType type, CardColor color, int number) {
		super(type.WILD, null, number, 50);
	}

}

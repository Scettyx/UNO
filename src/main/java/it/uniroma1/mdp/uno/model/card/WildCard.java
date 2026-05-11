package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class WildCard extends Card{

	public WildCard(CardColor color) {
		super(CardType.WILD, null, CardNumber.NONE, 50);
	}

}

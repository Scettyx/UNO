package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class SkipCard extends Card {

	protected SkipCard(CardType type, CardColor color, CardNumber number, int value) {
		super(type.SKIP, color, number.NONE, 20);
		
	}

}

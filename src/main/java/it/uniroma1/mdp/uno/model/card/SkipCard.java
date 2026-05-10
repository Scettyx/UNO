package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class SkipCard extends Card {

	protected SkipCard(CardType type, CardColor color, int number) {
		super(type.SKIP, color, number, 20);
		
	}

}

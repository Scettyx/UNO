package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class DrawTwoCard extends Card {

	protected DrawTwoCard(CardType type, CardColor color, CardNumber number, int value) {
		super(type.DRAW_TWO, color, number.NONE, 20);
		
	}

}

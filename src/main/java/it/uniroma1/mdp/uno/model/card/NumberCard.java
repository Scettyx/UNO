package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini		(M.2234123)
 */

public class NumberCard extends Card{

	protected NumberCard(CardType type, CardColor color, CardNumber number, int value) {
		super(type.NUMBER, color, number, number.getNumber() );
	}

}

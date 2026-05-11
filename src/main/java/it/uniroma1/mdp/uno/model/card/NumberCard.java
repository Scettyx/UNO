package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini		(M.2234123)
 */

public class NumberCard extends Card{

	public NumberCard(CardColor color, CardNumber number) {
		super(CardType.NUMBER, color, number, number.getNumber() );
	}

}

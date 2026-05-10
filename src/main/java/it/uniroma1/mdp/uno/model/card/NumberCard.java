package it.uniroma1.mdp.uno.model.card;

/**
 * @author Massimo Giorgini		(M.2234123)
 */

public class NumberCard extends Card{


	protected NumberCard(CardType type, CardColor color, int number, int value) {
		super(type.NUMBER, color, number, number);

	}
	
	/**
	 * ritorna il valore di una carta numerica
	 * 
	 */

}

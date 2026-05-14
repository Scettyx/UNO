package it.uniroma1.mdp.uno.model.deck;

import it.uniroma1.mdp.uno.model.card.Card;

/**
 * @author Massimo Giorgini (M.2234123)
 * 
 */
public class Hand extends CardCollection {
	public Hand() {

	}

	public boolean addCardToHand(Card card) {
		return cardList.add(card);
	}

	/**
	 * Rimuove una carta dal mazzo all'indice dato, e la ritorna.
	 */
	public Card getCardAtIndex(int index) {
		try {
			return this.cardList.remove(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

}
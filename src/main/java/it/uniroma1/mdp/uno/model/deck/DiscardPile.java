package it.uniroma1.mdp.uno.model.deck;

import it.uniroma1.mdp.uno.model.card.Card;

/**
 * @author Massimo Giorgini	(M.2234123)
 * 
 */
public class DiscardPile extends CardCollection {
	public DiscardPile() {
		// super, non serve il costruttore qua
	}

	/**
	 * Ritorna la carta in cima alla collezione di carte
	 */
	public Card getTopCard() {
		if (this.isEmpty() == false) {
			return cardList.get(cardList.size() - 1);
		}
		return null;
	}

	/**
	 * Aggiunge una carta in cima alla discard pile
	 */
	public boolean addCard(Card card) {
		return cardList.add(card);
	}
}
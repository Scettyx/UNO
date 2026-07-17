package it.uniroma1.mdp.uno.model.deck;

import it.uniroma1.mdp.uno.model.card.Card;

/**
 * @author Massimo Giorgini (M.2234123)
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
	 * sposta tutte le carte della DiscardPile, quella in cima, nel Deck e poi lo
	 * mischia, da usare quando finiscono le carte nel deck.
	 * 
	 * @param deck il deck in cui spostare le carte
	 */
	public void moveToDeck(Deck deck) {
		for (int i = 0; i < this.getNumCards(); i++) {
			deck.cardList.add(cardList.get(i));
		}
		cardList.clear();
		deck.shuffleDeck();
	}

	/**
	 * Aggiunge una carta in cima alla discard pile
	 */
	public boolean addCard(Card card) {
		return cardList.add(card);
	}
}
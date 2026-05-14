package it.uniroma1.mdp.uno.model.deck;

import java.util.ArrayList;

import it.uniroma1.mdp.uno.model.card.Card;

/**
 * @author Massimo Giorgini (M.2234123)
 * 
 *         Una classe astratta che rappresenta un gruppo di carte qualsiasi;
 *         sarà usata per il mazzo di carte totali, la mano dei giocatori e le
 *         carte sul tavolo.
 */
public abstract class CardCollection {
	protected ArrayList<Card> cardList;

	public CardCollection() {
		this.cardList = new ArrayList<Card>();
	}

	public int getNumCards() {
		return cardList.size();
	}

	public boolean isEmpty() {
		if (cardList.size() == 0) {
			return true;
		}
		return false;
	}
}
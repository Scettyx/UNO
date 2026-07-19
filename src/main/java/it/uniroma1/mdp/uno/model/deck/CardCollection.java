package it.uniroma1.mdp.uno.model.deck;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.mdp.uno.model.card.Card;

/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
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

	/**
	 * Ritorna il numero di carte all'interno della collezione
	 * 
	 * @return
	 */
	public int getNumCards() {
		return cardList.size();
	}

	/**
	 * Ritorna True se la collezione è vuota
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		if (cardList.size() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return Ritorna la lista delle carte carte
	 */
	public List<Card> getAllCards() {
		return this.cardList;
	}
	/**
	 * Ritorna una copia delle carte, utile per la GUI
	 * 
	 * @return
	 */
	public List<Card> getAllCardsCopy() {
		return new ArrayList<>(cardList);
	}

	/**
	 * Ritorna la carta all'indice specificato, utile per la GUI
	 * 
	 * @param index della carta
	 * @return la carta all'indice specificato o null se l'indice è fuori dai limiti
	 */
	public Card peekCardAtIndex(int index) {
		try {
			return cardList.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}
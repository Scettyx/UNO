package it.uniroma1.mdp.uno.model.deck;

import java.util.ArrayList;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardNumber;
import it.uniroma1.mdp.uno.model.card.DrawTwoCard;
import it.uniroma1.mdp.uno.model.card.NumberCard;
import it.uniroma1.mdp.uno.model.card.ReverseCard;
import it.uniroma1.mdp.uno.model.card.SkipCard;


/**
 * @author Massimo Giorgini (M.2234123)
 */
public class Deck extends CardCollection {
	
	public final static int LENGTH = 108;
	
	public Deck(ArrayList<Card> cardList) {
		this.cardList = cardList;
		createDeck();
	}
	
	/**
	 * Rimuove una carta dal mazzo all'indice dato, e la ritorna. Forse questa da spostare alla classe del mazzo del giocatore
	 */
	public Card getCard(int index) {
		try {
			return this.cardList.remove(index);
		} catch(IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	/**
	 * Crea il mazzo e aggiunge tutte e 108 le carte di UNO al suo interno
	 */
	public void createDeck() {
		CardColor[] colors = CardColor.values();
		for (int i = 0; i < colors.length-1; i++) {
			cardList.add(new NumberCard(CardColor.values()[i], CardNumber.ZERO));
			cardList.add(new SkipCard(CardColor.values()[i]));
			cardList.add(new SkipCard(CardColor.values()[i]));
			cardList.add(new ReverseCard(CardColor.values()[i]));
			cardList.add(new ReverseCard(CardColor.values()[i]));
			cardList.add(new DrawTwoCard(CardColor.values()[i]));
			cardList.add(new DrawTwoCard(CardColor.values()[i]));
			for (int j = 0; j < 9; j++) {
				cardList.add(new NumberCard(CardColor.values()[i], CardNumber.values()[j]));
				cardList.add(new NumberCard(CardColor.values()[i], CardNumber.values()[j]));
			}
		}
	}

}

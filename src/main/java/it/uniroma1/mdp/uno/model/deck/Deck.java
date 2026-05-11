package it.uniroma1.mdp.uno.model.deck;

import java.util.ArrayList;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.card.ActionCard;
import it.uniroma1.mdp.uno.model.card.WildCard;
import it.uniroma1.mdp.uno.model.card.NumberCard;


/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea 	(M.2241398)
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
		// Ciclo per carte colorate
		for (CardColor color : CardColor.values()) {
			if (!color.isRealColor()) continue;

			cardList.add(new NumberCard(color, 0));

			// Da vedere quante doppioni triploni etc per carta
			for (int i = 0; i <= 9; i++) {
				cardList.add(new NumberCard(color, i));
				cardList.add(new NumberCard(color, i));
			}

			for (int j = 0; j < 2; j++) {
				cardList.add(new ActionCard(CardType.DRAW_TWO, color));
				cardList.add(new ActionCard(CardType.REVERSE, color));
				cardList.add(new ActionCard(CardType.SKIP, color));
			}
		}

		for (int k = 0; k < 2; k++) {
			cardList.add(new WildCard(CardType.WILD));
			cardList.add(new WildCard(CardType.WILD_DRAW_FOUR));
		}
	}
}
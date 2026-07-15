package it.uniroma1.mdp.uno.model.deck;

import it.uniroma1.mdp.uno.model.card.*;

import java.util.Random;
import java.util.ArrayList;

/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
 * 
 * Rappresenta il mazzo principale di Carte sul tavolo, da cui verranno pescate le carte per formare i mazzi 
 */
public class Deck extends CardCollection {
	public final static int LENGTH = 108;

	public Deck() {
		createDeck();
		shuffleDeck();
	}

	/**
	 * Ritorna la carta in cima alla collezione di carte dalla posizione i dalla cima
	 * @param i la posizione dalla cima da cui prendere la carta (i = 1 è la carta in cima al mazzo)
	 */
	public Card getTopCard(int i ) {
		if (this.isEmpty() == false) {
			return cardList.get(cardList.size() - i);
		}
		return null;
	}

	/**
	 * mette in una CardCollection c la carta in posizione i dalla cima 
	 * 
	 * @param c la collezione di carte in cui mettere l acarta 
	 * @param i la posizione dalla cima del deck da cui prendere la carta da deck per metterla in c (i = 1 è la carta in cima)
	 */
	public Card drawFromTopCard(CardCollection c, int i) {
		if (this.isEmpty() == false) {
			Card temp = cardList.get(cardList.size() - i + 1);
			c.cardList.add(cardList.get(cardList.size() - i + 1));
			cardList.remove(cardList.size() - i + 1);
			return temp;
		}
		return null;
	}

	/**
	 * Dal deck pesca un numero scelto di carte e lo aggiunge ad
	 * una collezione di carte
	 * 
	 * @param c la collezione a cui aggiungere le carte
	 * @param cardAmount il numero di carte scelte casualmente da aggiungere alla collezione c
	 */
	public void drawCardRandom(CardCollection c, int cardAmount) {
		Random r = new Random();
		for (int i = 0; i < cardAmount; i++) {
			int cardToWithdraw = r.nextInt(cardList.size());
			c.cardList.add(cardList.get(cardToWithdraw));
			cardList.remove(cardToWithdraw);
		}
	}

	/**
	 * Crea il mazzo e aggiunge tutte e 108 le carte di UNO al suo interno
	 */
	public void createDeck() {
		// Ciclo per carte colorate
		for (CardColor color : CardColor.values()) {
			if (!color.isRealColor())
				continue;

			cardList.add(new NumberCard(color, 0));

			for (int i = 1; i <= 9; i++) {
				cardList.add(new NumberCard(color, i));
				cardList.add(new NumberCard(color, i));
			}

			for (int j = 0; j < 2; j++) {
				cardList.add(new ActionCard(CardType.DRAW_TWO, color));
				cardList.add(new ActionCard(CardType.REVERSE, color));
				cardList.add(new ActionCard(CardType.SKIP, color));
			}
		}

		for (int k = 0; k < 4; k++) {
			cardList.add(new WildCard(CardType.WILD));
			cardList.add(new WildCard(CardType.WILD_DRAW_FOUR));
		}
	}

	/**
	 * mescola il mazzo
	 */
	public void shuffleDeck() {
		java.util.Collections.shuffle(cardList);
	}
}
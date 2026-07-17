package it.uniroma1.mdp.uno.model.game;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.deck.Deck;
import it.uniroma1.mdp.uno.model.deck.DiscardPile;
import it.uniroma1.mdp.uno.model.player.Player;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class GameEngine {

	/**
	 * SINGLE: Partita singola, senza punti, con un round solo. Vince chi rimane con
	 * 0 carte.
	 * POINTS: Partita a punti, con più round; vince il primo giocatore che
	 * raggiunge la soglia di punteggio.
	 */
	public enum Mode {
		SINGLE, POINTS
	}

	private Mode gameMode;
	private CardColor currentColor;
	private int currentPlayer;
	private Player[] playerList;
	private Deck deck;
	private DiscardPile discardPile;

	/**
	 * Verso in cui scorrono i turni; true se orario, false se antiorario
	 */
	boolean direction;

	public GameEngine(Player[] plist, Mode gameMode) {
		this.gameMode = gameMode;
		currentPlayer = 0;
		playerList = plist;
		deck = new Deck();
		discardPile = new DiscardPile();
		direction = true;
		currentColor = null;

		/**
		 * distribuisce le 7 carte iniziali ad ogni giocatore.
		 */
		for (int i = 0; i < plist.length; i++) {
			deck.drawCardRandom(plist[i].getHand(), 7);
		}
	}

	/**
	 * metodo per mettere la prima carta del deck nel discardPile
	 */
	public void start(GameEngine game) {
		int i = 1; // stesso discorso, non ha senso cominciare da 0?
		while (discardPile.isEmpty()) {
			if (deck.getTopCard(i).getType() == CardType.NUMBER) {
				deck.drawFromTopCard(discardPile, i);
			}
			i++;
		}
	}

	/**
	 * questo metodo manda avanti i turni
	 */
	public void nextTurn() {
		if (direction) {
			// verso orario
			currentPlayer = (currentPlayer + 1) % playerList.length;
		} else {
			// verso antiorario
			currentPlayer = (currentPlayer - 1 + playerList.length) % playerList.length;
		}
	}

	// TODO:
	// -mettere controllo di penalità per chi non dichiara UNO
	// -aggiungere il sistema di conteggio dei punti alla fine di un round
	// -Implementare da qualche parte La challenge e i requisiti del Wild Draw Four
	// -aggiungere le condizioni di vittoria in base alla modalità scelta (quindi
	// aggiungere anche modalità)

	/**
	 * Questo metodo da inizio a un round della partita
	 */
	public void playRound() {
		boolean roundOver = false;
		while (!roundOver) {
			Player current = playerList[currentPlayer];
			Card playedCard = current.playTurn(discardPile.getTopCard());
			// pesca una carta dal deck se il giocatore non ha giocato nessuna carta nel suo
			// turno.
			if (playedCard == null) {
				deck.drawCardRandom(current.getHand(), 1);
			}
			// se il giocatore ha giocato una carta, viene scartata e messa in cima alla
			// discardPile.
			else {
				discardPile.addCard(playedCard);
				currentColor = playedCard.getActiveColor();

				// effetti legati a carte speciali
				switch (playedCard.getType()) {
					case REVERSE:
						direction = !direction;
						break;
					case SKIP:
						nextTurn();
						break;
					case DRAW_TWO:
						nextTurn();
						deck.drawCardRandom(current.getHand(), 2);
						break;
					case WILD: // il giocatore dovrà scegliere il colore attivo
						currentColor = playedCard.getActiveColor();
						break;
					case WILD_DRAW_FOUR:
						nextTurn();
						currentColor = playedCard.getActiveColor();
						deck.drawCardRandom(current.getHand(), 4);
						break;
					case NUMBER:
					default:
						break;
				}
			}
			// condizioni di fine round
			if (current.getHand().isEmpty()) {
				roundOver = true;
				break;
			}
			// Se il deck da cui si pescano le carte rimane vuoto, questo metodo sposta
			// tutte le carte dalla discardPile al deck (eccetto quella in cima).
			if (deck.isEmpty()) {
				discardPile.moveToDeck(deck);
			}
		}
		nextTurn();
	}
}

package it.uniroma1.mdp.uno.model.game;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.deck.Deck;
import it.uniroma1.mdp.uno.model.deck.DiscardPile;
import it.uniroma1.mdp.uno.model.player.Player;

/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
 */

public class GameEngine {

	public enum Mode {
		SINGLE, POINTS
	}

	private Mode gameMode;
	private CardColor currentColor;
	private int currentPlayer;
	private Player[] playerList;
	private Deck deck;
	private DiscardPile discardPile;
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
	 * Ritorna il mazzo principale della partita.
	 * 
	 * @return il Deck
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Ritorna il mazzo degli scarti.
	 * 
	 * @return la DiscardPile
	 */
	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	/**
	 * Ritorna l'array contenente tutti i giocatori della partita.
	 * 
	 * @return array di Player
	 */
	public Player[] getPlayerList() {
		return playerList;
	}

	/**
	 * Ritorna l'indice del giocatore che sta giocando in questo turno.
	 * 
	 * @return l'indice del giocatore corrente
	 */
	public int getCurrentPLayerIndex() {
		return currentPlayer;
	}

	/**
	 * Ritorna il verso del turno.
	 * 
	 * @return true se orario, false se antiorario
	 */
	public boolean getDirection() {
		return direction;
	}

	/**
	 * Ritorna il colore attivo in questo momento sul tavolo.
	 * 
	 * @return il CardColor corrente
	 */
	public CardColor getCurrentColor() {
		return currentColor;
	}

	/**
	 * Ritorna il numero totale di giocatori.
	 * 
	 * @return il numero di giocatori
	 */
	public int getPlayerCount() {
		return playerList.length;
	}

	/**
	 * Imposta il nuovo colore attivo sul tavolo.
	 * 
	 * @param color il nuovo colore
	 */
	public void setCurrentColor(CardColor color) {
		this.currentColor = color;
	}

	/**
	 * Imposta l'indice del giocatore corrente.
	 * 
	 * @param index l'indice del giocatore
	 */
	public void setCurrentPlayerIndex(int index) {
		this.currentPlayer = index;
	}

	/**
	 * Imposta il verso del turno.
	 * 
	 * @param direction true per orario, false per antiorario
	 */
	public void setDirection(boolean direction) {
		this.direction = direction;
	}

	/**
	 * Metodo per mettere la prima carta del deck nel discardPile.
	 */
	public void start(GameEngine game) {
		int i = 1; // Stesso discorso, non ha senso cominciare da 0?
		while (discardPile.isEmpty()) {
			if (deck.getTopCard(i).getType() == CardType.NUMBER) {
				deck.drawFromTopCard(discardPile, i);
			}
			i++;
		}
	}

	/**
	 * Manda avanti i turni
	 */
	public void nextTurn() {
		if (direction) {
			// Verso orario
			currentPlayer = (currentPlayer + 1) % playerList.length;
		} else {
			// Verso antiorario.
			currentPlayer = (currentPlayer - 1 + playerList.length) % playerList.length;
		}
	}

	// TODO:
	// - Mettere controllo di penalità per chi non dichiara UNO
	// - Aggiungere il sistema di conteggio dei punti alla fine di un round
	// - Implementare da qualche parte La challenge e i requisiti del Wild Draw Four
	// - Aggiungere le condizioni di vittoria in base alla modalità scelta (quindi
	// aggiungere anche modalità)

	/**
	 * Da inizio a un round della partita
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

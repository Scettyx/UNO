package it.uniroma1.mdp.uno.model.game;

import java.util.List;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.deck.Deck;
import it.uniroma1.mdp.uno.model.deck.DiscardPile;
import it.uniroma1.mdp.uno.model.player.HumanPlayer;
import it.uniroma1.mdp.uno.model.player.Player;
import it.uniroma1.mdp.uno.model.player.Player.PlayerType;
import it.uniroma1.mdp.uno.model.rules.RuleSet;

/**
 * 
 * 
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
 */

public class GameEngine {

	private GameMode gameMode;
	private RuleSet ruleSet;
	private CardColor currentColor;
	private int currentPlayer;
	private Player[] playerList;
	private Deck deck;
	private DiscardPile discardPile;
	private int pendingDrawPenalty = 0;
	boolean direction;

	public GameEngine(Player[] plist, GameMode gameMode, RuleSet ruleSet) {
		this.ruleSet = ruleSet;
		this.gameMode = gameMode;
		currentPlayer = 0;
		playerList = plist;
		deck = new Deck();
		discardPile = new DiscardPile();
		direction = true;
		currentColor = null;

	}

	public RuleSet getRuleSet() {
		return ruleSet;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	/**
	 * Ritorna il Deck.
	 * 
	 * @return il Deck
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Ritorna la DiscardPile.
	 * 
	 * @return la DiscardPile
	 */
	public DiscardPile getDiscardPile() {
		return discardPile;
	}

	/**
	 * Ritorna l'array con tutti i giocatori della partita.
	 * 
	 * @return array di Player
	 */
	public Player[] getPlayerList() {
		return playerList;
	}

	/**
	 * Ritorna l'indice del giocatore corrente.
	 * 
	 * @return l'indice del giocatore corrente
	 */
	public int getCurrentPlayerIndex() {
		return currentPlayer;
	}

	/**
	 * Ritorna il giocatore corrente.
	 * 
	 * @return il giocatore corrente
	 */
	public Player getCurrentPlayer() {
		return getPlayerList()[currentPlayer];
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
	 * Ritorna il colore attivo in questo momento.
	 * 
	 * @return il CardColor corrente
	 */
	public CardColor getCurrentColor() {
		return currentColor;
	}

	/**
	 * Ritorna il numero totale di giocatori.
	 * 
	 * @return il numero totale di giocatori
	 */
	public int getPlayerCount() {
		return playerList.length;
	}

	/**
	 * Imposta il nuovo colore attivo.
	 * 
	 * @param color il nuovo colore attivo
	 */
	public void setCurrentColor(CardColor color) {
		this.currentColor = color;
	}

	/**
	 * Imposta l'indice del giocatore.
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
	 * Questo metodo sposta il turno avanti.
	 */
	public void nextTurn() {
		// Se il giocatore ha pescato una carte nel turno precedente, gli viene rimosso
		// lo status che ha pescato.
		playerList[currentPlayer].setHasDrawn(false);
		if (playerList[currentPlayer].getPlayerType() == PlayerType.HUMAN) {
			HumanPlayer currentHumanPlayer = (HumanPlayer) playerList[currentPlayer];
			currentHumanPlayer.getSelectedCardsFromUI().clear();
		}
		for (Card i : playerList[currentPlayer].getHand().getAllCards()) {
			i.setDrawn(false);
		}
		if (direction) {
			// Verso orario
			currentPlayer = (currentPlayer + 1) % playerList.length;
		} else {
			// Verso antiorario.
			currentPlayer = (currentPlayer - 1 + playerList.length) % playerList.length;
		}
	}

	/**
	 * Questo metodo sposta il turno indietro.
	 */
	public void previousTurn() {
		if (direction) {
			// Verso orario
			currentPlayer = (currentPlayer - 1 + playerList.length) % playerList.length;
		} else {
			// Verso antiorario.
			currentPlayer = (currentPlayer + 1) % playerList.length;
		}
	}

	/**
	 * Assegna il punteggio al vincitore del round e aggiorna il suo punteggio
	 * totale.
	 * 
	 * @param winner il giocatore che ha vinto il round
	 */
	public void addPointsToWinner(Player winner) {
		for (Player i : playerList) {
			winner.setCurrentRoundScore(i.getHand().getHandScore());
		}
		winner.setTotalScore(winner.getCurrentRoundScore());
		winner.resetCurrentRoundScore();
	}

	/**
	 * La logica della challenge del Wild Draw Four; se il giocatore non poteva
	 * giocare la carta, pesca quattro carte. Se poteva giocare la carta, lo
	 * sfidante pesca 6 carte.
	 * 
	 * @param playedCard è la carta sulla cima del discardPile
	 * @param current    è il giocatore che ha giocato la carta WildDrawFour
	 */
	public void WildDrawFourChallenge(Card playedCard, Player current) {
		if (!current.WildDrawFourLegal(playedCard)) {
			deck.drawCardRandom(getPlayerList()[currentPlayer].getHand(), 4);
			return;
		}
		nextTurn();
		deck.drawCardRandom(getPlayerList()[currentPlayer].getHand(), 6);
		return;
	}

	/**
	 * Pesca una carta se il giocatore non ne ha giocata una in questo turno e gli
	 * permette di giocare la carta che ha appena pescato se è giocabile
	 * 
	 * @param current
	 * @param playedCard
	 */
	public Card drawIfNotPlayed(Player current) {
		if (current.getHasDrawn() == false) {
			current.setHasDrawn(true);
			return deck.drawFromTopCard(current.getHand(), 0);
		}
		return null;
	}

	/**
	 * Controlla lo stato della dichiarazione di UNO del giocatore.
	 * 
	 * @param current
	 */
	public void checkUnoDeclaration(Player current) {
		if (current.getHand().getNumCards() == 1 && current.getUnoState() != Player.UNOState.Called) {
			current.setUnoState(Player.UNOState.Unsafe, deck, current); // aggiungi poi un modo per cambiare l'UNOState
																		// a Called quando un altro giocatore richiama.
		}
	}

	/**
	 * le condizioni di vittoria di un ROUND in base alla modalità
	 * 
	 * @param current
	 * @param roundOver
	 */
	public void roundWinConditions(Player current) {
		current.setWon(true);
		addPointsToWinner(current);
		if (gameMode.getPointMatch() == true) {
			current.setWon(false);
			gameMode.setGameOver(true);
		} else if (gameMode.getPointMatch() == false) {
			for (Player player : getPlayerList()) {
				player.resetScore();
			}
		}
	}

	/**
	 * Se la modalità è a punti, controlla se alla fine di un round c'è un giocatore
	 * che ha raggiunto la soglia di vittoria; se non c'è, gioca un nuovo round
	 */
	public void gameWinConditions() {
		boolean winnerPresent = false;
		for (Player player : getPlayerList()) {
			if (player.getTotalScore() >= gameMode.getPointGoal()) {
				player.setWon(true);
				winnerPresent = true;
				gameMode.setGameOver(true);
			}
		}
		if (winnerPresent == false) {
			initializeRound();
		}
	}

	/**
	 * Metodo per mettere la prima carta del deck nel discardPile.
	 */
	public void firstDiscardCard() {
		int i = 0;
		while (discardPile.isEmpty()) {
			if (deck.getTopCard(i).getType() == CardType.NUMBER) {
				deck.drawFromTopCard(discardPile, i);
			}
			i++;
		}
	}

	/**
	 * Distribuisce le 7 carte iniziali ad ogni giocatore, rimescola il mazzo,
	 * toglie tutte le carte dalla discardPile.
	 */
	public void distributeCards() {
		this.deck = new Deck();
		for (Player player : getPlayerList()) {
			player.getHand().deleteAllCards();
		}
		discardPile.deleteAllCards();
		for (int i = 0; i < getPlayerList().length; i++) {
			deck.drawCardRandom(getPlayerList()[i].getHand(), 7);
		}
	}

	public void initializeRound() {
		distributeCards();
		firstDiscardCard();
	}

	/**
	 * Gestisce la logica dei Round nella partita.
	 */
	public void processTurn(Player current, List<Card> playedCards) {
		if (ruleSet.getStackDrawCards() && pendingDrawPenalty > 0) {
			boolean stacked = false;

			if (playedCards.size() > 0) {
				Card firstPlayed = playedCards.get(0);

				if (firstPlayed.getType() == CardType.DRAW_TWO || firstPlayed.getType() == CardType.WILD_DRAW_FOUR) {
					stacked = true;
				}
			}

			if (!stacked) {
				deck.drawCardRandom(current.getHand(), pendingDrawPenalty);
				pendingDrawPenalty = 0;

				if (playedCards.isEmpty()) {
					nextTurn();
					return;
				}
			}
		}

		if (playedCards.size() != 0) {
			for (Card playedCard : playedCards) {
				current.getHand().playCard(playedCard, discardPile);
				currentColor = playedCard.getActiveColor();

				// meccaniche dichiarazione UNO
				checkUnoDeclaration(current);

				discardPile.addCard(playedCard);
				current.getHand().getAllCards().remove(playedCard);

				// effetti legati a carte speciali
				switch (playedCard.getType()) {
					case REVERSE:
						direction = !direction;
						break;

					case SKIP:
						nextTurn();
						break;

					case DRAW_TWO:
						if (ruleSet.getStackDrawCards()) {
							pendingDrawPenalty = +2;
						} else {
							nextTurn();
							deck.drawCardRandom(getPlayerList()[currentPlayer].getHand(), 2);
						}
						break;

					case WILD:
						// Si prende un colore casuale se non si sceglie
						if (playedCard.getActiveColor() == CardColor.NONE || playedCard.getActiveColor() == null) {
							playedCard.setChosenColor(CardColor.getRandomColor());
						}
						currentColor = playedCard.getActiveColor(); // implementa che il giocatore dovrà scegliere il
																	// colore attivo
						break;

					case WILD_DRAW_FOUR:
						if (current.getIsChallenged()) { // controlla se il giocatore è stato sfidato dopo il
															// lancio del Wild Draw Four
							WildDrawFourChallenge(playedCard, current);
						} else { // se il giocatore non è stato sfidato dopo un Wild Draw Four, il prossimo
									// giocatore pesca le 4 carte normalmente.
							if (ruleSet.getStackDrawCards()) {
								pendingDrawPenalty = +4;
							} else {
								nextTurn();
								deck.drawCardRandom(getPlayerList()[currentPlayer].getHand(), 4);
							}
						}

						// Si prende un colore casuale se non si sceglie
						if (playedCard.getActiveColor() == CardColor.NONE || playedCard.getActiveColor() == null) {
							playedCard.setChosenColor(CardColor.getRandomColor());
						}
						currentColor = playedCard.getActiveColor(); // implementa che il giocatore dovrà scegliere il
																	// colore attivo
						break;

					case NUMBER:
						break;

					default:
						break;
				}
			}
		}

		// Se il deck da cui si pescano le carte rimane vuoto, questo metodo sposta
		// tutte le carte dalla discardPile al deck (eccetto quella in cima).
		if (deck.isEmpty()) {
			discardPile.moveToDeck(deck);
		}

		// condizioni di fine round. Se la partita è a round singolo invece che a punti,
		// il gioco può anche finire quì se un giocatore ha un mazzo vuoto.
		if (current.getHand().isEmpty()) {
			roundWinConditions(current);
			// se la partita è a punti, il gioco non finisce nel singolo round ma controlla
			// se è necessario giocarne uno nuovo
			if (gameMode.getPointMatch() == true) {
				gameWinConditions();
			}
			return;
		}

		nextTurn();
	}
}

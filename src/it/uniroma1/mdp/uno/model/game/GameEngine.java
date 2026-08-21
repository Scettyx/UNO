package it.uniroma1.mdp.uno.model.game;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.card.NumberCard;
import it.uniroma1.mdp.uno.model.deck.Deck;
import it.uniroma1.mdp.uno.model.deck.DiscardPile;
import it.uniroma1.mdp.uno.model.player.HumanPlayer;
import it.uniroma1.mdp.uno.model.player.Player;
import it.uniroma1.mdp.uno.model.player.Player.PlayerType;
import it.uniroma1.mdp.uno.model.player.Player.UNOState;
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
	private GameHistory gameHistory;
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
		this.gameHistory = new GameHistory();
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
	 * Ritorna il numero di carte da pescare con la regola dello stacking
	 * 
	 * @return il numero di carte da pescare
	 */
	public int getPendingDrawPenalty() {
		return pendingDrawPenalty;
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
	 * Getter per il GameHystory
	 * 
	 * @return il GameHistory
	 */
	public GameHistory getGameHistory() {
		return this.gameHistory;
	}

	public void contestUno(Player target) {
		if (target.getUnoState() == Player.UNOState.Unsafe) {
			target.setUnoState(Player.UNOState.Called, deck, target);
			GameAction contestAction = new GameAction("System", "UNO_CONTEST");
			contestAction.setChallenge(true, true);
			gameHistory.addGameAction(contestAction);
		}
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
			Card drawnCard = deck.drawFromTopCard(current.getHand(), 0);
			GameAction drawAction = new GameAction(current.getPlayerName(), "DRAW");
			gameHistory.addGameAction(drawAction);
			return drawnCard;
		}
		return null;
	}

	/**
	 * Imposta lo stato della dichiarazione UNO a Unsafe (nel BoardView).
	 * 
	 */
	public void setUnsafeUnoState(Player player) {
		player.setUnoState(Player.UNOState.Unsafe, deck, player);
	}

	/**
	 * Imposta lo stato della dichiarazione UNO a Safe (nel BoardView).
	 * 
	 */
	public void setSafeUnoState(Player player) {
		player.setUnoState(Player.UNOState.Safe, deck, player);
	}

	/**
	 * Imposta lo stato della dichiarazione UNO a Called (nel BoardView).
	 * 
	 */
	public void setCalledUnoState(Player player) {
		player.setUnoState(Player.UNOState.Called, deck, player);
	}

	/**
	 * Metodo per far pescare le carte ai giocatori a cui è stata contestata la
	 * mancata dichiarazione di UNO.
	 */
	public void punishUnsafePlayers() {
		for (Player p : playerList) {
			if (p.getUnoState() == Player.UNOState.Unsafe) {
				setCalledUnoState(p);
			}
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

		if (pendingDrawPenalty > 0) {
			boolean stacked = false;

			if (playedCards.size() > 0) {
				Card firstPlayed = playedCards.getFirst();

				if (firstPlayed.getType() == CardType.DRAW_TWO || firstPlayed.getType() == CardType.WILD_DRAW_FOUR) {
					stacked = true;
				}
			}

			if (!stacked) {
				deck.drawCardRandom(current.getHand(), pendingDrawPenalty);
				GameAction penaltyAction = new GameAction(current.getPlayerName(), "DRAW_PENALTY");
				gameHistory.addGameAction(penaltyAction);
				pendingDrawPenalty = 0;

				for (Card invalidCard : playedCards) {
					current.getHand().addCardToHand(invalidCard);
				}
				nextTurn();
				return;
			}
		}

		if (playedCards.size() != 0) {
			GameAction playAction = new GameAction(current.getPlayerName(), "PLAY");

			if (ruleSet.getNumberRush() && current.getPlayerType() == PlayerType.BOT && playedCards.size() == 1) {
				Card firstCard = playedCards.getFirst();

				if (firstCard.getType() == CardType.NUMBER) {
					NumberCard numCard = (NumberCard) firstCard;
					List<Card> handCards = new ArrayList<>(current.getHand().getAllCards());

					for (Card c : handCards) {

						if (c != firstCard && c.getType() == CardType.NUMBER) {
							NumberCard otherNum = (NumberCard) c;

							if (otherNum.getValue() == numCard.getValue()) {
								playedCards.add(otherNum);
							}
						}
					}
				}
			}

			for (Card playedCard : playedCards) {
				current.getHand().playCard(playedCard, discardPile);
				currentColor = playedCard.getActiveColor();
				playAction.addCardInvolved(playedCard);

				// meccaniche dichiarazione UNO
				if (current.getUnoState() == Player.UNOState.Safe && current.getHand().getNumCards() == 1) {
					playAction.setUnoCalled(true);
				}

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
							pendingDrawPenalty += 2;
							break;
						}

					case WILD:
						// Si prende un colore casuale se non si sceglie
						if (playedCard.getActiveColor() == CardColor.NONE || playedCard.getActiveColor() == null) {
							playedCard.setChosenColor(CardColor.getRandomColor());
						}
						currentColor = playedCard.getActiveColor(); // implementa che il giocatore dovrà scegliere il
																	// colore attivo
						break;

					case WILD_DRAW_FOUR:
						// controlla se il giocatore è stato sfidato dopo il lancio del Wild Draw Four
						if (current.getIsChallenged()) {
							WildDrawFourChallenge(playedCard, current);
						} else {
							pendingDrawPenalty += 4;
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

			gameHistory.addGameAction(playAction);
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

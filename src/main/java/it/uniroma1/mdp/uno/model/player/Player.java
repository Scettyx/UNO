package it.uniroma1.mdp.uno.model.player;

import it.uniroma1.mdp.uno.model.deck.Hand;

/**
 * @author Massimo Giorgini (M.2234123)
 * 
 */
public abstract class Player {
	private final String playerName;
	private final int playerID;
	private int totalScore;
	private int currentRoundScore;
	private boolean wonRound;

	public enum PlayerType {
		HUMAN, BOT
	}

	private final PlayerType playerType;

	/**
	 * SAFE: Al giocatore non può essere contestata la mancata dichiarazione di UNO
	 * CALLED: Il giocatore ha dichiarato UNO prima di finire il turno con una carta
	 * sola nel mazzo
	 * UNSAFE: Al giocatore può essere contestata la mancata dichiarazione di UNO
	 */
	public enum UNOState {
		Safe, Called, Unsafe
	}

	private UNOState unoState;

	private final Hand hand;

	public Player(String playerName, int playerID, PlayerType playerType) {
		this.playerName = playerName;
		this.playerID = playerID;
		this.playerType = playerType;
		this.hand = new Hand();
		totalScore = currentRoundScore = 0;
		wonRound = false;
		unoState = UNOState.Safe;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getPlayerID() {
		return playerID;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public int getCurrentRoundScore() {
		return currentRoundScore;
	}

	public boolean getWonRound() {
		return wonRound;
	}

	public PlayerType getPlayerType() {
		return playerType;
	}

	public UNOState getUnoState() {
		return unoState;
	}

	public Hand getHand() {
		return hand;
	}

	public void setWon() {
		wonRound = true;
	}

	public void setUnoState(UNOState newUnoState) {
		unoState = newUnoState;
	}

	public void resetScore() {
		totalScore = 0;
		currentRoundScore = 0;
		wonRound = false;
		unoState = UNOState.Safe;
	}

}
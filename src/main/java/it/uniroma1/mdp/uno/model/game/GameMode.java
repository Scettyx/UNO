package it.uniroma1.mdp.uno.model.game;
/**
 * Gestisce qualora, in una partita, si usi la modalità a partita singola oppure la modalità a partita con punti, e permette in tal caso
 * di configurare il limite di punti per vincere.
 * @author Massimo Giorgini (M.2234123)
 */
public class GameMode {
	private boolean pointMatch;
	private int pointGoal;
	private boolean gameOver;

	
	public GameMode(boolean pointMatch, int pointGoal) {
		this.pointMatch = pointMatch;
		this.pointGoal = pointGoal;
		this.gameOver = false;
	}
	
	public GameMode(boolean pointMatch) {
		this.pointMatch = pointMatch;
		this.pointGoal = 500;
		this.gameOver = false;
	}
	
	/**
	 * 
	 * @return true se la modalità della partita è a punti, false se è partita singola
 	 */
	public boolean getPointMatch() {
		return pointMatch;
	}
	
	/**
	 * 
	 * @return la soglia di punti totali a cui un giocatore deve arrivare per vincere
	 */
	public int getPointGoal() {
		return pointGoal;
	}
	
	/**
	 * permette di settare la soglia di punti a un valore messo in input
	 * @param value il valore a cui settare la nuova soglia di punti
	 */
	public void setPointGoal(int value) {
		pointGoal = value;
	}
	
	public boolean getGameOver() {
		return gameOver;
	}
	
	public void setGameOver(boolean value) {
		gameOver = value;
	}
}

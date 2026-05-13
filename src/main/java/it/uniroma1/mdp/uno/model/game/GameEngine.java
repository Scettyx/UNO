package it.uniroma1.mdp.uno.model.game;

import it.uniroma1.mdp.uno.model.deck.Deck;
import it.uniroma1.mdp.uno.model.deck.DiscardPile;
import it.uniroma1.mdp.uno.model.player.Player;

/**
 * @author Massimo Giorgini (M.2234123)
 */

public class GameEngine {
	
	/**
	 * SINGLE: Partita singola, senza punti, con un round solo. Vince chi rimane con 0 carte.
	 * POINTS: Partita a punti, con più round; vince il primo giocatore che raggiunge la soglia di punteggio.
	 */
	public enum Mode {SINGLE, POINTS} 
	private Mode gameMode;
	
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
		
		/**
		 * distribuisce le 7 carte iniziali ad ogni giocatore. 
		 */
		for (int i = 0; i < plist.length; i++) {
			deck.drawCard(plist[i].getHand(), 7);
		}
	}
	
	//aggiungi metodo per mettere la prima carta del mazzo sul discardPile
	public void start (GameEngine game) {
		
	}
	
}

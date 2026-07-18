package it.uniroma1.mdp.uno.model.player;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.deck.Hand;

/**
 * Rappresenta un giocatore generico (Umano o Bot) all'interno della partita.
 * Gestisce il punteggio, la mano di carte e lo stato relativo alla
 * dichiarazione di "UNO".
 * 
 * @author Massimo Giorgini (M.2234123)
 */
public abstract class Player {

	private final String playerName;
	private final int playerID;
	private int totalScore;
	private int currentRoundScore;
	private boolean wonRound;

	/**
	 * Definisce se il giocatore è umano o controllato dal computer.
	 */
	public enum PlayerType {
		HUMAN, BOT
	}

	/** Il tipo di giocatore (umano o bot) */
	private final PlayerType playerType;

	public enum UNOState {
		Safe, Called, Unsafe
	}

	/** Lo stato corrente della dichiarazione "UNO" per il giocatore */
	private UNOState unoState;

	/** La mano contenente le carte attualmente possedute dal giocatore */
	private final Hand hand;

	/**
	 * Costruisce un nuovo giocatore inizializzando il suo stato e assegnandogli
	 * una mano di carte vuota.
	 * 
	 * @param playerName del giocatore
	 * @param playerID   del giocatore
	 * @param playerType il tipo di giocatore se umano o bot
	 */
	public Player(String playerName, int playerID, PlayerType playerType) {
		this.playerName = playerName;
		this.playerID = playerID;
		this.playerType = playerType;
		this.hand = new Hand();
		totalScore = currentRoundScore = 0;
		wonRound = false;
		unoState = UNOState.Safe;
	}

	/**
	 * Il giocatore prende una carta se è il suo turno.
	 * 
	 * @param card la carta da aggiungere alla mano
	 * @return true se la carta è stata aggiunta con successo
	 */
	public boolean takeCard(Card card) {
		return getHand().addCardToHand(card);
	}

	/**
	 * Fa giocare al giocatore una specifica carta dalla sua mano.
	 * 
	 * @param i indice della carta della mano
	 * @return la carta che viene giocata (e rimossa dalla mano)
	 * @throws IllegalArgumentException se l'indice fornito non è valido
	 */
	public Card playCard(int i) {
		if (hand.getNumCards() <= i) {
			throw new IllegalArgumentException("Indice della carta non valido.");
		}
		return getHand().getCardAtIndex(i);
	}

	/**
	 * Definisce la logica con cui il giocatore sceglie quale carta giocare.
	 * Essendo un metodo astratto, viene implementato diversamente per Bot e Umani.
	 * 
	 * @param topDiscard la carta attualmente in cima alla pila degli scarti
	 * @return la carta scelta dal giocatore, oppure null se decide o è obbligato a
	 *         pescare
	 */
	public abstract Card playTurn(Card topDiscard);

	/**
	 * Ritorna il nome del giocatore.
	 * 
	 * @return il nome
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Ritorna l'ID del giocatore.
	 * 
	 * @return l'ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Ritorna il punteggio totale accumulato nei vari round.
	 * 
	 * @return il punteggio totale
	 */
	public int getTotalScore() {
		return totalScore;
	}

	/**
	 * Ritorna il punteggio nel round attuale.
	 * 
	 * @return il punteggio del round attuale
	 */
	public int getCurrentRoundScore() {
		return currentRoundScore;
	}

	/**
	 * Indica se il giocatore ha vinto il round.
	 * 
	 * @return true se ha vinto, false altrimenti
	 */
	public boolean getWonRound() {
		return wonRound;
	}

	/**
	 * Ritorna il tipo di giocatore.
	 * 
	 * @return il PlayerType
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}

	/**
	 * Ritorna lo stato attuale della dichiarazione di "UNO" per il giocatore.
	 * 
	 * @return lo stato UNOState tra Safe, Called e Unsafe
	 */
	public UNOState getUnoState() {
		return unoState;
	}

	/**
	 * Ritorna la mano di carte.
	 * 
	 * @return l'oggetto Hand
	 */
	public Hand getHand() {
		return hand;
	}

	/**
	 * Imposta il giocatore come vincitore del round corrente.
	 */
	public void setWon() {
		wonRound = true;
	}

	/**
	 * Aggiorna lo stato della dichiarazione di "UNO" del giocatore.
	 * 
	 * @param newUnoState il nuovo stato da assegnare
	 */
	public void setUnoState(UNOState newUnoState) {
		unoState = newUnoState;
	}

	/**
	 * Ripristina i punteggi e lo stato del giocatore per iniziare una nuova
	 * partita.
	 */
	public void resetScore() {
		totalScore = 0;
		currentRoundScore = 0;
		wonRound = false;
		unoState = UNOState.Safe;
	}

}
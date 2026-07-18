package it.uniroma1.mdp.uno.model.player;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.deck.Hand;

/**
 * Rappresenta un giocatore generico (Umano o Bot) all'interno della partita.
 * Gestisce il punteggio, la mano di carte e lo stato relativo alla dichiarazione di "UNO".
 * 
 * @author Massimo Giorgini (M.2234123)
 */
public abstract class Player {
	/** Nome del giocatore */
	private final String playerName;

	/** Identificativo univoco del giocatore */
	private final int playerID;

	/** Punteggio totale accumulato in tutti i round (per partite a punti) */
	private int totalScore;

	/** Punteggio calcolato nel round corrente */
	private int currentRoundScore;

	/** Indica se il giocatore ha vinto il round corrente svuotando la mano */
	private boolean wonRound;

	/**
	 * Definisce se il giocatore è umano o controllato dal computer.
	 */
	public enum PlayerType {
		HUMAN, BOT
	}

	/** Il tipo di giocatore (umano o bot) */
	private final PlayerType playerType;

	/**
	 * Stato della dichiarazione di "UNO" del giocatore.
	 * <ul>
	 * <li><b>Safe</b>: Al giocatore non può essere contestata la mancata dichiarazione di UNO.</li>
	 * <li><b>Called</b>: Il giocatore ha dichiarato UNO rimanendo con una sola carta in mano.</li>
	 * <li><b>Unsafe</b>: Al giocatore può essere contestata la mancata dichiarazione di UNO.</li>
	 * </ul>
	 */
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
	 * @param playerName il nome del giocatore
	 * @param playerID l'identificativo univoco
	 * @param playerType il tipo di giocatore (umano o bot)
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
	 * @param card la carta da pescare/aggiungere alla mano
	 * @return true se la carta è stata aggiunta con successo
	 */
	public boolean takeCard(Card card) {
		return getHand().addCardToHand(card);
	}

	/**
	 * Fa giocare al giocatore una specifica carta dalla sua mano.
	 * 
	 * @param i l'indice della carta all'interno della mano
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
	 * @param topDiscard la carta attualmente in cima alla pila degli scarti (usata per validare la giocata)
	 * @return la carta scelta dal giocatore, oppure null se decide (o è obbligato) a pescare
	 */
	public abstract Card playTurn(Card topDiscard);

	// ==========================================
	// Getter e Setter
	// ==========================================

	/**
	 * Restituisce il nome del giocatore.
	 * @return il nome
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Restituisce l'ID univoco del giocatore.
	 * @return l'ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Restituisce il punteggio totale accumulato nei vari round.
	 * @return il punteggio totale
	 */
	public int getTotalScore() {
		return totalScore;
	}

	/**
	 * Restituisce il punteggio ottenuto nel round corrente.
	 * @return il punteggio del round corrente
	 */
	public int getCurrentRoundScore() {
		return currentRoundScore;
	}

	/**
	 * Indica se il giocatore ha vinto l'ultimo round.
	 * @return true se ha vinto, false altrimenti
	 */
	public boolean getWonRound() {
		return wonRound;
	}

	/**
	 * Restituisce il tipo di giocatore (Umano o Bot).
	 * @return il PlayerType
	 */
	public PlayerType getPlayerType() {
		return playerType;
	}

	/**
	 * Restituisce lo stato attuale della dichiarazione "UNO" per questo giocatore.
	 * @return lo stato UNOState (Safe, Called, Unsafe)
	 */
	public UNOState getUnoState() {
		return unoState;
	}

	/**
	 * Restituisce la mano di carte del giocatore.
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
	 * @param newUnoState il nuovo stato da assegnare
	 */
	public void setUnoState(UNOState newUnoState) {
		unoState = newUnoState;
	}

	/**
	 * Ripristina i punteggi e lo stato del giocatore per iniziare una nuova partita.
	 */
	public void resetScore() {
		totalScore = 0;
		currentRoundScore = 0;
		wonRound = false;
		unoState = UNOState.Safe;
	}

}
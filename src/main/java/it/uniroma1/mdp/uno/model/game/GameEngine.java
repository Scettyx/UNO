package it.uniroma1.mdp.uno.model.game;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.deck.Deck;
import it.uniroma1.mdp.uno.model.deck.DiscardPile;
import it.uniroma1.mdp.uno.model.player.Player;
import it.uniroma1.mdp.uno.model.rules.RuleSet;

/**
 * @author Massimo Giorgini (M.2234123)
 * @author Cosmin Florea (M.2241398)
 */

public class GameEngine {

	public enum Mode {
		SINGLE, POINTS
	}

	private GameMode gameMode;
	private RuleSet ruleSet;
	private CardColor currentColor;
	private int currentPlayer;
	private Player[] playerList;
	private Deck deck;
	private DiscardPile discardPile;
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
	 * Questo metodo sposta il turno avanti.
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
	 * Assegna il punteggio al vincitore del round e aggiorna il suo punteggio totale.
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
	 * La logica della challenge del Wild Draw Four; se il giocatore non poteva giocare la carta, pesca quattro carte. Se poteva giocare la carta, lo sfidante pesca 6 carte.
	 * @param playedCard è la carta sulla cima del discardPile
	 * @param current è il giocatore che ha giocato la carta WildDrawFour
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
	 * Pesca una carta se il giocatore non ne ha giocata una in questo turno e gli permette di giocare la carta che ha appena pescato se è giocabile
	 * @param current
	 * @param playedCard
	 */
	public void drawIfNotPlayed(Player current, Card playedCard) {
		current.setHasDrawn(false);
		if (playedCard == null && current.getHasDrawn() == false) {
			deck.drawCardRandom(current.getHand(), 1);
			current.setHasDrawn(true);
			playedCard = current.playTurn(discardPile.getTopCard()); //da aggiungere una limitazione: il giocatore in questo caso può giocare solo la carta che ha pescato ora 
																	//(se hasDrawn = true, può giocare solo l'ultima carta pescata)
		}
	}
	
	public void checkUnoDeclaration(Player current) {
		while (current.getHand().getNumCards() == 1 && current.getUnoState() != Player.UNOState.Called) {
			current.setUnoState(Player.UNOState.Unsafe, deck, current); //aggiungi poi un modo per cambiare l'UNOState a Called quando un altro giocatore 
		}
	}
	
	/**
	 * le condizioni di vittoria di un ROUND in base alla modalità
	 * @param current
	 * @param roundOver
	 */
	public void roundWinConditions(Player current, boolean roundOver) {
		current.setWon(true);
		addPointsToWinner(current);
		if (gameMode.getPointMatch() == true) {
			current.setWon(false);
			gameMode.setGameOver(true);
			roundOver = true;
		}
		else if (gameMode.getPointMatch() == false) {
			for (Player player : getPlayerList()) {
				player.resetScore();
			}
			roundOver = true;
		}
	}
	
	/**
	 * Se la modalità è a punti, controlla se alla fine di un round c'è un giocatore che ha raggiunto la soglia di vittoria; se non c'è, gioca un nuovo round
	 */
	public void gameWinConditions() {
		boolean winnerPresent = false;
		for (Player player : getPlayerList()) {
			if (player.getTotalScore() >= gameMode.getPointGoal()){
				player.setWon(true);
				winnerPresent = true;
				gameMode.setGameOver(true);
			}
		}
		if (winnerPresent == false) {
			playRound();
		}
	}
	
	/**
	 * Distribuisce le 7 carte iniziali ad ogni giocatore, rimescola il mazzo, toglie tutte le carte dalla discardPile.
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
		
	/**
	 * Gestisce la logica dei Round nella partita.
	 */
	public void playRound() {
		distributeCards();
		boolean roundOver = false;
		while (!roundOver) {
			Player current = getPlayerList()[currentPlayer];
			Card playedCard = current.playTurn(discardPile.getTopCard());
			// pesca una carta dal deck se il giocatore non ha giocato nessuna carta nel suo turno.
			drawIfNotPlayed(current, playedCard);
			// se il giocatore ha giocato una carta, viene scartata e messa in cima alla discardPile.
			if (playedCard != null) {
				discardPile.addCard(playedCard);
				currentColor = playedCard.getActiveColor();
				
				//meccaniche dichiarazione UNO
				checkUnoDeclaration(current);
	
				// effetti legati a carte speciali
				switch (playedCard.getType()) {
					case REVERSE:
						direction = !direction;
					case SKIP:
						nextTurn();
					case DRAW_TWO:
						nextTurn();
						deck.drawCardRandom(current.getHand(), 2);
					case WILD: 
						currentColor = playedCard.getActiveColor(); // implementa che il giocatore dovrà scegliere il colore attivo
					case WILD_DRAW_FOUR: 
						if (current.getIsChallenged() == true) { //controlla se il giocatore è stato sfidato dopo il lancio del Wild Draw Four
							WildDrawFourChallenge(playedCard, current);
						} else { //se il giocatore non è stato sfidato dopo un Wild Draw Four, il prossimo giocatore pesca le 4 carte normalmente. 
							nextTurn();
							deck.drawCardRandom(getPlayerList()[currentPlayer].getHand(), 4);
						}
						currentColor = playedCard.getActiveColor(); // implementa che il giocatore dovrà scegliere il colore attivo
					case NUMBER:
						if (ruleSet.getNumberRush()) {
							for(Card i : current.getHand().getAllCards()) { //devo capire come implementare il RuleSet per number rush.....
								
							}
						}
				}
			}
			
			// Se il deck da cui si pescano le carte rimane vuoto, questo metodo sposta
						// tutte le carte dalla discardPile al deck (eccetto quella in cima).
			if (deck.isEmpty()) {
				discardPile.moveToDeck(deck);
			}
			
			// condizioni di fine round. Se la partita è a round singolo invece che a punti, il gioco può anche finire quì se un giocatore ha un mazzo vuoto.
			if (current.getHand().isEmpty()) {
				roundWinConditions(current, roundOver);
				break;
			}
			
			nextTurn();
		}
		//se la partita è a punti, il gioco non finisce nel singolo round ma controlla se è necessario giocarne uno nuovo
		if (gameMode.getPointMatch() == true) {
			gameWinConditions();
		}
	}
}

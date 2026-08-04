package it.uniroma1.mdp.uno.model.player;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.mdp.uno.model.card.Card;

/**
 * Classe di un giocatore umano.
 * @author Massimo Giorgini (M.2234123)
 */
public class HumanPlayer extends Player{
	private List<Card> selectedCardsFromUI = new ArrayList<>(); //le carte selezionate dal giocatore nell'interfaccia
	
	public HumanPlayer(String playerName, int playerID) {
		super(playerName, playerID, PlayerType.HUMAN);
	}
	
	public void setSelectedCardsFromUI (List<Card> cards) {
		this.selectedCardsFromUI = cards;
	}

	@Override
	public List<Card> playTurn(Card topDiscard) {
		List<Card> toPlay = new ArrayList<>(selectedCardsFromUI);
		selectedCardsFromUI.clear();
		return toPlay;
	}
	

}

package it.uniroma1.mdp.uno.model.player;

import java.util.ArrayList;
import java.util.List;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardType;

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
	
	public List<Card> getSelectedCardsFromUI() {
		return selectedCardsFromUI;
	}
	
	/**
	 * Metodo per controllare la presenza di carte solo numeriche tra le carte selezionate dal giocatore. Utile per la regola NumberRush
	 * @return true se ci sono solo carte numeriche tra le carte selezionate o se la lista è vuota, false se altrimenti.
	 */
	public boolean isSelectedCardsOnlyNumbers() {
		if(getSelectedCardsFromUI().size() == 0) {
			return true;
		}
		for(Card i : getSelectedCardsFromUI()) {
			if(i.getType() != CardType.NUMBER){
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Card> playTurn(Card topDiscard) {
		List<Card> toPlay = new ArrayList<>(selectedCardsFromUI);
		selectedCardsFromUI.clear();
		return toPlay;
	}
	

}

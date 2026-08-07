package it.uniroma1.mdp.uno.controller;

import it.uniroma1.mdp.uno.model.card.Card;
import javafx.fxml.FXML;

/**
 * La classe che fa interagire l'interfaccia grafica (View) con la logica del gioco (Model).
 * 
 * @author Massimo Giorgini (M.2234123)
 */
public class GameController {
	
	/**
	 * il giocatore seleziona una carta nel suo mazzo
	 * @param c
	 */
	@FXML
	public void onCardClicked(Card c) {
		
	}
	
	/**
	 * il giocatore decide di pescare una carta
	 */
	@FXML
	public void onDrawClicked() {
		System.out.println("funziona");
	}
	
	/**
	 * il giocatore dichiara UNO
	 */
	@FXML
	public void onUnoDeclared() {
		
	}
	

}


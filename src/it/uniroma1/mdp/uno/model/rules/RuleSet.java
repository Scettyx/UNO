package it.uniroma1.mdp.uno.model.rules;


/**
 * Classe per impostare le regole alternative.
 * 
 * @author Massimo Giorgini (M.2234123)
 */
public class RuleSet {
	private boolean stackDrawCards;
	private boolean numberRush;
	
	public RuleSet() {
		this.stackDrawCards = false;
		this.numberRush = false;
	}
	
	/**
	 * Questo metodo abilita la regola alternativa dello stacking delle carte pesca
	 */
	public void setStackDrawCards() {
		stackDrawCards = true;
	}
	
	/**
	 * Questo metodo abilita la regola alternativa del number rush
	 */
	public void setNumberRush() {
		numberRush = true; 
	}
	
	/**
	 * @return true se la regola alternativa dello stacking delle carte pesca è abilitata
	 */
	public boolean getStackDrawCards() {
		return stackDrawCards;
	}
	
	/**
	 * @return true se la regola alternativa del number rush è abilitata
	 */
	public boolean getNumberRush() {
		return numberRush;
	}
	
	/**
	 * Questo metodo disabilita la regola alternativa dello stacking delle carte pesca
	 */
	public void disableStackDrawCards() {
		stackDrawCards = false;
	}
	
	/**
	 * Questo metodo disabilita la regola alternativa del number rush
	 */
	public void disableNumberRush() {
		numberRush = false; 
	}
}

package it.uniroma1.mdp.uno.model.rules;


/**
 * Classe per impostare le regole alternative.
 * @author Massimo Giorgini (M.2234123)
 */
public class RuleSet {
	private boolean stackDrawCards;
	private boolean numberRush;
	
	public RuleSet() {
		this.stackDrawCards = false;
		this.numberRush = false;
	}
	
	public void setStackDrawCards() {
		stackDrawCards = true;
	}
	
	public void setNumberRush() {
		numberRush = true; 
	}
	
	public boolean getStackDrawCards() {
		return stackDrawCards;
	}
	
	public boolean getNumberRush() {
		return numberRush;
	}
	
	public void disableStackDrawCards() {
		stackDrawCards = false;
	}
	
	public void disableNumberRush() {
		numberRush = false; 
	}
}

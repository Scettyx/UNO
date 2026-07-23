package it.uniroma1.mdp.uno.model.rules;

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

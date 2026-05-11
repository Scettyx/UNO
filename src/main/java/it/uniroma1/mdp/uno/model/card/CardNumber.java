package it.uniroma1.mdp.uno.model.card;

public enum CardNumber {
	ZERO {public int getNumber() { return 0; }},    
    ONE {public int getNumber() { return 1; }},      
    TWO {public int getNumber() { return 2; }},      
    THREE {public int getNumber() { return 3; }},   
    FOUR {public int getNumber() { return 4; }},    
    FIVE {public int getNumber() { return 5; }},    
    SIX {public int getNumber() { return 6; }},     
    SEVEN {public int getNumber() { return 7; }},   
    EIGHT {public int getNumber() { return 8; }},    
    NINE {public int getNumber() { return 9; }},
    NONE;

    public int getNumber() {
    	return this.getNumber(); 
    }

}



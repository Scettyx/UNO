package it.uniroma1.mdp.uno.model.simulation;

import it.uniroma1.mdp.uno.model.game.GameEngine;
import it.uniroma1.mdp.uno.model.game.GameMode;
import it.uniroma1.mdp.uno.model.player.BotPlayer;
import it.uniroma1.mdp.uno.model.rules.RuleSet;


/**
 * Classe per la simulazione di una partita tra bot.
 * 
 * @author Massimo Giorgini (M.2234123)
 */
public class SimulationEngine extends GameEngine{

	public SimulationEngine(BotPlayer[] plist, GameMode gameMode, RuleSet ruleSet) {
		super(plist, gameMode, ruleSet);
	}

}

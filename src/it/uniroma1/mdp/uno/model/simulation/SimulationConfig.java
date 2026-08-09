package it.uniroma1.mdp.uno.model.simulation;

import it.uniroma1.mdp.uno.model.game.GameMode;
import it.uniroma1.mdp.uno.model.player.BotPlayer;
import it.uniroma1.mdp.uno.model.rules.RuleSet;

/**
 * Classe per configurare una simulazione.
 * @author Massimo Giorgini (M.2234123)
 */
public class SimulationConfig {
	
	public SimulationConfig() {	
	}
	
	public SimulationEngine createSimulation(BotPlayer[] plist, RuleSet ruleSet) {
		GameMode gameMode = new GameMode(false);
		SimulationEngine newSim = new SimulationEngine(plist, gameMode, ruleSet);
		return newSim;
	}
}

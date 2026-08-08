package it.uniroma1.mdp.uno.model.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che si occupa di memorizzare tutte le mosse fatte durante la partita
 * 
 * @author Cosmin Florea (M.2241398)
 */
public class GameHistory {

    private List<GameAction> actions;

    public GameHistory() {
        this.actions = new ArrayList<>();
    }

    /**
     * Aggiunge un'azione non nulla in elenco.
     * 
     * @param action da aggiungere
     */
    public void addGameAction(GameAction action) {
        if (action == null) {
            throw new NullPointerException("Azione nulla non valida");
        }
        this.actions.add(action);
    }

    /**
     * Getter per tutte le azioni
     * 
     * @return tutte le azioni
     */
    public List<GameAction> getAllActions() {
        return this.actions;
    }

    /**
     * Pulisce lo storico
     */
    public void clearHistory() {
        this.actions.clear();
    }

    /**
     * Stampa a schermo tutto lo storico
     */
    public void printHystory() {
        for (int i = 0; i < actions.size(); i++) {
            GameAction action = actions.get(i);
            System.out.println("Round " + i + " : " + action.setActionDescription());
        }
    }
}

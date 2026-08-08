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

    public void addGameAction(GameAction action) {
        if (action == null) {
            throw new NullPointerException("Azione nulla non valida");
        }
        this.actions.add(action);
    }

    public List<GameAction> getAllActions() {
        return this.actions;
    }
}

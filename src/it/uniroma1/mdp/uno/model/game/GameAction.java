package it.uniroma1.mdp.uno.model.game;

import it.uniroma1.mdp.uno.model.card.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che si occupa di memorizzare qualsiasi mossa e i relativi dati del
 * Player che le ha fatte, comprese di Challenge e chiamate di UNO
 * 
 * @author Cosmin Florea (M.2241398)
 */
public class GameAction {

    private final String playerName;
    private String actionType;
    private boolean unoCalled;
    private boolean challengeCalled;
    private boolean challengeSucceded;
    private List<Card> cardsInvolved;

    public GameAction(String playerName, String actionType) {
        this.playerName = playerName;
        this.actionType = actionType;
        this.unoCalled = false;
        this.challengeCalled = false;
        this.challengeSucceded = false;
    }

    /**
     * Aggiunge una carta all'evento
     * 
     * @param card aggiunta
     */
    public void addCardInvolved(Card card) {
        if (card == null) {
            throw new NullPointerException("Carta nulla non valida");
        }
        cardsInvolved.add(card);
    }

    /**
     * Getter per avere il nome del Player
     * 
     * @return nome del Player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Getter per avere il tipo d'azione
     * 
     * @return il tipo d'azione
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Getter per avere tutte le carte coinvolte
     * 
     * @return tutte le carte coinvolte
     */
    public List<Card> getCardsInvolved() {
        return cardsInvolved;
    }

    /**
     * Imposta la dichiarazione dell'Uno durante la partita
     * 
     * @param unoCalled nuovo stato
     */
    public void setUnoCalled(boolean unoCalled) {
        this.unoCalled = unoCalled;
    }

    /**
     * Getter per verificare la chiamata dell'Uno
     * 
     * @return se è stato chiamato Uno
     */
    public boolean getUnoCalled() {
        return unoCalled;
    }

    /**
     * Imposta una challenge
     * 
     * @param called   chiamata dell'Uno
     * @param succeded successo (o meno) della chiamata
     */
    public void setChallenge(boolean called, boolean succeded) {
        this.challengeCalled = called;
        this.challengeSucceded = succeded;
    }

    public String setActionDescription() {
        String description = playerName + " ha fatto " + actionType;

        if (!cardsInvolved.isEmpty()) {
            description += " con le carte ";
            for (Card c : cardsInvolved) {
                description += c.toString() + " ";
            }
        }

        if (unoCalled) {
            description += " ha chiamato Uno";
        }

        if (challengeCalled) {
            description += " Challenge chiamata: successo = " + challengeSucceded;
        }

        return description;
    }
}

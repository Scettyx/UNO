package it.uniroma1.mdp.uno.exception;

/**
 * @author Cosmin Florea (M.2241398)
 *
 *         Viene lanciata quando un giocatore tenta una Challenge non consentita
 */
public class InvalidChallengeException extends RuntimeException {

    /**
     * Lancia l'eccezione con un messaggio
     * 
     * @param message descrive la Challenge non consentita
     */
    public InvalidChallengeException(String message) {
        super(message);
    }
}
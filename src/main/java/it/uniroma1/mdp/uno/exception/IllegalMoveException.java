package it.uniroma1.mdp.uno.exception;

/**
 * @author Cosmin Florea (M.2241398)
 *
 *         Viene lanciato quando un giocatore fa una mossa non consentita
 */
public class IllegalMoveException extends RuntimeException {

    /**
     * Lancia l'eccezione con un messaggio
     * 
     * @param message descrive la mossa non consentita
     */
    public IllegalMoveException(String message) {
        super(message);
    }

    /**
     * Lancia l'eccezione con un messaggio e la causa
     * 
     * @param message descrive la mossa non consentita
     * @param cause   descrive la causa dell'errore
     */
    public IllegalMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
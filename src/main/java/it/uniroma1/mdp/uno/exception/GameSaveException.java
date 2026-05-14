package it.uniroma1.mdp.uno.exception;

/**
 * @author Cosmin Florea (M.2241398)
 * 
 *         Viene lanciata quando viene caricato uno stato di gioco
 */
public class GameSaveException extends RuntimeException {

    /**
     * Lancia l'eccezione con un messaggio
     * 
     * @param message descrive l'errore
     */
    public GameSaveException(String message) {
        super(message);
    }

    /**
     * Lancia l'eccezione con un messaggio e la causa
     * 
     * @param message descrive l'errore
     * @param cause   descrive la causa dell'errore
     */
    public GameSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
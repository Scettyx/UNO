package it.uniroma1.mdp.uno.model.player;

/**
 * Classe astratta per i Bot.
 * 
 * @author Cosmin Florea (M.2241398)
 */
public abstract class BotPlayer extends Player {

    public enum BotProfile {
        RANDOM,
        CONSERVATIVE,
        AGGRESSIVE
    }

    /** Profilo che adotterà il bot per tutta la partita; */
    private final BotProfile botProfile;

    /**
     * Costruisce il Bot.
     * 
     * @param playerName del bot
     * @param playerID   del bot
     * @param botProfile che adotterà tra RANDOM, GREEDY e AGGRESSIVE
     */
    public BotPlayer(String playerName, int playerID, BotProfile botProfile) {
        super(playerName, playerID, PlayerType.BOT);
        this.botProfile = botProfile;
    }

    /**
     * Ritorna il profilo comportamentale del bot.
     * 
     * @return il BotProfile associato a questo giocatore
     */
    public BotProfile getBotProfile() {
        return botProfile;
    }
}
package it.uniroma1.mdp.uno.model.player;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.card.NumberCard;
import it.uniroma1.mdp.uno.model.deck.Hand;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe astratta per i Bot.
 * 
 * @author Cosmin Florea (M.2241398)
 */
public abstract class BotPlayer extends Player {

    public enum BotProfile {
        RANDOM,
        GREEDY,
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
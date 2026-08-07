package it.uniroma1.mdp.uno.model.player;

import it.uniroma1.mdp.uno.model.card.Card;
import it.uniroma1.mdp.uno.model.card.CardColor;
import it.uniroma1.mdp.uno.model.card.CardType;
import it.uniroma1.mdp.uno.model.card.NumberCard;
import it.uniroma1.mdp.uno.model.deck.Hand;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Classe per i Bot con strategia casuale: il Bot sceglierà casualmente tra le
 * giocate disponibili.
 * 
 * @author Cosmin Florea (M.2241398)
 */
public class ConservativeBot extends BotPlayer {

    private Random random;

    /**
     * Costruisce il Bot.
     * 
     * @param playerName del bot
     * @param playerID   del bot
     * @param botProfile CONSERVATIVE
     */
    public ConservativeBot(String playerName, int playerID) {
        super(playerName, playerID, BotProfile.CONSERVATIVE);
        this.random = new Random();
    }

    private List<Card> collectPlayableCards(Card topDiscard) {
        List<Card> playable = new ArrayList<>();

        for (Card card : getHand().getAllCards()) {
            if (card.isPlayableOn(topDiscard)) {
                playable.add(card);
            }
        }
        return playable;
    }

    private CardColor pickRandomColor() {
        CardColor[] realColors = {
                CardColor.BLUE,
                CardColor.GREEN,
                CardColor.RED,
                CardColor.YELLOW
        };
        return realColors[random.nextInt(realColors.length)];
    }

    private Card findConservativeCard(List<Card> playableCards) {
        for (Card card : playableCards) {
            if (card.getType().name().equals("NUMBER")) {
                return card;
            }
        }

        for (Card card : playableCards) {
            if (card.getType().name().equals("REVERSE") ||
                    card.getType().name().equals("SKIP") ||
                    card.getType().name().equals("DRAW_TWO")) {
                return card;
            }
        }

        return playableCards.get(0);
    }

    @Override
    public List<Card> playTurn(Card topDiscard) {
        List<Card> playableCards = collectPlayableCards(topDiscard);

        if (playableCards.isEmpty()) {
            return new ArrayList<>();
        }

        Card chosenCard = findConservativeCard(playableCards);

        if (chosenCard.getType().isWild()) {
            chosenCard.setChosenColor(pickRandomColor());
        }

        // Rimuove la carta dalla mano
        getHand().getCardAtIndex(getHand().getAllCards().indexOf(chosenCard));

        List<Card> result = new ArrayList<>();
        result.add(chosenCard);
        return result;
    }
}
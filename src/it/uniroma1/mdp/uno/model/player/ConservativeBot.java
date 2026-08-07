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

    /**
     * Filtra le carte in mano per mettere in evidenza solo quelle giocabili sopra
     * l'ultima carta
     * 
     * @param topDiscard la carta in cima alla pila degli scrati
     * @return lista di carte giocabili
     */
    private List<Card> collectPlayableCards(Card topDiscard) {
        List<Card> playable = new ArrayList<>();

        for (Card card : getHand().getAllCards()) {
            if (card.isPlayableOn(topDiscard)) {
                playable.add(card);
            }
        }
        return playable;
    }

    /**
     * Sceglie un colore casuale quando piazza una carta Wild
     * 
     * @return un colore casuale
     */
    private CardColor pickRandomColor() {
        CardColor[] realColors = {
                CardColor.BLUE,
                CardColor.GREEN,
                CardColor.RED,
                CardColor.YELLOW
        };
        return realColors[random.nextInt(realColors.length)];
    }

    /**
     * Cerca tra le carte giocabili se c'è una carta numerica, altrimenti
     * cerca comunque una carta speciale prima di passare alle carte di tipo WILD
     * 
     * @param playableCards lista di carte giocabili
     * @return la prima carta Numerica giocabile o la prima carta speciale,
     *         altrimenti null
     */
    private Card findConservativeCard(List<Card> playableCards) {
        if (!playableCards.isEmpty()) {
            for (Card card : playableCards) {
                if (card.getType() == CardType.NUMBER) {
                    return card;
                }
            }

            for (Card card : playableCards) {
                if (card.getType() == CardType.REVERSE ||
                        card.getType() == CardType.SKIP ||
                        card.getType() == CardType.DRAW_TWO) {
                    return card;
                }
            }

            return playableCards.get(0);
        }
        return null;
    }

    /**
     * Sceglie in maniera casuale la carta da giocare tra quelle Playable
     */
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
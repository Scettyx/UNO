package it.uniroma1.mdp.uno.model.simulation;

import java.util.Map;
import java.util.HashMap;

import it.uniroma1.mdp.uno.model.game.GameAction;
import it.uniroma1.mdp.uno.model.game.GameHistory;
import it.uniroma1.mdp.uno.model.player.Player;

/**
 * Per le statistiche a fine Simulazione tra bot
 * 
 * @author Cosmin Florea (M.2241398)
 */
public class SimulationStats {
    
    // Inner class per le stats dei bot
    public static class BotStats {
        public int wins = 0;
        public int points = 0;      // somma dei punti guadagnati nelle partite vinte
        public int gamesPlayed = 0;
        public int cardsDrawed = 0;
        public int challengeCalled = 0;
        public int challengeSucceded = 0;
        public int noWin = 0;      // partite finite senza vincitore (anti-stallo)
    }

    private Map<String, BotStats> statsMap;
    // Conta le partite finite senza nessun vincitore (stallo)
    private int noWin = 0;

    public SimulationStats() {
        statsMap = new HashMap<>();
    }

    /**
     * @return il numero totale di partite finite in stallo (senza vincitore)
     */
    public int getNoWin() {
        return noWin;
    }

    /**
     * Trova la classe del bot partendo dal nome
     *
     * @param players lista dei players
     * @param name    del player
     * @return la classe del bot
     */
    public String findByName(Player[] players, String name) {
        for (Player p : players) {
            if (p.getPlayerName().equals(name)) {
                return p.getClass().getSimpleName();
            }
        }
        return null;
    }

    /**
     * Aggiorna le statistiche alla fine di una singola partita.
     *
     * @param players della partita
     * @param history della partita
     */
    public void updateStats(Player[] players, GameHistory history) {
        // Prima controlliamo se c'è un vincitore in questa partita
        boolean isThereWinner = false;
        for (Player p : players) {
            if (p.getWonRound()) {
                isThereWinner = true;
                break;
            }
        }

        if (!isThereWinner) {
            // Partita terminata per stallo: contiamo la partita ma non assegniamo vittorie
            noWin++;
            for (Player p : players) {
                String name = p.getClass().getSimpleName();

                if (!statsMap.containsKey(name)) {
                    statsMap.put(name, new BotStats());
                }

                statsMap.get(name).gamesPlayed++;
                statsMap.get(name).noWin++;
            }
            return; // nessun punto né vittoria da assegnare
        }

        for (Player p : players) {
            String name = p.getClass().getSimpleName();

            if (!statsMap.containsKey(name)) {
                statsMap.put(name, new BotStats());
            }

            BotStats stats = statsMap.get(name);
            stats.gamesPlayed++;

            if (p.getWonRound()) {
                stats.wins++;

                // Calcoliamo i punti del vincitore sommando le carte rimaste
                // in mano agli avversari (stessa logica di GameEngine.addPointsToWinner)
                int puntiGuadagnati = 0;
                for (Player avversario : players) {
                    if (avversario != p) {
                        for (it.uniroma1.mdp.uno.model.card.Card c : avversario.getHand().getAllCards()) {
                            puntiGuadagnati += c.getPointsValue();
                        }
                    }
                }
                stats.points += puntiGuadagnati;
            }
        }

        if (history != null) {
            for (GameAction action : history.getAllActions()) {
                String name = findByName(players, action.getPlayerName());

                if (name != null && statsMap.containsKey(name)) {
                    BotStats stats = statsMap.get(name);
                    String type = action.getActionType();

                    if (type.equals("DRAW") || type.equals("DRAW_PENALTY")) {
                        stats.cardsDrawed++;
                    } else if (type.equals("CHALLENGE")) {
                        stats.challengeCalled++;

                        if (action.setActionDescription().contains("VINCE")) {
                            stats.challengeSucceded++;
                        }
                    }
                }
            }
        }
    }

    /**
     * Ritorna la mappa con tutte le statistiche.
     * 
     * @return la mappa profilo -> BotStats
     */
    public Map<String, BotStats> getStatsMap() {
        return statsMap;
    }

    public void stampStats() {
        System.out.println("STATISTICHE FINALI DELLA SIMULAZIONE");

        for (Map.Entry<String, BotStats> entry : statsMap.entrySet()) {
            String profile = entry.getKey();
            BotStats stats = entry.getValue();
            double average = 0;

            if (stats.gamesPlayed > 0) {
                average = (double) stats.points / stats.gamesPlayed;
            }

            System.out.println("Profilo Bot: " + profile);
            System.out.println("- Vittorie: " + stats.wins);
            System.out.println("- Punteggio medio: " + average);
            System.out.println("- Carte pescate: " + stats.cardsDrawed + " volte");
            System.out.println("- Challenge chiamate: " + stats.challengeCalled);
            System.out.println("- Challenge vinte: " + stats.challengeSucceded);
            System.out.println();
        }
    }

}

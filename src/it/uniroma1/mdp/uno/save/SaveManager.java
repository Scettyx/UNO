package it.uniroma1.mdp.uno.save;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import it.uniroma1.mdp.uno.model.card.*;
import it.uniroma1.mdp.uno.model.game.GameEngine;
import it.uniroma1.mdp.uno.model.game.GameMode;
import it.uniroma1.mdp.uno.model.player.*;
import it.uniroma1.mdp.uno.model.rules.RuleSet;

/**
 * Gestisce il caricamento ed il salvataggio della partita.
 * Usa un DTO (Data Transfer Object) per evitare di serializzare l'intero
 * GameEngine (che contiene il mazzo di 108 carte, la storia partita ecc.).
 * Utilizza un CardDeserializer e PlayerDeserializer custom per gestire
 * il polimorfismo delle classi astratte.
 *
 * @author Cosmin Florea (M.2241398)
 */
public class SaveManager {

    private final Gson gson;
    private final String saveFilePath = "uno_savegame.json";

    public SaveManager() {
        this.gson = new GsonBuilder()
            .registerTypeAdapter(Card.class, new CardDeserializer())
            .registerTypeAdapter(Player.class, new PlayerDeserializer())
            .setPrettyPrinting()
            .create();
    }

    // ===================================================================
    // DTO: Oggetto leggero che rappresenta lo stato salvabile della partita
    // ===================================================================

    /**
     * Contiene solo i dati essenziali per ripristinare una partita.
     * Non include il mazzo completo (rigenerato a runtime) né la GameHistory.
     */
    private static class SaveDTO {
        boolean pointMatch;
        int winThreshold;
        boolean stackDrawCards;
        boolean numberRush;
        int currentPlayerIndex;
        boolean direction;
        String currentColor;
        int pendingDrawPenalty;
        PlayerDTO[] players;
    }

    /** Rappresenta un giocatore salvato */
    private static class PlayerDTO {
        String name;
        int id;
        String playerType;    // "HUMAN", "BOT"
        String botProfile;    // "RANDOM", "CONSERVATIVE", "AGGRESSIVE" (solo per BOT)
        int totalScore;
        CardDTO[] hand;
    }

    /** Rappresenta una carta salvata */
    private static class CardDTO {
        String type;          // es. "NUMBER", "DRAW_TWO", "WILD", "WILD_DRAW_FOUR"
        String originalColor; // es. "RED", "BLUE", "NONE"
        int numericValue;     // usato solo per NumberCard
    }

    // ===================================================================
    // Metodi principali
    // ===================================================================

    /**
     * Salva lo stato essenziale della partita su file JSON.
     *
     * @param engine il motore di gioco da salvare
     * @return true se il salvataggio va a buon fine, false altrimenti
     */
    public boolean saveGame(GameEngine engine) {
        if (engine == null) return false;

        try {
            SaveDTO dto = toDTO(engine);
            FileWriter writer = new FileWriter(saveFilePath);
            gson.toJson(dto, writer);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Carica una partita da file JSON e ricostruisce il GameEngine.
     *
     * @return L'istanza di GameEngine ricaricata, o null se il file non esiste o è corrotto
     */
    public GameEngine loadGame() {
        try {
            FileReader reader = new FileReader(saveFilePath);
            SaveDTO dto = gson.fromJson(reader, SaveDTO.class);
            reader.close();
            return fromDTO(dto);
        } catch (IOException e) {
            System.out.println("Errore di lettura o salvataggio inesistente.");
            return null;
        }
    }

    // ===================================================================
    // Conversioni DTO <-> GameEngine
    // ===================================================================

    /** Converte il GameEngine in un DTO leggero */
    private SaveDTO toDTO(GameEngine engine) {
        SaveDTO dto = new SaveDTO();
        dto.pointMatch = engine.getGameMode().getPointMatch();
        dto.winThreshold = engine.getGameMode().getPointGoal(); // fix: era getWinThreshold()
        dto.stackDrawCards = engine.getRuleSet().getStackDrawCards();
        dto.numberRush = engine.getRuleSet().getNumberRush();
        dto.currentPlayerIndex = engine.getCurrentPlayerIndex();
        dto.direction = engine.getDirection();
        dto.pendingDrawPenalty = engine.getPendingDrawPenalty();
        dto.currentColor = engine.getCurrentColor() != null ? engine.getCurrentColor().name() : null;

        Player[] players = engine.getPlayerList();
        dto.players = new PlayerDTO[players.length];
        for (int i = 0; i < players.length; i++) {
            dto.players[i] = playerToDTO(players[i]);
        }
        return dto;
    }

    private PlayerDTO playerToDTO(Player p) {
        PlayerDTO pd = new PlayerDTO();
        pd.name = p.getPlayerName();
        pd.id = p.getPlayerID();
        pd.totalScore = p.getTotalScore();
        pd.playerType = p.getPlayerType().name();
        if (p instanceof BotPlayer) {
            pd.botProfile = ((BotPlayer) p).getBotProfile().name();
        }
        var cards = p.getHand().getAllCardsCopy();
        pd.hand = new CardDTO[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            pd.hand[i] = cardToDTO(cards.get(i));
        }
        return pd;
    }

    private CardDTO cardToDTO(Card c) {
        CardDTO cd = new CardDTO();
        cd.type = c.getType().name();
        cd.originalColor = c.getOriginalColor().name();
        if (c instanceof NumberCard) {
            cd.numericValue = ((NumberCard) c).getValue(); // fix: era getNumber()
        }
        return cd;
    }

    private GameEngine fromDTO(SaveDTO dto) {
        // Ricostruisce i giocatori
        Player[] players = new Player[dto.players.length];
        for (int i = 0; i < dto.players.length; i++) {
            players[i] = playerFromDTO(dto.players[i]);
        }

        // Ricostruisce GameMode e RuleSet
        GameMode gm = new GameMode(dto.pointMatch, dto.winThreshold);
        RuleSet rs = new RuleSet(dto.stackDrawCards, dto.numberRush);

        // Crea il GameEngine e ripristina lo stato
        GameEngine engine = new GameEngine(players, gm, rs);
        engine.setCurrentPlayerIndex(dto.currentPlayerIndex);
        engine.setDirection(dto.direction);
        engine.setPendingDrawPenalty(dto.pendingDrawPenalty);
        if (dto.currentColor != null) {
            engine.setCurrentColor(CardColor.valueOf(dto.currentColor));
        }

        // Reinizializza il mazzo senza distribuire le carte (le hanno già in mano)
        // initializeDeckOnly() è definito in GameEngine
        engine.initializeDeckOnly();
        return engine;
    }

    /** Ricostruisce un Player dal suo DTO */
    private Player playerFromDTO(PlayerDTO pd) {
        Player p;
        if ("HUMAN".equals(pd.playerType)) {
            p = new HumanPlayer(pd.name, pd.id);
        } else {
            String profile = pd.botProfile != null ? pd.botProfile : "RANDOM";
            switch (profile) {
                case "CONSERVATIVE": p = new ConservativeBot(pd.name, pd.id); break;
                case "AGGRESSIVE":   p = new AggressiveBot(pd.name, pd.id);   break;
                default:             p = new RandomBot(pd.name, pd.id);       break;
            }
        }
        p.setTotalScore(pd.totalScore);

        // Ricostruisce le carte in mano
        if (pd.hand != null) {
            for (CardDTO cd : pd.hand) {
                p.getHand().addCardToHand(cardFromDTO(cd));
            }
        }
        return p;
    }

    /** Ricostruisce una Card dal suo DTO */
    private Card cardFromDTO(CardDTO cd) {
        CardType type = CardType.valueOf(cd.type);
        CardColor color = CardColor.valueOf(cd.originalColor);
        switch (type) {
            case NUMBER: return new NumberCard(color, cd.numericValue);
            case WILD:   return new WildCard(CardType.WILD);
            case WILD_DRAW_FOUR: return new WildCard(CardType.WILD_DRAW_FOUR);
            default:     return new ActionCard(type, color);
        }
    }

    // ===================================================================
    // Deserializer GSON custom (usati solo se usi il polimorfismo via JSON puro)
    // ===================================================================

    /** Deserializer per le sottoclassi di Card */
    private static class CardDeserializer implements JsonDeserializer<Card> {
        @Override
        public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            CardType type = CardType.valueOf(obj.get("type").getAsString());
            CardColor color = CardColor.valueOf(obj.get("originalColor").getAsString());
            switch (type) {
                case NUMBER: return new NumberCard(color, obj.get("numericValue").getAsInt());
                case WILD:   return new WildCard(CardType.WILD);
                case WILD_DRAW_FOUR: return new WildCard(CardType.WILD_DRAW_FOUR);
                default:     return new ActionCard(type, color);
            }
        }
    }

    /** Deserializer per le sottoclassi di Player */
    private static class PlayerDeserializer implements JsonDeserializer<Player> {
        @Override
        public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String playerType = obj.has("playerType") ? obj.get("playerType").getAsString() : "BOT";
            if ("HUMAN".equals(playerType)) return ctx.deserialize(json, HumanPlayer.class);
            String profile = obj.has("botProfile") ? obj.get("botProfile").getAsString() : "RANDOM";
            switch (profile) {
                case "CONSERVATIVE": return ctx.deserialize(json, ConservativeBot.class);
                case "AGGRESSIVE":   return ctx.deserialize(json, AggressiveBot.class);
                default:             return ctx.deserialize(json, RandomBot.class);
            }
        }
    }
}
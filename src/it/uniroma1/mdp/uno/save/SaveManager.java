package it.uniroma1.mdp.uno.save;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import it.uniroma1.mdp.uno.model.game.GameEngine;
import it.uniroma1.mdp.uno.model.player.*;

/**
 * Gestisce il caricamento ed il salvataggio della partita
 * 
 * @author Cosmin Florea (M.2241398)
 */
public class SaveManager {
    private final Gson gson;
    private final String saveFilePath = "uno_savegame.json";

    public SaveManager() {
        this.gson = new GsonBuilder()
        .registerTypeAdapter(Player.class, new PlayerDeserializer())
        .setPrettyPrinting()
        .create();
    }

    /**
     * Salva il game Engine su file
     * 
     * @param engine da salvare
     * @return true se il salvataggio va a buon fine
     */
    public boolean saveGame(GameEngine engine) {

        if (engine == null) {
            return false;
        }

        try {
            FileWriter writer = new FileWriter(saveFilePath);
            gson.toJson(engine, writer);
            return true;

        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Legge il file Json e carica il Game Engine
     * 
     * @return GameEngine o null
     */
    public GameEngine loadGame() {
        try (FileReader reader = new FileReader(saveFilePath);) {
            return gson.fromJson(reader, GameEngine.class);

        } catch (IOException e) {
            System.out.println("Errore di lettura");
            return null;
        }
    }

    public static class PlayerDeserializer implements JsonDeserializer<Player> {
        @Override
        public Player deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String playerType = jsonObject.has("playerType") ? jsonObject.get("botProfile").getAsString() : "RANDOM";

            if (playerType.equals("HUMAN")) {
                return context.deserialize(json, HumanPlayer.class);
            } else {
                String botProfile = jsonObject.has("botProfile") ? jsonObject.get("botProfile").getAsString() : "RANDOM";

                switch (botProfile) {
                    case "CONSERVATIVE":
                        return context.deserialize(json, ConservativeBot.class);
                    case "AGGRESIVE":
                        return context.deserialize(json, AggressiveBot.class);
                    default:
                        return context.deserialize(json, RandomBot.class);
                }
            }
        }
    }
}

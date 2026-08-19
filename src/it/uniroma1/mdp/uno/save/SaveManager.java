package it.uniroma1.mdp.uno.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import it.uniroma1.mdp.uno.model.game.GameEngine;

/**
 * Gestisce il caricamento ed il salvataggio della partita
 * 
 * @author Cosmin Florea (M.2241398)
 */
public class SaveManager {
    private final Gson gson;
    private final String saveFilePath = "uno_savegame.json";

    public SaveManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
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
            writer.flush();
            writer.close();
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
        try {
            FileReader reader = new FileReader(saveFilePath);
            GameEngine loadedEngine = gson.fromJson(reader, GameEngine.class);
            reader.close();
            return loadedEngine;

        } catch (IOException e) {
            System.out.println("Errore di lettura");
            e.printStackTrace();
            return null;
        }
    }
}

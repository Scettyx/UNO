package it.uniroma1.mdp.uno.view;

import java.net.URL;

import it.uniroma1.mdp.uno.model.game.GameEngine;
import it.uniroma1.mdp.uno.model.game.GameMode;
import it.uniroma1.mdp.uno.model.player.AggressiveBot;
import it.uniroma1.mdp.uno.model.player.BotPlayer;
import it.uniroma1.mdp.uno.model.player.ConservativeBot;
import it.uniroma1.mdp.uno.model.player.HumanPlayer;
import it.uniroma1.mdp.uno.model.player.Player;
import it.uniroma1.mdp.uno.model.player.RandomBot;
import it.uniroma1.mdp.uno.model.rules.RuleSet;
import it.uniroma1.mdp.uno.model.simulation.SimulationEngine;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MainApp dalla quale si ha accesso diretto al menu
 * 
 * @author Cosmin Florea (M.2241398)
 * @author Massimo Giorgini (M.2234123)
 */
public class MainApp extends Application {

    private StackPane root; 

    @Override
    public void start(Stage primaryStage) {
        root = new StackPane();
        root.getStyleClass().add("menu-bg"); // Applica lo sfondo in panno verde stile casinò

        // mostra il menu iniziale con il bottone "Inizia Partita"
        showMainMenu();

        Scene scene = new Scene(root, 1024, 768);
        
        URL cssUrl = getClass().getResource("/resources/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setTitle("UNO Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Schermata 1: Solo il bottone "Inizia Partita"
     */
    private void showMainMenu() {
        VBox menuCard = new VBox(25);
        menuCard.setAlignment(Pos.CENTER);
        menuCard.getStyleClass().add("menu-card");
        menuCard.setMaxSize(450, 300);
        
        Label titleLabel = new Label("UNO");
        titleLabel.getStyleClass().add("title-main");

        Button startButton = new Button("INIZIA PARTITA");
        startButton.getStyleClass().addAll("menu-button", "menu-button-primary");
        
        // Al click, passa al menu di configurazione
        startButton.setOnAction(e -> showMenuConfig());

        menuCard.getChildren().addAll(titleLabel, startButton);
        
        // Aggiorna la root
        root.getChildren().clear();
        root.getChildren().add(menuCard);
    }

    /**
     * Schermata 2: Scelta tra Simulazione e Partita Normale
     */
    private void showMenuConfig() {
        VBox configCard = new VBox(20);
        configCard.setAlignment(Pos.CENTER);
        configCard.getStyleClass().add("menu-card");
        configCard.setMaxWidth(600);

        Label titleLabel = new Label("IMPOSTAZIONI PARTITA");
        titleLabel.getStyleClass().add("title-sub");

        HBox typeBox = new HBox(15);
        typeBox.setAlignment(Pos.CENTER);
        Label typeLabel = new Label("Tipo di Partita:");
        typeLabel.getStyleClass().add("menu-label");
        
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Seleziona...", "Partita Normale", "Simulazione");
        typeCombo.setValue("Seleziona...");
        typeBox.getChildren().addAll(typeLabel, typeCombo);

        // Questo VBox conterrà dinamicamente i settaggi in base a cosa si sceglie nella ComboBox
        VBox dynamicOptionsBox = new VBox(15);
        dynamicOptionsBox.setAlignment(Pos.CENTER);

        // Listener che reagisce al cambio di tendina
        typeCombo.setOnAction(e -> {
            dynamicOptionsBox.getChildren().clear(); // Pulisce le vecchie opzioni
            String choice = typeCombo.getValue();
            
            if ("Simulazione".equals(choice)) {
                buildSimulationMenu(dynamicOptionsBox);
            } else if ("Partita Normale".equals(choice)) {
                buildNormalMenu(dynamicOptionsBox);
            }
        });

        // Bottone per tornare indietro al menu principale
        Button backButton = new Button("Torna Indietro");
        backButton.getStyleClass().addAll("menu-button", "back-button");
        backButton.setOnAction(e -> showMainMenu());

        configCard.getChildren().addAll(titleLabel, typeBox, dynamicOptionsBox, backButton);

        root.getChildren().clear();
        root.getChildren().add(configCard);
    }

    /**
     * Genera le opzioni per la SIMULAZIONE
     */
    private void buildSimulationMenu(VBox container) {
        // Regole alternative
        CheckBox stackCheck = new CheckBox("Attiva Stacking Carte Pesca (+2/+4)");
        CheckBox rushCheck = new CheckBox("Attiva Number Rush");

        // Numero giocatori (Bot)
        HBox playersBox = new HBox(10);
        playersBox.setAlignment(Pos.CENTER);
        Label playersLabel = new Label("Numero Bot:");
        playersLabel.getStyleClass().add("menu-label");
        
        ComboBox<Integer> playersCombo = new ComboBox<>();
        playersCombo.getItems().addAll(2, 3, 4, 5, 6);
        playersCombo.setValue(2);
        playersBox.getChildren().addAll(playersLabel, playersCombo);

        // Contenitore per i nomi dei bot
        VBox namesBox = new VBox(8);
        namesBox.setAlignment(Pos.CENTER);
        updateSimulationFields(namesBox, 2); // Inizializza con 2 campi di testo

        // Listener per aggiungere/rimuovere i campi in base al numero di bot scelto
        playersCombo.setOnAction(e -> updateSimulationFields(namesBox, playersCombo.getValue()));

        Button startSimButton = new Button("AVVIA SIMULAZIONE");
        startSimButton.getStyleClass().add("menu-button");
        startSimButton.setOnAction(e -> {
            System.out.println("Avvio Simulazione con " + playersCombo.getValue() + " bot.");
            System.out.println("Stacking: " + stackCheck.isSelected() + " | Number Rush: " + rushCheck.isSelected());
            
            namesBox.getChildren().forEach(node -> {
                if(node instanceof HBox) {
                    TextField tf = (TextField) ((HBox) node).getChildren().get(1);
                    @SuppressWarnings("unchecked")
                    ComboBox<String> botCategory = (ComboBox<String>) ((HBox) node).getChildren().get(2);
                    System.out.println("Nome Bot: " + tf.getText() + " | Categoria: " + botCategory.getValue());
                }
            });
            
            SimulationEngine simulation = buildSimulationEngine(namesBox, playersCombo.getValue(), stackCheck, rushCheck);
        });

        container.getChildren().addAll(stackCheck, rushCheck, playersBox, namesBox, startSimButton);
    }
    
    private SimulationEngine buildSimulationEngine(VBox names, int numPlayers, CheckBox stackWild, CheckBox numberRush) {
        java.util.List<String> playerNames = new java.util.ArrayList<>();
        java.util.List<String> botCategories = new java.util.ArrayList<>();
        BotPlayer[] playerList = new BotPlayer[numPlayers];
        
        names.getChildren().forEach(node -> {
            if(node instanceof HBox) {
                HBox row = (HBox) node;
                
                // Indice 1: TextField Nome, Indice 2: ComboBox Categoria Bot
                TextField nameField = (TextField) row.getChildren().get(1);
                @SuppressWarnings("unchecked")
                ComboBox<String> categoryCombo = (ComboBox<String>) row.getChildren().get(2);
                
                playerNames.add(nameField.getText());
                botCategories.add(categoryCombo.getValue());
            }
        });
        
        for(int i = 0; i < numPlayers; i++) {
            if(botCategories.get(i).equals("Random")) {
                playerList[i] = new RandomBot(playerNames.get(i), i);
            }
            else if(botCategories.get(i).equals("Conservativo")) {
                playerList[i] = new ConservativeBot(playerNames.get(i), i);
            }
            else if(botCategories.get(i).equals("Aggressivo")) {
                playerList[i] = new AggressiveBot(playerNames.get(i), i);
            }
        };
        
        // crea la modalità di gioco (singola)
        GameMode gameMode = new GameMode(false);
       
        // crea le regole di gioco
        RuleSet ruleSet = new RuleSet(stackWild.isSelected(), numberRush.isSelected());
        
        SimulationEngine simulation = new SimulationEngine(playerList, gameMode, ruleSet);
        return simulation;
    }
        

    /**
     * Genera le opzioni per la PARTITA NORMALE (con campi di testo dinamici)
     */
    private void buildNormalMenu(VBox container) {
        // Modalità Singola / A punti
        HBox modeBox = new HBox(10);
        modeBox.setAlignment(Pos.CENTER);
        Label modeLabel = new Label("Modalità:");
        modeLabel.getStyleClass().add("menu-label");
        
        ComboBox<String> modeCombo = new ComboBox<>();
        modeCombo.getItems().addAll("Singola", "A punti");
        modeCombo.setValue("Singola");
        modeBox.getChildren().addAll(modeLabel, modeCombo);

        // Contenitore per la soglia di vittoria (appare solo se si sceglie "A punti")
        VBox thresholdBox = new VBox();
        thresholdBox.setAlignment(Pos.CENTER);

        // Listener che aggiunge o rimuove il campo soglia in base alla modalità
        modeCombo.setOnAction(e -> {
            thresholdBox.getChildren().clear();
            if ("A punti".equals(modeCombo.getValue())) {
                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER);
                Label thresholdLabel = new Label("Soglia di Vittoria:");
                thresholdLabel.getStyleClass().add("menu-label");
                
                TextField thresholdField = new TextField("500");
                thresholdField.setMaxWidth(80);
                
                row.getChildren().addAll(thresholdLabel, thresholdField);
                thresholdBox.getChildren().add(row);
            }
        });

        // Regole alternative
        CheckBox stackCheck = new CheckBox("Attiva Stacking Carte Pesca (+2/+4)");
        CheckBox rushCheck = new CheckBox("Attiva Number Rush");

        // Numero giocatori
        HBox playersBox = new HBox(10);
        playersBox.setAlignment(Pos.CENTER);
        Label playersLabel = new Label("Numero Giocatori:");
        playersLabel.getStyleClass().add("menu-label");
        
        ComboBox<Integer> playersCombo = new ComboBox<>();
        playersCombo.getItems().addAll(2, 3, 4, 5, 6);
        playersCombo.setValue(2);
        playersBox.getChildren().addAll(playersLabel, playersCombo);

        // Contenitore per le impostazioni dei singoli giocatori
        VBox namesBox = new VBox(8);
        namesBox.setAlignment(Pos.CENTER);
        updateNormalFields(namesBox, 2); // Inizializza con 2 campi di testo

        // Listener per aggiungere/rimuovere i campi di testo in base al numero scelto
        playersCombo.setOnAction(e -> updateNormalFields(namesBox, playersCombo.getValue()));

        Button startNormalButton = new Button("AVVIA PARTITA");
        startNormalButton.getStyleClass().add("menu-button");
        startNormalButton.setOnAction(e -> {
            System.out.println("Avvio Partita Normale (" + modeCombo.getValue() + ") con " + playersCombo.getValue() + " giocatori.");
            System.out.println("Stacking: " + stackCheck.isSelected() + " | Number Rush: " + rushCheck.isSelected());
            
            namesBox.getChildren().forEach(node -> {
                if(node instanceof HBox) {
                    @SuppressWarnings("unchecked")
                    ComboBox<String> playerType = (ComboBox<String>) ((HBox) node).getChildren().get(1);
                    TextField tf = (TextField) ((HBox) node).getChildren().get(2);
                    @SuppressWarnings("unchecked")
                    ComboBox<String> botCategory = (ComboBox<String>) ((HBox) node).getChildren().get(3);
                    
                    String tipo = playerType.getValue();
                    if ("Bot".equals(tipo)) {
                        tipo += " (" + botCategory.getValue() + ")";
                    }
                    System.out.println("Giocatore: " + tf.getText() + " | Tipo: " + tipo);
                }
            });
            
            // Passa i parametri configurati nell'UI al Game Engine
            GameEngine game = buildGameEngine(namesBox, playersCombo.getValue(), modeCombo, thresholdBox, stackCheck, rushCheck);
            game.initializeRound();
            
            BoardView board = new BoardView(game);
            root.getChildren().clear();
            root.getChildren().add(board);
        });

        container.getChildren().addAll(modeBox, thresholdBox, stackCheck, rushCheck, playersBox, namesBox, startNormalButton);
    }
    
    /**
     * Questa classe passa i vari parametri configurati nell'interfaccia al GameEngine
     */
    private GameEngine buildGameEngine(VBox names, int numPlayers, ComboBox<String> mode, VBox valueThreshold, CheckBox stackWild, CheckBox numberRush) {
        // crea la lista di giocatori
        java.util.List<String> playerNames = new java.util.ArrayList<>();
        java.util.List<String> playerTypes = new java.util.ArrayList<>();
        java.util.List<String> botCategories = new java.util.ArrayList<>();
        
        names.getChildren().forEach(node -> {
            if(node instanceof HBox) {
                HBox row = (HBox) node;
                
                @SuppressWarnings("unchecked")
                ComboBox<String> playerTypeCombo = (ComboBox<String>) row.getChildren().get(1);
                TextField nameField = (TextField) row.getChildren().get(2);
                @SuppressWarnings("unchecked")
                ComboBox<String> categoryCombo = (ComboBox<String>) row.getChildren().get(3);
                
                playerTypes.add(playerTypeCombo.getValue());
                playerNames.add(nameField.getText());
                botCategories.add(categoryCombo.getValue());
            }
        });
        
        Player[] playerList = new Player[numPlayers];
        for(int i = 0; i < numPlayers; i++) {
            if (playerTypes.get(i).equals("Umano")) {
                playerList[i] = new HumanPlayer(playerNames.get(i), i);
            } else if("Bot".equals(playerTypes.get(i))){
                if(botCategories.get(i).equals("Random")) {
                    playerList[i] = new RandomBot(playerNames.get(i), i);
                }
                else if(botCategories.get(i).equals("Conservativo")) {
                    playerList[i] = new ConservativeBot(playerNames.get(i), i);
                }
                else if(botCategories.get(i).equals("Aggressivo")) {
                    playerList[i] = new AggressiveBot(playerNames.get(i), i);
                }
            };
        }
        
        // crea la modalità di gioco (singola o a punti)
        GameMode gameMode;
        if(mode.getValue().equals("Singola")) {
            gameMode = new GameMode(false);
        } else {
            HBox row = (HBox) valueThreshold.getChildren().get(0);
            TextField pointValue = (TextField) row.getChildren().get(1);
            int threshold = 500; // Valore di default in caso di input non valido
            try {
                threshold = Integer.parseInt(pointValue.getText().trim());
            } catch (NumberFormatException e) {
                System.err.println("Soglia punti non valida. Impostato valore di default (500).");
            }
            gameMode = new GameMode(true, threshold);
        }
        
        // crea le regole di gioco
        RuleSet ruleSet = new RuleSet(stackWild.isSelected(), numberRush.isSelected());
        
        GameEngine game = new GameEngine(playerList, gameMode, ruleSet);
        return game;
    }

    /**
     * Metodo di supporto per generare le TextField per i nomi dei BOT nella Simulazione
     */
    private void updateSimulationFields(VBox container, int numPlayers) {
        container.getChildren().clear(); 
        for (int i = 1; i <= numPlayers; i++) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER);
            
            Label label = new Label("Nome Bot " + i + ":");
            label.getStyleClass().add("menu-label");
            
            TextField nameField = new TextField("Bot " + i);
            nameField.setMaxWidth(120);
            
            ComboBox<String> categoryCombo = new ComboBox<>();
            categoryCombo.getItems().addAll("Conservativo", "Aggressivo", "Random");
            categoryCombo.setValue("Random");
            
            row.getChildren().addAll(label, nameField, categoryCombo);
            container.getChildren().add(row);
        }
    }

    /**
     * Metodo di supporto per generare i controlli (Tipo e Nome) nella Partita Normale
     */
    private void updateNormalFields(VBox container, int numPlayers) {
        container.getChildren().clear(); 
        for (int i = 1; i <= numPlayers; i++) {
            final int playerIndex = i; // Necessario per usarlo nella lambda del listener
            
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER);
            
            Label label = new Label("G" + playerIndex + ":");
            label.getStyleClass().add("menu-label");
            
            ComboBox<String> typeCombo = new ComboBox<>();
            typeCombo.getItems().addAll("Umano", "Bot");
            typeCombo.setValue("Umano");
            
            TextField nameField = new TextField("Giocatore " + playerIndex);
            nameField.setMaxWidth(120);
            
            ComboBox<String> categoryCombo = new ComboBox<>();
            categoryCombo.getItems().addAll("Conservativo", "Aggressivo", "Random");
            categoryCombo.setValue("Random");
            categoryCombo.setDisable(true); // Disabilitato di default se il giocatore è Umano
            
            // Aggiorna il nome suggerito e lo stato del menu a tendina se l'utente cambia il tipo
            typeCombo.setOnAction(e -> {
                if ("Bot".equals(typeCombo.getValue())) {
                    nameField.setText("Bot " + playerIndex);
                    categoryCombo.setDisable(false); // Abilita scelta categoria
                } else {
                    nameField.setText("Giocatore " + playerIndex);
                    categoryCombo.setDisable(true); // Disabilita scelta categoria
                }
            });
            
            row.getChildren().addAll(label, typeCombo, nameField, categoryCombo);
            container.getChildren().add(row);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

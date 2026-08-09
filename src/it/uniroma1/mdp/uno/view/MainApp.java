package it.uniroma1.mdp.uno.view;

import java.net.URL;

import it.uniroma1.mdp.uno.model.game.GameMode;
import it.uniroma1.mdp.uno.model.player.AggressiveBot;
import it.uniroma1.mdp.uno.model.player.ConservativeBot;
import it.uniroma1.mdp.uno.model.player.HumanPlayer;
import it.uniroma1.mdp.uno.model.player.Player;
import it.uniroma1.mdp.uno.model.player.Player.PlayerType;
import it.uniroma1.mdp.uno.model.player.RandomBot;
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
        root.getStyleClass().add("menu-bg"); //mette l'immagine come sfondo

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
        VBox menuBox = new VBox(30);
        menuBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("UNO");
        titleLabel.setStyle("-fx-font-size: 50px; -fx-text-fill: white; -fx-font-weight: bold;");

        Button startButton = new Button("INIZIA PARTITA");
        startButton.setStyle("-fx-font-size: 20px; -fx-padding: 15 30; -fx-font-weight: bold;");
        
        // Al click, passa al menu di configurazione
        startButton.setOnAction(e -> showMenuConfig());

        menuBox.getChildren().addAll(titleLabel, startButton);
        
        // Aggiorna la root
        root.getChildren().clear();
        root.getChildren().add(menuBox);
    }

    /**
     * Schermata 2: Scelta tra Simulazione e Partita Normale
     */
    private void showMenuConfig() {
        VBox configBox = new VBox(20);
        configBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("IMPOSTAZIONI PARTITA");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox typeBox = new HBox(10);
        typeBox.setAlignment(Pos.CENTER);
        Label typeLabel = new Label("Tipo di Partita:");
        typeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
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
        backButton.setOnAction(e -> showMainMenu());

        configBox.getChildren().addAll(titleLabel, typeBox, dynamicOptionsBox, backButton);

        root.getChildren().clear();
        root.getChildren().add(configBox);
    }

    /**
     * Genera le opzioni per la SIMULAZIONE
     */
    private void buildSimulationMenu(VBox container) {
        // Regole alternative
        CheckBox stackCheck = new CheckBox("Attiva Stacking Carte Pesca (+2/+4)");
        stackCheck.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        CheckBox rushCheck = new CheckBox("Attiva Number Rush");
        rushCheck.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Numero giocatori (Bot)
        HBox playersBox = new HBox(10);
        playersBox.setAlignment(Pos.CENTER);
        Label playersLabel = new Label("Numero Bot:");
        playersLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        ComboBox<Integer> playersCombo = new ComboBox<>();
        playersCombo.getItems().addAll(2, 3, 4, 5, 6);
        playersCombo.setValue(2);
        playersBox.getChildren().addAll(playersLabel, playersCombo);

        // Contenitore per i nomi dei bot
        VBox namesBox = new VBox(5);
        namesBox.setAlignment(Pos.CENTER);
        updateSimulationFields(namesBox, 2); // Inizializza con 2 campi di testo

        // Listener per aggiungere/rimuovere i campi in base al numero di bot scelto
        playersCombo.setOnAction(e -> updateSimulationFields(namesBox, playersCombo.getValue()));

        Button startSimButton = new Button("AVVIA SIMULAZIONE");
        startSimButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
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
            // TODO: Passare i dati a SimulationConfig
        });

        container.getChildren().addAll(stackCheck, rushCheck, playersBox, namesBox, startSimButton);
    }

    /**
     * Genera le opzioni per la PARTITA NORMALE (con campi di testo dinamici)
     */
    private void buildNormalMenu(VBox container) {
        // Modalità Singola / A punti
        HBox modeBox = new HBox(10);
        modeBox.setAlignment(Pos.CENTER);
        Label modeLabel = new Label("Modalità:");
        modeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
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
                thresholdLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
                
                TextField thresholdField = new TextField("500");
                thresholdField.setMaxWidth(80);
                
                row.getChildren().addAll(thresholdLabel, thresholdField);
                thresholdBox.getChildren().add(row);
            }
        });

        // Regole alternative
        CheckBox stackCheck = new CheckBox("Attiva Stacking Carte Pesca (+2/+4)");
        stackCheck.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        CheckBox rushCheck = new CheckBox("Attiva Number Rush");
        rushCheck.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");

        // Numero giocatori
        HBox playersBox = new HBox(10);
        playersBox.setAlignment(Pos.CENTER);
        Label playersLabel = new Label("Numero Giocatori:");
        playersLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        ComboBox<Integer> playersCombo = new ComboBox<>();
        playersCombo.getItems().addAll(2, 3, 4, 5, 6);
        playersCombo.setValue(2);
        playersBox.getChildren().addAll(playersLabel, playersCombo);

        // Contenitore per le impostazioni dei singoli giocatori
        VBox namesBox = new VBox(5);
        namesBox.setAlignment(Pos.CENTER);
        updateNormalFields(namesBox, 2); // Inizializza con 2 campi di testo

        // Listener per aggiungere/rimuovere i campi di testo in base al numero scelto
        playersCombo.setOnAction(e -> updateNormalFields(namesBox, playersCombo.getValue()));

        Button startNormalButton = new Button("AVVIA PARTITA");
        startNormalButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 20;");
        startNormalButton.setOnAction(e -> {
            System.out.println("Avvio Partita Normale (" + modeCombo.getValue() + ") con " + playersCombo.getValue() + " giocatori.");
            
            // Lettura del valore della soglia se presente
            if ("A punti".equals(modeCombo.getValue()) && !thresholdBox.getChildren().isEmpty()) {
                HBox row = (HBox) thresholdBox.getChildren().get(0);
                TextField tf = (TextField) row.getChildren().get(1);
                System.out.println("Soglia di Vittoria: " + tf.getText());
            }

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
            
            //TODO: Passare i parametri al GameEngine
            buildGameEngine(namesBox, playersCombo.getValue(), modeCombo, thresholdBox);
        });

        container.getChildren().addAll(modeBox, thresholdBox, stackCheck, rushCheck, playersBox, namesBox, startNormalButton);
    }
    
    /**
     * Questa classe passa i vari parametri configurati nell'interfaccia al GameEngine
     * @param names i nomi dei giocatori inseriti
     * @param numPlayers il numero di giocatori
     */
    private void buildGameEngine(VBox names, int numPlayers, ComboBox mode, VBox valueTreshold) {
    	java.util.List<String> playerNames = new java.util.ArrayList<>();
        java.util.List<String> playerTypes = new java.util.ArrayList<>();
        java.util.List<String> botCategories = new java.util.ArrayList<>();
        names.getChildren().forEach(node -> {
            if(node instanceof HBox) {
                HBox row = (HBox) node;
                
                // L'indice 1 è la ComboBox (Umano/Bot), l'indice 2 è il TextField (Nome), l'indice 3 è la categoria Bot
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
        	if (playerTypes.get(i) == "Umano") {
        		playerList[i] = new HumanPlayer(playerNames.get(i), i);
        	} else if(playerTypes.get(i) == "Bot"){
        		if(botCategories.get(i) == "Random") {
        			playerList[i] = new RandomBot(playerNames.get(i), i);
        		}
        		else if(botCategories.get(i) == "Conservativo") {
        			playerList[i] = new ConservativeBot(playerNames.get(i), i);
        		}
        		else if(botCategories.get(i) == "Aggressivo") {
        			playerList[i] = new AggressiveBot(playerNames.get(i), i);
        		}
        	};
        }
        
        if(mode.getValue() == "Singola") {
        	GameMode gameMode = new GameMode(false);
        } else {
        	GameMode gameMode = new GameMode(true, Integer.parseInt(valueTreshold.getText()));
        }
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
            label.setStyle("-fx-text-fill: white;");
            
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
            label.setStyle("-fx-text-fill: white;");
            
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
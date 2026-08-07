package it.uniroma1.mdp.uno.view;

import java.net.URL;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        
        VBox menuBox = new VBox(20); // 20 pixel di spazio
        menuBox.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("MENU PRINCIPALE - UNO");
        titleLabel.setStyle("-fx-font-size: 30px; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox modeBox = new HBox(10);
        modeBox.setAlignment(Pos.CENTER);
        Label modeLabel = new Label("Modalità:");
        modeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        ComboBox<String> modeCombo = new ComboBox<>();
        modeCombo.getItems().addAll("Singola", "A punti");
        modeCombo.setValue("Singola");
        modeBox.getChildren().addAll(modeLabel, modeCombo);

        HBox playersBox = new HBox(10);
        playersBox.setAlignment(Pos.CENTER);
        Label playersLabel = new Label("Numero Giocatori:");
        playersLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        ComboBox<Integer> playersCombo = new ComboBox<>();
        playersCombo.getItems().addAll(2, 3, 4, 5, 6);
        playersCombo.setValue(2);
        playersBox.getChildren().addAll(playersLabel, playersCombo);

        Button startButton = new Button("AVVIA PARTITA");
        startButton.setStyle("-fx-font-size: 18px; -fx-padding: 10 20;");
        startButton.setOnAction(e -> {
            System.out.println("Sta per cominciare una partita " + modeCombo.getValue() + " con " + playersCombo.getValue() + " giocatori");
        });

        menuBox.getChildren().addAll(titleLabel, modeBox, playersBox, startButton);

        StackPane root = new StackPane(menuBox);
        root.getStyleClass().add("menu-bg"); // Applica lo sfondo impostato nel CSS

        Scene scene = new Scene(root, 1024, 768);
        
        URL cssUrl = getClass().getResource("/resources/style.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        primaryStage.setTitle("UNO Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
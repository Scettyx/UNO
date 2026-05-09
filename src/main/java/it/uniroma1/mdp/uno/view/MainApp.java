package it.uniroma1.mdp.uno.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Prova UNO");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("UNO Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
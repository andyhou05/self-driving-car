/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.main;

import edu.vanier.selfdriving.controllers.FXMLGamemodeController;
import edu.vanier.selfdriving.controllers.FXMLLevelPickerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author USER
 */
public class Main extends Application {

    public static Scene scene;
    
    FXMLLoader gamemodeLoader = new FXMLLoader(getClass().getResource("/fxml/gamemode.fxml"));
    FXMLGamemodeController gamemodeController = new FXMLGamemodeController(scene);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        gamemodeLoader.setController(gamemodeController);

        Parent root = gamemodeLoader.load();

        scene = new Scene(root, 1100, 800);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        // We just need to bring the main window to front.
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setResizable(false);
    }
}
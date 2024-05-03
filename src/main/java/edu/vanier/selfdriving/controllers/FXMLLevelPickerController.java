/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.main.Main;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author USER
 */
public class FXMLLevelPickerController {

    @FXML
    Button btnLevel1;
    @FXML
    Button btnLevel2;
    @FXML
    Button btnLevel3;

    FXMLLoader levelLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
    FXMLGameControllerAI levelController = new FXMLGameControllerAI();

    EventHandler<ActionEvent> gameSwitch = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                Main.scene.setRoot(levelLoader.load());
                levelController.loadGame();
            } catch (IOException ex) {
                Logger.getLogger(FXMLLevelPickerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    public FXMLLevelPickerController() {
        levelLoader.setController(levelController);
    }

    @FXML
    public void initialize() {
        btnLevel1.setOnAction(gameSwitch);

        btnLevel2.setOnAction(gameSwitch);

        btnLevel3.setOnAction(gameSwitch);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.main.Main;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

/**
 *
 * @author USER
 */
public class FXMLGamemodeController {

    Scene scene;

    @FXML
    Button btnPlay;
    @FXML
    Button btnAiVs;
    @FXML
    Button btnAiPlay;

    FXMLLoader levelLoader = new FXMLLoader(getClass().getResource("/fxml/levels.fxml"));
    FXMLLevelsController levelsController = new FXMLLevelsController();

    EventHandler<ActionEvent> clickEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                Main.scene.setRoot(levelLoader.load());
            } catch (IOException ex) {
                Logger.getLogger(FXMLGamemodeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    public FXMLGamemodeController(Scene scene) {
        this.scene = scene;
        levelLoader.setController(levelsController);
    }

    @FXML
    public void initialize() {
        btnPlay.setOnAction(clickEvent);

        btnAiVs.setOnAction(clickEvent);

        btnAiPlay.setOnAction(clickEvent);
    }
}

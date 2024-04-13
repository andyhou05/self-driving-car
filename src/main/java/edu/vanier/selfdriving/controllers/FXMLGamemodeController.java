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

    FXMLLoader levelPickerLoader = new FXMLLoader(getClass().getResource("/fxml/levels.fxml"));
    FXMLLevelPickerController levelPickerController = new FXMLLevelPickerController();

    EventHandler<ActionEvent> clickEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                Main.scene.setRoot(levelPickerLoader.load());
            } catch (IOException ex) {
                Logger.getLogger(FXMLGamemodeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    public FXMLGamemodeController(Scene scene) {
        this.scene = scene;
        levelPickerLoader.setController(levelPickerController);
    }

    @FXML
    public void initialize() {
        btnPlay.setOnAction(clickEvent);

        btnAiVs.setOnAction(clickEvent);

        btnAiPlay.setOnAction(clickEvent);
    }
}
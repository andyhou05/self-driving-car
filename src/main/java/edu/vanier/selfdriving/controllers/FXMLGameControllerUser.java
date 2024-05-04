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
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 *
 * @author USER
 */
public class FXMLGameControllerUser{
    @FXML
    Pane roadPane;
    @FXML
    Pane controlsPane;
    @FXML
    Button btnReset;
    @FXML
    Button btnReturn;

    FXMLLoader levelPickerLoader = new FXMLLoader(getClass().getResource("/fxml/levels.fxml"));
    GameController gameControllerUser;

    public void loadGame() {
        gameControllerUser = new GameController(new CarSpawner(), roadPane);
    }
    
    @FXML
    public void initialize() {
        levelPickerLoader.setController(new FXMLLevelPickerController());
        btnReset.setOnAction(resetEvent);
        btnReturn.setOnAction(returnEvent);
    }

    EventHandler<ActionEvent> returnEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                Main.scene.setRoot(levelPickerLoader.load());
            } catch (IOException ex) {
                Logger.getLogger(FXMLGamemodeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };

    EventHandler<ActionEvent> resetEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            gameControllerUser.reset();
        }
    };
}

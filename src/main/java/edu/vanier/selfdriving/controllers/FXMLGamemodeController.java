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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author USER
 */
public class FXMLGamemodeController {

    Scene scene;
    MediaPlayer mediaPlayer;
    @FXML
    Button btnPlay;
    @FXML
    Button btnAiVs;
    @FXML
    Button btnAiPlay;
    @FXML
    Button carChooser;
    FXMLLoader levelPickerLoader = new FXMLLoader(getClass().getResource("/fxml/levels.fxml"));
    FXMLLevelPickerController levelPickerController = new FXMLLevelPickerController();
    FXMLLoader carsPickerLoader = new FXMLLoader(getClass().getResource("/fxml/cars.fxml"));
    FXMLCarsPickerController carsPickerController = new FXMLCarsPickerController();

    
    EventHandler<ActionEvent> carChooserClickEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                Main.scene.setRoot(carsPickerLoader.load());
            } catch (IOException ex) {
                Logger.getLogger(FXMLGamemodeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    };
    
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
        carsPickerLoader.setController(carsPickerController);
    }
    
    
    
    @FXML
    public void initialize() {
        btnPlay.setOnAction(clickEvent);

        btnAiVs.setOnAction(clickEvent);

        btnAiPlay.setOnAction(clickEvent);
        
        carChooser.setOnAction(carChooserClickEvent);
    }

    

}

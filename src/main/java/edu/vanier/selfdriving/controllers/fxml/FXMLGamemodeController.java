/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers.fxml;

import edu.vanier.selfdriving.controllers.GameController;
import edu.vanier.selfdriving.main.Main;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

/**
 * FXML Controller class for the game mode chooser scene.
 *
 * @author USER
 */
public class FXMLGamemodeController {

    @FXML
    Button btnPlay;
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

    EventHandler<ActionEvent> aiEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            chooseGamemode(false);
        }
    };
    EventHandler<ActionEvent> userEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            chooseGamemode(true);
        }
    };

    void chooseGamemode(boolean userControlled) {
        GameController.userControlled = userControlled;
        try {
            Main.scene.setRoot(levelPickerLoader.load());
        } catch (IOException ex) {
            Logger.getLogger(FXMLGamemodeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates an FXMLGamemodeController object, sets the controller of the
     * level picker and the car chooser.
     */
    public FXMLGamemodeController() {
        levelPickerLoader.setController(levelPickerController);
        carsPickerLoader.setController(carsPickerController);
    }

    /**
     * Initializes the JavaFX Components.
     */
    @FXML
    public void initialize() {
        btnPlay.setOnAction(userEvent);

        btnAiPlay.setOnAction(aiEvent);

        carChooser.setOnAction(carChooserClickEvent);
    }

}

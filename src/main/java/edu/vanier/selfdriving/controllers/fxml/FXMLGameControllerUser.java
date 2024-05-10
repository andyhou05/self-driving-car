/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers.fxml;

import edu.vanier.selfdriving.controllers.GameController;
import edu.vanier.selfdriving.controllers.SpawnerController;
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
 * FXML Controller class for the user controlled game mode.
 *
 * @author USER
 */
public class FXMLGameControllerUser extends FXMLGameController {

    @FXML
    Pane roadPane;
    @FXML
    Button btnReset;
    @FXML
    Button btnReturn;

    public FXMLGameControllerUser() {
        levelPickerLoader = new FXMLLoader(getClass().getResource("/fxml/levels.fxml"));
    }

    /**
     * Loads the AI controlled level.
     */
    @Override
    public void loadGame() {
        gameController = new GameController(new SpawnerController(), roadPane, level);
    }

    /**
     *
     */
    @FXML
    public void initialize() {
        levelPickerLoader.setController(new FXMLLevelPickerController());
        btnReset.setOnAction(resetEvent);
        btnReturn.setOnAction(returnEvent);
    }

    EventHandler<ActionEvent> returnEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            gameController.removeAllCars();
            gameController.camera.stop();
            gameController.getCarController().getAnimation().stop();
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
            gameController.reset();
        }
    };
}

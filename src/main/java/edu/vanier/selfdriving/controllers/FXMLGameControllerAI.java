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
 * Controller class of the MainApp's UI.
 *
 * @author
 */
public class FXMLGameControllerAI {

    @FXML
    Pane roadPane;
    @FXML
    Pane visualizerPane;
    @FXML
    Button btnReset;
    @FXML
    Button btnSave;
    @FXML
    Button btnHardReset;
    @FXML
    Button btnReturn;

    GameController gameControllerAI;
    FXMLLoader levelPickerLoader = new FXMLLoader(getClass().getResource("/fxml/levels.fxml"));

    @FXML
    public void initialize() {
        levelPickerLoader.setController(new FXMLLevelPickerController());
        btnReset.setOnAction(resetEvent);
        btnSave.setOnAction(saveEvent);
        btnHardReset.setOnAction(hardResetEvent);
        btnReturn.setOnAction(returnEvent);
    }

    public void loadGame() {
        gameControllerAI = new GameController(new CarSpawner(), roadPane, visualizerPane);
    }

    EventHandler<ActionEvent> returnEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            gameControllerAI.removeAllCars();
            gameControllerAI.camera.stop();
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
            gameControllerAI.reset();
        }
    };
    EventHandler<ActionEvent> saveEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            gameControllerAI.saveBestNetwork();
            gameControllerAI.reset();
        }
    };
    EventHandler<ActionEvent> hardResetEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            gameControllerAI.hardResetNetwork();
            gameControllerAI.reset();
        }
    };

}

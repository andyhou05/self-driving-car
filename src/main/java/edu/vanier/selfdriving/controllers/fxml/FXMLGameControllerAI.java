package edu.vanier.selfdriving.controllers.fxml;

import edu.vanier.selfdriving.controllers.GameControllerAI;
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
 * Controller class of the AI controlled game mode.
 *
 * @author
 */
public class FXMLGameControllerAI extends FXMLGameController {

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

    public FXMLGameControllerAI() {
        levelPickerLoader = levelPickerLoader = new FXMLLoader(getClass().getResource("/fxml/levels.fxml"));
    }

    /**
     * Initialize all JavFX Components
     */
    @FXML
    public void initialize() {
        levelPickerLoader.setController(new FXMLLevelPickerController());
        btnReset.setOnAction(resetEvent);
        btnSave.setOnAction(saveEvent);
        btnHardReset.setOnAction(hardResetEvent);
        btnReturn.setOnAction(returnEvent);
    }

    /**
     * Loads the AI controlled level.
     */
    @Override
    public void loadGame() {
        gameController = new GameControllerAI(roadPane, visualizerPane, level);
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
    EventHandler<ActionEvent> saveEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            ((GameControllerAI) gameController).saveNetwork();
            gameController.reset();
        }
    };
    EventHandler<ActionEvent> hardResetEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            ((GameControllerAI) gameController).hardResetNetwork();
            gameController.reset();
        }
    };

}

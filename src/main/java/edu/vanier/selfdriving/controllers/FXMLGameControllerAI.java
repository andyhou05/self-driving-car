package edu.vanier.selfdriving.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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

    GameController gameControllerAI;
    @FXML
    public void initialize() {
        btnReset.setOnAction(resetEvent);
        btnSave.setOnAction(saveEvent);
        btnHardReset.setOnAction(hardResetEvent);
    }
    
    public void loadGame(){
        gameControllerAI = new GameController(new CarSpawner(), roadPane, visualizerPane);
    }

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

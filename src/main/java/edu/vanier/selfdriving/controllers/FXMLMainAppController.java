package edu.vanier.selfdriving.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class of the MainApp's UI.
 *
 * @author frostybee
 */
public class FXMLMainAppController {

    private final static Logger logger = LoggerFactory.getLogger(FXMLMainAppController.class);
    @FXML
    Button btnReset;
    @FXML
    public void initialize() {
        btnReset.setOnAction(resetEvent);
    }
    
    public EventHandler<ActionEvent> resetEvent;
}

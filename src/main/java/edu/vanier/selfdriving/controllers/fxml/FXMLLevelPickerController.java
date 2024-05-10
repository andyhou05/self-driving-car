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
 * FXML Controller class for the level picker scene.
 *
 * @author USER
 */
public class FXMLLevelPickerController {

    @FXML
    Button btnLevel1;
    @FXML
    Button btnLevel2;
    @FXML
    Button btnLevel3;
    @FXML
    Button returnButton;
    /**
     * Creates an object of both FXML of the AI play and the user play Creates
     * an object of both Controller of the AI play and the user play
     */
    FXMLLoader levelLoaderAI = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
    FXMLGameControllerAI levelControllerAI = new FXMLGameControllerAI();
    FXMLLoader levelLoaderUser = new FXMLLoader(getClass().getResource("/fxml/gameUser.fxml"));
    FXMLGameControllerUser levelControllerUser = new FXMLGameControllerUser();

    EventHandler<ActionEvent> switchLevel1 = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            selectLevel("1");
        }
    };
    EventHandler<ActionEvent> switchLevel2 = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            selectLevel("2");
        }
    };
    EventHandler<ActionEvent> switchLevel3 = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            selectLevel("3");
        }
    };

    /**
     * Selects the level and loads it depending on if its AI or User controlled.
     *
     * @param level
     */
    void selectLevel(String level) {
        FXMLGameController.level = level;
        if (GameController.userControlled) {
            chooseLevel(levelLoaderUser, levelControllerUser);
        } else {
            chooseLevel(levelLoaderAI, levelControllerAI);
        }
    }

    public FXMLLevelPickerController() {
        levelLoaderAI.setController(levelControllerAI);
        levelLoaderUser.setController(levelControllerUser);
    }

    /**
     * Loads the level based on a loader and controller.
     *
     * @param loader
     * @param controller
     */
    void chooseLevel(FXMLLoader loader, FXMLGameController controller) {
        try {
            Main.scene.setRoot(loader.load());
            controller.loadGame();
        } catch (IOException ex) {
            Logger.getLogger(FXMLLevelPickerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller This method is called after the FXML file has
     * been loaded
     */
    @FXML
    public void initialize() {
        //Create an object of both gamemdodeFXML and its associated Controller.
        //Set controller
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxml/gamemode.fxml"));
        FXMLGamemodeController menuController = new FXMLGamemodeController();
        menuLoader.setController(menuController);
        btnLevel1.setOnAction(switchLevel1);

        btnLevel2.setOnAction(switchLevel2);

        btnLevel3.setOnAction(switchLevel3);
        //Lambda loading the gamemode FXML when returnButton is clicked
        returnButton.setOnAction(e -> {
            try {
                Main.scene.setRoot(menuLoader.load());
            } catch (IOException ex) {
                Logger.getLogger(FXMLGamemodeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}

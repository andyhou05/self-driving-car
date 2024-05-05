/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers.fxml;

import edu.vanier.selfdriving.controllers.GameController;
import edu.vanier.selfdriving.controllers.SpawnerController;
import edu.vanier.selfdriving.controllers.fxml.FXMLGamemodeController;
import edu.vanier.selfdriving.controllers.fxml.FXMLGameControllerUser;
import edu.vanier.selfdriving.controllers.fxml.FXMLGameControllerAI;
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

    FXMLLoader levelLoaderAI = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
    FXMLGameControllerAI levelControllerAI = new FXMLGameControllerAI();
    FXMLLoader levelLoaderUser = new FXMLLoader(getClass().getResource("/fxml/gameUser.fxml"));
    FXMLGameControllerUser levelControllerUser = new FXMLGameControllerUser();
    

    EventHandler<ActionEvent> gameSwitch = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            if(GameController.userControlled){
                chooseLevel(levelLoaderUser, levelControllerUser);
            } else{
                chooseLevel(levelLoaderAI, levelControllerAI);
            }
        }
    };

    public FXMLLevelPickerController() {
        levelLoaderAI.setController(levelControllerAI);
        levelLoaderUser.setController(levelControllerUser);
    }

    void chooseLevel(FXMLLoader loader, FXMLGameControllerAI controller) {
        try {
            Main.scene.setRoot(loader.load());
            controller.loadGame();
        } catch (IOException ex) {
            Logger.getLogger(FXMLLevelPickerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void chooseLevel(FXMLLoader loader, FXMLGameControllerUser controller) {
        try {
            Main.scene.setRoot(loader.load());
            controller.loadGame();
        } catch (IOException ex) {
            Logger.getLogger(FXMLLevelPickerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void initialize() {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxml/gamemode.fxml"));
        FXMLGamemodeController menuController = new FXMLGamemodeController(Main.scene);
        menuLoader.setController(menuController);
        btnLevel1.setOnAction(gameSwitch);

        btnLevel2.setOnAction(gameSwitch);

        btnLevel3.setOnAction(gameSwitch);
        returnButton.setOnAction(e -> {
            try {
                Main.scene.setRoot(menuLoader.load());
            } catch (IOException ex) {
                Logger.getLogger(FXMLGamemodeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}

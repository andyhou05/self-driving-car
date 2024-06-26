package edu.vanier.selfdriving.controllers.fxml;

import edu.vanier.selfdriving.controllers.GameController;
import edu.vanier.selfdriving.main.Main;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author 2273410 Controller for car selection
 *
 */
public class FXMLCarsPickerController {

    //Static ints and Strings to keep track of the type & color of cars being displayed
    public static int typeCounter = 0;
    public static String[] listOfColors = {"black", "blue", "green", "red"};
    public static int[] listOfTypes = {1, 2, 3, 4};

    @FXML
    Button car1;
    @FXML
    Button car2;
    @FXML
    Button car3;
    @FXML
    Button car4;
    @FXML
    Button returnButton;
    @FXML
    ImageView firstImage;
    @FXML
    ImageView secondImage;
    @FXML
    ImageView thirdImage;
    @FXML
    ImageView forthImage;
    @FXML
    Button switchCars;

    /**
     * Updates the ImageView with the new type of car This method is for when
     * the user clicks the Next Models button
     */
    public void updateCars() {
        firstImage.setImage(new Image("/sprites/car_" + listOfColors[0] + "_" + listOfTypes[typeCounter % 4] + ".png"));
        secondImage.setImage(new Image("/sprites/car_" + listOfColors[1] + "_" + listOfTypes[typeCounter % 4] + ".png"));
        thirdImage.setImage(new Image("/sprites/car_" + listOfColors[2] + "_" + listOfTypes[typeCounter % 4] + ".png"));
        forthImage.setImage(new Image("/sprites/car_" + listOfColors[3] + "_" + listOfTypes[typeCounter % 4] + ".png"));
        GameController.carNumber = Integer.toString(listOfTypes[typeCounter % 4]);
    }

    /**
     * Initializes the controller This method is called after the FXML file has
     * been loaded
     */
    @FXML
    public void initialize() {
        FXMLLoader menuLoader = new FXMLLoader(getClass().getResource("/fxml/gamemode.fxml"));
        FXMLGamemodeController menuController = new FXMLGamemodeController();
        menuLoader.setController(menuController);
        updateCars();
        switchCars.setOnAction(e -> {
            typeCounter += 1;
            updateCars();
        });
        returnButton.setOnAction(e -> {
            try {
                Main.scene.setRoot(menuLoader.load());
            } catch (IOException ex) {
                Logger.getLogger(FXMLGamemodeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        car1.setOnAction(e -> {
            GameController.carColor = "black";
        });
        car2.setOnAction(e -> {
            GameController.carColor = "blue";
        });
        car3.setOnAction(e -> {
            GameController.carColor = "green";
        });
        car4.setOnAction(e -> {
            GameController.carColor = "red";
        });

    }
}

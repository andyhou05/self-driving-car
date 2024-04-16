package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 2273410
 */
public class FXMLCarsPickerController {
    public static int typeCounter = 0;
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
    
    
    public void updateCars(){
        
    }
    
    
    @FXML
    void initalize(){
        
        
        
        switchCars.setOnAction(e ->{
            typeCounter+=1;
            updateCars();
    });
        
        
    }
}

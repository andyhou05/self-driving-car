/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 *
 * @author USER
 */
public class FXMLLevelsController {
    @FXML
    Button btnLevel1;
    @FXML
    Button btnLevel2;
    @FXML
    Button btnLevel3;
    
    @FXML
    public void initialize(){
        btnLevel1.setOnAction((event) -> {
            System.out.println("1");
        });
        
        btnLevel2.setOnAction((event) -> {
            System.out.println("2");
        });
        
        btnLevel3.setOnAction((event) -> {
            System.out.println("3");
        });
    }
}

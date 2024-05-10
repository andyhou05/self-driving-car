/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers.fxml;

import edu.vanier.selfdriving.controllers.GameController;
import javafx.fxml.FXMLLoader;

/**
 * Abstract FXML Controller class for the different game modes
 *
 * @author USER
 */
public abstract class FXMLGameController {

    public GameController gameController;
    public FXMLLoader levelPickerLoader = new FXMLLoader(getClass().getResource("/fxml/levels.fxml"));

    /**
     * Keeps track of the level number.
     */
    public static String level;

    /**
     * Initialization of gameController after all the JavaFX Components are
     * initialized.
     */
    public abstract void loadGame();
}

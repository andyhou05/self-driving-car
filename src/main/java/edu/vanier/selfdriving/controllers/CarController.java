/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;

/**
 *
 * @author USER
 */
public class CarController {
    Car car;
    Scene scene;
    AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            car.carRectangle.setLayoutY(car.carRectangle.getLayoutY() - car.getSpeedY());
        }
    };

    public CarController(Car car) {
        this.car = car;
        scene = car.carRectangle.getScene();
        moveCar();
        animation.start();
    }
    
    
    
    public void moveCar(){
        scene.setOnKeyPressed((event) -> {
           switch(event.getCode()){
               case W:
                   car.setSpeedY(5);
                   break;
               case S:
                   car.setSpeedY(-5);
                   break;
           }
        });
        scene.setOnKeyReleased((event) -> {
           car.setSpeedY(0);
        });
    }
}

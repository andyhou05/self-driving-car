/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;

/**
 *
 * @author USER
 */
public class CarController {

    Car car;
    Scene scene;
    boolean forward = false;
    boolean backward = false;
    AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (forward) {
                car.acceleration(1);
            } else if (backward) {
                car.acceleration(-1);
            } else {
                car.decceleration();
            }
            car.carRectangle.setLayoutY(car.carRectangle.getLayoutY() - car.getSpeedY());
        }
    };

    public CarController(Car car) {
        this.car = car;
        scene = car.carRectangle.getScene();
        checkKeypress();
        animation.start();
    }

    public void checkKeypress() {
        scene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case W:
                    forward = true;
                    break;
                case S:
                    backward = true;
                    break;
            }
        });
        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()){
                case W:
                    forward = false;
                    break;
                case S:
                    backward = false;
                    break;
            }
        });
    }
}

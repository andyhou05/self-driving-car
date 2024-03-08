/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;

/**
 *
 * @author USER
 */
public class CarController {
    
    Car car;
    Scene scene;
    int accelerationDirection = 0;
    boolean accelerating = false;
    boolean turningRight = false;
    boolean turningLeft = false;
    boolean flipRotate = false;
    AnimationTimer animation = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (accelerating) {
                car.acceleration(accelerationDirection);
            } else {
                car.decceleration(accelerationDirection);
            }
            if (turningRight && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
                if (flipRotate) {
                    car.rotate(1);
                } else {
                    car.rotate(-1);
                }
            } else if (turningLeft && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
                if (flipRotate) {
                    car.rotate(-1);
                } else {
                    car.rotate(1);
                }
            }
            car.carRectangle.setLayoutY(car.carRectangle.getLayoutY() - car.getSpeedY());
            car.carRectangle.setLayoutX(car.carRectangle.getLayoutX() - car.getSpeedX());
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
                    flipRotate = false;
                    accelerating = true;
                    accelerationDirection = 1;
                    break;
                case S:
                    flipRotate = true;
                    accelerating = true;
                    accelerationDirection = -1;
                    break;
                case A:
                    turningLeft = true;
                    break;
                case D:
                    turningRight = true;
                    break;
            }
        });
        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case W:
                case S:
                    accelerating = false;
                    break;
                case A:
                    turningLeft = false;
                    break;
                case D:
                    turningRight = false;
                    break;
            }
        });
    }
}

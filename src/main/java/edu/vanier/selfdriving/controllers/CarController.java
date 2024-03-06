/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * @author USER
 */
public class CarController {

    Car car;
    Scene scene;
    boolean forward = false;
    boolean backward = false;
    boolean right = false;
    boolean left = false;
    boolean flip_rotate = false;
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
            if (right && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
                if (flip_rotate) {
                    car.rotate(1);
                } else {
                    car.rotate(-1);
                }
            } else if (left && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
                if (flip_rotate) {
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
                    flip_rotate = false;
                    forward = true;
                    break;
                case S:
                    flip_rotate = true;
                    backward = true;
                    break;
                case A:
                    left = true;
                    break;
                case D:
                    right = true;
                    break;
            }
        });
        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case W:
                    forward = false;
                    break;
                case S:
                    backward = false;
                    break;
                case A:
                    left = false;
                    break;
                case D:
                    right = false;
                    break;
            }
        });
    }
}

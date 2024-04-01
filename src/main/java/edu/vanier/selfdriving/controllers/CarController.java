/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.models.Sensor;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author USER
 */
public class CarController {

    Car car;
    ArrayList<Car> enemyCars = new ArrayList<>();
    Scene scene;
    int direction = 0;
    boolean accelerating = false;
    boolean turningRight = false;
    boolean turningLeft = false;
    boolean flipRotate = false;
    AnimationTimer animation = new AnimationTimer() {
        private long FPS = 120L;
        private long INTERVAL = 1000000000L / FPS;
        private long last = 0;

        // Reference: https://www.youtube.com/watch?v=CYUjjnoXdrM
        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                checkCollisions();
                updateCarSpeed();
                car.getCarStack().setLayoutY(car.getCarStack().getLayoutY() - car.getSpeedY());
                car.getCarStack().setLayoutX(car.getCarStack().getLayoutX() - car.getSpeedX());
                car.getSensors().updateSensors(car.getCarStack().getRotate());
                //System.out.println(Sensor.sensorStartX);
                //System.out.println(Sensor.sensorStartY);
                //System.out.println("");

                // Move enemy cars
                for (Car enemyCar : enemyCars) {
                    enemyCar.getCarStack().setLayoutY(enemyCar.getCarStack().getLayoutY() - enemyCar.getSpeedY());
                }
                last = now;
            }
        }
    };

    public CarController(Car car, ArrayList<Car> enemyCars) {
        this.car = car;
        this.enemyCars = enemyCars;
        scene = car.getCarImageView().getScene();
        checkKeypress();
        animation.start();
    }

    private boolean checkRoadCollision() {
        Road carRoad = car.getRoad();
        StackPane carStack = car.getCarStack();
        Rectangle hitbox = car.getHitBox();
        return carStack.localToParent(hitbox.getBoundsInParent()).intersects(carRoad.getLeftBorder().getBoundsInParent())
                || carStack.localToParent(hitbox.getBoundsInParent()).intersects(carRoad.getRightBorder().getBoundsInParent());
    }

    private boolean checkCarCollisions() {
        Rectangle hitbox = car.getHitBox();
        StackPane carStack = car.getCarStack();
        for (Car enemyCar : enemyCars) {
            Rectangle enemyHitbox = enemyCar.getHitBox();
            StackPane enemyStack = enemyCar.getCarStack();
            if (enemyStack.localToParent(enemyHitbox.getBoundsInParent()).intersects(carStack.localToParent(hitbox.getBoundsInParent()))) {
                return true;
            }
        }
        return false;
    }

    public void checkCollisions() {
        if (checkRoadCollision() || checkCarCollisions()) {
            car.setMaxSpeed(0);
        }
    }

    private void updateCarSpeed() {
        if (accelerating) {
            car.acceleration(direction);
        } else {
            if (car.isCarMoving()) {
                car.decceleration(direction);
            }
        }
        if (turningRight && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
            if (flipRotate) {
                rotate(1);
            } else {
                rotate(-1);
            }
        } else if (turningLeft && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
            if (flipRotate) {
                rotate(-1);
            } else {
                rotate(1);
            }
        }
    }

    public void rotate(int direction) {
        car.getCarStack().setRotate(car.getCarStack().getRotate() - 1 * direction);
    }

    public void checkKeypress() {
        scene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case W:
                    flipRotate = false;
                    accelerating = true;
                    direction = 1;
                    break;
                case S:
                    flipRotate = true;
                    accelerating = true;
                    direction = -1;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isAccelerating() {
        return accelerating;
    }

    public void setAccelerating(boolean accelerating) {
        this.accelerating = accelerating;
    }

    public boolean isTurningRight() {
        return turningRight;
    }

    public void setTurningRight(boolean turningRight) {
        this.turningRight = turningRight;
    }

    public boolean isTurningLeft() {
        return turningLeft;
    }

    public void setTurningLeft(boolean turningLeft) {
        this.turningLeft = turningLeft;
    }

    public boolean isFlipRotate() {
        return flipRotate;
    }

    public void setFlipRotate(boolean flipRotate) {
        this.flipRotate = flipRotate;
    }

    public AnimationTimer getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationTimer animation) {
        this.animation = animation;
    }

}

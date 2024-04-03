/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.models.Sensor;
import static edu.vanier.selfdriving.models.Sensor.sensorStartX;
import static edu.vanier.selfdriving.models.Sensor.sensorStartY;
import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import edu.vanier.selfdriving.utils.MathUtils;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

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
                moveCar(car);
                updateSensors(car.getCarStack().getRotate());

                // Move enemy cars
                for (Car enemyCar : enemyCars) {
                    moveCar(enemyCar);
                }

                // Sensor Readings
                double [] inputs = new double [car.getSensorCount()];
                for (int i = 0; i < car.getSensorCount(); i++) {
                    updateSensorReading(car.getSensors()[i]);
                    inputs[i] = car.getSensors()[i].getReading();
                }

                // Neural Network
                NeuralNetwork neuralNetwork = car.getNeuralNetwork();
                neuralNetwork.setInput(inputs);
                neuralNetwork.feedforward();
                for (int i = 0; i < neuralNetwork.getOutput().length; i++) {
                    double value = neuralNetwork.getOutput()[i];
                    if (value == 1) {
                        switch (i) {
                            case 0:
                                goForward();
                                break;
                            case 1:
                                goBackward();
                                break;
                            case 2:
                                goLeft();
                                break;
                            case 3:
                                goRight();
                                break;
                        }
                    }
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

    private void moveCar(Car car) {
        car.getCarStack().setLayoutY(car.getCarStack().getLayoutY() - car.getSpeedY());
        car.getCarStack().setLayoutX(car.getCarStack().getLayoutX() - car.getSpeedX());
    }
    
    /**
     * Update the position of the Sensors when the Car moves.
     * @param angle
     */
    public void updateSensors(double angle){
        for(int i = 0; i < car.getSensorCount(); i++){
            // Move the Sensor with the Car.
            Sensor sensor = car.getSensors()[i];
            Line sensorLine = sensor.getSensorLine();
            double sensorSpread = car.getSensorSpread();
            sensorLine.setLayoutX(car.getCarStack().getLayoutX());
            sensorLine.setLayoutY(car.getCarStack().getLayoutY());
            
            // Update the angle of the Sensor with the angle of the Car.
            double rayAngle = MathUtils.lerp(sensorSpread / 2, -sensorSpread / 2, (double) i / (car.getSensorCount() - 1)) - (angle * Math.PI/180);
            double startX = sensorLine.getStartX();
            double startY = sensorLine.getStartY();
            double endX = startX - Math.sin(rayAngle) * Sensor.getSensorLength();
            double endY = startY - Math.cos(rayAngle) * Sensor.getSensorLength();
            sensorLine.setEndX(endX);
            sensorLine.setEndY(endY);
            sensorStartX = startX+car.getCarStack().getLayoutX();
            sensorStartY = startY+car.getCarStack().getLayoutY()+car.getCarStack().getTranslateY();
        }
    }

    private void updateSensorReading(Sensor sensor) {
        // Loop through enemy cars, if there is a reading with one or more of them, keep the highest
        Line sensorLine = sensor.getSensorLine();
        double enemyCarReading = 0;
        for (Car enemyCar : enemyCars) {
            enemyCarReading = Math.max(enemyCarReading, getSensorCarReading(sensor, enemyCar));
        }
        // Set the raeding of the sensor to be the highest reading between enemyCar and border
        double newReading = Math.max(getSensorBorderReading(sensor), enemyCarReading);
        sensor.setReading(newReading);
    }

    private double getSensorBorderReading(Sensor sensor) {
        Line sensorLine = sensor.getSensorLine();
        Line leftBorder = car.getRoad().getLeftBorder();
        Line rightBorder = car.getRoad().getRightBorder();
        double sensorLength = Sensor.getSensorLength();
        double position_x = car.getCarStack().getLayoutX() + 0.5 * car.getCarWidth();
        double position_y = car.getyPosition() + 0.5 * car.getCarLength();

        // Create a shape between intersection of sensor and left and right borders.
        Shape leftIntersection = Shape.intersect(sensorLine, leftBorder);
        Shape rightIntersection = Shape.intersect(sensorLine, rightBorder);

        // Get the x/y positions of the intersection shape
        double leftIntersection_x = leftIntersection.getBoundsInParent().getMaxX(); // If there is no intersection, this returns a shape with maxX == -1
        double leftIntersection_y = leftIntersection.getBoundsInParent().getMaxY();
        double rightIntersection_x = rightIntersection.getBoundsInParent().getMinX(); // If there is no intersection, this returns a shape with minX == 0
        double rightIntersection_y = rightIntersection.getBoundsInParent().getMaxY();

        if (leftIntersection_x != -1) {
            // Distance between car and border
            double distance = Math.sqrt(Math.pow(position_x - leftIntersection_x, 2) + Math.pow(position_y - leftIntersection_y, 2));
            if (distance > sensorLength) {
                distance = sensorLength;
            }

            // Get the length of the intersection
            double delta = Math.abs(distance - sensorLength);

            // Return a reading between 0 - 1 (far - close)
            double reading = delta / sensorLength;
            return reading;
        } else if (rightIntersection_x != 0) {
            double distance = Math.sqrt(Math.pow(position_x - rightIntersection_x, 2) + Math.pow(position_y - rightIntersection_y, 2));
            if (distance > sensorLength) {
                distance = sensorLength;
            }
            double delta = Math.abs(distance - sensorLength);
            double reading = delta / sensorLength;
            return reading;
        } // If no intersection, reading is 0
        return 0;
    }

    private double getSensorCarReading(Sensor sensor, Car car) {
        Line sensorLine = sensor.getSensorLine();
        Shape intersection = Shape.intersect(sensorLine, car.getHitBox()); // If there is no intersection, this returns a shape with centerX == -0.5
        double sensorLength = Sensor.getSensorLength();
        double position_x = this.car.getCarStack().getLayoutX() + 0.5 * car.getCarWidth();
        double position_y = this.car.getyPosition() + 0.5 * car.getCarLength();
        double intersection_x = intersection.getBoundsInParent().getCenterX();
        double intersection_y = intersection.getBoundsInParent().getMaxY();
        if (intersection_x != -0.5) {
            double distance = Math.sqrt(Math.pow(position_x - intersection_x, 2) + Math.pow(position_y - intersection_y, 2));
            if (distance > sensorLength) {
                distance = sensorLength;
            }
            double delta = Math.abs(distance - sensorLength);
            double reading = delta / sensorLength;
            return reading;
        }

        return 0;
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
                    goForward();
                    break;
                case S:
                    goBackward();
                    break;
                case A:
                    goLeft();
                    break;
                case D:
                    goRight();
                    break;
            }
        });
        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case W:
                case S:
                    slowDown();
                    break;
                case A:
                    noLeftTurn();
                    break;
                case D:
                    noRightTurn();
                    break;
            }
        });
    }

    private void goForward() {
        flipRotate = false;
        accelerating = true;
        direction = 1;
    }

    private void goBackward() {
        flipRotate = true;
        accelerating = true;
        direction = -1;
    }

    private void goRight() {
        turningRight = true;
    }

    private void goLeft() {
        turningLeft = true;
    }

    private void slowDown() {
        accelerating = false;
    }

    private void noRightTurn() {
        turningRight = false;
    }

    private void noLeftTurn() {
        turningLeft = false;
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

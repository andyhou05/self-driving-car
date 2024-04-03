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

    ArrayList<Car> cars;
    ArrayList<Car> enemyCars = new ArrayList<>();
    Scene scene;
    AnimationTimer animation = new AnimationTimer() {
        private long FPS = 120L;
        private long INTERVAL = 1000000000L / FPS;
        private long last = 0;

        // Reference: https://www.youtube.com/watch?v=CYUjjnoXdrM
        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                // Move enemy cars
                for (Car enemyCar : enemyCars) {
                    moveCar(enemyCar);
                }
                for (Car car : cars) {
                    checkCollisions(car);
                    updateCarSpeed(car);
                    moveCar(car);
                    updateSensors(car);

                    // Sensor Readings
                    double[] inputs = new double[car.getSensorCount()];
                    for (int i = 0; i < car.getSensorCount(); i++) {
                        updateSensorReading(car, car.getSensors()[i]);
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
                                    goForward(car);
                                    break;
                                case 1:
                                    goBackward(car);
                                    break;
                                case 2:
                                    goLeft(car);
                                    break;
                                case 3:
                                    goRight(car);
                                    break;
                            }
                        }
                    }
                }
                last = now;

            }
        }
    };

    public CarController(ArrayList<Car> cars, ArrayList<Car> enemyCars) {
        this.cars = cars;
        this.enemyCars = enemyCars;
        scene = cars.get(0).getCarImageView().getScene();
        //checkKeypress();
        animation.start();
    }

    private void moveCar(Car car) {
        car.getCarStack().setLayoutY(car.getCarStack().getLayoutY() - car.getSpeedY());
        car.getCarStack().setLayoutX(car.getCarStack().getLayoutX() - car.getSpeedX());
    }

    /**
     * Update the position of the Sensors when the Car moves.
     *
     * @param angle
     */
    public void updateSensors(Car car) {
        double angle = car.getCarStack().getRotate();
        for (int i = 0; i < car.getSensorCount(); i++) {
            // Move the Sensor with the Car.
            Sensor sensor = car.getSensors()[i];
            Line sensorLine = sensor.getSensorLine();
            double sensorSpread = car.getSensorSpread();
            sensorLine.setLayoutX(car.getCarStack().getLayoutX());
            sensorLine.setLayoutY(car.getCarStack().getLayoutY());

            // Update the angle of the Sensor with the angle of the Car.
            double rayAngle = MathUtils.lerp(sensorSpread / 2, -sensorSpread / 2, (double) i / (car.getSensorCount() - 1)) - (angle * Math.PI / 180);
            double startX = sensorLine.getStartX();
            double startY = sensorLine.getStartY();
            double endX = startX - Math.sin(rayAngle) * Sensor.getSensorLength();
            double endY = startY - Math.cos(rayAngle) * Sensor.getSensorLength();
            sensorLine.setEndX(endX);
            sensorLine.setEndY(endY);
            sensorStartX = startX + car.getCarStack().getLayoutX();
            sensorStartY = startY + car.getCarStack().getLayoutY() + car.getCarStack().getTranslateY();
        }
    }

    private void updateSensorReading(Car car, Sensor sensor) {
        // Loop through enemy cars, if there is a reading with one or more of them, keep the highest
        double enemyCarReading = 0;
        for (Car enemyCar : enemyCars) {
            enemyCarReading = Math.max(enemyCarReading, getSensorCarReading(sensor, enemyCar));
        }
        // Set the raeding of the sensor to be the highest reading between enemyCar and border
        double newReading = Math.max(getSensorBorderReading(car, sensor), enemyCarReading);
        sensor.setReading(newReading);
    }

    private double getSensorBorderReading(Car car, Sensor sensor) {
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
        double position_x = car.getCarStack().getLayoutX() + 0.5 * car.getCarWidth();
        double position_y = car.getyPosition() + 0.5 * car.getCarLength();
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

    private boolean checkRoadCollision(Car car) {
        Road carRoad = car.getRoad();
        StackPane carStack = car.getCarStack();
        Rectangle hitbox = car.getHitBox();
        return carStack.localToParent(hitbox.getBoundsInParent()).intersects(carRoad.getLeftBorder().getBoundsInParent())
                || carStack.localToParent(hitbox.getBoundsInParent()).intersects(carRoad.getRightBorder().getBoundsInParent());
    }

    private boolean checkCarCollisions(Car car) {
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

    public void checkCollisions(Car car) {
        if (checkRoadCollision(car) || checkCarCollisions(car)) {
            car.setMaxSpeed(0);
        }
    }

    private void updateCarSpeed(Car car) {
        if (car.isAccelerating()) {
            car.acceleration(car.getDirection());
        } else {
            if (car.isCarMoving()) {
                car.decceleration(car.getDirection());
            }
        }
        if (car.isTurningRight() && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
            if (car.isFlipRotate()) {
                rotate(car, 1);
            } else {
                rotate(car, -1);
            }
        } else if (car.isTurningLeft() && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
            if (car.isFlipRotate()) {
                rotate(car, -1);
            } else {
                rotate(car, 1);
            }
        }
    }

    public void rotate(Car car, int direction) {
        car.getCarStack().setRotate(car.getCarStack().getRotate() - 1 * direction);
    }

    public void checkKeypress(Car car) {
        scene.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case W:
                    goForward(car);
                    break;
                case S:
                    goBackward(car);
                    break;
                case A:
                    goLeft(car);
                    break;
                case D:
                    goRight(car);
                    break;
            }
        });
        scene.setOnKeyReleased((event) -> {
            switch (event.getCode()) {
                case W:
                case S:
                    slowDown(car);
                    break;
                case A:
                    noLeftTurn(car);
                    break;
                case D:
                    noRightTurn(car);
                    break;
            }
        });
    }

    private void goForward(Car car) {
        car.setFlipRotate(false);
        car.setAccelerating(true);
        car.setDirection(1);
    }

    private void goBackward(Car car) {
        car.setFlipRotate(true);
        car.setAccelerating(true);
        car.setDirection(-1);
    }

    private void goRight(Car car) {
        car.setTurningRight(true);
    }

    private void goLeft(Car car) {
        car.setTurningLeft(true);
    }

    private void slowDown(Car car) {
        car.setAccelerating(false);
    }

    private void noRightTurn(Car car) {
        car.setTurningRight(false);
    }

    private void noLeftTurn(Car car) {
        car.setTurningLeft(false);
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCar(ArrayList<Car> cars) {
        this.cars = cars;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public AnimationTimer getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationTimer animation) {
        this.animation = animation;
    }

    public ArrayList<Car> getEnemyCars() {
        return enemyCars;
    }

    public void setEnemyCars(ArrayList<Car> enemyCars) {
        this.enemyCars = enemyCars;
    }

}

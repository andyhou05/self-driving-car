/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.main.Main;
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
 * Class that controls and updates all Car object properties such as their
 * movement, sensors, collisions, etc.
 *
 * @author USER
 */
public class CarController {

    ArrayList<Car> cars;
    ArrayList<Car> enemyCars = new ArrayList<>();
    Car carToFollow;
    Scene scene;
    boolean userControlled = true;

    // Animation that controls the cars (movement, sensors, neural network, collisions...)
    AnimationTimer animation = new AnimationTimer() {
        // Reference: https://www.youtube.com/watch?v=CYUjjnoXdrM
        private long FPS = 120L;
        private long INTERVAL = 1000000000L / FPS;
        private long last = 0;

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                // Move enemy cars
                for (Car enemyCar : enemyCars) {
                    moveCar(enemyCar);
                }

                // Update car properties
                for (int i = 0; i < cars.size(); i++) {
                    Car car = cars.get(i);
                    moveCar(car);
                    // If the current car is dead, no need to check for collisions/sensors
                    if (car.isDead()) {
                        continue;
                    }
                    checkCollisions(car);
                    updateCarSpeed(car);

                    /* Only for AI controlled cars: 
                        --  Neural Network Based Movement
                        --  Updating Sensors (readings, position)
                     */
                    if (!userControlled) {
                        updateNeuralNetwork(car);
                        updateSensorPositions(car);
                        // Update Sensor Readings and Inputs
                        for (int j = 0; j < car.getSensorCount(); j++) {
                            updateSensorReading(car.getSensors()[j]);
                            car.getInputs()[j] = car.getSensors()[j].getReading();
                        }
                    }
                }
                last = now;
            }
        }
    };

    /**
     * Creates an empty CarController object.
     */
    public CarController() {
    }

    /**
     * Creates a CarController with a list of cars and enemyCars, initializes
     * movement for the car based on userControlled.
     *
     * @param cars
     * @param enemyCars
     * @param userControlled True if we want to control car with keyboard
     * inputs. False if we want to control car with a neural network.
     */
    public CarController(ArrayList<Car> cars, ArrayList<Car> enemyCars, boolean userControlled) {
        this.cars = cars;
        this.enemyCars = enemyCars;
        this.userControlled = userControlled;
        carToFollow = cars.get(0);
        scene = Main.scene;
        if (userControlled) {
            checkKeypress(carToFollow);
        }
        animation.start();
    }

    private void moveCar(Car car) {
        car.getCarStack().setLayoutY(car.getCarStack().getLayoutY() - car.getSpeedY());
        car.getCarStack().setLayoutX(car.getCarStack().getLayoutX() - car.getSpeedX());
    }

    /**
     * Update the position of the Sensors when the Car moves.
     *
     * @param car
     */
    public void updateSensorPositions(Car car) {
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

    private void updateSensorReading(Sensor sensor) {
        // Loop through enemy cars, if there is a reading with one or more of them, keep the highest
        double enemyCarReading = 0;
        for (Car enemyCar : enemyCars) {
            enemyCarReading = Math.max(enemyCarReading, getSensorCarReading(sensor, enemyCar));
        }
        // Set the reading of the sensor to be the highest reading between enemyCar and border
        double newReading = Math.max(getSensorBorderReading(sensor), enemyCarReading);
        sensor.setReading(newReading);
    }

    private double getSensorBorderReading(Sensor sensor) {
        Car car = sensor.getCar();
        Line sensorLine = sensor.getSensorLine();
        Line leftBorder = car.getRoad().getLeftBorder();
        Line rightBorder = car.getRoad().getRightBorder();
        double sensorLength = Sensor.getSensorLength();
        double position_x = car.getCarStack().getBoundsInParent().getCenterX();
        double position_y = car.getCarStack().getBoundsInParent().getCenterY();

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
        double position_x = sensor.getCar().getCarStack().getBoundsInParent().getCenterX();
        double position_y = sensor.getCar().getCarStack().getBoundsInParent().getCenterY();
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

    /**
     * Checks for collisions between car and the road/other cars
     *
     * @param car
     */
    public void checkCollisions(Car car) {
        if (checkRoadCollision(car) || checkCarCollisions(car)) {
            car.setMaxSpeed(0);
            car.setDead(true);
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
            int direction = car.isFlipRotate() ? 1 : -1;
            rotate(car, direction);
        } else if (car.isTurningLeft() && (Math.abs(car.getSpeedY()) > 0 || Math.abs(car.getSpeedX()) > 0)) {
            int direction = car.isFlipRotate() ? -1 : 1;
            rotate(car, direction);
        }
    }

    private void updateNeuralNetwork(Car car) {
        NeuralNetwork neuralNetwork = car.getNeuralNetwork();
        neuralNetwork.setInput(car.getInputs());
        neuralNetwork.feedforward();
        // Move Car based on output
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
            } else {
                switch (i) {
                    case 2:
                        noLeftTurn(car);
                        break;
                    case 3:
                        noRightTurn(car);
                        break;
                }
            }
        }
    }

    /**
     * Rotates the car based on a direction.
     *
     * @param car
     * @param direction the direction in which we want to rotate the car, can be
     * 1 or -1.
     */
    public void rotate(Car car, int direction) {
        if (Math.abs(direction) != 1) {
            throw new IllegalArgumentException("Direction must be either -1 or 1.");
        }
        car.getCarStack().setRotate(car.getCarStack().getRotate() - 1 * direction);
    }

    /**
     * Updates the position of the car based on user input.
     *
     * @param car
     */
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

    // Getters and Setters
    /**
     *
     * @return List of cars to move.
     */
    public ArrayList<Car> getCars() {
        return cars;
    }

    /**
     *
     * @param cars
     */
    public void setCar(ArrayList<Car> cars) {
        this.cars = cars;
    }

    /**
     *
     * @return Main scene.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     *
     * @param scene
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     *
     * @return Animation of the cars.
     */
    public AnimationTimer getAnimation() {
        return animation;
    }

    /**
     *
     * @param animation
     */
    public void setAnimation(AnimationTimer animation) {
        this.animation = animation;
    }

    /**
     *
     * @return List of enemy cars.
     */
    public ArrayList<Car> getEnemyCars() {
        return enemyCars;
    }

    /**
     *
     * @param enemyCars
     */
    public void setEnemyCars(ArrayList<Car> enemyCars) {
        this.enemyCars = enemyCars;
    }

    /**
     *
     * @return Car that the camera follows.
     */
    public Car getCarToFollow() {
        return carToFollow;
    }

    /**
     *
     * @param carToFollow
     */
    public void setCarToFollow(Car carToFollow) {
        this.carToFollow = carToFollow;
    }

    /**
     *
     * @return Control type of the car (user or AI)
     */
    public boolean isUserControlled() {
        return userControlled;
    }

    /**
     *
     * @param userControlled
     */
    public void setUserControlled(boolean userControlled) {
        this.userControlled = userControlled;
    }

    /**
     *
     * @param cars
     */
    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }
}

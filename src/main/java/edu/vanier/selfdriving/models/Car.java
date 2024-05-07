/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import edu.vanier.selfdriving.utils.MathUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author 2273410
 */
public class Car {

    // JavaFX Car properties
    StackPane carStack = new StackPane();
    Rectangle hitBox;
    Image carImage;
    ImageView carImageView = new ImageView();

    //Transition properties
    double xPosition;
    double yPosition;
    double speedX = 0.0;
    double speedY = 0.0;
    double speed = 0.0;
    double maxSpeed = 8;
    boolean carMoving = false;

    // General properties
    double carWidth = 60;
    double carLength = 120;
    double widthHitboxOffset = 16;
    double lengthHitboxOffset = 20;
    double accelerationValue = 0.3;
    double deccelerationValue = accelerationValue;
    int sensorCount = 5;
    double sensorSpread = Math.PI / 2;
    int direction = 0;
    boolean accelerating = false;
    boolean turningRight = false;
    boolean turningLeft = false;
    boolean flipRotate = false;
    boolean dead = false;
    boolean userControlled;
    Sensor[] sensors = new Sensor[5];
    Road road;
    NeuralNetwork neuralNetwork = new NeuralNetwork(5, 6, 4);
    double[] inputs = new double[sensorCount];

    public Car() {
        carImageView.setFitHeight(carLength);
        carImageView.setFitWidth(carWidth);
        hitBox = new Rectangle(carWidth - widthHitboxOffset, carLength - lengthHitboxOffset);
        hitBox.setVisible(false);
        carStack.setPrefHeight(carLength);
        carStack.setPrefWidth(carWidth);
        carStack.getChildren().addAll(carImageView, hitBox);
    }

    public Car(double x, double y, Image carImage, boolean userControlled) {
        this();
        this.xPosition = x;
        this.yPosition = y;
        this.carImage = carImage;
        this.userControlled = userControlled;
        carImageView.setImage(carImage);
        carStack.setLayoutX(x);
        carStack.setLayoutY(y);
        if (!userControlled) {
            initSensors();
        }
    }

    /**
     * Initialize the position of the Sensors based on the Car.
     *
     * @param xPosition
     * @param yPosition
     */
    public void initSensors() {
        for (int i = 0; i < sensorCount; i++) {
            Sensor sensor = new Sensor(this);
            sensors[i] = sensor;
        }
        for (int i = 0; i < sensorCount; i++) {
            double rayAngle = MathUtils.lerp(sensorSpread / 2, -sensorSpread / 2, (double) i / (sensorCount - 1));

            // Start the Sensor in the middle of the Car.
            double startX = 0.5 * carWidth;
            double startY = 0.5 * carLength;

            // Trig to direct Sensor in the correct direction
            double endX = startX - Math.sin(rayAngle) * Sensor.getSensorLength();
            double endY = startY - Math.cos(rayAngle) * Sensor.getSensorLength();
            Line sensorLine = new Line(startX, startY, endX, endY);
            sensorLine.setStrokeWidth(2);
            sensors[i].setSensorLine(sensorLine);
        }
    }

    public void acceleration(int direction) {
        carMoving = true;
        if (Math.abs(speed) < maxSpeed) {
            speed += accelerationValue * direction;
        } else {
            speed = maxSpeed * direction;
        }
        double angle = 90 - carStack.getRotate();
        speedY = speed * Math.sin(angle * (Math.PI / 180));
        speedX = -speed * Math.cos(angle * (Math.PI / 180));

    }

    public void decceleration(int direction) {
        // Make sure to stop car when deccelerating
        if (carMoving && speed * direction > 0) { // If direction is 1, speed will deccelerate by going down (3 m/s -> 0 m/s), once speed is negative, we know to stop deccelerating, and vice-versa
            speed -= deccelerationValue * direction;
        } else {
            speed = 0;
            carMoving = false;
        }

        double angle = 90 - carStack.getRotate();
        speedY = speed * Math.sin(angle * (Math.PI / 180));
        speedX = -speed * Math.cos(angle * (Math.PI / 180));
    }

    public void rotate(int direction) {
        carStack.setRotate(carStack.getRotate() - 5 * direction);
    }

    public void setSensorsVisible(boolean visible) {
        for (Line sensorLine : getSensorsLines()) {
            sensorLine.setVisible(visible);
        }
    }

    public void setCarVisible(boolean visible) {
        double opacity = 0;
        if (visible) {
            opacity = 1.0;
        } else {
            opacity = 0.2;
        }
        carImageView.setOpacity(opacity);
    }

    public void setVisible(boolean visible) {
        setCarVisible(visible);
        if (!userControlled) {
            setSensorsVisible(visible);
        }
    }
    
    //Getters and Setters for Car class physical properties
    public Image getCarImage() {
        return carImage;
    }

    public void setCarImage(Image carImage) {
        this.carImage = carImage;
    }

    public double getCarWidth() {
        return carWidth;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setCarWidth(double carWidth) {
        carImageView.setFitWidth(carWidth);
        carStack.setPrefWidth(carWidth);
        hitBox.setWidth(carWidth - widthHitboxOffset);
        this.carWidth = carWidth;
    }

    public double getCarLength() {
        return carLength;
    }

    public void setCarLength(double carLength) {
        carImageView.setFitHeight(carLength);
        carStack.setPrefHeight(carLength);
        hitBox.setHeight(carLength - lengthHitboxOffset);
        this.carLength = carLength;
    }

    public double getAccelerationValue() {
        return accelerationValue;
    }

    public void setAccelerationValue(double accelerationValue) {
        this.accelerationValue = accelerationValue;
    }

    public double getxPosition() {
        return carStack.getLayoutX();
    }

    public void setxPosition(double xPosition) {
        carStack.setLayoutX(xPosition);
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return carStack.getLayoutY();
    }

    public void setyPosition(double yPosition) {
        carStack.setLayoutY(yPosition);
        this.yPosition = yPosition;
    }

    public double getSpeedX() {
        return speedX;
    }

    public void setSpeedX(double speedX) {
        this.speedX = speedX;
    }

    public double getSpeedY() {
        return speedY;
    }

    public void setSpeedY(double speedY) {
        this.speedY = speedY;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public boolean isCarMoving() {
        return carMoving;
    }

    public void setCarMoving(boolean carMoving) {
        this.carMoving = carMoving;
    }

    public Sensor[] getSensors() {
        return this.sensors;
    }

    public Line[] getSensorsLines() {
        Line[] sensorLines = new Line[sensorCount];
        for (int i = 0; i < sensorCount; i++) {
            Sensor sensor = sensors[i];
            sensorLines[i] = sensor.getSensorLine();
        }
        return sensorLines;
    }

    public double getDeccelerationValue() {
        return deccelerationValue;
    }

    public void setDeccelerationValue(double deccelerationValue) {
        this.deccelerationValue = deccelerationValue;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public ImageView getCarImageView() {
        return carImageView;
    }

    public void setCarImageView(ImageView carImageView) {
        this.carImageView = carImageView;
    }

    public StackPane getCarStack() {
        return carStack;
    }

    public void setCarStack(StackPane carStack) {
        this.carStack = carStack;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        this.neuralNetwork = neuralNetwork;
    }

    public double getWidthHitboxOffset() {
        return widthHitboxOffset;
    }

    public void setWidthHitboxOffset(double widthHitboxOffset) {
        this.widthHitboxOffset = widthHitboxOffset;
    }

    public double getLengthHitboxOffset() {
        return lengthHitboxOffset;
    }

    public void setLengthHitboxOffset(double lengthHitboxOffset) {
        this.lengthHitboxOffset = lengthHitboxOffset;
    }

    public int getSensorCount() {
        return sensorCount;
    }

    public void setSensorCount(int sensorCount) {
        this.sensorCount = sensorCount;
    }

    public double getSensorSpread() {
        return sensorSpread;
    }

    public void setSensorSpread(double sensorSpread) {
        this.sensorSpread = sensorSpread;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    //Setters and getters for Car movements properties
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

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public double[] getInputs() {
        return inputs;
    }

    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }

}

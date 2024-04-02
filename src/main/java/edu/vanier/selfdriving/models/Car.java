/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
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

    //Static properties
    double carWidth = 60;
    double carLength = 120;
    double widthHitboxOffset = 16;
    double lengthHitboxOffset = 20;
    double accelerationValue = 0.3;
    double deccelerationValue = accelerationValue;
    Sensor sensors;
    Road road;
    NeuralNetwork neuralNetwork = new NeuralNetwork(5, 6, 4);

    public Car() {
        carImageView.setFitHeight(carLength);
        carImageView.setFitWidth(carWidth);
        hitBox = new Rectangle(carWidth - widthHitboxOffset, carLength - lengthHitboxOffset);
        hitBox.setVisible(false);
        carStack.setPrefHeight(carLength);
        carStack.setPrefWidth(carWidth);
        carStack.getChildren().addAll(carImageView, hitBox);
    }

    public Car(double x, double y, Image carImage) {
        this();
        this.xPosition = x;
        this.yPosition = y;
        this.carImage = carImage;
        carImageView.setImage(carImage);
        carStack.setLayoutX(x);
        carStack.setLayoutY(y);
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
        return xPosition;
    }

    public void setxPosition(double xPosition) {
        carStack.setLayoutX(xPosition);
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
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

    public Sensor getSensors() {
        return this.sensors;
    }

    public Line[] getSensorsList() {
        return sensors.sensors;
    }

    public void setSensors(Sensor sensors) {
        this.sensors = sensors;
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

}

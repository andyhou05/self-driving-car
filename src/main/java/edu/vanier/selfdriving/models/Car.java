/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author 2273410
 */
public class Car {

    //Car image
    Image carImage;
    public Rectangle carRectangle;

    //Static properties
    double carWidth; 
    double carLength; 
    double[] sensorsX = {0, (carWidth / 4), (carWidth / 2), ((3 * carWidth) / 4), (carWidth)};
    double[] sensorY = {0, 0, 0, 0, 0};
    double[] sensorAngles = {150, 120, 90, 60, 30};
    double accelerationValue = 0.035;

    //Transition properties
    double xPosition;
    double yPosition;
    double speedX = 0.0;
    double speedY = 0.0;
    double speed = 0.0;
    double maxSpeed = 3;
    boolean carMoving = false;
    int counter = 0;

    public Car() {
    }

    public Car(double x, double y, double sizeX, double sizeY) {
        this.xPosition = x;
        this.yPosition = y;
        this.carWidth = sizeX;
        this.carLength = sizeY;
        carRectangle = new Rectangle(x, y, sizeX, sizeY);
        speed = 0;

    }

    public Car(double x, double y, Image carImage) {
        this.xPosition = x;
        this.yPosition = y;
        this.carImage = carImage;
    }

    public void acceleration(int direction) {
        carMoving = true;
        if (Math.abs(speed) < maxSpeed) {
            speed += accelerationValue * direction;
        } else {
            speed = maxSpeed * direction;
        }
        double angle = 90 - carRectangle.getRotate();
        speedY = speed * Math.sin(angle * (Math.PI / 180));
        speedX = -speed * Math.cos(angle * (Math.PI / 180));

    }

    public void decceleration(int direction) {
        // Make sure to stop car when deccelerating
        if (carMoving && speed * direction > 0) { // If direction is 1, speed will deccelerate by going down (3 m/s -> 0 m/s), once speed is negative, we know to stop deccelerating, and vice-versa
            speed -= accelerationValue * direction;
        } else{
            speed = 0;
            carMoving = false;
        }

        double angle = 90 - carRectangle.getRotate();
        speedY = speed * Math.sin(angle * (Math.PI / 180));
        speedX = -speed * Math.cos(angle * (Math.PI / 180));
    }

    public void rotate(int direction) {
        this.carRectangle.setRotate(this.getCarRectangle().getRotate() - 1 * direction);
    }

    public Image getCarImage() {
        return carImage;
    }

    public void setCarImage(Image carImage) {
        this.carImage = carImage;
    }

    public Rectangle getCarRectangle() {
        return carRectangle;
    }

    public void setCarRectangle(Rectangle carRectangle) {
        this.carRectangle = carRectangle;
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
        this.carWidth = carWidth;
    }

    public double getCarLength() {
        return carLength;
    }

    public void setCarLength(double carLength) {
        this.carLength = carLength;
    }

    public double[] getSensorsX() {
        return sensorsX;
    }

    public void setSensorsX(double[] sensorsX) {
        this.sensorsX = sensorsX;
    }

    public double[] getSensorY() {
        return sensorY;
    }

    public void setSensorY(double[] sensorY) {
        this.sensorY = sensorY;
    }

    public double[] getSensorAngles() {
        return sensorAngles;
    }

    public void setSensorAngles(double[] sensorAngles) {
        this.sensorAngles = sensorAngles;
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
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
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

}

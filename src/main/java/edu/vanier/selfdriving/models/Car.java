/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import javafx.scene.image.Image;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author 2273410
 */
public class Car {

    //Car image
    Image carImage;
    Rectangle carRectangle;

    //Transition properties
    double xPosition;
    double yPosition;
    double speedX = 0.0;
    double speedY = 0.0;
    double speed = 0.0;
    double maxSpeed = 3;
    boolean carMoving = false;

    //Static properties
    double carWidth;
    double carLength;
    double accelerationValue = 0.09;
    double deccelerationValue = accelerationValue + 0.03;
    Sensor sensors;

    public Car() {
        this.carWidth = 40;
        this.carLength = 100;
        carRectangle = new Rectangle(carWidth, carLength);
    }

    public Car(double x, double y) {
        this.xPosition = x;
        this.yPosition = y;
        this.carWidth = 40;
        this.carLength = 100;
        carRectangle = new Rectangle(x, y, this.carWidth, this.carLength);
        speed = 0;
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
            speed -= deccelerationValue * direction;
        } else {
            speed = 0;
            carMoving = false;
        }

        double angle = 90 - carRectangle.getRotate();
        speedY = speed * Math.sin(angle * (Math.PI / 180));
        speedX = -speed * Math.cos(angle * (Math.PI / 180));
    }

    public void rotate(int direction) {
        this.carRectangle.setRotate(this.getCarRectangle().getRotate() - 5 * direction);
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
        carRectangle.setWidth(carWidth);
        this.carWidth = carWidth;
    }

    public double getCarLength() {
        return carLength;
    }

    public void setCarLength(double carLength) {
        carRectangle.setHeight(carLength);
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
        carRectangle.setX(xPosition);
        this.xPosition = xPosition;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        carRectangle.setY(yPosition);
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

}

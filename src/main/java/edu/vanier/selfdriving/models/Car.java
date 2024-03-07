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
    double sizeX; //= carImage.getWidth();
    double sizeY; //= carImage.getHeight();
    double[] sensorsX = {0, (sizeX / 4), (sizeX / 2), ((3 * sizeX) / 4), (sizeX)};
    double[] sensorY = {0, 0, 0, 0, 0};
    double[] sensorAngles = {150, 120, 90, 60, 30};
    double mass;
    double normalForce = mass * (9.8);
    double accelerationValue = 0.035;

    //Transition properties
    double x;
    double y;
    double speedX = 0.0;
    double speedY = 0.0;
    double speed = 0.0;
    double maxSpeed = 3;
    double rotationAngle;

    public Car() {
    }

    public Car(double x, double y, double sizeX, double sizeY) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        carRectangle = new Rectangle(x, y, sizeX, sizeY);
        speed = 0;

    }

    public Car(double x, double y, Image carImage, double mass) {
        this.x = x;
        this.y = y;
        this.carImage = carImage;
        this.mass = mass;
    }

    public void acceleration(int direction) {
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
        System.out.println(accelerationValue * direction);
        // Make sure to stop car when deccelrating
        if (speed * direction < 0) {
            speed = 0;
        } else{
            speed -= accelerationValue * direction;
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

    public double getSizeX() {
        return sizeX;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setSizeX(double sizeX) {
        this.sizeX = sizeX;
    }

    public double getSizeY() {
        return sizeY;
    }

    public void setSizeY(double sizeY) {
        this.sizeY = sizeY;
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

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getNormalForce() {
        return normalForce;
    }

    public void setNormalForce(double normalForce) {
        this.normalForce = normalForce;
    }

    public double getAccelerationValue() {
        return accelerationValue;
    }

    public void setAccelerationValue(double accelerationValue) {
        this.accelerationValue = accelerationValue;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
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

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

}

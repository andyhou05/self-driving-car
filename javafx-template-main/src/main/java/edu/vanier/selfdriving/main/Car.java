/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.main;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author 2273410
 */
public class Car {
    //Car image
    Image carImage;
    
    //Static properties
    double sizeX; //= carImage.getWidth();
    double sizeY; //= carImage.getHeight();
    double[] sensorsX = {0, (sizeX/4), (sizeX/2),((3*sizeX)/4),(sizeX)};
    double [] sensorY = {0,0,0,0,0};
    double[] sensorAngles = {150,120,90,60,30};
    double mass;
    double normalForce = mass * (9.8);
    double accelerationValue = 2.0;
    
    //Transition properties
    double x;
    double y;
    double speedX = 0.0;
    double speedY = 0.0;
    double maxSpeedY;
    double rotationAngle;
    
    public Car(){
    }
    
    public Car(double x, double y, double sizeX, double sizeY){
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        
    }
    
    public Car(double x, double y, Image carImage, double mass){
        this.x = x;
        this.y = y;
        this.carImage = carImage;
        this.mass = mass;
    }
    
    public Rectangle drawCar(){
        Rectangle r = new Rectangle();
        r.setX(x);
        r.setY(y);
        r.setWidth(sizeX);
        r.setHeight(sizeY);
        return r;
    }
    
    public void acceleration(){
        if(maxSpeedY>speedY){
        speedY += accelerationValue;}
    }
    
    public void decceleration(){
        if(maxSpeedY>Math.abs(speedY)){
        speedY -= accelerationValue;}
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import javafx.scene.shape.Line;

/**
 * Sensor class for Car to use.
 * @author USER
 */
public class Sensor {

    Car car;
    static double sensorLength = 400;
    double reading = 0;
    Line sensorLine;
    public static double sensorStartX;
    public static double sensorStartY;

    public Sensor() {
    }

    /**
     * Constructor for Sensor.
     * @param car Car that will be linked to the Sensors.
     */
    public Sensor(Car car) {
        this.car = car;
    }
    
    public static double getSensorLength(){
        return sensorLength;
    }

    public Car getCar() {
        return car;
    }

    public static double getSensorStartX() {
        return sensorStartX;
    }

    public static double getSensorStartY() {
        return sensorStartY;
    }

    public double getReading() {
        return reading;
    }

    public void setReading(double reading) {
        this.reading = reading;
    }

    public Line getSensorLine() {
        return sensorLine;
    }

    public void setSensorLine(Line sensorLine) {
        this.sensorLine = sensorLine;
    }
    
}

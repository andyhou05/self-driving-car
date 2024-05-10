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

    /**
     *
     */
    public static double sensorStartX;

    /**
     *
     */
    public static double sensorStartY;

    /**
     *
     */
    public Sensor() {
    }

    /**
     * Constructor for Sensor.
     * @param car Car that will be linked to the Sensors.
     */
    public Sensor(Car car) {
        this.car = car;
    }
    
    /**
     *
     * @return
     */
    public static double getSensorLength(){
        return sensorLength;
    }

    /**
     *
     * @return
     */
    public Car getCar() {
        return car;
    }

    /**
     *
     * @return
     */
    public static double getSensorStartX() {
        return sensorStartX;
    }

    /**
     *
     * @return
     */
    public static double getSensorStartY() {
        return sensorStartY;
    }

    /**
     *
     * @return
     */
    public double getReading() {
        return reading;
    }

    /**
     *
     * @param reading
     */
    public void setReading(double reading) {
        this.reading = reading;
    }

    /**
     *
     * @return
     */
    public Line getSensorLine() {
        return sensorLine;
    }

    /**
     *
     * @param sensorLine
     */
    public void setSensorLine(Line sensorLine) {
        this.sensorLine = sensorLine;
    }
    
}

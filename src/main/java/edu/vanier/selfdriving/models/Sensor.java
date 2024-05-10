/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import javafx.scene.shape.Line;

/**
 * Sensor class for Car to use.
 *
 * @author USER
 */
public class Sensor {

    Car car;
    static double sensorLength = 400;
    double reading = 0;
    Line sensorLine;

    /**
     * Starting x Position of the sensor.
     */
    public static double sensorStartX;

    /**
     * Starting y Position of the sensor
     */
    public static double sensorStartY;

    /**
     * Creates empty Sensor object.
     */
    public Sensor() {
    }

    /**
     * Creates Sensor object associated to a car.
     *
     * @param car Car that will be linked to the Sensors.
     */
    public Sensor(Car car) {
        this.car = car;
    }

    /**
     * Returns the length of a Sensor
     *
     * @return
     */
    public static double getSensorLength() {
        return sensorLength;
    }

    /**
     * Returns the Car that is connected to the sensor.
     *
     * @return
     */
    public Car getCar() {
        return car;
    }

    /**
     * Returns the reading value of the sensor.
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
     * Returns the Line object of the sensor.
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

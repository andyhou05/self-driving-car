/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import edu.vanier.selfdriving.utils.MathUtils;
import javafx.scene.shape.Line;

/**
 * Sensor class for Car to use.
 * @author USER
 */
public class Sensor {

    Car car;
    int sensorCount = 5;
    double sensorLength = 200;
    double sensorSpread = Math.PI / 4; // Angle between the most-left and most-right sensor in rad
    Line[] sensors = new Line[sensorCount];

    /**
     * Constructor for Sensor.
     * @param car Car that will be linked to the Sensors.
     */
    public Sensor(Car car) {
        this.car = car;
        initSensors();
    }

    /**
     * Initialize the position of the Sensors based on the Car.
     * @param xPosition
     * @param yPosition
     */
    public void initSensors() {
        double xPosition = car.getxPosition();
        double yPosition = car.getyPosition();
        for (int i = 0; i < sensorCount; i++) {
            double rayAngle = MathUtils.lerp(sensorSpread / 2, -sensorSpread / 2, (double) i / (sensorCount - 1));
            
            // Start the Sensor in the middle of the Car.
            double startX = xPosition + 0.5 * car.carWidth;
            double startY = yPosition + 0.5 * car.carLength;
            
            // Trig to direct Sensor in the correct direction
            double endX = startX - Math.sin(rayAngle) * sensorLength;
            double endY = startY - Math.cos(rayAngle) * sensorLength;
            sensors[i] = new Line(startX, startY, endX, endY);
        }
    }
    
    /**
     * Update the position of the Sensors when the Car moves.
     * @param angle
     */
    public void updateSensors(double angle){
        for(int i = 0; i < sensorCount; i++){
            // Move the Sensor with the Car.
            sensors[i].setLayoutX(car.carRectangle.getLayoutX());
            sensors[i].setLayoutY(car.carRectangle.getLayoutY());
            
            // Update the angle of the Sensor with the angle of the Car.
            double rayAngle = MathUtils.lerp(sensorSpread / 2, -sensorSpread / 2, (double) i / (sensorCount - 1)) - (angle * Math.PI/180);
            double startX = sensors[i].getStartX();
            double startY = sensors[i].getStartY();
            double endX = startX - Math.sin(rayAngle) * sensorLength;
            double endY = startY - Math.cos(rayAngle) * sensorLength;
            sensors[i].setEndX(endX);
            sensors[i].setEndY(endY);
        }
    }

}

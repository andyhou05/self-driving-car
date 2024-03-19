/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.models;

import javafx.scene.shape.Line;

/**
 *
 * @author USER
 */
public class Sensor {

    Car car;
    int rayCount = 5;
    double rayLength = 200;
    double raySpread = Math.PI / 4; // Angle between the most-left and most-right sensor in rad
    Line[] sensors = new Line[rayCount];

    public double lerp(double A, double B, double t) {
        return A + (B - A) * t;
    }

    public Sensor(Car car) {
        this.car = car;
        
        updateRays(car.getxPosition(), car.getyPosition());
    }

    public void updateRays(double xPosition, double yPosition) {
        for (int i = 0; i < rayCount; i++) {
            double rayAngle = lerp(raySpread / 2, -raySpread / 2, (double) i / (rayCount - 1)) + car.getCarRectangle().getRotate();
            double startX = xPosition + 0.5 * car.carWidth;
            double startY = yPosition + 0.5 * car.carLength;
            double endX = startX - Math.sin(rayAngle) * rayLength;
            double endY = startY - Math.cos(rayAngle) * rayLength;
            sensors[i] = new Line(startX, startY, endX, endY);
        }
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.utils.MathUtils;
import java.util.ArrayList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author 2276884
 */
public class CarSpawner {
    int carsToSpawn;
    ArrayList<Rectangle> cars = new ArrayList<>();
    Road road;
    double yIncrement;
    double yPosition = 200;
    double x_position_lane_one;
    double x_position_lane_two;
    double x_position_lane_three;
    double roadWidth;
    Pane root;

    public CarSpawner() {
    }

    public CarSpawner(int carsToSpawn, double yIncrement, Road road, double roadWidth, Pane root) {
        this.carsToSpawn = carsToSpawn;
        this.yIncrement = yIncrement;
        this.road = road;
        this.roadWidth = roadWidth;
        this.root = root;
        spawn();
    }
    private void spawn(){
        x_position_lane_one = MathUtils.lerp(0, roadWidth, (1.0f/6.0f));
        x_position_lane_two = MathUtils.lerp(0, roadWidth, (3.0f/6.0f));
        x_position_lane_three = MathUtils.lerp(0, roadWidth, (5.0f/6.0f));
        
        for(int i = 0; i < 100; i++, yPosition += yIncrement) {
            double random = Math.random();
            Car car = new Car();
            car.setMaxSpeed(1.5);
            car.setyPosition(yPosition);
            if(random <= 1f/3f){
                car.setxPosition(x_position_lane_one);
                cars.add(car.getCarRectangle());
            } else if(random <= 2f/3f){
                car.setxPosition(x_position_lane_two);
                cars.add(car.getCarRectangle());
            } else{
                car.setxPosition(x_position_lane_three);
                cars.add(car.getCarRectangle());
            }
            root.getChildren().add(car.getCarRectangle());
        }
    }

    public int getCarsToSpawn() {
        return carsToSpawn;
    }

    public void setCarsToSpawn(int carsToSpawn) {
        this.carsToSpawn = carsToSpawn;
    }

    public ArrayList<Rectangle> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Rectangle> cars) {
        this.cars = cars;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public double getyIncrement() {
        return yIncrement;
    }

    public void setyIncrement(double yIncrement) {
        this.yIncrement = yIncrement;
    }

    public double getX_position_lane_one() {
        return x_position_lane_one;
    }

    public void setX_position_lane_one(double x_position_lane_one) {
        this.x_position_lane_one = x_position_lane_one;
    }

    public double getX_position_lane_two() {
        return x_position_lane_two;
    }

    public void setX_position_lane_two(double x_position_lane_two) {
        this.x_position_lane_two = x_position_lane_two;
    }

    public double getX_position_lane_three() {
        return x_position_lane_three;
    }

    public void setX_position_lane_three(double x_position_lane_three) {
        this.x_position_lane_three = x_position_lane_three;
    }

    public double getRoadWidth() {
        return roadWidth;
    }

    public void setRoadWidth(double roadWidth) {
        this.roadWidth = roadWidth;
    }
    
}

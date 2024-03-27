/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import java.util.ArrayList;
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
    double yPosition = -200;
    double roadWidth;
    Pane root;

    public CarSpawner() {
    }

    public CarSpawner(int carsToSpawn, double yIncrement, Road road, Pane root) {
        this.carsToSpawn = carsToSpawn;
        this.yIncrement = yIncrement;
        this.road = road;
        this.root = root;
        spawn();
    }
    private void spawn(){
        
        for(int i = 0; i < 100; i++, yPosition += yIncrement) {
            double random = Math.random();
            Car car = new Car();
            car.setMaxSpeed(1.5);
            car.setyPosition(yPosition);
            if(random <= 1f/3f){
                car.setxPosition(road.getX_position_lane_one());
                cars.add(car.getCarRectangle());
            } else if(random <= 2f/3f){
                car.setxPosition(road.getX_position_lane_two());
                cars.add(car.getCarRectangle());
            } else{
                car.setxPosition(road.getX_position_lane_three());
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

    public double getRoadWidth() {
        return roadWidth;
    }

    public void setRoadWidth(double roadWidth) {
        this.roadWidth = roadWidth;
    }
    
}

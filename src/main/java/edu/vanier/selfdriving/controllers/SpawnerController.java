/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 *
 * @author 2276884
 */
public class SpawnerController {

    int carsToSpawn;
    ArrayList<StackPane> carsStack = new ArrayList<>();
    ArrayList<Car> cars = new ArrayList<>();
    Road road;
    double yIncrement;
    double yPosition;
    double roadWidth;
    Pane root;
    Image carImage;

    public SpawnerController() {
    }

    public SpawnerController(Road road, Pane root, Image carImage) {
        this.road = road;
        this.root = root;
        this.carImage = carImage;
    }

    public void spawn() {
        yPosition = 150;
        for (int i = 0; i < carsToSpawn; i++, yPosition += yIncrement) {
            double random = Math.random();
            Car car = new Car();
            car.getCarImageView().setImage(carImage);
            car.setSpeedY(2);
            car.setSpeedX(0);
            car.setyPosition(yPosition);
            if (random <= 1f / 3f) {
                car.setxPosition(road.getX_position_lane_one());
                carsStack.add(car.getCarStack());
            } else if (random <= 2f / 3f) {
                car.setxPosition(road.getX_position_lane_two());
                carsStack.add(car.getCarStack());
            } else {
                car.setxPosition(road.getX_position_lane_three());
                carsStack.add(car.getCarStack());
            }
            cars.add(car);
            root.getChildren().add(car.getCarStack());
        }
    }

    public void spawnLevelOne() {
        clear();
        for (int i = 0; i < 7; i++) {
            Car car = new Car();
            car.getCarImageView().setImage(carImage);
            car.setSpeedY(2);
            car.setSpeedX(0);
            car.setyPosition(yPosition);
            switch (i) {
                case 0:
                    car.setyPosition(150);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 1:
                    car.setyPosition(-50);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 2:
                    car.setyPosition(-250);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 3:
                    car.setyPosition(-450);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 4:
                    car.setyPosition(-650);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 5:
                    car.setyPosition(-850);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 6:
                    car.setyPosition(-850);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 7:
                    car.setyPosition(-1050);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
            }
            cars.add(car);
            carsStack.add(car.getCarStack());
            root.getChildren().add(car.getCarStack());
        }
    }

    public void clear() {
        carsStack.clear();
        cars.clear();
    }

    public int getCarsToSpawn() {
        return carsToSpawn;
    }

    public void setCarsToSpawn(int carsToSpawn) {
        this.carsToSpawn = carsToSpawn;
    }

    public ArrayList<StackPane> getCarsStack() {
        return carsStack;
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

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    public double getyPosition() {
        return yPosition;
    }

    public void setyPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public Pane getRoot() {
        return root;
    }

    public void setRoot(Pane root) {
        this.root = root;
    }

    public Image getCarImage() {
        return carImage;
    }

    public void setCarImage(Image carImage) {
        this.carImage = carImage;
    }

}

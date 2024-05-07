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
 * Class to spawn the enemy cars into the scene based on the level.
 *
 * @author 2276884
 */
public class SpawnerController {

    ArrayList<StackPane> carsStack = new ArrayList<>();
    ArrayList<Car> cars = new ArrayList<>();
    Road road;
    double roadWidth;
    Pane root;
    Image carImage;

    /**
     * Creates empty SpawnerController object.
     */
    public SpawnerController() {
    }

    /**
     * Creates SpawnerController object with Road and Root object to load them
     * in.
     *
     * @param road
     * @param root
     * @param carImage
     */
    public SpawnerController(Road road, Pane root) {
        this.road = road;
        this.root = root;
    }

    private Car createEnemyCar() {
        Car car = new Car();
        car.getCarImageView().setImage(carImage);
        car.setSpeedY(2);
        car.setSpeedX(0);
        return car;
    }

    /**
     * Spawns Level one.
     */
    public void spawnLevelOne() {
        carImage = new Image("/sprites/car_red_5.png");
        clear();
        for (int i = 0; i < 7; i++) {
            Car car = createEnemyCar();
            switch (i) {
                case 0:
                    car.setyPosition(150);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 1:
                    car.setyPosition(-150);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 2:
                    car.setyPosition(-450);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 3:
                    car.setyPosition(-750);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 4:
                    car.setyPosition(-1050);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 5:
                    car.setyPosition(-1250);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 6:
                    car.setyPosition(-1250);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 7:
                    car.setyPosition(-1550);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
            }
            cars.add(car);
            carsStack.add(car.getCarStack());
            root.getChildren().add(car.getCarStack());
        }
    }

    /**
     * Spawns Level two.
     */
    public void spawnLevelTwo() {
        carImage = new Image("/sprites/car_yellow_2.png");
        clear();
        for (int i = 0; i < 9; i++) {
            Car car = createEnemyCar();
            switch (i) {
                case 0:
                    car.setyPosition(150);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 1:
                    car.setyPosition(-100);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 2:
                    car.setyPosition(-100);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 3:
                    car.setyPosition(-350);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 4:
                    car.setyPosition(-600);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 5:
                    car.setyPosition(-850);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 6:
                    car.setyPosition(-1100);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 7:
                    car.setyPosition(-1100);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 8:
                    car.setyPosition(-1350);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 9:
                    car.setyPosition(-1600);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
            }
            cars.add(car);
            carsStack.add(car.getCarStack());
            root.getChildren().add(car.getCarStack());
        }
    }

    /**
     * Spawns Level three.
     */
    public void spawnLevelThree() {
        carImage = new Image("/sprites/car_black_4.png");
        clear();
        for (int i = 0; i < 11; i++) {
            Car car = createEnemyCar();
            switch (i) {
                case 0:
                    car.setyPosition(150);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 1:
                    car.setyPosition(150);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 2:
                    car.setyPosition(-250);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 3:
                    car.setyPosition(-550);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 4:
                    car.setyPosition(-850);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 5:
                    car.setyPosition(-850);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 6:
                    car.setyPosition(-1150);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
                case 7:
                    car.setyPosition(-1150);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 8:
                    car.setyPosition(-1450);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 9:
                    car.setyPosition(-1750);
                    car.setxPosition(road.getX_position_lane_three());
                    break;
                case 10:
                    car.setyPosition(-1950);
                    car.setxPosition(road.getX_position_lane_one());
                    break;
                case 11:
                    car.setyPosition(-1950);
                    car.setxPosition(road.getX_position_lane_two());
                    break;
            }
            cars.add(car);
            carsStack.add(car.getCarStack());
            root.getChildren().add(car.getCarStack());
        }
    }

    /**
     * Clears cars and carsStack lists.
     */
    public void clear() {
        carsStack.clear();
        cars.clear();
    }

    // Getters and Setters
    /**
     *
     * @return List of every Car's StackPane
     */
    public ArrayList<StackPane> getCarsStack() {
        return carsStack;
    }

    /**
     *
     * @return Road object where they live.
     */
    public Road getRoad() {
        return road;
    }

    /**
     *
     * @param road
     */
    public void setRoad(Road road) {
        this.road = road;
    }

    /**
     *
     * @return Road width.
     */
    public double getRoadWidth() {
        return roadWidth;
    }

    /**
     *
     * @param roadWidth
     */
    public void setRoadWidth(double roadWidth) {
        this.roadWidth = roadWidth;
    }

    /**
     *
     * @return List of cars in the spawner.
     */
    public ArrayList<Car> getCars() {
        return cars;
    }

    /**
     *
     * @param cars
     */
    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    /**
     *
     * @return Root where the cars live.
     */
    public Pane getRoot() {
        return root;
    }

    /**
     *
     * @param root
     */
    public void setRoot(Pane root) {
        this.root = root;
    }

    /**
     *
     * @return Image of the cars.
     */
    public Image getCarImage() {
        return carImage;
    }

    /**
     *
     * @param carImage
     */
    public void setCarImage(Image carImage) {
        this.carImage = carImage;
    }

}

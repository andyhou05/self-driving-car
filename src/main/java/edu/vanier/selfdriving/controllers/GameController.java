/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.main.Main;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.neuralnetwork.Mutation;
import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

/**
 *
 * @author USER
 */
public class GameController { 

    // Car Properties
    int carCount;
    public static boolean userControlled;
    Road road;
    SpawnerController spawner;
    CarController carController;
    Car carToFollow;
    ArrayList<Car> carGeneration = new ArrayList<>();

    // UI Properties
    Image playerImage;
    Image enemyImage = new Image("/sprites/car_red_5.png");
    public static String carNumber = "5";
    public static String carColor = "blue";

    // FX Properties
    Pane root = (Pane) Main.scene.getRoot();
    Pane roadPane;

    String level;
    
    // Camera Animation
    public AnimationTimer camera;

    public GameController(boolean userControlled, Pane roadPane, int carCount, String level) {
        this.userControlled = userControlled;
        this.roadPane = roadPane;
        this.carCount = carCount;
        this.level = level;
        playerImage = new Image("/sprites/car_" + carColor + "_" + carNumber + ".png");
        createRoad();
        createCarGeneration(playerImage);
        spawner = new SpawnerController(road, root, enemyImage);
        carController = new CarController(carGeneration, spawner.getCars(), userControlled);
        spawn();
    }

    public GameController(SpawnerController spawner, Pane roadPane, String level) {
        this(true, roadPane, 1, level);
        // Create camera for AI Controlled gamemode
        camera = new AnimationTimer() {
            private long FPS = 120L;
            private long INTERVAL = 1000000000L / FPS;
            private long last = 0;

            @Override
            public void handle(long now) {
                if (now - last > INTERVAL) {
                    // Kill car if collision occurs
                    if (carToFollow.isDead()) {
                        carToFollow.setVisible(false);
                    }

                    // Update Camera
                    moveCameraDown();

                    last = now;
                }
            }
        };
        camera.start();
    }

    public void spawn(){
        switch(level){
            case "1":
                spawner.spawnLevelOne();
                break;
            case "2":
                spawner.spawnLevelTwo();
                break;
            case "3":
                spawner.spawnLevelThree();
                break;
        }
        carController.setEnemyCars(spawner.getCars());
    }
    public void createRoad() {
        double roadWidth = roadPane.getPrefWidth() * 0.95;
        road = new Road(roadPane.getPrefWidth() / 2, roadWidth);
        root.getChildren().addAll(road.getLines());
    }

    public void createCarGeneration(Image image) {
        for (int i = 0; i < carCount; i++) {
            Car newCar = new Car(road.getX_position_lane_two(), 450, image, userControlled);
            newCar.setRoad(road);
            carGeneration.add(newCar);
            root.getChildren().add(newCar.getCarStack());
            if (!userControlled) {
                root.getChildren().addAll(newCar.getSensorsLines());
                newCar.setSensorsVisible(false);
            }
        }
        // Make them all less opaque
        for (Car car : carGeneration) {
            car.setCarVisible(false);
        }
        carToFollow = carGeneration.get(0);
        carToFollow.setVisible(true);
    }

    public void moveCameraDown() {
        // Move road lines down
        for (Line roadLine : road.getLines()) {
            roadLine.setTranslateY(roadLine.getTranslateY() + carToFollow.getSpeedY());
        }
        // Move enemy cars down
        for (StackPane enemyStack : spawner.getCarsStack()) {
            enemyStack.setTranslateY(enemyStack.getTranslateY() + carToFollow.getSpeedY());
        }
        // Move generation cars down
        for (Car otherCar : carGeneration) {
            otherCar.getCarStack().setTranslateY(otherCar.getCarStack().getTranslateY() + carToFollow.getSpeedY());
        }
        if (!userControlled) {
            // Move sensors down
            for (Line sensor : carToFollow.getSensorsLines()) {
                sensor.setTranslateY(sensor.getTranslateY() + carToFollow.getSpeedY());
            }
        }
    }

    public void removeAllCars() {
        for (int i = 0; i < carGeneration.size(); i++) {
            Car currentCar = carGeneration.get(i);
            if (!userControlled) {
                root.getChildren().removeAll(currentCar.getSensorsLines());
            }
            root.getChildren().remove(currentCar.getCarStack());
        }

        for (int i = 0; i < spawner.getCars().size(); i++) {
            Car currentCar = spawner.getCars().get(i);
            root.getChildren().remove(currentCar.getCarStack());
        }
        spawner.clear();
        carGeneration.clear();
    }

    public void reset() {
        removeAllCars();
        createCarGeneration(playerImage);
        road.resetLinePositions();
        carController.checkKeypress(carToFollow);
        spawn();
    }
}

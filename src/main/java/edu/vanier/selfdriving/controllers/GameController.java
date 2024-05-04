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
    boolean userControlled;
    Road road;
    CarSpawner spawner;
    CarController carController;
    Car carToFollow;
    ArrayList<Car> carGeneration = new ArrayList<>();

    // UI Properties
    Image playerImage;
    public static String carNumber = "5";
    public static String carColor = "blue";

    // FX Properties
    Pane root = (Pane) Main.scene.getRoot();
    Pane roadPane;

    // AI Gamemode Properties
    NeuralNetwork bestNetwork;
    Visualizer visualizer;

    // Camera Animation
    public AnimationTimer camera;

    private GameController(CarSpawner spawner, boolean userControlled, Pane roadPane) {
        this.spawner = spawner;
        this.userControlled = userControlled;
        this.roadPane = roadPane;
        carCount = userControlled ? 1 : 50;
        playerImage = new Image("/sprites/car_" + carColor + "_" + carNumber + ".png");
        createRoad();
        createCarGeneration(playerImage);
        carController = new CarController(carGeneration, spawner.getCars(), userControlled);
    }

    public GameController(CarSpawner spawner, Pane roadPane, Pane visualizerPane) {
        this(spawner, false, roadPane);

        // Create camera for AI Controlled gamemode
        camera = new AnimationTimer() {
            private long FPS = 120L;
            private long INTERVAL = 1000000000L / FPS;
            private long last = 0;

            @Override
            public void handle(long now) {
                if (now - last > INTERVAL) {
                    // Choose the next car if current one is dead
                    if (carToFollow.isDead() && !carGeneration.isEmpty()) {
                        carToFollow.setVisible(false);
                        chooseNextCar(carToFollow.getCarStack().getLayoutY());
                        carToFollow.setCarVisible(true);
                    }

                    // Update Camera
                    moveCameraDown();

                    // Update Viualizer
                    visualizer.updateVisualizer(carToFollow.getNeuralNetwork());

                    last = now;
                }
            }
        };
        visualizer = new Visualizer(visualizerPane, carToFollow.getNeuralNetwork());
        camera.start();
    }

    public GameController(CarSpawner spawner, Pane roadPane) {
        this(spawner, true, roadPane);
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

    public void createCarGeneration(NeuralNetwork network) {
        createCarGeneration(playerImage);
        if (network != null) {
            for (int i = 0; i < carCount; i++) {
                carGeneration.get(i).setNeuralNetwork(Mutation.mutate(network));
            }
            carToFollow.setNeuralNetwork(bestNetwork);
        }
    }

    private void chooseNextCar(double deathPosition) {
        Car nextCar = carToFollow;
        double deltaPosition = 0;
        for (int i = 0; i < carGeneration.size(); i++) {
            Car currentCar = carGeneration.get(i);
            if (currentCar.isDead()) {
                continue;
            }
            double bestY = nextCar.getCarStack().getLayoutY();
            double currentY = currentCar.getCarStack().getLayoutY();
            if (currentY < bestY) {
                nextCar = currentCar;
                deltaPosition = nextCar.getCarStack().getLayoutY() - deathPosition;
            }
        }
        carToFollow = nextCar;

        // In order to follow a new car, we must move everything down into the scene's frame
        for (Line sensorLine : nextCar.getSensorsLines()) {
            sensorLine.setVisible(true);
            sensorLine.setTranslateY(carToFollow.getCarStack().getTranslateY());
        }
        for (StackPane enemyStack : spawner.getCarsStack()) {
            enemyStack.setLayoutY(enemyStack.getLayoutY() - deltaPosition);
        }
        for (Car otherCar : carGeneration) {
            otherCar.getCarStack().setLayoutY(otherCar.getCarStack().getLayoutY() - deltaPosition);
        }
    }

    private void moveCameraDown() {
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

    void removeAllCars() {
        for (int i = 0; i < carGeneration.size(); i++) {
            Car currentCar = carGeneration.get(i);
            root.getChildren().removeAll(currentCar.getSensorsLines());
            root.getChildren().remove(currentCar.getCarStack());
        }

        for (int i = 0; i < spawner.getCarsToSpawn(); i++) {
            Car currentCar = spawner.getCars().get(i);
            root.getChildren().removeAll(currentCar.getSensorsLines());
            root.getChildren().remove(currentCar.getCarStack());
        }
        spawner.clear();
        carGeneration.clear();
    }

    public void reset() {
        removeAllCars();
        spawner.spawn();
        createCarGeneration(bestNetwork);
        road.resetLinePositions();
    }

    public void saveBestNetwork() {
        bestNetwork = carToFollow.getNeuralNetwork();
    }

    public void hardResetNetwork() {
        bestNetwork = null;
    }
}

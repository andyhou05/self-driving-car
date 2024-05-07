/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.main.Main;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

/**
 * Class to control the game logic such as the timer, the camera, initialization
 * of the game objects, etc. The Main controller of the Application.
 *
 * @author USER
 */
public class GameController {

    private double startTime;
    private double elapsedTime;

    // Car Properties
    int carCount;
    public static boolean userControlled; // Updates based on the game mode chosen by the user.
    Road road;
    SpawnerController spawner;
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

    // Game property
    String level;

    /**
     * Camera Animation.
     */
    public AnimationTimer camera;

    /**
     * Creates an empty GameController object.
     */
    public GameController() {
    }

    /**
     * Creates a general GameController object, initializes the game components
     * such as the roads and the cars based on which level is chosen.
     *
     * @param userControlled
     * @param roadPane
     * @param carCount
     * @param level
     */
    public GameController(boolean userControlled, Pane roadPane, int carCount, String level) {
        this.userControlled = userControlled;
        this.roadPane = roadPane;
        this.carCount = carCount;
        this.level = level;
        playerImage = new Image("/sprites/car_" + carColor + "_" + carNumber + ".png");
        createRoad();
        createCarGeneration(playerImage);
        spawner = new SpawnerController(road, root);
        carController = new CarController(carGeneration, spawner.getCars(), userControlled);
        spawn();
    }

    /**
     * Creates a GameController for User Controlled game mode.
     *
     * @param spawner
     * @param roadPane
     * @param level
     */
    public GameController(SpawnerController spawner, Pane roadPane, String level) {
        this(true, roadPane, 1, level);
        // Create camera for User Controlled gamemode
        camera = new AnimationTimer() {
            private long FPS = 120L;
            private long INTERVAL = 1000000000L / FPS;
            private long last = 0;

            @Override
            public void handle(long now) {
                if (now - last > INTERVAL) {
                    if (startTime == 0) {
                        startTime = now;
                    }
                    elapsedTime = now - startTime;
                    // Kill car if collision occurs
                    if (carToFollow.isDead()) {
                        carToFollow.setVisible(false);
                        stop();
                        Alert deathAlert = new Alert(Alert.AlertType.INFORMATION);
                        deathAlert.setContentText("You survived for: " + String.format("%.2f", elapsedTime / 1000000000) + "s");
                        deathAlert.setTitle("Game Over!");
                        deathAlert.setHeaderText("Score");
                        deathAlert.show();
                        deathAlert.setOnCloseRequest(event -> {
                            reset();
                            start();
                        });
                    }

                    // Update Camera
                    moveCameraDown();

                    last = now;
                }
            }
        };
        camera.start();
    }

    /**
     * Spawns enemy cars based on which level is chosen.
     */
    public void spawn() {
        switch (level) {
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

    /**
     * Initializes the roads of the level.
     */
    public void createRoad() {
        double roadWidth = roadPane.getPrefWidth() * 0.95;
        road = new Road(roadPane.getPrefWidth() / 2, roadWidth);
        root.getChildren().addAll(road.getLines());
    }

    /**
     * Initializes the cars/car of the level.
     *
     * @param image
     */
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

    /**
     * Moves the camera to follow the carToFollow.
     */
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

    /**
     * Removes all the cars from the scene.
     */
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

    /**
     * Reset the level.
     */
    public void reset() {
        removeAllCars();
        createCarGeneration(playerImage);
        road.resetLinePositions();
        carController.checkKeypress(carToFollow);
        spawn();
    }

    /**
     *
     * @return Starting time of the level.
     */
    public double getStartTime() {
        return startTime;
    }

    /**
     *
     * @param startTime
     */
    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    /**
     *
     * @return Elapsed time of the level.
     */
    public double getElapsedTime() {
        return elapsedTime;
    }

    /**
     *
     * @param elapsedTime
     */
    public void setElapsedTime(double elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    /**
     *
     * @return Cars to create in carGeneration.
     */
    public int getCarCount() {
        return carCount;
    }

    /**
     *
     * @param carCount
     */
    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }

    /**
     *
     * @return Control type of the cars/car (AI or User)
     */
    public static boolean isUserControlled() {
        return userControlled;
    }

    /**
     *
     * @param userControlled
     */
    public static void setUserControlled(boolean userControlled) {
        GameController.userControlled = userControlled;
    }

    /**
     *
     * @return Road object of the level.
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
     * @return Spawner of the level.
     */
    public SpawnerController getSpawner() {
        return spawner;
    }

    /**
     *
     * @param spawner
     */
    public void setSpawner(SpawnerController spawner) {
        this.spawner = spawner;
    }

    /**
     *
     * @return CarController of the level.
     */
    public CarController getCarController() {
        return carController;
    }

    /**
     *
     * @param carController
     */
    public void setCarController(CarController carController) {
        this.carController = carController;
    }

    /**
     *
     * @return carToFollow which the camera will follow.
     */
    public Car getCarToFollow() {
        return carToFollow;
    }

    /**
     *
     * @param carToFollow
     */
    public void setCarToFollow(Car carToFollow) {
        this.carToFollow = carToFollow;
    }

    /**
     *
     * @return List of cars that are in the level.
     */
    public ArrayList<Car> getCarGeneration() {
        return carGeneration;
    }

    /**
     *
     * @param carGeneration
     */
    public void setCarGeneration(ArrayList<Car> carGeneration) {
        this.carGeneration = carGeneration;
    }

    /**
     *
     * @return Image of the player cars.
     */
    public Image getPlayerImage() {
        return playerImage;
    }

    /**
     *
     * @param playerImage
     */
    public void setPlayerImage(Image playerImage) {
        this.playerImage = playerImage;
    }

    /**
     *
     * @return The model number of the car.
     */
    public static String getCarNumber() {
        return carNumber;
    }

    /**
     *
     * @param carNumber
     */
    public static void setCarNumber(String carNumber) {
        GameController.carNumber = carNumber;
    }

    /**
     *
     * @return The color of the car.
     */
    public static String getCarColor() {
        return carColor;
    }

    /**
     *
     * @param carColor
     */
    public static void setCarColor(String carColor) {
        GameController.carColor = carColor;
    }

    /**
     *
     * @return Root element of the Scene.
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
     * @return Pane in which the road lives in.
     */
    public Pane getRoadPane() {
        return roadPane;
    }

    /**
     *
     * @param roadPane
     */
    public void setRoadPane(Pane roadPane) {
        this.roadPane = roadPane;
    }

    /**
     *
     * @return Level number.
     */
    public String getLevel() {
        return level;
    }

    /**
     *
     * @param level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     *
     * @return Animation controlling the camera.
     */
    public AnimationTimer getCamera() {
        return camera;
    }

    /**
     *
     * @param camera
     */
    public void setCamera(AnimationTimer camera) {
        this.camera = camera;
    }

}

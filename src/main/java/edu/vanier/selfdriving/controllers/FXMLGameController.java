package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.main.Main;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class of the MainApp's UI.
 *
 * @author frostybee
 */
public class FXMLGameController {

    @FXML
    Pane roadPane;
    @FXML
    Pane visualizerPane;
    @FXML
    Button btnReset;

    public Road road;
    public CarSpawner spawner;
    CarController carController;
    Car carToFollow;
    ArrayList<Car> carGeneration = new ArrayList<>();
    Image enemyImage = new Image("/sprites/car_yellow_3.png");
    int carCount = 50;
    Pane root;
    public static String carColor = "";
    public static String carNumber = "";
    
    public AnimationTimer camera = new AnimationTimer() {
        private long FPS = 120L;
        private long INTERVAL = 1000000000L / FPS;
        private long last = 0;

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                // Choose the next car that is doing the best
                if (carToFollow.isDead() && !carGeneration.isEmpty()) {
                    carToFollow.setVisible(false);
                    chooseNextCar(carToFollow.getCarStack().getLayoutY());
                    carToFollow.setCarVisible(true);
                }

                moveCameraDown();

                last = now;
            }
        }
    };

    @FXML
    public void initialize() {
        Image playerImage = new Image("/sprites/car_"+carColor+"_"+carNumber+".png");
        for (int i = 0; i < carCount; i++) {
            Car newCar = new Car(road.getX_position_lane_two(), 450, playerImage);
            newCar.setRoad(road);
            carGeneration.add(newCar);
            root.getChildren().add(newCar.getCarStack());
            root.getChildren().addAll(newCar.getSensorsLines());
            newCar.setSensorsVisible(false);
        }
        // Make them all less opaque
        for (Car car : carGeneration) {
            car.setCarVisible(false);
        }
        carToFollow = carGeneration.get(0);
        carToFollow.setVisible(true);
        btnReset.setOnAction(resetEvent);
    }

    EventHandler<ActionEvent> resetEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            removeAllCars();
            spawner.spawn();
            
            road.resetLinePositions();
        }
    };

    public void initalizeLevel() {
        root = (Pane) Main.scene.getRoot();
        createRoad();
        spawner = new CarSpawner(4, -400, road, root, enemyImage);
        carController = new CarController(carGeneration, spawner.getCars());
        Visualizer visualizer = new Visualizer(visualizerPane, carToFollow.getNeuralNetwork());
        
        camera.start();
    }

    public void createRoad() {
        double roadWidth = roadPane.getPrefWidth() * 0.95;
        road = new Road(roadPane.getPrefWidth() / 2, roadWidth);
        root.getChildren().addAll(road.getLines());
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
        // Move sensors down
        for (Line sensor : carToFollow.getSensorsLines()) {
            sensor.setTranslateY(sensor.getTranslateY() + carToFollow.getSpeedY());
        }
        // Move enemy cars down
        for (StackPane enemyStack : spawner.getCarsStack()) {
            enemyStack.setTranslateY(enemyStack.getTranslateY() + carToFollow.getSpeedY());
        }
        // Move generation cars down
        for (Car otherCar : carGeneration) {
            otherCar.getCarStack().setTranslateY(otherCar.getCarStack().getTranslateY() + carToFollow.getSpeedY());
        }
    }

    private void removeAllCars() {
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
}

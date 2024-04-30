package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.main.Main;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.neuralnetwork.Mutation;
import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
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
    @FXML
    Button btnSave;
    @FXML
    Button btnHardReset;

    public Road road;
    public CarSpawner spawner;
    NeuralNetwork bestNetwork;
    CarController carController;
    Car carToFollow;
    ArrayList<Car> carGeneration = new ArrayList<>();
    Image playerImage = new Image("/sprites/car_blue_5.png");
    Image enemyImage = new Image("/sprites/car_yellow_3.png");
    int carCount = 50;
    Pane root;
    Visualizer visualizer;
    int index = 0;

    // Animation that controls the game itself
    public static String carNumber = "";
    public static String carColor = "";
            
    public AnimationTimer camera = new AnimationTimer() {
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

    @FXML
    public void initialize() {
        btnReset.setOnAction(resetEvent);
        btnSave.setOnAction(saveEvent);
        btnHardReset.setOnAction(hardResetEvent);
    }

    EventHandler<ActionEvent> resetEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            removeAllCars();
            spawner.spawn();
            createCarGeneration(bestNetwork);
            road.resetLinePositions();
        }
    };
    EventHandler<ActionEvent> saveEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            bestNetwork = carToFollow.getNeuralNetwork();
            removeAllCars();
            spawner.spawn();
            createCarGeneration(bestNetwork);
            road.resetLinePositions();
        }
    };
EventHandler<ActionEvent> hardResetEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            removeAllCars();
            spawner.spawn();
            createCarGeneration(playerImage);
            road.resetLinePositions();
        }
    };

    public void initalizeLevel() {
        playerImage = new Image("/sprites/car_"+carColor+"_"+carNumber+".png");
        root = (Pane) Main.scene.getRoot();
        createRoad();
        spawner = new CarSpawner(8, -400, road, root, enemyImage);
        createCarGeneration(playerImage);
        carController = new CarController(carGeneration, spawner.getCars());
        visualizer = new Visualizer(visualizerPane, carToFollow.getNeuralNetwork());
        
        camera.start();
    }

    public void createRoad() {
        double roadWidth = roadPane.getPrefWidth() * 0.95;
        road = new Road(roadPane.getPrefWidth() / 2, roadWidth);
        root.getChildren().addAll(road.getLines());
    }

    public void createCarGeneration(Image image) {
        for (int i = 0; i < carCount; i++) {
            Car newCar = new Car(road.getX_position_lane_two(), 450, image);
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

    private void goToNextCar(double position) {
        Car nextCar = carToFollow;
        double deltaPosition = 0;
        for (int i = 0; i < carGeneration.size(); i++) {
            updateIndex();
            nextCar = carGeneration.get(index);
            deltaPosition = nextCar.getCarStack().getLayoutY() - position;

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
    
    void updateIndex(){
        index++;
        if(index > carGeneration.size() - 1){
            index = 0;
        }
    }
}


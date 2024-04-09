package edu.vanier.selfdriving.main;

import edu.vanier.selfdriving.controllers.CarController;
import edu.vanier.selfdriving.controllers.CarSpawner;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.controllers.FXMLMainAppController;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.models.Sensor;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a JavaFX project template to be used for creating GUI applications.
 * The JavaFX GUI framework (version: 20.0.2) is linked to this project in the
 * build.gradle file.
 * @link: https://openjfx.io/javadoc/20/
 * @see: /Build Scripts/build.gradle
 * @author Sleiman Rabah.
 */
public class MainApp extends Application {

    private final static Logger logger = LoggerFactory.getLogger(MainApp.class);
    Pane root;
    Scene scene;
    Road road;
    CarSpawner spawner;
    CarController carController;
    Car carToFollow;
    ArrayList<Car> carGeneration = new ArrayList<>();
    Image playerImage = new Image("/images/car_blue_5.png");
    Image enemyImage = new Image("/images/car_yellow_3.png");
    int carCount = 50;

    @Override
    public void start(Stage primaryStage) {
        try {

            logger.info("Bootstrapping the application...");
            //-- 1) Load the scene graph from the specified FXML file and 
            // associate it with its FXML controller.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainApp_layout.fxml"));
            FXMLMainAppController controller = new FXMLMainAppController();
            controller.resetEvent = this.resetEvent;
            loader.setController(controller);
            root = loader.load();

            //-- 2) Create and set the scene to the stage.
            scene = new Scene(root, 600, 800);

            // Create a road for the car to navigate
            createRoad();

            // Spawn traffic
            spawner = new CarSpawner(4, -400, road, root, enemyImage);

            // Create a generation of cars to train
            createCarGeneration();

            // Controller for all cars
            carController = new CarController(carGeneration, spawner.getCars());

            // Reference: https://www.youtube.com/watch?v=CYUjjnoXdrM
            AnimationTimer camera = new AnimationTimer() {
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
            camera.start();

            // Set scene to stage
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            // We just need to bring the main window to front.
            primaryStage.setAlwaysOnTop(true);
            primaryStage.show();
            primaryStage.setAlwaysOnTop(false);
            primaryStage.setResizable(false);

        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    EventHandler<ActionEvent> resetEvent = new EventHandler<>() {
        @Override
        public void handle(ActionEvent event) {
            removeAllCars();
            spawner.spawn();
            createCarGeneration();
        }
    };

    private void createRoad() {
        double roadWidth = scene.getWidth() * 0.95;
        road = new Road(scene.getWidth() / 2, roadWidth);
        root.getChildren().addAll(road.getLines());
    }

    private void createCarGeneration() {
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

    public static void main(String[] args) {
        launch(args);

    }
}

package edu.vanier.selfdriving.main;

import edu.vanier.selfdriving.controllers.CarController;
import edu.vanier.selfdriving.controllers.CarSpawner;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.controllers.FXMLMainAppController;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.models.Sensor;
import java.io.IOException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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

    @Override
    public void start(Stage primaryStage) {
        Image playerImage = new Image("/images/car_blue_5.png");
        Image enemyImage = new Image("/images/car_yellow_3.png");
        try {

            logger.info("Bootstrapping the application...");
            //-- 1) Load the scene graph from the specified FXML file and 
            // associate it with its FXML controller.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainApp_layout.fxml"));
            loader.setController(new FXMLMainAppController());
            Pane root = loader.load();

            //-- 2) Create and set the scene to the stage.
            Scene scene = new Scene(root, 600, 800);

            // Create a road for the car to navigate
            double roadWidth = scene.getWidth() * 0.95;
            Road road1 = new Road(scene.getWidth() / 2, roadWidth);
            root.getChildren().addAll(road1.getLines());

            // Create a car and link it to its controller.
            Car car1 = new Car(road1.getX_position_lane_two(), 450, playerImage);
            car1.setSensors(new Sensor(car1));
            car1.setRoad(road1);

            root.getChildren().add(car1.getCarStack());
            root.getChildren().addAll(car1.getSensorsList());

            // Spawn cars
            CarSpawner spawner = new CarSpawner(1, -400, road1, root, enemyImage);
            
            // Controller for all cars
            CarController controller = new CarController(car1, spawner.getCars());

            // Reference: https://www.youtube.com/watch?v=CYUjjnoXdrM
            AnimationTimer camera = new AnimationTimer() {
                private long FPS = 120L;
                private long INTERVAL = 1000000000L / FPS;
                private long last = 0;

                @Override
                public void handle(long now) {
                    if (now - last > INTERVAL) {
                        // Move road lines down
                        for (Line roadLine : road1.getLines()) {
                            roadLine.setTranslateY(roadLine.getTranslateY() + car1.getSpeedY());
                        }
                        // Move sensors down
                        for (Line sensor : car1.getSensorsList()) {
                            sensor.setTranslateY(sensor.getTranslateY() + car1.getSpeedY());
                        }
                        // Move enemy cars down
                        for (StackPane enemyStack : spawner.getCarsStack()) {
                            enemyStack.setTranslateY(enemyStack.getTranslateY() + car1.getSpeedY());
                        }
                        // Move user car down
                        car1.getCarStack().setTranslateY(car1.getCarStack().getTranslateY() + car1.getSpeedY());
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

    public static void main(String[] args) {
        launch(args);

    }
}

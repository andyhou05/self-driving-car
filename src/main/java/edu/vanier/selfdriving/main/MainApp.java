package edu.vanier.selfdriving.main;

import edu.vanier.selfdriving.controllers.CarController;
import edu.vanier.selfdriving.controllers.CarSpawner;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.controllers.FXMLMainAppController;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.models.Sensor;
import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;
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
            Road road1 = new Road(scene.getWidth() / 2, roadWidth, 3);
            root.getChildren().addAll(road1.getLines());
            
            // Create a car and link it to its controller.
            Car car1 = new Car(100, 550, 50, 100);
            car1.setSensors(new Sensor(car1));
            car1.setRightBorder(road1.getLines().get(0)); // right border has index 0
            car1.setLeftBorder(road1.getLines().get(1)); // left border has index 1
            
            root.getChildren().add(car1.getCarRectangle());
            root.getChildren().addAll(car1.getSensorsList());
            CarController controller = new CarController(car1);
            
            // Spawn cars
            CarSpawner spawner = new CarSpawner(1,-400, road1, roadWidth, root);
            
            // Move lines downwards
            for (int i = 0; i < road1.getLines().size(); i++) {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(20000), road1.getLines().get(i));
                tt.setByY(2000000); // Move the lines downwards by 200 pixels
                tt.setInterpolator(Interpolator.LINEAR);
                tt.setCycleCount(TranslateTransition.INDEFINITE); // Repeat indefinitely
                tt.setAutoReverse(false);
                tt.play();
            }
            
            // Move sensors downrads
            for (int i = 0; i < car1.getSensorsList().length; i++) {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(20000), car1.getSensorsList()[i]);
                tt.setByY(2000000); 
                tt.setInterpolator(Interpolator.LINEAR);
                tt.setCycleCount(TranslateTransition.INDEFINITE); // Repeat indefinitely
                tt.setAutoReverse(false);
                tt.play();
            }
            
            // Move other cars downwards
            for (int i = 0; i < spawner.getCars().size(); i++) {
                TranslateTransition tt = new TranslateTransition(Duration.seconds(20000), spawner.getCars().get(i));
                tt.setByY(2000000); 
                tt.setInterpolator(Interpolator.LINEAR);
                tt.setCycleCount(TranslateTransition.INDEFINITE); // Repeat indefinitely
                tt.setAutoReverse(false);
                tt.play();
            }
            
            // Move user car downwards
            TranslateTransition tt = new TranslateTransition(Duration.seconds(20000), car1.getCarRectangle()); //Make car translate downwards at same speed
            tt.setByY(2000000);
            tt.setInterpolator(Interpolator.LINEAR);
            tt.setCycleCount(TranslateTransition.INDEFINITE);
            tt.setAutoReverse(false);
            tt.play();
            
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

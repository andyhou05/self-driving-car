package edu.vanier.selfdriving.main;

import edu.vanier.selfdriving.controllers.CarController;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.controllers.FXMLMainAppController;
import edu.vanier.selfdriving.models.Road;
import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
            
            ArrayList<Line> listOfLanes = new ArrayList<>();
            logger.info("Bootstrapping the application...");
            //-- 1) Load the scene graph from the specified FXML file and 
            // associate it with its FXML controller.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainApp_layout.fxml"));
            loader.setController(new FXMLMainAppController());
            Pane root = loader.load();
            
            //-- 2) Create and set the scene to the stage.
            Scene scene = new Scene(root, 600, 800);
            
            //Create a road for the car to navigate
            Road road1 = new Road(scene.getWidth()/2,scene.getWidth(),3);
            
            for (int i = 0; i<road1.getLaneCount()-1;i++){
                Line line = new Line(road1.getLanesValueX().get(i),road1.getBottom(),
                        road1.getLanesValueX().get(i),road1.getTop());
                line.getStrokeDashArray().addAll(10d, 10d);
                root.getChildren().add(line);
                listOfLanes.add(line);
            }
            
            
            // Create a car and link it to its controller.
            Car car1 = new Car(100, 550, 40, 100);
            root.getChildren().add(car1.carRectangle);
            CarController controller = new CarController(car1);
            
            for(int i=0;i<listOfLanes.size();i++){
                TranslateTransition tt = new TranslateTransition(Duration.seconds(20000),listOfLanes.get(i));
                tt.setByY(2000000); // Move the lines downwards by 200 pixels
                tt.setInterpolator(Interpolator.LINEAR);
                tt.setCycleCount(TranslateTransition.INDEFINITE); // Repeat indefinitely
                tt.setAutoReverse(false);
                tt.play();
            }
            TranslateTransition tt = new TranslateTransition(Duration.seconds(20000), car1.carRectangle); //Make car translat downwards at same speed
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

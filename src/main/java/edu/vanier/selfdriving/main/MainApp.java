package edu.vanier.selfdriving.main;

import edu.vanier.selfdriving.controllers.CarController;
import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.controllers.FXMLMainAppController;
import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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
        try {
            logger.info("Bootstrapping the application...");
            //-- 1) Load the scene graph from the specified FXML file and 
            // associate it with its FXML controller.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainApp_layout.fxml"));
            loader.setController(new FXMLMainAppController());
            Pane root = loader.load();
            
            //-- 2) Create and set the scene to the stage.
            Scene scene = new Scene(root, 1200, 800);
            
            // Create a car and link it to its controller.
            Car car1 = new Car(100, 700, 40, 100);
            root.getChildren().add(car1.carRectangle);
            CarController controller = new CarController(car1);
            
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();
            // We just need to bring the main window to front.
            primaryStage.setAlwaysOnTop(true);
            primaryStage.show();
            primaryStage.setAlwaysOnTop(false);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}

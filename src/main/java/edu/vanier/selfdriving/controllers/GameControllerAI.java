/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.models.Car;
import edu.vanier.selfdriving.neuralnetwork.Mutation;
import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;

/**
 * GameController child class that is used for AI controlled game mode to
 * control the network visualizer and network of the cars.
 *
 * @author USER
 */
public class GameControllerAI extends GameController {

    // AI Gamemode Properties
    NeuralNetwork bestNetwork;
    VisualizerController visualizer;

    /**
     * Creates an empty GameControllerAI object.
     */
    public GameControllerAI() {
    }

    /**
     * Creates a GameControllerAI object with 50 cars, initializes a visualizer
     * for the neural network.
     *
     * @param roadPane
     * @param visualizerPane
     * @param level
     */
    public GameControllerAI(Pane roadPane, Pane visualizerPane, String level) {
        super(false, roadPane, 50, level);

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
        visualizer = new VisualizerController(visualizerPane, carToFollow.getNeuralNetwork());
        camera.start();
    }

    /**
     * Creates a carGeneration, each car having a mutated version of
     * NeuralNetwork network.
     *
     * @param network
     */
    public void createCarGeneration(NeuralNetwork network) {
        createCarGeneration(playerImage);
        if (network != null) {
            for (int i = 0; i < carCount; i++) {
                carGeneration.get(i).setNeuralNetwork(Mutation.mutate(network));
            }
            carToFollow.setNeuralNetwork(bestNetwork);
        }
    }

    /**
     * Chooses next carToFollow if the current one is dead. Chooses with a
     * simple fitness function: Choose the car that has the least y position in
     * the pane.
     *
     * @param deathPosition
     */
    public void chooseNextCar(double deathPosition) {
        Car nextCar = carToFollow;
        double deltaPosition = 0; // Used to translate everything back down.
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

        // In order to follow the new car, we must translate everything down into the scene's frame
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

    /**
     * Reset the level.
     */
    public void reset() {
        removeAllCars();
        createCarGeneration(bestNetwork);
        visualizer.reset();
        road.resetLinePositions();
        spawn();
    }

    /**
     * Save the NeuralNetwork of carToFollow.
     */
    public void saveNetwork() {
        bestNetwork = carToFollow.getNeuralNetwork();
    }

    /**
     * Completely resets the bestNetwork.
     */
    public void hardResetNetwork() {
        bestNetwork = null;
    }

    // Getters and Setters
    /**
     *
     * @return Best NeuralNetwork of the level.
     */
    public NeuralNetwork getBestNetwork() {
        return bestNetwork;
    }

    /**
     *
     * @param bestNetwork
     */
    public void setBestNetwork(NeuralNetwork bestNetwork) {
        this.bestNetwork = bestNetwork;
    }

    /**
     *
     * @return Visualizer representing the network of carToFollow.
     */
    public VisualizerController getVisualizer() {
        return visualizer;
    }

    /**
     *
     * @param visualizer
     */
    public void setVisualizer(VisualizerController visualizer) {
        this.visualizer = visualizer;
    }

}

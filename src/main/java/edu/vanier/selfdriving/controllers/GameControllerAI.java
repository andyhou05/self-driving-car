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
 *
 * @author USER
 */
public class GameControllerAI extends GameController {

    // AI Gamemode Properties
    NeuralNetwork bestNetwork;
    VisualizerController visualizer;

    public GameControllerAI(SpawnerController spawner, Pane roadPane, Pane visualizerPane) {
        super(spawner, false, roadPane, 50);

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
    public void createCarGeneration(NeuralNetwork network) {
        createCarGeneration(playerImage);
        if (network != null) {
            for (int i = 0; i < carCount; i++) {
                carGeneration.get(i).setNeuralNetwork(Mutation.mutate(network));
            }
            carToFollow.setNeuralNetwork(bestNetwork);
        }
    }

    public void chooseNextCar(double deathPosition) {
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

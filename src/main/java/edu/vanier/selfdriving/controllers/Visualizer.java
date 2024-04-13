/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.controllers;

import edu.vanier.selfdriving.neuralnetwork.NeuralNetwork;
import edu.vanier.selfdriving.utils.MathUtils;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

/**
 *
 * @author USER
 */
public class Visualizer {

    Pane pane;
    NeuralNetwork network;
    double left;
    double right;
    double top;
    double bottom;
    double xOffset = 60;
    double yOffset = 120;
    ArrayList<Circle> nodes = new ArrayList<>();
    AnimationTimer animation = new AnimationTimer() {
        private long FPS = 120L;
        private long INTERVAL = 1000000000L / FPS;
        private long last = 0;

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                for (Circle node : nodes) {
                    node.setRotate(node.getRotate() + 0.5);
                }
            }
        }
    };

    public Visualizer() {
    }

    public Visualizer(Pane pane, NeuralNetwork network) {
        this.pane = pane;
        this.network = network;
        left = xOffset;
        right = pane.getPrefWidth() - xOffset;
        top = yOffset;
        bottom = pane.getPrefHeight() - yOffset;
        initializeNodes();
        animation.start();
    }

    public void initializeVisualizer() {

    }

    void initializeNodes() {
        initializeNodesInLayer(network.getInput(), bottom);
        initializeNodesInLayer(network.getHidden(), MathUtils.lerp(bottom, top, 0.5));
        initializeNodesInLayer(network.getOutput(), top);
    }

    void initializeNodesInLayer(double[] layer, double yPosition) {
        for (int i = 0; i < layer.length; i++) {
            double xPosition = MathUtils.lerp(left, right, (double) i / (layer.length - 1));
            Circle node = new Circle(xPosition, yPosition, 20, Color.TRANSPARENT);
            node.setStroke(Color.WHITE);
            node.setStrokeWidth(2);
            node.getStrokeDashArray().addAll(4d, 7.4d);
            pane.getChildren().add(node);
            nodes.add(node);
        }
    }
}

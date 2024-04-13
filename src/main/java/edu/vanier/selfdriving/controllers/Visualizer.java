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
import javafx.scene.shape.Line;
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
    ArrayList<Circle> biases = new ArrayList<>();
    ArrayList<Circle> nodes = new ArrayList<>();
    AnimationTimer animation = new AnimationTimer() {
        private long FPS = 120L;
        private long INTERVAL = 1000000000L / FPS;
        private long last = 0;

        @Override
        public void handle(long now) {
            if (now - last > INTERVAL) {
                for (Circle node : biases) {
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
        initializeVisualizer();
        animation.start();
    }

    public void initializeVisualizer() {
        initializeNodes();
        initializeConnections();
    }

    void initializeNodes() {
        initializeNodesInLayer(network.getInput(), bottom);
        initializeNodesInLayer(network.getHidden(), MathUtils.lerp(bottom, top, 0.5));
        initializeNodesInLayer(network.getOutput(), top);
    }

    void initializeNodesInLayer(double[] layer, double yPosition) {
        for (int i = 0; i < layer.length; i++) {
            double xPosition = MathUtils.lerp(left, right, (double) i / (layer.length - 1));

            Circle bias = new Circle(xPosition, yPosition, 25, Color.TRANSPARENT);
            Circle node = new Circle(xPosition, yPosition, 20);
            bias.setStrokeWidth(2);
            bias.getStrokeDashArray().addAll(4d, 7.4d);

            pane.getChildren().addAll(node, bias);
            biases.add(bias);
            nodes.add(node);
        }
    }

    void initializeConnections() {
        int inputAmount = network.getInput().length;
        int hiddenAmount = network.getHidden().length;
        int outputAmount = network.getOutput().length;

        for (int i = 0; i < inputAmount; i++) {
            Circle inputNode = biases.get(i);
            for (int j = inputAmount; j < biases.size() - outputAmount; j++) {
                Circle hiddenNode = biases.get(j);
                Line connection_ih = new Line(inputNode.getCenterX(), inputNode.getCenterY() - inputNode.getRadius(), hiddenNode.getCenterX(), hiddenNode.getCenterY() + hiddenNode.getRadius());
                connection_ih.setStrokeWidth(1.5);
                connection_ih.setStroke(Color.WHITE);
                pane.getChildren().add(connection_ih);
            }
        }

        for (int i = inputAmount; i < biases.size() - outputAmount; i++) {
            Circle hiddenNode = biases.get(i);
            for (int j = inputAmount + hiddenAmount; j < biases.size(); j++) {
                Circle outputNode = biases.get(j);
                Line connection_ho = new Line(hiddenNode.getCenterX(), hiddenNode.getCenterY() - hiddenNode.getRadius(), outputNode.getCenterX(), outputNode.getCenterY() + outputNode.getRadius());
                connection_ho.setStrokeWidth(1.5);
                connection_ho.setStroke(Color.WHITE);
                pane.getChildren().add(connection_ho);
            }
        }
    }

    public void updateVisualizer(NeuralNetwork newNetwork) {
        network = newNetwork;
        updateLayer(network.getInput(), 0, network.getInput().length);
        updateLayer(network.getHidden(),network.getBias_h(), network.getInput().length, nodes.size() - network.getOutput().length);
        updateLayer(network.getOutput(),network.getBias_o(), nodes.size() - network.getOutput().length, nodes.size());
    }

    
    void updateLayer(double[] layer, int startIndex, int endIndex) {
        for (int i = startIndex, j = 0; i < endIndex; j++, i++) {
            double nodeReading = layer[j];
            Circle node = nodes.get(i);
            Circle biasCircle = biases.get(i);
            Color nodeColor;
            if (nodeReading > 0.25) {
                nodeColor = Color.GREEN;
            } else {
                nodeColor = Color.RED;
            }
            node.setFill(nodeColor);
            biasCircle.setStroke(nodeColor);
            
        }
    }
    
    void updateLayer(double[] layer, double [] bias, int startIndex, int endIndex) {
        for (int i = startIndex, j = 0; i < endIndex; j++, i++) {
            double nodeReading = layer[j];
            double nodeBias = bias[j];
            Circle node = nodes.get(i);
            Circle biasCircle = biases.get(i);
            Color nodeColor;
            if (nodeReading > nodeBias) {
                nodeColor = Color.GREEN;
            } else {
                nodeColor = Color.RED;
            }
            node.setFill(nodeColor);
            biasCircle.setStroke(nodeColor);

        }
    }
}

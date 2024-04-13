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
    }

    void initializeNodes() {
        initializeNodesInLayer(network.getInput(), bottom);
        initializeNodesInLayer(network.getHidden(), MathUtils.lerp(bottom, top, 0.5));
        initializeNodesInLayer(network.getOutput(), top);
        
        initializeConnections();
    }

    void initializeNodesInLayer(double[] layer, double yPosition) {
        for (int i = 0; i < layer.length; i++) {
            double xPosition = MathUtils.lerp(left, right, (double) i / (layer.length - 1));
            Circle bias = new Circle(xPosition, yPosition, 25, Color.TRANSPARENT);
            Circle node = new Circle(xPosition, yPosition, 20, Color.WHITE);
            bias.setStroke(Color.WHITE);
            bias.setStrokeWidth(2);
            bias.getStrokeDashArray().addAll(4d, 7.4d);
            pane.getChildren().addAll(node, bias);
            biases.add(bias);
        }
    }

    void initializeConnections(){
        int inputAmount = network.getInput().length;
        int hiddenAmount = network.getHidden().length;
        int outputAmount = network.getOutput().length;
        
        for(int i = 0; i < inputAmount;i++){
            Circle inputNode = biases.get(i);
            for(int j = inputAmount; j < biases.size() - outputAmount;j++){
                Circle hiddenNode = biases.get(j);
                Line connection_ih = new Line(inputNode.getCenterX(), inputNode.getCenterY() - inputNode.getRadius(), hiddenNode.getCenterX(), hiddenNode.getCenterY() + hiddenNode.getRadius());
                connection_ih.setStrokeWidth(1.5);
                connection_ih.setStroke(Color.WHITE);
                pane.getChildren().add(connection_ih);
            }
        }
        
        for(int i = inputAmount; i < biases.size() - outputAmount;i++){
            Circle hiddenNode = biases.get(i);
            for(int j = inputAmount + hiddenAmount; j < biases.size(); j++){
                Circle outputNode = biases.get(j);
                Line connection_ho = new Line(hiddenNode.getCenterX(), hiddenNode.getCenterY() - hiddenNode.getRadius(), outputNode.getCenterX(), outputNode.getCenterY() + outputNode.getRadius());
                connection_ho.setStrokeWidth(1.5);
                connection_ho.setStroke(Color.WHITE);
                pane.getChildren().add(connection_ho);
            }
        }
    }
    
    void initalizeConnectionsInLayer(){
        
    }
}

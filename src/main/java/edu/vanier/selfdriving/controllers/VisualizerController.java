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

/**
 * Class to control the NeuralNetwork Visualizer.
 *
 * @author USER
 */
public class VisualizerController {

    Pane pane;
    NeuralNetwork network;
    double leftBorder;
    double rightBorder;
    double topBorder;
    double bottomBorder;
    double xOffset = 60;
    double yOffset = 120;
    ArrayList<Circle> biases = new ArrayList<>();
    ArrayList<Line> weights = new ArrayList<>();
    ArrayList<ArrayList<Circle>> nodesLists = new ArrayList<>();
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

    /**
     * Creates empty VisualizerController object.
     */
    public VisualizerController() {
    }

    /**
     * Creates and initializes a VisualizerController inside pane based off of
     * NeuralNetwork network.
     *
     * @param pane
     * @param network
     */
    public VisualizerController(Pane pane, NeuralNetwork network) {
        this.pane = pane;
        this.network = network;
        leftBorder = xOffset;
        rightBorder = pane.getPrefWidth() - xOffset;
        topBorder = yOffset;
        bottomBorder = pane.getPrefHeight() - yOffset;
        initializeVisualizer();
        animation.start();
    }

    /**
     * Initializes the visualizer.
     */
    public void initializeVisualizer() {
        initializeNodes();
        initializeConnections();
    }

    void initializeNodes() {
        initializeNodesInLayer(network.getInput(), bottomBorder);
        initializeNodesInLayer(network.getHidden(), network.getBias_h(), MathUtils.lerp(bottomBorder, topBorder, 0.5));
        initializeNodesInLayer(network.getOutput(), network.getBias_o(), topBorder);
    }

    void initializeNodesInLayer(double[] layer, double yPosition) {
        ArrayList<Circle> nodes = new ArrayList<>();
        for (int i = 0; i < layer.length; i++) {
            double xPosition = MathUtils.lerp(leftBorder, rightBorder, (double) i / (layer.length - 1));

            // Create a node.
            Circle node = new Circle(xPosition, yPosition, 20); // Nodes will represent the value of a NeuralNetwork node.
            node.setFill(Color.LIME);

            pane.getChildren().addAll(node);
            nodes.add(node);
        }
        nodesLists.add(nodes);
    }

    void initializeNodesInLayer(double[] layer, double[] biasValues, double yPosition) {
        ArrayList<Circle> nodes = new ArrayList<>();
        for (int i = 0; i < layer.length; i++) {
            double xPosition = MathUtils.lerp(leftBorder, rightBorder, (double) i / (layer.length - 1));
            double biasValue = biasValues[i];

            // Create a bias and a node.
            Circle node = new Circle(xPosition, yPosition, 20); // Nodes will represent the value of a NeuralNetwork node.
            node.setFill(Color.LIME);

            // Bias will surround the Node and its opacity depends on bias value.
            Circle bias = new Circle(xPosition, yPosition, 25, Color.TRANSPARENT); // Biases will surround the Node
            bias.setStrokeWidth(2);
            bias.getStrokeDashArray().addAll(4d, 7.4d);
            Color biasColor = (biasValue > 0) ? Color.LIME : Color.CRIMSON;
            bias.setStroke(biasColor);
            bias.setOpacity(Math.abs(biasValue));

            pane.getChildren().addAll(node, bias);
            biases.add(bias);
            nodes.add(node);
        }
        nodesLists.add(nodes);
    }

    void initializeConnections() {
        // Initalize connections between input - hidden and between hidden - output.
        initializeConnections(network.getWeights_ih(), 0, 1);
        initializeConnections(network.getWeights_ho(), 1, 2);

    }

    void initializeConnections(double[][] weights, int indexStart, int indexEnd) { // indexStart and indexEnd represent the index of the ArrayList in nodesLists
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[0].length; j++) {
                // Get the nodes of the bottomBorder layer and the topBorder layer.
                Circle endNode = nodesLists.get(indexEnd).get(i);
                Circle startNode = nodesLists.get(indexStart).get(j);
                double weight = weights[i][j];

                // Connections represent the weight.
                Line connection_ih = new Line(startNode.getCenterX(), startNode.getCenterY() - startNode.getRadius(),
                        endNode.getCenterX(), endNode.getCenterY() + endNode.getRadius());

                // Negative weight means red and positive weight means green
                Color color = (weight > 0) ? Color.LIME : Color.CRIMSON;

                // Set the opacity relative to the weight value
                double percentage = Math.abs(weight) / 3.0f;
                connection_ih.setStroke(color);
                connection_ih.setOpacity(percentage);
                connection_ih.setStrokeWidth(1.5);
                pane.getChildren().add(connection_ih);
                this.weights.add(connection_ih);
            }
        }
    }

    /**
     * Updates the Node's opacity value in the Visualizer based on the
     * NeuralNetwork
     *
     * @param newNetwork
     */
    public void updateVisualizer(NeuralNetwork newNetwork) {
        network = newNetwork;
        updateLayer(network.getInput(), 0);
        updateLayer(network.getHidden(), 1);
        updateLayer(network.getOutput(), 2);

    }

    void updateLayer(double[] layer, int index) {
        ArrayList<Circle> nodesLayer = nodesLists.get(index);
        for (int j = 0; j < nodesLayer.size(); j++) {
            // Update the opacity of a node based on the nodeReading
            double nodeReading = layer[j];
            Circle node = nodesLists.get(index).get(j);
            node.setOpacity(nodeReading);
        }
    }

    /**
     * Resets the visualizer.
     */
    public void reset() {
        pane.getChildren().removeAll(weights);
        pane.getChildren().removeAll(biases);
        for (ArrayList nodes : nodesLists) {
            pane.getChildren().removeAll(nodes);
        }
        weights.clear();
        biases.clear();
        nodesLists.clear();
        initializeVisualizer();
    }

    // Getters and Setters
    /**
     *
     * @return Pane where the visualizer lives.
     */
    public Pane getPane() {
        return pane;
    }

    /**
     *
     * @param pane
     */
    public void setPane(Pane pane) {
        this.pane = pane;
    }

    /**
     *
     * @return Network that the visualizer follows.
     */
    public NeuralNetwork getNetwork() {
        return network;
    }

    /**
     *
     * @param network
     */
    public void setNetwork(NeuralNetwork network) {
        this.network = network;
    }

    /**
     *
     * @return The position of the left border of the Pane.
     */
    public double getLeftBorder() {
        return leftBorder;
    }

    /**
     *
     * @param leftBorder
     */
    public void setLeftBorder(double leftBorder) {
        this.leftBorder = leftBorder;
    }

    /**
     *
     * @return Position of the right border of the Pane.
     */
    public double getRightBorder() {
        return rightBorder;
    }

    /**
     *
     * @param rightBorder
     */
    public void setRightBorder(double rightBorder) {
        this.rightBorder = rightBorder;
    }

    /**
     *
     * @return Position of the top border of the Pane.
     */
    public double getTopBorder() {
        return topBorder;
    }

    /**
     *
     * @param topBorder
     */
    public void setTopBorder(double topBorder) {
        this.topBorder = topBorder;
    }

    /**
     *
     * @return Position of the bottom border of the Pane.
     */
    public double getBottomBorder() {
        return bottomBorder;
    }

    /**
     *
     * @param bottomBorder
     */
    public void setBottomBorder(double bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    /**
     *
     * @return The x offset between the nodes and the left/right borders
     */
    public double getxOffset() {
        return xOffset;
    }

    /**
     *
     * @param xOffset
     */
    public void setxOffset(double xOffset) {
        this.xOffset = xOffset;
    }

    /**
     *
     * @return The y offset between the nodes and the top/bottom borders
     */
    public double getyOffset() {
        return yOffset;
    }

    /**
     *
     * @param yOffset
     */
    public void setyOffset(double yOffset) {
        this.yOffset = yOffset;
    }

    /**
     *
     * @return The list of bias circles.
     */
    public ArrayList<Circle> getBiases() {
        return biases;
    }

    /**
     *
     * @param biases
     */
    public void setBiases(ArrayList<Circle> biases) {
        this.biases = biases;
    }

    /**
     *
     * @return 2D ArrayList of Nodes, each inner list represents a layer.
     */
    public ArrayList<ArrayList<Circle>> getNodesLists() {
        return nodesLists;
    }

    /**
     *
     * @param nodesLists
     */
    public void setNodesLists(ArrayList<ArrayList<Circle>> nodesLists) {
        this.nodesLists = nodesLists;
    }

    /**
     *
     * @return The animation of the visualizer.
     */
    public AnimationTimer getAnimation() {
        return animation;
    }

    /**
     *
     * @param animation
     */
    public void setAnimation(AnimationTimer animation) {
        this.animation = animation;
    }
}

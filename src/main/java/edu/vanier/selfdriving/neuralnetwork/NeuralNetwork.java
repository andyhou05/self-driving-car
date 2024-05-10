/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.neuralnetwork;

import edu.vanier.selfdriving.utils.MathUtils;

/**
 * Neural Network used by the AI to control movements, has 3 layers, input,
 * output, and hidden.
 *
 * @author 2276884
 */
public class NeuralNetwork {

    // NN layers
    double[] input;
    double[] hidden;
    double[] output;

    // Biases 
    double[] bias_h;
    double[] bias_o;

    // Weights
    double[][] weights_ih;
    double[][] weights_ho;

    /**
     * Creates a Neural Network with specified number of input, hidden, and
     * output neurons.
     *
     * @param input
     * @param hidden
     * @param output
     */
    public NeuralNetwork(int input, int hidden, int output) {
        this.input = new double[input];
        this.hidden = new double[hidden];
        this.output = new double[output];

        bias_h = new double[hidden];
        bias_o = new double[output];

        weights_ih = new double[hidden][input];
        weights_ho = new double[output][hidden];

        // Automatically randomize the values every time we create a NN
        randomize();
    }

    /**
     * Creates NeuralNetwork object with specified values.
     *
     * @param bias_h
     * @param bias_o
     * @param weights_ih
     * @param weights_ho
     */
    public NeuralNetwork(double[] bias_h, double[] bias_o, double[][] weights_ih, double[][] weights_ho) {
        this.bias_h = bias_h;
        this.bias_o = bias_o;
        this.weights_ih = weights_ih;
        this.weights_ho = weights_ho;
    }

    /**
     * Randomize all the input values, weights, and biases
     */
    public void randomize() {
        // Bias values of the hidden layer
        for (int i = 0; i < bias_h.length; i++) {
            bias_h[i] = Math.random() * 2 - 1; // random number between -1 and 1
        }
        // Bias values of the output layer
        for (int i = 0; i < bias_o.length; i++) {
            bias_o[i] = Math.random() * 2 - 1; // random number between -1 and 1
        }
        // Weights between the input and hidden layers
        for (double[] weights_ih1 : weights_ih) {
            for (int j = 0; j < weights_ih1.length; j++) {
                weights_ih1[j] = Math.random() * 8 - 4; // random number between -3 and 3
            }
        }
        // Weights between the hidden and output layers
        for (double[] weights_ho1 : weights_ho) {
            for (int j = 0; j < weights_ho1.length; j++) {
                weights_ho1[j] = Math.random() * 8 - 4;
            }
        }
    }

    /**
     * Calculate the weighted sum for a given layer, ie input layer to hidden
     * layer
     *
     * @param weights
     * @param layer
     * @param bias
     * @param finalLayer
     * @return
     */
    public double[] weightedSum(double[][] weights, double[] layer, double[] bias, boolean finalLayer) {
        if (!finalLayer) {
            // Calculate the multiplication between the weights and the given layer, then add the bias
            double[] next_layer = MathUtils.vectorAddition(MathUtils.matrixVectorMultiply(weights, layer), bias);
            // Normalize all the values between 0 and 1
            for (int i = 0; i < next_layer.length; i++) {
                next_layer[i] = MathUtils.sigmoidActivation(next_layer[i]);
            }
            return next_layer;
        } else {
            // Calculate the multiplication between the weights and the given layer, then add the bias
            double[] next_layer = MathUtils.matrixVectorMultiply(weights, layer);
            // Normalize all the values between 0 and 1
            for (int i = 0; i < next_layer.length; i++) {
                next_layer[i] = MathUtils.sigmoidActivation(next_layer[i]);
                // We want to turn the final value as on/off values (0 or 1)
                if (next_layer[i] > bias[i]) {
                    next_layer[i] = 1;
                } else {
                    next_layer[i] = 0;
                }
            }
            return next_layer;
        }
    }

    /**
     * Basic feed forward algorithm to compute the outputs
     */
    public void feedforward() {
        // Calculate the weighted sum between the input layer to the hidden layer
        hidden = weightedSum(weights_ih, input, bias_h, false);
        // Repeat step between hidden and output layer
        output = weightedSum(weights_ho, hidden, bias_o, true);
    }

    /**
     * The input values of the neural network.
     *
     * @return
     */
    public double[] getInput() {
        return input;
    }

    /**
     *
     * @param input
     */
    public void setInput(double[] input) {
        this.input = input;
    }

    /**
     * The hidden layer values of the neural network.
     *
     * @return
     */
    public double[] getHidden() {
        return hidden;
    }

    /**
     *
     * @param hidden
     */
    public void setHidden(double[] hidden) {
        this.hidden = hidden;
    }

    /**
     * the output of the Neural network.
     *
     * @return
     */
    public double[] getOutput() {
        return output;
    }

    /**
     *
     * @param output
     */
    public void setOutput(double[] output) {
        this.output = output;
    }

    /**
     * the bias values of the hidden layer.
     *
     * @return
     */
    public double[] getBias_h() {
        return bias_h;
    }

    /**
     *
     * @param bias_h
     */
    public void setBias_h(double[] bias_h) {
        this.bias_h = bias_h;
    }

    /**
     * the bias values of the output layer.
     *
     * @return
     */
    public double[] getBias_o() {
        return bias_o;
    }

    /**
     *
     * @param bias_o
     */
    public void setBias_o(double[] bias_o) {
        this.bias_o = bias_o;
    }

    /**
     * the weights between the input layer and the hidden layer.
     *
     * @return
     */
    public double[][] getWeights_ih() {
        return weights_ih;
    }

    /**
     *
     * @param weights_ih
     */
    public void setWeights_ih(double[][] weights_ih) {
        this.weights_ih = weights_ih;
    }

    /**
     * the weights between the hidden layer and the output layer.
     *
     * @return
     */
    public double[][] getWeights_ho() {
        return weights_ho;
    }

    /**
     *
     * @param weights_ho
     */
    public void setWeights_ho(double[][] weights_ho) {
        this.weights_ho = weights_ho;
    }

}

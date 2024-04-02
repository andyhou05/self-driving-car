/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.neuralnetwork;

import edu.vanier.selfdriving.utils.MathUtils;

/**
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

    // Randomize all the input values, weights, and biases
    public void randomize() {
        // Bias values of the hidden layer
        for (int i = 0; i < bias_h.length; i++) {
            bias_h[i] = Math.random() * 2 - 1; // random number between -1 and 1
        }
        // Bias values of the output layer
        for (int i = 0; i < bias_o.length; i++) {
            bias_o[i] = Math.random(); // random number between 0 and 1
        }
        // Weights between the input and hidden layers
        for (int i = 0; i < weights_ih.length; i++) {
            for (int j = 0; j < weights_ih[i].length; j++) {
                weights_ih[i][j] = Math.random() * 6 - 3; // random number between -3 and 3
            }
        }
        // Weights between the hidden and output layers
        for (int i = 0; i < weights_ho.length; i++) {
            for (int j = 0; j < weights_ho[i].length; j++) {
                weights_ho[i][j] = Math.random() * 6 - 3;
            }
        }
    }

    // Calculate the weighted sum for a given layer, ie input layer to hidden layer
    public double[] weightedSum(double[][] weights, double[] layer, double[] bias, boolean finalLayer) {
        if (!finalLayer) {
            // Calculate the multiplication between the weights and the given layer, then add the bias
            double[] next_layer = MathUtils.vectorAddition(MathUtils.matrixVectorMultiply(weights, layer), bias);
            // Normalize all the values between 0 and 1
            for (int i = 0; i < next_layer.length; i++) {
                next_layer[i] = MathUtils.sigmoidActivation(next_layer[i]);
            }
            return next_layer;
        } else{
            // Calculate the multiplication between the weights and the given layer, then add the bias
            double[] next_layer = MathUtils.matrixVectorMultiply(weights, layer);
            // Normalize all the values between 0 and 1
            for (int i = 0; i < next_layer.length; i++) {
                next_layer[i] = MathUtils.sigmoidActivation(next_layer[i]);
                // We want to turn the final value as on/off values (0 or 1)
                if(next_layer[i] > bias[i]){
                    next_layer[i] = 1;
                } else{
                    next_layer[i] = 0;
                }
            }
            return next_layer;
        }
    }

    public void feedforward() {
        // Calculate the weighted sum between the input layer to the hidden layer
        hidden = weightedSum(weights_ih, input, bias_h, false);
        // Repeat step between hidden and output layer
        output = weightedSum(weights_ho, hidden, bias_o, true);
    }

    public double[] getInput() {
        return input;
    }

    public void setInput(double[] input) {
        this.input = input;
    }

    public double[] getHidden() {
        return hidden;
    }

    public void setHidden(double[] hidden) {
        this.hidden = hidden;
    }

    public double[] getOutput() {
        return output;
    }

    public void setOutput(double[] output) {
        this.output = output;
    }

    public double[] getBias_h() {
        return bias_h;
    }

    public void setBias_h(double[] bias_h) {
        this.bias_h = bias_h;
    }

    public double[] getBias_o() {
        return bias_o;
    }

    public void setBias_o(double[] bias_o) {
        this.bias_o = bias_o;
    }

    public double[][] getWeights_ih() {
        return weights_ih;
    }

    public void setWeights_ih(double[][] weights_ih) {
        this.weights_ih = weights_ih;
    }

    public double[][] getWeights_ho() {
        return weights_ho;
    }

    public void setWeights_ho(double[][] weights_ho) {
        this.weights_ho = weights_ho;
    }



}

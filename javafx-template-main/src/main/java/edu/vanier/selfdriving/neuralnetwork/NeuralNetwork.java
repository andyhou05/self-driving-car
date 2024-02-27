/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.neuralnetwork;

/**
 *
 * @author 2276884
 */
public class NeuralNetwork {

    // NN layers
    public double[] input;
    public double[] hidden;
    public double[] output;

    // Biases 
    public double[] bias_h;
    public double[] bias_o;

    // Weights
    public double[][] weights_ih;
    public double[][] weights_ho;

    public NeuralNetwork(int input, int hidden, int output) {
        this.input = new double[input];
        this.hidden = new double[hidden];
        this.output = new double[output];

        bias_h = new double[hidden];
        bias_o = new double[output];

        weights_ih = new double[hidden][input];
        weights_ho = new double[output][hidden];
    }

    // Randomize all the input values, weights, and biases
    public void randomize() {
        // Input values
        for (int i = 0; i < input.length; i++) {
            input[i] = Math.random(); // random number between 0 and 1
        }
        // Bias values of the hidden layer
        for (int i = 0; i < bias_h.length; i++) {
            bias_h[i] = Math.random() * 2 - 1; // random number between -1 and 1
        }
        // Bias values of the output layer
        for (int i = 0; i < bias_o.length; i++) {
            bias_o[i] = Math.random() * 2 - 1;
        }
        // Weights between the input and hidden layers
        for (int i = 0; i < weights_ih.length; i++) {
            for (int j = 0; j < weights_ih[i].length; j++) {
                weights_ih[i][j] = Math.random() * 2 - 1;
            }
        }
        // Weights between the hidden and output layers
        for (int i = 0; i < weights_ho.length; i++) {
            for (int j = 0; j < weights_ho[i].length; j++) {
                weights_ho[i][j] = Math.random() * 2 - 1;
            }
        }
    }
    
    public double[] weightedSum(double [][] weights, double[] layer, double[] bias){
        return null;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.neuralnetwork;

import edu.vanier.selfdriving.utils.MathUtils;

/**
 * Mutation Class to optimize the Neural Network.
 *
 * @author 2276884
 */
public class Mutation {

    // Static mutation rate.
    static double mutationRate = 0.1;

    /**
     * Mutates the NeuralNetwork network with a mutation rate of 0.1
     *
     * @param network
     * @return
     */
    public static NeuralNetwork mutate(NeuralNetwork network) {
        double[] bias_h = mutateBias(network.bias_h);
        double[] bias_o = mutateBias(network.bias_o);
        double[][] weights_ih = mutateWeight(network.weights_ih);
        double[][] weights_ho = mutateWeight(network.weights_ho);
        return new NeuralNetwork(bias_h, bias_o, weights_ih, weights_ho);
    }

    static double[] mutateBias(double[] bias) {
        double[] newBias = new double[bias.length];
        for (int i = 0; i < bias.length; i++) {
            // Lerp the new bias to a new random value by the mutation rate.
            newBias[i] = MathUtils.lerp(bias[i], Math.random() * 2 - 1, mutationRate);
        }
        return newBias;
    }

    static double[][] mutateWeight(double[][] weight) {
        double[][] newWeight = new double[weight.length][weight[0].length];
        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight[i].length; j++) {
                // Lerp the new weight to a new random value by the mutation rate.
                newWeight[i][j] = MathUtils.lerp(weight[i][j], Math.random() * 8 - 4, mutationRate);
            }
        }
        return newWeight;
    }
}

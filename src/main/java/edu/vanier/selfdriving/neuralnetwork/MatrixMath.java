/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.neuralnetwork;

/**
 *
 * @author 2276884
 */
public class MatrixMath {
    // Needs error handling, need to make sure all sizes are compatible
    public static double [] matrixVectorMultiply(double [][] matrix, double [] vector){
        double [] answer_vector = new double[matrix.length];
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                answer_vector[i] += matrix[i][j] * vector[j];
            }
        }
        return answer_vector;
    }
    
    public static double [] vectorAddition(double[] vector1, double [] vector2){
        double [] vectorSum = new double [vector1.length];
        for(int i = 0; i < vector1.length;i++){
            vectorSum[i] = vector1[i] + vector2[i];
        }
        return vectorSum;
    }
    
    public static double sigmoidActivation(double x){
        return 1/(1 + Math.pow(Math.E, -x));
    }
}

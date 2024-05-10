/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.vanier.selfdriving.utils;

/**
 * Math Utility functions.
 * @author 2276884
 */
public class MathUtils {

    /**
     * Matrix Vector multiplication formula.
     *
     * @param matrix
     * @param vector
     * @return Resultant vector between multiplication of a matrix and vector.
     */
    public static double[] matrixVectorMultiply(double[][] matrix, double[] vector) {
        try {
            if (matrix[0].length != vector.length) {
                throw new ArithmeticException();
            }
        } catch (ArithmeticException e) {
            System.out.println("Incompatible matrix and vector sizes");
            return null;
        }

        double[] answer_vector = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                answer_vector[i] += matrix[i][j] * vector[j];
            }
        }
        return answer_vector;
    }

    /**
     * Vector addition formula.
     *
     * @param vector1
     * @param vector2
     * @return Resultant Vector between two vectors.
     */
    public static double[] vectorAddition(double[] vector1, double[] vector2) {
        double[] vectorSum = new double[vector1.length];
        for (int i = 0; i < vector1.length; i++) {
            vectorSum[i] = vector1[i] + vector2[i];
        }
        return vectorSum;
    }

    /**
     * Sigmoid activation function.
     * @param x
     * @return Value between 0 and 1.
     */
    public static double sigmoidActivation(double x) {
        return 1 / (1 + Math.pow(Math.E, -x));
    }

    /**
     * Linear Interpolation method, allows for smooth transitions between 2
     * values, if A = 10, B = 20, and t = 0.5, lerp returns 15.
     *
     * @param A First value.
     * @param B Second value.
     * @param t Interpolation Factor.
     * @return Interpolated value between A and B by factor t.
     */
    public static double lerp(double A, double B, double t) {
        return A + (B - A) * t;
    }
}

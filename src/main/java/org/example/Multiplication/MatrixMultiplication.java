package org.example.Multiplication;

import java.util.Random;

public class MatrixMultiplication {
    public static void generateMatrix(int[][] matrix, int min, int max) {
        Random random = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j] = random.nextInt(max - min + 1) + min;
            }
        }
    }


    public static int[][] basicMatrixMultiplication(int[][] A, int[][] B) {
        int rows = A.length;
        int cols = B[0].length;
        int[][] C = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                C[i][j] = 0;
                for (int k = 0; k < A[0].length; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

}

package org.example.Multiplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelMatrixMultiplication {

    public static int[][] multiplyMatricesParallel(int[][] matrixA, int[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int colsB = matrixB[0].length;

        if (colsA != matrixB.length) {
            throw new IllegalArgumentException("Number of columns in A must match number of rows in B.");
        }

        int[][] result = new int[rowsA][colsB];
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int row = 0; row < rowsA; row++) {
            for (int col = 0; col < colsB; col++) {
                int finalRow = row;
                int finalCol = col;
                executor.execute(() -> result[finalRow][finalCol] = calculateCell(matrixA, matrixB, finalRow, finalCol));
            }
        }

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Matrix multiplication was interrupted", e);
        }

        return result;
    }

    private static int calculateCell(int[][] matrixA, int[][] matrixB, int row, int col) {
        int sum = 0;
        for (int i = 0; i < matrixA[0].length; i++) {
            sum += matrixA[row][i] * matrixB[i][col];
        }
        return sum;
    }
}

package org.example.Multiplication;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class MatrixMultiplicationTask implements Callable<int[][]>, Serializable {
    private final int[][] A;
    private final int[][] B;
    private final int startRow;
    private final int endRow;

    public MatrixMultiplicationTask(int[][] A, int[][] B, int startRow, int endRow) {
        this.A = A;
        this.B = B;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public int[][] call() {
        int rowsA = endRow - startRow;
        int colsB = B[0].length;
        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < B.length; k++) {
                    result[i][j] += A[startRow + i][k] * B[k][j];
                }
            }
        }
        return result;
    }
}

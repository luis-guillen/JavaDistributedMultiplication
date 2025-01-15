package org.example.Multiplication;

import com.hazelcast.core.IExecutorService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

public class DistributedMatrixMultiplication {

    public static int[][] performDistributedMatrixMultiplication(int[][] A, int[][] B) {
        int numRows = A.length;
        int chunkSize = numRows / Runtime.getRuntime().availableProcessors();
        List<Future<int[][]>> futures = new ArrayList<>();

        // Obtener el servicio de ejecución desde HazelcastManager
        IExecutorService executorService = HazelcastManager.getExecutorService("matrix-multiplication");

        for (int i = 0; i < numRows; i += chunkSize) {
            int startRow = i;
            int endRow = Math.min(startRow + chunkSize, numRows);

            // Crear y enviar la tarea al clúster
            MatrixMultiplicationTask task = new MatrixMultiplicationTask(A, B, startRow, endRow);
            futures.add(executorService.submit(task));
        }

        int[][] result = new int[numRows][B[0].length];
        int rowOffset = 0;
        for (Future<int[][]> future : futures) {
            try {
                int[][] partialResult = future.get();
                System.arraycopy(partialResult, 0, result, rowOffset, partialResult.length);
                rowOffset += partialResult.length;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
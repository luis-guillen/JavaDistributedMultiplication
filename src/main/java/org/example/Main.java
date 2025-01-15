package org.example;

import org.example.Multiplication.MatrixMultiplication;
import org.example.Multiplication.DistributedMatrixMultiplication;
import org.example.Multiplication.ParallelMatrixMultiplication;
import org.example.Multiplication.HazelcastManager;
import org.example.ResultsMaker.ResultsWriterUtility;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Main {

    public static void main(String[] args) {
        // Start Hazelcast Manager
        HazelcastManager.initialize();

        // Initialize runtime and OS statistics
        Runtime runtime = Runtime.getRuntime();
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        // Define matrix sizes and headers for results
        int[] matrixSizes = {64, 128, 256, 512, 1024, 2048, 4096};
        String[] resultHeaders = {"Size", "Execution Time (ms)", "Memory Usage (MB)", "CPU (%)", "Nodes", "Network Latency (ms)", "Transfer Time (ms)"};

        for (int size : matrixSizes) {
            // Generate matrices A and B
            int[][] matrixA = new int[size][size];
            int[][] matrixB = new int[size][size];
            MatrixMultiplication.generateMatrix(matrixA, 1, 9);
            MatrixMultiplication.generateMatrix(matrixB, 1, 9);

            // Perform benchmarks
            executeBenchmark(runtime, osBean, "sequential_results.txt", resultHeaders, size, ExecutionType.SEQUENTIAL, matrixA, matrixB);
            executeBenchmark(runtime, osBean, "parallel_results.txt", resultHeaders, size, ExecutionType.PARALLEL, matrixA, matrixB);
            executeBenchmark(runtime, osBean, "distributed_results.txt", resultHeaders, size, ExecutionType.DISTRIBUTED, matrixA, matrixB);
        }

        // Shutdown Hazelcast Manager
        HazelcastManager.shutdown();
    }

    private static void executeBenchmark(Runtime runtime, OperatingSystemMXBean osBean, String outputFile, String[] headers,
                                         int size, ExecutionType type, int[][] matrixA, int[][] matrixB) {
        runtime.gc();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.currentTimeMillis();

        int[][] result = null;
        double cpuUsage = 0;
        long executionTime, memoryUsage;

        switch (type) {
            case SEQUENTIAL:
                result = MatrixMultiplication.basicMatrixMultiplication(matrixA, matrixB);
                break;

            case PARALLEL:
                result = ParallelMatrixMultiplication.multiplyMatricesParallel(matrixA, matrixB);
                break;

            case DISTRIBUTED:
                long startTransferTime = System.currentTimeMillis();
                int nodes = HazelcastManager.getInstance().getCluster().getMembers().size();
                long transferTime = System.currentTimeMillis() - startTransferTime;

                result = DistributedMatrixMultiplication.performDistributedMatrixMultiplication(matrixA, matrixB);

                executionTime = System.currentTimeMillis() - startTime;
                memoryUsage = runtime.totalMemory() - runtime.freeMemory() - memoryBefore;
                cpuUsage = osBean.getSystemCpuLoad() * 100;

                ResultsWriterUtility.writeToText(outputFile, headers, new String[]{
                        String.valueOf(size),
                        String.valueOf(executionTime),
                        String.valueOf(memoryUsage / (1024 * 1024.0)),
                        String.valueOf(cpuUsage),
                        String.valueOf(nodes),
                        String.valueOf(transferTime),
                        String.valueOf(transferTime)
                });
                return;
        }

        executionTime = System.currentTimeMillis() - startTime;
        memoryUsage = runtime.totalMemory() - runtime.freeMemory() - memoryBefore;
        cpuUsage = osBean.getSystemCpuLoad() * 100;

        ResultsWriterUtility.writeToText(outputFile, headers, new String[]{
                String.valueOf(size),
                String.valueOf(executionTime),
                String.valueOf(memoryUsage / (1024 * 1024.0)),
                String.valueOf(cpuUsage),
                "N/A",
                "N/A",
                "N/A"
        });
    }

    private enum ExecutionType {
        SEQUENTIAL, PARALLEL, DISTRIBUTED
    }
}
package org.example.ResultsMaker;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResultsWriterUtility {

    public static void writeToText(String filePath, String[] headers, String[] data) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            if (Files.size(Paths.get(filePath)) == 0) {
                writer.append("---- Results ----\n");
            }
            writer.append("\nExecution Results:\n");
            for (int i = 0; i < headers.length; i++) {
                writer.append(headers[i]).append(": ").append(data[i]).append("\n");
            }
            writer.append("-----------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
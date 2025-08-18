package org.example.urlshortner.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedUrlGenerator {

    private static final long NUM_URLS = 100000000;
    private static final int NUM_THREADS = 8;
    private static final long CHUNK_SIZE = NUM_URLS / NUM_THREADS;
    private static final String OUTPUT_FILENAME = "unique_urls123.csv";
    private static final List<String> tempFiles = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Generating " + NUM_URLS + " unique URLs with " + NUM_THREADS + " threads...");
        long startTime = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i = 0; i < NUM_THREADS; i++) {
            final int threadId = i;
            final long start = threadId * CHUNK_SIZE;
            final long end = (threadId == NUM_THREADS - 1) ? NUM_URLS : (start + CHUNK_SIZE);

            executor.submit(() -> {
                String tempFilename = "temp_" + threadId + ".csv";
                tempFiles.add(tempFilename);
                try (FileWriter writer = new FileWriter(tempFilename)) {
                    for (long j = start; j < end; j++) {
                        String uniqueId = UUID.randomUUID().toString().replace("-", "");
                        writer.append("https://www.example.com/" + uniqueId + "\n");
                    }
                } catch (IOException e) {
                    System.err.println("Thread " + threadId + " failed: " + e.getMessage());
                }
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("\nAll threads finished generating chunks. Merging files...");

        // Merge the temporary files into a single output file
        mergeFiles();

        long endTime = System.currentTimeMillis();
        System.out.println("Successfully created '" + OUTPUT_FILENAME + "' with " + NUM_URLS + " unique URLs.");
        System.out.println("Total time: " + (endTime - startTime) + " ms");
    }

    private static void mergeFiles() {
        try (FileWriter finalWriter = new FileWriter(OUTPUT_FILENAME)) {
            for (String filename : tempFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        finalWriter.append(line + "\n");
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("An error occurred during file merging: " + e.getMessage());
        } finally {
            // Clean up temporary files
            for (String filename : tempFiles) {
                new File(filename).delete();
            }
        }
    }
}
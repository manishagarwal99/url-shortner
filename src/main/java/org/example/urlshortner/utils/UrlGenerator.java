package org.example.urlshortner.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class UrlGenerator {

    public static void main(String[] args) {
        // Define the file name and the number of URLs to generate
        String filename = "unique_urls.csv";
        long numUrls = 100000000;

        System.out.println("Generating " + numUrls + " unique URLs...");

        // Use a try-with-resources statement to automatically close the file writer
        try (FileWriter writer = new FileWriter(filename)) {
            // Generate and write each unique URL
            for (long i = 0; i < numUrls; i++) {
                // Using a UUID to ensure a unique URL
                String uniqueId = UUID.randomUUID().toString().replace("-", "");
                String url = "https://www.example.com/" + uniqueId;
                writer.append(url + "\n");

                // Print progress every 1 million URLs
                if ((i + 1) % 1000000 == 0) {
                    System.out.println("Generated " + (i + 1) + " URLs...");
                }
            }
            System.out.println("Successfully created '" + filename + "' with " + numUrls + " unique URLs.");

        } catch (IOException e) {
            System.err.println("An error occurred while writing the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

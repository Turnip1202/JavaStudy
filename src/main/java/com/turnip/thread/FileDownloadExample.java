package com.turnip.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Random;

/**
 * Multi-threaded file download example
 * Demonstrates the practical use of multithreading: improving program execution efficiency
 */
public class FileDownloadExample {
    
    // Simulated file sizes (unit: MB)
    private static final int[] FILE_SIZES = {10, 25, 15, 30, 20, 35, 12, 18};
    private static final String[] FILE_NAMES = {
        "file1.zip", "file2.pdf", "file3.mp4", "file4.exe", 
        "file5.jpg", "file6.docx", "file7.xlsx", "file8.pptx"
    };
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Multi-threading Practical Example: File Download ===\n");
        
        // Single-threaded download
        long startTime = System.currentTimeMillis();
        downloadFilesSequentially();
        long sequentialTime = System.currentTimeMillis() - startTime;
        
        System.out.println("\n" + "=".repeat(50));
        
        // Multi-threaded download
        startTime = System.currentTimeMillis();
        downloadFilesConcurrently();
        long concurrentTime = System.currentTimeMillis() - startTime;
        
        System.out.println("\n=== Performance Comparison ===");
        System.out.println("Single-threaded download time: " + sequentialTime + "ms");
        System.out.println("Multi-threaded download time: " + concurrentTime + "ms");
        System.out.println("Performance improvement: " + String.format("%.2f", (double)sequentialTime/concurrentTime) + "x");
    }
    
    /**
     * Single-threaded sequential file download
     */
    private static void downloadFilesSequentially() {
        System.out.println("Starting single-threaded download...");
        
        for (int i = 0; i < FILE_SIZES.length; i++) {
            downloadFile(FILE_NAMES[i], FILE_SIZES[i]);
        }
        
        System.out.println("Single-threaded download completed!");
    }
    
    /**
     * Multi-threaded concurrent file download
     */
    private static void downloadFilesConcurrently() throws InterruptedException {
        System.out.println("Starting multi-threaded download...");
        
        // Create a fixed-size thread pool
        ExecutorService executor = Executors.newFixedThreadPool(4);
        
        // Use CountDownLatch to wait for all threads to complete
        CountDownLatch latch = new CountDownLatch(FILE_SIZES.length);
        
        // Submit download tasks
        for (int i = 0; i < FILE_SIZES.length; i++) {
            final int index = i;
            executor.submit(() -> {
                downloadFile(FILE_NAMES[index], FILE_SIZES[index]);
                latch.countDown(); // Complete one task, decrement counter
            });
        }
        
        // Shutdown thread pool, no more new tasks
        executor.shutdown();
        
        // Wait for all download tasks to complete
        latch.await();
        
        System.out.println("Multi-threaded download completed!");
    }
    
    /**
     * Simulate file download
     * @param fileName file name
     * @param fileSize file size (MB)
     */
    private static void downloadFile(String fileName, int fileSize) {
        try {
            System.out.println(Thread.currentThread().getName() + 
                " started downloading: " + fileName + " (" + fileSize + "MB)");
            
            // Simulate download time - calculate based on file size
            long downloadTime = fileSize * 100L; // 100ms per MB
            
            // Add some randomness to simulate network fluctuations
            Random random = new Random();
            downloadTime += random.nextInt(500);
            
            Thread.sleep(downloadTime);
            
            System.out.println(Thread.currentThread().getName() + 
                " finished downloading: " + fileName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Download interrupted: " + fileName);
        }
    }
}
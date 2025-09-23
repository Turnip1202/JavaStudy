package com.turnip.interview;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.file.*;
import java.util.concurrent.Future;

/**
 * IO模型比较示例
 * 演示BIO、NIO、AIO三种IO模型的实现和性能差异
 */
public class IOModelComparisonExample {
    
    private static final String FILE_PATH = "test_file.txt";
    private static final int BUFFER_SIZE = 1024;
    
    public static void main(String[] args) {
        // 创建测试文件
        createTestFile();
        
        // BIO文件读取演示
        demonstrateBIO();
        
        // NIO文件读取演示
        demonstrateNIO();
        
        // AIO文件读取演示
        demonstrateAIO();
        
        // 性能比较
        performanceComparison();
        
        // 清理测试文件
        cleanupTestFile();
    }
    
    /**
     * 创建测试文件
     */
    private static void createTestFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (int i = 0; i < 1000; i++) {
                writer.write("这是测试数据行 " + i + " - Java IO性能测试\n");
            }
            System.out.println("测试文件创建完成: " + FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * BIO演示 - 传统阻塞式IO
     */
    public static void demonstrateBIO() {
        System.out.println("\n=== BIO (Blocking IO) 演示 ===");
        
        long startTime = System.currentTimeMillis();
        
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            int totalBytes = 0;
            
            while ((bytesRead = bis.read(buffer)) != -1) {
                totalBytes += bytesRead;
                // 模拟数据处理
                processData(buffer, bytesRead);
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("BIO读取完成");
            System.out.println("总字节数: " + totalBytes);
            System.out.println("耗时: " + (endTime - startTime) + "ms");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * NIO演示 - 非阻塞式IO
     */
    public static void demonstrateNIO() {
        System.out.println("\n=== NIO (Non-blocking IO) 演示 ===");
        
        long startTime = System.currentTimeMillis();
        
        try (FileChannel channel = FileChannel.open(Paths.get(FILE_PATH), StandardOpenOption.READ)) {
            
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            int totalBytes = 0;
            
            while (channel.read(buffer) > 0) {
                buffer.flip(); // 切换到读模式
                
                totalBytes += buffer.remaining();
                
                // 模拟数据处理
                processData(buffer);
                
                buffer.clear(); // 清空缓冲区，准备下次读取
            }
            
            long endTime = System.currentTimeMillis();
            System.out.println("NIO读取完成");
            System.out.println("总字节数: " + totalBytes);
            System.out.println("耗时: " + (endTime - startTime) + "ms");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * AIO演示 - 异步IO
     */
    public static void demonstrateAIO() {
        System.out.println("\n=== AIO (Asynchronous IO) 演示 ===");
        
        long startTime = System.currentTimeMillis();
        
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(
                Paths.get(FILE_PATH), StandardOpenOption.READ)) {
            
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            AIOHandler handler = new AIOHandler(startTime);
            
            // 异步读取
            Future<Integer> future = channel.read(buffer, 0, handler, new CompletionHandler<Integer, AIOHandler>() {
                @Override
                public void completed(Integer result, AIOHandler attachment) {
                    if (result > 0) {
                        buffer.flip();
                        attachment.totalBytes += buffer.remaining();
                        
                        // 模拟数据处理
                        processData(buffer);
                        
                        buffer.clear();
                        
                        // 继续读取下一块
                        attachment.position += result;
                        if (attachment.position < channel.size()) {
                            channel.read(buffer, attachment.position, attachment, this);
                        } else {
                            // 读取完成
                            long endTime = System.currentTimeMillis();
                            System.out.println("AIO读取完成");
                            System.out.println("总字节数: " + attachment.totalBytes);
                            System.out.println("耗时: " + (endTime - attachment.startTime) + "ms");
                            attachment.latch.countDown();
                        }
                    } else {
                        attachment.latch.countDown();
                    }
                }
                
                @Override
                public void failed(Throwable exc, AIOHandler attachment) {
                    exc.printStackTrace();
                    attachment.latch.countDown();
                }
            });
            
            // 等待异步操作完成
            handler.latch.await();
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 性能比较测试
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能比较测试 ===");
        
        int rounds = 5;
        long bioTotal = 0, nioTotal = 0;
        
        for (int i = 0; i < rounds; i++) {
            // BIO性能测试
            long bioTime = measureBIOPerformance();
            bioTotal += bioTime;
            
            // NIO性能测试
            long nioTime = measureNIOPerformance();
            nioTotal += nioTime;
            
            System.out.println("第" + (i + 1) + "轮 - BIO: " + bioTime + "ms, NIO: " + nioTime + "ms");
        }
        
        System.out.println("\n平均性能:");
        System.out.println("BIO平均耗时: " + (bioTotal / rounds) + "ms");
        System.out.println("NIO平均耗时: " + (nioTotal / rounds) + "ms");
        
        double improvement = ((double) (bioTotal - nioTotal) / bioTotal) * 100;
        System.out.println("NIO性能提升: " + String.format("%.2f", improvement) + "%");
    }
    
    /**
     * BIO性能测试
     */
    private static long measureBIOPerformance() {
        long startTime = System.currentTimeMillis();
        
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            
            byte[] buffer = new byte[BUFFER_SIZE];
            while (bis.read(buffer) != -1) {
                // 模拟处理
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * NIO性能测试
     */
    private static long measureNIOPerformance() {
        long startTime = System.currentTimeMillis();
        
        try (FileChannel channel = FileChannel.open(Paths.get(FILE_PATH), StandardOpenOption.READ)) {
            
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            
            while (channel.read(buffer) > 0) {
                buffer.clear();
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return System.currentTimeMillis() - startTime;
    }
    
    /**
     * 零拷贝演示
     */
    public static void demonstrateZeroCopy() {
        System.out.println("\n=== 零拷贝演示 ===");
        
        String sourceFile = FILE_PATH;
        String targetFile = "copy_" + FILE_PATH;
        
        try (FileChannel sourceChannel = FileChannel.open(Paths.get(sourceFile), StandardOpenOption.READ);
             FileChannel targetChannel = FileChannel.open(Paths.get(targetFile), 
                 StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            
            long startTime = System.currentTimeMillis();
            
            // 使用transferTo进行零拷贝
            long transferred = sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("零拷贝传输完成");
            System.out.println("传输字节数: " + transferred);
            System.out.println("耗时: " + (endTime - startTime) + "ms");
            
            // 清理目标文件
            Files.deleteIfExists(Paths.get(targetFile));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 内存映射文件演示
     */
    public static void demonstrateMemoryMappedFile() {
        System.out.println("\n=== 内存映射文件演示 ===");
        
        try (RandomAccessFile file = new RandomAccessFile(FILE_PATH, "r");
             FileChannel channel = file.getChannel()) {
            
            long startTime = System.currentTimeMillis();
            
            // 创建内存映射
            MappedByteBuffer mappedBuffer = channel.map(
                FileChannel.MapMode.READ_ONLY, 0, channel.size());
            
            int totalBytes = 0;
            while (mappedBuffer.hasRemaining()) {
                mappedBuffer.get(); // 读取字节
                totalBytes++;
            }
            
            long endTime = System.currentTimeMillis();
            
            System.out.println("内存映射读取完成");
            System.out.println("读取字节数: " + totalBytes);
            System.out.println("耗时: " + (endTime - startTime) + "ms");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 模拟数据处理 - byte数组版本
     */
    private static void processData(byte[] data, int length) {
        // 模拟CPU密集型操作
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += data[i];
        }
    }
    
    /**
     * 模拟数据处理 - ByteBuffer版本
     */
    private static void processData(ByteBuffer buffer) {
        // 模拟CPU密集型操作
        int sum = 0;
        while (buffer.hasRemaining()) {
            sum += buffer.get();
        }
    }
    
    /**
     * 清理测试文件
     */
    private static void cleanupTestFile() {
        try {
            Files.deleteIfExists(Paths.get(FILE_PATH));
            System.out.println("\n测试文件已清理");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * AIO处理器辅助类
 */
class AIOHandler {
    final long startTime;
    long position = 0;
    int totalBytes = 0;
    final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
    
    public AIOHandler(long startTime) {
        this.startTime = startTime;
    }
}
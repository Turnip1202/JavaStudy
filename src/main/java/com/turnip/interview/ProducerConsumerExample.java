package com.turnip.interview;

import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * 生产者消费者模式示例
 * 演示多种实现方式：wait/notify、Lock/Condition、BlockingQueue
 */
public class ProducerConsumerExample {
    
    public static void main(String[] args) {
        System.out.println("=== 生产者消费者模式演示 ===");
        
        // 方式1：wait/notify实现
        demonstrateWaitNotify();
        
        // 方式2：Lock/Condition实现
        demonstrateLockCondition();
        
        // 方式3：BlockingQueue实现
        demonstrateBlockingQueue();
    }
    
    /**
     * 方式1：使用wait/notify实现
     */
    public static void demonstrateWaitNotify() {
        System.out.println("\n--- wait/notify实现 ---");
        
        WaitNotifyBuffer buffer = new WaitNotifyBuffer(5);
        
        // 创建生产者线程
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    buffer.put("商品" + i);
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "生产者");
        
        // 创建消费者线程
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    String item = buffer.take();
                    System.out.println("消费者消费: " + item);
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "消费者");
        
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 方式2：使用Lock/Condition实现
     */
    public static void demonstrateLockCondition() {
        System.out.println("\n--- Lock/Condition实现 ---");
        
        LockConditionBuffer buffer = new LockConditionBuffer(5);
        
        // 创建多个生产者和消费者
        Thread[] producers = new Thread[2];
        Thread[] consumers = new Thread[2];
        
        for (int i = 0; i < 2; i++) {
            final int id = i;
            producers[i] = new Thread(() -> {
                try {
                    for (int j = 1; j <= 5; j++) {
                        buffer.put("P" + id + "-商品" + j);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "生产者" + id);
            
            consumers[i] = new Thread(() -> {
                try {
                    for (int j = 1; j <= 5; j++) {
                        String item = buffer.take();
                        System.out.println("消费者" + id + "消费: " + item);
                        Thread.sleep(120);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "消费者" + id);
        }
        
        // 启动所有线程
        for (int i = 0; i < 2; i++) {
            producers[i].start();
            consumers[i].start();
        }
        
        // 等待所有线程完成
        for (int i = 0; i < 2; i++) {
            try {
                producers[i].join();
                consumers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 方式3：使用BlockingQueue实现
     */
    public static void demonstrateBlockingQueue() {
        System.out.println("\n--- BlockingQueue实现 ---");
        
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);
        
        // 创建生产者线程
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    String item = "队列商品" + i;
                    queue.put(item); // 阻塞式放入
                    System.out.println("生产者生产: " + item + ", 队列大小: " + queue.size());
                    Thread.sleep(80);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Queue生产者");
        
        // 创建消费者线程
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    String item = queue.take(); // 阻塞式取出
                    System.out.println("消费者消费: " + item + ", 队列大小: " + queue.size());
                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Queue消费者");
        
        producer.start();
        consumer.start();
        
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 使用wait/notify实现的缓冲区
 */
class WaitNotifyBuffer {
    private final String[] buffer;
    private final int capacity;
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;
    
    public WaitNotifyBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new String[capacity];
    }
    
    public synchronized void put(String item) throws InterruptedException {
        // 缓冲区满时等待
        while (count == capacity) {
            System.out.println("缓冲区已满，生产者等待...");
            wait();
        }
        
        buffer[putIndex] = item;
        putIndex = (putIndex + 1) % capacity;
        count++;
        
        System.out.println("生产者生产: " + item + ", 缓冲区大小: " + count);
        
        // 通知等待的消费者
        notifyAll();
    }
    
    public synchronized String take() throws InterruptedException {
        // 缓冲区空时等待
        while (count == 0) {
            System.out.println("缓冲区为空，消费者等待...");
            wait();
        }
        
        String item = buffer[takeIndex];
        buffer[takeIndex] = null;
        takeIndex = (takeIndex + 1) % capacity;
        count--;
        
        // 通知等待的生产者
        notifyAll();
        
        return item;
    }
}

/**
 * 使用Lock/Condition实现的缓冲区
 */
class LockConditionBuffer {
    private final String[] buffer;
    private final int capacity;
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;
    
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();
    
    public LockConditionBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new String[capacity];
    }
    
    public void put(String item) throws InterruptedException {
        lock.lock();
        try {
            // 缓冲区满时等待
            while (count == capacity) {
                System.out.println("缓冲区已满，" + Thread.currentThread().getName() + "等待...");
                notFull.await();
            }
            
            buffer[putIndex] = item;
            putIndex = (putIndex + 1) % capacity;
            count++;
            
            System.out.println(Thread.currentThread().getName() + "生产: " + item + ", 缓冲区大小: " + count);
            
            // 通知等待的消费者
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    public String take() throws InterruptedException {
        lock.lock();
        try {
            // 缓冲区空时等待
            while (count == 0) {
                System.out.println("缓冲区为空，" + Thread.currentThread().getName() + "等待...");
                notEmpty.await();
            }
            
            String item = buffer[takeIndex];
            buffer[takeIndex] = null;
            takeIndex = (takeIndex + 1) % capacity;
            count--;
            
            // 通知等待的生产者
            notFull.signalAll();
            
            return item;
        } finally {
            lock.unlock();
        }
    }
}
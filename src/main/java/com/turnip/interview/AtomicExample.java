package com.turnip.interview;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子类和CAS操作示例
 * 演示AtomicInteger、AtomicReference以及CAS的使用
 */
public class AtomicExample {
    
    // 普通计数器（线程不安全）
    private static int normalCounter = 0;
    
    // 原子计数器（线程安全）
    private static AtomicInteger atomicCounter = new AtomicInteger(0);
    
    // volatile计数器（可见性保证，但不保证原子性）
    private static volatile int volatileCounter = 0;
    
    public static void main(String[] args) {
        System.out.println("=== 原子类和CAS操作演示 ===");
        
        // 基本原子操作演示
        demonstrateBasicAtomicOperations();
        
        // 并发计数器比较
        compareConcurrentCounters();
        
        // CAS操作演示
        demonstrateCASOperations();
        
        // ABA问题演示
        demonstrateABAProblem();
    }
    
    /**
     * 基本原子操作演示
     */
    public static void demonstrateBasicAtomicOperations() {
        System.out.println("\n--- 基本原子操作演示 ---");
        
        AtomicInteger atomic = new AtomicInteger(10);
        
        // 基本读写操作
        System.out.println("初始值: " + atomic.get());
        
        // 设置新值
        atomic.set(20);
        System.out.println("设置后: " + atomic.get());
        
        // 获取并设置
        int oldValue = atomic.getAndSet(30);
        System.out.println("getAndSet - 旧值: " + oldValue + ", 新值: " + atomic.get());
        
        // 增加操作
        int newValue = atomic.incrementAndGet(); // ++i
        System.out.println("incrementAndGet: " + newValue);
        
        int currentValue = atomic.getAndIncrement(); // i++
        System.out.println("getAndIncrement - 返回值: " + currentValue + ", 当前值: " + atomic.get());
        
        // 减少操作
        newValue = atomic.decrementAndGet(); // --i
        System.out.println("decrementAndGet: " + newValue);
        
        currentValue = atomic.getAndDecrement(); // i--
        System.out.println("getAndDecrement - 返回值: " + currentValue + ", 当前值: " + atomic.get());
        
        // 加法操作
        newValue = atomic.addAndGet(5);
        System.out.println("addAndGet(5): " + newValue);
        
        currentValue = atomic.getAndAdd(-3);
        System.out.println("getAndAdd(-3) - 返回值: " + currentValue + ", 当前值: " + atomic.get());
    }
    
    /**
     * 并发计数器比较
     */
    public static void compareConcurrentCounters() {
        System.out.println("\n--- 并发计数器比较 ---");
        
        int threadCount = 10;
        int incrementsPerThread = 1000;
        
        // 重置计数器
        normalCounter = 0;
        atomicCounter.set(0);
        volatileCounter = 0;
        
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        // 创建并启动线程
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    // 普通计数器（线程不安全）
                    normalCounter++;
                    
                    // 原子计数器（线程安全）
                    atomicCounter.incrementAndGet();
                    
                    // volatile计数器（线程不安全，只保证可见性）
                    volatileCounter++;
                }
                latch.countDown();
            }).start();
        }
        
        // 等待所有线程完成
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int expectedValue = threadCount * incrementsPerThread;
        System.out.println("期望值: " + expectedValue);
        System.out.println("普通计数器结果: " + normalCounter + (normalCounter == expectedValue ? " ✓" : " ✗"));
        System.out.println("原子计数器结果: " + atomicCounter.get() + (atomicCounter.get() == expectedValue ? " ✓" : " ✗"));
        System.out.println("volatile计数器结果: " + volatileCounter + (volatileCounter == expectedValue ? " ✓" : " ✗"));
    }
    
    /**
     * CAS操作演示
     */
    public static void demonstrateCASOperations() {
        System.out.println("\n--- CAS操作演示 ---");
        
        AtomicInteger atomic = new AtomicInteger(100);
        
        // compareAndSet操作
        System.out.println("初始值: " + atomic.get());
        
        // 期望值正确，更新成功
        boolean success = atomic.compareAndSet(100, 200);
        System.out.println("CAS(100->200): " + success + ", 当前值: " + atomic.get());
        
        // 期望值错误，更新失败
        success = atomic.compareAndSet(100, 300);
        System.out.println("CAS(100->300): " + success + ", 当前值: " + atomic.get());
        
        // weakCompareAndSet (在某些平台上可能失败)
        success = atomic.weakCompareAndSet(200, 250);
        System.out.println("weakCAS(200->250): " + success + ", 当前值: " + atomic.get());
        
        // 自旋CAS示例
        demonstrateSpinCAS();
    }
    
    /**
     * 自旋CAS示例
     */
    private static void demonstrateSpinCAS() {
        System.out.println("\n--- 自旋CAS示例 ---");
        
        AtomicInteger atomic = new AtomicInteger(0);
        
        // 模拟多个线程同时进行CAS操作
        int threadCount = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            new Thread(() -> {
                try {
                    startLatch.await(); // 等待同时开始
                    
                    // 自旋CAS，直到成功将值增加1
                    int current;
                    int next;
                    do {
                        current = atomic.get();
                        next = current + 1;
                        System.out.println("线程" + threadId + " 尝试CAS: " + current + " -> " + next);
                    } while (!atomic.compareAndSet(current, next));
                    
                    System.out.println("线程" + threadId + " CAS成功: " + next);
                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }
        
        // 开始执行
        startLatch.countDown();
        
        // 等待所有线程完成
        try {
            endLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("最终值: " + atomic.get());
    }
    
    /**
     * ABA问题演示
     */
    public static void demonstrateABAProblem() {
        System.out.println("\n--- ABA问题演示 ---");
        
        AtomicReference<String> atomicRef = new AtomicReference<>("A");
        
        // 线程1：模拟ABA操作
        Thread thread1 = new Thread(() -> {
            String value = atomicRef.get();
            System.out.println("线程1读取值: " + value);
            
            try {
                Thread.sleep(100); // 模拟一些处理时间
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // 尝试CAS，此时值可能已经被改变过了
            boolean success = atomicRef.compareAndSet(value, "C");
            System.out.println("线程1 CAS结果: " + success + ", 当前值: " + atomicRef.get());
        });
        
        // 线程2：在线程1处理期间修改值
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(50);
                
                // A -> B
                boolean success1 = atomicRef.compareAndSet("A", "B");
                System.out.println("线程2 CAS(A->B): " + success1);
                
                // B -> A (恢复原值，造成ABA问题)
                boolean success2 = atomicRef.compareAndSet("B", "A");
                System.out.println("线程2 CAS(B->A): " + success2);
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("ABA问题说明：线程1的CAS操作成功了，但实际上值已经被修改过");
        
        // AtomicStampedReference解决ABA问题
        demonstrateStampedReference();
    }
    
    /**
     * 使用AtomicStampedReference解决ABA问题
     */
    private static void demonstrateStampedReference() {
        System.out.println("\n--- AtomicStampedReference解决ABA问题 ---");
        
        AtomicStampedReference<String> stampedRef = new AtomicStampedReference<>("A", 1);
        
        // 线程1
        Thread thread1 = new Thread(() -> {
            int[] stampHolder = new int[1];
            String value = stampedRef.get(stampHolder);
            int stamp = stampHolder[0];
            
            System.out.println("线程1读取值: " + value + ", 版本: " + stamp);
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            // 使用版本号进行CAS
            boolean success = stampedRef.compareAndSet(value, "C", stamp, stamp + 1);
            System.out.println("线程1 CAS结果: " + success + ", 当前值: " + stampedRef.getReference() 
                + ", 版本: " + stampedRef.getStamp());
        });
        
        // 线程2
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(50);
                
                // A -> B
                boolean success1 = stampedRef.compareAndSet("A", "B", 1, 2);
                System.out.println("线程2 CAS(A->B): " + success1);
                
                // B -> A (版本号会变化)
                boolean success2 = stampedRef.compareAndSet("B", "A", 2, 3);
                System.out.println("线程2 CAS(B->A): " + success2 + ", 版本: " + stampedRef.getStamp());
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("AtomicStampedReference通过版本号避免了ABA问题");
    }
}
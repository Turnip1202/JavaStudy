package com.turnip.interview;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * List集合比较示例
 * 演示ArrayList和LinkedList的性能差异
 */
public class ListComparisonExample {
    
    public static void main(String[] args) {
        // 基本使用演示
        demonstrateBasicUsage();
        
        // 性能比较测试
        performanceComparison();
        
        // 线程安全测试
        demonstrateThreadSafety();
    }
    
    /**
     * 基本使用演示
     */
    public static void demonstrateBasicUsage() {
        System.out.println("=== List基本使用演示 ===");
        
        // ArrayList演示
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Apple");
        arrayList.add("Banana");
        arrayList.add("Cherry");
        arrayList.add(1, "Orange"); // 指定位置插入
        
        System.out.println("ArrayList: " + arrayList);
        System.out.println("第2个元素: " + arrayList.get(1));
        
        // LinkedList演示
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("Dog");
        linkedList.add("Cat");
        linkedList.addFirst("Bird"); // 头部插入
        linkedList.addLast("Fish");  // 尾部插入
        
        System.out.println("LinkedList: " + linkedList);
        System.out.println("第一个元素: " + linkedList.getFirst());
        System.out.println("最后一个元素: " + linkedList.getLast());
        
        // LinkedList作为队列使用
        linkedList.offer("Rabbit"); // 队列操作
        System.out.println("队列头: " + linkedList.poll());
        
        // LinkedList作为栈使用
        linkedList.push("Mouse"); // 栈操作
        System.out.println("栈顶: " + linkedList.pop());
    }
    
    /**
     * 性能比较测试
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能比较测试 ===");
        int size = 100000;
        
        // 测试随机访问性能
        testRandomAccess(size);
        
        // 测试头部插入性能
        testHeadInsertion(size);
        
        // 测试尾部插入性能
        testTailInsertion(size);
        
        // 测试中间插入性能
        testMiddleInsertion(size);
    }
    
    /**
     * 随机访问性能测试
     */
    private static void testRandomAccess(int size) {
        System.out.println("\n--- 随机访问性能测试 ---");
        
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        
        // 填充数据
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        
        // ArrayList随机访问
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int index = (int) (Math.random() * size);
            arrayList.get(index);
        }
        long arrayListTime = System.currentTimeMillis() - startTime;
        
        // LinkedList随机访问
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            int index = (int) (Math.random() * size);
            linkedList.get(index);
        }
        long linkedListTime = System.currentTimeMillis() - startTime;
        
        System.out.println("ArrayList随机访问耗时: " + arrayListTime + "ms");
        System.out.println("LinkedList随机访问耗时: " + linkedListTime + "ms");
    }
    
    /**
     * 头部插入性能测试
     */
    private static void testHeadInsertion(int size) {
        System.out.println("\n--- 头部插入性能测试 ---");
        
        // ArrayList头部插入
        List<Integer> arrayList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            arrayList.add(0, i); // 头部插入，需要移动所有元素
        }
        long arrayListTime = System.currentTimeMillis() - startTime;
        
        // LinkedList头部插入
        LinkedList<Integer> linkedList = new LinkedList<>();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            linkedList.addFirst(i); // 头部插入，只需要修改指针
        }
        long linkedListTime = System.currentTimeMillis() - startTime;
        
        System.out.println("ArrayList头部插入耗时: " + arrayListTime + "ms");
        System.out.println("LinkedList头部插入耗时: " + linkedListTime + "ms");
    }
    
    /**
     * 尾部插入性能测试
     */
    private static void testTailInsertion(int size) {
        System.out.println("\n--- 尾部插入性能测试 ---");
        
        // ArrayList尾部插入
        List<Integer> arrayList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            arrayList.add(i); // 尾部插入，通常O(1)
        }
        long arrayListTime = System.currentTimeMillis() - startTime;
        
        // LinkedList尾部插入
        LinkedList<Integer> linkedList = new LinkedList<>();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            linkedList.add(i); // 尾部插入，O(1)
        }
        long linkedListTime = System.currentTimeMillis() - startTime;
        
        System.out.println("ArrayList尾部插入耗时: " + arrayListTime + "ms");
        System.out.println("LinkedList尾部插入耗时: " + linkedListTime + "ms");
    }
    
    /**
     * 中间插入性能测试
     */
    private static void testMiddleInsertion(int size) {
        System.out.println("\n--- 中间插入性能测试 ---");
        
        List<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();
        
        // 预填充一半数据
        for (int i = 0; i < size / 2; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }
        
        // ArrayList中间插入
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            arrayList.add(arrayList.size() / 2, i);
        }
        long arrayListTime = System.currentTimeMillis() - startTime;
        
        // LinkedList中间插入（需要先定位到中间位置）
        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            linkedList.add(linkedList.size() / 2, i);
        }
        long linkedListTime = System.currentTimeMillis() - startTime;
        
        System.out.println("ArrayList中间插入耗时: " + arrayListTime + "ms");
        System.out.println("LinkedList中间插入耗时: " + linkedListTime + "ms");
    }
    
    /**
     * 线程安全演示
     */
    public static void demonstrateThreadSafety() {
        System.out.println("\n=== 线程安全演示 ===");
        
        // 普通ArrayList（线程不安全）
        List<Integer> unsafeList = new ArrayList<>();
        
        // 同步包装的ArrayList
        List<Integer> synchronizedList = Collections.synchronizedList(new ArrayList<>());
        
        // CopyOnWriteArrayList（读多写少场景）
        List<Integer> copyOnWriteList = new CopyOnWriteArrayList<>();
        
        // Vector（线程安全，但性能较差）
        List<Integer> vector = new Vector<>();
        
        // 并发测试
        testConcurrentAccess("ArrayList", unsafeList);
        testConcurrentAccess("SynchronizedList", synchronizedList);
        testConcurrentAccess("CopyOnWriteArrayList", copyOnWriteList);
        testConcurrentAccess("Vector", vector);
    }
    
    /**
     * 并发访问测试
     */
    private static void testConcurrentAccess(String listType, List<Integer> list) {
        System.out.println("\n测试 " + listType + ":");
        
        Thread[] threads = new Thread[10];
        
        // 创建10个线程同时添加元素
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    list.add(threadId * 1000 + j);
                }
            });
        }
        
        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }
        
        // 等待所有线程完成
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("期望大小: 10000, 实际大小: " + list.size());
        if (list.size() == 10000) {
            System.out.println(listType + " 线程安全 ✓");
        } else {
            System.out.println(listType + " 线程不安全 ✗");
        }
    }
}
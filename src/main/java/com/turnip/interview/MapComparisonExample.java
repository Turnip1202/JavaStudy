package com.turnip.interview;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Map集合比较示例
 * 演示HashMap、LinkedHashMap、TreeMap、ConcurrentHashMap的特点
 */
public class MapComparisonExample {
    
    public static void main(String[] args) {
        // 基本特性演示
        demonstrateBasicFeatures();
        
        // HashMap原理演示
        demonstrateHashMapPrinciple();
        
        // 并发安全演示
        demonstrateConcurrency();
        
        // 性能比较
        performanceComparison();
    }
    
    /**
     * 基本特性演示
     */
    public static void demonstrateBasicFeatures() {
        System.out.println("=== Map基本特性演示 ===");
        
        // HashMap - 无序，基于哈希表
        Map<String, Integer> hashMap = new HashMap<>();
        hashMap.put("Apple", 5);
        hashMap.put("Banana", 3);
        hashMap.put("Cherry", 8);
        hashMap.put("Apple", 6); // 覆盖之前的值
        System.out.println("HashMap (无序): " + hashMap);
        
        // LinkedHashMap - 维护插入顺序
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("Apple", 5);
        linkedHashMap.put("Banana", 3);
        linkedHashMap.put("Cherry", 8);
        linkedHashMap.put("Apple", 6); // 覆盖之前的值，但位置不变
        System.out.println("LinkedHashMap (插入顺序): " + linkedHashMap);
        
        // TreeMap - 按键排序
        Map<String, Integer> treeMap = new TreeMap<>();
        treeMap.put("Apple", 5);
        treeMap.put("Banana", 3);
        treeMap.put("Cherry", 8);
        treeMap.put("Apple", 6); // 覆盖之前的值
        System.out.println("TreeMap (键排序): " + treeMap);
        
        // TreeMap with custom comparator
        Map<String, Integer> treeMapDesc = new TreeMap<>(Collections.reverseOrder());
        treeMapDesc.put("Apple", 5);
        treeMapDesc.put("Banana", 3);
        treeMapDesc.put("Cherry", 8);
        System.out.println("TreeMap (键降序): " + treeMapDesc);
        
        // Hashtable - 线程安全，但性能较差
        Map<String, Integer> hashtable = new Hashtable<>();
        hashtable.put("Dog", 2);
        hashtable.put("Cat", 4);
        // hashtable.put(null, 1); // 不允许null键
        // hashtable.put("Fish", null); // 不允许null值
        System.out.println("Hashtable: " + hashtable);
    }
    
    /**
     * HashMap原理演示
     */
    public static void demonstrateHashMapPrinciple() {
        System.out.println("\n=== HashMap原理演示 ===");
        
        // 哈希冲突演示
        Map<HashCollisionKey, String> map = new HashMap<>();
        
        // 创建具有相同hashCode的键
        HashCollisionKey key1 = new HashCollisionKey("key1", 1);
        HashCollisionKey key2 = new HashCollisionKey("key2", 1); // 相同hashCode
        HashCollisionKey key3 = new HashCollisionKey("key3", 1); // 相同hashCode
        
        map.put(key1, "value1");
        map.put(key2, "value2");
        map.put(key3, "value3");
        
        System.out.println("哈希冲突处理:");
        System.out.println("key1的hashCode: " + key1.hashCode());
        System.out.println("key2的hashCode: " + key2.hashCode());
        System.out.println("key3的hashCode: " + key3.hashCode());
        System.out.println("Map内容: " + map);
        
        // null键值演示
        Map<String, String> nullMap = new HashMap<>();
        nullMap.put(null, "null key value");
        nullMap.put("key", null);
        nullMap.put("key2", "value2");
        System.out.println("包含null的HashMap: " + nullMap);
        
        // 容量和负载因子
        demonstrateCapacityAndLoadFactor();
    }
    
    /**
     * 容量和负载因子演示
     */
    private static void demonstrateCapacityAndLoadFactor() {
        System.out.println("\n--- 容量和负载因子演示 ---");
        
        // 默认初始容量16，负载因子0.75
        Map<Integer, String> defaultMap = new HashMap<>();
        System.out.println("默认HashMap，添加12个元素后会扩容（16 * 0.75 = 12）");
        
        for (int i = 1; i <= 15; i++) {
            defaultMap.put(i, "value" + i);
            if (i == 12) {
                System.out.println("添加第12个元素，可能触发扩容");
            }
        }
        
        // 指定初始容量的HashMap
        Map<Integer, String> customMap = new HashMap<>(32);
        System.out.println("指定初始容量32的HashMap，添加24个元素后会扩容");
        
        // 指定初始容量和负载因子
        Map<Integer, String> customLoadFactorMap = new HashMap<>(16, 0.5f);
        System.out.println("负载因子0.5的HashMap，添加8个元素后会扩容（16 * 0.5 = 8）");
    }
    
    /**
     * 并发安全演示
     */
    public static void demonstrateConcurrency() {
        System.out.println("\n=== 并发安全演示 ===");
        
        // HashMap - 线程不安全
        Map<Integer, String> unsafeMap = new HashMap<>();
        
        // Hashtable - 线程安全但性能差
        Map<Integer, String> hashtable = new Hashtable<>();
        
        // Collections.synchronizedMap - 同步包装器
        Map<Integer, String> synchronizedMap = Collections.synchronizedMap(new HashMap<>());
        
        // ConcurrentHashMap - 高性能并发Map
        Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        
        // 并发测试
        testConcurrentAccess("HashMap", unsafeMap);
        testConcurrentAccess("Hashtable", hashtable);
        testConcurrentAccess("SynchronizedMap", synchronizedMap);
        testConcurrentAccess("ConcurrentHashMap", concurrentMap);
    }
    
    /**
     * 并发访问测试
     */
    private static void testConcurrentAccess(String mapType, Map<Integer, String> map) {
        System.out.println("\n测试 " + mapType + ":");
        
        Thread[] threads = new Thread[10];
        
        // 创建10个线程同时添加元素
        for (int i = 0; i < 10; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    map.put(threadId * 1000 + j, "value" + (threadId * 1000 + j));
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
        
        System.out.println("期望大小: 10000, 实际大小: " + map.size());
        if (map.size() == 10000) {
            System.out.println(mapType + " 线程安全 ✓");
        } else {
            System.out.println(mapType + " 线程不安全 ✗");
        }
    }
    
    /**
     * 性能比较
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能比较 ===");
        int size = 100000;
        
        // 添加性能测试
        testPutPerformance(size);
        
        // 查找性能测试
        testGetPerformance(size);
        
        // 遍历性能测试
        testIterationPerformance(size);
    }
    
    /**
     * 添加性能测试
     */
    private static void testPutPerformance(int size) {
        System.out.println("\n--- 添加性能测试 ---");
        
        // HashMap
        Map<Integer, String> hashMap = new HashMap<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            hashMap.put(i, "value" + i);
        }
        long hashMapTime = System.currentTimeMillis() - startTime;
        
        // LinkedHashMap
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            linkedHashMap.put(i, "value" + i);
        }
        long linkedHashMapTime = System.currentTimeMillis() - startTime;
        
        // TreeMap
        Map<Integer, String> treeMap = new TreeMap<>();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            treeMap.put(i, "value" + i);
        }
        long treeMapTime = System.currentTimeMillis() - startTime;
        
        // ConcurrentHashMap
        Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            concurrentMap.put(i, "value" + i);
        }
        long concurrentMapTime = System.currentTimeMillis() - startTime;
        
        System.out.println("HashMap添加耗时: " + hashMapTime + "ms");
        System.out.println("LinkedHashMap添加耗时: " + linkedHashMapTime + "ms");
        System.out.println("TreeMap添加耗时: " + treeMapTime + "ms");
        System.out.println("ConcurrentHashMap添加耗时: " + concurrentMapTime + "ms");
    }
    
    /**
     * 查找性能测试
     */
    private static void testGetPerformance(int size) {
        System.out.println("\n--- 查找性能测试 ---");
        
        // 准备测试数据
        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
        Map<Integer, String> treeMap = new TreeMap<>();
        Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();
        
        for (int i = 0; i < size; i++) {
            String value = "value" + i;
            hashMap.put(i, value);
            linkedHashMap.put(i, value);
            treeMap.put(i, value);
            concurrentMap.put(i, value);
        }
        
        // 测试查找性能
        int searchCount = 10000;
        Random random = new Random();
        
        // HashMap查找
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < searchCount; i++) {
            hashMap.get(random.nextInt(size));
        }
        long hashMapTime = System.currentTimeMillis() - startTime;
        
        // LinkedHashMap查找
        startTime = System.currentTimeMillis();
        for (int i = 0; i < searchCount; i++) {
            linkedHashMap.get(random.nextInt(size));
        }
        long linkedHashMapTime = System.currentTimeMillis() - startTime;
        
        // TreeMap查找
        startTime = System.currentTimeMillis();
        for (int i = 0; i < searchCount; i++) {
            treeMap.get(random.nextInt(size));
        }
        long treeMapTime = System.currentTimeMillis() - startTime;
        
        // ConcurrentHashMap查找
        startTime = System.currentTimeMillis();
        for (int i = 0; i < searchCount; i++) {
            concurrentMap.get(random.nextInt(size));
        }
        long concurrentMapTime = System.currentTimeMillis() - startTime;
        
        System.out.println("HashMap查找耗时: " + hashMapTime + "ms");
        System.out.println("LinkedHashMap查找耗时: " + linkedHashMapTime + "ms");
        System.out.println("TreeMap查找耗时: " + treeMapTime + "ms");
        System.out.println("ConcurrentHashMap查找耗时: " + concurrentMapTime + "ms");
    }
    
    /**
     * 遍历性能测试
     */
    private static void testIterationPerformance(int size) {
        System.out.println("\n--- 遍历性能测试 ---");
        
        // 准备测试数据
        Map<Integer, String> hashMap = new HashMap<>();
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
        Map<Integer, String> treeMap = new TreeMap<>();
        
        for (int i = 0; i < size; i++) {
            String value = "value" + i;
            hashMap.put(i, value);
            linkedHashMap.put(i, value);
            treeMap.put(i, value);
        }
        
        // HashMap遍历
        long startTime = System.currentTimeMillis();
        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            // 遍历操作
        }
        long hashMapTime = System.currentTimeMillis() - startTime;
        
        // LinkedHashMap遍历
        startTime = System.currentTimeMillis();
        for (Map.Entry<Integer, String> entry : linkedHashMap.entrySet()) {
            // 遍历操作
        }
        long linkedHashMapTime = System.currentTimeMillis() - startTime;
        
        // TreeMap遍历
        startTime = System.currentTimeMillis();
        for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
            // 遍历操作
        }
        long treeMapTime = System.currentTimeMillis() - startTime;
        
        System.out.println("HashMap遍历耗时: " + hashMapTime + "ms");
        System.out.println("LinkedHashMap遍历耗时: " + linkedHashMapTime + "ms");
        System.out.println("TreeMap遍历耗时: " + treeMapTime + "ms");
    }
}

/**
 * 用于演示哈希冲突的键类
 * 具有相同的hashCode但不同的equals结果
 */
class HashCollisionKey {
    private String name;
    private int hashCode;
    
    public HashCollisionKey(String name, int hashCode) {
        this.name = name;
        this.hashCode = hashCode;
    }
    
    @Override
    public int hashCode() {
        return hashCode; // 故意返回相同的hashCode
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        HashCollisionKey that = (HashCollisionKey) obj;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
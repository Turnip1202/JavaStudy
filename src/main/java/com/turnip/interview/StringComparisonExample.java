package com.turnip.interview;

/**
 * String、StringBuilder、StringBuffer性能比较示例
 * 演示三种字符串操作方式的性能差异
 */
public class StringComparisonExample {
    
    public static void main(String[] args) {
        // 字符串不可变性演示
        demonstrateStringImmutability();
        
        // 性能比较
        performanceComparison();
        
        // 线程安全性演示
        demonstrateThreadSafety();
    }
    
    /**
     * 演示String的不可变性
     */
    public static void demonstrateStringImmutability() {
        System.out.println("=== String不可变性演示 ===");
        
        String str1 = "Hello";
        String str2 = str1;
        str1 = str1 + " World"; // 创建新的String对象
        
        System.out.println("str1: " + str1); // Hello World
        System.out.println("str2: " + str2); // Hello（原对象未改变）
        
        // 字符串池演示
        String s1 = "Java";
        String s2 = "Java";
        String s3 = new String("Java");
        
        System.out.println("s1 == s2: " + (s1 == s2)); // true，字符串池
        System.out.println("s1 == s3: " + (s1 == s3)); // false，不同对象
        System.out.println("s1.equals(s3): " + s1.equals(s3)); // true，内容相同
    }
    
    /**
     * 性能比较测试
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能比较测试 ===");
        int iterations = 10000;
        
        // String拼接测试
        long startTime = System.currentTimeMillis();
        String str = "";
        for (int i = 0; i < iterations; i++) {
            str += "a"; // 每次都创建新对象
        }
        long stringTime = System.currentTimeMillis() - startTime;
        System.out.println("String拼接耗时: " + stringTime + "ms");
        
        // StringBuilder拼接测试
        startTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a"); // 在原有缓冲区上操作
        }
        String sbResult = sb.toString();
        long sbTime = System.currentTimeMillis() - startTime;
        System.out.println("StringBuilder拼接耗时: " + sbTime + "ms");
        
        // StringBuffer拼接测试
        startTime = System.currentTimeMillis();
        StringBuffer sbf = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sbf.append("a"); // 线程安全版本，稍慢
        }
        String sbfResult = sbf.toString();
        long sbfTime = System.currentTimeMillis() - startTime;
        System.out.println("StringBuffer拼接耗时: " + sbfTime + "ms");
        
        System.out.println("性能比较：StringBuilder > StringBuffer >> String");
    }
    
    /**
     * 线程安全性演示
     */
    public static void demonstrateThreadSafety() {
        System.out.println("\n=== 线程安全性演示 ===");
        
        // StringBuilder线程不安全演示
        StringBuilder sb = new StringBuilder();
        Thread[] threads1 = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            threads1[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    sb.append("a");
                }
            });
            threads1[i].start();
        }
        
        // 等待所有线程完成
        for (Thread t : threads1) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("StringBuilder最终长度: " + sb.length() + " (期望: 10000)");
        if (sb.length() != 10000) {
            System.out.println("StringBuilder在多线程环境下不安全！");
        }
        
        // StringBuffer线程安全演示
        StringBuffer sbf = new StringBuffer();
        Thread[] threads2 = new Thread[10];
        
        for (int i = 0; i < 10; i++) {
            threads2[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    sbf.append("a");
                }
            });
            threads2[i].start();
        }
        
        // 等待所有线程完成
        for (Thread t : threads2) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("StringBuffer最终长度: " + sbf.length() + " (期望: 10000)");
        if (sbf.length() == 10000) {
            System.out.println("StringBuffer在多线程环境下是安全的！");
        }
    }
    
    /**
     * StringBuilder常用方法演示
     */
    public static void demonstrateStringBuilderMethods() {
        System.out.println("\n=== StringBuilder常用方法演示 ===");
        
        StringBuilder sb = new StringBuilder("Hello");
        
        // append - 追加
        sb.append(" World");
        System.out.println("append后: " + sb);
        
        // insert - 插入
        sb.insert(5, ",");
        System.out.println("insert后: " + sb);
        
        // delete - 删除
        sb.delete(5, 6);
        System.out.println("delete后: " + sb);
        
        // reverse - 反转
        sb.reverse();
        System.out.println("reverse后: " + sb);
        
        // setCharAt - 设置字符
        sb.setCharAt(0, 'h');
        System.out.println("setCharAt后: " + sb);
        
        // 容量相关
        System.out.println("length: " + sb.length());
        System.out.println("capacity: " + sb.capacity());
    }
}
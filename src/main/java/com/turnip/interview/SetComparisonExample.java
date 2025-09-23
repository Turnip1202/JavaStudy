package com.turnip.interview;

import java.util.*;

/**
 * Set集合比较示例
 * 演示HashSet、LinkedHashSet、TreeSet的特点和区别
 */
public class SetComparisonExample {
    
    public static void main(String[] args) {
        // 基本特性演示
        demonstrateBasicFeatures();
        
        // 去重机制演示
        demonstrateDuplication();
        
        // 自定义对象的Set操作
        demonstrateCustomObjects();
        
        // 性能比较
        performanceComparison();
    }
    
    /**
     * 基本特性演示
     */
    public static void demonstrateBasicFeatures() {
        System.out.println("=== Set基本特性演示 ===");
        
        // HashSet - 无序，基于哈希表
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Banana");
        hashSet.add("Apple");
        hashSet.add("Cherry");
        hashSet.add("Apple"); // 重复元素，不会被添加
        System.out.println("HashSet (无序): " + hashSet);
        
        // LinkedHashSet - 维护插入顺序
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("Banana");
        linkedHashSet.add("Apple");
        linkedHashSet.add("Cherry");
        linkedHashSet.add("Apple"); // 重复元素，不会被添加
        System.out.println("LinkedHashSet (插入顺序): " + linkedHashSet);
        
        // TreeSet - 自然排序或比较器排序
        Set<String> treeSet = new TreeSet<>();
        treeSet.add("Banana");
        treeSet.add("Apple");
        treeSet.add("Cherry");
        treeSet.add("Apple"); // 重复元素，不会被添加
        System.out.println("TreeSet (自然排序): " + treeSet);
        
        // TreeSet with custom comparator
        Set<String> treeSetDesc = new TreeSet<>(Collections.reverseOrder());
        treeSetDesc.add("Banana");
        treeSetDesc.add("Apple");
        treeSetDesc.add("Cherry");
        System.out.println("TreeSet (降序): " + treeSetDesc);
    }
    
    /**
     * 去重机制演示
     */
    public static void demonstrateDuplication() {
        System.out.println("\n=== 去重机制演示 ===");
        
        // 基本类型去重
        Set<Integer> numbers = new HashSet<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(1); // 重复
        numbers.add(3);
        System.out.println("数字去重: " + numbers);
        
        // 字符串去重
        Set<String> strings = new HashSet<>();
        strings.add("hello");
        strings.add("world");
        strings.add("hello"); // 重复
        System.out.println("字符串去重: " + strings);
        
        // null值处理
        Set<String> nullSet = new HashSet<>();
        nullSet.add("test");
        nullSet.add(null);
        nullSet.add(null); // 重复的null
        System.out.println("包含null的Set: " + nullSet);
        
        // TreeSet不允许null
        try {
            Set<String> treeSet = new TreeSet<>();
            treeSet.add("test");
            treeSet.add(null); // 会抛出异常
        } catch (NullPointerException e) {
            System.out.println("TreeSet不允许null值");
        }
    }
    
    /**
     * 自定义对象的Set操作
     */
    public static void demonstrateCustomObjects() {
        System.out.println("\n=== 自定义对象Set操作 ===");
        
        // 使用Student对象
        Set<Student> studentSet = new HashSet<>();
        studentSet.add(new Student("张三", 20));
        studentSet.add(new Student("李四", 21));
        studentSet.add(new Student("张三", 20)); // 相同的学生（重写了equals和hashCode）
        studentSet.add(new Student("张三", 22)); // 不同年龄的张三
        
        System.out.println("学生Set（HashSet）:");
        for (Student student : studentSet) {
            System.out.println("  " + student);
        }
        
        // TreeSet需要实现Comparable或提供Comparator
        Set<Student> treeStudentSet = new TreeSet<>();
        treeStudentSet.add(new Student("张三", 20));
        treeStudentSet.add(new Student("李四", 21));
        treeStudentSet.add(new Student("王五", 19));
        
        System.out.println("学生Set（TreeSet，按年龄排序）:");
        for (Student student : treeStudentSet) {
            System.out.println("  " + student);
        }
        
        // 使用自定义比较器的TreeSet
        Set<Student> nameTreeSet = new TreeSet<>((s1, s2) -> s1.getName().compareTo(s2.getName()));
        nameTreeSet.add(new Student("张三", 20));
        nameTreeSet.add(new Student("李四", 21));
        nameTreeSet.add(new Student("王五", 19));
        nameTreeSet.add(new Student("赵六", 22));
        
        System.out.println("学生Set（TreeSet，按姓名排序）:");
        for (Student student : nameTreeSet) {
            System.out.println("  " + student);
        }
    }
    
    /**
     * 性能比较
     */
    public static void performanceComparison() {
        System.out.println("\n=== 性能比较 ===");
        int size = 100000;
        
        // 添加性能测试
        testAddPerformance(size);
        
        // 查找性能测试
        testSearchPerformance(size);
        
        // 删除性能测试
        testRemovePerformance(size);
    }
    
    /**
     * 添加性能测试
     */
    private static void testAddPerformance(int size) {
        System.out.println("\n--- 添加性能测试 ---");
        
        // HashSet添加
        Set<Integer> hashSet = new HashSet<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            hashSet.add(i);
        }
        long hashSetTime = System.currentTimeMillis() - startTime;
        
        // LinkedHashSet添加
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            linkedHashSet.add(i);
        }
        long linkedHashSetTime = System.currentTimeMillis() - startTime;
        
        // TreeSet添加
        Set<Integer> treeSet = new TreeSet<>();
        startTime = System.currentTimeMillis();
        for (int i = 0; i < size; i++) {
            treeSet.add(i);
        }
        long treeSetTime = System.currentTimeMillis() - startTime;
        
        System.out.println("HashSet添加耗时: " + hashSetTime + "ms");
        System.out.println("LinkedHashSet添加耗时: " + linkedHashSetTime + "ms");
        System.out.println("TreeSet添加耗时: " + treeSetTime + "ms");
    }
    
    /**
     * 查找性能测试
     */
    private static void testSearchPerformance(int size) {
        System.out.println("\n--- 查找性能测试 ---");
        
        // 准备测试数据
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        
        for (int i = 0; i < size; i++) {
            hashSet.add(i);
            linkedHashSet.add(i);
            treeSet.add(i);
        }
        
        // 测试查找性能
        int searchCount = 10000;
        Random random = new Random();
        
        // HashSet查找
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < searchCount; i++) {
            hashSet.contains(random.nextInt(size));
        }
        long hashSetTime = System.currentTimeMillis() - startTime;
        
        // LinkedHashSet查找
        startTime = System.currentTimeMillis();
        for (int i = 0; i < searchCount; i++) {
            linkedHashSet.contains(random.nextInt(size));
        }
        long linkedHashSetTime = System.currentTimeMillis() - startTime;
        
        // TreeSet查找
        startTime = System.currentTimeMillis();
        for (int i = 0; i < searchCount; i++) {
            treeSet.contains(random.nextInt(size));
        }
        long treeSetTime = System.currentTimeMillis() - startTime;
        
        System.out.println("HashSet查找耗时: " + hashSetTime + "ms");
        System.out.println("LinkedHashSet查找耗时: " + linkedHashSetTime + "ms");
        System.out.println("TreeSet查找耗时: " + treeSetTime + "ms");
    }
    
    /**
     * 删除性能测试
     */
    private static void testRemovePerformance(int size) {
        System.out.println("\n--- 删除性能测试 ---");
        
        // 准备测试数据
        Set<Integer> hashSet = new HashSet<>();
        Set<Integer> linkedHashSet = new LinkedHashSet<>();
        Set<Integer> treeSet = new TreeSet<>();
        
        for (int i = 0; i < size; i++) {
            hashSet.add(i);
            linkedHashSet.add(i);
            treeSet.add(i);
        }
        
        // 删除一半元素
        int removeCount = size / 2;
        Random random = new Random();
        
        // HashSet删除
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < removeCount; i++) {
            hashSet.remove(random.nextInt(size));
        }
        long hashSetTime = System.currentTimeMillis() - startTime;
        
        // LinkedHashSet删除
        startTime = System.currentTimeMillis();
        for (int i = 0; i < removeCount; i++) {
            linkedHashSet.remove(random.nextInt(size));
        }
        long linkedHashSetTime = System.currentTimeMillis() - startTime;
        
        // TreeSet删除
        startTime = System.currentTimeMillis();
        for (int i = 0; i < removeCount; i++) {
            treeSet.remove(random.nextInt(size));
        }
        long treeSetTime = System.currentTimeMillis() - startTime;
        
        System.out.println("HashSet删除耗时: " + hashSetTime + "ms");
        System.out.println("LinkedHashSet删除耗时: " + linkedHashSetTime + "ms");
        System.out.println("TreeSet删除耗时: " + treeSetTime + "ms");
    }
}

/**
 * 学生类，用于演示自定义对象在Set中的使用
 * 实现了Comparable接口，重写了equals和hashCode方法
 */
class Student implements Comparable<Student> {
    private String name;
    private int age;
    
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return age == student.age && Objects.equals(name, student.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
    
    @Override
    public int compareTo(Student other) {
        // 按年龄排序
        return Integer.compare(this.age, other.age);
    }
    
    @Override
    public String toString() {
        return name + "(" + age + "岁)";
    }
}
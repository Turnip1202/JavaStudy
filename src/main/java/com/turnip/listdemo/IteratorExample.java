package com.turnip.listdemo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IteratorExample {
    public static void main(String[] args) {
        // 创建一个列表并添加元素
        List<String> fruits = new ArrayList<>();
        fruits.add("苹果");
        fruits.add("香蕉");
        fruits.add("橙子");
        fruits.add("葡萄");
        fruits.add("西瓜");

        System.out.println("===== 初始集合 =====");
        System.out.println(fruits);

        // 1. 使用迭代器遍历集合
        System.out.println("\n===== 迭代器遍历元素 =====");
        Iterator<String> iterator = fruits.iterator();
        while (iterator.hasNext()) { // 判断是否有下一个元素
            String fruit = iterator.next(); // 获取下一个元素
            System.out.println(fruit);
        }

        // 2. 使用迭代器遍历并移除特定元素
        System.out.println("\n===== 遍历并移除元素 =====");
        iterator = fruits.iterator(); // 重新获取迭代器
        while (iterator.hasNext()) {
            String fruit = iterator.next();
            if (fruit.equals("香蕉")) {
                iterator.remove(); // 使用迭代器的remove()方法移除元素
                System.out.println("已移除：" + fruit);
            }
        }

        System.out.println("\n===== 移除元素后的集合 =====");
        System.out.println(fruits);
    }
}
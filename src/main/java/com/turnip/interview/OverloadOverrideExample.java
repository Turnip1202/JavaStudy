package com.turnip.interview;

/**
 * 重载和重写示例
 * 演示方法重载（Overload）和方法重写（Override）的区别
 */
public class OverloadOverrideExample {
    
    public static void main(String[] args) {
        // 重载演示
        demonstrateOverload();
        
        // 重写演示
        demonstrateOverride();
    }
    
    /**
     * 重载演示
     */
    public static void demonstrateOverload() {
        System.out.println("=== 方法重载演示 ===");
        
        Calculator calc = new Calculator();
        
        // 调用不同的重载方法
        System.out.println("add(1, 2): " + calc.add(1, 2));
        System.out.println("add(1, 2, 3): " + calc.add(1, 2, 3));
        System.out.println("add(1.5, 2.5): " + calc.add(1.5, 2.5));
        System.out.println("add(\"Hello\", \"World\"): " + calc.add("Hello", "World"));
        
        // 可变参数重载
        System.out.println("add(1, 2, 3, 4, 5): " + calc.add(1, 2, 3, 4, 5));
    }
    
    /**
     * 重写演示
     */
    public static void demonstrateOverride() {
        System.out.println("\n=== 方法重写演示 ===");
        
        // 父类引用指向子类对象，体现多态
        Animal[] animals = {
            new Dog("旺财"),
            new Cat("咪咪"),
            new Bird("小鸟")
        };
        
        for (Animal animal : animals) {
            animal.makeSound(); // 调用各自重写的方法
            animal.move();      // 调用各自重写的方法
        }
    }
}

/**
 * 方法重载示例类
 */
class Calculator {
    
    // 重载方法1：两个int参数
    public int add(int a, int b) {
        System.out.println("调用了add(int, int)");
        return a + b;
    }
    
    // 重载方法2：三个int参数
    public int add(int a, int b, int c) {
        System.out.println("调用了add(int, int, int)");
        return a + b + c;
    }
    
    // 重载方法3：两个double参数
    public double add(double a, double b) {
        System.out.println("调用了add(double, double)");
        return a + b;
    }
    
    // 重载方法4：两个String参数
    public String add(String a, String b) {
        System.out.println("调用了add(String, String)");
        return a + b;
    }
    
    // 重载方法5：可变参数
    public int add(int... numbers) {
        System.out.println("调用了add(int...)");
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum;
    }
    
    // 注意：仅返回类型不同不能构成重载
    // public double add(int a, int b) { // 编译错误
    //     return a + b;
    // }
}

/**
 * 方法重写示例 - 动物基类
 */
abstract class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    // 抽象方法，子类必须重写
    public abstract void makeSound();
    
    // 具体方法，子类可以重写
    public void move() {
        System.out.println(name + "在移动");
    }
    
    // final方法，子类不能重写
    public final void eat() {
        System.out.println(name + "在吃东西");
    }
}

/**
 * 狗类 - 重写父类方法
 */
class Dog extends Animal {
    
    public Dog(String name) {
        super(name);
    }
    
    // 重写抽象方法（必须重写）
    @Override
    public void makeSound() {
        System.out.println(name + "在汪汪叫");
    }
    
    // 重写具体方法（可选重写）
    @Override
    public void move() {
        System.out.println(name + "在跑步");
    }
    
    // 子类特有方法
    public void wagTail() {
        System.out.println(name + "在摇尾巴");
    }
}

/**
 * 猫类 - 重写父类方法
 */
class Cat extends Animal {
    
    public Cat(String name) {
        super(name);
    }
    
    // 重写抽象方法
    @Override
    public void makeSound() {
        System.out.println(name + "在喵喵叫");
    }
    
    // 重写具体方法
    @Override
    public void move() {
        System.out.println(name + "在悄悄走路");
    }
    
    // 子类特有方法
    public void climb() {
        System.out.println(name + "在爬树");
    }
}

/**
 * 鸟类 - 重写父类方法
 */
class Bird extends Animal {
    
    public Bird(String name) {
        super(name);
    }
    
    // 重写抽象方法
    @Override
    public void makeSound() {
        System.out.println(name + "在唧唧叫");
    }
    
    // 重写具体方法
    @Override
    public void move() {
        System.out.println(name + "在飞翔");
    }
    
    // 子类特有方法
    public void fly() {
        System.out.println(name + "在高空飞行");
    }
}
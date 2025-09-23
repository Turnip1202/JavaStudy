package com.turnip.interview;

/**
 * Java基本数据类型示例
 * 演示自动装箱、拆箱，以及包装类的特性
 */
public class BasicDataTypesExample {
    
    public static void main(String[] args) {
        // 基本数据类型演示
        demonstrateBasicTypes();
        
        // 自动装箱和拆箱演示
        demonstrateAutoBoxing();
        
        // Integer缓存池演示
        demonstrateIntegerCache();
    }
    
    /**
     * 基本数据类型演示
     */
    public static void demonstrateBasicTypes() {
        System.out.println("=== 基本数据类型演示 ===");
        
        // 各种基本数据类型
        byte byteVar = 127;
        short shortVar = 32767;
        int intVar = 2147483647;
        long longVar = 9223372036854775807L;
        float floatVar = 3.14f;
        double doubleVar = 3.14159265359;
        char charVar = 'A';
        boolean boolVar = true;
        
        System.out.println("byte: " + byteVar + ", 字节数: " + Byte.BYTES);
        System.out.println("short: " + shortVar + ", 字节数: " + Short.BYTES);
        System.out.println("int: " + intVar + ", 字节数: " + Integer.BYTES);
        System.out.println("long: " + longVar + ", 字节数: " + Long.BYTES);
        System.out.println("float: " + floatVar + ", 字节数: " + Float.BYTES);
        System.out.println("double: " + doubleVar + ", 字节数: " + Double.BYTES);
        System.out.println("char: " + charVar + ", 字节数: " + Character.BYTES);
        System.out.println("boolean: " + boolVar);
    }
    
    /**
     * 自动装箱和拆箱演示
     */
    public static void demonstrateAutoBoxing() {
        System.out.println("\n=== 自动装箱和拆箱演示 ===");
        
        // 自动装箱：基本类型 → 包装类
        Integer integerObj = 100; // 等价于 Integer.valueOf(100)
        Double doubleObj = 3.14;  // 等价于 Double.valueOf(3.14)
        
        // 自动拆箱：包装类 → 基本类型
        int intValue = integerObj; // 等价于 integerObj.intValue()
        double doubleValue = doubleObj; // 等价于 doubleObj.doubleValue()
        
        System.out.println("装箱后的Integer: " + integerObj);
        System.out.println("拆箱后的int: " + intValue);
        System.out.println("装箱后的Double: " + doubleObj);
        System.out.println("拆箱后的double: " + doubleValue);
        
        // 包装类可以为null，基本类型不可以
        Integer nullInteger = null;
        try {
            int value = nullInteger; // 这里会抛出NullPointerException
        } catch (NullPointerException e) {
            System.out.println("拆箱null值会抛出NullPointerException");
        }
    }
    
    /**
     * Integer缓存池演示（-128到127）
     */
    public static void demonstrateIntegerCache() {
        System.out.println("\n=== Integer缓存池演示 ===");
        
        // 缓存范围内的比较
        Integer a1 = 100;
        Integer a2 = 100;
        System.out.println("a1 == a2 (100): " + (a1 == a2)); // true，同一个对象
        System.out.println("a1.equals(a2): " + a1.equals(a2)); // true
        
        // 超出缓存范围的比较
        Integer b1 = 200;
        Integer b2 = 200;
        System.out.println("b1 == b2 (200): " + (b1 == b2)); // false，不同对象
        System.out.println("b1.equals(b2): " + b1.equals(b2)); // true
        
        // 使用new关键字
        Integer c1 = new Integer(100);
        Integer c2 = new Integer(100);
        System.out.println("new Integer(100) == new Integer(100): " + (c1 == c2)); // false
        System.out.println("new Integer(100).equals(new Integer(100)): " + c1.equals(c2)); // true
        
        // 基本类型和包装类比较
        int primitive = 100;
        Integer wrapper = 100;
        System.out.println("primitive == wrapper: " + (primitive == wrapper)); // true，会拆箱
    }
}
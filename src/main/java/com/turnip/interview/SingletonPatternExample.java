package com.turnip.interview;

/**
 * 单例模式实现示例
 * 演示五种常见的单例模式实现方式
 */
public class SingletonPatternExample {
    
    public static void main(String[] args) {
        // 测试各种单例实现
        testSingleton();
        
        // 线程安全测试
        testThreadSafety();
    }
    
    /**
     * 测试各种单例实现
     */
    public static void testSingleton() {
        System.out.println("=== 单例模式测试 ===");
        
        // 饿汉式
        EagerSingleton eager1 = EagerSingleton.getInstance();
        EagerSingleton eager2 = EagerSingleton.getInstance();
        System.out.println("饿汉式单例: " + (eager1 == eager2));
        
        // 懒汉式
        LazySingleton lazy1 = LazySingleton.getInstance();
        LazySingleton lazy2 = LazySingleton.getInstance();
        System.out.println("懒汉式单例: " + (lazy1 == lazy2));
        
        // 双重检查锁定
        DoubleCheckedSingleton dcl1 = DoubleCheckedSingleton.getInstance();
        DoubleCheckedSingleton dcl2 = DoubleCheckedSingleton.getInstance();
        System.out.println("双重检查锁定单例: " + (dcl1 == dcl2));
        
        // 静态内部类
        StaticInnerSingleton inner1 = StaticInnerSingleton.getInstance();
        StaticInnerSingleton inner2 = StaticInnerSingleton.getInstance();
        System.out.println("静态内部类单例: " + (inner1 == inner2));
        
        // 枚举实现
        EnumSingleton enum1 = EnumSingleton.INSTANCE;
        EnumSingleton enum2 = EnumSingleton.INSTANCE;
        System.out.println("枚举单例: " + (enum1 == enum2));
    }
    
    /**
     * 线程安全测试
     */
    public static void testThreadSafety() {
        System.out.println("\n=== 线程安全测试 ===");
        
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        
        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                // 测试双重检查锁定的线程安全性
                DoubleCheckedSingleton instance = DoubleCheckedSingleton.getInstance();
                System.out.println("线程" + Thread.currentThread().getName() 
                    + "获取实例: " + instance.hashCode());
            });
        }
        
        for (Thread thread : threads) {
            thread.start();
        }
        
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 1. 饿汉式单例
 * 优点：线程安全，实现简单
 * 缺点：类加载时就创建实例，可能造成资源浪费
 */
class EagerSingleton {
    // 类加载时就创建实例
    private static final EagerSingleton INSTANCE = new EagerSingleton();
    
    // 私有构造方法
    private EagerSingleton() {
        System.out.println("EagerSingleton实例创建");
    }
    
    public static EagerSingleton getInstance() {
        return INSTANCE;
    }
    
    public void doSomething() {
        System.out.println("EagerSingleton doing something...");
    }
}

/**
 * 2. 懒汉式单例（线程不安全版本）
 * 优点：延迟创建，节省内存
 * 缺点：线程不安全
 */
class LazySingleton {
    private static LazySingleton instance;
    
    private LazySingleton() {
        System.out.println("LazySingleton实例创建");
    }
    
    // 线程不安全的实现
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
    
    // 线程安全的实现（但性能较差）
    public static synchronized LazySingleton getInstanceSafe() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}

/**
 * 3. 双重检查锁定单例
 * 优点：延迟创建，线程安全，性能好
 * 缺点：实现复杂
 */
class DoubleCheckedSingleton {
    // volatile保证可见性和禁止指令重排序
    private static volatile DoubleCheckedSingleton instance;
    
    private DoubleCheckedSingleton() {
        System.out.println("DoubleCheckedSingleton实例创建");
    }
    
    public static DoubleCheckedSingleton getInstance() {
        // 第一次检查
        if (instance == null) {
            synchronized (DoubleCheckedSingleton.class) {
                // 第二次检查
                if (instance == null) {
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
}

/**
 * 4. 静态内部类单例
 * 优点：延迟创建，线程安全，性能好，实现简单
 * 推荐使用
 */
class StaticInnerSingleton {
    
    private StaticInnerSingleton() {
        System.out.println("StaticInnerSingleton实例创建");
    }
    
    // 静态内部类，只有在调用getInstance()时才会被加载
    private static class SingletonHolder {
        private static final StaticInnerSingleton INSTANCE = new StaticInnerSingleton();
    }
    
    public static StaticInnerSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}

/**
 * 5. 枚举实现单例
 * 优点：线程安全，防止反序列化，实现简单
 * 缺点：不支持延迟创建
 */
enum EnumSingleton {
    INSTANCE;
    
    // 构造方法
    EnumSingleton() {
        System.out.println("EnumSingleton实例创建");
    }
    
    public void doSomething() {
        System.out.println("EnumSingleton doing something...");
    }
}

/**
 * 6. 容器式单例
 * 适用于管理多个单例对象
 */
class ContainerSingleton {
    private static java.util.Map<String, Object> singletonMap = 
        new java.util.concurrent.ConcurrentHashMap<>();
    
    private ContainerSingleton() {}
    
    public static void registerService(String key, Object instance) {
        if (!singletonMap.containsKey(key)) {
            singletonMap.put(key, instance);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T getService(String key) {
        return (T) singletonMap.get(key);
    }
}

/**
 * 单例模式的应用示例 - 数据库连接池
 */
class DatabaseConnectionPool {
    private static volatile DatabaseConnectionPool instance;
    private final java.util.Queue<String> connectionPool;
    private final int maxConnections = 10;
    
    private DatabaseConnectionPool() {
        connectionPool = new java.util.concurrent.ConcurrentLinkedQueue<>();
        // 初始化连接池
        for (int i = 0; i < maxConnections; i++) {
            connectionPool.offer("Connection-" + i);
        }
        System.out.println("数据库连接池初始化完成，连接数: " + maxConnections);
    }
    
    public static DatabaseConnectionPool getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionPool.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionPool();
                }
            }
        }
        return instance;
    }
    
    public String getConnection() {
        String connection = connectionPool.poll();
        if (connection != null) {
            System.out.println("获取连接: " + connection);
        } else {
            System.out.println("连接池已满，无可用连接");
        }
        return connection;
    }
    
    public void releaseConnection(String connection) {
        if (connection != null) {
            connectionPool.offer(connection);
            System.out.println("释放连接: " + connection);
        }
    }
    
    public int getAvailableConnections() {
        return connectionPool.size();
    }
}
package com.turnip.thread;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockExample {
    // 模拟缓存
    private final Map<String, Object> cache = new HashMap<>();
    // 读写锁
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    // 读锁
    private  final Lock readLock = rwLock.readLock();
    // 写锁
    private final Lock writeLock = rwLock.writeLock();

    // 读操作，使用读锁
    public Object get(String key) {
        readLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 读取数据: " + key);
            return cache.get(key);
        } finally {
            readLock.unlock();
        }
    }

    // 写操作，使用写锁
    public void put(String key, Object value) {
        writeLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " 写入数据: " + key);
            cache.put(key, value);
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        ReadWriteLockExample cache = new ReadWriteLockExample();

        // 创建5个读线程
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    cache.get("key" + j);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "读线程" + i).start();
        }

        // 创建2个写线程
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 3; j++) {
                    cache.put("key" + j, "value" + j);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "写线程" + i).start();
        }
    }
}

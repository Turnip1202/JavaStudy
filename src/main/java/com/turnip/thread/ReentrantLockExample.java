package com.turnip.thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockExample {
    private int count = 0;
    private static final Lock lock = new ReentrantLock(); // 可重入锁

    public void increment() {
        // 获取锁
        lock.lock();
        try {
            count++;
        } finally {
            // 确保锁一定会被释放
            lock.unlock();
        }
    }

    // 尝试在指定时间内获取锁
    public boolean tryIncrementWithTimeout(long timeout) throws InterruptedException {
        // 尝试在指定时间内获取锁
        if (lock.tryLock(timeout, java.util.concurrent.TimeUnit.MILLISECONDS)) {
            try {
                count++;
                return true;
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        ReentrantLockExample example = new ReentrantLockExample();

        // 演示超时获取锁的特性
        Thread t1 = new Thread(() -> {
            lock.lock(); // t1先获取锁
            try {
                System.out.println("线程1获取到锁，将持有500ms");
                Thread.sleep(500); // 持有锁500ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                System.out.println("线程1释放锁");
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                System.out.println("线程2尝试获取锁，超时时间300ms");
                boolean success = example.tryIncrementWithTimeout(300);
                if (success) {
                    System.out.println("线程2成功获取锁");
                } else {
                    System.out.println("线程2获取锁超时");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        Thread.sleep(100); // 确保t1先启动
        t2.start();

        t1.join();
        t2.join();

        System.out.println("最终计数: " + example.getCount());
    }
}

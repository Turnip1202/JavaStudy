package com.turnip.thread;

public class SynchronizedExample {
    // 共享资源
    private int count = 0;

    // 未加锁的方法，会出现线程安全问题
    public void incrementWithoutLock() {
        count++;
    }

    // 使用synchronized修饰的方法，保证线程安全
    public synchronized void incrementWithLock() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedExample example = new SynchronizedExample();

        // 创建10个线程，每个线程执行1000次递增
        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    // 这里尝试替换为incrementWithLock()看看结果
                    example.incrementWithLock();
                }
            });
        }

        // 启动所有线程
        for (Thread thread : threads) {
            thread.start();
        }

        // 等待所有线程执行完毕
        for (Thread thread : threads) {
            thread.join();
        }

        // 理想结果应该是10000，但使用incrementWithoutLock()时通常不是
        System.out.println("最终计数: " + example.getCount());
    }
}

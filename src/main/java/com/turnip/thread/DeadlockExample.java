package com.turnip.thread;

public class DeadlockExample {
    // 创建两个锁对象
    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    // 线程1: 先获取lockA，再尝试获取lockB
    static class Thread1 extends Thread {
        public void run() {
            synchronized (lockA) {
                System.out.println("线程1获取了lockA，尝试获取lockB");
                try {
                    Thread.sleep(100); // 增加死锁概率（让线程2有机会先获取lockB）
                } catch (InterruptedException e) {}

                synchronized (lockB) { // 此时线程1会等待lockB
                    System.out.println("线程1获取了lockB");
                }
            }
        }
    }

    // 线程2: 先获取lockB，再尝试获取lockA（与线程1顺序相反）
    static class Thread2 extends Thread {
        public void run() {
            synchronized (lockB) { // 恢复为先获取lockB
                System.out.println("线程2获取了lockB，尝试获取lockA");
                try {
                    Thread.sleep(100); // 增加死锁概率
                } catch (InterruptedException e) {}

                synchronized (lockA) { // 此时线程2会等待lockA
                    System.out.println("线程2获取了lockA");
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread1().start();
        new Thread2().start();
    }
}
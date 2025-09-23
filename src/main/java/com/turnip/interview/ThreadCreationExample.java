package com.turnip.interview;

import java.util.concurrent.*;

/**
 * 线程创建方式示例
 * 演示四种创建线程的方式及其特点
 */
public class ThreadCreationExample {
    
    public static void main(String[] args) {
        System.out.println("=== 四种创建线程的方式演示 ===");
        
        // 方式1：继承Thread类
        demonstrateThreadExtension();
        
        // 方式2：实现Runnable接口
        demonstrateRunnableInterface();
        
        // 方式3：实现Callable接口
        demonstrateCallableInterface();
        
        // 方式4：线程池方式
        demonstrateThreadPool();
        
        // 线程状态演示
        demonstrateThreadStates();
    }
    
    /**
     * 方式1：继承Thread类
     */
    public static void demonstrateThreadExtension() {
        System.out.println("\n--- 方式1：继承Thread类 ---");
        
        MyThread1 thread1 = new MyThread1("Thread-1");
        MyThread1 thread2 = new MyThread1("Thread-2");
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 方式2：实现Runnable接口
     */
    public static void demonstrateRunnableInterface() {
        System.out.println("\n--- 方式2：实现Runnable接口 ---");
        
        MyRunnable runnable = new MyRunnable();
        Thread thread1 = new Thread(runnable, "Runnable-1");
        Thread thread2 = new Thread(runnable, "Runnable-2");
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Lambda表达式方式
        Thread lambdaThread = new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                System.out.println("Lambda线程: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Lambda-Thread");
        
        lambdaThread.start();
        try {
            lambdaThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 方式3：实现Callable接口
     */
    public static void demonstrateCallableInterface() {
        System.out.println("\n--- 方式3：实现Callable接口 ---");
        
        MyCallable callable = new MyCallable("计算任务");
        FutureTask<String> futureTask = new FutureTask<>(callable);
        Thread thread = new Thread(futureTask, "Callable-Thread");
        
        thread.start();
        
        try {
            // 获取返回结果，会阻塞直到任务完成
            String result = futureTask.get();
            System.out.println("Callable返回结果: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 方式4：线程池方式
     */
    public static void demonstrateThreadPool() {
        System.out.println("\n--- 方式4：线程池方式 ---");
        
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        // 提交Runnable任务
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executor.submit(() -> {
                System.out.println("线程池任务 " + taskId + " 执行中，线程: " 
                    + Thread.currentThread().getName());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        
        // 提交Callable任务
        Future<Integer> future = executor.submit(() -> {
            Thread.sleep(200);
            return 42;
        });
        
        try {
            Integer result = future.get();
            System.out.println("线程池Callable结果: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 线程状态演示
     */
    public static void demonstrateThreadStates() {
        System.out.println("\n--- 线程状态演示 ---");
        
        Thread stateThread = new Thread(() -> {
            try {
                System.out.println("线程开始执行");
                Thread.sleep(1000); // TIMED_WAITING状态
                
                synchronized (ThreadCreationExample.class) {
                    Thread.sleep(500); // 持有锁的TIMED_WAITING状态
                }
                
                System.out.println("线程执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "StateDemo-Thread");
        
        // NEW状态
        System.out.println("创建后的状态: " + stateThread.getState());
        
        stateThread.start();
        
        // RUNNABLE状态
        System.out.println("启动后的状态: " + stateThread.getState());
        
        try {
            Thread.sleep(500);
            // TIMED_WAITING状态
            System.out.println("睡眠中的状态: " + stateThread.getState());
            
            stateThread.join();
            // TERMINATED状态
            System.out.println("结束后的状态: " + stateThread.getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 继承Thread类的方式
 */
class MyThread1 extends Thread {
    
    public MyThread1(String name) {
        super(name);
    }
    
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(getName() + ": " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 实现Runnable接口的方式
 */
class MyRunnable implements Runnable {
    
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 实现Callable接口的方式
 */
class MyCallable implements Callable<String> {
    private String taskName;
    
    public MyCallable(String taskName) {
        this.taskName = taskName;
    }
    
    @Override
    public String call() throws Exception {
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < 3; i++) {
            result.append(taskName).append("-").append(i).append(" ");
            System.out.println(Thread.currentThread().getName() + " 执行: " + taskName + "-" + i);
            Thread.sleep(100);
        }
        
        return result.toString().trim();
    }
}
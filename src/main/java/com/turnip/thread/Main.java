package com.turnip.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Main {

    /**
     * 创建线程的方式
     * 1.继承Thread类
     * 2.实现Runnable接口
     * 3.实现Callable接口
     */
    public static void main(String[] args) {

        Thread t1= new MyThread1();
        t1.start();

        Runnable mt2= new MyThread2();
        Thread t2 = new Thread(mt2);
        t2.start();

        Callable<String> mt3 = new MyThread3();
        FutureTask<String> ft3 = new FutureTask<>(mt3);
        Thread t3 = new Thread(ft3);
        t3.start();
        try {
            System.out.println("返回的结果："+ft3.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("Main Thread:"+i);
        }



    }
}
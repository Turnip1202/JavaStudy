package com.turnip.io_study;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO (Blocking IO) 示例
 * BIO是同步阻塞IO模型，服务器实现模式为一个连接一个线程，
 * 即客户端有连接请求时服务器端就需要启动一个线程进行处理，
 * 如果这个连接不做任何事情就会造成不必要的线程开销。
 */
public class BioExample {
    public static void main(String[] args) {
        // 启动服务器
        new Thread(() -> {
            try {
                startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // 等待服务器启动
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 启动客户端
        new Thread(() -> {
            try {
                startClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 启动BIO服务器
     * @throws IOException
     */
    public static void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println("BIO服务器已启动，监听端口: 8080");

        while (true) {
            // 阻塞方法，等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("收到客户端连接: " + socket.getRemoteSocketAddress());

            // 为每个连接创建一个线程处理
            executorService.execute(() -> {
                try {
                    handleClient(socket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 处理客户端请求
     * @param socket
     * @throws IOException
     */
    public static void handleClient(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("服务器收到: " + line);
            // 回显消息
            writer.println("服务器回显: " + line);
            
            if ("bye".equals(line)) {
                break;
            }
        }
        
        reader.close();
        writer.close();
        socket.close();
        System.out.println("客户端连接已关闭: " + socket.getRemoteSocketAddress());
    }

    /**
     * 启动客户端
     * @throws IOException
     */
    public static void startClient() throws IOException {
        Socket socket = new Socket("localhost", 8080);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("BIO客户端已连接到服务器");

        // 发送消息
        writer.println("Hello BIO Server");
        System.out.println("客户端收到: " + reader.readLine());

        writer.println("bye");
        System.out.println("客户端收到: " + reader.readLine());

        reader.close();
        writer.close();
        socket.close();
        System.out.println("客户端已关闭");
    }
}
package com.turnip.io_study;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO (Non-blocking IO) 示例
 * NIO是同步非阻塞IO模型，服务器实现模式为多个连接一个线程，
 * 即客户端的连接都会注册到多路复用器上，多路复用器轮询到连接有IO请求时才进行处理。
 */
public class NioExample {
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
     * 启动NIO服务器
     * @throws IOException
     */
    public static void startServer() throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式
        serverSocketChannel.bind(new InetSocketAddress(8081));

        // 创建Selector
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("NIO服务器已启动，监听端口: 8081");

        while (true) {
            // 阻塞直到有事件发生
            int select = selector.select();
            if (select == 0) {
                continue;
            }

            // 获取有事件发生的SelectionKey
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    // 处理连接事件
                    handleAccept(key, selector);
                } else if (key.isReadable()) {
                    // 处理读事件
                    handleRead(key);
                } else if (key.isWritable()) {
                    // 处理写事件
                    handleWrite(key);
                }
            }
        }
    }

    /**
     * 处理连接事件
     * @param key
     * @param selector
     * @throws IOException
     */
    private static void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        if (socketChannel != null) {
            System.out.println("收到客户端连接: " + socketChannel.getRemoteAddress());
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }
    }

    /**
     * 处理读事件
     * @param key
     * @throws IOException
     */
    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int len = socketChannel.read(buffer);
        
        if (len > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String msg = new String(bytes);
            System.out.println("服务器收到: " + msg);
            
            // 准备回写数据
            String response = "服务器回显: " + msg;
            ByteBuffer writeBuffer = ByteBuffer.wrap(response.getBytes());
            socketChannel.write(writeBuffer);
            
            if ("bye".equals(msg)) {
                socketChannel.close();
                System.out.println("客户端连接已关闭: " + socketChannel.getRemoteAddress());
            }
        } else if (len < 0) {
            socketChannel.close();
            System.out.println("客户端连接已关闭: " + socketChannel.getRemoteAddress());
        }
    }

    /**
     * 处理写事件
     * @param key
     * @throws IOException
     */
    private static void handleWrite(SelectionKey key) throws IOException {
        // 在这个示例中，写操作在读操作后直接执行，不需要单独处理
    }

    /**
     * 启动客户端
     * @throws IOException
     */
    public static void startClient() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 8081));
        socketChannel.configureBlocking(false);

        System.out.println("NIO客户端已连接到服务器");

        // 发送消息
        String msg = "Hello NIO Server";
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        socketChannel.write(buffer);

        // 读取服务器响应
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        while (socketChannel.read(readBuffer) == 0) {
            // 等待数据
        }
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);
        System.out.println("客户端收到: " + new String(bytes));

        // 发送bye消息
        String byeMsg = "bye";
        ByteBuffer byeBuffer = ByteBuffer.wrap(byeMsg.getBytes());
        socketChannel.write(byeBuffer);

        // 读取服务器响应
        ByteBuffer byeReadBuffer = ByteBuffer.allocate(1024);
        while (socketChannel.read(byeReadBuffer) == 0) {
            // 等待数据
        }
        byeReadBuffer.flip();
        byte[] byeBytes = new byte[byeReadBuffer.remaining()];
        byeReadBuffer.get(byeBytes);
        System.out.println("客户端收到: " + new String(byeBytes));

        socketChannel.close();
        System.out.println("客户端已关闭");
    }
}
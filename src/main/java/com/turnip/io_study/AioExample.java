package com.turnip.io_study;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.security.Security;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * AIO (Asynchronous IO) 示例
 * AIO是异步非阻塞IO模型，服务器实现模式为多个有效请求一个线程，
 * 客户端的IO请求都是由操作系统先完成了再通知服务器应用去处理，
 * 在此之前客户端不需要等待或轮询。
 */
public class AioExample {
    public static void main(String[] args) {
        // 启动服务器
        new Thread(() -> {
            try {
                startServer();
            } catch (IOException | InterruptedException e) {
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
            } catch (IOException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 启动AIO服务器
     * @throws IOException
     * @throws InterruptedException
     */
    public static void startServer() throws IOException, InterruptedException {
        AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8082));
        System.out.println("AIO服务器已启动，监听端口: 8082");

        // 接受客户端连接
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
                // 继续接受下一个连接
                serverSocketChannel.accept(null, this);
                
                try {
                    System.out.println("收到客户端连接: " + socketChannel.getRemoteAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                // 处理客户端请求
                handleClient(socketChannel);
            }

            @Override
            public void failed(Throwable exc, Void attachment) {
                exc.printStackTrace();
            }
        });

        // 保持服务器运行
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 处理客户端请求
     * @param socketChannel
     */
    private static void handleClient(AsynchronousSocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        
        // 异步读取数据
        readFromChannel(socketChannel, buffer);
    }

    /**
     * 从通道读取数据
     * @param socketChannel
     * @param buffer
     */
    private static void readFromChannel(AsynchronousSocketChannel socketChannel, ByteBuffer buffer) {
        socketChannel.read(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (result > 0) {
                    attachment.flip();
                    byte[] bytes = new byte[attachment.remaining()];
                    attachment.get(bytes);
                    String msg = new String(bytes);
                    System.out.println("服务器收到: " + msg);
                    
                    // 回写数据
                    String response = "服务器回显: " + msg;
                    ByteBuffer writeBuffer = ByteBuffer.wrap(response.getBytes());
                    socketChannel.write(writeBuffer, null, new CompletionHandler<Integer, Void>() {
                        @Override
                        public void completed(Integer result, Void attachment) {
                            if (!"bye".equals(msg)) {
                                // 继续读取数据
                                buffer.clear();
                                readFromChannel(socketChannel, buffer);
                            } else {
                                try {
                                    System.out.println("客户端连接已关闭: " + socketChannel.getRemoteAddress());
                                    socketChannel.close();
                                } catch (IOException e) {
                                    // 忽略关闭异常
                                }
                            }
                        }

                        @Override
                        public void failed(Throwable exc, Void attachment) {
                            try {
                                if (socketChannel.isOpen()) {
                                    System.out.println("客户端连接已关闭: " + socketChannel.getRemoteAddress());
                                    socketChannel.close();
                                }
                            } catch (IOException e) {
                                // 忽略关闭异常
                            }
                        }
                    });
                } else if (result < 0) {
                    try {
                        if (socketChannel.isOpen()) {
                            System.out.println("客户端连接已关闭: " + socketChannel.getRemoteAddress());
                            socketChannel.close();
                        }
                    } catch (IOException e) {
                        // 忽略关闭异常
                    }
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    if (socketChannel.isOpen()) {
                        System.out.println("客户端连接已关闭");
                        socketChannel.close();
                    }
                } catch (IOException e) {
                    // 忽略关闭异常
                }
            }
        });
    }

    /**
     * 启动客户端
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void startClient() throws IOException, ExecutionException, InterruptedException {
        AsynchronousSocketChannel socketChannel = AsynchronousSocketChannel.open();
        Future<Void> connectFuture = socketChannel.connect(new InetSocketAddress("localhost", 8082));
        connectFuture.get(); // 等待连接完成
        System.out.println("AIO客户端已连接到服务器");

        // 发送消息
        String msg = "Hello AIO Server";
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        Future<Integer> writeFuture = socketChannel.write(buffer);
        writeFuture.get(); // 等待写入完成

        // 读取服务器响应
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        Future<Integer> readFuture = socketChannel.read(readBuffer);
        readFuture.get(); // 等待读取完成
        readBuffer.flip();
        byte[] bytes = new byte[readBuffer.remaining()];
        readBuffer.get(bytes);
        System.out.println("客户端收到: " + new String(bytes));

        // 发送bye消息
        String byeMsg = "bye";
        ByteBuffer byeBuffer = ByteBuffer.wrap(byeMsg.getBytes());
        Future<Integer> byeWriteFuture = socketChannel.write(byeBuffer);
        byeWriteFuture.get(); // 等待写入完成

        // 读取服务器响应
        ByteBuffer byeReadBuffer = ByteBuffer.allocate(1024);
        Future<Integer> byeReadFuture = socketChannel.read(byeReadBuffer);
        byeReadFuture.get(); // 等待读取完成
        byeReadBuffer.flip();
        byte[] byeBytes = new byte[byeReadBuffer.remaining()];
        byeReadBuffer.get(byeBytes);
        System.out.println("客户端收到: " + new String(byeBytes));

        socketChannel.close();
        System.out.println("客户端已关闭");
    }
}
# Java IO和NIO面试题

## 1. IO基础概念

### 题目1：什么是IO？Java IO的分类有哪些？

**答案：**
IO（Input/Output）是计算机系统中数据传输的过程。

**Java IO分类：**
1. **按数据流向分类**：
   - 输入流（Input Stream）：从数据源读取数据
   - 输出流（Output Stream）：向目标写入数据

2. **按处理数据单位分类**：
   - 字节流（Byte Stream）：以字节为单位，处理二进制数据
   - 字符流（Character Stream）：以字符为单位，处理文本数据

3. **按功能分类**：
   - 节点流：直接连接数据源的流
   - 处理流：对其他流进行包装的流

### 题目2：字节流和字符流的区别？

**答案：**
| 特性 | 字节流 | 字符流 |
|------|--------|--------|
| 处理单位 | 字节（8位） | 字符（16位） |
| 基类 | InputStream/OutputStream | Reader/Writer |
| 适用场景 | 二进制文件、图片、音频 | 文本文件 |
| 编码处理 | 不处理编码 | 自动处理编码转换 |

**代码示例见：** `IOBasicsExample.java`

## 2. BIO (Blocking IO)

### 题目3：什么是BIO？它的特点是什么？

**答案：**
BIO（Blocking IO）是传统的阻塞式IO模型。

**特点：**
- **同步阻塞**：线程在IO操作期间被阻塞
- **一对一模型**：一个连接对应一个线程
- **资源消耗大**：大量连接时需要大量线程
- **编程简单**：代码逻辑相对简单

### 题目4：BIO的性能瓶颈在哪里？

**答案：**
1. **线程开销大**：每个连接需要一个线程
2. **内存消耗**：线程栈占用内存（默认1MB）
3. **上下文切换**：线程切换开销
4. **连接限制**：系统线程数有限

**代码示例见：** `BIOExample.java`

## 3. NIO (Non-blocking IO)

### 题目5：什么是NIO？它解决了什么问题？

**答案：**
NIO（Non-blocking IO）是Java 1.4引入的新IO API。

**核心组件：**
- **Channel（通道）**：双向数据传输通道
- **Buffer（缓冲区）**：数据容器
- **Selector（选择器）**：多路复用器

**解决的问题：**
- 高并发连接处理
- 减少线程数量
- 提高系统吞吐量

### 题目6：NIO的核心概念详解？

**答案：**

**Channel（通道）：**
- FileChannel：文件IO
- SocketChannel：TCP客户端
- ServerSocketChannel：TCP服务端
- DatagramChannel：UDP

**Buffer（缓冲区）：**
- 容量（capacity）：缓冲区总大小
- 位置（position）：下一个读写位置
- 限制（limit）：有效数据的边界
- 标记（mark）：记住的位置

**Selector（选择器）：**
- 单线程管理多个Channel
- 事件驱动：OP_READ、OP_WRITE、OP_CONNECT、OP_ACCEPT

**代码示例见：** `NIOExample.java`

## 4. AIO (Asynchronous IO)

### 题目7：什么是AIO？它与NIO的区别？

**答案：**
AIO（Asynchronous IO）是Java 1.7引入的异步IO。

**AIO vs NIO：**
| 特性 | NIO | AIO |
|------|-----|-----|
| 同步性 | 同步非阻塞 | 异步非阻塞 |
| 编程模型 | 事件驱动 | 回调函数 |
| 适用场景 | 高并发连接 | 高并发且连接活跃 |
| 复杂度 | 中等 | 较高 |

### 题目8：AIO的实现原理？

**答案：**
- **异步调用**：IO操作立即返回，不阻塞线程
- **回调机制**：操作完成后调用CompletionHandler
- **系统支持**：依赖操作系统的异步IO机制
- **适用场景**：连接数多且连接活跃的场景

**代码示例见：** `AIOExample.java`

## 5. IO模型比较

### 题目9：BIO、NIO、AIO的对比？

**答案：**
| 特性 | BIO | NIO | AIO |
|------|-----|-----|-----|
| 同步性 | 同步阻塞 | 同步非阻塞 | 异步非阻塞 |
| 线程模型 | 1:1 | 1:N | 1:N |
| 复杂度 | 简单 | 复杂 | 复杂 |
| 吞吐量 | 低 | 高 | 高 |
| 适用场景 | 连接数少 | 连接数多 | 连接数多且活跃 |

### 题目10：如何选择IO模型？

**答案：**
- **BIO**：连接数少（<1000），逻辑简单
- **NIO**：连接数多（>1000），连接不都活跃
- **AIO**：连接数多且都很活跃

**代码示例见：** `IOModelComparisonExample.java`

## 6. 零拷贝

### 题目11：什么是零拷贝？Java中如何实现？

**答案：**
零拷贝是指数据传输过程中减少数据在用户空间和内核空间之间的拷贝次数。

**传统IO流程：**
1. 磁盘 → 内核缓冲区
2. 内核缓冲区 → 用户缓冲区
3. 用户缓冲区 → Socket缓冲区
4. Socket缓冲区 → 网卡

**零拷贝优化：**
1. **sendfile()**：直接从文件到网卡
2. **mmap()**：内存映射文件
3. **DirectByteBuffer**：直接内存

**Java实现：**
- `FileChannel.transferTo()`
- `FileChannel.transferFrom()`
- `MappedByteBuffer`

## 7. 序列化

### 题目12：Java序列化机制？

**答案：**
序列化是将对象转换为字节序列的过程，反序列化是逆过程。

**实现方式：**
1. **Serializable接口**：JDK原生序列化
2. **Externalizable接口**：自定义序列化
3. **第三方框架**：Kryo、Protobuf、Hessian

**序列化问题：**
- 性能较差
- 体积较大
- 安全性问题
- 版本兼容性

### 题目13：transient关键字的作用？

**答案：**
`transient`关键字用于标记不需要序列化的字段。

**特点：**
- 被transient修饰的字段不会被序列化
- 反序列化时被赋予默认值
- 常用于敏感信息或可重新计算的字段

## 8. 文件操作

### 题目14：Java 7中的NIO.2有什么改进？

**答案：**
NIO.2（JSR 203）引入了新的文件系统API：

**主要特性：**
- **Path接口**：更好的路径操作
- **Files类**：丰富的文件操作方法
- **文件监控**：WatchService
- **异步文件IO**：AsynchronousFileChannel

**代码示例见：** `NIO2Example.java`
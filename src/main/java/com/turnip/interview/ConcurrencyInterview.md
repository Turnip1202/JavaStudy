# Java多线程并发面试题

## 1. 线程基础

### 题目1：创建线程有哪几种方式？它们的区别是什么？

**答案：**
1. **继承Thread类**
   - 直接继承Thread类，重写run()方法
   - 单继承限制，不够灵活

2. **实现Runnable接口**
   - 实现Runnable接口，重写run()方法
   - 支持多继承，更灵活

3. **实现Callable接口**
   - 可以有返回值，可以抛出异常
   - 需要配合FutureTask使用

4. **线程池方式**
   - 通过Executor框架创建线程
   - 更好的资源管理和性能优化

**代码示例见：** `ThreadCreationExample.java`

### 题目2：线程的生命周期状态有哪些？

**答案：**
```
NEW (新建)
    ↓ start()
RUNNABLE (可运行)
    ↓ ↑
BLOCKED (阻塞) ←→ WAITING (等待) ←→ TIMED_WAITING (超时等待)
    ↓
TERMINATED (终止)
```

- **NEW**：线程被创建但未启动
- **RUNNABLE**：线程正在运行或等待CPU分配
- **BLOCKED**：线程被阻塞，等待监视器锁
- **WAITING**：线程等待其他线程的特定动作
- **TIMED_WAITING**：有时限的等待
- **TERMINATED**：线程执行完毕

## 2. 线程同步

### 题目3：synchronized关键字的作用和原理？

**答案：**
- **作用**：保证在同一时刻，只有一个线程可以执行某个方法或代码块
- **实现**：
  - 修饰方法：锁定当前对象或类
  - 修饰代码块：锁定指定对象
- **原理**：基于JVM的监视器锁（Monitor）实现
- **特点**：可重入、不可中断

### 题目4：volatile关键字的作用？

**答案：**
1. **保证可见性**：一个线程修改后，其他线程立即可见
2. **禁止指令重排序**：防止编译器和处理器优化导致的问题
3. **不保证原子性**：不能替代synchronized用于复合操作

**代码示例见：** `VolatileExample.java`

### 题目5：Lock接口与synchronized的区别？

**答案：**
| 特性 | synchronized | Lock |
|------|-------------|------|
| 实现方式 | JVM内置 | JDK提供 |
| 可中断性 | 不可中断 | 可中断 |
| 超时机制 | 无 | 有 |
| 公平性 | 非公平 | 可设置 |
| 条件变量 | 1个 | 多个 |
| 释放方式 | 自动 | 手动 |

**代码示例见：** `LockComparisonExample.java`

## 3. 线程通信

### 题目6：wait()、notify()、notifyAll()的使用场景？

**答案：**
- **wait()**：使当前线程等待，释放锁
- **notify()**：唤醒一个等待的线程
- **notifyAll()**：唤醒所有等待的线程
- **使用条件**：必须在synchronized块中使用

### 题目7：生产者消费者模式的实现？

**代码示例见：** `ProducerConsumerExample.java`

## 4. 线程池

### 题目8：线程池的核心参数有哪些？

**答案：**
- **corePoolSize**：核心线程数
- **maximumPoolSize**：最大线程数
- **keepAliveTime**：线程空闲时间
- **unit**：时间单位
- **workQueue**：任务队列
- **threadFactory**：线程工厂
- **handler**：拒绝策略

### 题目9：常见的线程池类型？

**答案：**
1. **FixedThreadPool**：固定大小线程池
2. **CachedThreadPool**：缓存线程池
3. **SingleThreadExecutor**：单线程池
4. **ScheduledThreadPool**：定时任务线程池

**代码示例见：** `ThreadPoolExample.java`

## 5. 并发工具类

### 题目10：CountDownLatch、CyclicBarrier、Semaphore的区别？

**答案：**
- **CountDownLatch**：计数器闭锁，一次性的
- **CyclicBarrier**：循环屏障，可重复使用
- **Semaphore**：信号量，控制访问资源的线程数量

**代码示例见：** `ConcurrencyUtilsExample.java`

## 6. 原子类

### 题目11：AtomicInteger的实现原理？

**答案：**
- 基于CAS（Compare And Swap）操作
- 无锁算法，避免了synchronized的开销
- 底层使用Unsafe类的native方法

### 题目12：CAS操作的优缺点？

**答案：**
**优点：**
- 无锁操作，性能好
- 避免线程阻塞

**缺点：**
- ABA问题
- 循环时间长开销大
- 只能保证一个共享变量的原子操作

**代码示例见：** `AtomicExample.java`

## 7. 死锁

### 题目13：什么是死锁？如何避免？

**答案：**
**死锁条件：**
1. 互斥条件
2. 请求和保持条件
3. 不剥夺条件
4. 环路等待条件

**避免方法：**
1. 避免嵌套锁
2. 避免无限期等待
3. 死锁检测

**代码示例见：** `DeadlockExample.java`

## 8. 并发集合

### 题目14：ConcurrentHashMap的实现原理？

**答案：**
- **JDK 1.7**：分段锁（Segment），减少锁粒度
- **JDK 1.8**：CAS + synchronized，性能更优

### 题目15：CopyOnWriteArrayList适用场景？

**答案：**
- **适用场景**：读多写少
- **实现原理**：写时复制，读操作无锁
- **缺点**：写操作成本高，数据一致性问题

**代码示例见：** `ConcurrentCollectionExample.java`
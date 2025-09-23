# Java面试题总览

欢迎使用Java面试题集合！这是一套全面的Java面试准备资料，包含了从基础概念到高级主题的各个方面。

## 📚 面试题分类

### 1. Java基础 (`JavaBasicsInterview.md`)
- **数据类型相关**：基本数据类型、包装类、自动装箱拆箱
- **面向对象编程**：封装、继承、多态、重载重写
- **异常处理**：异常体系、try-catch-finally执行顺序
- **关键字详解**：final、static、volatile等

**配套代码示例：**
- `BasicDataTypesExample.java` - 数据类型和自动装箱拆箱演示
- `StringComparisonExample.java` - String性能比较和线程安全测试
- `OOPExample.java` - 面向对象三大特性完整演示
- `OverloadOverrideExample.java` - 重载重写详细对比

### 2. 集合框架 (`CollectionFrameworkInterview.md`)
- **集合层次结构**：Collection和Map体系详解
- **List集合对比**：ArrayList vs LinkedList性能分析
- **Set集合特性**：HashSet、TreeSet、LinkedHashSet差异
- **Map实现原理**：HashMap底层机制、ConcurrentHashMap并发实现

**配套代码示例：**
- `ListComparisonExample.java` - List性能测试和线程安全对比
- `SetComparisonExample.java` - Set去重机制和自定义对象处理
- `MapComparisonExample.java` - Map性能测试和并发安全演示

### 3. 多线程并发 (`ConcurrencyInterview.md`)
- **线程基础**：创建方式、生命周期状态
- **同步机制**：synchronized、volatile、Lock接口
- **线程通信**：wait/notify、生产者消费者模式
- **线程池技术**：核心参数、常见类型、最佳实践
- **并发工具**：CountDownLatch、CyclicBarrier、Semaphore
- **原子操作**：AtomicInteger、CAS机制、ABA问题

**配套代码示例：**
- `ThreadCreationExample.java` - 四种线程创建方式演示
- `ProducerConsumerExample.java` - 三种生产者消费者实现
- `AtomicExample.java` - 原子类和CAS操作详解

### 4. IO和NIO (`IOAndNIOInterview.md`)
- **IO基础概念**：字节流vs字符流、节点流vs处理流
- **BIO模型**：传统阻塞IO特点和性能瓶颈
- **NIO核心**：Channel、Buffer、Selector三大组件
- **AIO异步**：回调机制和适用场景
- **零拷贝技术**：原理和Java实现方式

**配套代码示例：**
- `IOModelComparisonExample.java` - BIO/NIO/AIO性能对比测试

### 5. JVM和内存管理 (`JVMAndMemoryInterview.md`)
- **内存结构**：堆、方法区、栈、程序计数器详解
- **垃圾回收**：GC算法、收集器选择、性能调优
- **类加载机制**：双亲委派模型、类加载过程
- **JVM参数调优**：常用参数、GC日志分析
- **性能监控**：命令行工具、图形化工具使用

### 6. 设计模式 (`DesignPatternsInterview.md`)
- **创建型模式**：单例、工厂、建造者模式详解
- **结构型模式**：适配器、装饰器、代理模式对比
- **行为型模式**：观察者、策略、模板方法应用
- **设计原则**：SOLID原则和最佳实践
- **实际应用**：Spring框架中的设计模式运用

**配套代码示例：**
- `SingletonPatternExample.java` - 五种单例模式实现和线程安全测试

## 🎯 使用建议

### 面试准备策略
1. **基础巩固**：先掌握Java基础和集合框架
2. **深入理解**：重点学习多线程、JVM、IO等核心技术
3. **实践验证**：运行代码示例，观察实际效果
4. **举一反三**：基于示例代码进行扩展练习

### 学习路径推荐
```
Java基础 → 集合框架 → 多线程并发 → IO/NIO → JVM调优 → 设计模式
```

### 代码运行方法
所有示例代码都位于 `com.turnip.interview` 包下，可以直接运行main方法：

```bash
# 编译
javac -d . src/main/java/com/turnip/interview/*.java

# 运行示例
java com.turnip.interview.BasicDataTypesExample
java com.turnip.interview.ThreadCreationExample
java com.turnip.interview.SingletonPatternExample
```

## 📋 面试重点

### 高频考点
- **集合框架**：HashMap原理、ConcurrentHashMap实现
- **多线程**：synchronized vs Lock、线程池参数
- **JVM**：内存模型、GC算法、类加载机制
- **设计模式**：单例模式实现、Spring中的设计模式

### 实战经验题
- 如何排查线程死锁？
- 如何进行JVM性能调优？
- 如何处理高并发场景？
- 如何设计可扩展的系统架构？

## 🔧 扩展学习

### 进阶主题
- Spring框架原理
- 微服务架构
- 分布式系统
- 性能优化实战

### 推荐工具
- **开发工具**：IntelliJ IDEA、Eclipse
- **性能监控**：JVisualVM、JProfiler、Arthas
- **压力测试**：JMeter、Gatling

## 📞 问题反馈

如果在学习过程中遇到问题，可以：
1. 检查代码是否完整复制
2. 确认Java版本兼容性
3. 查看控制台错误信息
4. 参考官方文档

## 🎉 结语

这套面试题覆盖了Java开发的核心知识点，结合理论讲解和实际代码，帮助你全面准备Java技术面试。

**记住：理论与实践相结合，多思考多练习，才能真正掌握这些知识点！**

祝你面试顺利！🚀
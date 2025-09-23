# Java基础面试题集合

## 1. 数据类型相关

### 题目1：Java中基本数据类型有哪些？它们的默认值和取值范围是什么？

**答案：**
Java有8种基本数据类型：

| 类型 | 字节数 | 取值范围 | 默认值 |
|------|--------|----------|--------|
| byte | 1 | -128 ~ 127 | 0 |
| short | 2 | -32,768 ~ 32,767 | 0 |
| int | 4 | -2,147,483,648 ~ 2,147,483,647 | 0 |
| long | 8 | -9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807 | 0L |
| float | 4 | ±1.4E-45 ~ ±3.4028235E38 | 0.0f |
| double | 8 | ±4.9E-324 ~ ±1.7976931348623157E308 | 0.0d |
| char | 2 | 0 ~ 65,535（Unicode字符） | '\u0000' |
| boolean | 1 | true/false | false |

### 题目2：int和Integer的区别？什么是自动装箱和拆箱？

**答案：**
- `int`是基本数据类型，直接存储在栈中
- `Integer`是包装类，是对象，存储在堆中
- 自动装箱：基本类型自动转换为包装类型
- 自动拆箱：包装类型自动转换为基本类型

**代码示例见：** `BasicDataTypesExample.java`

### 题目3：String、StringBuilder、StringBuffer的区别？

**答案：**
- **String**：不可变，线程安全，适合少量字符串操作
- **StringBuilder**：可变，线程不安全，适合单线程大量字符串操作
- **StringBuffer**：可变，线程安全，适合多线程大量字符串操作

**代码示例见：** `StringComparisonExample.java`

## 2. 面向对象编程

### 题目4：面向对象的三大特性是什么？请分别解释。

**答案：**
1. **封装（Encapsulation）**：将数据和操作数据的方法组合在一起，隐藏对象的内部实现细节
2. **继承（Inheritance）**：子类可以继承父类的属性和方法，实现代码复用
3. **多态（Polymorphism）**：同一个接口或父类引用可以指向不同的实现类对象

**代码示例见：** `OOPExample.java`

### 题目5：重载（Overload）和重写（Override）的区别？

**答案：**
- **重载**：同一个类中，方法名相同但参数列表不同
- **重写**：子类重新实现父类的方法，方法签名必须相同

**代码示例见：** `OverloadOverrideExample.java`

### 题目6：抽象类和接口的区别？

**答案：**
| 特性 | 抽象类 | 接口 |
|------|--------|------|
| 关键字 | abstract class | interface |
| 继承 | 单继承 | 多实现 |
| 构造方法 | 可以有 | 不能有 |
| 成员变量 | 任意 | public static final |
| 成员方法 | 任意 | public abstract（默认方法除外） |
| 访问修饰符 | 任意 | public |

**代码示例见：** `AbstractInterfaceExample.java`

## 3. 异常处理

### 题目7：Java异常体系结构是怎样的？

**答案：**
```
Throwable
├── Error（错误，程序无法处理）
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── ...
└── Exception（异常，程序可以处理）
    ├── RuntimeException（运行时异常，非检查异常）
    │   ├── NullPointerException
    │   ├── IndexOutOfBoundsException
    │   └── ...
    └── 检查异常
        ├── IOException
        ├── SQLException
        └── ...
```

### 题目8：try-catch-finally的执行顺序？finally块一定会执行吗？

**答案：**
- 正常情况：try → finally
- 异常情况：try → catch → finally
- finally几乎总是执行，除非：
  1. System.exit()
  2. JVM崩溃
  3. 线程被中断

**代码示例见：** `ExceptionHandlingExample.java`

## 4. 关键字

### 题目9：final、finally、finalize的区别？

**答案：**
- **final**：修饰符，表示最终的、不可变的
- **finally**：异常处理语句块，无论是否异常都会执行
- **finalize**：Object类的方法，垃圾回收前调用

### 题目10：static关键字的作用？

**答案：**
- 修饰变量：类变量，所有实例共享
- 修饰方法：类方法，不需要实例化即可调用
- 修饰代码块：静态代码块，类加载时执行
- 修饰类：静态内部类

**代码示例见：** `StaticExample.java`
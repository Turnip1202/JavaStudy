# Java集合框架面试题

## 1. 集合框架概述

### 题目1：Java集合框架的层次结构是怎样的？

**答案：**
```
Collection (接口)
├── List (接口) - 有序集合，允许重复
│   ├── ArrayList - 动态数组，线程不安全
│   ├── LinkedList - 双向链表，线程不安全
│   └── Vector - 动态数组，线程安全（已过时）
│       └── Stack - 栈，继承Vector
├── Set (接口) - 无序集合，不允许重复
│   ├── HashSet - 哈希表实现
│   ├── LinkedHashSet - 有序的HashSet
│   └── TreeSet - 红黑树实现，有序
└── Queue (接口) - 队列
    ├── ArrayDeque - 数组双端队列
    ├── LinkedList - 也实现了Queue接口
    └── PriorityQueue - 优先队列

Map (接口) - 键值对集合
├── HashMap - 哈希表，线程不安全
├── LinkedHashMap - 有序的HashMap
├── TreeMap - 红黑树，有序
├── Hashtable - 哈希表，线程安全（已过时）
└── ConcurrentHashMap - 线程安全的HashMap
```

### 题目2：ArrayList和LinkedList的区别？

**答案：**
| 特性 | ArrayList | LinkedList |
|------|-----------|------------|
| 底层实现 | 动态数组 | 双向链表 |
| 随机访问 | O(1) | O(n) |
| 插入删除（头部） | O(n) | O(1) |
| 插入删除（尾部） | O(1) | O(1) |
| 插入删除（中间） | O(n) | O(1) |
| 内存占用 | 较少 | 较多（额外指针） |
| 缓存友好性 | 好 | 差 |

**代码示例见：** `ListComparisonExample.java`

## 2. List集合

### 题目3：ArrayList的扩容机制是什么？

**答案：**
1. 初始容量为10（无参构造器）
2. 当容量不足时，扩容为原来的1.5倍
3. 使用`Arrays.copyOf()`方法复制数组
4. 扩容是昂贵操作，建议预估容量

### 题目4：如何实现ArrayList的线程安全？

**答案：**
1. `Collections.synchronizedList(list)`
2. `Vector`（不推荐）
3. `CopyOnWriteArrayList`
4. 使用锁机制

**代码示例见：** `ThreadSafeListExample.java`

## 3. Set集合

### 题目5：HashSet如何保证元素唯一性？

**答案：**
1. 基于HashMap实现，元素作为key存储
2. 通过`hashCode()`和`equals()`方法判断重复
3. 先比较hashCode，如果相同再比较equals
4. 重写equals必须重写hashCode

### 题目6：HashSet、LinkedHashSet、TreeSet的区别？

**答案：**
| 特性 | HashSet | LinkedHashSet | TreeSet |
|------|---------|---------------|---------|
| 底层实现 | HashMap | LinkedHashMap | 红黑树 |
| 有序性 | 无序 | 插入顺序 | 自然顺序/比较器 |
| 时间复杂度 | O(1) | O(1) | O(log n) |
| 元素要求 | hashCode/equals | hashCode/equals | Comparable/Comparator |

**代码示例见：** `SetComparisonExample.java`

## 4. Map集合

### 题目7：HashMap的底层实现原理？

**答案：**
1. **JDK 1.7及之前**：数组 + 链表
2. **JDK 1.8及之后**：数组 + 链表 + 红黑树
3. **扩容条件**：元素个数 > 容量 × 负载因子（0.75）
4. **树化条件**：链表长度 ≥ 8 且数组长度 ≥ 64
5. **退化条件**：红黑树节点 ≤ 6 时退化为链表

### 题目8：HashMap在JDK 1.8中的优化？

**答案：**
1. **数据结构优化**：引入红黑树，解决链表过长问题
2. **扩容优化**：优化了rehash过程，避免死循环
3. **计算优化**：优化了hash函数，减少碰撞

### 题目9：ConcurrentHashMap的实现原理？

**答案：**
- **JDK 1.7**：分段锁（Segment）
- **JDK 1.8**：CAS + synchronized

**代码示例见：** `MapComparisonExample.java`

## 5. Queue和Deque

### 题目10：Queue的常用实现类及其特点？

**答案：**
- **ArrayDeque**：基于数组的双端队列，性能好
- **LinkedList**：基于链表，实现List和Deque接口
- **PriorityQueue**：优先队列，基于堆实现

**代码示例见：** `QueueExample.java`

## 6. 集合工具类

### 题目11：Collections工具类的常用方法？

**答案：**
- `sort()`：排序
- `reverse()`：反转
- `shuffle()`：随机打乱
- `binarySearch()`：二分查找
- `max()/min()`：最大/最小值
- `synchronizedXxx()`：同步包装器
- `unmodifiableXxx()`：不可修改包装器

**代码示例见：** `CollectionsUtilExample.java`

## 7. 性能优化

### 题目12：如何选择合适的集合类？

**答案：**
1. **需要索引访问**：ArrayList
2. **频繁插入删除**：LinkedList
3. **无重复元素**：HashSet
4. **有序集合**：TreeSet/TreeMap
5. **线程安全**：ConcurrentHashMap、CopyOnWriteArrayList
6. **大量查找**：HashMap

### 题目13：集合性能优化建议？

**答案：**
1. **预估容量**：避免频繁扩容
2. **选择合适数据结构**：根据使用场景
3. **重写equals和hashCode**：提高HashMap性能
4. **使用EnumSet/EnumMap**：枚举类型集合
5. **考虑使用原始类型集合**：Trove、Eclipse Collections
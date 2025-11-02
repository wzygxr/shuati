# 堆算法全面总结

## 一、堆的基本概念与特性

### 1.1 堆的定义
堆是一种特殊的完全二叉树数据结构，满足堆属性：
- **最大堆**：任意节点的值 >= 其子节点的值（根节点最大）
- **最小堆**：任意节点的值 <= 其子节点的值（根节点最小）

### 1.2 堆的核心操作时间复杂度
| 操作 | 时间复杂度 | 说明 |
|------|------------|------|
| 插入元素 | O(log n) | 上浮操作 |
| 删除最值 | O(log n) | 下沉操作 |
| 获取最值 | O(1) | 直接访问根节点 |
| 建堆 | O(n) | Floyd建堆算法 |

### 1.3 堆的存储结构
堆通常使用数组存储，索引关系：
- 父节点索引：`(i-1)/2`
- 左子节点索引：`2*i + 1`
- 右子节点索引：`2*i + 2`

## 二、堆的应用场景分类

### 2.1 Top K 问题
**特征**：找最大/最小的K个元素
**典型题目**：
- LeetCode 215: 数组中的第K个最大元素
- LeetCode 347: 前K个高频元素
- LeetCode 973: 最接近原点的K个点

**解题模板**：
```java
// 找最大K个元素：使用大小为K的最小堆
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
for (int num : nums) {
    if (minHeap.size() < k) {
        minHeap.offer(num);
    } else if (num > minHeap.peek()) {
        minHeap.poll();
        minHeap.offer(num);
    }
}
return minHeap.peek();
```

### 2.2 数据流处理
**特征**：动态维护最值或中位数
**典型题目**：
- LeetCode 295: 数据流的中位数
- LeetCode 703: 数据流的第K大元素
- HackerRank: 查找运行中位数

**解题模板**（中位数问题）：
```java
// 双堆结构：最大堆存储较小一半，最小堆存储较大一半
PriorityQueue<Integer> maxHeap; // 较小一半
PriorityQueue<Integer> minHeap; // 较大一半

public void addNum(int num) {
    if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
        maxHeap.offer(num);
    } else {
        minHeap.offer(num);
    }
    
    // 平衡堆大小
    if (maxHeap.size() > minHeap.size() + 1) {
        minHeap.offer(maxHeap.poll());
    } else if (minHeap.size() > maxHeap.size()) {
        maxHeap.offer(minHeap.poll());
    }
}
```

### 2.3 调度与优先级问题
**特征**：按优先级处理任务
**典型题目**：
- LeetCode 621: 任务调度器
- LeetCode 630: 课程表 III
- LeetCode 502: IPO

**解题模板**：
```java
// 按截止时间排序，使用堆动态调整
Arrays.sort(tasks, (a, b) -> a.deadline - b.deadline);
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);

for (Task task : tasks) {
    currentTime += task.duration;
    maxHeap.offer(task.duration);
    
    if (currentTime > task.deadline) {
        currentTime -= maxHeap.poll(); // 移除最耗时的任务
    }
}
```

### 2.4 合并多个有序序列
**特征**：合并K个有序数组/链表
**典型题目**：
- LeetCode 23: 合并K个排序链表
- LeetCode 378: 有序矩阵中第K小的元素
- 洛谷 P1090: 合并果子

**解题模板**：
```java
// 合并K个有序链表
PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);

// 将所有链表的头节点加入堆
for (ListNode node : lists) {
    if (node != null) minHeap.offer(node);
}

ListNode dummy = new ListNode(0);
ListNode current = dummy;

while (!minHeap.isEmpty()) {
    ListNode node = minHeap.poll();
    current.next = node;
    current = current.next;
    
    if (node.next != null) {
        minHeap.offer(node.next);
    }
}
```

## 三、堆算法技巧与优化

### 3.1 堆类型选择策略
| 需求 | 堆类型 | 说明 |
|------|--------|------|
| 找最大K个元素 | 最小堆（大小为K） | 堆顶是K个元素中的最小值 |
| 找最小K个元素 | 最大堆（大小为K） | 堆顶是K个元素中的最大值 |
| 实时获取最大值 | 最大堆 | 堆顶始终是当前最大值 |
| 实时获取最小值 | 最小堆 | 堆顶始终是当前最小值 |
| 数据流中位数 | 双堆结构 | 平衡大小，中位数在堆顶 |

### 3.2 时间复杂度优化技巧
1. **控制堆大小**：对于Top K问题，维护大小为K的堆
2. **避免重复计算**：缓存中间结果，如频率统计
3. **合理选择算法**：在数据量小时，简单排序可能更快
4. **批量操作**：减少堆操作的次数

### 3.3 空间复杂度优化
1. **原地操作**：尽可能使用原地算法
2. **对象复用**：避免创建不必要的对象
3. **流式处理**：对于大数据集，使用流式处理

## 四、各语言堆实现对比

### 4.1 Java
```java
// 最小堆（默认）
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// 最大堆
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);

// 自定义比较器
PriorityQueue<Point> heap = new PriorityQueue<>(
    (a, b) -> a.distance() - b.distance()
);
```

### 4.2 Python
```python
import heapq

# 最小堆（默认）
min_heap = []
heapq.heappush(min_heap, value)

# 最大堆（使用负数）
max_heap = []
heapq.heappush(max_heap, -value)
max_value = -heapq.heappop(max_heap)
```

### 4.3 C++
```cpp
#include <queue>

// 最小堆
priority_queue<int, vector<int>, greater<int>> min_heap;

// 最大堆（默认）
priority_queue<int> max_heap;

// 自定义比较器
struct Compare {
    bool operator()(const Point& a, const Point& b) {
        return a.distance > b.distance;
    }
};
priority_queue<Point, vector<Point>, Compare> heap;
```

## 五、常见错误与调试技巧

### 5.1 常见错误
1. **堆大小控制错误**：忘记维护固定大小的堆
2. **比较器逻辑错误**：最大堆和最小堆混淆
3. **空堆访问**：在空堆上调用peek()或poll()
4. **并发访问**：多线程环境下的同步问题

### 5.2 调试技巧
1. **打印堆状态**：
```java
System.out.println("Heap: " + heap);
```

2. **验证中间结果**：
```java
assert heap.size() == k : "堆大小应为" + k;
```

3. **性能分析**：
```java
long startTime = System.currentTimeMillis();
// 算法执行
long endTime = System.currentTimeMillis();
System.out.println("执行时间: " + (endTime - startTime) + "ms");
```

## 六、面试考点总结

### 6.1 基础考点
1. 堆的基本概念和性质
2. 堆操作的时间复杂度分析
3. 堆的构建过程
4. 堆排序算法

### 6.2 应用考点
1. Top K问题的多种解法对比
2. 数据流中位数的维护
3. 调度问题的贪心策略
4. 多路归并的实现

### 6.3 进阶考点
1. 堆的工程化实现
2. 堆在系统设计中的应用
3. 堆与其他数据结构的结合使用
4. 堆的并发安全实现

## 七、题目索引表

| 序号 | 题目名称 | 平台 | 难度 | 关键技巧 |
|------|----------|------|------|----------|
| 1 | 数组中的第K个最大元素 | LeetCode 215 | 中等 | Top K问题模板 |
| 2 | 前K个高频元素 | LeetCode 347 | 中等 | 频率统计+堆 |
| 3 | 数据流的中位数 | LeetCode 295 | 困难 | 双堆结构 |
| 4 | 合并K个排序链表 | LeetCode 23 | 困难 | 多路归并 |
| 5 | 滑动窗口最大值 | LeetCode 239 | 困难 | 单调队列 |
| 6 | 任务调度器 | LeetCode 621 | 中等 | 频率调度 |
| 7 | 有序矩阵中第K小的元素 | LeetCode 378 | 中等 | 多指针+堆 |
| 8 | IPO | LeetCode 502 | 困难 | 贪心+堆 |
| 9 | 课程表 III | LeetCode 630 | 困难 | 截止时间调度 |
| 10 | 雇佣K名工人的最低成本 | LeetCode 857 | 困难 | 比率排序+堆 |

## 八、学习路径建议

### 8.1 初学者路径
1. 掌握堆的基本概念和操作
2. 练习Top K问题的经典题目
3. 理解数据流处理的基本模式
4. 完成基础题目的多种实现

### 8.2 进阶者路径
1. 深入理解堆的底层实现
2. 掌握复杂调度问题的解法
3. 学习堆在系统设计中的应用
4. 研究堆的并发安全实现

### 8.3 高手路径
1. 参与开源项目中堆相关的实现
2. 研究堆在分布式系统中的应用
3. 探索堆与机器学习算法的结合
4. 贡献堆算法的新应用场景

通过系统学习本总结文档，您将能够全面掌握堆算法的核心知识，并在实际应用中游刃有余。
# Class035: 高级数据结构设计与实现

本目录专注于实现各种高级数据结构，这些数据结构在算法面试和实际工程中都有广泛应用。所有实现都满足O(1)或近似O(1)的时间复杂度要求。

## 目录内容

1. [SetAll功能的哈希表](#1-setall功能的哈希表)
2. [LRU缓存](#2-lru缓存)
3. [O(1)时间插入、删除和获取随机元素](#3-o1时间插入删除和获取随机元素)
4. [允许重复元素的O(1)数据结构](#4-允许重复元素的o1数据结构)
5. [数据流的中位数](#5-数据流的中位数)
6. [最大频率栈](#6-最大频率栈)
7. [全O(1)的数据结构](#7-全o1的数据结构)

---

## 1. SetAll功能的哈希表

### 题目描述
实现一个支持setAll功能的哈希表，支持以下操作：
1. put(k, v): 插入或更新键值对
2. get(k): 获取键对应的值
3. setAll(v): 将所有键的值都设置为v

要求所有操作的时间复杂度都是O(1)

### 算法思路
使用时间戳技术实现setAll功能：
1. 为每个键值对记录插入/更新的时间戳
2. 为setAll操作记录时间戳
3. get操作时比较键值对的时间戳和setAll时间戳，返回较新的值

### 相关题目
- 牛客网: [设计有setAll功能的哈希表](https://www.nowcoder.com/practice/7c4559f138e74ceb9ba57d76fd169967)
- 类似设计题目在各大OJ平台都有出现

### 时间复杂度分析
- put操作: O(1) - 哈希表插入/更新
- get操作: O(1) - 哈希表查找 + 时间戳比较
- setAll操作: O(1) - 更新全局变量

### 空间复杂度分析
O(n) - n为键值对的个数，需要哈希表存储所有键值对及相关信息

### 工程化考量
1. 异常处理: 处理非法输入
2. 边界场景: 空哈希表、大量数据等
3. 时间戳溢出: 在实际应用中需要注意时间戳溢出问题

### 代码实现
- [Java版本](Code01_SetAllHashMap.java)
- [C++版本](Code01_SetAllHashMap.cpp)
- [Python版本](Code01_SetAllHashMap.py)

---

## 2. LRU缓存

### 题目描述
LRU (Least Recently Used) 最近最少使用缓存机制是一种常用的页面置换算法。当缓存满时，会优先淘汰最长时间未被访问的数据。

### 算法思路
1. 使用双向链表维护访问顺序，最近访问的节点放在头部，最久未访问的节点在尾部
2. 使用哈希表实现O(1)时间复杂度的查找操作
3. 当访问一个节点时，将其移动到链表头部
4. 当插入新节点且缓存满时，删除链表尾部节点

### 相关题目
- LeetCode 146. [LRU Cache](https://leetcode.com/problems/lru-cache/)
- 剑指Offer II 031. [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/)
- 牛客网: [设计LRU缓存结构](https://www.nowcoder.com/practice/5dfded165916435d9defb053c63f1e84)
- LeetCode 460. [LFU Cache](https://leetcode.com/problems/lfu-cache/) (最近最不经常使用)

### 时间复杂度分析
- get操作: O(1) - 哈希表查找 + 链表节点移动
- put操作: O(1) - 哈希表插入/更新 + 链表节点插入/删除

### 空间复杂度分析
O(capacity) - 哈希表和双向链表最多存储capacity个节点

### 工程化考量
1. 异常处理: 检查非法输入如capacity<=0
2. 线程安全: 当前实现非线程安全，如需线程安全可使用ReentrantReadWriteLock
3. 内存管理: 节点复用、及时清理无用对象避免内存泄漏
4. 可配置性: 支持自定义容量
5. 单元测试: 需要覆盖各种边界情况和操作组合

### 代码实现
- [Java版本](Code02_LRU.java)
- [C++版本](Code02_LRU.cpp)
- [Python版本](Code02_LRU.py)

---

## 3. O(1)时间插入、删除和获取随机元素

### 题目描述
设计一个支持在平均时间复杂度O(1)下执行以下操作的数据结构：
1. insert(val): 当元素val不存在时返回true，并向集合中插入该项，否则返回false
2. remove(val): 元素val存在时，从集合中移除该项，返回true，否则返回false
3. getRandom: 随机返回现有集合中的一项，每个元素应该有相同的概率被返回

### 算法思路
1. 使用数组(ArrayList)存储元素，实现O(1)时间复杂度的随机访问
2. 使用哈希表(HashMap)存储元素值到其在数组中索引的映射，实现O(1)时间复杂度的查找
3. 插入操作：直接在数组末尾添加元素，并在哈希表中记录其索引
4. 删除操作：将要删除的元素与数组末尾元素交换，然后删除末尾元素，更新哈希表
5. 随机获取：使用Random类随机生成索引，访问数组中对应元素

### 相关题目
- LeetCode 380. [常数时间插入、删除和获取随机元素](https://leetcode.com/problems/insert-delete-getrandom-o1/)
- 牛客网: [O(1)时间插入、删除和获取随机元素](https://www.nowcoder.com/discuss/353149939293298688)
- 剑指Offer专项突破版: 数据结构设计相关题目

### 时间复杂度分析
- insert操作: O(1) - 数组末尾插入 + 哈希表插入
- remove操作: O(1) - 哈希表查找 + 数组元素交换 + 数组末尾删除 + 哈希表更新
- getRandom操作: O(1) - 随机索引生成 + 数组访问

### 空间复杂度分析
O(n) - n为集合中元素个数，需要数组和哈希表分别存储元素和索引映射

### 工程化考量
1. 异常处理: 处理空集合的getRandom操作
2. 边界场景: 空集合、单元素集合等
3. 随机性: 确保getRandom方法能真正等概率返回每个元素
4. 内存管理: 及时清理无用对象避免内存泄漏

### 代码实现
- [Java版本](Code03_InsertDeleteRandom.java)
- [C++版本](Code03_InsertDeleteRandom.cpp)
- [Python版本](Code03_InsertDeleteRandom.py)

---

## 4. 允许重复元素的O(1)数据结构

### 题目描述
设计一个支持在平均时间复杂度O(1)下执行以下操作的数据结构（允许重复元素）：
1. insert(val): 将一个元素val插入到集合中，返回true
2. remove(val): 如果元素val存在，则从中删除一个实例，返回true，否则返回false
3. getRandom: 随机返回集合中的一个元素，每个元素被返回的概率与其在集合中的数量成线性关系

### 算法思路
与不允许重复的版本相比，主要变化在于需要处理重复元素：
1. 使用数组(ArrayList)存储所有元素，实现O(1)时间复杂度的随机访问
2. 使用哈希表(HashMap)存储元素值到其在数组中索引集合的映射
3. 插入操作：在数组末尾添加元素，并在哈希表中记录其索引
4. 删除操作：将要删除的元素与数组末尾元素交换，然后删除末尾元素，更新哈希表
5. 随机获取：使用Random类随机生成索引，访问数组中对应元素

### 相关题目
- LeetCode 381. [常数时间插入、删除和获取随机元素-允许重复](https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/)
- LeetCode 380. [常数时间插入、删除和获取随机元素](https://leetcode.com/problems/insert-delete-getrandom-o1/)

### 时间复杂度分析
- insert操作: O(1) - 数组末尾插入 + 哈希表更新
- remove操作: O(1) - 哈希表查找 + 数组元素交换 + 数组末尾删除 + 哈希表更新
- getRandom操作: O(1) - 随机索引生成 + 数组访问

### 空间复杂度分析
O(n) - n为集合中元素个数，需要数组和哈希表分别存储元素和索引映射

### 工程化考量
1. 异常处理: 处理空集合的getRandom操作
2. 边界场景: 空集合、单元素集合等
3. 随机性: 确保getRandom方法能真正按概率返回每个元素
4. 内存管理: 及时清理无用对象避免内存泄漏

### 代码实现
- [Java版本](Code04_InsertDeleteRandomDuplicatesAllowed.java)
- [C++版本](Code04_InsertDeleteRandomDuplicatesAllowed.cpp)
- [Python版本](Code04_InsertDeleteRandomDuplicatesAllowed.py)

---

## 5. 数据流的中位数

### 题目描述
设计一个支持以下两种操作的数据结构：
1. void addNum(int num) - 从数据流中添加一个整数到数据结构中
2. double findMedian() - 返回目前所有元素的中位数

中位数是有序列表中间的数。如果列表长度是偶数，中位数则是中间两个数的平均值。

### 算法思路
使用两个优先队列（堆）来维护数据：
1. maxHeap（最大堆）：存储较小的一半元素
2. minHeap（最小堆）：存储较大的一半元素

保持两个堆的大小平衡：
1. 当元素总数为偶数时，两个堆大小相等
2. 当元素总数为奇数时，maxHeap比minHeap多一个元素

### 相关题目
- LeetCode 295. [数据流的中位数](https://leetcode.com/problems/find-median-from-data-stream/)
- 剑指Offer 41. [数据流中的中位数](https://leetcode.cn/problems/shu-ju-liu-zhong-de-zhong-wei-shu-lcof/)
- LeetCode 480. [滑动窗口中位数](https://leetcode.com/problems/sliding-window-median/)

### 时间复杂度分析
- addNum操作: O(log n) - 堆的插入和调整操作
- findMedian操作: O(1) - 直接访问堆顶元素

### 空间复杂度分析
O(n) - n为添加的元素个数，需要两个堆分别存储元素

### 工程化考量
1. 异常处理: 处理空数据流的findMedian操作
2. 边界场景: 空数据流、单元素数据流等
3. 数值精度: 注意整数除法的精度问题
4. 内存管理: 及时清理无用对象避免内存泄漏

### 代码实现
- [Java版本](Code05_MedianFinder.java)
- [C++版本](Code05_MedianFinder.cpp)
- [Python版本](Code05_MedianFinder.py)

---

## 6. 最大频率栈

### 题目描述
实现一个类似栈的数据结构，支持以下操作：
1. push(val): 将一个整数val压入栈顶
2. pop(): 删除并返回栈中出现频率最高的元素
   如果出现频率最高的元素不只一个，则移除并返回最接近栈顶的元素

### 算法思路
使用两个哈希表来维护数据：
1. valueTimes: 记录每个值的出现频率
2. cntValues: 记录每个频率对应的值列表（使用ArrayList实现）
3. topTimes: 记录当前最大频率

push操作：
1. 更新值的频率
2. 将值添加到对应频率的列表中
3. 更新最大频率

pop操作：
1. 从最大频率对应的列表中移除最后一个元素
2. 更新该元素的频率
3. 如果最大频率列表为空，则减少最大频率

### 相关题目
- LeetCode 895. [最大频率栈](https://leetcode.com/problems/maximum-frequency-stack/)
- 牛客网: [最大频率栈](https://www.nowcoder.com/discuss/791601453080055808)

### 时间复杂度分析
- push操作: O(1) - 哈希表操作和列表操作都是O(1)
- pop操作: O(1) - 哈希表操作和列表操作都是O(1)

### 空间复杂度分析
O(n) - n为push操作的次数，需要存储所有元素及其频率信息

### 工程化考量
1. 异常处理: 处理空栈的pop操作
2. 边界场景: 空栈、单元素栈等
3. 内存管理: 及时清理无用对象避免内存泄漏

### 代码实现
- [Java版本](Code06_MaximumFrequencyStack.java)
- [C++版本](Code06_MaximumFrequencyStack.cpp)
- [Python版本](Code06_MaximumFrequencyStack.py)

---

## 7. 全O(1)的数据结构

### 题目描述
设计一个数据结构支持以下操作，所有操作的时间复杂度都为O(1)：
1. inc(key): 将key的计数增加1，如果key不存在则插入计数为1的key
2. dec(key): 将key的计数减少1，如果计数变为0则删除key
3. getMaxKey(): 返回计数最大的任意一个key，如果不存在返回空字符串
4. getMinKey(): 返回计数最小的任意一个key，如果不存在返回空字符串

### 算法思路
使用双向链表+哈希表的组合数据结构：
1. 双向链表节点存储计数值和拥有该计数值的所有key集合
2. 哈希表存储key到链表节点的映射
3. 维护头尾哨兵节点简化边界处理
4. 保证链表按计数值单调递增排列

### 相关题目
- LeetCode 432. [全O(1)的数据结构](https://leetcode.com/problems/all-oone-data-structure/)
- LeetCode 146. [LRU缓存](https://leetcode.com/problems/lru-cache/)

### 时间复杂度分析
所有操作: O(1) - 哈希表操作和链表节点操作都是O(1)

### 空间复杂度分析
O(n) - n为不同key的个数，需要链表节点和哈希表存储相关信息

### 工程化考量
1. 异常处理: 处理空数据结构的getMaxKey和getMinKey操作
2. 边界场景: 空数据结构、单元素数据结构等
3. 内存管理: 及时清理无用对象避免内存泄漏

### 代码实现
- [Java版本](Code07_AllO1.java)
- [C++版本](Code07_AllO1.cpp)
- [Python版本](Code07_AllO1.py)

---

## 总结

本章涵盖了多种高级数据结构的设计与实现，这些数据结构在实际工程中有着广泛的应用：

1. **SetAll HashMap**: 通过时间戳技术实现批量更新操作
2. **LRU Cache**: 使用双向链表和哈希表实现缓存淘汰策略
3. **Randomized Set**: 结合数组和哈希表实现随机访问
4. **Median Finder**: 使用双堆结构维护数据流的中位数
5. **Frequency Stack**: 通过频率分组实现最大频率元素的快速访问
6. **All O(1) Data Structure**: 使用双向链表和哈希表实现所有操作O(1)时间复杂度

### 设计技巧总结

1. **时间戳技术**: 用于处理批量更新操作，避免实际更新所有元素
2. **双数据结构组合**: 常见的有数组+哈希表、堆+哈希表、链表+哈希表等组合
3. **哨兵节点**: 简化链表操作的边界处理
4. **频率分组**: 将相同频率的元素组织在一起，便于快速访问
5. **双指针/双堆**: 维护数据的有序性或特定属性

### 工程化考虑

1. **异常处理**: 合理处理边界情况和非法输入
2. **内存管理**: 及时清理无用对象，避免内存泄漏
3. **线程安全**: 在多线程环境下考虑同步机制
4. **可配置性**: 支持自定义参数，提高复用性
5. **性能优化**: 关注常数项优化和缓存友好性

### 面试要点

1. **理解设计思想**: 不仅要会写代码，还要理解为什么要这样设计
2. **复杂度分析**: 准确分析时间和空间复杂度
3. **边界处理**: 考虑各种边界情况
4. **扩展性思考**: 思考如何扩展功能或优化性能
# FHQ-Treap (无旋Treap) 实现汇总

本目录包含了FHQ-Treap（无旋Treap）数据结构的多种实现及其在经典算法问题中的应用。FHQ-Treap是一种高效的平衡树数据结构，通过分裂（split）和合并（merge）操作实现各种复杂的数据结构操作。

## 已实现的问题

### 1. 基础FHQ-Treap实现
- **Code01_FHQTreapWithCount1.py** - Python实现，包含计数压缩的FHQ-Treap
- **Code01_FHQTreapWithCount1.cpp** - C++实现，使用节点池优化内存管理

### 2. LeetCode 2336 - 无限集中的最小数字
- **LeetCode2336_SmallestNumberInInfiniteSet.java** - Java实现
- **LeetCode2336_SmallestNumberInInfiniteSet.py** - Python实现
- **LeetCode2336_SmallestNumberInInfiniteSet.cpp** - C++实现

#### 问题描述
设计一个数据结构，维护一个包含所有正整数的无限集，并支持以下操作：
- `popSmallest()`: 弹出并返回集合中最小的整数
- `addBack(num)`: 将一个之前弹出的正整数num添加回集合中

#### 解题思路
使用FHQ-Treap维护已删除的元素集合，同时使用current_min变量优化最小值查询，实现O(log k)的操作复杂度。

### 3. LeetCode 1845 - 座位预约管理系统
- **LeetCode1845_SeatReservationManager.java** - Java实现
- **LeetCode1845_SeatReservationManager.py** - Python实现
- **LeetCode1845_SeatReservationManager.cpp** - C++实现

#### 问题描述
设计一个系统来管理电影院座位的预约，支持以下操作：
- `reserve()`: 预约一个最小编号的可用座位
- `unreserve(seatNumber)`: 取消预约指定的座位

#### 解题思路
使用FHQ-Treap维护被取消预约的座位集合，优先分配最小的可用座位，实现高效的座位管理。

### 4. 洛谷 P3391 - 文艺平衡树
- **LuoguP3391_ArtisticBalancedTree.java** - Java实现
- **LuoguP3391_ArtisticBalancedTree.py** - Python实现
- **LuoguP3391_ArtisticBalancedTree.cpp** - C++实现

#### 问题描述
实现一个支持区间反转操作的平衡树，最终输出反转后的序列。

#### 解题思路
使用FHQ-Treap结合懒标记（lazy propagation）实现高效的区间反转操作，通过按大小分裂和合并操作维护区间。

## FHQ-Treap核心操作

### 1. 分裂（Split）
- **按值分裂**：将树分为两部分，左边所有节点的值小于等于key，右边所有节点的值大于key
- **按大小分裂**：将树分为两部分，左边包含k个节点，右边包含剩余节点

### 2. 合并（Merge）
将两棵满足条件的树合并为一棵，要求左树的最大值小于右树的最小值。

### 3. 懒标记（Lazy Propagation）
用于区间操作的优化，延迟处理子树的更新，在需要访问子树时才下传标记。

## 时间复杂度分析

- **插入/删除操作**：O(log n)
- **查询操作**（如查询第k大、前驱、后继）：O(log n)
- **区间操作**（如区间反转）：O(log n)

## 语言特性对比

### Python
- 简洁易读，递归实现清晰
- 注意递归深度限制
- 使用随机浮点数作为优先级

### Java
- 面向对象设计，类结构清晰
- 垃圾回收自动管理内存
- 随机数生成更规范

### C++
- 最高效的性能
- 手动内存管理，需要注意内存泄漏
- 使用结构化绑定提高代码可读性
- 节点池优化提高内存分配效率

## 学习建议

1. **理解核心原理**：掌握分裂和合并操作的本质
2. **练习基础操作**：实现插入、删除、查询等基本功能
3. **学习懒标记**：掌握如何使用懒标记优化区间操作
4. **解决实际问题**：通过不同类型的题目巩固理解
5. **优化实现细节**：根据不同语言特性进行针对性优化

## 应用场景

1. **动态维护有序序列**：支持高效的插入、删除和查询
2. **区间操作**：区间反转、区间查询等
3. **可持久化数据结构**：通过复制路径实现历史版本的保存
4. **优先队列管理**：动态维护最小值/最大值
5. **索引与排名问题**：支持快速的排名查询和按排名访问

## 总结

FHQ-Treap是一种功能强大且灵活的数据结构，通过简单的分裂和合并操作可以实现各种复杂的数据结构功能。它的无旋特性使其实现更加简单，同时保持了优秀的时间复杂度。本目录提供的多种语言实现和经典问题解法，希望能够帮助大家深入理解和掌握FHQ-Treap数据结构。
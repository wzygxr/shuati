# 持久化线段树（主席树）全面解析

## 算法介绍

持久化线段树（Persistent Segment Tree），也称为主席树，是一种支持历史版本查询的数据结构。它的核心思想是：

1. **函数式编程思想**：每次更新时创建新的节点，而不是修改原有节点
2. **路径共享**：对于未修改的子树，新版本与旧版本共享节点
3. **前缀和思想**：通过维护前缀版本，可以快速计算区间信息

## 时间和空间复杂度

- **时间复杂度**：
  - 构建：O(n log n)
  - 单点更新：O(log n)
  - 区间查询：O(log n)
- **空间复杂度**：O(n log n)

## 应用场景

1. **静态区间第K小问题**：多次查询区间第K小的数
2. **树上路径查询**：查询树上两点之间路径的第K小值
3. **历史版本查询**：查询数组在某个历史时刻的状态
4. **区间不同元素查询**：查询区间内有多少不同的元素
5. **区间Mex查询**：查询区间内最小缺失的自然数
6. **动态范围计数**：支持区间添加和查询计数

## 经典题目及解决方案

### 1. SPOJ MKTHNUM - K-th Number

- **题目链接**：https://www.spoj.com/problems/MKTHNUM/
- **题目描述**：给定一个数组，多次查询区间第K小的数
- **解法**：利用持久化线段树维护前缀版本的权值线段树，通过两个版本的差值得到区间信息
- **实现**：
  - Python: `persistent_segment_tree_solutions.py`中的`MKTHNUM`类
  - Java: `PersistentSegmentTreeSolutions.java`中的`MKTHNUM`方法
  - C++: `persistent_segment_tree_solutions.cpp`中的`MKTHNUM`命名空间

### 2. SPOJ COT - Count on a Tree

- **题目链接**：https://www.spoj.com/problems/COT/
- **题目描述**：给定一棵树，多次查询两点之间路径上的第K小的数
- **解法**：结合LCA算法和树上持久化线段树，利用节点到根的路径信息计算两点路径
- **实现**：
  - Python: `persistent_segment_tree_solutions.py`中的`COT`类
  - Java: `PersistentSegmentTreeSolutions.java`中的`COT`方法
  - C++: `persistent_segment_tree_solutions.cpp`中的`COT`命名空间

### 3. LeetCode 2276 - Count Integers in Intervals

- **题目链接**：https://leetcode.com/problems/count-integers-in-intervals/
- **题目描述**：实现一个数据结构，支持添加区间和查询区间内整数的个数
- **解法**：使用动态开点线段树高效维护区间覆盖状态
- **实现**：
  - Python: `persistent_segment_tree_solutions.py`中的`CountIntervals`类
  - Java: `PersistentSegmentTreeSolutions.java`中的`CountIntervals`类
  - C++: `persistent_segment_tree_solutions.cpp`中的`CountIntervals`类

### 4. LeetCode 1970 - Smallest Missing Genetic Value in Each Subtree

- **题目链接**：https://leetcode.com/problems/smallest-missing-genetic-value-in-each-subtree/
- **题目描述**：给定一棵树，每个节点有一个基因值，求每个子树中最小缺失的基因值
- **解法**：利用并查集和DFS进行高效查询
- **实现**：
  - Python: `persistent_segment_tree_solutions.py`中的`SmallestMissingGeneticValue`类
  - Java: `PersistentSegmentTreeSolutions.java`中的`smallestMissingGeneticValue`方法
  - C++: `persistent_segment_tree_solutions.cpp`中的`SmallestMissingGeneticValue`命名空间

### 5. SPOJ DQUERY - D-query

- **题目链接**：https://www.spoj.com/problems/DQUERY/
- **题目描述**：给定一个数组，多次查询区间内不同元素的个数
- **解法**：使用离线处理和树状数组，或者使用持久化线段树维护元素最后一次出现的位置
- **实现**：
  - Python: `persistent_segment_tree_solutions.py`中的`DQUERY`类
  - Java: `PersistentSegmentTreeSolutions.java`中的`DQUERY`方法
  - C++: `persistent_segment_tree_solutions.cpp`中的`DQUERY`命名空间

### 6. 第一次出现位置序列查询

- **题目描述**：查询区间内第一次出现的元素位置
- **解法**：使用持久化线段树维护元素最后一次出现的位置
- **实现**：
  - Python: `persistent_segment_tree_solutions.py`中的`FirstOccurrence`类
  - Java: `PersistentSegmentTreeSolutions.java`中的`firstOccurrence`方法
  - C++: `persistent_segment_tree_solutions.cpp`中的`FirstOccurrence`命名空间

### 7. 区间最小缺失自然数查询（区间Mex查询）

- **题目描述**：查询区间内最小缺失的自然数
- **解法**：使用持久化线段树维护元素最后一次出现的位置，通过二分查找缺失的最小值
- **实现**：
  - Python: `persistent_segment_tree_solutions.py`中的`RangeMex`类
  - Java: `PersistentSegmentTreeSolutions.java`中的`rangeMex`方法
  - C++: `persistent_segment_tree_solutions.cpp`中的`RangeMex`命名空间

## 算法实现要点

### 1. 节点结构设计

每个节点需要包含：
- 左右子节点的索引/指针
- 当前节点维护的信息（计数、最小值等）

### 2. 动态开点策略

为了节省空间，通常采用动态开点方式，只在需要时创建新节点。

### 3. 版本管理

维护一个根节点数组，每个元素对应一个历史版本。

### 4. 离散化处理

对于值域较大的情况，需要进行离散化处理。

## 工程化考量

### 1. 内存优化

- 使用预分配的数组存储节点信息（C++）
- 合理估计最大节点数，避免内存溢出

### 2. 性能优化

- 使用快速IO（如C++的`ios::sync_with_stdio(false)`）
- 避免不必要的节点创建
- 合理使用缓存，提高访问效率

### 3. 异常处理

- 处理边界情况（如空数组、查询超出范围等）
- 处理极端输入（如很大的数据规模）

### 4. 跨语言实现差异

- C++: 更适合处理大规模数据，需要手动管理内存
- Java: 代码更简洁，自动内存管理，但性能略低
- Python: 实现简单，但对于大规模数据可能性能不足

## 总结

持久化线段树是一种强大的数据结构，特别适合处理需要历史版本查询的问题。通过合理设计节点结构和更新策略，可以高效地解决各种区间查询问题。在实际应用中，需要根据具体问题选择合适的实现方式，并考虑性能优化和内存管理。

## 扩展应用

1. **二维主席树**：处理二维平面上的范围查询
2. **树链剖分+主席树**：解决树上路径问题
3. **可持久化线段树合并**：处理子树信息合并问题
4. **离线主席树**：处理离线查询问题

## 相关资源

- [LeetCode 持久化线段树题目](https://leetcode.com/tag/segment-tree/)
- [SPOJ 经典题目](https://www.spoj.com/problems/tag/persistent-segment-tree)
- [洛谷 主席树专题](https://www.luogu.com.cn/training/1437)
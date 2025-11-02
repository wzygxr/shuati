# class143 - 最短路算法与同余最短路

## 概述

class143主要讲解了图论中的最短路算法，包括Dijkstra算法和01-BFS算法，以及一种特殊的建图技巧——同余最短路。本课程涵盖了从基础到高级的多种最短路算法及其应用，通过大量实际题目帮助学习者深入理解算法本质。

## 算法详解

### 1. Dijkstra算法

Dijkstra算法用于解决带权有向图或无向图中的单源最短路径问题，要求所有边的权重为非负数。

#### 核心思想
使用贪心策略，每次选择当前未确定最短路径的节点中距离源点最近的节点，然后更新其邻居节点的距离。

#### 时间复杂度
- 朴素实现：O(V²)
- 堆优化实现：O((V + E) * log V)

#### 适用场景
- 单源最短路径问题
- 图中所有边权重为非负数
- 可用于解决同余最短路问题

### 2. 01-BFS算法

01-BFS用于解决边权仅为0或1的图上的最短路径问题。

#### 核心思想
使用双端队列(deque)代替优先队列，边权为0的节点加入队首，边权为1的节点加入队尾。

#### 时间复杂度
O(V + E)，比Dijkstra算法更优。

#### 适用场景
- 图中边权仅为0或1
- 需要求单源最短路径
- 比Dijkstra算法更高效

### 3. 同余最短路

同余最短路是一种特殊的图论建模技巧，通过构建模某个数意义下的最短路图来解决一些数论相关的问题。

#### 核心思想
选择一个基准数x，构建模x意义下的最短路图，每个节点表示模x的余数，通过其他数在不同余数之间建立边。

#### 时间复杂度
- 使用Dijkstra：O(x * log x)
- 使用01-BFS：O(x)

#### 适用场景
- 涉及多个数的线性组合问题
- 需要统计满足某种条件的数的个数
- 数据范围很大，无法直接使用动态规划

## 题目列表

### 基础题目

1. **跳楼机** (Code01_Elevator.*)
   - 算法：Dijkstra + 同余最短路
   - 链接：https://www.luogu.com.cn/problem/P3403

2. **牛场围栏** (Code02_CattleFence.*)
   - 算法：Dijkstra
   - 链接：https://www.luogu.com.cn/problem/P2662

3. **正整数倍的最小数位和** (Code03_SmallMultiple.*)
   - 算法：01-BFS
   - 链接：https://atcoder.jp/contests/abc077/tasks/arc084_b

4. **墨墨的等式** (Code04_MomoEquation*.*)  
   - 算法：Dijkstra + 同余最短路 + 两次转圈法
   - 链接：https://www.luogu.com.cn/problem/P2371

5. **背包** (Code05_Knapsack.*)
   - 算法：同余最短路 + 两次转圈法
   - 链接：https://www.luogu.com.cn/problem/P9140

6. **糖果传递** (CandyDistribution.*)
   - 算法：贪心 + 中位数
   - 链接：https://www.luogu.com.cn/problem/P2512

### 补充练习题

7. **网络延迟时间** (Code06_DijkstraExample1.*)
   - 算法：Dijkstra
   - 链接：https://leetcode.cn/problems/network-delay-time/

8. **迷宫最短路径** (Code07_ZeroOneBFSExample1.*)
   - 算法：01-BFS
   - 链接：https://atcoder.jp/contests/abc176/tasks/abc176_d

### 扩展练习题

9. **K 站中转内最便宜的航班** (Code08_DijkstraExtended.*)
   - 算法：限制边数的Dijkstra算法
   - 链接：https://leetcode.cn/problems/cheapest-flights-within-k-stops/

10. **使网格图至少有一条有效路径的最小代价** (Code09_ZeroOneBFSExtended.*)
    - 算法：01-BFS
    - 链接：https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/

11. **正整数倍的最小数位和(扩展)** (Code10_ModularShortestPath.*)
    - 算法：同余最短路
    - 链接：https://atcoder.jp/contests/abc077/tasks/arc084_b

## 详细内容

### 算法分析与比较
请参考 [AlgorithmAnalysis.md](AlgorithmAnalysis.md) 文件，其中包含了各种算法的详细分析、比较和适用场景。

### 扩展题目列表
请参考 [ExtendedProblems.md](ExtendedProblems.md) 文件，其中包含了来自各大OJ平台的更多相关题目。

### 最终报告
请参考 [FinalReport.md](FinalReport.md) 文件，其中包含了本项目的完整总结和学习建议。

## 算法技巧总结

### Dijkstra算法实现要点
1. 使用优先队列优化时间复杂度
2. 注意处理重边和自环
3. 使用链式前向星存储图结构
4. 正确实现松弛操作

### 01-BFS实现要点
1. 使用双端队列(deque)而非普通队列
2. 边权为0时加入队首，边权为1时加入队尾
3. 保持队列中元素的单调性

### 同余最短路建图要点
1. 选择合适的基准数（通常是输入中的最小数）
2. 构建模基准数意义下的图
3. 正确计算边权（通常是数值本身）
4. 后处理时注意边界条件

### 糖果传递问题要点
1. 利用环形结构的特殊性质
2. 通过前缀和将问题转化为中位数问题
3. 掌握贪心策略的应用

## 工程化考虑

### 异常处理
1. 输入数据验证
2. 边界条件检查
3. 内存使用优化

### 性能优化
1. 选择合适的数据结构
2. 避免不必要的计算
3. 使用位运算优化

### 代码可读性
1. 清晰的变量命名
2. 详细的注释说明
3. 模块化设计

## 相关算法扩展

### 与标准库实现对比
Java标准库中的`PriorityQueue`可以用于实现Dijkstra算法，但需要自定义比较器。

### 与其他最短路算法的关系
1. **BFS**：适用于边权全为1的无权图
2. **Dijkstra**：适用于非负权图
3. **01-BFS**：适用于边权仅为0或1的图
4. **SPFA**：适用于含负权边的图（可能有负环）
5. **Floyd**：适用于多源最短路径

## 学习建议

1. 熟练掌握Dijkstra算法的两种实现（朴素版和堆优化版）
2. 理解01-BFS的正确性和适用场景
3. 掌握同余最短路的建图技巧
4. 多做练习题，加深对算法本质的理解
5. 注意算法在工程实践中的应用
6. 掌握贪心算法在特殊问题中的应用
7. 学习如何根据具体问题选择合适的算法

## 项目结构

```
class143/
├── Code01_Elevator.*          # 跳楼机问题
├── Code02_CattleFence.*       # 牛场围栏问题
├── Code03_SmallMultiple.*     # 正整数倍的最小数位和问题
├── Code04_MomoEquation*.*     # 墨墨的等式问题
├── Code05_Knapsack.*          # 背包问题
├── Code06_DijkstraExample1.*  # Dijkstra算法练习题
├── Code07_ZeroOneBFSExample1.*# 01-BFS练习题
├── Code08_DijkstraExtended.*  # Dijkstra算法扩展练习题
├── Code09_ZeroOneBFSExtended.*# 01-BFS扩展练习题
├── Code10_ModularShortestPath.*# 同余最短路扩展练习题
├── CandyDistribution.*        # 糖果传递问题
├── TestAlgorithms.*           # 算法测试类
├── test_algorithms.*          # Python算法测试文件
├── README.md                  # 项目说明文档
├── AlgorithmAnalysis.md       # 算法分析与比较
├── ExtendedProblems.md        # 扩展题目列表
├── AdditionalProblems.md      # 补充题目列表
└── FinalReport.md             # 最终报告
```

其中`*`代表Java、C++、Python三种语言的实现文件。
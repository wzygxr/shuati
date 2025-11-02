# class143 最短路算法与同余最短路 - 最终报告

## 1. 项目概述

本项目全面介绍了图论中的最短路算法，包括Dijkstra算法、01-BFS算法和同余最短路技巧。通过大量实际题目和代码实现，帮助学习者深入理解这些算法的本质和应用场景。

## 2. 算法详解

### 2.1 Dijkstra算法
Dijkstra算法用于解决带权有向图或无向图中的单源最短路径问题，要求所有边的权重为非负数。

**核心思想：**
使用贪心策略，每次选择当前未确定最短路径的节点中距离源点最近的节点，然后更新其邻居节点的距离。

**时间复杂度：**
- 朴素实现：O(V²)
- 堆优化实现：O((V + E) * log V)

**适用场景：**
- 单源最短路径问题
- 图中所有边权重为非负数
- 可用于解决同余最短路问题

### 2.2 01-BFS算法
01-BFS用于解决边权仅为0或1的图上的最短路径问题。

**核心思想：**
使用双端队列(deque)代替优先队列，边权为0的节点加入队首，边权为1的节点加入队尾。

**时间复杂度：**
O(V + E)，比Dijkstra算法更优。

**适用场景：**
- 图中边权仅为0或1
- 需要求单源最短路径
- 比Dijkstra算法更高效

### 2.3 同余最短路
同余最短路是一种特殊的图论建模技巧，通过构建模某个数意义下的最短路图来解决一些数论相关的问题。

**核心思想：**
选择一个基准数x，构建模x意义下的最短路图，每个节点表示模x的余数，通过其他数在不同余数之间建立边。

**时间复杂度：**
- 使用Dijkstra：O(x * log x)
- 使用01-BFS：O(x)

**适用场景：**
- 涉及多个数的线性组合问题
- 需要统计满足某种条件的数的个数
- 数据范围很大，无法直接使用动态规划

### 多源BFS

#### 核心思想
多源BFS用于解决从多个源点同时开始搜索的最短路径问题。它将所有源点同时加入队列，然后进行BFS搜索。

#### 时间复杂度
O(V + E)

#### 适用场景
1. 多个起点同时开始搜索
2. 需要求所有点到最近源点的距离
3. 一些特殊的矩阵问题

## 题目汇总

### Dijkstra算法相关题目
1. 网络延迟时间（LeetCode 743）
2. 跳楼机（洛谷 P3403）
3. 墨墨的等式（洛谷 P2371）
4. 单源最短路径（洛谷 P4779）
5. Til the Cows Come Home（POJ 2387）
6. Dijkstra?（Codeforces 20C）
7. K 站中转内最便宜的航班（LeetCode 787）
8. Path With Minimum Effort（LeetCode 1631）
9. 为高尔夫比赛砍树（LeetCode 675）
10. 最短路径中的边（LeetCode 3123）
11. 设计可以求最短路径的图类（LeetCode 2642）
12. 逃离大迷宫（LeetCode 1036）
13. 最小体力消耗路径（LeetCode 1631）
14. 迷宫III（LeetCode 499）
15. 最短路径访问所有节点（LeetCode 847）
16. 信使（洛谷 P1629）
17. 最优乘车（洛谷 P1073）
18. 拯救大兵瑞恩（洛谷 P2962）
19. 香甜的黄油（POJ 3615）
20. 最短路（HDU 2544）
21. 阈值距离内邻居最少的城市（LeetCode 1334）
22. 次短路径（POJ 3255）
23. 牛的旅行（POJ 1546）
24. 最短路径计数（洛谷 P1608）
25. 最短路径树（洛谷 P2676）

### 01-BFS相关题目
1. Wizard in Maze（AtCoder ABC176_D）
2. 正整数倍的最小数位和（AtCoder ARC084_B）
3. Three States（Codeforces 590C）
4. Ocean Currents（UVA 11573）
5. KATHTHI（SPOJ KATHTHI）
6. 到达角落需要移除障碍物的最小数目（LeetCode 2290）
7. 最少侧跳次数（LeetCode 1824）
8. 使网格图至少有一条有效路径的最小代价（LeetCode 1368）
9. 方格取数（洛谷 P1004）
10. 巡逻的士兵（洛谷 P1429）
11. 逃离僵尸岛（洛谷 P2491）
12. 寻找道路（洛谷 P2296）
13. 道路和航线（洛谷 P2384）
14. 回家的最短路径（POJ 3259）
15. 逃离（HDU 6214）
16. 滑动谜题（LeetCode 773）
17. 最小基因变化（LeetCode 433）
18. 单词接龙（LeetCode 127）
19. 迷宫中离入口最近的出口（LeetCode 1926）
20. 墙与门（LeetCode 286）

### 同余最短路相关题目
1. 跳楼机（洛谷 P3403）
2. 墨墨的等式（洛谷 P2371）
3. 牛场围栏（洛谷 P2662）
4. 背包（洛谷 P9140）
5. Oppa Funcan Style Remastered（Codeforces 986F）
6. 数列分段（洛谷 P1776）
7. 数学作业（洛谷 P1948）
8. 硬币问题（POJ 3250）
9. 青蛙的约会（POJ 1061）
10. 荒岛野人（洛谷 P2421）
11. 正整数倍的最小数位和（AtCoder ARC084_B）
12. 同余方程（洛谷 P1082）
13. 表达整数的奇怪方式（洛谷 P2480）
14. 余数之和（洛谷 P2261）
15. 数列区间和（洛谷 P1642）

### 多源BFS相关题目
1. 01 矩阵（LeetCode 542）
2. 腐烂的橘子（LeetCode 994）
3. 最大人工岛（LeetCode 827）
4. 距离顺序排列矩阵单元格（LeetCode 1030）
5. 地图分析（LeetCode 1162）
6. 墙与门（LeetCode 286）
7. 矩阵中的距离（洛谷 P1124）
8. 消防（洛谷 P1329）
9. 奶牛排队（洛谷 P1658）
10. 电路维修（洛谷 P2243）

## 代码实现

### 4.1 Java实现
- Code01_Elevator.java: 跳楼机问题
- Code02_CattleFence.java: 牛场围栏问题
- Code03_SmallMultiple.java: 正整数倍的最小数位和问题
- Code04_MomoEquation*.java: 墨墨的等式问题
- Code05_Knapsack.java: 背包问题
- Code06_DijkstraExample1.java: Dijkstra算法练习题
- Code07_ZeroOneBFSExample1.java: 01-BFS练习题
- Code08_DijkstraExtended.java: Dijkstra算法扩展练习题
- Code09_ZeroOneBFSExtended.java: 01-BFS扩展练习题
- Code10_ModularShortestPath.java: 同余最短路扩展练习题
- TestAlgorithms.java: 算法测试类
- CandyDistribution.java: 糖果传递问题

### 4.2 C++实现
- Code01_Elevator.cpp: 跳楼机问题
- Code02_CattleFence.cpp: 牛场围栏问题
- Code03_SmallMultiple.cpp: 正整数倍的最小数位和问题
- Code04_MomoEquation*.cpp: 墨墨的等式问题
- Code05_Knapsack.cpp: 背包问题
- Code06_DijkstraExample1.cpp: Dijkstra算法练习题
- Code07_ZeroOneBFSExample1.cpp: 01-BFS练习题
- Code08_DijkstraExtended.cpp: Dijkstra算法扩展练习题
- Code09_ZeroOneBFSExtended.cpp: 01-BFS扩展练习题
- Code10_ModularShortestPath.cpp: 同余最短路扩展练习题
- CandyDistribution.cpp: 糖果传递问题

### 4.3 Python实现
- Code01_Elevator.py: 跳楼机问题
- Code02_CattleFence.py: 牛场围栏问题
- Code03_SmallMultiple.py: 正整数倍的最小数位和问题
- Code04_MomoEquation*.py: 墨墨的等式问题
- Code05_Knapsack.py: 背包问题
- Code06_DijkstraExample1.py: Dijkstra算法练习题
- Code07_ZeroOneBFSExample1.py: 01-BFS练习题
- Code08_DijkstraExtended.py: Dijkstra算法扩展练习题
- Code09_ZeroOneBFSExtended.py: 01-BFS扩展练习题
- Code10_ModularShortestPath.py: 同余最短路扩展练习题
- test_algorithms.py: 算法测试文件
- CandyDistribution.py: 糖果传递问题

## 5. 测试结果

通过运行TestAlgorithms.java和test_algorithms.py，我们验证了所有算法实现的正确性：

1. **Dijkstra算法测试：**
   - 从节点0到各节点的最短距离计算正确
   - 到节点0的距离：0
   - 到节点1的距离：8
   - 到节点2的距离：5
   - 到节点3的距离：9
   - 到节点4的距离：7

2. **01-BFS算法测试：**
   - 从(0,0)到(2,2)的最短路径长度：4

3. **同余最短路算法测试：**
   - k=3时，结果：3
   - k=7时，结果：6
   - k=5时，结果：-1

4. **糖果传递问题测试：**
   - 测试用例1结果: 6
   - 测试用例2结果: 15
   - 测试用例3结果: 0

## 6. 工程化考虑

### 6.1 异常处理
1. 输入数据验证：检查输入是否符合题目要求
2. 边界条件检查：处理特殊情况，如空图、单节点等
3. 内存使用优化：合理分配内存，避免浪费

### 6.2 性能优化
1. 选择合适的数据结构：如优先队列、双端队列等
2. 避免不必要的计算：如重复计算、无效状态等
3. 使用位运算优化：在某些情况下可以提高效率

### 6.3 代码可读性
1. 清晰的变量命名：使用有意义的变量名
2. 详细的注释说明：解释算法思路和关键步骤
3. 模块化设计：将功能拆分为独立的模块

## 7. 学习建议

### 7.1 基础掌握
1. 熟练掌握Dijkstra算法的两种实现（朴素版和堆优化版）
2. 理解01-BFS的正确性和适用场景
3. 掌握同余最短路的建图技巧

### 7.2 进阶提升
1. 学习其他最短路算法：如Bellman-Ford、Floyd-Warshall等
2. 理解算法的数学原理和证明过程
3. 掌握算法的变种和扩展应用

### 7.3 实践应用
1. 多做练习题，加深对算法本质的理解
2. 参与编程竞赛，提高算法应用能力
3. 注意算法在工程实践中的应用

## 8. 总结

通过本项目的学习，我们全面掌握了最短路算法的核心思想和实现方法。从基础的Dijkstra算法到高效的01-BFS，再到解决数论问题的同余最短路技巧，每种算法都有其独特的应用场景和优势。

在实际应用中，我们需要根据具体问题的特点选择合适的算法：
- 对于一般的非负权图最短路问题，使用Dijkstra算法
- 对于边权仅为0或1的图，使用01-BFS算法以获得更好的时间复杂度
- 对于涉及数论的线性组合问题，使用同余最短路技巧

通过大量的练习和实践，我们可以更好地理解和应用这些算法，提高解决实际问题的能力。
# class143 扩展题目和训练

## Dijkstra算法相关题目

### 1. 网络延迟时间
- **题目来源**: LeetCode 743
- **题目链接**: https://leetcode.cn/problems/network-delay-time/
- **算法**: Dijkstra算法
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

### 2. 跳楼机
- **题目来源**: 洛谷 P3403
- **题目链接**: https://www.luogu.com.cn/problem/P3403
- **算法**: 同余最短路 + Dijkstra算法
- **时间复杂度**: O(x * log x)
- **空间复杂度**: O(x)

### 3. 墨墨的等式
- **题目来源**: 洛谷 P2371
- **题目链接**: https://www.luogu.com.cn/problem/P2371
- **算法**: 同余最短路 + Dijkstra算法
- **时间复杂度**: O(x * log x + n)
- **空间复杂度**: O(x)

### 4. 单源最短路径
- **题目来源**: 洛谷 P4779
- **题目链接**: https://www.luogu.com.cn/problem/P4779
- **算法**: 堆优化Dijkstra算法
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

### 5. Til the Cows Come Home
- **题目来源**: POJ 2387
- **题目链接**: http://poj.org/problem?id=2387
- **算法**: Dijkstra算法
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

### 6. Dijkstra?
- **题目来源**: Codeforces 20C
- **题目链接**: https://codeforces.com/problemset/problem/20/C
- **算法**: Dijkstra算法
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

### 7. K 站中转内最便宜的航班
- **题目来源**: LeetCode 787
- **题目链接**: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
- **算法**: 限制边数的Dijkstra算法
- **时间复杂度**: O(K * E * log V)
- **空间复杂度**: O(V + E)

### 8. Path With Minimum Effort
- **题目来源**: LeetCode 1631
- **题目链接**: https://leetcode.cn/problems/path-with-minimum-effort/
- **算法**: Dijkstra算法 + 二分搜索
- **时间复杂度**: O(M * N * log(M * N))
- **空间复杂度**: O(M * N)

### 9. 为高尔夫比赛砍树
- **题目来源**: LeetCode 675
- **题目链接**: https://leetcode.cn/problems/cut-off-trees-for-golf-event/
- **算法**: Dijkstra算法 + BFS
- **时间复杂度**: O((M * N)^2 * log(M * N))
- **空间复杂度**: O(M * N)

### 10. 最短路径中的边
- **题目来源**: LeetCode 3123
- **题目链接**: https://leetcode.cn/problems/find-edges-in-shortest-paths/
- **算法**: Dijkstra算法 + 边标记
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

### 11. 设计可以求最短路径的图类
- **题目来源**: LeetCode 2642
- **题目链接**: https://leetcode.cn/problems/design-graph-with-shortest-path-calculator/
- **算法**: Dijkstra算法
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

### 12. 逃离大迷宫
- **题目来源**: LeetCode 1036
- **题目链接**: https://leetcode.cn/problems/escape-a-large-maze/
- **算法**: BFS + Dijkstra
- **时间复杂度**: O(障碍物数量^2)
- **空间复杂度**: O(障碍物数量^2)

### 13. 最小体力消耗路径
- **题目来源**: LeetCode 1631
- **题目链接**: https://leetcode.cn/problems/path-with-minimum-effort/
- **算法**: Dijkstra算法
- **时间复杂度**: O(M * N * log(M * N))
- **空间复杂度**: O(M * N)

### 14. 迷宫III
- **题目来源**: LeetCode 499
- **题目链接**: https://leetcode.cn/problems/the-maze-iii/
- **算法**: Dijkstra算法
- **时间复杂度**: O(M * N * log(M * N))
- **空间复杂度**: O(M * N)

### 15. 最短路径访问所有节点
- **题目来源**: LeetCode 847
- **题目链接**: https://leetcode.cn/problems/shortest-path-visiting-all-nodes/
- **算法**: BFS + 状态压缩
- **时间复杂度**: O(2^N * N^2)
- **空间复杂度**: O(2^N * N)

### 16. 信使
- **题目来源**: 洛谷 P1629
- **题目链接**: https://www.luogu.com.cn/problem/P1629
- **算法**: Dijkstra算法
- **时间复杂度**: O(N^2)
- **空间复杂度**: O(N^2)

### 17. 最优乘车
- **题目来源**: 洛谷 P1073
- **题目链接**: https://www.luogu.com.cn/problem/P1073
- **算法**: BFS + Dijkstra
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 18. 拯救大兵瑞恩
- **题目来源**: 洛谷 P2962
- **题目链接**: https://www.luogu.com.cn/problem/P2962
- **算法**: Dijkstra算法 + 状态压缩
- **时间复杂度**: O(2^K * M * N * log(2^K * M * N))
- **空间复杂度**: O(2^K * M * N)

### 19. 香甜的黄油
- **题目来源**: POJ 3615
- **题目链接**: http://poj.org/problem?id=3615
- **算法**: Dijkstra算法
- **时间复杂度**: O(C * (P + C) * log P)
- **空间复杂度**: O(P + C)

### 20. 最短路
- **题目来源**: HDU 2544
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2544
- **算法**: Dijkstra算法
- **时间复杂度**: O(N^2)
- **空间复杂度**: O(N^2)

### 21. 阈值距离内邻居最少的城市
- **题目来源**: LeetCode 1334
- **题目链接**: https://leetcode.cn/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/
- **算法**: Dijkstra算法/Floyd算法
- **时间复杂度**: O(N * (N + E) * log N)
- **空间复杂度**: O(N + E)

### 22. 次短路径
- **题目来源**: POJ 3255
- **题目链接**: http://poj.org/problem?id=3255
- **算法**: Dijkstra算法变种
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

### 23. 牛的旅行
- **题目来源**: POJ 1546
- **题目链接**: http://poj.org/problem?id=1546
- **算法**: Dijkstra算法 + Floyd算法
- **时间复杂度**: O(N^3)
- **空间复杂度**: O(N^2)

### 24. 最短路径计数
- **题目来源**: 洛谷 P1608
- **题目链接**: https://www.luogu.com.cn/problem/P1608
- **算法**: Dijkstra算法 + 动态规划
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

### 25. 最短路径树
- **题目来源**: 洛谷 P2676
- **题目链接**: https://www.luogu.com.cn/problem/P2676
- **算法**: Dijkstra算法 + 最小生成树
- **时间复杂度**: O((V + E) * log V)
- **空间复杂度**: O(V + E)

## 01-BFS相关题目

### 1. Wizard in Maze
- **题目来源**: AtCoder ABC176_D
- **题目链接**: https://atcoder.jp/contests/abc176/tasks/abc176_d
- **算法**: 01-BFS
- **时间复杂度**: O(N * M)
- **空间复杂度**: O(N * M)

### 2. 正整数倍的最小数位和
- **题目来源**: AtCoder ARC084_B
- **题目链接**: https://atcoder.jp/contests/abc077/tasks/arc084_b
- **算法**: 01-BFS
- **时间复杂度**: O(k)
- **空间复杂度**: O(k)

### 3. Three States
- **题目来源**: Codeforces 590C
- **题目链接**: https://codeforces.com/contest/590/problem/C
- **算法**: 01-BFS
- **时间复杂度**: O(N * M)
- **空间复杂度**: O(N * M)

### 4. Ocean Currents
- **题目来源**: UVA 11573
- **题目链接**: https://vjudge.net/problem/UVA-11573
- **算法**: 01-BFS
- **时间复杂度**: O(N * M)
- **空间复杂度**: O(N * M)

### 5. KATHTHI
- **题目来源**: SPOJ KATHTHI
- **题目链接**: https://vjudge.net/problem/SPOJ-KATHTHI
- **算法**: 01-BFS
- **时间复杂度**: O(N * M)
- **空间复杂度**: O(N * M)

### 6. 到达角落需要移除障碍物的最小数目
- **题目来源**: LeetCode 2290
- **题目链接**: https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/
- **算法**: 01-BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 7. 最少侧跳次数
- **题目来源**: LeetCode 1824
- **题目链接**: https://leetcode.cn/problems/minimum-sideway-jumps/
- **算法**: 01-BFS
- **时间复杂度**: O(N)
- **空间复杂度**: O(N)

### 8. 使网格图至少有一条有效路径的最小代价
- **题目来源**: LeetCode 1368
- **题目链接**: https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/
- **算法**: 01-BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 9. 方格取数
- **题目来源**: 洛谷 P1004
- **题目链接**: https://www.luogu.com.cn/problem/P1004
- **算法**: 01-BFS + 动态规划
- **时间复杂度**: O(N^2)
- **空间复杂度**: O(N^2)

### 10. 巡逻的士兵
- **题目来源**: 洛谷 P1429
- **题目链接**: https://www.luogu.com.cn/problem/P1429
- **算法**: 01-BFS
- **时间复杂度**: O(N^2)
- **空间复杂度**: O(N^2)

### 11. 逃离僵尸岛
- **题目来源**: 洛谷 P2491
- **题目链接**: https://www.luogu.com.cn/problem/P2491
- **算法**: 01-BFS + 多源BFS
- **时间复杂度**: O(N + M)
- **空间复杂度**: O(N + M)

### 12. 寻找道路
- **题目来源**: 洛谷 P2296
- **题目链接**: https://www.luogu.com.cn/problem/P2296
- **算法**: 01-BFS
- **时间复杂度**: O(N + M)
- **空间复杂度**: O(N + M)

### 13. 道路和航线
- **题目来源**: 洛谷 P2384
- **题目链接**: https://www.luogu.com.cn/problem/P2384
- **算法**: 01-BFS + 拓扑排序
- **时间复杂度**: O(N + M)
- **空间复杂度**: O(N + M)

### 14. 回家的最短路径
- **题目来源**: POJ 3259
- **题目链接**: http://poj.org/problem?id=3259
- **算法**: 01-BFS
- **时间复杂度**: O(N * M)
- **空间复杂度**: O(N * M)

### 15. 逃离
- **题目来源**: HDU 6214
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=6214
- **算法**: 01-BFS
- **时间复杂度**: O(N)
- **空间复杂度**: O(N)

### 16. 滑动谜题
- **题目来源**: LeetCode 773
- **题目链接**: https://leetcode.cn/problems/sliding-puzzle/
- **算法**: 01-BFS/BFS
- **时间复杂度**: O(N! * M)
- **空间复杂度**: O(N! * M)

### 17. 最小基因变化
- **题目来源**: LeetCode 433
- **题目链接**: https://leetcode.cn/problems/minimum-genetic-mutation/
- **算法**: 01-BFS/BFS
- **时间复杂度**: O(N * M)
- **空间复杂度**: O(N * M)

### 18. 单词接龙
- **题目来源**: LeetCode 127
- **题目链接**: https://leetcode.cn/problems/word-ladder/
- **算法**: 01-BFS/BFS
- **时间复杂度**: O(N * M)
- **空间复杂度**: O(N * M)

### 19. 迷宫中离入口最近的出口
- **题目来源**: LeetCode 1926
- **题目链接**: https://leetcode.cn/problems/nearest-exit-from-entrance-in-maze/
- **算法**: 01-BFS/BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 20. 墙与门
- **题目来源**: LeetCode 286
- **题目链接**: https://leetcode.cn/problems/walls-and-gates/
- **算法**: 多源BFS/01-BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

## 同余最短路相关题目

### 1. 跳楼机
- **题目来源**: 洛谷 P3403
- **题目链接**: https://www.luogu.com.cn/problem/P3403
- **算法**: 同余最短路 + Dijkstra算法
- **时间复杂度**: O(x * log x)
- **空间复杂度**: O(x)

### 2. 墨墨的等式
- **题目来源**: 洛谷 P2371 / BZOJ 2118
- **题目链接**: https://www.luogu.com.cn/problem/P2371
- **算法**: 同余最短路 + Dijkstra算法
- **时间复杂度**: O(x * log x + n)
- **空间复杂度**: O(x)

### 3. 牛场围栏
- **题目来源**: 洛谷 P2662
- **题目链接**: https://www.luogu.com.cn/problem/P2662
- **算法**: 同余最短路 + Dijkstra算法
- **时间复杂度**: O(x * log x)
- **空间复杂度**: O(x)

### 4. 背包
- **题目来源**: 洛谷 P9140
- **题目链接**: https://www.luogu.com.cn/problem/P9140
- **算法**: 同余最短路 + 两次转圈法
- **时间复杂度**: O(x + n + m)
- **空间复杂度**: O(x)

### 5. Oppa Funcan Style Remastered
- **题目来源**: Codeforces 986F
- **题目链接**: https://codeforces.com/problemset/problem/986/F
- **算法**: 同余最短路 + 多源最短路
- **时间复杂度**: O(k * x * log x)
- **空间复杂度**: O(x)

### 6. 数列分段
- **题目来源**: 洛谷 P1776
- **题目链接**: https://www.luogu.com.cn/problem/P1776
- **算法**: 同余最短路 + 动态规划
- **时间复杂度**: O(n * x)
- **空间复杂度**: O(n * x)

### 7. 数学作业
- **题目来源**: 洛谷 P1948
- **题目链接**: https://www.luogu.com.cn/problem/P1948
- **算法**: 同余最短路
- **时间复杂度**: O(k * x)
- **空间复杂度**: O(x)

### 8. 硬币问题
- **题目来源**: POJ 3250
- **题目链接**: http://poj.org/problem?id=3250
- **算法**: 同余最短路
- **时间复杂度**: O(x * log x)
- **空间复杂度**: O(x)

### 9. 青蛙的约会
- **题目来源**: POJ 1061
- **题目链接**: http://poj.org/problem?id=1061
- **算法**: 同余最短路
- **时间复杂度**: O(log x)
- **空间复杂度**: O(1)

### 10. 荒岛野人
- **题目来源**: 洛谷 P2421
- **题目链接**: https://www.luogu.com.cn/problem/P2421
- **算法**: 同余最短路
- **时间复杂度**: O(m^2 * log n)
- **空间复杂度**: O(m)

### 11. 正整数倍的最小数位和
- **题目来源**: AtCoder ARC084_B
- **题目链接**: https://atcoder.jp/contests/abc077/tasks/arc084_b
- **算法**: 同余最短路/01-BFS
- **时间复杂度**: O(k)
- **空间复杂度**: O(k)

### 12. 同余方程
- **题目来源**: 洛谷 P1082
- **题目链接**: https://www.luogu.com.cn/problem/P1082
- **算法**: 同余最短路/扩展欧几里得
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 13. 表达整数的奇怪方式
- **题目来源**: 洛谷 P2480
- **题目链接**: https://www.luogu.com.cn/problem/P2480
- **算法**: 同余最短路/中国剩余定理
- **时间复杂度**: O(n * log n)
- **空间复杂度**: O(n)

### 14. 余数之和
- **题目来源**: 洛谷 P2261
- **题目链接**: https://www.luogu.com.cn/problem/P2261
- **算法**: 同余最短路/数学推导
- **时间复杂度**: O(sqrt(n))
- **空间复杂度**: O(1)

### 15. 数列区间和
- **题目来源**: 洛谷 P1642
- **题目链接**: https://www.luogu.com.cn/problem/P1642
- **算法**: 同余最短路/动态规划
- **时间复杂度**: O(n * k)
- **空间复杂度**: O(n * k)

## 多源BFS相关题目

### 1. 01 矩阵
- **题目来源**: LeetCode 542
- **题目链接**: https://leetcode.cn/problems/01-matrix/
- **算法**: 多源BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 2. 腐烂的橘子
- **题目来源**: LeetCode 994
- **题目链接**: https://leetcode.cn/problems/rotting-oranges/
- **算法**: 多源BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 3. 最大人工岛
- **题目来源**: LeetCode 827
- **题目链接**: https://leetcode.cn/problems/making-a-large-island/
- **算法**: BFS + 连通分量
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 4. 距离顺序排列矩阵单元格
- **题目来源**: LeetCode 1030
- **题目链接**: https://leetcode.cn/problems/matrix-cells-in-distance-order/
- **算法**: 多源BFS
- **时间复杂度**: O(R * C)
- **空间复杂度**: O(R * C)

### 5. 地图分析
- **题目来源**: LeetCode 1162
- **题目链接**: https://leetcode.cn/problems/as-far-from-land-as-possible/
- **算法**: 多源BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 6. 墙与门
- **题目来源**: LeetCode 286
- **题目链接**: https://leetcode.cn/problems/walls-and-gates/
- **算法**: 多源BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 7. 矩阵中的距离
- **题目来源**: 洛谷 P1124
- **题目链接**: https://www.luogu.com.cn/problem/P1124
- **算法**: 多源BFS
- **时间复杂度**: O(M * N)
- **空间复杂度**: O(M * N)

### 8. 消防
- **题目来源**: 洛谷 P1329
- **题目链接**: https://www.luogu.com.cn/problem/P1329
- **算法**: 多源BFS
- **时间复杂度**: O(N)
- **空间复杂度**: O(N)

### 9. 奶牛排队
- **题目来源**: 洛谷 P1658
- **题目链接**: https://www.luogu.com.cn/problem/P1658
- **算法**: 多源BFS
- **时间复杂度**: O(N)
- **空间复杂度**: O(N)

### 10. 电路维修
- **题目来源**: 洛谷 P2243
- **题目链接**: https://www.luogu.com.cn/problem/P2243
- **算法**: 多源BFS/01-BFS
- **时间复杂度**: O(N * M)
- **空间复杂度**: O(N * M)

## 算法思路总结

### Dijkstra算法
Dijkstra算法用于解决带权有向图或无向图中的单源最短路径问题，要求所有边的权重为非负数。

#### 适用场景：
1. 单源最短路径问题
2. 图中所有边权重为非负数
3. 可用于解决同余最短路问题

#### 核心思想：
使用贪心策略，每次选择当前未确定最短路径的节点中距离源点最近的节点，然后更新其邻居节点的距离。

#### 优化：
使用优先队列(堆)优化，将时间复杂度从O(V²)优化到O((V + E) * log V)。

### 01-BFS算法
01-BFS用于解决边权仅为0或1的图上的最短路径问题。

#### 适用场景：
1. 图中边权仅为0或1
2. 需要求单源最短路径
3. 比Dijkstra算法更高效

#### 核心思想：
使用双端队列(deque)代替优先队列，边权为0的节点加入队首，边权为1的节点加入队尾。

#### 时间复杂度：
O(V + E)，比Dijkstra算法的O((V + E) * log V)更优。

### 同余最短路
同余最短路是一种特殊的图论建模技巧，通过构建模某个数意义下的最短路图来解决一些数论相关的问题。

#### 适用场景：
1. 涉及多个数的线性组合问题
2. 需要统计满足某种条件的数的个数
3. 数据范围很大，无法直接使用动态规划

#### 核心思想：
选择一个基准数x，构建模x意义下的最短路图，每个节点表示模x的余数，通过其他数在不同余数之间建立边。

#### 时间复杂度：
O(x * log x)（使用Dijkstra）或O(x)（使用01-BFS）

### 多源BFS
多源BFS用于解决从多个源点同时开始搜索的最短路径问题。

#### 适用场景：
1. 多个起点同时开始搜索
2. 需要求所有点到最近源点的距离
3. 一些特殊的矩阵问题

#### 核心思想：
将所有源点同时加入队列，然后进行BFS搜索。

#### 时间复杂度：
O(V + E)
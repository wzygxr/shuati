# Kruskal重构树 算法总结

## 1. 算法简介

Kruskal重构树是一种基于Kruskal算法构建的特殊数据结构，主要用于解决图论中与路径边权极值相关的问题。它将原图的边权信息转化为树上的点权，从而将路径上的边权问题转化为树上节点的点权问题。

## 2. 构建过程

1. 将原图中的所有边按照边权排序（升序或降序根据题目要求）
2. 执行Kruskal算法构建最小/最大生成树
3. 每当合并两个连通分量时，不是直接连接两个点，而是：
   - 新建一个节点，节点权值为当前边的边权
   - 将两个连通分量的根节点作为新建节点的左右子节点
   - 更新并查集，将新建节点作为新的根节点

## 3. 重要性质

1. **结构性质**：重构树是一棵二叉树，原图中的n个节点是重构树的叶子节点
2. **堆性质**：
   - 如果按边权升序构建，则重构树满足大根堆性质
   - 如果按边权降序构建，则重构树满足小根堆性质
3. **路径性质**：原图中两点间路径的边权极值等于重构树上两点LCA的节点权值
   - 按边权升序构建：两点间路径最大边权的最小值 = LCA节点权值
   - 按边权降序构建：两点间路径最小边权的最大值 = LCA节点权值
4. **连通性**：重构树中两点连通当且仅当原图中两点连通

## 4. 应用场景

### 4.1 路径边权限制类问题
- 求两点间所有路径中最大边权的最小值
- 求两点间所有路径中最小边权的最大值

### 4.2 可达性问题
- 在边权限制下，从某点能到达的所有节点
- 通常结合倍增算法实现快速查询

### 4.3 经典题目
1. **P2245 星际导航** - [洛谷链接](https://www.luogu.com.cn/problem/P2245)，求两点间路径最大边权的最小值
2. **P1967 [NOIP2013 提高组] 货车运输** - [洛谷链接](https://www.luogu.com.cn/problem/P1967)，求两点间路径最小边权的最大值
3. **U92652 【模板】kruskal重构树** - [洛谷链接](https://www.luogu.com.cn/problem/U92652)，模板题，求两点间路径最大边权的最小值
4. **P4768 [NOI2018] 归程** - [洛谷链接](https://www.luogu.com.cn/problem/P4768)，结合最短路和Kruskal重构树
5. **CF1706E Qpwoeirut and Vertices** - [Codeforces链接](https://codeforces.com/contest/1706/problem/E)，加边直到连通
6. **CF1416D Graph and Queries** - [Codeforces链接](https://codeforces.com/contest/1416/problem/D)，删边和查询
7. **P7834 [ONTAK2010] Peaks 加强版** - [洛谷链接](https://www.luogu.com.cn/problem/P7834)，结合可持久化线段树
8. **[AGC002D] Stamp Rally** - [AtCoder链接](https://atcoder.jp/contests/agc002/tasks/agc002_d)，访问指定数量节点的最小边权最大值
9. **LibreOJ 137 最小瓶颈路加强版** - [LibreOJ链接](https://loj.ac/p/137)，标准最小瓶颈路问题
10. **洛谷 P2504 聪明的猴子** - [洛谷链接](https://www.luogu.com.cn/problem/P2504)，最小瓶颈树问题
11. **CF825G Tree Queries** - [Codeforces链接](https://codeforces.com/problemset/problem/825/G)，树上查询问题
12. **P4899 [IOI 2018] werewolf 狼人** - [洛谷链接](https://www.luogu.com.cn/problem/P4899)，IOI题目
13. **CF1578L Labyrinth** - [Codeforces链接](https://codeforces.com/problemset/problem/1578/L)，迷宫问题
14. **P6765 [APIO2020] 交换城市** - [洛谷链接](https://www.luogu.com.cn/problem/P6765)，城市连通性问题
15. **CF1253F Cheap Robot** - [Codeforces链接](https://codeforces.com/problemset/problem/1253/F)，机器人路径规划
16. **P4197 Peaks** - [洛谷链接](https://www.luogu.com.cn/problem/P4197)，经典题目
17. **CF1408G Clusterization Counting** - [Codeforces链接](https://codeforces.com/problemset/problem/1408/G)，计数问题
18. **P3684 [CERC2016] 机棚障碍 Hangar Hurdles** - [洛谷链接](https://www.luogu.com.cn/problem/P3684)，几何结合图论
19. **CF1628E Groceries in Meteor Town** - [Codeforces链接](https://codeforces.com/problemset/problem/1628/E)，天气影响的购物问题
20. **P5360 [SDOI2019] 世界地图** - [洛谷链接](https://www.luogu.com.cn/problem/P5360)，地图连通性问题
21. **CF1797F Li Hua and Path** - [Codeforces链接](https://codeforces.com/problemset/problem/1797/F)，路径问题
22. **UVA1265 Tour Belt** - [UVA链接](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3706)，旅游路线问题
23. **AT_arc098_d [ARC098F] Donation** - [AtCoder链接](https://atcoder.jp/contests/arc098/tasks/arc098_d)，捐赠问题
24. **Comet OJ - Contest #11 D.Disaster** - [Comet OJ链接](https://www.cometoj.com/contest/54/problem/D)，Kruskal重构树+倍增+dfs序+线段树区间乘
25. **Educational Codeforces Round 122 (Div.2) E. Spanning Tree Queries** - [Codeforces链接](https://codeforces.com/contest/1633/problem/E)，最小生成树分段一次函数
26. **Educational Codeforces Round 152 F. XOR Partition** - [Codeforces链接](https://codeforces.com/contest/1849/problem/F)，boruvka完全图最小生成树
27. **Codeforces Round #111 (Div.2) D.Edges in MST** - [Codeforces链接](https://codeforces.com/contest/162/problem/D)，最小生成树边的分类
28. **P9984 (USACO23DEC) A Graph Problem P** - [洛谷链接](https://www.luogu.com.cn/problem/P9984)，以边的编号为边权的Kruskal重构树

## 5. 时间复杂度分析

- 构建Kruskal重构树：O(m log m)，主要消耗在边排序上
- DFS预处理（构建倍增表）：O(n)
- 单次查询（LCA）：O(log n)
- 总复杂度（q次查询）：O(m log m + q log n)

## 6. 空间复杂度分析

- 存储边：O(m)
- 存储重构树：O(n)
- 倍增表：O(n log n)
- 总空间复杂度：O(n log n + m)

## 7. 实现要点

1. **并查集**：用于维护连通性
2. **树的存储**：通常使用邻接表
3. **倍增LCA**：预处理和查询都需要
4. **排序策略**：
   - 求最大边权最小值：按边权升序排序
   - 求最小边权最大值：按边权降序排序

## 8. 与其他算法的对比

| 算法 | 适用场景 | 时间复杂度 | 空间复杂度 | 优势 |
|------|----------|------------|------------|------|
| Kruskal重构树 | 路径边权限制问题 | O(m log m + q log n) | O(n log n + m) | 可处理在线查询，结构清晰 |
| 二分答案+并查集 | 离线问题 | O((m + q) log m) | O(n + m) | 实现简单 |
| 树链剖分 | 树上问题 | O(m log m + q log²n) | O(n log n) | 更通用 |

## 9. 注意事项

1. **排序方向**：根据题目要求确定是按升序还是降序排序
2. **连通性检查**：查询前需要检查两点是否连通
3. **数据范围**：重构树节点数约为2n-1，注意数组大小
4. **语言特性**：
   - Java中注意递归爆栈问题，需要使用迭代实现DFS
   - C++中可以使用递归DFS
   - Python中注意性能问题，可能需要优化IO

## 10. 工程化考虑

1. **异常处理**：输入数据合法性检查
2. **性能优化**：
   - 快速IO（特别是Java和Python）
   - 内存池优化（C++）
3. **可维护性**：
   - 代码模块化
   - 详细注释
   - 清晰的变量命名
4. **测试**：
   - 边界情况测试
   - 性能测试
   - 正确性验证

## 11. 补充题目列表

以下是在class164中实现的Kruskal重构树相关题目以及更多推荐题目：

### 11.1 基础模板题
- **U92652 【模板】kruskal重构树** - [洛谷链接](https://www.luogu.com.cn/problem/U92652)，基础模板题，求两点间路径最大边权的最小值
- **洛谷 P2504 聪明的猴子** - [洛谷链接](https://www.luogu.com.cn/problem/P2504)，最小瓶颈树问题，证明最小生成树是最小瓶颈树的典型例题

### 11.2 经典应用题
- **P1967 [NOIP2013 提高组] 货车运输** - [洛谷链接](https://www.luogu.com.cn/problem/P1967)，求两点间路径最小边权的最大值，实际应用中的运输问题
- **P2245 星际导航** - [洛谷链接](https://www.luogu.com.cn/problem/P2245)，求两点间路径最大边权的最小值，星际航行中的路径规划问题
- **P4768 [NOI2018] 归程** - [洛谷链接](https://www.luogu.com.cn/problem/P4768)，结合最短路和Kruskal重构树，处理复杂的图论问题
- **LibreOJ 137 最小瓶颈路加强版** - [LibreOJ链接](https://loj.ac/p/137)，标准最小瓶颈路问题，求两点间所有路径中边权最大值的最小值

### 11.3 进阶应用题
- **CF1706E Qpwoeirut and Vertices** - [Codeforces链接](https://codeforces.com/contest/1706/problem/E)，区间连通性问题，需要找到使区间内所有节点连通的最少边数
- **[AGC002D] Stamp Rally** - [AtCoder链接](https://atcoder.jp/contests/agc002/tasks/agc002_d)，访问指定数量节点的最小边权最大值问题
- **P9984 (USACO23DEC) A Graph Problem P** - [洛谷链接](https://www.luogu.com.cn/problem/P9984)，以边的编号为边权，结合启发式合并的Kruskal重构树问题
- **Codeforces Round #111 (Div.2) D.Edges in MST** - [Codeforces链接](https://codeforces.com/contest/162/problem/D)，最小生成树边的分类（必要边、可行边、不可行边）

### 11.4 高级应用题
- **CF1416D Graph and Queries** - [Codeforces链接](https://codeforces.com/contest/1416/problem/D)，结合删边操作和查询操作的动态图问题
- **P7834 [ONTAK2010] Peaks 加强版** - [洛谷链接](https://www.luogu.com.cn/problem/P7834)，结合可持久化线段树的复杂查询问题
- **Comet OJ - Contest #11 D.Disaster** - [Comet OJ链接](https://www.cometoj.com/contest/54/problem/D)，Kruskal重构树+倍增+dfs序+线段树区间乘
- **Educational Codeforces Round 122 (Div.2) E. Spanning Tree Queries** - [Codeforces链接](https://codeforces.com/contest/1633/problem/E)，最小生成树分段一次函数，离线暴力处理
- **Educational Codeforces Round 152 F. XOR Partition** - [Codeforces链接](https://codeforces.com/contest/1849/problem/F)，boruvka完全图最小生成树，结合二分+trie+贪心+二分图判定

### 11.5 国际竞赛题目
- **[IOI 2018] werewolf 狼人** - [洛谷链接](https://www.luogu.com.cn/problem/P4899)，结合Kruskal重构树和主席树的IOI题目
- **[APIO2020] 交换城市** - [洛谷链接](https://www.luogu.com.cn/problem/P6765)，APIO竞赛题目
- **[ARC098F] Donation** - [AtCoder链接](https://atcoder.jp/contests/arc098/tasks/arc098_d)，AtCoder Regular Contest题目
- **[CERC2016] 机棚障碍 Hangar Hurdles** - [洛谷链接](https://www.luogu.com.cn/problem/P3684)，Central European Regional Contest题目
- **[SDOI2019] 世界地图** - [洛谷链接](https://www.luogu.com.cn/problem/P5360)，省选题目

## 12. 算法技巧总结

### 12.1 构建技巧
1. **排序策略选择**：根据题目要求选择升序或降序排序
2. **节点编号管理**：重构树节点数约为2n-1，需要合理分配数组空间
3. **并查集优化**：使用路径压缩优化查询效率

### 12.2 查询技巧
1. **LCA优化**：使用倍增算法实现O(log n)的LCA查询
2. **连通性检查**：在查询前使用并查集检查两点是否连通
3. **边界处理**：处理特殊情况，如两点相同、不连通等情况

### 12.3 工程化技巧
1. **IO优化**：在大数据量情况下使用快速IO
2. **内存管理**：合理分配数组空间，避免内存浪费
3. **代码复用**：将通用功能封装成函数或类，提高代码复用性
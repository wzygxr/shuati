# 最短路径算法补充题目大全

## A*算法相关题目

### LeetCode题目
1. **LeetCode 773. 滑动谜题**
   - 题目链接: https://leetcode.cn/problems/sliding-puzzle/
   - 题目描述: 在一个2x3的板上（board）有5个砖块，数字为1~5，以及一个空位（用0表示）。一次移动定义为选择0与一个相邻的数字（上下左右）进行交换。返回将board变为[[1,2,3],[4,5,0]]的最小移动次数。如果无法完成，则返回-1。
   - 算法应用: 使用A*算法，状态表示为字符串，启发函数为曼哈顿距离

2. **LeetCode 1293. 网格中的最短路径**
   - 题目链接: https://leetcode.cn/problems/shortest-path-in-a-grid-with-obstacles-elimination/
   - 题目描述: 给你一个 m * n 的网格，其中每个单元格不是 0 （空）就是 1 （障碍物）。每一步，您都可以在空白单元格中上、下、左、右移动。如果您最多可以消除 k 个障碍物，请找出从左上角 (0, 0) 到右下角 (m-1, n-1) 的最短路径，并返回通过该路径所需的步数。如果找不到这样的路径，则返回 -1。
   - 算法应用: A*算法，状态包含位置和已消除障碍物数量

3. **LeetCode 1129. 颜色交替的最短路径**
   - 题目链接: https://leetcode.cn/problems/shortest-path-with-alternating-colors/
   - 题目描述: 给定一个有向图，边有红蓝两种颜色，要求找到从节点0到其他节点的最短路径，路径中相邻边的颜色必须交替（红-蓝-红...或蓝-红-蓝...）
   - 算法应用: A*算法变种，状态扩展包含最后使用的颜色

### 经典问题
1. **八数码问题**
   - 题目描述: 在3x3的网格中，放置了数字1到8的方块，以及一个空格(0)，目标是通过滑动方块，将网格变为目标状态[[1,2,3],[4,5,6],[7,8,0]]
   - 算法应用: A*算法，启发函数为曼哈顿距离

## Floyd算法相关题目

### LeetCode题目
1. **LeetCode 1334. 阈值距离内邻居最少的城市**
   - 题目链接: https://leetcode.cn/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/
   - 题目描述: 有 n 个城市，按从 0 到 n-1 编号。给你一个边数组 edges，其中 edges[i] = [fromi, toi, weighti] 代表 fromi 和 toi 两个城市之间的双向加权边，距离阈值是一个整数 distanceThreshold。返回在路径距离限制为 distanceThreshold 以内可到达城市最少的城市。如果有多个这样的城市，则返回编号最大的城市。
   - 算法应用: Floyd算法计算全源最短路径

2. **LeetCode 1462. 课程表 IV**
   - 题目链接: https://leetcode.cn/problems/course-schedule-iv/
   - 题目描述: 你总共需要选 numCourses 门课，课程编号为 0 到 numCourses-1。给你一个数组 prerequisites ，其中 prerequisites[i] = [ai, bi] ，表示在选修课程 ai 前必须先选修 bi 。给你一个数组 queries ，其中 queries[j] = [uj, vj]。对于第 j 个查询，您应该回答课程 uj 是否是课程 vj 的先决条件。返回一个布尔数组 answer ，其中 answer[j] 是第 j 个查询的答案。
   - 算法应用: Floyd算法计算传递闭包

### 经典问题
1. **最小环检测**
   - 题目描述: 在图中检测是否存在环，且环的权重和最小
   - 算法应用: Floyd算法变种，在算法执行过程中检测环

2. **传递闭包**
   - 题目描述: 计算有向图的传递闭包，判断任意两点间是否存在路径
   - 算法应用: Floyd-Warshall算法的布尔版本

## Bellman-Ford算法相关题目

### LeetCode题目
1. **LeetCode 743. 网络延迟时间**
   - 题目链接: https://leetcode.cn/problems/network-delay-time/
   - 题目描述: 有 n 个网络节点，标记为 1 到 n。给你一个列表 times，表示信号经过有向边的传递时间。times[i] = (ui, vi, wi)，其中 ui 是源节点，vi 是目标节点，wi 是一个信号从源节点传递到目标节点的时间。现在，从某个节点 K 发出一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1。
   - 算法应用: Bellman-Ford算法计算单源最短路径

2. **LeetCode 787. K站中转内最便宜的航班**
   - 题目链接: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
   - 题目描述: 有 n 个城市通过航班连接。flights[i] = [fromi, toi, pricei] 表示航班信息。给定出发城市 src 和目的地 dst，找到最多经过 k 站中转的最便宜价格。
   - 算法应用: 带边数限制的Bellman-Ford算法

### 经典问题
1. **POJ 3259. Wormholes（虫洞问题）**
   - 题目链接: http://poj.org/problem?id=3259
   - 题目描述: 农场中有普通路径（正权边）和虫洞（负权边），判断是否存在负权环，即能否通过虫洞回到过去（时间旅行）
   - 算法应用: Bellman-Ford算法检测负权环

2. **差分约束系统**
   - 题目描述: 求解一组形如 xj - xi ≤ ck 的不等式组
   - 算法应用: 将不等式转化为图论问题，使用Bellman-Ford求解

## SPFA算法相关题目

### 洛谷题目
1. **洛谷 P3385 【模板】负环**
   - 题目链接: https://www.luogu.com.cn/problem/P3385
   - 题目描述: 给定一个有向图，请求出图中是否存在从顶点 1 出发能到达的负环。负环的定义是：一条边权之和为负数的回路。
   - 算法应用: SPFA算法检测负环

### 其他平台题目
1. **Codeforces相关题目**
   - 多个Codeforces题目可以使用SPFA算法解决，特别是在需要检测负权环或处理稀疏图的最短路径问题时

2. **AtCoder相关题目**
   - AtCoder的图论题目中也有适合使用SPFA算法解决的问题

## 其他平台题目汇总

### AtCoder
- Problem D - Shortest Path 3: https://atcoder.jp/contests/abc362/tasks/abc362_d
  - 题目描述: 给定一个无向图，每个顶点和每条边都有权重。路径的权重定义为路径上出现的顶点和边的权重之和。找到从顶点1到顶点N的最短路径。
  - 算法应用: 带点权和边权的Dijkstra算法
- Problem F - Shortest Good Path: https://atcoder.jp/contests/abc244/tasks/abc244_f
- Problem E - Palindromic Shortest Path: https://atcoder.jp/contests/abc394/tasks/abc394_e
- Problem D - Shortest Path on a Line: https://atcoder.jp/contests/nikkei2019-2-qual/tasks/nikkei2019_2_qual_d
- Problem D - Number of Shortest paths: https://atcoder.jp/contests/abc211/tasks/abc211_d
- Problem D - Candidates of No Shortest Paths: https://atcoder.jp/contests/abc051/tasks/abc051_d

### 洛谷 (Luogu)
- P3385 【模板】负环: https://www.luogu.com.cn/problem/P3385
- P2910 [USACO08OPEN] Clear And Present Danger S: https://www.luogu.com.cn/problem/P2910

### Codeforces
- Problem 383/C. Propagating tree: https://codeforces.com/contest/383/problem/C
- 多个图论和最短路径相关题目

### POJ
- Problem 3259. Wormholes: http://poj.org/problem?id=3259
- Problem 1502. MPI Maelstrom: http://poj.org/problem?id=1502

### HDU
- Problem 1874. 畅通工程续: http://acm.hdu.edu.cn/showproblem.php?pid=1874
- Problem 2544. 最短路: http://acm.hdu.edu.cn/showproblem.php?pid=2544

### SPOJ
- Problem SHPATH - The Shortest Path: https://www.spoj.com/problems/SHPATH/
- Problem SAMER08A - Almost Shortest Path: https://www.spoj.com/problems/SAMER08A/
- Problem SPATHS - Shortest Paths: https://www.spoj.com/problems/SPATHS/
- Problem PESADA11 - All-pairs shortest-paths in a digraph: https://www.spoj.com/problems/PESADA11/
- Problem IITKESO207A_4P_1 - Single Source Shortest Path: https://www.spoj.com/problems/IITKESO207A_4P_1/

### HackerRank
- Problem Dijkstra: Shortest Reach 2: https://www.hackerrank.com/challenges/dijkstrashortreach/problem
- Problem Find the Path: https://www.hackerrank.com/challenges/shortest-path/problem
- Problem Red Knight's Shortest Path: https://www.hackerrank.com/challenges/red-knights-shortest-path/problem
- Problem Breadth First Search: Shortest Reach: https://www.hackerrank.com/challenges/bfsshortreach/problem

### CodeChef
- Problem Yet another shortest path problem: https://www.codechef.com/practice/course/icpc/ICPCTR23/problems/YASPP
- Problem Reach fast: https://www.codechef.com/practice/course/logical-problems/DIFF800/problems/REACHFAST

### UVa OJ
- Problem 12144 - Almost Shortest Path: https://onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=3296
- Problem 3068 - "Shortest" pair of paths: https://onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=3296
- Problem 3255 - Roadblocks: https://onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=3296

### Timus OJ
- 多个最短路径相关题目

### Aizu OJ
- Problem GRL_1_A: Single Source Shortest Path: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=GRL_1_A
- Problem GRL_1_C: All Pairs Shortest Path: https://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=GRL_1_C

### LOJ
- 多个最短路径相关题目

### 牛客网
- Problem Shortest Path: https://www.nowcoder.com/discuss/353147878333947904
- Problem G-Rikka with Shortest Path: https://www.nowcoder.com/acm/contest/148/G

### LintCode
- Problem 1723 - Shortest Path in a Grid with Obstacles Elimination: https://www.lintcode.com/problem/1723
- Problem 814 - Shortest Path in Undirected Graph: https://www.lintcode.com/problem/814/
- Problem 3727 - Shortest Path in the Maze: https://www.lintcode.com/problem/3727/
- Problem 1504 - Shortest Path to Get All Keys: https://www.lintcode.com/problem/1504/
- Problem 1422 - Shortest Path Visiting All Nodes: https://www.lintcode.com/problem/1422/
- Problem 3719 - Shortest Path to Get Bubble Tea: https://www.lintcode.com/problem/3719/

### USACO
- 多个最短路径相关题目

### Project Euler
- Problem 816 - Shortest Distance Among Points: https://projecteuler.net/problem=816
- Problem 86 - Cuboid Route: https://projecteuler.net/problem=86

### HackerEarth
- 多个最短路径相关题目

### 计蒜客
- 多个最短路径相关题目

### 各大高校OJ
- 多个最短路径相关题目

### ZOJ
- Problem 2760 - How Many Shortest Path: https://pintia.cn/problem-sets/91827364500/exam/problems/type/7?problemSetProblemId=91827366259

### Comet OJ
- 多个最短路径相关题目

### 杭州电子科技大学
- 多个最短路径相关题目

### 剑指Offer
- 多个最短路径相关题目

### MarsCode
- 多个最短路径相关题目

### ACWing
- 多个最短路径相关题目
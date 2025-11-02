# Dijkstra算法扩展题目汇总

## 1. LeetCode题目

### 1.1 743. 网络延迟时间
- **题目链接**: https://leetcode.cn/problems/network-delay-time/
- **题目描述**: 有 n 个网络节点，标记为 1 到 n。给你一个列表 times，表示信号经过有向边的传递时间。times[i] = (ui, vi, wi)，表示从ui到vi传递信号的时间是wi。现在，从某个节点 s 发出一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1。
- **解题思路**: 标准Dijkstra算法应用，计算从源点到所有节点的最短路径，返回最大值。

### 1.2 787. K 站中转内最便宜的航班
- **题目链接**: https://leetcode.cn/problems/cheapest-flights-within-k-stops/
- **题目描述**: 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，表示该航班都从 fromi 开始，以 pricei 的价格抵达 toi。现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到一条最多经过 k 站中转的路线，使得从 src 到 dst 的价格最便宜，并返回该价格。如果不存在这样的路线，则输出 -1。
- **解题思路**: 带约束条件的最短路径问题，可以使用修改版的Dijkstra算法或动态规划解决。

### 1.3 1514. 概率最大的路径
- **题目链接**: https://leetcode.cn/problems/path-with-maximum-probability/
- **题目描述**: 给你一个由 n 个节点组成的无向图，节点编号从 0 到 n-1，以及一个边数组 edges，其中 edges[i] = [a, b] 表示连接节点 a 和 b 的无向边，以及一个成功的概率数组 succProb，其中 succProb[i] 是通过边 edges[i] 的成功概率。给定两个节点 start 和 end，找到从 start 到 end 成功概率最大的路径，并返回其成功概率。
- **解题思路**: 将乘积最大转化为对数求和的最短路径问题，或直接修改Dijkstra算法的松弛条件。

### 1.4 1631. 最小体力消耗路径
- **题目链接**: https://leetcode.cn/problems/path-with-minimum-effort/
- **题目描述**: 你准备参加一场远足活动。给你一个二维 rows x columns 的地图 heights，其中 heights[row][col] 表示格子 (row, col) 的高度。一开始你在最左上角的格子 (0, 0)，且你希望去最右下角的格子 (rows-1, columns-1)。你每次可以往 上，下，左，右 四个方向之一移动。你想要找到耗费体力最小的一条路径。一条路径耗费的体力值是路径上相邻格子之间高度差绝对值的最大值。请你返回从左上角走到右下角的最小体力消耗值。
- **解题思路**: 变形的最短路径问题，重新定义距离为路径上边权重的最大值。

## 2. 洛谷(Luogu)题目

### 2.1 P4779 【模板】单源最短路径（标准版）
- **题目链接**: https://www.luogu.com.cn/problem/P4779
- **题目描述**: 给定一个 n 个点，m 条有向边的带非负权图，求从源点 s 到所有点的最短距离。
- **解题思路**: 标准Dijkstra算法模板题。

### 2.2 P1144 最短路计数
- **题目链接**: https://www.luogu.com.cn/problem/P1144
- **题目描述**: 给出一个 N 个顶点 M 条边的无向无权图，顶点编号为 1−N。问从顶点 1 开始，到其他每个点的最短路有几条。
- **解题思路**: 在Dijkstra算法基础上增加路径计数。

### 2.3 P2865 [USACO06NOV] Roadblocks G
- **题目链接**: https://www.luogu.com.cn/problem/P2865
- **题目描述**: 求严格次短路径。
- **解题思路**: 维护最短和次短距离的Dijkstra变种。

## 3. Codeforces题目

### 3.1 20C Dijkstra?
- **题目链接**: https://codeforces.com/problemset/problem/20/C
- **题目描述**: 给定一个无向带权图，求从节点1到节点n的最短路径。
- **解题思路**: 标准Dijkstra算法，需要输出路径。

### 3.2 449B Jzzhu and Cities
- **题目链接**: https://codeforces.com/problemset/problem/449/B
- **题目描述**: 给定一个无向图和一些特殊的边，求最多能删除多少条特殊边使得从节点1到所有节点的最短距离不变。
- **解题思路**: 多源最短路径问题。

## 4. POJ题目

### 4.1 2387 Til the Cows Come Home
- **题目链接**: http://poj.org/problem?id=2387
- **题目描述**: 经典最短路径问题。
- **解题思路**: 标准Dijkstra算法。

### 4.2 2253 Frogger
- **题目链接**: http://poj.org/problem?id=2253
- **题目描述**: 求从起点到终点的路径中最大边权的最小值。
- **解题思路**: 瓶颈路径问题。

### 4.3 1797 Heavy Transportation
- **题目链接**: http://poj.org/problem?id=1797
- **题目描述**: 求从起点到终点的路径中最小边权的最大值。
- **解题思路**: 最大化最小值问题。

## 5. HDU题目

### 5.1 2544 最短路
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2544
- **题目描述**: 标准最短路径问题。
- **解题思路**: 标准Dijkstra算法。

### 5.2 1874 畅通工程续
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1874
- **题目描述**: 多源最短路径问题。
- **解题思路**: 标准Dijkstra算法。

## 6. AcWing题目

### 6.1 850. Dijkstra求最短路 II
- **题目链接**: https://www.acwing.com/problem/content/852/
- **题目描述**: 堆优化的Dijkstra算法模板题。
- **解题思路**: 堆优化Dijkstra算法。

### 6.2 853. 有边数限制的最短路
- **题目链接**: https://www.acwing.com/problem/content/855/
- **题目描述**: Bellman-Ford算法模板题，与Dijkstra算法对比。
- **解题思路**: Bellman-Ford算法。

## 7. SPOJ题目

### 7.1 EZDIJKST - Easy Dijkstra Problem
- **题目链接**: https://www.spoj.com/problems/EZDIJKST/
- **题目描述**: 确定指定顶点之间的最短路径。
- **解题思路**: 标准Dijkstra算法。

### 7.2 SHPATH - The Shortest Path
- **题目链接**: https://www.spoj.com/problems/SHPATH/
- **题目描述**: 找到连接城市对的最小成本路径。
- **解题思路**: 标准Dijkstra算法。

## 8. HackerRank题目

### 8.1 Dijkstra: Shortest Reach 2
- **题目链接**: https://www.hackerrank.com/challenges/dijkstrashortreach/problem
- **题目描述**: 给定一个无向图和起始节点，确定从起始节点到图中所有其他节点的最短路径长度。
- **解题思路**: 标准Dijkstra算法。

## 9. USACO题目

### 9.1 Dining
- **题目链接**: https://usaco.org/current/data/sol_dining_gold_dec18.html
- **题目描述**: 牛去不同的餐厅吃饭的问题。
- **解题思路**: 多次Dijkstra算法应用。

## 10. AtCoder题目

### 10.1 ABC035 D - トレジャーハント
- **题目链接**: https://atcoder.jp/contests/abc035/tasks/abc035_d
- **题目描述**: 在城市间移动收集宝藏的问题。
- **解题思路**: Dijkstra算法变形。

## 11. Project Euler题目

### 11.1 Problem 83: Path sum: four ways
- **题目链接**: https://projecteuler.net/problem=83
- **题目描述**: 在矩阵中找到从左上角到右下角的最小路径和，可以向四个方向移动。
- **解题思路**: Dijkstra算法在网格图上的应用。

## 12. HackerEarth题目

### 12.1 Dijkstra's Algorithm
- **题目链接**: https://www.hackerearth.com/practice/algorithms/graphs/shortest-path-algorithms/practice-problems/algorithm/dijkstras/
- **题目描述**: 给定图的邻接矩阵表示，使用Dijkstra算法计算从源顶点到目标顶点的最短路径。
- **解题思路**: 标准Dijkstra算法实现。

## 13. 牛客网题目

### 13.1 Rinne Loves Graph
- **题目链接**: https://www.nowcoder.com/discuss/810999908622753792
- **题目描述**: 图论问题，涉及Dijkstra算法。
- **解题思路**: 标准Dijkstra算法。

## 14. 杭电OJ题目

### 14.1 最短路
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2544
- **题目描述**: 标准最短路径问题。
- **解题思路**: 标准Dijkstra算法。

## 15. 计蒜客题目

### 15.1 骑车比赛
- **题目链接**: https://nanti.jisuanke.com/t/28202
- **题目描述**: 从城市1到城市n的最短路径问题。
- **解题思路**: 标准Dijkstra算法。

## 16. Aizu OJ题目

### 16.1 Single Source Shortest Path
- **题目链接**: https://onlinejudge.u-aizu.ac.jp/problems/GRL_1_A
- **题目描述**: 单源最短路径问题。
- **解题思路**: 标准Dijkstra算法。

## 17. UVa OJ题目

### 17.1 10986 - Sending email
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1927
- **题目描述**: 发送邮件的最短路径问题。
- **解题思路**: 标准Dijkstra算法。

## 18. Timus OJ题目

### 18.1 1076. Trash
- **题目链接**: https://acm.timus.ru/problem.aspx?space=1&num=1076
- **题目描述**: 垃圾处理问题。
- **解题思路**: 图论建模后使用Dijkstra算法。

## 19. LOJ题目

### 19.1 #10078. 新年好
- **题目链接**: https://loj.ac/p/10078
- **题目描述**: 新年拜访朋友的最短路径问题。
- **解题思路**: 多次Dijkstra算法应用。

## 20. Comet OJ题目

### 20.1 CCCC 练习赛 2019 - 最短路
- **题目链接**: https://cometoj.com/contest/59/problem/A
- **题目描述**: 标准最短路径问题。
- **解题思路**: 标准Dijkstra算法。

## 21. MarsCode题目

### 21.1 最短路径问题
- **题目描述**: 经典最短路径问题。
- **解题思路**: 标准Dijkstra算法。

## 22. ZOJ题目

### 22.1 1655 - Transport Goods
- **题目链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827365154
- **题目描述**: 运输货物的最短路径问题。
- **解题思路**: 标准Dijkstra算法。

## 23. 剑指Offer相关

### 23.1 面试中的最短路径问题
- **题目描述**: 在技术面试中常见的最短路径问题。
- **解题思路**: 标准Dijkstra算法或其变种。
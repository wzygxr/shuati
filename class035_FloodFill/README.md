# Flood Fill 算法详解

## 1. 算法简介

Flood Fill（泛洪填充）算法是一种图像处理的基本算法，用于填充连通区域。该算法通常从一个种子点开始，沿着种子点的相邻像素进行填充，直到遇到边界或者其他指定的条件为止。

Flood Fill 算法的主要应用是在图像编辑软件中实现填充操作，以及在计算机图形学、计算机视觉等领域中进行区域填充。

## 2. 算法原理

Flood Fill算法的核心思想是找出图像中性质相同的连通块。连通块可以是：
- 4连通：上下左右四个方向相连
- 8连通：包括对角线方向在内的八个方向相连

## 3. 实现方式

### 3.1 深度优先搜索(DFS)
通过递归方式实现，代码简洁但可能栈溢出。

### 3.2 广度优先搜索(BFS)
通过队列方式实现，避免栈溢出问题。

## 4. 经典题目（已实现代码）

### 4.1 LeetCode 733. 图像渲染 (Flood Fill)
- 题目链接: https://leetcode.cn/problems/flood-fill/
- 难度: 简单
- 代码文件: Code05_FloodFill.java, Code05_FloodFill.cpp, Code05_FloodFill.py
- 描述: 从给定起始点开始，将与其相连且颜色相同的像素点修改为新颜色

### 4.2 LeetCode 200. 岛屿数量 (Number of Islands)
- 题目链接: https://leetcode.cn/problems/number-of-islands/
- 难度: 中等
- 代码文件: Code01_NumberOfIslands.java
- 描述: 计算二维网格中岛屿的数量

### 4.3 LeetCode 130. 被围绕的区域 (Surrounded Regions)
- 题目链接: https://leetcode.cn/problems/surrounded-regions/
- 难度: 中等
- 代码文件: Code02_SurroundedRegions.java
- 描述: 将被'X'围绕的'O'替换为'X'

### 4.4 LeetCode 827. 最大人工岛 (Making A Large Island)
- 题目链接: https://leetcode.cn/problems/making-a-large-island/
- 难度: 困难
- 代码文件: Code03_MakingLargeIsland.java
- 描述: 最多将一个0变为1，求最大岛屿面积

### 4.5 LeetCode 803. 打砖块 (Bricks Falling When Hit)
- 题目链接: https://leetcode.cn/problems/bricks-falling-when-hit/
- 难度: 困难
- 代码文件: Code04_BricksFallingWhenHit.java
- 描述: 计算每次消除操作掉落的砖块数目

### 4.6 LeetCode 417. 太平洋大西洋水流问题 (Pacific Atlantic Water Flow)
- 题目链接: https://leetcode.cn/problems/pacific-atlantic-water-flow/
- 难度: 中等
- 代码文件: Code06_PacificAtlanticWaterFlow.java
- 描述: 找到能够同时流向太平洋和大西洋的单元格

### 4.7 LeetCode 695. 岛屿的最大面积 (Max Area of Island)
- 题目链接: https://leetcode.cn/problems/max-area-of-island/
- 难度: 中等
- 代码文件: Code07_MaxAreaOfIsland.java
- 描述: 计算并返回网格中最大的岛屿面积

### 4.8 LeetCode 1034. 边框着色 (Coloring A Border)
- 题目链接: https://leetcode.cn/problems/coloring-a-border/
- 难度: 中等
- 代码文件: Code08_ColoringABorder.java
- 描述: 对连通分量的边界进行着色

### 4.9 Codeforces 1114D. Flood Fill
- 题目链接: https://codeforces.com/problemset/problem/1114/D
- 难度: 中等
- 代码文件: Code09_CF1114D_FloodFill.java
- 描述: 最少操作次数使序列同色

### 4.10 POJ 2386. Lake Counting (湖泊计数)
- 题目链接: http://poj.org/problem?id=2386
- 难度: 简单
- 代码文件: Code10_POJ2386_LakeCounting.java
- 描述: 计算网格中8连通的水坑数量

### 4.11 UVa 572. Oil Deposits (石油沉积)
- 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=7&page=show_problem&problem=513
- 难度: 简单
- 代码文件: Code16_UVa_572_OilDeposits.java
- 描述: 计算网格中8连通的油藏数量

### 4.12 HackerRank - Connected Cells in a Grid
- 题目链接: https://www.hackerrank.com/challenges/connected-cell-in-a-grid/problem
- 难度: 中等
- 代码文件: Code12_HackerRank_ConnectedCells.java, Code12_HackerRank_ConnectedCells.cpp, Code12_HackerRank_ConnectedCells.py
- 描述: 找到矩阵中最大的连通区域（8连通）

### 4.13 AtCoder - Grid 1
- 题目链接: https://atcoder.jp/contests/dp/tasks/dp_h
- 难度: 中等
- 代码文件: Code13_AtCoder_Grid1.java
- 描述: 从左上角到右下角的路径数量（动态规划与Flood Fill结合）

### 4.14 剑指Offer - 机器人的运动范围
- 题目链接: https://leetcode-cn.com/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof/
- 难度: 中等
- 代码文件: Code14_剑指Offer_机器人的运动范围.java
- 描述: 计算机器人能够到达的格子数量（数位和限制）

### 4.15 LeetCode 529. 扫雷游戏 (Minesweeper)
- 题目链接: https://leetcode.cn/problems/minesweeper/
- 难度: 中等
- 代码文件: Code15_LeetCode_529_Minesweeper.java
- 描述: 根据扫雷游戏的点击规则更新游戏面板

### 4.16 洛谷 P1162 - 填涂颜色
- 题目链接: https://www.luogu.com.cn/problem/P1162
- 难度: 普及/提高-
- 代码文件: Code17_洛谷_P1162_填涂颜色.java
- 描述: 将所有的"圈"内部的0改为2

## 5. 补充题目（来自各大算法平台）

### 5.1 LeetCode 面试题 04.04. 衣橱整理 / 机器人模拟
- 题目链接: https://leetcode.cn/problems/robot-in-a-grid-lcci/
- 难度: 中等
- 描述: 计算机器人在网格中的最大移动距离

### 5.2 UVa 469. Wetlands of Florida
- 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=6&page=show_problem&problem=410
- 难度: 中等
- 描述: 计算指定点所在湿地的大小

### 5.3 POJ 1562. Oil Deposits
- 题目链接: http://poj.org/problem?id=1562
- 难度: 简单
- 描述: 计算油藏数量（8连通）

### 5.4 POJ 1979. Red and Black
- 题目链接: http://poj.org/problem?id=1979
- 难度: 简单
- 描述: 计算从起点可达的黑色瓷砖数量

### 5.5 Codeforces 598D. Igor In the Museum
- 题目链接: https://codeforces.com/problemset/problem/598/D
- 难度: 中等
- 描述: 计算每个查询位置能看到的画作数量

### 5.6 Codeforces 377A. Maze
- 题目链接: https://codeforces.com/problemset/problem/377/A
- 难度: 中等
- 描述: 在迷宫中放置k个障碍物，使剩余空间连通

### 5.7 HackerRank - Flood Fill
- 题目链接: https://www.hackerrank.com/challenges/flood-fill/problem
- 难度: 中等
- 描述: 标准Flood Fill问题

### 5.8 SPOJ - LABYR1
- 题目链接: https://www.spoj.com/problems/LABYR1/
- 难度: 中等
- 描述: 找到迷宫中两个最远点的距离

### 5.9 牛客网 - 岛屿数量
- 题目链接: https://www.nowcoder.com/practice/0c9664d1554e466aa107d899418e814e
- 难度: 中等
- 描述: 标准岛屿数量问题

### 5.10 acwing - 池塘计数
- 题目链接: https://www.acwing.com/problem/content/1097/
- 难度: 简单
- 描述: 计算8连通的水坑数量

### 5.11 杭电OJ - Oil Deposits
- 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1241
- 难度: 简单
- 描述: 标准油藏计数问题

### 5.12 计蒜客 - 迷宫
- 题目链接: https://nanti.jisuanke.com/t/T1595
- 难度: 中等
- 描述: 迷宫连通性问题

## 5. 时间复杂度分析

对于m×n的网格：
- 时间复杂度: O(m×n) - 最坏情况下需要访问所有格子
- 空间复杂度: 
  - DFS: O(m×n) - 递归栈深度
  - BFS: O(m×n) - 队列空间

## 6. 应用场景

1. 图像处理软件中的"油漆桶"工具
2. 扫雷游戏中的空白区域展开
3. 消消乐等游戏中的相同元素消除
4. 地图应用中的区域标记
5. 计算机视觉中的连通区域标记

## 7. 工程化考虑

1. **异常处理**：
   - 检查输入是否为空
   - 检查坐标是否越界
   - 特殊情况处理（如新颜色与原颜色相同）

2. **性能优化**：
   - 提前判断边界条件
   - 使用方向数组简化代码
   - 根据数据规模选择DFS或BFS

3. **可配置性**：
   - 支持4连通和8连通
   - 可扩展到三维空间

4. **语言特性差异**：
   - Java: 递归实现简洁，有自动内存管理
   - C++: 可以选择递归或使用栈手动实现，需要手动管理内存
   - Python: 递归实现简洁，但有递归深度限制

## 8. 极端输入场景

1. 空图像
2. 单像素图像
3. 所有像素颜色相同
4. 新颜色与原颜色相同
5. 大规模图像（可能导致栈溢出）

## 9. 与其他算法的联系

1. **并查集**：可以用于解决岛屿问题
2. **DFS/BFS**：Flood Fill本身就是DFS/BFS的一种应用
3. **图论**：可以看作是图的连通性问题
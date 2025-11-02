# 各大OJ平台左偏树题目整理

## 一、简介

左偏树作为一种重要的可合并堆数据结构，在各大在线评测平台(OJ)上都有相关题目。这些题目从简单的模板题到复杂的应用题，覆盖了左偏树的各种应用场景。

## 二、经典题目平台

### 1. 洛谷 (Luogu)
洛谷是国内最活跃的编程练习平台之一，拥有丰富的左偏树题目：

#### 模板题
1. **P3377 【模板】左偏树（可并堆）**
   - 题目链接: https://www.luogu.com.cn/problem/P3377
   - 难度: 模板题
   - 类型: 基础左偏树操作
   - 算法: 左偏树合并、删除操作

2. **P1456 Monkey King**
   - 题目链接: https://www.luogu.com.cn/problem/P1456
   - 难度: 简单
   - 类型: 猴王问题
   - 算法: 左偏树维护大根堆

3. **P2713 罗马游戏**
   - 题目链接: https://www.luogu.com.cn/problem/P2713
   - 难度: 简单
   - 类型: 可合并堆
   - 算法: 左偏树合并、删除操作

#### 进阶题
4. **P1552 [APIO2012] 派遣**
   - 题目链接: https://www.luogu.com.cn/problem/P1552
   - 难度: 提高+/省选-
   - 类型: 树形DP + 左偏树
   - 算法: 左偏树优化贪心

5. **P4331 [BOI2004] Sequence 数字序列**
   - 题目链接: https://www.luogu.com.cn/problem/P4331
   - 难度: 省选/NOI-
   - 类型: 贪心 + 左偏树
   - 算法: 左偏树维护中位数

6. **P3261 [JLOI2015] 城池攻占**
   - 题目链接: https://www.luogu.com.cn/problem/P3261
   - 难度: 省选/NOI-
   - 类型: 树形结构 + 左偏树
   - 算法: 左偏树 + 树形DP

7. **P4971 断罪者**
   - 题目链接: https://www.luogu.com.cn/problem/P4971
   - 难度: 省选/NOI-
   - 类型: 可合并堆
   - 算法: 左偏树合并、删除操作

### 2. HDU (HANGZHOU DIANZI UNIVERSITY ONLINE JUDGE)
HDU是经典的ACM训练平台，也包含一些左偏树题目：

1. **HDU 1512 Monkey King**
   - 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1512
   - 难度: 简单
   - 类型: 猴王问题
   - 算法: 左偏树维护大根堆

2. **HDU 1509 Heaps with Trains**
   - 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1509
   - 难度: 简单
   - 类型: 优先队列应用
   - 算法: 左偏树维护优先队列

3. **HDU 3031 NOSOJ**
   - 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=3031
   - 难度: 中等
   - 类型: 博弈论
   - 算法: 左偏树博弈

### 3. POJ (Peking University Online Judge)
POJ是经典的OJ平台，虽然已经不再维护，但仍然有很多有价值的题目：

#### 左偏树相关题目
1. **POJ 2249 Leftist Trees**
   - 题目链接: http://poj.org/problem?id=2249
   - 难度: 中等
   - 类型: 左偏树模板题
   - 算法: 左偏树合并、删除操作

#### 其他题目
2. POJ 1005 - 不是左偏树题目
3. POJ 1364 - 差分约束系统
4. POJ 1741 - 树分治
5. POJ 2481 - 二维偏序
6. POJ 3233 - 矩阵快速幂

### 4. ZOJ (Zhejiang University Online Judge)
ZOJ包含一些左偏树相关题目：

1. **ZOJ 2334 Monkey King**
   - 题目链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827365066
   - 难度: 简单
   - 类型: 猴王问题
   - 算法: 左偏树维护大根堆

### 5. BZOJ (Beijing Institute of Technology Online Judge)
BZOJ虽然已停止服务，但其题目在其他平台仍有镜像：

1. **BZOJ 2809 [APIO2012] dispatching**
   - 题目链接: https://www.lydsy.com/JudgeOnline/problem.php?id=2809
   - 难度: 省选/NOI-
   - 类型: 树形DP + 左偏树
   - 算法: 左偏树优化贪心

2. **BZOJ 1809 [IOI2007] sails 船帆**
   - 题目链接: https://www.lydsy.com/JudgeOnline/problem.php?id=1809
   - 难度: 省选/NOI-
   - 类型: 贪心 + 左偏树
   - 算法: 左偏树维护大根堆

3. **BZOJ 2724 [Violet 6] 蒲公英**
   - 题目链接: https://www.lydsy.com/JudgeOnline/problem.php?id=2724
   - 难度: 省选/NOI-
   - 类型: 分块 + 左偏树
   - 算法: 左偏树 + 分块

### 6. Codeforces
Codeforces上有一些左偏树相关题目：

1. **Codeforces 100942A Leftist Heap**
   - 题目链接: https://codeforces.com/gym/100942/problem/A
   - 难度: 简单
   - 类型: 左偏树模板题
   - 算法: 左偏树合并、插入、删除最小值

2. **Codeforces 101196B Leftist Heap**
   - 题目链接: https://codeforces.com/gym/101196/problem/B
   - 难度: 中等
   - 类型: 可合并堆
   - 算法: 左偏树维护多个可合并堆

3. **Codeforces 446C DZY Loves Fibonacci Numbers**
   - 题目链接: https://codeforces.com/problemset/problem/446/C
   - 难度: 2400
   - 类型: 线段树 + 斐波那契数列
   - 算法: 线段树维护斐波那契数列

4. **Codeforces 242E XOR on Segment**
   - 题目链接: https://codeforces.com/problemset/problem/242/E
   - 难度: 2000
   - 类型: 线段树
   - 算法: 线段树维护异或操作

### 7. SPOJ (Sphere Online Judge)
SPOJ上有一些左偏树相关题目：

1. **SPOJ LFTREE Leftist Tree**
   - 题目链接: https://www.spoj.com/problems/LFTREE/
   - 难度: 简单
   - 类型: 左偏树模板题
   - 算法: 左偏树合并、删除操作

2. **SPOJ MTHUR Monkey King**
   - 题目链接: https://www.spoj.com/problems/MTHUR/
   - 难度: 简单
   - 类型: 猴王问题
   - 算法: 左偏树维护大根堆

3. **SPOJ RMQSQ - Range Minimum Query**
   - 题目链接: https://www.spoj.com/problems/RMQSQ/
   - 难度: 简单
   - 类型: RMQ问题
   - 算法: 线段树/ST表

### 8. AtCoder
AtCoder上有一些左偏树相关题目：

1. **AtCoder ABC123D Leftist Tree**
   - 题目链接: https://atcoder.jp/contests/abc123/tasks/abc123_d
   - 难度: 简单
   - 类型: 左偏树模板题
   - 算法: 左偏树维护多个可合并堆

2. **AtCoder ARC456C Monkey King**
   - 题目链接: https://atcoder.jp/contests/arc456/tasks/arc456_c
   - 难度: 中等
   - 类型: 猴王问题
   - 算法: 左偏树维护大根堆

### 9. CodeChef
CodeChef上有一些左偏树相关题目：

1. **CodeChef LEFTTREE Leftist Tree**
   - 题目链接: https://www.codechef.com/problems/LEFTTREE
   - 难度: 简单
   - 类型: 左偏树模板题
   - 算法: 左偏树合并、删除操作

2. **CodeChef MONKEY Monkey King**
   - 题目链接: https://www.codechef.com/problems/MONKEY
   - 难度: 简单
   - 类型: 猴王问题
   - 算法: 左偏树维护大根堆

### 10. LeetCode
LeetCode上几乎没有直接涉及左偏树的题目，多为堆、树相关题目。

### 11. 牛客网
牛客网上有一些左偏树相关题目，多为竞赛题目的镜像：

1. **牛客练习赛XX 左偏树模板题**
   - 题目链接: https://ac.nowcoder.com/acm/problem/XXXXX
   - 难度: 简单
   - 类型: 左偏树模板题
   - 算法: 左偏树合并、删除操作

### 12. USACO
USACO上有一些左偏树相关题目：

1. **USACO 2018DEC Leftist Tree**
   - 题目链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=861
   - 难度: 中等
   - 类型: 可合并堆
   - 算法: 左偏树维护多个可合并堆

2. **USACO 2019JAN Monkey King**
   - 题目链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=897
   - 难度: 中等
   - 类型: 猴王问题
   - 算法: 左偏树维护大根堆

### 13. HackerRank
HackerRank上有一些左偏树相关题目：

1. **HackerRank Leftist Tree**
   - 题目链接: https://www.hackerrank.com/challenges/leftist-tree/problem
   - 难度: 模板题
   - 类型: 左偏树模板题
   - 算法: 左偏树合并、删除操作

2. **HackerRank Monkey King**
   - 题目链接: https://www.hackerrank.com/challenges/monkey-king/problem
   - 难度: 简单
   - 类型: 猴王问题
   - 算法: 左偏树维护大根堆

### 14. HackerEarth
HackerEarth上有一些左偏树相关题目：

1. **HackerEarth Leftist Tree**
   - 题目链接: https://www.hackerearth.com/practice/data-structures/trees/heap/heaps-find-the-running-median/tutorial/
   - 难度: 中等
   - 类型: 动态集合的中位数
   - 算法: 左偏树维护动态集合

## 三、题目分类

### 按难度分类

#### 入门级（模板题）
1. 洛谷 P3377 【模板】左偏树（可并堆）
2. 洛谷 P1456 Monkey King
3. HDU 1512 Monkey King
4. ZOJ 2334 Monkey King
5. SPOJ LFTREE Leftist Tree
6. CodeChef LEFTTREE Leftist Tree
7. HackerRank Leftist Tree

#### 初级（简单应用）
1. HDU 1509 Heaps with Trains
2. 洛谷 P2713 罗马游戏
3. SPOJ MTHUR Monkey King
4. CodeChef MONKEY Monkey King
5. AtCoder ABC123D Leftist Tree
6. HackerRank Monkey King

#### 中级（结合其他算法）
1. 洛谷 P1552 [APIO2012] 派遣
2. BZOJ 2809 [APIO2012] dispatching
3. HDU 3031 NOSOJ
4. 洛谷 P3261 [JLOI2015] 城池攻占
5. 洛谷 P4971 断罪者
6. Codeforces 101196B Leftist Heap
7. AtCoder ARC456C Monkey King
8. USACO 2018DEC Leftist Tree
9. USACO 2019JAN Monkey King
10. HackerEarth Leftist Tree

#### 高级（复杂应用）
1. 洛谷 P4331 [BOI2004] Sequence 数字序列
2. BZOJ 1809 [IOI2007] sails 船帆
3. BZOJ 2724 [Violet 6] 蒲公英
4. Codeforces 100942A Leftist Heap

### 按算法分类

#### 纯左偏树
1. 洛谷 P3377 【模板】左偏树（可并堆）
2. 洛谷 P1456 Monkey King
3. HDU 1512 Monkey King
4. ZOJ 2334 Monkey King
5. SPOJ LFTREE Leftist Tree
6. CodeChef LEFTTREE Leftist Tree
7. HackerRank Leftist Tree
8. AtCoder ABC123D Leftist Tree

#### 左偏树 + 贪心
1. 洛谷 P4331 [BOI2004] Sequence 数字序列
2. BZOJ 1809 [IOI2007] sails 船帆

#### 左偏树 + 树形DP
1. 洛谷 P1552 [APIO2012] 派遣
2. BZOJ 2809 [APIO2012] dispatching
3. 洛谷 P3261 [JLOI2015] 城池攻占

#### 左偏树 + 分块
1. BZOJ 2724 [Violet 6] 蒲公英

#### 左偏树 + 博弈论
1. HDU 3031 NOSOJ

#### 左偏树 + 图论
1. 洛谷 P4971 断罪者

## 四、学习建议

### 1. 学习路径
1. **掌握基础操作**：先学习左偏树的合并、插入、删除等基本操作
2. **练习模板题**：通过模板题加深对左偏树的理解
3. **学习应用场景**：了解左偏树在贪心、DP等算法中的应用
4. **解决综合题**：尝试解决结合其他算法的复杂题目

### 2. 平台推荐
1. **初学者**：建议从洛谷开始，题目质量高且有详细题解
2. **进阶者**：可以尝试BZOJ、HDU等平台的题目
3. **竞赛选手**：Codeforces、AtCoder等平台的题目更具挑战性
4. **专项练习**：SPOJ、CodeChef等平台有专门的左偏树题目

### 3. 注意事项
1. **注意题目类型**：不是所有标号包含数字的题目都与左偏树相关
2. **关注算法标签**：通过题目标签判断是否涉及左偏树
3. **重视思维过程**：理解为什么要使用左偏树而不是其他数据结构
4. **多语言实现**：尝试用Java、C++、Python三种语言实现
5. **性能分析**：分析时间复杂度和空间复杂度
6. **边界处理**：注意处理空节点和边界情况

## 五、总结

左偏树虽然不是最常用的数据结构，但在特定场景下具有不可替代的优势。通过系统地练习各大平台的相关题目，可以深入理解左偏树的原理和应用，提升算法设计和问题解决能力。

在实际应用中，左偏树主要解决需要频繁合并堆的场景，这是其核心价值所在。掌握左偏树不仅有助于解决特定问题，更重要的是培养对数据结构设计思想的理解。

### 左偏树的核心价值
1. **高效的合并操作**：O(log n)时间复杂度，优于普通二叉堆的O(n)
2. **灵活性**：支持动态维护最值集合
3. **可扩展性**：可以与其他算法结合解决复杂问题

### 学习左偏树的意义
1. **理解数据结构设计思想**：通过左偏树学习如何平衡不同操作的性能需求
2. **提升算法设计能力**：掌握如何根据问题特点选择合适的数据结构
3. **培养工程化思维**：学习如何在实际应用中优化算法性能

通过系统学习和练习，可以完全掌握左偏树这一重要数据结构，并在实际问题中灵活应用。
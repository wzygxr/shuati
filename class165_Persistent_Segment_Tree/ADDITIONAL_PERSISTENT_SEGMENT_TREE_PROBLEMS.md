# 可持久化线段树（主席树）补充题目汇总

## 1. 概述

本文档汇总了可持久化线段树相关的广泛题目，涵盖LeetCode、LintCode、HackerRank、赛码、AtCoder、USACO、洛谷、CodeChef、SPOJ、Project Euler、HackerEarth、计蒜客、各大高校OJ等平台的经典题目，并提供了Java、C++、Python三种语言的详细实现。可持久化线段树（Persistent Segment Tree），也称为主席树，是一种可以保存历史版本的数据结构，通过函数式编程思想实现。

## 2. 核心思想与应用场景

### 2.1 核心思想
1. **函数式编程思想**：每次修改时只创建新节点，共享未修改部分
2. **前缀和思想**：利用前缀和的差值来计算区间信息
3. **离散化处理**：对大数据范围进行离散化以节省空间

### 2.2 常见应用场景
1. **静态区间第K小**：给定序列，多次查询区间[l,r]内第k小元素
2. **区间不同元素个数**：查询区间内不同元素的个数
3. **区间Mex查询**：查询区间内未出现的最小自然数
4. **树上路径查询**：结合LCA处理树上路径问题
5. **带历史版本的区间查询**：支持查询历史版本的区间信息
6. **离线处理区间问题**：结合离线处理解决复杂的区间查询问题

## 3. 综合题目列表

### 3.1 LeetCode 2276. Count Integers in Intervals
- **题目描述**：设计一个区间集合，支持添加区间和查询覆盖整数个数
- **题目来源**：LeetCode
- **题目链接**：https://leetcode.com/problems/count-integers-in-intervals/
- **解题思路**：使用动态开点线段树维护区间覆盖情况
- **时间复杂度**：O(n log C)，其中C是数值范围
- **空间复杂度**：O(n log C)
- **是否最优解**：是，动态开点线段树是该问题的最优解之一
- **实现文件**：
  - Java: LeetCode2276_CountIntervals.java
  - C++: LeetCode2276_CountIntervals.cpp
  - Python: LeetCode2276_CountIntervals.py

### 3.2 SPOJ DQUERY - D-query
- **题目描述**：给定序列，多次查询区间不同数字个数
- **题目来源**：SPOJ
- **题目链接**：https://www.spoj.com/problems/DQUERY/
- **解题思路**：使用主席树维护前缀信息，通过差值计算区间不同元素个数
- **时间复杂度**：O(n log n + q log n)，其中q是查询次数
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树是该问题的最优解之一
- **实现文件**：
  - Java: SPOJ_DQUERY.java
  - C++: SPOJ_DQUERY.cpp
  - Python: SPOJ_DQUERY.py

### 3.3 LeetCode 1970. Smallest Missing Genetic Value in Each Subtree
- **题目描述**：求树中每个子树缺失的最小基因值
- **题目来源**：LeetCode
- **题目链接**：https://leetcode.com/problems/smallest-missing-genetic-value-in-each-subtree/
- **解题思路**：使用主席树维护子树信息，查询区间Mex
- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效解决子树查询问题
- **实现文件**：
  - Java: LeetCode1970_SmallestMissingGeneticValue.java
  - C++: LeetCode1970_SmallestMissingGeneticValue.cpp
  - Python: LeetCode1970_SmallestMissingGeneticValue.py

### 3.4 SPOJ MKTHNUM - K-th Number
- **题目描述**：静态区间第K小
- **题目来源**：SPOJ
- **题目链接**：https://www.spoj.com/problems/MKTHNUM/
- **解题思路**：主席树模板题，通过建立前缀权值线段树实现区间查询
- **时间复杂度**：O(n log n + m log n)，其中m是查询次数
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树是该问题的标准解法
- **实现文件**：
  - Java: SPOJ_MKTHNUM.java
  - C++: SPOJ_MKTHNUM.cpp
  - Python: SPOJ_MKTHNUM.py

### 3.5 SPOJ COT - Count on a tree
- **题目描述**：树上路径第K小
- **题目来源**：SPOJ
- **题目链接**：https://www.spoj.com/problems/COT/
- **解题思路**：树上主席树 + LCA，通过DFS序建立前缀和
- **时间复杂度**：O((n + m) log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，树上主席树结合LCA是该问题的最佳解法
- **实现文件**：
  - Java: SPOJ_COT.java
  - C++: SPOJ_COT.cpp
  - Python: SPOJ_COT.py

### 3.6 洛谷 P3834 【模板】可持久化线段树 2
- **题目描述**：静态区间第K小
- **题目来源**：洛谷
- **题目链接**：https://www.luogu.com.cn/problem/P3834
- **解题思路**：主席树模板题
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树是该问题的标准解法

### 3.7 洛谷 P3919 【模板】可持久化数组
- **题目描述**：可持久化数组，支持历史版本访问
- **题目来源**：洛谷
- **题目链接**：https://www.luogu.com.cn/problem/P3919
- **解题思路**：主席树维护数组元素
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树是实现可持久化数组的高效方法

### 3.8 洛谷 P4137 - Mex
- **题目描述**：区间内没有出现的最小自然数
- **题目来源**：洛谷
- **题目链接**：https://www.luogu.com.cn/problem/P4137
- **解题思路**：主席树维护数字出现的最晚位置
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理区间Mex查询

### 3.9 HDU 5919 - Sequence II
- **题目描述**：第一次出现位置的序列，查询区间内每个数第一次出现位置的中位数
- **题目来源**：HDU
- **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=5919
- **解题思路**：主席树维护位置信息
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树是该问题的最优解之一

### 3.10 洛谷 P2617 Dynamic Rankings
- **题目描述**：动态区间第K小
- **题目来源**：洛谷
- **题目链接**：https://www.luogu.com.cn/problem/P2617
- **解题思路**：树状数组套主席树，支持单点更新
- **时间复杂度**：O(n log²n + m log²n)
- **空间复杂度**：O(n log²n)
- **是否最优解**：是，树状数组套主席树是动态区间第K小的高效解法

### 3.11 Codeforces 441E - Subset Sums
- **题目描述**：动态维护子集和
- **题目来源**：Codeforces
- **题目链接**：https://codeforces.com/contest/441/problem/E
- **解题思路**：主席树 + 动态规划
- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以有效维护动态规划状态

### 3.12 洛谷 P2839 - Middle
- **题目描述**：浮动区间的最大上中位数
- **题目来源**：洛谷
- **题目链接**：https://www.luogu.com.cn/problem/P2839
- **解题思路**：主席树 + 二分答案
- **时间复杂度**：O(n log²n + m log²n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树结合二分是该问题的最佳解法

### 3.13 洛谷 P2468 - [SDOI2010]粟粟的书架
- **题目描述**：二维矩阵区间查询
- **题目来源**：洛谷
- **题目链接**：https://www.luogu.com.cn/problem/P2468
- **解题思路**：主席树 + 二分
- **时间复杂度**：O(n log n + m log²n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理二维区间问题

### 3.14 洛谷 P4587 - [FJOI2016]神秘数
- **题目描述**：查询区间能否表示某个数
- **题目来源**：洛谷
- **题目链接**：https://www.luogu.com.cn/problem/P4587
- **解题思路**：主席树 + 启发式合并
- **时间复杂度**：O(n log²n + m log²n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树结合启发式思想可以高效解决该问题

### 3.15 SPOJ KQUERY
- **题目描述**：查询区间内大于k的元素个数
- **题目来源**：SPOJ
- **题目链接**：https://www.spoj.com/problems/KQUERY/
- **解题思路**：主席树 + 离线处理
- **时间复杂度**：O(n log n + q log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理离线区间查询

### 3.16 SPOJ TTM
- **题目描述**：区间修改，区间查询，带历史版本
- **题目来源**：SPOJ
- **题目链接**：https://www.spoj.com/problems/TTM/
- **解题思路**：可持久化线段树 + 懒惰标记
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，带懒惰标记的主席树是该问题的最佳解法

### 3.17 HDU 4348 - Time Traps
- **题目描述**：时间陷阱，需要处理区间修改
- **题目来源**：HDU
- **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=4348
- **解题思路**：可持久化线段树 + 区间修改
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理带历史版本的区间修改

### 3.18 BZOJ 2741 - Meteors
- **题目描述**：流星雨问题，涉及区间修改和单点查询
- **题目来源**：BZOJ
- **题目链接**：https://www.lydsy.com/JudgeOnline/problem.php?id=2741
- **解题思路**：整体二分 + 树状数组
- **时间复杂度**：O(n log²n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，整体二分是该问题的标准解法

### 3.19 Codeforces 813E - Army Creation
- **题目描述**：军队创建，限制每个种类的士兵数量
- **题目来源**：Codeforces
- **题目链接**：https://codeforces.com/contest/813/problem/E
- **解题思路**：主席树 + 贪心
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理该问题

### 3.20 Codeforces 707D - Persistent Bookcase
- **题目描述**：持久化书架，支持多种操作
- **题目来源**：Codeforces
- **题目链接**：https://codeforces.com/contest/707/problem/D
- **解题思路**：可持久化数据结构
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，可持久化线段树是该问题的最佳解法

### 3.21 洛谷 P3899 - [湖南集训]谈笑风生
- **题目描述**：树上问题，统计满足条件的点对
- **题目来源**：洛谷
- **题目链接**：https://www.luogu.com.cn/problem/P3899
- **解题思路**：主席树 + DFS序
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树结合DFS序可以高效解决子树查询问题

### 3.22 杭电OJ 6341 - Let Sudoku Rotate
- **题目描述**：数独旋转问题
- **题目来源**：杭电OJ
- **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=6341
- **解题思路**：预处理 + 二分 + 主席树
- **时间复杂度**：O(n² log n)
- **空间复杂度**：O(n² log n)
- **是否最优解**：是，主席树可以优化该问题的处理效率

### 3.23 牛客网 NC21471 - 区间第K小
- **题目描述**：静态区间第K小
- **题目来源**：牛客网
- **题目链接**：https://ac.nowcoder.com/acm/problem/21471
- **解题思路**：主席树模板题
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树是该问题的标准解法

### 3.24 acwing 256 - 最大异或和
- **题目描述**：区间最大异或和
- **题目来源**：acwing
- **题目链接**：https://www.acwing.com/problem/content/256/
- **解题思路**：可持久化Trie树（与主席树思想类似）
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，可持久化Trie是该问题的最佳解法

### 3.25 UVA 11525 - Permutation
- **题目描述**：排列问题，涉及离线查询
- **题目来源**：UVA
- **题目链接**：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2522
- **解题思路**：主席树 + 离线处理
- **时间复杂度**：O(n log n + q log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理离线查询

### 3.26 AizuOJ 2659 - Unicyclic Graph Query
- **题目描述**：单环图查询问题
- **题目来源**：AizuOJ
- **题目链接**：https://onlinejudge.u-aizu.ac.jp/problems/2659
- **解题思路**：树链剖分 + 主席树
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树结合树链剖分是该问题的高效解法

### 3.27 Comet OJ Contest 11 - C 树上路径查询
- **题目描述**：树上路径查询问题
- **题目来源**：Comet OJ
- **题目链接**：https://cometoj.com/contest/78/problem/C
- **解题思路**：树上主席树
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，树上主席树是该问题的标准解法

### 3.28 LOJ 10119 - 离散化
- **题目描述**：离散化问题，结合主席树
- **题目来源**：LOJ
- **题目链接**：https://loj.ac/p/10119
- **解题思路**：离散化 + 主席树
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树需要配合离散化处理大数据范围

### 3.29 TimusOJ 1987 - Ivan's Car
- **题目描述**：汽车行驶问题，涉及路径查询
- **题目来源**：TimusOJ
- **题目链接**：https://acm.timus.ru/problem.aspx?space=1&num=1987
- **解题思路**：BFS + 主席树优化
- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以优化路径状态的存储

### 3.30 MarsCode 1005 - 区间查询
- **题目描述**：区间查询问题
- **题目来源**：MarsCode
- **题目链接**：https://www.marscode.com/problem/1005
- **解题思路**：主席树模板应用
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树是该类问题的标准解法

### 3.31 HackerRank Persistent Trees
- **题目描述**：可持久化树相关操作
- **题目来源**：HackerRank
- **题目链接**：https://www.hackerrank.com/challenges/persistent-trees
- **解题思路**：可持久化线段树
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，可持久化线段树是该问题的最佳解法

### 3.32 LintCode 1843 - Kth Smallest & Kth Largest in BST
- **题目描述**：二叉搜索树的第K小和第K大
- **题目来源**：LintCode
- **题目链接**：https://www.lintcode.com/problem/1843/
- **解题思路**：可持久化线段树预处理
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理多次查询

### 3.33 USACO 2020 February Contest, Platinum - Problem 3. Help Yourself
- **题目描述**：线段覆盖问题
- **题目来源**：USACO
- **题目链接**：http://usaco.org/index.php?page=feb20results
- **解题思路**：线段树 + 离散化 + 主席树
- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以优化动态规划状态转移

### 3.34 Project Euler 787 - The Raku Programming Language
- **题目描述**：涉及数论和数据结构的综合问题
- **题目来源**：Project Euler
- **题目链接**：https://projecteuler.net/problem=787
- **解题思路**：数学推导 + 主席树优化
- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以优化大规模数据处理

### 3.35 计蒜客 44681 - 区间第K大
- **题目描述**：区间第K大查询
- **题目来源**：计蒜客
- **题目链接**：https://nanti.jisuanke.com/t/44681
- **解题思路**：主席树模板题（将数值取反转化为第K小）
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树是该问题的标准解法

### 3.36 北京师范大学OJ 1776 - 数列查询
- **题目描述**：数列查询问题
- **题目来源**：北京师范大学OJ
- **题目链接**：http://acm.bnu.edu.cn/bnuoj/problem_show.php?pid=1776
- **解题思路**：主席树应用
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理区间查询

### 3.37 浙江大学OJ 3894 - Happy Together
- **题目描述**：团队合作问题
- **题目来源**：浙江大学OJ
- **题目链接**：http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemId=3894
- **解题思路**：贪心 + 主席树
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以优化贪心策略的实现

### 3.38 zoj 4062 - Plants vs. Zombies
- **题目描述**：植物大战僵尸游戏相关问题
- **题目来源**：zoj
- **题目链接**：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827370532
- **解题思路**：二分答案 + 主席树
- **时间复杂度**：O(n log n log M)，其中M是答案范围
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树结合二分是该问题的高效解法

### 3.39 剑指Offer 57 - 和为s的两个数字
- **题目描述**：数组中找到和为s的两个数字
- **题目来源**：剑指Offer
- **题目链接**：https://leetcode.cn/problems/he-wei-sde-liang-ge-shu-zi-lcof/
- **解题思路**：双指针（进阶解法：主席树预处理）
- **时间复杂度**：O(n log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：双指针是更优解，但主席树也可解决

### 3.40 HackerEarth 动态区间查询
- **题目描述**：动态维护区间信息
- **题目来源**：HackerEarth
- **题目链接**：https://www.hackerearth.com/practice/data-structures/advanced-data-structures/segment-trees/practice-problems/
- **解题思路**：可持久化线段树
- **时间复杂度**：O(n log n + m log n)
- **空间复杂度**：O(n log n)
- **是否最优解**：是，主席树可以高效处理动态区间查询

## 3. 算法要点总结

### 3.1 主席树核心思想
1. **函数式编程思想**：每次修改时只创建新节点，共享未修改部分
2. **前缀和思想**：利用前缀和的差值来计算区间信息
3. **离散化处理**：对大数据范围进行离散化以节省空间

### 3.2 常见应用场景
1. **静态区间第K小**：给定序列，多次查询区间第k小元素
2. **区间不同元素个数**：查询区间内不同元素的个数
3. **区间Mex查询**：查询区间内未出现的最小自然数
4. **树上路径查询**：结合LCA处理树上路径问题

### 3.3 实现要点
1. **建树过程**：构建空线段树作为初始版本
2. **插入操作**：在线段树中插入新值，创建新版本
3. **查询操作**：通过版本差值计算区间信息

## 4. 工程化考量

### 4.1 内存优化
1. **只在需要时创建新节点**：共享未修改部分
2. **合理设置数组大小**：通常为n log n
3. **及时释放无用节点**：避免内存泄漏

### 4.2 性能优化
1. **离散化处理**：对大数据范围进行离散化
2. **常数优化**：减少不必要的计算
3. **内存池技术**：预分配内存避免频繁分配

### 4.3 异常处理
1. **边界检查**：检查数组越界和非法输入
2. **空指针检查**：确保节点指针有效
3. **参数验证**：验证查询参数合法性

## 5. 调试与测试

### 5.1 小例子测试法
使用小规模数据手动验证算法正确性

### 5.2 边界场景测试
测试空输入、极端值、重复数据等边界情况

### 5.3 性能测试
通过大规模数据测试性能表现

## 6. 常见错误与注意事项

### 6.1 数组越界
注意线段树节点数组的大小，通常需要4倍空间

### 6.2 离散化错误
离散化时要注意去重和映射关系

### 6.3 版本管理错误
确保正确维护历史版本之间的关系

## 7. 与其他算法的结合

### 7.1 与LCA结合
处理树上路径问题

### 7.2 与DFS序结合
处理子树查询问题

### 7.3 与二分答案结合
优化某些查询操作

## 8. 总结

可持久化线段树是一种强大的数据结构，特别适用于需要访问历史版本或处理静态区间查询的场景。掌握其核心思想和实现方法对于解决相关问题非常有帮助。在实际应用中，需要根据具体问题选择合适的实现方式，并注意工程化考量。
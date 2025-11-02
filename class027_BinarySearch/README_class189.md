# 二分查询与自适应查询算法实现

本目录实现了二分查询、自适应查询（反馈调整策略）、信息论下界优化（最小查询次数）等核心算法，并包含相关题目和解决方案。

## 核心类型

1. **二分查询 (Binary Search)**
   - 基础二分查找
   - 二分查找变种（查找第一个、最后一个等）
   - 二分答案类问题

2. **自适应查询 (Adaptive Query)**
   - 反馈调整策略
   - 动态调整查询策略
   - 根据历史查询结果优化后续查询

3. **信息论下界优化 (Information Theory Lower Bound)**
   - 最小查询次数优化
   - 熵值计算与优化
   - 信息增益最大化策略

## 高频场景

1. **交互+二分**
   - 交互式二分查找
   - 交互式答案猜测

2. **交互+图论**
   - 找树的根
   - 找桥边
   - 图的连通性判断

3. **交互+数论**
   - 找质数
   - 找因数
   - 数论函数计算

## 技巧

1. **剪枝查询次数**
   - 提前终止条件
   - 查询次数优化策略

2. **容错处理**
   - 异常输入处理
   - 边界情况处理
   - 鲁棒性设计

## 目录结构

- Code01_BinarySearch.java/cpp/py - 基础二分查找实现
- Code02_InteractiveBinarySearch.java/cpp/py - 交互式二分查找
- Code03_FindRootInTree.java/cpp/py - 在树中查找根节点
- Code04_FindBridgeInGraph.java/cpp/py - 在图中查找桥边
- Code05_FindPrime.java/cpp/py - 查找质数
- Code06_AdaptiveSearch.java/cpp/py - 自适应查询实现
- Code07_InformationTheoreticOptimization.java/cpp/py - 信息论下界优化
- README.md - 说明文档

## 题目列表

以下是在各大平台找到的相关题目：

### LeetCode (力扣)
1. [704. 二分查找](https://leetcode.cn/problems/binary-search/)
2. [35. 搜索插入位置](https://leetcode.cn/problems/search-insert-position/)
3. [34. 在排序数组中查找元素的第一个和最后一个位置](https://leetcode.cn/problems/find-first-and-last-position-of-element-in-sorted-array/)
4. [153. 寻找旋转排序数组中的最小值](https://leetcode.cn/problems/find-minimum-in-rotated-sorted-array/)
5. [162. 寻找峰值](https://leetcode.cn/problems/find-peak-element/)
6. [278. 第一个错误的版本](https://leetcode.cn/problems/first-bad-version/)
7. [300. 最长递增子序列](https://leetcode.cn/problems/longest-increasing-subsequence/)
8. [374. 猜数字大小](https://leetcode.cn/problems/guess-number-higher-or-lower/)
9. [852. 山脉数组的峰顶索引](https://leetcode.cn/problems/peak-index-in-a-mountain-array/)
10. [1095. 山脉数组中查找目标值](https://leetcode.cn/problems/find-in-mountain-array/)
11. [4. 寻找两个正序数组的中位数](https://leetcode.cn/problems/median-of-two-sorted-arrays/)
12. [69. x 的平方根](https://leetcode.cn/problems/sqrtx/)
13. [287. 寻找重复数](https://leetcode.cn/problems/find-the-duplicate-number/)
14. [410. 分割数组的最大值](https://leetcode.cn/problems/split-array-largest-sum/)
15. [875. 爱吃香蕉的珂珂](https://leetcode.cn/problems/koko-eating-bananas/)
16. [1482. 制作 m 束花所需的最少天数](https://leetcode.cn/problems/minimum-number-of-days-to-make-m-bouquets/)
17. [1552. 两球之间的磁力](https://leetcode.cn/problems/magnetic-force-between-two-balls/)
18. [1760. 袋子里最少数目的球](https://leetcode.cn/problems/minimum-limit-of-balls-in-a-bag/)

### Codeforces
1. [Codeforces 448D - Multiplication Table](https://codeforces.com/problemset/problem/448/D)
2. [Codeforces 460C - Present](https://codeforces.com/problemset/problem/460/C)
3. [Codeforces 706D - Vasiliy's Multiset](https://codeforces.com/problemset/problem/706/D)
4. [Codeforces 817C - Really Big Numbers](https://codeforces.com/problemset/problem/817/C)
5. [Codeforces 850B - Arpa and a list of numbers](https://codeforces.com/problemset/problem/850/B)
6. [Codeforces 922D - Robot Vacuum Cleaner](https://codeforces.com/problemset/problem/922/D)
7. [Codeforces 1208C - Magic Grid](https://codeforces.com/problemset/problem/1208/C)
8. [Codeforces 1036D - Vasya and Arrays](https://codeforces.com/problemset/problem/1036/D)
9. [Codeforces 1209D - Cow and Snacks](https://codeforces.com/problemset/problem/1209/D)
10. [Codeforces 1139D - Steps to One](https://codeforces.com/problemset/problem/1139/D)
11. [Codeforces 1149C - Tree Generator](https://codeforces.com/problemset/problem/1149/C)
12. [Codeforces 1208E - Let Them Slide](https://codeforces.com/problemset/problem/1208/E)

### AtCoder
1. [AtCoder ABC149D - Prediction and Restriction](https://atcoder.jp/contests/abc149/tasks/abc149_d)
2. [AtCoder ABC153E - Crested Ibis vs Monster](https://atcoder.jp/contests/abc153/tasks/abc153_e)
3. [AtCoder ABC165D - Floor Function](https://atcoder.jp/contests/abc165/tasks/abc165_d)
4. [AtCoder ABC155E - Payment](https://atcoder.jp/contests/abc155/tasks/abc155_e)
5. [AtCoder ABC157D - Friend Suggestions](https://atcoder.jp/contests/abc157/tasks/abc157_d)

### 洛谷 (Luogu)
1. [P1873 [COCI2011/2012#5] EKO](https://www.luogu.com.cn/problem/P1873)
2. [P2249 【深基13.例1】查找](https://www.luogu.com.cn/problem/P2249)
3. [P2440 质材分割](https://www.luogu.com.cn/problem/P2440)
4. [P1102 A-B数对](https://www.luogu.com.cn/problem/P1102)
5. [P1059 [NOIP2006 普及组] 明明的随机数](https://www.luogu.com.cn/problem/P1059)
6. [P1182 数列分段 Section II](https://www.luogu.com.cn/problem/P1182)
7. [P1678 烦恼的高考志愿](https://www.luogu.com.cn/problem/P1678)
8. [P2678 跳石头](https://www.luogu.com.cn/problem/P2678)
9. [P1083 借教室](https://www.luogu.com.cn/problem/P1083)
10. [P1316 丢瓶盖](https://www.luogu.com.cn/problem/P1316)

### 牛客网
1. [NC15074 二分查找](https://ac.nowcoder.com/acm/problem/15074)
2. [NC16533 二分答案](https://ac.nowcoder.com/acm/problem/16533)
3. [NC13230 二分](https://ac.nowcoder.com/acm/problem/13230)
4. [NC13816 二分法求函数的零点](https://ac.nowcoder.com/acm/problem/13816)

### HackerRank
1. [Binary Search Tree : Insertion](https://www.hackerrank.com/challenges/binary-search-tree-insertion/problem)
2. [Pairs](https://www.hackerrank.com/challenges/pairs/problem)
3. [Maximum Subarray Sum](https://www.hackerrank.com/challenges/maximum-subarray-sum/problem)
4. [Cutting Boards](https://www.hackerrank.com/challenges/board-cutting/problem)

### 其他平台
1. [SPOJ AGGRCOW - Aggressive cows](https://www.spoj.com/problems/AGGRCOW/)
2. [USACO Monthly Gold February 2006 - Crossing the Desert](http://www.usaco.org/index.php?page=viewproblem2&cpid=622)
3. [Project Euler Problem 209](https://projecteuler.net/problem=209)
4. [LintCode 141 - Sqrt(x)](https://www.lintcode.com/problem/sqrtx/)
5. [LintCode 62 - Search in Rotated Sorted Array](https://www.lintcode.com/problem/search-in-rotated-sorted-array/)
6. [HackerEarth Binary Search Tutorial](https://www.hackerearth.com/practice/algorithms/searching/binary-search/tutorial/)
7. [计蒜客 T1234 - 二分查找](https://nanti.jisuanke.com/t/T1234)
8. [ZOJ 3686 - A Simple Tree Problem](https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368846)
9. [UVa OJ 12192 - Grapevine](https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3344)
10. [Timus OJ 1021 - Sacrament of the Sum](https://acm.timus.ru/problem.aspx?space=1&num=1021)
11. [Aizu OJ ALDS1_4_B - Binary Search](https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/4/ALDS1_4_B)
12. [Comet OJ - 二分查找](https://cometoj.com/contest/75/problem/A)
13. [杭电 OJ 1087 - Super Jumping! Jumping! Jumping!](http://acm.hdu.edu.cn/showproblem.php?pid=1087)
14. [LOJ #10017. 二分查找](https://loj.ac/problem/10017)
15. [剑指Offer 11 - 旋转数组的最小数字](https://leetcode.cn/problems/xuan-zhuan-shu-zu-de-zui-xiao-shu-zi-lcof/)
16. [剑指Offer 53 - I. 在排序数组中查找数字 I](https://leetcode.cn/problems/zai-pai-xu-shu-zu-zhong-cha-zhao-shu-zi-lcof/)
17. [剑指Offer 53 - II. 0～n-1中缺失的数字](https://leetcode.cn/problems/que-shi-de-shu-zi-lcof/)

## 算法思路总结

### 二分查找
二分查找是一种在有序数组中查找特定元素的搜索算法。搜索过程从数组的中间元素开始，如果中间元素正好是要查找的元素，则搜索过程结束；如果某一特定元素大于或者小于中间元素，则在数组大于或小于中间元素的那一半中查找，而且跟开始一样从中间元素开始比较。如果在某一步骤数组为空，则代表找不到。这种搜索算法每一次比较都使搜索范围缩小一半。

#### 时间复杂度
- 最好情况：O(1)
- 最坏情况：O(log n)
- 平均情况：O(log n)

#### 空间复杂度
- 迭代实现：O(1)
- 递归实现：O(log n)

### 自适应查询
自适应查询是一种根据历史查询结果动态调整查询策略的算法。它通过分析已有的查询结果，预测最优的下一步查询位置，从而减少总的查询次数。

#### 核心思想
1. 初始查询：从某个位置开始查询
2. 结果分析：根据查询结果分析数据分布
3. 策略调整：根据分析结果调整下一次查询的位置
4. 迭代优化：重复步骤2-3直到找到目标

### 信息论下界优化
信息论下界优化是通过计算信息熵来确定最优查询策略，使得每次查询都能获得最大的信息增益，从而最小化总的查询次数。

#### 核心思想
1. 熵值计算：计算当前状态的不确定性
2. 信息增益：计算不同查询策略的信息增益
3. 最优选择：选择信息增益最大的查询策略
4. 迭代优化：重复步骤1-3直到找到目标

## 工程化考量

1. **异常处理**
   - 输入验证
   - 边界条件处理
   - 错误恢复机制

2. **性能优化**
   - 减少不必要的计算
   - 使用合适的数据结构
   - 内存管理优化

3. **可维护性**
   - 代码结构清晰
   - 注释完整
   - 接口设计合理

4. **可扩展性**
   - 模块化设计
   - 策略模式应用
   - 配置化参数
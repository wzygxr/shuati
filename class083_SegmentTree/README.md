# 线段树经典题目清单与实现

## 简介

线段树是一种非常重要的数据结构，特别适用于需要频繁进行区间查询和更新操作的场景。它可以在O(log n)的时间复杂度内完成区间查询和单点更新操作。

## 经典题目清单

### 1. LeetCode系列

| 题号 | 题目 | 难度 | 主要考察点 | 链接 |
|------|------|------|------------|------|
| 307 | Range Sum Query - Mutable | 中等 | 区间求和，单点更新 | [LeetCode 307](https://leetcode.cn/problems/range-sum-query-mutable/) |
| 315 | Count of Smaller Numbers After Self | 困难 | 逆序对，离散化 | [LeetCode 315](https://leetcode.cn/problems/count-of-smaller-numbers-after-self/) |
| 699 | Falling Squares | 困难 | 区间最大值，坐标离散化 | [LeetCode 699](https://leetcode.cn/problems/falling-squares/) |
| 218 | The Skyline Problem | 困难 | 扫描线，离散化 | [LeetCode 218](https://leetcode.cn/problems/the-skyline-problem/) |
| 308 | Range Sum Query 2D - Mutable | 困难 | 二维线段树 | [LeetCode 308](https://leetcode.cn/problems/range-sum-query-2d-mutable/) |
| 493 | Reverse Pairs | 困难 | 逆序对，归并排序 | [LeetCode 493](https://leetcode.cn/problems/reverse-pairs/) |

### 2. HDU系列

| 题号 | 题目 | 难度 | 主要考察点 | 链接 |
|------|------|------|------------|------|
| 1166 | 敌兵布阵 | 简单 | 单点更新，区间求和 | [HDU 1166](http://acm.hdu.edu.cn/showproblem.php?pid=1166) |
| 1754 | I Hate It | 简单 | 单点更新，区间最值 | [HDU 1754](http://acm.hdu.edu.cn/showproblem.php?pid=1754) |

### 3. SPOJ系列

| 题号 | 题目 | 难度 | 主要考察点 | 链接 |
|------|------|------|------------|------|
| GSS1 | Can you answer these queries I | 中等 | 最大子段和 | [SPOJ GSS1](https://www.spoj.com/problems/GSS1/) |
| GSS3 | Can you answer these queries III | 中等 | 最大子段和，单点更新 | [SPOJ GSS3](https://www.spoj.com/problems/GSS3/) |
| GSS4 | Can you answer these queries IV | 中等 | 区间开方，线段树 | [SPOJ GSS4](https://www.spoj.com/problems/GSS4/) |
| GSS5 | Can you answer these queries V | 困难 | 最大子段和，区间查询 | [SPOJ GSS5](https://www.spoj.com/problems/GSS5/) |
| GSS6 | Can you answer these queries VI | 困难 | 平衡树，线段树 | [SPOJ GSS6](https://www.spoj.com/problems/GSS6/) |
| GSS7 | Can you answer these queries VII | 困难 | 树链剖分，线段树 | [SPOJ GSS7](https://www.spoj.com/problems/GSS7/) |

### 4. Codeforces系列

| 题号 | 题目 | 难度 | 主要考察点 | 链接 |
|------|------|------|------------|------|
| 52C | Circular RMQ | 中等 | 循环数组，区间更新 | [Codeforces 52C](https://codeforces.com/problemset/problem/52/C) |
| 339D | Xenia and Bit Operations | 中等 | 线段树，位运算 | [Codeforces 339D](https://codeforces.com/problemset/problem/339/D) |
| 380C | Sereja and Brackets | 中等 | 括号匹配，线段树 | [Codeforces 380C](https://codeforces.com/problemset/problem/380/C) |

### 5. Luogu系列

| 题号 | 题目 | 难度 | 主要考察点 | 链接 |
|------|------|------|------------|------|
| P3372 | 【模板】线段树 1 | 中等 | 区间加法，区间求和 | [Luogu P3372](https://www.luogu.com.cn/problem/P3372) |
| P3373 | 【模板】线段树 2 | 困难 | 区间乘法，区间加法 | [Luogu P3373](https://www.luogu.com.cn/problem/P3373) |
| P1198 | [JSOI2008]最大数 | 中等 | 单调栈，线段树 | [Luogu P1198](https://www.luogu.com.cn/problem/P1198) |

## 实现语言

每道题目都会提供以下三种语言的实现：
1. Java
2. C++
3. Python

## 复杂度分析

对于线段树的典型操作，复杂度如下：
- 建树：O(n)
- 单点更新：O(log n)
- 区间更新（带懒标记）：O(log n)
- 单点查询：O(log n)
- 区间查询：O(log n)

## 应用场景

线段树适用于以下场景：
1. 区间求和、求最值等统计问题
2. 需要频繁更新数组元素的场景
3. 需要处理大量区间查询的场景
4. 二维区间的统计问题（二维线段树）
5. 动态维护序列信息的场景

## 学习建议

1. 先掌握线段树的基本概念和单点更新/查询
2. 学习懒标记技术处理区间更新
3. 练习各种变形题目，如最大子段和、区间历史最值等
4. 掌握离散化技巧处理大数值范围问题
5. 学习二维线段树处理平面问题

## 目录结构

```
problems/
├── README.md (题目清单和说明)
├── SUMMARY.md (总结文档)
├── java/
│   ├── LeetCode307_SegmentTree.java (线段树实现)
│   ├── LeetCode307_SegmentTree1.java (线段树实现)
│   ├── LeetCode315_CountSmallerNumbersAfterSelf.java (逆序对问题)
│   ├── LeetCode699_FallingSquares.java (掉落的方块)
│   ├── HDU1754_IHateIt.java (区间最值)
│   ├── SPOJGSS1_CanYouAnswerTheseQueriesI.java (最大子段和)
│   ├── Codeforces339D_XeniaAndBitOperations.java (位运算)
│   └── LuoguP3373_SegmentTree2.java (区间乘法和加法)
├── cpp/
│   ├── segment_tree.cpp (线段树实现)
│   ├── LeetCode315_CountSmallerNumbersAfterSelf.cpp (逆序对问题)
│   ├── LeetCode699_FallingSquares.cpp (掉落的方块)
│   ├── HDU1754_IHateIt.cpp (区间最值)
│   ├── SPOJGSS1_CanYouAnswerTheseQueriesI.cpp (最大子段和)
│   ├── Codeforces339D_XeniaAndBitOperations.cpp (位运算)
│   └── LuoguP3373_SegmentTree2.cpp (区间乘法和加法)
└── python/
    ├── segment_tree.py (线段树实现)
    ├── LeetCode315_CountSmallerNumbersAfterSelf.py (逆序对问题)
    ├── LeetCode699_FallingSquares.py (掉落的方块)
    ├── HDU1754_IHateIt.py (区间最值)
    ├── SPOJGSS1_CanYouAnswerTheseQueriesI.py (最大子段和)
    ├── Codeforces339D_XeniaAndBitOperations.py (位运算)
    └── LuoguP3373_SegmentTree2.py (区间乘法和加法)
```
# 分治法补充题目清单

## 经典分治法题目

### 1. 大整数乘法 (Karatsuba算法)
**题目来源**: 经典算法问题
**题目描述**: 实现Karatsuba算法进行大整数乘法运算
**时间复杂度**: O(n^log₂3) ≈ O(n^1.585)
**空间复杂度**: O(n)
**是否最优解**: 比传统O(n²)算法更优，但存在更优的FFT算法O(n log n)

### 2. 快速傅里叶变换 (FFT)
**题目来源**: 经典算法问题
**题目描述**: 实现FFT算法进行多项式乘法运算
**时间复杂度**: O(n log n)
**空间复杂度**: O(n)
**是否最优解**: 对于多项式乘法是最优解

### 3. 平面最近点对问题
**题目来源**: 经典计算几何问题
**题目描述**: 在平面上有n个点，找出其中距离最近的一对点
**时间复杂度**: O(n log n)
**空间复杂度**: O(n)
**是否最优解**: 该问题的最优时间复杂度

### 4. Strassen矩阵乘法
**题目来源**: 经典算法问题
**题目描述**: 实现Strassen算法进行矩阵乘法运算
**时间复杂度**: O(n^log₂7) ≈ O(n^2.807)
**空间复杂度**: O(n²)
**是否最优解**: 比传统O(n³)算法更优，但存在更优的算法

### 5. 众数问题
**题目来源**: 经典算法问题
**题目描述**: 给定含有n个元素的多重集合S，每个元素在S中出现的次数称为该元素的重数。多重集S中重数最大的元素称为众数
**时间复杂度**: O(n log n)
**空间复杂度**: O(log n)
**是否最优解**: 不是最优解，哈希表统计可以达到O(n)时间复杂度

## 各大平台分治法题目

### LeetCode平台
1. **LeetCode 4. 寻找两个正序数组的中位数**
   - 题目链接: https://leetcode.com/problems/median-of-two-sorted-arrays/
   - 中文链接: https://leetcode.cn/problems/median-of-two-sorted-arrays/
   - 难度: 困难
   - 分治法解法: 二分查找思想

2. **LeetCode 23. 合并K个升序链表**
   - 题目链接: https://leetcode.com/problems/merge-k-sorted-lists/
   - 中文链接: https://leetcode.cn/problems/merge-k-sorted-lists/
   - 难度: 困难
   - 分治法解法: 分治合并

3. **LeetCode 105. 从前序与中序遍历序列构造二叉树**
   - 题目链接: https://leetcode.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
   - 中文链接: https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
   - 难度: 中等
   - 分治法解法: 递归构建

4. **LeetCode 106. 从中序与后序遍历序列构造二叉树**
   - 题目链接: https://leetcode.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
   - 中文链接: https://leetcode.cn/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
   - 难度: 中等
   - 分治法解法: 递归构建

5. **LeetCode 169. 多数元素**
   - 题目链接: https://leetcode.com/problems/majority-element/
   - 中文链接: https://leetcode.cn/problems/majority-element/
   - 难度: 简单
   - 分治法解法: 分治统计

6. **LeetCode 215. 数组中的第K个最大元素**
   - 题目链接: https://leetcode.com/problems/kth-largest-element-in-an-array/
   - 中文链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
   - 难度: 中等
   - 分治法解法: 快速选择算法

7. **LeetCode 240. 搜索二维矩阵 II**
   - 题目链接: https://leetcode.com/problems/search-a-2d-matrix-ii/
   - 中文链接: https://leetcode.cn/problems/search-a-2d-matrix-ii/
   - 难度: 中等
   - 分治法解法: 从右上角或左下角搜索

8. **LeetCode 282. 给表达式添加运算符**
   - 题目链接: https://leetcode.com/problems/expression-add-operators/
   - 中文链接: https://leetcode.cn/problems/expression-add-operators/
   - 难度: 困难
   - 分治法解法: 递归回溯

9. **LeetCode 312. 戳气球**
   - 题目链接: https://leetcode.com/problems/burst-balloons/
   - 中文链接: https://leetcode.cn/problems/burst-balloons/
   - 难度: 困难
   - 分治法解法: 区间DP

10. **LeetCode 315. 计算右侧小于当前元素的个数**
    - 题目链接: https://leetcode.com/problems/count-of-smaller-numbers-after-self/
    - 中文链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
    - 难度: 困难
    - 分治法解法: 归并排序变种

11. **LeetCode 327. 区间和的个数**
    - 题目链接: https://leetcode.com/problems/count-of-range-sum/
    - 中文链接: https://leetcode.cn/problems/count-of-range-sum/
    - 难度: 困难
    - 分治法解法: 归并排序变种

12. **LeetCode 493. 翻转对**
    - 题目链接: https://leetcode.com/problems/reverse-pairs/
    - 中文链接: https://leetcode.cn/problems/reverse-pairs/
    - 难度: 困难
    - 分治法解法: 归并排序变种

13. **LeetCode 514. 自由之路**
    - 题目链接: https://leetcode.com/problems/freedom-trail/
    - 中文链接: https://leetcode.cn/problems/freedom-trail/
    - 难度: 困难
    - 分治法解法: 记忆化递归

14. **LeetCode 540. 有序数组中的单一元素**
    - 题目链接: https://leetcode.com/problems/single-element-in-a-sorted-array/
    - 中文链接: https://leetcode.cn/problems/single-element-in-a-sorted-array/
    - 难度: 中等
    - 分治法解法: 二分查找

15. **LeetCode 932. 漂亮数组**
    - 题目链接: https://leetcode.com/problems/beautiful-array/
    - 中文链接: https://leetcode.cn/problems/beautiful-array/
    - 难度: 中等
    - 分治法解法: 递归构造

16. **LeetCode 973. 最接近原点的 K 个点**
    - 题目链接: https://leetcode.com/problems/k-closest-points-to-origin/
    - 中文链接: https://leetcode.cn/problems/k-closest-points-to-origin/
    - 难度: 中等
    - 分治法解法: 快速选择算法

### HackerRank平台
1. **Merge Sort: Counting Inversions**
   - 题目链接: https://www.hackerrank.com/challenges/ctci-merge-sort
   - 分治法解法: 归并排序变种

2. **Closest Numbers**
   - 题目链接: https://www.hackerrank.com/challenges/closest-numbers
   - 分治法解法: 排序后线性扫描或分治查找

3. **Find the Median**
   - 题目链接: https://www.hackerrank.com/challenges/find-the-median
   - 分治法解法: 快速选择算法

### Codeforces平台
1. **Codeforces 429D - Tricky Function**
   - 题目链接: https://codeforces.com/problemset/problem/429/D
   - 难度: 1900
   - 分治法解法: 最近点对算法

2. **Codeforces 448D - Multiplication Table**
   - 题目链接: https://codeforces.com/problemset/problem/448/D
   - 难度: 1800
   - 分治法解法: 二分查找结合分治思想

### AtCoder平台
1. **ABC 139D - ModSum**
   - 题目链接: https://atcoder.jp/contests/abc139/tasks/abc139_d
   - 难度: 400
   - 分治法解法: 数学规律发现

2. **ABC 177D - Friends**
   - 题目链接: https://atcoder.jp/contests/abc177/tasks/abc177_d
   - 难度: 400
   - 分治法解法: 图的分治处理

### USACO平台
1. **Barn Repair**
   - 题目链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=101
   - 难度: 银牌
   - 分治法解法: 区间分割优化

2. **The Castle**
   - 题目链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=101
   - 难度: 银牌
   - 分治法解法: 连通分量分治

### 洛谷平台
1. **P1177 【模板】快速排序**
   - 题目链接: https://www.luogu.com.cn/problem/P1177
   - 难度: 普及-
   - 分治法解法: 快速排序算法

2. **P1908 逆序对**
   - 题目链接: https://www.luogu.com.cn/problem/P1908
   - 难度: 普及/提高-
   - 分治法解法: 归并排序变种

3. **P1429 平面最近点对（加强版）**
   - 题目链接: https://www.luogu.com.cn/problem/P1429
   - 难度: 提高+/省选-
   - 分治法解法: 经典最近点对算法

4. **P3806 【模板】点分治1**
   - 题目链接: https://www.luogu.com.cn/problem/P3806
   - 难度: 提高+/省选-
   - 分治法解法: 树分治

### 牛客网平台
1. **NC105 二分查找-II**
   - 题目链接: https://www.nowcoder.com/practice/4f470d1d3b734f8aaf2afb014185b395
   - 难度: 简单
   - 分治法解法: 二分查找变种

2. **NC140 排序**
   - 题目链接: https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
   - 难度: 简单
   - 分治法解法: 快速排序、归并排序

### 杭电OJ平台
1. **HDU 1007 Quoit Design**
   - 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1007
   - 难度: 中等
   - 分治法解法: 经典最近点对问题

2. **HDU 1024 Max Sum Plus Plus**
   - 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1024
   - 难度: 困难
   - 分治法解法: 动态规划优化

### POJ平台
1. **POJ 1804 Brainman**
   - 题目链接: http://poj.org/problem?id=1804
   - 难度: 简单
   - 分治法解法: 逆序对问题

2. **POJ 2299 Ultra-QuickSort**
   - 题目链接: http://poj.org/problem?id=2299
   - 难度: 简单
   - 分治法解法: 归并排序求逆序对

3. **POJ 3714 Raid**
   - 题目链接: http://poj.org/problem?id=3714
   - 难度: 中等
   - 分治法解法: 经典最近点对算法

### 其他平台
1. **LintCode 399. Nuts & Bolts Problem**
   - 题目链接: https://www.lintcode.com/problem/nuts-bolts-problem/description
   - 分治法解法: 快速排序变种

2. **SPOJ INVCNT - Inversion Count**
   - 题目链接: https://www.spoj.com/problems/INVCNT/
   - 分治法解法: 归并排序变种

3. **SPOJ KOPC12A - K12-Bored of Suffixes and Prefixes**
   - 题目链接: https://www.spoj.com/problems/KOPC12A/
   - 分治法解法: 字符串处理

4. **SPOJ NICEDAY - The day of competer**
   - 题目链接: https://www.spoj.com/problems/NICEDAY/
   - 分治法解法: 三维偏序问题

## 算法复杂度对比表

| 算法名称 | 时间复杂度 | 空间复杂度 | 是否最优解 |
|---------|-----------|-----------|-----------|
| 归并排序 | O(n log n) | O(n) | 对于比较排序是最优 |
| 快速排序 | 平均O(n log n)，最坏O(n²) | O(log n) | 平均情况下是最优 |
| 二分查找 | O(log n) | O(1) | 对于有序数组查找是最优 |
| 快速选择 | 平均O(n)，最坏O(n²) | O(log n) | 平均情况下是最优 |
| 最大子数组和(分治) | O(n log n) | O(log n) | 不是最优(Kadane算法O(n)) |
| 多数元素(分治) | O(n log n) | O(log n) | 不是最优(摩尔投票O(n)) |
| 第K大元素(分治) | 平均O(n) | O(log n) | 平均情况下是最优 |
| 平面最近点对 | O(n log n) | O(n) | 对该问题是最优 |
| Karatsuba大整数乘法 | O(n^1.585) | O(n) | 比传统算法更优 |
| Strassen矩阵乘法 | O(n^2.807) | O(n²) | 比传统算法更优 |
| FFT多项式乘法 | O(n log n) | O(n) | 对多项式乘法是最优 |

## 学习建议

1. **掌握基础**: 熟练掌握归并排序、快速排序、二分查找等基础分治算法
2. **理解思想**: 理解分治法的核心思想：分解、解决、合并
3. **分析复杂度**: 学会使用主定理分析分治算法的时间复杂度
4. **实践应用**: 多做题，理解在不同场景下如何应用分治法
5. **优化技巧**: 学习如何优化分治算法，如剪枝、记忆化等
6. **扩展学习**: 学习更高级的分治算法，如FFT、Karatsuba乘法等
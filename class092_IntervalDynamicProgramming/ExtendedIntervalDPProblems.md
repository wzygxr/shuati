# 区间动态规划扩展题目清单

本文件整理了与class077中区间动态规划问题相关的更多练习题目，来源于各大算法平台。

## 📚 按平台分类

### LeetCode (力扣)
1. **LeetCode 312. 戳气球** - https://leetcode.cn/problems/burst-balloons/
   - 类型：区间DP
   - 难度：困难
   - 简介：给定n个气球，每个气球上有一个数字。戳破第i个气球可以获得nums[left] * nums[i] * nums[right]枚硬币，求能获得的最大硬币数。

2. **LeetCode 1547. 切棍子的最小成本** - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
   - 类型：区间DP
   - 难度：困难
   - 简介：给定一根长度为n的木棍和一个位置数组cuts，每次切割的成本等于当前木棍的长度，求切完所有位置的最小成本。

3. **LeetCode 1000. 合并石头的最低成本** - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
   - 类型：区间DP
   - 难度：困难
   - 简介：给定n堆石头和一个整数k，每次可以选择连续的k堆石头合并为一堆，成本为这k堆石头的总数，求合并成一堆的最低成本。

4. **LeetCode 664. 奇怪的打印机** - https://leetcode.cn/problems/strange-printer/
   - 类型：区间DP
   - 难度：困难
   - 简介：打印机有以下两个特殊要求：每次打印一个字符序列；每次可以打印任意数量的相同字符。

5. **LeetCode 516. 最长回文子序列** - https://leetcode.cn/problems/longest-palindromic-subsequence/
   - 类型：区间DP
   - 难度：中等
   - 简介：给定一个字符串s，找出其中最长的回文子序列的长度。

6. **LeetCode 132. 分割回文串 II** - https://leetcode.cn/problems/palindrome-partitioning-ii/
   - 类型：区间DP
   - 难度：困难
   - 简介：给定一个字符串s，将其分割成一些子串，使每个子串都是回文串，求最少分割次数。

7. **LeetCode 1039. 多边形三角剖分的最低得分** - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
   - 类型：区间DP
   - 难度：中等
   - 简介：给定一个凸多边形，每个顶点都有一个值，将其三角剖分后，每个三角形的值为三个顶点值的乘积，求最低总得分。

8. **LeetCode 1246. 删除回文子数组** - https://leetcode.cn/problems/palindrome-removal/
   - 类型：区间DP
   - 难度：困难
   - 简介：给定一个整数数组arr，每次可以选择并删除一个回文子数组，求删除所有数字的最少操作次数。

9. **LeetCode 1130. 叶值的最小代价生成树** - https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
   - 类型：区间DP
   - 难度：中等
   - 简介：给定一个正整数数组，构造一个叶值为该数组的二叉树，每个非叶节点的值为两个子节点值的乘积，求最小代价。

10. **LeetCode 1770. 执行乘法运算的最大分数** - https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
    - 类型：区间DP
    - 难度：中等
    - 简介：给定两个数组nums和multipliers，每次从nums的头部或尾部取一个数与multipliers[i]相乘，求最大得分。

### 经典区间DP问题

1. **石子合并问题**
   - 类型：区间DP
   - 简介：给定n堆石子排成一排，每次合并相邻的两堆石子，合并的得分为两堆石子数目的和，求合并成一堆的最大/最小得分。

2. **矩阵链乘法**
   - 类型：区间DP
   - 简介：给定一系列矩阵，确定它们的乘法顺序，使得计算乘积所需的标量乘法次数最少。

3. **最优二叉搜索树**
   - 类型：区间DP
   - 简介：给定n个关键字的搜索概率和n+1个虚拟键的搜索概率，构造一棵期望搜索代价最小的二叉搜索树。

4. **编辑距离**
   - 类型：区间DP
   - 简介：给定两个字符串，计算通过插入、删除、替换操作将一个字符串转换为另一个字符串的最小操作次数。

## 🧠 解题思路与技巧

### 核心思想
区间动态规划是一种通过将问题分解为子区间来解决的动态规划方法。主要思路是：
1. 定义状态：dp[i][j]表示区间[i,j]上的最优解
2. 状态转移：枚举分割点k，将区间[i,j]分为[i,k]和[k+1,j]两部分
3. 枚举顺序：按区间长度从小到大进行计算

### 状态转移方程模板
```
dp[i][j] = optimal(dp[i][k] + dp[k+1][j] + cost)
```

### 填表顺序
```java
// 枚举区间长度
for (int len = 2; len <= n; len++) {
    // 枚举起点
    for (int i = 0; i <= n - len; i++) {
        int j = i + len - 1;
        // 枚举分割点
        for (int k = i; k < j; k++) {
            dp[i][j] = optimal(dp[i][k] + dp[k+1][j] + cost);
        }
    }
}
```

### 时间复杂度分析
区间DP的时间复杂度通常为O(n³)，其中：
- 第一层循环枚举区间长度：O(n)
- 第二层循环枚举区间起点：O(n)
- 第三层循环枚举分割点：O(n)

### 空间复杂度分析
空间复杂度通常为O(n²)，用于存储dp数组。

## 🎯 工程化实践要点

### 1. 异常处理
- 输入验证：检查数组是否为空
- 边界处理：处理长度为0、1、2的特殊情况

### 2. 性能优化
- 空间压缩：在某些情况下可以优化空间复杂度
- 剪枝优化：通过数学性质减少不必要的计算

### 3. 代码可读性
- 变量命名清晰：使用有意义的变量名
- 注释详细：解释状态定义和转移方程

## 📈 常见应用场景

1. **字符串处理**：最长回文子序列、编辑距离等
2. **数组操作**：石子合并、戳气球等
3. **图形问题**：多边形三角剖分、最优二叉搜索树等
4. **游戏策略**：预测赢家等博弈问题

## 📘 学习资源推荐

### 书籍
1. 《算法导论》- Thomas H. Cormen等
2. 《算法竞赛入门经典》- 刘汝佳
3. 《挑战程序设计竞赛》- 秋叶拓哉等

### 在线资源
1. LeetCode - https://leetcode.cn/
2. LintCode - https://www.lintcode.com/
3. HackerRank - https://www.hackerrank.com/
4. Codeforces - https://codeforces.com/

## 🧪 本专题新增实现代码

我们为以下三个经典区间DP问题提供了Java、C++和Python三种语言的实现：

1. **戳气球问题** (Code07_BurstBalloons)
   - Java实现: Code07_BurstBalloons.java
   - C++实现: Code07_BurstBalloons.cpp
   - Python实现: Code07_BurstBalloons.py

2. **石子合并问题** (Code08_StoneMerge)
   - Java实现: Code08_StoneMerge.java
   - C++实现: Code08_StoneMerge.cpp
   - Python实现: Code08_StoneMerge.py

3. **最长回文子序列** (Code09_LongestPalindromicSubsequence)
   - Java实现: Code09_LongestPalindromicSubsequence.java
   - C++实现: Code09_LongestPalindromicSubsequence.cpp
   - Python实现: Code09_LongestPalindromicSubsequence.py
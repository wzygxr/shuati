# Class076 - 区间动态规划扩展题目清单

本文件整理了与class076中区间动态规划问题相关的更多练习题目，来源于各大算法平台。

## 📚 按平台分类

### LeetCode (力扣)
1. **LeetCode 132. 分割回文串 II** - https://leetcode.cn/problems/palindrome-partitioning-ii/
   - 类型：区间DP
   - 难度：困难
   - 相关题目：Code07_PalindromePartitioningII.java

2. **LeetCode 516. 最长回文子序列** - https://leetcode.cn/problems/longest-palindromic-subsequence/
   - 类型：区间DP
   - 难度：中等
   - 相关题目：Code08_LongestPalindromicSubsequence.java

3. **LeetCode 312. 戳气球** - https://leetcode.cn/problems/burst-balloons/
   - 类型：区间DP
   - 难度：困难
   - 相关题目：Code05_BurstBalloons.java

4. **LeetCode 1547. 切棍子的最小成本** - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
   - 类型：区间DP
   - 难度：困难
   - 相关题目：Code04_MinimumCostToCutAStick.java

5. **LeetCode 1039. 多边形三角剖分的最低得分** - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
   - 类型：区间DP
   - 难度：中等
   - 相关题目：Code03_MinimumScoreTriangulationOfPolygon.java

6. **LeetCode 1000. 合并石头的最低成本** - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
   - 类型：区间DP
   - 难度：困难

7. **LeetCode 664. 奇怪的打印机** - https://leetcode.cn/problems/strange-printer/
   - 类型：区间DP
   - 难度：困难
   - 相关题目：Code09_StrangePrinter.java, Code09_StrangePrinter.cpp, Code09_StrangePrinter.py

8. **LeetCode 1246. 删除回文子数组** - https://leetcode.cn/problems/palindrome-removal/
   - 类型：区间DP
   - 难度：困难

9. **LeetCode 1130. 叶值的最小代价生成树** - https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
   - 类型：区间DP
   - 难度：中等

10. **LeetCode 1770. 执行乘法运算的最大分数** - https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
    - 类型：区间DP
    - 难度：中等

### LintCode (炼码)
1. **LintCode 108. 分割回文串 II** - https://www.lintcode.com/problem/108/
   - 类型：区间DP
   - 难度：中等

2. **LintCode 1063. 凸多边形的三角剖分** - https://www.lintcode.com/problem/1063/
   - 类型：区间DP
   - 难度：困难

3. **LintCode 136. 分割回文串** - https://www.lintcode.com/problem/136/
   - 类型：区间DP
   - 难度：中等

### HackerRank
1. **HackerRank - Sherlock and Cost** - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
   - 类型：区间DP
   - 难度：中等

2. **HackerRank - Palindrome Index** - https://www.hackerrank.com/challenges/palindrome-index/problem
   - 类型：区间DP
   - 难度：简单

3. **HackerRank - Game of Stones** - https://www.hackerrank.com/challenges/game-of-stones-1/problem
   - 类型：博弈DP
   - 难度：简单

### Codeforces
1. **Codeforces 1327D - Infinite Path** - https://codeforces.com/problemset/problem/1327/D
   - 类型：区间DP
   - 难度：1900

2. **Codeforces 1373C - Pluses and Minuses** - https://codeforces.com/problemset/problem/1373/C
   - 类型：区间DP
   - 难度：1600

### AtCoder
1. **AtCoder ABC144D - Water Bottle** - https://atcoder.jp/contests/abc144/tasks/abc144_d
   - 类型：区间DP
   - 难度：绿

2. **AtCoder ABC161D - Lunlun Number** - https://atcoder.jp/contests/abc161/tasks/abc161_d
   - 类型：区间DP
   - 难度：茶

## 🧠 区间动态规划知识点总结

### 1. 核心思想
区间动态规划是一种特殊的动态规划，它按照区间长度递增的顺序进行状态转移。通常用`dp[i][j]`表示区间`[i,j]`上的最优解。

### 2. 状态转移方程模式
```
dp[i][j] = max/min {dp[i][k] + dp[k+1][j] + cost}  (i <= k < j)
```

### 3. 填表顺序
区间DP通常按照区间长度从小到大进行填表：
```java
for (int len = 2; len <= n; len++) {           // 枚举区间长度
    for (int i = 0; i <= n - len; i++) {       // 枚举区间起点
        int j = i + len - 1;                   // 计算区间终点
        for (int k = i; k < j; k++) {          // 枚举分割点
            dp[i][j] = max/min(dp[i][j], dp[i][k] + dp[k+1][j] + cost);
        }
    }
}
```

### 4. 常见应用场景
1. **字符串处理**：回文串相关问题
2. **数组处理**：分割、合并问题
3. **几何问题**：多边形三角剖分
4. **博弈问题**：游戏策略选择

### 5. 时间和空间复杂度
- **时间复杂度**：通常为O(n³)，其中n为区间长度
- **空间复杂度**：通常为O(n²)，可以优化到O(n)

## 🎯 解题技巧总结

### 1. 状态定义技巧
- 明确`dp[i][j]`的含义，通常是区间[i,j]上的最优解
- 注意边界条件，如`dp[i][i]`的初始化

### 2. 状态转移技巧
- 枚举分割点k，将大问题分解为两个子问题
- 考虑边界情况，如区间长度为1或2的情况

### 3. 优化技巧
- **空间优化**：使用滚动数组或变量代替二维数组
- **预处理**：提前计算辅助信息，如回文串判断
- **剪枝**：在状态转移时加入剪枝条件

## 🚀 工程化考量

### 1. 异常处理
- 检查输入参数合法性
- 处理边界条件
- 防止整数溢出

### 2. 性能优化
- 选择合适的数据结构
- 减少不必要的计算
- 空间优化降低内存使用

### 3. 可测试性
- 提供完整的测试用例
- 覆盖边界场景
- 验证算法正确性

## 📈 学习路径建议

### 第一阶段：基础掌握
1. 理解区间DP基本思想
2. 掌握状态定义和转移方程
3. 完成所有简单题目

### 第二阶段：类型熟悉
1. 理解各类区间DP问题的特征
2. 掌握优化技巧
3. 完成中等难度题目

### 第三阶段：高阶应用
1. 学习高级优化技巧
2. 掌握实际应用中的变种问题
3. 完成困难题目

---
**最后更新时间**：2025-10-20  
**作者**：AI Assistant
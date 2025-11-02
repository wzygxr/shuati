# class066 - 动态规划专题

本目录包含动态规划相关的算法题目和解答，涵盖基础动态规划、状态机DP、区间DP等多种类型。

## 题目列表

### 基础动态规划

1. **Code01_FibonacciNumber** - 斐波那契数列
   - 题目：计算第n个斐波那契数
   - 来源：LeetCode 509
   - 网址：https://leetcode.cn/problems/fibonacci-number/
   - 时间复杂度：O(n)，空间复杂度：O(1)

2. **Code02_MinimumCostForTickets** - 最低票价
   - 题目：在给定的旅行天数中，选择最便宜的购票方案
   - 来源：LeetCode 983
   - 网址：https://leetcode.cn/problems/minimum-cost-for-tickets/
   - 时间复杂度：O(n)，空间复杂度：O(n)

3. **Code03_DecodeWays** - 解码方法
   - 题目：将数字字符串解码为字母组合的方法数
   - 来源：LeetCode 91
   - 网址：https://leetcode.cn/problems/decode-ways/
   - 时间复杂度：O(n)，空间复杂度：O(n)

4. **Code04_DecodeWaysII** - 解码方法II
   - 题目：包含通配符的数字字符串解码方法数
   - 来源：LeetCode 639
   - 网址：https://leetcode.cn/problems/decode-ways-ii/
   - 时间复杂度：O(n)，空间复杂度：O(n)

5. **Code05_UglyNumberII** - 丑数II
   - 题目：找出第n个丑数（只包含质因子2、3、5的正整数）
   - 来源：LeetCode 264
   - 网址：https://leetcode.cn/problems/ugly-number-ii/
   - 时间复杂度：O(n)，空间复杂度：O(n)

6. **Code06_LongestValidParentheses** - 最长有效括号
   - 题目：找出最长的有效括号子串
   - 来源：LeetCode 32
   - 网址：https://leetcode.cn/problems/longest-valid-parentheses/
   - 时间复杂度：O(n)，空间复杂度：O(n)

7. **Code07_UniqueSubstringsWraparoundString** - 环绕字符串中唯一的子字符串
   - 题目：在环绕字符串中找出唯一的子字符串数量
   - 来源：LeetCode 467
   - 网址：https://leetcode.cn/problems/unique-substrings-in-wraparound-string/
   - 时间复杂度：O(n)，空间复杂度：O(1)

8. **Code08_DistinctSubsequencesII** - 不同的子序列II
   - 题目：计算字符串的不同非空子序列数量
   - 来源：LeetCode 940
   - 网址：https://leetcode.cn/problems/distinct-subsequences-ii/
   - 时间复杂度：O(n)，空间复杂度：O(1)

9. **Code09_LongestCommonSubsequence** - 最长公共子序列
   - 题目：找出两个字符串的最长公共子序列
   - 来源：LeetCode 1143
   - 网址：https://leetcode.cn/problems/longest-common-subsequence/
   - 时间复杂度：O(m*n)，空间复杂度：O(min(m,n))

### 新增动态规划题目

10. **Code19_ClimbingStairs** - 爬楼梯
    - 题目：计算爬到n阶楼梯的不同方法数
    - 来源：LeetCode 70
    - 网址：https://leetcode.cn/problems/climbing-stairs/
    - 时间复杂度：O(n)，空间复杂度：O(1)

11. **Code20_MinCostClimbingStairs** - 使用最小花费爬楼梯
    - 题目：选择最小花费的爬楼梯方案
    - 来源：LeetCode 746
    - 网址：https://leetcode.cn/problems/min-cost-climbing-stairs/
    - 时间复杂度：O(n)，空间复杂度：O(1)

12. **Code21_HouseRobberII** - 打家劫舍II
    - 题目：环形房屋的最大盗窃金额（不能偷相邻房屋）
    - 来源：LeetCode 213
    - 网址：https://leetcode.cn/problems/house-robber-ii/
    - 时间复杂度：O(n)，空间复杂度：O(1)

13. **Code22_DeleteAndEarn** - 删除并获得点数
    - 题目：选择数字获得点数，但删除相邻数字
    - 来源：LeetCode 740
    - 网址：https://leetcode.cn/problems/delete-and-earn/
    - 时间复杂度：O(n + k)，空间复杂度：O(k)

14. **Code23_MaximumProductSubarray** - 乘积最大子数组
    - 题目：找出乘积最大的连续子数组
    - 来源：LeetCode 152
    - 网址：https://leetcode.cn/problems/maximum-product-subarray/
    - 时间复杂度：O(n)，空间复杂度：O(1)

15. **Code24_BestTimeToBuyAndSellStock** - 买卖股票的最佳时机
    - 题目：一次交易的最大利润
    - 来源：LeetCode 121
    - 网址：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
    - 时间复杂度：O(n)，空间复杂度：O(1)

## 算法技巧总结

### 1. 基础动态规划模式
- **斐波那契数列**：f(n) = f(n-1) + f(n-2)
- **爬楼梯问题**：类似斐波那契，状态转移简单
- **最小花费爬楼梯**：选择最小花费的路径

### 2. 字符串解码问题
- **解码方法**：注意0的特殊处理
- **通配符解码**：处理*字符的多种可能

### 3. 序列问题
- **最长有效括号**：使用栈或动态规划
- **环绕字符串**：连续字符的处理

### 4. 子序列问题
- **最长公共子序列**：经典二维DP
- **不同子序列**：计数类DP

### 5. 环形问题处理
- **打家劫舍II**：分解为两个线性问题
- 核心技巧：环形数组 → [0, n-2] 和 [1, n-1]

### 6. 状态机DP
- **买卖股票**：持有/不持有状态
- **乘积最大子数组**：同时维护最大最小值

### 7. 转化技巧
- **删除并获得点数**：转化为打家劫舍问题
- **Kadane算法变种**：最大子数组和的应用

## 时间复杂度分析

| 题目 | 最优时间复杂度 | 空间复杂度 | 关键优化 |
|------|---------------|------------|----------|
| 斐波那契数列 | O(n) | O(1) | 滚动数组 |
| 最低票价 | O(n) | O(n) | 记忆化搜索 |
| 解码方法 | O(n) | O(n) | 状态转移 |
| 丑数II | O(n) | O(n) | 三指针 |
| 最长有效括号 | O(n) | O(n) | 栈/DP |
| 买卖股票 | O(n) | O(1) | 记录最低价 |

## 工程化考量

### 1. 边界处理
- 空数组、单元素数组
- 极端输入值（0、负数、大数）
- 字符串边界（空串、单字符）

### 2. 性能优化
- 空间优化：使用滚动数组
- 时间优化：避免重复计算
- 预处理：统计频率、排序等

### 3. 代码质量
- 清晰的变量命名
- 模块化的函数设计
- 详细的注释说明

### 4. 测试覆盖
- 边界测试用例
- 性能测试数据
- 特殊场景验证

## 学习建议

1. **理解状态定义**：明确dp[i]的含义
2. **掌握状态转移**：找到递推关系式
3. **优化空间复杂度**：学会使用滚动数组
4. **处理边界情况**：考虑各种极端输入
5. **多解法对比**：理解不同解法的优劣

## 扩展练习

建议按照以下顺序练习：
1. 先掌握基础题目（1-9）
2. 再挑战中等难度（10-15）
3. 最后尝试高级应用

每个题目都提供了Java、C++、Python三种语言的实现，方便对比学习。

## 资源链接

- [LeetCode动态规划专题](https://leetcode.cn/tag/dynamic-programming/)
- [动态规划算法模板](https://github.com/youngyangyang04/leetcode-master)
- [算法可视化工具](https://visualgo.net/zh)

---
*最后更新：2025-10-24*
# Class053: 单调栈专题

本章节包含单调栈相关的经典题目和实现，涵盖Java、C++和Python三种语言版本。

## 📚 题目列表

### 1. 最大宽度坡 (Maximum Width Ramp)
- **文件**: `Code01_MaximumWidthRamp.java`
- **题目链接**: https://leetcode.cn/problems/maximum-width-ramp/
- **难度**: 中等
- **核心思路**: 使用单调递减栈存储可能的坡底索引，然后从右向左遍历寻找最大宽度坡

### 2. 去除重复字母 (Remove Duplicate Letters)
- **文件**: `Code02_RemoveDuplicateLetters.java`
- **题目链接**: https://leetcode.cn/problems/remove-duplicate-letters/
- **难度**: 中等
- **核心思路**: 使用单调递增栈保证字典序最小，同时确保每个字符都出现一次

### 3. 大鱼吃小鱼问题 (Big Fish Eat Small Fish)
- **文件**: `Code03_BigFishEatSmallFish.java`
- **题目链接**: https://www.nowcoder.com/practice/77199defc4b74b24b8ebf6244e1793de
- **难度**: 困难
- **核心思路**: 使用单调递减栈记录每条鱼被吃掉需要的轮数

### 4. 统计全1子矩形的数量 (Count Submatrices With All Ones)
- **文件**: `Code04_CountSubmatricesWithAllOnes.java`
- **题目链接**: https://leetcode.cn/problems/count-submatrices-with-all-ones/
- **难度**: 困难
- **核心思路**: 使用单调递增栈计算以每个位置为右下角的全1子矩形数量

### 5. 接雨水 (Trapping Rain Water)
- **文件**: 
  - `Code05_TrappingRainWater.java`
  - `Code05_TrappingRainWater.cpp`
  - `Code05_TrappingRainWater.py`
- **题目链接**: https://leetcode.cn/problems/trapping-rain-water/
- **难度**: 困难
- **核心思路**: 使用单调递减栈找到凹槽，计算能接住的雨水量

### 6. 柱状图中最大的矩形 (Largest Rectangle in Histogram)
- **文件**: 
  - `Code06_LargestRectangleInHistogram.java`
  - `Code06_LargestRectangleInHistogram.cpp`
  - `Code06_LargestRectangleInHistogram.py`
- **题目链接**: https://leetcode.cn/problems/largest-rectangle-in-histogram/
- **难度**: 困难
- **核心思路**: 使用单调递增栈找到每个柱子左右两边第一个比它矮的柱子，计算最大矩形面积

### 7. 下一个更大元素 I (Next Greater Element I)
- **文件**: `Code07_NextGreaterElementI.java`
- **题目链接**: https://leetcode.cn/problems/next-greater-element-i/
- **难度**: 简单
- **核心思路**: 使用单调递减栈预处理nums2数组，用哈希表记录每个元素的下一个更大元素

### 8. 下一个更大元素 II (Next Greater Element II)
- **文件**: `Code08_NextGreaterElementII.java`
- **题目链接**: https://leetcode.cn/problems/next-greater-element-ii/
- **难度**: 中等
- **核心思路**: 使用单调递减栈处理循环数组，遍历两遍数组模拟循环效果

### 9. 每日温度 (Daily Temperatures)
- **文件**: `Code09_DailyTemperatures.java`
- **题目链接**: https://leetcode.cn/problems/daily-temperatures/
- **难度**: 中等
- **核心思路**: 使用单调递减栈找到每个温度下一个更高温度出现在几天后

### 10. 股票价格跨度 (Online Stock Span)
- **文件**: `Code10_OnlineStockSpan.java`
- **题目链接**: https://leetcode.cn/problems/online-stock-span/
- **难度**: 中等
- **核心思路**: 使用单调递减栈存储(价格,跨度)二元组，合并小于等于当前价格的跨度

### 11. 移掉K位数字 (Remove K Digits)
- **文件**: 
  - `Code11_RemoveKDigits.java`
  - `Code11_RemoveKDigits.cpp`
  - `Code11_RemoveKDigits.py`
- **题目链接**: https://leetcode.cn/problems/remove-k-digits/
- **难度**: 中等
- **核心思路**: 使用单调递增栈，从左到右遍历数字字符串，当遇到更小数字且还有可移除位数时弹出栈顶元素

### 12. 132模式 (132 Pattern)
- **文件**: 
  - `Code12_Find132Pattern.java`
  - `Code12_Find132Pattern.cpp`
  - `Code12_Find132Pattern.py`
- **题目链接**: https://leetcode.cn/problems/132-pattern/
- **难度**: 中等
- **核心思路**: 使用单调递减栈从右向左遍历，维护可能作为"3"的元素，记录可能作为"2"的元素

### 13. 子数组的最小值之和 (Sum of Subarray Minimums)
- **文件**: 
  - `Code13_SumOfSubarrayMins.java`
  - `Code13_SumOfSubarrayMins.cpp`
  - `Code13_SumOfSubarrayMins.py`
- **题目链接**: https://leetcode.cn/problems/sum-of-subarray-minimums/
- **难度**: 中等
- **核心思路**: 使用单调递增栈找到每个元素左边和右边第一个更小元素的位置，计算以该元素为最小值的子数组数量

### 14. 表现良好的最长时间段 (Longest Well-Performing Interval)
- **文件**: 
  - `Code14_LongestWellPerformingInterval.java`
  - `Code14_LongestWellPerformingInterval.cpp`
  - `Code14_LongestWellPerformingInterval.py`
- **题目链接**: https://leetcode.cn/problems/longest-well-performing-interval/
- **难度**: 中等
- **核心思路**: 将问题转化为前缀和问题，使用单调递减栈存储前缀和索引，找和大于0的最长子数组

### 15. 队列中可以看到的人数 (Number of Visible People in a Queue)
- **文件**: 
  - `Code15_CanSeePersonsCount.java`
  - `Code15_CanSeePersonsCount.cpp`
  - `Code15_CanSeePersonsCount.py`
- **题目链接**: https://leetcode.cn/problems/number-of-visible-people-in-a-queue/
- **难度**: 困难
- **核心思路**: 使用单调递减栈从右向左遍历，栈中所有比当前元素小的元素都能被看到，直到遇到一个比它大的元素

### 16. 滑动窗口最大值 (Sliding Window Maximum)
- **文件**: 
  - `Code16_SlidingWindowMaximum.java`
  - `Code16_SlidingWindowMaximum.cpp`
  - `Code16_SlidingWindowMaximum.py`
- **题目链接**: https://leetcode.cn/problems/sliding-window-maximum/
- **难度**: 困难
- **核心思路**: 使用单调递减双端队列维护滑动窗口中的最大值候选者

### 17. 最小栈 (Min Stack)
- **文件**: 
  - `Code17_MinStack.java`
  - `Code17_MinStack.cpp`
  - `Code17_MinStack.py`
- **题目链接**: https://leetcode.cn/problems/min-stack/
- **难度**: 简单
- **核心思路**: 使用双栈法（一个存储数据，一个存储最小值）

### 18. 使括号有效的最少删除 (Minimum Remove to Make Valid Parentheses)
- **文件**: 
  - `Code18_MinimumRemoveToMakeValidParentheses.java`
  - `Code18_MinimumRemoveToMakeValidParentheses.cpp`
  - `Code18_MinimumRemoveToMakeValidParentheses.py`
- **题目链接**: https://leetcode.cn/problems/minimum-remove-to-make-valid-parentheses/
- **难度**: 中等
- **核心思路**: 使用栈和标记数组删除无效括号

### 19. 岛屿数量 (Number of Islands)
- **文件**: 
  - `Code19_NumberOfIslands.java`
  - `Code19_NumberOfIslands.cpp`
  - `Code19_NumberOfIslands.py`
- **题目链接**: https://leetcode.cn/problems/number-of-islands/
- **难度**: 中等
- **核心思路**: 使用DFS/BFS/并查集标记相连的陆地

### 20. 有效的括号 (Valid Parentheses)
- **文件**: 
  - `Code20_ValidParentheses.java`
  - `Code20_ValidParentheses.cpp`
  - `Code20_ValidParentheses.py`
- **题目链接**: https://leetcode.cn/problems/valid-parentheses/
- **难度**: 简单
- **核心思路**: 使用栈匹配括号对

### 21. 字符串解码 (Decode String)
- **文件**: `Code21_DecodeString.java`
- **题目链接**: https://leetcode.cn/problems/decode-string/
- **难度**: 中等
- **核心思路**: 使用两个栈（数字栈和字符串栈）处理嵌套解码

### 22. 小行星碰撞 (Asteroid Collision)
- **题目链接**: https://leetcode.cn/problems/asteroid-collision/
- **难度**: 中等
- **核心思路**: 使用栈模拟小行星碰撞过程

### 23. 最长递增子序列 (Longest Increasing Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-increasing-subsequence/
- **难度**: 中等
- **核心思路**: 使用单调递增栈优化动态规划

### 24. 最大矩形 (Maximal Rectangle)
- **题目链接**: https://leetcode.cn/problems/maximal-rectangle/
- **难度**: 困难
- **核心思路**: 将问题转化为柱状图中最大矩形问题，使用单调栈解决

### 25. 栈的最小值 (Min Stack)
- **题目链接**: https://www.lintcode.com/problem/12/
- **难度**: 简单
- **核心思路**: 设计一个支持getMin操作的栈

### 26. 字符串转换整数 (atoi)
- **题目链接**: https://leetcode.cn/problems/string-to-integer-atoi/
- **难度**: 中等
- **核心思路**: 使用栈处理数字转换

### 27. 基本计算器 II (Basic Calculator II)
- **题目链接**: https://leetcode.cn/problems/basic-calculator-ii/
- **难度**: 中等
- **核心思路**: 使用栈计算表达式的值

### 28. 最大子矩形 (Maximal Rectangle)
- **题目链接**: https://www.acwing.com/problem/content/133/
- **难度**: 困难
- **核心思路**: 基于单调栈的直方图最大矩形问题扩展

### 29. 包含重复元素的排列 (Permutations II)
- **题目链接**: https://leetcode.cn/problems/permutations-ii/
- **难度**: 中等
- **核心思路**: 使用栈进行回溯搜索

### 30. 函数的独占时间 (Exclusive Time of Functions)
- **题目链接**: https://leetcode.cn/problems/exclusive-time-of-functions/
- **难度**: 中等
- **核心思路**: 使用栈记录函数调用信息

### 31. 最大频率栈 (Maximum Frequency Stack)
- **题目链接**: https://leetcode.cn/problems/maximum-frequency-stack/
- **难度**: 困难
- **核心思路**: 使用多个栈，每个栈存储相同频率的元素

### 32. 单调栈的基本应用 (Monotonic Stack Basics)
- **题目链接**: https://codeforces.com/contest/1313/problem/C1
- **难度**: 中等
- **核心思路**: 使用单调栈解决序列问题

### 33. 平衡括号的最小插入次数 (Minimum Insertions to Balance a Parentheses String)
- **题目链接**: https://leetcode.cn/problems/minimum-insertions-to-balance-a-parentheses-string/
- **难度**: 中等
- **核心思路**: 使用栈记录括号匹配状态

### 34. 有效的括号字符串 (Valid Parenthesis String)
- **题目链接**: https://leetcode.cn/problems/valid-parenthesis-string/
- **难度**: 中等
- **核心思路**: 使用栈处理 '*' 作为通配符的情况

### 35. 下一个更大元素 III (Next Greater Element III)
- **题目链接**: https://leetcode.cn/problems/next-greater-element-iii/
- **难度**: 中等
- **核心思路**: 使用单调栈找到下一个排列

### 36. 最小路径和 (Minimum Path Sum)
- **题目链接**: https://leetcode.cn/problems/minimum-path-sum/
- **难度**: 中等
- **核心思路**: 使用栈进行深度优先搜索

### 37. 编辑距离 (Edit Distance)
- **题目链接**: https://leetcode.cn/problems/edit-distance/
- **难度**: 困难
- **核心思路**: 栈辅助回溯

### 38. 最长有效括号 (Longest Valid Parentheses)
- **题目链接**: https://leetcode.cn/problems/longest-valid-parentheses/
- **难度**: 困难
- **核心思路**: 使用栈记录无效括号的位置

### 39. 括号生成 (Generate Parentheses)
- **题目链接**: https://leetcode.cn/problems/generate-parentheses/
- **难度**: 中等
- **核心思路**: 使用栈进行回溯生成

### 40. 栈与队列的转换 (Implement Queue using Stacks)
- **题目链接**: https://leetcode.cn/problems/implement-queue-using-stacks/
- **难度**: 简单
- **核心思路**: 使用两个栈实现队列

### 41. 接雨水 II (Trapping Rain Water II)
- **题目链接**: https://leetcode.cn/problems/trapping-rain-water-ii/
- **难度**: 困难
- **核心思路**: 使用优先队列（堆）模拟高度图

### 42. 加油站 (Gas Station)
- **题目链接**: https://leetcode.cn/problems/gas-station/
- **难度**: 中等
- **核心思路**: 使用单调栈优化

### 43. 寻找峰值 (Find Peak Element)
- **题目链接**: https://leetcode.cn/problems/find-peak-element/
- **难度**: 中等
- **核心思路**: 使用单调栈找到峰值

### 44. 合并区间 (Merge Intervals)
- **题目链接**: https://leetcode.cn/problems/merge-intervals/
- **难度**: 中等
- **核心思路**: 使用栈合并重叠区间

### 45. 最长公共子序列 (Longest Common Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-common-subsequence/
- **难度**: 中等
- **核心思路**: 栈辅助回溯

### 46. 最小覆盖子串 (Minimum Window Substring)
- **题目链接**: https://leetcode.cn/problems/minimum-window-substring/
- **难度**: 困难
- **核心思路**: 滑动窗口与栈结合

### 47. 路径总和 II (Path Sum II)
- **题目链接**: https://leetcode.cn/problems/path-sum-ii/
- **难度**: 中等
- **核心思路**: 使用栈进行深度优先搜索

### 48. 最小栈 II (Min Stack II)
- **题目链接**: https://www.nowcoder.com/practice/4c776177d2c04c2494f2555c9fcc1e49
- **难度**: 中等
- **核心思路**: 设计一个支持O(1)时间获取最小值的栈

### 49. 字符串匹配 (String Matching)
- **题目链接**: https://www.acwing.com/problem/content/144/
- **难度**: 中等
- **核心思路**: KMP算法与栈结合

### 50. 最大子数组和 (Maximum Subarray)
- **题目链接**: https://leetcode.cn/problems/maximum-subarray/
- **难度**: 简单
- **核心思路**: 使用单调栈优化动态规划

## 🧠 单调栈核心思想

单调栈是一种特殊的栈结构，其中的元素保持单调性（单调递增或单调递减）。它主要用于解决以下几类问题：

1. **寻找下一个更大/更小元素**：如每日温度、下一个更大元素等问题
2. **寻找上一个更大/更小元素**：通过从右向左遍历转换为第一类问题
3. **计算面积/体积**：如接雨水、柱状图中最大矩形等问题
4. **优化递归/动态规划**：某些可以用单调栈优化的DP状态转移

### 核心操作步骤：

1. **维护单调性**：当新元素破坏栈的单调性时，弹出栈顶元素直到满足单调性
2. **处理弹出元素**：根据题目要求对弹出的元素进行处理
3. **入栈**：将新元素入栈

## ⏱️ 复杂度分析

- **时间复杂度**：O(n) - 每个元素最多入栈和出栈各一次
- **空间复杂度**：O(n) - 栈的空间最多为n

## 🎯 适用场景

单调栈适用于以下特征的问题：

1. **一维数组**：需要寻找任一个元素的右边或左边第一个比自己大或小的元素位置
2. **区间最值**：需要快速找到某个区间的最大值或最小值
3. **优化嵌套循环**：将O(n²)的暴力解法优化为O(n)

## 📖 学习建议

1. **理解单调性**：搞清楚什么时候使用单调递增栈，什么时候使用单调递减栈
2. **掌握模板**：熟练掌握单调栈的基本操作模板
3. **多做练习**：从简单到困难，逐步提高
4. **总结变化**：不同题目的变化点在哪里
5. **代码实践**：手写实现，不要依赖IDE

### 51. 子数组的最大最小值之差 (Maximum Absolute Difference in Subarrays)
- **题目链接**: https://www.hackerrank.com/contests/hackerrank-internship/challenges/absolute-element-sums
- **难度**: 困难
- **核心思路**: 使用两个单调队列（一个递增，一个递减）维护滑动窗口的最小值和最大值，计算差值的最大值

### 52. 所有可能的递增子序列 (All Possible Increasing Subsequences)
- **题目链接**: https://atcoder.jp/contests/abc217/tasks/abc217_d
- **难度**: 中等
- **核心思路**: 使用单调栈记录递增子序列的结束位置

### 53. 寻找右侧第一个小于当前元素的位置 (Find Right Smaller)
- **题目链接**: https://www.lintcode.com/problem/495/
- **难度**: 中等
- **核心思路**: 使用单调递增栈从右向左遍历数组

### 54. 最大子矩阵 III (Maximal Submatrix III)
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=3480
- **难度**: 困难
- **核心思路**: 基于单调栈的最大矩形问题扩展，处理带权值的矩阵

### 55. 合并石头的最低成本 (Minimum Cost to Merge Stones)
- **题目链接**: https://leetcode.cn/problems/minimum-cost-to-merge-stones/
- **难度**: 困难
- **核心思路**: 动态规划与单调栈优化

### 56. 最短路径访问所有节点 (Shortest Path Visiting All Nodes)
- **题目链接**: https://leetcode.cn/problems/shortest-path-visiting-all-nodes/
- **难度**: 困难
- **核心思路**: BFS与状态压缩结合，使用栈优化搜索路径

### 57. 最多能完成排序的块 (Maximum Number of Achievable Transfer Requests)
- **题目链接**: https://leetcode.cn/problems/maximum-number-of-achievable-transfer-requests/
- **难度**: 困难
- **核心思路**: 状态压缩与单调栈优化

### 58. 最大连续子序列 (Maximum Continuous Subsequence)
- **题目链接**: https://www.spoj.com/problems/KGSS/
- **难度**: 中等
- **核心思路**: 使用单调栈优化最大子序列和的计算

### 59. 矩形覆盖 (Rectangle Cover)
- **题目链接**: https://www.acwing.com/problem/content/399/
- **难度**: 困难
- **核心思路**: 基于单调栈的矩形覆盖问题

### 60. 双栈排序 (Two Stacks Sorting)
- **题目链接**: https://www.luogu.com.cn/problem/P1198
- **难度**: 困难
- **核心思路**: 使用单调栈进行双栈排序

### 61. 股票买卖 III (Best Time to Buy and Sell Stock III)
- **题目链接**: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
- **难度**: 困难
- **核心思路**: 使用单调栈优化股票买卖策略

### 62. 最小字典序字符串 (Lexicographical Smallest String)
- **题目链接**: https://codeforces.com/contest/1204/problem/B
- **难度**: 中等
- **核心思路**: 使用单调栈构建最小字典序字符串

### 63. 最长交替子序列 (Longest Alternating Subsequence)
- **题目链接**: https://leetcode.cn/problems/longest-palindromic-subsequence/
- **难度**: 中等
- **核心思路**: 使用单调栈优化最长交替子序列的计算

### 64. 二维接雨水 (Trapping Rain Water in 2D)
- **题目链接**: https://oj.leetcode.com/problems/trapping-rain-water-ii/
- **难度**: 困难
- **核心思路**: 优先队列与单调栈结合解决二维接雨水问题

### 65. 寻找子数组的最小和最大元素 (Find Min and Max in Subarray)
- **题目链接**: https://www.codechef.com/problems/MAXAND18
- **难度**: 中等
- **核心思路**: 使用单调栈快速查询子数组的最小和最大元素

### 66. 字符串合并 (String Merge)
- **题目链接**: https://codeforces.com/problemset/problem/1294/E
- **难度**: 困难
- **核心思路**: 动态规划与单调栈优化

### 67. 最大交换次数 (Maximum Swap)
- **题目链接**: https://leetcode.cn/problems/maximum-swap/
- **难度**: 中等
- **核心思路**: 使用单调栈找到最佳交换位置

### 68. 最多能完成排序的块 II (Max Chunks To Make Sorted II)
- **题目链接**: https://leetcode.cn/problems/max-chunks-to-make-sorted-ii/
- **难度**: 困难
- **核心思路**: 使用单调栈维护块的最大值

### 69. 不同的子序列 II (Distinct Subsequences II)
- **题目链接**: https://leetcode.cn/problems/distinct-subsequences-ii/
- **难度**: 困难
- **核心思路**: 动态规划与单调栈优化

### 70. 最小覆盖子数组 (Minimum Covering Subarray)
- **题目链接**: https://www.acwing.com/problem/content/154/
- **难度**: 困难
- **核心思路**: 滑动窗口与单调栈结合

### 71. 最大子矩阵和 (Maximum Submatrix Sum)
- **题目链接**: https://www.lintcode.com/problem/405/
- **难度**: 困难
- **核心思路**: 二维前缀和与单调栈结合

### 72. 路径规划问题 (Path Planning)
- **题目链接**: https://www.spoj.com/problems/ADASTRNG/
- **难度**: 困难
- **核心思路**: 使用单调栈优化路径规划

### 73. 最小生成树 (Minimum Spanning Tree)
- **题目链接**: https://atcoder.jp/contests/abc206/tasks/abc206_e
- **难度**: 困难
- **核心思路**: Kruskal算法与单调栈优化

### 74. 网络流问题 (Network Flow)
- **题目链接**: https://www.hackerearth.com/practice/algorithms/graphs/min-cut/practice-problems/
- **难度**: 困难
- **核心思路**: 单调栈优化网络流算法

### 75. 字符串匹配问题 (String Matching Problem)
- **题目链接**: https://www.nowcoder.com/practice/2e38f28dd1d44af78c6a03bee4b0b4b3
- **难度**: 中等
- **核心思路**: KMP算法与单调栈结合

## 🧠 单调栈核心思想

单调栈是一种特殊的栈结构，其中的元素保持单调性（单调递增或单调递减）。它主要用于解决以下几类问题：

1. **寻找下一个更大/更小元素**：如每日温度、下一个更大元素等问题
2. **寻找上一个更大/更小元素**：通过从右向左遍历转换为第一类问题
3. **计算面积/体积**：如接雨水、柱状图中最大矩形等问题
4. **优化递归/动态规划**：某些可以用单调栈优化的DP状态转移

### 核心操作步骤：

1. **维护单调性**：当新元素破坏栈的单调性时，弹出栈顶元素直到满足单调性
2. **处理弹出元素**：根据题目要求对弹出的元素进行处理
3. **入栈**：将新元素入栈

## ⏱️ 复杂度分析

- **时间复杂度**：O(n) - 每个元素最多入栈和出栈各一次
- **空间复杂度**：O(n) - 栈的空间最多为n

## 🎯 适用场景

单调栈适用于以下特征的问题：

1. **一维数组**：需要寻找任一个元素的右边或左边第一个比自己大或小的元素位置
2. **区间最值**：需要快速找到某个区间的最大值或最小值
3. **优化嵌套循环**：将O(n²)的暴力解法优化为O(n)

## 📖 学习建议

1. **理解单调性**：搞清楚什么时候使用单调递增栈，什么时候使用单调递减栈
2. **掌握模板**：熟练掌握单调栈的基本操作模板
3. **多做练习**：从简单到困难，逐步提高
4. **总结变化**：不同题目的变化点在哪里
5. **代码实践**：手写实现，不要依赖IDE

## 🚀 运行测试

### Java
```bash
cd class053
javac Code01_MaximumWidthRamp.java
java -cp .. class053.Code01_MaximumWidthRamp

# 运行新添加的Java代码示例
javac Code11_RemoveKDigits.java
java -cp .. class053.Code11_RemoveKDigits
```

### Python
```bash
cd class053
python Code05_TrappingRainWater.py

# 运行新添加的Python代码示例
python Code11_RemoveKDigits.py
```

### C++
```bash
cd class053
g++ -std=c++11 Code05_TrappingRainWater.cpp -o Code05_TrappingRainWater
./Code05_TrappingRainWater

# 编译并运行新添加的C++代码示例
g++ -std=c++11 Code11_RemoveKDigits.cpp -o Code11_RemoveKDigits
./Code11_RemoveKDigits
```

---

**最后更新**: 2025年10月19日
**作者**: Algorithm Journey
**版本**: v1.1
# Class055: 单调队列专题

本章节包含单调队列（Monotonic Queue）的核心实现和经典应用题目。

## 🧠 算法概述

单调队列是一种特殊的双端队列，其中的元素保持单调性（单调递增或单调递减）。它主要用于解决滑动窗口最值问题和优化动态规划。

### 核心思想

1. **单调性维护**：队列中的元素按照特定规则保持单调递增或递减
2. **窗口管理**：维护固定或可变大小的窗口，通过左右边界滑动遍历所有可能的子区间
3. **最值获取**：队首元素始终为当前窗口的最值（最大值或最小值）

### 时间复杂度优势

- 暴力解法：O(n*k) - 对每个窗口遍历找最值
- 单调队列：O(n) - 每个元素最多入队出队一次

## 📚 题目列表

### 1. 和至少为K的最短子数组
- **文件**：
  - `Code01_ShortestSubarrayWithSumAtLeastK.java`
  - `Code01_ShortestSubarrayWithSumAtLeastK.cpp`
  - `Code01_ShortestSubarrayWithSumAtLeastK.py`
- **题目链接**：https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
- **难度**：困难
- **核心思路**：前缀和 + 单调递增队列优化
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
- **是否最优解**：✅ 是

### 2. 满足不等式的最大值
- **文件**：
  - `Code02_MaxValueOfEquation.java`
  - `Code02_MaxValueOfEquation.cpp`
  - `Code02_MaxValueOfEquation.py`
- **题目链接**：https://leetcode.cn/problems/max-value-of-equation/
- **难度**：困难
- **核心思路**：单调递减队列维护(y-x)值
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
- **是否最优解**：✅ 是

### 3. 你可以安排的最多任务数目
- **文件**：
  - `Code03_MaximumNumberOfTasksYouCanAssign.java`
  - `Code03_MaximumNumberOfTasksYouCanAssign.cpp`
  - `Code03_MaximumNumberOfTasksYouCanAssign.py`
- **题目链接**：https://leetcode.cn/problems/maximum-number-of-tasks-you-can-assign/
- **难度**：困难
- **核心思路**：二分答案 + 贪心策略 + 单调队列优化
- **时间复杂度**：O((n+m) * log(n+m) + min(n,m) * log(min(n,m)))
- **空间复杂度**：O(min(n,m))
- **是否最优解**：✅ 是

### 4. 滑动窗口最大值（单调队列经典模板题）
- **文件**：
  - `Code04_SlidingWindowMaximum.java`
  - `Code04_SlidingWindowMaximum.cpp`
  - `Code04_SlidingWindowMaximum.py`
- **题目链接**：https://leetcode.cn/problems/sliding-window-maximum/
- **难度**：困难
- **核心思路**：单调递减队列维护窗口最大值
- **时间复杂度**：O(n)
- **空间复杂度**：O(k)
- **是否最优解**：✅ 是

### 5. 绝对差不超过限制的最长连续子数组（双单调队列应用）
- **文件**：
  - `Code05_LongestSubarrayAbsoluteLimit.java`
  - `Code05_LongestSubarrayAbsoluteLimit.cpp`
  - `Code05_LongestSubarrayAbsoluteLimit.py`
- **题目链接**：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
- **难度**：中等
- **核心思路**：滑动窗口 + 双单调队列（单调递增+单调递减）
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
- **是否最优解**：✅ 是

### 6. 洛谷 P1886 滑动窗口/【模板】单调队列
- **文件**：
  - `Code06_LuoguP1886_SlidingWindow.java`
  - `Code06_LuoguP1886_SlidingWindow.cpp`
  - `Code06_LuoguP1886_SlidingWindow.py`
- **题目链接**：https://www.luogu.com.cn/problem/P1886
- **难度**：中等
- **核心思路**：双单调队列（单调递增队列求最小值，单调递减队列求最大值）
- **时间复杂度**：O(n)
- **空间复杂度**：O(k)
- **是否最优解**：✅ 是

### 7. POJ 2823 Sliding Window
- **文件**：
  - `Code07_POJ2823_SlidingWindow.java`
  - `Code07_POJ2823_SlidingWindow.cpp`
  - `Code07_POJ2823_SlidingWindow.py`
- **题目链接**：http://poj.org/problem?id=2823
- **难度**：中等
- **核心思路**：双单调队列（单调递增队列求最小值，单调递减队列求最大值）
- **时间复杂度**：O(n)
- **空间复杂度**：O(k)
- **是否最优解**：✅ 是

### 8. LeetCode 1425. 带限制的子序列和
- **文件**：
  - `Code08_ConstrainedSubsequenceSum.java`
  - `Code08_ConstrainedSubsequenceSum.cpp`
  - `Code08_ConstrainedSubsequenceSum.py`
- **题目链接**：https://leetcode.cn/problems/constrained-subsequence-sum/
- **难度**：困难
- **核心思路**：动态规划 + 单调递减队列优化
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
- **是否最优解**：✅ 是

### 9. LeetCode 1438. 绝对差不超过限制的最长连续子数组
- **文件**：
  - `Code09_LeetCode1438.java`
  - `Code09_LeetCode1438.cpp`
  - `Code09_LeetCode1438.py`
- **题目链接**：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
- **难度**：中等
- **核心思路**：滑动窗口 + 双单调队列（单调递增+单调递减）
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
- **是否最优解**：✅ 是

### 10. LeetCode 1696. 跳跃游戏 VI
- **文件**：
  - `Code10_LeetCode1696.java`
  - `Code10_LeetCode1696.cpp`
  - `Code10_LeetCode1696.py`
- **题目链接**：https://leetcode.cn/problems/jump-game-vi/
- **难度**：中等
- **核心思路**：动态规划 + 单调递减队列优化
- **时间复杂度**：O(n)
- **空间复杂度**：O(n)
- **是否最优解**：✅ 是

### 11. LeetCode 239. 滑动窗口最大值
- **文件**：
  - `Code11_LeetCode239.java`
  - `Code11_LeetCode239.cpp`
  - `Code11_LeetCode239.py`
- **题目链接**：https://leetcode.cn/problems/sliding-window-maximum/
- **难度**：困难
- **核心思路**：单调递减队列维护窗口最大值
- **时间复杂度**：O(n)
- **空间复杂度**：O(k)
- **是否最优解**：✅ 是

## 🎯 单调队列适用题型

### 1. 滑动窗口最值问题
- **特征**：固定或可变大小的窗口，求每个窗口的最大/最小值
- **方法**：单调队列
- **代表题**：LeetCode 239, 洛谷 P1886

### 2. 子数组/子序列和问题
- **特征**：涉及前缀和，需要快速查找满足条件的区间
- **方法**：前缀和 + 单调队列
- **代表题**：LeetCode 862, 1425

### 3. 绝对差限制问题
- **特征**：需要同时维护区间最大值和最小值
- **方法**：双单调队列
- **代表题**：LeetCode 1438

### 4. 队列维护最值
- **特征**：队列操作的同时需要O(1)获取最值
- **方法**：辅助单调队列
- **代表题**：剑指Offer 59-II

### 5. 优化动态规划
- **特征**：DP状态转移方程中需要查询区间最值
- **方法**：单调队列优化
- **代表题**：LeetCode 1425

## 🧠 单调队列核心操作

### 1. 维护单调性（队尾）
当新元素进入队列时，从队尾开始比较，移除破坏单调性的元素

### 2. 移除过期元素（队首）
检查队首元素是否超出窗口范围，如果超出则移除

### 3. 添加新元素（队尾）
将新元素添加到队列尾部

### 4. 获取最值（队首）
队首元素始终为当前窗口的最值

## ⏱️ 复杂度分析

- **时间复杂度**：O(n) - 每个元素最多入队出队一次
- **空间复杂度**：O(k) - k为窗口大小

## 🎯 适用场景

单调队列适用于以下特征的问题：

1. **滑动窗口**：固定或可变大小窗口的最值查询
2. **区间最值**：需要快速找到某个区间的最大值或最小值
3. **优化嵌套循环**：将O(n²)的暴力解法优化为O(n)
4. **动态规划优化**：某些可以用单调队列优化的DP状态转移

## 🚀 运行测试

### Java
```bash
cd class055
javac Code01_ShortestSubarrayWithSumAtLeastK.java
java -cp .. class055.Code01_ShortestSubarrayWithSumAtLeastK
```

### Python
```bash
cd class055
python Code01_ShortestSubarrayWithSumAtLeastK.py
```

### C++
```bash
cd class055
g++ -std=c++11 Code01_ShortestSubarrayWithSumAtLeastK.cpp -o Code01_ShortestSubarrayWithSumAtLeastK
./Code01_ShortestSubarrayWithSumAtLeastK
```

## 📖 学习建议

1. **先理解原理**：搞清楚为什么单调队列可以优化时间复杂度
2. **掌握模板**：熟练掌握滑动窗口最值和前缀和+单调队列两种模板
3. **多做练习**：从简单到困难，逐步提高
4. **总结变化**：不同题目的变化点在哪里
5. **代码实践**：手写实现，不要依赖IDE

---
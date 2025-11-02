# Class067: 动态规划进阶专题 - 补充题目清单

## 📋 目录
- [📚 按平台分类](#-按平台分类)
- [🎯 学习建议](#-学习建议)
- [🔍 解题技巧](#-解题技巧)
- [📊 复杂度分析](#-复杂度分析)
- [🔧 工程化考量](#-工程化考量)
- [🧪 测试用例设计](#-测试用例设计)
- [🚀 性能优化策略](#-性能优化策略)

本文件整理了与class067中动态规划问题相关的更多练习题目，来源于各大算法平台。每个题目都附有详细的解题思路、复杂度分析和实现要点。

## 🔍 解题技巧

### 1. 动态规划解题框架
```
1. 定义状态：明确dp[i][j]或dp[i]的含义
2. 状态转移：找出状态之间的关系式
3. 初始化：确定基准情况
4. 计算顺序：确定填表顺序
5. 返回结果：确定最终答案的位置
```

### 2. 常见DP类型及解题模式

#### 线性DP
- **特征**：状态转移只依赖于前几个状态
- **典型问题**：斐波那契数列、爬楼梯、最大子数组和
- **技巧**：状态压缩、滚动数组

#### 区间DP
- **特征**：状态定义与区间长度相关
- **典型问题**：矩阵连乘、石子合并、回文分割
- **技巧**：枚举分割点、长度递增遍历

#### 背包DP
- **特征**：物品选择、容量限制
- **典型问题**：0-1背包、完全背包、多重背包
- **技巧**：空间优化、状态定义转换

#### 字符串DP
- **特征**：涉及字符串匹配、编辑距离
- **典型问题**：最长公共子序列、编辑距离
- **技巧**：二维DP表、边界处理

#### 树形DP
- **特征**：在树结构上进行状态转移
- **典型问题**：二叉树最大路径和、树的最大独立集
- **技巧**：后序遍历、状态合并

#### 状态压缩DP
- **特征**：状态用位运算表示
- **典型问题**：旅行商问题、状态选择
- **技巧**：位运算优化、状态枚举

## 📊 复杂度分析

### 时间复杂度计算
```
线性DP: O(n) 或 O(n^2)
区间DP: O(n^3)
背包DP: O(n*C) 或 O(n*V)
字符串DP: O(m*n)
树形DP: O(n)
状态压缩DP: O(n*2^k)
```

### 空间复杂度优化
```
1. 滚动数组：将二维数组压缩为一维
2. 状态压缩：用位运算减少状态表示
3. 记忆化搜索：避免重复计算
4. 原地修改：利用输入数组存储状态
```

## 🔧 工程化考量

### 1. 代码可读性
- 变量命名清晰
- 函数职责单一
- 注释详细准确
- 代码结构模块化

### 2. 异常处理
```java
// 输入验证
if (grid == null || grid.length == 0) return 0;
if (word == null || word.isEmpty()) return false;
```

### 3. 边界条件
- 空输入处理
- 单元素情况
- 极端数据规模
- 特殊字符处理

### 4. 性能监控
- 时间复杂度分析
- 空间复杂度分析
- 内存使用监控
- 执行时间统计

## 🧪 测试用例设计

### 1. 基础测试用例
```java
// 最小路径和测试用例
int[][] grid1 = {{1,3,1},{1,5,1},{4,2,1}}; // 正常情况
int[][] grid2 = {{1}}; // 单元素
int[][] grid3 = {{1,2,3,4,5}}; // 单行
int[][] grid4 = {{1},{2},{3},{4},{5}}; // 单列
int[][] grid5 = new int[0][0]; // 空网格
```

### 2. 边界测试用例
- 最大数据规模测试
- 最小数据规模测试
- 特殊值测试（0、负数、极大值）
- 重复数据测试

### 3. 性能测试用例
- 大规模随机数据
- 最坏情况数据
- 平均情况数据
- 特殊分布数据

## 🚀 性能优化策略

### 1. 算法层面优化
- 选择合适的数据结构
- 减少不必要的计算
- 利用数学性质简化
- 分治思想应用

### 2. 代码层面优化
- 避免重复函数调用
- 减少对象创建
- 使用基本类型代替包装类
- 循环展开优化

### 3. 内存优化
- 对象复用
- 缓存策略
- 内存池技术
- 垃圾回收优化

---

## 📚 按平台分类（详细扩展）

### LeetCode (力扣) - 详细解析

#### 1. LeetCode 62. 不同路径
**题目链接**: https://leetcode.cn/problems/unique-paths/  
**问题描述**: 机器人从m×n网格的左上角移动到右下角，每次只能向右或向下移动一步，问有多少条不同的路径。

**解题思路**:
```java
// 动态规划解法
public int uniquePaths(int m, int n) {
    int[][] dp = new int[m][n];
    
    // 初始化第一行和第一列
    for (int i = 0; i < m; i++) dp[i][0] = 1;
    for (int j = 0; j < n; j++) dp[0][j] = 1;
    
    // 状态转移
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = dp[i-1][j] + dp[i][j-1];
        }
    }
    return dp[m-1][n-1];
}
```

**复杂度分析**:
- 时间复杂度: O(m×n)
- 空间复杂度: O(m×n)，可优化为O(min(m,n))

**优化版本**:
```java
// 空间优化版本
public int uniquePathsOptimized(int m, int n) {
    int[] dp = new int[n];
    Arrays.fill(dp, 1);
    
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[j] += dp[j-1];
        }
    }
    return dp[n-1];
}
```

#### 2. LeetCode 63. 不同路径 II
**题目链接**: https://leetcode.cn/problems/unique-paths-ii/  
**问题描述**: 在62题基础上，网格中有障碍物，障碍物位置不能通过。

**解题思路**:
```java
public int uniquePathsWithObstacles(int[][] obstacleGrid) {
    int m = obstacleGrid.length, n = obstacleGrid[0].length;
    int[][] dp = new int[m][n];
    
    // 初始化第一行和第一列（遇到障碍物则后面都为0）
    for (int i = 0; i < m && obstacleGrid[i][0] == 0; i++) {
        dp[i][0] = 1;
    }
    for (int j = 0; j < n && obstacleGrid[0][j] == 0; j++) {
        dp[0][j] = 1;
    }
    
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            if (obstacleGrid[i][j] == 0) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
    }
    return dp[m-1][n-1];
}
```

**关键点**: 遇到障碍物时，该位置的路径数为0。

#### 3. LeetCode 64. 最小路径和
**题目链接**: https://leetcode.cn/problems/minimum-path-sum/  
**问题描述**: 在m×n网格中找一条从左上角到右下角的路径，使得路径上的数字总和最小。

**解题思路**:
```java
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[][] dp = new int[m][n];
    
    dp[0][0] = grid[0][0];
    // 初始化第一行和第一列
    for (int i = 1; i < m; i++) dp[i][0] = dp[i-1][0] + grid[i][0];
    for (int j = 1; j < n; j++) dp[0][j] = dp[0][j-1] + grid[0][j];
    
    for (int i = 1; i < m; i++) {
        for (int j = 1; j < n; j++) {
            dp[i][j] = Math.min(dp[i-1][j], dp[i][j-1]) + grid[i][j];
        }
    }
    return dp[m-1][n-1];
}
```

**空间优化版本**:
```java
public int minPathSumOptimized(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    int[] dp = new int[n];
    
    dp[0] = grid[0][0];
    for (int j = 1; j < n; j++) dp[j] = dp[j-1] + grid[0][j];
    
    for (int i = 1; i < m; i++) {
        dp[0] += grid[i][0];
        for (int j = 1; j < n; j++) {
            dp[j] = Math.min(dp[j], dp[j-1]) + grid[i][j];
        }
    }
    return dp[n-1];
}
```

#### 4. LeetCode 72. 编辑距离
**题目链接**: https://leetcode.cn/problems/edit-distance/  
**问题描述**: 给定两个单词word1和word2，计算将word1转换成word2所使用的最少操作数。操作包括插入、删除、替换一个字符。

**解题思路**:
```java
public int minDistance(String word1, String word2) {
    int m = word1.length(), n = word2.length();
    int[][] dp = new int[m+1][n+1];
    
    // 初始化边界
    for (int i = 0; i <= m; i++) dp[i][0] = i;
    for (int j = 0; j <= n; j++) dp[0][j] = j;
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (word1.charAt(i-1) == word2.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1];
            } else {
                dp[i][j] = Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]) + 1;
            }
        }
    }
    return dp[m][n];
}
```

**状态转移解释**:
- `dp[i-1][j]`: 删除操作
- `dp[i][j-1]`: 插入操作  
- `dp[i-1][j-1]`: 替换操作

#### 5. LeetCode 115. 不同的子序列
**题目链接**: https://leetcode.cn/problems/distinct-subsequences/  
**问题描述**: 给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数。

**解题思路**:
```java
public int numDistinct(String s, String t) {
    int m = s.length(), n = t.length();
    int[][] dp = new int[m+1][n+1];
    
    // 初始化：空字符串是任何字符串的一个子序列
    for (int i = 0; i <= m; i++) dp[i][0] = 1;
    
    for (int i = 1; i <= m; i++) {
        for (int j = 1; j <= n; j++) {
            if (s.charAt(i-1) == t.charAt(j-1)) {
                dp[i][j] = dp[i-1][j-1] + dp[i-1][j];
            } else {
                dp[i][j] = dp[i-1][j];
            }
        }
    }
    return dp[m][n];
}
```

#### 6. LeetCode 120. 三角形最小路径和
**题目链接**: https://leetcode.cn/problems/triangle/  
**问题描述**: 给定一个三角形 triangle ，找出自顶向下的最小路径和。

**解题思路**:
```java
public int minimumTotal(List<List<Integer>> triangle) {
    int n = triangle.size();
    int[] dp = new int[n+1];
    
    // 从底向上计算
    for (int i = n-1; i >= 0; i--) {
        for (int j = 0; j <= i; j++) {
            dp[j] = Math.min(dp[j], dp[j+1]) + triangle.get(i).get(j);
        }
    }
    return dp[0];
}
```

#### 7. LeetCode 300. 最长递增子序列
**题目链接**: https://leetcode.cn/problems/longest-increasing-subsequence/  
**问题描述**: 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。

**解题思路**:
```java
public int lengthOfLIS(int[] nums) {
    if (nums == null || nums.length == 0) return 0;
    
    int[] dp = new int[nums.length];
    Arrays.fill(dp, 1);
    int maxLen = 1;
    
    for (int i = 1; i < nums.length; i++) {
        for (int j = 0; j < i; j++) {
            if (nums[i] > nums[j]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
        maxLen = Math.max(maxLen, dp[i]);
    }
    return maxLen;
}
```

**优化版本** (O(n log n)):
```java
public int lengthOfLISOptimized(int[] nums) {
    int[] tails = new int[nums.length];
    int size = 0;
    
    for (int x : nums) {
        int i = 0, j = size;
        while (i != j) {
            int m = (i + j) / 2;
            if (tails[m] < x) {
                i = m + 1;
            } else {
                j = m;
            }
        }
        tails[i] = x;
        if (i == size) size++;
    }
    return size;
}
```

#### 8. LeetCode 322. 零钱兑换
**题目链接**: https://leetcode.cn/problems/coin-change/  
**问题描述**: 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。计算并返回可以凑成总金额所需的最少的硬币个数。

**解题思路**:
```java
public int coinChange(int[] coins, int amount) {
    int[] dp = new int[amount+1];
    Arrays.fill(dp, amount+1);
    dp[0] = 0;
    
    for (int i = 1; i <= amount; i++) {
        for (int coin : coins) {
            if (i >= coin) {
                dp[i] = Math.min(dp[i], dp[i-coin] + 1);
            }
        }
    }
    return dp[amount] > amount ? -1 : dp[amount];
}
```

#### 9. LeetCode 198. 打家劫舍
**题目链接**: https://leetcode.cn/problems/house-robber/  
**问题描述**: 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。

**解题思路**:
```java
public int rob(int[] nums) {
    if (nums.length == 0) return 0;
    if (nums.length == 1) return nums[0];
    
    int prev2 = nums[0];
    int prev1 = Math.max(nums[0], nums[1]);
    
    for (int i = 2; i < nums.length; i++) {
        int current = Math.max(prev1, prev2 + nums[i]);
        prev2 = prev1;
        prev1 = current;
    }
    return prev1;
}
```

#### 10. LeetCode 124. 二叉树中的最大路径和
**题目链接**: https://leetcode.cn/problems/binary-tree-maximum-path-sum/  
**问题描述**: 二叉树中的路径被定义为一条节点序列，序列中每对相邻节点之间都存在一条边。同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。

**解题思路**:
```java
public int maxPathSum(TreeNode root) {
    int[] maxSum = {Integer.MIN_VALUE};
    maxPathSumHelper(root, maxSum);
    return maxSum[0];
}

private int maxPathSumHelper(TreeNode node, int[] maxSum) {
    if (node == null) return 0;
    
    // 递归计算左右子树的最大贡献值
    int leftGain = Math.max(maxPathSumHelper(node.left, maxSum), 0);
    int rightGain = Math.max(maxPathSumHelper(node.right, maxSum), 0);
    
    // 节点的最大路径和取决于该节点的值与该节点的左右子节点的最大贡献值
    int priceNewpath = node.val + leftGain + rightGain;
    
    // 更新答案
    maxSum[0] = Math.max(maxSum[0], priceNewpath);
    
    // 返回节点的最大贡献值
    return node.val + Math.max(leftGain, rightGain);
}
```

### LintCode (炼码) - 精选题目

### LeetCode (力扣)
1. **LeetCode 62. 不同路径** - https://leetcode.cn/problems/unique-paths/
   - 类型：线性DP
   - 难度：中等

2. **LeetCode 63. 不同路径 II** - https://leetcode.cn/problems/unique-paths-ii/
   - 类型：线性DP
   - 难度：中等

3. **LeetCode 64. 最小路径和** - https://leetcode.cn/problems/minimum-path-sum/
   - 类型：线性DP
   - 难度：中等

4. **LeetCode 72. 编辑距离** - https://leetcode.cn/problems/edit-distance/
   - 类型：字符串DP
   - 难度：困难

5. **LeetCode 97. 交错字符串** - https://leetcode.cn/problems/interleaving-string/
   - 类型：字符串DP
   - 难度：困难

6. **LeetCode 115. 不同的子序列** - https://leetcode.cn/problems/distinct-subsequences/
   - 类型：字符串DP
   - 难度：困难

7. **LeetCode 120. 三角形最小路径和** - https://leetcode.cn/problems/triangle/
   - 类型：线性DP
   - 难度：中等

8. **LeetCode 132. 分割回文串 II** - https://leetcode.cn/problems/palindrome-partitioning-ii/
   - 类型：区间DP
   - 难度：困难

9. **LeetCode 174. 地下城游戏** - https://leetcode.cn/problems/dungeon-game/
   - 类型：线性DP
   - 难度：困难

10. **LeetCode 221. 最大正方形** - https://leetcode.cn/problems/maximal-square/
    - 类型：线性DP
    - 难度：中等

11. **LeetCode 300. 最长递增子序列** - https://leetcode.cn/problems/longest-increasing-subsequence/
    - 类型：线性DP
    - 难度：中等

12. **LeetCode 312. 戳气球** - https://leetcode.cn/problems/burst-balloons/
    - 类型：区间DP
    - 难度：困难

13. **LeetCode 322. 零钱兑换** - https://leetcode.cn/problems/coin-change/
    - 类型：背包DP
    - 难度：中等

14. **LeetCode 329. 矩阵中的最长递增路径** - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
    - 类型：记忆化搜索
    - 难度：困难

15. **LeetCode 368. 最大整除子集** - https://leetcode.cn/problems/largest-divisible-subset/
    - 类型：线性DP
    - 难度：中等

16. **LeetCode 410. 分割数组的最大值** - https://leetcode.cn/problems/split-array-largest-sum/
    - 类型：二分答案+DP
    - 难度：困难

17. **LeetCode 416. 分割等和子集** - https://leetcode.cn/problems/partition-equal-subset-sum/
    - 类型：背包DP
    - 难度：中等

18. **LeetCode 486. 预测赢家** - https://leetcode.cn/problems/predict-the-winner/
    - 类型：区间DP
    - 难度：中等

19. **LeetCode 494. 目标和** - https://leetcode.cn/problems/target-sum/
    - 类型：背包DP
    - 难度：中等

20. **LeetCode 516. 最长回文子序列** - https://leetcode.cn/problems/longest-palindromic-subsequence/
    - 类型：区间DP
    - 难度：中等

21. **LeetCode 583. 两个字符串的删除操作** - https://leetcode.cn/problems/delete-operation-for-two-strings/
    - 类型：字符串DP
    - 难度：中等

22. **LeetCode 647. 回文子串** - https://leetcode.cn/problems/palindromic-substrings/
    - 类型：区间DP
    - 难度：中等

23. **LeetCode 712. 两个字符串的最小ASCII删除和** - https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/
    - 类型：字符串DP
    - 难度：中等

24. **LeetCode 718. 最长重复子数组** - https://leetcode.cn/problems/maximum-length-of-repeated-subarray/
    - 类型：线性DP
    - 难度：中等

25. **LeetCode 746. 使用最小花费爬楼梯** - https://leetcode.cn/problems/min-cost-climbing-stairs/
    - 类型：线性DP
    - 难度：简单

26. **LeetCode 871. 最低加油次数** - https://leetcode.cn/problems/minimum-number-of-refueling-stops/
    - 类型：背包DP
    - 难度：困难

27. **LeetCode 931. 下降路径最小和** - https://leetcode.cn/problems/minimum-falling-path-sum/
    - 类型：线性DP
    - 难度：中等

28. **LeetCode 1035. 不相交的线** - https://leetcode.cn/problems/uncrossed-lines/
    - 类型：字符串DP
    - 难度：中等

29. **LeetCode 1143. 最长公共子序列** - https://leetcode.cn/problems/longest-common-subsequence/
    - 类型：字符串DP
    - 难度：中等

30. **LeetCode 1277. 统计全为 1 的正方形子矩阵** - https://leetcode.cn/problems/count-square-submatrices-with-all-ones/
    - 类型：线性DP
    - 难度：中等

### LintCode (炼码) - 详细解析

#### 1. LintCode 109. 数字三角形
**题目链接**: https://www.lintcode.com/problem/triangle/  
**问题描述**: 给定一个数字三角形，找到从顶部到底部的最小路径和。每一步可以移动到下一行相邻的数字。

**解题思路**:
```java
public int minimumTotal(int[][] triangle) {
    int n = triangle.length;
    int[][] dp = new int[n][n];
    
    // 初始化最后一行
    for (int j = 0; j < n; j++) {
        dp[n-1][j] = triangle[n-1][j];
    }
    
    // 从底向上计算
    for (int i = n-2; i >= 0; i--) {
        for (int j = 0; j <= i; j++) {
            dp[i][j] = Math.min(dp[i+1][j], dp[i+1][j+1]) + triangle[i][j];
        }
    }
    return dp[0][0];
}
```

**优化版本**:
```java
public int minimumTotalOptimized(int[][] triangle) {
    int n = triangle.length;
    int[] dp = new int[n];
    
    // 初始化最后一行
    for (int j = 0; j < n; j++) {
        dp[j] = triangle[n-1][j];
    }
    
    // 从底向上计算，空间优化
    for (int i = n-2; i >= 0; i--) {
        for (int j = 0; j <= i; j++) {
            dp[j] = Math.min(dp[j], dp[j+1]) + triangle[i][j];
        }
    }
    return dp[0];
}
```

#### 2. LintCode 110. 最小路径和
与LeetCode 64题相同，但测试数据可能不同。

#### 3. LintCode 118. 不同的路径
与LeetCode 62题相同。

#### 4. LintCode 119. 编辑距离
与LeetCode 72题相同。

#### 5. LintCode 163. 不同的路径 II
与LeetCode 63题相同。

### HackerRank - 国际平台题目详解

#### 1. HackerRank - The Coin Change Problem
**题目链接**: https://www.hackerrank.com/challenges/coin-change/problem  
**问题描述**: 给定不同面额的硬币和一个总金额，计算可以凑成总金额的硬币组合数。

**解题思路**:
```java
public static long getWays(int n, List<Long> c) {
    long[] dp = new long[n+1];
    dp[0] = 1;
    
    for (long coin : c) {
        for (long j = coin; j <= n; j++) {
            dp[(int)j] += dp[(int)(j - coin)];
        }
    }
    return dp[n];
}
```

**关键点**: 这是完全背包问题的变种，注意组合数的计算顺序。

#### 2. HackerRank - Sherlock and Cost
**题目链接**: https://www.hackerrank.com/challenges/sherlock-and-cost/problem  
**问题描述**: 给定数组B，构造数组A使得1 ≤ A[i] ≤ B[i]，且∑|A[i] - A[i-1]|最大。

**解题思路**:
```java
public static int cost(List<Integer> B) {
    int n = B.size();
    int[][] dp = new int[n][2]; // dp[i][0]: A[i]=1, dp[i][1]: A[i]=B[i]
    
    for (int i = 1; i < n; i++) {
        // A[i] = 1
        dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + Math.abs(1 - B.get(i-1)));
        // A[i] = B[i]
        dp[i][1] = Math.max(dp[i-1][0] + Math.abs(B.get(i) - 1), 
                           dp[i-1][1] + Math.abs(B.get(i) - B.get(i-1)));
    }
    return Math.max(dp[n-1][0], dp[n-1][1]);
}
```

### 牛客网 (Nowcoder) - 国内平台题目

#### 1. 牛客网 - 背包问题
**题目链接**: https://www.nowcoder.com/practice/02b6d3a72fe54c59b2fc99fb80e7e5dc  
**问题描述**: 标准的0-1背包问题实现。

**解题思路**:
```java
public int knapsack(int V, int n, int[][] vw) {
    int[] dp = new int[V+1];
    
    for (int i = 0; i < n; i++) {
        int v = vw[i][0], w = vw[i][1];
        for (int j = V; j >= v; j--) {
            dp[j] = Math.max(dp[j], dp[j-v] + w);
        }
    }
    return dp[V];
}
```

#### 2. 牛客网 - 最长递增子序列
**题目链接**: https://www.nowcoder.com/practice/9cf027bf54714ad889d4f30ff0ae5481  
**问题描述**: 求数组的最长递增子序列长度。

**解题思路**:
```java
public int LIS(int[] arr) {
    if (arr == null || arr.length == 0) return 0;
    
    int[] dp = new int[arr.length];
    Arrays.fill(dp, 1);
    int maxLen = 1;
    
    for (int i = 1; i < arr.length; i++) {
        for (int j = 0; j < i; j++) {
            if (arr[i] > arr[j]) {
                dp[i] = Math.max(dp[i], dp[j] + 1);
            }
        }
        maxLen = Math.max(maxLen, dp[i]);
    }
    return maxLen;
}
```

**优化版本** (O(n log n)):
```java
public int LISOptimized(int[] arr) {
    int[] tails = new int[arr.length];
    int size = 0;
    
    for (int x : arr) {
        int i = 0, j = size;
        while (i != j) {
            int m = (i + j) / 2;
            if (tails[m] < x) {
                i = m + 1;
            } else {
                j = m;
            }
        }
        tails[i] = x;
        if (i == size) size++;
    }
    return size;
}
```

### 洛谷 (Luogu) - 国内OJ平台

#### 1. 洛谷 P1048 采药
**题目链接**: https://www.luogu.com.cn/problem/P1048  
**问题描述**: 标准的0-1背包问题。

**解题思路**:
```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt(), M = sc.nextInt();
        int[] dp = new int[T+1];
        
        for (int i = 0; i < M; i++) {
            int t = sc.nextInt(), v = sc.nextInt();
            for (int j = T; j >= t; j--) {
                dp[j] = Math.max(dp[j], dp[j-t] + v);
            }
        }
        System.out.println(dp[T]);
    }
}
```

#### 2. 洛谷 P1216 数字三角形
**题目链接**: https://www.luogu.com.cn/problem/P1216  
**问题描述**: 数字三角形最大路径和。

**解题思路**:
```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] triangle = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                triangle[i][j] = sc.nextInt();
            }
        }
        
        // 从底向上计算
        for (int i = n-2; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                triangle[i][j] += Math.max(triangle[i+1][j], triangle[i+1][j+1]);
            }
        }
        System.out.println(triangle[0][0]);
    }
}
```

### Codeforces - 国际竞赛平台

#### 1. Codeforces 455A - Boredom
**题目链接**: https://codeforces.com/problemset/problem/455/A  
**问题描述**: 给定数组，选择元素x获得x分，但不能再选择x-1和x+1，求最大得分。

**解题思路**:
```java
public static long solve(int[] arr) {
    int maxVal = 100000;
    long[] count = new long[maxVal+1];
    
    for (int num : arr) {
        count[num]++;
    }
    
    long[] dp = new long[maxVal+1];
    dp[1] = count[1];
    
    for (int i = 2; i <= maxVal; i++) {
        dp[i] = Math.max(dp[i-1], dp[i-2] + count[i] * i);
    }
    return dp[maxVal];
}
```

#### 2. Codeforces 429B - Working out
**题目链接**: https://codeforces.com/problemset/problem/429/B  
**问题描述**: 两个人在网格中行走，求不相交路径的最大和。

**解题思路**:
```java
public static long solve(int[][] grid) {
    int n = grid.length, m = grid[0].length;
    long[][][] dp = new long[4][n+2][m+2];
    
    // 四个方向的DP
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= m; j++) {
            dp[0][i][j] = grid[i-1][j-1] + Math.max(dp[0][i-1][j], dp[0][i][j-1]);
        }
    }
    
    for (int i = 1; i <= n; i++) {
        for (int j = m; j >= 1; j--) {
            dp[1][i][j] = grid[i-1][j-1] + Math.max(dp[1][i-1][j], dp[1][i][j+1]);
        }
    }
    
    for (int i = n; i >= 1; i--) {
        for (int j = 1; j <= m; j++) {
            dp[2][i][j] = grid[i-1][j-1] + Math.max(dp[2][i+1][j], dp[2][i][j-1]);
        }
    }
    
    for (int i = n; i >= 1; i--) {
        for (int j = m; j >= 1; j--) {
            dp[3][i][j] = grid[i-1][j-1] + Math.max(dp[3][i+1][j], dp[3][i][j+1]);
        }
    }
    
    long ans = 0;
    for (int i = 2; i < n; i++) {
        for (int j = 2; j < m; j++) {
            long case1 = dp[0][i-1][j] + dp[3][i+1][j] + dp[2][i][j-1] + dp[1][i][j+1];
            long case2 = dp[0][i][j-1] + dp[3][i][j+1] + dp[2][i+1][j] + dp[1][i-1][j];
            ans = Math.max(ans, Math.max(case1, case2));
        }
    }
    return ans;
}
```

### AtCoder - 日本竞赛平台

#### 1. AtCoder DP Contest A - Frog 1
**题目链接**: https://atcoder.jp/contests/dp/tasks/dp_a  
**问题描述**: 青蛙跳石头，每次跳1或2步，求最小代价。

**解题思路**:
```java
public static int solve(int[] h) {
    int n = h.length;
    int[] dp = new int[n];
    dp[0] = 0;
    dp[1] = Math.abs(h[1] - h[0]);
    
    for (int i = 2; i < n; i++) {
        dp[i] = Math.min(dp[i-1] + Math.abs(h[i] - h[i-1]), 
                        dp[i-2] + Math.abs(h[i] - h[i-2]));
    }
    return dp[n-1];
}
```

#### 2. AtCoder DP Contest D - Knapsack 1
**题目链接**: https://atcoder.jp/contests/dp/tasks/dp_d  
**问题描述**: 标准0-1背包问题。

**解题思路**:
```java
public static long solve(int n, int W, int[] w, int[] v) {
    long[] dp = new long[W+1];
    
    for (int i = 0; i < n; i++) {
        for (int j = W; j >= w[i]; j--) {
            dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
        }
    }
    return dp[W];
}
```

### POJ (北京大学在线评测系统)

#### 1. POJ 1088 滑雪
**题目链接**: http://poj.org/problem?id=1088  
**问题描述**: 在矩阵中找到最长的递减路径。

**解题思路** (记忆化搜索):
```java
public class Ski {
    private int[][] matrix, memo;
    private int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
    
    public int longestSkiPath(int[][] matrix) {
        this.matrix = matrix;
        int m = matrix.length, n = matrix[0].length;
        memo = new int[m][n];
        int maxLen = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                maxLen = Math.max(maxLen, dfs(i, j));
            }
        }
        return maxLen;
    }
    
    private int dfs(int i, int j) {
        if (memo[i][j] != 0) return memo[i][j];
        
        int maxLen = 1;
        for (int[] dir : dirs) {
            int x = i + dir[0], y = j + dir[1];
            if (x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length && 
                matrix[x][y] < matrix[i][j]) {
                maxLen = Math.max(maxLen, 1 + dfs(x, y));
            }
        }
        return memo[i][j] = maxLen;
    }
}
```

### HDU (杭州电子科技大学)

#### 1. HDU 1024 Max Sum Plus Plus
**题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1024  
**问题描述**: 将数组分成m个不相交子数组，求最大和。

**解题思路**:
```java
public static long solve(int m, int[] arr) {
    int n = arr.length;
    long[] dp = new long[n+1];
    long[] preMax = new long[n+1];
    long maxSum = Long.MIN_VALUE;
    
    for (int i = 1; i <= m; i++) {
        maxSum = Long.MIN_VALUE;
        for (int j = i; j <= n; j++) {
            dp[j] = Math.max(dp[j-1], preMax[j-1]) + arr[j-1];
            preMax[j-1] = maxSum;
            maxSum = Math.max(maxSum, dp[j]);
        }
    }
    return maxSum;
}
```

### 补充题目实现文件

接下来创建补充题目的具体实现文件...

### HackerRank
1. **HackerRank - The Coin Change Problem** - https://www.hackerrank.com/challenges/coin-change/problem
   - 类型：背包DP
   - 难度：中等

2. **HackerRank - Sherlock and Cost** - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
   - 类型：线性DP
   - 难度：中等

3. **HackerRank - Candies** - https://www.hackerrank.com/challenges/candies/problem
   - 类型：线性DP
   - 难度：困难

4. **HackerRank - Max Array Sum** - https://www.hackerrank.com/challenges/max-array-sum/problem
   - 类型：线性DP
   - 难度：中等

5. **HackerRank - Abbreviation** - https://www.hackerrank.com/challenges/abbr/problem
   - 类型：字符串DP
   - 难度：中等

### 牛客网 (Nowcoder)
1. **牛客网 - 背包问题** - https://www.nowcoder.com/practice/02b6d3a72fe54c59b2fc99fb80e7e5dc
   - 类型：背包DP
   - 难度：中等

2. **牛客网 - 最长递增子序列** - https://www.nowcoder.com/practice/9cf027bf54714ad889d4f30ff0ae5481
   - 类型：线性DP
   - 难度：中等

3. **牛客网 - 编辑距离** - https://www.nowcoder.com/practice/9d29902cd8e9405a86703a76c6707c97
   - 类型：字符串DP
   - 难度：困难

### 洛谷 (Luogu)
1. **洛谷 P1048 [NOIP2005 普及组] 采药** - https://www.luogu.com.cn/problem/P1048
   - 类型：背包DP
   - 难度：普及-

2. **洛谷 P1049 [NOIP2001 普及组] 装箱问题** - https://www.luogu.com.cn/problem/P1049
   - 类型：背包DP
   - 难度：普及-

3. **洛谷 P1115 最大子段和** - https://www.luogu.com.cn/problem/P1115
   - 类型：线性DP
   - 难度：普及/提高-

4. **洛谷 P1216 [USACO1.5][IOI1994]数字三角形 Number Triangles** - https://www.luogu.com.cn/problem/P1216
   - 类型：线性DP
   - 难度：普及/提高-

5. **洛谷 P1220 关路灯** - https://www.luogu.com.cn/problem/P1220
   - 类型：区间DP
   - 难度：提高+/省选-

6. **洛谷 P1434 [SHOI2010]滑雪** - https://www.luogu.com.cn/problem/P1434
   - 类型：记忆化搜索
   - 难度：普及-

7. **洛谷 P1508 Likecloud-吃、吃、吃** - https://www.luogu.com.cn/problem/P1508
   - 类型：线性DP
   - 难度：普及-

8. **洛谷 P1880 [NOI1995] 石子合并** - https://www.luogu.com.cn/problem/P1880
   - 类型：区间DP
   - 难度：提高+/省选-

### Codeforces
1. **Codeforces 455A - Boredom** - https://codeforces.com/problemset/problem/455/A
   - 类型：线性DP
   - 难度：1500

2. **Codeforces 429B - Working out** - https://codeforces.com/problemset/problem/429/B
   - 类型：线性DP
   - 难度：2000

3. **Codeforces 118D - Caesar's Legions** - https://codeforces.com/problemset/problem/118/D
   - 类型：线性DP
   - 难度：1700

4. **Codeforces 466C - Number of Ways** - https://codeforces.com/problemset/problem/466/C
   - 类型：线性DP
   - 难度：1700

5. **Codeforces 489C - Given Length and Sum of Digits...** - https://codeforces.com/problemset/problem/489/C
   - 类型：线性DP
   - 难度：1700

### AtCoder
1. **AtCoder ABC040 C - 柱柱柱柱柱** - https://atcoder.jp/contests/abc040/tasks/abc040_c
   - 类型：线性DP
   - 难度：灰

2. **AtCoder ABC129 C - Typical Stairs** - https://atcoder.jp/contests/abc129/tasks/abc129_c
   - 类型：线性DP
   - 难度：茶

3. **AtCoder DP Contest A - Frog 1** - https://atcoder.jp/contests/dp/tasks/dp_a
   - 类型：线性DP
   - 难度：灰

4. **AtCoder DP Contest B - Frog 2** - https://atcoder.jp/contests/dp/tasks/dp_b
   - 类型：线性DP
   - 难度：茶

5. **AtCoder DP Contest C - Vacation** - https://atcoder.jp/contests/dp/tasks/dp_c
   - 类型：线性DP
   - 难度：茶

6. **AtCoder DP Contest D - Knapsack 1** - https://atcoder.jp/contests/dp/tasks/dp_d
   - 类型：背包DP
   - 难度：绿

7. **AtCoder DP Contest E - Knapsack 2** - https://atcoder.jp/contests/dp/tasks/dp_e
   - 类型：背包DP
   - 难度：绿

8. **AtCoder DP Contest F - LCS** - https://atcoder.jp/contests/dp/tasks/dp_f
   - 类型：字符串DP
   - 难度：黄

9. **AtCoder DP Contest G - Longest Path** - https://atcoder.jp/contests/dp/tasks/dp_g
   - 类型：记忆化搜索
   - 难度：绿

10. **AtCoder DP Contest H - Grid 1** - https://atcoder.jp/contests/dp/tasks/dp_h
    - 类型：线性DP
    - 难度：绿

11. **AtCoder DP Contest I - Coins** - https://atcoder.jp/contests/dp/tasks/dp_i
    - 类型：概率DP
    - 难度：黄

12. **AtCoder DP Contest J - Sushi** - https://atcoder.jp/contests/dp/tasks/dp_j
    - 类型：概率DP
    - 难度：橙

13. **AtCoder DP Contest K - Stones** - https://atcoder.jp/contests/dp/tasks/dp_k
    - 类型：博弈DP
    - 难度：茶

14. **AtCoder DP Contest L - Deque** - https://atcoder.jp/contests/dp/tasks/dp_l
    - 类型：区间DP
    - 难度：橙

15. **AtCoder DP Contest M - Candies** - https://atcoder.jp/contests/dp/tasks/dp_m
    - 类型：背包DP
    - 难度：橙

16. **AtCoder DP Contest N - Slimes** - https://atcoder.jp/contests/dp/tasks/dp_n
    - 类型：区间DP
    - 难度：绿

17. **AtCoder DP Contest O - Matching** - https://atcoder.jp/contests/dp/tasks/dp_o
    - 类型：状态压缩DP
    - 难度：橙

18. **AtCoder DP Contest P - Independent Set** - https://atcoder.jp/contests/dp/tasks/dp_p
    - 类型：树形DP
    - 难度：绿

19. **AtCoder DP Contest Q - Flowers** - https://atcoder.jp/contests/dp/tasks/dp_q
    - 类型：线性DP
    - 难度：黄

20. **AtCoder DP Contest R - Walk** - https://atcoder.jp/contests/dp/tasks/dp_r
    - 类型：矩阵快速幂+DP
    - 难度：橙

### POJ (北京大学在线评测系统)
1. **POJ 1088 滑雪** - http://poj.org/problem?id=1088
   - 类型：记忆化搜索
   - 难度：中等

2. **POJ 1159 Palindrome** - http://poj.org/problem?id=1159
   - 类型：字符串DP
   - 难度：中等

3. **POJ 1163 The Triangle** - http://poj.org/problem?id=1163
   - 类型：线性DP
   - 难度：简单

4. **POJ 1661 Help Jimmy** - http://poj.org/problem?id=1661
   - 类型：线性DP
   - 难度：中等

5. **POJ 2533 Longest Ordered Subsequence** - http://poj.org/problem?id=2533
   - 类型：线性DP
   - 难度：简单

### HDU (杭州电子科技大学在线评测系统)
1. **HDU 1024 Max Sum Plus Plus** - http://acm.hdu.edu.cn/showproblem.php?pid=1024
   - 类型：线性DP
   - 难度：困难

2. **HDU 1029 Ignatius and the Princess IV** - http://acm.hdu.edu.cn/showproblem.php?pid=1029
   - 类型：线性DP
   - 难度：中等

3. **HDU 1069 Monkey and Banana** - http://acm.hdu.edu.cn/showproblem.php?pid=1069
   - 类型：线性DP
   - 难度：中等

4. **HDU 1074 Doing Homework** - http://acm.hdu.edu.cn/showproblem.php?pid=1074
   - 类型：状态压缩DP
   - 难度：困难

5. **HDU 1087 Super Jumping! Jumping! Jumping!** - http://acm.hdu.edu.cn/showproblem.php?pid=1087
   - 类型：线性DP
   - 难度：中等

### ZOJ (浙江大学在线评测系统)
1. **ZOJ 1074 To the Max** - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364593
   - 类型：线性DP
   - 难度：中等

2. **ZOJ 1093 Monkey and Banana** - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364592
   - 类型：线性DP
   - 难度：中等

### CodeChef
1. **CodeChef - TACHSTCK** - https://www.codechef.com/problems/TACHSTCK
   - 类型：线性DP
   - 难度：简单

2. **CodeChef - DELISH** - https://www.codechef.com/problems/DELISH
   - 类型：线性DP
   - 难度：中等

### SPOJ
1. **SPOJ - EDIST** - https://www.spoj.com/problems/EDIST/
   - 类型：字符串DP
   - 难度：中等

2. **SPOJ - ACODE** - https://www.spoj.com/problems/ACODE/
   - 类型：线性DP
   - 难度：中等

### Project Euler
1. **Project Euler 15: Lattice paths** - https://projecteuler.net/problem=15
   - 类型：线性DP
   - 难度：中等

2. **Project Euler 67: Maximum path sum II** - https://projecteuler.net/problem=67
   - 类型：线性DP
   - 难度：中等

## 🎯 学习建议

1. **循序渐进**: 从简单题目开始，逐步挑战困难题目
2. **分类练习**: 按类型练习，掌握每种DP类型的解题套路
3. **总结归纳**: 每做完一类题目后总结规律和技巧
4. **多语言实现**: 用不同编程语言实现，加深理解
5. **性能分析**: 分析时间复杂度和空间复杂度，掌握优化方法

---
**最后更新时间**：2025-10-28  
**作者**：AI Assistant
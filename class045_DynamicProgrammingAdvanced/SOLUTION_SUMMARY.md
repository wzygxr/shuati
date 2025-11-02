# Class067: 动态规划进阶专题 - 解题思路与技巧总结

## 🧠 动态规划核心思想

动态规划是一种通过把原问题分解为相对简单的子问题的方式求解复杂问题的方法。动态规划常常适用于有重叠子问题和最优子结构性质的问题。

### 核心要素
1. **状态定义**: 确定dp数组的含义
2. **状态转移方程**: 找到状态之间的关系
3. **初始化**: 确定初始状态的值
4. **填表顺序**: 确定计算顺序
5. **返回值**: 确定最终答案

## 📚 题型分类与解法

### 1. 线性DP

#### 特点
- 状态沿着一个方向递推，如数组、字符串
- 状态转移通常只依赖于前几个状态

#### 典型题目
- 最小路径和
- 最长公共子序列
- 最长递增子序列

#### 解题思路
1. 确定状态：dp[i]或dp[i][j]表示什么含义
2. 状态转移：当前状态如何从前一个或几个状态推导出来
3. 边界处理：确定初始状态值
4. 优化：考虑空间压缩

#### 代码模板
```java
// 一维线性DP
int[] dp = new int[n];
dp[0] = initialValue;
for (int i = 1; i < n; i++) {
    dp[i] = transitionFunction(dp[i-1], ...);
}
return dp[n-1];

// 二维线性DP
int[][] dp = new int[n][m];
// 初始化边界
for (int i = 0; i < n; i++) {
    for (int j = 0; j < m; j++) {
        dp[i][j] = transitionFunction(dp[i-1][j], dp[i][j-1], ...);
    }
}
return dp[n-1][m-1];
```

### 2. 区间DP

#### 特点
- 状态表示区间[i,j]的最优值
- 通过枚举分割点k进行状态转移

#### 典型题目
- 最长回文子序列
- 石子合并
- 矩阵链乘法

#### 解题思路
1. 状态定义：dp[i][j]表示区间[i,j]的最优值
2. 枚举长度：从小到大枚举区间长度
3. 枚举起点：确定区间起点
4. 枚举分割点：在区间内枚举分割点k
5. 状态转移：dp[i][j] = optimal(dp[i][k] + dp[k+1][j] + cost)

#### 代码模板
```java
// 区间DP
int[][] dp = new int[n][n];
// 初始化长度为1的区间
for (int i = 0; i < n; i++) {
    dp[i][i] = initialValue;
}

// 枚举长度
for (int len = 2; len <= n; len++) {
    // 枚举起点
    for (int i = 0; i <= n - len; i++) {
        int j = i + len - 1;
        dp[i][j] = initialValue;
        // 枚举分割点
        for (int k = i; k < j; k++) {
            dp[i][j] = optimal(dp[i][j], dp[i][k] + dp[k+1][j] + cost);
        }
    }
}
```

### 3. 树形DP

#### 特点
- 在树结构上进行动态规划
- 状态表示以某节点为根的子树信息

#### 典型题目
- 节点数为n高度不大于m的二叉树个数
- 没有上司的舞会
- 树的直径

#### 解题思路
1. 状态定义：dp[u]表示以节点u为根的子树的最优值
2. DFS遍历：通过深度优先搜索遍历树
3. 状态转移：根据子节点的dp值更新当前节点的dp值
4. 边界处理：叶子节点的dp值

#### 代码模板
```java
// 树形DP
void dfs(int u, int parent) {
    // 初始化当前节点的dp值
    dp[u] = initialValue;
    
    // 遍历子节点
    for (int v : tree[u]) {
        if (v != parent) {
            dfs(v, u);
            // 根据子节点更新当前节点
            dp[u] = transitionFunction(dp[u], dp[v]);
        }
    }
}
```

### 4. 记忆化搜索

#### 特点
- 从顶向下递归计算
- 通过缓存避免重复计算
- 适用于状态转移方向不明确的问题

#### 典型题目
- 矩阵中的最长递增路径
- 滑雪
- 括号匹配

#### 解题思路
1. 状态定义：确定搜索状态
2. 边界条件：确定递归终止条件
3. 记忆化：使用数组或哈希表缓存已计算结果
4. 递归计算：通过递归计算状态值

#### 代码模板
```java
// 记忆化搜索
int[][] memo = new int[n][m];
for (int i = 0; i < n; i++) {
    Arrays.fill(memo[i], -1); // -1表示未计算
}

int dfs(int state) {
    // 如果已计算，直接返回
    if (memo[state] != -1) {
        return memo[state];
    }
    
    // 边界条件
    if (baseCondition) {
        return memo[state] = baseValue;
    }
    
    // 递归计算
    int result = 0;
    for (int next : nextStates) {
        result = optimal(result, dfs(next));
    }
    
    // 缓存结果并返回
    return memo[state] = result;
}
```

## ⚙️ 优化技巧

### 1. 空间优化

#### 滚动数组
当当前状态只依赖于前几行/列的状态时，可以使用滚动数组优化空间。

```java
// 原始版本 O(m*n)空间
int[][] dp = new int[m][n];
// ...

// 优化版本 O(n)空间
int[] dp = new int[n];
// ...
```

#### 变量替换
当当前状态只依赖于少数几个状态时，可以用变量替换数组。

```java
// 原始版本 O(n)空间
int[] dp = new int[n];
// ...

// 优化版本 O(1)空间
int prev1 = 0, prev2 = 0, current = 0;
// ...
```

### 2. 时间优化

#### 前缀和/差分
在需要频繁计算区间和的场景中，使用前缀和优化。

```java
// 预处理前缀和
int[] prefixSum = new int[n+1];
for (int i = 0; i < n; i++) {
    prefixSum[i+1] = prefixSum[i] + arr[i];
}

// O(1)时间计算区间和
int rangeSum = prefixSum[r+1] - prefixSum[l];
```

#### 单调队列/单调栈
在需要维护区间最值的场景中，使用单调队列或单调栈优化。

## 🎯 解题步骤

### 1. 问题分析
- 确定问题类型（线性DP、区间DP、树形DP等）
- 分析状态转移关系
- 确定边界条件

### 2. 状态设计
- 明确dp数组的含义
- 确定状态维度
- 考虑状态压缩的可能性

### 3. 状态转移
- 写出状态转移方程
- 确定填表顺序
- 处理边界情况

### 4. 优化实现
- 考虑空间优化
- 考虑时间优化
- 编写清晰的代码

### 5. 测试验证
- 编写测试用例
- 验证边界条件
- 检查算法正确性

## 📈 复杂度分析

### 时间复杂度
- 线性DP：O(n) 到 O(n²)
- 区间DP：O(n³)
- 树形DP：O(n)
- 记忆化搜索：取决于状态数和转移复杂度

### 空间复杂度
- 基础版本：O(n) 到 O(n²)
- 优化版本：O(1) 到 O(n)

## 🛠️ 工程化实践

### 1. 异常处理
```java
// 输入验证
if (arr == null || arr.length == 0) {
    return 0;
}

// 边界处理
if (n == 1) {
    return arr[0];
}
```

### 2. 性能优化
```java
// 使用StringBuilder优化字符串拼接
StringBuilder sb = new StringBuilder();

// 预分配集合大小
List<Integer> list = new ArrayList<>(capacity);
```

### 3. 代码可读性
```java
// 使用有意义的变量名
int maxProfit = 0;
int minPrice = Integer.MAX_VALUE;

// 添加注释说明关键步骤
// 状态转移：选择当前元素或不选择
dp[i] = Math.max(dp[i-1], dp[i-2] + nums[i]);
```

## 📚 学习资源推荐

### 书籍
1. 《算法导论》- Thomas H. Cormen等
2. 《算法竞赛入门经典》- 刘汝佳
3. 《挑战程序设计竞赛》- 秋叶拓哉等

### 在线平台
1. **LeetCode**: https://leetcode.cn/
2. **Codeforces**: https://codeforces.com/
3. **AtCoder**: https://atcoder.jp/
4. **洛谷**: https://www.luogu.com.cn/

### 视频教程
1. **B站算法区**: 各种算法讲解视频
2. **YouTube**: MIT 6.006 Introduction to Algorithms

## 🎓 学习路径建议

### 第一阶段：基础掌握
1. 理解动态规划基本思想
2. 掌握状态定义和转移方程
3. 完成所有简单题目

### 第二阶段：类型熟悉
1. 理解各类DP问题的特征
2. 掌握优化技巧
3. 完成中等难度题目

### 第三阶段：高阶应用
1. 学习状态压缩、数位DP等高级技巧
2. 掌握实际应用中的变种问题
3. 完成困难题目

---
**最后更新时间**：2025-10-18  
**作者**：AI Assistant
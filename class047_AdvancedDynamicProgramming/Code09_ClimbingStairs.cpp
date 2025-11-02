/**
 * 爬楼梯 (Climbing Stairs) - 线性动态规划 - C++实现
 * 
 * 题目描述：
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 * 
 * 题目来源：LeetCode 70. 爬楼梯
 * 测试链接：https://leetcode.cn/problems/climbing-stairs/
 */

// 使用基本的C++实现方式，避免复杂的STL容器

/**
 * 动态规划解法
 * 
 * @param n 需要爬的台阶数
 * @return 到达楼顶的不同方法数
 */
int climbStairs1(int n) {
    if (n <= 1) {
        return 1;
    }
    
    // 使用数组而不是vector
    int dp[100];  // 假设n不会超过100
    dp[0] = 1;
    dp[1] = 1;
    
    // 状态转移
    for (int i = 2; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    
    return dp[n];
}

/**
 * 空间优化的动态规划解法
 * 
 * @param n 需要爬的台阶数
 * @return 到达楼顶的不同方法数
 */
int climbStairs2(int n) {
    if (n <= 1) {
        return 1;
    }
    
    // 只需要保存前两个状态
    int prev2 = 1;  // dp[i-2]
    int prev1 = 1;  // dp[i-1]
    int current = 0; // dp[i]
    
    // 状态转移
    for (int i = 2; i <= n; i++) {
        current = prev1 + prev2;
        prev2 = prev1;
        prev1 = current;
    }
    
    return current;
}

// 简单的全局数组用于记忆化
int memo[100];

/**
 * 深度优先搜索 + 记忆化
 * 
 * @param n 当前需要爬的台阶数
 * @return 到达楼顶的不同方法数
 */
int dfs(int n) {
    // 边界条件
    if (n <= 1) {
        return 1;
    }
    
    // 检查是否已经计算过
    if (memo[n] != 0) {  // 假设0表示未计算
        return memo[n];
    }
    
    // 状态转移
    int ans = dfs(n - 1) + dfs(n - 2);
    
    // 记忆化存储
    memo[n] = ans;
    return ans;
}

/**
 * 记忆化搜索解法
 * 
 * @param n 需要爬的台阶数
 * @return 到达楼顶的不同方法数
 */
int climbStairs3(int n) {
    // 初始化记忆化数组
    for (int i = 0; i <= n; i++) {
        memo[i] = 0;
    }
    
    return dfs(n);
}
/**
 * 打家劫舍 (House Robber) - 线性动态规划 - C++实现
 * 
 * 题目描述：
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
 * 影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
 * 如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，
 * 一夜之内能够偷窃到的最高金额。
 * 
 * 题目来源：LeetCode 198. 打家劫舍
 * 测试链接：https://leetcode.cn/problems/house-robber/
 */

// 使用基本的C++实现方式，避免复杂的STL容器

/**
 * 动态规划解法
 * 
 * @param nums 每个房屋存放金额的数组
 * @param numsSize 数组长度
 * @return 能够偷窃到的最高金额
 */
int rob1(int* nums, int numsSize) {
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    if (numsSize == 1) {
        return nums[0];
    }
    
    if (numsSize == 2) {
        return nums[0] > nums[1] ? nums[0] : nums[1];
    }
    
    // dp[i] 表示偷窃前 i+1 个房屋能获得的最大金额
    int dp[1000];  // 假设数组长度不会超过1000
    dp[0] = nums[0];
    dp[1] = nums[0] > nums[1] ? nums[0] : nums[1];
    
    // 状态转移
    for (int i = 2; i < numsSize; i++) {
        int steal = dp[i - 2] + nums[i];  // 偷窃当前房屋
        int skip = dp[i - 1];  // 不偷窃当前房屋
        dp[i] = steal > skip ? steal : skip;
    }
    
    return dp[numsSize - 1];
}

/**
 * 空间优化的动态规划解法
 * 
 * @param nums 每个房屋存放金额的数组
 * @param numsSize 数组长度
 * @return 能够偷窃到的最高金额
 */
int rob2(int* nums, int numsSize) {
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    if (numsSize == 1) {
        return nums[0];
    }
    
    // 只需要保存前两个状态
    int prev2 = nums[0];  // dp[i-2]
    int prev1 = nums[0] > nums[1] ? nums[0] : nums[1];  // dp[i-1]
    
    if (numsSize == 2) {
        return prev1;
    }
    
    int current = 0; // dp[i]
    
    // 状态转移
    for (int i = 2; i < numsSize; i++) {
        int steal = prev2 + nums[i];  // 偷窃当前房屋
        int skip = prev1;  // 不偷窃当前房屋
        current = steal > skip ? steal : skip;
        prev2 = prev1;
        prev1 = current;
    }
    
    return current;
}

// 简单的全局数组用于记忆化
int memo[1000];

/**
 * 深度优先搜索 + 记忆化
 * 
 * @param nums 每个房屋存放金额的数组
 * @param i 当前处理到第几个房屋
 * @return 能够偷窃到的最高金额
 */
int dfs(int* nums, int i) {
    // 边界条件
    if (i < 0) {
        return 0;
    }
    
    if (i == 0) {
        return nums[0];
    }
    
    // 检查是否已经计算过
    if (memo[i] != -1) {
        return memo[i];
    }
    
    // 状态转移：偷窃当前房屋或不偷窃当前房屋
    int steal = dfs(nums, i - 2) + nums[i];
    int skip = dfs(nums, i - 1);
    int ans = steal > skip ? steal : skip;
    
    // 记忆化存储
    memo[i] = ans;
    return ans;
}

/**
 * 记忆化搜索解法
 * 
 * @param nums 每个房屋存放金额的数组
 * @param numsSize 数组长度
 * @return 能够偷窃到的最高金额
 */
int rob3(int* nums, int numsSize) {
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    // 初始化记忆化数组
    for (int i = 0; i < numsSize; i++) {
        memo[i] = -1;
    }
    
    return dfs(nums, numsSize - 1);
}
/**
 * 最长递增子序列 (Longest Increasing Subsequence) - 线性动态规划 - C++实现
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 
 * 题目来源：LeetCode 300. 最长递增子序列
 * 测试链接：https://leetcode.cn/problems/longest-increasing-subsequence/
 */

// 使用基本的C++实现方式，避免复杂的STL容器

/**
 * 动态规划解法 O(n^2)
 * 
 * @param nums 整数数组
 * @param numsSize 数组长度
 * @return 最长递增子序列的长度
 */
int lengthOfLIS1(int* nums, int numsSize) {
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    // dp[i] 表示以 nums[i] 结尾的最长递增子序列的长度
    int dp[10000];  // 假设数组长度不会超过10000
    
    // 初始化：每个元素本身构成长度为1的子序列
    for (int i = 0; i < numsSize; i++) {
        dp[i] = 1;
    }
    
    int maxLength = 1;
    
    // 状态转移
    for (int i = 1; i < numsSize; i++) {
        for (int j = 0; j < i; j++) {
            if (nums[j] < nums[i]) {
                int candidate = dp[j] + 1;
                if (candidate > dp[i]) {
                    dp[i] = candidate;
                }
            }
        }
        if (dp[i] > maxLength) {
            maxLength = dp[i];
        }
    }
    
    return maxLength;
}

/**
 * 二分查找优化解法 O(n log n)
 * 
 * @param nums 整数数组
 * @param numsSize 数组长度
 * @return 最长递增子序列的长度
 */
int lengthOfLIS2(int* nums, int numsSize) {
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    // tails[i] 表示长度为 i+1 的递增子序列的最小尾部元素
    int tails[10000];  // 假设数组长度不会超过10000
    int len = 0;  // 当前最长递增子序列的长度
    
    for (int i = 0; i < numsSize; i++) {
        int num = nums[i];
        // 使用二分查找找到第一个大于等于 num 的位置
        int left = 0, right = len;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (tails[mid] < num) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        
        // 更新 tails 数组
        tails[left] = num;
        
        // 如果插入位置在末尾，说明找到了更长的递增子序列
        if (left == len) {
            len++;
        }
    }
    
    return len;
}

// 简单的全局数组用于记忆化
int memo[10000];

/**
 * 深度优先搜索 + 记忆化
 * 
 * @param nums 整数数组
 * @param i 当前处理到的位置
 * @param numsSize 数组长度
 * @return 以 nums[i] 结尾的最长递增子序列的长度
 */
int dfs(int* nums, int i, int numsSize) {
    // 检查是否已经计算过
    if (memo[i] != -1) {
        return memo[i];
    }
    
    // 初始化：至少为1（自身）
    int maxLength = 1;
    
    // 寻找前面所有较小元素的最长子序列
    for (int j = 0; j < i; j++) {
        if (nums[j] < nums[i]) {
            int candidate = dfs(nums, j, numsSize) + 1;
            if (candidate > maxLength) {
                maxLength = candidate;
            }
        }
    }
    
    // 记忆化存储
    memo[i] = maxLength;
    return maxLength;
}

/**
 * 记忆化搜索解法
 * 
 * @param nums 整数数组
 * @param numsSize 数组长度
 * @return 最长递增子序列的长度
 */
int lengthOfLIS3(int* nums, int numsSize) {
    if (nums == 0 || numsSize == 0) {
        return 0;
    }
    
    // 初始化记忆化数组
    for (int i = 0; i < numsSize; i++) {
        memo[i] = -1;
    }
    
    int maxLength = 0;
    // 尝试以每个元素作为结尾
    for (int i = 0; i < numsSize; i++) {
        int candidate = dfs(nums, i, numsSize);
        if (candidate > maxLength) {
            maxLength = candidate;
        }
    }
    
    return maxLength;
}
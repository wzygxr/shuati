// LeetCode 416. 分割等和子集
// 题目描述：给你一个只包含正整数的非空数组 nums。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
// 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
// 
// 解题思路：
// 这是一个01背包问题的变形。我们需要将数组分成两个和相等的子集，等价于找到一个子集，其和为整个数组和的一半。
// 1. 首先检查数组总和是否为偶数，如果为奇数则不可能分割成两个等和的子集
// 2. 如果总和为偶数，问题转化为：是否可以从数组中选择一些元素，使其和恰好为总和的一半
// 3. 状态定义：dp[j] 表示是否可以选择一些元素，使其和恰好为 j
// 4. 状态转移方程：dp[j] = dp[j] || dp[j - nums[i]]
// 5. 初始状态：dp[0] = true（空集的和为0）
// 
// 时间复杂度：O(n * target)，其中 n 是数组长度，target 是数组和的一半
// 空间复杂度：O(target)

#define MAX_TARGET 20001

// 获取两个数中的较大值
int max(int a, int b) {
    return a > b ? a : b;
}

/**
 * 判断是否可以将数组分割成两个和相等的子集
 * 
 * 解题思路：
 * 这是一个01背包问题的变形。我们需要将数组分成两个和相等的子集，等价于找到一个子集，其和为整个数组和的一半。
 * 1. 首先检查数组总和是否为偶数，如果为奇数则不可能分割成两个等和的子集
 * 2. 如果总和为偶数，问题转化为：是否可以从数组中选择一些元素，使其和恰好为总和的一半
 * 3. 状态定义：dp[j] 表示是否可以选择一些元素，使其和恰好为 j
 * 4. 状态转移方程：dp[j] = dp[j] || dp[j - nums[i]]
 * 5. 初始状态：dp[0] = true（空集的和为0）
 * 
 * 参数:
 *   nums: 输入的非空数组
 *   numsSize: 数组长度
 * 返回值:
 *   如果可以分割返回true，否则返回false
 */
bool canPartition(int* nums, int numsSize) {
    // 参数验证
    if (numsSize < 2) {
        return false; // 数组长度小于2，无法分割
    }
    
    // 计算数组总和
    int sum = 0;
    for (int i = 0; i < numsSize; i++) {
        sum += nums[i];
    }
    
    // 如果总和为奇数，无法分割成两个等和的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    // 目标值为总和的一半
    int target = sum / 2;
    
    // 创建dp数组，dp[j]表示是否可以选择一些元素，使其和恰好为j
    // 这是一个布尔型的01背包问题
    bool dp[MAX_TARGET];
    for (int i = 0; i <= target; i++) {
        dp[i] = false;
    }
    dp[0] = true; // 基础情况：空集的和为0，这是可达的
    
    // 遍历每个元素（物品）
    for (int i = 0; i < numsSize; i++) {
        // 01背包问题，逆序遍历容量
        // 这样可以保证每个元素只使用一次
        for (int j = target; j >= nums[i]; j--) {
            // 状态转移：选择当前元素或不选当前元素
            // dp[j] = 不选择当前元素 || 选择当前元素
            // 不选择当前元素：dp[j]（保持原值）
            // 选择当前元素：dp[j - nums[i]]（前一个状态）
            dp[j] = dp[j] || dp[j - nums[i]];
        }
        
        // 优化：如果已经找到解，可以提前结束
        // 如果dp[target]为true，说明已经找到了和为target的子集
        if (dp[target]) {
            return true;
        }
    }
    
    // 返回是否能找到和为target的子集
    return dp[target];
}

/**
 * 优化版本：包含更多的剪枝条件
 * 
 * 参数:
 *   nums: 输入的非空数组
 *   numsSize: 数组长度
 * 返回值:
 *   如果可以分割返回true，否则返回false
 */
bool canPartitionOptimized(int* nums, int numsSize) {
    // 参数验证
    if (numsSize < 2) {
        return false;
    }
    
    // 计算数组总和和最大值
    int sum = 0;
    int maxNum = 0;
    for (int i = 0; i < numsSize; i++) {
        sum += nums[i];
        maxNum = max(maxNum, nums[i]);
    }
    
    // 如果总和为奇数，无法分割
    if (sum % 2 != 0) {
        return false;
    }
    
    int target = sum / 2;
    
    // 如果最大值大于target，不可能分割
    // 因为任何一个元素都比target大，无法组成target
    if (maxNum > target) {
        return false;
    }
    
    // 创建dp数组
    bool dp[MAX_TARGET];
    for (int i = 0; i <= target; i++) {
        dp[i] = false;
    }
    dp[0] = true;
    
    for (int i = 0; i < numsSize; i++) {
        // 优化：如果当前元素大于target，可以跳过
        // 因为当前元素本身就比target大，无法用于组成target
        if (nums[i] > target) {
            continue;
        }
        
        for (int j = target; j >= nums[i]; j--) {
            dp[j] = dp[j] || dp[j - nums[i]];
        }
        
        if (dp[target]) {
            return true;
        }
    }
    
    return dp[target];
}

/*
 * 示例:
 * 输入: nums = [1,5,11,5]
 * 输出: true
 * 解释: 数组可以分割成 [1, 5, 5] 和 [11]。
 *
 * 输入: nums = [1,2,3,5]
 * 输出: false
 * 解释: 数组不能分割成两个元素和相等的子集。
 *
 * 时间复杂度: O(n * target)
 *   - 外层循环遍历所有元素：O(n)
 *   - 内层循环遍历目标值：O(target)
 * 空间复杂度: O(target)
 *   - 一维DP数组的空间消耗
 */
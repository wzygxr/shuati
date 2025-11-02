#include <iostream>
#include <vector>
#include <numeric>
#include <algorithm>

// LeetCode 416. 分割等和子集
// 题目描述：给你一个 只包含正整数 的 非空 数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
// 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
// 
// 解题思路：
// 这是一个典型的01背包问题的变种。我们可以将问题转化为：
// 1. 计算数组的总和sum
// 2. 如果sum是奇数，直接返回false（无法分成两个和相等的子集）
// 3. 如果sum是偶数，问题转化为：是否存在一个子集，使得其和为sum/2
// 4. 这相当于01背包问题，容量为sum/2，物品价值和重量都是nums[i]，问是否能恰好装满背包
// 
// 状态定义：dp[i] 表示是否可以选择一些数字，使得它们的和恰好为i
// 状态转移方程：dp[i] = dp[i] || dp[i - nums[j]]，其中j遍历所有数字，且i >= nums[j]
// 初始状态：dp[0] = true（表示和为0可以通过不选任何数字来实现）
// 
// 时间复杂度：O(n * target)，其中n是数组长度，target是sum/2
// 空间复杂度：O(target)，使用一维DP数组

using namespace std;

/**
 * 判断是否可以将数组分割成两个和相等的子集
 * @param nums 非空正整数数组
 * @return 是否可以分割
 */
bool canPartition(vector<int>& nums) {
    // 参数验证
    if (nums.size() <= 1) {
        return false;
    }
    
    // 计算数组总和
    int sum = accumulate(nums.begin(), nums.end(), 0);
    
    // 如果总和是奇数，无法分成两个和相等的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    // 目标和为总和的一半
    int target = sum / 2;
    
    // 创建DP数组，dp[i]表示是否可以选择一些数字，使得它们的和恰好为i
    vector<bool> dp(target + 1, false);
    
    // 初始状态：和为0可以通过不选任何数字来实现
    dp[0] = true;
    
    // 遍历每个数字（物品）
    for (int num : nums) {
        // 逆序遍历目标和（容量），防止重复使用同一个数字
        for (int i = target; i >= num; i--) {
            // 状态转移：选择当前数字或不选择当前数字
            dp[i] = dp[i] || dp[i - num];
        }
        
        // 提前终止：如果已经找到可以组成target的子集，直接返回true
        if (dp[target]) {
            return true;
        }
    }
    
    return dp[target];
}

/**
 * 优化版本：添加一些剪枝条件
 */
bool canPartitionOptimized(vector<int>& nums) {
    // 参数验证
    if (nums.size() <= 1) {
        return false;
    }
    
    // 计算数组总和，并找出最大值
    int sum = 0;
    int maxNum = 0;
    for (int num : nums) {
        sum += num;
        maxNum = max(maxNum, num);
    }
    
    // 如果总和是奇数，无法分成两个和相等的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    int target = sum / 2;
    
    // 如果数组中最大值大于target，那么这个数字必须单独在一个子集，但剩下的数字之和无法等于target
    if (maxNum > target) {
        return false;
    }
    
    // 如果数组中有元素等于target，直接返回true
    for (int num : nums) {
        if (num == target) {
            return true;
        }
    }
    
    // 创建DP数组
    vector<bool> dp(target + 1, false);
    dp[0] = true;
    
    for (int num : nums) {
        for (int i = target; i >= num; i--) {
            dp[i] = dp[i] || dp[i - num];
            if (dp[target]) {
                return true;
            }
        }
    }
    
    return dp[target];
}

/**
 * 二维DP实现，更容易理解
 */
bool canPartition2D(vector<int>& nums) {
    // 参数验证
    if (nums.size() <= 1) {
        return false;
    }
    
    int sum = accumulate(nums.begin(), nums.end(), 0);
    
    if (sum % 2 != 0) {
        return false;
    }
    
    int target = sum / 2;
    int n = nums.size();
    
    // dp[i][j]表示前i个数字是否可以组成和为j的子集
    vector<vector<bool>> dp(n + 1, vector<bool>(target + 1, false));
    
    // 初始化：前0个数字只能组成和为0的子集
    dp[0][0] = true;
    
    // 遍历每个数字
    for (int i = 1; i <= n; i++) {
        int num = nums[i - 1];
        // 遍历每个可能的和
        for (int j = 0; j <= target; j++) {
            // 不选择当前数字
            dp[i][j] = dp[i - 1][j];
            
            // 选择当前数字（如果j >= num）
            if (j >= num) {
                dp[i][j] = dp[i][j] || dp[i - 1][j - num];
            }
        }
        
        // 提前终止
        if (dp[i][target]) {
            return true;
        }
    }
    
    return dp[n][target];
}

/**
 * 递归+记忆化搜索实现
 */
bool canPartitionDFS(vector<int>& nums) {
    // 参数验证
    if (nums.size() <= 1) {
        return false;
    }
    
    int sum = accumulate(nums.begin(), nums.end(), 0);
    
    if (sum % 2 != 0) {
        return false;
    }
    
    int target = sum / 2;
    
    // 使用记忆化搜索，memo[i][j]表示从第i个数字开始，是否可以找到和为j的子集
    vector<vector<int>> memo(nums.size(), vector<int>(target + 1, -1)); // -1表示未计算，0表示false，1表示true
    
    function<bool(int, int)> dfs = [&](int index, int currentTarget) -> bool {
        // 基础情况：找到目标和
        if (currentTarget == 0) {
            return true;
        }
        
        // 基础情况：超出数组范围或目标和为负
        if (index >= nums.size() || currentTarget < 0) {
            return false;
        }
        
        // 如果已经计算过，直接返回结果
        if (memo[index][currentTarget] != -1) {
            return memo[index][currentTarget] == 1;
        }
        
        // 选择当前数字或不选择当前数字
        bool result = dfs(index + 1, currentTarget - nums[index]) || 
                      dfs(index + 1, currentTarget);
        
        // 记忆化结果
        memo[index][currentTarget] = result ? 1 : 0;
        return result;
    };
    
    return dfs(0, target);
}

/**
 * 位运算优化版本
 * 使用位图记录所有可能的和
 */
bool canPartitionBitwise(vector<int>& nums) {
    // 参数验证
    if (nums.size() <= 1) {
        return false;
    }
    
    int sum = accumulate(nums.begin(), nums.end(), 0);
    
    if (sum % 2 != 0) {
        return false;
    }
    
    int target = sum / 2;
    
    // 使用位掩码表示所有可能的和
    // bit[i] = 1表示可以组成和为i的子集
    // 注意：这里使用long long来避免溢出
    // 如果target很大，可能需要使用bitset
    long long dp = 1; // 初始状态：可以组成和为0的子集
    
    for (int num : nums) {
        // 对于每个数字，更新可能的和集合
        dp |= dp << num;
    }
    
    // 检查是否可以组成和为target的子集
    return (dp & (1LL << target)) != 0;
}

int main() {
    // 测试用例1
    vector<int> nums1 = {1, 5, 11, 5};
    cout << "测试用例1结果: " << (canPartition(nums1) ? "true" : "false") << endl; // 预期输出: true
    
    // 测试用例2
    vector<int> nums2 = {1, 2, 3, 5};
    cout << "测试用例2结果: " << (canPartition(nums2) ? "true" : "false") << endl; // 预期输出: false
    
    // 测试用例3
    vector<int> nums3 = {1, 2, 5};
    cout << "测试用例3结果: " << (canPartition(nums3) ? "true" : "false") << endl; // 预期输出: false
    
    // 测试用例4
    vector<int> nums4 = {2, 2, 3, 5};
    cout << "测试用例4结果: " << (canPartition(nums4) ? "true" : "false") << endl; // 预期输出: false
    
    // 测试用例5
    vector<int> nums5 = {1, 2, 3, 4, 5, 6, 7};
    cout << "测试用例5结果: " << (canPartition(nums5) ? "true" : "false") << endl; // 预期输出: true
    
    return 0;
}
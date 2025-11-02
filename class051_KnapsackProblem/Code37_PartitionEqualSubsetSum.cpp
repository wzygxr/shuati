// LeetCode 416. 分割等和子集
// 题目描述：给你一个只包含正整数的非空数组 nums。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
// 链接：https://leetcode.cn/problems/partition-equal-subset-sum/
// 
// 解题思路：
// 这是一个0-1背包问题的变种。问题可以转化为：是否存在一个子集，其和等于整个数组和的一半。
// 
// 状态定义：dp[j] 表示是否能组成和为j的子集
// 状态转移方程：dp[j] = dp[j] || dp[j - nums[i]]
// 初始状态：dp[0] = true，表示空子集的和为0是可以组成的
// 
// 时间复杂度：O(n * target)，其中n是数组长度，target是数组和的一半
// 空间复杂度：O(target)，使用一维DP数组

#include <iostream>
#include <vector>
#include <numeric>
#include <algorithm>
#include <cstring>
using namespace std;

/**
 * 判断是否可以将数组分割成两个和相等的子集
 * @param nums 非空数组，只包含正整数
 * @return 是否可以分割
 */
bool canPartition(vector<int>& nums) {
    if (nums.size() < 2) {
        return false;
    }
    
    // 计算数组总和
    int sum = accumulate(nums.begin(), nums.end(), 0);
    
    // 如果总和是奇数，不可能分成两个和相等的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    // 目标和为总和的一半
    int target = sum / 2;
    
    // 创建DP数组，dp[j]表示是否能组成和为j的子集
    vector<bool> dp(target + 1, false);
    
    // 初始状态：空子集的和为0是可以组成的
    dp[0] = true;
    
    // 遍历每个数字
    for (int num : nums) {
        // 逆序遍历，避免重复使用同一个数字
        for (int j = target; j >= num; j--) {
            // 更新状态：不选当前数字 或 选当前数字（如果可以的话）
            dp[j] = dp[j] || dp[j - num];
        }
    }
    
    return dp[target];
}

/**
 * 使用二维DP数组的版本（更直观但空间效率较低）
 * @param nums 非空数组，只包含正整数
 * @return 是否可以分割
 */
bool canPartition2D(vector<int>& nums) {
    if (nums.size() < 2) {
        return false;
    }
    
    // 计算数组总和
    int sum = accumulate(nums.begin(), nums.end(), 0);
    
    // 如果总和是奇数，不可能分成两个和相等的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    // 目标和为总和的一半
    int target = sum / 2;
    int n = nums.size();
    
    // 创建二维DP数组，dp[i][j]表示前i个数字是否能组成和为j的子集
    vector<vector<bool>> dp(n + 1, vector<bool>(target + 1, false));
    
    // 初始状态：空子集的和为0是可以组成的
    for (int i = 0; i <= n; i++) {
        dp[i][0] = true;
    }
    
    // 填充DP数组
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= target; j++) {
            // 不选第i个数字
            dp[i][j] = dp[i-1][j];
            
            // 选第i个数字（如果可以的话）
            if (j >= nums[i-1]) {
                dp[i][j] = dp[i][j] || dp[i-1][j - nums[i-1]];
            }
        }
    }
    
    return dp[n][target];
}

/**
 * 优化的一维DP版本，提前处理一些边界情况
 * @param nums 非空数组，只包含正整数
 * @return 是否可以分割
 */
bool canPartitionOptimized(vector<int>& nums) {
    if (nums.size() < 2) {
        return false;
    }
    
    // 计算数组总和
    int sum = 0;
    int maxNum = 0;
    for (int num : nums) {
        sum += num;
        maxNum = max(maxNum, num);
    }
    
    // 如果总和是奇数，不可能分成两个和相等的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    // 目标和为总和的一半
    int target = sum / 2;
    
    // 如果最大的数字大于目标和，不可能分割
    if (maxNum > target) {
        return false;
    }
    
    // 创建DP数组
    vector<bool> dp(target + 1, false);
    dp[0] = true;
    
    for (int num : nums) {
        for (int j = target; j >= num; j--) {
            dp[j] = dp[j] || dp[j - num];
        }
    }
    
    return dp[target];
}

/**
 * 使用递归+记忆化搜索实现
 * 这个方法对于较大的输入可能会超时，但展示了递归的思路
 * @param nums 非空数组，只包含正整数
 * @return 是否可以分割
 */
bool canPartitionRecursive(vector<int>& nums) {
    if (nums.size() < 2) {
        return false;
    }
    
    // 计算数组总和
    int sum = accumulate(nums.begin(), nums.end(), 0);
    
    // 如果总和是奇数，不可能分成两个和相等的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    // 目标和为总和的一半
    int target = sum / 2;
    int n = nums.size();
    
    // 创建记忆化缓存
    vector<vector<int>> memo(n, vector<int>(target + 1, -1)); // -1表示未计算
    
    function<bool(int, int)> dfs = [&](int index, int currentSum) -> bool {
        // 找到目标和
        if (currentSum == target) {
            return true;
        }
        
        // 超过目标和或处理完所有元素
        if (currentSum > target || index == n) {
            return false;
        }
        
        // 检查缓存
        if (memo[index][currentSum] != -1) {
            return memo[index][currentSum];
        }
        
        // 递归调用：选当前元素 或 不选当前元素
        bool result = dfs(index + 1, currentSum + nums[index]) || 
                     dfs(index + 1, currentSum);
        
        // 缓存结果
        memo[index][currentSum] = result;
        return result;
    };
    
    return dfs(0, 0);
}

/**
 * 使用位操作优化的版本
 * 对于较大的数组但元素值不大的情况，位操作可以更高效
 * @param nums 非空数组，只包含正整数
 * @return 是否可以分割
 */
bool canPartitionBitSet(vector<int>& nums) {
    if (nums.size() < 2) {
        return false;
    }
    
    // 计算数组总和
    int sum = accumulate(nums.begin(), nums.end(), 0);
    
    // 如果总和是奇数，不可能分成两个和相等的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    // 目标和为总和的一半
    int target = sum / 2;
    
    // 使用位集表示可达的和
    // bitset的第i位为1表示和为i是可达的
    unsigned long long bitset = 1; // 初始状态，和为0是可达的
    
    for (int num : nums) {
        // 位操作：当前可达的和 | (之前可达的和 + 当前数字)
        bitset |= bitset << num;
    }
    
    // 检查目标和是否可达
    return (bitset & (1ULL << target)) != 0;
}

// 打印数组函数
void printArray(const vector<int>& arr) {
    cout << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        cout << arr[i];
        if (i < arr.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 主函数，用于测试
int main() {
    // 测试用例1
    vector<int> nums1 = {1, 5, 11, 5};
    cout << "测试用例1 (";
    printArray(nums1);
    cout << ") 结果: " << (canPartition(nums1) ? "true" : "false") << endl; // 预期输出: true
    
    // 测试用例2
    vector<int> nums2 = {1, 2, 3, 5};
    cout << "测试用例2 (";
    printArray(nums2);
    cout << ") 结果: " << (canPartition(nums2) ? "true" : "false") << endl; // 预期输出: false
    
    // 测试不同实现
    cout << "\n测试不同实现:\n";
    cout << "二维DP版本 (测试用例1): " << (canPartition2D(nums1) ? "true" : "false") << endl;
    cout << "优化版本 (测试用例1): " << (canPartitionOptimized(nums1) ? "true" : "false") << endl;
    cout << "递归版本 (测试用例1): " << (canPartitionRecursive(nums1) ? "true" : "false") << endl;
    cout << "位操作版本 (测试用例1): " << (canPartitionBitSet(nums1) ? "true" : "false") << endl;
    
    // 测试用例3
    vector<int> nums3 = {1, 2, 3, 4, 5, 6, 7};
    cout << "\n测试用例3 (";
    printArray(nums3);
    cout << ") 结果: " << (canPartition(nums3) ? "true" : "false") << endl; // 预期输出: true
    
    // 测试用例4
    vector<int> nums4 = {100, 100, 100, 100, 100, 100, 100, 100};
    cout << "\n测试用例4 (多个100): " << (canPartition(nums4) ? "true" : "false") << endl; // 预期输出: true
    
    return 0;
}
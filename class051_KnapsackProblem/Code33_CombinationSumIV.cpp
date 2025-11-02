#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>
#include <functional>

// LeetCode 377. 组合总和 IV
// 题目描述：给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。
// 请你从 nums 中找出并返回总和为 target 的元素组合的个数。
// 注意：顺序不同的序列被视作不同的组合。
// 链接：https://leetcode.cn/problems/combination-sum-iv/
// 
// 解题思路：
// 这是一个与完全背包相关但关注排列而非组合的问题：
// - 数字可以多次使用（完全背包的特点）
// - 顺序不同的序列视为不同的组合（与组合数问题的关键区别）
// 
// 状态定义：dp[i] 表示凑成目标值i的不同排列数
// 状态转移方程：dp[i] += dp[i - num]，其中num是nums中的每个元素，且i >= num
// 初始状态：dp[0] = 1（凑成目标值0只有一种方式：不选择任何数字）
// 
// 时间复杂度：O(target * n)，其中n是数组nums的长度
// 空间复杂度：O(target)，使用一维DP数组

using namespace std;

/**
 * 计算凑成目标值的不同排列数
 * @param nums 不同整数组成的数组
 * @param target 目标整数
 * @return 总和为target的元素组合的个数
 */
int combinationSum4(vector<int>& nums, int target) {
    // 参数验证
    if (target < 0) {
        return 0;
    }
    if (target == 0) {
        return 1;
    }
    if (nums.empty()) {
        return 0;
    }
    
    // 创建DP数组，dp[i]表示凑成目标值i的不同排列数
    // 注意：由于可能的数值较大，我们需要处理溢出问题
    vector<long long> dp(target + 1, 0);
    dp[0] = 1; // 凑成目标值0只有一种方式：不选择任何数字
    
    // 遍历目标值，从1到target
    // 注意：与零钱兑换II不同，这里我们先遍历目标值，再遍历数组元素，这样可以考虑不同顺序的排列
    for (int i = 1; i <= target; ++i) {
        // 遍历数组中的每个元素
        for (int num : nums) {
            // 如果当前元素小于等于剩余需要凑成的目标值，更新dp[i]
            if (num <= i) {
                dp[i] += dp[i - num];
                // 防止溢出（题目保证结果在32位有符号整数范围内）
                if (dp[i] > INT_MAX) {
                    dp[i] = INT_MAX;
                }
            }
        }
    }
    
    return static_cast<int>(dp[target]);
}

/**
 * 递归+记忆化搜索实现
 * @param nums 不同整数组成的数组
 * @param target 目标整数
 * @return 总和为target的元素组合的个数
 */
int combinationSum4DFS(vector<int>& nums, int target) {
    // 参数验证
    if (target < 0) {
        return 0;
    }
    if (target == 0) {
        return 1;
    }
    if (nums.empty()) {
        return 0;
    }
    
    // 使用unordered_map作为记忆化缓存
    unordered_map<int, int> memo;
    memo[0] = 1; // 凑成目标值0只有一种方式
    
    function<int(int)> dfs = [&](int remain) -> int {
        // 检查缓存
        if (memo.find(remain) != memo.end()) {
            return memo[remain];
        }
        
        int ways = 0;
        
        // 尝试使用每个元素
        for (int num : nums) {
            if (num <= remain) {
                // 递归计算剩余值的排列数
                ways += dfs(remain - num);
                // 防止溢出
                if (ways > INT_MAX) {
                    ways = INT_MAX;
                }
            }
        }
        
        // 缓存结果
        memo[remain] = ways;
        return ways;
    };
    
    return dfs(target);
}

/**
 * 优化版本：提前排序和剪枝
 * @param nums 不同整数组成的数组
 * @param target 目标整数
 * @return 总和为target的元素组合的个数
 */
int combinationSum4Optimized(vector<int>& nums, int target) {
    // 参数验证
    if (target < 0) {
        return 0;
    }
    if (target == 0) {
        return 1;
    }
    if (nums.empty()) {
        return 0;
    }
    
    // 对数组进行排序，以便在后续处理中进行剪枝
    sort(nums.begin(), nums.end());
    
    // 创建DP数组
    vector<long long> dp(target + 1, 0);
    dp[0] = 1;
    
    // 遍历目标值
    for (int i = 1; i <= target; ++i) {
        // 遍历数组中的元素
        for (int num : nums) {
            // 如果当前元素大于剩余需要凑成的目标值，由于数组已排序，后面的元素更大，可以提前退出循环
            if (num > i) {
                break;
            }
            dp[i] += dp[i - num];
            // 防止溢出
            if (dp[i] > INT_MAX) {
                dp[i] = INT_MAX;
            }
        }
    }
    
    return static_cast<int>(dp[target]);
}

/**
 * 递归+记忆化搜索实现的另一种方式，使用数组作为缓存
 * @param nums 不同整数组成的数组
 * @param target 目标整数
 * @return 总和为target的元素组合的个数
 */
int combinationSum4DFSArray(vector<int>& nums, int target) {
    // 参数验证
    if (target < 0) {
        return 0;
    }
    if (target == 0) {
        return 1;
    }
    if (nums.empty()) {
        return 0;
    }
    
    // 使用数组作为缓存，初始值为-1表示未计算
    vector<int> memo(target + 1, -1);
    memo[0] = 1; // 凑成目标值0只有一种方式
    
    function<int(int)> dfs = [&](int remain) -> int {
        // 检查缓存
        if (memo[remain] != -1) {
            return memo[remain];
        }
        
        int ways = 0;
        
        // 尝试使用每个元素
        for (int num : nums) {
            if (num <= remain) {
                ways += dfs(remain - num);
                // 防止溢出
                if (ways > INT_MAX) {
                    ways = INT_MAX;
                }
            }
        }
        
        // 缓存结果
        memo[remain] = ways;
        return ways;
    };
    
    return dfs(target);
}

int main() {
    // 测试用例1
    vector<int> nums1 = {1, 2, 3};
    int target1 = 4;
    cout << "测试用例1结果: " << combinationSum4(nums1, target1) << endl; // 预期输出: 7 ([1,1,1,1], [1,1,2], [1,2,1], [1,3], [2,1,1], [2,2], [3,1])
    
    // 测试用例2
    vector<int> nums2 = {9};
    int target2 = 3;
    cout << "测试用例2结果: " << combinationSum4(nums2, target2) << endl; // 预期输出: 0
    
    // 测试DFS实现
    cout << "测试用例1 (DFS): " << combinationSum4DFS(nums1, target1) << endl;
    
    // 测试优化版本
    cout << "测试用例1 (优化版本): " << combinationSum4Optimized(nums1, target1) << endl;
    
    // 测试DFS+数组缓存实现
    cout << "测试用例1 (DFS+数组缓存): " << combinationSum4DFSArray(nums1, target1) << endl;
    
    return 0;
}
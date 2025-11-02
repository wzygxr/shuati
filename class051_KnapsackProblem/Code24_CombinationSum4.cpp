#include <iostream>
#include <vector>
#include <algorithm>
#include <unordered_map>

// LeetCode 377. 组合总和 Ⅳ
// 题目描述：给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。
// 请你从 nums 中找出并返回总和为 target 的元素组合的个数。
// 注意：顺序不同的序列被视作不同的组合。
// 链接：https://leetcode.cn/problems/combination-sum-iv/
// 
// 解题思路：
// 这是一个完全背包问题的变种，但是与传统的完全背包问题不同，这里需要计算的是排列数而不是组合数。
// 对于排列数，我们需要先遍历容量（target），再遍历物品（nums数组），这样可以确保不同顺序的序列被视为不同的组合。
// 
// 状态定义：dp[i] 表示总和为i的元素组合的个数
// 状态转移方程：dp[i] += dp[i - num]，对于每个num，如果i >= num
// 初始状态：dp[0] = 1，表示总和为0的组合只有一种（空组合）
// 
// 时间复杂度：O(target * n)，其中n是nums数组的长度
// 空间复杂度：O(target)，使用一维DP数组

using namespace std;

/**
 * 找出总和为target的元素组合的个数
 * @param nums 不同整数组成的数组
 * @param target 目标整数
 * @return 总和为target的元素组合的个数
 */
int combinationSum4(vector<int>& nums, int target) {
    // 参数验证
    if (nums.empty()) {
        return 0;
    }
    
    // 创建一维DP数组，dp[i]表示总和为i的元素组合的个数
    vector<long long> dp(target + 1, 0); // 使用long long防止溢出
    
    // 初始状态：总和为0的组合只有一种（空组合）
    dp[0] = 1;
    
    // 注意：为了计算排列数，我们先遍历容量（target），再遍历物品（nums数组）
    // 这样可以确保不同顺序的序列被视为不同的组合
    for (int i = 1; i <= target; i++) {
        for (int num : nums) {
            // 状态转移：如果当前容量i大于等于物品重量num
            if (i >= num && dp[i - num] <= INT_MAX - dp[i]) {
                dp[i] += dp[i - num];
            }
        }
    }
    
    // 返回结果：总和为target的元素组合的个数
    return dp[target] > INT_MAX ? INT_MAX : static_cast<int>(dp[target]);
}

/**
 * 优化版本：剪枝处理
 */
int combinationSum4Optimized(vector<int>& nums, int target) {
    // 参数验证
    if (nums.empty()) {
        return 0;
    }
    
    // 排序nums数组，方便后续剪枝
    sort(nums.begin(), nums.end());
    
    // 创建一维DP数组
    vector<long long> dp(target + 1, 0);
    dp[0] = 1;
    
    // 先遍历容量，再遍历物品
    for (int i = 1; i <= target; i++) {
        for (int num : nums) {
            // 剪枝：如果num大于i，后续的num会更大，不需要继续遍历
            if (num > i) {
                break;
            }
            // 防止整数溢出
            if (dp[i - num] <= INT_MAX - dp[i]) {
                dp[i] += dp[i - num];
            }
        }
    }
    
    return dp[target] > INT_MAX ? INT_MAX : static_cast<int>(dp[target]);
}

/**
 * 递归+记忆化搜索实现
 */
int combinationSum4DFS(vector<int>& nums, int target) {
    // 参数验证
    if (nums.empty()) {
        return 0;
    }
    
    // 使用unordered_map作为缓存
    unordered_map<int, long long> memo; // 键为剩余目标值，值为对应的组合数
    
    // 定义DFS函数
    function<long long(int)> dfs = [&](int remaining) {
        // 基础情况：剩余目标值为0，返回1（表示找到一种组合）
        if (remaining == 0) {
            return 1LL;
        }
        
        // 基础情况：剩余目标值小于0，返回0（表示无法找到组合）
        if (remaining < 0) {
            return 0LL;
        }
        
        // 检查缓存
        if (memo.find(remaining) != memo.end()) {
            return memo[remaining];
        }
        
        // 计算所有可能的组合数
        long long count = 0;
        for (int num : nums) {
            // 递归计算使用当前num后的组合数
            count += dfs(remaining - num);
            // 防止整数溢出
            if (count > INT_MAX) {
                break;
            }
        }
        
        // 缓存结果
        memo[remaining] = count;
        
        return count;
    };
    
    // 调用递归函数
    long long result = dfs(target);
    return result > INT_MAX ? INT_MAX : static_cast<int>(result);
}

/**
 * 使用数组作为缓存的优化DFS实现
 */
int combinationSum4DFSWithArrayCache(vector<int>& nums, int target) {
    // 参数验证
    if (nums.empty()) {
        return 0;
    }
    
    // 使用vector作为缓存，初始值为-1表示未计算
    vector<long long> cache(target + 1, -1);
    
    // 定义DFS函数
    function<long long(int)> dfs = [&](int remaining) {
        // 基础情况
        if (remaining == 0) {
            return 1LL;
        }
        if (remaining < 0) {
            return 0LL;
        }
        
        // 检查缓存
        if (cache[remaining] != -1) {
            return cache[remaining];
        }
        
        // 计算组合数
        long long count = 0;
        for (int num : nums) {
            count += dfs(remaining - num);
            if (count > INT_MAX) {
                break;
            }
        }
        
        // 缓存结果
        cache[remaining] = count;
        
        return count;
    };
    
    long long result = dfs(target);
    return result > INT_MAX ? INT_MAX : static_cast<int>(result);
}

/**
 * 回溯算法实现（注意：对于大数会超时，仅作为参考）
 */
int combinationSum4Backtracking(vector<int>& nums, int target) {
    // 参数验证
    if (nums.empty()) {
        return 0;
    }
    
    // 结果计数器
    long long count = 0;
    
    // 定义回溯函数
    function<void(int)> backtrack = [&](int remaining) {
        // 找到一个有效组合
        if (remaining == 0) {
            count++;
            return;
        }
        
        // 超过目标值，直接返回
        if (remaining < 0 || count > INT_MAX) {
            return;
        }
        
        // 尝试每个数字
        for (int num : nums) {
            backtrack(remaining - num);
        }
    };
    
    // 调用回溯函数
    backtrack(target);
    
    return count > INT_MAX ? INT_MAX : static_cast<int>(count);
}

/**
 * 使用BFS的实现方式
 */
int combinationSum4BFS(vector<int>& nums, int target) {
    if (nums.empty()) {
        return 0;
    }
    
    // dp[i]表示总和为i的元素组合的个数
    vector<long long> dp(target + 1, 0);
    dp[0] = 1;
    
    // BFS思想：从小到大计算每个值的组合数
    for (int i = 0; i <= target; i++) {
        // 如果当前值i无法达到，跳过
        if (dp[i] == 0) {
            continue;
        }
        
        // 尝试在当前值的基础上添加每个数字
        for (int num : nums) {
            // 确保不会超出target
            if (i + num <= target) {
                // 防止溢出
                if (dp[i] <= INT_MAX - dp[i + num]) {
                    dp[i + num] += dp[i];
                }
            }
        }
    }
    
    return dp[target] > INT_MAX ? INT_MAX : static_cast<int>(dp[target]);
}

int main() {
    // 测试用例1
    vector<int> nums1 = {1, 2, 3};
    int target1 = 4;
    cout << "测试用例1结果: " << combinationSum4(nums1, target1) << endl; // 预期输出: 7
    
    // 测试用例2
    vector<int> nums2 = {9};
    int target2 = 3;
    cout << "测试用例2结果: " << combinationSum4(nums2, target2) << endl; // 预期输出: 0
    
    // 测试用例3
    vector<int> nums3 = {1, 2, 4};
    int target3 = 32;
    cout << "测试用例3结果: " << combinationSum4(nums3, target3) << endl; // 大数测试
    
    // 测试用例4
    vector<int> nums4 = {1, 50};
    int target4 = 100;
    cout << "测试用例4结果: " << combinationSum4(nums4, target4) << endl; // 预期输出: 3
    
    return 0;
}
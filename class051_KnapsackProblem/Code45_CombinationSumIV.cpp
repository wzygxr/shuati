#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <chrono>

using namespace std;

// LeetCode 377. 组合总和 Ⅳ
// 题目描述：给定一个由不同整数组成的数组 nums 和一个目标整数 target，
// 请从 nums 中找出并返回总和为 target 的元素组合的个数。
// 链接：https://leetcode.cn/problems/combination-sum-iv/
// 
// 解题思路：
// 这是一个完全背包问题的排列数变种，需要计算所有可能的排列数。
// 与零钱兑换II不同，这里顺序不同的序列被视为不同的组合。
// 
// 状态定义：dp[i] 表示总和为 i 的元素组合个数
// 状态转移方程：dp[i] = sum(dp[i - num])，其中 num 是 nums 中的元素且 i >= num
// 初始状态：dp[0] = 1（空组合）
// 
// 关键点：为了计算排列数，需要将目标值循环放在外层，数组元素循环放在内层
// 
// 时间复杂度：O(target * n)，其中 n 是数组长度
// 空间复杂度：O(target)，使用一维DP数组
// 
// 工程化考量：
// 1. 异常处理：处理空数组、负数等情况
// 2. 整数溢出：使用long long类型处理大数
// 3. 性能优化：排序数组进行剪枝
// 4. 边界条件：target=0时返回1

class Solution {
public:
    /**
     * 动态规划解法 - 计算排列数
     * @param nums 不同整数组成的数组
     * @param target 目标整数
     * @return 总和为target的元素组合个数
     */
    int combinationSum4(vector<int>& nums, int target) {
        // 参数验证
        if (nums.empty()) {
            return target == 0 ? 1 : 0;
        }
        if (target < 0) {
            return 0;
        }
        
        // 特殊情况处理
        if (target == 0) {
            return 1; // 空组合
        }
        
        // 创建DP数组
        vector<unsigned int> dp(target + 1, 0);
        dp[0] = 1; // 空组合
        
        // 为了计算排列数，需要将目标值循环放在外层
        // 数组元素循环放在内层
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    // 使用unsigned int避免溢出警告
                    if (dp[i] > UINT_MAX - dp[i - num]) {
                        // 处理溢出情况
                        dp[i] = UINT_MAX;
                    } else {
                        dp[i] += dp[i - num];
                    }
                }
            }
        }
        
        return static_cast<int>(dp[target]);
    }
    
    /**
     * 优化的动态规划解法 - 处理整数溢出
     * 使用long long类型避免整数溢出
     */
    int combinationSum4Optimized(vector<int>& nums, int target) {
        if (nums.empty()) {
            return target == 0 ? 1 : 0;
        }
        if (target < 0) {
            return 0;
        }
        
        // 使用long long数组避免整数溢出
        vector<long long> dp(target + 1, 0);
        dp[0] = 1;
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                    // 如果超过int最大值，取最大值
                    if (dp[i] > INT_MAX) {
                        dp[i] = INT_MAX;
                    }
                }
            }
        }
        
        return static_cast<int>(dp[target]);
    }
    
    /**
     * 带剪枝优化的动态规划解法
     * 先排序数组，当num > i时提前终止内层循环
     */
    int combinationSum4WithPruning(vector<int>& nums, int target) {
        if (nums.empty()) {
            return target == 0 ? 1 : 0;
        }
        if (target < 0) {
            return 0;
        }
        
        // 排序数组，便于剪枝
        sort(nums.begin(), nums.end());
        vector<unsigned int> dp(target + 1, 0);
        dp[0] = 1;
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (num > i) {
                    break; // 剪枝：由于数组已排序，后续数字更大
                }
                if (dp[i] > UINT_MAX - dp[i - num]) {
                    dp[i] = UINT_MAX;
                } else {
                    dp[i] += dp[i - num];
                }
            }
        }
        
        return static_cast<int>(dp[target]);
    }
    
    /**
     * 递归+记忆化搜索解法
     */
    int combinationSum4DFS(vector<int>& nums, int target) {
        if (nums.empty()) {
            return target == 0 ? 1 : 0;
        }
        if (target < 0) {
            return 0;
        }
        
        // 使用记忆化数组
        vector<int> memo(target + 1, -1);
        return dfs(nums, target, memo);
    }
    
    /**
     * 递归辅助函数
     */
    int dfs(vector<int>& nums, int target, vector<int>& memo) {
        // 基础情况
        if (target == 0) {
            return 1;
        }
        if (target < 0) {
            return 0;
        }
        
        // 检查记忆化数组
        if (memo[target] != -1) {
            return memo[target];
        }
        
        int count = 0;
        for (int num : nums) {
            count += dfs(nums, target - num, memo);
        }
        
        memo[target] = count;
        return count;
    }
};

/**
 * 测试函数
 */
void testCombinationSum4() {
    Solution sol;
    
    // 测试用例
    vector<pair<vector<int>, int>> testCases = {
        {{1, 2, 3}, 4},   // 预期：7
        {{9}, 3},         // 预期：0
        {{1, 2, 3}, 0},   // 预期：1
        {{1, 2, 3}, 1},   // 预期：1
        {{1, 2, 3}, 2},   // 预期：2
        {{1, 2, 3}, 3}    // 预期：4
    };
    
    cout << "组合总和IV问题测试：" << endl;
    for (auto& testCase : testCases) {
        vector<int> nums = testCase.first;
        int target = testCase.second;
        
        int result1 = sol.combinationSum4(nums, target);
        int result2 = sol.combinationSum4Optimized(nums, target);
        int result3 = sol.combinationSum4WithPruning(nums, target);
        int result4 = sol.combinationSum4DFS(nums, target);
        
        cout << "nums=[";
        for (size_t i = 0; i < nums.size(); i++) {
            cout << nums[i];
            if (i < nums.size() - 1) cout << ", ";
        }
        cout << "], target=" << target 
             << ": DP=" << result1 
             << ", Optimized=" << result2 
             << ", Pruning=" << result3 
             << ", DFS=" << result4 << endl;
        
        // 验证结果一致性
        if (result1 != result2 || result2 != result3 || result3 != result4) {
            cout << "警告：不同方法结果不一致！" << endl;
        }
    }
    
    // 性能测试 - 大规模数据
    vector<int> largeNums = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    int largeTarget = 50;
    
    auto startTime = chrono::high_resolution_clock::now();
    int largeResult = sol.combinationSum4WithPruning(largeNums, largeTarget);
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    
    cout << "大规模测试: nums长度=" << largeNums.size() 
         << ", target=" << largeTarget 
         << ", 结果=" << largeResult 
         << ", 耗时=" << duration.count() << "ms" << endl;
    
    // 边界情况测试
    cout << "边界情况测试：" << endl;
    vector<int> emptyNums;
    cout << "空数组, target=0: " << sol.combinationSum4(emptyNums, 0) << endl; // 预期：1
    cout << "空数组, target=1: " << sol.combinationSum4(emptyNums, 1) << endl; // 预期：0
    cout << "负数target: " << sol.combinationSum4({1, 2, 3}, -1) << endl; // 预期：0
}

int main() {
    try {
        testCombinationSum4();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    return 0;
}

/*
 * 复杂度分析：
 * 
 * 方法1：动态规划（排列数）
 * - 时间复杂度：O(target * n)
 *   - 外层循环：target 次
 *   - 内层循环：n 次（数组长度）
 * - 空间复杂度：O(target)
 * 
 * 方法2：优化的动态规划（处理溢出）
 * - 时间复杂度：O(target * n)（与方法1相同）
 * - 空间复杂度：O(target)
 * 
 * 方法3：带剪枝的动态规划
 * - 时间复杂度：O(target * n)（平均情况下由于剪枝可能更快）
 * - 空间复杂度：O(target)
 * 
 * 方法4：递归+记忆化搜索
 * - 时间复杂度：O(target * n)（每个状态计算一次）
 * - 空间复杂度：O(target)（递归栈深度+记忆化数组）
 * 
 * C++特定优化：
 * 1. 使用vector代替数组，更安全
 * 2. 使用unsigned int处理大数
 * 3. 使用chrono进行精确性能测试
 * 4. 异常处理使用C++标准异常
 * 
 * 关键点分析：
 * 1. 排列数 vs 组合数：本题需要计算排列数，因此遍历顺序很重要
 * 2. 整数溢出：当target较大时，结果可能超过int范围
 * 3. 剪枝优化：排序数组可以在内层循环提前终止
 * 
 * 工程化考量：
 * 1. 模块化设计：将不同解法封装为类方法
 * 2. 类型安全：使用适当的类型避免溢出
 * 3. 性能优化：利用STL算法进行排序
 * 4. 测试覆盖：包含各种边界情况
 */
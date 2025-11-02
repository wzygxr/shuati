#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

// 组合总和 Ⅳ
// 给你一个由不同整数组成的数组 nums ，和一个目标整数 target
// 请你从 nums 中找出并返回总和为 target 的元素组合的个数
// 顺序不同的序列被视作不同的组合
// 测试链接 : https://leetcode.cn/problems/combination-sum-iv/

/*
 * 算法详解：
 * 这是一个完全背包问题的变种，求组合数（考虑顺序）。与标准的完全背包求组合数不同，
 * 本题中顺序不同的序列被视为不同的组合，因此需要调整遍历顺序。
 * 
 * 解题思路：
 * 1. 状态定义：dp[i]表示总和为i的组合数
 * 2. 状态转移：对于每个总和i，枚举所有可能的数字nums[j]
 *    dp[i] += dp[i - nums[j]]  (当i >= nums[j]时)
 * 3. 遍历顺序：外层遍历背包容量，内层遍历物品（与标准完全背包相反）
 * 
 * 时间复杂度分析：
 * 设数组长度为n，目标值为target
 * 1. 动态规划计算：O(n * target)
 * 总时间复杂度：O(n * target)
 * 
 * 空间复杂度分析：
 * 1. DP数组：O(target)
 * 
 * 相关题目扩展：
 * 1. LeetCode 377. 组合总和 Ⅳ（本题）
 * 2. LeetCode 518. 零钱兑换 II（不考虑顺序的组合数）
 * 3. LeetCode 322. 零钱兑换（求最少硬币数）
 * 4. LeetCode 279. 完全平方数（完全背包变种）
 * 5. LeetCode 139. 单词拆分（字符串匹配+背包）
 */

class Solution {
public:
    static const int MOD = 1000000007;  // 防止整数溢出
    
    // 标准DP版本
    int combinationSum4(vector<int>& nums, int target) {
        if (nums.empty()) return 0;
        if (target == 0) return 1;
        
        // 创建DP数组
        vector<long long> dp(target + 1, 0);  // 使用long long防止溢出
        dp[0] = 1;  // 总和为0的组合数为1
        
        // 外层遍历背包容量，内层遍历物品（考虑顺序）
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i >= num) {
                    dp[i] += dp[i - num];
                    // 防止整数溢出
                    if (dp[i] >= MOD) dp[i] %= MOD;
                }
            }
        }
        
        return dp[target];
    }
    
    // 优化版本：先排序，可以提前终止内层循环
    int combinationSum4Optimized(vector<int>& nums, int target) {
        if (nums.empty()) return 0;
        if (target == 0) return 1;
        
        // 对数组排序
        sort(nums.begin(), nums.end());
        
        // 如果最小的数字都大于target，直接返回0
        if (nums[0] > target) return 0;
        
        vector<long long> dp(target + 1, 0);
        dp[0] = 1;
        
        for (int i = 1; i <= target; i++) {
            for (int num : nums) {
                if (i < num) break;  // 提前终止，因为后面的数字更大
                dp[i] += dp[i - num];
                if (dp[i] >= MOD) dp[i] %= MOD;
            }
        }
        
        return dp[target];
    }
    
    // 记忆化搜索版本（DFS + 记忆化）
    int combinationSum4Memo(vector<int>& nums, int target) {
        if (nums.empty()) return 0;
        if (target == 0) return 1;
        
        vector<long long> memo(target + 1, -1);
        memo[0] = 1;
        
        return dfs(nums, target, memo);
    }
    
private:
    int dfs(vector<int>& nums, int target, vector<long long>& memo) {
        if (target < 0) return 0;
        if (memo[target] != -1) return memo[target];
        
        long long count = 0;
        for (int num : nums) {
            if (target >= num) {
                count += dfs(nums, target - num, memo);
                if (count >= MOD) count %= MOD;
            }
        }
        
        memo[target] = count;
        return count;
    }
};

// 测试函数
void testCombinationSum4() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 3};
    int target1 = 4;
    cout << "测试用例1:" << endl;
    cout << "标准版本: " << solution.combinationSum4(nums1, target1) << endl;
    cout << "优化版本: " << solution.combinationSum4Optimized(nums1, target1) << endl;
    cout << "记忆化版本: " << solution.combinationSum4Memo(nums1, target1) << endl;
    cout << "预期结果: 7" << endl;
    cout << "解释：可能的组合有：" << endl;
    cout << "(1, 1, 1, 1), (1, 1, 2), (1, 2, 1), (1, 3), (2, 1, 1), (2, 2), (3, 1)" << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {9};
    int target2 = 3;
    cout << "测试用例2:" << endl;
    cout << "标准版本: " << solution.combinationSum4(nums2, target2) << endl;
    cout << "优化版本: " << solution.combinationSum4Optimized(nums2, target2) << endl;
    cout << "记忆化版本: " << solution.combinationSum4Memo(nums2, target2) << endl;
    cout << "预期结果: 0" << endl;
    cout << endl;
    
    // 测试用例3：边界情况
    vector<int> nums3 = {};
    int target3 = 0;
    cout << "测试用例3（边界情况）:" << endl;
    cout << "标准版本: " << solution.combinationSum4(nums3, target3) << endl;
    cout << "优化版本: " << solution.combinationSum4Optimized(nums3, target3) << endl;
    cout << "记忆化版本: " << solution.combinationSum4Memo(nums3, target3) << endl;
    cout << "预期结果: 1" << endl;
    cout << endl;
    
    // 测试用例4：较大规模
    vector<int> nums4 = {1, 2, 4, 8};
    int target4 = 10;
    cout << "测试用例4:" << endl;
    cout << "标准版本: " << solution.combinationSum4(nums4, target4) << endl;
    cout << "优化版本: " << solution.combinationSum4Optimized(nums4, target4) << endl;
    cout << "记忆化版本: " << solution.combinationSum4Memo(nums4, target4) << endl;
    cout << "预期结果: 64" << endl;
}

int main() {
    testCombinationSum4();
    return 0;
}

/*
 * =============================================================================================
 * 补充题目：LeetCode 518. 零钱兑换 II（C++实现）
 * 题目链接：https://leetcode.cn/problems/coin-change-ii/
 * 
 * C++实现：
 * class Solution {
 * public:
 *     int change(int amount, vector<int>& coins) {
 *         if (amount == 0) return 1;
 *         if (coins.empty()) return 0;
 *         
 *         vector<long long> dp(amount + 1, 0);
 *         dp[0] = 1;
 *         
 *         // 外层遍历物品，内层遍历背包容量（不考虑顺序）
 *         for (int coin : coins) {
 *             for (int i = coin; i <= amount; i++) {
 *                 dp[i] += dp[i - coin];
 *             }
 *         }
 *         
 *         return dp[amount];
 *     }
 * };
 * 
 * 工程化考量：
 * 1. 使用long long类型防止整数溢出
 * 2. 使用引用避免不必要的拷贝
 * 3. 添加输入验证和异常处理
 * 4. 使用const引用作为函数参数
 * 
 * 优化思路：
 * 1. 空间压缩：使用一维数组进行优化
 * 2. 剪枝优化：当硬币面额大于剩余金额时跳过
 * 3. 预处理：计算硬币的最大公约数
 */
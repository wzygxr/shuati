#include <iostream>
#include <vector>
#include <unordered_map>
#include <string>

// LeetCode 494. 目标和
// 题目描述：给你一个整数数组 nums 和一个整数 target 。
// 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个 表达式 ：
// 例如，nums = [2, 1] ，可以在 2 之前添加 '+' ，在 1 之前添加 '-' ，然后串联起来得到表达式 "+2-1" 。
// 返回可以通过上述方法构造的、运算结果等于 target 的不同 表达式 的数目。
// 链接：https://leetcode.cn/problems/target-sum/
// 
// 解题思路：
// 这是一个0-1背包问题的变种。我们可以将问题转化为：找到一个子集P，使得sum(P) - sum(N) = target，其中N是数组中不在P中的元素。
// 可以证明：sum(P) - sum(N) = target => sum(P) = (sum(nums) + target) / 2
// 因此，问题转化为：在数组nums中，有多少个子集的和等于(sum(nums) + target) / 2。
// 
// 状态定义：dp[j] 表示和为j的子集数目
// 状态转移方程：dp[j] += dp[j - nums[i]]，其中nums[i]是当前元素，且j >= nums[i]
// 初始状态：dp[0] = 1，表示和为0的子集数目为1（空集）
// 
// 时间复杂度：O(n * sum)，其中n是数组的长度，sum是数组元素的和
// 空间复杂度：O(sum)，使用一维DP数组

class Solution {
public:
    // 基础DP解法
    int findTargetSumWays(std::vector<int>& nums, int target) {
        int n = nums.size();
        if (n == 0) {
            return target == 0 ? 1 : 0;
        }
        
        // 计算数组元素的总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        // 检查是否有解
        // 1. sum < abs(target)：总和小于目标值的绝对值，无解
        // 2. (sum + target) % 2 != 0：sum + target必须是偶数，否则无法平均分成两部分
        if (sum < std::abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        // 计算目标和：sum(P) = (sum + target) / 2
        int targetSum = (sum + target) / 2;
        if (targetSum < 0) {
            return 0; // 目标和为负数，无解
        }
        
        // 创建DP数组，dp[j]表示和为j的子集数目
        std::vector<int> dp(targetSum + 1, 0);
        
        // 初始状态：和为0的子集数目为1（空集）
        dp[0] = 1;
        
        // 填充DP数组
        for (int num : nums) {
            // 注意：这里我们从后往前遍历，避免重复使用同一个元素
            for (int j = targetSum; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        
        return dp[targetSum];
    }
    
    // 使用二维DP数组的实现
    int findTargetSumWays2D(std::vector<int>& nums, int target) {
        int n = nums.size();
        if (n == 0) {
            return target == 0 ? 1 : 0;
        }
        
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        
        if (sum < std::abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        int targetSum = (sum + target) / 2;
        if (targetSum < 0) {
            return 0;
        }
        
        // 创建二维DP数组
        std::vector<std::vector<int>> dp(n + 1, std::vector<int>(targetSum + 1, 0));
        
        // 初始状态：使用前0个元素，和为0的子集数目为1（空集）
        dp[0][0] = 1;
        
        // 填充DP数组
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1]; // 当前元素
            for (int j = 0; j <= targetSum; j++) {
                // 不选择当前元素
                dp[i][j] = dp[i - 1][j];
                // 选择当前元素（如果j >= num）
                if (j >= num) {
                    dp[i][j] += dp[i - 1][j - num];
                }
            }
        }
        
        return dp[n][targetSum];
    }
    
    // 使用回溯法的实现
    int findTargetSumWaysBacktrack(std::vector<int>& nums, int target) {
        int count = 0;
        backtrack(nums, target, 0, 0, count);
        return count;
    }
    
    // 使用记忆化递归的实现
    int findTargetSumWaysMemo(std::vector<int>& nums, int target) {
        // 创建记忆化缓存，键为(index, currentSum)，值为对应的方法数
        std::unordered_map<std::string, int> memo;
        return backtrackMemo(nums, target, 0, 0, memo);
    }
    
    // 优化版本，考虑数组中包含0的情况
    int findTargetSumWaysOptimized(std::vector<int>& nums, int target) {
        int n = nums.size();
        if (n == 0) {
            return target == 0 ? 1 : 0;
        }
        
        int sum = 0;
        int zeroCount = 0;
        
        // 计算总和和0的个数
        for (int num : nums) {
            sum += num;
            if (num == 0) {
                zeroCount++;
            }
        }
        
        // 检查是否有解
        if (sum < std::abs(target) || (sum + target) % 2 != 0) {
            return 0;
        }
        
        int targetSum = (sum + target) / 2;
        if (targetSum < 0) {
            return 0;
        }
        
        // 过滤掉0，单独处理
        std::vector<int> nonZeroNums;
        for (int num : nums) {
            if (num != 0) {
                nonZeroNums.push_back(num);
            }
        }
        
        // 创建DP数组
        std::vector<int> dp(targetSum + 1, 0);
        dp[0] = 1;
        
        // 填充DP数组
        for (int num : nonZeroNums) {
            for (int j = targetSum; j >= num; j--) {
                dp[j] += dp[j - num];
            }
        }
        
        // 每个0有两种选择（+0或-0），所以总方法数乘以2^zeroCount
        return dp[targetSum] * static_cast<int>(std::pow(2, zeroCount));
    }
    
    // 打印所有可能的表达式
    void printAllExpressions(std::vector<int>& nums, int target) {
        std::vector<std::string> result;
        std::string currentExpr;
        
        // 第一个数特殊处理，不需要前面的符号
        currentExpr = std::to_string(nums[0]);
        backtrackExpressions(nums, target, 1, nums[0], currentExpr, result);
        
        // 尝试第一个数为负数的情况
        currentExpr = "-" + std::to_string(nums[0]);
        backtrackExpressions(nums, target, 1, -nums[0], currentExpr, result);
        
        std::cout << "所有可能的表达式:" << std::endl;
        for (const std::string& expr : result) {
            std::cout << expr << std::endl;
        }
        std::cout << "总共有 " << result.size() << " 种不同的表达式。" << std::endl;
    }

private:
    // 回溯辅助方法
    void backtrack(const std::vector<int>& nums, int target, int index, int currentSum, int& count) {
        // 已经处理完所有元素
        if (index == nums.size()) {
            if (currentSum == target) {
                count++;
            }
            return;
        }
        
        // 尝试加上当前元素
        backtrack(nums, target, index + 1, currentSum + nums[index], count);
        
        // 尝试减去当前元素
        backtrack(nums, target, index + 1, currentSum - nums[index], count);
    }
    
    // 记忆化递归辅助方法
    int backtrackMemo(const std::vector<int>& nums, int target, int index, int currentSum,
                      std::unordered_map<std::string, int>& memo) {
        // 已经处理完所有元素
        if (index == nums.size()) {
            return currentSum == target ? 1 : 0;
        }
        
        // 生成缓存键
        std::string key = std::to_string(index) + "," + std::to_string(currentSum);
        
        // 检查是否已经计算过
        if (memo.find(key) != memo.end()) {
            return memo[key];
        }
        
        // 计算两种情况的结果之和
        int add = backtrackMemo(nums, target, index + 1, currentSum + nums[index], memo);
        int subtract = backtrackMemo(nums, target, index + 1, currentSum - nums[index], memo);
        
        // 存储结果到缓存
        memo[key] = add + subtract;
        
        return add + subtract;
    }
    
    // 回溯辅助方法，用于生成所有可能的表达式
    void backtrackExpressions(const std::vector<int>& nums, int target, int index, int currentSum,
                             std::string currentExpr, std::vector<std::string>& result) {
        if (index == nums.size()) {
            if (currentSum == target) {
                result.push_back(currentExpr);
            }
            return;
        }
        
        int num = nums[index];
        
        // 尝试加上当前元素
        backtrackExpressions(nums, target, index + 1, currentSum + num,
                           currentExpr + "+" + std::to_string(num), result);
        
        // 尝试减去当前元素
        backtrackExpressions(nums, target, index + 1, currentSum - num,
                           currentExpr + "-" + std::to_string(num), result);
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> nums1 = {1, 1, 1, 1, 1};
    int target1 = 3;
    std::cout << "测试用例1结果: " << solution.findTargetSumWays(nums1, target1) << " (预期: 5)" << std::endl;
    solution.printAllExpressions(nums1, target1);
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {1};
    int target2 = 1;
    std::cout << "测试用例2结果: " << solution.findTargetSumWays(nums2, target2) << " (预期: 1)" << std::endl;
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例3
    std::vector<int> nums3 = {1, 2, 3, 4, 5};
    int target3 = 3;
    std::cout << "测试用例3结果: " << solution.findTargetSumWays(nums3, target3) << " (预期: 5)" << std::endl;
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例4 - 无法满足的情况
    std::vector<int> nums4 = {1, 2, 3};
    int target4 = 7;
    std::cout << "测试用例4结果: " << solution.findTargetSumWays(nums4, target4) << " (预期: 0)" << std::endl;
    
    // 测试各种实现方法
    std::cout << "\n测试各种实现方法:" << std::endl;
    std::cout << "二维DP版本: " << solution.findTargetSumWays2D(nums1, target1) << std::endl;
    std::cout << "回溯版本: " << solution.findTargetSumWaysBacktrack(nums1, target1) << std::endl;
    std::cout << "记忆化递归版本: " << solution.findTargetSumWaysMemo(nums1, target1) << std::endl;
    std::cout << "优化版本: " << solution.findTargetSumWaysOptimized(nums1, target1) << std::endl;
    
    return 0;
}
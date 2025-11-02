#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

// 最长递增子序列 (Longest Increasing Subsequence, LIS)
// 题目链接: https://leetcode.cn/problems/longest-increasing-subsequence/
// 难度: 中等
// 这是一个经典的动态规划问题，也可以用贪心+二分查找优化
class Solution {
public:
    // 方法1: 暴力递归（超时解法，仅作为思路展示）
    // 时间复杂度: O(2^n) - 每个元素有选或不选两种选择
    // 空间复杂度: O(n) - 递归调用栈深度
    int lengthOfLIS1(std::vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        return process1(nums, 0, INT_MIN);
    }

private:
    // 递归函数: 从index位置开始，前面的最大值为prevMax时，能形成的最长递增子序列长度
    int process1(const std::vector<int>& nums, int index, int prevMax) {
        // 基本情况: 已经处理完所有元素
        if (index == nums.size()) {
            return 0;
        }
        
        // 选择不使用当前元素
        int notTake = process1(nums, index + 1, prevMax);
        
        // 选择使用当前元素（如果可以）
        int take = 0;
        if (nums[index] > prevMax) {
            take = 1 + process1(nums, index + 1, nums[index]);
        }
        
        // 返回两种选择中的最大值
        return std::max(notTake, take);
    }

public:
    // 方法2: 记忆化搜索（带备忘录的递归）
    // 时间复杂度: O(n^2) - 每个状态只计算一次，共有n^2个状态
    // 空间复杂度: O(n^2) - 备忘录大小
    int lengthOfLIS2(std::vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        int n = nums.size();
        // memo[i][j] 表示从第i个位置开始，前面最大值为nums[j]时的LIS长度
        // j = nums.size()表示前面最大值为负无穷的情况
        std::vector<std::vector<int>> memo(n, std::vector<int>(n + 1, -1));
        return process2(nums, 0, n, memo);
    }

private:
    int process2(const std::vector<int>& nums, int index, int prevIndex, std::vector<std::vector<int>>& memo) {
        if (index == nums.size()) {
            return 0;
        }
        
        // 检查是否已经计算过
        if (memo[index][prevIndex] != -1) {
            return memo[index][prevIndex];
        }
        
        // 不选当前元素
        int notTake = process2(nums, index + 1, prevIndex, memo);
        
        // 选当前元素（如果可以）
        int take = 0;
        if (prevIndex == nums.size() || nums[index] > nums[prevIndex]) {
            take = 1 + process2(nums, index + 1, index, memo);
        }
        
        // 记录结果
        memo[index][prevIndex] = std::max(notTake, take);
        return memo[index][prevIndex];
    }

public:
    // 方法3: 动态规划（自底向上）
    // 时间复杂度: O(n^2) - 双重循环
    // 空间复杂度: O(n) - dp数组大小
    int lengthOfLIS3(std::vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        int n = nums.size();
        // dp[i]表示以nums[i]结尾的最长递增子序列的长度
        std::vector<int> dp(n, 1);
        
        int maxLength = 1;
        // 遍历每个元素作为结尾
        for (int i = 1; i < n; i++) {
            // 遍历i之前的所有元素
            for (int j = 0; j < i; j++) {
                // 如果前面的元素小于当前元素，可以接在后面形成更长的递增子序列
                if (nums[i] > nums[j]) {
                    dp[i] = std::max(dp[i], dp[j] + 1);
                }
            }
            // 更新全局最大值
            maxLength = std::max(maxLength, dp[i]);
        }
        
        return maxLength;
    }

    // 方法4: 贪心 + 二分查找（最优解）
    // 时间复杂度: O(n log n) - 遍历数组O(n)，每次二分查找O(log n)
    // 空间复杂度: O(n) - tails数组大小
    // 核心思想: 维护一个数组tails，其中tails[i]表示长度为i+1的所有递增子序列的结尾元素的最小值
    int lengthOfLIS4(std::vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        std::vector<int> tails;
        
        for (int num : nums) {
            // 二分查找num在tails数组中的插入位置
            auto it = std::lower_bound(tails.begin(), tails.end(), num);
            
            // 更新tails数组
            if (it == tails.end()) {
                tails.push_back(num);
            } else {
                *it = num;
            }
        }
        
        // tails数组的长度就是最长递增子序列的长度
        return tails.size();
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1: 标准测试
    std::vector<int> nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
    std::cout << "测试用例1结果:" << std::endl;
    std::cout << "暴力递归: " << solution.lengthOfLIS1(nums1) << std::endl; // 预期输出: 4
    std::cout << "记忆化搜索: " << solution.lengthOfLIS2(nums1) << std::endl; // 预期输出: 4
    std::cout << "动态规划: " << solution.lengthOfLIS3(nums1) << std::endl; // 预期输出: 4
    std::cout << "贪心+二分: " << solution.lengthOfLIS4(nums1) << std::endl; // 预期输出: 4
    
    // 测试用例2: 完全递增数组
    std::vector<int> nums2 = {1, 2, 3, 4, 5};
    std::cout << "\n测试用例2结果:" << std::endl;
    std::cout << "贪心+二分: " << solution.lengthOfLIS4(nums2) << std::endl; // 预期输出: 5
    
    // 测试用例3: 完全递减数组
    std::vector<int> nums3 = {5, 4, 3, 2, 1};
    std::cout << "\n测试用例3结果:" << std::endl;
    std::cout << "贪心+二分: " << solution.lengthOfLIS4(nums3) << std::endl; // 预期输出: 1
    
    // 测试用例4: 空数组
    std::vector<int> nums4 = {};
    std::cout << "\n测试用例4结果:" << std::endl;
    std::cout << "贪心+二分: " << solution.lengthOfLIS4(nums4) << std::endl; // 预期输出: 0
    
    // 测试用例5: 单元素数组
    std::vector<int> nums5 = {1};
    std::cout << "\n测试用例5结果:" << std::endl;
    std::cout << "贪心+二分: " << solution.lengthOfLIS4(nums5) << std::endl; // 预期输出: 1
    
    return 0;
}
#include <iostream>
#include <vector>
#include <algorithm>
#include <string>

/**
 * 摆动序列 - LeetCode 376
 * 题目来源：https://leetcode.cn/problems/wiggle-subsequence/
 * 难度：中等
 * 题目描述：如果连续数字之间的差严格地在正数和负数之间交替，则数字序列称为摆动序列。
 * 第一个差（如果存在的话）可能是正数或负数。少于两个元素的序列也是摆动序列。
 * 例如，[1,7,4,9,2,5] 是一个摆动序列，因为差值 (6,-3,5,-7,3) 是正负交替出现的。
 * 相反, [1,4,7,2,5] 和 [1,7,4,5,5] 不是摆动序列，第一个序列是因为它的前两个差值都是正数，
 * 第二个序列是因为它的最后一个差值为零。
 * 
 * 核心思路：
 * 1. 这道题可以使用贪心算法来解决，因为我们只需要记录序列的趋势变化
 * 2. 摆动序列的关键在于相邻元素的差值交替变化
 * 3. 我们可以维护当前的趋势（上升、下降或初始状态），然后遍历数组，统计趋势变化的次数
 * 
 * 复杂度分析：
 * 时间复杂度：O(n)，其中n是数组的长度，我们只需要遍历一次数组
 * 空间复杂度：O(1)，只使用了常数级别的额外空间
 */

class Solution {
public:
    /**
     * 计算最长摆动子序列的长度 - 动态规划优化版本
     * @param nums 输入数组
     * @return 最长摆动子序列的长度
     */
    int wiggleMaxLength(const std::vector<int>& nums) {
        // 边界情况：如果数组长度小于2，直接返回数组长度
        if (nums.empty()) {
            return 0;
        }
        if (nums.size() == 1) {
            return 1;
        }
        
        int up = 1;   // 以最后一个差值为正的最长摆动子序列长度
        int down = 1; // 以最后一个差值为负的最长摆动子序列长度
        
        // 遍历数组，从第二个元素开始
        for (int i = 1; i < nums.size(); ++i) {
            if (nums[i] > nums[i - 1]) {
                // 当前是上升趋势，最长摆动子序列长度等于之前下降趋势的长度加1
                up = down + 1;
            } else if (nums[i] < nums[i - 1]) {
                // 当前是下降趋势，最长摆动子序列长度等于之前上升趋势的长度加1
                down = up + 1;
            }
            // 如果相等，不做任何操作，保持up和down不变
        }
        
        // 返回较大的值，因为最后一个差值可能是正也可能是负
        return std::max(up, down);
    }
    
    /**
     * 贪心算法解法 - 记录趋势变化
     * @param nums 输入数组
     * @return 最长摆动子序列的长度
     */
    int wiggleMaxLengthGreedy(const std::vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        if (nums.size() == 1) {
            return 1;
        }
        
        int count = 1;    // 至少有一个元素
        int prevDiff = 0; // 前一个差值
        int currDiff = 0; // 当前差值
        
        for (int i = 1; i < nums.size(); ++i) {
            currDiff = nums[i] - nums[i - 1];
            
            // 如果当前差值与前一个差值符号不同，说明出现了摆动
            if ((currDiff > 0 && prevDiff <= 0) || (currDiff < 0 && prevDiff >= 0)) {
                ++count;
                prevDiff = currDiff;
            }
        }
        
        return count;
    }
    
    /**
     * 动态规划解法 - 标准DP
     * @param nums 输入数组
     * @return 最长摆动子序列的长度
     */
    int wiggleMaxLengthDP(const std::vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        int n = nums.size();
        if (n == 1) {
            return 1;
        }
        
        // dp[i][0]: 以nums[i]结尾且最后一个差值为正的最长摆动子序列长度
        // dp[i][1]: 以nums[i]结尾且最后一个差值为负的最长摆动子序列长度
        std::vector<std::vector<int>> dp(n, std::vector<int>(2, 1));
        
        int maxLen = 1;
        
        for (int i = 1; i < n; ++i) {
            for (int j = 0; j < i; ++j) {
                if (nums[i] > nums[j]) {
                    // 如果nums[i] > nums[j]，可以接在以nums[j]结尾且最后一个差值为负的序列后面
                    dp[i][0] = std::max(dp[i][0], dp[j][1] + 1);
                } else if (nums[i] < nums[j]) {
                    // 如果nums[i] < nums[j]，可以接在以nums[j]结尾且最后一个差值为正的序列后面
                    dp[i][1] = std::max(dp[i][1], dp[j][0] + 1);
                }
            }
            // 更新最大值
            maxLen = std::max(maxLen, std::max(dp[i][0], dp[i][1]));
        }
        
        return maxLen;
    }
};

// 辅助函数：打印数组
void printVector(const std::vector<int>& vec) {
    std::cout << "[";
    for (size_t i = 0; i < vec.size(); ++i) {
        std::cout << vec[i];
        if (i < vec.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]";
}

// 测试函数：测试所有解法
void testAllSolutions(const std::vector<int>& nums, Solution& solution) {
    std::cout << "输入数组: ";
    printVector(nums);
    std::cout << std::endl;
    
    std::cout << "解法1（动态规划优化版）: " << solution.wiggleMaxLength(nums) << std::endl;
    std::cout << "解法2（贪心算法）: " << solution.wiggleMaxLengthGreedy(nums) << std::endl;
    std::cout << "解法3（常规动态规划）: " << solution.wiggleMaxLengthDP(nums) << std::endl;
    std::cout << "--------------------------" << std::endl;
}

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> nums1 = {1, 7, 4, 9, 2, 5};
    std::cout << "测试用例1：" << std::endl;
    std::cout << "预期结果：6" << std::endl;
    std::cout << "实际结果：" << solution.wiggleMaxLength(nums1) << std::endl;
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {1, 17, 5, 10, 13, 15, 10, 5, 16, 8};
    std::cout << "测试用例2：" << std::endl;
    std::cout << "预期结果：7" << std::endl;
    std::cout << "实际结果：" << solution.wiggleMaxLength(nums2) << std::endl;
    std::cout << std::endl;
    
    // 测试用例3
    std::vector<int> nums3 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    std::cout << "测试用例3：" << std::endl;
    std::cout << "预期结果：2" << std::endl;
    std::cout << "实际结果：" << solution.wiggleMaxLength(nums3) << std::endl;
    std::cout << std::endl;
    
    // 测试用例4：边界情况
    std::vector<int> nums4 = {1}; // 只有一个元素
    std::cout << "测试用例4：" << std::endl;
    std::cout << "预期结果：1" << std::endl;
    std::cout << "实际结果：" << solution.wiggleMaxLength(nums4) << std::endl;
    std::cout << std::endl;
    
    // 测试用例5：边界情况
    std::vector<int> nums5 = {1, 1}; // 所有元素相同
    std::cout << "测试用例5：" << std::endl;
    std::cout << "预期结果：1" << std::endl;
    std::cout << "实际结果：" << solution.wiggleMaxLength(nums5) << std::endl;
    std::cout << std::endl;
    
    // 详细测试所有解法
    std::cout << "详细比较所有解法：" << std::endl;
    std::cout << "--------------------------" << std::endl;
    
    testAllSolutions(nums1, solution);
    testAllSolutions(nums2, solution);
    testAllSolutions(nums3, solution);
    testAllSolutions(nums4, solution);
    testAllSolutions(nums5, solution);
    
    return 0;
}
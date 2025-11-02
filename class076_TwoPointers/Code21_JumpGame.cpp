#include <iostream>
#include <vector>
#include <chrono>
#include <random>

/**
 * LeetCode 55. 跳跃游戏 (Jump Game)
 * 
 * 题目描述:
 * 给定一个非负整数数组 nums，你最初位于数组的第一个位置。
 * 数组中的每个元素代表你在该位置可以跳跃的最大长度。
 * 判断你是否能够到达最后一个位置。
 * 
 * 示例1:
 * 输入: nums = [2,3,1,1,4]
 * 输出: true
 * 解释: 可以先跳 1 步，从位置 0 到达位置 1, 然后再从位置 1 跳 3 步到达最后一个位置。
 * 
 * 示例2:
 * 输入: nums = [3,2,1,0,4]
 * 输出: false
 * 解释: 无论怎样，总会到达索引为 3 的位置。但该位置的最大跳跃长度是 0 ， 所以永远不能到达最后一个位置。
 * 
 * 提示:
 * 1 <= nums.length <= 10^4
 * 0 <= nums[i] <= 10^5
 * 
 * 题目链接: https://leetcode.com/problems/jump-game/
 * 
 * 解题思路:
 * 这道题可以使用贪心算法来解决。我们的目标是判断是否能够到达最后一个位置。
 * 
 * 贪心策略：维护一个变量表示当前能够到达的最远位置。遍历数组，不断更新这个最远位置。
 * 如果在任何时候，当前能够到达的最远位置小于当前遍历到的索引，说明无法到达该位置，也就无法到达最后一个位置。
 * 
 * 具体来说，我们维护一个变量 maxReach，表示当前能够到达的最远位置。初始时，maxReach = 0。
 * 遍历数组，对于每个位置 i，如果 i > maxReach，说明无法到达位置 i，返回 false。
 * 否则，更新 maxReach = max(maxReach, i + nums[i])。
 * 如果 maxReach >= nums.size() - 1，说明已经可以到达最后一个位置，返回 true。
 * 
 * 时间复杂度: O(n)，其中 n 是数组的长度。我们只需要遍历数组一次。
 * 空间复杂度: O(1)，只使用了常数级别的额外空间。
 * 
 * 此外，我们还提供三种其他解法：
 * 1. 动态规划解法（自顶向下）：时间复杂度 O(n^2)，空间复杂度 O(n)
 * 2. 动态规划解法（自底向上）：时间复杂度 O(n^2)，空间复杂度 O(n)
 * 3. 回溯解法：时间复杂度 O(2^n)，空间复杂度 O(n)，但在大规模输入时可能会超时
 */

class Solution {
public:
    /**
     * 解法一: 贪心算法（最优解）
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    bool canJumpGreedy(const std::vector<int>& nums) {
        // 参数校验
        if (nums.size() <= 1) {
            return true; // 如果数组为空或只有一个元素，已经在终点
        }
        
        int maxReach = 0; // 当前能够到达的最远位置
        
        // 遍历数组
        for (int i = 0; i < nums.size(); i++) {
            // 如果当前位置已经无法到达，返回false
            if (i > maxReach) {
                return false;
            }
            
            // 更新能够到达的最远位置
            maxReach = std::max(maxReach, i + nums[i]);
            
            // 如果已经可以到达或超过最后一个位置，可以提前返回true
            if (maxReach >= static_cast<int>(nums.size()) - 1) {
                return true;
            }
        }
        
        // 遍历完整个数组后，判断是否能够到达最后一个位置
        return maxReach >= static_cast<int>(nums.size()) - 1;
    }
    
    /**
     * 解法二: 动态规划 - 自顶向下
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    bool canJumpDynamicProgrammingTopDown(const std::vector<int>& nums) {
        // 参数校验
        if (nums.size() <= 1) {
            return true; // 如果数组为空或只有一个元素，已经在终点
        }
        
        int n = nums.size();
        // memo[i]表示从位置i是否可以到达最后一个位置
        // 0: 未计算, 1: 可以到达, 2: 无法到达
        std::vector<int> memo(n, 0);
        // 最后一个位置可以到达自身
        memo[n - 1] = 1;
        
        return canJumpFromPosition(0, nums, memo);
    }
    
    /**
     * 辅助方法：判断从位置pos是否可以到达最后一个位置
     * 
     * @param pos 当前位置
     * @param nums 非负整数数组
     * @param memo 记忆化数组
     * @return 是否能够到达最后一个位置
     */
    bool canJumpFromPosition(int pos, const std::vector<int>& nums, std::vector<int>& memo) {
        // 如果已经计算过，直接返回结果
        if (memo[pos] != 0) {
            return memo[pos] == 1;
        }
        
        // 计算从当前位置可以到达的最远位置
        int furthestJump = std::min(pos + nums[pos], static_cast<int>(nums.size()) - 1);
        
        // 尝试从当前位置跳到所有可能的位置
        for (int nextPos = pos + 1; nextPos <= furthestJump; nextPos++) {
            if (canJumpFromPosition(nextPos, nums, memo)) {
                memo[pos] = 1; // 可以到达
                return true;
            }
        }
        
        memo[pos] = 2; // 无法到达
        return false;
    }
    
    /**
     * 解法三: 动态规划 - 自底向上
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    bool canJumpDynamicProgrammingBottomUp(const std::vector<int>& nums) {
        // 参数校验
        if (nums.size() <= 1) {
            return true; // 如果数组为空或只有一个元素，已经在终点
        }
        
        int n = nums.size();
        // dp[i]表示从位置i是否可以到达最后一个位置
        std::vector<bool> dp(n, false);
        // 最后一个位置可以到达自身
        dp[n - 1] = true;
        
        // 从后往前遍历
        for (int i = n - 2; i >= 0; i--) {
            // 计算从当前位置可以到达的最远位置
            int furthestJump = std::min(i + nums[i], n - 1);
            
            // 检查从当前位置能否跳到一个可以到达终点的位置
            for (int j = i + 1; j <= furthestJump; j++) {
                if (dp[j]) {
                    dp[i] = true;
                    break; // 一旦找到一个可达的位置，就可以停止检查
                }
            }
        }
        
        // 返回是否可以从起始位置到达终点
        return dp[0];
    }
    
    /**
     * 解法四: 回溯（暴力解法，在大规模输入时可能会超时）
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    bool canJumpBacktracking(const std::vector<int>& nums) {
        // 参数校验
        if (nums.size() <= 1) {
            return true; // 如果数组为空或只有一个元素，已经在终点
        }
        
        return canJumpFromPositionBacktracking(0, nums);
    }
    
    /**
     * 辅助方法：使用回溯判断从位置pos是否可以到达最后一个位置
     * 
     * @param pos 当前位置
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    bool canJumpFromPositionBacktracking(int pos, const std::vector<int>& nums) {
        // 基本情况：已经到达最后一个位置
        if (pos == static_cast<int>(nums.size()) - 1) {
            return true;
        }
        
        // 计算从当前位置可以到达的最远位置
        int furthestJump = std::min(pos + nums[pos], static_cast<int>(nums.size()) - 1);
        
        // 尝试从当前位置跳到所有可能的位置（优先尝试跳得更远）
        for (int nextPos = furthestJump; nextPos > pos; nextPos--) {
            if (canJumpFromPositionBacktracking(nextPos, nums)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * LeetCode官方接口的实现（使用贪心算法）
     * 
     * @param nums 非负整数数组
     * @return 是否能够到达最后一个位置
     */
    bool canJump(const std::vector<int>& nums) {
        return canJumpGreedy(nums);
    }
};

/**
 * 打印数组
 */
void printArray(const std::vector<int>& arr) {
    std::cout << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        std::cout << arr[i];
        if (i < arr.size() - 1) {
            std::cout << ", ";
        }
    }
    std::cout << "]" << std::endl;
}

/**
 * 性能测试
 */
void performanceTest(const std::vector<int>& nums, Solution& solution) {
    // 测试贪心算法
    auto startTime = std::chrono::high_resolution_clock::now();
    bool result1 = solution.canJumpGreedy(nums);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "贪心算法结果: " << (result1 ? "true" : "false") << std::endl;
    std::cout << "贪心算法耗时: " << duration.count() << "ms" << std::endl;
    
    // 测试动态规划自底向上
    startTime = std::chrono::high_resolution_clock::now();
    bool result2 = solution.canJumpDynamicProgrammingBottomUp(nums);
    endTime = std::chrono::high_resolution_clock::now();
    duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "动态规划(自底向上)结果: " << (result2 ? "true" : "false") << std::endl;
    std::cout << "动态规划(自底向上)耗时: " << duration.count() << "ms" << std::endl;
    
    // 注意：以下两种方法在大规模数组上可能会超时，所以只在小规模数组上测试
    if (nums.size() <= 1000) {
        // 测试动态规划自顶向下
        startTime = std::chrono::high_resolution_clock::now();
        bool result3 = solution.canJumpDynamicProgrammingTopDown(nums);
        endTime = std::chrono::high_resolution_clock::now();
        duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
        std::cout << "动态规划(自顶向下)结果: " << (result3 ? "true" : "false") << std::endl;
        std::cout << "动态规划(自顶向下)耗时: " << duration.count() << "ms" << std::endl;
        
        // 测试回溯算法
        if (nums.size() <= 30) { // 回溯算法在小数据量下才能快速运行
            startTime = std::chrono::high_resolution_clock::now();
            bool result4 = solution.canJumpBacktracking(nums);
            endTime = std::chrono::high_resolution_clock::now();
            duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
            std::cout << "回溯算法结果: " << (result4 ? "true" : "false") << std::endl;
            std::cout << "回溯算法耗时: " << duration.count() << "ms" << std::endl;
        } else {
            std::cout << "数组过大，跳过回溯算法测试" << std::endl;
        }
    } else {
        std::cout << "数组过大，跳过动态规划(自顶向下)和回溯算法测试" << std::endl;
    }
}

/**
 * 生成测试用例
 */
std::vector<int> generateTestCase(int n, bool canReachEnd) {
    std::vector<int> nums(n);
    
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<> distrib(1, 5);
    
    if (canReachEnd) {
        // 生成可以到达终点的数组
        for (int i = 0; i < n - 1; i++) {
            // 确保可以到达终点，当前位置的值至少为n-1-i
            int minVal = n - 1 - i;
            int randVal = distrib(gen);
            nums[i] = std::max(randVal, minVal);
        }
    } else {
        // 生成无法到达终点的数组
        std::uniform_int_distribution<> posDistrib(1, n - 2);
        int zeroPosition = posDistrib(gen); // 确保0不在最后一个位置
        nums[zeroPosition] = 0;
        
        // 填充0之前的位置
        for (int i = 0; i < zeroPosition; i++) {
            // 确保无法越过0
            int maxVal = zeroPosition - i;
            int randVal = distrib(gen);
            nums[i] = std::min(randVal, maxVal);
        }
        
        // 填充0之后的位置
        for (int i = zeroPosition + 1; i < n; i++) {
            nums[i] = distrib(gen);
        }
    }
    
    nums[n - 1] = 0; // 最后一个元素不影响
    return nums;
}

int main() {
    Solution solution;
    
    // 测试用例1
    std::vector<int> nums1 = {2, 3, 1, 1, 4};
    std::cout << "测试用例1:" << std::endl;
    std::cout << "nums = ";
    printArray(nums1);
    std::cout << "贪心算法结果: " << (solution.canJumpGreedy(nums1) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "动态规划(自顶向下)结果: " << (solution.canJumpDynamicProgrammingTopDown(nums1) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "动态规划(自底向上)结果: " << (solution.canJumpDynamicProgrammingBottomUp(nums1) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "回溯算法结果: " << (solution.canJumpBacktracking(nums1) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << std::endl;
    
    // 测试用例2
    std::vector<int> nums2 = {3, 2, 1, 0, 4};
    std::cout << "测试用例2:" << std::endl;
    std::cout << "nums = ";
    printArray(nums2);
    std::cout << "贪心算法结果: " << (solution.canJumpGreedy(nums2) ? "true" : "false") << std::endl; // 预期输出: false
    std::cout << "动态规划(自顶向下)结果: " << (solution.canJumpDynamicProgrammingTopDown(nums2) ? "true" : "false") << std::endl; // 预期输出: false
    std::cout << "动态规划(自底向上)结果: " << (solution.canJumpDynamicProgrammingBottomUp(nums2) ? "true" : "false") << std::endl; // 预期输出: false
    std::cout << "回溯算法结果: " << (solution.canJumpBacktracking(nums2) ? "true" : "false") << std::endl; // 预期输出: false
    std::cout << std::endl;
    
    // 测试用例3 - 边界情况：只有一个元素
    std::vector<int> nums3 = {0};
    std::cout << "测试用例3（单元素数组）:" << std::endl;
    std::cout << "nums = ";
    printArray(nums3);
    std::cout << "贪心算法结果: " << (solution.canJumpGreedy(nums3) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "动态规划(自顶向下)结果: " << (solution.canJumpDynamicProgrammingTopDown(nums3) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "动态规划(自底向上)结果: " << (solution.canJumpDynamicProgrammingBottomUp(nums3) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "回溯算法结果: " << (solution.canJumpBacktracking(nums3) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << std::endl;
    
    // 测试用例4 - 边界情况：全是0
    std::vector<int> nums4 = {0, 0, 0, 0, 0};
    std::cout << "测试用例4（全是0）:" << std::endl;
    std::cout << "nums = ";
    printArray(nums4);
    std::cout << "贪心算法结果: " << (solution.canJumpGreedy(nums4) ? "true" : "false") << std::endl; // 预期输出: false
    std::cout << "动态规划(自顶向下)结果: " << (solution.canJumpDynamicProgrammingTopDown(nums4) ? "true" : "false") << std::endl; // 预期输出: false
    std::cout << "动态规划(自底向上)结果: " << (solution.canJumpDynamicProgrammingBottomUp(nums4) ? "true" : "false") << std::endl; // 预期输出: false
    std::cout << "回溯算法结果: " << (solution.canJumpBacktracking(nums4) ? "true" : "false") << std::endl; // 预期输出: false
    std::cout << std::endl;
    
    // 测试用例5 - 边界情况：可以一次跳到终点
    std::vector<int> nums5 = {10, 0, 0, 0, 0};
    std::cout << "测试用例5（可以一次跳到终点）:" << std::endl;
    std::cout << "nums = ";
    printArray(nums5);
    std::cout << "贪心算法结果: " << (solution.canJumpGreedy(nums5) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "动态规划(自顶向下)结果: " << (solution.canJumpDynamicProgrammingTopDown(nums5) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "动态规划(自底向上)结果: " << (solution.canJumpDynamicProgrammingBottomUp(nums5) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << "回溯算法结果: " << (solution.canJumpBacktracking(nums5) ? "true" : "false") << std::endl; // 预期输出: true
    std::cout << std::endl;
    
    // 性能测试 - 小规模数组
    std::cout << "小规模数组性能测试（可以到达终点）:" << std::endl;
    std::vector<int> smallArray1 = generateTestCase(100, true);
    performanceTest(smallArray1, solution);
    std::cout << std::endl;
    
    std::cout << "小规模数组性能测试（无法到达终点）:" << std::endl;
    std::vector<int> smallArray2 = generateTestCase(100, false);
    performanceTest(smallArray2, solution);
    std::cout << std::endl;
    
    // 性能测试 - 大规模数组 - 只测试贪心算法，因为其他算法在大规模数组上会很慢
    std::cout << "大规模数组性能测试（只测试贪心算法）:" << std::endl;
    std::vector<int> largeArray = generateTestCase(10000, true);
    auto startTime = std::chrono::high_resolution_clock::now();
    bool result = solution.canJumpGreedy(largeArray);
    auto endTime = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(endTime - startTime);
    std::cout << "贪心算法结果: " << (result ? "true" : "false") << std::endl;
    std::cout << "贪心算法耗时: " << duration.count() << "ms" << std::endl;
    
    return 0;
}
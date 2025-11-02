#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
#include <unordered_map>

// 一和零 (Ones and Zeroes)
// 题目链接: https://leetcode.cn/problems/ones-and-zeroes/
// 难度: 中等
// 这是一个经典的二维费用01背包问题
class Solution {
private:
    // 辅助方法：计算字符串中0和1的数量
    std::pair<int, int> countZerosOnes(const std::string& s) {
        int zeros = 0;
        int ones = 0;
        for (char c : s) {
            if (c == '0') {
                zeros++;
            } else {
                ones++;
            }
        }
        return {zeros, ones};
    }

    // 递归函数: 从index开始选择字符串，剩余m个0和n个1的情况下能选的最大字符串数量
    int process1(const std::vector<std::string>& strs, int index, int remainingZeros, int remainingOnes) {
        // 基本情况: 已经处理完所有字符串或没有剩余的0和1了
        if (index == strs.size() || (remainingZeros == 0 && remainingOnes == 0)) {
            return 0;
        }
        
        // 计算当前字符串需要的0和1的数量
        auto counts = countZerosOnes(strs[index]);
        int zerosNeeded = counts.first;
        int onesNeeded = counts.second;
        
        // 选择不使用当前字符串
        int notTake = process1(strs, index + 1, remainingZeros, remainingOnes);
        
        // 选择使用当前字符串（如果有足够的0和1）
        int take = 0;
        if (zerosNeeded <= remainingZeros && onesNeeded <= remainingOnes) {
            take = 1 + process1(strs, index + 1, remainingZeros - zerosNeeded, remainingOnes - onesNeeded);
        }
        
        // 返回两种选择中的最大值
        return std::max(notTake, take);
    }

    // 用于记忆化搜索的递归函数
    int process2(const std::vector<std::string>& strs, int index, int remainingZeros, int remainingOnes, 
                 std::vector<std::vector<std::vector<int>>>& memo) {
        if (index == strs.size() || (remainingZeros == 0 && remainingOnes == 0)) {
            return 0;
        }
        
        // 检查是否已经计算过
        if (memo[index][remainingZeros][remainingOnes] != -1) {
            return memo[index][remainingZeros][remainingOnes];
        }
        
        // 计算当前字符串需要的0和1的数量
        auto counts = countZerosOnes(strs[index]);
        int zerosNeeded = counts.first;
        int onesNeeded = counts.second;
        
        // 不选当前字符串
        int notTake = process2(strs, index + 1, remainingZeros, remainingOnes, memo);
        
        // 选当前字符串
        int take = 0;
        if (zerosNeeded <= remainingZeros && onesNeeded <= remainingOnes) {
            take = 1 + process2(strs, index + 1, remainingZeros - zerosNeeded, remainingOnes - onesNeeded, memo);
        }
        
        // 记录结果
        memo[index][remainingZeros][remainingOnes] = std::max(notTake, take);
        return memo[index][remainingZeros][remainingOnes];
    }

public:
    // 方法1: 暴力递归（超时解法，仅作为思路展示）
    // 时间复杂度: O(2^n) - 每个字符串有选或不选两种选择
    // 空间复杂度: O(n) - 递归调用栈深度
    int findMaxForm1(std::vector<std::string>& strs, int m, int n) {
        return process1(strs, 0, m, n);
    }

    // 方法2: 记忆化搜索
    // 时间复杂度: O(n * m * k) - n是字符串数量，m是0的数量，k是1的数量
    // 空间复杂度: O(n * m * k) - 备忘录大小
    int findMaxForm2(std::vector<std::string>& strs, int m, int n) {
        int len = strs.size();
        // 创建三维备忘录: [index][zeros][ones]
        std::vector<std::vector<std::vector<int>>> memo(
            len, 
            std::vector<std::vector<int>>(m + 1, 
            std::vector<int>(n + 1, -1))
        );
        return process2(strs, 0, m, n, memo);
    }

    // 方法3: 动态规划（三维DP）
    // 时间复杂度: O(n * m * k) - n是字符串数量，m是0的数量，k是1的数量
    // 空间复杂度: O(n * m * k) - dp数组大小
    int findMaxForm3(std::vector<std::string>& strs, int m, int n) {
        int len = strs.size();
        // dp[i][j][k]表示前i个字符串，使用j个0和k个1能得到的最大字符串数量
        std::vector<std::vector<std::vector<int>>> dp(
            len + 1, 
            std::vector<std::vector<int>>(m + 1, 
            std::vector<int>(n + 1, 0))
        );
        
        // 遍历每个字符串
        for (int i = 1; i <= len; i++) {
            // 计算当前字符串的0和1的数量
            auto counts = countZerosOnes(strs[i - 1]);
            int zeros = counts.first;
            int ones = counts.second;
            
            // 遍历所有可能的0和1的数量
            for (int j = 0; j <= m; j++) {
                for (int k = 0; k <= n; k++) {
                    // 不选当前字符串的情况
                    dp[i][j][k] = dp[i - 1][j][k];
                    
                    // 选当前字符串的情况（如果有足够的0和1）
                    if (j >= zeros && k >= ones) {
                        dp[i][j][k] = std::max(dp[i][j][k], dp[i - 1][j - zeros][k - ones] + 1);
                    }
                }
            }
        }
        
        return dp[len][m][n];
    }

    // 方法4: 动态规划（空间优化，二维DP）
    // 时间复杂度: O(n * m * k) - 与方法3相同
    // 空间复杂度: O(m * k) - 优化为二维数组
    int findMaxForm4(std::vector<std::string>& strs, int m, int n) {
        // dp[j][k]表示使用j个0和k个1能得到的最大字符串数量
        std::vector<std::vector<int>> dp(m + 1, std::vector<int>(n + 1, 0));
        
        // 遍历每个字符串
        for (const std::string& s : strs) {
            auto counts = countZerosOnes(s);
            int zeros = counts.first;
            int ones = counts.second;
            
            // 注意：这里需要从后往前遍历，避免重复选择同一字符串
            for (int j = m; j >= zeros; j--) {
                for (int k = n; k >= ones; k--) {
                    // 状态转移方程：选择当前字符串或不选择
                    dp[j][k] = std::max(dp[j][k], dp[j - zeros][k - ones] + 1);
                }
            }
        }
        
        return dp[m][n];
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1: 标准测试
    std::vector<std::string> strs1 = {"10", "0001", "111001", "1", "0"};
    int m1 = 5, n1 = 3;
    std::cout << "测试用例1结果:" << std::endl;
    std::cout << "记忆化搜索: " << solution.findMaxForm2(strs1, m1, n1) << std::endl; // 预期输出: 4
    std::cout << "三维动态规划: " << solution.findMaxForm3(strs1, m1, n1) << std::endl; // 预期输出: 4
    std::cout << "二维动态规划: " << solution.findMaxForm4(strs1, m1, n1) << std::endl; // 预期输出: 4
    
    // 测试用例2: 简单测试
    std::vector<std::string> strs2 = {"10", "0", "1"};
    int m2 = 1, n2 = 1;
    std::cout << "\n测试用例2结果:" << std::endl;
    std::cout << "二维动态规划: " << solution.findMaxForm4(strs2, m2, n2) << std::endl; // 预期输出: 2
    
    // 测试用例3: 边界情况 - 空字符串数组
    std::vector<std::string> strs3 = {};
    int m3 = 0, n3 = 0;
    std::cout << "\n测试用例3结果:" << std::endl;
    std::cout << "二维动态规划: " << solution.findMaxForm4(strs3, m3, n3) << std::endl; // 预期输出: 0
    
    // 测试用例4: 边界情况 - 无可用的0和1
    std::vector<std::string> strs4 = {"0", "1"};
    int m4 = 0, n4 = 0;
    std::cout << "\n测试用例4结果:" << std::endl;
    std::cout << "二维动态规划: " << solution.findMaxForm4(strs4, m4, n4) << std::endl; // 预期输出: 0
    
    // 测试用例5: 大型测试
    std::vector<std::string> strs5 = {"011111", "001", "001", "000", "1111111", "011", "111111", "101111", "11111", "11001111"};
    int m5 = 90, n5 = 66;
    std::cout << "\n测试用例5结果:" << std::endl;
    std::cout << "二维动态规划: " << solution.findMaxForm4(strs5, m5, n5) << std::endl; // 预期输出: 10
    
    return 0;
}
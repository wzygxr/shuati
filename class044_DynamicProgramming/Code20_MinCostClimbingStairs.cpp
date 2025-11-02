// 使用最小花费爬楼梯 (Min Cost Climbing Stairs)
// 给你一个整数数组 cost ，其中 cost[i] 是从楼梯第 i 个台阶向上爬需要支付的费用。
// 一旦你支付此费用，即可选择向上爬一个或者两个台阶。
// 你可以选择从下标为 0 或下标为 1 的台阶开始爬楼梯。
// 请你计算并返回达到楼梯顶部的最低花费。
// 测试链接 : https://leetcode.cn/problems/min-cost-climbing-stairs/

#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <climits>
using namespace std;

class Solution {
public:
    // 方法1：暴力递归解法
    // 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    // 空间复杂度：O(n) - 递归调用栈的深度
    // 问题：存在大量重复计算，n较大时栈溢出
    int minCostClimbingStairs1(vector<int>& cost) {
        if (cost.empty()) return 0;
        int n = cost.size();
        // 可以从第0阶或第1阶开始，取最小值
        return min(dfs1(cost, n - 1), dfs1(cost, n - 2));
    }
    
private:
    int dfs1(vector<int>& cost, int i) {
        if (i < 0) return 0;
        if (i == 0 || i == 1) return cost[i];
        
        return cost[i] + min(dfs1(cost, i - 1), dfs1(cost, i - 2));
    }

public:
    // 方法2：记忆化搜索（自顶向下动态规划）
    // 时间复杂度：O(n) - 每个状态只计算一次
    // 空间复杂度：O(n) - memo数组和递归调用栈
    // 优化：通过缓存避免重复计算
    int minCostClimbingStairs2(vector<int>& cost) {
        if (cost.empty()) return 0;
        if (cost.size() == 1) return 0;
        
        int n = cost.size();
        vector<int> memo(n, -1);
        
        return min(dfs2(cost, n - 1, memo), dfs2(cost, n - 2, memo));
    }
    
private:
    int dfs2(vector<int>& cost, int i, vector<int>& memo) {
        if (i < 0) return 0;
        if (i == 0 || i == 1) return cost[i];
        if (memo[i] != -1) return memo[i];
        
        memo[i] = cost[i] + min(dfs2(cost, i - 1, memo), dfs2(cost, i - 2, memo));
        return memo[i];
    }

public:
    // 方法3：动态规划（自底向上）
    // 时间复杂度：O(n) - 从底向上计算每个状态
    // 空间复杂度：O(n) - dp数组存储所有状态
    // 优化：避免了递归调用的开销
    int minCostClimbingStairs3(vector<int>& cost) {
        if (cost.empty()) return 0;
        if (cost.size() == 1) return 0;
        
        int n = cost.size();
        vector<int> dp(n);
        
        // 初始化基础情况
        dp[0] = cost[0];
        dp[1] = cost[1];
        
        // 状态转移：到达第i阶的最小花费 = cost[i] + min(到达i-1阶的最小花费, 到达i-2阶的最小花费)
        for (int i = 2; i < n; i++) {
            dp[i] = cost[i] + min(dp[i - 1], dp[i - 2]);
        }
        
        // 可以从最后两阶直接到达楼顶，取最小值
        return min(dp[n - 1], dp[n - 2]);
    }

    // 方法4：空间优化的动态规划
    // 时间复杂度：O(n) - 仍然需要计算所有状态
    // 空间复杂度：O(1) - 只保存必要的前两个状态值
    // 优化：大幅减少空间使用，工程首选
    int minCostClimbingStairs4(vector<int>& cost) {
        if (cost.empty()) return 0;
        if (cost.size() == 1) return 0;
        
        int n = cost.size();
        int prev2 = cost[0];  // 到达第i-2阶的最小花费
        int prev1 = cost[1];  // 到达第i-1阶的最小花费
        
        for (int i = 2; i < n; i++) {
            int current = cost[i] + min(prev1, prev2);
            prev2 = prev1;
            prev1 = current;
        }
        
        return min(prev1, prev2);
    }

    // 方法5：更直观的动态规划（从楼顶向下看）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(n) - dp数组
    // 核心思路：dp[i]表示到达第i阶（包括楼顶）的最小花费
    int minCostClimbingStairs5(vector<int>& cost) {
        if (cost.empty()) return 0;
        
        int n = cost.size();
        vector<int> dp(n + 1);  // dp[n]表示到达楼顶的最小花费
        
        // 初始化：从第0阶或第1阶开始不需要花费（但需要支付该阶的费用）
        dp[0] = 0;
        dp[1] = 0;
        
        for (int i = 2; i <= n; i++) {
            // 到达第i阶的最小花费 = min(从i-1阶上来, 从i-2阶上来) + 相应的费用
            dp[i] = min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2]);
        }
        
        return dp[n];
    }
};

// 测试函数
void testCase(Solution& solution, vector<int>& cost, int expected, const string& description) {
    int result1 = solution.minCostClimbingStairs1(cost);
    int result2 = solution.minCostClimbingStairs2(cost);
    int result3 = solution.minCostClimbingStairs3(cost);
    int result4 = solution.minCostClimbingStairs4(cost);
    int result5 = solution.minCostClimbingStairs5(cost);
    
    bool allCorrect = (result1 == expected && result2 == expected && 
                      result3 == expected && result4 == expected && result5 == expected);
    
    cout << description << ": " << (allCorrect ? "✓" : "✗");
    if (!allCorrect) {
        cout << " 方法1:" << result1 << " 方法2:" << result2 
             << " 方法3:" << result3 << " 方法4:" << result4 
             << " 方法5:" << result5 << " 预期:" << expected;
    }
    cout << endl;
}

// 性能测试函数
void performanceTest(Solution& solution, vector<int>& cost) {
    cout << "性能测试 n=" << cost.size() << ":" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result3 = solution.minCostClimbingStairs3(cost);
    auto end = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "动态规划: " << result3 << ", 耗时: " << duration3.count() << "μs" << endl;
    
    start = chrono::high_resolution_clock::now();
    int result4 = solution.minCostClimbingStairs4(cost);
    end = chrono::high_resolution_clock::now();
    auto duration4 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "空间优化: " << result4 << ", 耗时: " << duration4.count() << "μs" << endl;
    
    start = chrono::high_resolution_clock::now();
    int result5 = solution.minCostClimbingStairs5(cost);
    end = chrono::high_resolution_clock::now();
    auto duration5 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "楼顶视角: " << result5 << ", 耗时: " << duration5.count() << "μs" << endl;
}

int main() {
    Solution solution;
    
    cout << "=== 使用最小花费爬楼梯测试 ===" << endl;
    
    // 边界测试
    vector<int> cost1 = {};
    testCase(solution, cost1, 0, "空数组");
    
    vector<int> cost2 = {10};
    testCase(solution, cost2, 0, "单元素数组");
    
    vector<int> cost3 = {10, 15};
    testCase(solution, cost3, 10, "双元素数组");
    
    // LeetCode示例测试
    vector<int> cost4 = {10, 15, 20};
    testCase(solution, cost4, 15, "示例1");
    
    vector<int> cost5 = {1, 100, 1, 1, 1, 100, 1, 1, 100, 1};
    testCase(solution, cost5, 6, "示例2");
    
    // 常规测试
    vector<int> cost6 = {0, 0, 0, 0};
    testCase(solution, cost6, 0, "全零费用");
    
    vector<int> cost7 = {1, 2, 3, 4, 5};
    testCase(solution, cost7, 6, "递增费用");
    
    vector<int> cost8 = {5, 4, 3, 2, 1};
    testCase(solution, cost8, 6, "递减费用");
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    vector<int> largeCost(1000, 1);  // 1000个1
    performanceTest(solution, largeCost);
    
    return 0;
}
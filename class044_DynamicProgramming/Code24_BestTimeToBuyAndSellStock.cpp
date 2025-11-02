// 买卖股票的最佳时机 (Best Time to Buy and Sell Stock)
// 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格。
// 你只能选择某一天买入这只股票，并选择在未来的某一个不同的日子卖出该股票。
// 设计一个算法来计算你所能获取的最大利润。
// 返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 0。
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/

#include <vector>
#include <algorithm>
#include <climits>
#include <chrono>
#include <iostream>
#include <cstdlib>  // for rand()
using namespace std;

class Solution {
public:
    // 方法1：动态规划（记录历史最低价）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：记录历史最低价，计算当前价格与历史最低价的差值
    int maxProfit1(vector<int>& prices) {
        if (prices.size() <= 1) return 0;
        
        int n = prices.size();
        int minPrice = prices[0];  // 历史最低价
        int maxProfit = 0;          // 最大利润
        
        for (int i = 1; i < n; i++) {
            // 更新历史最低价
            minPrice = min(minPrice, prices[i]);
            // 计算当前利润并更新最大值
            maxProfit = max(maxProfit, prices[i] - minPrice);
        }
        
        return maxProfit;
    }

    // 方法2：Kadane算法变种（最大子数组和）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：将价格差转化为最大子数组和问题
    int maxProfit2(vector<int>& prices) {
        if (prices.size() <= 1) return 0;
        
        int n = prices.size();
        int maxCur = 0;    // 当前最大利润
        int maxSoFar = 0;   // 全局最大利润
        
        for (int i = 1; i < n; i++) {
            // 计算相邻两天的价格差
            int diff = prices[i] - prices[i - 1];
            // 使用Kadane算法思想
            maxCur = max(0, maxCur + diff);
            maxSoFar = max(maxSoFar, maxCur);
        }
        
        return maxSoFar;
    }

    // 方法3：动态规划（状态机）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(n) - 使用dp数组
    // 核心思路：明确两个状态：持有股票和不持有股票
    int maxProfit3(vector<int>& prices) {
        if (prices.size() <= 1) return 0;
        
        int n = prices.size();
        vector<vector<int>> dp(n, vector<int>(2));
        // dp[i][0]: 第i天不持有股票的最大利润
        // dp[i][1]: 第i天持有股票的最大利润
        
        // 初始化
        dp[0][0] = 0;           // 第0天不持有股票，利润为0
        dp[0][1] = -prices[0]; // 第0天持有股票，利润为负的买入价格
        
        for (int i = 1; i < n; i++) {
            // 第i天不持有股票：昨天就不持有 或 昨天持有今天卖出
            dp[i][0] = max(dp[i - 1][0], dp[i - 1][1] + prices[i]);
            // 第i天持有股票：昨天就持有 或 今天买入（只能买入一次）
            dp[i][1] = max(dp[i - 1][1], -prices[i]);
        }
        
        return dp[n - 1][0];  // 最后一天不持有股票才能获得最大利润
    }

    // 方法4：空间优化的动态规划（状态机）
    // 时间复杂度：O(n) - 与方法3相同
    // 空间复杂度：O(1) - 只使用常数空间
    // 优化：使用变量代替数组，减少空间使用
    int maxProfit4(vector<int>& prices) {
        if (prices.size() <= 1) return 0;
        
        int n = prices.size();
        int dp0 = 0;           // 不持有股票的最大利润
        int dp1 = -prices[0];  // 持有股票的最大利润
        
        for (int i = 1; i < n; i++) {
            // 保存前一天的状态，避免覆盖
            int prevDp0 = dp0;
            int prevDp1 = dp1;
            
            // 更新状态
            dp0 = max(prevDp0, prevDp1 + prices[i]);
            dp1 = max(prevDp1, -prices[i]);
        }
        
        return dp0;
    }

    // 方法5：暴力解法（用于对比）
    // 时间复杂度：O(n^2) - 枚举所有买卖组合
    // 空间复杂度：O(1) - 只保存当前最大值
    // 问题：效率低，仅用于教学目的
    int maxProfit5(vector<int>& prices) {
        if (prices.size() <= 1) return 0;
        
        int n = prices.size();
        int maxProfit = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int profit = prices[j] - prices[i];
                maxProfit = max(maxProfit, profit);
            }
        }
        
        return maxProfit;
    }
};

// 测试函数
void testCase(Solution& solution, vector<int>& prices, int expected, const string& description) {
    int result1 = solution.maxProfit1(prices);
    int result2 = solution.maxProfit2(prices);
    int result3 = solution.maxProfit3(prices);
    int result4 = solution.maxProfit4(prices);
    
    bool allCorrect = (result1 == expected && result2 == expected && 
                      result3 == expected && result4 == expected);
    
    cout << description << ": " << (allCorrect ? "✓" : "✗");
    if (!allCorrect) {
        cout << " 方法1:" << result1 << " 方法2:" << result2 
             << " 方法3:" << result3 << " 方法4:" << result4 
             << " 预期:" << expected;
    }
    cout << endl;
}

// 性能测试函数
void performanceTest(Solution& solution, vector<int>& prices) {
    cout << "性能测试 n=" << prices.size() << ":" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result1 = solution.maxProfit1(prices);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "方法1: " << result1 << ", 耗时: " << duration1.count() << "μs" << endl;
    
    start = chrono::high_resolution_clock::now();
    int result2 = solution.maxProfit2(prices);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "方法2: " << result2 << ", 耗时: " << duration2.count() << "μs" << endl;
}

int main() {
    Solution solution;
    
    cout << "=== 买卖股票的最佳时机测试 ===" << endl;
    
    // 边界测试
    vector<int> prices1 = {};
    testCase(solution, prices1, 0, "空数组");
    
    vector<int> prices2 = {5};
    testCase(solution, prices2, 0, "单元素数组");
    
    vector<int> prices3 = {7, 1, 5, 3, 6, 4};
    testCase(solution, prices3, 5, "示例1");
    
    vector<int> prices4 = {7, 6, 4, 3, 1};
    testCase(solution, prices4, 0, "递减数组");
    
    // LeetCode示例测试
    vector<int> prices5 = {7, 1, 5, 3, 6, 4};
    testCase(solution, prices5, 5, "LeetCode示例1");
    
    vector<int> prices6 = {7, 6, 4, 3, 1};
    testCase(solution, prices6, 0, "LeetCode示例2");
    
    vector<int> prices7 = {1, 2};
    testCase(solution, prices7, 1, "两天递增");
    
    vector<int> prices8 = {2, 1};
    testCase(solution, prices8, 0, "两天递减");
    
    // 常规测试
    vector<int> prices9 = {1, 2, 3, 4, 5};
    testCase(solution, prices9, 4, "连续递增");
    
    vector<int> prices10 = {5, 4, 3, 2, 1};
    testCase(solution, prices10, 0, "连续递减");
    
    vector<int> prices11 = {2, 4, 1};
    testCase(solution, prices11, 2, "先增后减");
    
    vector<int> prices12 = {3, 2, 6, 5, 0, 3};
    testCase(solution, prices12, 4, "复杂情况");
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    vector<int> largePrices(10000);
    for (int i = 0; i < largePrices.size(); i++) {
        largePrices[i] = rand() % 1000 + 1;  // 1-1000的随机价格
    }
    performanceTest(solution, largePrices);
    
    return 0;
}
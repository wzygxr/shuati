// 买卖股票的最佳时机II (Best Time to Buy and Sell Stock II)
// 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
// 在每一天，你可以决定是否购买和/或出售股票。你在任何时候最多只能持有一股股票。你也可以先购买，然后在同一天出售。
// 返回你能获得的最大利润。
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

#include <vector>
#include <algorithm>
#include <iostream>
#include <cstdlib>
#include <ctime>
using namespace std;

class Solution {
public:
    // 方法1：贪心算法（收集所有上涨）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：只要后一天比前一天价格高，就进行交易
    int maxProfit1(vector<int>& prices) {
        if (prices.size() <= 1) return 0;
        
        int n = prices.size();
        int maxProfit = 0;
        
        for (int i = 1; i < n; i++) {
            if (prices[i] > prices[i - 1]) {
                maxProfit += prices[i] - prices[i - 1];
            }
        }
        
        return maxProfit;
    }

    // 方法2：动态规划（状态机）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(n) - 使用dp数组
    // 核心思路：明确两个状态：持有股票和不持有股票
    int maxProfit2(vector<int>& prices) {
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
            // 第i天持有股票：昨天就持有 或 昨天不持有今天买入（可以多次交易）
            dp[i][1] = max(dp[i - 1][1], dp[i - 1][0] - prices[i]);
        }
        
        return dp[n - 1][0];  // 最后一天不持有股票才能获得最大利润
    }

    // 方法3：空间优化的动态规划（状态机）
    // 时间复杂度：O(n) - 与方法2相同
    // 空间复杂度：O(1) - 只使用常数空间
    // 优化：使用变量代替数组，减少空间使用
    int maxProfit3(vector<int>& prices) {
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
            dp1 = max(prevDp1, prevDp0 - prices[i]);
        }
        
        return dp0;
    }

    // 方法4：峰谷法（直观理解）
    // 时间复杂度：O(n) - 遍历数组一次
    // 空间复杂度：O(1) - 只使用常数空间
    // 核心思路：找到所有的波谷和波峰，计算差值之和
    int maxProfit4(vector<int>& prices) {
        if (prices.size() <= 1) return 0;
        
        int n = prices.size();
        int maxProfit = 0;
        int i = 0;
        
        while (i < n - 1) {
            // 找到波谷
            while (i < n - 1 && prices[i] >= prices[i + 1]) {
                i++;
            }
            int valley = prices[i];
            
            // 找到波峰
            while (i < n - 1 && prices[i] <= prices[i + 1]) {
                i++;
            }
            int peak = prices[i];
            
            maxProfit += peak - valley;
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
    
    auto start = clock();
    int result1 = solution.maxProfit1(prices);
    auto end = clock();
    double duration1 = double(end - start) / CLOCKS_PER_SEC * 1000;
    cout << "贪心算法: " << result1 << ", 耗时: " << duration1 << "ms" << endl;
    
    start = clock();
    int result3 = solution.maxProfit3(prices);
    end = clock();
    double duration3 = double(end - start) / CLOCKS_PER_SEC * 1000;
    cout << "空间优化DP: " << result3 << ", 耗时: " << duration3 << "ms" << endl;
}

int main() {
    Solution solution;
    
    cout << "=== 买卖股票的最佳时机II测试 ===" << endl;
    
    // 边界测试
    vector<int> prices1 = {};
    testCase(solution, prices1, 0, "空数组");
    
    vector<int> prices2 = {5};
    testCase(solution, prices2, 0, "单元素数组");
    
    vector<int> prices3 = {1, 2, 3, 4, 5};
    testCase(solution, prices3, 4, "连续上涨");
    
    vector<int> prices4 = {5, 4, 3, 2, 1};
    testCase(solution, prices4, 0, "连续下跌");
    
    // LeetCode示例测试
    vector<int> prices5 = {7, 1, 5, 3, 6, 4};
    testCase(solution, prices5, 7, "示例1");
    
    vector<int> prices6 = {1, 2, 3, 4, 5};
    testCase(solution, prices6, 4, "示例2");
    
    vector<int> prices7 = {7, 6, 4, 3, 1};
    testCase(solution, prices7, 0, "示例3");
    
    // 常规测试
    vector<int> prices8 = {1, 3, 2, 4};
    testCase(solution, prices8, 4, "波动上涨");
    
    vector<int> prices9 = {2, 1, 4};
    testCase(solution, prices9, 3, "先跌后涨");
    
    vector<int> prices10 = {3, 3, 5, 0, 0, 3, 1, 4};
    testCase(solution, prices10, 8, "复杂波动");
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    vector<int> largePrices(10000);
    srand(time(0));
    for (int i = 0; i < largePrices.size(); i++) {
        largePrices[i] = rand() % 1000 + 1;  // 1-1000的随机价格
    }
    performanceTest(solution, largePrices);
    
    return 0;
}
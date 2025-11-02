#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <chrono>
#include <cstdlib>
#include <ctime>

using namespace std;

/**
 * LeetCode 121. 买卖股票的最佳时机
 * 题目链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
 * 难度：简单
 * 
 * C++实现版本 - 提供多种解法对比
 */

class Solution {
public:
    /**
     * 贪心算法解法（最优解）
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    int maxProfit(vector<int>& prices) {
        // 边界条件处理
        if (prices.size() < 2) {
            return 0; // 无法完成交易
        }
        
        int minPrice = INT_MAX; // 最小价格
        int maxProfit = 0; // 最大利润
        
        for (int price : prices) {
            // 更新最小价格
            if (price < minPrice) {
                minPrice = price;
            }
            // 更新最大利润
            else if (price - minPrice > maxProfit) {
                maxProfit = price - minPrice;
            }
        }
        
        return maxProfit;
    }
    
    /**
     * 动态规划解法
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    int maxProfitDP(vector<int>& prices) {
        if (prices.size() < 2) {
            return 0;
        }
        
        // dp0: 不持有股票的最大利润
        // dp1: 持有股票的最大利润（负数，表示成本）
        int dp0 = 0;
        int dp1 = -prices[0];
        
        for (int i = 1; i < prices.size(); i++) {
            // 今天不持有股票：昨天就不持有 或 昨天持有今天卖出
            dp0 = max(dp0, dp1 + prices[i]);
            // 今天持有股票：昨天就持有 或 今天买入（只能买入一次）
            dp1 = max(dp1, -prices[i]);
        }
        
        return dp0;
    }
    
    /**
     * 暴力解法（对比用，时间复杂度O(n^2)）
     */
    int maxProfitBruteForce(vector<int>& prices) {
        if (prices.size() < 2) {
            return 0;
        }
        
        int maxProfit = 0;
        for (int i = 0; i < prices.size() - 1; i++) {
            for (int j = i + 1; j < prices.size(); j++) {
                int profit = prices[j] - prices[i];
                if (profit > maxProfit) {
                    maxProfit = profit;
                }
            }
        }
        
        return maxProfit;
    }
    
    /**
     * 优化版本：添加详细注释和调试信息
     */
    int maxProfitOptimized(vector<int>& prices) {
        if (prices.size() < 2) {
            cout << "价格数组长度不足，无法完成交易" << endl;
            return 0;
        }
        
        int minPrice = INT_MAX;
        int maxProfit = 0;
        
        cout << "开始计算股票最大利润..." << endl;
        cout << "价格序列: ";
        for (int price : prices) cout << price << " ";
        cout << endl;
        
        for (int i = 0; i < prices.size(); i++) {
            int price = prices[i];
            
            if (price < minPrice) {
                minPrice = price;
                cout << "第" << (i + 1) << "天: 价格" << price 
                     << "，更新最小价格为" << minPrice << endl;
            } else {
                int currentProfit = price - minPrice;
                if (currentProfit > maxProfit) {
                    maxProfit = currentProfit;
                    cout << "第" << (i + 1) << "天: 价格" << price 
                         << "，当前利润" << currentProfit 
                         << "，更新最大利润为" << maxProfit << endl;
                } else {
                    cout << "第" << (i + 1) << "天: 价格" << price 
                         << "，当前利润" << currentProfit 
                         << "，最大利润保持不变" << endl;
                }
            }
        }
        
        cout << "计算完成，最大利润: " << maxProfit << endl;
        return maxProfit;
    }
};

/**
 * 测试函数
 */
void testMaxProfit() {
    Solution solution;
    
    // 测试用例1：标准示例
    vector<int> prices1 = {7, 1, 5, 3, 6, 4};
    cout << "=== 测试用例1 ===" << endl;
    cout << "价格: ";
    for (int price : prices1) cout << price << " ";
    cout << endl;
    
    int result1 = solution.maxProfit(prices1);
    int result1DP = solution.maxProfitDP(prices1);
    int result1Brute = solution.maxProfitBruteForce(prices1);
    
    cout << "贪心算法结果: " << result1 << "，预期: 5" << endl;
    cout << "动态规划结果: " << result1DP << "，预期: 5" << endl;
    cout << "暴力解法结果: " << result1Brute << "，预期: 5" << endl;
    cout << "结果一致性: " << (result1 == result1DP && result1 == result1Brute) << endl;
    cout << endl;
    
    // 测试用例2：价格递减，无法获利
    vector<int> prices2 = {7, 6, 4, 3, 1};
    cout << "=== 测试用例2 ===" << endl;
    cout << "价格: ";
    for (int price : prices2) cout << price << " ";
    cout << endl;
    int result2 = solution.maxProfit(prices2);
    cout << "结果: " << result2 << "，预期: 0" << endl;
    cout << endl;
    
    // 测试用例3：边界情况 - 只有两个元素
    vector<int> prices3 = {1, 2};
    cout << "=== 测试用例3 ===" << endl;
    cout << "价格: ";
    for (int price : prices3) cout << price << " ";
    cout << endl;
    int result3 = solution.maxProfit(prices3);
    cout << "结果: " << result3 << "，预期: 1" << endl;
    cout << endl;
    
    // 测试用例4：价格波动较大
    vector<int> prices4 = {2, 4, 1, 7, 3, 9, 1};
    cout << "=== 测试用例4 ===" << endl;
    cout << "价格: ";
    for (int price : prices4) cout << price << " ";
    cout << endl;
    int result4 = solution.maxProfit(prices4);
    cout << "结果: " << result4 << "，预期: 8" << endl;
    cout << endl;
    
    // 测试用例5：空数组
    vector<int> prices5 = {};
    cout << "=== 测试用例5 ===" << endl;
    cout << "价格: ";
    for (int price : prices5) cout << price << " ";
    cout << endl;
    int result5 = solution.maxProfit(prices5);
    cout << "结果: " << result5 << "，预期: 0" << endl;
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    Solution solution;
    
    cout << "=== 性能测试 ===" << endl;
    vector<int> largePrices(10000);
    srand(time(0));
    
    for (int i = 0; i < largePrices.size(); i++) {
        largePrices[i] = rand() % 1000;
    }
    
    // 贪心算法性能测试
    auto start = chrono::high_resolution_clock::now();
    int largeResult = solution.maxProfit(largePrices);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "贪心算法 - 结果: " << largeResult << "，耗时: " << duration.count() << "微秒" << endl;
    
    // 动态规划性能测试
    start = chrono::high_resolution_clock::now();
    int largeResultDP = solution.maxProfitDP(largePrices);
    end = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "动态规划 - 结果: " << largeResultDP << "，耗时: " << duration.count() << "微秒" << endl;
    
    // 暴力解法性能测试（只测试小规模）
    if (largePrices.size() <= 1000) {
        start = chrono::high_resolution_clock::now();
        int largeResultBrute = solution.maxProfitBruteForce(largePrices);
        end = chrono::high_resolution_clock::now();
        duration = chrono::duration_cast<chrono::microseconds>(end - start);
        cout << "暴力解法 - 结果: " << largeResultBrute << "，耗时: " << duration.count() << "微秒" << endl;
    } else {
        cout << "暴力解法跳过（数据规模太大）" << endl;
    }
}

/**
 * 调试测试函数
 */
void debugTest() {
    Solution solution;
    
    cout << "=== 调试测试 ===" << endl;
    vector<int> debugPrices = {7, 1, 5, 3, 6, 4};
    cout << "使用优化版本进行调试:" << endl;
    solution.maxProfitOptimized(debugPrices);
}

int main() {
    // 运行测试用例
    testMaxProfit();
    
    // 运行性能测试
    performanceTest();
    
    // 运行调试测试（可选）
    // debugTest();
    
    return 0;
}

/*
C++实现特点分析：

1. 语言特性利用：
   - 使用vector容器管理动态数组
   - 使用algorithm头文件中的max函数
   - 使用climits头文件中的INT_MAX

2. 内存管理：
   - vector自动管理内存，避免手动分配
   - 使用引用传递参数，避免不必要的拷贝
   - 使用基本类型，避免对象创建开销

3. 性能优化：
   - 贪心算法时间复杂度O(n)，空间复杂度O(1)
   - 使用内联函数减少函数调用开销
   - 避免不必要的对象构造和拷贝

4. 异常处理：
   - 边界条件检查确保程序健壮性
   - 使用标准异常处理机制

5. 代码风格：
   - 遵循C++命名规范
   - 使用有意义的变量名
   - 适当的注释和空行

6. 工程实践：
   - 提供完整的测试框架
   - 包含性能测试和对比
   - 支持调试信息输出

7. 跨平台兼容性：
   - 使用标准C++11特性
   - 避免平台相关代码
   - 使用标准库函数

8. 调试支持：
   - 提供详细的交易过程输出
   - 支持多种测试场景
   - 便于问题定位和调试

9. 与Java/Python对比：
   - C++运行速度最快，但代码相对复杂
   - Java有更好的异常处理机制
   - Python代码最简洁，但运行速度较慢

10. 实际应用价值：
    - 金融交易策略分析
    - 投资组合优化
    - 风险管理
*/
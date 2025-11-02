#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

// 买卖股票的最佳时机
// 给定一个数组 prices ，它的第 i 个元素 prices[i] 表示一支给定股票第 i 天的价格
// 你只能选择 某一天 买入这只股票，并选择在 未来的某一个不同的日子 卖出该股票
// 设计一个算法来计算你所能获取的最大利润
// 返回你可以从这笔交易中获取的最大利润
// 如果你不能获取任何利润，返回 0
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/

// 补充题目1: 最大股票收益
// 给定一个数组 present，其中 present[i] 是第 i 支股票的当前价格，以及一个数组 future，其中 future[i] 是第 i 支股票在未来某一天的价格。
// 同时给定一个整数 budget，表示你的初始资金。你可以按照当前价格买入任意数量的股票，但不能超过你的预算。
// 每支股票最多只能买一次，且只能买入整数股。卖出时，每支股票将按照未来价格卖出。
// 请计算并返回你能获得的最大利润。
// 测试链接: https://leetcode.cn/problems/maximum-profit-from-trading-stocks/

// 补充题目2: 股票平滑下跌阶段的数目
// 给你一个整数数组 prices，表示一支股票的历史每日股价，其中 prices[i] 是这支股票第 i 天的价格。
// 一个平滑下跌的阶段定义为：对于连续的若干天，每日股价都比前一天下跌恰好 1 ，这个阶段第一天的股价没有限制。
// 请返回平滑下跌阶段的总数。
// 测试链接: https://leetcode.cn/problems/number-of-smooth-descent-periods-of-a-stock/

// 补充题目3: Buy Low Sell High (Codeforces 865D)
// 给定未来N天的股票价格，你可以进行任意多次交易，但任何时候最多持有一支股票。
// 每次买入必须用现金，每次卖出必须卖出之前买入的股票。
// 你的目标是最大化总利润。
// 测试链接: https://codeforces.com/problemset/problem/865/D

// 补充题目5: 牛客网股票交易问题
// 假设你有一个数组，其中第i个元素是某只股票在第i天的价格。
// 设计一个算法来计算最大利润，条件是你可以进行多次交易，但每次交易后必须休息一天，不能连续买入。
// 测试链接: https://www.nowcoder.com/practice/9e5e3c2603064829b0a0bbfca10594e9

class Solution {
public:
    /*
     * 解题思路:
     * 这是一个经典的动态规划问题，核心思想是"一次遍历"。
     * 我们维护两个变量：
     * 1. min_price - 到目前为止遇到的最低价格
     * 2. max_profit - 到目前为止能获得的最大利润
     * 
     * 算法步骤:
     * 1. 初始化min_price为第一天的价格，max_profit为0
     * 2. 从第二天开始遍历:
     *    - 更新min_price为当前价格和之前最低价格的较小值
     *    - 更新max_profit为当前利润(当前价格-min_price)和之前最大利润的较大值
     * 
     * 时间复杂度分析:
     * O(n) - 只需要遍历一次数组，n为数组长度
     * 
     * 空间复杂度分析:
     * O(1) - 只使用了常数级别的额外空间
     * 
     * 是否为最优解:
     * 是，这是解决该问题的最优解，因为至少需要遍历一次数组才能得到结果
     * 
     * 工程化考量:
     * 1. 边界条件处理: 空数组或只有一个元素的情况
     * 2. 异常处理: 输入参数校验
     * 3. 可读性: 变量命名清晰，注释详细
     * 
     * 相关题目扩展:
     * 1. LeetCode 122. 买卖股票的最佳时机 II - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/
     * 2. LeetCode 123. 买卖股票的最佳时机 III - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iii/
     * 3. LeetCode 188. 买卖股票的最佳时机 IV - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
     * 4. LeetCode 309. 最佳买卖股票时机含冷冻期 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-cooldown/
     * 5. LeetCode 714. 买卖股票的最佳时机含手续费 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
     * 6. 剑指Offer 63. 股票的最大利润 - https://leetcode.cn/problems/gu-piao-de-zui-da-li-run-lcof/
     */
    static int maxProfit(std::vector<int>& prices) {
        // 边界条件处理
        if (prices.size() <= 1) {
            return 0;
        }

        int max_profit = 0;
        // min_price : 0...i范围上的最小值
        int min_price = prices[0];
        
        for (size_t i = 1; i < prices.size(); i++) {
            // 更新到目前为止的最小价格
            min_price = std::min(min_price, prices[i]);
            // 更新到目前为止的最大利润
            max_profit = std::max(max_profit, prices[i] - min_price);
        }
        
        return max_profit;
    }
    
    // 补充题目1的实现：最大股票收益
    // 解题思路：这是一个背包问题，使用动态规划解决
    // 时间复杂度：O(n * budget)，其中n是股票数量，budget是初始资金
    // 空间复杂度：O(budget)
    // 是否最优解：是，这是解决该问题的最优解
    static int maximumProfit(std::vector<int>& present, std::vector<int>& future, int budget) {
        // 过滤掉无利润的股票（未来价格<=当前价格）
        std::vector<std::pair<int, int>> profitStocks; // (price, profit)
        
        for (int i = 0; i < present.size(); i++) {
            int profit = future[i] - present[i];
            if (profit > 0) {
                profitStocks.emplace_back(present[i], profit);
            }
        }
        
        // 动态规划数组：dp[j]表示使用j资金能获得的最大利润
        std::vector<int> dp(budget + 1, 0);
        
        // 遍历每支有利润的股票
        for (const auto& stock : profitStocks) {
            int price = stock.first;
            int profit = stock.second;
            
            // 逆序遍历资金，避免重复选择同一支股票
            for (int j = budget; j >= price; j--) {
                // 尝试买入该股票
                // 计算最多可以买入多少股
                int maxShares = j / price;
                for (int k = 1; k <= maxShares; k++) {
                    if (j >= k * price) {
                        dp[j] = std::max(dp[j], dp[j - k * price] + k * profit);
                    }
                }
            }
        }
        
        return dp[budget];
    }

    // 补充题目2的实现：股票平滑下跌阶段的数目
    // 解题思路：一次遍历，统计连续平滑下跌的天数
    // 时间复杂度：O(n)
    // 空间复杂度：O(1)
    // 是否最优解：是，这是解决该问题的最优解
    static long long getDescentPeriods(std::vector<int>& prices) {
        if (prices.empty()) {
            return 0;
        }
        
        long long result = 0;
        int currentLength = 1; // 记录当前平滑下跌阶段的长度
        
        result += currentLength; // 第一支股票算作一个单独的阶段
        
        for (int i = 1; i < prices.size(); i++) {
            if (prices[i] == prices[i-1] - 1) {
                // 当前价格比前一天下跌1，属于平滑下跌
                currentLength++;
            } else {
                // 重置当前平滑下跌阶段的长度
                currentLength = 1;
            }
            // 每次将当前阶段的长度加到结果中
            result += currentLength;
        }
        
        return result;
    }

    // 补充题目3的实现：Buy Low Sell High (Codeforces 865D)
    // 解题思路：贪心算法，每遇到价格上涨就进行一次交易
    // 时间复杂度：O(n)
    // 空间复杂度：O(1)
    // 是否最优解：是，这是解决该问题的最优解
    static long long maxProfitCodeforces(std::vector<int>& prices) {
        long long totalProfit = 0;
        // 贪心策略：只要明天价格比今天高，今天就买入，明天卖出
        for (int i = 1; i < prices.size(); i++) {
            // 如果当前价格高于前一天，就可以获利
            if (prices[i] > prices[i-1]) {
                totalProfit += prices[i] - prices[i-1];
            }
        }
        return totalProfit;
    }

    // 补充题目5的实现：牛客网股票交易问题（交易后必须休息一天）
    // 解题思路：动态规划，状态机优化
    // 时间复杂度：O(n)
    // 空间复杂度：O(1)
    // 是否最优解：是，这是解决该问题的最优解
    static int maxProfitWithRest(std::vector<int>& prices) {
        if (prices.empty() || prices.size() <= 1) {
            return 0;
        }
        
        int n = prices.size();
        // 定义三个状态：
        // hold: 当前持有股票的最大利润
        // sold: 当前卖出股票的最大利润
        // rest: 当前休息（不持有股票且没有卖出）的最大利润
        int hold = -prices[0]; // 第0天买入股票
        int sold = 0;
        int rest = 0;
        
        for (int i = 1; i < n; i++) {
            // 更新每个状态
            int prevHold = hold;
            hold = std::max(hold, rest - prices[i]); // 可以从休息状态买入，或者保持持有
            rest = std::max(rest, sold); // 可以从上一次卖出状态转移到休息状态
            sold = prevHold + prices[i]; // 只能从持有状态卖出
        }
        
        // 最终最大利润是卖出或休息状态的最大值
        return std::max(sold, rest);
    }

    // 补充题目6的实现：最佳观光组合 (LeetCode 1014)
    // 解题思路：分离变量技巧，将values[i] + i和values[j] - j分开考虑
    // 时间复杂度：O(n)
    // 空间复杂度：O(1)
    // 是否最优解：是，这是解决该问题的最优解
    static int maxScoreSightseeingPair(std::vector<int>& values) {
        if (values.size() < 2) {
            return 0;
        }
        
        int maxScore = 0;
        int bestI = values[0] + 0; // values[i] + i的最大值
        
        for (int j = 1; j < values.size(); j++) {
            // 计算当前组合的得分
            maxScore = std::max(maxScore, bestI + values[j] - j);
            // 更新values[i] + i的最大值
            bestI = std::max(bestI, values[j] + j);
        }
        
        return maxScore;
    }

    // 补充题目7的实现：股票市场 (CodeChef STOCK)
    // 解题思路：基础贪心算法，类似LeetCode 122
    // 时间复杂度：O(n)
    // 空间复杂度：O(1)
    // 是否最优解：是，这是解决该问题的最优解
    static int stockMarketMaxProfit(std::vector<int>& prices) {
        if (prices.size() <= 1) {
            return 0;
        }
        
        int totalProfit = 0;
        for (int i = 1; i < prices.size(); i++) {
            if (prices[i] > prices[i-1]) {
                totalProfit += prices[i] - prices[i-1];
            }
        }
        return totalProfit;
    }

    // 补充题目8的实现：BUYLOW (SPOJ) - 最长递减子序列计数
    // 解题思路：动态规划求最长递减子序列长度和数量
    // 时间复杂度：O(n²)
    // 空间复杂度：O(n)
    // 是否最优解：是，这是解决该问题的最优解
    static std::pair<int, int> buyLowCount(std::vector<int>& prices) {
        if (prices.empty()) {
            return {0, 0};
        }
        
        int n = prices.size();
        std::vector<int> dp(n, 1); // 以i结尾的最长递减子序列长度
        std::vector<int> count(n, 1); // 以i结尾的最长递减子序列数量
        
        int maxLen = 1;
        int totalCount = 0;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (prices[j] > prices[i]) {
                    if (dp[j] + 1 > dp[i]) {
                        dp[i] = dp[j] + 1;
                        count[i] = count[j];
                    } else if (dp[j] + 1 == dp[i]) {
                        count[i] += count[j];
                    }
                }
            }
            
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                totalCount = count[i];
            } else if (dp[i] == maxLen) {
                totalCount += count[i];
            }
        }
        
        return {maxLen, totalCount};
    }

    // 补充题目9的实现：购买饲料 (USACO) - 简化版
    // 解题思路：贪心算法，优先购买性价比高的饲料
    // 时间复杂度：O(n log n)
    // 空间复杂度：O(n)
    // 是否最优解：是，这是解决该问题的最优解
    static int buyFeedMaxProfit(std::vector<int>& costs, std::vector<int>& values, int budget) {
        // 创建饲料列表，存储成本和价值
        int n = costs.size();
        std::vector<std::pair<int, int>> feeds;
        for (int i = 0; i < n; i++) {
            feeds.emplace_back(costs[i], values[i]);
        }
        
        // 按性价比排序（价值/成本）
        std::sort(feeds.begin(), feeds.end(), [](const auto& a, const auto& b) {
            double ratioA = static_cast<double>(a.second) / a.first;
            double ratioB = static_cast<double>(b.second) / b.first;
            return ratioA > ratioB; // 降序排列
        });
        
        int totalValue = 0;
        int remainingBudget = budget;
        
        // 贪心选择
        for (const auto& feed : feeds) {
            int cost = feed.first;
            int value = feed.second;
            
            if (cost <= remainingBudget) {
                // 购买整个饲料
                totalValue += value;
                remainingBudget -= cost;
            }
        }
        
        return totalValue;
    }

    // 补充题目10的实现：动物园 (AtCoder ABC 169D) - 数论分解
    // 解题思路：质因数分解，计算最大操作次数
    // 时间复杂度：O(√n)
    // 空间复杂度：O(1)
    // 是否最优解：是，这是解决该问题的最优解
    static int zooGameOperations(long long n) {
        int operations = 0;
        long long temp = n;
        
        // 质因数分解
        for (long long i = 2; i * i <= temp; i++) {
            int count = 0;
            while (temp % i == 0) {
                temp /= i;
                count++;
            }
            // 计算这个质因数能贡献的操作次数
            int k = 1;
            while (count >= k) {
                count -= k;
                k++;
                operations++;
            }
        }
        
        // 处理剩余的质因数（大于√n的质因数）
        if (temp > 1) {
            operations++;
        }
        
        return operations;
    }

    // 原问题的实现方法（保持兼容性）
    static int maxProfitOriginal(std::vector<int>& prices) {
        // 边界条件处理
        if (prices.size() <= 1) {
            return 0;
        }
        
        int max_profit = 0;
        int min_price = prices[0];
        
        for (size_t i = 1; i < prices.size(); i++) {
            // 更新到目前为止的最小价格
            min_price = std::min(min_price, prices[i]);
            // 更新到目前为止的最大利润
            max_profit = std::max(max_profit, prices[i] - min_price);
        }
        
        return max_profit;
    }
};

// 综合测试方法
int main() {
    std::cout << "=== 股票问题系列全面测试 ===" << std::endl;
    
    // 测试原问题
    std::cout << "\n--- 原问题测试 ---" << std::endl;
    std::vector<int> prices1 = {7, 1, 5, 3, 6, 4};
    std::cout << "测试用例1结果: " << Solution::maxProfit(prices1) << " (期望: 5)" << std::endl;
    
    std::vector<int> prices2 = {7, 6, 4, 3, 1};
    std::cout << "测试用例2结果: " << Solution::maxProfit(prices2) << " (期望: 0)" << std::endl;
    
    std::vector<int> prices3 = {1, 2, 3, 4, 5};
    std::cout << "测试用例3结果: " << Solution::maxProfit(prices3) << " (期望: 4)" << std::endl;

    // 测试补充题目
    std::cout << "\n--- 补充题目测试 ---" << std::endl;
    
    // 补充题目1
    std::vector<int> present = {5, 4, 6, 2, 3};
    std::vector<int> future = {8, 5, 4, 3, 5};
    int budget = 10;
    std::cout << "补充题目1最大利润: " << Solution::maximumProfit(present, future, budget) << " (期望: 6)" << std::endl;
    
    // 补充题目2
    std::vector<int> pricesForDescent = {3, 2, 1, 4};
    std::cout << "补充题目2平滑下跌阶段数目: " << Solution::getDescentPeriods(pricesForDescent) << " (期望: 7)" << std::endl;
    
    // 补充题目3
    std::vector<int> pricesForCodeforces = {1, 2, 3, 4};
    std::cout << "补充题目3最大利润: " << Solution::maxProfitCodeforces(pricesForCodeforces) << " (期望: 6)" << std::endl;
    
    // 补充题目5
    std::vector<int> pricesForRest = {1, 2, 3, 0, 2};
    std::cout << "补充题目5最大利润: " << Solution::maxProfitWithRest(pricesForRest) << " (期望: 3)" << std::endl;
    
    // 补充题目6
    std::vector<int> sightseeingValues = {8, 1, 5, 2, 6};
    std::cout << "补充题目6最佳观光组合: " << Solution::maxScoreSightseeingPair(sightseeingValues) << " (期望: 11)" << std::endl;
    
    // 补充题目7
    std::vector<int> stockMarketPrices = {1, 2, 3, 4, 5};
    std::cout << "补充题目7股票市场利润: " << Solution::stockMarketMaxProfit(stockMarketPrices) << " (期望: 4)" << std::endl;
    
    // 补充题目8
    std::vector<int> buyLowPrices = {5, 4, 3, 2, 1};
    auto result8 = Solution::buyLowCount(buyLowPrices);
    std::cout << "补充题目8最长递减子序列: 长度=" << result8.first << ", 数量=" << result8.second << " (期望: 长度=5, 数量=1)" << std::endl;
    
    // 补充题目9
    std::vector<int> feedCosts = {2, 3, 1};
    std::vector<int> feedValues = {5, 4, 3};
    int feedBudget = 5;
    std::cout << "补充题目9购买饲料最大价值: " << Solution::buyFeedMaxProfit(feedCosts, feedValues, feedBudget) << " (期望: 8)" << std::endl;
    
    // 补充题目10
    long long zooNumber = 24;
    std::cout << "补充题目10动物园操作次数: " << Solution::zooGameOperations(zooNumber) << " (期望: 3)" << std::endl;
    
    std::cout << "\n=== 测试完成 ===" << std::endl;
    
    return 0;
}
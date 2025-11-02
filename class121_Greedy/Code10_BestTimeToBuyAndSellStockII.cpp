#include <iostream>
#include <vector>
using namespace std;

// 买卖股票的最佳时机 II
// 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格。
// 在每一天，你可以决定是否购买和/或出售股票。你在任何时候 最多 只能持有 一股 股票。
// 你也可以先购买，然后在 同一天 出售。
// 返回 你能获得的最大利润。
// 测试链接: https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

class Code10_BestTimeToBuyAndSellStockII {
public:
    /**
     * 买卖股票的最佳时机 II 问题的贪心解法
     * 
     * 解题思路：
     * 1. 使用贪心策略，只要第二天的价格比今天高，就在今天买入明天卖出
     * 2. 累加所有上涨的差值就是最大利润
     * 3. 这相当于抓住了每一次上涨的机会，避免了每一次下跌的损失
     * 
     * 贪心策略的正确性：
     * 我们可以将整个交易过程分解为每天的小交易，如果明天价格更高就今天买入明天卖出
     * 这样可以获取所有可能的利润，而且不会错过任何盈利机会
     * 
     * 时间复杂度：O(n)，只需要遍历数组一次
     * 空间复杂度：O(1)，只使用了常数个额外变量
     * 
     * @param prices 股票每天的价格数组
     * @return 能获得的最大利润
     */
    static int maxProfit(vector<int>& prices) {
        // 边界条件处理：如果数组为空或只有一个元素，则无法获利
        if (prices.size() <= 1) {
            return 0;
        }

        // 1. 初始化最大利润
        int maxProfit = 0;

        // 2. 遍历数组，从第一天到倒数第二天
        for (int i = 0; i < prices.size() - 1; i++) {
            // 3. 如果明天价格比今天高，则今天买入明天卖出
            if (prices[i + 1] > prices[i]) {
                maxProfit += prices[i + 1] - prices[i];
            }
        }

        // 4. 返回最大利润
        return maxProfit;
    }
};

// 测试方法
int main() {
    // 测试用例1
    // 输入: prices = [7,1,5,3,6,4]
    // 输出: 7
    // 解释: 第2天买入第3天卖出(利润4)，第4天买入第5天卖出(利润3)，总利润7
    vector<int> prices1 = {7, 1, 5, 3, 6, 4};
    cout << "测试用例1结果: " << Code10_BestTimeToBuyAndSellStockII::maxProfit(prices1) << endl; // 期望输出: 7

    // 测试用例2
    // 输入: prices = [1,2,3,4,5]
    // 输出: 4
    // 解释: 第1天买入第5天卖出，利润4
    vector<int> prices2 = {1, 2, 3, 4, 5};
    cout << "测试用例2结果: " << Code10_BestTimeToBuyAndSellStockII::maxProfit(prices2) << endl; // 期望输出: 4

    // 测试用例3
    // 输入: prices = [7,6,4,3,1]
    // 输出: 0
    // 解释: 价格持续下跌，不交易利润最大
    vector<int> prices3 = {7, 6, 4, 3, 1};
    cout << "测试用例3结果: " << Code10_BestTimeToBuyAndSellStockII::maxProfit(prices3) << endl; // 期望输出: 0

    // 测试用例4：边界情况
    // 输入: prices = [1]
    // 输出: 0
    vector<int> prices4 = {1};
    cout << "测试用例4结果: " << Code10_BestTimeToBuyAndSellStockII::maxProfit(prices4) << endl; // 期望输出: 0

    // 测试用例5：复杂情况
    // 输入: prices = [1,2,1,3,2,5]
    // 输出: 6
    // 解释: 第1天买入第2天卖出(利润1)，第3天买入第4天卖出(利润2)，第5天买入第6天卖出(利润3)
    vector<int> prices5 = {1, 2, 1, 3, 2, 5};
    cout << "测试用例5结果: " << Code10_BestTimeToBuyAndSellStockII::maxProfit(prices5) << endl; // 期望输出: 6

    return 0;
}
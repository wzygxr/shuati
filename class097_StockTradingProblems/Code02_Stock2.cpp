#include <iostream>
#include <vector>
#include <algorithm>

// 买卖股票的最佳时机 II
// 给你一个整数数组 prices ，其中 prices[i] 表示某支股票第 i 天的价格
// 在每一天，你可以决定是否购买和/或出售股票
// 你在任何时候 最多 只能持有 一股 股票
// 你也可以先购买，然后在 同一天 出售
// 返回 你能获得的 最大 利润
// 测试链接 : https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-ii/

class Solution {
public:
    /*
     * 解题思路:
     * 这是股票系列问题中最简单的一个变种，允许无限次交易。
     * 核心思想是"贪心算法"，只要明天价格比今天高，就在今天买入明天卖出。
     * 或者可以理解为收集所有的上升波段。
     * 
     * 算法步骤:
     * 1. 遍历价格数组，从第二天开始
     * 2. 如果今天价格比昨天高，就将差值加入总利润
     * 3. 返回总利润
     * 
     * 时间复杂度分析:
     * O(n) - 只需要遍历一次数组，n为数组长度
     * 
     * 空间复杂度分析:
     * O(1) - 只使用了常数级别的额外空间
     * 
     * 是否为最优解:
     * 是，这是解决该问题的最优解
     * 
     * 工程化考量:
     * 1. 边界条件处理: 空数组或只有一个元素的情况
     * 2. 异常处理: 输入参数校验
     * 3. 可读性: 变量命名清晰，注释详细
     * 
     * 相关题目扩展:
     * 1. LeetCode 121. 买卖股票的最佳时机 - https://leetcode.cn/problems/best-time-to-buy-and-sell-stock/
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
        
        int ans = 0;
        for (size_t i = 1; i < prices.size(); i++) {
            // 只要今天价格比昨天高，就将差值加入总利润
            // 这等价于在所有上涨日进行交易
            ans += std::max(prices[i] - prices[i - 1], 0);
        }
        return ans;
    }
};

// 测试方法
int main() {
    // 测试用例1: [7,1,5,3,6,4] -> 7
    std::vector<int> prices1 = {7, 1, 5, 3, 6, 4};
    std::cout << "测试用例1结果: " << Solution::maxProfit(prices1) << std::endl; // 期望输出: 7
    // 解释: 第2天买入(1)，第3天卖出(5)获利4；第4天买入(3)，第5天卖出(6)获利3；总利润7
    
    // 测试用例2: [1,2,3,4,5] -> 4
    std::vector<int> prices2 = {1, 2, 3, 4, 5};
    std::cout << "测试用例2结果: " << Solution::maxProfit(prices2) << std::endl; // 期望输出: 4
    // 解释: 第1天买入(1)，第5天卖出(5)获利4
    
    // 测试用例3: [7,6,4,3,1] -> 0
    std::vector<int> prices3 = {7, 6, 4, 3, 1};
    std::cout << "测试用例3结果: " << Solution::maxProfit(prices3) << std::endl; // 期望输出: 0
    // 解释: 价格持续下跌，不交易利润最大
    
    return 0;
}
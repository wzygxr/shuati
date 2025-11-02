package class082;

// 牛客网股票交易问题
// 题目链接: https://blog.csdn.net/m0_48554728/article/details/120830277
// 题目描述:
// 假设你有一个数组，其中第i个元素是股票在第i天的价格。
// 你可以买入一次股票和卖出一次股票（并非每天都可以买入或卖出一次，总共只能买入和卖出一次），
// 问能获得的最大收益是多少。
//
// 解题思路:
// 这是一个经典的动态规划问题，核心思想是"一次遍历"。
// 我们维护两个变量：
// 1. minPrice - 到目前为止遇到的最低价格
// 2. maxProfit - 到目前为止能获得的最大利润
//
// 算法步骤:
// 1. 初始化minPrice为第一天的价格，maxProfit为0
// 2. 从第二天开始遍历:
//    - 更新minPrice为当前价格和之前最低价格的较小值
//    - 更新maxProfit为当前利润(当前价格-minPrice)和之前最大利润的较大值
//
// 时间复杂度分析:
// O(n) - 只需要遍历一次数组，n为数组长度
//
// 空间复杂度分析:
// O(1) - 只使用了常数级别的额外空间
//
// 是否为最优解:
// 是，这是解决该问题的最优解，因为至少需要遍历一次数组才能得到结果
//
// 工程化考量:
// 1. 边界条件处理: 空数组或只有一个元素的情况
// 2. 异常处理: 输入参数校验
// 3. 可读性: 变量命名清晰，注释详细

public class Code13_NowcoderStock {
    
    /**
     * 计算最大利润
     * 
     * @param prices 股票价格数组，prices[i]表示第i天的股票价格
     * @return 最大利润，如果无法交易则返回0
     * 
     * 算法详解:
     * 使用贪心算法解决股票交易问题，核心思想是维护到目前为止的最低价格和最大利润。
     * 通过一次遍历即可得到最优解，时间复杂度为O(n)，空间复杂度为O(1)。
     */
    public static int maxProfit(int[] prices) {
        // 边界条件处理：空数组或只有一个元素的情况
        // 如果数组为空或元素个数小于等于1，则无法进行交易，利润为0
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        
        // minPrice: 到目前为止遇到的最低价格
        // 初始化为第一天的价格，因为我们只能从第一天开始交易
        int minPrice = prices[0];
        
        // maxProfit: 到目前为止能获得的最大利润
        // 初始化为0，表示如果后续没有更好的交易机会，至少不会亏损
        int maxProfit = 0;
        
        // 一次遍历：从第二天开始遍历数组
        // 这是因为我们需要比较当前价格与之前的价格来计算利润
        for (int i = 1; i < prices.length; i++) {
            // 更新到目前为止的最小价格
            // 如果当前价格比之前记录的最低价格更低，则更新最低价格
            // 这确保我们始终知道到目前为止的最优买入时机
            minPrice = Math.min(minPrice, prices[i]);
            
            // 更新到目前为止的最大利润
            // 计算如果今天卖出股票能获得的利润（当前价格 - 最低买入价格）
            // 如果这个利润比之前记录的最大利润更高，则更新最大利润
            maxProfit = Math.max(maxProfit, prices[i] - minPrice);
        }
        
        // 返回计算得到的最大利润
        return maxProfit;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: [1,4,2] -> 3
        // 最佳交易策略：第1天买入(价格1)，第2天卖出(价格4)，利润=4-1=3
        int[] prices1 = {1, 4, 2};
        int result1 = maxProfit(prices1);
        System.out.println("测试用例1结果: " + result1); // 期望输出: 3
        assert result1 == 3 : "测试用例1失败";
        
        // 测试用例2: [2,4,1] -> 2
        // 最佳交易策略：第1天买入(价格2)，第2天卖出(价格4)，利润=4-2=2
        // 注意：虽然第3天价格更低(1)，但之后没有更高的价格卖出，所以不考虑
        int[] prices2 = {2, 4, 1};
        int result2 = maxProfit(prices2);
        System.out.println("测试用例2结果: " + result2); // 期望输出: 2
        assert result2 == 2 : "测试用例2失败";
        
        // 测试用例3: [3,2,1] -> 0
        // 最佳交易策略：价格持续下跌，不交易利润最大，利润=0
        int[] prices3 = {3, 2, 1};
        int result3 = maxProfit(prices3);
        System.out.println("测试用例3结果: " + result3); // 期望输出: 0
        assert result3 == 0 : "测试用例3失败";
        
        System.out.println("所有测试通过!");
    }
}
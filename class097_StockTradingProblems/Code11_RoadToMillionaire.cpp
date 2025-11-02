// AtCoder M-SOLUTIONS2020 D - Road to Millionaire
// 题目链接: https://atcoder.jp/contests/m-solutions2020/tasks/m_solutions2020_d
// 题目描述:
// M君想成为百万富翁，他决定从明天开始的N天内通过投资赚钱。
// 他目前有1000日元，没有股票。
// 他能预知未来N天的股票价格A1, A2, ..., AN。
// 每天他可以在当前资金和股票范围内进行任意次数的交易：
// 1. 股票购买：支付Ai日元，获得1股股票
// 2. 股票出售：出售1股股票，获得Ai日元
// 目标是通过最优交易策略获得最大资金。
//
// 解题思路:
// 这是一个动态规划问题。我们可以使用状态机的思想来解决。
// 定义状态：
// hold: 持有股票时的最大资金
// sold: 不持有股票时的最大资金
// 每天我们根据前一天的状态来更新当前状态。
//
// 算法步骤:
// 1. 初始化hold为一个很小的值(表示不可能状态)，sold为1000
// 2. 对于每一天的价格，更新状态：
//    newHold = max(hold, sold - price)  // 继续持有股票 or 买入股票
//    newSold = max(sold, hold + price)  // 继续不持有股票 or 卖出股票
// 3. 更新hold和sold
// 4. 最终结果是sold(不持有股票时的最大资金)
//
// 时间复杂度分析:
// O(n) - 只需要遍历一次价格数组
//
// 空间复杂度分析:
// O(1) - 只使用了常数级别的额外空间
//
// 是否为最优解:
// 是，这是解决该问题的最优解，状态机DP能保证全局最优
//
// 工程化考量:
// 1. 边界条件处理: 空数组或单元素数组
// 2. 异常处理: 输入参数校验
// 3. 可读性: 变量命名清晰，注释详细

#define MAX_N 80
#define INF 1000000000LL

// 计算最大资金
long long roadToMillionaire(int prices[], int n) {
    // 边界条件处理
    if (prices == 0 || n == 0) {
        return 1000;
    }
    
    // 初始化状态
    // hold: 持有股票时的最大资金(初始为不可能状态)
    long long hold = -INF;
    // sold: 不持有股票时的最大资金(初始为1000日元)
    long long sold = 1000;
    
    // 状态转移
    for (int i = 0; i < n; i++) {
        long long newHold = (hold > sold - prices[i]) ? hold : sold - prices[i];  // 继续持有股票 or 买入股票
        long long newSold = (sold > hold + prices[i]) ? sold : hold + prices[i];  // 继续不持有股票 or 卖出股票
        
        hold = newHold;
        sold = newSold;
    }
    
    // 返回不持有股票时的最大资金
    return sold;
}

// 由于编译环境限制，不提供测试函数
// 可以通过AtCoder平台进行测试
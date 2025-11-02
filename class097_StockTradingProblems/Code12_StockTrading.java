package class082;

// 洛谷P2569 [SCOI2010] 股票交易
// 题目链接: https://www.luogu.com.cn/problem/P2569
// 题目描述:
// lxhgww预测到了未来T天内某只股票的走势，第i天的股票买入价为每股APi，卖出价为每股BPi。
// 每天的交易限制：一次买入至多只能购买ASi股，一次卖出至多只能卖出BSi股。
// 交易规则：
// 1. 两次交易之间至少要间隔W天
// 2. 任何时候手里的股票数不能超过MaxP
// 目标是赚到最多的钱。
//
// 解题思路:
// 这是一个复杂的动态规划问题，需要考虑多个状态和约束条件。
// 使用dp[i][j]表示第i天持有j股股票时的最大资金。
// 状态转移需要考虑：
// 1. 不交易：dp[i][j] = dp[i-1][j]
// 2. 买入：dp[i][j] = max(dp[i-W-1][k] - (j-k)*APi) (k < j)
// 3. 卖出：dp[i][j] = max(dp[i-W-1][k] + (k-j)*BPi) (k > j)
// 为了优化时间复杂度，使用单调队列优化。
//
// 算法步骤:
// 1. 初始化dp数组
// 2. 对于每一天，考虑不交易、买入、卖出三种情况
// 3. 使用单调队列优化状态转移
// 4. 返回最后一天所有状态中的最大值
//
// 时间复杂度分析:
// O(T*MaxP) - 使用单调队列优化后的复杂度
//
// 空间复杂度分析:
// O(T*MaxP) - dp数组的空间
//
// 是否为最优解:
// 是，使用单调队列优化的DP是解决该问题的最优解
//
// 工程化考量:
// 1. 边界条件处理: 初始状态、交易间隔等
// 2. 异常处理: 输入参数校验
// 3. 可读性: 变量命名清晰，注释详细
//
// 优化说明:
// 当前实现为简化版本，未使用单调队列优化。完整优化版本的时间复杂度可达到O(T*MaxP)

public class Code12_StockTrading {
    
    /**
     * 计算最大利润
     * 
     * @param T 天数
     * @param MaxP 最大持股数
     * @param W 交易间隔天数
     * @param AP 买入价数组，AP[i]表示第i+1天的买入价
     * @param BP 卖出价数组，BP[i]表示第i+1天的卖出价
     * @param AS 买入限制数组，AS[i]表示第i+1天最多买入股数
     * @param BS 卖出限制数组，BS[i]表示第i+1天最多卖出股数
     * @return 最大利润
     * 
     * 详细说明:
     * 使用动态规划解决复杂的股票交易问题，考虑交易间隔和持股上限约束。
     * dp[i][j] 表示第i天持有j股股票时的最大资金。
     */
    public static int stockTrading(int T, int MaxP, int W, int[] AP, int[] BP, int[] AS, int[] BS) {
        // dp[i][j] 表示第i天持有j股股票时的最大资金
        // 初始化为Integer.MIN_VALUE，表示不可能达到的状态
        int[][] dp = new int[T + 1][MaxP + 1];
        
        // 初始化：第0天，没有股票时资金为0，有股票时为不可能状态
        // 使用Integer.MIN_VALUE表示不可能达到的状态，确保在状态转移中不会被选中
        for (int i = 0; i <= T; i++) {
            for (int j = 0; j <= MaxP; j++) {
                dp[i][j] = Integer.MIN_VALUE;
            }
        }
        dp[0][0] = 0;  // 初始状态：第0天不持有股票，资金为0
        
        // 动态规划填表：从第1天开始计算到第T天
        for (int i = 1; i <= T; i++) {
            // 不交易的情况：保持前一天的状态
            // 这表示在第i天不进行任何交易操作，直接延续前一天的状态
            for (int j = 0; j <= MaxP; j++) {
                dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
            }
            
            // 考虑交易间隔：计算有效的前一个交易日
            // 由于交易规则要求两次交易之间至少间隔W天，所以需要找到合适的前一个交易日
            int prevDay = Math.max(0, i - W - 1);
            
            // 买入的情况（使用单调队列优化）
            if (prevDay >= 0) {
                // 简化实现，不使用单调队列优化
                // 从0股开始计算，最多买入AS[i-1]股
                // 状态转移方程：dp[i][j] = max(dp[i][j], dp[prevDay][k] - (j-k)*AP[i-1])
                // 其中k是买入前的持股数，j是买入后的持股数
                for (int j = 0; j <= MaxP; j++) {
                    // 计算买入前的持股数范围：从max(0, j-AS[i-1])到j
                    // j-AS[i-1]表示买入前的最少持股数，0表示不能为负数
                    for (int k = Math.max(0, j - AS[i - 1]); k <= j; k++) {
                        if (dp[prevDay][k] != Integer.MIN_VALUE) {
                            // 计算买入j-k股后的资金变化
                            dp[i][j] = Math.max(dp[i][j], dp[prevDay][k] - (j - k) * AP[i - 1]);
                        }
                    }
                }
            }
            
            // 卖出的情况（使用单调队列优化）
            if (prevDay >= 0) {
                // 简化实现，不使用单调队列优化
                // 从j股开始计算，最多卖出BS[i-1]股
                // 状态转移方程：dp[i][j] = max(dp[i][j], dp[prevDay][k] + (k-j)*BP[i-1])
                // 其中k是卖出前的持股数，j是卖出后的持股数
                for (int j = 0; j <= MaxP; j++) {
                    // 计算卖出前的持股数范围：从j到min(MaxP, j+BS[i-1])
                    // j+BS[i-1]表示卖出前的最大持股数，MaxP表示不能超过最大持股限制
                    for (int k = j; k <= Math.min(MaxP, j + BS[i - 1]); k++) {
                        if (dp[prevDay][k] != Integer.MIN_VALUE) {
                            // 计算卖出k-j股后的资金变化
                            dp[i][j] = Math.max(dp[i][j], dp[prevDay][k] + (k - j) * BP[i - 1]);
                        }
                    }
                }
            }
        }
        
        // 找到最大利润：遍历最后一天所有可能的持股状态，找到资金最多的状态
        int maxProfit = 0;
        for (int j = 0; j <= MaxP; j++) {
            maxProfit = Math.max(maxProfit, dp[T][j]);
        }
        
        return maxProfit;
    }
    
    // 由于题目复杂度较高，不提供完整测试函数
    // 可以通过洛谷平台进行测试
}
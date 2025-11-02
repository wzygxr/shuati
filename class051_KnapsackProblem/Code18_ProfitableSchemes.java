package class073;

// LeetCode 879. 盈利计划
// 题目描述：集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
// 第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。
// 如果成员参与了其中一项工作，就不能参与另一项工作。
// 工作的任何至少产生 minProfit 利润的子集称为 盈利计划 。
// 并且工作的成员总数最多为 n 。
// 有多少种计划可以选择？因为答案很大，所以返回结果模 10^9 + 7 的值。
// 链接：https://leetcode.cn/problems/profitable-schemes/
// 
// 解题思路：
// 这是一个三维费用的01背包问题。
// 三维分别是：员工人数、利润、可选的工作数量
// 状态定义：dp[i][j][k] 表示考虑前i个工作，使用j个员工，获得至少k的利润的方案数
// 状态转移方程：
// - 不选择第i个工作：dp[i][j][k] = dp[i-1][j][k]
// - 选择第i个工作：dp[i][j][k] += dp[i-1][j-group[i-1]][Math.max(0, k-profit[i-1])]
// 
// 时间复杂度：O(N * minProfit * n)，其中N是工作数量，n是员工人数
// 空间复杂度：O(N * minProfit * n)，可以优化到O(minProfit * n)

public class Code18_ProfitableSchemes {
    // 模数
    private static final int MOD = 1000000007;

    // 主方法，用于测试
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 5, minProfit1 = 3;
        int[] group1 = {2, 2};
        int[] profit1 = {2, 3};
        System.out.println("测试用例1结果: " + profitableSchemes(n1, minProfit1, group1, profit1)); // 预期输出: 2
        
        // 测试用例2
        int n2 = 10, minProfit2 = 5;
        int[] group2 = {2, 3, 5};
        int[] profit2 = {6, 7, 8};
        System.out.println("测试用例2结果: " + profitableSchemes(n2, minProfit2, group2, profit2)); // 预期输出: 7
    }
    
    /**
     * 计算盈利计划的数量
     * 
     * 解题思路：
     * 这是一个三维费用的01背包问题。
     * 三维分别是：员工人数、利润、可选的工作数量
     * 状态定义：dp[i][j][k] 表示考虑前i个工作，使用j个员工，获得至少k的利润的方案数
     * 状态转移方程：
     * - 不选择第i个工作：dp[i][j][k] = dp[i-1][j][k]
     * - 选择第i个工作：dp[i][j][k] += dp[i-1][j-group[i-1]][Math.max(0, k-profit[i-1])]
     * 
     * @param n 员工总数
     * @param minProfit 最小利润要求
     * @param group 每个工作所需的员工数
     * @param profit 每个工作的利润
     * @return 满足条件的计划数（模10^9+7）
     */
    public static int profitableSchemes(int n, int minProfit, int[] group, int[] profit) {
        // 参数验证
        if (n <= 0) {
            return minProfit <= 0 ? 1 : 0;
        }
        if (group == null || profit == null || group.length == 0 || group.length != profit.length) {
            return minProfit <= 0 ? 1 : 0;
        }
        
        int m = group.length; // 工作数量
        
        // 创建三维DP数组：dp[i][j][k]表示考虑前i个工作，使用j个员工，获得至少k的利润的方案数
        // 这是一个三维费用的01背包问题
        int[][][] dp = new int[m + 1][n + 1][minProfit + 1];
        
        // 初始状态：没有选择任何工作时，使用0个员工，利润为0的方案数为1
        // 这是动态规划的边界条件
        dp[0][0][0] = 1;
        
        // 遍历每个工作（物品）
        for (int i = 1; i <= m; i++) {
            int g = group[i - 1]; // 当前工作所需的员工数（第一个费用）
            int p = profit[i - 1]; // 当前工作的利润（第二个费用）
            
            // 遍历员工数（第一个费用维度）
            for (int j = 0; j <= n; j++) {
                // 遍历利润要求（第二个费用维度）
                for (int k = 0; k <= minProfit; k++) {
                    // 不选择当前工作的情况
                    // 保持前一个状态的方案数
                    dp[i][j][k] = dp[i - 1][j][k];
                    
                    // 选择当前工作的情况，需要确保员工数足够
                    if (j >= g) {
                        // 计算选择当前工作后所需的最小利润
                        // 如果k < p，则前一个状态需要的利润为0（因为负利润没有意义）
                        int prevProfit = Math.max(0, k - p);
                        // 更新方案数，注意取模
                        // dp[i][j][k] = 不选择当前工作的方案数 + 选择当前工作的方案数
                        dp[i][j][k] = (dp[i][j][k] + dp[i - 1][j - g][prevProfit]) % MOD;
                    }
                }
            }
        }
        
        // 计算所有可能的方案数：员工数不超过n，利润至少为minProfit
        // 遍历所有可能的员工数，累加方案数
        int result = 0;
        for (int j = 0; j <= n; j++) {
            result = (result + dp[m][j][minProfit]) % MOD;
        }
        
        return result;
    }
    
    /**
     * 空间优化版本：使用二维DP数组
     * 
     * @param n 员工总数
     * @param minProfit 最小利润要求
     * @param group 每个工作所需的员工数
     * @param profit 每个工作的利润
     * @return 满足条件的计划数（模10^9+7）
     */
    public static int profitableSchemesOptimized(int n, int minProfit, int[] group, int[] profit) {
        // 参数验证
        if (n <= 0) {
            return minProfit <= 0 ? 1 : 0;
        }
        if (group == null || profit == null || group.length == 0 || group.length != profit.length) {
            return minProfit <= 0 ? 1 : 0;
        }
        
        int m = group.length; // 工作数量
        
        // 创建二维DP数组：dp[j][k]表示使用j个员工，获得至少k的利润的方案数
        // 使用滚动数组优化空间复杂度，相当于将dp[i][j][k]压缩为dp[j][k]
        int[][] dp = new int[n + 1][minProfit + 1];
        
        // 初始状态：使用0个员工，利润为0的方案数为1
        dp[0][0] = 1;
        
        // 遍历每个工作（物品）
        for (int i = 0; i < m; i++) {
            int g = group[i]; // 当前工作所需的员工数
            int p = profit[i]; // 当前工作的利润
            
            // 逆序遍历员工数，避免重复计算
            // 倒序遍历确保每个物品只使用一次
            for (int j = n; j >= g; j--) {
                // 逆序遍历利润要求
                for (int k = minProfit; k >= 0; k--) {
                    // 计算选择当前工作后所需的最小利润
                    int prevProfit = Math.max(0, k - p);
                    // 更新方案数，注意取模
                    dp[j][k] = (dp[j][k] + dp[j - g][prevProfit]) % MOD;
                }
            }
        }
        
        // 计算所有可能的方案数
        int result = 0;
        for (int j = 0; j <= n; j++) {
            result = (result + dp[j][minProfit]) % MOD;
        }
        
        return result;
    }
    
    /**
     * 进一步优化：针对利润维度进行优化，当利润超过minProfit时，可以将其视为等于minProfit
     * 
     * @param n 员工总数
     * @param minProfit 最小利润要求
     * @param group 每个工作所需的员工数
     * @param profit 每个工作的利润
     * @return 满足条件的计划数（模10^9+7）
     */
    public static int profitableSchemesFurtherOptimized(int n, int minProfit, int[] group, int[] profit) {
        // 参数验证
        if (n <= 0) {
            return minProfit <= 0 ? 1 : 0;
        }
        if (group == null || profit == null || group.length == 0 || group.length != profit.length) {
            return minProfit <= 0 ? 1 : 0;
        }
        
        // 创建二维DP数组
        int[][] dp = new int[n + 1][minProfit + 1];
        dp[0][0] = 1;
        
        // 遍历每个工作
        for (int i = 0; i < group.length; i++) {
            int g = group[i];
            int p = profit[i];
            
            // 剪枝：如果工作所需员工数超过总员工数，跳过
            // 因为当前工作本身就无法完成
            if (g > n) {
                continue;
            }
            
            // 逆序遍历员工数
            for (int j = n; j >= g; j--) {
                // 计算当前工作的实际利润贡献（不超过minProfit）
                // 当利润超过minProfit时，可以将其视为等于minProfit
                int actualProfit = Math.min(p, minProfit);
                
                // 遍历利润要求
                for (int k = minProfit; k >= 0; k--) {
                    // 计算选择当前工作后所需的最小利润
                    int prevProfit = Math.max(0, k - actualProfit);
                    // 更新方案数，注意取模
                    dp[j][k] = (dp[j][k] + dp[j - g][prevProfit]) % MOD;
                }
            }
        }
        
        // 计算所有可能的方案数
        int result = 0;
        for (int j = 0; j <= n; j++) {
            result = (result + dp[j][minProfit]) % MOD;
        }
        
        return result;
    }
    
    /*
     * 示例:
     * 输入: n = 5, minProfit = 3, group = [2,2], profit = [2,3]
     * 输出: 2
     * 解释: 至少产生3利润的计划有2种：完成工作1和工作2，或仅完成工作2。
     *
     * 输入: n = 10, minProfit = 5, group = [2,3,5], profit = [6,7,8]
     * 输出: 7
     * 解释: 至少产生5利润的计划有7种。
     *
     * 时间复杂度: O(N * minProfit * n)
     *   - 外层循环遍历所有工作：O(N)
     *   - 中层循环遍历员工数：O(n)
     *   - 内层循环遍历利润：O(minProfit)
     * 空间复杂度: O(minProfit * n)
     *   - 二维DP数组的空间消耗
     */
}
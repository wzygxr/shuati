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

#define MOD 1000000007
#define MAX_N 101
#define MAX_P 101

// 获取两个数中的较大值
int max(int a, int b) {
    return a > b ? a : b;
}

// 获取两个数中的较小值
int min(int a, int b) {
    return a < b ? a : b;
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
 * 参数:
 *   n: 员工总数
 *   minProfit: 最小利润要求
 *   group: 每个工作所需的员工数
 *   groupSize: group数组的大小
 *   profit: 每个工作的利润
 *   profitSize: profit数组的大小
 * 返回值:
 *   满足条件的计划数（模10^9+7）
 */
int profitableSchemes(int n, int minProfit, int* group, int groupSize, int* profit, int profitSize) {
    // 参数验证
    if (n <= 0) {
        return minProfit <= 0 ? 1 : 0;
    }
    if (groupSize == 0 || profitSize == 0 || groupSize != profitSize) {
        return minProfit <= 0 ? 1 : 0;
    }
    
    int m = groupSize; // 工作数量
    
    // 创建三维DP数组：dp[i][j][k]表示考虑前i个工作，使用j个员工，获得至少k的利润的方案数
    // 这是一个三维费用的01背包问题
    int dp[101][MAX_N][MAX_P]; // 假设最多100个工作
    
    // 初始化DP数组
    for (int i = 0; i <= m; i++) {
        for (int j = 0; j <= n; j++) {
            for (int k = 0; k <= minProfit; k++) {
                dp[i][j][k] = 0;
            }
        }
    }
    
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
                    int prevProfit = max(0, k - p);
                    // 更新方案数，注意取模
                    // dp[i][j][k] = 不选择当前工作的方案数 + 选择当前工作的方案数
                    long long newValue = (long long)dp[i][j][k] + dp[i - 1][j - g][prevProfit];
                    dp[i][j][k] = newValue % MOD;
                }
            }
        }
    }
    
    // 计算所有可能的方案数：员工数不超过n，利润至少为minProfit
    // 遍历所有可能的员工数，累加方案数
    int result = 0;
    for (int j = 0; j <= n; j++) {
        result = ((long long)result + dp[m][j][minProfit]) % MOD;
    }
    
    return result;
}

/**
 * 空间优化版本：使用二维DP数组
 * 
 * 参数:
 *   n: 员工总数
 *   minProfit: 最小利润要求
 *   group: 每个工作所需的员工数
 *   groupSize: group数组的大小
 *   profit: 每个工作的利润
 *   profitSize: profit数组的大小
 * 返回值:
 *   满足条件的计划数（模10^9+7）
 */
int profitableSchemesOptimized(int n, int minProfit, int* group, int groupSize, int* profit, int profitSize) {
    // 参数验证
    if (n <= 0) {
        return minProfit <= 0 ? 1 : 0;
    }
    if (groupSize == 0 || profitSize == 0 || groupSize != profitSize) {
        return minProfit <= 0 ? 1 : 0;
    }
    
    int m = groupSize; // 工作数量
    
    // 创建二维DP数组：dp[j][k]表示使用j个员工，获得至少k的利润的方案数
    // 使用滚动数组优化空间复杂度，相当于将dp[i][j][k]压缩为dp[j][k]
    int dp[MAX_N][MAX_P];
    
    // 初始化DP数组
    for (int j = 0; j <= n; j++) {
        for (int k = 0; k <= minProfit; k++) {
            dp[j][k] = 0;
        }
    }
    
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
                int prevProfit = max(0, k - p);
                // 更新方案数，注意取模
                long long newValue = (long long)dp[j][k] + dp[j - g][prevProfit];
                dp[j][k] = newValue % MOD;
            }
        }
    }
    
    // 计算所有可能的方案数
    int result = 0;
    for (int j = 0; j <= n; j++) {
        result = ((long long)result + dp[j][minProfit]) % MOD;
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
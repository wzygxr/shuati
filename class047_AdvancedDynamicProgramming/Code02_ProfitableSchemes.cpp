/**
 * 盈利计划 (Profitable Schemes) - 多维费用背包问题 - C++实现
 * 
 * 题目描述：
 * 集团里有 n 名员工，他们可以完成各种各样的工作创造利润。
 * 第 i 种工作会产生 profit[i] 的利润，它要求 group[i] 名成员共同参与。
 * 如果成员参与了其中一项工作，就不能参与另一项工作。
 * 工作的任何至少产生 minProfit 利润的子集称为盈利计划，并且工作的成员总数最多为 n。
 * 有多少种计划可以选择？因为答案很大，答案对 1000000007 取模。
 * 
 * 题目来源：LeetCode 879. 盈利计划
 * 测试链接：https://leetcode.cn/problems/profitable-schemes/
 * 
 * 解题思路：
 * 这是一个多维费用背包问题，有两个维度的限制：员工数量限制和利润要求。
 * 我们需要计算满足员工数量不超过n且利润至少为minProfit的方案数。
 * 
 * 算法实现：
 * 1. 动态规划：使用三维DP表存储状态
 * 2. 空间优化：使用二维数组滚动更新
 * 
 * 时间复杂度分析：
 * - 动态规划：O(m * n * minProfit)，其中m为工作数量
 * - 空间优化：O(n * minProfit)，空间复杂度最优
 * 
 * 空间复杂度分析：
 * - 动态规划：O(m * n * minProfit)，三维DP表
 * - 空间优化：O(n * minProfit)，二维DP表
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
public:
    int mod = 1000000007;
    
    /**
     * 动态规划解法
     * 
     * @param n 员工数量上限
     * @param minProfit 最小利润要求
     * @param group 每个工作需要的员工数
     * @param profit 每个工作产生的利润
     * @return 方案数
     */
    int profitableSchemes(int n, int minProfit, vector<int>& group, vector<int>& profit) {
        int m = group.size();
        // dp[i][j][k] 表示前i个工作，使用j个员工，产生至少k利润的方案数
        vector<vector<vector<int>>> dp(m + 1, 
            vector<vector<int>>(n + 1, vector<int>(minProfit + 1, 0)));
        
        // 初始化：0个工作，0个员工，0利润的方案数为1
        for (int j = 0; j <= n; j++) {
            dp[0][j][0] = 1;
        }
        
        for (int i = 1; i <= m; i++) {
            int g = group[i - 1];
            int p = profit[i - 1];
            
            for (int j = 0; j <= n; j++) {
                for (int k = 0; k <= minProfit; k++) {
                    // 不选当前工作
                    dp[i][j][k] = dp[i - 1][j][k];
                    // 选当前工作（如果员工数量足够）
                    if (j >= g) {
                        int prevProfit = max(0, k - p);
                        dp[i][j][k] = (dp[i][j][k] + dp[i - 1][j - g][prevProfit]) % mod;
                    }
                }
            }
        }
        
        return dp[m][n][minProfit];
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param n 员工数量上限
     * @param minProfit 最小利润要求
     * @param group 每个工作需要的员工数
     * @param profit 每个工作产生的利润
     * @return 方案数
     */
    int profitableSchemesOptimized(int n, int minProfit, vector<int>& group, vector<int>& profit) {
        int m = group.size();
        // dp[j][k] 表示使用j个员工，产生至少k利润的方案数
        vector<vector<int>> dp(n + 1, vector<int>(minProfit + 1, 0));
        
        // 初始化：0个员工，0利润的方案数为1
        for (int j = 0; j <= n; j++) {
            dp[j][0] = 1;
        }
        
        for (int i = 0; i < m; i++) {
            int g = group[i];
            int p = profit[i];
            
            // 从后往前更新，避免重复使用同一个工作
            for (int j = n; j >= g; j--) {
                for (int k = minProfit; k >= 0; k--) {
                    int prevProfit = max(0, k - p);
                    dp[j][k] = (dp[j][k] + dp[j - g][prevProfit]) % mod;
                }
            }
        }
        
        return dp[n][minProfit];
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 5, minProfit1 = 3;
    vector<int> group1 = {2, 2};
    vector<int> profit1 = {2, 3};
    cout << "测试用例1:" << endl;
    cout << "n = " << n1 << ", minProfit = " << minProfit1 << endl;
    cout << "group = [2, 2], profit = [2, 3]" << endl;
    cout << "方法1结果: " << solution.profitableSchemes(n1, minProfit1, group1, profit1) << endl;
    cout << "方法2结果: " << solution.profitableSchemesOptimized(n1, minProfit1, group1, profit1) << endl;
    cout << endl;
    
    // 测试用例2
    int n2 = 10, minProfit2 = 5;
    vector<int> group2 = {2, 3, 5};
    vector<int> profit2 = {6, 7, 8};
    cout << "测试用例2:" << endl;
    cout << "n = " << n2 << ", minProfit = " << minProfit2 << endl;
    cout << "group = [2, 3, 5], profit = [6, 7, 8]" << endl;
    cout << "方法1结果: " << solution.profitableSchemes(n2, minProfit2, group2, profit2) << endl;
    cout << "方法2结果: " << solution.profitableSchemesOptimized(n2, minProfit2, group2, profit2) << endl;
    
    return 0;
}
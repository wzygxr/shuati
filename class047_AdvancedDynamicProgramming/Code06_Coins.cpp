/**
 * Coins (概率DP) - C++实现
 * 
 * 题目描述：
 * 有N枚硬币，第i枚硬币抛出后正面朝上的概率是p[i]。
 * 现在将这N枚硬币都抛一次，求正面朝上的硬币数比反面朝上的硬币数多的概率。
 * 
 * 解题思路：
 * 这是一道典型的概率动态规划问题。
 * 我们可以使用dp[i][j]表示前i枚硬币中，有j枚正面朝上的概率。
 * 状态转移方程：
 * dp[i][j] = dp[i-1][j] * (1-p[i]) + dp[i-1][j-1] * p[i]
 * 其中dp[i-1][j] * (1-p[i])表示第i枚硬币反面朝上，之前有j枚正面朝上的概率
 * dp[i-1][j-1] * p[i]表示第i枚硬币正面朝上，之前有j-1枚正面朝上的概率
 * 
 * 由于要求正面朝上的硬币数比反面朝上的硬币数多，即正面朝上的硬币数 > N/2
 * 所以我们需要计算dp[N][N/2+1] + dp[N][N/2+2] + ... + dp[N][N]
 * 
 * 时间复杂度：O(N^2)
 * 空间复杂度：O(N^2)
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <cmath>
#include <functional>
using namespace std;

class Solution {
public:
    /**
     * 动态规划解法
     * 
     * @param p 硬币正面朝上的概率数组
     * @return 正面朝上的硬币数比反面朝上的硬币数多的概率
     */
    double probabilityOfHeads1(vector<double>& p) {
        int n = p.size();
        // dp[i][j] 表示前i枚硬币中，有j枚正面朝上的概率
        vector<vector<double>> dp(n + 1, vector<double>(n + 1, 0.0));
        
        // 初始状态：0枚硬币，0枚正面朝上概率为1
        dp[0][0] = 1.0;
        
        // 状态转移
        for (int i = 1; i <= n; i++) {
            // 0枚正面朝上只能是当前硬币也是反面朝上
            dp[i][0] = dp[i - 1][0] * (1 - p[i - 1]);
            
            for (int j = 1; j <= i; j++) {
                // 第i枚硬币反面朝上 + 第i枚硬币正面朝上
                dp[i][j] = dp[i - 1][j] * (1 - p[i - 1]) + dp[i - 1][j - 1] * p[i - 1];
            }
        }
        
        // 计算正面朝上的硬币数比反面朝上的硬币数多的概率
        // 即正面朝上的硬币数 > n/2
        double result = 0.0;
        for (int j = n / 2 + 1; j <= n; j++) {
            result += dp[n][j];
        }
        
        return result;
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param p 硬币正面朝上的概率数组
     * @return 正面朝上的硬币数比反面朝上的硬币数多的概率
     */
    double probabilityOfHeads2(vector<double>& p) {
        int n = p.size();
        // 只需要保存前一层的状态
        vector<double> dp(n + 1, 0.0);
        dp[0] = 1.0;
        
        // 状态转移
        for (int i = 1; i <= n; i++) {
            // 从后往前更新，避免重复使用更新后的值
            for (int j = i; j >= 1; j--) {
                dp[j] = dp[j] * (1 - p[i - 1]) + dp[j - 1] * p[i - 1];
            }
            // 更新dp[0]
            dp[0] = dp[0] * (1 - p[i - 1]);
        }
        
        // 计算正面朝上的硬币数比反面朝上的硬币数多的概率
        double result = 0.0;
        for (int j = n / 2 + 1; j <= n; j++) {
            result += dp[j];
        }
        
        return result;
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param p 硬币正面朝上的概率数组
     * @return 正面朝上的硬币数比反面朝上的硬币数多的概率
     */
    double probabilityOfHeads3(vector<double>& p) {
        int n = p.size();
        // 记忆化数组
        vector<vector<double>> memo(n + 1, vector<double>(n + 1, -1.0));
        
        // 计算正面朝上的硬币数比反面朝上的硬币数多的概率
        double result = 0.0;
        for (int j = n / 2 + 1; j <= n; j++) {
            result += dfs(p, n, j, memo);
        }
        
        return result;
    }

private:
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param p 硬币正面朝上的概率数组
     * @param i 当前处理到第几枚硬币
     * @param j 需要正面朝上的硬币数
     * @param memo 记忆化数组
     * @return 概率值
     */
    double dfs(vector<double>& p, int i, int j, vector<vector<double>>& memo) {
        // 边界条件
        if (j < 0 || j > i) {
            return 0.0;
        }
        
        if (i == 0) {
            return j == 0 ? 1.0 : 0.0;
        }
        
        // 检查是否已经计算过
        if (memo[i][j] != -1.0) {
            return memo[i][j];
        }
        
        // 第i枚硬币反面朝上 + 第i枚硬币正面朝上
        double ans = dfs(p, i - 1, j, memo) * (1 - p[i - 1]) + 
                     dfs(p, i - 1, j - 1, memo) * p[i - 1];
        
        // 记忆化存储
        memo[i][j] = ans;
        return ans;
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    vector<double> p1 = {0.3, 0.6, 0.8};
    cout << "测试用例1:" << endl;
    cout << "硬币正面朝上概率: [0.3, 0.6, 0.8]" << endl;
    cout << "方法1结果: " << solution.probabilityOfHeads1(p1) << endl;
    cout << "方法2结果: " << solution.probabilityOfHeads2(p1) << endl;
    cout << "方法3结果: " << solution.probabilityOfHeads3(p1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<double> p2 = {0.5};
    cout << "测试用例2:" << endl;
    cout << "硬币正面朝上概率: [0.5]" << endl;
    cout << "方法1结果: " << solution.probabilityOfHeads1(p2) << endl;
    cout << "方法2结果: " << solution.probabilityOfHeads2(p2) << endl;
    cout << "方法3结果: " << solution.probabilityOfHeads3(p2) << endl;
    cout << endl;
    
    // 测试用例3
    vector<double> p3 = {0.42, 0.01, 0.42, 0.99, 0.42};
    cout << "测试用例3:" << endl;
    cout << "硬币正面朝上概率: [0.42, 0.01, 0.42, 0.99, 0.42]" << endl;
    cout << "方法1结果: " << solution.probabilityOfHeads1(p3) << endl;
    cout << "方法2结果: " << solution.probabilityOfHeads2(p3) << endl;
    cout << "方法3结果: " << solution.probabilityOfHeads3(p3) << endl;
    
    return 0;
}
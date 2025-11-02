/**
 * 矩阵中和能被 K 整除的路径 (Paths in Matrix Whose Sum is Divisible by K) - 路径计数动态规划 - C++实现
 * 
 * 题目描述：
 * 给一个下标从0开始的 n * m 整数矩阵 grid 和一个整数 k。
 * 从起点(0,0)出发，每步只能往下或者往右，你想要到达终点(m-1, n-1)。
 * 请你返回路径和能被 k 整除的路径数目，答案对 1000000007 取模。
 * 
 * 题目来源：LeetCode 2435. 矩阵中和能被 K 整除的路径
 * 测试链接：https://leetcode.cn/problems/paths-in-matrix-whose-sum-is-divisible-by-k/
 * 
 * 解题思路：
 * 这是一个路径计数动态规划问题，需要在网格中统计满足特定条件（路径和能被K整除）的路径数量。
 * 由于路径数量可能很大，需要对结果取模。
 * 
 * 算法实现：
 * 1. 动态规划：使用三维DP表存储状态（位置+余数）
 * 2. 空间优化：使用二维数组滚动更新
 * 
 * 时间复杂度分析：
 * - 动态规划：O(n * m * k)，需要填充三维DP表
 * - 空间优化：O(n * m * k)，时间复杂度相同但空间更优
 * 
 * 空间复杂度分析：
 * - 动态规划：O(n * m * k)，三维DP表
 * - 空间优化：O(m * k)，二维DP表
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
     * @param grid 整数矩阵
     * @param k 除数
     * @return 路径数目
     */
    int numberOfPaths(vector<vector<int>>& grid, int k) {
        int n = grid.size();
        int m = grid[0].size();
        
        // dp[i][j][r] 表示到达(i,j)时路径和模k余r的路径数
        vector<vector<vector<int>>> dp(n, 
            vector<vector<int>>(m, vector<int>(k, 0)));
        
        // 初始化起点
        dp[0][0][grid[0][0] % k] = 1;
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int r = 0; r < k; r++) {
                    if (dp[i][j][r] == 0) continue;
                    
                    // 向右移动
                    if (j + 1 < m) {
                        int newR = (r + grid[i][j + 1]) % k;
                        dp[i][j + 1][newR] = (dp[i][j + 1][newR] + dp[i][j][r]) % mod;
                    }
                    
                    // 向下移动
                    if (i + 1 < n) {
                        int newR = (r + grid[i + 1][j]) % k;
                        dp[i + 1][j][newR] = (dp[i + 1][j][newR] + dp[i][j][r]) % mod;
                    }
                }
            }
        }
        
        return dp[n - 1][m - 1][0];
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param grid 整数矩阵
     * @param k 除数
     * @return 路径数目
     */
    int numberOfPathsOptimized(vector<vector<int>>& grid, int k) {
        int n = grid.size();
        int m = grid[0].size();
        
        // dp[j][r] 表示当前行到达第j列时路径和模k余r的路径数
        vector<vector<int>> dp(m, vector<int>(k, 0));
        
        // 初始化起点
        dp[0][grid[0][0] % k] = 1;
        
        for (int i = 0; i < n; i++) {
            vector<vector<int>> nextDp(m, vector<int>(k, 0));
            
            for (int j = 0; j < m; j++) {
                for (int r = 0; r < k; r++) {
                    if (dp[j][r] == 0) continue;
                    
                    // 当前网格的值
                    int currentVal = grid[i][j];
                    
                    // 向右移动
                    if (j + 1 < m) {
                        int newR = (r + grid[i][j + 1]) % k;
                        nextDp[j + 1][newR] = (nextDp[j + 1][newR] + dp[j][r]) % mod;
                    }
                    
                    // 向下移动
                    if (i + 1 < n) {
                        int newR = (r + grid[i + 1][j]) % k;
                        dp[j][newR] = (dp[j][newR] + dp[j][r]) % mod;
                    }
                }
            }
            
            // 更新dp数组
            if (i < n - 1) {
                for (int j = 0; j < m; j++) {
                    for (int r = 0; r < k; r++) {
                        dp[j][r] = nextDp[j][r];
                    }
                }
            }
        }
        
        return dp[m - 1][0];
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> grid1 = {{5,2,4},{3,0,5},{0,7,2}};
    int k1 = 3;
    cout << "测试用例1:" << endl;
    cout << "网格: [[5,2,4],[3,0,5],[0,7,2]]" << endl;
    cout << "k = " << k1 << endl;
    cout << "方法1结果: " << solution.numberOfPaths(grid1, k1) << endl;
    cout << "方法2结果: " << solution.numberOfPathsOptimized(grid1, k1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<vector<int>> grid2 = {{0,0}};
    int k2 = 5;
    cout << "测试用例2:" << endl;
    cout << "网格: [[0,0]]" << endl;
    cout << "k = " << k2 << endl;
    cout << "方法1结果: " << solution.numberOfPaths(grid2, k2) << endl;
    cout << "方法2结果: " << solution.numberOfPathsOptimized(grid2, k2) << endl;
    cout << endl;
    
    // 测试用例3
    vector<vector<int>> grid3 = {{7,3,4,9},{2,3,6,2},{2,3,7,0}};
    int k3 = 1;
    cout << "测试用例3:" << endl;
    cout << "网格: [[7,3,4,9],[2,3,6,2],[2,3,7,0]]" << endl;
    cout << "k = " << k3 << endl;
    cout << "方法1结果: " << solution.numberOfPaths(grid3, k3) << endl;
    cout << "方法2结果: " << solution.numberOfPathsOptimized(grid3, k3) << endl;
    
    return 0;
}
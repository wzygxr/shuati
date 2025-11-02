// 题目来源：https://leetcode.cn/problems/unique-paths/
// 题目描述：一个机器人位于一个 m x n 网格的左上角。机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角。问总共有多少条不同的路径？
// 解题思路：使用动态规划。dp[i][j]表示到达位置(i,j)的不同路径数。
// 时间复杂度：O(m*n)
// 空间复杂度：O(m*n)，可以优化到O(n)或O(min(m,n))

#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <functional>
using namespace std;

class Solution {
public:
    /**
     * 基本动态规划解法
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 到达右下角的不同路径数
     */
    int uniquePaths1(int m, int n) {
        // 边界条件检查
        if (m <= 0 || n <= 0) {
            return 0;
        }
        
        // dp[i][j] 表示到达位置(i,j)的不同路径数
        vector<vector<int>> dp(m, vector<int>(n, 0));
        
        // 初始化第一行和第一列
        // 第一行只能从左边来
        for (int j = 0; j < n; ++j) {
            dp[0][j] = 1;
        }
        
        // 第一列只能从上边来
        for (int i = 0; i < m; ++i) {
            dp[i][0] = 1;
        }
        
        // 状态转移
        for (int i = 1; i < m; ++i) {
            for (int j = 1; j < n; ++j) {
                dp[i][j] = dp[i-1][j] + dp[i][j-1];
            }
        }
        
        return dp[m-1][n-1];
    }
    
    /**
     * 空间优化的动态规划解法（使用一维数组）
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 到达右下角的不同路径数
     */
    int uniquePaths2(int m, int n) {
        // 边界条件检查
        if (m <= 0 || n <= 0) {
            return 0;
        }
        
        // 优化空间：只需要保存一行的状态
        vector<int> dp(n, 1); // 初始化为1，相当于第一行
        
        // 从第二行开始
        for (int i = 1; i < m; ++i) {
            // 对于每一行，从第二个元素开始（第一个元素始终为1）
            for (int j = 1; j < n; ++j) {
                dp[j] = dp[j] + dp[j-1]; // dp[j]原来的值相当于dp[i-1][j]，dp[j-1]是当前行左侧的值
            }
        }
        
        return dp[n-1];
    }
    
    /**
     * 进一步优化空间：交换m和n，保证始终处理较小的维度
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 到达右下角的不同路径数
     */
    int uniquePaths3(int m, int n) {
        // 边界条件检查
        if (m <= 0 || n <= 0) {
            return 0;
        }
        
        // 确保n是较小的维度，以节省空间
        if (m < n) {
            swap(m, n);
        }
        
        vector<long long> dp(n, 1); // 使用long long防止溢出
        
        for (int i = 1; i < m; ++i) {
            for (int j = 1; j < n; ++j) {
                dp[j] = dp[j] + dp[j-1];
            }
        }
        
        return static_cast<int>(dp[n-1]);
    }
    
    /**
     * 数学解法：组合问题
     * 从(m-1 + n-1)步中选择(m-1)步向下走（其余向右走）
     * 即计算组合数 C(m+n-2, m-1)
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 到达右下角的不同路径数
     */
    int uniquePaths4(int m, int n) {
        // 边界条件检查
        if (m <= 0 || n <= 0) {
            return 0;
        }
        
        // 确保m >= n，这样计算组合数时可以减少计算量
        if (m < n) {
            swap(m, n);
        }
        
        // 计算 C(m+n-2, n-1)
        long long result = 1;
        for (int i = 1, j = m; i < n; ++i, ++j) {
            result = result * j / i;
        }
        
        return static_cast<int>(result);
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 到达右下角的不同路径数
     */
    int uniquePaths5(int m, int n) {
        // 边界条件检查
        if (m <= 0 || n <= 0) {
            return 0;
        }
        
        // 创建记忆化数组
        vector<vector<int>> memo(m, vector<int>(n, -1));
        
        return dfs(0, 0, m, n, memo);
    }
    
private:
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param i 当前行坐标
     * @param j 当前列坐标
     * @param m 网格总行数
     * @param n 网格总列数
     * @param memo 记忆化数组
     * @return 从(i,j)到达右下角的不同路径数
     */
    int dfs(int i, int j, int m, int n, vector<vector<int>>& memo) {
        // 到达终点
        if (i == m-1 && j == n-1) {
            return 1;
        }
        
        // 检查是否已经计算过
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        
        int paths = 0;
        
        // 向右移动
        if (j + 1 < n) {
            paths += dfs(i, j+1, m, n, memo);
        }
        
        // 向下移动
        if (i + 1 < m) {
            paths += dfs(i+1, j, m, n, memo);
        }
        
        // 记忆化存储
        memo[i][j] = paths;
        return paths;
    }
    
public:
    /**
     * 动态规划解法（处理大数情况）
     * 使用long long来防止整数溢出
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @return 到达右下角的不同路径数
     */
    long long uniquePathsLarge(int m, int n) {
        // 边界条件检查
        if (m <= 0 || n <= 0) {
            return 0;
        }
        
        // 优化空间：只需要保存一行的状态
        vector<long long> dp(n, 1);
        
        // 从第二行开始
        for (int i = 1; i < m; ++i) {
            for (int j = 1; j < n; ++j) {
                dp[j] = dp[j] + dp[j-1];
            }
        }
        
        return dp[n-1];
    }
};

int main() {
    Solution solution;
    
    // 测试用例1：3x2网格
    int m1 = 3, n1 = 2;
    cout << "测试用例1: " << m1 << "x" << n1 << "网格" << endl;
    cout << "方法1结果: " << solution.uniquePaths1(m1, n1) << endl;
    cout << "方法2结果: " << solution.uniquePaths2(m1, n1) << endl;
    cout << "方法3结果: " << solution.uniquePaths3(m1, n1) << endl;
    cout << "方法4结果: " << solution.uniquePaths4(m1, n1) << endl;
    cout << "方法5结果: " << solution.uniquePaths5(m1, n1) << endl;
    cout << "大数结果: " << solution.uniquePathsLarge(m1, n1) << endl;
    cout << endl;
    
    // 测试用例2：3x7网格
    int m2 = 3, n2 = 7;
    cout << "测试用例2: " << m2 << "x" << n2 << "网格" << endl;
    cout << "方法1结果: " << solution.uniquePaths1(m2, n2) << endl;
    cout << "方法2结果: " << solution.uniquePaths2(m2, n2) << endl;
    cout << "方法3结果: " << solution.uniquePaths3(m2, n2) << endl;
    cout << "方法4结果: " << solution.uniquePaths4(m2, n2) << endl;
    cout << "方法5结果: " << solution.uniquePaths5(m2, n2) << endl;
    cout << "大数结果: " << solution.uniquePathsLarge(m2, n2) << endl;
    cout << endl;
    
    // 测试用例3：较大的网格
    int m3 = 10, n3 = 10;
    cout << "测试用例3: " << m3 << "x" << n3 << "网格" << endl;
    cout << "方法1结果: " << solution.uniquePaths1(m3, n3) << endl;
    cout << "方法2结果: " << solution.uniquePaths2(m3, n3) << endl;
    cout << "方法3结果: " << solution.uniquePaths3(m3, n3) << endl;
    cout << "方法4结果: " << solution.uniquePaths4(m3, n3) << endl;
    cout << "方法5结果: " << solution.uniquePaths5(m3, n3) << endl;
    cout << "大数结果: " << solution.uniquePathsLarge(m3, n3) << endl;
    
    return 0;
}
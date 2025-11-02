/**
 * 骑士拨号器 (Knight Dialer) - C++实现
 * 
 * 题目描述：
 * 象棋骑士有一个独特的移动方式，它可以垂直移动两个方格，水平移动一个方格，
 * 或者水平移动两个方格，垂直移动一个方格(两者都形成一个 L 的形状)。
 * 我们有一个象棋骑士和一个电话垫，如下所示，骑士只能站在一个数字单元格上。
 * 给定一个整数 n，返回我们可以拨多少个长度为 n 的不同电话号码。
 * 
 * 解题思路：
 * 这是一道典型的动态规划问题。
 * 我们可以使用dp[i][j]表示骑士在数字i上，还能跳j步的方案数。
 * 状态转移方程：
 * dp[i][j] = sum(dp[next][j-1]) for all next that can be reached from i
 * 
 * 骑士在数字键盘上的移动规则：
 * 0 -> 4, 6
 * 1 -> 6, 8
 * 2 -> 7, 9
 * 3 -> 4, 8
 * 4 -> 0, 3, 9
 * 5 -> (无法移动)
 * 6 -> 0, 1, 7
 * 7 -> 2, 6
 * 8 -> 1, 3
 * 9 -> 2, 4
 * 
 * 时间复杂度：O(N)
 * 空间复杂度：O(1)
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <string>
#include <functional>
using namespace std;

class Solution {
private:
    static const int MOD = 1000000007;
    
    // 骑士在每个数字上可以跳到的下一个数字
    vector<vector<int>> moves = {
        {4, 6},        // 0
        {6, 8},        // 1
        {7, 9},        // 2
        {4, 8},        // 3
        {0, 3, 9},     // 4
        {},            // 5 (无法移动)
        {0, 1, 7},     // 6
        {2, 6},        // 7
        {1, 3},        // 8
        {2, 4}         // 9
    };
    
public:
    /**
     * 动态规划解法
     * 
     * @param n 电话号码长度
     * @return 不同电话号码的数量
     */
    int knightDialer1(int n) {
        if (n == 1) {
            return 10;
        }
        
        // dp[i] 表示当前在数字i上的方案数
        vector<long long> dp(10, 1);
        
        // 状态转移
        for (int step = 2; step <= n; step++) {
            vector<long long> next(10, 0);
            for (int i = 0; i < 10; i++) {
                for (int nextNum : moves[i]) {
                    next[nextNum] = (next[nextNum] + dp[i]) % MOD;
                }
            }
            dp = next;
        }
        
        // 计算总方案数
        long long result = 0;
        for (int i = 0; i < 10; i++) {
            result = (result + dp[i]) % MOD;
        }
        
        return (int) result;
    }
    
    /**
     * 空间优化的动态规划解法
     * 
     * @param n 电话号码长度
     * @return 不同电话号码的数量
     */
    int knightDialer2(int n) {
        if (n == 1) {
            return 10;
        }
        
        // dp[i] 表示当前在数字i上的方案数
        vector<long long> dp(10, 1);
        vector<long long> next(10, 0);
        
        // 状态转移
        for (int step = 2; step <= n; step++) {
            // 初始化next数组
            for (int i = 0; i < 10; i++) {
                next[i] = 0;
            }
            
            for (int i = 0; i < 10; i++) {
                for (int nextNum : moves[i]) {
                    next[nextNum] = (next[nextNum] + dp[i]) % MOD;
                }
            }
            
            // 交换dp和next
            swap(dp, next);
        }
        
        // 计算总方案数
        long long result = 0;
        for (int i = 0; i < 10; i++) {
            result = (result + dp[i]) % MOD;
        }
        
        return (int) result;
    }
    
    /**
     * 记忆化搜索解法
     * 
     * @param n 电话号码长度
     * @return 不同电话号码的数量
     */
    int knightDialer3(int n) {
        if (n == 1) {
            return 10;
        }
        
        // memo[i][j] 表示在数字i上还能跳j步的方案数
        vector<vector<long long>> memo(10, vector<long long>(n + 1, -1));
        
        long long result = 0;
        // 从每个数字开始
        for (int i = 0; i < 10; i++) {
            result = (result + dfs(i, n - 1, memo)) % MOD;
        }
        
        return (int) result;
    }

private:
    /**
     * 深度优先搜索 + 记忆化
     * 
     * @param num 当前所在的数字
     * @param steps 剩余步数
     * @param memo 记忆化数组
     * @return 方案数
     */
    long long dfs(int num, int steps, vector<vector<long long>>& memo) {
        // 边界条件
        if (steps == 0) {
            return 1;
        }
        
        // 检查是否已经计算过
        if (memo[num][steps] != -1) {
            return memo[num][steps];
        }
        
        long long ans = 0;
        // 尝试跳到下一个数字
        for (int nextNum : moves[num]) {
            ans = (ans + dfs(nextNum, steps - 1, memo)) % MOD;
        }
        
        // 记忆化存储
        memo[num][steps] = ans;
        return ans;
    }
};

// 测试方法
int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 1;
    cout << "测试用例1:" << endl;
    cout << "电话号码长度: " << n1 << endl;
    cout << "方法1结果: " << solution.knightDialer1(n1) << endl;
    cout << "方法2结果: " << solution.knightDialer2(n1) << endl;
    cout << "方法3结果: " << solution.knightDialer3(n1) << endl;
    cout << endl;
    
    // 测试用例2
    int n2 = 2;
    cout << "测试用例2:" << endl;
    cout << "电话号码长度: " << n2 << endl;
    cout << "方法1结果: " << solution.knightDialer1(n2) << endl;
    cout << "方法2结果: " << solution.knightDialer2(n2) << endl;
    cout << "方法3结果: " << solution.knightDialer3(n2) << endl;
    cout << endl;
    
    // 测试用例3
    int n3 = 3;
    cout << "测试用例3:" << endl;
    cout << "电话号码长度: " << n3 << endl;
    cout << "方法1结果: " << solution.knightDialer1(n3) << endl;
    cout << "方法2结果: " << solution.knightDialer2(n3) << endl;
    cout << "方法3结果: " << solution.knightDialer3(n3) << endl;
    
    return 0;
}
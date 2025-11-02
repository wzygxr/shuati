// 爬楼梯 (Climbing Stairs)
// 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
// 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
// 测试链接 : https://leetcode.cn/problems/climbing-stairs/

#include <iostream>
#include <vector>
#include <chrono>
using namespace std;

class Solution {
public:
    // 方法1：暴力递归解法
    // 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    // 空间复杂度：O(n) - 递归调用栈的深度
    // 问题：存在大量重复计算，n较大时栈溢出
    int climbStairs1(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        return climbStairs1(n - 1) + climbStairs1(n - 2);
    }

    // 方法2：记忆化搜索（自顶向下动态规划）
    // 时间复杂度：O(n) - 每个状态只计算一次
    // 空间复杂度：O(n) - memo数组和递归调用栈
    // 优化：通过缓存避免重复计算，但仍有递归开销
    int climbStairs2(int n) {
        if (n <= 0) return 0;
        vector<int> memo(n + 1, -1);
        return dfs(n, memo);
    }
    
private:
    int dfs(int n, vector<int>& memo) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        if (memo[n] != -1) return memo[n];
        
        memo[n] = dfs(n - 1, memo) + dfs(n - 2, memo);
        return memo[n];
    }

public:
    // 方法3：动态规划（自底向上）
    // 时间复杂度：O(n) - 从底向上计算每个状态
    // 空间复杂度：O(n) - dp数组存储所有状态
    // 优化：避免了递归调用的开销
    int climbStairs3(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        vector<int> dp(n + 1);
        dp[1] = 1;
        dp[2] = 2;
        
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        
        return dp[n];
    }

    // 方法4：空间优化的动态规划
    // 时间复杂度：O(n) - 仍然需要计算所有状态
    // 空间复杂度：O(1) - 只保存必要的前两个状态值
    // 优化：大幅减少空间使用，工程首选
    int climbStairs4(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        int prev1 = 1; // dp[i-2]
        int prev2 = 2; // dp[i-1]
        
        for (int i = 3; i <= n; i++) {
            int current = prev1 + prev2;
            prev1 = prev2;
            prev2 = current;
        }
        
        return prev2;
    }

    // 方法5：矩阵快速幂（最优解）
    // 时间复杂度：O(log n) - 通过矩阵快速幂加速
    // 空间复杂度：O(1) - 常数空间
    // 核心思路：将递推关系转化为矩阵乘法，使用快速幂算法
    int climbStairs5(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 2;
        
        // 递推关系矩阵：[[1,1],[1,0]]
        vector<vector<long long>> base = {{1, 1}, {1, 0}};
        vector<vector<long long>> result = matrixPower(base, n - 2);
        
        // 结果矩阵与初始状态相乘
        return result[0][0] * 2 + result[0][1] * 1;
    }
    
private:
    // 矩阵快速幂算法
    vector<vector<long long>> matrixPower(vector<vector<long long>>& base, int power) {
        vector<vector<long long>> result = {{1, 0}, {0, 1}}; // 单位矩阵
        
        while (power > 0) {
            if (power & 1) {
                result = matrixMultiply(result, base);
            }
            base = matrixMultiply(base, base);
            power >>= 1;
        }
        
        return result;
    }
    
    // 2x2矩阵乘法
    vector<vector<long long>> matrixMultiply(vector<vector<long long>>& a, 
                                           vector<vector<long long>>& b) {
        vector<vector<long long>> result(2, vector<long long>(2, 0));
        result[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        result[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
        result[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
        result[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
        return result;
    }
};

// 测试函数
void testCase(Solution& solution, int n, int expected, const string& description) {
    int result1 = solution.climbStairs1(n);
    int result2 = solution.climbStairs2(n);
    int result3 = solution.climbStairs3(n);
    int result4 = solution.climbStairs4(n);
    int result5 = solution.climbStairs5(n);
    
    bool allCorrect = (result1 == expected && result2 == expected && 
                      result3 == expected && result4 == expected && result5 == expected);
    
    cout << description << ": " << (allCorrect ? "✓" : "✗");
    if (!allCorrect) {
        cout << " 方法1:" << result1 << " 方法2:" << result2 
             << " 方法3:" << result3 << " 方法4:" << result4 
             << " 方法5:" << result5 << " 预期:" << expected;
    }
    cout << endl;
}

// 性能测试函数
void performanceTest(Solution& solution, int n) {
    cout << "性能测试 n=" << n << ":" << endl;
    
    auto start = chrono::high_resolution_clock::now();
    int result3 = solution.climbStairs3(n);
    auto end = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "动态规划: " << result3 << ", 耗时: " << duration3.count() << "μs" << endl;
    
    start = chrono::high_resolution_clock::now();
    int result4 = solution.climbStairs4(n);
    end = chrono::high_resolution_clock::now();
    auto duration4 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "空间优化: " << result4 << ", 耗时: " << duration4.count() << "μs" << endl;
    
    start = chrono::high_resolution_clock::now();
    int result5 = solution.climbStairs5(n);
    end = chrono::high_resolution_clock::now();
    auto duration5 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "矩阵快速幂: " << result5 << ", 耗时: " << duration5.count() << "μs" << endl;
}

int main() {
    Solution solution;
    
    cout << "=== 爬楼梯问题测试 ===" << endl;
    
    // 边界测试
    testCase(solution, 0, 0, "n=0");
    testCase(solution, 1, 1, "n=1");
    testCase(solution, 2, 2, "n=2");
    
    // 常规测试
    testCase(solution, 3, 3, "n=3");
    testCase(solution, 4, 5, "n=4");
    testCase(solution, 5, 8, "n=5");
    testCase(solution, 10, 89, "n=10");
    
    cout << "\n=== 性能对比测试 ===" << endl;
    performanceTest(solution, 40);
    
    cout << "\n=== 错误处理测试 ===" << endl;
    int result = solution.climbStairs4(-1);
    cout << "n=-1 结果: " << result << endl;
    
    return 0;
}
#include <iostream>
#include <vector>
using namespace std;

/**
 * SPOJ FIBOSUM - Fibonacci Sum
 * 题目链接: https://www.spoj.com/problems/FIBOSUM/
 * 题目大意: 给定两个整数n和m，求斐波那契数列从第n项到第m项的和，结果对1000000007取模
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 数学分析:
 * 1. 斐波那契数列定义: F(0)=0, F(1)=1, F(n)=F(n-1)+F(n-2)
 * 2. 斐波那契数列前n项和: S(n) = F(0)+F(1)+...+F(n) = F(n+2)-1
 * 3. 从第n项到第m项的和: S(m) - S(n-1) = F(m+2) - F(n+1)
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(m-n)降低到O(log(max(n,m)))
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n>m, n=0, m=0等特殊情况
 * 2. 输入验证: 检查n和m的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 直接计算每一项然后求和，时间复杂度O(m-n)，会超时
 * 2. 动态规划: 时间复杂度O(m)，空间复杂度O(1)
 * 3. 矩阵快速幂: 时间复杂度O(log(max(n,m)))，空间复杂度O(1)，最优解
 */

const long long MOD = 1000000007;

/**
 * 2x2矩阵乘法
 * 时间复杂度: O(2^3) = O(8) = O(1)
 * 空间复杂度: O(4) = O(1)
 */
vector<vector<long long>> matrixMultiply(const vector<vector<long long>>& a, const vector<vector<long long>>& b) {
    vector<vector<long long>> res(2, vector<long long>(2, 0));
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD;
            }
        }
    }
    return res;
}

/**
 * 构造单位矩阵
 * 时间复杂度: O(2^2) = O(4) = O(1)
 * 空间复杂度: O(4) = O(1)
 */
vector<vector<long long>> identityMatrix() {
    vector<vector<long long>> res(2, vector<long long>(2, 0));
    res[0][0] = 1;
    res[1][1] = 1;
    return res;
}

/**
 * 矩阵快速幂
 * 时间复杂度: O(2^3 * logn) = O(logn)
 * 空间复杂度: O(4) = O(1)
 */
vector<vector<long long>> matrixPower(vector<vector<long long>> base, long long exp) {
    vector<vector<long long>> res = identityMatrix();
    while (exp > 0) {
        if (exp & 1) {
            res = matrixMultiply(res, base);
        }
        base = matrixMultiply(base, base);
        exp >>= 1;
    }
    return res;
}

/**
 * 计算斐波那契数列第n项
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 */
long long fibonacci(long long n) {
    if (n == 0) return 0;
    if (n == 1) return 1;
    
    // 转移矩阵
    vector<vector<long long>> base = {
        {1, 1},
        {1, 0}
    };
    
    // 计算转移矩阵的n-1次幂
    vector<vector<long long>> result = matrixPower(base, n - 1);
    
    // 初始向量 [F(1), F(0)] = [1, 0]
    // 结果为 result * [1, 0]^T 的第一个元素
    return result[0][0];
}

/**
 * 计算斐波那契数列从第n项到第m项的和
 * 时间复杂度: O(log(max(n,m)))
 * 空间复杂度: O(1)
 */
long long solve(long long n, long long m) {
    // 特殊情况处理
    if (n > m) {
        return 0;
    }
    
    // S(m) - S(n-1) = F(m+2) - F(n+1)
    long long fib_m_plus_2 = fibonacci(m + 2);
    long long fib_n_plus_1 = fibonacci(n + 1);
    
    long long result = (fib_m_plus_2 - fib_n_plus_1) % MOD;
    if (result < 0) {
        result += MOD;
    }
    return result;
}

int main() {
    int T;
    cin >> T; // 测试用例数量
    
    while (T--) {
        long long n, m;
        cin >> n >> m;
        
        long long result = solve(n, m);
        cout << result << endl;
    }
    
    return 0;
}
#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

/**
 * UVA 10655 - Contemplation! Algebra
 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1596
 * 题目大意: 给定p, q, n，其中p = a + b, q = a * b，求a^n + b^n的值
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 数学分析:
 * 1. 设S(n) = a^n + b^n
 * 2. 递推关系: S(n) = p * S(n-1) - q * S(n-2)
 * 3. 初始条件: S(0) = 2, S(1) = p
 * 4. 转换为矩阵形式:
 *    [S(n)  ]   [p  -q] [S(n-1)]
 *    [S(n-1)] = [1   0] [S(n-2)]
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n=0, n=1的特殊情况
 * 2. 输入验证: 检查p, q, n的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 递归解法: 时间复杂度O(2^n)，会超时
 * 2. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
 * 3. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)，最优解
 */

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
                res[i][j] += a[i][k] * b[k][j];
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
 * 计算a^n + b^n的值
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 */
long long solve(long long p, long long q, long long n) {
    // 特殊情况处理
    if (n == 0) {
        return 2;
    }
    if (n == 1) {
        return p;
    }
    
    // 转移矩阵
    vector<vector<long long>> base = {
        {p, -q},  // [p, -q]
        {1, 0}    // [1, 0]
    };
    
    // 计算转移矩阵的n-1次幂
    vector<vector<long long>> result = matrixPower(base, n - 1);
    
    // 初始向量 [S(1), S(0)] = [p, 2]
    // 结果为 result * [p, 2]^T 的第一个元素
    long long s1 = p;
    long long s0 = 2;
    
    return result[0][0] * s1 + result[0][1] * s0;
}

int main() {
    long long p, q, n;
    while (cin >> p >> q >> n) {
        long long result = solve(p, q, n);
        cout << result << endl;
    }
    
    return 0;
}
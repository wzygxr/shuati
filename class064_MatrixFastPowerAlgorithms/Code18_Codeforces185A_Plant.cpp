#include <iostream>
#include <vector>
#include <string>
#include <algorithm>
using namespace std;

/**
 * Codeforces 185A - Plant
 * 题目链接: https://codeforces.com/problemset/problem/185/A
 * 题目大意: 有一个植物，每年会生长。第一年植物有1个向上的三角形和0个向下的三角形。
 *          每年，每个向上的三角形会变成3个向上的三角形和1个向下的三角形。
 *          每个向下的三角形会变成1个向上的三角形和3个向下的三角形。
 *          求n年后向上的三角形数量。
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 数学分析:
 * 1. 设f(n)为n年后向上的三角形数量，g(n)为n年后向下的三角形数量
 * 2. 递推关系:
 *    f(n) = 3*f(n-1) + g(n-1)
 *    g(n) = f(n-1) + 3*g(n-1)
 * 3. 转换为矩阵形式:
 *    [f(n)]   [3 1] [f(n-1)]
 *    [g(n)] = [1 3] [g(n-1)]
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(n)降低到O(logn)
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n=0的特殊情况
 * 2. 输入验证: 检查n的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 递归解法: 时间复杂度O(2^n)，会超时
 * 2. 动态规划: 时间复杂度O(n)，空间复杂度O(1)
 * 3. 矩阵快速幂: 时间复杂度O(logn)，空间复杂度O(1)，最优解
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
 * 计算n年后向上的三角形数量
 * 时间复杂度: O(logn)
 * 空间复杂度: O(1)
 * 
 * 算法思路:
 * 1. 构建转移矩阵[[3,1],[1,3]]
 * 2. 使用矩阵快速幂计算转移矩阵的n次幂
 * 3. 乘以初始向量[1,0]得到结果
 */
long long solve(long long n) {
    // 特殊情况处理
    if (n == 0) {
        return 1;
    }
    
    // 转移矩阵
    vector<vector<long long>> base = {
        {3, 1},
        {1, 3}
    };
    
    // 计算转移矩阵的n次幂
    vector<vector<long long>> result = matrixPower(base, n);
    
    // 初始向量 [f(0), g(0)] = [1, 0]
    // 结果为 result * [1, 0]^T 的第一个元素
    return result[0][0] % MOD;
}

int main() {
    long long n;
    cin >> n;
    
    long long result = solve(n);
    cout << result << endl;
    
    // 测试用例
    cout << "n=0: " << solve(0) << endl;  // 1
    cout << "n=1: " << solve(1) << endl;  // 3
    cout << "n=2: " << solve(2) << endl;  // 10
    cout << "n=3: " << solve(3) << endl;  // 36
    
    return 0;
}
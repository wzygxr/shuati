#include <iostream>
#include <vector>
using namespace std;

/**
 * HDU 1575 - Tr A
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1575
 * 题目大意: 给定一个n×n的矩阵A，求A^k的迹（主对角线元素之和）mod 9973
 * 解法: 使用矩阵快速幂求解
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 * 
 * 数学分析:
 * 1. 矩阵的迹定义为矩阵主对角线元素之和
 * 2. 对于矩阵幂A^k，其迹等于A^k的主对角线元素之和
 * 3. 使用矩阵快速幂计算A^k，然后求迹
 * 
 * 优化思路:
 * 1. 使用矩阵快速幂将时间复杂度从O(k*n^3)降低到O(n^3 * logk)
 * 2. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: k=0的特殊情况（单位矩阵的迹为n）
 * 2. 输入验证: 检查矩阵维度和k的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 直接计算A^k然后求迹，时间复杂度O(k*n^3)
 * 2. 矩阵快速幂: 时间复杂度O(n^3 * logk)
 * 3. 最优性: 当k较大时，矩阵快速幂明显优于暴力解法
 */

const int MOD = 9973;

/**
 * 矩阵乘法
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 */
vector<vector<int>> matrixMultiply(const vector<vector<int>>& a, const vector<vector<int>>& b, int n) {
    vector<vector<int>> res(n, vector<int>(n, 0));
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            long long sum = 0;
            for (int c = 0; c < n; c++) {
                sum = (sum + (long long)a[i][c] * b[c][j]) % MOD;
            }
            res[i][j] = (int)sum;
        }
    }
    return res;
}

/**
 * 构造单位矩阵
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n^2)
 */
vector<vector<int>> identityMatrix(int n) {
    vector<vector<int>> res(n, vector<int>(n, 0));
    for (int i = 0; i < n; i++) {
        res[i][i] = 1;
    }
    return res;
}

/**
 * 矩阵快速幂
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 */
vector<vector<int>> matrixPower(const vector<vector<int>>& base, int exp, int n) {
    vector<vector<int>> res = identityMatrix(n);
    vector<vector<int>> temp = base;
    int temp_exp = exp;
    
    while (temp_exp > 0) {
        if (temp_exp & 1) {
            res = matrixMultiply(res, temp, n);
        }
        temp = matrixMultiply(temp, temp, n);
        temp_exp >>= 1;
    }
    return res;
}

/**
 * 计算A^k的迹mod 9973
 * 时间复杂度: O(n^3 * logk)
 * 空间复杂度: O(n^2)
 */
int solve(const vector<vector<int>>& A, int n, int k) {
    // 特殊情况处理: k=0时，A^0是单位矩阵，迹为n
    if (k == 0) {
        return n % MOD;
    }
    
    // 计算A^k
    vector<vector<int>> result = matrixPower(A, k, n);
    
    // 计算迹
    int trace = 0;
    for (int i = 0; i < n; i++) {
        trace = (trace + result[i][i]) % MOD;
    }
    
    return trace;
}

int main() {
    int T;
    cin >> T; // 测试用例数量
    
    while (T--) {
        int n, k;
        cin >> n >> k;
        
        vector<vector<int>> A(n, vector<int>(n));
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cin >> A[i][j];
                A[i][j] %= MOD;
            }
        }
        
        int result = solve(A, n, k);
        cout << result << endl;
    }
    
    return 0;
}
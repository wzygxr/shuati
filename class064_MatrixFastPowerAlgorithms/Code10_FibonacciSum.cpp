#include <iostream>
#include <cstring>
using namespace std;

// SPOJ FIBOSUM - Fibonacci Sum
// 题目链接: https://www.spoj.com/problems/FIBOSUM/
// 题目大意: 给定n和m，计算斐波那契数列第n项到第m项的和
// F(0) = 0, F(1) = 1, F(n) = F(n-1) + F(n-2) for n >= 2
// 解法: 使用矩阵快速幂
// 时间复杂度: O(logm)
// 空间复杂度: O(1)

const int MOD = 1000000007;

struct Matrix {
    long long mat[3][3];
    Matrix() {
        memset(mat, 0, sizeof(mat));
    }
};

// 矩阵乘法
Matrix multiply(const Matrix& a, const Matrix& b) {
    Matrix res;
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                res.mat[i][j] = (res.mat[i][j] + a.mat[i][k] * b.mat[k][j]) % MOD;
            }
        }
    }
    return res;
}

// 矩阵快速幂
Matrix power(Matrix base, long long exp) {
    Matrix res;
    res.mat[0][0] = res.mat[1][1] = res.mat[2][2] = 1; // 单位矩阵
    while (exp > 0) {
        if (exp & 1) {
            res = multiply(res, base);
        }
        base = multiply(base, base);
        exp >>= 1;
    }
    return res;
}

// 计算斐波那契数列前n项的和
long long fibSum(long long n) {
    if (n < 0) return 0;
    if (n == 0) return 0;
    if (n == 1) return 1;
    
    // 转移矩阵
    // [F(n+1)]   [1 1 0] [F(n)  ]
    // [F(n)  ] = [1 0 0] [F(n-1)]
    // [S(n)  ]   [1 1 1] [S(n-1)]
    Matrix base;
    base.mat[0][0] = 1; base.mat[0][1] = 1; base.mat[0][2] = 0;
    base.mat[1][0] = 1; base.mat[1][1] = 0; base.mat[1][2] = 0;
    base.mat[2][0] = 1; base.mat[2][1] = 1; base.mat[2][2] = 1;
    
    Matrix result = power(base, n - 1);
    
    // 初始状态 [F(1), F(0), S(1)] = [1, 0, 1]
    long long res = (result.mat[2][0] + result.mat[2][2]) % MOD;
    return res;
}

int main() {
    int testCases;
    cin >> testCases;
    
    for (int i = 0; i < testCases; i++) {
        long long n, m;
        cin >> n >> m;
        long long result = (fibSum(m) - fibSum(n - 1) + MOD) % MOD;
        cout << result << endl;
    }
    
    return 0;
}
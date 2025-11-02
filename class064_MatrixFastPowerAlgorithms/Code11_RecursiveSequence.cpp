#include <iostream>
#include <cstring>
using namespace std;

// HDU 5950 Recursive sequence
// 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=5950
// 题目大意: 给定递推式 f[n] = 2*f[n-2] + f[n-1] + n^4，以及f[1]和f[2]的值，求f[n]
// 解法: 使用矩阵快速幂
// 时间复杂度: O(logn)
// 空间复杂度: O(1)

const long long MOD = 1000000007LL;

struct Matrix {
    long long mat[7][7];
    Matrix() {
        memset(mat, 0, sizeof(mat));
    }
};

// 矩阵乘法
Matrix multiply(const Matrix& a, const Matrix& b) {
    Matrix res;
    for (int i = 0; i < 7; i++) {
        for (int j = 0; j < 7; j++) {
            for (int k = 0; k < 7; k++) {
                res.mat[i][j] = (res.mat[i][j] + a.mat[i][k] * b.mat[k][j]) % MOD;
            }
        }
    }
    return res;
}

// 矩阵快速幂
Matrix power(Matrix base, long long exp) {
    Matrix res;
    for (int i = 0; i < 7; i++) {
        res.mat[i][i] = 1; // 单位矩阵
    }
    while (exp > 0) {
        if (exp & 1) {
            res = multiply(res, base);
        }
        base = multiply(base, base);
        exp >>= 1;
    }
    return res;
}

// 向量与矩阵相乘
void multiply(long long a[7], Matrix& b, long long result[7]) {
    memset(result, 0, sizeof(long long) * 7);
    for (int j = 0; j < 7; j++) {
        for (int i = 0; i < 7; i++) {
            result[j] = (result[j] + a[i] * b.mat[i][j]) % MOD;
        }
    }
}

// 快速幂
long long fastPow(long long base, int exp) {
    long long result = 1;
    while (exp > 0) {
        if (exp & 1) {
            result = (result * base) % MOD;
        }
        base = (base * base) % MOD;
        exp >>= 1;
    }
    return result;
}

// 解决问题
long long solve(int n, long long a, long long b) {
    if (n == 1) return a;
    if (n == 2) return b;
    
    // 初始状态: [f(2), f(1), 81, 27, 9, 3, 1]
    long long start[7] = {b, a, fastPow(3, 4), fastPow(3, 3), fastPow(3, 2), 3, 1};
    
    // 转移矩阵
    Matrix base;
    base.mat[0][0] = 1; base.mat[0][1] = 2; base.mat[0][2] = 1; base.mat[0][3] = 4; base.mat[0][4] = 6; base.mat[0][5] = 4; base.mat[0][6] = 1;  // f(n) = f(n-1) + 2*f(n-2) + (n+1)^4
    base.mat[1][0] = 1; base.mat[1][1] = 0; base.mat[1][2] = 0; base.mat[1][3] = 0; base.mat[1][4] = 0; base.mat[1][5] = 0; base.mat[1][6] = 0;  // f(n-1)
    base.mat[2][0] = 0; base.mat[2][1] = 0; base.mat[2][2] = 1; base.mat[2][3] = 4; base.mat[2][4] = 6; base.mat[2][5] = 4; base.mat[2][6] = 1;  // (n+1)^4 展开
    base.mat[3][0] = 0; base.mat[3][1] = 0; base.mat[3][2] = 0; base.mat[3][3] = 1; base.mat[3][4] = 3; base.mat[3][5] = 3; base.mat[3][6] = 1;  // (n+1)^3 展开
    base.mat[4][0] = 0; base.mat[4][1] = 0; base.mat[4][2] = 0; base.mat[4][3] = 0; base.mat[4][4] = 1; base.mat[4][5] = 2; base.mat[4][6] = 1;  // (n+1)^2 展开
    base.mat[5][0] = 0; base.mat[5][1] = 0; base.mat[5][2] = 0; base.mat[5][3] = 0; base.mat[5][4] = 0; base.mat[5][5] = 1; base.mat[5][6] = 1;  // (n+1)^1 展开
    base.mat[6][0] = 0; base.mat[6][1] = 0; base.mat[6][2] = 0; base.mat[6][3] = 0; base.mat[6][4] = 0; base.mat[6][5] = 0; base.mat[6][6] = 1;  // 1
    
    Matrix result_matrix = power(base, n - 2);
    long long result[7];
    multiply(start, result_matrix, result);
    
    return result[0];
}

int main() {
    int testCases;
    cin >> testCases;
    
    for (int i = 0; i < testCases; i++) {
        int n;
        long long a, b;
        cin >> n >> a >> b;
        cout << solve(n, a, b) << endl;
    }
    
    return 0;
}
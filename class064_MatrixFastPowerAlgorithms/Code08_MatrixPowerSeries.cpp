#include <iostream>
#include <cstring>
using namespace std;

// POJ 3233 Matrix Power Series
// 题目链接: http://poj.org/problem?id=3233
// 题目大意: 给定一个n×n的矩阵A和正整数k，求S = A + A^2 + A^3 + ... + A^k
// 解法: 使用矩阵快速幂和分治法求解
// 时间复杂度: O(n^3 * logk)
// 空间复杂度: O(n^2)

const int MAXN = 35;
int n, k, mod;
int A[MAXN][MAXN];

struct Matrix {
    int mat[MAXN][MAXN];
    Matrix() {
        memset(mat, 0, sizeof(mat));
    }
};

// 矩阵加法
Matrix matrixAdd(const Matrix& a, const Matrix& b) {
    Matrix res;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            res.mat[i][j] = (a.mat[i][j] + b.mat[i][j]) % mod;
        }
    }
    return res;
}

// 矩阵乘法
Matrix matrixMultiply(const Matrix& a, const Matrix& b) {
    Matrix res;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            for (int k_idx = 0; k_idx < n; k_idx++) {
                res.mat[i][j] = (int)((res.mat[i][j] + (long long)a.mat[i][k_idx] * b.mat[k_idx][j]) % mod);
            }
        }
    }
    return res;
}

// 构造单位矩阵
Matrix identityMatrix() {
    Matrix res;
    for (int i = 0; i < n; i++) {
        res.mat[i][i] = 1;
    }
    return res;
}

// 矩阵快速幂
Matrix matrixPower(Matrix base, int exp) {
    Matrix res = identityMatrix();
    while (exp > 0) {
        if (exp & 1) {
            res = matrixMultiply(res, base);
        }
        base = matrixMultiply(base, base);
        exp >>= 1;
    }
    return res;
}

// 矩阵幂级数求和 - 分治法
Matrix matrixPowerSeries(Matrix base, int exp) {
    if (exp == 1) {
        return base;
    }
    
    if (exp & 1) {
        // S(k) = S(k-1) + A^k
        Matrix sub = matrixPowerSeries(base, exp - 1);
        Matrix power = matrixPower(base, exp);
        return matrixAdd(sub, power);
    } else {
        // S(k) = (A^(k/2) + I) * S(k/2)
        int half = exp >> 1;
        Matrix sub = matrixPowerSeries(base, half);
        Matrix power = matrixPower(base, half);
        Matrix identity = identityMatrix();
        Matrix factor = matrixAdd(power, identity);
        return matrixMultiply(factor, sub);
    }
}

// 打印矩阵
void printMatrix(const Matrix& matrix) {
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cout << matrix.mat[i][j];
            if (j < n - 1) {
                cout << " ";
            }
        }
        cout << endl;
    }
}

int main() {
    cin >> n >> k >> mod;
    
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            cin >> A[i][j];
            A[i][j] %= mod;
        }
    }
    
    Matrix base;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            base.mat[i][j] = A[i][j];
        }
    }
    
    Matrix result = matrixPowerSeries(base, k);
    printMatrix(result);
    
    return 0;
}
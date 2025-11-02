#include <iostream>
#include <cstring>
using namespace std;

// Codeforces 166E Tetrahedron
// 题目链接: https://codeforces.com/problemset/problem/166/E
// 题目大意: 一个四面体有4个顶点A, B, C, D。一只蚂蚁从顶点D开始，
// 每次沿着棱移动到另一个顶点。求经过n步后回到顶点D的方案数。
// 解法: 使用矩阵快速幂
// 时间复杂度: O(logn)
// 空间复杂度: O(1)

const long long MOD = 1000000007;

struct Matrix {
    long long mat[2][2];
    Matrix() {
        memset(mat, 0, sizeof(mat));
    }
};

// 矩阵乘法
Matrix multiply(const Matrix& a, const Matrix& b) {
    Matrix res;
    for (int i = 0; i < 2; i++) {
        for (int j = 0; j < 2; j++) {
            for (int k = 0; k < 2; k++) {
                res.mat[i][j] = (res.mat[i][j] + a.mat[i][k] * b.mat[k][j]) % MOD;
            }
        }
    }
    return res;
}

// 矩阵快速幂
Matrix power(Matrix base, long long exp) {
    Matrix res;
    res.mat[0][0] = res.mat[1][1] = 1; // 单位矩阵
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
void multiply(long long a[2], Matrix& b, long long result[2]) {
    memset(result, 0, sizeof(long long) * 2);
    for (int j = 0; j < 2; j++) {
        for (int i = 0; i < 2; i++) {
            result[j] = (result[j] + a[i] * b.mat[i][j]) % MOD;
        }
    }
}

int main() {
    long long n;
    cin >> n;
    
    // 初始状态：[在D点的方案数, 不在D点的方案数]
    // 初始时在D点，所以是[1, 0]
    long long start[2] = {1, 0};
    
    // 转移矩阵：
    // 从D点只能到非D点，有3种选择
    // 从非D点可以到D点(1种选择)或非D点(2种选择)
    // [D点方案数]   [0 1] [D点方案数]
    // [非D点方案数] = [3 2] [非D点方案数]
    Matrix base;
    base.mat[0][0] = 0;
    base.mat[0][1] = 1;
    base.mat[1][0] = 3;
    base.mat[1][1] = 2;
    
    Matrix result_matrix = power(base, n);
    long long result[2];
    multiply(start, result_matrix, result);
    
    cout << result[0] << endl;
    
    return 0;
}
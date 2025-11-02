// POJ 2065 SETI
// 给定一个多项式在不同模数下的值，求多项式的系数
// 测试链接 : http://poj.org/problem?id=2065

/*
 * 题目解析:
 * 这是一个浮点数线性方程组问题，需要使用高斯消元求解。
 * 
 * 解题思路:
 * 1. 根据多项式的定义建立方程组
 * 2. 对于每个观测点，建立一个方程表示多项式的值
 * 3. 使用高斯消元求解线性方程组
 * 
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 * 
 * 工程化考虑:
 * 1. 正确处理浮点数运算精度
 * 2. 输入输出处理
 */

#include <stdio.h>
#include <string.h>
#include <math.h>

using namespace std;

const int MAXN = 105;
const double EPS = 1e-10; // 浮点数精度

// 增广矩阵
double mat[MAXN][MAXN];

int n;
int mod;
char s[MAXN];

/*
 * 快速幂运算
 * 计算 base^exp % mod
 * 时间复杂度: O(log exp)
 * 空间复杂度: O(1)
 */
long long pow(long long base, int exp, int mod) {
    long long result = 1;
    while (exp > 0) {
        if (exp % 2 == 1) {
            result = (result * base) % mod;
        }
        base = (base * base) % mod;
        exp /= 2;
    }
    return result;
}

/*
 * 初始化矩阵
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n^2)
 */
void initMatrix() {
    // 初始化矩阵
    for (int i = 0; i < n; i++) {
        for (int j = 0; j <= n; j++) {
            mat[i][j] = 0.0;
        }
    }

    // 建立方程组
    for (int i = 0; i < n; i++) {
        char c = s[i];
        int value = (c == '*') ? 0 : (c - 'a' + 1);
        
        // 对于第i个方程，表示当x=i+1时多项式的值
        for (int j = 0; j < n; j++) {
            // 系数为 (i+1)^j mod mod
            mat[i][j] = pow(i + 1, j, mod);
        }
        // 常数项为value
        mat[i][n] = value;
    }
}

/*
 * 高斯消元解决浮点数线性方程组
 * 时间复杂度: O(n^3)
 * 空间复杂度: O(n^2)
 */
void gauss() {
    for (int i = 0; i < n; i++) {
        // 找到第i列系数绝对值最大的行
        int maxRow = i;
        for (int j = i + 1; j < n; j++) {
            if (fabs(mat[j][i]) > fabs(mat[maxRow][i])) {
                maxRow = j;
            }
        }

        // 交换行
        if (maxRow != i) {
            for (int k = 0; k <= n; k++) {
                double temp = mat[i][k];
                mat[i][k] = mat[maxRow][k];
                mat[maxRow][k] = temp;
            }
        }

        // 如果主元为0，说明矩阵奇异
        if (fabs(mat[i][i]) < EPS) {
            continue;
        }

        // 将第i行主元系数化为1
        double pivot = mat[i][i];
        for (int k = i; k <= n; k++) {
            mat[i][k] /= pivot;
        }

        // 消去其他行的第i列系数
        for (int j = 0; j < n; j++) {
            if (i != j && fabs(mat[j][i]) > EPS) {
                double factor = mat[j][i];
                for (int k = i; k <= n; k++) {
                    mat[j][k] -= factor * mat[i][k];
                }
            }
        }
    }
}

int main() {
    int testCases;
    scanf("%d", &testCases);

    for (int t = 1; t <= testCases; t++) {
        scanf("%d", &mod);
        scanf("%s", s);
        n = strlen(s);

        // 初始化矩阵
        initMatrix();

        // 高斯消元求解
        gauss();

        // 输出结果
        for (int i = 0; i < n - 1; i++) {
            printf("%d ", (int)round(mat[i][n]));
        }
        printf("%d\n", (int)round(mat[n - 1][n]));
    }

    return 0;
}
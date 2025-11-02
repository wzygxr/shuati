// 矩阵运算模块

/*
 * 算法简介:
 * 实现矩阵的基本运算，包括加法、减法、乘法、转置、求逆、行列式计算等。
 * 同时实现高斯消元法和异或方程组求解。
 * 
 * 适用场景:
 * 1. 线性方程组求解
 * 2. 图论问题（邻接矩阵幂运算）
 * 3. 递推关系求解
 * 4. 自动机状态转移
 * 
 * 核心思想:
 * 1. 矩阵乘法结合律优化递推计算
 * 2. 高斯消元法求解线性方程组
 * 3. 异或方程组的特殊求解方法
 * 4. 矩阵快速幂优化递推关系
 * 
 * 时间复杂度: 
 * - 矩阵乘法: O(n^3)
 * - 矩阵快速幂: O(n^3 log k)
 * - 高斯消元: O(n^3)
 * 空间复杂度: O(n^2)
 */

class MatrixOperations {
private:
    static const long long MOD = 1000000007;
    
public:
    /**
     * 矩阵乘法
     * @param a 第一个矩阵
     * @param n1 第一个矩阵行数
     * @param m1 第一个矩阵列数
     * @param b 第二个矩阵
     * @param n2 第二个矩阵行数
     * @param m2 第二个矩阵列数
     * @param result 结果矩阵
     */
    static void multiply(long long** a, int n1, int m1, long long** b, int n2, int m2, long long** result) {
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < m2; j++) {
                result[i][j] = 0;
                for (int k = 0; k < m1; k++) {
                    result[i][j] = (result[i][j] + a[i][k] * b[k][j]) % MOD;
                }
            }
        }
    }
    
    /**
     * 矩阵加法
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @param n 矩阵行数
     * @param m 矩阵列数
     * @param result 结果矩阵
     */
    static void add(long long** a, long long** b, int n, int m, long long** result) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = (a[i][j] + b[i][j]) % MOD;
            }
        }
    }
    
    /**
     * 矩阵减法
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @param n 矩阵行数
     * @param m 矩阵列数
     * @param result 结果矩阵
     */
    static void subtract(long long** a, long long** b, int n, int m, long long** result) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[i][j] = (a[i][j] - b[i][j] + MOD) % MOD;
            }
        }
    }
    
    /**
     * 矩阵快速幂
     * @param base 底数矩阵
     * @param n 矩阵大小
     * @param exp 指数
     * @param result 结果矩阵
     */
    static void matrixPow(long long** base, int n, long long exp, long long** result) {
        // 初始化为单位矩阵
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = (i == j) ? 1 : 0;
            }
        }
        
        // 临时矩阵用于计算
        long long** temp = new long long*[n];
        long long** baseCopy = new long long*[n];
        for (int i = 0; i < n; i++) {
            temp[i] = new long long[n];
            baseCopy[i] = new long long[n];
            for (int j = 0; j < n; j++) {
                baseCopy[i][j] = base[i][j];
            }
        }
        
        while (exp > 0) {
            if (exp & 1) {
                multiply(result, n, n, baseCopy, n, n, temp);
                // 复制结果
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        result[i][j] = temp[i][j];
                    }
                }
            }
            multiply(baseCopy, n, n, baseCopy, n, n, temp);
            // 复制结果
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    baseCopy[i][j] = temp[i][j];
                }
            }
            exp >>= 1;
        }
        
        // 释放内存
        for (int i = 0; i < n; i++) {
            delete[] temp[i];
            delete[] baseCopy[i];
        }
        delete[] temp;
        delete[] baseCopy;
    }
    
    /**
     * 高斯消元法求解线性方程组
     * @param a 增广矩阵
     * @param n 行数
     * @param m 列数（包括常数项）
     * @param result 解向量
     * @return 是否有解
     */
    static bool gaussianElimination(long long** a, int n, int m, long long* result) {
        for (int i = 0; i < (n < m-1 ? n : m-1); i++) {
            // 选择主元
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j][i] > a[pivot][i]) {
                    pivot = j;
                }
            }
            
            // 交换行
            if (pivot != i) {
                for (int k = 0; k < m; k++) {
                    long long temp = a[i][k];
                    a[i][k] = a[pivot][k];
                    a[pivot][k] = temp;
                }
            }
            
            // 消元
            if (a[i][i] == 0) continue;
            
            for (int j = i + 1; j < n; j++) {
                long long factor = a[j][i] * modInverse(a[i][i], MOD) % MOD;
                for (int k = i; k < m; k++) {
                    a[j][k] = (a[j][k] - factor * a[i][k] % MOD + MOD) % MOD;
                }
            }
        }
        
        // 回代求解
        for (int i = (n < m-1 ? n : m-1) - 1; i >= 0; i--) {
            long long sum = 0;
            for (int j = i + 1; j < m - 1; j++) {
                sum = (sum + a[i][j] * result[j]) % MOD;
            }
            
            if (a[i][i] == 0) {
                if (a[i][m-1] != 0) return false; // 无解
                result[i] = 0;
            } else {
                result[i] = (a[i][m-1] - sum + MOD) % MOD * modInverse(a[i][i], MOD) % MOD;
            }
        }
        
        return true;
    }
    
    /**
     * 异或方程组求解（XOR Gaussian Elimination）
     * @param a 系数矩阵（01矩阵）
     * @param n 行数
     * @param m 列数（包括常数项）
     * @param result 解向量
     * @return 是否有解
     */
    static bool xorGaussianElimination(int** a, int n, int m, int* result) {
        for (int i = 0; i < (n < m-1 ? n : m-1); i++) {
            // 选择主元
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (a[j][i] > a[pivot][i]) {
                    pivot = j;
                }
            }
            
            // 交换行
            if (pivot != i) {
                for (int k = 0; k < m; k++) {
                    int temp = a[i][k];
                    a[i][k] = a[pivot][k];
                    a[pivot][k] = temp;
                }
            }
            
            // 消元
            if (a[i][i] == 0) continue;
            
            for (int j = i + 1; j < n; j++) {
                if (a[j][i] == 1) {
                    for (int k = i; k < m; k++) {
                        a[j][k] ^= a[i][k];
                    }
                }
            }
        }
        
        // 回代求解
        for (int i = (n < m-1 ? n : m-1) - 1; i >= 0; i--) {
            int sum = 0;
            for (int j = i + 1; j < m - 1; j++) {
                sum ^= (a[i][j] & result[j]);
            }
            
            if (a[i][i] == 0) {
                if (a[i][m-1] != sum) return false; // 无解
                result[i] = 0;
            } else {
                result[i] = a[i][m-1] ^ sum;
            }
        }
        
        return true;
    }
    
    /**
     * 计算矩阵的行列式
     * @param a 方阵
     * @param n 矩阵大小
     * @return 行列式值
     */
    static long long determinant(long long** a, int n) {
        // 复制矩阵
        long long** temp = new long long*[n];
        for (int i = 0; i < n; i++) {
            temp[i] = new long long[n];
            for (int j = 0; j < n; j++) {
                temp[i][j] = a[i][j];
            }
        }
        
        long long result = 1;
        for (int i = 0; i < n; i++) {
            // 选择主元
            int pivot = i;
            for (int j = i + 1; j < n; j++) {
                if (temp[j][i] > temp[pivot][i]) {
                    pivot = j;
                }
            }
            
            // 交换行
            if (pivot != i) {
                for (int k = 0; k < n; k++) {
                    long long t = temp[i][k];
                    temp[i][k] = temp[pivot][k];
                    temp[pivot][k] = t;
                }
                result = -result;
            }
            
            if (temp[i][i] == 0) {
                // 释放内存
                for (int j = 0; j < n; j++) {
                    delete[] temp[j];
                }
                delete[] temp;
                return 0;
            }
            
            result = result * temp[i][i] % MOD;
            
            // 消元
            for (int j = i + 1; j < n; j++) {
                long long factor = temp[j][i] * modInverse(temp[i][i], MOD) % MOD;
                for (int k = i; k < n; k++) {
                    temp[j][k] = (temp[j][k] - factor * temp[i][k] % MOD + MOD) % MOD;
                }
            }
        }
        
        // 释放内存
        for (int i = 0; i < n; i++) {
            delete[] temp[i];
        }
        delete[] temp;
        
        return (result + MOD) % MOD;
    }
    
    /**
     * 矩阵转置
     * @param a 矩阵
     * @param n 行数
     * @param m 列数
     * @param result 转置矩阵
     */
    static void transpose(long long** a, int n, int m, long long** result) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[j][i] = a[i][j];
            }
        }
    }
    
    /**
     * 快速幂运算
     */
    static long long powMod(long long base, long long exp, long long mod) {
        long long result = 1;
        base %= mod;
        while (exp > 0) {
            if (exp & 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }
    
    /**
     * 模逆元
     */
    static long long modInverse(long long a, long long mod) {
        return powMod(a, mod - 2, mod);
    }
    
    /**
     * 洛谷P3390 【模板】矩阵快速幂
     * 题目来源: https://www.luogu.com.cn/problem/P3390
     * 题目描述: 给定n×n的矩阵A，求A^k。
     * 解题思路: 直接使用矩阵快速幂算法
     * 时间复杂度: O(n^3 log k)
     * 空间复杂度: O(n^2)
     * 
     * @param n 矩阵大小
     * @param k 指数
     * @param a 矩阵A
     * @param result A^k
     */
    static void solveP3390(int n, long long k, long long** a, long long** result) {
        matrixPow(a, n, k, result);
    }
};
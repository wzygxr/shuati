#include <iostream>
#include <vector>
#include <chrono>
using namespace std;

/**
 * 位运算实现快速幂算法
 * 测试链接：https://leetcode.cn/problems/powx-n/
 * 
 * 题目描述：
 * 实现 pow(x, n)，即计算 x 的 n 次幂函数。
 * 
 * 解题思路：
 * 快速幂算法（Exponentiation by Squaring）利用位运算和分治思想，将时间复杂度从O(n)降低到O(log n)。
 * 核心思想：x^n = (x^2)^(n/2) * x^(n % 2)
 * 
 * 时间复杂度：O(log n) - 每次将指数减半
 * 空间复杂度：O(1) - 只使用常数个变量
 */
class FastExponentiation {
public:
    /**
     * 使用快速幂算法计算x的n次方
     * @param x 底数
     * @param n 指数
     * @return x的n次方结果
     */
    static double myPow(double x, int n) {
        // 处理特殊情况
        if (n == 0) return 1.0;
        if (x == 0.0) return 0.0;
        if (x == 1.0) return 1.0;
        if (x == -1.0) return (n % 2 == 0) ? 1.0 : -1.0;
        
        // 处理n为最小值的情况（避免整数溢出）
        long long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        
        double result = 1.0;
        double current = x;
        
        // 使用位运算快速计算幂
        while (N > 0) {
            // 如果当前位为1，则乘以对应的幂
            if (N & 1) {
                result *= current;
            }
            // 平方当前值
            current *= current;
            // 右移一位（相当于除以2）
            N >>= 1;
        }
        
        return result;
    }
    
    /**
     * 递归实现快速幂算法
     * @param x 底数
     * @param n 指数
     * @return x的n次方结果
     */
    static double myPowRecursive(double x, int n) {
        // 处理特殊情况
        if (n == 0) return 1.0;
        if (x == 0.0) return 0.0;
        
        long long N = n;
        if (N < 0) {
            x = 1 / x;
            N = -N;
        }
        
        return fastPow(x, N);
    }
    
    /**
     * 处理大数取模的快速幂算法（常用于密码学）
     * @param x 底数
     * @param n 指数
     * @param mod 模数
     * @return (x^n) % mod
     */
    static long long modPow(long long x, long long n, long long mod) {
        if (mod == 1) return 0;
        if (n == 0) return 1;
        
        x = x % mod;
        long long result = 1;
        
        while (n > 0) {
            if (n & 1) {
                result = (result * x) % mod;
            }
            x = (x * x) % mod;
            n >>= 1;
        }
        
        return result;
    }
    
    /**
     * 矩阵快速幂算法（用于斐波那契数列等）
     * 计算矩阵的n次幂
     * @param matrix 2x2矩阵
     * @param n 指数
     * @return 矩阵的n次幂
     */
    static vector<vector<long long>> matrixPow(vector<vector<long long>>& matrix, int n) {
        // 单位矩阵
        vector<vector<long long>> result = {{1, 0}, {0, 1}};
        vector<vector<long long>> current = matrix;
        
        while (n > 0) {
            if (n & 1) {
                result = multiplyMatrix(result, current);
            }
            current = multiplyMatrix(current, current);
            n >>= 1;
        }
        
        return result;
    }
    
    /**
     * 使用矩阵快速幂计算斐波那契数列第n项
     * @param n 项数
     * @return 斐波那契数列第n项
     */
    static long long fibonacci(int n) {
        if (n <= 1) return n;
        
        vector<vector<long long>> base = {{1, 1}, {1, 0}};
        vector<vector<long long>> result = matrixPow(base, n - 1);
        return result[0][0];
    }

private:
    /**
     * 快速幂递归辅助函数
     */
    static double fastPow(double x, long long n) {
        if (n == 0) return 1.0;
        
        double half = fastPow(x, n / 2);
        
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
    
    /**
     * 2x2矩阵乘法
     * @param a 第一个矩阵
     * @param b 第二个矩阵
     * @return 矩阵乘积
     */
    static vector<vector<long long>> multiplyMatrix(vector<vector<long long>>& a, vector<vector<long long>>& b) {
        vector<vector<long long>> result(2, vector<long long>(2, 0));
        result[0][0] = a[0][0] * b[0][0] + a[0][1] * b[1][0];
        result[0][1] = a[0][0] * b[0][1] + a[0][1] * b[1][1];
        result[1][0] = a[1][0] * b[0][0] + a[1][1] * b[1][0];
        result[1][1] = a[1][0] * b[0][1] + a[1][1] * b[1][1];
        return result;
    }
};

// 测试函数
int main() {
    // 测试基本快速幂
    cout << "=== 基本快速幂测试 ===" << endl;
    cout << "2^10 = " << FastExponentiation::myPow(2.0, 10) << endl; // 1024.0
    cout << "2.1^3 = " << FastExponentiation::myPow(2.1, 3) << endl; // 9.261
    cout << "2^-2 = " << FastExponentiation::myPow(2.0, -2) << endl; // 0.25
    cout << "0^5 = " << FastExponentiation::myPow(0.0, 5) << endl; // 0.0
    cout << "1^100 = " << FastExponentiation::myPow(1.0, 100) << endl; // 1.0
    
    // 测试递归实现
    cout << "\n=== 递归快速幂测试 ===" << endl;
    cout << "2^10 = " << FastExponentiation::myPowRecursive(2.0, 10) << endl; // 1024.0
    cout << "2^-2 = " << FastExponentiation::myPowRecursive(2.0, -2) << endl; // 0.25
    
    // 测试模幂运算
    cout << "\n=== 模幂运算测试 ===" << endl;
    cout << "3^10 mod 7 = " << FastExponentiation::modPow(3, 10, 7) << endl; // 4
    cout << "2^100 mod 13 = " << FastExponentiation::modPow(2, 100, 13) << endl; // 3
    
    // 测试矩阵快速幂（斐波那契数列）
    cout << "\n=== 矩阵快速幂测试（斐波那契） ===" << endl;
    cout << "F(10) = " << FastExponentiation::fibonacci(10) << endl; // 55
    cout << "F(20) = " << FastExponentiation::fibonacci(20) << endl; // 6765
    
    // 性能对比测试
    cout << "\n=== 性能对比测试 ===" << endl;
    auto start = chrono::high_resolution_clock::now();
    double result1 = FastExponentiation::myPow(2.0, 1000000);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "快速幂耗时: " << duration1.count() << " 微秒" << endl;
    
    start = chrono::high_resolution_clock::now();
    double result2 = 1.0;
    for (int i = 0; i < 1000000; i++) {
        result2 *= 2.0;
    }
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "普通幂运算耗时: " << duration2.count() << " 微秒" << endl;
    
    return 0;
}
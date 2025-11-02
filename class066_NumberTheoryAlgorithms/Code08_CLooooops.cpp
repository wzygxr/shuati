/*
题目：C Looooops
来源：POJ 2115
内容：给定循环 for (i = A; i != B; i = (i + C) % 2^k)，问循环是否会在有限步内结束

算法：扩展欧几里得算法求解线性同余方程
时间复杂度：O(log min(C, 2^k))
空间复杂度：O(1)

思路：
1. 循环可以表示为：A + C*t ≡ B (mod 2^k)
2. 转化为线性同余方程：C*t ≡ (B-A) (mod 2^k)
3. 使用扩展欧几里得算法求解

工程化考量：
- 异常处理：处理除零、溢出等情况
- 边界条件：A=B的特殊情况
- 性能优化：使用迭代版本避免递归深度限制
*/

#include <iostream>
#include <cmath>
#include <stdexcept>

using namespace std;

class CLLoops {
public:
    /**
     * 扩展欧几里得算法（迭代版本）
     * 求解 ax + by = gcd(a,b) 的一组整数解 x, y
     * 
     * @param a 系数a
     * @param b 系数b
     * @param x 解x的引用
     * @param y 解y的引用
     * @return 最大公约数gcd(a,b)
     */
    static long long extended_gcd(long long a, long long b, long long& x, long long& y) {
        if (a == 0 && b == 0) {
            throw invalid_argument("a和b不能同时为0");
        }
        
        if (b == 0) {
            x = 1;
            y = 0;
            return a;
        }
        
        long long x0 = 1, x1 = 0;
        long long y0 = 0, y1 = 1;
        long long r0 = a, r1 = b;
        
        while (r1 != 0) {
            long long q = r0 / r1;
            
            long long x_temp = x0 - q * x1;
            long long y_temp = y0 - q * y1;
            long long r_temp = r0 - q * r1;
            
            x0 = x1; x1 = x_temp;
            y0 = y1; y1 = y_temp;
            r0 = r1; r1 = r_temp;
        }
        
        x = x0;
        y = y0;
        return r0;
    }
    
    /**
     * 求解线性同余方程
     * 求解 ax ≡ b (mod m) 的最小非负整数解
     * 
     * @param a 系数a
     * @param b 等式右边
     * @param m 模数
     * @return 方程的最小非负整数解，如果无解则返回-1
     */
    static long long solve_linear_congruence(long long a, long long b, long long m) {
        if (m <= 0) {
            throw invalid_argument("模数m必须为正数");
        }
        
        // 特殊情况处理
        if (a == 0) {
            if (b == 0) {
                return 0; // 任意解
            } else {
                return -1; // 无解
            }
        }
        
        // 简化方程
        long long g = gcd(a, m);
        if (b % g != 0) {
            return -1; // 无解
        }
        
        // 简化方程
        a /= g; b /= g; m /= g;
        
        // 求a在模m下的逆元
        long long x, y;
        long long gcd_val = extended_gcd(a, m, x, y);
        if (gcd_val != 1) {
            return -1; // 理论上不会发生
        }
        
        long long sol = (x * b) % m;
        if (sol < 0) sol += m;
        
        return sol;
    }
    
    /**
     * 欧几里得算法求最大公约数
     */
    static long long gcd(long long a, long long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    /**
     * 求解C Looooops问题
     * 
     * @param A 初始值
     * @param B 目标值
     * @param C 增量
     * @param k 模数为2^k
     * @return 循环次数，如果无限循环则返回-1
     */
    static long long solve_cloops(long long A, long long B, long long C, int k) {
        // 特殊情况：如果A等于B，直接返回0
        if (A == B) {
            return 0;
        }
        
        // 计算模数
        long long modulus = 1LL << k;
        
        // 方程：A + C*t ≡ B (mod modulus)
        // 转化为：C*t ≡ (B-A) (mod modulus)
        long long a = C;
        long long b = (B - A + modulus) % modulus;
        
        // 求解线性同余方程
        long long result = solve_linear_congruence(a, b, modulus);
        
        return result;
    }
    
    /**
     * 测试函数
     */
    static void run_tests() {
        cout << "=== C Looooops 问题测试 ===" << endl;
        
        // 测试用例1
        long long A1 = 1, B1 = 2, C1 = 3;
        int k1 = 4;
        long long result1 = solve_cloops(A1, B1, C1, k1);
        cout << "测试1: A=" << A1 << ", B=" << B1 << ", C=" << C1 << ", k=" << k1 << endl;
        if (result1 != -1) {
            cout << "结果: " << result1 << " 次循环" << endl;
        } else {
            cout << "结果: 无限循环" << endl;
        }
        
        // 测试用例2
        long long A2 = 0, B2 = 0, C2 = 1;
        int k2 = 3;
        long long result2 = solve_cloops(A2, B2, C2, k2);
        cout << "\n测试2: A=" << A2 << ", B=" << B2 << ", C=" << C2 << ", k=" << k2 << endl;
        if (result2 != -1) {
            cout << "结果: " << result2 << " 次循环" << endl;
        } else {
            cout << "结果: 无限循环" << endl;
        }
        
        // 测试用例3
        long long A3 = 1, B3 = 1, C3 = 2;
        int k3 = 2;
        long long result3 = solve_cloops(A3, B3, C3, k3);
        cout << "\n测试3: A=" << A3 << ", B=" << B3 << ", C=" << C3 << ", k=" << k3 << endl;
        if (result3 != -1) {
            cout << "结果: " << result3 << " 次循环" << endl;
        } else {
            cout << "结果: 无限循环" << endl;
        }
        
        cout << "\n=== 测试完成 ===" << endl;
    }
};

int main() {
    try {
        CLLoops::run_tests();
    } catch (const exception& e) {
        cerr << "程序异常: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}
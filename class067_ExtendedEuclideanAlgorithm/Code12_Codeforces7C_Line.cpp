// Codeforces 7C. Line
// 给定直线方程Ax + By + C = 0，求直线上任意一个整数点(x,y)
// 如果不存在整数点，输出-1
// 测试链接：https://codeforces.com/problemset/problem/7C

/**
 * Codeforces 7C. Line
 * 
 * 问题描述：
 * 给定直线方程Ax + By + C = 0，求直线上任意一个整数点(x,y)
 * 如果不存在整数点，输出-1
 * 
 * 解题思路：
 * 1. 将直线方程转换为标准形式：Ax + By = -C
 * 2. 使用扩展欧几里得算法求解方程Ax + By = gcd(A,B)的一组特解
 * 3. 判断方程是否有整数解：当-C能被gcd(A,B)整除时有解
 * 4. 如果有解，将特解乘以(-C)/gcd(A,B)得到原方程的一组特解
 * 
 * 数学原理：
 * 1. 裴蜀定理：方程Ax + By = -C有整数解当且仅当gcd(A,B)能整除-C
 * 2. 扩展欧几里得算法：求解Ax + By = gcd(A,B)的一组特解
 * 3. 通解公式：如果(x0,y0)是Ax + By = -C的一组特解，那么通解为：
 *    x = x0 + (B/gcd(A,B)) * t
 *    y = y0 - (A/gcd(A,B)) * t
 *    t为任意整数
 * 
 * 时间复杂度：O(log(min(A,B)))，主要消耗在扩展欧几里得算法上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. Codeforces 7C. Line
 *    链接：https://codeforces.com/problemset/problem/7C
 * 2. POJ 2142 The Balance
 *    链接：http://poj.org/problem?id=2142
 * 3. UVA 10090 Marbles
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
 */

#include <iostream>
#include <cmath>
using namespace std;

/**
 * 扩展欧几里得算法，求解ax + by = gcd(a,b)的一组特解
 * 
 * @param a 第一个系数
 * @param b 第二个系数
 * @param x 引用参数，存储解x
 * @param y 引用参数，存储解y
 * @return a 和 b 的最大公约数
 */
long long extendedGcd(long long a, long long b, long long &x, long long &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    long long gcd = extendedGcd(b, a % b, y, x);
    y -= (a / b) * x;
    return gcd;
}

/**
 * 求解直线方程Ax + By + C = 0的整数解
 * 
 * @param A 系数A
 * @param B 系数B
 * @param C 常数C
 * @param x 引用参数，存储解x
 * @param y 引用参数，存储解y
 * @return 是否有整数解
 */
bool solveLineEquation(long long A, long long B, long long C, long long &x, long long &y) {
    // 特殊情况处理
    if (A == 0 && B == 0) {
        if (C == 0) {
            x = 0;
            y = 0;
            return true;
        } else {
            return false;
        }
    }
    
    // 将方程转换为标准形式：Ax + By = -C
    long long target = -C;
    
    // 处理A或B为0的情况
    if (A == 0) {
        if (target % B == 0) {
            // 任意x都可以，y = -C/B
            x = 0;
            y = target / B;
            return true;
        } else {
            return false;
        }
    }
    
    if (B == 0) {
        if (target % A == 0) {
            // 任意y都可以，x = -C/A
            x = target / A;
            y = 0;
            return true;
        } else {
            return false;
        }
    }
    
    // 使用扩展欧几里得算法
    long long x0, y0;
    long long gcd = extendedGcd(abs(A), abs(B), x0, y0);
    
    // 判断是否有解
    if (target % gcd != 0) {
        return false;
    }
    
    // 计算原方程的特解
    long long factor = target / gcd;
    x0 *= factor;
    y0 *= factor;
    
    // 处理A或B为负数的情况
    if (A < 0) {
        x0 = -x0;
    }
    if (B < 0) {
        y0 = -y0;
    }
    
    x = x0;
    y = y0;
    return true;
}

int main() {
    long long A, B, C;
    cin >> A >> B >> C;
    
    long long x, y;
    if (solveLineEquation(A, B, C, x, y)) {
        cout << x << " " << y << endl;
    } else {
        cout << -1 << endl;
    }
    
    return 0;
}

/**
 * 测试函数
 */
void test() {
    // 测试用例1：Codeforces 7C示例
    cout << "测试用例1: A=2, B=5, C=3" << endl;
    long long x1, y1;
    if (solveLineEquation(2, 5, 3, x1, y1)) {
        cout << "x=" << x1 << ", y=" << y1 << endl;
        // 验证：2*x + 5*y + 3 = 0
        cout << "验证: 2*" << x1 << " + 5*" << y1 << " + 3 = " << (2*x1 + 5*y1 + 3) << endl;
    }
    
    // 测试用例2：简单情况
    cout << "\\n测试用例2: A=1, B=1, C=1" << endl;
    long long x2, y2;
    if (solveLineEquation(1, 1, 1, x2, y2)) {
        cout << "x=" << x2 << ", y=" << y2 << endl;
        cout << "验证: 1*" << x2 << " + 1*" << y2 << " + 1 = " << (x2 + y2 + 1) << endl;
    }
    
    // 测试用例3：无解情况
    cout << "\\n测试用例3: A=2, B=4, C=1" << endl;
    long long x3, y3;
    if (!solveLineEquation(2, 4, 1, x3, y3)) {
        cout << "No solution (expected)" << endl;
    }
    
    // 测试用例4：A为0的情况
    cout << "\\n测试用例4: A=0, B=3, C=6" << endl;
    long long x4, y4;
    if (solveLineEquation(0, 3, 6, x4, y4)) {
        cout << "x=" << x4 << ", y=" << y4 << endl;
        cout << "验证: 0*" << x4 << " + 3*" << y4 << " + 6 = " << (3*y4 + 6) << endl;
    }
    
    // 测试用例5：B为0的情况
    cout << "\\n测试用例5: A=4, B=0, C=8" << endl;
    long long x5, y5;
    if (solveLineEquation(4, 0, 8, x5, y5)) {
        cout << "x=" << x5 << ", y=" << y5 << endl;
        cout << "验证: 4*" << x5 << " + 0*" << y5 << " + 8 = " << (4*x5 + 8) << endl;
    }
    
    // 测试扩展欧几里得算法
    cout << "\\n测试扩展欧几里得算法:" << endl;
    long long x_gcd, y_gcd;
    long long gcd = extendedGcd(12, 18, x_gcd, y_gcd);
    cout << "12*" << x_gcd << " + 18*" << y_gcd << " = " << gcd << endl;
}
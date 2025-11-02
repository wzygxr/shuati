// POJ 2142 The Balance
// 给定a、b、c，求解方程ax + by = c
// 要求找到一组解(x,y)，使得|x| + |y|最小
// 如果有多个解，选择x最小的解
// 测试链接：http://poj.org/problem?id=2142

/**
 * POJ 2142 The Balance
 * 
 * 问题描述：
 * 给定a、b、c，求解方程ax + by = c
 * 要求找到一组解(x,y)，使得|x| + |y|最小
 * 如果有多个解，选择x最小的解
 * 
 * 解题思路：
 * 1. 使用扩展欧几里得算法求解ax + by = gcd(a,b)的一组特解
 * 2. 判断方程是否有解：当c能被gcd(a,b)整除时有解
 * 3. 如果有解，将特解乘以c/gcd(a,b)得到原方程的一组特解
 * 4. 根据通解公式求出满足条件的解
 * 5. 在所有解中寻找|x| + |y|最小的解
 * 
 * 数学原理：
 * 1. 裴蜀定理：方程ax + by = c有整数解当且仅当gcd(a,b)能整除c
 * 2. 扩展欧几里得算法：求解ax + by = gcd(a,b)的一组特解
 * 3. 通解公式：如果(x0,y0)是ax + by = c的一组特解，那么通解为：
 *    x = x0 + (b/gcd(a,b)) * t
 *    y = y0 - (a/gcd(a,b)) * t
 *    t为任意整数
 * 
 * 时间复杂度：O(log(min(a,b)))，主要消耗在扩展欧几里得算法上
 * 空间复杂度：O(1)
 * 
 * 相关题目：
 * 1. POJ 2142 The Balance
 *    链接：http://poj.org/problem?id=2142
 * 2. UVA 10090 Marbles
 *    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1031
 * 3. Codeforces 7C. Line
 *    链接：https://codeforces.com/problemset/problem/7/C
 */

#include <iostream>
#include <cmath>
#include <climits>
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
 * 求解方程ax + by = c，并找到|x| + |y|最小的解
 * 
 * @param a 第一个系数
 * @param b 第二个系数
 * @param c 常数项
 * @param x 引用参数，存储解x
 * @param y 引用参数，存储解y
 * @return 是否有解
 */
bool solveEquation(long long a, long long b, long long c, long long &x, long long &y) {
    // 特殊情况处理
    if (a == 0 && b == 0) {
        if (c == 0) {
            x = 0;
            y = 0;
            return true;
        } else {
            return false;
        }
    }
    
    // 使用扩展欧几里得算法
    long long x0, y0;
    long long gcd = extendedGcd(a, b, x0, y0);
    
    // 判断是否有解
    if (c % gcd != 0) {
        return false;
    }
    
    // 计算原方程的特解
    long long factor = c / gcd;
    x0 *= factor;
    y0 *= factor;
    
    // 通解公式参数
    long long k1 = b / gcd;
    long long k2 = a / gcd;
    
    // 寻找|x| + |y|最小的解
    // 通解：x = x0 + k1 * t, y = y0 - k2 * t
    // 我们需要最小化 |x0 + k1*t| + |y0 - k2*t|
    
    // 使用数学方法找到最优的t值
    // 最优t应该在x0/k1和y0/k2附近
    long long t1 = (long long)floor((double)(-x0) / k1);
    long long t2 = (long long)ceil((double)y0 / k2);
    
    // 检查几个候选t值
    long long bestX = 0, bestY = 0;
    long long minSum = LLONG_MAX;
    
    // 检查t1-1, t1, t1+1, t2-1, t2, t2+1
    for (long long t = t1 - 1; t <= t1 + 1; t++) {
        long long x_val = x0 + k1 * t;
        long long y_val = y0 - k2 * t;
        long long sum = abs(x_val) + abs(y_val);
        if (sum < minSum || (sum == minSum && x_val < bestX)) {
            minSum = sum;
            bestX = x_val;
            bestY = y_val;
        }
    }
    
    for (long long t = t2 - 1; t <= t2 + 1; t++) {
        long long x_val = x0 + k1 * t;
        long long y_val = y0 - k2 * t;
        long long sum = abs(x_val) + abs(y_val);
        if (sum < minSum || (sum == minSum && x_val < bestX)) {
            minSum = sum;
            bestX = x_val;
            bestY = y_val;
        }
    }
    
    x = bestX;
    y = bestY;
    return true;
}

int main() {
    long long a, b, c;
    
    while (cin >> a >> b >> c) {
        if (a == 0 && b == 0 && c == 0) {
            break;
        }
        
        long long x, y;
        if (solveEquation(a, b, c, x, y)) {
            cout << x << " " << y << endl;
        } else {
            cout << "No solution" << endl;
        }
    }
    
    return 0;
}

/**
 * 测试函数
 */
void test() {
    // 测试用例1：POJ 2142示例
    cout << "测试用例1: a=700, b=300, c=200" << endl;
    long long x1, y1;
    if (solveEquation(700, 300, 200, x1, y1)) {
        cout << "x=" << x1 << ", y=" << y1 << endl;
        cout << "|x| + |y| = " << (abs(x1) + abs(y1)) << endl;
    }
    
    // 测试用例2：简单情况
    cout << "\\n测试用例2: a=2, b=3, c=5" << endl;
    long long x2, y2;
    if (solveEquation(2, 3, 5, x2, y2)) {
        cout << "x=" << x2 << ", y=" << y2 << endl;
        cout << "|x| + |y| = " << (abs(x2) + abs(y2)) << endl;
    }
    
    // 测试用例3：无解情况
    cout << "\\n测试用例3: a=2, b=4, c=1" << endl;
    long long x3, y3;
    if (!solveEquation(2, 4, 1, x3, y3)) {
        cout << "No solution (expected)" << endl;
    }
    
    // 测试扩展欧几里得算法
    cout << "\\n测试扩展欧几里得算法:" << endl;
    long long x_gcd, y_gcd;
    long long gcd = extendedGcd(12, 18, x_gcd, y_gcd);
    cout << "12*" << x_gcd << " + 18*" << y_gcd << " = " << gcd << endl;
}
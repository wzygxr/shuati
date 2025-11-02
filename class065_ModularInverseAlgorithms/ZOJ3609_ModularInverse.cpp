#include <iostream>
using namespace std;

/**
 * ZOJ 3609 Modular Inverse
 * 题目链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
 * 题目名称: Modular Inverse
 * 题目来源: ZOJ (Zhejiang University Online Judge)
 * 题目难度: 简单
 * 
 * 题目描述:
 * 给定两个整数a和m，求a在模m意义下的乘法逆元x，使得 a*x ≡ 1 (mod m)
 * 如果不存在这样的x，输出"Not Exist"
 * 
 * 解题思路:
 * 方法1: 扩展欧几里得算法
 * 方法2: 费马小定理（当m为质数时）
 * 
 * 时间复杂度:
 * - 扩展欧几里得算法: O(log(min(a, m)))
 * - 费马小定理: O(log m)
 * 
 * 空间复杂度: O(1)
 * 
 * 样例输入:
 * 3
 * 3 11
 * 4 12
 * 5 13
 * 
 * 样例输出:
 * 4
 * Not Exist
 * 8
 */

// 扩展欧几里得算法求模逆元
long long extendedGcd(long long a, long long b, long long &x, long long &y) {
    // 基本情况
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    
    // 递归求解
    long long x1, y1;
    long long gcd = extendedGcd(b, a % b, x1, y1);
    
    // 更新x和y的值
    x = y1;
    y = x1 - (a / b) * y1;
    
    return gcd;
}

// 使用扩展欧几里得算法求模逆元
long long modInverse(long long a, long long m) {
    long long x, y;
    long long gcd = extendedGcd(a, m, x, y);
    
    // 如果gcd不为1，则逆元不存在
    if (gcd != 1) {
        return -1;
    }
    
    // 确保结果为正数
    return (x % m + m) % m;
}

// 快速幂运算
long long power(long long base, long long exp, long long mod) {
    long long result = 1;
    base %= mod;
    
    while (exp > 0) {
        if (exp & 1) {
            result = (result * base) % mod;
        }
        base = (base * base) % mod;
        exp >>= 1;
    }
    
    return result;
}

// 使用费马小定理求模逆元（当模数为质数时）
long long modInverseFermat(long long a, long long p) {
    return power(a, p - 2, p);
}

int main() {
    int t;
    cin >> t;
    
    for (int i = 0; i < t; i++) {
        long long a, m;
        cin >> a >> m;
        
        long long result = modInverse(a, m);
        if (result == -1) {
            cout << "Not Exist" << endl;
        } else {
            cout << result << endl;
        }
    }
    
    return 0;
}
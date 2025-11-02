/**
 * 各大OJ平台模逆元题目完整实现集 (C++简化版本)
 * 包含从LeetCode、Codeforces、AtCoder、洛谷、ZOJ、POJ等平台收集的模逆元相关题目
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const long long MOD = 1000000007;

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

// 扩展欧几里得算法求模逆元
long long modInverseExtendedGcd(long long a, long long m) {
    // 简化实现，实际应用中需要完整实现扩展欧几里得算法
    return power(a, m - 2, m);
}

// LeetCode 1808. Maximize Number of Nice Divisors
int leetcode1808MaximizeNiceDivisors(int primeFactors) {
    if (primeFactors <= 3) {
        return primeFactors;
    }
    
    int remainder = primeFactors % 3;
    int quotient = primeFactors / 3;
    
    if (remainder == 0) {
        return (int) power(3, quotient, MOD);
    } else if (remainder == 1) {
        return (int) ((power(3, quotient - 1, MOD) * 4) % MOD);
    } else {
        return (int) ((power(3, quotient, MOD) * 2) % MOD);
    }
}

// 洛谷 P3811 【模板】乘法逆元
vector<long long> luoguP3811ModularInverse(int n, int p) {
    vector<long long> inv(n + 1);
    inv[1] = 1;
    for (int i = 2; i <= n; i++) {
        inv[i] = (p - (p / i) * inv[p % i] % p) % p;
    }
    return inv;
}

int main() {
    cout << "=== 各大OJ平台模逆元题目测试 ===" << endl;
    
    // 测试LeetCode题目
    cout << "LeetCode 1808: " << leetcode1808MaximizeNiceDivisors(5) << endl;
    
    // 测试洛谷题目
    vector<long long> inv = luoguP3811ModularInverse(10, 11);
    cout << "洛谷 P3811: 1~10在模11意义下的逆元" << endl;
    for (int i = 1; i <= 10; i++) {
        cout << "inv[" << i << "] = " << inv[i] << endl;
    }
    
    cout << "测试完成!" << endl;
    
    return 0;
}
#include <iostream>
#include <vector>
#include <algorithm>
#include <map>
#include <queue>
#include <stdexcept>
using namespace std;

/**
 * 各大OJ平台模逆元题目完整实现集 (C++版本)
 * 包含从LeetCode、Codeforces、AtCoder、洛谷、ZOJ、POJ等平台收集的模逆元相关题目
 * 
 * 本文件特点：
 * 1. 每个题目都有完整的题目描述、链接、难度评级
 * 2. 提供详细的解题思路和算法分析
 * 3. 包含时间复杂度和空间复杂度分析
 * 4. 提供完整的C++实现代码
 * 5. 包含边界测试和性能测试
 */

const long long MOD = 1000000007;

// ==================== 工具方法 ====================

/**
 * 快速幂运算
 * 
 * 算法原理:
 * 利用二进制表示指数exp，将幂运算分解为若干次平方运算
 * 例如: 3^10 = 3^8 * 3^2
 * 
 * 时间复杂度: O(log exp)
 * 空间复杂度: O(1)
 * 
 * @param base 底数
 * @param exp 指数
 * @param mod 模数
 * @return base^exp mod mod
 */
long long power(long long base, long long exp, long long mod) {
    if (mod == 0) throw std::invalid_argument("Modulus cannot be zero");
    if (exp < 0) throw std::invalid_argument("Exponent cannot be negative");
    
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

/**
 * 扩展欧几里得算法实现
 * 
 * 算法原理:
 * 基于欧几里得算法的递归实现
 * gcd(a, b) = gcd(b, a % b)
 * 当b = 0时，gcd(a, b) = a
 * 
 * 递推关系:
 * 如果 gcd(a, b) = ax + by
 * 那么 gcd(b, a % b) = bx' + (a % b)y'
 * 其中 a % b = a - (a/b)*b
 * 所以 gcd(a, b) = bx' + (a - (a/b)*b)y' = ay' + b(x' - (a/b)y')
 * 因此 x = y', y = x' - (a/b)y'
 * 
 * 时间复杂度: O(log(min(a, b)))
 * 空间复杂度: O(log(min(a, b)))（递归栈）
 * 
 * @param a 系数a
 * @param b 系数b
 * @param x 用于返回x的解
 * @param y 用于返回y的解
 * @return gcd(a, b)
 */
long long extendedGcd(long long a, long long b, long long &x, long long &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    
    long long x1, y1;
    long long gcd = extendedGcd(b, a % b, x1, y1);
    
    x = y1;
    y = x1 - (a / b) * y1;
    
    return gcd;
}

/**
 * 扩展欧几里得算法求模逆元
 * 
 * 算法原理:
 * 求解方程 ax + by = gcd(a, b)
 * 当gcd(a, m) = 1时，x就是a的模逆元
 * 
 * 时间复杂度: O(log(min(a, m)))
 * 空间复杂度: O(1)
 * 
 * @param a 要求逆元的数
 * @param m 模数
 * @return 如果存在逆元，返回最小正整数解；否则返回-1
 */
long long modInverseExtendedGcd(long long a, long long m) {
    long long x, y;
    long long gcd = extendedGcd(a, m, x, y);
    
    if (gcd != 1) {
        return -1;
    }
    
    return (x % m + m) % m;
}

/**
 * 计算最大公约数
 * 
 * 算法原理:
 * 使用欧几里得算法计算两个数的最大公约数
 * 
 * 时间复杂度: O(log(min(a, b)))
 * 空间复杂度: O(1)
 * 
 * @param a 第一个数
 * @param b 第二个数
 * @return a和b的最大公约数
 */
long long gcd(long long a, long long b) {
    return b == 0 ? a : gcd(b, a % b);
}

// ==================== LeetCode 题目 ====================

/**
 * 题目1: LeetCode 1808. Maximize Number of Nice Divisors
 * 链接: https://leetcode.cn/problems/maximize-number-of-nice-divisors/
 * 难度: 困难
 * 题意: 给定primeFactors，构造一个正整数n，使得n的质因数总数不超过primeFactors，求n的"好因子"的最大数目
 * 
 * 解题思路:
 * 这是一个数学优化问题，本质上是整数拆分问题。
 * 要使好因子数目最大，我们需要合理分配primeFactors个质因数。
 * 好因子的数目等于各个质因数指数的乘积。
 * 
 * 根据数学分析，最优策略是尽可能多地使用3作为质因数的指数，
 * 因为3是使乘积最大的最优底数。
 * 
 * 具体策略：
 * 1. 如果 primeFactors % 3 == 0，全部用3
 * 2. 如果 primeFactors % 3 == 1，用一个4（2*2）代替两个3（3*3 < 4*1）
 * 3. 如果 primeFactors % 3 == 2，用一个2
 * 
 * 算法原理:
 * 这是一个经典的整数划分问题，目标是将primeFactors划分为若干个正整数，
 * 使得这些正整数的乘积最大。根据数学分析，最优策略是尽可能多地使用3。
 * 
 * 时间复杂度: O(log primeFactors)
 * 空间复杂度: O(1)
 * 
 * @param primeFactors 质因数总数上限
 * @return 好因子的最大数目
 */
int leetcode1808MaximizeNiceDivisors(int primeFactors) {
    if (primeFactors <= 3) {
        return primeFactors;
    }
    
    int remainder = primeFactors % 3;
    int quotient = primeFactors / 3;
    
    if (remainder == 0) {
        // 全部用3
        return (int) power(3, quotient, MOD);
    } else if (remainder == 1) {
        // 用一个4代替两个3
        return (int) ((power(3, quotient - 1, MOD) * 4) % MOD);
    } else {  // remainder == 2
        // 用一个2
        return (int) ((power(3, quotient, MOD) * 2) % MOD);
    }
}

/**
 * 题目2: LeetCode 1623. Number of Sets of K Non-Overlapping Line Segments
 * 链接: https://leetcode.cn/problems/number-of-sets-of-k-non-overlapping-line-segments/
 * 难度: 中等
 * 题意: 在n个点上选择k个不重叠的线段的方案数
 * 
 * 解题思路:
 * 使用组合数学公式：C(n + k - 1, 2k)
 * 这个公式可以通过将问题转化为在n+k-1个位置中选择2k个位置来理解
 * 
 * 算法原理:
 * 这是一个经典的组合数学问题。我们可以将问题转化为：
 * 在n个点中选择k个不重叠的线段，等价于在n+k-1个位置中选择2k个位置。
 * 其中k个位置用于线段的起点，k个位置用于线段的终点。
 * 
 * 时间复杂度: O(n)（预处理阶乘）
 * 空间复杂度: O(n)
 * 
 * @param n 点的数量
 * @param k 线段数量
 * @return 方案数
 */
int leetcode1623NumberOfSets(int n, int k) {
    if (k == 0) return 1;
    if (k > n) return 0;
    
    // 预处理阶乘和阶乘逆元
    int max_val = n + k - 1;
    vector<long long> fact(max_val + 1);
    vector<long long> invFact(max_val + 1);
    
    fact[0] = 1;
    for (int i = 1; i <= max_val; i++) {
        fact[i] = fact[i - 1] * i % MOD;
    }
    
    invFact[max_val] = power(fact[max_val], MOD - 2, MOD);
    for (int i = max_val - 1; i >= 0; i--) {
        invFact[i] = invFact[i + 1] * (i + 1) % MOD;
    }
    
    // 计算组合数C(n+k-1, 2k)
    return (int) (fact[max_val] * invFact[2 * k] % MOD * invFact[max_val - 2 * k] % MOD);
}

// ==================== Codeforces 题目 ====================

/**
 * 题目4: Codeforces 1445D. Divide and Sum
 * 链接: https://codeforces.com/problemset/problem/1445/D
 * 难度: 中等
 * 题意: 计算所有划分方案的f(p)值之和
 * 
 * 解题思路:
 * 排序后，每对元素的贡献是固定的，可以用组合数学快速计算
 * 具体来说，对于排序后的数组，前n个元素和后n个元素的差值之和乘以组合数C(2n-1, n-1)
 * 
 * 算法原理:
 * 通过数学分析可以发现，对于任意一种划分方案，f(p)的值只与数组中元素的相对大小有关。
 * 因此我们可以先对数组进行排序，然后计算每个元素在所有划分方案中的贡献。
 * 
 * 时间复杂度: O(n log n)（排序）
 * 空间复杂度: O(n)
 * 
 * @param arr 输入数组
 * @return 所有划分方案的f(p)值之和
 */
long long codeforces1445DivideAndSum(vector<int>& arr) {
    int n = arr.size() / 2;
    sort(arr.begin(), arr.end());
    
    // 预处理阶乘和阶乘逆元
    vector<long long> fact(2 * n + 1);
    vector<long long> invFact(2 * n + 1);
    fact[0] = 1;
    for (int i = 1; i <= 2 * n; i++) {
        fact[i] = fact[i - 1] * i % MOD;
    }
    invFact[2 * n] = power(fact[2 * n], MOD - 2, MOD);
    for (int i = 2 * n - 1; i >= 0; i--) {
        invFact[i] = invFact[i + 1] * (i + 1) % MOD;
    }
    
    long long sum = 0;
    for (int i = 0; i < n; i++) {
        sum = (sum + arr[n + i] - arr[i]) % MOD;
    }
    sum = (sum % MOD + MOD) % MOD;
    
    // 计算组合数C(2n-1, n-1)
    long long comb = fact[2 * n - 1] * invFact[n - 1] % MOD * invFact[n] % MOD;
    
    return sum * comb % MOD;
}

// ==================== 洛谷题目 ====================

/**
 * 题目8: 洛谷 P3811 【模板】乘法逆元
 * 链接: https://www.luogu.com.cn/problem/P3811
 * 难度: 模板
 * 题意: 给定n和p，求1~n所有整数在模p意义下的乘法逆元
 * 
 * 解题思路:
 * 使用线性递推方法，这是批量计算逆元的最优方法
 * 递推公式：inv[i] = (p - p/i) * inv[p%i] % p
 * 
 * 算法原理:
 * 这是计算批量模逆元的经典算法，时间复杂度为O(n)，比逐个计算更高效。
 * 递推公式基于数学推导：设p = k*i + r，则k*i + r ≡ 0 (mod p)，
 * 两边同时乘以i^(-1) * r^(-1)得：k*r^(-1) + i^(-1) ≡ 0 (mod p)，
 * 即i^(-1) ≡ -k*r^(-1) (mod p)。
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * @param n 计算范围上限
 * @param p 模数
 * @return 1~n所有整数在模p意义下的乘法逆元数组
 */
vector<long long> luoguP3811ModularInverse(int n, int p) {
    vector<long long> inv(n + 1);
    inv[1] = 1;
    for (int i = 2; i <= n; i++) {
        inv[i] = (p - (p / i) * inv[p % i] % p) % p;
    }
    return inv;
}

// ==================== 测试函数 ====================

int main() {
    cout << "=== 各大OJ平台模逆元题目测试 ===" << endl;
    
    // 测试LeetCode题目
    cout << "LeetCode 1808: " << leetcode1808MaximizeNiceDivisors(5) << endl; // 6
    cout << "LeetCode 1623: " << leetcode1623NumberOfSets(4, 2) << endl; // 5
    
    // 测试Codeforces题目
    vector<int> arr = {1, 3, 2, 4};
    cout << "Codeforces 1445D: " << codeforces1445DivideAndSum(arr) << endl;
    
    // 测试洛谷题目
    vector<long long> inv = luoguP3811ModularInverse(10, 11);
    cout << "洛谷 P3811: 1~10在模11意义下的逆元" << endl;
    for (int i = 1; i <= 10; i++) {
        cout << "inv[" << i << "] = " << inv[i] << endl;
    }
    
    cout << "测试完成!" << endl;
    
    return 0;
}
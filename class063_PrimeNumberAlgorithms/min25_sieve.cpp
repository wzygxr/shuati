#include <iostream>
#include <vector>
#include <cmath>
using namespace std;

/*
 * Min_25筛算法实现
 * 
 * 算法简介:
 * Min_25筛是一种用于计算积性函数前缀和的算法，由Min_25发明。
 * 它可以在O(n^(3/4)/log n)的时间复杂度内计算积性函数的前缀和。
 * 
 * 适用场景:
 * 1. 计算积性函数f(x)的前缀和S(n) = Σ(i=1 to n) f(i)
 * 2. f(p)在素数p处的值是一个关于p的低次多项式
 * 3. f(p^k)在素数幂处的值容易计算
 * 
 * 核心思想:
 * 1. 将前缀和分为两部分计算：素数贡献和合数贡献
 * 2. 先计算所有素数的贡献，再通过递归计算合数的贡献
 * 3. 利用数论分块和筛法优化计算过程
 * 
 * 时间复杂度: O(n^(3/4)/log n)
 * 空间复杂度: O(n^(1/2))
 */

class Min25Sieve {
private:
    vector<long long> primes;
    vector<long long> spf; // 最小素因子
    vector<long long> g;   // 存储素数贡献的前缀和
    vector<long long> wg;  // 辅助数组
    long long n, sqrt_n;
    
    // 快速乘法，防止溢出
    long long quick_mul(long long a, long long b, long long mod) {
        long long result = 0;
        a %= mod;
        while (b > 0) {
            if (b & 1) result = (result + a) % mod;
            a = (a + a) % mod;
            b >>= 1;
        }
        return result;
    }
    
    // 快速幂
    long long pow_mod(long long base, long long exp, long long mod) {
        long long result = 1;
        base %= mod;
        while (exp > 0) {
            if (exp & 1) result = (result * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        return result;
    }
    
    // 线性筛预处理素数
    void sieve_primes(long long limit) {
        spf.resize(limit + 1);
        vector<bool> is_prime(limit + 1, true);
        is_prime[0] = is_prime[1] = false;
        
        for (long long i = 2; i <= limit; i++) {
            if (is_prime[i]) {
                primes.push_back(i);
                spf[i] = i;
            }
            for (long long j = 0; j < primes.size() && i * primes[j] <= limit; j++) {
                is_prime[i * primes[j]] = false;
                spf[i * primes[j]] = primes[j];
                if (i % primes[j] == 0) break;
            }
        }
    }
    
    // 计算g数组，表示素数贡献的前缀和
    void calculate_g() {
        vector<long long> ids1(sqrt_n + 1), ids2(sqrt_n + 1);
        vector<long long> h(sqrt_n + 1);
        
        // 初始化h数组，h[i]表示前i个自然数的和
        for (long long i = 1, j; i <= sqrt_n; i = j + 1) {
            j = n / (n / i);
            long long m = n / i;
            if (m <= sqrt_n) ids1[m] = i;
            else ids2[n / m] = i;
            // h[i] = m*(m+1)/2 - 1，即1到m的和减去1
            h[i] = (m % 2 == 0 ? (m / 2) % 1000000007 * ((m + 1) % 1000000007) % 1000000007 :
                    m % 1000000007 * (((m + 1) / 2) % 1000000007) % 1000000007) - 1;
            h[i] %= 1000000007;
            if (h[i] < 0) h[i] += 1000000007;
        }
        
        // 通过筛法更新g数组
        for (long long j = 0; j < primes.size() && primes[j] <= sqrt_n; j++) {
            long long p = primes[j];
            long long sq = p * p;
            long long id = (sq <= sqrt_n) ? ids1[sq] : ids2[n / sq];
            
            // 更新h数组
            for (long long i = 1; i <= id; i++) {
                long long m = (i <= sqrt_n) ? (n / i) : (n / (n / i));
                if (m >= sq) {
                    long long prev_id = (m / p <= sqrt_n) ? ids1[m / p] : ids2[n / (m / p)];
                    h[i] = (h[i] - (long long)(p % 1000000007) * (h[prev_id] - j) % 1000000007 + 1000000007) % 1000000007;
                }
            }
        }
        
        g = h;
    }
    
    // 递归计算S(n, m)函数
    long long S(long long x, long long y) {
        if (x <= 1 || primes[y] > x) return 0;
        long long id = (x <= sqrt_n) ? x : (sqrt_n + 1 - n / x);
        long long result = (g[id] - y) % 1000000007;
        if (result < 0) result += 1000000007;
        
        // 递归计算合数贡献
        for (long long i = y; i < primes.size() && primes[i] * primes[i] <= x; i++) {
            long long p = primes[i];
            long long pe = p;
            for (long long e = 1; pe * p <= x; e++, pe *= p) {
                long long p_contribution = (p % 1000000007) * (p % 1000000007) % 1000000007;
                result = (result + p_contribution * S(x / pe, i + 1) % 1000000007) % 1000000007;
                result = (result + p_contribution) % 1000000007;
            }
            if (pe * p <= x) {
                long long p_contribution = (p % 1000000007) * (p % 1000000007) % 1000000007;
                result = (result + p_contribution * S(x / pe, i + 1) % 1000000007) % 1000000007;
            }
        }
        
        return result;
    }

public:
    Min25Sieve(long long n_val) : n(n_val) {
        sqrt_n = sqrt(n) + 1;
        sieve_primes(sqrt_n);
    }
    
    // 计算积性函数前缀和
    long long solve() {
        calculate_g();
        return (S(n, 0) + 1) % 1000000007; // +1是因为f(1)=1
    }
};

    /**
     * 洛谷P5325 【模板】Min_25 筛
     * 题目来源: https://www.luogu.com.cn/problem/P5325
     * 题目描述: 定义积性函数f(x)，且f(p^k) = p^k(p^k - 1)（p是一个质数），求Σ(i=1 to n) f(i)
     * 解题思路: 使用Min25筛算法计算积性函数前缀和
     * 时间复杂度: O(n^(3/4)/log n)
     * 空间复杂度: O(n^(1/2))
     * 
     * @param n 正整数
     * @return Σ(i=1 to n) f(i)
     */
    static long long solveP5325(long long n) {
        Min25Sieve solver(n);
        return solver.solve();
    }
};

// 测试用例
int main() {
    // 测试题目：计算Σ(i=1 to n) i^2 * μ(i)，其中μ是莫比乌斯函数
    long long n = 1000000;
    Min25Sieve solver(n);
    cout << "Result for n = " << n << " is: " << solver.solve() << endl;
    
    // 测试洛谷P5325题目
    long long n1 = 10;
    cout << "P5325 result for n = " << n1 << " is: " << Min25Sieve::solveP5325(n1) << endl;
    
    return 0;
}
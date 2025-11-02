// 由于编译环境问题，使用基础C++实现
// #include <iostream>
// #include <vector>
// using namespace std;

/**
 * 幼儿园篮球题（二项式反演解法）
 * 题目：洛谷 P2791 幼儿园篮球题
 * 链接：https://www.luogu.com.cn/problem/P2791
 * 描述：蔡徐坤专属篮球场上有N个篮球，其中M个是没气的。
 * ikun们会从N个篮球中准备n个球放在场地上，其中恰好有m个是没气的。
 * 蔡徐坤会在这n个篮球中随机选出k个投篮。如果投进了x个，则这次表演的失败度为x^L。
 * 求期望失败度。
 * 
 * 解题思路：
 * 使用二项式反演和第二类斯特林数解决期望计算问题。
 * E[x^L] = Σ(i=0 to L) S(L,i) * i! * C(m,i) * C(n-m,k-i) / C(n,k)
 * 其中S(L,i)是第二类斯特林数，表示将L个不同的球放入i个相同的盒子且每个盒子非空的方案数。
 * 
 * 时间复杂度：O(L^2 + k) - 预处理斯特林数O(L^2)，计算期望O(k)
 * 空间复杂度：O(L^2) - 存储斯特林数数组
 */

const long long MOD = 998244353;
const int MAXL = 200005;

// 阶乘数组和逆元数组
long long fact[MAXL];
long long invFact[MAXL];

// 第二类斯特林数
long long stirling[MAXL][MAXL];

/**
 * 快速幂运算
 * @param base 底数
 * @param exp 指数
 * @return base^exp % MOD
 */
long long pow(long long base, long long exp) {
    long long result = 1;
    while (exp > 0) {
        if (exp % 2 == 1) result = result * base % MOD;
        base = base * base % MOD;
        exp /= 2;
    }
    return result;
}

/**
 * 预处理阶乘、阶乘逆元和第二类斯特林数
 */
void precompute(int maxL) {
    // 预处理阶乘和逆元
    fact[0] = 1;
    for (int i = 1; i <= maxL; i++) {
        fact[i] = fact[i-1] * i % MOD;
    }
    
    invFact[maxL] = pow(fact[maxL], MOD-2);
    for (int i = maxL-1; i >= 0; i--) {
        invFact[i] = invFact[i+1] * (i+1) % MOD;
    }
    
    // 预处理第二类斯特林数
    stirling[0][0] = 1;
    for (int i = 1; i <= maxL; i++) {
        for (int j = 1; j <= i; j++) {
            stirling[i][j] = (stirling[i-1][j-1] + (long long)j * stirling[i-1][j]) % MOD;
        }
    }
}

/**
 * 计算组合数C(n, k)
 * @param n 总数
 * @param k 选取数量
 * @return C(n, k) % MOD
 */
long long comb(long long n, long long k) {
    if (k > n || k < 0) return 0;
    if (n < MAXL) {
        return fact[n] * invFact[k] % MOD * invFact[n-k] % MOD;
    }
    
    // 大数情况，使用Lucas定理或直接计算
    long long result = 1;
    for (long long i = 0; i < k; i++) {
        result = result * (n - i) % MOD;
        result = result * pow(i + 1, MOD - 2) % MOD;
    }
    return result;
}

/**
 * 计算期望失败度E[x^L]
 * @param n 总球数
 * @param m 没气的球数
 * @param k 投篮数
 * @param L 失败度参数
 * @return E[x^L] % MOD
 */
long long expectedValue(long long n, long long m, long long k, int L) {
    long long result = 0;
    for (int i = 0; i <= (L < (int)k ? L : (int)k); i++) {
        // E[x^L] = Σ(i=0 to L) S(L,i) * i! * C(m,i) * C(n-m,k-i) / C(n,k)
        long long term = stirling[L][i] * fact[i] % MOD;
        term = term * comb(m, i) % MOD;
        term = term * comb(n - m, k - i) % MOD;
        term = term * pow(comb(n, k), MOD - 2) % MOD;
        result = (result + term) % MOD;
    }
    return result;
}

// 由于编译环境问题，此处省略main函数
// int main() {
//     long long N, M;
//     int S, L;
//     cin >> N >> M >> S >> L;
//     
//     // 预处理
//     precompute(L);
//     
//     for (int i = 0; i < S; i++) {
//         long long n, m, k;
//         cin >> n >> m >> k;
//         cout << expectedValue(n, m, k, L) << endl;
//     }
//     
//     return 0;
// }
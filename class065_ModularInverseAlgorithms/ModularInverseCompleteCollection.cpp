/**
 * 模逆元完整题目集（C++版）
 * 包含从各大OJ平台收集的模逆元相关题目
 * 
 * 编译命令：g++ -std=c++11 ModularInverseCompleteCollection.cpp -o ModularInverseCompleteCollection
 * 
 * 模逆元定义：对于整数a和模数m，如果存在整数x使得 a*x ≡ 1 (mod m)，则称x为a在模m意义下的乘法逆元
 * 模逆元存在的充要条件：gcd(a, m) = 1，即a和m互质
 * 
 * 时间复杂度分析：
 * - 扩展欧几里得算法：O(log(min(a, m)))
 * - 费马小定理：O(log m)（仅当m为质数时）
 * - 线性递推：O(n)（批量计算1~n的逆元）
 * 
 * 空间复杂度分析：
 * - 扩展欧几里得算法：O(log(min(a, m)))（递归栈）
 * - 费马小定理：O(1)
 * - 线性递推：O(n)（存储逆元数组）
 * 
 * C++特性注意事项：
 * 1. 使用long long类型避免溢出
 * 2. 负数取模处理：(x % mod + mod) % mod
 * 3. 使用引用传递避免拷贝大对象
 * 4. 使用vector动态数组
 * 5. 使用algorithm库的sort等函数
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <string>
#include <sstream>
#include <cstring>
#include <cstdio>
#include <cstdlib>
#include <map>
#include <set>
#include <queue>
#include <stack>
#include <bitset>
#include <iomanip>
#include <functional>
#include <numeric>
#include <limits>
#include <chrono>
#include <random>
#include <type_traits>
#include <unordered_map>
#include <unordered_set>
#include <stdexcept>
#include <cstdint>

using namespace std;

typedef long long ll;
typedef unsigned long long ull;
typedef pair<int, int> pii;
typedef pair<ll, ll> pll;
typedef vector<int> vi;
typedef vector<ll> vll;
typedef vector<bool> vb;
typedef vector<string> vs;
typedef vector<vector<int>> vvi;
typedef vector<vector<ll>> vvll;

const int MOD = 1000000007;
const int INF = 0x3f3f3f3f;
const double PI = acos(-1.0);
const double EPS = 1e-9;

// ==================== 基础算法实现 ====================

/**
 * 扩展欧几里得算法求模逆元（通用方法）
 * 适用于任何模数，是最通用的模逆元求解方法
 * 
 * 时间复杂度: O(log(min(a, m)))
 * 空间复杂度: O(log(min(a, m)))（递归调用栈）
 * 
 * @param a 要求逆元的数
 * @param m 模数
 * @return 如果存在逆元，返回最小正整数解；否则返回-1
 */
ll modInverseExtendedGcd(ll a, ll m) {
    // 参数验证
    if (m == 0) {
        throw invalid_argument("Modulus cannot be zero");
    }
    
    // 处理负数情况，确保a和m都是正数
    a = (a % m + m) % m;
    m = abs(m);
    
    ll x, y;
    ll gcd = extendedGcd(a, m, x, y);
    
    // 逆元存在的充要条件是a和m互质
    if (gcd != 1) {
        return -1; // 逆元不存在
    }
    
    // 确保结果为正数
    return (x % m + m) % m;
}

/**
 * 扩展欧几里得算法实现
 * 求解 ax + by = gcd(a, b)
 * 
 * @param a 系数a
 * @param b 系数b
 * @param x 用于返回x的解（引用传递）
 * @param y 用于返回y的解（引用传递）
 * @return gcd(a, b)
 */
ll extendedGcd(ll a, ll b, ll &x, ll &y) {
    if (b == 0) {
        x = 1;
        y = 0;
        return a;
    }
    
    ll x1, y1;
    ll gcd = extendedGcd(b, a % b, x1, y1);
    
    x = y1;
    y = x1 - (a / b) * y1;
    
    return gcd;
}

/**
 * 快速幂运算
 * 计算base^exp mod mod
 * 
 * 时间复杂度: O(log exp)
 * 空间复杂度: O(1)
 * 
 * @param base 底数
 * @param exp 指数
 * @param mod 模数
 * @return base^exp mod mod
 */
ll power(ll base, ll exp, ll mod) {
    if (mod == 0) throw invalid_argument("Modulus cannot be zero");
    if (exp < 0) throw invalid_argument("Exponent cannot be negative");
    
    ll result = 1;
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
 * 费马小定理求模逆元（仅当模数为质数时）
 * 根据费马小定理：a^(p-1) ≡ 1 (mod p)，所以 a^(-1) ≡ a^(p-2) (mod p)
 * 
 * 时间复杂度: O(log p)
 * 空间复杂度: O(1)
 * 
 * @param a 要求逆元的数
 * @param p 质数模数
 * @return a在模p意义下的逆元
 */
ll modInverseFermat(ll a, ll p) {
    if (p <= 1) throw invalid_argument("Prime modulus must be greater than 1");
    a = (a % p + p) % p;
    return power(a, p - 2, p);
}

/**
 * 线性递推求1~n所有整数的模逆元
 * 递推公式：inv[i] = (p - p/i) * inv[p%i] % p
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * @param n 范围上限
 * @param p 模数（质数）
 * @return 逆元数组
 */
vector<ll> buildInverseAll(int n, int p) {
    vector<ll> inv(n + 1);
    inv[1] = 1;
    for (int i = 2; i <= n; i++) {
        inv[i] = (p - (p / i) * inv[p % i] % p) % p;
    }
    return inv;
}

// ==================== 各大OJ平台题目实现 ====================

/**
 * 题目1: ZOJ 3609 Modular Inverse
 * 链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
 * 难度: 简单
 * 题意: 给定a和m，求a在模m意义下的乘法逆元
 */
void solveZOJ3609() {
    int t;
    cin >> t;
    while (t--) {
        ll a, m;
        cin >> a >> m;
        ll result = modInverseExtendedGcd(a, m);
        if (result == -1) {
            cout << "Not Exist" << endl;
        } else {
            cout << result << endl;
        }
    }
}

/**
 * 题目2: 洛谷 P3811 【模板】乘法逆元
 * 链接: https://www.luogu.com.cn/problem/P3811
 * 难度: 模板
 * 题意: 给定n和p，求1~n所有整数在模p意义下的乘法逆元
 */
void solveLuoguP3811() {
    int n, p;
    cin >> n >> p;
    vector<ll> inv = buildInverseAll(n, p);
    for (int i = 1; i <= n; i++) {
        cout << inv[i] << endl;
    }
}

/**
 * 题目3: POJ 1845 Sumdiv
 * 链接: http://poj.org/problem?id=1845
 * 难度: 中等
 * 题意: 计算A^B的所有约数之和模9901
 */
int sumDiv(int A, int B) {
    const int MOD = 9901;
    if (A == 0) return 0;
    if (B == 0) return 1;
    
    ll result = 1;
    // 质因数分解
    for (int i = 2; i * i <= A; i++) {
        if (A % i == 0) {
            int cnt = 0;
            while (A % i == 0) {
                cnt++;
                A /= i;
            }
            // 计算等比数列和: (i^(cnt*B+1)-1)/(i-1) mod MOD
            ll numerator = (power(i, (ll)cnt * B + 1, MOD) - 1 + MOD) % MOD;
            ll denominator = modInverseExtendedGcd(i - 1, MOD);
            if (denominator == -1) {
                // 当i-1 ≡ 0 mod MOD时，等比数列和为cnt*B+1
                result = result * (cnt * B + 1) % MOD;
            } else {
                result = result * numerator % MOD * denominator % MOD;
            }
        }
    }
    
    if (A > 1) {
        ll numerator = (power(A, B + 1, MOD) - 1 + MOD) % MOD;
        ll denominator = modInverseExtendedGcd(A - 1, MOD);
        if (denominator == -1) {
            result = result * (B + 1) % MOD;
        } else {
            result = result * numerator % MOD * denominator % MOD;
        }
    }
    
    return (int)result;
}

/**
 * 题目4: Codeforces 1445D. Divide and Sum
 * 链接: https://codeforces.com/problemset/problem/1445/D
 * 难度: 中等
 * 题意: 计算所有划分方案的f(p)值之和
 */
ll divideAndSum(vector<int>& arr) {
    const int MOD = 998244353;
    int n = arr.size() / 2;
    sort(arr.begin(), arr.end());
    
    // 预处理阶乘和阶乘逆元
    vector<ll> fact(2 * n + 1);
    vector<ll> invFact(2 * n + 1);
    fact[0] = 1;
    for (int i = 1; i <= 2 * n; i++) {
        fact[i] = fact[i - 1] * i % MOD;
    }
    invFact[2 * n] = power(fact[2 * n], MOD - 2, MOD);
    for (int i = 2 * n - 1; i >= 0; i--) {
        invFact[i] = invFact[i + 1] * (i + 1) % MOD;
    }
    
    ll sum = 0;
    for (int i = 0; i < n; i++) {
        sum = (sum + arr[n + i] - arr[i]) % MOD;
    }
    sum = (sum % MOD + MOD) % MOD;
    
    // 计算组合数C(2n-1, n-1)
    ll comb = fact[2 * n - 1] * invFact[n - 1] % MOD * invFact[n] % MOD;
    
    return sum * comb % MOD;
}

/**
 * 题目5: AtCoder ABC182E. Throne
 * 链接: https://atcoder.jp/contests/abc182/tasks/abc182_e
 * 难度: 中等
 * 题意: 在圆桌上移动，求到达特定位置的最小步数
 */
ll throne(ll N, ll S, ll K) {
    // 解方程: (S + K*x) ≡ 0 (mod N)
    // 即 K*x ≡ -S (mod N)
    ll g = __gcd(K, N);
    if (S % g != 0) return -1;
    
    ll newN = N / g;
    ll newK = K / g;
    ll newS = (-S) / g;
    
    ll inv = modInverseExtendedGcd(newK, newN);
    if (inv == -1) return -1;
    
    ll x = (inv * newS % newN + newN) % newN;
    return x;
}

/**
 * 题目6: CodeChef FOMBINATORIAL
 * 链接: https://www.codechef.com/problems/FOMBINATORIAL
 * 难度: 中等
 * 题意: 计算组合数取模
 */
ll fombinatorial(int n, int m, int mod) {
    // 预处理阶乘和阶乘逆元
    vector<ll> fact(n + 1);
    vector<ll> invFact(n + 1);
    fact[0] = 1;
    for (int i = 1; i <= n; i++) {
        fact[i] = fact[i - 1] * i % mod;
    }
    invFact[n] = power(fact[n], mod - 2, mod);
    for (int i = n - 1; i >= 0; i--) {
        invFact[i] = invFact[i + 1] * (i + 1) % mod;
    }
    
    // 计算组合数C(n, m)
    return fact[n] * invFact[m] % mod * invFact[n - m] % mod;
}

/**
 * 题目7: USACO 2009 Feb Gold Bulls and Cows
 * 链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=862
 * 难度: 中等
 * 题意: 计算满足特定条件的排列数
 */
ll bullsAndCows(int n, int k) {
    const int MOD = 1000000007;
    // 使用组合数学和模逆元计算
    vector<ll> fact(n + 1);
    vector<ll> invFact(n + 1);
    fact[0] = 1;
    for (int i = 1; i <= n; i++) {
        fact[i] = fact[i - 1] * i % MOD;
    }
    invFact[n] = power(fact[n], MOD - 2, MOD);
    for (int i = n - 1; i >= 0; i--) {
        invFact[i] = invFact[i + 1] * (i + 1) % MOD;
    }
    
    ll result = 0;
    for (int i = 0; i <= k; i++) {
        ll term = fact[n - i] * invFact[i] % MOD * invFact[k - i] % MOD;
        if (i % 2 == 0) {
            result = (result + term) % MOD;
        } else {
            result = (result - term + MOD) % MOD;
        }
    }
    
    return result;
}

/**
 * 题目8: 牛客练习赛68 B. 牛牛的算术
 * 链接: https://ac.nowcoder.com/acm/contest/11173/B
 * 难度: 中等
 * 题意: 计算表达式的值
 */
ll niuniuArithmetic(int n, int mod) {
    // 计算 1! + 2! + ... + n! mod mod
    ll sum = 0;
    ll fact = 1;
    for (int i = 1; i <= n; i++) {
        fact = fact * i % mod;
        sum = (sum + fact) % mod;
    }
    return sum;
}

/**
 * 题目9: LintCode 109 数字三角形
 * 链接: https://www.lintcode.com/problem/109/
 * 难度: 简单
 * 题意: 求从顶部到底部的最大路径和
 */
int minimumTotal(vector<vector<int>>& triangle) {
    int n = triangle.size();
    vector<vector<int>> dp(n, vector<int>(n, 0));
    
    for (int i = n - 1; i >= 0; i--) {
        for (int j = 0; j <= i; j++) {
            if (i == n - 1) {
                dp[i][j] = triangle[i][j];
            } else {
                dp[i][j] = triangle[i][j] + min(dp[i + 1][j], dp[i + 1][j + 1]);
            }
        }
    }
    
    return dp[0][0];
}

/**
 * 题目10: 计蒜客 A1638 逆元
 * 链接: https://nanti.jisuanke.com/t/A1638
 * 难度: 简单
 * 题意: 求单个数的模逆元
 */
void solveJisuankeA1638() {
    int t;
    cin >> t;
    while (t--) {
        ll a, p;
        cin >> a >> p;
        ll result = modInverseExtendedGcd(a, p);
        if (result == -1) {
            cout << "Not Exist" << endl;
        } else {
            cout << result << endl;
        }
    }
}

// ==================== 测试函数 ====================

int main() {
    cout << "=== 模逆元完整题目集测试（C++版）===" << endl;
    
    // 测试基础算法
    cout << "基础算法测试:" << endl;
    cout << "modInverseExtendedGcd(3, 11) = " << modInverseExtendedGcd(3, 11) << endl; // 4
    cout << "modInverseFermat(5, 13) = " << modInverseFermat(5, 13) << endl; // 8
    
    // 测试各大OJ题目
    cout << "\n各大OJ题目测试:" << endl;
    
    // POJ 1845
    cout << "POJ 1845 Sumdiv: sumDiv(2, 3) = " << sumDiv(2, 3) << endl; // 15
    
    // AtCoder ABC182E
    cout << "AtCoder ABC182E Throne: throne(10, 4, 3) = " << throne(10, 4, 3) << endl;
    
    // CodeChef FOMBINATORIAL
    cout << "CodeChef FOMBINATORIAL: fombinatorial(5, 2, MOD) = " << fombinatorial(5, 2, MOD) << endl;
    
    // 边界测试
    cout << "\n边界测试:" << endl;
    cout << "modInverseExtendedGcd(0, 5) = " << modInverseExtendedGcd(0, 5) << endl; // -1
    cout << "modInverseExtendedGcd(6, 8) = " << modInverseExtendedGcd(6, 8) << endl; // -1
    
    // 性能测试
    cout << "\n性能测试:" << endl;
    auto start = chrono::high_resolution_clock::now();
    vector<ll> inv = buildInverseAll(1000000, MOD);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "线性递推计算1~1000000的逆元耗时: " << duration.count() << "ms" << endl;
    
    cout << "测试完成!" << endl;
    
    return 0;
}
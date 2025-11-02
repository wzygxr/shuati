/**
 * 模逆元综合实现(C++版本)
 * 包含多种求解模逆元的方法及相关的算法题目实现
 * 
 * 模逆元的定义：
 * 对于整数a和模数m，如果存在整数x使得 a*x ≡ 1 (mod m)，则称x为a在模m意义下的乘法逆元
 * 
 * 模逆元存在的充要条件：gcd(a, m) = 1，即a和m互质
 * 
 * 应用场景：
 * 1. 数论计算中除法取模：在模运算中实现除法操作
 * 2. 组合数学中计算组合数取模：处理阶乘和阶乘逆元
 * 3. 密码学中RSA算法等：非对称加密算法的核心
 * 4. 算法竞赛中的各种数学题：如POJ 1845、LeetCode 1623等题目
 * 5. 编码理论：纠错码的设计和实现
 * 6. 工程应用：分布式系统中的一致性哈希、负载均衡等
 */

const long long MOD = 1000000007LL;

/**
 * 扩展欧几里得算法
 * 求解 ax + by = gcd(a, b)
 * 
 * 算法核心思想：
 * 利用欧几里得算法的递归特性，同时维护x和y的解
 * 
 * @param a 系数a
 * @param b 系数b
 * @param x 用于返回x的解（引用参数）
 * @param y 用于返回y的解（引用参数）
 * @return gcd(a, b)
 * 
 * 时间复杂度: O(log(min(a, b)))
 * 空间复杂度: O(log(min(a, b))) - 递归栈空间
 * 
 * 工程化考量：
 * - 参数验证：a和b可以为负数，但结果仍正确
 * - 溢出防护：处理大整数时可能需要使用更大的数据类型
 */
long long extendedGcd(long long a, long long b, long long *x, long long *y) {
    // 基本情况
    if (b == 0) {
        *x = 1;
        *y = 0;
        return a;
    }
    
    // 递归求解
    long long x1, y1;
    long long gcd = extendedGcd(b, a % b, &x1, &y1);
    
    // 更新x和y的值
    *x = y1;
    *y = x1 - (a / b) * y1;
    
    return gcd;
}

/**
 * 使用扩展欧几里得算法求模逆元
 * 适用于模数不一定是质数的情况
 * 
 * 算法核心思想：
 * 当gcd(a, mod) = 1时，扩展欧几里得算法求得的x即为逆元
 * 
 * 时间复杂度: O(log(min(a, mod)))
 * 空间复杂度: O(log(min(a, mod))) - 递归栈空间
 * 
 * @param a 要求逆元的数
 * @param mod 模数
 * @return 如果存在逆元，返回最小正整数解；否则返回-1表示逆元不存在
 * 
 * 工程化考量：
 * - 参数验证：a不能为0，mod必须大于1
 * - 异常处理：当a和mod不互质时返回-1
 * - 边界情况：确保返回结果是正数
 */
long long modInverseExtendedGcd(long long a, long long mod) {
    long long x, y;
    long long gcd = extendedGcd(a, mod, &x, &y);
    
    // 如果gcd不为1，则逆元不存在
    if (gcd != 1) {
        return -1;
    }
    
    // 确保结果为正数
    return (x % mod + mod) % mod;
}

/**
 * 快速幂运算（二分幂算法）
 * 计算base^exp mod mod
 * 
 * 算法核心思想：
 * 将指数分解为二进制形式，利用二进制位的性质减少乘法次数
 * 
 * 时间复杂度: O(log exp)
 * 空间复杂度: O(1)
 * 
 * @param base 底数
 * @param exp 指数
 * @param mod 模数
 * @return base^exp mod mod
 * 
 * 工程化考量：
 * - 溢出防护：每一步乘法后都取模，防止中间结果溢出
 * - 边界情况：处理exp=0和mod=1的特殊情况
 * - 性能优化：使用位运算提高效率
 */
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

/**
 * 使用费马小定理求模逆元（当模数为质数时）
 * 根据费马小定理: a^(p-1) ≡ 1 (mod p)，其中p是质数且a与p互质
 * 所以 a^(-1) ≡ a^(p-2) (mod p)
 * 
 * 算法核心思想：
 * 利用快速幂计算a的p-2次方模p
 * 
 * 时间复杂度: O(log(p))
 * 空间复杂度: O(1)
 * 
 * @param a 要求逆元的数
 * @param p 质数模数
 * @return 如果存在逆元，返回a在模p意义下的逆元；否则返回-1
 * 
 * 工程化考量：
 * - 参数验证：p必须是质数，a不能为0
 * - 边界情况：当a是p的倍数时，逆元不存在
 * - 性能注意：当p很大时，快速幂仍然高效
 */
long long modInverseFermat(long long a, long long p) {
    // 检查a是否与p互质
    if (power(a, p - 1, p) != 1) {
        return -1; // 逆元不存在
    }
    return power(a, p - 2, p);
}

/**
 * 使用线性递推方法计算1~n所有整数在模p意义下的乘法逆元
 * 递推公式推导：
 * 设 p = k*i + r，其中 k = p / i（整除），r = p % i
 * 则有 k*i + r ≡ 0 (mod p)
 * 两边同时乘以 i^(-1) * r^(-1) 得：
 * k*r^(-1) + i^(-1) ≡ 0 (mod p)
 * 即 i^(-1) ≡ -k*r^(-1) (mod p)
 * 由于 r < i，所以 r 的逆元在计算 i 的逆元时已经计算过了
 * 
 * 算法核心思想：
 * 利用已计算的较小数的逆元快速计算较大数的逆元
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * @param n 要计算逆元的范围上限
 * @param p 模数（必须为质数）
 * @param inv 用于存储逆元的数组（需要预分配至少n+1的空间）
 * 
 * 工程化考量：
 * - 参数验证：p必须是质数，n必须大于等于1
 * - 内存管理：确保inv数组有足够的空间
 * - 边界情况：处理n=1的特殊情况
 * - 性能优化：适用于需要批量计算多个数的逆元的场景
 */
void buildInverseAll(int n, int p, long long inv[]) {
    inv[1] = 1;
    for (int i = 2; i <= n; i++) {
        inv[i] = (p - (p / i) * inv[p % i] % p) % p;
    }
}

/**
 * LeetCode 1808. Maximize Number of Nice Divisors 题目实现
 * 题目链接: https://leetcode.cn/problems/maximize-number-of-nice-divisors/
 * 
 * 题目描述:
 * 给你一个正整数 primeFactors 。你需要构造一个正整数 n ，它满足以下条件：
 * 1. n 质因数（质因数需要考虑重复的情况）的数目 不超过 primeFactors 个。
 * 2. n 好因子的数目最大化。
 * 
 * 解题思路:
 * - 将问题转化为：将primeFactors分解为若干个正整数的和，使得这些数的乘积最大
 * - 数学上，将数尽可能分解为3的幂次能得到最大乘积
 * - 特殊情况处理：当余数为1时，使用一个4代替两个3
 * 
 * 算法本质：
 * 利用模逆元进行大数运算中的快速幂计算
 * 
 * 时间复杂度: O(log(primeFactors)) - 主要是快速幂的复杂度
 * 空间复杂度: O(1)
 * 
 * 这是最优解，因为我们利用了数学规律直接得到了最优分解方式
 */
int maxNiceDivisors(int primeFactors) {
    // 特殊情况处理 - 边界条件优化
    if (primeFactors <= 3) {
        return primeFactors;
    }
    
    int remainder = primeFactors % 3;
    int quotient = primeFactors / 3;
    
    if (remainder == 0) {
        // 全部用3
        return (int) (power(3, quotient, MOD));
    } else if (remainder == 1) {
        // 用一个4代替两个3
        return (int) ((power(3, quotient - 1, MOD) * 4) % MOD);
    } else {  // remainder == 2
        // 用一个2
        return (int) ((power(3, quotient, MOD) * 2) % MOD);
    }
}

/**
 * POJ 1845 Sumdiv 题目实现
 * 题目链接: http://poj.org/problem?id=1845
 * 
 * 题目描述:
 * 计算 A^B 的所有约数的和，并对结果取模 9901
 * 
 * 解题思路:
 * 1. 质因数分解：将A分解为质因数的乘积 A = p1^a1 * p2^a2 * ... * pn^an
 * 2. A^B = p1^(a1*B) * p2^(a2*B) * ... * pn^(an*B)
 * 3. 约数和公式：S = (1 + p1 + p1^2 + ... + p1^(a1*B)) * ... * (1 + pn + pn^2 + ... + pn^(an*B))
 * 4. 使用等比数列求和公式：1 + p + p^2 + ... + p^k = (p^(k+1) - 1) / (p - 1)
 * 5. 使用模逆元计算除法
 * 
 * 算法本质：
 * 质因数分解 + 等比数列求和 + 模逆元应用
 * 
 * 时间复杂度: O(sqrt(A) + log k)，其中k是最大的指数
 * 空间复杂度: O(1)
 * 
 * 这是最优解，因为质因数分解已经是最优的，等比数列求和使用二分法也达到了对数级别
 */
long long sumOfDivisors(long long A, long long B) {
    const int MOD = 9901;
    long long result = 1;
    
    // 质因数分解A
    for (long long p = 2; p * p <= A; ++p) {
        if (A % p == 0) {
            int exponent = 0;
            while (A % p == 0) {
                exponent++;
                A /= p;
            }
            // 计算(1 + p + p^2 + ... + p^(exponent*B)) mod MOD
            long long k = exponent * B;
            
            if ((p - 1) % MOD == 0) {
                // 特殊情况：p ≡ 1 mod MOD
                result = (result * (k + 1)) % MOD;
            } else {
                // 使用费马小定理求逆元，因为MOD是质数
                long long numerator = (power(p, k + 1, MOD) - 1 + MOD) % MOD;
                long long denominator = power(p - 1, MOD - 2, MOD);
                result = (result * numerator % MOD) * denominator % MOD;
            }
        }
    }
    
    // 处理A可能剩下的质因数
    if (A > 1) {
        long long k = B;
        if ((A - 1) % MOD == 0) {
            result = (result * (k + 1)) % MOD;
        } else {
            long long numerator = (power(A, k + 1, MOD) - 1 + MOD) % MOD;
            long long denominator = power(A - 1, MOD - 2, MOD);
            result = (result * numerator % MOD) * denominator % MOD;
        }
    }
    
    return result;
}

/**
 * LeetCode 1623. All Possible Full Binary Trees 题目实现
 * 题目链接: https://leetcode.cn/problems/all-possible-full-binary-trees/
 * 
 * 题目描述:
 * 给你一个整数n，请返回所有可能的满二叉树结构，其中满二叉树的定义是：每个节点要么有两个子节点，要么没有子节点。
 * （注：本题实际不是直接使用模逆元，但可以使用卡特兰数的概念来理解）
 * 
 * 这里我们实现一个简化版本，仅计算满二叉树的数量，并使用模运算
 * 
 * 解题思路:
 * - 动态规划：dp[n] 表示使用n个节点能构造的满二叉树数量
 * - 状态转移方程：dp[n] = sum(dp[i] * dp[n-1-i])，其中i从1到n-2，步长为2
 * - 因为满二叉树的节点数必须是奇数，所以只处理奇数的n
 * 
 * 算法本质：
 * 卡特兰数的一种变体计算
 * 
 * 时间复杂度: O(n^2)
 * 空间复杂度: O(n)
 * 
 * 这是最优解，因为动态规划已经达到了多项式时间复杂度
 */
long long countFullBinaryTrees(int n) {
    const int MOD = 1000000007;
    
    // 特殊情况：n必须是奇数，且至少为1
    if (n % 2 == 0 || n < 1) {
        return 0;
    }
    
    vector<long long> dp(n + 1, 0);
    dp[1] = 1;  // 基础情况
    
    for (int i = 3; i <= n; i += 2) {
        for (int j = 1; j < i; j += 2) {
            dp[i] = (dp[i] + dp[j] * dp[i - 1 - j]) % MOD;
        }
    }
    
    return dp[n];
}

/**
 * Codeforces 1445D. Divide and Sum 题目实现
 * 题目链接: https://codeforces.com/problemset/problem/1445/D
 * 
 * 题目描述:
 * 给定一个长度为2n的数组，将其分成两个长度为n的数组s和t。
 * 对s进行升序排序，对t进行降序排序。
 * 计算所有可能的分割方式对应的|s[i] - t[i]|之和的总和。
 * 
 * 解题思路:
 * 1. 首先对整个数组排序
 * 2. 结论：对于排序后的数组，最优的分割方式是s取前n个元素，t取后n个元素
 * 3. 对于每一种分割方式，总和为sum_{i=n}^{2n-1} a[i] * C(2n-2-i+n-1, n-1) - sum_{i=0}^{n-1} a[i] * C(i+n-1, n-1)
 * 4. 使用组合数计算和模逆元优化
 * 
 * 算法本质：
 * 组合数学 + 前缀和 + 模逆元优化
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 * 
 * 这是最优解，因为我们通过数学分析将问题简化为O(n)的计算
 */
long long divideAndSum(vector<long long>& a, int n) {
    const int MOD = 998244353;
    
    // 排序数组
    sort(a.begin(), a.end());
    
    // 预处理阶乘和阶乘的逆元
    vector<long long> fact(2 * n + 1), inv_fact(2 * n + 1);
    fact[0] = 1;
    for (int i = 1; i <= 2 * n; ++i) {
        fact[i] = fact[i - 1] * i % MOD;
    }
    
    // 使用费马小定理求阶乘的逆元
    inv_fact[2 * n] = power(fact[2 * n], MOD - 2, MOD);
    for (int i = 2 * n - 1; i >= 0; --i) {
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD;
    }
    
    // 计算组合数C(k, r) = fact[k] * inv_fact[r] * inv_fact[k-r] % MOD
    auto comb = [&](int k, int r) -> long long {
        if (r < 0 || r > k) return 0;
        return fact[k] * inv_fact[r] % MOD * inv_fact[k - r] % MOD;
    };
    
    long long result = 0;
    for (int i = 0; i < n; ++i) {
        // 计算组合数C(n-1+i, i) = C(n-1+i, n-1)
        long long c = comb(n - 1 + i, i);
        // 前n个元素的贡献是负的，后n个元素的贡献是正的
        result = (result - a[i] * c % MOD + a[i + n] * c % MOD + MOD) % MOD;
    }
    
    return result;
}

// 主函数，用于测试各个功能
int main() {
    // 测试扩展欧几里得算法
    long long x, y;
    long long g = extendedGcd(12, 5, &x, &y);
    printf("gcd(12, 5) = %lld, x = %lld, y = %lld\n", g, x, y);
    
    // 测试模逆元计算
    long long inv1 = modInverseExtendedGcd(3, 7);
    long long inv2 = modInverseFermat(3, 7);
    printf("Inverse of 3 mod 7 (extendedGcd): %lld\n", inv1);
    printf("Inverse of 3 mod 7 (Fermat): %lld\n", inv2);
    
    // 测试线性递推计算逆元
    const int MAX_N = 10;
    long long invs[MAX_N + 1];
    buildInverseAll(MAX_N, 1000000007, invs);
    printf("Inverses from 1 to %d:\n", MAX_N);
    for (int i = 1; i <= MAX_N; ++i) {
        printf("inv[%d] = %lld\n", i, invs[i]);
    }
    
    // 测试LeetCode 1808
    printf("maxNiceDivisors(5) = %d\n", maxNiceDivisors(5));
    printf("maxNiceDivisors(8) = %d\n", maxNiceDivisors(8));
    
    // 测试POJ 1845
    printf("sumOfDivisors(2, 3) = %lld\n", sumOfDivisors(2, 3));  // 应输出 15 (1+2+4+8)
    printf("sumOfDivisors(4, 2) = %lld\n", sumOfDivisors(4, 2));  // 应输出 21 (1+2+4+8+16)
    
    // 测试满二叉树数量计算
    printf("countFullBinaryTrees(3) = %lld\n", countFullBinaryTrees(3));  // 应输出 1
    printf("countFullBinaryTrees(5) = %lld\n", countFullBinaryTrees(5));  // 应输出 2
    
    // 测试Codeforces 1445D
    vector<long long> a = {1, 2, 3, 4};
    printf("divideAndSum([1,2,3,4], 2) = %lld\n", divideAndSum(a, 2));  // 应输出 8
    
    // 测试边界情况和异常场景
    printf("Inverse of 2 mod 4 (should not exist): %lld\n", modInverseExtendedGcd(2, 4));  // 应输出 -1
    
    return 0;
}
/**
 * LeetCode 1808. Maximize Number of Nice Divisors
 * 题目链接: https://leetcode.cn/problems/maximize-number-of-nice-divisors/
 * 
 * 题目描述:
 * 给你一个正整数 primeFactors 。你需要构造一个正整数 n ，它满足以下条件：
 * 1. n 质因数（质因数需要考虑重复的情况）的数目 不超过 primeFactors 个。
 * 2. n 好因子的数目最大化。
 * 
 * 如果 n 的一个因子可以被 n 的每一个质因数整除，我们称这个因子是 好因子 。
 * 比方说，如果 n = 12 ，那么它的质因数为 [2,2,3] ，那么 6 和 12 是好因子，但 3 和 4 不是。
 * 
 * 请你返回 n 的好因子的数目。由于答案可能会很大，请返回答案对 10^9 + 7 取余 的结果。
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
 * 时间复杂度: O(log primeFactors)
 * 空间复杂度: O(1)
 */

const int MOD = 1000000007;

/**
 * 快速幂运算
 * 计算 (base^exp) % mod
 * 
 * @param base 底数
 * @param exp 指数
 * @param mod 模数
 * @return (base^exp) % mod
 */
long long power(long long base, long long exp, int mod) {
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
 * 计算好因子的最大数目
 * 
 * @param primeFactors 质因数的数目上限
 * @return 好因子的最大数目模 10^9 + 7
 */
int maxNiceDivisors(int primeFactors) {
    // 特殊情况处理
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
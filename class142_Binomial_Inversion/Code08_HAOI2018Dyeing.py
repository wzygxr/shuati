# [HAOI2018]染色
# 题目描述：有一个长度为N的序列和M种颜色，给定S以及一个序列W。
# 对于一种染色方案，假设其中有k种颜色恰好出现了S次，则其价值为W_k。
# 求所有染色方案的价值和对1004535809取模的结果。
# 1 <= N <= 10^7, 1 <= M <= 10^5, 1 <= S <= 150, 0 <= W_i < 1004535809
# 测试链接: https://www.luogu.com.cn/problem/P4491

'''
二项式反演在染色问题中的应用：

问题描述：
有一个长度为N的序列和M种颜色，每个位置可以染成M种颜色中的某一种。
对于一种染色方案，假设其中有k种颜色恰好出现了S次，则其价值为W_k。
求所有染色方案的价值和。

解题思路：
设f(i)表示恰好有i种颜色恰好出现S次的方案数（目标）
设g(i)表示至少有i种颜色恰好出现S次的方案数

显然，g(i)更容易计算：
1. 先从M种颜色中选出i种颜色，方案数为C(M, i)
2. 从N个位置中选出i*S个位置分配给这i种颜色，每种颜色恰好S个位置，方案数为C(N, i*S) * (i*S)! / (S!)^i
3. 剩下的N-i*S个位置可以染成剩下的M-i种颜色中的任意一种，方案数为(M-i)^(N-i*S)
   
因此：g(i) = C(M, i) * C(N, i*S) * (i*S)! / (S!)^i * (M-i)^(N-i*S)

根据二项式反演公式2：
f(k) = Σ(i=k to min(M, N/S)) (-1)^(i-k) * C(i, k) * g(i)

最终答案为：Σ(k=0 to min(M, N/S)) W_k * f(k)

相关题目：
1. 洛谷 P4491 [HAOI2018]染色（标准题目）
2. 洛谷 P5505 [JSOI2011]分特产（类似思想）
'''

MOD = 1004535809
MAXN = 10000001

# 预处理阶乘和逆元
fact = [0] * MAXN
ifact = [0] * MAXN

# 快速幂
def pow_mod(base, exp, mod):
    res = 1
    while exp > 0:
        if exp % 2 == 1:
            res = res * base % mod
        base = base * base % mod
        exp //= 2
    return res

# 预处理
def init():
    global fact, ifact
    fact[0] = 1
    for i in range(1, MAXN):
        fact[i] = fact[i-1] * i % MOD
    
    ifact[MAXN-1] = pow_mod(fact[MAXN-1], MOD-2, MOD)
    for i in range(MAXN-2, -1, -1):
        ifact[i] = ifact[i+1] * (i+1) % MOD

# 计算组合数C(n, k)
def comb(n, k):
    if k > n or k < 0:
        return 0
    return fact[n] * ifact[k] % MOD * ifact[n-k] % MOD

# 主函数
if __name__ == "__main__":
    init()
    
    N, M, S = map(int, input().split())
    W = list(map(int, input().split()))
    
    # 预处理S的阶乘的逆元
    invSFact = pow_mod(fact[S], MOD-2, MOD)
    
    # 计算g函数值
    limit = min(M, N//S)
    g = [0] * (limit+1)
    for i in range(limit+1):
        # g(i) = C(M, i) * C(N, i*S) * (i*S)! / (S!)^i * (M-i)^(N-i*S)
        if i*S > N:
            g[i] = 0
        else:
            term1 = comb(M, i)
            term2 = comb(N, i*S)
            term3 = fact[i*S]
            term4 = pow_mod(invSFact, i, MOD)
            term5 = pow_mod(M-i, N-i*S, MOD)
            g[i] = term1 * term2 % MOD * term3 % MOD * term4 % MOD * term5 % MOD
    
    # 使用二项式反演计算f函数值
    f = [0] * (limit+1)
    for k in range(limit+1):
        f[k] = 0
        for i in range(k, limit+1):
            # (-1)^(i-k) * C(i, k) * g(i)
            term = comb(i, k) * g[i] % MOD
            if (i-k) % 2 == 0:
                f[k] = (f[k] + term) % MOD
            else:
                f[k] = (f[k] - term + MOD) % MOD
    
    # 计算最终答案
    ans = 0
    for k in range(limit+1):
        ans = (ans + W[k] * f[k]) % MOD
    
    print(ans)
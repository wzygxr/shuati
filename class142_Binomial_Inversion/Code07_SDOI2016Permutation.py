# [SDOI2016]排列计数
# 题目描述：求有多少种1到n的排列a，满足序列恰好有m个位置i，使得a_i = i。
# 1 <= T <= 5*10^5, 1 <= n <= 10^6, 0 <= m <= 10^6
# 测试链接: https://www.luogu.com.cn/problem/P4071

'''
二项式反演在排列计数问题中的应用：

问题描述：
求有多少种1到n的排列a，满足序列恰好有m个位置i，使得a_i = i。

解题思路：
设f(i)表示恰好有i个位置满足a[j] = j的排列数（即答案）
设g(i)表示至少有i个位置满足a[j] = j的排列数

显然，g(i)更容易计算：
1. 先从n个位置中选出i个位置固定，方案数为C(n, i)
2. 剩下的(n-i)个位置必须错排，方案数为D(n-i)
   
因此：g(i) = C(n, i) * D(n-i)

根据二项式反演公式2：
f(m) = Σ(i=m to n) (-1)^(i-m) * C(i, m) * g(i)
     = Σ(i=m to n) (-1)^(i-m) * C(i, m) * C(n, i) * D(n-i)

其中D(k)是k个元素的错排数，可以用递推公式计算：
D(0) = 1, D(1) = 0
D(k) = (k-1) * (D(k-1) + D(k-2))

相关题目：
1. 洛谷 P4071 [SDOI2016]排列计数（标准题目）
2. 洛谷 P1595 信封问题（错排问题）
'''

MOD = 1000000007
MAXN = 1000001

# 预处理阶乘和逆元
fact = [0] * MAXN
ifact = [0] * MAXN
# 错排数
derange = [0] * MAXN

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
    # 预处理阶乘
    fact[0] = 1
    for i in range(1, MAXN):
        fact[i] = fact[i-1] * i % MOD
    
    # 预处理逆元
    ifact[MAXN-1] = pow_mod(fact[MAXN-1], MOD-2, MOD)
    for i in range(MAXN-2, -1, -1):
        ifact[i] = ifact[i+1] * (i+1) % MOD
    
    # 预处理错排数
    derange[0] = 1
    derange[1] = 0
    for i in range(2, MAXN):
        derange[i] = (i-1) * (derange[i-1] + derange[i-2]) % MOD

# 计算组合数C(n, k)
def comb(n, k):
    if k > n or k < 0:
        return 0
    return fact[n] * ifact[k] % MOD * ifact[n-k] % MOD

# 计算恰好有m个位置满足a[i] = i的排列数
def solve(n, m):
    # 特殊情况处理
    if m > n:
        return 0
    
    # 使用二项式反演计算答案
    ans = 0
    for i in range(m, n+1):
        # (-1)^(i-m) * C(i, m) * C(n, i) * D(n-i)
        term = comb(i, m) * comb(n, i) % MOD * derange[n-i] % MOD
        if (i-m) % 2 == 0:
            ans = (ans + term) % MOD
        else:
            ans = (ans - term + MOD) % MOD
    return ans

# 主函数
if __name__ == "__main__":
    init()
    
    T = int(input())
    for _ in range(T):
        n, m = map(int, input().split())
        print(solve(n, m))
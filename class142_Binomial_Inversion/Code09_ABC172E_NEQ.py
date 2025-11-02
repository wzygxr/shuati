# AtCoder ABC172E NEQ
# 题目描述：给定两个数N M，要求构造两个长度为N的序列A和B，
# 满足以下条件：
# 1. 1 <= A_i, B_i <= M
# 2. A_i != B_i (1 <= i <= N)
# 3. A_i != A_j, B_i != B_j (1 <= i < j <= N)
# 求满足条件的序列对(A,B)的个数，答案对(10^9+7)取模。
# 1 <= N <= M <= 5*10^5
# 测试链接: https://atcoder.jp/contests/abc172/tasks/abc172_e

'''
二项式反演在序列计数问题中的应用：

问题描述：
构造两个长度为N的序列A和B，满足：
1. 元素范围在[1,M]之间
2. 对应位置元素不相等(A_i != B_i)
3. 各自序列内元素互不相等(A_i != A_j, B_i != B_j)

解题思路：
设f(i)表示恰好有i个位置满足A_j = B_j的方案数（目标）
设g(i)表示至少有i个位置满足A_j = B_j的方案数

显然，g(i)更容易计算：
1. 先从N个位置中选出i个位置，使得A_j = B_j，方案数为C(N, i)
2. 从M个数中选出i个数分配给这i个位置，方案数为P(M, i) = M!/(M-i)!
3. 剩下的N-i个位置需要满足A_j != B_j且各自序列内元素互不相等
   这等价于求长度为(N-i)的错排方案数，方案数为D(N-i, M-i)
   
因此：g(i) = C(N, i) * P(M, i) * D(N-i, M-i)

其中D(n, m)表示从m个数中选出n个数排列成序列，使得对应位置不相等的方案数，
可以用容斥原理计算：
D(n, m) = Σ(k=0 to n) (-1)^k * C(n, k) * P(m-k, n-k)

根据二项式反演公式2：
f(0) = Σ(i=0 to N) (-1)^i * C(N, i) * g(i)
     = Σ(i=0 to N) (-1)^i * C(N, i) * C(N, i) * P(M, i) * D(N-i, M-i)

相关题目：
1. AtCoder ABC172E NEQ（标准题目）
2. 洛谷 P4071 [SDOI2016]排列计数（类似思想）
'''

MOD = 1000000007
MAXN = 500001

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

# 计算排列数P(n, k)
def perm(n, k):
    if k > n or k < 0:
        return 0
    return fact[n] * ifact[n-k] % MOD

# 计算组合数C(n, k)
def comb(n, k):
    if k > n or k < 0:
        return 0
    return fact[n] * ifact[k] % MOD * ifact[n-k] % MOD

# 计算D(n, m)：从m个数中选出n个数排列成序列，使得对应位置不相等的方案数
def D(n, m):
    if n > m:
        return 0
    res = 0
    for k in range(n+1):
        # (-1)^k * C(n, k) * P(m-k, n-k)
        term = comb(n, k) * perm(m-k, n-k) % MOD
        if k % 2 == 0:
            res = (res + term) % MOD
        else:
            res = (res - term + MOD) % MOD
    return res

# 主函数
if __name__ == "__main__":
    init()
    
    N, M = map(int, input().split())
    
    # 使用二项式反演计算答案
    ans = 0
    for i in range(N+1):
        # (-1)^i * C(N, i) * C(N, i) * P(M, i) * D(N-i, M-i)
        term = comb(N, i) * comb(N, i) % MOD * perm(M, i) % MOD * D(N-i, M-i) % MOD
        if i % 2 == 0:
            ans = (ans + term) % MOD
        else:
            ans = (ans - term + MOD) % MOD
    
    print(ans)
# Codeforces 1342E Placing Rooks
# 题目大意：
# 在一个n×n的棋盘上放置n个车，要求：
# 1. 每个格子都至少被一个车攻击到
# 2. 恰好有k对车互相攻击
# 求方案数，答案对998244353取模

# 解题思路：
# 1. 要满足条件1，每行或每列都必须有车
# 2. 如果k >= n，则无解，因为n个车最多形成(n-1)对互相攻击
# 3. 假设每行都有车，有m列有车，则会产生(n-m)对互相攻击
#    因此m = n - k
# 4. 问题转化为：把n个不同的车放进(n-k)个不同的列中，每列至少有一个车
# 5. 这是典型的第二类斯特林数问题，方案数为S(n, n-k) * (n-k)!
# 6. 由于可以是每行有车或每列有车，所以总方案数要乘以2
# 7. 特殊情况：k=0时，每行每列都恰好有一个车，方案数为n!

# 二项式反演在本题中的应用：
# 第二类斯特林数可以用容斥原理计算，也可以用二项式反演理解
# S(n, m) = (1/m!) * Σ(i=0 to m) (-1)^(m-i) * C(m, i) * i^n

MOD = 998244353

def pow_mod(base, exp, mod):
    res = 1
    while exp > 0:
        if exp % 2 == 1:
            res = res * base % mod
        base = base * base % mod
        exp //= 2
    return res

def modinv(a, mod):
    return pow_mod(a, mod - 2, mod)

# 预处理阶乘和阶乘逆元
def init_fact(n):
    fact = [1] * (n + 1)
    for i in range(1, n + 1):
        fact[i] = fact[i - 1] * i % MOD
    ifact = [1] * (n + 1)
    ifact[n] = modinv(fact[n], MOD)
    for i in range(n - 1, -1, -1):
        ifact[i] = ifact[i + 1] * (i + 1) % MOD
    return fact, ifact

# 计算组合数
def comb(n, k, fact, ifact):
    if k > n or k < 0:
        return 0
    return fact[n] * ifact[k] % MOD * ifact[n - k] % MOD

# 计算第二类斯特林数S(n, m) * m!
# 即将n个不同的球放入m个不同的盒子，每个盒子非空的方案数
def stirling2(n, m, fact, ifact):
    if m > n or m < 0:
        return 0
    res = 0
    for i in range(m + 1):
        term = comb(m, i, fact, ifact) * pow_mod(i, n, MOD) % MOD
        if (m - i) % 2 == 0:
            res = (res + term) % MOD
        else:
            res = (res - term + MOD) % MOD
    return res

def main():
    n, k = map(int, input().split())
    
    # 特殊情况：k >= n 无解
    if k >= n:
        print(0)
        return
    
    # 预处理
    fact, ifact = init_fact(n)
    
    # 特殊情况：k = 0
    if k == 0:
        print(fact[n])
        return
    
    # 一般情况：m = n - k
    m = n - k
    
    # 计算将n个车放入m列，每列至少一个的方案数
    ways = stirling2(n, m, fact, ifact)
    
    # 选择哪m列
    ways = ways * comb(n, m, fact, ifact) % MOD
    
    # 可以是每行有车或每列有车，所以乘以2
    ways = ways * 2 % MOD
    
    print(ways)

if __name__ == "__main__":
    main()
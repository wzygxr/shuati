"""
NTT (Number Theoretic Transform) 算法实现

算法简介:
NTT是快速数论变换，是FFT在模意义下的替代算法。
它避免了浮点运算的精度问题，适用于需要精确整数运算的场景。

适用场景:
1. 多项式乘法
2. 大整数乘法
3. 组合数学计数问题

核心思想:
1. 利用模意义下的原根替代FFT中的单位根
2. 保持FFT的分治结构和蝴蝶操作
3. 通过模运算保证精度

时间复杂度: O(n log n)
空间复杂度: O(n)
"""

MOD = 998244353  # 常用的NTT模数
G = 3  # MOD的原根

def pow_mod(base, exp, mod):
    """
    快速幂运算
    """
    result = 1
    base %= mod
    while exp > 0:
        if exp & 1:
            result = result * base % mod
        base = base * base % mod
        exp >>= 1
    return result

def mod_inverse(a, mod):
    """
    模逆元
    """
    return pow_mod(a, mod - 2, mod)

def ntt(a, inv):
    """
    数论变换 (NTT)
    :param a: 输入数组
    :param inv: 是否为逆变换 (False为正变换，True为逆变换)
    """
    n = len(a)
    
    # 位逆序置换
    j = 0
    for i in range(1, n):
        bit = n >> 1
        while (j & bit) != 0:
            j ^= bit
            bit >>= 1
        j ^= bit
        if i < j:
            a[i], a[j] = a[j], a[i]
    
    # 蝴蝶操作
    len_val = 2
    while len_val <= n:
        wn = pow_mod(G, (MOD - 1) // len_val, MOD)
        if inv:
            wn = mod_inverse(wn, MOD)
        
        for i in range(0, n, len_val):
            w = 1
            for j in range(len_val // 2):
                u = a[i + j]
                v = a[i + j + len_val // 2] * w % MOD
                a[i + j] = (u + v) % MOD
                a[i + j + len_val // 2] = (u - v + MOD) % MOD
                w = w * wn % MOD
        len_val <<= 1
    
    # 逆变换需要除以n
    if inv:
        inv_n = mod_inverse(n, MOD)
        for i in range(n):
            a[i] = a[i] * inv_n % MOD

def multiply(a, b):
    """
    多项式乘法
    :param a: 第一个多项式的系数数组
    :param b: 第二个多项式的系数数组
    :return: 乘积多项式的系数数组
    """
    n = 1
    while n < len(a) + len(b):
        n <<= 1
    
    fa = [0] * n
    fb = [0] * n
    
    # 复制数组并扩展到2的幂次长度
    for i in range(len(a)):
        fa[i] = a[i]
    for i in range(len(b)):
        fb[i] = b[i]
    
    # 正向NTT
    ntt(fa, False)
    ntt(fb, False)
    
    # 点值乘法
    for i in range(n):
        fa[i] = fa[i] * fb[i] % MOD
    
    # 逆向NTT
    ntt(fa, True)
    
    return fa

def poly_inverse(a, n):
    """
    多项式逆元
    :param a: 多项式系数数组
    :param n: 结果长度
    :return: 逆元多项式系数数组
    """
    if n == 1:
        result = [0] * 1
        result[0] = mod_inverse(a[0], MOD)
        return result
    
    m = (n + 1) >> 1
    b = poly_inverse(a, m)
    
    # 扩展到n
    a0 = [0] * n
    b0 = [0] * n
    for i in range(min(len(a), n)):
        a0[i] = a[i]
    for i in range(min(len(b), n)):
        b0[i] = b[i]
    
    # 2*B - A*B*B
    tmp = multiply(multiply(a0, b0), b0)
    result = [0] * n
    for i in range(n):
        result[i] = (2 * b0[i] % MOD - tmp[i] + MOD) % MOD
    
    return result

def poly_sqrt(a, n):
    """
    多项式开方
    :param a: 多项式系数数组
    :param n: 结果长度
    :return: 开方多项式系数数组
    """
    if n == 1:
        result = [0] * 1
        result[0] = 1  # 简化处理，实际应计算模意义下的二次剩余
        return result
    
    m = (n + 1) >> 1
    b = poly_sqrt(a, m)
    c = poly_inverse(b, n)
    
    # 扩展到n
    a0 = [0] * n
    b0 = [0] * n
    c0 = [0] * n
    for i in range(min(len(a), n)):
        a0[i] = a[i]
    for i in range(min(len(b), n)):
        b0[i] = b[i]
    for i in range(min(len(c), n)):
        c0[i] = c[i]
    
    # (B + A/B) / 2
    tmp = multiply(a0, c0)
    result = [0] * n
    inv2 = mod_inverse(2, MOD)
    for i in range(n):
        result[i] = (b0[i] + tmp[i]) % MOD * inv2 % MOD
    
    return result

def poly_ln(a, n):
    """
    多项式对数
    :param a: 多项式系数数组
    :param n: 结果长度
    :return: 对数多项式系数数组
    """
    inv = poly_inverse(a, n)
    derivative = [0] * (n - 1)
    for i in range(n - 1):
        derivative[i] = (i + 1) * a[i + 1] % MOD
    
    tmp = multiply(inv, derivative)
    result = [0] * n
    for i in range(n - 1):
        result[i + 1] = tmp[i] * mod_inverse(i + 1, MOD) % MOD
    
    return result

def poly_exp(a, n):
    """
    多项式指数
    :param a: 多项式系数数组
    :param n: 结果长度
    :return: 指数多项式系数数组
    """
    if n == 1:
        result = [0] * 1
        result[0] = 1
        return result
    
    m = (n + 1) >> 1
    b = poly_exp(a, m)
    c = poly_ln(b, n)
    
    # 扩展到n
    a0 = [0] * n
    b0 = [0] * n
    c0 = [0] * n
    for i in range(min(len(a), n)):
        a0[i] = a[i]
    for i in range(min(len(b), n)):
        b0[i] = b[i]
    for i in range(min(len(c), n)):
        c0[i] = c[i]
    
    # B * (1 - C + A)
    for i in range(n):
        c0[i] = (1 - c0[i] + a0[i] + MOD + MOD) % MOD
    
    result = multiply(b0, c0)
    return result[:n]

def solve_p3803(n, m, f, g):
    """
    洛谷P3803 【模板】多项式乘法（FFT）
    题目来源: https://www.luogu.com.cn/problem/P3803
    题目描述: 给定一个n次多项式F(x)，和一个m次多项式G(x)，请求出F(x)和G(x)的乘积。
    解题思路: 直接使用NTT算法计算多项式乘法
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    
    :param n: n次多项式F(x)的次数
    :param m: m次多项式G(x)的次数
    :param f: F(x)的系数数组
    :param g: G(x)的系数数组
    :return: F(x)和G(x)乘积的系数数组
    """
    return multiply(f, g)

def solve_p4245(n, m, p, f, g):
    """
    洛谷P4245 【模板】任意模数多项式乘法
    题目来源: https://www.luogu.com.cn/problem/P4245
    题目描述: 给定两个多项式F(x)和G(x)的系数，以及模数p，请求出F(x)和G(x)的卷积模p的结果。
    解题思路: 使用NTT算法计算多项式乘法，然后对结果取模
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    
    :param n: F(x)的次数
    :param m: G(x)的次数
    :param p: 模数
    :param f: F(x)的系数数组
    :param g: G(x)的系数数组
    :return: F(x)和G(x)卷积模p的结果
    """
    # 这里简化处理，实际应该使用中国剩余定理或其他方法处理任意模数
    # 为了演示目的，我们直接使用NTT计算然后取模
    result = multiply(f, g)
    
    # 对结果取模
    for i in range(len(result)):
        result[i] %= p
    
    return result

# 测试用例
if __name__ == "__main__":
    # 测试多项式乘法: (1 + 2x) * (1 + 3x) = 1 + 5x + 6x^2
    a = [1, 2]
    b = [1, 3]
    result = multiply(a, b)
    print("Multiply result:", end=" ")
    for i in range(min(len(result), 3)):
        print(result[i], end=" ")
    print()
    
    # 测试洛谷P3803题目
    n1, m1 = 1, 1  # 1次多项式
    f1 = [1, 2]  # F(x) = 1 + 2x
    g1 = [1, 3]  # G(x) = 1 + 3x
    result1 = solve_p3803(n1, m1, f1, g1)
    print("P3803 result:", end=" ")
    for i in range(n1 + m1 + 1):
        print(result1[i], end=" ")
    print()
    
    # 边界情况测试
    # 测试空数组
    empty1 = []
    empty2 = []
    empty_result = solve_p3803(0, 0, empty1, empty2)
    print("Boundary test 1 - multiplication of empty arrays:", end=" ")
    for i in range(min(5, len(empty_result))):
        print(empty_result[i], end=" ")
    print()
    
    # 测试单元素数组
    single1 = [7]
    single2 = [3]
    single_result = solve_p3803(0, 0, single1, single2)
    print("Boundary test 2 - multiplication of single elements:", single_result[0])
    
    # 测试较大数组
    large1 = [1, 2, 3, 4, 5]
    large2 = [5, 4, 3, 2, 1]
    large_result = solve_p3803(4, 4, large1, large2)
    print("Boundary test 3 - multiplication of larger arrays:", end=" ")
    for i in range(min(9, len(large_result))):
        print(large_result[i], end=" ")
    print()
    
    # 测试洛谷P4245题目
    n2, m2 = 1, 1  # 1次多项式
    p2 = 1000000007  # 模数
    f2 = [1, 2]  # F(x) = 1 + 2x
    g2 = [1, 3]  # G(x) = 1 + 3x
    result2 = solve_p4245(n2, m2, p2, f2, g2)
    print("P4245 result:", end=" ")
    for i in range(n2 + m2 + 1):
        print(result2[i], end=" ")
    print()
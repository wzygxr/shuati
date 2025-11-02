"""
FWT (Fast Walsh-Hadamard Transform) 算法实现

算法简介:
FWT是快速沃尔什-哈达玛变换，用于计算位运算卷积。
它可以高效地计算OR、AND、XOR三种位运算的卷积。

适用场景:
1. 位运算卷积计算
2. 子集枚举问题
3. 组合计数问题

核心思想:
1. 利用沃尔什-哈达玛矩阵的性质
2. 通过分治方法实现快速变换
3. 支持三种不同的位运算: OR、AND、XOR

时间复杂度: O(n log n)
空间复杂度: O(n)
"""

MOD = 1000000007

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

def xor_fwt(a, inv):
    """
    XOR FWT变换
    :param a: 输入数组
    :param inv: 是否为逆变换
    """
    n = len(a)
    l = 1
    while l < n:
        for i in range(0, n, l << 1):
            for j in range(l):
                x = a[i + j]
                y = a[i + j + l]
                a[i + j] = (x + y) % MOD
                a[i + j + l] = (x - y + MOD) % MOD
        l <<= 1
    
    # 逆变换需要除以n
    if inv:
        inv_n = mod_inverse(n, MOD)
        for i in range(n):
            a[i] = a[i] * inv_n % MOD

def or_fwt(a, inv):
    """
    OR FWT变换
    :param a: 输入数组
    :param inv: 是否为逆变换
    """
    n = len(a)
    l = 1
    while l < n:
        for i in range(0, n, l << 1):
            for j in range(l):
                if not inv:
                    a[i + j + l] = (a[i + j + l] + a[i + j]) % MOD
                else:
                    a[i + j + l] = (a[i + j + l] - a[i + j] + MOD) % MOD
        l <<= 1

def and_fwt(a, inv):
    """
    AND FWT变换
    :param a: 输入数组
    :param inv: 是否为逆变换
    """
    n = len(a)
    l = 1
    while l < n:
        for i in range(0, n, l << 1):
            for j in range(l):
                if not inv:
                    a[i + j] = (a[i + j] + a[i + j + l]) % MOD
                else:
                    a[i + j] = (a[i + j] - a[i + j + l] + MOD) % MOD
        l <<= 1

def xor_convolution(a, b):
    """
    XOR卷积
    :param a: 第一个数组
    :param b: 第二个数组
    :return: XOR卷积结果
    """
    n = 1
    while n < max(len(a), len(b)):
        n <<= 1
    
    fa = [0] * n
    fb = [0] * n
    
    for i in range(len(a)):
        fa[i] = a[i]
    for i in range(len(b)):
        fb[i] = b[i]
    
    xor_fwt(fa, False)
    xor_fwt(fb, False)
    
    for i in range(n):
        fa[i] = fa[i] * fb[i] % MOD
    
    xor_fwt(fa, True)
    return fa

def or_convolution(a, b):
    """
    OR卷积
    :param a: 第一个数组
    :param b: 第二个数组
    :return: OR卷积结果
    """
    n = 1
    while n < max(len(a), len(b)):
        n <<= 1
    
    fa = [0] * n
    fb = [0] * n
    
    for i in range(len(a)):
        fa[i] = a[i]
    for i in range(len(b)):
        fb[i] = b[i]
    
    or_fwt(fa, False)
    or_fwt(fb, False)
    
    for i in range(n):
        fa[i] = fa[i] * fb[i] % MOD
    
    or_fwt(fa, True)
    return fa

def and_convolution(a, b):
    """
    AND卷积
    :param a: 第一个数组
    :param b: 第二个数组
    :return: AND卷积结果
    """
    n = 1
    while n < max(len(a), len(b)):
        n <<= 1
    
    fa = [0] * n
    fb = [0] * n
    
    for i in range(len(a)):
        fa[i] = a[i]
    for i in range(len(b)):
        fb[i] = b[i]
    
    and_fwt(fa, False)
    and_fwt(fb, False)
    
    for i in range(n):
        fa[i] = fa[i] * fb[i] % MOD
    
    and_fwt(fa, True)
    return fa

def subset_convolution(a, b):
    """
    子集卷积 (Subset Convolution)
    :param a: 第一个数组
    :param b: 第二个数组
    :return: 子集卷积结果
    """
    n = len(a)
    import math
    log_n = 0
    while (1 << log_n) < n:
        log_n += 1
    
    # 按照位数分组
    fa = [[0] * n for _ in range(log_n + 1)]
    fb = [[0] * n for _ in range(log_n + 1)]
    
    for i in range(n):
        bits = bin(i).count('1')
        fa[bits][i] = a[i]
        fb[bits][i] = b[i]
    
    # 对每一层进行OR FWT
    for i in range(log_n + 1):
        or_fwt(fa[i], False)
        or_fwt(fb[i], False)
    
    # 卷积计算
    result = [[0] * n for _ in range(log_n + 1)]
    for i in range(log_n + 1):
        for j in range(i + 1):
            for k in range(n):
                result[i][k] = (result[i][k] + fa[j][k] * fb[i - j][k]) % MOD
    
    # 逆变换
    for i in range(log_n + 1):
        or_fwt(result[i], True)
    
    # 提取结果
    res = [0] * n
    for i in range(n):
        bits = bin(i).count('1')
        res[i] = result[bits][i]
    
    return res

def solve_p4717(n, a, b):
    """
    洛谷P4717 【模板】快速莫比乌斯/沃尔什变换 (FMT/FWT)
    题目来源: https://www.luogu.com.cn/problem/P4717
    题目描述: 给定长度为2^n两个序列A,B，设C_i=Σ(j⊕k=i)A_j×B_k，分别当⊕是or, and, xor时求出C。
    解题思路: 直接使用FWT算法分别计算OR、AND、XOR三种卷积
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    
    :param n: 整数n
    :param a: 数组A
    :param b: 数组B
    :return: 包含OR、AND、XOR卷积结果的列表
    """
    or_result = or_convolution(a, b)
    and_result = and_convolution(a, b)
    xor_result = xor_convolution(a, b)
    
    return [or_result, and_result, xor_result]

def solve_p6097(n, a, b):
    """
    洛谷P6097 【模板】子集卷积
    题目来源: https://www.luogu.com.cn/problem/P6097
    题目描述: 给定两个长度为2^n的序列a和b，求出序列c，其中c_k=Σ(i&j=0,i|j=k)a_i*b_j
    解题思路: 使用子集卷积算法，通过按位数分组和OR卷积来实现
    时间复杂度: O(n^2 * 2^n)
    空间复杂度: O(n * 2^n)
    
    :param n: 集合大小
    :param a: 序列a
    :param b: 序列b
    :return: 序列c
    """
    return subset_convolution(a, b)

# 测试用例
if __name__ == "__main__":
    # 测试XOR卷积
    a = [1, 2, 3, 4]
    b = [1, 1, 1, 1]
    result = xor_convolution(a, b)
    print("XOR convolution result:", end=" ")
    for i in range(len(result)):
        print(result[i], end=" ")
    print()
    
    # 测试洛谷P4717题目
    n = 2  # 2^2 = 4个元素
    a1 = [1, 2, 3, 4]
    b1 = [1, 2, 3, 4]
    results = solve_p4717(n, a1, b1)
    
    print("OR convolution result:", end=" ")
    for i in range(len(results[0])):
        print(results[0][i], end=" ")
    print()
    
    print("AND convolution result:", end=" ")
    for i in range(len(results[1])):
        print(results[1][i], end=" ")
    print()
    
    print("XOR convolution result:", end=" ")
    for i in range(len(results[2])):
        print(results[2][i], end=" ")
    print()
    
    # 边界情况测试
    # 测试空数组
    empty1 = []
    empty2 = []
    empty_results = solve_p4717(0, empty1, empty2)
    print("Boundary test 1 - XOR convolution of empty arrays:", end=" ")
    for i in range(min(5, len(empty_results[2]))):
        print(empty_results[2][i], end=" ")
    print()
    
    # 测试单元素数组
    single1 = [5]
    single2 = [3]
    single_results = solve_p4717(0, single1, single2)
    print("Boundary test 2 - XOR convolution of single elements:", single_results[2][0])
    
    # 测试较大数组
    large1 = [1, 2, 3, 4, 5, 6, 7, 8]
    large2 = [8, 7, 6, 5, 4, 3, 2, 1]
    large_results = solve_p4717(3, large1, large2)
    print("Boundary test 3 - XOR convolution of larger arrays:", end=" ")
    for i in range(min(8, len(large_results[2]))):
        print(large_results[2][i], end=" ")
    print()
    
    # 测试洛谷P6097题目
    n2 = 2  # 2^2 = 4个元素
    a2 = [1, 2, 3, 4]
    b2 = [1, 2, 3, 4]
    result2 = solve_p6097(n2, a2, b2)
    print("P6097 subset convolution result:", end=" ")
    for i in range(len(result2)):
        print(result2[i], end=" ")
    print()
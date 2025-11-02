"""
BSGS (Baby-Step Giant-Step) 算法实现

算法简介:
BSGS算法用于求解离散对数问题: 给定a, b, p，求最小的非负整数x使得 a^x ≡ b (mod p)

适用场景:
1. 求解离散对数问题
2. 当p较小时，可以使用BSGS算法
3. 当p较大时，可以使用扩展BSGS算法

核心思想:
1. 将x表示为x = i*m - j，其中m = ceil(sqrt(p))
2. 原式变为 a^(i*m - j) ≡ b (mod p)
3. 移项得 a^(i*m) ≡ b * a^j (mod p)
4. 预处理所有a^j (baby step)，然后枚举i计算a^(i*m)查找 (giant step)

时间复杂度: O(sqrt(p))
空间复杂度: O(sqrt(p))
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

def extended_gcd(a, b):
    """
    扩展欧几里得算法
    """
    if b == 0:
        return a, 1, 0
    gcd, x, y = extended_gcd(b, a % b)
    return gcd, y, x - (a // b) * y

def mod_inverse(a, mod):
    """
    模逆元
    """
    gcd, x, y = extended_gcd(a, mod)
    if gcd != 1:
        return -1  # 不存在逆元
    return (x % mod + mod) % mod

def gcd(a, b):
    """
    求最大公约数
    """
    return a if b == 0 else gcd(b, a % b)

def bsgs(a, b, p):
    """
    BSGS算法求解 a^x ≡ b (mod p)，其中gcd(a, p) = 1
    """
    a %= p
    b %= p
    if b == 1:
        return 0
    
    # 计算m = ceil(sqrt(p))
    import math
    m = math.ceil(math.sqrt(p))
    
    # Baby steps: 计算 a^j mod p 并存储到字典中
    baby_steps = {}
    aj = 1
    for j in range(m):
        if aj not in baby_steps:
            baby_steps[aj] = j
        aj = aj * a % p
    
    # Giant steps: 计算 γ = a^m mod p
    gamma = pow_mod(a, m, p)
    
    # 查找满足条件的i
    gamma_i = 1
    for i in range(m):
        # 计算 b * (γ^i)^(-1) mod p
        target = b * mod_inverse(gamma_i, p) % p
        if target in baby_steps:
            x = i * m + baby_steps[target]
            if x >= 0:
                return x
        gamma_i = gamma_i * gamma % p
    
    return -1  # 无解

def ex_bsgs(a, b, p):
    """
    扩展BSGS算法，处理gcd(a, p) ≠ 1的情况
    """
    a %= p
    b %= p
    if b == 1:
        return 0
    
    gcd_val = 1
    c = 0
    ap = a
    bp = b
    pp = p
    
    # 处理gcd(a, p) ≠ 1的情况
    while (gcd_val := gcd(ap, pp)) > 1:
        if bp % gcd_val != 0:
            return -1  # 无解
        pp //= gcd_val
        bp //= gcd_val
        c += 1
        # 检查是否已经找到解
        result = pow_mod(ap, c, p)
        if result == bp:
            return c
        ap = ap * a // gcd_val % pp
    
    # 使用BSGS算法求解约简后的方程
    result = bsgs(ap, bp, pp)
    if result == -1:
        return -1
    return result + c

def solve_p3846(p, b, n):
    """
    洛谷P3846 [TJOI2007]可爱的质数/【模板】BSGS
    题目来源: https://www.luogu.com.cn/problem/P3846
    题目描述: 给定一个质数p，以及一个整数b，一个整数n，现在要求你计算一个最小的非负整数l，满足b^l ≡ n (mod p)
    解题思路: 直接使用BSGS算法求解离散对数问题
    时间复杂度: O(sqrt(p))
    空间复杂度: O(sqrt(p))
    
    :param p: 质数
    :param b: 底数
    :param n: 结果
    :return: 最小的非负整数l，如果无解返回-1
    """
    # 特殊情况处理
    if n == 1:
        return 0  # b^0 = 1
    if b == n:
        return 1  # b^1 = b
    
    # 使用BSGS算法求解
    return bsgs(b, n, p)

def solve_abc335g(n, p, a):
    """
    AtCoder ABC335 G - Discrete Logarithm Problems
    题目来源: https://atcoder.jp/contests/abc335/tasks/abc335_g
    题目描述: 给定N个整数A_1,...,A_N和素数P，求满足条件的整数对(i,j)的个数，
    条件是存在正整数k使得A_i^k ≡ A_j (mod P)
    解题思路: 对于每个A_i，我们预处理它能生成的所有值A_i^k mod P，然后统计每个值出现的次数。
    为了高效计算，我们使用BSGS算法来找出每个A_i生成的所有值。
    时间复杂度: O(N * sqrt(P))
    空间复杂度: O(N * sqrt(P))
    
    :param n: 整数个数
    :param p: 素数
    :param a: 整数数组
    :return: 满足条件的整数对(i,j)的个数
    """
    # 统计每个值出现的次数
    value_count = {}
    
    # 对于每个A_i，计算它能生成的所有值
    for i in range(n):
        ai = a[i]
        if ai == 0:
            # 特殊情况：0^k = 0 (k > 0)
            if 0 in value_count:
                value_count[0] += 1
            else:
                value_count[0] = 1
            continue
        
        # 使用BSGS算法找出ai生成的所有值
        generated_values = set()
        current_value = ai % p
        cycle_start = -1
        seen = {}
        
        # 找到循环节
        for k in range(1, p + 1):
            if current_value in seen:
                cycle_start = seen[current_value]
                break
            seen[current_value] = k
            generated_values.add(current_value)
            
            # 如果当前值是1，那么之后会循环
            if current_value == 1:
                break
            
            current_value = current_value * ai % p
        
        # 统计生成的值
        for value in generated_values:
            if value in value_count:
                value_count[value] += 1
            else:
                value_count[value] = 1
    
    # 计算满足条件的对数
    result = 0
    for i in range(n):
        ai = a[i]
        if ai in value_count:
            result += value_count[ai]
    
    return result

# 测试用例
if __name__ == "__main__":
    # 测试BSGS算法
    a1, b1, p1 = 2, 3, 11
    result1 = bsgs(a1, b1, p1)
    print(f"BSGS: {a1}^x ≡ {b1} (mod {p1}), x = {result1}")
    
    # 测试扩展BSGS算法
    a2, b2, p2 = 2, 3, 12
    result2 = ex_bsgs(a2, b2, p2)
    print(f"ExBSGS: {a2}^x ≡ {b2} (mod {p2}), x = {result2}")
    
    # 测试洛谷P3846题目
    p3, b3, n3 = 5, 2, 3
    result3 = solve_p3846(p3, b3, n3)
    if result3 == -1:
        print("no solution")
    else:
        print(f"P3846: {b3}^x ≡ {n3} (mod {p3}), x = {result3}")
    
    # 边界情况测试
    # 测试b^0 = 1的情况
    p4, b4, n4 = 7, 3, 1
    result4 = solve_p3846(p4, b4, n4)
    print(f"Boundary test 1: {b4}^x ≡ {n4} (mod {p4}), x = {result4}")
    
    # 测试b^1 = b的情况
    p5, b5, n5 = 11, 5, 5
    result5 = solve_p3846(p5, b5, n5)
    print(f"Boundary test 2: {b5}^x ≡ {n5} (mod {p5}), x = {result5}")
    
    # 测试无解情况
    p6, b6, n6 = 7, 2, 3  # 2^x ≡ 3 (mod 7) 无解
    result6 = solve_p3846(p6, b6, n6)
    if result6 == -1:
        print(f"Boundary test 3: {b6}^x ≡ {n6} (mod {p6}), no solution")
    else:
        print(f"Boundary test 3: {b6}^x ≡ {n6} (mod {p6}), x = {result6}")
    
    # 测试AtCoder ABC335 G题目
    n7, p7 = 3, 13
    a7 = [2, 3, 5]
    result7 = solve_abc335g(n7, p7, a7)
    print(f"ABC335G: n={n7}, p={p7}, result={result7}")
#!/usr/bin/env python3

"""
模逆元综合实现
包含多种求解模逆元的方法及相关的算法题目实现

模逆元的定义：
对于整数a和模数m，如果存在整数x使得 a*x ≡ 1 (mod m)，则称x为a在模m意义下的乘法逆元

模逆元存在的充要条件：gcd(a, m) = 1，即a和m互质

应用场景：
1. 数论计算中除法取模：在模运算中实现除法操作
2. 组合数学中计算组合数取模：处理阶乘和阶乘逆元
3. 密码学中RSA算法等：非对称加密算法的核心
4. 算法竞赛中的各种数学题：如POJ 1845、LeetCode 1623等题目
5. 编码理论：纠错码的设计和实现
6. 工程应用：分布式系统中的一致性哈希、负载均衡等

算法学习要点：
- 不同求逆元方法的适用场景
- 模运算的性质和优化技巧
- 边界情况和异常处理
- 数学公式的工程化实现
"""

MOD = 1000000007


def extended_gcd(a, b):
    """
    扩展欧几里得算法
    求解 ax + by = gcd(a, b)
    
    算法核心思想：
    利用欧几里得算法的递归特性，同时维护x和y的解
    
    Args:
        a: 系数a
        b: 系数b
        
    Returns:
        (gcd, x, y): gcd(a, b)和对应的x, y值
    
    Time Complexity: O(log(min(a, b)))
    Space Complexity: O(log(min(a, b))) - 递归栈空间
    
    工程化考量：
    - 参数验证：a和b可以为负数，但结果仍正确
    - Python中的递归深度限制：对于特别大的数，可能需要改为非递归实现
    """
    if b == 0:
        return a, 1, 0
    
    gcd, x1, y1 = extended_gcd(b, a % b)
    x = y1
    y = x1 - (a // b) * y1
    
    return gcd, x, y


def mod_inverse_extended_gcd(a, mod):
    """
    使用扩展欧几里得算法求模逆元
    适用于模数不一定是质数的情况
    
    算法核心思想：
    当gcd(a, mod) = 1时，扩展欧几里得算法求得的x即为逆元
    
    Args:
        a: 要求逆元的数
        mod: 模数
        
    Returns:
        如果存在逆元，返回最小正整数解；否则返回-1
        
    Time Complexity: O(log(min(a, mod)))
    Space Complexity: O(log(min(a, mod))) - 递归栈空间
    
    工程化考量：
    - 参数验证：a不能为0，mod必须大于1
    - 异常处理：当a和mod不互质时返回-1
    - 边界情况：确保返回结果是正数
    """
    # 参数验证
    if a == 0 or mod <= 1:
        return -1
    
    gcd, x, y = extended_gcd(a, mod)
    
    # 如果gcd不为1，则逆元不存在
    if gcd != 1:
        return -1
    
    # 确保结果为正数
    return (x % mod + mod) % mod


def power(base, exp, mod):
    """
    快速幂运算（二分幂算法）
    计算base^exp mod mod
    
    算法核心思想：
    将指数分解为二进制形式，利用二进制位的性质减少乘法次数
    
    Args:
        base: 底数
        exp: 指数
        mod: 模数
        
    Returns:
        base^exp mod mod
        
    Time Complexity: O(log exp)
    Space Complexity: O(1)
    
    工程化考量：
    - 溢出防护：Python整数不会溢出，但取模操作可以减少计算量
    - 边界情况：处理exp=0和mod=1的特殊情况
    - 性能优化：使用位运算提高效率
    """
    # 边界情况处理
    if mod == 1:
        return 0
    if exp == 0:
        return 1 if base != 0 else 0
    
    result = 1
    base %= mod
    
    while exp > 0:
        if exp & 1:
            result = (result * base) % mod
        base = (base * base) % mod
        exp >>= 1
    
    return result


def mod_inverse_fermat(a, p):
    """
    使用费马小定理求模逆元（当模数为质数时）
    根据费马小定理: a^(p-1) ≡ 1 (mod p)，其中p是质数且a与p互质
    所以 a^(-1) ≡ a^(p-2) (mod p)
    
    算法核心思想：
    利用快速幂计算a的p-2次方模p
    
    Args:
        a: 要求逆元的数
        p: 质数模数
        
    Returns:
        如果存在逆元，返回a在模p意义下的逆元；否则返回-1
        
    Time Complexity: O(log(p))
    Space Complexity: O(1)
    
    工程化考量：
    - 参数验证：p必须是质数，a不能为0
    - 边界情况：当a是p的倍数时，逆元不存在
    - 性能注意：当p很大时，快速幂仍然高效
    """
    # 参数验证
    if a == 0 or p <= 1:
        return -1
    
    # 检查a是否与p互质
    if power(a, p - 1, p) != 1:
        return -1  # 逆元不存在
    return power(a, p - 2, p)


def build_inverse_all(n, p):
    """
    使用线性递推方法计算1~n所有整数在模p意义下的乘法逆元
    递推公式推导：
    设 p = k*i + r，其中 k = p // i（整除），r = p % i
    则有 k*i + r ≡ 0 (mod p)
    两边同时乘以 i^(-1) * r^(-1) 得：
    k*r^(-1) + i^(-1) ≡ 0 (mod p)
    即 i^(-1) ≡ -k*r^(-1) (mod p)
    由于 r < i，所以 r 的逆元在计算 i 的逆元时已经计算过了
    
    算法核心思想：
    利用已计算的较小数的逆元快速计算较大数的逆元
    
    Args:
        n: 要计算逆元的范围上限
        p: 模数（必须为质数）
        
    Returns:
        存储逆元的列表，索引从0到n
        
    Time Complexity: O(n)
    Space Complexity: O(n)
    
    工程化考量：
    - 参数验证：p必须是质数，n必须大于等于1
    - 内存优化：对于特别大的n，可以考虑生成器模式节省内存
    - 性能优化：适用于需要批量计算多个数的逆元的场景
    """
    # 参数验证
    if n < 1 or p <= 1:
        return []
    
    inv = [0] * (n + 1)
    inv[1] = 1  # 基础情况
    
    for i in range(2, n + 1):
        inv[i] = (p - (p // i) * inv[p % i] % p) % p
    
    return inv


def preprocess_factorial(n, p):
    """
    预处理阶乘和阶乘逆元
    
    算法核心思想：
    1. 正向计算阶乘数组
    2. 反向计算阶乘逆元数组，利用费马小定理
    
    Args:
        n: 阶乘上限
        p: 模数（通常为质数）
        
    Returns:
        (fact, inv_fact): 阶乘数组和阶乘逆元数组
    
    Time Complexity: O(n)
    Space Complexity: O(n)
    
    工程化考量：
    - 参数验证：n必须非负，p必须大于1
    - 模运算优化：每一步都进行模运算防止数值过大
    """
    # 参数验证
    if n < 0 or p <= 1:
        return [], []
    
    fact = [1] * (n + 1)
    for i in range(1, n + 1):
        fact[i] = fact[i - 1] * i % p
    
    inv_fact = [1] * (n + 1)
    inv_fact[n] = power(fact[n], p - 2, p)
    for i in range(n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % p
    
    return fact, inv_fact


def combination(n, k, fact, inv_fact, p):
    """
    计算组合数C(n, k) mod p
    使用预处理阶乘和逆元的方法
    
    算法核心思想：
    利用公式 C(n, k) = n! / (k! * (n-k)!)
    在模运算中使用逆元代替除法
    
    Args:
        n: 组合数上标
        k: 组合数下标
        fact: 阶乘数组
        inv_fact: 阶乘逆元数组
        p: 模数
        
    Returns:
        C(n, k) mod p
        
    Time Complexity: O(1) 查询
    Space Complexity: O(n) 预处理空间
    
    工程化考量：
    - 参数验证：检查k的范围
    - 边界情况：处理n < k或k < 0的情况
    """
    if k > n or k < 0:
        return 0
    # 确保fact和inv_fact数组长度足够
    if n >= len(fact) or k >= len(inv_fact) or (n - k) >= len(inv_fact):
        raise ValueError("预处理数组长度不足")
    return (fact[n] * inv_fact[k] % p) * inv_fact[n - k] % p


def max_nice_divisors(prime_factors):
    """
    LeetCode 1808. Maximize Number of Nice Divisors 题目实现
    题目链接: https://leetcode.cn/problems/maximize-number-of-nice-divisors/
    
    题目描述:
    给你一个正整数 primeFactors 。你需要构造一个正整数 n ，它满足以下条件：
    1. n 质因数（质因数需要考虑重复的情况）的数目 不超过 primeFactors 个。
    2. n 好因子的数目最大化。
    
    解题思路:
    - 将问题转化为：将primeFactors分解为若干个正整数的和，使得这些数的乘积最大
    - 数学上，将数尽可能分解为3的幂次能得到最大乘积
    - 特殊情况处理：当余数为1时，使用一个4代替两个3
    
    算法本质：
    利用模逆元进行大数运算中的快速幂计算
    
    Args:
        prime_factors: 质因数的数目上限
        
    Returns:
        好因子的最大数目模 10^9 + 7
    
    Time Complexity: O(log(prime_factors)) - 主要是快速幂的复杂度
    Space Complexity: O(1)
    
    这是最优解，因为我们利用了数学规律直接得到了最优分解方式
    """
    # 特殊情况处理 - 边界条件优化
    if prime_factors <= 3:
        return prime_factors
    
    remainder = prime_factors % 3
    quotient = prime_factors // 3
    
    if remainder == 0:
        # 全部用3
        return power(3, quotient, MOD)
    elif remainder == 1:
        # 用一个4代替两个3
        return (power(3, quotient - 1, MOD) * 4) % MOD
    else:  # remainder == 2
        # 用一个2
        return (power(3, quotient, MOD) * 2) % MOD


def solve_zoj_3609():
    """
    ZOJ 3609 Modular Inverse 题目实现
    题目链接: http://acm.zju.edu.cn/onlinejudge/showProblem.do?problemCode=3609
    
    题目描述:
    给定两个整数a和m，求a在模m意义下的乘法逆元x，使得 a*x ≡ 1 (mod m)
    如果不存在这样的x，输出"Not Exist"
    
    解题思路:
    使用扩展欧几里得算法求解模逆元
    
    Time Complexity: O(T * log(min(a, m))), T为测试用例数量
    Space Complexity: O(1)
    """
    t = int(input())
    
    for _ in range(t):
        a, m = map(int, input().split())
        
        result = mod_inverse_extended_gcd(a, m)
        if result == -1:
            print("Not Exist")
        else:
            print(result)


def sum_of_divisors(A, B):
    """
    POJ 1845 Sumdiv 题目实现
    题目链接: http://poj.org/problem?id=1845
    
    题目描述:
    计算 A^B 的所有约数的和，并对结果取模 9901
    
    解题思路:
    1. 质因数分解：将A分解为质因数的乘积 A = p1^a1 * p2^a2 * ... * pn^an
    2. A^B = p1^(a1*B) * p2^(a2*B) * ... * pn^(an*B)
    3. 约数和公式：S = (1 + p1 + p1^2 + ... + p1^(a1*B)) * ... * (1 + pn + pn^2 + ... + pn^(an*B))
    4. 使用等比数列求和公式：1 + p + p^2 + ... + p^k = (p^(k+1) - 1) / (p - 1)
    5. 使用模逆元计算除法
    
    算法本质：
    质因数分解 + 等比数列求和 + 模逆元应用
    
    Args:
        A: 底数
        B: 指数
        
    Returns:
        A^B的所有约数和模9901的结果
    
    Time Complexity: O(sqrt(A) + log k)，其中k是最大的指数
    Space Complexity: O(1)
    
    这是最优解，因为质因数分解已经是最优的，等比数列求和使用二分法也达到了对数级别
    """
    MOD = 9901
    result = 1
    
    # 质因数分解A
    p = 2
    while p * p <= A:
        if A % p == 0:
            exponent = 0
            while A % p == 0:
                exponent += 1
                A //= p
            # 计算(1 + p + p^2 + ... + p^(exponent*B)) mod MOD
            k = exponent * B
            
            if (p - 1) % MOD == 0:
                # 特殊情况：p ≡ 1 mod MOD
                result = (result * (k + 1)) % MOD
            else:
                # 使用费马小定理求逆元，因为MOD是质数
                numerator = (power(p, k + 1, MOD) - 1 + MOD) % MOD
                denominator = power(p - 1, MOD - 2, MOD)
                result = (result * numerator % MOD) * denominator % MOD
        p += 1
    
    # 处理A可能剩下的质因数
    if A > 1:
        k = B
        if (A - 1) % MOD == 0:
            result = (result * (k + 1)) % MOD
        else:
            numerator = (power(A, k + 1, MOD) - 1 + MOD) % MOD
            denominator = power(A - 1, MOD - 2, MOD)
            result = (result * numerator % MOD) * denominator % MOD
    
    return result


def count_full_binary_trees(n):
    """
    LeetCode 1623. All Possible Full Binary Trees 题目实现
    题目链接: https://leetcode.cn/problems/all-possible-full-binary-trees/
    
    题目描述:
    给你一个整数n，请返回所有可能的满二叉树结构，其中满二叉树的定义是：每个节点要么有两个子节点，要么没有子节点。
    （注：本题实际不是直接使用模逆元，但可以使用卡特兰数的概念来理解）
    
    这里我们实现一个简化版本，仅计算满二叉树的数量，并使用模运算
    
    解题思路:
    - 动态规划：dp[n] 表示使用n个节点能构造的满二叉树数量
    - 状态转移方程：dp[n] = sum(dp[i] * dp[n-1-i])，其中i从1到n-2，步长为2
    - 因为满二叉树的节点数必须是奇数，所以只处理奇数的n
    
    算法本质：
    卡特兰数的一种变体计算
    
    Args:
        n: 节点数量
        
    Returns:
        满二叉树的数量模10^9+7的结果
    
    Time Complexity: O(n^2)
    Space Complexity: O(n)
    
    这是最优解，因为动态规划已经达到了多项式时间复杂度
    """
    MOD = 1000000007
    
    # 特殊情况：n必须是奇数，且至少为1
    if n % 2 == 0 or n < 1:
        return 0
    
    dp = [0] * (n + 1)
    dp[1] = 1  # 基础情况
    
    for i in range(3, n + 1, 2):
        for j in range(1, i, 2):
            dp[i] = (dp[i] + dp[j] * dp[i - 1 - j]) % MOD
    
    return dp[n]


def divide_and_sum(a, n):
    """
    Codeforces 1445D. Divide and Sum 题目实现
    题目链接: https://codeforces.com/problemset/problem/1445/D
    
    题目描述:
    给定一个长度为2n的数组，将其分成两个长度为n的数组s和t。
    对s进行升序排序，对t进行降序排序。
    计算所有可能的分割方式对应的|s[i] - t[i]|之和的总和。
    
    解题思路:
    1. 首先对整个数组排序
    2. 结论：对于排序后的数组，最优的分割方式是s取前n个元素，t取后n个元素
    3. 对于每一种分割方式，总和为sum_{i=n}^{2n-1} a[i] * C(2n-2-i+n-1, n-1) - sum_{i=0}^{n-1} a[i] * C(i+n-1, n-1)
    4. 使用组合数计算和模逆元优化
    
    算法本质：
    组合数学 + 前缀和 + 模逆元优化
    
    Args:
        a: 输入数组
        n: 分割后的每个数组的长度
        
    Returns:
        所有分割方式的总和模998244353的结果
    
    Time Complexity: O(n)
    Space Complexity: O(n)
    
    这是最优解，因为我们通过数学分析将问题简化为O(n)的计算
    """
    MOD = 998244353
    
    # 排序数组
    a.sort()
    
    # 预处理阶乘和阶乘的逆元
    fact = [1] * (2 * n + 1)
    for i in range(1, 2 * n + 1):
        fact[i] = fact[i - 1] * i % MOD
    
    inv_fact = [1] * (2 * n + 1)
    inv_fact[2 * n] = power(fact[2 * n], MOD - 2, MOD)
    for i in range(2 * n - 1, -1, -1):
        inv_fact[i] = inv_fact[i + 1] * (i + 1) % MOD
    
    # 计算组合数C(k, r)
    def comb(k, r):
        if r < 0 or r > k:
            return 0
        return fact[k] * inv_fact[r] % MOD * inv_fact[k - r] % MOD
    
    result = 0
    for i in range(n):
        # 计算组合数C(n-1+i, i) = C(n-1+i, n-1)
        c = comb(n - 1 + i, i)
        # 前n个元素的贡献是负的，后n个元素的贡献是正的
        result = (result - a[i] * c % MOD + a[i + n] * c % MOD + MOD) % MOD
    
    return result


def solve_luogu_p3811():
    """
    洛谷 P3811 【模板】乘法逆元 题目实现
    题目链接: https://www.luogu.com.cn/problem/P3811
    
    题目描述:
    给定 n, p 求 1∼n 中所有整数在模 p 意义下的乘法逆元。
    
    解题思路:
    使用线性递推方法高效计算1~n的逆元
    
    Time Complexity: O(n)
    Space Complexity: O(n)
    """
    n, p = map(int, input().split())
    inv = build_inverse_all(n, p)
    for i in range(1, n + 1):
        print(inv[i])


def main():
    """主函数，测试各种模逆元求解方法和相关题目"""
    print("=== 模逆元求解方法测试 ===")
    
    # 测试扩展欧几里得算法求模逆元
    test_cases_extended_gcd = [(3, 11), (2, 5), (4, 7), (2, 4)]
    for a, mod in test_cases_extended_gcd:
        result = mod_inverse_extended_gcd(a, mod)
        if result == -1:
            print(f"扩展欧几里得算法: {a} 在模 {mod} 意义下没有逆元")
        else:
            print(f"扩展欧几里得算法: {a} 在模 {mod} 意义下的逆元是 {result}")
    
    # 测试费马小定理求模逆元
    test_cases_fermat = [(5, 13), (3, 7), (2, 17)]
    for a, p in test_cases_fermat:
        result = mod_inverse_fermat(a, p)
        if result == -1:
            print(f"费马小定理: {a} 在模 {p} 意义下没有逆元")
        else:
            print(f"费马小定理: {a} 在模 {p} 意义下的逆元是 {result}")
    
    # 测试线性递推求所有逆元
    n, p = 10, 11
    inv = build_inverse_all(n, p)
    print(f"\n线性递推求1~{n}在模{p}意义下的逆元:")
    for i in range(1, n + 1):
        print(f"inv[{i}] = {inv[i]}")
    
    # 测试LeetCode 1808题目
    print("\n=== LeetCode 1808测试 ===")
    test_cases_1808 = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    for prime_factors in test_cases_1808:
        result = max_nice_divisors(prime_factors)
        print(f"primeFactors = {prime_factors}, max nice divisors = {result}")
    
    # 测试组合数计算
    print("\n=== 组合数计算测试 ===")
    n2, k = 10, 3
    fact, inv_fact = preprocess_factorial(n2, MOD)
    comb = combination(n2, k, fact, inv_fact, MOD)
    print(f"C({n2}, {k}) mod {MOD} = {comb}")
    
    # 测试POJ 1845题目
    print("\n=== POJ 1845测试 ===")
    test_cases_1845 = [(2, 3), (4, 2), (3, 4)]
    for A, B in test_cases_1845:
        result = sum_of_divisors(A, B)
        print(f"A = {A}, B = {B}, sum of divisors mod 9901 = {result}")
    
    # 测试满二叉树数量计算
    print("\n=== LeetCode 1623测试（简化版）===")
    test_cases_trees = [1, 3, 5, 7, 9]
    for nodes in test_cases_trees:
        result = count_full_binary_trees(nodes)
        print(f"nodes = {nodes}, full binary trees count = {result}")
    
    # 测试Codeforces 1445D题目
    print("\n=== Codeforces 1445D测试 ===")
    test_cases_divide_sum = [
        ([1, 2, 3, 4], 2),
        ([1, 1, 1, 1], 2),
        ([1, 2, 3, 4, 5, 6], 3)
    ]
    for a, n in test_cases_divide_sum:
        result = divide_and_sum(a, n)
        print(f"array = {a}, n = {n}, sum = {result}")
    
    # 测试边界情况和异常场景
    print("\n=== 边界情况和异常场景测试 ===")
    # 逆元不存在的情况
    print(f"逆元不存在测试 - 2 mod 4: {mod_inverse_extended_gcd(2, 4)}")
    # 参数验证测试
    print(f"参数验证测试 - 0 mod 5: {mod_inverse_extended_gcd(0, 5)}")
    print(f"参数验证测试 - 3 mod 1: {mod_inverse_extended_gcd(3, 1)}")
    # 大数测试
    print(f"大数测试 - 123456789 mod 1000000007: {mod_inverse_extended_gcd(123456789, 1000000007)}")


if __name__ == "__main__":
    main()


if __name__ == "__main__":
    main()
# 生成函数和组合计数（Burnside引理/Polya定理）的Python实现
# 时间复杂度：根据具体问题而定
# 空间复杂度：根据具体问题而定

import math
from typing import List, Dict, Tuple, Set

# 生成函数 - 多项式乘法（普通生成函数）
def multiply_polynomials(a: List[int], b: List[int]) -> List[int]:
    """
    多项式乘法（普通生成函数）
    时间复杂度：O(n²)
    空间复杂度：O(n)
    
    参数：
    a -- 第一个多项式的系数列表
    b -- 第二个多项式的系数列表
    
    返回：
    乘积多项式的系数列表
    """
    res = [0] * (len(a) + len(b) - 1)
    for i in range(len(a)):
        for j in range(len(b)):
            res[i + j] += a[i] * b[j]
    return res

# 生成函数 - 计算组合数 C(n, k)
def compute_combinations(n: int, k: int) -> List[List[int]]:
    """
    使用动态规划计算组合数 C(n, k)
    时间复杂度：O(n*k)
    空间复杂度：O(n*k)
    
    参数：
    n -- 总数
    k -- 选择的数量
    
    返回：
    组合数表 C[i][j] = C(i, j)
    """
    C = [[0] * (k + 1) for _ in range(n + 1)]
    for i in range(n + 1):
        C[i][0] = 1
        for j in range(1, min(i, k) + 1):
            C[i][j] = C[i-1][j-1] + C[i-1][j]
    return C

# 快速幂算法
def power(a: int, b: int) -> int:
    """
    快速幂算法
    时间复杂度：O(log b)
    空间复杂度：O(1)
    """
    res = 1
    while b > 0:
        if b % 2 == 1:
            res *= a
        a *= a
        b //= 2
    return res

# 快速幂算法（模运算）
def power_mod(a: int, b: int, mod: int) -> int:
    """
    快速幂算法（模运算）
    时间复杂度：O(log b)
    空间复杂度：O(1)
    """
    res = 1
    a %= mod
    while b > 0:
        if b % 2 == 1:
            res = (res * a) % mod
        a = (a * a) % mod
        b //= 2
    return res

# 计算欧拉函数 φ(n)
def euler_phi(n: int) -> int:
    """
    计算欧拉函数 φ(n)
    时间复杂度：O(√n)
    空间复杂度：O(1)
    
    参数：
    n -- 输入整数
    
    返回：
    欧拉函数值
    """
    res = n
    # 遍历所有可能的质因数
    for p in range(2, int(math.isqrt(n)) + 1):
        if n % p == 0:
            # p是一个质因数
            while n % p == 0:
                n //= p
            res -= res // p
    # 如果n还有大于sqrt(n)的质因数
    if n > 1:
        res -= res // n
    return res

# 扩展欧几里得算法
def extended_gcd(a: int, b: int) -> Tuple[int, int, int]:
    """
    扩展欧几里得算法
    时间复杂度：O(log min(a, b))
    空间复杂度：O(1)
    
    返回：
    (gcd, x, y) 满足 gcd = a*x + b*y
    """
    if b == 0:
        return a, 1, 0
    gcd, x1, y1 = extended_gcd(b, a % b)
    x = y1
    y = x1 - (a // b) * y1
    return gcd, x, y

# 模逆元
def mod_inverse(a: int, mod: int) -> int:
    """
    计算模逆元
    时间复杂度：O(log min(a, mod))
    空间复杂度：O(1)
    
    参数：
    a -- 整数
    mod -- 模数
    
    返回：
    模逆元，如果不存在返回-1
    """
    gcd, x, _ = extended_gcd(a, mod)
    if gcd != 1:
        return -1  # 不存在逆元
    return (x % mod + mod) % mod

# Burnside引理：计算等价类的数量
def burnside(m: int, fixed_points: List[int]) -> int:
    """
    Burnside引理：计算等价类的数量
    时间复杂度：O(m)
    空间复杂度：O(1)
    
    参数：
    m -- 置换群的大小
    fixed_points -- 每个置换的不动点数目列表
    
    返回：
    等价类数目
    """
    return sum(fixed_points) // m

# Polya定理：计算涂色方案数
def polya(n: int, k: int, rotations: List[int]) -> int:
    """
    Polya定理：计算涂色方案数
    时间复杂度：O(|rotations|)
    空间复杂度：O(1)
    
    参数：
    n -- 物体数量
    k -- 颜色数量
    rotations -- 旋转置换的循环数列表
    
    返回：
    不同的涂色方案数
    """
    return sum(power(k, cycles) for cycles in rotations) // len(rotations)

# 项链问题：计算用k种颜色涂色n个珠子的项链的不同方案数
def necklace(n: int, k: int) -> int:
    """
    项链问题：考虑旋转等价
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    total = 0
    for d in range(1, n + 1):
        if n % d == 0:
            total += euler_phi(d) * power(k, n // d)
    return total // n

# 手镯问题：计算用k种颜色涂色n个珠子的手镯的不同方案数
def bracelet(n: int, k: int) -> int:
    """
    手镯问题：考虑旋转和翻转等价
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    # 旋转等价部分
    total = 0
    for d in range(1, n + 1):
        if n % d == 0:
            total += euler_phi(d) * power(k, n // d)
    
    # 翻转等价部分
    if n % 2 == 0:
        # 偶数情况
        total += (n // 2) * power(k, n // 2 + 1)  # 经过两个珠子的翻转
        total += (n // 2) * power(k, n // 2)      # 经过两个对中心点的翻转
    else:
        # 奇数情况
        total += n * power(k, (n + 1) // 2)  # 经过一个珠子和一个对中心点的翻转
    
    return total // (2 * n)

# 指数生成函数乘法
def multiply_exponential(a: List[float], b: List[float]) -> List[float]:
    """
    指数生成函数乘法
    时间复杂度：O(n²)
    空间复杂度：O(n)
    """
    res = [0.0] * (len(a) + len(b) - 1)
    for i in range(len(a)):
        for j in range(len(b)):
            res[i + j] += a[i] * b[j]
    return res

# 计算阶乘和阶乘的逆元
def compute_factorials(n: int, mod: int) -> Tuple[List[int], List[int]]:
    """
    计算阶乘和阶乘的逆元
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    fact = [1] * (n + 1)
    for i in range(1, n + 1):
        fact[i] = (fact[i-1] * i) % mod
    
    inv_fact = [1] * (n + 1)
    inv_fact[n] = power_mod(fact[n], mod - 2, mod)
    for i in range(n-1, -1, -1):
        inv_fact[i] = (inv_fact[i+1] * (i+1)) % mod
    
    return fact, inv_fact

# 组合数 C(n, k) 模运算
def comb_mod(n: int, k: int, fact: List[int], inv_fact: List[int], mod: int) -> int:
    """
    计算组合数 C(n, k) 模 mod
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    if k < 0 or k > n:
        return 0
    return fact[n] * inv_fact[k] % mod * inv_fact[n - k] % mod

# 打印多项式
def print_polynomial(poly: List[int], name: str) -> None:
    """
    打印多项式
    """
    print(f"{name}: ", end="")
    terms = []
    for i, coeff in enumerate(poly):
        if coeff == 0:
            continue
        term = ""
        if coeff < 0:
            term += "-"
            coeff = -coeff
        elif terms:
            term += "+"
        
        if coeff != 1 or i == 0:
            term += str(coeff)
        
        if i > 0:
            term += f"x^{i}"
        terms.append(term)
    
    print(" ".join(terms))

# 力扣第1758题：生成交替二进制字符串的最少操作次数
def min_changes(s: str) -> int:
    """
    生成交替二进制字符串的最少操作次数
    时间复杂度：O(n)
    空间复杂度：O(1)
    
    参数：
    s -- 输入字符串
    
    返回：
    最少操作次数
    """
    changes_start_0 = 0  # 以0开头的交替字符串需要的最少修改次数
    changes_start_1 = 0  # 以1开头的交替字符串需要的最少修改次数
    
    for i, c in enumerate(s):
        if i % 2 == 0:
            # 偶数位置
            if c == '1':
                changes_start_0 += 1
            else:
                changes_start_1 += 1
        else:
            # 奇数位置
            if c == '0':
                changes_start_0 += 1
            else:
                changes_start_1 += 1
    
    return min(changes_start_0, changes_start_1)

# 力扣第46题：全排列
def permute(nums: List[int]) -> List[List[int]]:
    """
    全排列
    时间复杂度：O(n * n!)
    空间复杂度：O(n)
    
    参数：
    nums -- 输入数组
    
    返回：
    所有可能的全排列
    """
    result = []
    used = [False] * len(nums)
    
    def backtrack(current: List[int]):
        if len(current) == len(nums):
            result.append(current.copy())
            return
        
        for i in range(len(nums)):
            if used[i]:
                continue
            used[i] = True
            current.append(nums[i])
            backtrack(current)
            current.pop()
            used[i] = False
    
    backtrack([])
    return result

# 力扣第77题：组合
def combine(n: int, k: int) -> List[List[int]]:
    """
    组合
    时间复杂度：O(C(n,k) * k)
    空间复杂度：O(k)
    
    参数：
    n -- 总数范围 [1, n]
    k -- 选择的数量
    
    返回：
    所有可能的组合
    """
    result = []
    
    def backtrack(start: int, current: List[int]):
        if len(current) == k:
            result.append(current.copy())
            return
        
        # 剪枝：i的上界可以优化为 n - (k - len(current)) + 1
        for i in range(start, n - (k - len(current)) + 2):
            current.append(i)
            backtrack(i + 1, current)
            current.pop()
    
    backtrack(1, [])
    return result

# 力扣第363题：矩形区域不超过 K 的最大数值和
def max_sum_submatrix(matrix: List[List[int]], k: int) -> float:
    """
    矩形区域不超过 K 的最大数值和
    时间复杂度：O(m² * n log n)
    空间复杂度：O(n)
    
    参数：
    matrix -- 二维整数矩阵
    k -- 目标值
    
    返回：
    不超过k的最大矩形和
    """
    if not matrix or not matrix[0]:
        return 0
    
    import bisect
    
    m, n = len(matrix), len(matrix[0])
    max_sum = -float('inf')
    
    # 枚举左右边界
    for left in range(n):
        row_sum = [0] * m  # 记录每一行在当前左右边界内的元素和
        for right in range(left, n):
            # 更新每行的和
            for i in range(m):
                row_sum[i] += matrix[i][right]
            
            # 计算前缀和
            prefix_sum = [0]
            curr_sum = 0
            for num in row_sum:
                curr_sum += num
                # 查找前缀和中是否存在 curr_sum - k
                idx = bisect.bisect_left(prefix_sum, curr_sum - k)
                if idx < len(prefix_sum):
                    max_sum = max(max_sum, curr_sum - prefix_sum[idx])
                # 将当前前缀和加入列表
                bisect.insort(prefix_sum, curr_sum)
    
    return max_sum

# 主函数 - 测试代码
def main():
    # 测试多项式乘法（普通生成函数）
    print("=== 普通生成函数测试 ===")
    a = [1, 2, 3]  # 1 + 2x + 3x^2
    b = [4, 5, 6]  # 4 + 5x + 6x^2
    
    print_polynomial(a, "多项式A")
    print_polynomial(b, "多项式B")
    
    product = multiply_polynomials(a, b)
    print_polynomial(product, "乘积")
    
    # 测试组合数计算
    print("\n=== 组合数计算测试 ===")
    n, k = 5, 3
    C = compute_combinations(n, k)
    print(f"C(5, 3) = {C[5][3]}")
    
    # 测试Burnside引理
    print("\n=== Burnside引理测试 ===")
    fixed_points = [4, 0, 0, 0]  # 正方形的4个旋转置换的不动点数
    equivalence_classes = burnside(4, fixed_points)
    print(f"等价类数目（正方形旋转）: {equivalence_classes}")
    
    # 测试Polya定理
    print("\n=== Polya定理测试 ===")
    rotations = [4, 1, 2, 1]  # 正方形的4个旋转置换的循环数
    colorings = polya(4, 2, rotations)
    print(f"用2种颜色给正方形顶点涂色的方案数: {colorings}")
    
    # 测试项链问题
    print("\n=== 项链问题测试 ===")
    beads, colors = 5, 3
    necklace_count = necklace(beads, colors)
    bracelet_count = bracelet(beads, colors)
    print(f"{beads}个珠子，{colors}种颜色的项链方案数: {necklace_count}")
    print(f"{beads}个珠子，{colors}种颜色的手镯方案数: {bracelet_count}")
    
    # 测试力扣第1758题
    print("\n=== 力扣第1758题测试 ===")
    test_cases = [
        ("0100", 1),
        ("10", 0),
        ("1111", 2)
    ]
    for s, expected in test_cases:
        result = min_changes(s)
        print(f"输入: '{s}'，最少操作次数: {result} (期望: {expected}, {'✓' if result == expected else '✗'})")
    
    # 测试力扣第46题
    print("\n=== 力扣第46题测试 ===")
    nums = [1, 2, 3]
    permutations = permute(nums)
    print(f"全排列结果: {permutations}")
    print(f"全排列数量: {len(permutations)}")
    
    # 测试力扣第77题
    print("\n=== 力扣第77题测试 ===")
    combinations = combine(4, 2)
    print(f"组合结果: {combinations}")
    print(f"组合数量: {len(combinations)}")
    
    # 测试力扣第363题
    print("\n=== 力扣第363题测试 ===")
    matrix = [
        [1, 0, 1],
        [0, -2, 3]
    ]
    k_value = 2
    result = max_sum_submatrix(matrix, k_value)
    print(f"矩阵: {matrix}")
    print(f"k = {k_value}")
    print(f"最大矩形和: {result}")
    
    """
    生成函数和组合计数算法总结：
    
    1. 普通生成函数：
       - 用于计数组合问题，如物品选择、整数分拆等
       - 多项式乘法对应组合的合并
       - 时间复杂度：多项式乘法O(n²)，可以使用FFT优化到O(n log n)
       - 空间复杂度：O(n)
    
    2. 指数生成函数：
       - 用于排列问题，考虑顺序的组合
       - 乘法规则与普通生成函数不同
       - 适用于有顺序要求的计数问题
    
    3. Burnside引理：
       - 计算群作用下的等价类数目
       - 公式：等价类数目 = (1/|G|) * Σ(不动点数目)
       - 适用于解决对称性计数问题
    
    4. Polya定理：
       - Burnside引理的特例，针对置换群作用下的计数问题
       - 特别适用于涂色问题
       - 公式：方案数 = (1/|G|) * Σ(k^c(π))，其中c(π)是置换π的循环数
    
    应用场景：
    1. 组合数学中的计数问题
    2. 离散数学中的群论应用
    3. 密码学中的哈希函数设计
    4. 计算机图形学中的对称性检测
    5. 分子生物学中的序列分析
    6. 机器学习中的特征组合和概率建模
    
    相关题目：
    1. 力扣第77题：组合 - 组合问题
    2. 力扣第46题：全排列 - 排列问题
    3. Burnside引理/Polya定理相关问题 - 对称计数问题
    
    优化方向：
    1. 多项式乘法可以使用FFT加速
    2. 大规模数据下的阶乘计算可以使用快速幂和模运算
    3. 对于重复子问题，可以使用记忆化搜索优化
    """

if __name__ == "__main__":
    main()
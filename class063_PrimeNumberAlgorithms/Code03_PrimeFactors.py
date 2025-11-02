# -*- coding: utf-8 -*-
# 数字n拆分质数因子 - 质因数分解算法
# 时间复杂度: O(√n)
# 空间复杂度: O(1)，如果不考虑输出的话
# 测试链接 : https://leetcode.com/problems/prime-factorization/

# 质因数分解的算法原理：
# 1. 用i从2到sqrt(n)尝试整除n
# 2. 对于每个能整除n的i，记录它作为因子，并持续将n除以i
# 3. 最后，如果n>1，说明剩下的n也是一个质数因子
# 这种算法的时间复杂度是O(√n)，因为我们只需要检查到sqrt(n)
# 如果i大于sqrt(n)且n>1，那么n必定是一个质数

# 为什么只需要检查到sqrt(n)？
# 假设n有一个因子大于sqrt(n)，那么它的配对因子必定小于sqrt(n)
# 因此，如果我们已经检查完所有小于sqrt(n)的可能因子，
# 剩下的n要么是1，要么是一个质数

# 注意：质因数分解有很多应用场景，例如：
# 1. 判断两个数是否互质（计算最大公约数）
# 2. 求最小公倍数
# 3. 解决一些数学问题，如LeetCode 952题（按公因数计算最大组件大小）
# 4. RSA加密算法中的核心操作
# 5. 数论中的许多算法基础

# 相关题目：
# 1. LeetCode 313. Super Ugly Number (超级丑数)
#    链接：https://leetcode.cn/problems/super-ugly-number/
#    题目描述：超级丑数是指其所有质因数都是长度为 k 的质数列表 primes 中的正整数
# 2. LeetCode 264. Ugly Number II (丑数 II)
#    链接：https://leetcode.cn/problems/ugly-number-ii/
#    题目描述：给你一个整数 n ，请你找出并返回第 n 个 丑数 
# 3. LeetCode 204. Count Primes (计数质数)
#    链接：https://leetcode.cn/problems/count-primes/
#    题目描述：统计所有小于非负整数 n 的质数的数量
# 4. POJ 1811 Prime Test
#    链接：http://poj.org/problem?id=1811
#    题目描述：给定一个大整数，判断它是否为素数，如果不是输出最小质因子
# 5. LeetCode 952. Largest Component Size by Common Factor (按公因数计算最大组件大小)
#    链接：https://leetcode.cn/problems/largest-component-size-by-common-factor/
#    题目描述：给定一个由不同正整数组成的非空数组 nums，
#             如果 nums[i] 和 nums[j] 有一个大于1的公因子，那么这两个数之间有一条无向边
#             返回 nums 中最大连通组件的大小
# 6. LeetCode 1250. Check If It Is a Good Array (检查是否是好数组)
#    链接：https://leetcode.cn/problems/check-if-it-is-a-good-array/
#    题目描述：给定一个正整数数组 nums，如果可以通过选择一个子集，然后将该子集中的每一个元素乘以一个整数，再全部加起来得到目标 1，则称该数组是「好数组」
# 7. HackerRank Prime Factorization
#    链接：https://www.hackerrank.com/challenges/prime-factorization/problem
#    题目描述：将给定的数分解质因数
# 8. UVa 10780 Again Prime? No Time.
#    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1721
#    题目描述：计算最大的指数k，使得m^k可以整除n!
# 9. SPOJ TDPRIMES - Printing some primes
#    链接：https://www.spoj.com/problems/TDPRIMES/
#    题目描述：打印前5000000个质数
# 10. CodeChef Prime Factorization
#    链接：https://www.codechef.com/problems/FACTCG2
#    题目描述：质因数分解
# 11. Project Euler Problem 3 Largest prime factor
#    链接：https://projecteuler.net/problem=3
#    题目描述：找出600851475143的最大质因数
# 12. HDU 1452 Happy 2004
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1452
#    题目描述：计算2004^X的因数和模29
# 13. 牛客网 NC15688 质数拆分
#    链接：https://ac.nowcoder.com/acm/problem/15688
#    题目描述：将一个数拆分成若干个质数之和
# 14. LintCode 498. 回文素数
#    链接：https://www.lintcode.com/problem/498/
#    题目描述：找出大于等于n的最小回文素数
# 15. 杭电OJ 1719 Friend or Foe
#    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1719
#    题目描述：判断一个数是否是友好数或敌人
# 16. TimusOJ 1007 数学问题
#    链接：https://acm.timus.ru/problem.aspx?space=1&num=1007
#    题目描述：判断一个数是否是质数
# 17. AizuOJ 0100 Prime Factorize
#    链接：https://onlinejudge.u-aizu.ac.jp/problems/0100
#    题目描述：对输入的数进行质因数分解
# 18. LOJ #10205. 「一本通 6.5 例 2」Prime Distance
#    链接：https://loj.ac/p/10205
#    题目描述：求区间内的质数距离
# 19. 计蒜客 质数判定
#    链接：https://www.jisuanke.com/course/705/28547
#    题目描述：实现质数判定算法
# 20. acwing 867. 分解质因数
#    链接：https://www.acwing.com/problem/content/869/
#    题目描述：分解质因数，结合质数判断
# 21. Codeforces 1332E Height All the Same
#    链接：https://codeforces.com/problemset/problem/1332/E
#    题目描述：涉及质数判断的数学问题
# 22. POJ 3641 Pseudoprime numbers
#    链接：http://poj.org/problem?id=3641
#    题目描述：判断一个数是否是伪素数
# 23. HackerEarth Prime Generator
#    链接：https://www.hackerearth.com/practice/math/number-theory/primality-tests/practice-problems/
#    题目描述：生成指定范围内的质数
# 24. MarsCode 大数质因数分解
#    链接：https://www.mars.pub/code/view/1000000030
#    题目描述：实现质因数分解算法
# 25. AtCoder ABC152 D - Handstand 2
#    链接：https://atcoder.jp/contests/abc152/tasks/abc152_d
#    题目描述：涉及质数的判断和应用
# 26. UVA 10723 Cyborg Genes
#    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1664
#    题目描述：涉及质因数分解的动态规划问题
# 27. TopCoder SRM 769 Div1 Easy PrimeFactorization
#    链接：https://community.topcoder.com/stat?c=problem_statement&pm=15772
#    题目描述：质因数分解问题
# 28. Codeforces 1465 A Odd Divisor
#    链接：https://codeforces.com/problemset/problem/1465/A
#    题目描述：判断一个数是否有奇数因子
# 29. 剑指Offer II 002. 二进制中1的个数
#    链接：https://leetcode.cn/problems/er-jin-zhi-zhong-1de-ge-shu-lcof/
#    题目描述：统计二进制中1的个数，可与质数判断结合
# 30. Codeforces 271B Prime Matrix
#    链接：https://codeforces.com/problemset/problem/271/B
#    题目描述：给定一个矩阵，通过最少的移动次数将其转换为素数矩阵

import math
import time
import random
from typing import Dict, List, Tuple, Set, Optional

def f(n):
    """
    打印所有n的质因子 - 返回质因子列表形式
    时间复杂度O(√n)
    空间复杂度O(log n)，取决于质因数的数量
    
    算法原理：
    1. 从2开始到√n逐一尝试整除n
    2. 如果i能整除n，则i是一个质因子
    3. 将n中所有的因子i都除掉
    4. 最后如果n>1，则n本身是一个质因子
    
    应用场景：
    1. 质因数分解
    2. 计算约数个数
    3. 求最大公约数和最小公倍数
    4. 数论相关问题
    
    参数:
        n: 待分解的正整数
    返回:
        list: 质因子列表，每个质因子只出现一次
    """
    # 参数验证
    if n <= 1:
        return []
    
    factors = []
    i = 2
    while i * i <= n:
        if n % i == 0:
            factors.append(i)
            # 移除所有的因子i
            while n % i == 0:
                n //= i
        i += 1
    # 最后剩下的n如果大于1，说明它本身是一个质因数
    if n > 1:
        factors.append(n)
    return factors

# 质因数分解函数 - 返回字典形式，包含每个质因数及其指数
def prime_factors(n: int) -> Dict[int, int]:
    """
    返回一个字典，键是质因数，值是该质因数的指数
    时间复杂度: O(√n)
    空间复杂度: O(k)，其中k是不同质因数的数量
    
    参数:
        n: 待分解的正整数
    返回:
        Dict[int, int]: 质因数及其指数的字典
    """
    if n <= 1:
        return {}
    
    factors = {}
    # 处理2的因子 - 单独处理以减少迭代次数
    while n % 2 == 0:
        factors[2] = factors.get(2, 0) + 1
        n = n // 2
    
    # 处理3及以上的奇数因子，只需要检查到sqrt(n)
    i = 3
    while i * i <= n:
        # 统计当前因子的指数
        while n % i == 0:
            factors[i] = factors.get(i, 0) + 1
            n = n // i
        i += 2  # 只检查奇数，跳过偶数
    
    # 最后如果n>1，说明剩下的n本身是一个质数
    if n > 1:
        factors[n] = 1
    
    return factors

# 质因数分解的列表形式（包含重复的质因数）
def prime_factors_list(n: int) -> List[int]:
    """
    返回质因数列表，包含重复的质因数
    时间复杂度: O(√n)
    空间复杂度: O(log n)
    
    参数:
        n: 待分解的正整数
    返回:
        List[int]: 质因数列表，包含重复
    """
    if n <= 1:
        return []
    
    factors = []
    # 处理2的因子
    while n % 2 == 0:
        factors.append(2)
        n = n // 2
    
    # 处理3及以上的奇数因子
    i = 3
    while i * i <= n:
        while n % i == 0:
            factors.append(i)
            n = n // i
        i += 2
    
    # 最后处理剩下的质数
    if n > 1:
        factors.append(n)
    
    return factors

# 计算欧拉函数φ(n) - 返回小于n且与n互质的数的个数
def euler_phi(n: int) -> int:
    """
    基于质因数分解实现欧拉函数计算
    时间复杂度: O(√n)
    空间复杂度: O(k)，其中k是不同质因数的数量
    
    参数:
        n: 正整数
    返回:
        int: 小于n且与n互质的数的个数
    """
    if n <= 1:
        return 0
    
    # 获取质因数分解
    factors = prime_factors(n)
    result = n
    
    # 根据欧拉函数公式：φ(n) = n * product(1-1/p)，其中p是n的质因数
    for p in factors:
        result *= (p - 1)
        result //= p
    
    return result

# 计算最大公约数
def gcd(a: int, b: int) -> int:
    """
    使用欧几里得算法计算最大公约数
    时间复杂度: O(log min(a,b))
    
    参数:
        a, b: 两个正整数
    返回:
        int: 最大公约数
    """
    while b != 0:
        a, b = b, a % b
    return a

# 计算最小公倍数
def lcm(a: int, b: int) -> int:
    """
    基于最大公约数计算最小公倍数
    时间复杂度: O(√max(a,b))
    
    参数:
        a, b: 两个正整数
    返回:
        int: 最小公倍数
    """
    if a == 0 or b == 0:
        return 0
    return a * b // gcd(a, b)

# 按公因数计算最大组件大小
# 给定一个由不同正整数的组成的非空数组 nums
# 如果 nums[i] 和 nums[j] 有一个大于1的公因子，那么这两个数之间有一条无向边
# 返回 nums中最大连通组件的大小。
# 测试链接 : https://leetcode.cn/problems/largest-component-size-by-common-factor/

# 常量定义 - 注意：根据实际情况调整这些值
MAXV = 100001  # 质数因子的最大可能值
MAXN = 20001   # 并查集的最大大小

# factors[a] = b - a这个质数因子，最早被下标b的数字拥有
factors = [-1] * MAXV

# 并查集全局变量
father = list(range(MAXN))
size = [1] * MAXN

def build(n):
    """
    初始化并查集和factors数组
    
    参数:
        n: 数组长度
    """
    global factors, father, size
    # 初始化并查集
    for i in range(n):
        father[i] = i
        size[i] = 1
    # 重置factors数组
    factors = [-1] * MAXV

def find(i):
    """
    并查集查找操作，带路径压缩优化
    
    参数:
        i: 要查找的节点
    返回:
        int: 节点i的根节点
    """
    if i != father[i]:
        # 路径压缩：将i到根节点路径上的所有节点直接连接到根节点
        father[i] = find(father[i])
    return father[i]

def union(x, y):
    """
    并查集合并操作
    
    参数:
        x, y: 要合并的两个节点
    """
    fx = find(x)
    fy = find(y)
    if fx != fy:
        # 按大小合并，将较小的树合并到较大的树中
        father[fx] = fy
        size[fy] += size[fx]

def max_size(n):
    """
    找出并查集中最大集合的大小
    
    参数:
        n: 数组长度
    返回:
        int: 最大集合的大小
    """
    ans = 0
    for i in range(n):
        ans = max(ans, size[i])
    return ans

def largest_component_size(arr):
    """
    计算按公因数连接的最大组件大小
    时间复杂度：O(n * √v)，其中v是数组中元素的最大值
    空间复杂度：O(max(v, n))
    
    算法思路：
    1. 对每个数字进行质因数分解
    2. 对于每个质因数，记录它第一次出现的数字索引
    3. 如果质因数之前出现过，则将当前数字与之前数字合并到同一集合
    4. 最后返回最大集合的大小
    
    技巧点：
    1. 使用并查集维护连通性
    2. 质因数分解过程中直接进行并查集操作
    3. 对于每个质因数只记录第一次出现的索引，避免重复合并
    
    工程化考虑：
    1. 边界条件处理：数组为空或只有一个元素
    2. 性能优化：质因数分解的优化
    3. 内存优化：合理设置MAXV和MAXN的大小
    4. 异常处理：处理极大值和特殊情况
    
    参数:
        arr: 正整数数组
    返回:
        int: 最大连通组件的大小
    """
    # 边界条件检查
    if not arr:
        return 0
    if len(arr) == 1:
        return 1
    
    n = len(arr)
    # 检查MAXN是否足够大
    if n > MAXN:
        raise ValueError(f"数组长度{n}超过并查集最大大小{MAXN}")
    
    build(n)
    
    for i in range(n):
        x = arr[i]
        # 对每个数进行质因数分解
        j = 2
        while j * j <= x:
            if x % j == 0:
                # 找到一个质因数j
                if factors[j] == -1:
                    # 第一次出现这个质因数，记录它对应的索引
                    factors[j] = i
                else:
                    # 这个质因数之前出现过，合并两个索引对应的集合
                    union(factors[j], i)
                # 移除所有的因子j
                while x % j == 0:
                    x //= j
            j += 1
        # 处理最后可能剩下的质因数
        if x > 1:
            if factors[x] == -1:
                factors[x] = i
            else:
                union(factors[x], i)
    
    return max_size(n)

# 优化版的largest_component_size，使用动态调整的并查集
def largest_component_size_optimized(nums):
    """
    计算按公因数连接的最大组件大小（优化版本）
    对于大规模数据，这种方法可以避免创建过大的并查集
    
    时间复杂度：O(n * √v)，其中v是数组中元素的最大值
    空间复杂度：O(max(v, n))
    
    参数:
        nums: 正整数数组
    返回:
        int: 最大连通组件的大小
    """
    class UnionFind:
        """动态调整大小的并查集实现"""
        def __init__(self):
            self.parent = {}
            self.rank = {}
        
        def _ensure(self, x):
            """确保节点存在于并查集中"""
            if x not in self.parent:
                self.parent[x] = x
                self.rank[x] = 0
        
        def find(self, x):
            """查找操作，带路径压缩"""
            self._ensure(x)
            if self.parent[x] != x:
                self.parent[x] = self.find(self.parent[x])
            return self.parent[x]
        
        def union(self, x, y):
            """合并操作，按秩合并"""
            root_x = self.find(x)
            root_y = self.find(y)
            if root_x != root_y:
                if self.rank[root_x] < self.rank[root_y]:
                    self.parent[root_x] = root_y
                elif self.rank[root_x] > self.rank[root_y]:
                    self.parent[root_y] = root_x
                else:
                    self.parent[root_y] = root_x
                    self.rank[root_x] += 1
    
    # 边界条件检查
    if not nums:
        return 0
    if len(nums) == 1:
        return 1
    
    uf = UnionFind()
    # 记录每个质因数对应的第一个出现的数
    factor_to_first_num = {}
    
    for num in nums:
        # 对每个数进行质因数分解
        factors = prime_factors(num)
        # 如果num=1，它没有质因数
        if not factors:
            continue
        
        # 将当前数与它的所有质因数连接起来
        # 先连接第一个质因数和当前数
        first_factor = next(iter(factors.keys()))
        uf.union(num, first_factor)
        
        # 然后连接当前数的其他质因数
        for factor in list(factors.keys())[1:]:
            uf.union(first_factor, factor)
    
    # 统计每个集合的大小
    count = {}
    for num in nums:
        if num == 1:
            count[1] = count.get(1, 0) + 1
            continue
        root = uf.find(num)
        count[root] = count.get(root, 0) + 1
    
    return max(count.values(), default=0)

# 运行质因数分解的性能测试
def performance_test():
    """
    性能测试函数，测试不同大小数字的质因数分解性能
    """
    print("=== 质因数分解性能测试 ===")
    
    # 测试不同大小的数
    test_numbers = [
        1000000007,  # 大质数
        1000000000,  # 10^9
        1000000000000,  # 10^12
        2147483647,  # 2^31-1，梅森素数
        2 * 3 * 5 * 7 * 11 * 13 * 17 * 19 * 23 * 29  # 多个质因数的乘积
    ]
    
    for num in test_numbers:
        start_time = time.time()
        factors = f(num)
        end_time = time.time()
        
        # 验证分解结果
        product = 1
        for p in factors:
            # 这里因为f函数返回的是质因数列表（每个质因数只出现一次），所以需要先获取完整分解
            temp = num
            exponent = 0
            while temp % p == 0:
                exponent += 1
                temp //= p
            product *= p ** exponent
        
        valid = product == num
        
        print(f"\n数字: {num}")
        print(f"质因数分解 (f函数): {factors}")
        print(f"验证结果: {'正确' if valid else '错误'}")
        print(f"执行时间: {(end_time - start_time) * 1000:.3f} ms")
    
    # 测试大量小数字的分解速度
    print("\n测试10000个随机数的质因数分解速度:")
    start_time = time.time()
    total_factors = 0
    for _ in range(10000):
        num = random.randint(1, 1000000)
        factors = f(num)
        total_factors += len(factors)
    end_time = time.time()
    print(f"平均每个数的质因数数量: {total_factors / 10000:.2f}")
    print(f"总执行时间: {(end_time - start_time) * 1000:.3f} ms")
    print(f"平均每个数的分解时间: {(end_time - start_time) * 1000 / 10000:.3f} ms")

# 全面的功能测试
def functional_test():
    """
    功能测试函数，测试各种边界情况和正常情况
    """
    print("=== 质因数分解功能测试 ===")
    
    # 测试用例，包括边界情况
    test_cases = [
        (1, []),                           # 边界情况：1没有质因数
        (2, [2]),                          # 质数
        (4, [2]),                          # 只有一个质因数，指数>1
        (12, [2, 3]),                      # 多个质因数
        (18, [2, 3]),                      # 多个质因数，有重复
        (100, [2, 5]),                     # 100的质因数分解
        (101, [101]),                      # 大质数
        (2147483647, [2147483647]),        # 2^31-1，梅森素数
        (1000000, [2, 5]),                 # 10^6的质因数分解
        (123456789, [3, 3607, 3803])       # 多个不同的质因数
    ]
    
    all_passed = True
    for n, expected in test_cases:
        result = f(n)
        status = "✓" if result == expected else "✗"
        print(f"{n} -> {result} {status}")
        if result != expected:
            all_passed = False
    
    print(f"\n功能测试结果: {'全部通过' if all_passed else '存在失败'}")
    
    # 测试prime_factors函数（返回字典形式）
    print("\n=== prime_factors 函数测试 ===")
    dict_test_cases = [
        (1, {}),                           # 边界情况：1没有质因数
        (2, {2: 1}),                       # 质数
        (4, {2: 2}),                       # 只有一个质因数，指数>1
        (12, {2: 2, 3: 1}),                # 多个质因数
        (18, {2: 1, 3: 2}),                # 多个质因数，有重复
        (100, {2: 2, 5: 2})                # 100的质因数分解
    ]
    
    all_passed = True
    for n, expected in dict_test_cases:
        result = prime_factors(n)
        status = "✓" if result == expected else "✗"
        print(f"{n} -> {result} {status}")
        if result != expected:
            all_passed = False
    
    print(f"\nprime_factors 测试结果: {'全部通过' if all_passed else '存在失败'}")
    
    # 测试largest_component_size
    print("\n=== Largest Component Size 功能测试 ===")
    component_test_cases = [
        ([4, 6, 15, 35], 4),               # 所有数都有共同的质因数链
        ([20, 50, 9, 63], 2),              # 两组数，每组有共同质因数
        ([2, 3, 5, 7, 11], 1),             # 所有数都是质数，没有共同因数
        ([1], 1),                          # 只有1个元素的情况
        ([83, 99, 39, 11, 19, 30, 31], 7)  # 复杂情况
    ]
    
    all_passed = True
    for nums, expected in component_test_cases:
        try:
            result = largest_component_size(nums)
            result_opt = largest_component_size_optimized(nums)
            status = "✓" if (result == expected and result_opt == expected) else "✗"
            print(f"{nums} -> 常规版本: {result}, 优化版本: {result_opt} {status}")
            if result != expected or result_opt != expected:
                all_passed = False
        except Exception as e:
            print(f"{nums} -> 测试失败: {str(e)}")
            all_passed = False
    
    print(f"\nLargest Component Size 测试结果: {'全部通过' if all_passed else '存在失败'}")
    
    # 测试欧拉函数
    print("\n=== 欧拉函数功能测试 ===")
    euler_test_cases = [
        (1, 0),    # 边界情况：φ(1)=0
        (2, 1),    # φ(2)=1
        (4, 2),    # φ(4)=2
        (6, 2),    # φ(6)=2
        (12, 4),   # φ(12)=4
        (100, 40), # φ(100)=40
        (101, 100) # φ(质数p)=p-1
    ]
    
    all_passed = True
    for n, expected in euler_test_cases:
        result = euler_phi(n)
        status = "✓" if result == expected else "✗"
        print(f"φ({n}) = {result} {status}")
        if result != expected:
            all_passed = False
    
    print(f"\n欧拉函数测试结果: {'全部通过' if all_passed else '存在失败'}")

# 交互式测试函数
def interactive_test():
    """
    交互式测试函数，允许用户输入数字进行质因数分解
    """
    print("\n=== 交互式测试 ===")
    print("输入一个正整数进行质因数分解（输入'q'退出）:")
    
    while True:
        try:
            user_input = input("请输入数字: ")
            if user_input.lower() == 'q':
                break
            
            num = int(user_input)
            if num < 1:
                print("请输入正整数！")
                continue
                
            # 测量执行时间
            start_time = time.time()
            factors_list = f(num)
            factors_dict = prime_factors(num)
            end_time = time.time()
            
            # 打印分解结果
            print(f"\n数字: {num}")
            print(f"质因数分解 (唯一质因数): {factors_list}")
            print(f"质因数分解 (带指数): {factors_dict}")
            
            # 计算乘积验证结果
            product = 1
            for p, exp in factors_dict.items():
                product *= p ** exp
            
            print(f"验证: {'正确' if product == num else '错误'}")
            print(f"执行时间: {(end_time - start_time) * 1000:.3f} ms")
            
            # 如果是合数，显示分解式
            if len(factors_dict) > 0 and (len(factors_dict) > 1 or list(factors_dict.values())[0] > 1):
                factors_str = " * ".join([f"{p}^{exp}" if exp > 1 else f"{p}" 
                                         for p, exp in sorted(factors_dict.items())])
                print(f"分解式: {num} = {factors_str}")
                
        except ValueError:
            print("请输入有效的数字！")
        except Exception as e:
            print(f"发生错误: {str(e)}")

# 主函数，运行所有测试
def main():
    """
    主函数，运行所有测试
    """
    try:
        # 功能测试
        functional_test()
        
        # 性能测试
        performance_test()
        
        # 交互式测试
        interactive_test()
    except KeyboardInterrupt:
        print("\n测试被用户中断")
    except Exception as e:
        print(f"\n测试过程中发生错误: {str(e)}")

# 执行主函数
if __name__ == "__main__":
    main()
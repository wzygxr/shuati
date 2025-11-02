#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
投票问题（Ballot Problem）- 卡特兰数的扩展应用

核心问题：
在一次选举中，候选人A得到n张票，候选人B得到m张票，其中n>m
计算在计票过程中A的票数始终严格大于B的票数的方案数

相关题目：
- LeetCode 22. 括号生成
- LeetCode 96. 不同的二叉搜索树
- LeetCode 95. 不同的二叉搜索树 II
- LeetCode 32. 最长有效括号
- LeetCode 856. 括号的分数
- 洛谷 P1320 压缩技术（续集）
- LintCode 427. 生成括号
- 牛客网 NC146 括号生成

数学背景：
这个问题的答案是 (n-m)/(n+m) * C(n+m, n)，当n=m时，结果等价于第n项卡特兰数
是卡特兰数的一个重要扩展应用，提供了更通用的计数模型

实现特点：
1. 支持两种解法：公式法和反射原理法
2. 完整的边界条件检查
3. 高效的预处理机制优化性能
4. 模块化设计便于维护和扩展
"""

import sys
import time
from typing import List

class Solution:
    """投票问题解决方案类"""
    
    # 模数 - 用于处理大数运算
    MOD = 1000000007
    
    # 最大预处理范围
    MAXN = 2000001
    
    # 阶乘余数表 - 预计算并缓存
    fac = [0] * MAXN
    
    # 阶乘逆元表 - 预计算并缓存
    inv = [0] * MAXN
    
    # 标记是否已初始化
    initialized = False
    
    @classmethod
    def build(cls, n: int) -> None:
        """
        构建阶乘和逆元表
        
        核心思路：预处理阶乘和逆元表，避免重复计算，提高多次查询的效率
        使用费马小定理计算逆元，适合模数为质数的情况
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        参数:
            n (int): 最大值
        """
        # 避免重复构建，优化性能
        if cls.initialized and cls.fac[n] != 0:
            return
        
        # 初始化基础值
        cls.fac[0] = cls.inv[0] = 1
        cls.fac[1] = 1
        
        # 计算阶乘表
        for i in range(2, n + 1):
            cls.fac[i] = (i * cls.fac[i - 1]) % cls.MOD
        
        # 使用费马小定理计算最大n的逆元
        cls.inv[n] = cls.power(cls.fac[n], cls.MOD - 2)
        
        # 反向递推计算其他逆元
        for i in range(n - 1, 0, -1):
            cls.inv[i] = ((i + 1) * cls.inv[i + 1]) % cls.MOD
        
        # 标记已初始化
        cls.initialized = True
    
    @classmethod
    def power(cls, x: int, p: int) -> int:
        """
        快速幂运算 - 计算x^p % MOD
        
        核心思路：利用二进制分解指数，将幂运算转换为多项式乘积
        每次迭代将底数平方，指数减半，实现对数级别的时间复杂度
        
        时间复杂度：O(log p)
        空间复杂度：O(1)
        
        参数:
            x (int): 底数
            p (int): 指数
        返回:
            int: x^p % MOD
        """
        # 对底数进行模运算预处理
        x = x % cls.MOD
        ans = 1
        
        # 快速幂核心逻辑
        while p > 0:
            # 如果当前最低位为1，乘上当前x的值
            if (p & 1) == 1:
                ans = (ans * x) % cls.MOD
            # 底数平方
            x = (x * x) % cls.MOD
            # 指数右移一位（相当于除以2）
            p >>= 1
        return ans
    
    @classmethod
    def combination(cls, n: int, k: int) -> int:
        """
        计算组合数C(n, k) = n!/(k!(n-k)!)
        
        核心思路：利用预处理的阶乘和逆元表进行快速查询
        C(n,k) = n! * inv(k!) * inv((n-k)!) mod MOD
        
        时间复杂度：O(1) - 依赖于预处理
        空间复杂度：O(1)
        
        参数:
            n (int): 总数
            k (int): 选择数
        返回:
            int: C(n, k) % MOD
        """
        # 边界情况处理
        if k > n or k < 0:
            return 0
        if k == 0 or k == n:
            return 1
        
        # 确保阶乘表已初始化
        if not cls.initialized or cls.fac[n] == 0:
            cls.build(n)
        
        # 计算组合数：C(n,k) = n!/(k!(n-k)!) = n! * inv(k!) * inv((n-k)!) mod MOD
        result = ((cls.fac[n] * cls.inv[k]) % cls.MOD) * cls.inv[n - k] % cls.MOD
        return result
    
    @classmethod
    def ballot_problem(cls, n: int, m: int) -> int:
        """
        投票问题解法 - 公式法
        
        核心思路：直接应用投票问题的数学公式 (n-m)/(n+m) * C(n+m, n)
        在模运算环境下，除法转换为乘以模逆元
        
        时间复杂度：O(n+m)（预处理时间），之后O(1)
        空间复杂度：O(n+m)
        
        参数:
            n (int): A的票数
            m (int): B的票数
        返回:
            int: 满足条件的计票方式数模MOD的结果
        """
        # 边界条件处理
        # 当n < m时，不可能满足A始终领先B
        if n < m:
            return 0
        # 当m=0时，只有一种方式（全是A的票）
        if m == 0:
            return 1
        
        # 预处理阶乘和逆元表
        cls.build(n + m)
        
        # 计算公式: (n-m)/(n+m) * C(n+m, n)
        # 在模运算中，除法转换为乘以模逆元
        numerator = (n - m) % cls.MOD
        denominator = (n + m) % cls.MOD
        
        # 计算分母的逆元
        denominator_inv = cls.power(denominator, cls.MOD - 2)
        
        # 计算组合数 C(n+m, n)
        comb = cls.combination(n + m, n)
        
        # 计算最终结果：(分子 * 分母逆元) * 组合数 % MOD
        result = ((numerator * denominator_inv) % cls.MOD) * comb % cls.MOD
        return result
    
    @classmethod
    def ballot_problem_reflection(cls, n: int, m: int) -> int:
        """
        投票问题的另一种实现 - 使用反射原理
        
        核心思路：使用反射原理将问题转化为总方案数减去无效方案数
        无效方案数可通过反射技术计算为 C(n+m, n+1)
        
        参数:
            n (int): A的票数
            m (int): B的票数
        返回:
            int: 满足条件的计票方式数模MOD的结果
        """
        # 边界条件处理
        if n < m:
            return 0
        if m == 0:
            return 1
        
        # 预处理阶乘和逆元表
        cls.build(n + m)
        
        # 使用反射原理公式：C(n+m, n) - C(n+m, n+1)
        c1 = cls.combination(n + m, n)
        c2 = cls.combination(n + m, n + 1)  # 等价于C(n+m, m-1)
        
        # 处理负数情况，确保结果为正
        result = (c1 - c2 + cls.MOD) % cls.MOD
        return result
    
    @classmethod
    def catalan_number(cls, n: int) -> int:
        """
        特殊情况：当n=m时，投票问题就变成了卡特兰数
        
        核心思路：卡特兰数是投票问题的特例，当n=m时的情况
        公式：Catalan(n) = C(2n, n) * (n+1)^{-1} (mod MOD)
        
        时间复杂度：O(n)（预处理时间），之后O(1)
        空间复杂度：O(n)
        
        参数:
            n (int): 第n项卡特兰数
        返回:
            int: 第n项卡特兰数模MOD的结果
        """
        # 边界情况
        if n == 0:
            return 1
        
        # 预处理阶乘和逆元表
        cls.build(2 * n)
        
        # 计算卡特兰数：C(2n, n)/(n+1) = C(2n, n) * inv(n+1) mod MOD
        combination = cls.combination(2 * n, n)
        inv_n_plus_1 = cls.power(n + 1, cls.MOD - 2)
        result = (combination * inv_n_plus_1) % cls.MOD
        return result
    
    @classmethod
    def reset(cls) -> None:
        """
        重置初始化状态 - 用于测试或内存管理
        """
        cls.initialized = False

def print_performance(operation: str, duration: float) -> None:
    """
    打印性能指标，格式化输出执行时间
    
    参数:
        operation (str): 操作描述
        duration (float): 执行时间（秒）
    """
    if duration < 1e-6:
        print(f"  {operation}: {duration * 1e9:.2f} ns")
    elif duration < 1e-3:
        print(f"  {operation}: {duration * 1e6:.2f} μs")
    elif duration < 1:
        print(f"  {operation}: {duration * 1e3:.2f} ms")
    else:
        print(f"  {operation}: {duration:.2f} s")

def main() -> None:
    """
    主方法 - 测试所有实现并比较性能
    """
    print("===== 投票问题（Ballot Problem）测试 =====")
    
    # 测试用例1: 基本测试
    print("\n1. 基本测试:")
    basic_tests = [(2, 1), (3, 1), (3, 2), (5, 3), (8, 6), (10, 5)]
    
    for n, m in basic_tests:
        print(f"A: {n}票, B: {m}票")
        print(f"  标准公式: {Solution.ballot_problem(n, m)}")
        print(f"  反射原理: {Solution.ballot_problem_reflection(n, m)}")
    
    # 测试用例2: 边界情况
    print("\n2. 边界情况测试:")
    print(f"n=1, m=0: {Solution.ballot_problem(1, 0)}")  # 应返回1
    print(f"n=5, m=5: {Solution.ballot_problem(5, 5)}")  # 应返回0（因为需要严格大于）
    print(f"n=3, m=4: {Solution.ballot_problem(3, 4)}")  # 应返回0（n<m）
    
    # 测试用例3: 卡特兰数测试
    print("\n3. 卡特兰数测试:")
    for i in range(6):
        print(f"Catalan({i}) = {Solution.catalan_number(i)}")
    
    # 测试用例4: 性能测试
    print("\n4. 性能测试:")
    
    # 重置初始化状态以准确测量首次调用性能
    Solution.reset()
    
    start_time = time.time()
    Solution.ballot_problem(10000, 5000)  # 第一次调用，包含预处理
    end_time = time.time()
    print(f"第一次调用（含预处理）耗时: {(end_time - start_time) * 1000:.2f} ms")
    
    start_time = time.time()
    Solution.ballot_problem(8000, 4000)  # 第二次调用，复用预处理
    end_time = time.time()
    print_performance("第二次调用（复用预处理）耗时", end_time - start_time)
    
    # 测试用例5: 公式等价性验证
    print("\n5. 公式等价性验证:")
    for n in range(2, 7):
        for m in range(1, n):
            result1 = Solution.ballot_problem(n, m)
            result2 = Solution.ballot_problem_reflection(n, m)
            if result1 == result2:
                print(f"✓ n={n}, m={m} 公式等价性验证通过")
            else:
                print(f"✗ n={n}, m={m} 公式等价性验证失败: {result1} vs {result2}")

if __name__ == "__main__":
    main()
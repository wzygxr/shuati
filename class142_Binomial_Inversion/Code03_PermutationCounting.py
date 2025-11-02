#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
排列计数问题（Permutation Counting Problem）
题目：洛谷 P4071 [SDOI2016]排列计数
链接：https://www.luogu.com.cn/problem/P4071
描述：求有多少种1到n的排列a，满足序列恰好有m个位置i，使得a_i = i

Python实现特点：
- 使用二项式反演求解部分错位排列问题
- 预处理阶乘和逆元以优化组合数计算
- 提供多种实现方法：直接计算和优化版本
- 详细的复杂度分析和注释
- 完整的异常处理和测试用例
"""

MOD = 10**9 + 7  # 模数
MAXN = 10**6 + 1  # 最大数据范围

class PermutationCounting:
    def __init__(self, n, k):
        """
        初始化排列计数问题
        
        Args:
            n: 元素个数
            k: 恰好k个固定点
            
        Raises:
            ValueError: 当参数无效时抛出异常
        """
        # 参数验证
        if not isinstance(n, int) or not isinstance(k, int):
            raise ValueError("参数n和k必须是整数")
        if n < 0:
            raise ValueError("参数n必须是非负整数")
        if k < 0 or k > n:
            raise ValueError("参数k必须在0到n之间")
            
        self.n = n
        self.k = k
        self.fact = []      # 阶乘数组
        self.inv_fact = []  # 阶乘逆元数组
        
        # 预处理阶乘和逆元
        self.precompute()
    
    def precompute(self):
        """
        预处理阶乘和阶乘的逆元
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        # 计算阶乘数组
        max_needed = self.n
        self.fact = [1] * (max_needed + 1)
        for i in range(1, max_needed + 1):
            self.fact[i] = (self.fact[i-1] * i) % MOD
        
        # 计算最大阶乘的逆元
        self.inv_fact = [1] * (max_needed + 1)
        self.inv_fact[max_needed] = pow(self.fact[max_needed], MOD-2, MOD)
        
        # 倒序计算其他阶乘的逆元
        for i in range(max_needed - 1, -1, -1):
            self.inv_fact[i] = (self.inv_fact[i+1] * (i+1)) % MOD
    
    def comb(self, a, b):
        """
        计算组合数C(a, b)
        时间复杂度：O(1) - 使用预处理的阶乘和逆元
        
        Args:
            a: 总数
            b: 选取的数量
            
        Returns:
            组合数C(a, b)对MOD取模的结果
        """
        if b < 0 or b > a:
            return 0
        return (self.fact[a] * self.inv_fact[b] % MOD) * self.inv_fact[a - b] % MOD
    
    def count_fixed_points(self):
        """
        使用二项式反演计算恰好k个固定点的排列数
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Returns:
            恰好k个固定点的排列数，对MOD取模
        """
        # 计算C(n, k)
        c_n_k = self.comb(self.n, self.k)
        
        # 计算D(n-k)：(n-k)个元素的错排数
        m = self.n - self.k
        derangement = 0
        
        # 计算D(m) = m! * Σ(i=0到m) (-1)^i / i!
        for i in range(0, m + 1):
            # 计算符号 (-1)^i
            sign = 1 if i % 2 == 0 else -1
            
            # 计算项：m! * (-1)^i / i! = fact[m] * inv_fact[i] * sign
            term = self.fact[m] * self.inv_fact[i] % MOD
            
            # 处理负数情况
            if sign < 0:
                term = (MOD - term) % MOD
            
            # 累加结果
            derangement = (derangement + term) % MOD
        
        # 最终结果：C(n, k) * D(n-k)
        return c_n_k * derangement % MOD
    
    def count_fixed_points_optimized(self):
        """
        优化版本：直接使用递推计算错排数
        时间复杂度：O(n)
        空间复杂度：O(1)
        
        Returns:
            恰好k个固定点的排列数，对MOD取模
        """
        # 计算C(n, k)
        c_n_k = self.comb(self.n, self.k)
        
        # 计算D(n-k)：使用递推公式
        m = self.n - self.k
        
        # 边界条件
        if m == 0:
            return c_n_k  # D(0) = 1
        if m == 1:
            return 0  # D(1) = 0
        
        # 使用递推公式计算错排数：D(m) = (m-1) * (D(m-1) + D(m-2))
        d_prev2 = 1  # D(0)
        d_prev1 = 0  # D(1)
        d_curr = 0
        
        for i in range(2, m + 1):
            d_curr = ((i - 1) * (d_prev1 + d_prev2)) % MOD
            # 更新状态
            d_prev2, d_prev1 = d_prev1, d_curr
        
        # 最终结果：C(n, k) * D(n-k)
        return c_n_k * d_curr % MOD

    def test(self):
        """
        运行测试用例
        
        Returns:
            bool: 所有测试通过返回True，否则返回False
        """
        # 测试用例
        test_cases = [
            (3, 1, 3),   # n=3, k=1, 期望结果3种排列
            (4, 2, 6),   # n=4, k=2, 期望结果6种排列
            (1, 0, 0),   # n=1, k=0, 期望结果0
            (1, 1, 1),   # n=1, k=1, 期望结果1
            (0, 0, 1)    # n=0, k=0, 期望结果1
        ]
        
        all_passed = True
        
        print("排列计数问题测试：")
        print("=" * 70)
        print(f"{'n':<5}{'k':<5}{'预期结果':<10}{'常规方法':<15}{'优化方法':<15}{'状态'}")
        print("=" * 70)
        
        for n, k, expected in test_cases:
            try:
                problem = PermutationCounting(n, k)
                result1 = problem.count_fixed_points()
                result2 = problem.count_fixed_points_optimized()
                
                passed = (result1 == expected) and (result2 == expected)
                all_passed &= passed
                
                print(f"{n:<5}{k:<5}{expected:<10}{result1:<15}{result2:<15}{'✓' if passed else '✗'}")
            except Exception as e:
                print(f"{n:<5}{k:<5}{expected:<10}{'异常':<15}{'异常':<15}{'✗'}")
                print(f"  错误信息: {e}")
                all_passed = False
        
        print("=" * 70)
        print(f"测试{'通过' if all_passed else '失败'}")
        print()
        
        return all_passed

def main():
    """
    主函数，处理输入并计算结果
    """
    # 运行测试
    PermutationCounting(1, 1).test()
    
    # 边界情况测试
    print("边界情况测试：")
    try:
        PermutationCounting(-1, 0)
        print("  错误：未捕获到n为负数的异常")
    except ValueError as e:
        print(f"  成功：捕获到n为负数的异常 - {e}")
    
    try:
        PermutationCounting(5, 6)
        print("  错误：未捕获到k大于n的异常")
    except ValueError as e:
        print(f"  成功：捕获到k大于n的异常 - {e}")
    
    # 性能测试
    print("\n性能测试：")
    print("=" * 70)
    
    import time
    
    # 测试较大的数据规模
    large_n = 100000
    large_k = 50000
    
    start_time = time.time()
    problem = PermutationCounting(large_n, large_k)
    result = problem.count_fixed_points_optimized()
    end_time = time.time()
    
    print(f"n = {large_n}, k = {large_k}")
    print(f"结果 = {result}")
    print(f"计算时间 = {(end_time - start_time) * 1000:.2f} ms")
    print("=" * 70)
    
    # 处理用户输入
    try:
        print("\n请输入n和k：")
        n = int(input("n = "))
        k = int(input("k = "))
        
        problem = PermutationCounting(n, k)
        result = problem.count_fixed_points_optimized()
        print(f"恰好有{k}个固定点的排列数为: {result}")
        
    except ValueError as e:
        print(f"输入错误: {e}")

if __name__ == "__main__":
    main()
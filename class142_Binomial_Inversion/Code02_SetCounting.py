#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
集合计数问题（Set Counting Problem）
题目：洛谷 P10596 集合计数 / BZOJ2839 集合计数
链接：https://www.luogu.com.cn/problem/P10596
描述：从2^n个子集中选出若干个集合，使交集恰好包含k个元素的方案数

Python实现特点：
- 使用二项式反演将"恰好k个元素"转换为"至少k个元素"问题
- 预处理阶乘和逆元以优化组合数计算
- 包含详细的复杂度分析和代码注释
- 提供完整的异常处理和测试用例
"""

MOD = 10**9 + 7  # 模数

class SetCounting:
    def __init__(self, n, k):
        """
        初始化集合计数问题
        
        Args:
            n: 元素总数
            k: 交集恰好包含的元素个数
            
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
        # 为避免重复计算，确保n至少为1
        max_n = max(1, self.n)
        self.fact = [1] * (max_n + 1)
        self.inv_fact = [1] * (max_n + 1)
        
        # 计算阶乘
        for i in range(1, max_n + 1):
            self.fact[i] = (self.fact[i-1] * i) % MOD
        
        # 使用费马小定理计算逆元
        self.inv_fact[max_n] = pow(self.fact[max_n], MOD-2, MOD)
        
        # 倒序计算其他阶乘的逆元
        for i in range(max_n-1, -1, -1):
            self.inv_fact[i] = (self.inv_fact[i+1] * (i+1)) % MOD
    
    def comb(self, a, b):
        """
        计算组合数 C(a, b) = a!/(b! * (a-b)!)
        时间复杂度：O(1) - 使用预处理的阶乘和逆元
        
        Args:
            a: 总数
            b: 选取的数量
            
        Returns:
            组合数C(a, b)对MOD取模的结果
        """
        if b < 0 or b > a:
            return 0
        if a == 0 and b == 0:
            return 1
            
        # 确保a在预处理范围内
        if a >= len(self.fact):
            # 如果a大于预处理的范围，重新预处理
            old_n = len(self.fact) - 1
            max_n = a
            self.fact.extend([1] * (max_n - old_n))
            self.inv_fact.extend([1] * (max_n - old_n))
            
            for i in range(old_n + 1, max_n + 1):
                self.fact[i] = (self.fact[i-1] * i) % MOD
            
            self.inv_fact[max_n] = pow(self.fact[max_n], MOD-2, MOD)
            for i in range(max_n-1, old_n, -1):
                self.inv_fact[i] = (self.inv_fact[i+1] * (i+1)) % MOD
        
        return (self.fact[a] * self.inv_fact[b] % MOD) * self.inv_fact[a - b] % MOD
    
    def compute(self):
        """
        计算集合计数问题的解
        
        算法原理：
        1. 定义f(k)为交集恰好有k个元素的方案数
        2. 定义g(k)为交集至少有k个元素的方案数
        3. 通过二项式反演，f(k) = Σ(i=k到n) [(-1)^(i-k) * C(i,k) * g(i)]
        4. g(i) = C(n,i) * (2^(2^(n-i)) - 1) 表示选择i个固定元素，其余元素任意组合
        
        时间复杂度分析：O(n log max_pow) - 计算2的高次幂需要O(log max_pow)时间
        空间复杂度分析：O(n) - 需要存储阶乘和逆元数组
        
        Returns:
            交集恰好有k个元素的方案数，对MOD取模
        """
        if self.k > self.n:
            return 0
        
        # 预计算g数组
        g = [0] * (self.n + 1)
        
        # 计算g[i] = C(n,i) * (2^(2^(n-i)) - 1)
        tmp = 1  # 2^(2^0) = 2^1 = 2
        for i in range(self.n, -1, -1):
            # g[i]暂时保存2^(2^(n-i))
            g[i] = tmp
            # 计算下一个tmp = 2^(2^(n-i+1)) = (2^(2^(n-i)))^2
            tmp = tmp * tmp % MOD
        
        # 计算完整的g[i] = C(n,i) * (2^(2^(n-i)) - 1)
        for i in range(self.n + 1):
            # 减去1相当于加上MOD-1（模运算中的负数处理）
            g[i] = (g[i] + MOD - 1) * self.comb(self.n, i) % MOD
        
        # 应用二项式反演公式计算f(k)
        result = 0
        for i in range(self.k, self.n + 1):
            # 计算符号：(-1)^(i-k)
            if (i - self.k) % 2 == 0:
                # 偶数次幂，符号为正
                result = (result + self.comb(i, self.k) * g[i] % MOD) % MOD
            else:
                # 奇数次幂，符号为负，相当于乘以MOD-1
                result = (result + self.comb(i, self.k) * g[i] % MOD * (MOD - 1) % MOD) % MOD
        
        return result

    def test(self):
        """
        运行测试用例
        
        Returns:
            bool: 所有测试通过返回True，否则返回False
        """
        # 测试用例
        test_cases = [
            (3, 1, 9),  # n=3, k=1, 期望结果9
            (4, 2, 18), # n=4, k=2, 期望结果18
            (1, 0, 1),  # n=1, k=0, 期望结果1
            (1, 1, 1),  # n=1, k=1, 期望结果1
            (0, 0, 0)   # n=0, k=0, 期望结果0
        ]
        
        all_passed = True
        
        print("集合计数问题测试：")
        print("=" * 60)
        print(f"{'n':<5}{'k':<5}{'预期结果':<10}{'实际结果':<10}{'状态'}")
        print("=" * 60)
        
        for n, k, expected in test_cases:
            try:
                problem = SetCounting(n, k)
                actual = problem.compute()
                passed = actual == expected
                all_passed &= passed
                
                print(f"{n:<5}{k:<5}{expected:<10}{actual:<10}{'✓' if passed else '✗'}")
            except Exception as e:
                print(f"{n:<5}{k:<5}{expected:<10}{'异常':<10}{'✗'}")
                print(f"  错误信息: {e}")
                all_passed = False
        
        print("=" * 60)
        print(f"测试{'通过' if all_passed else '失败'}")
        print()
        
        return all_passed

def main():
    """
    主函数，处理输入并计算结果
    """
    # 运行测试
    SetCounting(1, 1).test()
    
    # 边界情况测试
    print("边界情况测试：")
    try:
        SetCounting(-1, 0)
        print("  错误：未捕获到n为负数的异常")
    except ValueError as e:
        print(f"  成功：捕获到n为负数的异常 - {e}")
    
    try:
        SetCounting(5, 6)
        print("  错误：未捕获到k大于n的异常")
    except ValueError as e:
        print(f"  成功：捕获到k大于n的异常 - {e}")
    
    # 处理用户输入
    try:
        print("\n请输入n和k：")
        n = int(input("n = "))
        k = int(input("k = "))
        
        problem = SetCounting(n, k)
        result = problem.compute()
        print(f"从2^{n}个子集中选出若干个集合，使交集恰好包含{k}个元素的方案数为: {result}")
        
    except ValueError as e:
        print(f"输入错误: {e}")

if __name__ == "__main__":
    main()
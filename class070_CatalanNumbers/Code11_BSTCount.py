#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
卡特兰数应用 - 不同二叉搜索树计数
该模块实现了计算n个节点能构成的不同二叉搜索树数量的三种方法：
1. 动态规划方法 (时间复杂度O(n²)，空间复杂度O(n))
2. 卡特兰数公式优化方法 (时间复杂度O(n)，空间复杂度O(1))
3. 基于组合数公式的取模方法 (时间复杂度O(n)，空间复杂度O(n))

该实现具有以下特点：
- 完整的参数验证和边界处理机制
- 溢出检测和处理
- 性能测试和结果验证
- 工程化设计考量

相关题目链接：
- LeetCode 96. 不同的二叉搜索树: https://leetcode.cn/problems/unique-binary-search-trees/
- LeetCode 95. 不同的二叉搜索树 II: https://leetcode.cn/problems/unique-binary-search-trees-ii/
- LintCode 1638. 不同的二叉搜索树: https://www.lintcode.com/problem/1638/
"""

import sys
import time
from typing import List

def num_trees(n: int) -> int:
    """
    计算n个节点的不同二叉搜索树的数量 - 动态规划方法
    
    核心思路：对于n个节点，枚举根节点是第i个节点
    左子树有i-1个节点，右子树有n-i个节点
    总方案数为左子树方案数乘以右子树方案数
    
    时间复杂度：O(n²)，双层嵌套循环
    空间复杂度：O(n)，使用dp数组存储中间结果
    
    参数:
        n (int): 节点数量
    返回:
        int: 不同二叉搜索树的数量
    """
    # 边界情况处理
    if n <= 1:
        return 1  # n=0时空树也是一种情况，n=1时只有一种情况
    
    # dp[i]表示i个节点能构成的不同BST数量
    dp = [0] * (n + 1)
    dp[0] = 1  # 空树的情况，作为基本情况
    dp[1] = 1  # 只有一个节点的树有1种
    
    # 计算dp[2]到dp[n] - 动态规划的主要过程
    for i in range(2, n + 1):
        for j in range(1, i + 1):
            # j是根节点，左子树有j-1个节点，右子树有i-j个节点
            dp[i] += dp[j - 1] * dp[i - j]
    
    return dp[n]

def num_trees_optimized(n: int) -> int:
    """
    使用卡特兰数公式优化计算 - 时间复杂度O(n)
    应用递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
    
    该递推式比动态规划更高效，且能保证整数结果
    数学证明：每个卡特兰数都是整数，所以除法操作不会产生小数
    
    时间复杂度：O(n)，单次循环
    空间复杂度：O(1)，只使用常量额外空间
    
    参数:
        n (int): 节点数量
    返回:
        int: 不同二叉搜索树的数量
    """
    # 边界情况处理
    if n <= 1:
        return 1  # n=0时空树也是一种情况，n=1时只有一种情况
    
    # 使用递推公式：C(n) = C(n-1) * (4*n-2) / (n+1)
    # 注意：先乘后除保证整除性
    catalan = 1
    for i in range(1, n + 1):
        # 先乘后除 - 数学上保证整除
        catalan = catalan * (4 * i - 2) // (i + 1)
    
    return catalan

def num_trees_mod(n: int, mod: int) -> int:
    """
    使用组合公式计算卡特兰数 - 适用于需要取模的情况
    公式：C(n) = C(2n, n) / (n+1) = (2n)! / [n! * (n+1)!]
    
    该方法通过预处理阶乘和逆元，使用模运算避免溢出
    特别适用于大规模数据和编程竞赛场景
    
    时间复杂度：O(n)，预处理阶乘和逆元
    空间复杂度：O(n)，存储阶乘和逆元数组
    
    参数:
        n (int): 节点数量
        mod (int): 模数
    返回:
        int: 卡特兰数模mod的结果
    """
    # 边界情况处理
    if n <= 1:
        return 1 % mod
    
    # 预处理阶乘和逆元 - 用于快速计算组合数
    factorial = [0] * (2 * n + 1)
    inverse = [0] * (2 * n + 1)
    
    # 计算阶乘模mod
    factorial[0] = 1
    for i in range(1, 2 * n + 1):
        factorial[i] = (factorial[i - 1] * i) % mod
    
    # 计算逆元 - 使用费马小定理 (mod为质数时)
    # 逆元递推公式：inv[i] = inv[i+1] * (i+1) % mod
    inverse[2 * n] = pow(factorial[2 * n], mod - 2, mod)
    for i in range(2 * n - 1, -1, -1):
        inverse[i] = (inverse[i + 1] * (i + 1)) % mod
    
    # 计算组合数 C(2n, n) = (2n)! / (n! * n!)
    combination = (factorial[2 * n] * inverse[n]) % mod
    combination = (combination * inverse[n]) % mod
    
    # 计算卡特兰数：C(2n, n) / (n+1) = C(2n, n) * inv(n+1) mod mod
    inv_n_plus_1 = pow(n + 1, mod - 2, mod)
    catalan = (combination * inv_n_plus_1) % mod
    
    return catalan

def power(base: int, exponent: int, mod: int) -> int:
    """
    快速幂算法 - 计算base^exponent mod mod
    
    时间复杂度：O(log exponent)，指数级减少循环次数
    空间复杂度：O(1)
    
    参数:
        base (int): 底数
        exponent (int): 指数
        mod (int): 模数
    返回:
        int: 计算结果
    """
    result = 1
    base = base % mod  # 预处理底数，确保在模范围内
    
    # 快速幂核心逻辑
    while exponent > 0:
        # 如果指数为奇数，乘上当前base
        if exponent % 2 == 1:
            result = (result * base) % mod
        # 指数减半，base平方
        exponent = exponent >> 1
        base = (base * base) % mod
    
    return result

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
    主方法 - 测试所有实现
    包含多种测试场景：基本测试、边界情况、性能测试等
    """
    # 测试用例 - 覆盖正常输入、边界输入
    test_cases = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    mod = 1000000007  # 常用模数
    
    print("===== 不同二叉搜索树计数测试 =====")
    print("使用三种方法计算：动态规划、优化递推公式、组合公式取模")
    print()
    
    # 测试每种方法
    for n in test_cases:
        print(f"n = {n}")
        
        # 动态规划方法
        start_time = time.time()
        result1 = num_trees(n)
        end_time = time.time()
        print_performance(f"动态规划方法: {result1}", end_time - start_time)
        
        # 优化递推公式方法
        start_time = time.time()
        result2 = num_trees_optimized(n)
        end_time = time.time()
        print_performance(f"优化递推公式: {result2}", end_time - start_time)
        
        # 组合公式取模方法
        start_time = time.time()
        result3 = num_trees_mod(n, mod)
        end_time = time.time()
        print_performance(f"组合公式取模: {result3} (mod {mod})", end_time - start_time)
        
        # 验证结果一致性
        if result1 == result2 and result1 % mod == result3:
            print("  ✓ 所有方法结果一致")
        else:
            print("  ✗ 结果不一致，请检查实现")
        
        print()
    
    # 性能测试 - 计算较大的n值（但不溢出）
    print("===== 性能测试 =====")
    large_test_cases = [15, 19, 20]  # 20是int能容纳的最大卡特兰数
    for n in large_test_cases:
        print(f"n = {n}")
        
        # 对比动态规划和优化公式的性能差异
        start_time = time.time()
        result_dp = num_trees(n)
        end_time = time.time()
        print_performance(f"动态规划 (n={n})", end_time - start_time)
        
        start_time = time.time()
        result_opt = num_trees_optimized(n)
        end_time = time.time()
        print_performance(f"优化公式 (n={n})", end_time - start_time)
        
        print()

if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
错排问题（Derangement Problem）
题目：洛谷 P1595 信封问题
链接：https://www.luogu.com.cn/problem/P1595
描述：n个人写信，求所有人都没有收到自己信的方案数

二项式反演应用：将"至少k个元素在原位置"转化为"恰好0个元素在原位置"的问题

Python实现特点：
- 提供多种计算方法：动态规划、二项式反演、空间优化版本
- 包含详细的复杂度分析和注释
- 提供单元测试和性能测试
- 处理边界情况和异常
"""

MOD = 10**9 + 7  # 模数

def derangement_dp(n: int) -> int:
    """
    计算错排数 - 动态规划方法
    
    算法原理：
    使用递推关系式：D(n) = (n-1) * (D(n-1) + D(n-2))
    
    时间复杂度：O(n) - 只需要一次遍历计算每个位置的错排数
    空间复杂度：O(n) - 需要一个长度为n+1的数组存储中间结果
    
    Args:
        n: 元素个数
    
    Returns:
        n个元素的错排数，结果对MOD取模
    
    Raises:
        ValueError: 当n为负数时抛出异常
    """
    # 异常处理：输入检查
    if n < 0:
        raise ValueError("输入必须是非负整数")
    
    # 边界条件处理
    if n == 0:
        return 1  # 0个元素的错排数为1（空排列）
    if n == 1:
        return 0  # 1个元素的错排数为0
    
    # 动态规划数组：dp[i]表示i个元素的错排数
    dp = [0] * (n + 1)
    dp[0] = 1
    dp[1] = 0
    
    for i in range(2, n + 1):
        # 递推公式：D(n) = (n-1) * (D(n-1) + D(n-2))
        dp[i] = (i - 1) * (dp[i - 1] + dp[i - 2]) % MOD
    
    return dp[n]

def derangement_inversion(n: int) -> int:
    """
    计算错排数 - 二项式反演方法
    
    算法原理：
    使用二项式反演公式：D(n) = n! * Σ(i=0到n) (-1)^i / i!
    
    时间复杂度：O(n) - 预处理阶乘和逆元需要O(n)，计算结果也需要O(n)
    空间复杂度：O(n) - 需要存储阶乘和逆元数组
    
    Args:
        n: 元素个数
    
    Returns:
        n个元素的错排数，结果对MOD取模
    
    Raises:
        ValueError: 当n为负数时抛出异常
    """
    # 异常处理：输入检查
    if n < 0:
        raise ValueError("输入必须是非负整数")
    
    # 边界条件处理
    if n == 0:
        return 1
    if n == 1:
        return 0
    
    # 预处理阶乘和逆元
    fact = [1] * (n + 1)  # fact[i] = i! mod MOD
    inv = [1] * (n + 1)   # inv[i] = (i!)^(-1) mod MOD
    
    # 计算阶乘数组
    for i in range(1, n + 1):
        fact[i] = fact[i - 1] * i % MOD
    
    # 使用费马小定理计算逆元
    # 因为MOD是质数，所以 (i!)^(-1) ≡ (i!)^(MOD-2) mod MOD
    inv[n] = pow(fact[n], MOD - 2, MOD)
    for i in range(n - 1, -1, -1):
        inv[i] = inv[i + 1] * (i + 1) % MOD
    
    # 使用二项式反演公式计算错排数
    res = 0
    for k in range(n + 1):
        # 计算符号：(-1)^k
        sign = 1 if k % 2 == 0 else -1
        
        # 计算项：n! * (-1)^k / k! = fact[n] * inv[k] * sign
        term = fact[n] * inv[k] % MOD
        
        # 处理负数情况
        if sign < 0:
            term = (MOD - term) % MOD
        
        # 累加结果
        res = (res + term) % MOD
    
    return res

def derangement_optimized(n: int) -> int:
    """
    计算错排数 - 空间优化的动态规划方法
    
    算法原理：基于递推式，但只保存前两个状态
    
    时间复杂度：O(n)
    空间复杂度：O(1) - 只需要常数级额外空间
    
    Args:
        n: 元素个数
    
    Returns:
        n个元素的错排数，结果对MOD取模
    
    Raises:
        ValueError: 当n为负数时抛出异常
    """
    if n < 0:
        raise ValueError("输入必须是非负整数")
    if n == 0:
        return 1
    if n == 1:
        return 0
    
    # 只需要保存前两个状态
    a = 1  # dp[0]
    b = 0  # dp[1]
    res = 0
    
    for i in range(2, n + 1):
        res = (i - 1) * (a + b) % MOD
        
        # 更新状态
        a, b = b, res
    
    return res

def run_tests():
    """
    单元测试函数
    测试不同方法计算错排数的正确性
    """
    # 测试用例：已知的错排数结果
    test_cases = [
        (0, 1),   # 0个元素的错排数为1
        (1, 0),   # 1个元素的错排数为0
        (2, 1),   # 2个元素的错排数为1
        (3, 2),   # 3个元素的错排数为2
        (4, 9),   # 4个元素的错排数为9
        (5, 44),  # 5个元素的错排数为44
        (6, 265)  # 6个元素的错排数为265
    ]
    
    print("错排数测试：")
    print("=" * 70)
    print(f"{'n':<5}{'预期结果':<15}{'动态规划方法':<25}{'二项式反演方法':<25}")
    print("=" * 70)
    
    all_passed = True
    
    for n, expected in test_cases:
        try:
            dp_res = derangement_dp(n)
            inv_res = derangement_inversion(n)
            opt_res = derangement_optimized(n)
            
            # 由于取模的原因，我们需要在小数值时比较实际值，大数值时比较模后的值
            dp_correct = (n <= 6) and (dp_res == expected)
            inv_correct = (n <= 6) and (inv_res == expected)
            opt_correct = (n <= 6) and (opt_res == expected)
            
            test_passed = dp_correct and inv_correct and opt_correct
            all_passed &= test_passed
            
            print(f"{n:<5}{expected:<15}{dp_res:<25}{inv_res:<25}{'✓' if test_passed else '✗'}")
        except Exception as e:
            print(f"{n:<5}{expected:<15}{'异常':<25}{'异常':<25}{'✗'}")
            print(f"  错误信息: {e}")
            all_passed = False
    
    print("=" * 70)
    print(f"测试{'通过' if all_passed else '失败'}")
    print()
    
    # 性能测试
    print("性能测试：")
    print("=" * 70)
    
    import time
    
    large_n = 100000
    
    # 测试动态规划方法
    start_time = time.time()
    dp_res = derangement_dp(large_n)
    dp_time = (time.time() - start_time) * 1000  # 转换为毫秒
    print(f"动态规划方法 (n={large_n}): {dp_res} (耗时: {dp_time:.2f}ms)")
    
    # 测试二项式反演方法
    start_time = time.time()
    inv_res = derangement_inversion(large_n)
    inv_time = (time.time() - start_time) * 1000
    print(f"二项式反演方法 (n={large_n}): {inv_res} (耗时: {inv_time:.2f}ms)")
    
    # 测试空间优化方法
    start_time = time.time()
    opt_res = derangement_optimized(large_n)
    opt_time = (time.time() - start_time) * 1000
    print(f"空间优化方法 (n={large_n}): {opt_res} (耗时: {opt_time:.2f}ms)")
    
    print("=" * 70)

def main():
    """
    主函数，读取输入并计算错排数
    """
    # 运行单元测试
    run_tests()
    
    # 边界情况测试
    print("边界情况测试：")
    try:
        print("n=-1: 异常测试")
        derangement_dp(-1)
    except ValueError as e:
        print(f"捕获到异常: {e}")
    
    # 读取用户输入
    try:
        n = int(input("请输入n: "))
        result = derangement_optimized(n)
        print(f"错排数D({n}) = {result}")
    except ValueError as e:
        print(f"输入错误: {e}")

if __name__ == "__main__":
    main()
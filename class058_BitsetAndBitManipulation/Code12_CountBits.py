#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SPOJ问题: Counting Bits
题目链接: https://www.spoj.com/problems/COUNT1BIT/
题目描述: 计算从1到n的所有整数的二进制表示中1的个数之和

方法1: 逐位计算法
时间复杂度: O(log n)
空间复杂度: O(1)

方法2: 动态规划法  
时间复杂度: O(log n)
空间复杂度: O(log n)

方法3: 内置函数法
时间复杂度: O(n log n)
空间复杂度: O(1)

工程化考量:
1. 异常处理: 处理负数输入
2. 性能优化: 使用位运算替代除法
3. 可读性: 清晰的变量命名和注释
4. 测试验证: 包含单元测试和性能测试
5. 类型安全: 使用类型注解
6. 文档化: 详细的docstring
"""

import time
import sys
from typing import Callable, List, Tuple
from functools import lru_cache
import math

class CountBits:
    """
    SPOJ Counting Bits问题的多种解决方案
    """
    
    @staticmethod
    def count_bits_method1(n: int) -> int:
        """
        方法1: 逐位计算法（最优解）
        思路: 计算每一位对总和的贡献
        对于第i位，每2^(i+1)个数中，有2^i个数的该位为1
        
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        Args:
            n: 要计算的最大整数
            
        Returns:
            int: 从1到n的所有整数的二进制表示中1的个数之和
            
        Raises:
            ValueError: 如果n为负数
        """
        if n < 0:
            raise ValueError("输入必须是非负数")
        
        count = 0
        i = 1  # 当前处理的位
        
        while i <= n:
            # 计算当前位的周期
            divisor = i * 2
            # 完整周期的数量
            full_cycles = n // divisor
            # 完整周期中1的个数
            count += full_cycles * i
            
            # 不完整周期中1的个数
            remainder = n % divisor
            if remainder >= i:
                count += remainder - i + 1
            
            # 防止溢出
            if i > sys.maxsize // 2:
                break
            i *= 2
        
        return count
    
    @staticmethod
    @lru_cache(maxsize=None)
    def count_bits_method2(n: int) -> int:
        """
        方法2: 动态规划法（记忆化递归）
        思路: 利用已知结果计算更大数的结果
        dp[n] = dp[n//2] + (n % 2)
        
        时间复杂度: O(log n)
        空间复杂度: O(log n)
        
        Args:
            n: 要计算的最大整数
            
        Returns:
            int: 从1到n的所有整数的二进制表示中1的个数之和
        """
        if n <= 0:
            return 0
        
        # 找到最大的2的幂
        power = 1
        while power * 2 <= n:
            power *= 2
        
        # 计算结果
        # 对于[0, power-1]区间的数，1的个数是已知的
        # 对于[power, n]区间的数，可以递归计算
        return (power // 2) * (power.bit_length() - 1) + \
               (n - power + 1) + \
               CountBits.count_bits_method2(n - power)
    
    @staticmethod
    def count_bits_method3(n: int) -> int:
        """
        方法3: 内置函数法（简单但较慢）
        思路: 对每个数使用内置的bit_count函数（Python 3.10+）
        
        时间复杂度: O(n log n)
        空间复杂度: O(1)
        
        Args:
            n: 要计算的最大整数
            
        Returns:
            int: 从1到n的所有整数的二进制表示中1的个数之和
        """
        if n < 0:
            raise ValueError("输入必须是非负数")
        
        count = 0
        for i in range(1, n + 1):
            count += i.bit_count()
        return count
    
    @staticmethod
    def count_bits_method4(n: int) -> int:
        """
        方法4: 查表法（空间换时间）
        思路: 预计算小范围的popcount，然后分段计算
        
        时间复杂度: O(n)
        空间复杂度: O(1) - 查表空间固定
        
        Args:
            n: 要计算的最大整数
            
        Returns:
            int: 从1到n的所有整数的二进制表示中1的个数之和
        """
        if n < 0:
            raise ValueError("输入必须是非负数")
        
        # 预计算0-255的popcount
        table = [0] * 256
        for i in range(256):
            table[i] = table[i // 2] + (i & 1)
        
        count = 0
        for i in range(1, n + 1):
            num = i
            # 分8次查表（64位整数）
            count += table[num & 0xFF] + \
                    table[(num >> 8) & 0xFF] + \
                    table[(num >> 16) & 0xFF] + \
                    table[(num >> 24) & 0xFF] + \
                    table[(num >> 32) & 0xFF] + \
                    table[(num >> 40) & 0xFF] + \
                    table[(num >> 48) & 0xFF] + \
                    table[(num >> 56) & 0xFF]
        
        return count
    
    @staticmethod
    def performance_test():
        """性能测试工具"""
        test_cases = [1000, 10000, 100000, 1000000]
        methods = [
            ("逐位计算法", CountBits.count_bits_method1),
            ("动态规划法", CountBits.count_bits_method2),
            ("内置函数法", CountBits.count_bits_method3),
            ("查表法", CountBits.count_bits_method4)
        ]
        
        print("=== 性能测试 ===")
        for n in test_cases:
            print(f"n = {n}:")
            
            for method_name, method_func in methods:
                start_time = time.time()
                result = method_func(n)
                end_time = time.time()
                duration = (end_time - start_time) * 1e6  # 转换为微秒
                
                print(f"  {method_name:15}: {result:10} ({duration:8.2f} μs)")
            print()
    
    @staticmethod
    def unit_test():
        """单元测试"""
        print("=== 单元测试 ===")
        
        test_cases = [
            (0, 0),
            (1, 1),
            (2, 2),
            (3, 4),
            (4, 5),
            (5, 7),
            (10, 17),
            (100, 197),
            (1000, 1987)
        ]
        
        methods = [
            CountBits.count_bits_method1,
            CountBits.count_bits_method2,
            CountBits.count_bits_method3,
            CountBits.count_bits_method4
        ]
        
        for n, expected in test_cases:
            print(f"测试 n = {n} (期望: {expected})")
            
            for i, method in enumerate(methods, 1):
                try:
                    result = method(n)
                    passed = (result == expected)
                    status = "通过" if passed else "失败"
                    print(f"  方法{i}: {result} ({status})")
                    
                    if not passed:
                        print(f"错误: 方法{i}计算结果错误", file=sys.stderr)
                except Exception as e:
                    print(f"错误: 方法{i}抛出异常: {e}", file=sys.stderr)
            print()
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("=== 复杂度分析 ===")
        
        print("方法1 - 逐位计算法:")
        print("  时间复杂度: O(log n) - 处理每一位")
        print("  空间复杂度: O(1) - 只使用常数空间")
        print("  优势: 最优时间复杂度，适合大数计算")
        print("  劣势: 逻辑相对复杂")
        
        print("方法2 - 动态规划法:")
        print("  时间复杂度: O(log n) - 递归深度为log n")
        print("  空间复杂度: O(log n) - 递归栈空间")
        print("  优势: 思路清晰，易于理解")
        print("  劣势: 递归可能栈溢出")
        
        print("方法3 - 内置函数法:")
        print("  时间复杂度: O(n log n) - 每个数需要O(log n)时间")
        print("  空间复杂度: O(1) - 常数空间")
        print("  优势: 实现简单")
        print("  劣势: 时间复杂度较高")
        
        print("方法4 - 查表法:")
        print("  时间复杂度: O(n) - 线性扫描")
        print("  空间复杂度: O(1) - 查表空间固定")
        print("  优势: 比内置函数法快")
        print("  劣势: 仍然需要线性时间")
    
    @staticmethod
    def boundary_test():
        """边界测试"""
        print("=== 边界测试 ===")
        
        boundary_cases = [
            0, 1, 2, 3,
            (1 << 62) - 1,  # 2^62 - 1
            (1 << 62)       # 2^62
        ]
        
        for n in boundary_cases:
            print(f"测试边界值 n = {n}:")
            
            try:
                result1 = CountBits.count_bits_method1(n)
                result2 = CountBits.count_bits_method2(n)
                
                print(f"  方法1结果: {result1}")
                print(f"  方法2结果: {result2}")
                
                if n <= 1000000:  # 避免超时
                    result3 = CountBits.count_bits_method3(n)
                    result4 = CountBits.count_bits_method4(n)
                    print(f"  方法3结果: {result3}")
                    print(f"  方法4结果: {result4}")
                    
                    # 验证一致性
                    if result1 == result2 == result3 == result4:
                        print("  ✓ 所有方法结果一致")
                    else:
                        print("  ✗ 方法结果不一致", file=sys.stderr)
            except Exception as e:
                print(f"  错误: {e}", file=sys.stderr)
            print()
    
    @staticmethod
    def interactive_test():
        """交互式测试"""
        print("=== 交互式测试 ===")
        print("请输入要测试的n值 (输入-1退出): ")
        
        while True:
            try:
                n_str = input().strip()
                if not n_str:
                    continue
                
                n = int(n_str)
                if n == -1:
                    break
                
                if n < 0:
                    print("错误: 输入必须是非负数")
                    continue
                
                start_time = time.time()
                result = CountBits.count_bits_method1(n)
                end_time = time.time()
                duration = (end_time - start_time) * 1e6  # 转换为微秒
                
                print(f"结果: {result} (计算时间: {duration:.2f} μs)")
                
                # 对于较小的n，显示详细计算过程
                if n <= 100:
                    print("详细计算过程:")
                    total_sum = 0
                    for i in range(1, n + 1):
                        bits = i.bit_count()
                        total_sum += bits
                        binary_str = bin(i)[2:].zfill(8)
                        print(f"  {i:3} ({binary_str:>8}): {bits} 个1")
                    print(f"总和: {total_sum}")
                
            except ValueError:
                print("错误: 请输入有效的整数")
            except KeyboardInterrupt:
                print("\n程序被用户中断")
                break
            except Exception as e:
                print(f"计算错误: {e}", file=sys.stderr)


def main():
    """主函数 - 测试和演示"""
    print("SPOJ Counting Bits 问题解决方案")
    print("计算从1到n的所有整数的二进制表示中1的个数之和")
    print("=" * 60)
    
    # 运行单元测试
    CountBits.unit_test()
    
    # 运行边界测试
    CountBits.boundary_test()
    
    # 运行性能测试
    CountBits.performance_test()
    
    # 复杂度分析
    CountBits.complexity_analysis()
    
    # 交互式测试
    CountBits.interactive_test()
    
    print("程序结束")


if __name__ == "__main__":
    main()
"""
位算法优化实现 - 第一部分
包含LeetCode多个位算法优化相关题目的解决方案

题目列表:
1. LeetCode 29 - 两数相除
2. LeetCode 50 - Pow(x, n)
3. LeetCode 60 - 排列序列
4. LeetCode 89 - 格雷编码
5. LeetCode 134 - 加油站
"""

import time
from typing import List
import sys
import math
from functools import lru_cache
from collections import defaultdict

class BitAlgorithmOptimizations:
    """位算法优化类"""
    
    @staticmethod
    def divide(dividend: int, divisor: int) -> int:
        """
        LeetCode 29 - 两数相除
        给定两个整数，被除数 dividend 和除数 divisor。将两数相除，要求不使用乘法、除法和 mod 运算符。
        
        方法: 位运算 + 二分查找
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        原理: 使用位运算模拟除法，通过左移实现快速乘法
        """
        # 处理特殊情况
        if dividend == -2**31 and divisor == -1:
            return 2**31 - 1  # 溢出情况
        
        if divisor == 1:
            return dividend
        if divisor == -1:
            return -dividend
        
        # 确定符号
        negative = (dividend < 0) ^ (divisor < 0)
        
        # 转换为正数处理
        ldividend = abs(dividend)
        ldivisor = abs(divisor)
        
        result = 0
        
        while ldividend >= ldivisor:
            temp = ldivisor
            multiple = 1
            
            # 使用位运算加速
            while ldividend >= (temp << 1):
                temp <<= 1
                multiple <<= 1
            
            ldividend -= temp
            result += multiple
        
        return -result if negative else result
    
    @staticmethod
    def my_pow(x: float, n: int) -> float:
        """
        LeetCode 50 - Pow(x, n)
        实现 pow(x, n) ，即计算 x 的 n 次幂函数。
        
        方法: 快速幂算法（位运算）
        时间复杂度: O(log n)
        空间复杂度: O(1)
        
        原理: 将指数n分解为二进制，利用x^(a+b) = x^a * x^b
        """
        if n == 0:
            return 1.0
        if x == 1.0:
            return 1.0
        if x == -1.0:
            return 1.0 if n % 2 == 0 else -1.0
        
        N = n
        if N < 0:
            x = 1 / x
            N = -N
        
        result = 1.0
        current_product = x
        
        # 快速幂算法
        while N > 0:
            if N % 2 == 1:
                result *= current_product
            current_product *= current_product
            N //= 2
        
        return result
    
    @staticmethod
    def get_permutation(n: int, k: int) -> str:
        """
        LeetCode 60 - 排列序列
        给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
        按大小顺序列出所有排列情况，并一一标记，返回第 k 个排列。
        
        方法: 数学 + 位标记
        时间复杂度: O(n^2)
        空间复杂度: O(n)
        """
        # 计算阶乘
        factorial = [1] * (n + 1)
        for i in range(1, n + 1):
            factorial[i] = factorial[i-1] * i
        
        # 标记已使用的数字
        used = [False] * (n + 1)
        result = []
        
        k -= 1  # 转换为0-based索引
        
        for i in range(n, 0, -1):
            # 确定当前位的数字
            segment = factorial[i-1]
            index = k // segment
            k %= segment
            
            # 找到第index个未使用的数字
            count = 0
            for j in range(1, n + 1):
                if not used[j]:
                    if count == index:
                        result.append(str(j))
                        used[j] = True
                        break
                    count += 1
        
        return ''.join(result)
    
    @staticmethod
    def gray_code(n: int) -> List[int]:
        """
        LeetCode 89 - 格雷编码
        格雷编码是一个二进制数字系统，在该系统中，两个连续的数值仅有一个位数的差异。
        给定一个代表编码总位数的非负整数 n，打印其格雷编码序列。
        
        方法: 镜像反射法
        时间复杂度: O(2^n)
        空间复杂度: O(2^n)
        
        原理: G(i) = i ^ (i >> 1)
        """
        result = []
        total = 1 << n
        
        for i in range(total):
            result.append(i ^ (i >> 1))
        
        return result
    
    @staticmethod
    def can_complete_circuit(gas: List[int], cost: List[int]) -> int:
        """
        LeetCode 134 - 加油站
        在一条环路上有 N 个加油站，从其中的一个加油站出发，开始时油箱为空。
        如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
        
        方法: 贪心算法
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        total_gas = sum(gas)
        total_cost = sum(cost)
        
        if total_gas < total_cost:
            return -1
        
        current_gas = 0
        start_index = 0
        
        for i in range(len(gas)):
            current_gas += gas[i] - cost[i]
            
            if current_gas < 0:
                start_index = i + 1
                current_gas = 0
        
        return start_index
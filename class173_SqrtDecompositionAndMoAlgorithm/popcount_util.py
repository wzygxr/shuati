#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Popcount（汉明重量）工具类
提供多种高效计算二进制中1的个数的方法
用于算法优化和位运算加速
"""

def popcount_builtin(x):
    """
    使用Python内置方法计算popcount
    对于Python 3.10+，可以使用int.bit_count()方法
    
    Args:
        x: 待计算的整数
    
    Returns:
        int: x的二进制表示中1的个数
    """
    if hasattr(int, 'bit_count'):
        # Python 3.10+ 的内置方法
        return x.bit_count()
    else:
        # 兼容旧版本Python
        return bin(x).count('1')

def popcount_bitwise(x):
    """
    使用位运算优化实现popcount
    分治法计算，适用于任何平台
    
    Args:
        x: 待计算的整数
    
    Returns:
        int: x的二进制表示中1的个数
    """
    # 由于Python的int可以无限大，我们需要处理负数和大整数的情况
    # 对于正数，直接处理
    # 对于负数，我们只处理其低32位
    if x < 0:
        x &= 0xFFFFFFFF
    
    # 每两位一组，统计每组中1的个数
    x = x - ((x >> 1) & 0x55555555)
    # 每四位一组，统计每组中1的个数
    x = (x & 0x33333333) + ((x >> 2) & 0x33333333)
    # 每八位一组，统计每组中1的个数
    x = (x + (x >> 4)) & 0x0f0f0f0f
    # 累加所有8位组
    x = x + (x >> 8)
    x = x + (x >> 16)
    # 取低6位作为结果
    return x & 0x3f

def popcount_loop(x):
    """
    使用循环移位实现popcount
    简单直观，但效率较低
    
    Args:
        x: 待计算的整数
    
    Returns:
        int: x的二进制表示中1的个数
    """
    count = 0
    # 处理负数情况
    if x < 0:
        x &= 0xFFFFFFFF
    while x != 0:
        count += x & 1
        x >>= 1
    return count

def popcount_kernighan(x):
    """
    使用Brian Kernighan算法计算popcount
    每次清除最低位的1，直到所有位都为0
    
    Args:
        x: 待计算的整数
    
    Returns:
        int: x的二进制表示中1的个数
    """
    count = 0
    # 处理负数情况
    if x < 0:
        x &= 0xFFFFFFFF
    while x != 0:
        x &= x - 1  # 清除最低位的1
        count += 1
    return count

# 预计算查表
POPCOUNT_TABLE = [0] * 256
for i in range(256):
    POPCOUNT_TABLE[i] = popcount_loop(i)

def popcount_table(x):
    """
    使用查表法计算popcount
    适用于32位整数
    
    Args:
        x: 待计算的整数
    
    Returns:
        int: x的二进制表示中1的个数
    """
    # 处理负数情况
    if x < 0:
        x &= 0xFFFFFFFF
    return (POPCOUNT_TABLE[(x >> 24) & 0xFF] +
            POPCOUNT_TABLE[(x >> 16) & 0xFF] +
            POPCOUNT_TABLE[(x >> 8) & 0xFF] +
            POPCOUNT_TABLE[x & 0xFF])

def hamming_distance(x, y):
    """
    计算两个数的汉明距离（二进制不同位的个数）
    
    Args:
        x: 第一个数
        y: 第二个数
    
    Returns:
        int: 汉明距离
    """
    return popcount_builtin(x ^ y)

def popcount_array(arr):
    """
    计算数组中所有数的popcount之和
    适用于需要统计多个数的位信息的场景
    
    Args:
        arr: 整数数组
    
    Returns:
        int: 所有数的popcount之和
    """
    return sum(popcount_builtin(x) for x in arr)

def benchmark():
    """
    性能测试方法
    比较不同popcount实现的性能
    """
    import time
    
    ITERATIONS = 1000000
    test_numbers = [
        0, 1, -1, 2**31 - 1, -(2**31),
        0xAAAAAAAA, 0x55555555, 0x12345678
    ]
    
    print(f"Popcount性能测试（{ITERATIONS}次迭代）：")
    
    # 测试内置方法
    start = time.time()
    total = 0
    for i in range(ITERATIONS):
        total += popcount_builtin(test_numbers[i % len(test_numbers)])
    end = time.time()
    print(f"内置方法: {(end - start) * 1000:.3f} ms, 结果: {total}")
    
    # 测试位运算法
    start = time.time()
    total = 0
    for i in range(ITERATIONS):
        total += popcount_bitwise(test_numbers[i % len(test_numbers)])
    end = time.time()
    print(f"位运算法: {(end - start) * 1000:.3f} ms, 结果: {total}")
    
    # 测试Kernighan算法
    start = time.time()
    total = 0
    for i in range(ITERATIONS):
        total += popcount_kernighan(test_numbers[i % len(test_numbers)])
    end = time.time()
    print(f"Kernighan算法: {(end - start) * 1000:.3f} ms, 结果: {total}")
    
    # 测试查表法
    start = time.time()
    total = 0
    for i in range(ITERATIONS):
        total += popcount_table(test_numbers[i % len(test_numbers)])
    end = time.time()
    print(f"查表法: {(end - start) * 1000:.3f} ms, 结果: {total}")

def test_popcount():
    """
    测试各种popcount实现
    """
    test_num = 0b101101
    print(f"测试数字: {test_num} (二进制: {bin(test_num)})")
    print(f"内置方法popcount: {popcount_builtin(test_num)}")
    print(f"位运算法popcount: {popcount_bitwise(test_num)}")
    print(f"循环法popcount: {popcount_loop(test_num)}")
    print(f"Kernighan算法popcount: {popcount_kernighan(test_num)}")
    print(f"查表法popcount: {popcount_table(test_num)}")
    
    print("\n汉明距离测试:")
    a = 0b1010
    b = 0b1100
    print(f"{a}({bin(a)}) 和 {b}({bin(b)}) 的汉明距离: {hamming_distance(a, b)}")
    
    print("\n性能测试:")
    benchmark()

if __name__ == "__main__":
    test_popcount()
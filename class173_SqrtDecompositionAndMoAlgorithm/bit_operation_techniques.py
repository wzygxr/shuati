#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
位运算技巧工具类
提供高效的位操作实现，包括：
1. 子集枚举（使用经典的 for(s=sub; s; s=(s-1)&mask) 模式）
2. Popcount（汉明重量）优化算法
3. 掩码预处理技术

位运算在算法优化中具有极高的效率，可以将O(n)的操作优化到O(1)或更低
"""

import time
import random
from typing import List, Dict, Set, Tuple


def enumerate_subsets(mask: int) -> List[int]:
    """
    使用经典的子集枚举算法枚举指定掩码的所有非空子集
    时间复杂度：O(2^k)，其中k是掩码中1的个数
    空间复杂度：O(1)，不计算结果存储
    
    Args:
        mask: 要枚举子集的掩码
    
    Returns:
        List[int]: 所有非空子集的列表
    """
    subsets = []
    sub = mask
    # 经典的子集枚举模式：for(s=sub; s; s=(s-1)&mask)
    while True:
        subsets.append(sub)
        sub = (sub - 1) & mask
        if sub == mask:
            break  # 当回到mask时，表示所有子集都已枚举完成
    return subsets


def enumerate_subset_pairs(mask: int) -> List[Tuple[int, int]]:
    """
    使用位运算枚举所有可能的子集对，满足A是B的子集
    时间复杂度：O(3^n)，其中n是位数（对于n位数字，共有3^n种这样的子集对）
    
    Args:
        mask: 最大的掩码
    
    Returns:
        List[Tuple[int, int]]: 所有满足A是B的子集对 (A, B) 的列表
    """
    pairs = []
    # 枚举所有可能的B
    b = 0
    while True:
        # 枚举B的所有子集A
        a = b
        while True:
            pairs.append((a, b))
            a = (a - 1) & b
            if a == b:
                break
        
        # 生成下一个B
        b = (b - mask) & mask
        if b == 0 and mask != 0:
            break
        
    return pairs

# 预处理8位数字的popcount查找表
POPCOUNT_TABLE = [0] * 256
for i in range(256):
    POPCOUNT_TABLE[i] = bin(i).count('1')


def popcount_table(n: int) -> int:
    """
    使用查表法计算整数的二进制中1的个数（popcount）
    这是一种空间换时间的优化方法，预处理所有可能的字节值
    时间复杂度：O(1)
    空间复杂度：O(1)，需要256字节的查找表
    
    Args:
        n: 要计算的整数
    
    Returns:
        int: n的二进制表示中1的个数
    """
    # 将整数分解为多个字节，查表累加
    count = 0
    while n:
        count += POPCOUNT_TABLE[n & 0xFF]
        n >>= 8
    return count


def popcount_brian_kernighan(n: int) -> int:
    """
    使用Brian Kernighan算法计算popcount
    时间复杂度：O(k)，其中k是1的个数
    空间复杂度：O(1)
    
    Args:
        n: 要计算的整数
    
    Returns:
        int: n的二进制表示中1的个数
    """
    count = 0
    # 每次循环清除最低位的1，直到n变为0
    while n:
        n &= n - 1  # 清除最低位的1
        count += 1
    return count


def popcount_builtin(n: int) -> int:
    """
    使用Python内置函数计算popcount
    这是Python中最高效的popcount实现
    
    Args:
        n: 要计算的整数
    
    Returns:
        int: n的二进制表示中1的个数
    """
    return bin(n).count('1')


def precompute_subsets_by_size(n: int) -> List[List[int]]:
    """
    预处理所有可能的子集掩码，按1的个数分组
    这在需要按子集大小处理问题时非常有用
    
    Args:
        n: 位数
    
    Returns:
        List[List[int]]: 按1的个数分组的子集列表，其中第k个列表包含所有恰好有k个1的掩码
    """
    subsets_by_size = [[] for _ in range(n + 1)]
    
    # 枚举所有可能的子集（0到2^n-1）
    max_mask = (1 << n) - 1
    for mask in range(max_mask + 1):
        count = popcount_builtin(mask)
        subsets_by_size[count].append(mask)
    
    return subsets_by_size


def precompute_complements(n: int) -> Dict[int, int]:
    """
    预处理所有可能的掩码及其对应的补码
    
    Args:
        n: 位数
    
    Returns:
        Dict[int, int]: 掩码到其补码的映射
    """
    complements = {}
    max_mask = (1 << n) - 1
    for mask in range(max_mask + 1):
        complements[mask] = mask ^ max_mask  # 异或操作计算补码
    return complements


def precompute_supersets(n: int) -> Dict[int, List[int]]:
    """
    预处理所有可能的掩码及其超集
    
    Args:
        n: 位数
    
    Returns:
        Dict[int, List[int]]: 每个掩码对应的所有超集
    """
    supersets = {}
    max_mask = (1 << n) - 1
    
    # 初始化每个掩码的超集列表
    for mask in range(max_mask + 1):
        supersets[mask] = []
    
    # 对于每个可能的超集
    for superset in range(max_mask + 1):
        # 找出它的所有子集并更新对应子集的超集列表
        subset = superset
        while True:
            supersets[subset].append(superset)
            subset = (subset - 1) & superset
            if subset == superset:
                break
    
    return supersets


def are_disjoint(a: int, b: int) -> bool:
    """
    检查两个掩码是否不相交（没有共同的1位）
    时间复杂度：O(1)
    
    Args:
        a: 第一个掩码
        b: 第二个掩码
    
    Returns:
        bool: 如果两个掩码不相交返回True，否则返回False
    """
    return (a & b) == 0


def symmetric_difference(a: int, b: int) -> int:
    """
    计算两个掩码的对称差（异或）
    时间复杂度：O(1)
    
    Args:
        a: 第一个掩码
        b: 第二个掩码
    
    Returns:
        int: 对称差的结果
    """
    return a ^ b


def get_lowest_set_bit(mask: int) -> int:
    """
    获取掩码中最低位的1
    时间复杂度：O(1)
    
    Args:
        mask: 输入掩码
    
    Returns:
        int: 只保留最低位1的掩码
    """
    # 使用补码性质：-mask 是 mask 的补码
    return mask & -mask


def get_highest_set_bit(mask: int) -> int:
    """
    获取掩码中最高位的1
    时间复杂度：O(1)
    
    Args:
        mask: 输入掩码
    
    Returns:
        int: 只保留最高位1的掩码
    """
    if mask == 0:
        return 0
    # 找到最高位1的位置
    highest_bit_position = mask.bit_length() - 1
    return 1 << highest_bit_position


def get_lowest_set_bit_position(mask: int) -> int:
    """
    计算掩码中1的最低位置（从0开始计数）
    时间复杂度：O(1)
    
    Args:
        mask: 输入掩码
    
    Returns:
        int: 最低位1的位置，如果没有1则返回-1
    """
    if mask == 0:
        return -1
    # 计算末尾0的个数
    return (mask & -mask).bit_length() - 1


def get_highest_set_bit_position(mask: int) -> int:
    """
    计算掩码中1的最高位置（从0开始计数）
    时间复杂度：O(1)
    
    Args:
        mask: 输入掩码
    
    Returns:
        int: 最高位1的位置，如果没有1则返回-1
    """
    if mask == 0:
        return -1
    return mask.bit_length() - 1


def calculate_subset_xor(nums: List[int]) -> List[int]:
    """
    计算所有可能的子集异或和
    时间复杂度：O(2^n)
    
    Args:
        nums: 输入数组
    
    Returns:
        List[int]: 所有子集异或和的列表
    """
    xors = {0}  # 空集的异或和为0
    
    for num in nums:
        new_xors = set(xors)
        for xor in xors:
            new_xors.add(xor ^ num)
        xors = new_xors
    
    return list(xors)


def knapsack_with_bitmask(weights: List[int], values: List[int], capacity: int) -> int:
    """
    使用位掩码优化的动态规划解决背包问题
    时间复杂度：O(2^n)
    空间复杂度：O(1)，使用整型掩码表示状态
    
    Args:
        weights: 物品重量数组
        values: 物品价值数组
        capacity: 背包容量
    
    Returns:
        int: 最大价值
    """
    n = len(weights)
    max_value = 0
    
    # 枚举所有可能的子集（2^n种可能）
    for mask in range(1 << n):
        total_weight = 0
        total_value = 0
        
        # 计算子集的总重量和总价值
        for i in range(n):
            if mask & (1 << i):
                total_weight += weights[i]
                total_value += values[i]
                
                # 剪枝：如果总重量已经超过容量，提前退出
                if total_weight > capacity:
                    break
        
        # 如果总重量不超过容量，更新最大价值
        if total_weight <= capacity and total_value > max_value:
            max_value = total_value
    
    return max_value


def test_bit_operations():
    """
    测试位运算技巧的各种功能
    """
    print("=== 位运算技巧测试 ===")
    
    # 测试子集枚举
    mask = 0b1011  # 二进制 1011，十进制 11
    print(f"枚举掩码 0b{mask:b} 的所有子集：")
    subsets = enumerate_subsets(mask)
    for subset in subsets:
        print(f"0b{subset:b} ({subset})")
    
    # 测试Popcount方法比较
    print("\nPopcount方法比较：")
    test_number = 0xAAAAAAAA  # 二进制中1的个数为16
    print(f"数字 0x{test_number:x} 的二进制中1的个数：")
    print(f"查表法: {popcount_table(test_number)}")
    print(f"Brian Kernighan算法: {popcount_brian_kernighan(test_number)}")
    print(f"Python内置方法: {popcount_builtin(test_number)}")
    
    # 测试预处理子集
    print("\n按1的个数分组的子集（n=4）：")
    subsets_by_size = precompute_subsets_by_size(4)
    for i in range(len(subsets_by_size)):
        print(f"包含 {i} 个1的子集：")
        for subset in subsets_by_size[i]:
            # 格式化二进制输出，保持4位
            bin_str = bin(subset)[2:].zfill(4)
            print(f"0b{bin_str} ({subset})  ", end="")
        print()
    
    # 测试位操作辅助函数
    print("\n位操作辅助函数：")
    test_mask = 0b101010  # 二进制 101010，十进制 42
    print(f"掩码 0b{test_mask:b} ({test_mask}) 的最低位1: 0b{get_lowest_set_bit(test_mask):b}")
    print(f"掩码 0b{test_mask:b} ({test_mask}) 的最高位1: 0b{get_highest_set_bit(test_mask):b}")
    print(f"掩码 0b{test_mask:b} ({test_mask}) 的最低位1位置: {get_lowest_set_bit_position(test_mask)}")
    print(f"掩码 0b{test_mask:b} ({test_mask}) 的最高位1位置: {get_highest_set_bit_position(test_mask)}")
    
    # 测试子集异或和
    print("\n子集异或和计算：")
    nums = [1, 2, 3]
    xors = calculate_subset_xor(nums)
    print(f"数组 {nums} 的所有子集异或和: {xors}")
    
    # 测试掩码优化的背包问题
    print("\n掩码优化的背包问题：")
    weights = [2, 3, 4, 5]
    values = [3, 4, 5, 6]
    capacity = 8
    max_value = knapsack_with_bitmask(weights, values, capacity)
    print(f"物品重量: {weights}")
    print(f"物品价值: {values}")
    print(f"背包容量: {capacity}")
    print(f"最大价值: {max_value}")


def benchmark_popcount():
    """
    比较不同Popcount实现的性能
    """
    print("\n=== Popcount性能基准测试 ===")
    
    test_iterations = 1000000
    test_numbers = [random.randint(0, 2**31 - 1) for _ in range(test_iterations)]
    
    # 测试查表法性能
    start_time = time.time()
    table_sum = 0
    for num in test_numbers:
        table_sum += popcount_table(num)
    table_time = time.time() - start_time
    
    # 测试Brian Kernighan算法性能
    start_time = time.time()
    bk_sum = 0
    for num in test_numbers:
        bk_sum += popcount_brian_kernighan(num)
    bk_time = time.time() - start_time
    
    # 测试Python内置方法性能
    start_time = time.time()
    builtin_sum = 0
    for num in test_numbers:
        builtin_sum += popcount_builtin(num)
    builtin_time = time.time() - start_time
    
    # 验证结果一致性
    results_match = (table_sum == bk_sum) and (bk_sum == builtin_sum)
    
    print(f"测试 {test_iterations} 次Popcount操作的结果一致性: {results_match}")
    print(f"查表法耗时: {table_time:.6f} 秒")
    print(f"Brian Kernighan算法耗时: {bk_time:.6f} 秒")
    print(f"Python内置方法耗时: {builtin_time:.6f} 秒")
    print(f"加速比 (查表法/BK): {table_time / bk_time:.2f}x")
    print(f"加速比 (查表法/内置): {table_time / builtin_time:.2f}x")
    print(f"加速比 (BK/内置): {bk_time / builtin_time:.2f}x")


def interactive_mode():
    """
    交互式测试函数
    """
    print("=== 位运算技巧工具 ===")
    print("输入操作编号:")
    print("1. 枚举子集")
    print("2. 计算Popcount")
    print("3. 位操作辅助函数")
    print("4. 子集异或和计算")
    print("5. 掩码优化背包问题")
    print("6. Popcount性能基准测试")
    print("0. 退出")
    
    while True:
        try:
            choice = int(input("\n请输入操作编号: "))
            
            if choice == 0:
                print("程序已退出")
                return
            elif choice == 1:
                mask = int(input("请输入掩码（十进制）: "))
                subsets = enumerate_subsets(mask)
                print(f"掩码 {mask} (0b{mask:b}) 的所有子集：")
                for subset in subsets:
                    print(f"0b{subset:b} ({subset})")
            elif choice == 2:
                num = int(input("请输入要计算Popcount的数字: "))
                print(f"数字 {num} (0b{num:b}) 的二进制中1的个数：")
                print(f"查表法: {popcount_table(num)}")
                print(f"Brian Kernighan算法: {popcount_brian_kernighan(num)}")
                print(f"Python内置方法: {popcount_builtin(num)}")
            elif choice == 3:
                test_mask = int(input("请输入掩码（十进制）: "))
                print(f"掩码 {test_mask} (0b{test_mask:b}) 的位操作结果：")
                print(f"最低位1: 0b{get_lowest_set_bit(test_mask):b} ({get_lowest_set_bit(test_mask)})")
                print(f"最高位1: 0b{get_highest_set_bit(test_mask):b} ({get_highest_set_bit(test_mask)})")
                print(f"最低位1位置: {get_lowest_set_bit_position(test_mask)}")
                print(f"最高位1位置: {get_highest_set_bit_position(test_mask)}")
            elif choice == 4:
                n = int(input("请输入数组元素个数: "))
                print("请输入数组元素（空格分隔）：")
                array = list(map(int, input().split()))[:n]
                xors = calculate_subset_xor(array)
                print(f"数组 {array} 的所有子集异或和: {xors}")
            elif choice == 5:
                item_count = int(input("请输入物品个数: "))
                print("请输入物品的重量和价值（每行一个物品，格式：重量 价值）：")
                weights = []
                values = []
                for i in range(item_count):
                    w, v = map(int, input().split())
                    weights.append(w)
                    values.append(v)
                capacity = int(input("请输入背包容量: "))
                max_value = knapsack_with_bitmask(weights, values, capacity)
                print(f"最大价值: {max_value}")
            elif choice == 6:
                benchmark_popcount()
            else:
                print("无效的操作编号，请重新输入")
        except Exception as e:
            print(f"操作出错: {e}")


if __name__ == "__main__":
    # 运行测试
    test_bit_operations()
    
    # 启动交互模式
    interactive_mode()
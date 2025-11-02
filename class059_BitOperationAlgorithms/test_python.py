#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
位运算算法测试脚本

该脚本用于测试各种位运算算法的正确性，包括加减乘除四则运算以及其他位运算相关算法
所有测试方法都使用位运算实现，避免使用任何算术运算符（+、-、*、/）

测试内容包括：
1. 加法运算测试
2. 减法运算测试
3. 乘法运算测试
4. 除法运算测试
5. 其他位运算相关函数测试

作者: Algorithm Journey
版本: 1.0
"""

# 导入位运算实现模块
from bit_operation_add_minus_multiply_divide import BitOperationAddMinusMultiplyDivide

# 程序入口点
if __name__ == "__main__":
    print("位运算测试：")
    
    # 测试加法
    print("加法测试：")
    print("10 + 20 =", BitOperationAddMinusMultiplyDivide.add(10, 20))
    print("(-5) + 3 =", BitOperationAddMinusMultiplyDivide.add(-5, 3))
    print("(-10) + (-20) =", BitOperationAddMinusMultiplyDivide.add(-10, -20))
    
    # 测试减法
    print("\n减法测试：")
    print("10 - 5 =", BitOperationAddMinusMultiplyDivide.minus(10, 5))
    print("5 - 10 =", BitOperationAddMinusMultiplyDivide.minus(5, 10))
    
    # 测试乘法
    print("\n乘法测试：")
    print("6 * 7 =", BitOperationAddMinusMultiplyDivide.multiply(6, 7))
    print("(-3) * 4 =", BitOperationAddMinusMultiplyDivide.multiply(-3, 4))
    
    # 测试除法
    print("\n除法测试：")
    print("10 / 3 =", BitOperationAddMinusMultiplyDivide.divide(10, 3))
    print("(-10) / 3 =", BitOperationAddMinusMultiplyDivide.divide(-10, 3))
    
    # 测试其他位运算相关函数
    print("\n其他位运算函数测试：")
    print("二进制中1的个数 (10):", BitOperationAddMinusMultiplyDivide.hamming_weight(10))
    print("是否为2的幂 (8):", BitOperationAddMinusMultiplyDivide.is_power_of_two(8))
    print("是否为2的幂 (10):", BitOperationAddMinusMultiplyDivide.is_power_of_two(10))
    print("汉明距离 (1, 4):", BitOperationAddMinusMultiplyDivide.hamming_distance(1, 4))
    
    # 测试新添加的函数
    print("\n新添加函数测试：")
    
    # 测试只出现一次的数字
    nums1 = [2, 2, 1]
    print("只出现一次的数字 [2,2,1]:", BitOperationAddMinusMultiplyDivide.single_number(nums1))
    
    # 测试缺失的数字
    nums2 = [3, 0, 1]
    print("缺失的数字 [3,0,1]:", BitOperationAddMinusMultiplyDivide.missing_number(nums2))
    
    # 测试比特位计数
    bits = BitOperationAddMinusMultiplyDivide.count_bits(5)
    print("比特位计数 (5):", bits)
    
    # 测试只出现一次的数字III
    nums3 = [1, 2, 1, 3, 2, 5]
    result = BitOperationAddMinusMultiplyDivide.single_numberIII(nums3)
    print("只出现一次的数字III [1,2,1,3,2,5]:", result)
    
    # 测试数组中两个数的最大异或值
    nums4 = [3, 10, 5, 25, 2, 8]
    print("数组中两个数的最大异或值 [3,10,5,25,2,8]:", BitOperationAddMinusMultiplyDivide.find_maximum_xor(nums4))
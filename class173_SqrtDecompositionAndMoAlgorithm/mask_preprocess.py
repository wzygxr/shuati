#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
掩码预处理工具模块
提供常用位掩码的预处理和位运算优化功能
用于算法中的位操作加速
"""

# 预定义的最大数组大小
MAX_N = 1 << 20  # 1,048,576

# 常用掩码
ALL_ONES_8 = 0xFF        # 8位全1掩码
ALL_ONES_16 = 0xFFFF     # 16位全1掩码
ALL_ONES_32 = 0xFFFFFFFF # 32位全1掩码

ALTERNATE_ODD = 0x55555555  # 01010101...
ALTERNATE_EVEN = 0xAAAAAAAA # 10101010...

ALL_ONES_LAST_4 = 0x0000000F  # 最后4位全1
ALL_ONES_FIRST_4 = 0xF0000000 # 最前4位全1

# 预处理数组
lowbit = None         # 最低位1的值
highest_bit = None    # 最高位1的位置（从0开始）
bit_count = None      # 二进制中1的个数（popcount）
next_set_bit = None   # 下一个设置位的位置
prev_set_bit = None   # 上一个设置位的位置
parity = None         # 奇偶校验位（1的个数的奇偶性）

def initialize():
    """
    初始化所有预处理数组
    """
    global lowbit, highest_bit, bit_count, next_set_bit, prev_set_bit, parity
    
    # 初始化数组
    lowbit = [0] * MAX_N
    highest_bit = [0] * MAX_N
    bit_count = [0] * MAX_N
    next_set_bit = [-1] * MAX_N
    prev_set_bit = [-1] * MAX_N
    parity = [0] * MAX_N
    
    # 计算最低位1
    for i in range(1, MAX_N):
        lowbit[i] = i & (-i)
    
    # 计算最高位1的位置
    highest_bit[0] = -1  # 特殊情况
    for i in range(1, MAX_N):
        highest_bit[i] = highest_bit[i >> 1] + 1
    
    # 计算bit_count（使用动态规划）
    bit_count[0] = 0
    for i in range(1, MAX_N):
        bit_count[i] = bit_count[i & (i - 1)] + 1
    
    # 计算nextSetBit
    for i in range(1, MAX_N):
        temp = i
        lsb = temp & -temp
        temp ^= lsb  # 清除最低位1
        if temp != 0:
            # 计算下一个最低位1的位置
            next_set_bit[i] = (temp & -temp).bit_length() - 1
    
    # 计算prevSetBit
    for i in range(1, MAX_N):
        temp = i
        highest = temp.bit_length() - 1
        temp ^= (1 << highest)  # 清除最高位1
        if temp != 0:
            prev_set_bit[i] = temp.bit_length() - 1
    
    # 计算奇偶校验
    parity[0] = 0
    for i in range(1, MAX_N):
        parity[i] = parity[i >> 1] ^ (i & 1)

# 初始化预处理数组
initialize()

def get_lowbit(x):
    """
    获取最低位1的值
    等同于 x & (-x)
    
    Args:
        x: 输入整数
    
    Returns:
        int: 最低位1的值
    """
    if x == 0:
        return 0
    if 0 <= x < MAX_N:
        return lowbit[x]
    # 对于超出预计算范围的数
    return x & (-x)

def get_highest_bit_position(x):
    """
    获取最高位1的位置（从0开始）
    
    Args:
        x: 输入整数
    
    Returns:
        int: 最高位1的位置
    """
    if x == 0:
        return -1
    if 0 <= x < MAX_N:
        return highest_bit[x]
    # 对于超出预计算范围的数
    return x.bit_length() - 1

def get_bit_count(x):
    """
    获取二进制中1的个数
    
    Args:
        x: 输入整数
    
    Returns:
        int: 1的个数
    """
    if x == 0:
        return 0
    if 0 <= x < MAX_N:
        return bit_count[x]
    # 对于超出预计算范围的数
    if hasattr(int, 'bit_count'):
        return x.bit_count()
    return bin(x).count('1')

def get_next_set_bit(x):
    """
    获取下一个设置位的位置
    
    Args:
        x: 输入整数
    
    Returns:
        int: 下一个设置位的位置，如果没有返回-1
    """
    if 0 <= x < MAX_N:
        return next_set_bit[x]
    # 动态计算
    temp = x
    lsb = temp & -temp
    temp ^= lsb
    if temp == 0:
        return -1
    return (temp & -temp).bit_length() - 1

def get_prev_set_bit(x):
    """
    获取上一个设置位的位置
    
    Args:
        x: 输入整数
    
    Returns:
        int: 上一个设置位的位置，如果没有返回-1
    """
    if 0 <= x < MAX_N:
        return prev_set_bit[x]
    # 动态计算
    temp = x
    highest = temp.bit_length() - 1
    temp ^= (1 << highest)
    if temp == 0:
        return -1
    return temp.bit_length() - 1

def get_parity(x):
    """
    获取奇偶校验位（1的个数的奇偶性）
    
    Args:
        x: 输入整数
    
    Returns:
        int: 1表示奇数个1，0表示偶数个1
    """
    if 0 <= x < MAX_N:
        return parity[x]
    # 动态计算
    x ^= x >> 16
    x ^= x >> 8
    x ^= x >> 4
    x ^= x >> 2
    x ^= x >> 1
    return x & 1

def generate_all_ones_mask(length):
    """
    生成特定长度的全1掩码
    
    Args:
        length: 掩码长度
    
    Returns:
        int: 全1掩码
    """
    return (1 << length) - 1

def generate_alternating_mask(start_with_one=True):
    """
    生成交替位掩码
    
    Args:
        start_with_one: 是否以1开始
    
    Returns:
        int: 交替位掩码
    """
    return ALTERNATE_ODD if start_with_one else ALTERNATE_EVEN

def is_power_of_two(x):
    """
    检查数x是否是2的幂
    
    Args:
        x: 输入整数
    
    Returns:
        bool: 是否是2的幂
    """
    return x > 0 and (x & (x - 1)) == 0

def next_power_of_two(x):
    """
    对齐到下一个2的幂
    
    Args:
        x: 输入整数
    
    Returns:
        int: 下一个大于等于x的2的幂
    """
    if x <= 0:
        return 1
    x -= 1
    x |= x >> 1
    x |= x >> 2
    x |= x >> 4
    x |= x >> 8
    x |= x >> 16
    return x + 1

def to_binary_string(x, bits=32):
    """
    将整数转换为固定长度的二进制字符串
    
    Args:
        x: 输入整数
        bits: 位数
    
    Returns:
        str: 二进制字符串
    """
    if x < 0:
        # 对于负数，显示补码
        x = (1 << bits) + x
    return bin(x)[2:].zfill(bits)

def test_mask_preprocess():
    """
    测试掩码预处理功能
    """
    test_num = 0b10110100  # 180
    print(f"测试数字: {test_num} (二进制: {to_binary_string(test_num, 8)})")
    
    print(f"最低位1的值: {get_lowbit(test_num)} (二进制: {to_binary_string(get_lowbit(test_num), 8)})")
    
    print(f"最高位1的位置: {get_highest_bit_position(test_num)}")
    
    print(f"二进制中1的个数: {get_bit_count(test_num)}")
    
    print(f"下一个设置位的位置: {get_next_set_bit(test_num)}")
    
    print(f"上一个设置位的位置: {get_prev_set_bit(test_num)}")
    
    print(f"奇偶校验位: {get_parity(test_num)}")
    
    # 测试掩码生成
    print("\n掩码生成测试:")
    print(f"8位全1掩码: {generate_all_ones_mask(8)} (二进制: {to_binary_string(generate_all_ones_mask(8), 8)})")
    
    print(f"交替位掩码(以1开始): {to_binary_string(generate_alternating_mask(True), 8)}")
    
    print(f"交替位掩码(以0开始): {to_binary_string(generate_alternating_mask(False), 8)}")
    
    # 测试工具方法
    print("\n工具方法测试:")
    print(f"64是否是2的幂: {is_power_of_two(64)}")
    print(f"100是否是2的幂: {is_power_of_two(100)}")
    
    print(f"100的下一个2的幂: {next_power_of_two(100)}")
    print(f"128的下一个2的幂: {next_power_of_two(128)}")

if __name__ == "__main__":
    test_mask_preprocess()
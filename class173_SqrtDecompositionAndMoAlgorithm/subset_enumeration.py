#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
子集枚举算法实现
核心技巧：使用位掩码高效枚举集合的所有子集
时间复杂度：O(2^n)，其中n是元素个数
"""

def enumerate_all_subsets(mask):
    """
    枚举mask的所有非空子集
    核心公式：sub = (sub - 1) & mask
    
    Args:
        mask: 表示原集合的位掩码
    
    Returns:
        list: 所有非空子集的位掩码列表
    """
    subsets = []
    sub = mask
    while sub > 0:
        subsets.append(sub)
        sub = (sub - 1) & mask
    return subsets

def enumerate_subsets_of_size(n, k):
    """
    枚举大小为k的子集
    使用Gosper's Hack算法
    
    Args:
        n: 集合大小
        k: 子集大小
    
    Returns:
        list: 所有大小为k的子集的位掩码列表
    """
    subsets = []
    if k == 0 or k > n:
        return subsets
    
    mask = (1 << k) - 1  # 初始化为k个1
    
    while mask < (1 << n):
        subsets.append(mask)
        
        # Gosper's Hack - 高效生成下一个k元素子集
        c = mask & -mask  # 最低位的1
        r = mask + c
        mask = (((r ^ mask) >> 2) // c) | r
    
    return subsets

def enumerate_subsets_with_required(mask, required_elements):
    """
    枚举包含特定元素的子集
    
    Args:
        mask: 原集合的位掩码
        required_elements: 必须包含的元素的位掩码
    
    Returns:
        list: 包含所有required_elements的子集的位掩码列表
    """
    subsets = []
    
    # 检查required_elements是否是mask的子集
    if (required_elements & mask) != required_elements:
        return subsets  # required_elements包含mask中没有的元素
    
    # 枚举所有包含required_elements的子集
    remaining = mask & (~required_elements)
    sub = remaining
    while True:
        subsets.append(sub | required_elements)
        if sub == 0:
            break
        sub = (sub - 1) & remaining
    
    return subsets

def mask_to_indices(mask):
    """
    将位掩码转换为元素索引列表
    
    Args:
        mask: 位掩码
    
    Returns:
        list: 元素索引列表
    """
    indices = []
    i = 0
    while mask > 0:
        if mask & 1:
            indices.append(i)
        mask >>= 1
        i += 1
    return indices

def count_subsets(mask):
    """
    计算子集的数量
    
    Args:
        mask: 位掩码
    
    Returns:
        int: 子集数量（包括空集）
    """
    return 1 << bin(mask).count('1')

def test_subset_enumeration():
    """
    测试子集枚举功能
    """
    # 测试1：枚举所有子集
    mask = 0b1011  # 表示集合{0,1,3}
    print(f"原集合(二进制): {bin(mask)}")
    print("所有非空子集:")
    subsets = enumerate_all_subsets(mask)
    for sub in subsets:
        print(f"  {bin(sub)} -> {mask_to_indices(sub)}")
    
    # 测试2：枚举大小为2的子集
    print("\n大小为2的子集:")
    size2_subsets = enumerate_subsets_of_size(4, 2)
    for sub in size2_subsets:
        print(f"  {bin(sub)} -> {mask_to_indices(sub)}")
    
    # 测试3：枚举包含特定元素的子集
    required = 0b101  # 必须包含元素0和2
    print("\n包含元素0和2的子集:")
    required_subsets = enumerate_subsets_with_required(0b1111, required)
    for sub in required_subsets:
        print(f"  {bin(sub)} -> {mask_to_indices(sub)}")

if __name__ == "__main__":
    test_subset_enumeration()
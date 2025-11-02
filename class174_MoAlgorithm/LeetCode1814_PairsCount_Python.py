#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
LeetCode 1814 数对统计问题的普通莫队算法实现

题目描述：
统计数组中满足 nums[i] + reverse(nums[j]) == nums[j] + reverse(nums[i]) 的数对 (i, j) 的个数，其中 0 <= i < j < n

解题思路：
1. 首先观察等式 nums[i] + reverse(nums[j]) == nums[j] + reverse(nums[i])
2. 可以变形为 nums[i] - reverse(nums[i]) == nums[j] - reverse(nums[j])
3. 令 a[i] = nums[i] - reverse(nums[i])，则问题转化为统计有多少对 (i, j) 满足 a[i] == a[j] 且 i < j
4. 这样我们可以将问题转化为区间查询问题，使用莫队算法来优化计算

时间复杂度分析：
- 莫队算法的时间复杂度为 O((n + q) * sqrt(n))，其中 n 是数组长度，q 是查询次数
- 在本题中，我们可以看作是一个离线查询，所以时间复杂度为 O(n * sqrt(n))

空间复杂度分析：
- 存储数组、a数组、查询结构等需要 O(n) 的空间
- 统计频率的字典需要 O(n) 的空间
- 总体空间复杂度为 O(n)

工程化考量：
1. 异常处理：处理数组为空的情况
2. 性能优化：预处理所有reverse值
3. 代码可读性：清晰的变量命名和详细的注释
4. 模块化设计：将主要功能拆分为多个函数
"""

import math
from collections import defaultdict


def reverse_number(num):
    """
    计算一个数的反转
    
    Args:
        num: 输入的整数
    
    Returns:
        反转后的整数
    """
    reversed_num = 0
    while num > 0:
        reversed_num = reversed_num * 10 + num % 10
        num //= 10
    return reversed_num


def compare_queries(q1, q2, block_size):
    """
    比较两个查询的顺序，用于莫队算法的排序
    
    Args:
        q1: 第一个查询 (l, r, idx)
        q2: 第二个查询 (l, r, idx)
        block_size: 块的大小
    
    Returns:
        比较结果，用于排序
    """
    # 首先按照左边界所在的块排序
    if q1[0] // block_size != q2[0] // block_size:
        return q1[0] // block_size - q2[0] // block_size
    # 对于同一块内的查询，按照右边界排序，偶数块升序，奇数块降序（奇偶排序优化）
    if (q1[0] // block_size) % 2 == 0:
        return q1[1] - q2[1]
    else:
        return q2[1] - q1[1]


def count_nice_pairs(nums):
    """
    主解题函数，统计满足条件的数对数量
    
    Args:
        nums: 输入数组
    
    Returns:
        满足条件的数对数量（模 10^9 + 7）
    """
    # 异常处理：空数组或单元素数组没有数对
    if not nums or len(nums) <= 1:
        return 0
    
    n = len(nums)
    MOD = 10**9 + 7
    
    # 预处理a数组，计算nums[i] - reverse(nums[i])
    a = [num - reverse_number(num) for num in nums]
    
    # 创建一个查询，查询整个数组
    queries = [(0, n-1, 0)]
    
    # 计算块的大小，一般取sqrt(n)左右
    block_size = int(math.sqrt(n)) + 1
    
    # 对查询进行排序
    queries.sort(key=lambda q: (q[0] // block_size, q[1] if (q[0] // block_size) % 2 == 0 else -q[1]))
    
    # 初始化变量
    frequency_map = defaultdict(int)
    current_result = 0
    results = [0] * 1
    
    # 初始化当前区间的左右指针
    cur_l, cur_r = 0, -1
    
    # 处理每个查询
    for q in queries:
        q_l, q_r, q_idx = q
        
        # 调整左右指针到目标位置
        # 扩展左边界
        while cur_l > q_l:
            cur_l -= 1
            val = a[cur_l]
            # 如果这个值之前已经出现过，那么新增的这个元素会与之前的所有相同值形成新的数对
            current_result += frequency_map[val]
            frequency_map[val] += 1
        
        # 扩展右边界
        while cur_r < q_r:
            cur_r += 1
            val = a[cur_r]
            current_result += frequency_map[val]
            frequency_map[val] += 1
        
        # 收缩左边界
        while cur_l < q_l:
            val = a[cur_l]
            frequency_map[val] -= 1
            # 移除的元素会减少的数对数量等于移除前该值的频率-1
            current_result -= frequency_map[val]
            cur_l += 1
        
        # 收缩右边界
        while cur_r > q_r:
            val = a[cur_r]
            frequency_map[val] -= 1
            current_result -= frequency_map[val]
            cur_r -= 1
        
        # 保存当前查询的结果
        results[q_idx] = current_result % MOD
    
    return results[0]


def main():
    """
    主函数，用于测试
    """
    # 测试用例1
    nums1 = [42, 11, 1, 97]
    print(f"Test Case 1: {count_nice_pairs(nums1)}")  # 预期输出: 2
    
    # 测试用例2
    nums2 = [13, 10, 35, 24, 76]
    print(f"Test Case 2: {count_nice_pairs(nums2)}")  # 预期输出: 4
    
    # 边界测试用例
    nums3 = [1]
    print(f"Test Case 3 (Single element): {count_nice_pairs(nums3)}")  # 预期输出: 0
    
    nums4 = []
    print(f"Test Case 4 (Empty array): {count_nice_pairs(nums4)}")  # 预期输出: 0


if __name__ == "__main__":
    main()
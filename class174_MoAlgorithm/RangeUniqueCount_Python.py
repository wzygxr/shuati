#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
区间不同数问题的常规莫队算法实现

题目描述：
给定一个数组，多次查询区间[l, r]中有多少个不同的数。

解题思路：
1. 使用莫队算法离线处理所有查询
2. 将数组分成大小为 sqrt(n) 的块
3. 按照块号对查询进行排序，同一块内按右端点排序
4. 维护当前区间的不同数计数

时间复杂度分析：
- 排序查询的时间复杂度为 O(m log m)
- 处理所有查询的时间复杂度为 O(n * sqrt(n))
- 总体时间复杂度为 O(n * sqrt(n) + m log m)

空间复杂度分析：
- 存储数组、查询、计数数组等需要 O(n + m) 的空间

工程化考量：
1. 异常处理：处理边界情况和无效查询
2. 性能优化：使用奇偶排序优化，合理选择块的大小
3. 代码可读性：清晰的变量命名和详细的注释
4. 模块化设计：将主要功能拆分为多个函数
"""

import math
from collections import defaultdict


def discretize(arr):
    """
    离散化函数
    
    Args:
        arr: 原始数组
    
    Returns:
        tuple: (离散化后的数组, 原始值到离散值的映射, 离散值到原始值的映射)
    """
    value_set = set(arr)
    value_list = sorted(value_set)
    
    value_to_id = {val: i for i, val in enumerate(value_list)}
    id_to_value = {i: val for i, val in enumerate(value_list)}
    
    discretized = [value_to_id[num] for num in arr]
    
    return discretized, value_to_id, id_to_value


def solve_range_unique_count(arr, queries_input):
    """
    主解题函数 - 使用字典进行计数
    
    Args:
        arr: 输入数组
        queries_input: 查询列表，每个查询包含[l, r]
    
    Returns:
        每个查询的结果（区间内不同数的数量）
    """
    # 异常处理
    if not arr or not queries_input:
        return []
    
    n = len(arr)
    m = len(queries_input)
    
    # 计算块的大小
    block_size = int(math.sqrt(n)) + 1
    
    # 创建查询对象
    queries = []
    for i, (l, r) in enumerate(queries_input):
        # 假设输入是1-based的，转换为0-based
        l0, r0 = l - 1, r - 1
        block = l0 // block_size
        queries.append((l0, r0, i, block))
    
    # 对查询进行排序
    # 奇偶排序优化：偶数块按r升序，奇数块按r降序
    queries.sort(key=lambda x: (x[3], x[1] if x[3] % 2 == 0 else -x[1]))
    
    # 初始化结果数组
    answers = [0] * m
    
    # 使用字典计数
    count_map = defaultdict(int)
    current_result = 0  # 当前区间内不同数的数量
    
    # 初始化当前区间的左右指针
    cur_l = 0
    cur_r = -1
    
    # 处理每个查询
    for l, r, idx, _ in queries:
        # 调整左右指针到目标位置
        # 向右扩展右端点
        while cur_r < r:
            cur_r += 1
            num = arr[cur_r]
            count_map[num] += 1
            if count_map[num] == 1:
                current_result += 1
        
        # 向左收缩右端点
        while cur_r > r:
            num = arr[cur_r]
            count_map[num] -= 1
            if count_map[num] == 0:
                current_result -= 1
            cur_r -= 1
        
        # 向左扩展左端点
        while cur_l > l:
            cur_l -= 1
            num = arr[cur_l]
            count_map[num] += 1
            if count_map[num] == 1:
                current_result += 1
        
        # 向右收缩左端点
        while cur_l < l:
            num = arr[cur_l]
            count_map[num] -= 1
            if count_map[num] == 0:
                current_result -= 1
            cur_l += 1
        
        # 保存当前查询的结果
        answers[idx] = current_result
    
    return answers

def solve_range_unique_count_optimized(arr, queries_input):
    """
    优化版本 - 使用离散化和数组计数提高性能
    
    Args:
        arr: 输入数组
        queries_input: 查询列表，每个查询包含[l, r]
    
    Returns:
        每个查询的结果（区间内不同数的数量）
    """
    # 异常处理
    if not arr or not queries_input:
        return []
    
    n = len(arr)
    m = len(queries_input)
    
    # 离散化处理
    discrete_arr, _, _ = discretize(arr)
    value_range = len(set(arr))
    
    # 计算块的大小
    block_size = int(math.sqrt(n)) + 1
    
    # 创建查询对象
    queries = []
    for i, (l, r) in enumerate(queries_input):
        # 假设输入是1-based的，转换为0-based
        l0, r0 = l - 1, r - 1
        block = l0 // block_size
        queries.append((l0, r0, i, block))
    
    # 对查询进行排序
    # 奇偶排序优化：偶数块按r升序，奇数块按r降序
    queries.sort(key=lambda x: (x[3], x[1] if x[3] % 2 == 0 else -x[1]))
    
    # 初始化结果数组
    answers = [0] * m
    
    # 使用数组计数
    count = [0] * value_range
    current_result = 0  # 当前区间内不同数的数量
    
    # 初始化当前区间的左右指针
    cur_l = 0
    cur_r = -1
    
    # 处理每个查询
    for l, r, idx, _ in queries:
        # 调整左右指针到目标位置
        # 向右扩展右端点
        while cur_r < r:
            cur_r += 1
            num_id = discrete_arr[cur_r]
            count[num_id] += 1
            if count[num_id] == 1:
                current_result += 1
        
        # 向左收缩右端点
        while cur_r > r:
            num_id = discrete_arr[cur_r]
            count[num_id] -= 1
            if count[num_id] == 0:
                current_result -= 1
            cur_r -= 1
        
        # 向左扩展左端点
        while cur_l > l:
            cur_l -= 1
            num_id = discrete_arr[cur_l]
            count[num_id] += 1
            if count[num_id] == 1:
                current_result += 1
        
        # 向右收缩左端点
        while cur_l < l:
            num_id = discrete_arr[cur_l]
            count[num_id] -= 1
            if count[num_id] == 0:
                current_result -= 1
            cur_l += 1
        
        # 保存当前查询的结果
        answers[idx] = current_result
    
    return answers

def main():
    """
    主函数，用于测试
    """
    # 测试用例
    arr = [1, 2, 1, 3, 4, 2, 5]
    queries = [
        (1, 5),  # 查询区间[1,5]中不同数的数量
        (2, 6),  # 查询区间[2,6]中不同数的数量
        (3, 7)   # 查询区间[3,7]中不同数的数量
    ]
    
    # 使用优化版本
    results = solve_range_unique_count_optimized(arr, queries)
    
    # 输出结果
    print("Query Results:")
    for result in results:
        print(result)
    
    # 验证两种方法结果一致
    results2 = solve_range_unique_count(arr, queries)
    print("Results match:", results == results2)


if __name__ == "__main__":
    main()
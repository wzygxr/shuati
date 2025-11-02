#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
牛客网 NC15278 区间第k大问题的回滚莫队算法实现

题目描述：
给定一个数组，多次查询区间内的第k大元素

解题思路：
1. 区间第k大问题可以使用回滚莫队算法解决
2. 回滚莫队主要用于解决难以撤销操作的问题
3. 对于区间第k大问题，我们可以使用值域分块来维护

时间复杂度分析：
- 回滚莫队的时间复杂度为 O(n * sqrt(n))，其中 n 是数组长度
- 值域分块的查询时间为 O(sqrt(maxValue))
- 总体时间复杂度为 O(n * sqrt(n) * sqrt(maxValue))

空间复杂度分析：
- 存储数组、查询结构等需要 O(n) 的空间
- 值域分块数组需要 O(maxValue) 的空间
- 总体空间复杂度为 O(n + maxValue)

工程化考量：
1. 异常处理：处理边界情况和无效查询
2. 性能优化：使用离散化来减小值域范围
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
        tuple: (离散化后的数组, 离散化后的值域范围, 原始值到离散值的映射, 离散值到原始值的映射)
    """
    value_set = set(arr)
    value_list = sorted(value_set)
    
    # 创建映射表
    value_to_id = {val: i + 1 for i, val in enumerate(value_list)}  # 从1开始编号
    id_to_value = {i + 1: val for i, val in enumerate(value_list)}
    
    # 离散化数组
    discretized = [value_to_id[num] for num in arr]
    
    return discretized, len(value_list), value_to_id, id_to_value


def solve_kth_largest(arr, queries_input):
    """
    主解题函数
    
    Args:
        arr: 输入数组
        queries_input: 查询列表，每个查询包含[l, r, k]
    
    Returns:
        每个查询的第k大元素列表
    """
    # 异常处理
    if not arr or not queries_input:
        return []
    
    n = len(arr)
    q = len(queries_input)
    
    # 离散化
    discretized, value_range, value_to_id, id_to_value = discretize(arr)
    
    # 计算块的大小
    block_size = int(math.sqrt(n)) + 1
    value_block_size = int(math.sqrt(value_range)) + 1
    
    # 创建查询对象
    queries = []
    for i, (l, r, k) in enumerate(queries_input):
        # 假设输入是1-based的，转换为0-based
        l0, r0 = l - 1, r - 1
        block = l0 // block_size
        queries.append((l0, r0, k, i, block))
    
    # 对查询进行排序
    # 左端点在同一块内的查询，按右端点降序排列；否则按左端点升序排列
    queries.sort(key=lambda x: (x[4], -x[1]))
    
    # 初始化答案数组
    answers = [0] * q
    
    # 初始化计数数组
    max_value = value_range + 2
    max_block = (value_range + value_block_size - 1) // value_block_size + 2
    
    # 处理每个块
    current_block = -1
    cur_r = -1
    
    for l, r, k, idx, block in queries:
        # 如果是新的块，重置当前右端点和计数
        if block != current_block:
            # 清空计数
            value_count = [0] * max_value
            block_count = [0] * max_block
            current_block = block
            cur_r = block * block_size + block_size - 1
            cur_r = min(cur_r, n - 1)
        
        # 处理右端点
        while cur_r < r:
            cur_r += 1
            val = discretized[cur_r]
            value_count[val] += 1
            block_count[val // value_block_size] += 1
        
        # 暂时保存当前状态，用于回滚
        temp_value_count = value_count.copy()
        temp_block_count = block_count.copy()
        
        # 扩展左端点
        temp_l = block * block_size
        while temp_l > l:
            temp_l -= 1
            val = discretized[temp_l]
            temp_value_count[val] += 1
            temp_block_count[val // value_block_size] += 1
        
        # 查询第k大的数
        sum_count = 0
        result = -1
        
        # 先按块查找
        for i in range(len(temp_block_count) - 1, -1, -1):
            if sum_count + temp_block_count[i] < k:
                sum_count += temp_block_count[i]
            else:
                # 在当前块中查找
                start = i * value_block_size
                end = min(start + value_block_size - 1, value_range)
                for j in range(end, start - 1, -1):
                    sum_count += temp_value_count[j]
                    if sum_count >= k:
                        result = id_to_value[j]
                        break
                break
        
        # 保存查询结果
        answers[idx] = result
    
    return answers


def main():
    """
    主函数，用于测试
    """
    # 测试用例
    arr = [1, 3, 2, 4, 5]
    queries = [
        [1, 5, 2],  # 查询区间[1,5]的第2大元素
        [2, 4, 1],  # 查询区间[2,4]的第1大元素
        [3, 5, 3]   # 查询区间[3,5]的第3大元素
    ]
    
    results = solve_kth_largest(arr, queries)
    
    # 输出结果
    print("Query Results:")
    for result in results:
        print(result)


if __name__ == "__main__":
    main()
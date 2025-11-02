#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
带修改的莫队算法实现

题目描述：
给定一个数组，支持两种操作：
1. 修改操作：将数组中某个位置的元素修改为新值
2. 查询操作：查询区间[l, r]中有多少个不同的数

解题思路：
1. 使用带修改的莫队算法离线处理所有查询和修改
2. 将数组分成大小为 n^(2/3) 的块（最优块大小）
3. 按照块号、右端点块号、时间戳进行排序
4. 维护当前区间的不同数计数和时间戳

时间复杂度分析：
- 排序查询的时间复杂度为 O(m log m)
- 处理所有查询的时间复杂度为 O(n^(5/3))
- 总体时间复杂度为 O(n^(5/3) + m log m)

空间复杂度分析：
- 存储数组、查询、修改、计数数组等需要 O(n + m) 的空间

工程化考量：
1. 异常处理：处理边界情况和无效查询
2. 性能优化：使用最优的块大小 n^(2/3)
3. 代码可读性：清晰的变量命名和详细的注释
4. 模块化设计：将主要功能拆分为多个函数
"""

import math
from collections import defaultdict


def discretize(arr, modifications):
    """
    离散化函数
    
    Args:
        arr: 原始数组
        modifications: 修改列表
    
    Returns:
        tuple: (离散化后的数组, 原始值到离散值的映射, 离散值到原始值的映射)
    """
    value_set = set(arr)
    for mod in modifications:
        _, old_val, new_val = mod
        value_set.add(old_val)
        value_set.add(new_val)
    
    value_list = sorted(value_set)
    value_to_id = {val: i for i, val in enumerate(value_list)}
    id_to_value = {i: val for i, val in enumerate(value_list)}
    
    discretized = [value_to_id[num] for num in arr]
    
    return discretized, value_to_id, id_to_value


def solve_mo_with_modifications(arr, queries_input, modifications_input):
    """
    主解题函数
    
    Args:
        arr: 初始数组
        queries_input: 查询列表，每个查询包含[l, r, t]
        modifications_input: 修改列表，每个修改包含[pos, newVal]
    
    Returns:
        每个查询的结果（区间内不同数的数量）
    """
    # 异常处理
    if not arr or not queries_input:
        return []
    
    n = len(arr)
    m = len(queries_input)
    k = len(modifications_input) if modifications_input else 0
    
    # 计算块的大小（最优为 n^(2/3)）
    block_size = int(n ** (2/3)) + 1
    
    # 创建原始数组的副本，用于记录修改
    original_arr = arr.copy()
    
    # 创建修改对象
    modifications = []
    for i in range(k):
        pos = modifications_input[i][0] - 1  # 转换为0-based
        new_val = modifications_input[i][1]
        old_val = original_arr[pos]
        modifications.append((pos, old_val, new_val))
        original_arr[pos] = new_val  # 更新原始数组用于下一次修改
    
    # 离散化处理
    discrete_arr, value_to_id, _ = discretize(arr, modifications)
    
    # 创建查询对象
    queries = []
    for i, (l, r, t) in enumerate(queries_input):
        # 假设输入是1-based的，转换为0-based
        l0, r0 = l - 1, r - 1
        block_l = l0 // block_size
        block_r = r0 // block_size
        queries.append((l0, r0, t, i, block_l, block_r))
    
    # 对查询进行排序
    # 按照块号、右端点块号、时间戳进行排序
    queries.sort(key=lambda x: (x[4], x[5], x[2]))
    
    # 初始化结果数组
    answers = [0] * m
    
    # 使用数组计数
    value_range = len(set(arr + [mod[1] for mod in modifications] + [mod[2] for mod in modifications]))
    count = [0] * value_range
    current_result = 0  # 当前区间内不同数的数量
    
    # 初始化当前区间的左右指针和时间戳
    cur_l = 0
    cur_r = -1
    cur_t = 0
    
    # 定义应用修改的函数
    def apply_modification(t):
        nonlocal current_result
        pos, old_val, new_val = modifications[t]
        old_id = value_to_id[old_val]
        new_id = value_to_id[new_val]
        
        # 如果修改的位置在当前区间内，需要更新计数
        if cur_l <= pos <= cur_r:
            count[old_id] -= 1
            if count[old_id] == 0:
                current_result -= 1
            
            count[new_id] += 1
            if count[new_id] == 1:
                current_result += 1
        
        # 更新离散化数组
        discrete_arr[pos] = new_id
    
    # 定义撤销修改的函数
    def undo_modification(t):
        nonlocal current_result
        pos, old_val, new_val = modifications[t]
        old_id = value_to_id[old_val]
        new_id = value_to_id[new_val]
        
        # 如果修改的位置在当前区间内，需要更新计数
        if cur_l <= pos <= cur_r:
            count[new_id] -= 1
            if count[new_id] == 0:
                current_result -= 1
            
            count[old_id] += 1
            if count[old_id] == 1:
                current_result += 1
        
        # 更新离散化数组
        discrete_arr[pos] = old_id
    
    # 处理每个查询
    for l, r, t, idx, _, _ in queries:
        # 调整时间戳到目标时间
        while cur_t < t:
            apply_modification(cur_t)
            cur_t += 1
        while cur_t > t:
            cur_t -= 1
            undo_modification(cur_t)
        
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


def solve_mo_with_modifications_optimized(arr, queries_input, modifications_input):
    """
    优化版本，使用字典进行计数，适用于数值范围较大的情况
    
    Args:
        arr: 初始数组
        queries_input: 查询列表，每个查询包含[l, r, t]
        modifications_input: 修改列表，每个修改包含[pos, newVal]
    
    Returns:
        每个查询的结果（区间内不同数的数量）
    """
    # 异常处理
    if not arr or not queries_input:
        return []
    
    n = len(arr)
    m = len(queries_input)
    k = len(modifications_input) if modifications_input else 0
    
    # 计算块的大小（最优为 n^(2/3)）
    block_size = int(n ** (2/3)) + 1
    
    # 创建原始数组的副本，用于记录修改
    original_arr = arr.copy()
    
    # 创建修改对象
    modifications = []
    for i in range(k):
        pos = modifications_input[i][0] - 1  # 转换为0-based
        new_val = modifications_input[i][1]
        old_val = original_arr[pos]
        modifications.append((pos, old_val, new_val))
        original_arr[pos] = new_val  # 更新原始数组用于下一次修改
    
    # 创建查询对象
    queries = []
    for i, (l, r, t) in enumerate(queries_input):
        # 假设输入是1-based的，转换为0-based
        l0, r0 = l - 1, r - 1
        block_l = l0 // block_size
        block_r = r0 // block_size
        queries.append((l0, r0, t, i, block_l, block_r))
    
    # 对查询进行排序
    # 按照块号、右端点块号、时间戳进行排序
    queries.sort(key=lambda x: (x[4], x[5], x[2]))
    
    # 初始化结果数组
    answers = [0] * m
    
    # 使用字典计数
    count_map = defaultdict(int)
    current_result = 0  # 当前区间内不同数的数量
    
    # 初始化当前区间的左右指针和时间戳
    cur_l = 0
    cur_r = -1
    cur_t = 0
    
    # 定义应用修改的函数
    def apply_modification(t):
        nonlocal current_result
        pos, old_val, new_val = modifications[t]
        
        # 如果修改的位置在当前区间内，需要更新计数
        if cur_l <= pos <= cur_r:
            count_map[old_val] -= 1
            if count_map[old_val] == 0:
                current_result -= 1
            
            count_map[new_val] += 1
            if count_map[new_val] == 1:
                current_result += 1
        
        # 更新数组
        original_arr[pos] = new_val
    
    # 定义撤销修改的函数
    def undo_modification(t):
        nonlocal current_result
        pos, old_val, new_val = modifications[t]
        
        # 如果修改的位置在当前区间内，需要更新计数
        if cur_l <= pos <= cur_r:
            count_map[new_val] -= 1
            if count_map[new_val] == 0:
                current_result -= 1
            
            count_map[old_val] += 1
            if count_map[old_val] == 1:
                current_result += 1
        
        # 更新数组
        original_arr[pos] = old_val
    
    # 处理每个查询
    for l, r, t, idx, _, _ in queries:
        # 调整时间戳到目标时间
        while cur_t < t:
            apply_modification(cur_t)
            cur_t += 1
        while cur_t > t:
            cur_t -= 1
            undo_modification(cur_t)
        
        # 调整左右指针到目标位置
        # 向右扩展右端点
        while cur_r < r:
            cur_r += 1
            num = original_arr[cur_r]
            count_map[num] += 1
            if count_map[num] == 1:
                current_result += 1
        
        # 向左收缩右端点
        while cur_r > r:
            num = original_arr[cur_r]
            count_map[num] -= 1
            if count_map[num] == 0:
                current_result -= 1
            cur_r -= 1
        
        # 向左扩展左端点
        while cur_l > l:
            cur_l -= 1
            num = original_arr[cur_l]
            count_map[num] += 1
            if count_map[num] == 1:
                current_result += 1
        
        # 向右收缩左端点
        while cur_l < l:
            num = original_arr[cur_l]
            count_map[num] -= 1
            if count_map[num] == 0:
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
    
    # 查询列表：每个查询为[l, r, t]，表示在第t次修改后查询区间[l, r]
    queries = [
        (1, 5, 0),  # 查询区间[1,5]在第0次修改后（即初始状态）
        (2, 6, 1),  # 查询区间[2,6]在第1次修改后
        (3, 7, 2)   # 查询区间[3,7]在第2次修改后
    ]
    
    # 修改列表：每个修改为[pos, newVal]，表示将位置pos的值修改为newVal
    modifications = [
        (2, 6),     # 将位置2的值修改为6
        (4, 7),     # 将位置4的值修改为7
        (6, 8)      # 将位置6的值修改为8
    ]
    
    # 使用优化版本
    results = solve_mo_with_modifications(arr, queries, modifications)
    
    # 输出结果
    print("Query Results:")
    for result in results:
        print(result)
    
    # 验证两种方法结果一致
    results2 = solve_mo_with_modifications_optimized(arr.copy(), queries, modifications.copy())
    print("Results match:", results == results2)


if __name__ == "__main__":
    main()
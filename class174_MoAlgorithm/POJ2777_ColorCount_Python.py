#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
POJ 2777 颜色出现次数统计问题的普通莫队算法实现

题目描述：
给定一个长度为L的数组，每个元素代表一种颜色（用1到T之间的整数表示）。
支持两种操作：
1. C A B：将位置A的颜色改为B
2. Q A B：查询区间[A,B]内有多少种不同的颜色

解题思路：
1. 这是一个支持单点修改和区间查询的问题，适合使用带修改莫队算法
2. 带修改莫队在普通莫队的基础上增加了时间维度，对查询进行三维排序
3. 维护当前区间内每种颜色的出现次数，以及当前不同颜色的数量

时间复杂度分析：
- 带修改莫队的时间复杂度为 O(n^(5/3))，其中 n 是数组长度
- 空间复杂度为 O(n)

工程化考量：
1. 异常处理：处理数组边界情况
2. 性能优化：预处理所有操作
3. 代码可读性：清晰的变量命名和详细的注释
4. 模块化设计：将主要功能拆分为多个函数
"""

import math
from collections import defaultdict


def solve_color_count(L, T, O, initial_colors, operations):
    """
    主解题函数
    
    Args:
        L: 数组长度
        T: 颜色种类数
        O: 操作数
        initial_colors: 初始颜色数组
        operations: 操作列表
    
    Returns:
        每个查询操作的结果列表
    """
    # 初始化数据结构
    colors = initial_colors.copy()  # 位置从1开始，索引0不使用
    original_colors = initial_colors.copy()
    modifies = []  # 存储所有修改操作
    queries = []  # 存储所有查询操作
    
    # 处理所有操作
    query_index = 0
    for op in operations:
        type_op = op[0]
        A = int(op[1])
        B = int(op[2])
        
        if type_op == 'C':
            # 修改操作
            modifies.append((A, colors[A], B))
            colors[A] = B  # 立即应用修改，便于后续操作使用最新状态
        elif type_op == 'Q':
            # 查询操作
            t = len(modifies) - 1 if modifies else -1
            queries.append((A, B, t, query_index))
            query_index += 1
    
    # 恢复原始颜色，因为我们需要重新应用修改
    colors = original_colors.copy()
    
    # 计算块的大小，对于带修改莫队，通常取n^(2/3)
    block_size = int(math.pow(L, 2.0 / 3.0)) + 1
    
    # 对查询进行排序的键函数
    def query_key(q):
        l, r, t, idx = q
        # 三维排序：块号 -> 右边界 -> 时间戳
        return (l // block_size, r // block_size, t)
    
    # 对查询进行排序
    queries.sort(key=query_key)
    
    # 初始化变量
    count = defaultdict(int)  # 用于存储每种颜色出现的次数
    current_result = 0
    answers = [0] * len(queries)
    
    # 初始化当前区间的左右指针和时间戳
    cur_l = 1
    cur_r = 0
    cur_t = -1
    
    # 添加元素到当前区间
    def add(pos):
        nonlocal current_result
        color = colors[pos]
        if count[color] == 0:
            current_result += 1
        count[color] += 1
    
    # 从当前区间移除元素
    def remove(pos):
        nonlocal current_result
        color = colors[pos]
        count[color] -= 1
        if count[color] == 0:
            current_result -= 1
    
    # 应用或回滚一个修改操作
    def apply_modify(modify_idx, apply):
        pos, old_color, new_color = modifies[modify_idx]
        
        # 确定要切换的颜色
        from_color = old_color if apply else new_color
        to_color = new_color if apply else old_color
        
        # 如果当前位置在查询区间内，需要更新统计
        if pos >= cur_l and pos <= cur_r:
            # 先移除旧颜色的影响
            remove(pos)
            # 更新颜色
            colors[pos] = to_color
            # 再添加新颜色的影响
            add(pos)
        else:
            # 如果当前位置不在查询区间内，直接更新颜色
            colors[pos] = to_color
    
    # 处理每个查询
    for q in queries:
        q_l, q_r, q_t, q_idx = q
        
        # 调整时间戳到目标时间
        while cur_t < q_t:
            cur_t += 1
            apply_modify(cur_t, True)
        while cur_t > q_t:
            apply_modify(cur_t, False)
            cur_t -= 1
        
        # 调整左右指针到目标位置
        # 扩展左边界
        while cur_l > q_l:
            cur_l -= 1
            add(cur_l)
        
        # 扩展右边界
        while cur_r < q_r:
            cur_r += 1
            add(cur_r)
        
        # 收缩左边界
        while cur_l < q_l:
            remove(cur_l)
            cur_l += 1
        
        # 收缩右边界
        while cur_r > q_r:
            remove(cur_r)
            cur_r -= 1
        
        # 保存当前查询的结果
        answers[q_idx] = current_result
    
    return answers


def main():
    """
    主函数，用于测试
    """
    # 测试用例
    L = 10
    T = 3
    O = 4
    # 位置从1开始，索引0不使用
    initial_colors = [0, 1, 2, 1, 3, 2, 1, 2, 3, 1, 2]
    
    operations = [
        ['Q', '1', '10'],
        ['C', '2', '3'],
        ['Q', '1', '10'],
        ['Q', '3', '6']
    ]
    
    results = solve_color_count(L, T, O, initial_colors, operations)
    
    # 输出结果
    print("Query Results:")
    for result in results:
        print(result)


if __name__ == "__main__":
    main()
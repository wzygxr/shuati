#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
树上莫队算法实现 - 树上路径查询问题

题目描述：
给定一棵树，每个节点有一个权值。多次查询两个节点之间的路径上有多少种不同的权值。

解题思路：
1. 树上莫队通过欧拉序或DFS序将树结构转换为线性结构
2. 使用时间戳标记每个节点的进入和离开时间
3. 将树上的路径查询转换为线性数组的区间查询
4. 应用莫队算法处理这些区间查询

时间复杂度分析：
- 树上莫队的时间复杂度为 O(n * sqrt(n))，其中 n 是树的节点数

空间复杂度分析：
- 存储树的邻接表、欧拉序等需要 O(n) 的空间
- 其他辅助数组需要 O(n) 的空间
- 总体空间复杂度为 O(n)

工程化考量：
1. 异常处理：处理树为空或查询无效的情况
2. 性能优化：合理选择块的大小，使用奇偶排序优化
3. 代码可读性：清晰的变量命名和详细的注释
"""

import math
from collections import defaultdict


def discretize(values):
    """
    离散化函数
    
    Args:
        values: 原始权值数组
    
    Returns:
        tuple: (离散化后的数组, 离散化后的值域范围, 原始值到离散值的映射, 离散值到原始值的映射)
    """
    value_set = set(values)
    value_list = sorted(value_set)
    
    value_to_id = {val: i + 1 for i, val in enumerate(value_list)}  # 从1开始编号
    id_to_value = {i + 1: val for i, val in enumerate(value_list)}
    
    discretized = [value_to_id[val] for val in values]
    
    return discretized, len(value_list), value_to_id, id_to_value


def dfs_lca(tree, n, start_node=1):
    """
    DFS预处理LCA所需的父节点和深度信息
    
    Args:
        tree: 树的邻接表
        n: 节点数
        start_node: 起始节点
    
    Returns:
        tuple: (in_time, out_time, parent, depth, euler)
    """
    time = 0
    in_time = [0] * (n + 1)  # 进入时间
    out_time = [0] * (n + 1) # 离开时间
    parent = [0] * (n + 1)   # 父节点
    depth = [0] * (n + 1)    # 深度
    euler = [0] * (2 * n + 2) # 欧拉序数组
    
    stack = [(start_node, False)]
    while stack:
        node, visited = stack.pop()
        if visited:
            out_time[node] = time
            continue
            
        in_time[node] = time
        euler[time] = node
        time += 1
        
        # 重新压入当前节点（标记为已访问）
        stack.append((node, True))
        
        # 压入子节点（逆序以保持顺序）
        for neighbor in reversed(tree[node]):
            if neighbor != parent[node]:
                parent[neighbor] = node
                depth[neighbor] = depth[node] + 1
                stack.append((neighbor, False))
    
    return in_time, out_time, parent, depth, euler


def preprocess_lca(n, parent):
    """
    预处理倍增表
    
    Args:
        n: 节点数
        parent: 父节点数组
    
    Returns:
        list: 倍增表
    """
    log_max = int(math.log2(n)) + 2
    up = [[0] * (n + 1) for _ in range(log_max)]
    
    # 初始化up[0]层
    for i in range(1, n + 1):
        up[0][i] = parent[i]
    
    # 填充倍增表
    for k in range(1, log_max):
        for i in range(1, n + 1):
            up[k][i] = up[k-1][up[k-1][i]]
    
    return up


def find_lca(u, v, depth, up):
    """
    查找两个节点的最近公共祖先
    
    Args:
        u: 节点u
        v: 节点v
        depth: 深度数组
        up: 倍增表
    
    Returns:
        int: 最近公共祖先
    """
    if depth[u] < depth[v]:
        u, v = v, u
    
    # 将u提升到与v同一深度
    log_max = len(up)
    for k in range(log_max - 1, -1, -1):
        if depth[u] - (1 << k) >= depth[v]:
            u = up[k][u]
    
    if u == v:
        return u
    
    # 同时提升u和v
    for k in range(log_max - 1, -1, -1):
        if up[k][u] != up[k][v]:
            u = up[k][u]
            v = up[k][v]
    
    return up[0][u]


def solve_tree_mo(n, m, val, edges, queries_input):
    """
    主解题函数
    
    Args:
        n: 节点数
        m: 查询数
        val: 节点权值数组
        edges: 边的列表
        queries_input: 查询列表，每个查询包含两个节点u和v
    
    Returns:
        list: 每个查询的结果
    """
    # 异常处理
    if n == 0 or m == 0:
        return []
    
    # 构建邻接表
    tree = [[] for _ in range(n + 1)]
    for u, v in edges:
        tree[u].append(v)
        tree[v].append(u)
    
    # 离散化
    values = [0] * (n + 1)  # 节点编号从1开始
    for i in range(1, n + 1):
        values[i] = val[i]
    
    # 离散化权值
    discrete_values, value_range, _, _ = discretize(values)
    
    # 预处理LCA相关信息
    in_time, out_time, parent, depth, euler = dfs_lca(tree, n)
    
    # 预处理倍增表
    up = preprocess_lca(n, parent)
    
    # 转换查询
    block_size = int(math.sqrt(n)) + 1
    queries = []
    for idx in range(m):
        u, v = queries_input[idx]
        
        # 确保u的进入时间小于v的进入时间
        if in_time[u] > in_time[v]:
            u, v = v, u
        
        ancestor = find_lca(u, v, depth, up)
        
        # 处理两种情况：u是v的祖先，或者不是
        if ancestor == u:
            l = in_time[u]
            r = in_time[v]
        else:
            l = out_time[u]
            r = in_time[v]
        
        queries.append((l, r, ancestor, idx, l // block_size))
    
    # 对查询进行排序
    # 奇偶排序优化：偶数块按r升序，奇数块按r降序
    queries.sort(key=lambda x: (x[4], x[1] if x[4] % 2 == 0 else -x[1]))
    
    # 初始化莫队算法相关变量
    count = [0] * (value_range + 2)
    current_result = 0
    in_current = [False] * (n + 1)
    answers = [0] * m
    
    # 初始化当前区间的左右指针
    cur_l = 1
    cur_r = 0
    
    # 处理每个查询
    for l, r, ancestor, idx, _ in queries:
        # 调整左右指针到目标位置
        while cur_l > l: 
            cur_l -= 1
            node = euler[cur_l]
            if in_current[node]:
                # 移除节点
                val_id = discrete_values[node]
                count[val_id] -= 1
                if count[val_id] == 0:
                    current_result -= 1
            else:
                # 添加节点
                val_id = discrete_values[node]
                if count[val_id] == 0:
                    current_result += 1
                count[val_id] += 1
            in_current[node] = not in_current[node]
        
        while cur_r < r: 
            cur_r += 1
            node = euler[cur_r]
            if in_current[node]:
                # 移除节点
                val_id = discrete_values[node]
                count[val_id] -= 1
                if count[val_id] == 0:
                    current_result -= 1
            else:
                # 添加节点
                val_id = discrete_values[node]
                if count[val_id] == 0:
                    current_result += 1
                count[val_id] += 1
            in_current[node] = not in_current[node]
        
        while cur_l < l: 
            node = euler[cur_l]
            if in_current[node]:
                # 移除节点
                val_id = discrete_values[node]
                count[val_id] -= 1
                if count[val_id] == 0:
                    current_result -= 1
            else:
                # 添加节点
                val_id = discrete_values[node]
                if count[val_id] == 0:
                    current_result += 1
                count[val_id] += 1
            in_current[node] = not in_current[node]
            cur_l += 1
        
        while cur_r > r: 
            node = euler[cur_r]
            if in_current[node]:
                # 移除节点
                val_id = discrete_values[node]
                count[val_id] -= 1
                if count[val_id] == 0:
                    current_result -= 1
            else:
                # 添加节点
                val_id = discrete_values[node]
                if count[val_id] == 0:
                    current_result += 1
                count[val_id] += 1
            in_current[node] = not in_current[node]
            cur_r -= 1
        
        # 处理LCA节点
        if ancestor != euler[l]:
            val_id = discrete_values[ancestor]
            if not in_current[ancestor]:
                if count[val_id] == 0:
                    current_result += 1
                count[val_id] += 1
                # 临时记录状态变化
                lca_added = True
            else:
                count[val_id] -= 1
                if count[val_id] == 0:
                    current_result -= 1
                lca_added = False
        else:
            lca_added = False
        
        # 保存当前查询的结果
        answers[idx] = current_result
        
        # 恢复LCA节点的状态
        if lca_added:
            val_id = discrete_values[ancestor]
            count[val_id] -= 1
            if count[val_id] == 0:
                current_result -= 1
        elif ancestor != euler[l] and in_current[ancestor]:
            val_id = discrete_values[ancestor]
            if count[val_id] == 0:
                current_result += 1
            count[val_id] += 1
    
    return answers


def main():
    """
    主函数，用于测试
    """
    # 测试用例
    n = 5
    m = 2
    val = [0, 1, 2, 1, 3, 2]  # 节点编号从1开始，索引0不使用
    edges = [
        (1, 2),
        (1, 3),
        (2, 4),
        (2, 5)
    ]
    queries = [
        (3, 4),  # 查询节点3和4之间路径上的不同权值数量
        (2, 5)   # 查询节点2和5之间路径上的不同权值数量
    ]
    
    results = solve_tree_mo(n, m, val, edges, queries)
    
    # 输出结果
    print("Query Results:")
    for result in results:
        print(result)


if __name__ == "__main__":
    main()
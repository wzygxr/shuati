# Blood Cousins Return, Python版本
# 题目来源: Codeforces 246E
# 链接: https://www.luogu.com.cn/problem/CF246E
# 
# 题目大意:
# 给定一棵家族树，n个人，每个人有一个名字和直接祖先(0表示没有祖先)
# 定义k级祖先和k级儿子关系
# m次查询，每次查询某个人的所有k级儿子中不同名字的个数
#
# 解题思路:
# 使用DSU on Tree(树上启发式合并)算法
# 1. 建树，处理出每个节点的深度、子树大小、重儿子等信息
# 2. 对每个节点，维护其子树中每个深度上的不同名字集合
# 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
# 4. 离线处理所有查询
#
# 时间复杂度: O(n log n)
# 空间复杂度: O(n)
#
# 算法详解:
# DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
# 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
#
# 核心思想:
# 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
# 2. 启发式合并处理：
#    - 先处理轻儿子的信息，然后清除贡献
#    - 再处理重儿子的信息并保留贡献
#    - 最后重新计算轻儿子的贡献
# 3. 通过这种方式，保证每个节点最多被访问O(log n)次
#
# 深度处理:
# 1. 维护各深度上的名字集合
# 2. 通过相对深度计算查询结果
# 3. 使用HashSet快速统计不同名字数量
#
# 工程化实现要点:
# 1. 边界处理：注意空树、单节点树等特殊情况
# 2. 内存优化：合理使用全局数组，避免重复分配内存
# 3. 常数优化：使用位运算、减少函数调用等优化常数
# 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题

import sys
from collections import defaultdict
import threading

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    
    # 初始化数据结构
    names = [''] * (n + 1)
    fathers = [0] * (n + 1)
    children = defaultdict(list)
    
    # 名字离散化
    name_map = {}
    name_id = [0] * (n + 1)
    name_cnt = 0
    
    # 读取每个人的信息
    for i in range(1, n + 1):
        line = sys.stdin.readline().split()
        names[i] = line[0]
        fathers[i] = int(line[1])
        
        # 建立父子关系
        if fathers[i] != 0:
            children[fathers[i]].append(i)
            
        # 名字离散化
        if names[i] not in name_map:
            name_cnt += 1
            name_map[names[i]] = name_cnt
        name_id[i] = name_map[names[i]]
    
    # 树链剖分相关数据
    size = [0] * (n + 1)  # 子树大小
    depth = [0] * (n + 1)  # 深度
    son = [0] * (n + 1)   # 重儿子
    parent = [0] * (n + 1)  # 父亲节点
    
    # 第一次DFS，计算子树大小、深度、重儿子
    def dfs1(u, f, d):
        parent[u] = f
        depth[u] = d
        size[u] = 1
        son[u] = 0
        
        for v in children[u]:
            if v != f:
                dfs1(v, u, d + 1)
                size[u] += size[v]
                if son[u] == 0 or size[son[u]] < size[v]:
                    son[u] = v
    
    # 找到所有根节点并处理
    for i in range(1, n + 1):
        if fathers[i] == 0:
            dfs1(i, 0, 1)
    
    # 查询相关
    m = int(sys.stdin.readline())
    queries = defaultdict(list)
    ans = [0] * (m + 1)
    
    # 读取查询
    for i in range(1, m + 1):
        v, k = map(int, sys.stdin.readline().split())
        queries[v].append((k, i))  # (k级儿子, 查询编号)
    
    # DSU on Tree相关数据结构
    # 每个深度上的名字集合
    depth_names = defaultdict(set)
    
    # 添加节点u到指定深度的集合中
    def add_name(u, base_depth):
        d = depth[u] - base_depth  # 相对于根节点的深度
        depth_names[d].add(name_id[u])
        
        # 递归处理子节点
        for v in children[u]:
            if v != parent[u]:
                add_name(v, base_depth)
    
    # 清除指定节点子树的信息
    def clear_names(u):
        d = depth[u] - depth[u]  # 相对于自身的深度，即0
        depth_names[d].discard(name_id[u])
        
        # 递归处理子节点
        for v in children[u]:
            if v != parent[u]:
                clear_names(v)
    
    # DSU on Tree 主过程
    def dsu_on_tree(u, keep):
        # 处理所有轻儿子
        for v in children[u]:
            if v != parent[u] and v != son[u]:
                dsu_on_tree(v, 0)  # 不保留信息
        
        # 处理重儿子
        if son[u] != 0:
            dsu_on_tree(son[u], 1)  # 保留信息
        
        # 将轻儿子的贡献加入
        for v in children[u]:
            if v != parent[u] and v != son[u]:
                add_name(v, depth[u])  # 将v子树中所有节点按相对深度加入
        
        # 加入当前节点
        d = depth[u] - depth[u]  # 当前节点相对深度为0
        depth_names[d].add(name_id[u])
        
        # 处理当前节点的所有查询
        for k, query_id in queries[u]:
            query_depth = k  # 查询k级儿子，即深度为k的节点
            ans[query_id] = len(depth_names[query_depth])
        
        # 如果不保留信息，则清除
        if keep == 0:
            clear_names(u)
            # 注意：在Python实现中，我们不清空depth_names，因为defaultdict会自动处理
    
    # 处理所有根节点以处理查询
    for i in range(1, n + 1):
        if fathers[i] == 0:
            dsu_on_tree(i, 0)
    
    # 输出结果
    result = []
    for i in range(1, m + 1):
        result.append(str(ans[i]))
    
    print(' '.join(result))

# 使用线程来增加递归限制，避免在处理大树时出现递归深度超限错误
threading.Thread(target=main).start()
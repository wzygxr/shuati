#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
牛客 NC15093 最大生成树
题目链接：https://ac.nowcoder.com/acm/problem/15093

题目描述：
给定一个无向图，要求找到一棵生成树，使得这棵生成树的边权之和最大。

解题思路：
使用Kruskal算法的变种，通过左偏树来维护并查集结构，实现按秩合并优化。
与传统的Kruskal算法类似，但选择边的顺序是从大到小，以获得最大生成树。

算法步骤：
1. 将所有边按权重从大到小排序
2. 初始化并查集结构（使用左偏树实现）
3. 遍历排序后的边，如果边的两个端点不在同一集合中，则将该边加入生成树
4. 重复步骤3直到生成树包含V-1条边

时间复杂度：O(E log V)，其中E是边数，V是顶点数
空间复杂度：O(V + E)

相关题目：
- Java实现：MaxSpanningTree_Java.java
- Python实现：MaxSpanningTree_Python.py
- C++实现：MaxSpanningTree_Cpp.cpp
"""

class Edge:
    """
    边类
    """
    def __init__(self, from_, to, weight):
        self.from_ = from_   # 起始顶点
        self.to = to         # 终止顶点
        self.weight = weight  # 权重

class LeftistTreeNode:
    """
    左偏树节点类（用于并查集的按秩合并）
    """
    def __init__(self, value):
        self.parent = value  # 父节点（用于并查集）
        self.size = 1        # 子树大小（用于按秩合并）
        self.value = value   # 节点值（这里存储顶点编号）
        self.dist = 0        # 距离（空路径长度）
        self.left = None
        self.right = None

def merge(a, b):
    """
    合并两个左偏树
    :param a: 第一棵左偏树的根节点
    :param b: 第二棵左偏树的根节点
    :return: 合并后的左偏树根节点
    """
    # 处理空树情况
    if a is None:
        return b
    if b is None:
        return a
    
    # 这里不关心具体的顺序，因为我们只是用左偏树来维护并查集
    a.right = merge(a.right, b)
    
    # 维护左偏性质：左子树的距离应大于等于右子树的距离
    if a.left is None or (a.right is not None and a.left.dist < a.right.dist):
        a.left, a.right = a.right, a.left
    
    # 更新距离：叶子节点距离为0，非叶子节点距离为其右子树距离+1
    a.dist = 0 if a.right is None else a.right.dist + 1
    return a

def find(nodes, x):
    """
    查找根节点（带路径压缩优化）
    :param nodes: 左偏树节点数组
    :param x: 顶点编号
    :return: 顶点x所在集合的根节点
    """
    # 路径压缩：将查找路径上的所有节点直接连接到根节点
    if nodes[x].parent != x:
        nodes[x].parent = find(nodes, nodes[x].parent)
    return nodes[x].parent

def union(nodes, x, y):
    """
    合并两个集合
    :param nodes: 左偏树节点数组
    :param x: 顶点编号
    :param y: 顶点编号
    """
    root_x = find(nodes, x)
    root_y = find(nodes, y)
    
    # 如果两个顶点已在同一集合中，无需合并
    if root_x == root_y:
        return
    
    # 按秩合并：将较小的树合并到较大的树上，以保持树的平衡
    if nodes[root_x].size < nodes[root_y].size:
        # 交换x和y，确保root_x是较大的树
        root_x, root_y = root_y, root_x
    
    # 将root_y的父节点设为root_x，完成合并
    nodes[root_y].parent = root_x
    # 更新根节点的大小
    nodes[root_x].size += nodes[root_y].size
    # 使用左偏树合并两个集合
    nodes[root_x] = merge(nodes[root_x], nodes[root_y])

def max_spanning_tree(V, edges):
    """
    计算最大生成树的边权和
    :param V: 顶点数
    :param edges: 边列表
    :return: 最大生成树的边权和
    """
    # 初始化左偏树节点数组，索引0不使用，顶点编号从1开始
    nodes = [None] * (V + 1)
    for i in range(1, V + 1):
        nodes[i] = LeftistTreeNode(i)
    
    # 按边权从大到小排序，以获得最大生成树
    edges.sort(key=lambda x: -x.weight)
    
    total_weight = 0  # 最大生成树的总权重
    edge_count = 0    # 已选择的边数
    
    # Kruskal算法：选择最大的边，避免环
    for edge in edges:
        # 如果边的两个端点不在同一集合中，则可以安全地添加这条边
        if find(nodes, edge.from_) != find(nodes, edge.to):
            union(nodes, edge.from_, edge.to)
            total_weight += edge.weight
            edge_count += 1
            
            # 生成树有V-1条边，达到这个数量就停止
            if edge_count == V - 1:
                break
    
    # 检查是否形成了生成树（所有顶点都在同一集合中）
    # 如果是森林（多个连通分量），则无法形成生成树
    # 根据题目描述，应该保证图是连通的
    return total_weight

def main():
    """
    主函数，读取输入并输出结果
    输入格式：
    第一行包含两个整数V和E，分别表示顶点数和边数
    接下来E行，每行包含三个整数from、to、weight，表示一条边
    输出格式：
    输出最大生成树的边权和
    """
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    V = int(input[ptr])
    ptr += 1
    E = int(input[ptr])
    ptr += 1
    
    edges = []
    for _ in range(E):
        from_ = int(input[ptr])
        ptr += 1
        to = int(input[ptr])
        ptr += 1
        weight = int(input[ptr])
        ptr += 1
        edges.append(Edge(from_, to, weight))
    
    result = max_spanning_tree(V, edges)
    print(result)

if __name__ == "__main__":
    main()
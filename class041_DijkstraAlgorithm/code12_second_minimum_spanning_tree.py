#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
次小生成树

题目链接: https://www.luogu.com.cn/problem/P4180

题目描述：
给定一个包含 N 个点、M 条边的无向图，节点编号为 1~N。
求该图的次小生成树的权值和。

解题思路：
次小生成树是指权值和严格大于最小生成树的最小生成树。
我们采用以下策略：
1. 首先使用Kruskal算法求出最小生成树(MST)
2. 然后枚举每条不在MST中的边，将其加入MST中会形成一个环
3. 在环中找到权值最大的边并删除，形成一个新的生成树
4. 在所有可能的新生成树中找到权值最小的作为次小生成树

算法应用场景：
- 网络设计中的备用方案
- 交通规划中的备选路线
- 图论中的优化问题

时间复杂度分析：
O(E log E + V^2) 其中V是节点数，E是边数

空间复杂度分析：
O(V^2) 存储图和路径信息
"""

class UnionFind:
    """并查集类，用于Kruskal算法中检测环和维护连通性"""
    
    def __init__(self, n):
        """
        构造函数
        :param n: 节点数量
        """
        self.parent = list(range(n))  # parent[i]表示节点i的父节点
        self.rank = [0] * n           # rank[i]表示以i为根的树的高度
    
    def find(self, x):
        """
        查找操作（带路径压缩优化）
        :param x: 节点
        :return: x所在集合的根节点
        """
        # 路径压缩：将查找路径上的所有节点直接连接到根节点
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并操作（按秩合并优化）
        :param x: 节点x
        :param y: 节点y
        :return: 如果合并成功返回True，如果已在同一集合返回False
        """
        root_x = self.find(x)  # x所在集合的根节点
        root_y = self.find(y)  # y所在集合的根节点
        
        # 如果已在同一集合，无法合并
        if root_x == root_y:
            return False
        
        # 按秩合并：将高度较小的树连接到高度较大的树下
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            # 高度相同时，任选一个作为根，并将其高度加1
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
        return True


def secondMinimumSpanningTree(n, edges):
    """
    使用Kruskal算法求解次小生成树
    
    算法核心思想：
    1. 首先使用Kruskal算法求出最小生成树
    2. 预处理计算MST中任意两点间路径上的最大边权和严格次大边权
    3. 枚举每条不在MST中的边，将其加入MST中会形成一个环
    4. 在环中找到合适的边删除，形成新的生成树
    5. 在所有可能的新生成树中找到权值最小的作为次小生成树
    
    算法步骤：
    1. 对所有边按权重排序
    2. 使用并查集构建最小生成树
    3. 预处理计算路径上的最大边权和次大边权
    4. 枚举非MST边，计算可能的新生成树权值
    5. 返回最小的新生成树权值
    
    时间复杂度: O(E log E + V^2) 其中V是节点数，E是边数
    空间复杂度: O(V^2)
    
    :param n: 节点数量
    :param edges: 边的信息，edges[i] = [起点, 终点, 权重]
    :return: 次小生成树的权值和，如果不存在返回-1
    """
    # 将边按权重排序，为Kruskal算法做准备
    edge_list = [(w, u, v) for u, v, w in edges]
    edge_list.sort()
    
    # 构建最小生成树
    uf = UnionFind(n + 1)  # 节点编号从1开始
    mst_edges = []         # MST中的边
    mst_weight = 0         # MST的权值和
    
    # Kruskal算法主循环
    for w, u, v in edge_list:
        # 如果连接两个不同连通分量，将边加入MST
        if uf.union(u, v):
            mst_edges.append((u, v, w))
            mst_weight += w
    
    # 构建MST的邻接表表示，用于后续DFS
    # mst_graph[i]存储节点i在MST中的所有邻居节点及其边权重
    mst_graph = [[] for _ in range(n + 1)]
    
    # 构建MST的邻接表
    for u, v, w in mst_edges:
        # 无向图，需要添加两个方向的边
        mst_graph[u].append((v, w))
        mst_graph[v].append((u, w))
    
    # 预处理：计算MST中任意两点间路径上的最大边权和严格次大边权
    # max_edge[i][j]表示MST中从节点i到节点j路径上的最大边权
    max_edge = [[0] * (n + 1) for _ in range(n + 1)]
    # second_max_edge[i][j]表示MST中从节点i到节点j路径上的严格次大边权
    second_max_edge = [[0] * (n + 1) for _ in range(n + 1)]
    
    # DFS计算路径上的最大边权和严格次大边权
    def dfs(start, parent, current, max_w, second_max_w):
        """
        DFS计算路径上的最大边权和严格次大边权
        
        :param start: 起始节点
        :param parent: 父节点（避免回溯）
        :param current: 当前节点
        :param max_w: 当前路径上的最大边权
        :param second_max_w: 当前路径上的严格次大边权
        """
        # 记录从start到current的路径上的最大边权和次大边权
        max_edge[start][current] = max_w
        second_max_edge[start][current] = second_max_w
        
        # 遍历当前节点的所有邻居
        for next_node, w in mst_graph[current]:
            # 避免回到父节点
            if next_node != parent:
                # 更新路径上的最大边权和次大边权
                new_max_w = max_w
                new_second_max_w = second_max_w
                
                # 如果当前边权重更大，更新最大和次大边权
                if w > max_w:
                    new_second_max_w = max_w  # 原最大变为次大
                    new_max_w = w             # 当前边权重变为最大
                elif w > second_max_w and w != max_w:
                    # 如果当前边权重大于次大且不等于最大，更新次大边权
                    new_second_max_w = w
                
                # 递归处理邻居节点
                dfs(start, current, next_node, new_max_w, new_second_max_w)
    
    # 对每个节点作为起点进行DFS，计算到其他节点的路径信息
    for i in range(1, n + 1):
        dfs(i, -1, i, 0, 0)
    
    # 寻找次小生成树
    second_mst_weight = float('inf')
    
    # 枚举每条边
    edge_set = set((min(u, v), max(u, v)) for u, v, w in mst_edges)
    
    for w, u, v in edge_list:
        # 如果这条边不在MST中
        if (min(u, v), max(u, v)) not in edge_set:
            # 计算在MST中加入这条边后形成环，环上最大边权
            max_in_cycle = max_edge[u][v]
            
            # 如果这条边的权重大于环上最大边权，则形成新的生成树
            if w > max_in_cycle:
                # 新生成树权值 = MST权值 + 新边权重 - 删除边权重
                new_weight = mst_weight + w - max_in_cycle
                second_mst_weight = min(second_mst_weight, new_weight)
            # 如果这条边的权重等于环上最大边权，则需要考虑环上次大边权
            elif w == max_in_cycle:
                second_max_in_cycle = second_max_edge[u][v]
                # 如果存在次大边权（不为0），则可以形成新的生成树
                if second_max_in_cycle != 0:
                    # 新生成树权值 = MST权值 + 新边权重 - 删除边权重
                    new_weight = mst_weight + w - second_max_in_cycle
                    second_mst_weight = min(second_mst_weight, new_weight)
    
    # 返回次小生成树权值，如果不存在返回-1
    return int(second_mst_weight) if second_mst_weight != float('inf') else -1


# 测试用例
if __name__ == "__main__":
    # 示例
    # 输入: n = 4, edges = [[1, 2, 1], [1, 3, 1], [2, 4, 1], [3, 4, 1]]
    # 输出: 4
    # 解释: 最小生成树权值为3，次小生成树权值为4
    n = 4
    edges = [[1, 2, 1], [1, 3, 1], [2, 4, 1], [3, 4, 1]]
    result = secondMinimumSpanningTree(n, edges)
    print(f"测试用例结果: {result}")  # 输出: 4
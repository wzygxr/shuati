#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1584. 连接所有点的最小费用 (Min Cost to Connect All Points)

题目来源：https://leetcode.cn/problems/min-cost-to-connect-all-points/

题目描述：
给你一个points数组，表示 2D 平面上的一些点，其中 points[i] = [xi, yi] 。
连接点 [xi, yi] 和点 [xj, yj] 的费用为它们之间的曼哈顿距离：|xi - xj| + |yi - yj|，
其中 |val| 表示 val 的绝对值。请你返回将所有点连接的最小总费用。
只有任意两点之间有且仅有一条简单路径时，才认为所有点都已连接。

算法思路：
这是一个最小生成树（Minimum Spanning Tree, MST）问题，可以使用以下算法解决：
1. Kruskal算法：使用并查集和贪心策略
2. Prim算法：使用优先队列和贪心策略
3. 最近点对算法的变种：通过构建完全图然后应用MST算法

时间复杂度：
- Kruskal算法：O(E log E) = O(n² log n)，其中E是边数，n是点数
- Prim算法：O(E log V) = O(n² log n)，其中V是顶点数
- 空间复杂度：O(n²)

应用场景：
1. 网络设计：最小化网络连接成本
2. 电路设计：最小化电路板布线长度
3. 交通运输：最小化道路建设成本

相关题目：
1. LeetCode 1135. 最低成本联通所有城市
2. LeetCode 743. 网络延迟时间
3. LeetCode 612. 平面上的最短距离
"""

import heapq
from typing import List

class UnionFind:
    """并查集类，用于Kruskal算法中检测环"""
    
    def __init__(self, n):
        self.parent = list(range(n))
        self.rank = [0] * n
        self.components = n
    
    def find(self, x):
        """
        查找元素的根节点（带路径压缩优化）
        :param x: 元素
        :return: 根节点
        """
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])  # 路径压缩
        return self.parent[x]
    
    def union(self, x, y):
        """
        合并两个集合（按秩合并优化）
        :param x: 第一个元素
        :param y: 第二个元素
        :return: 如果合并成功返回True，如果已在同一集合返回False
        """
        root_x = self.find(x)
        root_y = self.find(y)
        
        if root_x == root_y:
            return False  # 已在同一集合，不能合并
        
        # 按秩合并
        if self.rank[root_x] < self.rank[root_y]:
            self.parent[root_x] = root_y
        elif self.rank[root_x] > self.rank[root_y]:
            self.parent[root_y] = root_x
        else:
            self.parent[root_y] = root_x
            self.rank[root_x] += 1
        
        self.components -= 1
        return True
    
    def get_components(self):
        """
        获取连通分量数量
        :return: 连通分量数量
        """
        return self.components


class Solution:
    """解决方案类"""
    
    @staticmethod
    def min_cost_connect_points_kruskal(points: List[List[int]]) -> int:
        """
        方法1：Kruskal算法解决最小生成树问题
        时间复杂度：O(n² log n)
        空间复杂度：O(n²)
        :param points: 点坐标数组
        :return: 连接所有点的最小费用
        """
        n = len(points)
        if n <= 1:
            return 0
        
        # 创建所有边
        edges = []  # [(cost, from, to), ...]
        for i in range(n):
            for j in range(i + 1, n):
                cost = abs(points[i][0] - points[j][0]) + abs(points[i][1] - points[j][1])
                edges.append((cost, i, j))
        
        # 按费用排序边
        edges.sort()
        
        # 使用并查集构建最小生成树
        uf = UnionFind(n)
        total_cost = 0
        
        for cost, from_point, to_point in edges:
            if uf.union(from_point, to_point):
                total_cost += cost
                # 如果所有点都已连接，提前结束
                if uf.get_components() == 1:
                    break
        
        return total_cost
    
    @staticmethod
    def min_cost_connect_points_prim(points: List[List[int]]) -> int:
        """
        方法2：Prim算法解决最小生成树问题
        时间复杂度：O(n²)
        空间复杂度：O(n)
        :param points: 点坐标数组
        :return: 连接所有点的最小费用
        """
        n = len(points)
        if n <= 1:
            return 0
        
        # 使用优先队列存储边（费用，目标点）
        # Python的heapq是最小堆
        pq = [(0, 0)]  # (cost, point_index)
        
        # 记录已访问的点
        visited = [False] * n
        total_cost = 0
        edges_used = 0
        
        while pq and edges_used < n:
            cost, point_index = heapq.heappop(pq)
            
            # 如果点已访问，跳过
            if visited[point_index]:
                continue
            
            # 标记点为已访问
            visited[point_index] = True
            total_cost += cost
            edges_used += 1
            
            # 添加与当前点相连的所有边到优先队列
            for i in range(n):
                if not visited[i]:
                    edge_cost = abs(points[point_index][0] - points[i][0]) + \
                               abs(points[point_index][1] - points[i][1])
                    heapq.heappush(pq, (edge_cost, i))
        
        return total_cost
    
    @staticmethod
    def min_cost_connect_points_optimized_prim(points: List[List[int]]) -> int:
        """
        方法3：优化的Prim算法（使用距离数组）
        时间复杂度：O(n²)
        空间复杂度：O(n)
        :param points: 点坐标数组
        :return: 连接所有点的最小费用
        """
        n = len(points)
        if n <= 1:
            return 0
        
        # 距离数组，记录每个点到已构建MST的最小距离
        min_dist = [float('inf')] * n
        
        # 记录已访问的点
        visited = [False] * n
        total_cost = 0
        
        # 从第一个点开始
        min_dist[0] = 0
        
        for _ in range(n):
            # 找到距离最小的未访问点
            current_point = -1
            for j in range(n):
                if not visited[j] and (current_point == -1 or min_dist[j] < min_dist[current_point]):
                    current_point = j
            
            # 标记点为已访问并累加费用
            visited[current_point] = True
            total_cost += min_dist[current_point]
            
            # 更新其他点到已构建MST的最小距离
            for j in range(n):
                if not visited[j]:
                    cost = abs(points[current_point][0] - points[j][0]) + \
                          abs(points[current_point][1] - points[j][1])
                    min_dist[j] = min(min_dist[j], cost)
        
        return int(total_cost)


def test_min_cost_connect_points():
    """测试函数"""
    print("=== 测试 LeetCode 1584. 连接所有点的最小费用 ===")
    
    # 测试用例1
    points1 = [[0,0],[2,2],[3,10],[5,2],[7,0]]
    print("测试用例1:")
    print("点集:", points1)
    print("Kruskal算法结果:", Solution.min_cost_connect_points_kruskal(points1))
    print("Prim算法结果:", Solution.min_cost_connect_points_prim(points1))
    print("优化Prim算法结果:", Solution.min_cost_connect_points_optimized_prim(points1))
    print("期望结果: 20")
    print()
    
    # 测试用例2
    points2 = [[3,12],[-2,5],[-4,1]]
    print("测试用例2:")
    print("点集:", points2)
    print("Kruskal算法结果:", Solution.min_cost_connect_points_kruskal(points2))
    print("Prim算法结果:", Solution.min_cost_connect_points_prim(points2))
    print("优化Prim算法结果:", Solution.min_cost_connect_points_optimized_prim(points2))
    print("期望结果: 18")
    print()
    
    # 测试用例3：边界情况
    points3 = [[0,0]]
    print("测试用例3 (单点):")
    print("点集:", points3)
    print("Kruskal算法结果:", Solution.min_cost_connect_points_kruskal(points3))
    print("Prim算法结果:", Solution.min_cost_connect_points_prim(points3))
    print("优化Prim算法结果:", Solution.min_cost_connect_points_optimized_prim(points3))
    print("期望结果: 0")
    print()


if __name__ == "__main__":
    test_min_cost_connect_points()
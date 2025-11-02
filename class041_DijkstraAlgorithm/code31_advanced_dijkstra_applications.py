#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Dijkstra算法高级应用与扩展（Python实现）

本类包含Dijkstra算法在各种复杂场景下的高级应用：
1. 多目标优化最短路径
2. 实时动态最短路径
3. 分布式最短路径计算
4. 增量式最短路径更新

算法应用场景：
- 智能交通系统中的实时路线规划
- 分布式网络中的路由计算
- 大规模图数据库的最短路径查询
- 动态变化图中的增量更新
"""

import heapq
from collections import defaultdict
import threading
from concurrent.futures import ThreadPoolExecutor
import sys

class MultiObjectiveShortestPath:
    """
    多目标优化最短路径问题
    
    问题描述：同时优化多个目标（如时间、成本、舒适度等）
    解法思路：使用帕累托最优解集，维护多个目标函数
    
    时间复杂度：O((V+E) * P * log(V*P))，其中P是帕累托解的数量
    空间复杂度：O(V * P)
    """
    
    @staticmethod
    def multi_objective_dijkstra(n, edges, src, dst):
        """
        多目标最短路径求解
        
        Args:
            n: 节点数
            edges: 边列表，每条边包含多个权重
            src: 源点
            dst: 目标点
        
        Returns:
            帕累托最优解集
        """
        # 构建邻接表，每条边有多个权重
        graph = defaultdict(list)
        for edge in edges:
            u, v = edge[0], edge[1]
            weights = edge[2:]  # 多个权重值
            graph[u].append((v, weights))
            graph[v].append((u, weights))  # 无向图
        
        # 每个节点的帕累托最优解集
        pareto_front = defaultdict(set)
        
        # 初始状态：源点的解为全0
        zero_cost = tuple([0] * (len(edges[0]) - 2))  # 假设有多个目标
        pareto_front[src].add(zero_cost)
        
        # 优先队列，按第一个目标函数排序
        heap = [(0, src, zero_cost)]  # (第一个成本, 节点, 成本元组)
        
        while heap:
            first_cost, u, costs_tuple = heapq.heappop(heap)
            costs = list(costs_tuple)
            
            # 检查当前解是否仍然在帕累托前沿
            if not MultiObjectiveShortestPath.is_pareto_optimal(pareto_front[u], costs_tuple):
                continue
            
            if u == dst:
                # 找到目标点，继续寻找其他帕累托解
                continue
            
            # 扩展邻居节点
            for v, weights in graph[u]:
                # 计算新成本
                new_costs = [c + w for c, w in zip(costs, weights)]
                new_costs_tuple = tuple(new_costs)
                
                # 检查新解是否可以被接受
                if MultiObjectiveShortestPath.is_pareto_optimal(pareto_front[v], new_costs_tuple):
                    # 更新帕累托前沿
                    MultiObjectiveShortestPath.update_pareto_front(pareto_front[v], new_costs_tuple)
                    heapq.heappush(heap, (new_costs[0], v, new_costs))
        
        return list(pareto_front[dst])
    
    @staticmethod
    def is_pareto_optimal(front, costs_tuple):
        """检查解是否帕累托最优"""
        for solution in front:
            if all(s <= c for s, c in zip(solution, costs_tuple)):
                # 被支配，不是帕累托最优
                return False
        return True
    
    @staticmethod
    def update_pareto_front(front, new_costs):
        """更新帕累托前沿"""
        # 移除被新解支配的旧解
        to_remove = []
        for solution in front:
            if all(s >= c for s, c in zip(solution, new_costs)):
                to_remove.append(solution)
        
        for solution in to_remove:
            front.remove(solution)
        
        front.add(new_costs)

class DynamicDijkstra:
    """
    实时动态最短路径算法
    
    问题描述：图中边权重随时间动态变化，需要实时更新最短路径
    解法思路：增量式更新，只重新计算受影响的部分
    
    时间复杂度：平均O(k * logV)，k是受影响节点数
    空间复杂度：O(V+E)
    """
    
    def __init__(self, n, edges):
        self.n = n
        self.graph = defaultdict(list)
        self.dist = [float('inf')] * n
        self.visited = [False] * n
        
        # 初始建图
        for u, v, w in edges:
            self.add_edge(u, v, w)
        
        # 初始计算最短路径
        self.compute_full_dijkstra(0)  # 假设源点为0
    
    def add_edge(self, u, v, w):
        """添加边"""
        self.graph[u].append((v, w))
        self.graph[v].append((u, w))  # 无向图
    
    def remove_edge(self, u, v):
        """移除边"""
        self.graph[u] = [edge for edge in self.graph[u] if edge[0] != v]
        self.graph[v] = [edge for edge in self.graph[v] if edge[0] != u]
    
    def compute_full_dijkstra(self, source):
        """全量Dijkstra计算"""
        self.dist = [float('inf')] * self.n
        self.dist[source] = 0
        
        heap = [(0, source)]
        
        while heap:
            d, u = heapq.heappop(heap)
            
            if d > self.dist[u]:
                continue
            
            for v, w in self.graph[u]:
                new_dist = d + w
                if new_dist < self.dist[v]:
                    self.dist[v] = new_dist
                    heapq.heappush(heap, (new_dist, v))
    
    def update_edge(self, u, v, new_weight):
        """更新边权重"""
        # 首先移除旧边（如果存在）
        self.remove_edge(u, v)
        # 添加新边
        self.add_edge(u, v, new_weight)
        
        # 增量式更新最短路径
        self.incremental_update(u, v, new_weight)
    
    def incremental_update(self, u, v, new_weight):
        """增量式更新最短路径"""
        heap = []
        
        # 将受影响节点加入队列
        if self.dist[u] != float('inf'):
            heapq.heappush(heap, (self.dist[u], u))
        if self.dist[v] != float('inf'):
            heapq.heappush(heap, (self.dist[v], v))
        
        # 局部Dijkstra更新
        visited = set()
        
        while heap:
            d, node = heapq.heappop(heap)
            
            if node in visited:
                continue
            visited.add(node)
            
            if d > self.dist[node]:
                continue
            
            self.dist[node] = d
            
            for neighbor, w in self.graph[node]:
                new_dist = d + w
                if new_dist < self.dist[neighbor]:
                    self.dist[neighbor] = new_dist
                    heapq.heappush(heap, (new_dist, neighbor))
    
    def get_shortest_distance(self, target):
        """获取最短距离"""
        return -1 if self.dist[target] == float('inf') else self.dist[target]

class IncrementalDijkstra:
    """
    增量式最短路径更新算法
    
    问题描述：当图中只有少量边权重发生变化时，高效更新最短路径
    解法思路：利用原有最短路径信息，只更新受影响的部分
    
    时间复杂度：O(k * logV)，k是受影响节点数
    """
    
    def __init__(self, n, edges):
        self.n = n
        self.graph = defaultdict(list)
        self.dist = [float('inf')] * n
        self.parent = [-1] * n
        
        # 初始建图
        for u, v, w in edges:
            self.add_edge(u, v, w)
        
        # 初始计算最短路径
        self.compute_full_dijkstra(0)  # 假设源点为0
    
    def add_edge(self, u, v, w):
        """添加边"""
        self.graph[u].append((v, w))
        self.graph[v].append((u, w))
    
    def compute_full_dijkstra(self, source):
        """全量Dijkstra计算"""
        self.dist = [float('inf')] * self.n
        self.dist[source] = 0
        
        heap = [(0, source)]
        
        while heap:
            d, u = heapq.heappop(heap)
            
            if d > self.dist[u]:
                continue
            
            for v, w in self.graph[u]:
                new_dist = d + w
                if new_dist < self.dist[v]:
                    self.dist[v] = new_dist
                    self.parent[v] = u
                    heapq.heappush(heap, (new_dist, v))
    
    def handle_weight_increase(self, u, v, old_weight, new_weight):
        """处理权重增加"""
        if new_weight <= old_weight:
            return  # 权重减少或不变，不需要特殊处理
        
        # 检查这条边是否在最短路径树中
        if self.parent[v] == u or self.parent[u] == v:
            # 边在最短路径树中，需要重新计算受影响部分
            self.recompute_affected_nodes(u, v)
    
    def handle_weight_decrease(self, u, v, old_weight, new_weight):
        """处理权重减少"""
        if new_weight >= old_weight:
            return  # 权重增加或不变，不需要特殊处理
        
        # 权重减少，可能产生更短路径
        heap = []
        
        # 从u和v开始更新
        if self.dist[u] != float('inf'):
            heapq.heappush(heap, (self.dist[u], u))
        if self.dist[v] != float('inf'):
            heapq.heappush(heap, (self.dist[v], v))
        
        # 局部Dijkstra更新
        while heap:
            d, node = heapq.heappop(heap)
            
            if d > self.dist[node]:
                continue
            
            for neighbor, w in self.graph[node]:
                new_dist = d + w
                if new_dist < self.dist[neighbor]:
                    self.dist[neighbor] = new_dist
                    self.parent[neighbor] = node
                    heapq.heappush(heap, (new_dist, neighbor))
    
    def recompute_affected_nodes(self, u, v):
        """重新计算受影响节点"""
        # 标记受影响节点（在u或v的子树中的节点）
        affected = [False] * self.n
        self.mark_affected(u, affected)
        self.mark_affected(v, affected)
        
        # 重新计算受影响节点的最短路径
        heap = []
        
        # 将所有受影响节点加入队列
        for i in range(self.n):
            if affected[i] and self.dist[i] != float('inf'):
                heapq.heappush(heap, (self.dist[i], i))
        
        # 局部Dijkstra更新
        while heap:
            d, node = heapq.heappop(heap)
            
            if d > self.dist[node]:
                continue
            
            for neighbor, w in self.graph[node]:
                new_dist = d + w
                if new_dist < self.dist[neighbor]:
                    self.dist[neighbor] = new_dist
                    self.parent[neighbor] = node
                    heapq.heappush(heap, (new_dist, neighbor))
    
    def mark_affected(self, node, affected):
        """标记受影响节点"""
        if affected[node]:
            return
        affected[node] = True
        
        for neighbor, _ in self.graph[node]:
            if self.parent[neighbor] == node:
                self.mark_affected(neighbor, affected)
    
    def get_distance(self, target):
        """获取距离"""
        return self.dist[target]

def test_multi_objective():
    """测试多目标优化"""
    print("=== 多目标优化最短路径测试 ===")
    
    n = 4
    edges = [
        (0, 1, 2, 3),  # u, v, 成本1, 成本2
        (0, 2, 1, 4),
        (1, 3, 3, 1),
        (2, 3, 2, 2)
    ]
    
    solutions = MultiObjectiveShortestPath.multi_objective_dijkstra(n, edges, 0, 3)
    print(f"帕累托最优解数量: {len(solutions)}")
    for sol in solutions:
        print(f"成本1: {sol[0]}, 成本2: {sol[1]}")

def test_dynamic_dijkstra():
    """测试动态最短路径"""
    print("\n=== 动态最短路径测试 ===")
    
    n = 4
    edges = [
        (0, 1, 2),
        (0, 2, 4),
        (1, 3, 3),
        (2, 3, 1)
    ]
    
    dd = DynamicDijkstra(n, edges)
    print(f"初始最短距离: {dd.get_shortest_distance(3)}")
    
    # 更新边权重
    dd.update_edge(2, 3, 5)
    print(f"更新后最短距离: {dd.get_shortest_distance(3)}")

def test_incremental_update():
    """测试增量式更新"""
    print("\n=== 增量式更新测试 ===")
    
    n = 4
    edges = [
        (0, 1, 2),
        (0, 2, 4),
        (1, 3, 3),
        (2, 3, 1)
    ]
    
    id = IncrementalDijkstra(n, edges)
    print(f"初始距离: {id.get_distance(3)}")
    
    # 处理权重减少
    id.handle_weight_decrease(2, 3, 1, 0)
    print(f"权重减少后距离: {id.get_distance(3)}")

if __name__ == "__main__":
    test_multi_objective()
    test_dynamic_dijkstra()
    test_incremental_update()
#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
带约束条件的最短路径问题（Python实现）

题目：K站中转内最便宜的航班（LeetCode 787）
链接：https://leetcode.cn/problems/cheapest-flights-within-k-stops/

题目描述：
有 n 个城市通过一些航班连接。给你一个数组 flights，
其中 flights[i] = [fromi, toi, pricei] ，表示该航班从城市 fromi 到城市 toi，价格为 pricei。
请你找到出一条最多经过 k 站中转的路线，使得从城市 src 到城市 dst 的价格最便宜，并返回该价格。
如果不存在这样的路线，则返回 -1。

解题思路：
1. 方法1：动态规划 + Dijkstra算法
2. 方法2：Bellman-Ford算法变种
3. 方法3：BFS + 剪枝

算法应用场景：
- 航班路线规划（中转次数限制）
- 网络路由（跳数限制）
- 物流配送（中转站限制）

时间复杂度分析：
- 方法1：O(K * E * log(V))
- 方法2：O(K * E)
- 方法3：O(V^K) 最坏情况，但实际剪枝后效率较高
"""

import heapq
from collections import defaultdict, deque
import sys

def find_cheapest_price1(n, flights, src, dst, k):
    """
    方法1：动态规划 + Dijkstra算法（最优解法）
    
    算法步骤：
    1. 使用动态规划思想，dp[k][v]表示经过k次中转到达城市v的最小成本
    2. 使用优先队列进行状态扩展，每个状态包含(当前城市, 已用中转次数, 当前成本)
    3. 当到达目标城市且中转次数不超过K时，更新最小成本
    
    时间复杂度：O(K * E * log(V))
    空间复杂度：O(V * K)
    """
    # 构建邻接表
    graph = defaultdict(list)
    for from_city, to_city, price in flights:
        graph[from_city].append((to_city, price))
    
    # dp数组：dp[stops][city]表示经过stops次中转到达city的最小成本
    dp = [[float('inf')] * n for _ in range(k + 2)]
    dp[0][src] = 0
    
    # 优先队列：存储(中转次数, 当前城市, 当前成本)
    heap = [(0, src, 0)]  # (stops, city, cost)
    
    while heap:
        stops, city, cost = heapq.heappop(heap)
        
        # 如果到达目标城市，返回结果
        if city == dst:
            return cost
        
        # 如果中转次数已用完，跳过
        if stops > k:
            continue
        
        # 遍历所有邻居城市
        for next_city, price in graph[city]:
            new_cost = cost + price
            new_stops = stops + (1 if next_city != dst else 0)  # 目标城市不算中转
            
            if new_stops <= k + 1 and new_cost < dp[new_stops][next_city]:
                dp[new_stops][next_city] = new_cost
                heapq.heappush(heap, (new_stops, next_city, new_cost))
    
    return -1

def find_cheapest_price2(n, flights, src, dst, k):
    """
    方法2：Bellman-Ford算法变种
    
    算法步骤：
    1. 进行K+1次松弛操作（K次中转对应K+1条边）
    2. 每次迭代更新从源点到各城市的最小成本
    3. 使用临时数组避免同一轮次内的相互影响
    
    时间复杂度：O(K * E)
    空间复杂度：O(V)
    """
    # 距离数组
    dist = [float('inf')] * n
    dist[src] = 0
    
    # 进行K+1次松弛操作
    for i in range(k + 1):
        # 使用临时数组避免同一轮次内的相互影响
        temp = dist.copy()
        updated = False
        
        for from_city, to_city, price in flights:
            if dist[from_city] != float('inf') and dist[from_city] + price < temp[to_city]:
                temp[to_city] = dist[from_city] + price
                updated = True
        
        dist = temp
        # 如果没有更新，提前结束
        if not updated:
            break
    
    return -1 if dist[dst] == float('inf') else dist[dst]

def find_cheapest_price3(n, flights, src, dst, k):
    """
    方法3：BFS + 剪枝
    
    算法步骤：
    1. 使用BFS进行层次遍历，每层代表一次中转
    2. 维护每个城市的最小成本，进行剪枝优化
    3. 当中转次数超过K时停止搜索
    
    时间复杂度：O(V^K) 最坏情况，但实际剪枝后效率较高
    空间复杂度：O(V)
    """
    # 构建邻接表
    graph = defaultdict(list)
    for from_city, to_city, price in flights:
        graph[from_city].append((to_city, price))
    
    # 最小成本数组
    min_cost = [float('inf')] * n
    min_cost[src] = 0
    
    queue = deque()
    queue.append((src, 0))  # (当前城市, 当前成本)
    
    stops = 0
    
    while queue and stops <= k:
        size = len(queue)
        
        # 当前层的临时最小成本
        temp_cost = min_cost.copy()
        
        for _ in range(size):
            city, cost = queue.popleft()
            
            for next_city, price in graph[city]:
                new_cost = cost + price
                
                # 剪枝：如果新成本不小于已知最小成本，跳过
                if new_cost < temp_cost[next_city]:
                    temp_cost[next_city] = new_cost
                    queue.append((next_city, new_cost))
        
        min_cost = temp_cost
        stops += 1
    
    return -1 if min_cost[dst] == float('inf') else min_cost[dst]

class MultiConstraintShortestPath:
    """
    扩展：多约束条件的最短路径问题
    
    题目：带时间和成本约束的路径规划
    需要同时考虑时间约束和成本约束，找到满足所有约束条件的最优路径
    """
    
    @staticmethod
    def multi_constraint_shortest_path(n, edges, src, dst, max_time, max_cost):
        """
        多约束最短路径算法
        
        Args:
            n: 节点数
            edges: 边列表，格式为 [u, v, time, cost]
            src: 源点
            dst: 目标点
            max_time: 最大时间约束
            max_cost: 最大成本约束
        
        Returns:
            满足约束的最小成本，不存在返回-1
        """
        # 构建邻接表
        graph = defaultdict(list)
        for u, v, time, cost in edges:
            graph[u].append((v, time, cost))
            graph[v].append((u, time, cost))  # 无向图
        
        # 优先队列：存储(当前成本, 已用时间, 当前节点)
        heap = [(0, 0, src)]  # (cost, time, city)
        
        # 记录每个节点在特定时间下的最小成本
        node_state = defaultdict(dict)
        node_state[src][0] = 0
        
        while heap:
            cost, time, city = heapq.heappop(heap)
            
            if city == dst:
                return cost
            
            for next_city, edge_time, edge_cost in graph[city]:
                new_time = time + edge_time
                new_cost = cost + edge_cost
                
                # 检查约束条件
                if new_time > max_time or new_cost > max_cost:
                    continue
                
                # 剪枝：如果存在更优的状态，跳过
                should_add = True
                for existing_time, existing_cost in node_state[next_city].items():
                    if new_time >= existing_time and new_cost >= existing_cost:
                        should_add = False
                        break
                
                if should_add:
                    # 移除被新状态支配的旧状态
                    to_remove = []
                    for existing_time, existing_cost in node_state[next_city].items():
                        if existing_time >= new_time and existing_cost >= new_cost:
                            to_remove.append(existing_time)
                    
                    for t in to_remove:
                        del node_state[next_city][t]
                    
                    node_state[next_city][new_time] = new_cost
                    heapq.heappush(heap, (new_cost, new_time, next_city))
        
        return -1

def test_case_1():
    """
    测试用例1：K站中转内最便宜的航班
    """
    print("=== 测试用例1：K站中转内最便宜的航班 ===")
    n = 4
    flights = [
        (0, 1, 100), (1, 2, 100), (2, 0, 100), (1, 3, 600), (2, 3, 200)
    ]
    src, dst, k = 0, 3, 1
    
    print(f"方法1结果（动态规划+Dijkstra）: {find_cheapest_price1(n, flights, src, dst, k)}")
    print(f"方法2结果（Bellman-Ford变种）: {find_cheapest_price2(n, flights, src, dst, k)}")
    print(f"方法3结果（BFS+剪枝）: {find_cheapest_price3(n, flights, src, dst, k)}")

def test_case_2():
    """
    测试用例2：多约束最短路径
    """
    print("\n=== 测试用例2：多约束最短路径 ===")
    n = 4
    edges = [
        (0, 1, 2, 10), (0, 2, 5, 20), (1, 3, 3, 15), (2, 3, 1, 30)
    ]
    src, dst, max_time, max_cost = 0, 3, 6, 40
    
    result = MultiConstraintShortestPath.multi_constraint_shortest_path(
        n, edges, src, dst, max_time, max_cost)
    print(f"多约束最短路径结果: {result}")

def algorithm_analysis():
    """
    算法分析
    """
    print("\n=== 算法分析 ===")
    print("方法1：动态规划+Dijkstra")
    print("  - 优点：效率高，O(K * E * log(V))")
    print("  - 缺点：空间复杂度较高")
    print("  - 适用：中等规模图")
    
    print("方法2：Bellman-Ford变种")
    print("  - 优点：实现简单，空间效率高")
    print("  - 缺点：时间复杂度O(K * E)")
    print("  - 适用：小规模图或稀疏图")
    
    print("方法3：BFS+剪枝")
    print("  - 优点：实现简单，适合约束严格的情况")
    print("  - 缺点：最坏情况指数级复杂度")
    print("  - 适用：约束非常严格的情况")

if __name__ == "__main__":
    test_case_1()
    test_case_2()
    algorithm_analysis()
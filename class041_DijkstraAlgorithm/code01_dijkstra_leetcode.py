#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Dijkstra算法模版（LeetCode）

题目：网络延迟时间
链接：https://leetcode.cn/problems/network-delay-time

题目描述：
有 n 个网络节点，标记为 1 到 n。
给你一个列表 times，表示信号经过 有向 边的传递时间。
times[i] = (ui, vi, wi)，表示从ui到vi传递信号的时间是wi。
现在，从某个节点 s 发出一个信号。
需要多久才能使所有节点都收到信号？
如果不能使所有节点收到信号，返回 -1。

解题思路：
这是一个典型的单源最短路径问题，可以使用Dijkstra算法解决。
1. 构建图的邻接表表示
2. 使用优先队列实现Dijkstra算法
3. 计算从源节点到所有其他节点的最短距离
4. 返回所有最短距离中的最大值，即为网络延迟时间

算法应用场景：
- 网络路由协议
- GPS导航系统
- 社交网络中计算影响力传播时间

时间复杂度分析：
- O((V+E)logV)，其中V是节点数，E是边数

空间复杂度分析：
- O(V+E)，存储图和距离数组
"""

import heapq
from collections import defaultdict
import sys

def networkDelayTime(times, n, s):
    """
    使用Dijkstra算法计算网络延迟时间
    
    算法步骤：
    1. 构建邻接表表示的图
    2. 初始化距离数组，源节点距离为0，其他节点为无穷大
    3. 使用优先队列维护待处理节点，按距离从小到大排序
    4. 不断取出距离最小的节点，更新其邻居节点的最短距离
    5. 最后检查是否所有节点都能到达，返回最大距离
    
    时间复杂度：O((V+E)logV)
    空间复杂度：O(V+E)
    
    Args:
        times: List[List[int]] - 有向边的权重信息，times[i] = (ui, vi, wi)
        n: int - 节点总数
        s: int - 源节点
    
    Returns:
        int - 网络延迟时间，如果不能使所有节点收到信号则返回-1
    """
    # 构建邻接表表示的图
    # graph[i] 存储节点i的所有邻居节点及其边权重
    graph = defaultdict(list)
    for u, v, w in times:
        graph[u].append((v, w))
    
    # distance[i] 表示从源节点s到节点i的最短距离
    # 初始化距离为无穷大，表示尚未访问
    distance = [float('inf')] * (n + 1)
    # 源节点到自己的距离为0
    distance[s] = 0
    
    # visited[i] 表示节点i是否已经确定了最短距离
    # 用于避免重复处理已经确定最短距离的节点
    visited = [False] * (n + 1)
    
    # 优先队列，按距离从小到大排序
    # 元组含义：(距离, 节点编号)
    heap = [(0, s)]
    
    # Dijkstra算法主循环
    while heap:
        # 取出距离源点最近的节点
        # 由于使用优先队列，第一个元素总是距离最小的节点
        dist, u = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        # 这是为了避免同一节点多次处理导致的重复计算
        if visited[u]:
            continue
        
        # 标记为已处理，表示已确定从源节点到该节点的最短距离
        visited[u] = True
        
        # 遍历u的所有邻居节点
        # 对于每个邻居节点v，检查是否可以通过u节点获得更短的路径
        for v, w in graph[u]:
            # 如果邻居节点未访问且通过u到达v的距离更短，则更新
            # 松弛操作：如果 distance[u] + w < distance[v]，则更新distance[v]
            if not visited[v] and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                # 将更新后的节点加入优先队列
                heapq.heappush(heap, (distance[v], v))
    
    # 找到最大的最短距离
    # 这个最大值就是网络延迟时间，即所有节点收到信号所需的最长时间
    max_dist = max(distance[1:])  # 从索引1开始，因为节点编号从1开始
    
    # 如果有节点无法到达，返回-1
    # 如果max_dist仍为初始值float('inf')，说明有节点不可达
    return -1 if max_dist == float('inf') else max_dist



# =============================================================================
# 第K短路问题
# =============================================================================

"""
第K短路问题

题目描述：
给定一个有向图，求从起点s到终点t的第K短路径的长度。

解题思路：
第K短路问题可以通过改进的Dijkstra算法来解决。
我们使用A*算法的思想，维护一个优先队列，队列中的元素按照预估总距离（已走距离+到终点的启发式距离）排序。
每次取出预估总距离最小的节点，如果是终点，则记录次数，当次数达到K时，返回当前距离。
为了提高效率，我们可以预先计算终点到所有其他节点的最短距离作为启发式函数。

算法应用场景：
- 交通导航中提供多条备选路线
- 网络路由中的路径多样性
- 机器人路径规划中的备选路径

时间复杂度分析：
O(E*K*log(E*K))，其中E是边数，K是要求的第K短路

空间复杂度分析：
O(V+E+K*V)，存储图、距离数组和优先队列
"""

def find_kth_shortest_path(start, end, n, edges, K):
    """
    使用A*算法求解第K短路问题
    
    算法步骤：
    1. 首先在反向图上运行Dijkstra算法，计算终点到所有节点的最短距离（作为启发式函数）
    2. 在原图上使用优先队列进行启发式搜索，队列按预估总距离排序
    3. 每次从队列取出一个节点，如果是终点则计数增加
    4. 当终点计数达到K时，返回当前路径长度
    
    时间复杂度：O(E*K*log(E*K))
    空间复杂度：O(V+E+K*V)
    
    Args:
        start: int - 起点
        end: int - 终点
        n: int - 节点总数
        edges: List[List[int]] - 边列表，格式为 [u, v, w]
        K: int - 要求的第K短路
    
    Returns:
        int - 第K短路的长度，如果不存在返回-1
    """
    # 构建原图和反向图
    graph = [[] for _ in range(n + 1)]
    reverse_graph = [[] for _ in range(n + 1)]
    
    for u, v, w in edges:
        graph[u].append((v, w))
        reverse_graph[v].append((u, w))
    
    # 步骤1：在反向图上运行Dijkstra算法，计算终点到所有节点的最短距离
    def dijkstra_reverse(start_node):
        dist = [float('inf')] * (n + 1)
        dist[start_node] = 0
        heap = [(0, start_node)]
        
        while heap:
            d, u = heapq.heappop(heap)
            if d > dist[u]:
                continue
            
            for v, w in reverse_graph[u]:
                if dist[v] > d + w:
                    dist[v] = d + w
                    heapq.heappush(heap, (dist[v], v))
        
        return dist
    
    # 计算终点到所有节点的最短距离（启发式函数）
    dist_to_end = dijkstra_reverse(end)
    
    # 步骤2：使用A*算法寻找第K短路
    # 优先队列，按照预估总距离排序：(预估总距离, 已走距离, 当前节点)
    heap = [(dist_to_end[start], 0, start)]
    
    # 记录到达每个节点的路径数
    count = [0] * (n + 1)
    
    while heap:
        _, current_dist, u = heapq.heappop(heap)
        
        # 如果到达终点，计数加一
        if u == end:
            count[u] += 1
            if count[u] == K:
                return current_dist
        
        # 如果到达该节点的路径数已经超过K，跳过
        if count[u] > K:
            continue
        count[u] += 1
        
        # 遍历所有邻居节点
        for v, w in graph[u]:
            new_dist = current_dist + w
            # 计算预估总距离：已走距离 + 到终点的最短距离
            estimated_total = new_dist + dist_to_end[v]
            heapq.heappush(heap, (estimated_total, new_dist, v))
    
    return -1  # 不存在第K短路



# =============================================================================
# 带状态的最短路径问题：电动车游城市
# =============================================================================

"""
带状态的最短路径问题：电动车游城市

题目描述：
城市之间有公路相连，每条公路有长度。电动车有一个电池容量限制，每行驶1公里消耗1单位电量。
城市中可以充电，充电可以将电量恢复到满。求从起点到终点的最短路径长度。

解题思路：
这是一个典型的带状态的最短路径问题。
状态不仅包括当前所在城市，还包括当前剩余电量。
我们可以使用Dijkstra算法的变种，其中每个状态是(城市, 电量)，边表示行驶或充电操作。

算法应用场景：
- 电动车路径规划
- 资源受限的路径优化
- 带约束条件的最短路径问题

时间复杂度分析：
O(C*E*log(C*V))，其中C是电池容量，E是边数，V是节点数

空间复杂度分析：
O(C*V)，存储状态和距离数组
"""

def find_shortest_path_with_charge(start, end, n, edges, capacity):
    """
    使用Dijkstra算法求解电动车路径规划问题
    
    算法步骤：
    1. 将状态定义为(城市, 电量)，其中电量范围是0到capacity
    2. 初始化距离数组，记录到达每个状态的最短距离
    3. 使用优先队列进行搜索，队列按距离排序
    4. 对于每个状态，考虑充电和行驶两种操作
    5. 更新可达的新状态并加入队列
    
    时间复杂度：O(C*E*log(C*V))
    空间复杂度：O(C*V)
    
    Args:
        start: int - 起点城市
        end: int - 终点城市
        n: int - 城市总数
        edges: List[List[int]] - 边列表，格式为 [u, v, w]
        capacity: int - 电池容量
    
    Returns:
        int - 最短路径长度，如果无法到达返回-1
    """
    # 构建图的邻接表表示
    graph = [[] for _ in range(n + 1)]
    for u, v, w in edges:
        graph[u].append((v, w))
        graph[v].append((u, w))  # 假设公路是双向的
    
    # dist[u][c] 表示到达城市u且剩余电量为c时的最短距离
    dist = [[float('inf')] * (capacity + 1) for _ in range(n + 1)]
    
    # 优先队列，按照距离排序：(距离, 城市, 电量)
    heap = [(0, start, capacity)]
    dist[start][capacity] = 0
    
    while heap:
        d, u, c = heapq.heappop(heap)
        
        # 如果已经到达终点，返回最短距离
        if u == end:
            return d
        
        # 如果当前距离大于记录的最小距离，跳过
        if d > dist[u][c]:
            continue
        
        # 操作1：在当前城市充电，将电量充满
        if c < capacity:
            if dist[u][capacity] > d:
                dist[u][capacity] = d
                heapq.heappush(heap, (d, u, capacity))
        
        # 操作2：前往相邻城市
        for v, w in graph[u]:
            # 检查电量是否足够行驶这段距离
            if c >= w:
                new_c = c - w
                if dist[v][new_c] > d + w:
                    dist[v][new_c] = d + w
                    heapq.heappush(heap, (d + w, v, new_c))
    
    return -1  # 无法到达终点



# 测试代码
if __name__ == "__main__":
    # 测试网络延迟时间
    times = [[2, 1, 1], [2, 3, 1], [3, 4, 1]]
    n = 4
    s = 2
    print("网络延迟时间:", networkDelayTime(times, n, s))
    
    # 测试第K短路
    edges_kth = [[1, 2, 1], [1, 3, 3], [2, 3, 1], [3, 4, 2], [2, 4, 5]]
    start_kth, end_kth, n_kth, K = 1, 4, 4, 3
    print("第3短路长度:", find_kth_shortest_path(start_kth, end_kth, n_kth, edges_kth, K))
    
    # 测试电动车路径规划
    edges_charge = [[1, 2, 3], [2, 3, 4], [1, 3, 10], [3, 4, 5]]
    start_charge, end_charge, n_charge, capacity = 1, 4, 4, 6
    print("电动车最短路径:", find_shortest_path_with_charge(start_charge, end_charge, n_charge, edges_charge, capacity))


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    # 输入：times = [[2,1,1],[2,3,1],[3,4,1]], n = 4, s = 2
    # 输出：2
    times1 = [[2,1,1],[2,3,1],[3,4,1]]
    n1 = 4
    s1 = 2
    result1 = networkDelayTime(times1, n1, s1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 2
    
    # 测试用例2
    # 输入：times = [[1,2,1]], n = 2, s = 1
    # 输出：1
    times2 = [[1,2,1]]
    n2 = 2
    s2 = 1
    result2 = networkDelayTime(times2, n2, s2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 1
    
    # 测试用例3
    # 输入：times = [[1,2,1]], n = 2, s = 2
    # 输出：-1
    times3 = [[1,2,1]]
    n3 = 2
    s3 = 2
    result3 = networkDelayTime(times3, n3, s3)
    print(f"测试用例3结果: {result3}")  # 期望输出: -1
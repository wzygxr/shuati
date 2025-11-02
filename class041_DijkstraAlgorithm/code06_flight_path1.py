#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
飞行路线（语言提供的堆）

题目链接：https://www.luogu.com.cn/problem/P4568

题目描述：
Alice和Bob现在要乘飞机旅行，他们选择了一家相对便宜的航空公司
该航空公司一共在n个城市设有业务，设这些城市分别标记为0 ~ n−1
一共有m种航线，每种航线连接两个城市，并且航线有一定的价格
Alice 和 Bob 现在要从一个城市沿着航线到达另一个城市，途中可以进行转机
航空公司对他们这次旅行也推出优惠，他们可以免费在最多k种航线上搭乘飞机
那么 Alice 和 Bob 这次出行最少花费多少

解题思路：
这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
与传统最短路径不同的是，这里的状态不仅包括城市位置，还包括已使用的免费机会次数。
我们将状态定义为(城市, 已使用免费机会次数)，图中的节点是这些状态对。
边有两种类型：
1. 免费边：使用一次免费机会乘坐航班，花费为0
2. 付费边：正常付费乘坐航班，花费为票价
使用Dijkstra算法找到从起点状态(起点城市, 0次免费机会)到终点状态(终点城市, 任意免费机会次数)的最少花费。

算法应用场景：
- 优惠券使用策略优化
- 资源受限的路径规划
- 多状态动态规划问题

时间复杂度分析：
O((V+k*E)log(V+k*E)) 其中V是城市数，E是航线数

空间复杂度分析：
O(V*k) 存储距离数组和访问标记数组
"""

import heapq
from collections import defaultdict
import sys

def dijkstra(n, m, k, s, t, flights):
    """
    使用Dijkstra算法求解最短路径
    
    算法核心思想：
    1. 将问题转化为图论中的最短路径问题
    2. 状态定义为(城市, 已使用免费机会次数)，图中的节点是这些状态对
    3. 边有两种类型：免费边和付费边
    4. 使用Dijkstra算法找到从起点状态到终点状态的最少花费
    
    算法步骤：
    1. 初始化距离数组，起点状态距离为0，其他状态为无穷大
    2. 使用优先队列维护待处理状态，按花费从小到大排序
    3. 不断取出花费最小的状态，通过使用或不使用免费机会扩展新状态
    4. 当处理到终点城市时，直接返回结果（剪枝优化）
    
    时间复杂度: O((V+k*E)log(V+k*E)) 其中V是城市数，E是航线数
    空间复杂度: O(V*k)
    
    Args:
        n: int - 城市数量
        m: int - 航线数量
        k: int - 免费机会次数
        s: int - 起点城市
        t: int - 终点城市
        flights: List[List[int]] - 航线信息，flights[i] = [起点城市, 终点城市, 价格]
    
    Returns:
        int - 从起点城市到终点城市的最少花费
    """
    # 构建邻接表表示的图
    graph = defaultdict(list)
    # 添加边到图中（无向图）
    for a, b, c in flights:
        graph[a].append((b, c))  # 起点城市到终点城市
        graph[b].append((a, c))  # 终点城市到起点城市
    
    # distance[i][j]表示到达城市i且已使用j次免费机会的最少花费
    # 初始化为最大值，表示尚未访问
    distance = [[float('inf')] * (k + 1) for _ in range(n)]
    
    # visited[i][j]表示状态(城市i, 使用j次免费机会)是否已经确定了最短路径
    # 用于避免重复处理已经确定最短路径的状态
    visited = [[False] * (k + 1) for _ in range(n)]
    
    # 初始状态：在起点城市且未使用免费机会，花费为0
    distance[s][0] = 0
    
    # 优先队列，按花费从小到大排序
    # 元组含义：(花费, 城市, 已使用免费机会次数)
    heap = [(0, s, 0)]
    
    # Dijkstra算法主循环
    while heap:
        # 取出花费最小的状态
        cost, u, use = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        # 这是为了避免同一状态多次处理导致的重复计算
        if visited[u][use]:
            continue
        
        # 标记为已处理，表示已确定从起点到该状态的最少花费
        visited[u][use] = True
        
        # 如果到达终点，直接返回结果
        # 常见剪枝优化：发现终点直接返回，不用等都结束
        # 这是因为Dijkstra算法的特性保证了第一次到达终点时就是最优解
        if u == t:
            return cost
        
        # 遍历所有出边（从当前城市出发的所有航线）
        for v, w in graph[u]:
            # 使用免费机会
            # 如果还有免费机会且使用免费机会后花费更少
            if use < k and distance[v][use + 1] > distance[u][use]:
                # 使用免费
                distance[v][use + 1] = distance[u][use]  # 花费为0
                # 将使用免费机会后的新状态加入优先队列
                heapq.heappush(heap, (distance[v][use + 1], v, use + 1))
            
            # 不使用免费机会
            # 如果不使用免费机会且花费更少
            if distance[v][use] > distance[u][use] + w:
                # 不用免费
                distance[v][use] = distance[u][use] + w  # 花费为原花费加票价
                # 将不使用免费机会的新状态加入优先队列
                heapq.heappush(heap, (distance[v][use], v, use))
    
    # 理论上不会执行到这里，因为从起点到终点总是存在路径
    return -1


# 测试用例
if __name__ == "__main__":
    # 由于这是洛谷的在线测试题，这里只提供简单的测试用例
    # 实际使用时需要根据输入格式进行调整
    
    # 示例测试用例
    # n = 5, m = 7, k = 1
    # s = 0, t = 4
    # flights = [[0,1,10],[1,2,10],[2,3,10],[3,4,10],[0,2,5],[1,3,5],[2,4,5]]
    # 期望输出: 15
    
    n = 5
    m = 7
    k = 1
    s = 0
    t = 4
    flights = [[0,1,10],[1,2,10],[2,3,10],[3,4,10],[0,2,5],[1,3,5],[2,4,5]]
    
    result = dijkstra(n, m, k, s, t, flights)
    print(f"测试用例结果: {result}")  # 期望输出: 15
#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
电动车游城市

题目链接：https://leetcode.cn/problems/DFPeFJ/

题目描述：
小明的电动车电量充满时可行驶距离为 cnt，每行驶 1 单位距离消耗 1 单位电量，且花费 1 单位时间
小明想选择电动车作为代步工具。地图上共有 N 个景点，景点编号为 0 ~ N-1
他将地图信息以 [城市 A 编号,城市 B 编号,两城市间距离] 格式整理在在二维数组 paths，
表示城市 A、B 间存在双向通路。
初始状态，电动车电量为 0。每个城市都设有充电桩，
charge[i] 表示第 i 个城市每充 1 单位电量需要花费的单位时间。
请返回小明最少需要花费多少单位时间从起点城市 start 抵达终点城市 end

解题思路：
这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
与传统最短路径不同的是，这里的状态不仅包括城市位置，还包括电动车的电量。
我们将状态定义为(城市, 电量)，图中的节点是这些状态对。
边有两种类型：
1. 充电边：在当前城市充电1单位电量，时间消耗为charge[城市]
2. 行驶边：从当前城市行驶到相邻城市，时间消耗为距离，电量消耗为距离
使用Dijkstra算法找到从起点状态(起点城市, 0电量)到终点状态(终点城市, 任意电量)的最短时间。

算法应用场景：
- 电动车路径规划
- 资源受限的路径优化问题
- 多状态动态规划问题

时间复杂度分析：
O(n*cnt*log(n*cnt)) 其中n是城市数量，cnt是电动车最大电量

空间复杂度分析：
O(n*cnt) 存储距离数组和访问标记数组
"""

import heapq
from collections import defaultdict

def electricCarPlan(paths, cnt, start, end, charge):
    """
    使用Dijkstra算法求解最短时间
    
    算法核心思想：
    1. 将问题转化为图论中的最短路径问题
    2. 状态定义为(城市, 电量)，图中的节点是这些状态对
    3. 边有两种类型：充电边和行驶边
    4. 使用Dijkstra算法找到从起点状态到终点状态的最短时间
    
    算法步骤：
    1. 初始化距离数组，起点状态距离为0，其他状态为无穷大
    2. 使用优先队列维护待处理状态，按时间从小到大排序
    3. 不断取出时间最小的状态，通过充电或行驶扩展新状态
    4. 当处理到终点城市时，直接返回结果（剪枝优化）
    
    时间复杂度: O(n*cnt*log(n*cnt)) 其中n是城市数量
    空间复杂度: O(n*cnt)
    
    Args:
        paths: List[List[int]] - 城市间的路径信息，paths[i] = [城市A编号, 城市B编号, 距离]
        cnt: int - 电动车最大电量
        start: int - 起点城市编号
        end: int - 终点城市编号
        charge: List[int] - 每个城市充电1单位电量所需时间，charge[i]表示城市i的充电时间
    
    Returns:
        int - 从起点城市到终点城市的最少时间
    """
    n = len(charge)  # 城市数量
    
    # 构建邻接表表示的图
    graph = defaultdict(list)
    # 添加边到图中（无向图）
    # 对于每条路径，添加两个方向的边
    for path in paths:
        graph[path[0]].append((path[1], path[2]))  # 城市A到城市B
        graph[path[1]].append((path[0], path[2]))  # 城市B到城市A
    
    # distance[i][j]表示到达城市i且电量为j的最少时间
    # 初始化为最大值，表示尚未访问
    distance = [[float('inf')] * (cnt + 1) for _ in range(n)]
    # 初始状态：在起点城市且电量为0，时间为0
    distance[start][0] = 0
    
    # visited[i][j]表示状态(城市i, 电量j)是否已经确定了最短时间
    # 用于避免重复处理已经确定最短时间的状态
    visited = [[False] * (cnt + 1) for _ in range(n)]
    
    # 优先队列，按时间从小到大排序
    # 元组含义：(时间, 城市, 电量)
    heap = [(0, start, 0)]
    
    # Dijkstra算法主循环
    while heap:
        # 取出时间最小的状态
        cost, cur, power = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        # 这是为了避免同一状态多次处理导致的重复计算
        if visited[cur][power]:
            continue
        
        # 如果到达终点，直接返回结果
        # 常见剪枝优化：发现终点直接返回，不用等都结束
        # 这是因为Dijkstra算法的特性保证了第一次到达终点时就是最优解
        if cur == end:
            return cost
        
        # 标记为已处理，表示已确定从起点到该状态的最少时间
        visited[cur][power] = True
        
        # 在当前城市充电1单位
        # 这是状态扩展的第一种方式：充电
        if power < cnt:
            # 充一格电，电量不能超过最大电量cnt
            # 新状态：(当前城市, 电量+1)
            # 时间消耗：cost + charge[cur]
            if not visited[cur][power + 1] and cost + charge[cur] < distance[cur][power + 1]:
                distance[cur][power + 1] = cost + charge[cur]
                # 将充电后的新状态加入优先队列
                heapq.heappush(heap, (cost + charge[cur], cur, power + 1))
        
        # 去别的城市
        # 这是状态扩展的第二种方式：行驶
        for nextCity, dist in graph[cur]:
            # 不充电去别的城市
            restPower = power - dist  # 行驶后剩余电量（电量消耗等于距离）
            nextCost = cost + dist    # 行驶后的时间（时间消耗等于距离）
            
            # 电量足够且新的时间更短
            # 1. restPower >= 0：电量足够行驶到下一个城市
            # 2. not visited[nextCity][restPower]：新状态未访问过
            # 3. nextCost < distance[nextCity][restPower]：新时间更短
            if restPower >= 0 and not visited[nextCity][restPower] and nextCost < distance[nextCity][restPower]:
                distance[nextCity][restPower] = nextCost
                # 将行驶后的新状态加入优先队列
                heapq.heappush(heap, (nextCost, nextCity, restPower))
    
    # 理论上不会执行到这里，因为从起点到终点总是存在路径
    return -1


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    # 输入：paths = [[1,3,3],[3,2,1],[2,1,3],[0,1,4],[3,0,5]], cnt = 6, start = 1, end = 0, charge = [2,10,4,10,1]
    # 输出：43
    paths1 = [[1,3,3],[3,2,1],[2,1,3],[0,1,4],[3,0,5]]
    cnt1 = 6
    start1 = 1
    end1 = 0
    charge1 = [2,10,4,10,1]
    result1 = electricCarPlan(paths1, cnt1, start1, end1, charge1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 43
    
    # 测试用例2
    # 输入：paths = [[0,1,3],[1,2,1],[2,3,1],[3,4,1]], cnt = 5, start = 0, end = 4, charge = [1,2,3,4,5]
    # 输出：10
    paths2 = [[0,1,3],[1,2,1],[2,3,1],[3,4,1]]
    cnt2 = 5
    start2 = 0
    end2 = 4
    charge2 = [1,2,3,4,5]
    result2 = electricCarPlan(paths2, cnt2, start2, end2, charge2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 10
#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
路径最大概率

题目链接: https://leetcode.cn/problems/path-with-maximum-probability/

题目描述：
给你一个由 n 个节点（下标从 0 开始）组成的无向加权图，
该图由一个描述边的列表组成，其中 edges[i] = [a, b] 表示连接节点 a 和 b 的一条无向边，
且该边遍历成功的概率为 succProb[i] 。
指定两个节点分别作为起点 start 和终点 end ，
请你找出从起点到终点成功概率最大的路径，并返回其成功概率。
如果不存在从 start 到 end 的路径，返回 0。

解题思路：
这是一个变形的最短路径问题，可以使用Dijkstra算法解决。
与传统最短路径不同的是，这里要找的是最大概率路径，而不是最小距离路径。
我们将概率作为边的权重，路径的概率是所有边概率的乘积。
使用Dijkstra算法找到从起点到终点的最大概率路径。

算法应用场景：
- 网络传输中的最大成功概率路径
- 通信系统中的可靠路径选择
- 风险评估中的最大收益路径

时间复杂度分析：
O((V+E)logV) 其中V是节点数，E是边数

空间复杂度分析：
O(V+E) 存储图、概率数组和访问标记数组
"""

import heapq
from collections import defaultdict

def maxProbability(n, edges, succProb, start, end):
    """
    使用Dijkstra算法求解路径最大概率
    
    算法核心思想：
    1. 将问题转化为图论中的最短路径问题的变种
    2. 边的权重是遍历成功的概率
    3. 路径的概率是所有边概率的乘积
    4. 使用Dijkstra算法找到从起点到终点的最大概率路径
    
    算法步骤：
    1. 初始化概率数组，起点概率为1，其他节点为0
    2. 使用优先队列维护待处理节点，按概率从大到小排序
    3. 不断取出概率最大的节点，更新其邻居节点的最大概率
    4. 当处理到终点时，直接返回结果（剪枝优化）
    
    时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    空间复杂度: O(V+E)
    
    Args:
        n: int - 节点数量
        edges: List[List[int]] - 边的信息，edges[i] = [节点a, 节点b]
        succProb: List[float] - 边的成功概率，succProb[i]对应edges[i]的概率
        start: int - 起点节点
        end: int - 终点节点
    
    Returns:
        float - 从起点到终点的最大成功概率
    """
    # 构建邻接表表示的图
    # graph[i] 存储节点i的所有邻居节点及其边概率
    graph = defaultdict(list)
    
    # 添加边到图中
    # 对于每条边，将其添加到两个节点的邻居列表中（无向图）
    for i in range(len(edges)):
        a, b = edges[i]
        prob = succProb[i]
        # 添加a到b的边
        graph[a].append((b, prob))
        # 添加b到a的边（无向图）
        graph[b].append((a, prob))
    
    # probability[i]表示从起点start到节点i的最大成功概率
    # 初始化为0.0，表示尚未访问
    probability = [0.0] * n
    
    # visited[i]表示节点i是否已经确定了最大概率
    # 用于避免重复处理已经确定最大概率的节点
    visited = [False] * n
    
    # 优先队列，按概率从大到小排序
    # 元组含义：(-概率, 节点) 使用负数实现大顶堆
    heap = [(-1.0, start)]
    
    # 初始状态：在起点，概率为1
    probability[start] = 1.0  # 从起点到自身的概率为1
    
    # Dijkstra算法主循环
    while heap:
        # 取出概率最大的节点
        neg_prob, u = heapq.heappop(heap)
        prob = -neg_prob  # 转换回正数概率
        
        # 如果已经处理过，跳过
        # 这是为了避免同一节点多次处理导致的重复计算
        if visited[u]:
            continue
        
        # 标记为已处理，表示已确定从起点到该节点的最大概率
        visited[u] = True
        
        # 如果到达终点，直接返回结果
        # 常见剪枝优化：发现终点直接返回，不用等都结束
        # 这是因为Dijkstra算法的特性保证了第一次到达终点时就是最优解
        if u == end:
            return prob
        
        # 遍历所有邻居节点
        for v, edgeProb in graph[u]:
            # 从起点经过u到达v的概率 = 从起点到u的概率 * 从u到v的概率
            newProb = prob * edgeProb
            
            # 如果通过u到达v的概率更大，则更新
            # 松弛操作：如果 newProb > probability[v]，则更新probability[v]
            if newProb > probability[v]:
                probability[v] = newProb  # 更新最大概率
                # 将更新后的节点加入优先队列
                heapq.heappush(heap, (-newProb, v))
    
    # 不存在从起点到终点的路径
    return 0.0


# 测试用例
if __name__ == "__main__":
    # 示例1
    # 输入: n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5, 0.5, 0.2], start = 0, end = 2
    # 输出: 0.25000
    # 解释: 有两条路径从0到2，0->2概率为0.2，0->1->2概率为0.5*0.5=0.25，最大概率为0.25
    n1 = 3
    edges1 = [[0,1],[1,2],[0,2]]
    succProb1 = [0.5, 0.5, 0.2]
    start1 = 0
    end1 = 2
    result1 = maxProbability(n1, edges1, succProb1, start1, end1)
    print(f"测试用例1结果: {result1:.5f}")  # 输出: 0.25000
    
    # 示例2
    # 输入: n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5, 0.5, 0.3], start = 0, end = 2
    # 输出: 0.30000
    # 解释: 直接路径0->2概率为0.3，大于0->1->2概率0.25，最大概率为0.3
    n2 = 3
    edges2 = [[0,1],[1,2],[0,2]]
    succProb2 = [0.5, 0.5, 0.3]
    start2 = 0
    end2 = 2
    result2 = maxProbability(n2, edges2, succProb2, start2, end2)
    print(f"测试用例2结果: {result2:.5f}")  # 输出: 0.30000
    
    # 示例3
    # 输入: n = 3, edges = [[0,1]], succProb = [0.5], start = 0, end = 2
    # 输出: 0.00000
    # 解释: 节点0和2不连通，不存在路径，返回0
    n3 = 3
    edges3 = [[0,1]]
    succProb3 = [0.5]
    start3 = 0
    end3 = 2
    result3 = maxProbability(n3, edges3, succProb3, start3, end3)
    print(f"测试用例3结果: {result3:.5f}")  # 输出: 0.00000
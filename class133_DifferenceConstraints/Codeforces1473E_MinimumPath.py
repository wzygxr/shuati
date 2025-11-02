#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 1473E - Minimum Path 差分约束系统解法（Python版本）

题目链接：https://codeforces.com/contest/1473/problem/E

题目描述：
给定一个带权无向图，定义一条路径的代价为：
路径上所有边的权重之和减去最大边权重加上最小边权重。
求从节点1到其他所有节点的最小代价。

解题思路：
这是一个复杂的图论问题，可以通过状态扩展和差分约束思想来解决。
我们可以将每个节点扩展为4种状态：
状态0：正常路径
状态1：已经减去了一条边的权重（即已经使用了"减去最大边"操作）
状态2：已经加上了一条边的权重（即已经使用了"加上最小边"操作）
状态3：既减去了最大边又加上了最小边

对于每条边(u, v, w)，我们可以进行以下状态转移：
1. 正常转移：状态0 -> 状态0，代价为w
2. 减去当前边：状态0 -> 状态1，代价为0（相当于减去最大边）
3. 加上当前边：状态0 -> 状态2，代价为2w（相当于加上最小边）
4. 从状态1转移：状态1 -> 状态1，代价为w
5. 从状态1加上边：状态1 -> 状态3，代价为2w
6. 从状态2转移：状态2 -> 状态2，代价为w
7. 从状态2减去边：状态2 -> 状态3，代价为0
8. 状态3转移：状态3 -> 状态3，代价为w

这样我们就将原问题转化为在扩展图上求最短路的问题。
最终答案就是状态3的最短路径值。

时间复杂度：O((n + m) * log(n))，使用Dijkstra算法
空间复杂度：O(n + m)

相关题目：
1. Codeforces 1473E - Minimum Path - 本题
2. POJ 1201 Intervals - 区间选点问题
3. POJ 2983 Is the Information Reliable? - 信息可靠性判断
4. POJ 3169 Layout - 奶牛排队布局问题
5. POJ 1364 King - 国王序列约束问题
6. 洛谷 P5960 【模板】差分约束算法
7. 洛谷 P1993 小K的农场
8. 洛谷 P1250 种树
9. 洛谷 P2294 [HNOI2005]狡猾的商人
10. 洛谷 P4926 [1007]倍杀测量者
11. 洛谷 P3275 [SCOI2011]糖果
12. LibreOJ #10087 「一本通3.4 例1」Intervals
13. LibreOJ #10088 「一本通3.4 例2」出纳员问题
14. AtCoder ABC216G 01Sequence

工程化考虑：
1. 异常处理：输入校验、图构建检查、算法执行检测
2. 性能优化：优先队列优化、状态压缩
3. 可维护性：状态定义清晰、转移逻辑明确
4. 可扩展性：支持更多操作类型
5. 边界情况：单节点、空图、极端权重
6. 测试用例：基本功能、边界值、异常情况
"""

import sys
import heapq

class Graph:
    """图类，使用链式前向星存储"""
    
    def __init__(self, max_nodes, max_edges):
        self.max_nodes = max_nodes
        self.max_edges = max_edges
        self.head = [0] * (max_nodes + 1)
        self.next = [0] * (max_edges * 2 + 10)
        self.to = [0] * (max_edges * 2 + 10)
        self.weight = [0] * (max_edges * 2 + 10)
        self.cnt = 1
    
    def add_edge(self, u, v, w):
        """添加边到图中"""
        self.next[self.cnt] = self.head[u]
        self.to[self.cnt] = v
        self.weight[self.cnt] = w
        self.head[u] = self.cnt
        self.cnt += 1

class Dijkstra:
    """Dijkstra算法实现"""
    
    def __init__(self, graph, max_nodes):
        self.graph = graph
        self.max_nodes = max_nodes
        self.INF = 10**18
    
    def shortest_path(self, n):
        """计算最短路径"""
        # 初始化距离数组
        dist = [[self.INF] * 4 for _ in range(n + 1)]
        
        # 优先队列
        pq = []
        dist[1][0] = 0
        heapq.heappush(pq, (0, 1, 0))
        
        while pq:
            cost, u, state = heapq.heappop(pq)
            
            if cost != dist[u][state]:
                continue
            
            # 遍历所有邻接边
            i = self.graph.head[u]
            while i > 0:
                v = self.graph.to[i]
                w = self.graph.weight[i]
                
                # 状态转移
                # 状态0 -> 状态0：正常转移
                if dist[v][state] > cost + w:
                    dist[v][state] = cost + w
                    heapq.heappush(pq, (dist[v][state], v, state))
                
                # 状态0 -> 状态1：减去当前边（最大边）
                if state == 0 and dist[v][1] > cost:
                    dist[v][1] = cost
                    heapq.heappush(pq, (dist[v][1], v, 1))
                
                # 状态0 -> 状态2：加上当前边（最小边）
                if state == 0 and dist[v][2] > cost + 2 * w:
                    dist[v][2] = cost + 2 * w
                    heapq.heappush(pq, (dist[v][2], v, 2))
                
                # 状态1 -> 状态1：正常转移
                if state == 1 and dist[v][1] > cost + w:
                    dist[v][1] = cost + w
                    heapq.heappush(pq, (dist[v][1], v, 1))
                
                # 状态1 -> 状态3：加上当前边
                if state == 1 and dist[v][3] > cost + 2 * w:
                    dist[v][3] = cost + 2 * w
                    heapq.heappush(pq, (dist[v][3], v, 3))
                
                # 状态2 -> 状态2：正常转移
                if state == 2 and dist[v][2] > cost + w:
                    dist[v][2] = cost + w
                    heapq.heappush(pq, (dist[v][2], v, 2))
                
                # 状态2 -> 状态3：减去当前边
                if state == 2 and dist[v][3] > cost:
                    dist[v][3] = cost
                    heapq.heappush(pq, (dist[v][3], v, 3))
                
                # 状态3 -> 状态3：正常转移
                if state == 3 and dist[v][3] > cost + w:
                    dist[v][3] = cost + w
                    heapq.heappush(pq, (dist[v][3], v, 3))
                
                i = self.graph.next[i]
        
        return dist

def main():
    """主函数"""
    data = sys.stdin.read().strip().split()
    if not data:
        return
    
    n = int(data[0])
    m = int(data[1])
    idx = 2
    
    # 初始化图
    max_nodes = n + 1
    max_edges = m
    graph = Graph(max_nodes, max_edges)
    
    # 读入边
    for i in range(m):
        if idx + 2 >= len(data):
            break
            
        u = int(data[idx])
        v = int(data[idx + 1])
        w = int(data[idx + 2])
        idx += 3
        
        # 无向图，添加双向边
        graph.add_edge(u, v, w)
        graph.add_edge(v, u, w)
    
    # 运行Dijkstra算法
    dijkstra = Dijkstra(graph, max_nodes)
    dist = dijkstra.shortest_path(n)
    
    # 输出结果
    result = []
    for i in range(2, n + 1):
        result.append(str(dist[i][3]))
    print(" ".join(result))

if __name__ == "__main__":
    main()
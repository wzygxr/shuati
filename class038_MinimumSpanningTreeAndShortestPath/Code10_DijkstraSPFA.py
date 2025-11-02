# Dijkstra算法与SPFA算法的实现与比较
# 
# 解题思路:
# Dijkstra算法适用于所有边权为非负数的图，使用优先队列优化，时间复杂度为O(E log V)
# SPFA算法是Bellman-Ford算法的队列优化版本，可以处理负权边，时间复杂度平均为O(E)，最坏情况下为O(VE)
# 当图中存在负权边时，Dijkstra算法可能会给出错误的结果，此时应使用SPFA算法
#
# 时间复杂度:
# Dijkstra: O(E log V)，其中E是边数，V是顶点数
# SPFA: 平均O(E)，最坏O(VE)
# 空间复杂度: O(V + E)
#
# 两种算法的应用场景:
# 1. 当图中所有边的权值都是非负数时，优先使用Dijkstra算法
# 2. 当图中存在负权边时，必须使用SPFA算法
# 3. SPFA还可以用来检测图中是否存在负权环

import heapq
from collections import deque

def dijkstra(graph, start):
    """
    Dijkstra算法实现 - 使用优先队列优化
    参数:
        graph: 图的邻接表表示，格式为[[(邻居, 权重), ...], ...]
        start: 起始顶点
    返回:
        distances: 从起始顶点到所有顶点的最短距离
    """
    n = len(graph)
    # 初始化距离数组，全部设为无穷大
    distances = [float('inf')] * n
    # 起始顶点到自身的距离为0
    distances[start] = 0
    # 使用优先队列，存储(距离, 顶点)，按照距离从小到大排序
    priority_queue = [(0, start)]
    # 记录已确定最短距离的顶点
    visited = [False] * n
    
    while priority_queue:
        # 取出当前距离最小的顶点
        current_distance, current_vertex = heapq.heappop(priority_queue)
        
        # 如果该顶点已经处理过，跳过
        if visited[current_vertex]:
            continue
        
        # 标记该顶点为已处理
        visited[current_vertex] = True
        
        # 遍历当前顶点的所有邻居
        for neighbor, weight in graph[current_vertex]:
            # 如果通过当前顶点到达邻居的距离更短
            if distances[neighbor] > current_distance + weight:
                distances[neighbor] = current_distance + weight
                # 将更新后的邻居顶点加入优先队列
                heapq.heappush(priority_queue, (distances[neighbor], neighbor))
    
    return distances

def spfa(graph, start):
    """
    SPFA算法实现 - Bellman-Ford算法的队列优化版本
    参数:
        graph: 图的邻接表表示，格式为[[(邻居, 权重), ...], ...]
        start: 起始顶点
    返回:
        distances: 从起始顶点到所有顶点的最短距离
        has_negative_cycle: 图中是否存在负权环
    """
    n = len(graph)
    # 初始化距离数组，全部设为无穷大
    distances = [float('inf')] * n
    # 起始顶点到自身的距离为0
    distances[start] = 0
    # 使用队列存储待处理的顶点
    queue = deque([start])
    # 记录顶点是否在队列中
    in_queue = [False] * n
    in_queue[start] = True
    # 记录每个顶点的入队次数，用于检测负权环
    count = [0] * n
    count[start] = 1
    # 标记图中是否存在负权环
    has_negative_cycle = False
    
    while queue and not has_negative_cycle:
        # 取出队首顶点
        current_vertex = queue.popleft()
        in_queue[current_vertex] = False
        
        # 遍历当前顶点的所有邻居
        for neighbor, weight in graph[current_vertex]:
            # 如果通过当前顶点到达邻居的距离更短
            if distances[neighbor] > distances[current_vertex] + weight:
                distances[neighbor] = distances[current_vertex] + weight
                # 如果邻居顶点不在队列中，将其加入队列
                if not in_queue[neighbor]:
                    queue.append(neighbor)
                    in_queue[neighbor] = True
                    count[neighbor] += 1
                    # 如果一个顶点的入队次数超过n，说明存在负权环
                    if count[neighbor] > n:
                        has_negative_cycle = True
                        break
    
    return distances, has_negative_cycle

# 测试函数
def test():
    # 测试用例1: 所有边权为正的图
    graph1 = [
        [(1, 4), (2, 2)],  # 顶点0的邻居
        [(3, 2), (2, 5)],  # 顶点1的邻居
        [(3, 1)],          # 顶点2的邻居
        []                 # 顶点3的邻居
    ]
    start1 = 0
    print("Test 1 (all positive weights):")
    print("Dijkstra result:", dijkstra(graph1, start1))  # 预期输出: [0, 4, 2, 3]
    distances1, has_cycle1 = spfa(graph1, start1)
    print("SPFA result:", distances1)  # 预期输出: [0, 4, 2, 3]
    print("Has negative cycle:", has_cycle1)  # 预期输出: False
    print()
    
    # 测试用例2: 包含负权边的图
    graph2 = [
        [(1, 4), (2, 2)],  # 顶点0的邻居
        [(3, 2), (2, -5)], # 顶点1的邻居 (注意这里有负权边)
        [(3, 1)],          # 顶点2的邻居
        []                 # 顶点3的邻居
    ]
    start2 = 0
    print("Test 2 (with negative weight):")
    # Dijkstra算法在有负权边的情况下可能会给出错误结果
    print("Dijkstra result:", dijkstra(graph2, start2))  # 可能得到错误结果
    distances2, has_cycle2 = spfa(graph2, start2)
    print("SPFA result:", distances2)  # 预期输出: [0, 4, -1, 0]
    print("Has negative cycle:", has_cycle2)  # 预期输出: False
    print()
    
    # 测试用例3: 包含负权环的图
    graph3 = [
        [(1, 4)],          # 顶点0的邻居
        [(2, 2)],          # 顶点1的邻居
        [(1, -5), (3, 1)], # 顶点2的邻居 (1->2->1形成负权环)
        []                 # 顶点3的邻居
    ]
    start3 = 0
    print("Test 3 (with negative cycle):")
    # Dijkstra算法在有负权环的情况下会给出错误结果
    print("Dijkstra result:", dijkstra(graph3, start3))  # 错误结果
    distances3, has_cycle3 = spfa(graph3, start3)
    print("SPFA detected negative cycle:", has_cycle3)  # 预期输出: True

if __name__ == "__main__":
    test()
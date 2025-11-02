# LeetCode 743. Network Delay Time
# 题目链接: https://leetcode.cn/problems/network-delay-time/
# 
# 题目描述:
# 有n个网络节点，标记为1到n。给你一个列表times，表示信号经过有向边的传递时间。times[i] = (u_i, v_i, w_i)，其中u_i是源节点，v_i是目标节点，w_i是一个信号从源节点传递到目标节点的时间。
# 现在，从某个节点k发出一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回-1。
#
# 解题思路:
# 这是一个典型的单源最短路径问题，可以使用Dijkstra算法来解决，因为所有边的权值都是正数（传递时间）。
# 我们需要找到从节点k到所有其他节点的最短路径，然后取其中的最大值作为答案。
# 如果有节点无法到达，则返回-1。
#
# 时间复杂度: O(E log V)，其中E是边数，V是顶点数
# 空间复杂度: O(V + E)
# 是否为最优解: 是，Dijkstra算法是解决带权有向图中单源最短路径问题的高效算法

import heapq

def networkDelayTime(times, n, k):
    # 构建邻接表表示的图
    graph = [[] for _ in range(n + 1)]  # 节点编号从1开始
    for u, v, w in times:
        graph[u].append((v, w))
    
    # 使用Dijkstra算法计算从k到所有节点的最短路径
    # 初始化距离数组，全部设为无穷大
    distances = [float('inf')] * (n + 1)
    # 起始节点到自身的距离为0
    distances[k] = 0
    # 使用优先队列，存储(距离, 节点)，按照距离从小到大排序
    priority_queue = [(0, k)]
    # 记录已确定最短距离的节点
    visited = [False] * (n + 1)
    
    while priority_queue:
        # 取出当前距离最小的节点
        current_distance, current_node = heapq.heappop(priority_queue)
        
        # 如果该节点已经处理过，跳过
        if visited[current_node]:
            continue
        
        # 标记该节点为已处理
        visited[current_node] = True
        
        # 遍历当前节点的所有邻居
        for neighbor, weight in graph[current_node]:
            # 如果通过当前节点到达邻居的距离更短
            if distances[neighbor] > current_distance + weight:
                distances[neighbor] = current_distance + weight
                # 将更新后的邻居节点加入优先队列
                heapq.heappush(priority_queue, (distances[neighbor], neighbor))
    
    # 找到所有节点中最大的最短距离
    max_distance = 0
    for i in range(1, n + 1):
        if distances[i] == float('inf'):
            return -1  # 有节点无法到达
        max_distance = max(max_distance, distances[i])
    
    return max_distance

# 另一种实现方式：使用SPFA算法（适用于可能有负权边的情况）
def networkDelayTime_spfa(times, n, k):
    # 构建邻接表表示的图
    graph = [[] for _ in range(n + 1)]
    for u, v, w in times:
        graph[u].append((v, w))
    
    # 初始化距离数组，全部设为无穷大
    distances = [float('inf')] * (n + 1)
    # 起始节点到自身的距离为0
    distances[k] = 0
    # 使用队列存储待处理的节点
    queue = [k]
    # 记录节点是否在队列中
    in_queue = [False] * (n + 1)
    in_queue[k] = True
    
    while queue:
        # 取出队首节点
        current_node = queue.pop(0)
        in_queue[current_node] = False
        
        # 遍历当前节点的所有邻居
        for neighbor, weight in graph[current_node]:
            # 如果通过当前节点到达邻居的距离更短
            if distances[neighbor] > distances[current_node] + weight:
                distances[neighbor] = distances[current_node] + weight
                # 如果邻居节点不在队列中，将其加入队列
                if not in_queue[neighbor]:
                    queue.append(neighbor)
                    in_queue[neighbor] = True
    
    # 找到所有节点中最大的最短距离
    max_distance = 0
    for i in range(1, n + 1):
        if distances[i] == float('inf'):
            return -1  # 有节点无法到达
        max_distance = max(max_distance, distances[i])
    
    return max_distance

# 测试用例
def test():
    # 测试用例1
    times1 = [[2, 1, 1], [2, 3, 1], [3, 4, 1]]
    n1 = 4
    k1 = 2
    print(f"Test 1 (Dijkstra): {networkDelayTime(times1, n1, k1)}")  # 预期输出: 2
    print(f"Test 1 (SPFA): {networkDelayTime_spfa(times1, n1, k1)}")  # 预期输出: 2
    
    # 测试用例2
    times2 = [[1, 2, 1]]
    n2 = 2
    k2 = 1
    print(f"Test 2 (Dijkstra): {networkDelayTime(times2, n2, k2)}")  # 预期输出: 1
    print(f"Test 2 (SPFA): {networkDelayTime_spfa(times2, n2, k2)}")  # 预期输出: 1
    
    # 测试用例3 - 有节点无法到达
    times3 = [[1, 2, 1]]
    n3 = 3
    k3 = 1
    print(f"Test 3 (Dijkstra): {networkDelayTime(times3, n3, k3)}")  # 预期输出: -1
    print(f"Test 3 (SPFA): {networkDelayTime_spfa(times3, n3, k3)}")  # 预期输出: -1

if __name__ == "__main__":
    test()
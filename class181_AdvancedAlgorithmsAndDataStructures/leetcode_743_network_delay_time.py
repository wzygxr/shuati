"""
LeetCode 743. 网络延迟时间 (Network Delay Time) - Python版本
题目链接：https://leetcode.com/problems/network-delay-time/

题目描述：
有 n 个网络节点，标记为 1 到 n。给定一个列表 times，表示信号经过有向边的传递时间。
times[i] = (u, v, w)，其中 u 是源节点，v 是目标节点，w 是一个信号从源到目标的时间。
从某个节点 k 发出信号，需要多久才能使所有节点都收到信号？如果不可能使所有节点收到信号，返回 -1。

算法思路：
使用优先队列优化的Dijkstra算法求解单源最短路径问题。
Python标准库中的heapq提供了高效的堆操作。

时间复杂度：O(E log V)，其中E是边数，V是节点数
空间复杂度：O(V + E)

最优解分析：
这是Dijkstra算法的标准实现，使用二叉堆优化。
对于大多数实际场景，性能已经足够优秀。

边界场景：
1. 单个节点：直接返回0
2. 无法到达所有节点：返回-1
3. 自环边：需要正确处理
4. 负权边：Dijkstra不适用，需要使用Bellman-Ford

工程化考量：
1. 使用邻接表存储图结构，节省空间
2. 添加输入验证，确保节点编号有效
3. 处理大输入规模时的内存优化
4. 使用列表推导式和生成器表达式提高代码可读性
"""

import heapq
import sys
from typing import List

class Solution:
    def networkDelayTime(self, times: List[List[int]], n: int, k: int) -> int:
        """
        使用Dijkstra算法计算网络延迟时间
        
        Args:
            times: 边列表，每个元素为[u, v, w]
            n: 节点数量
            k: 起始节点
            
        Returns:
            int: 最大延迟时间，如果无法到达所有节点返回-1
        """
        # 输入验证
        if not times or n <= 0 or k < 1 or k > n:
            return -1
        
        # 构建邻接表
        graph = [[] for _ in range(n + 1)]
        for u, v, w in times:
            if 1 <= u <= n and 1 <= v <= n:
                graph[u].append((v, w))
        
        # 初始化距离数组
        dist = [sys.maxsize] * (n + 1)
        dist[k] = 0
        
        # 使用最小堆（优先队列）
        heap = [(0, k)]
        heapq.heapify(heap)
        
        # 记录已访问节点
        visited = [False] * (n + 1)
        
        while heap:
            current_dist, u = heapq.heappop(heap)
            
            if visited[u]:
                continue
            visited[u] = True
            
            # 遍历所有邻接边
            for v, weight in graph[u]:
                new_dist = current_dist + weight
                if new_dist < dist[v]:
                    dist[v] = new_dist
                    heapq.heappush(heap, (new_dist, v))
        
        # 找到最大延迟时间
        max_delay = 0
        for i in range(1, n + 1):
            if dist[i] == sys.maxsize:
                return -1  # 有节点不可达
            max_delay = max(max_delay, dist[i])
        
        return max_delay

    def networkDelayTimeBellmanFord(self, times: List[List[int]], n: int, k: int) -> int:
        """
        使用Bellman-Ford算法计算网络延迟时间（支持负权边）
        
        Args:
            times: 边列表，每个元素为[u, v, w]
            n: 节点数量
            k: 起始节点
            
        Returns:
            int: 最大延迟时间，如果无法到达所有节点返回-1
        """
        # 输入验证
        if not times or n <= 0 or k < 1 or k > n:
            return -1
        
        # 初始化距离数组
        dist = [sys.maxsize] * (n + 1)
        dist[k] = 0
        
        # Bellman-Ford算法：松弛所有边n-1次
        for _ in range(n - 1):
            updated = False
            for u, v, w in times:
                if 1 <= u <= n and 1 <= v <= n and dist[u] != sys.maxsize:
                    if dist[u] + w < dist[v]:
                        dist[v] = dist[u] + w
                        updated = True
            # 如果没有更新，提前结束
            if not updated:
                break
        
        # 检查负权环（可选，本题中权重为正）
        for u, v, w in times:
            if 1 <= u <= n and 1 <= v <= n and dist[u] != sys.maxsize:
                if dist[u] + w < dist[v]:
                    return -1  # 存在负权环
        
        # 找到最大延迟时间
        max_delay = 0
        for i in range(1, n + 1):
            if dist[i] == sys.maxsize:
                return -1  # 有节点不可达
            max_delay = max(max_delay, dist[i])
        
        return max_delay

def test_network_delay_time():
    """测试函数"""
    solution = Solution()
    
    # 测试用例1：标准情况
    times1 = [[2, 1, 1], [2, 3, 1], [3, 4, 1]]
    result1 = solution.networkDelayTime(times1, 4, 2)
    print(f"测试用例1结果: {result1} (期望: 2)")
    
    # 测试用例2：无法到达所有节点
    times2 = [[1, 2, 1]]
    result2 = solution.networkDelayTime(times2, 2, 2)
    print(f"测试用例2结果: {result2} (期望: -1)")
    
    # 测试用例3：单个节点
    times3 = []
    result3 = solution.networkDelayTime(times3, 1, 1)
    print(f"测试用例3结果: {result3} (期望: 0)")
    
    # 测试用例4：复杂情况
    times4 = [[1, 2, 1], [2, 3, 2], [1, 3, 4]]
    result4 = solution.networkDelayTime(times4, 3, 1)
    print(f"测试用例4结果: {result4} (期望: 3)")
    
    # 测试Bellman-Ford算法
    print("\n=== Bellman-Ford算法测试 ===")
    result1_bf = solution.networkDelayTimeBellmanFord(times1, 4, 2)
    print(f"测试用例1(Bellman-Ford): {result1_bf} (期望: 2)")
    
    # 性能测试
    print("\n=== 性能测试 ===")
    import time
    import random
    
    # 生成随机图
    random.seed(42)
    node_count = 1000
    edge_count = 5000
    times_large = []
    
    for _ in range(edge_count):
        u = random.randint(1, node_count)
        v = random.randint(1, node_count)
        w = random.randint(1, 100)
        times_large.append([u, v, w])
    
    # Dijkstra算法性能测试
    start_time = time.time()
    result_dijkstra = solution.networkDelayTime(times_large, node_count, 1)
    dijkstra_time = time.time() - start_time
    print(f"Dijkstra算法处理{node_count}个节点,{edge_count}条边时间: {dijkstra_time:.4f}秒")
    
    # Bellman-Ford算法性能测试
    start_time = time.time()
    result_bf = solution.networkDelayTimeBellmanFord(times_large, node_count, 1)
    bf_time = time.time() - start_time
    print(f"Bellman-Ford算法处理{node_count}个节点,{edge_count}条边时间: {bf_time:.4f}秒")
    
    print("所有测试用例执行完成")

if __name__ == "__main__":
    print("=== LeetCode 743. 网络延迟时间 - Python版本测试 ===")
    test_network_delay_time()
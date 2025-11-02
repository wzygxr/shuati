import heapq
from collections import defaultdict

"""
LeetCode 1129. 颜色交替的最短路径 - A*算法实现

题目链接: https://leetcode.cn/problems/shortest-path-with-alternating-colors/
题目描述: 给定一个有向图，边有红蓝两种颜色，要求找到从节点0到其他节点的最短路径，
路径中相邻边的颜色必须交替（红-蓝-红...或蓝-红-蓝...）

算法思路:
1. 状态扩展: 状态包含(节点, 最后使用的颜色)
2. 启发函数: 使用到目标节点的估计距离
3. 约束处理: 强制颜色交替的移动约束

时间复杂度: O(N*M*log(N*M))，其中N是节点数，M是边数
空间复杂度: O(N*M)
"""

# 颜色常量
RED = 0
BLUE = 1
NO_COLOR = -1

def shortestAlternatingPaths(n, redEdges, blueEdges):
    """
    颜色交替最短路径算法实现
    
    Args:
        n: 节点数量
        redEdges: 红色边数组
        blueEdges: 蓝色边数组
    
    Returns:
        从节点0到各节点的最短路径长度数组，无法到达则为-1
    """
    # 构建邻接表，包含颜色信息
    graph = defaultdict(list)
    
    # 添加红色边
    for edge in redEdges:
        graph[edge[0]].append((edge[1], RED))
    
    # 添加蓝色边
    for edge in blueEdges:
        graph[edge[0]].append((edge[1], BLUE))
    
    # 结果数组
    result = [-1] * n
    
    # 记录访问状态: visited[node][color] 表示以某种颜色到达节点的状态是否已访问
    visited = [[False, False] for _ in range(n)]
    
    # 优先队列，存储 (距离, 节点, 最后使用的颜色)
    pq = [(0, 0, NO_COLOR)]
    
    # 初始状态：从节点0开始，距离为0，没有使用颜色
    result[0] = 0
    
    while pq:
        distance, node, last_color = heapq.heappop(pq)
        
        # 如果该状态已访问，跳过
        if last_color != NO_COLOR and visited[node][last_color]:
            continue
        
        # 标记为已访问
        if last_color != NO_COLOR:
            visited[node][last_color] = True
        
        # 遍历所有邻接边
        for next_node, color in graph[node]:
            # 颜色必须交替（初始状态除外）
            if last_color == NO_COLOR or last_color != color:
                # 如果还没有找到到达next_node的路径，或者找到了更短的路径
                if result[next_node] == -1 or distance + 1 < result[next_node]:
                    result[next_node] = distance + 1
                
                # 如果该状态未访问，加入队列
                if not visited[next_node][color]:
                    heapq.heappush(pq, (distance + 1, next_node, color))
    
    return result

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    n1 = 3
    redEdges1 = [[0,1],[1,2]]
    blueEdges1 = []
    result1 = shortestAlternatingPaths(n1, redEdges1, blueEdges1)
    print(f"测试用例1结果: {result1}")  # 期望输出: [0,1,-1]
    
    # 测试用例2
    n2 = 3
    redEdges2 = [[0,1]]
    blueEdges2 = [[2,1]]
    result2 = shortestAlternatingPaths(n2, redEdges2, blueEdges2)
    print(f"测试用例2结果: {result2}")  # 期望输出: [0,1,-1]
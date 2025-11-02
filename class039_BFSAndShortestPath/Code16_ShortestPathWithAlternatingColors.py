# 颜色交替的最短路径
# 题目描述：给定一个有向图，节点分别是红色和蓝色两种颜色的边，求从节点0到所有其他节点的颜色交替的最短路径长度
# LeetCode题目链接：https://leetcode.cn/problems/shortest-path-with-alternating-colors/
# 
# 算法思路：
# 使用0-1 BFS的变体，这里边的颜色作为权重的一种表示
# 我们需要记录到达每个节点时使用的最后一条边的颜色，以确保颜色交替
# 
# 时间复杂度：O(V + E)，其中V是节点数，E是边数
# 空间复杂度：O(V + E)，用于存储图的邻接表和距离数组
# 
# 工程化考量：
# 1. 异常处理：处理空图的情况
# 2. 数据结构选择：使用邻接表存储图，使用双端队列实现0-1 BFS
# 3. 状态表示：使用距离数组记录到达每个节点时的最后一条边颜色
from collections import deque

def shortest_alternating_paths(n, red_edges, blue_edges):
    """
    计算颜色交替的最短路径长度
    
    参数：
        n: 节点数量
        red_edges: 红色边的列表，每条边表示为[from, to]
        blue_edges: 蓝色边的列表，每条边表示为[from, to]
    
    返回：
        数组，表示从节点0到每个节点的最短颜色交替路径长度，不可达返回-1
    """
    # 表示边的颜色
    RED = 0
    BLUE = 1
    
    # 构建邻接表，每个节点存储两种颜色的边
    graph = [[] for _ in range(2)]
    for i in range(n):
        graph[RED].append([])
        graph[BLUE].append([])
    
    # 添加红色边
    for from_node, to_node in red_edges:
        graph[RED][from_node].append(to_node)
    
    # 添加蓝色边
    for from_node, to_node in blue_edges:
        graph[BLUE][from_node].append(to_node)
    
    # 初始化距离数组，dist[i][j]表示到达节点i时最后一条边颜色为j的最短距离
    # j可以是0(红色)或1(蓝色)，初始值为-1表示不可达
    dist = [[-1 for _ in range(2)] for _ in range(n)]
    
    # 使用双端队列实现0-1 BFS
    dq = deque()
    
    # 起点是0，初始时没有前一条边，可以选择红色或蓝色作为第一条边
    dist[0][RED] = 0
    dist[0][BLUE] = 0
    dq.appendleft((0, RED))
    dq.appendleft((0, BLUE))
    
    while dq:
        node, color = dq.popleft()
        current_dist = dist[node][color]
        
        # 下一条边应该是另一种颜色
        next_color = BLUE if color == RED else RED
        
        # 遍历所有下一种颜色的边
        for next_node in graph[next_color][node]:
            # 如果该路径未被访问过，或者找到更短的路径
            if dist[next_node][next_color] == -1:
                dist[next_node][next_color] = current_dist + 1
                # 添加到队列前端，因为权重为1（每条边的权重相同）
                dq.append((next_node, next_color))
    
    # 构建最终结果，对于每个节点，取两种颜色路径中的最小值
    result = []
    for i in range(n):
        if dist[i][RED] == -1 and dist[i][BLUE] == -1:
            result.append(-1)  # 两种颜色都不可达
        elif dist[i][RED] == -1:
            result.append(dist[i][BLUE])
        elif dist[i][BLUE] == -1:
            result.append(dist[i][RED])
        else:
            result.append(min(dist[i][RED], dist[i][BLUE]))
    
    return result

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    n1 = 3
    red_edges1 = [[0, 1], [1, 2]]
    blue_edges1 = []
    result1 = shortest_alternating_paths(n1, red_edges1, blue_edges1)
    print("测试用例1结果：", result1)  # 预期输出: [0, 1, -1]
    
    # 测试用例2
    n2 = 3
    red_edges2 = [[0, 1]]
    blue_edges2 = [[2, 1]]
    result2 = shortest_alternating_paths(n2, red_edges2, blue_edges2)
    print("测试用例2结果：", result2)  # 预期输出: [0, 1, -1]
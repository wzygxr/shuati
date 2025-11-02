import heapq
import math

# POJ 2253 Frogger
# Freddy Frog坐在湖中间的一块石头上，突然他注意到Fiona Frog坐在另一块石头上
# 他计划去拜访她，但由于水很脏且充满了游客的防晒霜，他想避免游泳，而是通过跳跃到达
# 你的任务是计算Freddy到达Fiona那里的最小跳跃距离
# 测试链接：http://poj.org/problem?id=2253

def frogger(n, stones):
    """
    使用修改的Dijkstra算法求解最小最大跳跃距离
    时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    空间复杂度: O(V+E) 存储图和距离数组
    """
    # 构建邻接矩阵表示的图，存储两点间的欧几里得距离
    graph = [[0.0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            if i != j:
                dx = stones[i][0] - stones[j][0]
                dy = stones[i][1] - stones[j][1]
                graph[i][j] = math.sqrt(dx * dx + dy * dy)
    
    # maxJump[i] 表示从源节点0到节点i的路径上最大跳跃距离的最小值
    maxJump = [float('inf')] * n
    # 源节点到自己的跳跃距离为0
    maxJump[0] = 0
    
    # visited[i] 表示节点i是否已经确定了最小最大跳跃距离
    visited = [False] * n
    
    # 优先队列，按最大跳跃距离从小到大排序
    # 存储 (最大跳跃距离, 节点)
    heap = [(0, 0)]
    
    while heap:
        # 取出最大跳跃距离最小的节点
        jump, u = heapq.heappop(heap)
        
        # 如果已经处理过，跳过
        if visited[u]:
            continue
        # 标记为已处理
        visited[u] = True
        
        # 遍历u的所有邻居节点
        for v in range(n):
            if u != v:
                # 计算通过当前路径到达新点的最大跳跃距离
                # 是当前路径上所有跳跃距离的最大值
                newJump = max(maxJump[u], graph[u][v])
                
                # 如果新的最大跳跃距离更小，则更新
                if not visited[v] and newJump < maxJump[v]:
                    maxJump[v] = newJump
                    heapq.heappush(heap, (maxJump[v], v))
    
    return maxJump[1]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 2
    stones1 = [[0,0],[3,4]]
    print("测试用例1结果: {:.3f}".format(frogger(n1, stones1)))  # 期望输出: 5.000
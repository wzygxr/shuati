import heapq
from collections import defaultdict

# POJ 1797 Heavy Transportation
# 你的任务是找出从交叉点1（Hugo的位置）到交叉点n（客户的位置）可以运输的最大重量
# 测试链接：http://poj.org/problem?id=1797

def heavy_transportation(n, edges):
    """
    使用修改的Dijkstra算法求解最大载重路径
    时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    空间复杂度: O(V+E) 存储图和距离数组
    """
    # 构建邻接表表示的图
    graph = defaultdict(list)
    
    # 添加边到图中
    for u, v, w in edges:
        # 无向图，需要添加两条边
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    # maxWeight[i] 表示从源节点1到节点i的最大载重量
    maxWeight = [0] * (n + 1)
    # 源节点到自己的载重量为无穷大
    maxWeight[1] = float('inf')
    
    # visited[i] 表示节点i是否已经确定了最大载重量
    visited = [False] * (n + 1)
    
    # 优先队列，按载重量从大到小排序
    # 存储 (-载重量, 节点) 元组，因为Python的heapq是最小堆
    heap = [(-float('inf'), 1)]
    
    while heap:
        # 取出载重量最大的节点
        neg_weight, u = heapq.heappop(heap)
        weight = -neg_weight
        
        # 如果已经处理过，跳过
        if visited[u]:
            continue
        # 标记为已处理
        visited[u] = True
        
        # 遍历u的所有邻居节点
        for v, w in graph[u]:
            # 计算通过当前路径到达新点的最大载重量
            # 是当前路径上所有边载重量的最小值
            newWeight = min(maxWeight[u], w)
            
            # 如果新的载重量更大，则更新
            if not visited[v] and newWeight > maxWeight[v]:
                maxWeight[v] = newWeight
                heapq.heappush(heap, (-maxWeight[v], v))
    
    return int(maxWeight[n])

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 3
    edges1 = [[1,2,3],[1,3,2],[2,3,4]]
    print("测试用例1结果:", heavy_transportation(n1, edges1))  # 期望输出: 3
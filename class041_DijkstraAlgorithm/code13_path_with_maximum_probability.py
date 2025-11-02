import heapq
from collections import defaultdict

# 路径中最大概率问题
# 给你一个由 n 个节点（下标从 0 开始）组成的无向加权图，记为 G
# 该图由边的列表表示，其中 edges[i] = [a, b] 表示连接节点 a 和 b 的无向边，且该边遍历成功的概率为 succProb[i]
# 指定两个节点分别作为起点 start 和终点 end，要求找出从起点到终点成功的最大概率
# 如果不存在从 start 到 end 的路径，返回 0
# 测试链接：https://leetcode.cn/problems/path-with-maximum-probability

def max_probability(n, edges, succProb, start, end):
    """
    使用Dijkstra算法求解最大概率路径
    时间复杂度: O((V+E)logV) 其中V是节点数，E是边数
    空间复杂度: O(V+E) 存储图和距离数组
    """
    # 构建邻接表表示的图
    graph = defaultdict(list)
    
    # 添加边到图中
    for i in range(len(edges)):
        u, v = edges[i]
        prob = succProb[i]
        # 无向图，需要添加两条边
        graph[u].append((v, prob))
        graph[v].append((u, prob))
    
    # probability[i] 表示从起点start到节点i的最大概率
    probability = [0.0] * n
    # 起点到自己的概率为1
    probability[start] = 1.0
    
    # visited[i] 表示节点i是否已经确定了最大概率
    visited = [False] * n
    
    # 优先队列，按概率从大到小排序
    # 存储 (-概率, 节点) 元组，因为Python的heapq是最小堆
    heap = [(-1.0, start)]
    
    while heap:
        # 取出概率最大的节点
        neg_prob, u = heapq.heappop(heap)
        prob = -neg_prob
        
        # 如果已经处理过，跳过
        if visited[u]:
            continue
        # 标记为已处理
        visited[u] = True
        
        # 遍历u的所有邻居节点
        for v, edgeProb in graph[u]:
            # 如果邻居节点未访问且通过u到达v的概率更大，则更新
            if not visited[v] and probability[u] * edgeProb > probability[v]:
                probability[v] = probability[u] * edgeProb
                heapq.heappush(heap, (-probability[v], v))
    
    return probability[end]

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    n1 = 3
    edges1 = [[0,1],[1,2],[0,2]]
    succProb1 = [0.5, 0.5, 0.2]
    start1 = 0
    end1 = 2
    print("测试用例1结果:", max_probability(n1, edges1, succProb1, start1, end1))  # 期望输出: 0.25
    
    # 测试用例2
    n2 = 3
    edges2 = [[0,1],[1,2],[0,2]]
    succProb2 = [0.5, 0.5, 0.3]
    start2 = 0
    end2 = 2
    print("测试用例2结果:", max_probability(n2, edges2, succProb2, start2, end2))  # 期望输出: 0.3
    
    # 测试用例3
    n3 = 3
    edges3 = [[0,1]]
    succProb3 = [0.5]
    start3 = 0
    end3 = 2
    print("测试用例3结果:", max_probability(n3, edges3, succProb3, start3, end3))  # 期望输出: 0.0
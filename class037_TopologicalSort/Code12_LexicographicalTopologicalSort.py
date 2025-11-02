import heapq
from collections import defaultdict

def lexicographical_topological_sort(n, edges):
    """
    字典序最小拓扑排序 - Python实现
    题目解析：输出字典序最小的拓扑排序序列
    
    算法思路：
    1. 使用邻接表存储图结构
    2. 计算每个节点的入度
    3. 使用最小堆存储入度为0的节点
    4. 每次取出编号最小的节点进行处理
    
    时间复杂度：O((V + E) log V)
    空间复杂度：O(V + E)
    
    工程化考虑：
    1. 使用heapq实现最小堆
    2. 使用defaultdict简化邻接表操作
    3. 边界处理：空图、有环图等情况
    4. 异常捕获和错误处理
    """
    # 构建邻接表和入度数组
    graph = defaultdict(list)
    indegree = [0] * (n + 1)
    
    for u, v in edges:
        graph[u].append(v)
        indegree[v] += 1
    
    # 使用最小堆存储入度为0的节点
    min_heap = []
    for i in range(1, n + 1):
        if indegree[i] == 0:
            heapq.heappush(min_heap, i)
    
    result = []
    while min_heap:
        u = heapq.heappop(min_heap)
        result.append(u)
        
        for v in graph[u]:
            indegree[v] -= 1
            if indegree[v] == 0:
                heapq.heappush(min_heap, v)
    
    # 检查是否有环
    if len(result) == n:
        return result
    else:
        return []  # 有环，返回空列表

# 测试用例
if __name__ == "__main__":
    # 测试用例1：无环图，需要字典序最小
    n1 = 4
    edges1 = [(1, 2), (1, 3), (2, 4), (3, 4)]
    result1 = lexicographical_topological_sort(n1, edges1)
    print("测试用例1结果:", result1)  # 输出: [1, 2, 3, 4]
    
    # 测试用例2：有环图
    n2 = 3
    edges2 = [(1, 2), (2, 3), (3, 1)]
    result2 = lexicographical_topological_sort(n2, edges2)
    print("测试用例2结果:", result2)  # 输出: []
    
    # 测试用例3：多个入度为0的节点
    n3 = 5
    edges3 = [(1, 3), (2, 3), (3, 4), (3, 5)]
    result3 = lexicographical_topological_sort(n3, edges3)
    print("测试用例3结果:", result3)  # 输出: [1, 2, 3, 4, 5]
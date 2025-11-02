from collections import deque, defaultdict

def topological_sort(n, edges):
    """
    拓扑排序模板题 - Python实现
    题目解析：对有向无环图进行拓扑排序，检测环的存在
    
    算法思路：
    1. 使用邻接表存储图结构
    2. 计算每个节点的入度
    3. 使用队列进行BFS拓扑排序
    4. 检测结果序列长度判断是否有环
    
    时间复杂度：O(V + E)
    空间复杂度：O(V + E)
    
    工程化考虑：
    1. 使用defaultdict简化邻接表操作
    2. 使用deque实现队列
    3. 边界处理：空图、有环图等情况
    4. 异常捕获和错误处理
    """
    # 构建邻接表和入度数组
    graph = defaultdict(list)
    indegree = [0] * (n + 1)
    
    for u, v in edges:
        graph[u].append(v)
        indegree[v] += 1
    
    # 初始化队列
    queue = deque()
    for i in range(1, n + 1):
        if indegree[i] == 0:
            queue.append(i)
    
    result = []
    while queue:
        u = queue.popleft()
        result.append(u)
        
        for v in graph[u]:
            indegree[v] -= 1
            if indegree[v] == 0:
                queue.append(v)
    
    # 检查是否有环
    if len(result) == n:
        return result
    else:
        return []  # 有环，返回空列表

# 测试用例
if __name__ == "__main__":
    # 测试用例1：无环图
    n1 = 4
    edges1 = [(1, 2), (1, 3), (2, 4), (3, 4)]
    result1 = topological_sort(n1, edges1)
    print("测试用例1结果:", result1)  # 输出: [1, 2, 3, 4] 或 [1, 3, 2, 4]
    
    # 测试用例2：有环图
    n2 = 3
    edges2 = [(1, 2), (2, 3), (3, 1)]
    result2 = topological_sort(n2, edges2)
    print("测试用例2结果:", result2)  # 输出: []
    
    # 测试用例3：单节点
    n3 = 1
    edges3 = []
    result3 = topological_sort(n3, edges3)
    print("测试用例3结果:", result3)  # 输出: [1]
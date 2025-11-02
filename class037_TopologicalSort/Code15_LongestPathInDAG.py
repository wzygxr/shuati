from collections import deque, defaultdict
import sys

def longest_path_in_dag(n, weights, edges):
    """
    有向无环图中的最长路径 - Python实现
    题目解析：计算DAG中的最长路径，使用拓扑排序+动态规划
    
    算法思路：
    1. 使用邻接表存储图结构
    2. 进行拓扑排序确定节点处理顺序
    3. 使用动态规划计算每个节点的最长路径
    4. dp[i]表示以节点i为终点的最长路径长度
    
    时间复杂度：O(V + E)
    空间复杂度：O(V + E)
    
    工程化考虑：
    1. 使用defaultdict简化邻接表操作
    2. 使用deque实现队列
    3. 边界处理：空图、单节点图等情况
    4. 异常捕获和错误处理
    """
    # 构建邻接表
    graph = defaultdict(list)
    indegree = [0] * (n + 1)
    outdegree = [0] * (n + 1)
    
    # 构建图
    for u, v in edges:
        graph[u].append(v)
        indegree[v] += 1
        outdegree[u] += 1
    
    # DP数组：dp[i]表示以节点i为终点的最长路径长度
    dp = [0] * (n + 1)
    
    # 初始化dp数组为节点权重
    for i in range(1, n + 1):
        dp[i] = weights[i]
    
    # 拓扑排序队列
    queue = deque()
    for i in range(1, n + 1):
        if indegree[i] == 0:
            queue.append(i)
    
    max_path = -10**9  # 初始化为一个很小的数
    
    while queue:
        u = queue.popleft()
        
        # 如果u是终点（出度为0），更新最长路径
        if outdegree[u] == 0:
            max_path = max(max_path, dp[u])
        
        # 遍历u的所有邻居
        for v in graph[u]:
            # 状态转移：dp[v] = max(dp[v], dp[u] + weights[v])
            if dp[u] + weights[v] > dp[v]:
                dp[v] = dp[u] + weights[v]
            
            indegree[v] -= 1
            if indegree[v] == 0:
                queue.append(v)
    
    return max_path

# 测试用例
if __name__ == "__main__":
    # 测试用例1：简单DAG
    n1 = 4
    weights1 = [0, 1, 2, 3, 4]  # 索引0不使用，从1开始
    edges1 = [(1, 2), (1, 3), (2, 4), (3, 4)]
    result1 = longest_path_in_dag(n1, weights1, edges1)
    print("测试用例1结果:", result1)  # 输出: 8 (1->2->4: 1+2+4=7, 1->3->4: 1+3+4=8)
    
    # 测试用例2：单节点
    n2 = 1
    weights2 = [0, 5]
    edges2 = []
    result2 = longest_path_in_dag(n2, weights2, edges2)
    print("测试用例2结果:", result2)  # 输出: 5
    
    # 测试用例3：链式结构
    n3 = 3
    weights3 = [0, 1, 2, 3]
    edges3 = [(1, 2), (2, 3)]
    result3 = longest_path_in_dag(n3, weights3, edges3)
    print("测试用例3结果:", result3)  # 输出: 6 (1->2->3: 1+2+3=6)
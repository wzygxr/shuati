from collections import deque

def maximum_invitations(favorite):
    """
    参加会议的最多员工数 - 基环树问题 - Python实现
    题目解析：处理基环树，分类讨论环和链的情况
    
    算法思路：
    1. 使用拓扑排序找出所有不在环上的节点
    2. 计算链的深度
    3. 分类处理不同大小的环
    4. 取所有情况的最大值
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    
    工程化考虑：
    1. 图结构分析：识别基环树特性
    2. 分类讨论：处理不同大小的环
    3. 链长度计算：使用拓扑排序
    4. 边界处理：单节点、自环等情况
    """
    n = len(favorite)
    
    # 计算入度
    indegree = [0] * n
    for i in range(n):
        indegree[favorite[i]] += 1
    
    # 拓扑排序：找出所有不在环上的节点
    visited = [False] * n
    depth = [0] * n  # 链的深度
    queue = deque()
    
    for i in range(n):
        if indegree[i] == 0:
            queue.append(i)
            visited[i] = True
    
    # 计算链的深度
    while queue:
        curr = queue.popleft()
        nxt = favorite[curr]
        depth[nxt] = max(depth[nxt], depth[curr] + 1)
        
        indegree[nxt] -= 1
        if indegree[nxt] == 0:
            queue.append(nxt)
            visited[nxt] = True
    
    max_circle = 0  # 最大环的大小
    sum_pairs = 0   # 所有大小为2的环加上链的长度之和
    
    # 处理环
    for i in range(n):
        if not visited[i]:
            # 找到环
            circle_size = 0
            curr = i
            
            while not visited[curr]:
                visited[curr] = True
                circle_size += 1
                curr = favorite[curr]
            
            if circle_size == 2:
                # 大小为2的环：可以加上两条链的长度
                sum_pairs += 2 + depth[i] + depth[favorite[i]]
            else:
                # 大小大于2的环：只能选择环的大小
                max_circle = max(max_circle, circle_size)
    
    return max(max_circle, sum_pairs)

# 测试用例
if __name__ == "__main__":
    # 测试用例1：大小为2的环加上链
    favorite1 = [2, 2, 1, 2]
    print("测试用例1:", maximum_invitations(favorite1))  # 输出: 3
    
    # 测试用例2：大小为3的环
    favorite2 = [1, 2, 0]
    print("测试用例2:", maximum_invitations(favorite2))  # 输出: 3
    
    # 测试用例3：多个环
    favorite3 = [3, 0, 1, 4, 1]
    print("测试用例3:", maximum_invitations(favorite3))  # 输出: 4
    
    # 测试用例4：自环
    favorite4 = [0]
    print("测试用例4:", maximum_invitations(favorite4))  # 输出: 1
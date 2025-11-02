# LeetCode 1192. Critical Connections in a Network
# 题目链接: https://leetcode.cn/problems/critical-connections-in-a-network/
# 
# 题目描述:
# 力扣数据中心有n台服务器，编号为0到n-1。服务器之间形成一个无向拓扑图，其中connections[i] = [a, b]表示服务器a和b之间的连接。
# 连接是无向的，也就是说connections[i] = [a, b]和connections[i] = [b, a]表示的是同一个连接。
# 请你找出所有的关键连接，即删除这些连接后，服务器之间的连通性会受到影响的连接。请以任意顺序返回这些连接。
#
# 解题思路:
# 这是一个典型的寻找无向图中桥（Bridge）的问题。桥是指在图中，如果删除该边后，图会分成两个或更多的连通分量。
# 我们可以使用Tarjan算法来高效地找出所有的桥。Tarjan算法基于深度优先搜索（DFS），通过记录每个节点的发现时间（discovery time）和能够回溯到的最早的节点（low value）来判断一条边是否为桥。
#
# 时间复杂度: O(V + E)，其中V是顶点数，E是边数
# 空间复杂度: O(V + E)
# 是否为最优解: 是，Tarjan算法是寻找图中桥的线性时间算法

from collections import defaultdict

def criticalConnections(n, connections):
    # 构建邻接表表示的图
    graph = defaultdict(list)
    for u, v in connections:
        graph[u].append(v)
        graph[v].append(u)
    
    # 存储结果的列表
    result = []
    
    # 初始化发现时间和low值数组
    disc = [float('inf')] * n  # 节点的发现时间
    low = [float('inf')] * n   # 节点能够回溯到的最早的节点的发现时间
    time = [0]  # 使用列表来存储时间，以便在递归中修改
    
    def dfs(node, parent):
        # 设置当前节点的发现时间和low值
        disc[node] = time[0]
        low[node] = time[0]
        time[0] += 1
        
        # 遍历当前节点的所有邻居
        for neighbor in graph[node]:
            # 如果邻居是父节点，跳过
            if neighbor == parent:
                continue
            
            # 如果邻居还没有被访问过
            if disc[neighbor] == float('inf'):
                dfs(neighbor, node)
                
                # 更新当前节点的low值
                low[node] = min(low[node], low[neighbor])
                
                # 检查边(node, neighbor)是否为桥
                if low[neighbor] > disc[node]:
                    result.append([node, neighbor])
            else:
                # 如果邻居已经被访问过，且不是父节点，说明找到一条回边
                # 更新当前节点的low值
                low[node] = min(low[node], disc[neighbor])
    
    # 从节点0开始DFS（任意未访问的节点都可以作为起点）
    for i in range(n):
        if disc[i] == float('inf'):
            dfs(i, -1)
    
    return result

# 测试用例
def test():
    # 测试用例1
    n1 = 4
    connections1 = [[0, 1], [1, 2], [2, 0], [1, 3]]
    print(f"Test 1: {criticalConnections(n1, connections1)}")
    # 预期输出: [[1, 3]]
    
    # 测试用例2
    n2 = 2
    connections2 = [[0, 1]]
    print(f"Test 2: {criticalConnections(n2, connections2)}")
    # 预期输出: [[0, 1]]
    
    # 测试用例3 - 多个桥
    n3 = 5
    connections3 = [[0, 1], [1, 2], [2, 3], [3, 4], [2, 4]]
    print(f"Test 3: {criticalConnections(n3, connections3)}")
    # 预期输出: [[0, 1], [1, 2]]

if __name__ == "__main__":
    test()
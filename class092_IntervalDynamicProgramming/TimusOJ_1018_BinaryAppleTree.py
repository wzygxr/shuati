# TimusOJ 1018 Binary Apple Tree
# 题目来源：https://acm.timus.ru/problem.aspx?space=1&num=1018
# 题目大意：给定一棵二叉苹果树，树的节点代表分叉点，边代表树枝，每条边上有一定数量的苹果。
# 现在要移除一些树枝，使得最终剩下的树枝数量恰好为Q条，同时保留的苹果数量最多。
# 树的根节点是1号节点。
#
# 解题思路：
# 1. 这是一个树形动态规划问题，但也可以用区间DP的思想来解决
# 2. dp[i][j]表示以节点i为根的子树中保留j条边能获得的最大苹果数
# 3. 对于每个节点，考虑其左右子树的分配情况
# 4. 状态转移：枚举左子树保留的边数k，右子树保留的边数为j-1-k
# 5. dp[i][j] = max(dp[left][k] + dp[right][j-1-k] + apple[left_edge] + apple[right_edge])
#
# 时间复杂度：O(n^3) - 三层循环：节点数、保留边数、左子树边数分配
# 空间复杂度：O(n^2) - dp数组占用空间
#
# 工程化考虑：
# 1. 输入验证：检查输入是否合法
# 2. 树结构处理：正确构建树的结构
# 3. 边界处理：处理节点数较少的特殊情况
# 4. 异常处理：对于不合法输入给出适当提示

import sys
from collections import defaultdict

def solve(n, q, edges):
    """
    主函数：解决二叉苹果树问题
    时间复杂度: O(n^3) - 三层循环：节点数、保留边数、左子树边数分配
    空间复杂度: O(n^2) - dp数组占用空间
    """
    if n <= 1 or q <= 0:
        return 0
    
    # 构建邻接表表示的树
    graph = defaultdict(list)
    for edge in edges:
        graph[edge[0]].append((edge[1], edge[2]))
        graph[edge[1]].append((edge[0], edge[2]))  # 无向图
    
    # dp[i][j]表示以节点i为根的子树中保留j条边能获得的最大苹果数
    dp = [[-1] * (q + 1) for _ in range(n + 1)]
    
    # 从根节点开始进行树形DP
    return dfs(1, -1, q, graph, dp)

def dfs(node, parent, edges_count, graph, dp):
    """深度优先搜索进行树形DP"""
    # 如果已经计算过，直接返回结果
    if dp[node][edges_count] != -1:
        return dp[node][edges_count]
    
    # 边界条件
    if edges_count == 0:
        dp[node][edges_count] = 0
        return 0
    
    # 获取子节点
    children = []
    for neighbor, apple in graph[node]:
        if neighbor != parent:
            children.append((neighbor, apple))
    
    # 如果没有子节点
    if not children:
        dp[node][edges_count] = 0
        return 0
    
    # 只有一个子节点的情况
    if len(children) == 1:
        child_node, child_apple = children[0]
        result = dfs(child_node, node, edges_count - 1, graph, dp) + child_apple
        dp[node][edges_count] = result
        return result
    
    # 有两个子节点的情况
    left_node, left_apple = children[0]
    right_node, right_apple = children[1]
    
    max_apples = 0
    # 枚举左子树保留的边数
    for left_edges in range(edges_count):
        right_edges = edges_count - 1 - left_edges
        if right_edges >= 0:
            left_apples = dfs(left_node, node, left_edges, graph, dp)
            right_apples = dfs(right_node, node, right_edges, graph, dp)
            total_apples = left_apples + right_apples + left_apple + right_apple
            max_apples = max(max_apples, total_apples)
    
    dp[node][edges_count] = max_apples
    return max_apples

if __name__ == "__main__":
    # 读取输入
    parts = input().split()
    n = int(parts[0])
    q = int(parts[1])
    
    # 读取边信息
    edges = []
    for _ in range(n - 1):
        parts = input().split()
        from_node = int(parts[0])
        to_node = int(parts[1])
        apple = int(parts[2])
        edges.append((from_node, to_node, apple))
    
    result = solve(n, q, edges)
    print(result)
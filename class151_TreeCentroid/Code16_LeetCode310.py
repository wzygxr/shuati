# LeetCode 310. 最小高度树
# 题目来源：LeetCode 310 https://leetcode.cn/problems/minimum-height-trees/
# 题目描述：对于一个具有 n 个节点的树，给定 n-1 条边，找到所有可能的最小高度树的根节点。
# 算法思想：最小高度树的根节点就是树的重心
# 解题思路：
# 1. 树的高度定义为从根节点到最远叶子节点的边数
# 2. 最小高度树的根节点就是树的重心，即删除该节点后最大连通分量最小的节点
# 3. 通过一次DFS计算每个节点的最大子树大小，找到具有最小最大子树大小的所有节点
# 时间复杂度：O(n)，只需要一次DFS遍历
# 空间复杂度：O(n)，用于存储树结构和递归栈

import sys
from collections import defaultdict

def findMinHeightTrees(n, edges):
    """
    寻找最小高度树的根节点（即树的重心）
    :param n: 节点数量
    :param edges: 边的数组，每条边用两个节点表示
    :return: 所有最小高度树的根节点列表
    """
    # 边界情况处理
    if n == 1:
        # 只有一个节点时，该节点就是最小高度树的根
        return [0]
    if n == 2:
        # 只有两个节点时，两个节点都是最小高度树的根
        return [0, 1]
    
    # 构建邻接表
    graph = defaultdict(list)
    for u, v in edges:
        graph[u].append(v)
        graph[v].append(u)
    
    # size[i]表示以节点i为根的子树的节点数量
    size = [0] * n
    
    # maxSub[i]表示以节点i为根时的最大子树大小
    maxSub = [0] * n
    
    # 计算子树大小和最大子树大小
    # u: 当前访问的节点
    # parent: u的父节点，避免回到父节点形成环
    def dfs(u, parent):
        # 初始化当前节点u的子树大小为1（包含节点u本身）
        size[u] = 1
        # 初始化当前节点u的最大子树大小为0
        maxSub[u] = 0
        
        # 遍历节点u的所有邻接节点
        for v in graph[u]:
            # 如果不是父节点，则继续DFS
            if v != parent:
                # 递归访问子节点v，父节点为u
                dfs(v, u)
                
                # 将子节点v的子树大小加到当前节点u的子树大小中
                size[u] += size[v]
                
                # 更新以u为根时的最大子树大小
                maxSub[u] = max(maxSub[u], size[v])
        
        # 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
        maxSub[u] = max(maxSub[u], n - size[u])
    
    # 第一次DFS计算子树信息
    # 从节点0开始DFS，父节点为-1（表示没有父节点）
    dfs(0, -1)
    
    # 找到最小的最大子树大小
    # 遍历所有节点，找到最小的maxSub值
    minMaxSub = min(maxSub)
    
    # 收集所有重心（最小高度树的根）
    # 这些节点具有最小的最大子树大小
    result = []
    for i in range(n):
        if maxSub[i] == minMaxSub:
            result.append(i)
    
    return result

# 测试方法
def test():
    # 测试用例1
    n1 = 4
    edges1 = [[1, 0], [1, 2], [1, 3]]
    result1 = findMinHeightTrees(n1, edges1)
    print("测试用例1结果:", result1)  # 期望输出: [1]
    
    # 测试用例2
    n2 = 6
    edges2 = [[3, 0], [3, 1], [3, 2], [3, 4], [5, 4]]
    result2 = findMinHeightTrees(n2, edges2)
    print("测试用例2结果:", result2)  # 期望输出: [3, 4]
    
    # 边界测试用例
    n3 = 1
    edges3 = []
    result3 = findMinHeightTrees(n3, edges3)
    print("测试用例3结果:", result3)  # 期望输出: [0]

if __name__ == "__main__":
    test()
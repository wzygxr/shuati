import sys
from sys import stdin

"""
洛谷U81904 - 树的直径
题目描述：给定一棵树，树中每条边都有一个权值，树中两点之间的距离定义为连接两点的路径边权之和。
树中最远的两个节点之间的距离被称为树的直径，连接这两点的路径被称为树的最长链。
现在让你求出树的最长链的距离。

输入格式：
- 第一行为一个正整数n，表示这颗树有n个节点
- 接下来的n−1行，每行三个正整数u,v,w，表示u,v（u,v<=n）有一条权值为w的边相连

输出格式：输入仅一行，表示树的最长链的距离

解题思路：使用树形DP法求解，因为边权可能为负
对于每个节点，维护两个值：
1. 该节点到其子树中的最长路径长度
2. 该节点到其子树中的次长路径长度
那么，经过该节点的最长路径就是这两个值的和。遍历所有节点，取最大值即为树的直径

时间复杂度：O(n)，空间复杂度：O(n)
"""

def main():
    sys.setrecursionlimit(1 << 25)  # 增加递归深度限制
    
    n = int(stdin.readline())
    
    # 初始化邻接表
    graph = [[] for _ in range(n + 1)]  # 节点编号从1开始
    
    # 读取边
    for _ in range(n - 1):
        u, v, w = map(int, stdin.readline().split())
        # 无向树，添加双向边
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    max_diameter = [0]  # 使用列表包装，以便在递归中修改
    
    def tree_dp(current, parent):
        """
        树形DP函数，计算每个节点的最长路径和次长路径，并更新全局最大直径
        
        Args:
            current: 当前节点
            parent: 当前节点的父节点，避免回到父节点
        
        Returns:
            int: 当前节点到其子树中的最长路径长度
        """
        max1 = 0  # 最长路径
        max2 = 0  # 次长路径
        
        for next_node, weight in graph[current]:
            # 避免回到父节点
            if next_node == parent:
                continue
            
            # 递归计算子节点的最长路径
            depth = tree_dp(next_node, current) + weight
            
            # 更新最长和次长路径
            if depth > max1:
                max2 = max1
                max1 = depth
            elif depth > max2:
                max2 = depth
        
        # 更新全局最大直径
        max_diameter[0] = max(max_diameter[0], max1 + max2)
        
        # 返回当前节点的最长路径
        return max1
    
    # 从任意节点开始树形DP，这里选择1号节点
    tree_dp(1, -1)
    
    # 输出树的直径
    print(max_diameter[0])

if __name__ == "__main__":
    main()
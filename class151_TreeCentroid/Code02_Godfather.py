# 教父 (Godfather)
# 题目来源: POJ 3107 http://poj.org/problem?id=3107
# 问题描述: 给定一棵n个节点的树，找到树的所有重心
# 树的重心定义: 找到一个点，其所有的子树中最大的子树节点数不超过总节点数的一半
# 算法思路:
# 1. 使用DFS遍历树，计算每个节点的子树大小
# 2. 对于每个节点，计算删除该节点后形成的各个连通块的大小
# 3. 找到满足条件（最大连通块大小不超过n/2）的所有节点，即为重心
# 时间复杂度: O(n)，每个节点访问一次
# 空间复杂度: O(n)，用于存储邻接表和递归栈

import sys
from collections import defaultdict

def main():
    # 读取节点数量
    n = int(sys.stdin.readline())
    
    # 初始化邻接表
    adj = defaultdict(list)
    
    # 读取n-1条边
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        # 添加无向边
        adj[u].append(v)
        adj[v].append(u)
    
    # size[i]表示以节点i为根的子树的节点数量
    size = [0] * (n + 1)
    
    # maxsub[i]表示以节点i为根时的最大子树大小
    maxsub = [0] * (n + 1)
    
    # 深度优先搜索函数，用于计算子树大小和最大子树大小
    # u: 当前访问的节点
    # f: u的父节点，避免回到父节点形成环
    def dfs(u, f):
        # 初始化当前节点u的子树大小为1（包含节点u本身）
        size[u] = 1
        # 初始化当前节点u的最大子树大小为0
        maxsub[u] = 0
        
        # 遍历u的所有邻接节点
        for v in adj[u]:
            # 如果不是父节点，则继续DFS
            if v != f:
                # 递归访问子节点v
                dfs(v, u)
                
                # 将子节点v的子树大小加到当前节点u的子树大小中
                size[u] += size[v]
                
                # 更新以u为根时的最大子树大小
                maxsub[u] = max(maxsub[u], size[v])
        
        # 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
        maxsub[u] = max(maxsub[u], n - size[u])
    
    # 从节点1开始DFS，父节点为0（表示没有父节点）
    dfs(1, 0)
    
    # 查找所有重心
    # 根据树的重心性质，树最多有两个重心，且这两个重心相邻
    centers = []
    for i in range(1, n + 1):
        # 如果节点i的最大子树大小不超过总节点数的一半，则i是重心
        if maxsub[i] <= n // 2:
            centers.append(i)
    
    # 按编号顺序排序
    centers.sort()
    
    # 输出结果
    print(' '.join(map(str, centers)))

if __name__ == "__main__":
    main()
# Balancing Act (平衡行为)
# 题目来源: POJ 1655 http://poj.org/problem?id=1655
# 问题描述: 给定一棵n个节点的树，找到树的重心
# 树的重心定义: 找到一个点，其所有的子树中最大的子树节点数最少
# 算法思路: 
# 1. 使用DFS遍历树，计算每个节点的子树大小
# 2. 对于每个节点，计算删除该节点后形成的各个连通块的大小
# 3. 找到使最大连通块大小最小的节点，即为重心
# 时间复杂度: O(n)，每个节点访问一次
# 空间复杂度: O(n)，用于存储邻接表和递归栈

import sys
from collections import defaultdict

def main():
    # 读取测试用例数量
    t = int(sys.stdin.readline())
    
    # 处理每个测试用例
    for _ in range(t):
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
        
        # 记录找到的重心节点编号和最大子树的节点数
        center = 1
        best = n
        
        # 深度优先搜索函数，用于计算子树大小和找到重心
        # u: 当前访问的节点
        # f: u的父节点，避免回到父节点形成环
        def dfs(u, f):
            nonlocal center, best
            
            # 初始化当前节点u的子树大小为1（包含节点u本身）
            size[u] = 1
            
            # 以当前节点u做根节点，最大的子树有多少节点
            maxsub = 0
            
            # 遍历u的所有邻接节点
            for v in adj[u]:
                # 如果不是父节点，则继续DFS
                if v != f:
                    dfs(v, u)
                    # 将子节点v的子树大小加到当前节点u的子树大小中
                    size[u] += size[v]
                    # 更新以u为根时的最大子树大小
                    maxsub = max(maxsub, size[v])
            
            # 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
            maxsub = max(maxsub, n - size[u])
            
            # 更新重心：如果当前节点的最大子树更小，或者子树大小相同但节点编号更小
            # 题目要求找到编号最小的重心
            if maxsub < best or (maxsub == best and u < center):
                best = maxsub
                center = u
        
        # 从节点1开始DFS，父节点为0（表示没有父节点）
        dfs(1, 0)
        
        # 输出重心节点编号和最大子树的节点数
        print(center, best)

if __name__ == "__main__":
    main()
# 删增边使其重心唯一 (Link Cut Centroids)
# 题目来源: Codeforces 1406C https://codeforces.com/problemset/problem/1406/C
# 题目来源: 洛谷 CF1406C https://www.luogu.com.cn/problem/CF1406C
# 问题描述: 给定一棵n个节点的树，希望调整树的结构使得重心是唯一的节点
# 调整方式：先删除一条边、然后增加一条边
# 算法思路:
# 1. 首先找到树的所有重心（最多两个）
# 2. 如果只有一个重心，需要删掉连接重心的任意一条边，再把这条边加上
# 3. 如果有两个重心，调整的方式是先删除一条边、然后增加一条边，使重心是唯一的
#    具体做法：找到其中一个重心的最大子树中的一个叶子节点，将该叶子节点连接到另一个重心上
# 时间复杂度: O(n)，每个节点访问常数次
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
        
        # maxsub[i]表示以节点i为根时的最大子树大小
        maxsub = [0] * (n + 1)
        
        # 收集所有的重心，最多两个
        centers = [0, 0]
        
        # 最大子树上的叶节点
        leaf = 0
        
        # 叶节点的父亲节点
        leafFather = 0
        
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
        
        # 随意找一个叶节点和该叶节点的父亲节点
        # 哪一组都可以
        # u: 当前访问的节点
        # f: u的父节点
        def find(u, f):
            nonlocal leaf, leafFather
            
            # 遍历u的所有邻接节点
            for v in adj[u]:
                # 如果当前边指向的节点不是父节点
                if v != f:
                    # 递归查找子节点
                    find(v, u)
                    return
            
            # 如果没有子节点（即为叶节点），记录该叶节点和其父节点
            leaf = u
            leafFather = f
        
        # 返回重心的数量
        def centerCnt():
            m = 0
            # 查找所有重心
            # 根据树的重心性质，树最多有两个重心，且这两个重心相邻
            for i in range(1, n + 1):
                # 如果节点i的最大子树大小不超过总节点数的一半，则i是重心
                if maxsub[i] <= n // 2:
                    centers[m] = i
                    m += 1
            return m
        
        # 从节点1开始DFS，父节点为0（表示没有父节点）
        dfs(1, 0)
        
        if centerCnt() == 1:
            # 如果只有一个重心
            # 需要删掉连接重心的任意一条边，再把这条边加上
            # 这里选择重心连接的第一条边
            first_neighbor = adj[centers[0]][0]
            print(centers[0], first_neighbor)
            print(centers[0], first_neighbor)
        else:
            # 如果有两个重心（centers[0]和centers[1]）
            # 调整的方式是先删除一条边、然后增加一条边，使重心是唯一的
            # 具体做法：找到其中一个重心(centers[1])的最大子树中的一个叶子节点，
            # 将该叶子节点连接到另一个重心(centers[0])上
            
            # 在centers[1]的最大子树中找一个叶节点和其父节点
            find(centers[1], centers[0])
            
            # 删除leafFather和leaf之间的边，增加centers[0]和leaf之间的边
            print(leafFather, leaf)
            print(centers[0], leaf)

if __name__ == "__main__":
    main()
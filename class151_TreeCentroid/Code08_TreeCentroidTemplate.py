# 【模板】树的重心
# 题目来源: 洛谷 U328173 https://www.luogu.com.cn/problem/U328173
# 问题描述: 给定一棵无根树，求这棵树的重心（可能有多个）
# 树的重心定义: 计算以无根树每个点为根节点时的最大子树大小，这个值最小的点称为无根树的重心
# 算法思路:
# 1. 通过一次DFS计算每个节点作为根时的最大子树大小
# 2. 找到具有最小最大子树大小的所有节点，即为重心
# 时间复杂度：O(n)，只需要一次DFS遍历
# 空间复杂度：O(n)，用于存储树结构和递归栈

import sys
from collections import defaultdict

# 增加递归深度限制，防止大数据时栈溢出
sys.setrecursionlimit(1000000)

def main():
    # 读取节点数量n
    n = int(sys.stdin.readline())
    
    # 邻接表存储树结构，adj[u]表示与节点u相邻的所有节点列表
    adj = defaultdict(list)
    
    # 读取边信息并构建树
    # 无根树有n-1条边
    for _ in range(n - 1):
        u, v = map(int, sys.stdin.readline().split())
        # 由于是无根树，添加无向边
        adj[u].append(v)
        adj[v].append(u)
    
    # 子树大小数组，size[i]表示以节点i为根的子树的节点数量
    size = [0] * (n + 1)
    
    # 每个节点的最大子树大小数组，max_sub[i]表示以节点i为根时的最大子树大小
    max_sub = [0] * (n + 1)
    
    # 第一次DFS，计算每个节点的子树大小和最大子树大小
    # u: 当前访问的节点
    # father: u的父节点，避免回到父节点形成环
    def dfs1(u, father):
        # 初始化当前节点u的子树大小为1（包含节点u本身）
        size[u] = 1
        # 初始化当前节点u的最大子树大小为0
        max_sub[u] = 0
        
        # 遍历所有与节点u相邻的节点
        for v in adj[u]:
            # 如果不是父节点，则继续DFS
            if v != father:
                # 递归访问子节点v，父节点为u
                dfs1(v, u)
                # 将子节点v的子树大小加到当前节点u的子树大小中
                size[u] += size[v]
                # 更新以u为根时的最大子树大小
                max_sub[u] = max(max_sub[u], size[v])
        
        # 计算父节点方向的子树大小（即整棵树去掉以u为根的子树后剩余的部分）
        # 并更新最大子树大小
        max_sub[u] = max(max_sub[u], n - size[u])
    
    # 第一次DFS计算子树信息
    # 从节点1开始DFS，父节点为0（表示没有父节点）
    dfs1(1, 0)
    
    # 找到所有重心
    # 初始化最小的最大子树大小为n（最大可能值）
    min_max_sub = n
    
    # 找到最小的最大子树大小
    # 遍历所有节点，找到最小的max_sub值
    for i in range(1, n + 1):
        min_max_sub = min(min_max_sub, max_sub[i])
    
    # 收集所有具有最小最大子树大小的节点
    # 这些节点就是树的重心
    centroids = []
    for i in range(1, n + 1):
        if max_sub[i] == min_max_sub:
            centroids.append(i)
    
    # 按照题目要求，输出重心节点编号（可能有多个），按升序排列
    centroids.sort()
    print(' '.join(map(str, centroids)))

if __name__ == "__main__":
    main()
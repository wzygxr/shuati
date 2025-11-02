# Kay and Snowflake (雪花与凯)
# 题目来源: Codeforces 686D https://codeforces.com/contest/686/problem/D
# 问题描述: 给定一棵有根树，求出每一棵子树的重心是哪一个节点
# 树的重心定义：找到一个点，其所有的子树中最大的子树节点数最少
# 算法思路:
# 1. 首先通过DFS计算每个子树的大小
# 2. 对于每个节点，利用其最大子树的重心信息来快速找到当前子树的重心
# 3. 利用性质：子树的重心要么是最大子树的重心，要么在从最大子树重心到根节点的路径上
# 时间复杂度：O(n)，每个节点最多被访问常数次
# 空间复杂度：O(n)，用于存储树结构和递归栈

import sys
from collections import defaultdict

def main():
    # 读取节点数量n和查询数量q
    n, q = map(int, sys.stdin.readline().split())
    
    # 初始化邻接表和父节点数组
    # adj[u]存储节点u的所有子节点列表
    adj = defaultdict(list)
    # parent[i]存储节点i的父节点
    parent = [0] * (n + 1)
    
    # 读取父节点信息并构建树
    if n > 1:
        # 读取2到n节点的父节点信息
        parents = list(map(int, sys.stdin.readline().split()))
        for i in range(2, n + 1):
            parent[i] = parents[i - 2]
            # 添加边，将节点i添加到其父节点的子节点列表中
            adj[parent[i]].append(i)
    
    # 根节点的父节点设为-1，表示没有父节点
    parent[1] = -1
    
    # 子树大小和重心数组
    # size[i]表示以节点i为根的子树的节点数量
    size = [0] * (n + 1)
    # centroid[i]表示以节点i为根的子树的重心
    centroid = [0] * (n + 1)
    
    # 计算每个子树的大小
    # 使用DFS递归计算以节点u为根的子树大小
    def compute_size(u):
        # 初始化当前节点u的子树大小为0
        size[u] = 0
        
        # 递归计算每个子节点的子树大小
        for v in adj[u]:
            compute_size(v)
            size[u] += size[v]
        
        # 加上节点u本身
        size[u] += 1
    
    # 计算每个子树的重心
    # 利用已知的子树重心信息来快速计算当前子树的重心
    def compute_centroid(u):
        # 如果子树只有一个节点，重心就是它本身
        if size[u] == 1:
            centroid[u] = u
            return
        
        # 找到最大的子树
        # 初始化最大子树为第一个子节点
        largest = adj[u][0]
        
        # 遍历所有子节点，找到子树大小最大的子节点
        for v in adj[u]:
            # 递归计算子节点v的重心
            compute_centroid(v)
            
            # 更新最大子树
            if size[largest] < size[v]:
                largest = v
        
        # 子树大小的一半（向上取整）
        # 这是判断一个节点是否为重心的关键阈值
        half = (size[u] + 1) // 2
        
        # 从最大子树的重心开始向上查找
        # 利用性质：子树的重心要么是最大子树的重心，要么在从最大子树重心到根节点的路径上
        cur = centroid[largest]
        
        # 沿着从最大子树重心到当前节点u的路径向上查找
        while cur != u:
            # 如果当前节点的子树大小小于half，说明它不可能是重心，需要继续向上查找
            if size[cur] < half:
                cur = parent[cur]
            else:
                # 如果当前节点的子树大小大于等于half，且父节点方向的子树大小也小于half，则找到重心
                # 父节点方向的子树大小 = 整棵子树大小 - 当前节点子树大小
                if size[u] - size[cur] < half:
                    break
                else:
                    # 否则继续向上查找
                    cur = parent[cur]
        centroid[u] = cur
    
    # 计算每个子树的大小
    compute_size(1)
    
    # 计算每个子树的重心
    compute_centroid(1)
    
    # 处理查询
    for _ in range(q):
        u = int(sys.stdin.readline())
        # 输出以节点u为根的子树的重心
        print(centroid[u])

if __name__ == "__main__":
    main()
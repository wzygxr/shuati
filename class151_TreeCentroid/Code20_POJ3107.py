# POJ 3107 Godfather
# 题目描述：给定一棵树，找出所有的重心节点
# 算法思想：直接应用树的重心查找算法
# 测试链接：http://poj.org/problem?id=3107
# 时间复杂度：O(n)
# 空间复杂度：O(n)

import sys
from sys import stdin

# 设置递归深度以避免栈溢出
sys.setrecursionlimit(10**6)

def main():
    n = int(stdin.readline())
    
    # 初始化邻接表
    graph = [[] for _ in range(n + 1)]
    
    # 读取边
    for _ in range(n - 1):
        u, v = map(int, stdin.readline().split())
        graph[u].append(v)
        graph[v].append(u)
    
    # 子树大小
    size_ = [0] * (n + 1)
    # 每个节点的最大子树大小
    max_sub = [0] * (n + 1)
    # 最小的最大子树大小
    min_max_sub = float('inf')
    
    # 计算子树大小和最大子树大小
    def dfs(u, parent):
        nonlocal min_max_sub
        size_[u] = 1
        max_sub[u] = 0
        
        for v in graph[u]:
            if v != parent:
                dfs(v, u)
                size_[u] += size_[v]
                max_sub[u] = max(max_sub[u], size_[v])
        
        # 计算父方向的子树大小
        max_sub[u] = max(max_sub[u], n - size_[u])
        # 更新最小的最大子树大小
        if max_sub[u] < min_max_sub:
            min_max_sub = max_sub[u]
    
    # 第一次DFS计算子树信息
    dfs(1, -1)
    
    # 收集所有重心
    centroids = []
    for i in range(1, n + 1):
        if max_sub[i] == min_max_sub:
            centroids.append(i)
    
    # 排序输出
    centroids.sort()
    print(' '.join(map(str, centroids)))

# 运行主函数
if __name__ == "__main__":
    main()

# 注意：在POJ上提交时，需要将代码适配为POJ的格式
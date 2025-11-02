# Codeforces 1406C. Link Cut Centroids
# 题目描述：给定一棵树，执行一次操作：切断一条边，然后添加一条新边，使得新树只有一个重心
# 算法思想：如果树原本有两个重心，切断连接它们的路径上的一条边，然后将其中一个重心连接到另一个重心的子树中
# 测试链接：https://codeforces.com/problemset/problem/1406/C
# 时间复杂度：O(n)
# 空间复杂度：O(n)

import sys
from sys import stdin

# 设置递归深度以避免栈溢出
sys.setrecursionlimit(10**6)

def main():
    t = int(stdin.readline())  # 测试用例数量
    
    for _ in range(t):
        n = int(stdin.readline())
        
        # 初始化数据结构
        graph = [[] for _ in range(n + 1)]
        size_ = [0] * (n + 1)
        max_sub = [0] * (n + 1)
        
        # 读取边
        edges = []
        for _ in range(n - 1):
            u, v = map(int, stdin.readline().split())
            graph[u].append(v)
            graph[v].append(u)
            edges.append((u, v))
        
        # 计算子树大小和最大子树大小
        def dfs(u, parent):
            size_[u] = 1
            max_sub[u] = 0
            
            for v in graph[u]:
                if v != parent:
                    dfs(v, u)
                    size_[u] += size_[v]
                    max_sub[u] = max(max_sub[u], size_[v])
            
            max_sub[u] = max(max_sub[u], n - size_[u])
        
        # 第一次DFS计算子树信息
        dfs(1, -1)
        
        # 找到树的所有重心
        min_max_sub = float('inf')
        centroids = []
        
        for i in range(1, n + 1):
            if max_sub[i] < min_max_sub:
                min_max_sub = max_sub[i]
                centroids = [i]
            elif max_sub[i] == min_max_sub:
                centroids.append(i)
        
        # 找到一个子节点用于连接
        def find_child(u, parent):
            for v in graph[u]:
                if v != parent:
                    return v
            return -1  # 不应该到达这里
        
        # 如果只有一个重心，无需操作
        if len(centroids) == 1:
            # 输出任意一条边
            u, v = edges[0]
            print(f"{u} {v}")
            print(f"{u} {v}")
        else:
            # 有两个重心，centroids[0]和centroids[1]
            c1 = centroids[0]
            c2 = centroids[1]
            
            # 找到c1在c2方向上的子节点
            child = -1
            for v in graph[c2]:
                if v != c1 and size_[v] > size_[c2]:
                    child = v
                    break
            if child == -1:
                # 如果没找到，任选c1的一个子节点
                child = find_child(c1, c2)
            
            # 切断c1和child的边，连接c2和child
            print(f"{c1} {child}")
            print(f"{c2} {child}")

# 运行主函数
if __name__ == "__main__":
    main()

# 注意：在Codeforces上提交时，需要将代码适配为Codeforces的格式
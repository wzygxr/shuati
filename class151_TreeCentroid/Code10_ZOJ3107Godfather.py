# ZOJ 3107 Godfather
# 找到树的所有重心
# 树的重心定义：删除这个点后，剩余各个连通块中点数的最大值不超过总节点数的一半
# 测试链接 : https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367606
# 时间复杂度：O(n)
# 空间复杂度：O(n)

import sys
from collections import defaultdict

# 读取输入优化
input = sys.stdin.read
sys.setrecursionlimit(1000000)

def main():
    data = input().split()
    idx = 0
    
    while idx < len(data):
        n = int(data[idx])
        idx += 1
        
        # 邻接表存储树
        adj = defaultdict(list)
        
        # 读取边信息并构建树
        for _ in range(n - 1):
            u = int(data[idx])
            idx += 1
            v = int(data[idx])
            idx += 1
            adj[u].append(v)
            adj[v].append(u)
        
        # 子树大小
        size = [0] * (n + 1)
        
        # 每个节点的最大子树大小
        max_sub = [0] * (n + 1)
        
        # 第一次DFS，计算每个节点的子树大小和最大子树大小
        def dfs1(u, father):
            size[u] = 1
            max_sub[u] = 0
            
            # 遍历所有子节点
            for v in adj[u]:
                if v != father:
                    dfs1(v, u)
                    size[u] += size[v]
                    max_sub[u] = max(max_sub[u], size[v])
            
            # 计算父节点方向的子树大小
            max_sub[u] = max(max_sub[u], n - size[u])
        
        # 第一次DFS计算子树信息
        dfs1(1, 0)
        
        # 找到所有重心
        min_max_sub = n  # 初始化为最大值
        
        # 找到最小的最大子树大小
        for i in range(1, n + 1):
            min_max_sub = min(min_max_sub, max_sub[i])
        
        # 收集所有具有最小最大子树大小的节点
        centroids = []
        for i in range(1, n + 1):
            if max_sub[i] == min_max_sub:
                centroids.append(i)
        
        # 输出结果
        print(' '.join(map(str, sorted(centroids))))

if __name__ == "__main__":
    main()
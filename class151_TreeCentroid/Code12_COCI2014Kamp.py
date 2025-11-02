# COCI 2014/2015 #1 Kamp
# 给定一颗有n个节点的无根树，每一条边有一个经过的时间，树上有K个关键节点，
# 对于每一个节点u，需要回答从u出发到所有关键节点的最小时间
# 利用树的重心性质优化计算
# 测试链接 : https://oj.uz/problem/view/COCI15_kamp
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
        w = int(data[idx])
        idx += 1
        adj[u].append((v, w))
        adj[v].append((u, w))
    
    k = int(data[idx])
    idx += 1
    
    # 关键节点标记
    is_key = [False] * (n + 1)
    
    # 读取关键节点
    for _ in range(k):
        key_node = int(data[idx])
        idx += 1
        is_key[key_node] = True
    
    # 子树中关键节点的数量
    key_count = [0] * (n + 1)
    
    # 以u为根的子树中，从u出发遍历所有关键节点并返回u的最小时间
    subtree_time = [0] * (n + 1)
    
    # 从u出发遍历所有关键节点的最小时间（不需要返回u）
    min_time = [0] * (n + 1)
    
    # 第一次DFS，计算子树信息
    def dfs1(u, father):
        key_count[u] = 1 if is_key[u] else 0
        subtree_time[u] = 0
        
        # 遍历所有子节点
        for v, w in adj[u]:
            if v != father:
                dfs1(v, u)
                key_count[u] += key_count[v]
                
                # 如果子树中有关键节点，需要加上往返时间
                if key_count[v] > 0:
                    subtree_time[u] += subtree_time[v] + 2 * w
    
    # 第二次DFS，换根DP计算答案
    def dfs2(u, father, father_weight):
        if u == 1:
            # 根节点的最小时间就是子树时间
            min_time[u] = subtree_time[u]
        else:
            # 非根节点的最小时间需要考虑从父节点来的路径
            min_time[u] = subtree_time[u]
            
            # 如果父节点子树中有关键节点，需要考虑从父节点来的路径
            if key_count[1] - key_count[u] > 0:
                father_time = min_time[father]
                
                # 如果u是father的子树中包含关键节点的子树，需要减去u的贡献
                if key_count[u] > 0:
                    father_time -= subtree_time[u] + 2 * father_weight
                
                # 加上从u到father再遍历father其他子树的时间
                if key_count[1] - key_count[u] > 0:
                    min_time[u] += father_time + 2 * father_weight
        
        # 递归处理子节点
        for v, w in adj[u]:
            if v != father:
                dfs2(v, u, w)
    
    # 特殊情况：如果没有关键节点
    if k == 0:
        for i in range(1, n + 1):
            print(0)
    else:
        # 第一次DFS计算子树信息
        dfs1(1, 0)
        
        # 第二次DFS换根DP计算答案
        dfs2(1, 0, 0)
        
        # 输出结果
        for i in range(1, n + 1):
            # 如果不需要返回起点，可以减去最远关键节点的往返时间
            result = min_time[i]
            print(result)

if __name__ == "__main__":
    main()
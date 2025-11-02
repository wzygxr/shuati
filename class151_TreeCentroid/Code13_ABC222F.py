# AtCoder ABC222 F - Expensive Expense
# 给定一棵树，边权为路费，点权为观光费。从u去v旅游的费用定义为路费加上v点的观光费
# 求从每个点出发到其它点旅游的最大费用
# 换根DP，与树的重心相关
# 测试链接 : https://atcoder.jp/contests/abc222/tasks/abc222_f
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
    
    # 点权（观光费）
    D = [0] * (n + 1)
    
    # 读取点权（观光费）
    for i in range(1, n + 1):
        D[i] = int(data[idx])
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
    
    # 以u为根的子树中，从u出发到子树节点的最大费用
    max_down = [0] * (n + 1)
    
    # 从u出发到所有节点的最大费用
    max_cost = [0] * (n + 1)
    
    # 第一次DFS，计算向下最大费用
    def dfs1(u, father):
        max_down[u] = D[u]  # 至少包含自己的观光费
        
        # 遍历所有子节点
        for v, w in adj[u]:
            if v != father:
                dfs1(v, u)
                # 更新从u出发向下的最大费用
                max_down[u] = max(max_down[u], max_down[v] + w)
    
    # 第二次DFS，换根DP计算答案
    def dfs2(u, father, father_cost):
        # 从u出发的最大费用是向下最大费用和从父节点来的最大费用的最大值
        max_cost[u] = max(max_down[u], father_cost + D[u])
        
        # 计算从u到各个子节点的最大费用
        # 找到最大值和次大值
        max1 = -1
        max2 = -1
        max1_child = -1
        
        for v, w in adj[u]:
            if v != father:
                cost = max_down[v] + w
                if cost > max1:
                    max2 = max1
                    max1 = cost
                    max1_child = v
                elif cost > max2:
                    max2 = cost
        
        # 递归处理子节点
        for v, w in adj[u]:
            if v != father:
                # 计算从v向上看的最大费用
                up_cost = father_cost + w  # 从父节点来的费用
                
                # 如果v不是产生最大费用的子节点，可以加上最大费用
                # 否则加上次大费用
                if v == max1_child:
                    up_cost = max(up_cost, max2 + w)
                else:
                    up_cost = max(up_cost, max1 + w)
                
                # 加上v节点的观光费
                up_cost += D[u]
                
                dfs2(v, u, up_cost)
    
    # 第一次DFS计算向下最大费用
    dfs1(1, 0)
    
    # 第二次DFS换根DP计算答案
    dfs2(1, 0, 0)
    
    # 输出结果
    for i in range(1, n + 1):
        print(max_cost[i])

if __name__ == "__main__":
    main()
# 选择节点做根使流量和最大(递归版)
# 题目来源：POJ 3585 Accumulation Degree
# 题目链接：http://poj.org/problem?id=3585
# 测试链接 : http://poj.org/problem?id=3585
# 提交以下的code，提交时请把类名改成"Main"

'''
题目解析：
给定一棵树，每条边有流量限制。对于每个节点作为根，计算从该节点流向所有叶子节点的最大流量和。
叶子节点是度数为1的节点（根节点度数为1时不算叶子）。

算法思路：
1. 第一次DFS：计算以节点1为根时，每个节点向其子树所有叶子节点的流量和
   - flow[u]表示从节点u流向其子树中所有叶子节点的流量和
   - 如果v是叶子节点，则flow[u] += weight(e)（e是u到v的边）
   - 如果v不是叶子节点，则flow[u] += min(flow[v], weight(e))

2. 第二次DFS：换根DP，计算每个节点作为根时的流量和
   - 当从节点u换根到节点v时：
     * 如果u是叶子节点，则dp[v] = flow[v] + weight(e)
     * 如果u不是叶子节点：
       - 计算u向外的流量：uOut = dp[u] - min(flow[v], weight(e))
       - 计算dp[v] = flow[v] + min(uOut, weight(e))

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code04_MaximizeFlow1.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code04_MaximizeFlow1.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code04_MaximizeFlow1.cpp
'''

import sys
from collections import defaultdict

sys.setrecursionlimit(1000000)

def main():
    test_case = int(input())
    
    for _ in range(test_case):
        n = int(input())
        
        # 构建邻接表
        graph = defaultdict(list)
        degree = [0] * (n + 1)
        
        for _ in range(n - 1):
            u, v, w = map(int, input().split())
            graph[u].append((v, w))
            graph[v].append((u, w))
            degree[u] += 1
            degree[v] += 1
        
        # 初始化数组
        flow = [0] * (n + 1)  # flow[u]表示从u出发流向u节点为头的子树上，所有的叶节点，流量是多少
        dp = [0] * (n + 1)    # dp[u]表示从u出发流向u节点为根的整棵树上，所有的叶节点，流量是多少
        
        # 第一次DFS：计算以节点1为根时，每个节点向子树的流量
        def dfs1(u, f):
            # 先递归处理所有子节点
            for v, w in graph[u]:
                if v != f:
                    dfs1(v, u)
            
            # 计算从节点u向子树的流量
            for v, w in graph[u]:
                if v != f:
                    # 如果v是叶子节点（度数为1），则直接加上边的权重
                    # 否则，加上min(从v子树流出的流量, 边的权重)
                    if degree[v] == 1:
                        flow[u] += w
                    else:
                        flow[u] += min(flow[v], w)
        
        # 第二次DFS：换根DP，计算每个节点作为根时的流量
        def dfs2(u, f):
            for v, w in graph[u]:
                if v != f:
                    # 如果u是叶子节点
                    if degree[u] == 1:
                        dp[v] = flow[v] + w
                    else:
                        # u不是叶子节点
                        # 计算u向外的流量（不包括流向v的流量）
                        u_out = dp[u] - min(flow[v], w)
                        # 计算v作为根时的流量
                        dp[v] = flow[v] + min(u_out, w)
                    dfs2(v, u)
        
        # 第一次DFS计算以节点1为根时的流量
        dfs1(1, 0)
        dp[1] = flow[1]
        # 第二次DFS换根计算所有节点作为根时的流量
        dfs2(1, 0)
        
        # 找到最大流量
        ans = max(dp[1:n+1])
        print(ans)

if __name__ == "__main__":
    main()
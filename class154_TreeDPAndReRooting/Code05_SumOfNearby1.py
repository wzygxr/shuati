# 每个节点距离k以内的权值和(递归版)
# 题目来源：USACO 2012 FEB Nearby Cows
# 题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=124
# 测试链接 : https://www.luogu.com.cn/problem/P3047
# 提交以下的code，提交时请把类名改成"Main"

'''
题目解析：
给定一棵树，每个节点有权值。对于每个节点，计算距离它不超过k的所有节点的权值和。

算法思路：
1. 第一次DFS：计算以节点1为根时，每个节点向子树的权值和
   - sum[u][i]表示节点u的子树中，距离u恰好为i的节点权值和

2. 第二次DFS：换根DP，计算每个节点作为根时的距离k以内权值和
   - dp[u][i]表示整棵树中，距离u恰好为i的节点权值和
   - 当从节点u换根到节点v时：
     * dp[v][0] = sum[v][0] （v节点本身）
     * dp[v][1] = sum[v][1] + dp[u][0] （v的子节点 + u节点）
     * 对于i >= 2: dp[v][i] = sum[v][i] + dp[u][i-1] - sum[v][i-2]
       - dp[u][i-1]是距离u为i-1的节点权值和
       - sum[v][i-2]是要减去的重复计算部分

时间复杂度：O(n*k) - 两次DFS遍历，每次处理k个距离
空间复杂度：O(n*k) - 存储图和DP数组
是否为最优解：是，对于k较小的情况，这是最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code05_SumOfNearby1.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code05_SumOfNearby1.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code05_SumOfNearby1.cpp
'''

import sys
from collections import defaultdict

sys.setrecursionlimit(1000000)

def main():
    n, k = map(int, input().split())
    
    # 构建邻接表
    graph = defaultdict(list)
    for _ in range(n - 1):
        u, v = map(int, input().split())
        graph[u].append(v)
        graph[v].append(u)
    
    # 读取每个节点的权值
    weights = [0] * (n + 1)
    for i in range(1, n + 1):
        weights[i] = int(input())
    
    # 初始化数组
    # sum[u][i]表示以u为头的子树内，距离为i的节点权值和
    sum_dist = [[0] * (k + 1) for _ in range(n + 1)]
    # dp[u][i]表示以u做根，整棵树上，距离为i的节点权值和
    dp = [[0] * (k + 1) for _ in range(n + 1)]
    
    # 第一次DFS：计算以节点1为根时，每个节点向子树的距离权值和
    def dfs1(u, f):
        # 设置节点u本身（距离为0）的权值
        sum_dist[u][0] = weights[u]
        
        # 先递归处理所有子节点
        for v in graph[u]:
            if v != f:
                dfs1(v, u)
        
        # 计算从节点u向子树的距离权值和
        for v in graph[u]:
            if v != f:
                # 对于每个距离j，将v子树中距离v为j-1的节点加到u的统计中
                for j in range(1, k + 1):
                    sum_dist[u][j] += sum_dist[v][j - 1]
    
    # 第二次DFS：换根DP，计算每个节点作为根时的距离权值和
    def dfs2(u, f):
        for v in graph[u]:
            if v != f:
                # 换根公式
                dp[v][0] = sum_dist[v][0]  # 节点v本身
                if k >= 1:
                    dp[v][1] = sum_dist[v][1] + dp[u][0]  # v的子节点 + u节点
                # 对于距离i >= 2的情况
                for i in range(2, k + 1):
                    # dp[v][i] = v子树中的节点 + u子树中除了v子树的节点
                    # dp[u][i-1]是u子树中距离u为i-1的节点
                    # sum[v][i-2]是v子树中距离v为i-2的节点，需要减去避免重复计算
                    dp[v][i] = sum_dist[v][i] + dp[u][i - 1] - sum_dist[v][i - 2]
                dfs2(v, u)
    
    # 第一次DFS计算以节点1为根时的距离权值和
    dfs1(1, 0)
    # 初始化节点1作为根时的dp值
    for i in range(k + 1):
        dp[1][i] = sum_dist[1][i]
    # 第二次DFS换根计算所有节点作为根时的距离权值和
    dfs2(1, 0)
    
    # 输出每个节点距离k以内的权值和
    for i in range(1, n + 1):
        ans = 0
        # 将所有距离内的权值相加
        for j in range(k + 1):
            ans += dp[i][j]
        print(ans)

if __name__ == "__main__":
    main()
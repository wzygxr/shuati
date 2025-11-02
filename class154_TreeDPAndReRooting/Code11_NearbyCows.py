# Nearby Cows G
# 题目来源：USACO 2012 FEB Nearby Cows
# 题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=124
# 测试链接 : https://www.luogu.com.cn/problem/P3047
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

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
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code11_NearbyCows.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code11_NearbyCows.py
'''

import sys
from collections import defaultdict, deque
import threading

def main():
    # 读取输入
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    # 读取节点数和距离k
    n, k = map(int, input_lines[0].split())
    
    # 构建邻接表
    graph = defaultdict(list)
    for i in range(1, n):
        u, v = map(int, input_lines[i].split())
        graph[u].append(v)
        graph[v].append(u)
    
    # 读取每个节点的权值
    weights = [0] * (n + 1)
    weight_values = list(map(int, input_lines[n].split()))
    for i in range(1, n + 1):
        weights[i] = weight_values[i - 1]
    
    # sum[u][i] : 以u为头的子树内，距离为i的节点权值和
    sum_vals = [[0] * (k + 1) for _ in range(n + 1)]
    
    # dp[u][i] : 以u做根，整棵树上，距离为i的节点权值和
    dp = [[0] * (k + 1) for _ in range(n + 1)]
    
    # 第一次DFS：计算以节点1为根时，每个节点向子树的距离权值和
    def dfs1(u, f):
        # 初始化节点u本身
        sum_vals[u][0] = weights[u]
        # 先递归处理所有子节点
        for v in graph[u]:
            if v != f:
                dfs1(v, u)
        # 计算从节点u向子树的距离权值和
        for v in graph[u]:
            if v != f:
                # 对于每个距离j，将v子树中距离v为j-1的节点加到u的统计中
                for j in range(1, k + 1):
                    sum_vals[u][j] += sum_vals[v][j - 1]
    
    # 第二次DFS：换根DP，计算每个节点作为根时的距离权值和
    def dfs2(u, f):
        for v in graph[u]:
            if v != f:
                # 换根公式
                dp[v][0] = sum_vals[v][0]  # 节点v本身
                dp[v][1] = sum_vals[v][1] + dp[u][0]  # v的子节点 + u节点
                # 对于距离i >= 2的情况
                for i in range(2, k + 1):
                    # dp[v][i] = v子树中的节点 + u子树中除了v子树的节点
                    # dp[u][i-1]是u子树中距离u为i-1的节点
                    # sum_vals[v][i-2]是v子树中距离v为i-2的节点，需要减去避免重复计算
                    dp[v][i] = sum_vals[v][i] + dp[u][i - 1] - sum_vals[v][i - 2]
                dfs2(v, u)
    
    # 执行两次DFS
    dfs1(1, 0)
    # 初始化节点1作为根时的dp值
    for i in range(k + 1):
        dp[1][i] = sum_vals[1][i]
    dfs2(1, 0)
    
    # 输出每个节点距离k以内的权值和
    for i in range(1, n + 1):
        ans = 0
        # 将所有距离内的权值相加
        for j in range(k + 1):
            ans += dp[i][j]
        print(ans)

# 使用线程来增加递归限制，避免栈溢出
threading.Thread(target=main).start()
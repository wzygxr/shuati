# 可以到达每一个节点的最少边反转次数
# 题目来源：LeetCode 2858. Minimum Edge Reversals So Every Node Is Reachable
# 题目链接：https://leetcode.cn/problems/minimum-edge-reversals-so-every-node-is-reachable/
# 测试链接 : https://leetcode.cn/problems/minimum-edge-reversals-so-every-node-is-reachable/
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

'''
题目解析：
给定一个有向图，如果将所有边视为无向边，则图形成一棵树。我们需要为每个节点计算，
以该节点为根时，最少需要反转多少条边的方向，才能从该节点到达所有其他节点。

算法思路：
1. 第一次DFS：以节点0为根，计算每个节点子树内的反向边数
   - 对于每条边u->v，如果在DFS遍历中是从u到v，则不需要翻转（权重为0）
   - 如果在DFS遍历中应该是v->u，但实际上存储的是u->v，则需要翻转（权重为1）

2. 第二次DFS：换根DP，计算每个节点作为根时需要翻转的边数
   - 当从节点u换根到节点v时：
     * 如果原边是u->v（权重为0），换根后需要翻转，dp[v] = dp[u] + 1
     * 如果原边是v->u（权重为1），换根后不需要翻转，dp[v] = dp[u] - 1

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code10_MinEdgeReversals.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code10_MinEdgeReversals.py
'''

import sys
from collections import defaultdict, deque
import threading

def main():
    # 读取输入
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    i = 0
    while i < len(input_lines):
        if not input_lines[i]:
            i += 1
            continue
            
        # 读取节点数
        n = int(input_lines[i])
        i += 1
        
        # 构建邻接表，每条边存储两个方向
        # 正向边权重为0，反向边权重为1
        graph = defaultdict(list)
        for j in range(n - 1):
            u, v = map(int, input_lines[i + j].split())
            # 添加两条边，一条正向（权重0），一条反向（权重1）
            graph[u].append((v, 0))  # 原边方向: u -> v
            graph[v].append((u, 1))  # 反向边: v -> u
        i += n - 1
        
        # reverse[u] : u到所有子节点需要逆转的边数
        reverse = [0] * n
        # dp[u] : u做根到全树节点需要逆转的边数
        dp = [0] * n
        
        # 第一次DFS：计算以节点0为根时需要翻转的边数
        def dfs1(u, f):
            for v, w in graph[u]:
                if v != f:
                    dfs1(v, u)
                    # 累加子节点需要翻转的边数
                    # w为0表示原边是u->v，不需要翻转
                    # w为1表示原边是v->u，需要翻转
                    reverse[u] += reverse[v] + w
        
        # 第二次DFS：换根DP，计算每个节点作为根时需要翻转的边数
        def dfs2(u, f):
            for v, w in graph[u]:
                if v != f:
                    if w == 0:
                        # 原边方向 : u -> v
                        # 换根后需要翻转这条边
                        dp[v] = dp[u] + 1
                    else:
                        # 原边方向 : v -> u
                        # 换根后不需要翻转这条边
                        dp[v] = dp[u] - 1
                    dfs2(v, u)
        
        # 执行两次DFS
        dfs1(0, -1)
        dp[0] = reverse[0]
        dfs2(0, -1)
        
        # 输出结果
        print(" ".join(map(str, dp)))

# 使用线程来增加递归限制，避免栈溢出
threading.Thread(target=main).start()
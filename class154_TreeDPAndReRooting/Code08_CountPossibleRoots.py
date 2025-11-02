# 统计可能的树根数目
# 题目来源：LeetCode 2581. Count Number of Possible Root Nodes
# 题目链接：https://leetcode.cn/problems/count-number-of-possible-root-nodes/
# 测试链接 : https://leetcode.cn/problems/count-number-of-possible-root-nodes/
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

'''
题目解析：
这是一道典型的换根DP问题。我们需要统计有多少个节点可以作为根，使得至少有k个猜测是正确的。

算法思路：
1. 第一次DFS：以节点0为根，计算每个节点子树内的正确猜测数
   - 对于每条边u-v，如果猜测中存在(u,v)，则表示u是v的父节点，这是一个正确猜测
   - 统计以0为根时的正确猜测数

2. 第二次DFS：换根DP，计算每个节点作为根时的正确猜测数
   - 当从节点u换根到节点v时：
     * 原来u是v的父节点，现在v是u的父节点
     * 如果猜测中存在(u,v)，则换根后这个猜测就不再正确
     * 如果猜测中存在(v,u)，则换根后这个猜测就变为正确
     * 因此：dp[v] = dp[u] - (u,v存在?) + (v,u存在?)

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code08_CountPossibleRoots.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code08_CountPossibleRoots.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code08_CountPossibleRoots.cpp
'''

import sys
from collections import defaultdict, deque
import threading

def main():
    # 读取测试用例数
    testCase = int(sys.stdin.readline())
    
    for _ in range(testCase):
        # 读取节点数
        n = int(sys.stdin.readline())
        
        # 构建邻接表
        graph = defaultdict(list)
        for _ in range(n - 1):
            u, v = map(int, sys.stdin.readline().split())
            graph[u].append(v)
            graph[v].append(u)
        
        # 读取猜测数和阈值
        m, k = map(int, sys.stdin.readline().split())
        
        # 存储所有猜测
        guesses = set()
        for _ in range(m):
            u, v = map(int, sys.stdin.readline().split())
            guesses.add((u, v))
        
        # dp[i]: 以节点i为根时的正确猜测数
        dp = [0] * n
        
        # 第一次DFS：计算以节点0为根时的正确猜测数
        def dfs1(u, f):
            for v in graph[u]:
                if v != f:
                    dfs1(v, u)
                    # 如果猜测中存在(u,v)，则这是一个正确的猜测
                    if (u, v) in guesses:
                        dp[0] += 1
        
        # 第二次DFS：换根DP，计算每个节点作为根时的正确猜测数
        def dfs2(u, f):
            for v in graph[u]:
                if v != f:
                    # 换根公式：
                    # 原来u是v的父节点，现在v是u的父节点
                    # 如果猜测中存在(u,v)，则换根后这个猜测就不再正确 (-1)
                    # 如果猜测中存在(v,u)，则换根后这个猜测就变为正确 (+1)
                    dp[v] = dp[u]
                    if (u, v) in guesses:
                        dp[v] -= 1
                    if (v, u) in guesses:
                        dp[v] += 1
                    dfs2(v, u)
        
        # 执行两次DFS
        dfs1(0, -1)
        dfs2(0, -1)
        
        # 统计满足条件的根节点数目
        ans = 0
        for i in range(n):
            if dp[i] >= k:
                ans += 1
        
        print(ans)

# 使用线程来增加递归限制，避免栈溢出
threading.Thread(target=main).start()
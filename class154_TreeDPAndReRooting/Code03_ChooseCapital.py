# 翻转道路数量最少的首都
# 题目来源：Codeforces 219D Choosing Capital for Treeland
# 题目链接：https://codeforces.com/problemset/problem/219/D
# 测试链接 : https://www.luogu.com.cn/problem/CF219D
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

'''
题目解析：
给定一个有向树，选择一个节点作为根（首都），使得从该根可以到达所有其他节点。
由于边是有向的，可能需要翻转一些边的方向。目标是最小化需要翻转的边数。

算法思路：
1. 第一次DFS：计算以节点1为根时需要翻转的边数
   - 对于每条边u->v，如果在DFS遍历中是从u到v，则不需要翻转（权重为0）
   - 如果在DFS遍历中应该是v->u，但实际上存储的是u->v，则需要翻转（权重为1）
   - reverse[u]表示节点u到其所有子节点需要逆转的边数

2. 第二次DFS：换根DP，计算每个节点作为根时需要翻转的边数
   - 当从节点u换根到节点v时：
     * 如果原边是u->v（权重为0），换根后需要翻转，dp[v] = dp[u] + 1
     * 如果原边是v->u（权重为1），换根后不需要翻转，dp[v] = dp[u] - 1

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code03_ChooseCapital.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code03_ChooseCapital.py
'''

import sys
from collections import defaultdict

sys.setrecursionlimit(1000000)

def main():
    n = int(input())
    
    # 构建邻接表，存储有向边
    # graph[u] = [(v, w)] 其中w表示边的方向权重
    # w=0表示原边是u->v，不需要翻转
    # w=1表示原边是v->u，需要翻转
    graph = defaultdict(list)
    
    for _ in range(n - 1):
        u, v = map(int, input().split())
        # 添加两条边，一条正向（权重0），一条反向（权重1）
        graph[u].append((v, 0))
        graph[v].append((u, 1))
    
    # 初始化数组
    reverse = [0] * (n + 1)  # reverse[u]表示u到所有子节点需要逆转的边数
    dp = [0] * (n + 1)       # dp[u]表示u做根到全树节点需要逆转的边数
    
    # 第一次DFS：计算以节点1为根时需要翻转的边数
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
    
    # 第一次DFS计算以节点1为根时需要翻转的边数
    dfs1(1, 0)
    dp[1] = reverse[1]
    # 第二次DFS换根计算所有节点作为根时需要翻转的边数
    dfs2(1, 0)
    
    # 找到最小翻转边数
    min_reverse = min(dp[1:n+1])
    
    # 输出结果
    print(min_reverse)
    result = []
    for i in range(1, n + 1):
        if dp[i] == min_reverse:
            result.append(str(i))
    print(' '.join(result))

if __name__ == "__main__":
    main()
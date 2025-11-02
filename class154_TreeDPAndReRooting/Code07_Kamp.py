# 聚会后送每个人回家最短用时
# 题目来源：COCI 2015 Kamp
# 题目链接：https://oj.uz/problem/view/COCI_2015_kamp
# 测试链接 : https://www.luogu.com.cn/problem/P6419
# 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

'''
题目解析：
给定一棵树和k个需要接送的乘客位置，对于每个节点作为聚会点，计算送所有乘客回家的最短时间。
车从聚会点出发，送完所有乘客后不需要回到聚会点。

算法思路：
1. 第一次DFS：计算以节点1为根时，每个节点子树内的信息
   - people[u]表示节点u子树内需要接送的乘客数量
   - incost[u]表示在节点u子树内接送所有乘客并回到u的最小代价
   - inner1[u]表示在节点u子树内接送乘客的最长链
   - inner2[u]表示在节点u子树内接送乘客的次长链
   - choose[u]表示最长链来自u的哪个子节点

2. 第二次DFS：换根DP，计算每个节点作为聚会点时的总代价
   - outcost[u]表示在节点u子树外接送所有乘客并回到u的最小代价
   - outer[u]表示在节点u子树外接送乘客的最长链
   - 当从节点u换根到节点v时：
     * 计算outcost[v]和outer[v]

3. 最终答案：对于节点i，答案为incost[i] + outcost[i] - max(inner1[i], outer[i])
   - incost[i] + outcost[i]表示接送所有乘客的总代价
   - max(inner1[i], outer[i])表示最长的那条链不需要返回

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code07_Kamp.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code07_Kamp.py
'''

import sys
from collections import defaultdict

sys.setrecursionlimit(1000000)

def main():
    n, k = map(int, input().split())
    
    # 构建邻接表
    graph = defaultdict(list)
    for _ in range(n - 1):
        u, v, w = map(int, input().split())
        graph[u].append((v, w))
        graph[v].append((u, w))
    
    # 记录每个节点的乘客数量
    people = [0] * (n + 1)
    for _ in range(k):
        u = int(input())
        people[u] += 1
    
    # 初始化数组
    incost = [0] * (n + 1)    # incost[i]: i内部，从i出发送完所有乘客回到i的最少代价
    inner1 = [0] * (n + 1)    # inner1[i]: i内部，从i出发送乘客的最长链
    inner2 = [0] * (n + 1)    # inner2[i]: i内部，从i出发送乘客的次长链
    choose = [0] * (n + 1)    # choose[i]: 送乘客的最长链来自i的哪个儿子
    outcost = [0] * (n + 1)   # outcost[i]: i外部，从i出发送完所有乘客回到i的最少代价
    outer = [0] * (n + 1)     # outer[i]: i外部，从i出发送乘客的最长链
    
    # 第一次DFS：计算以节点1为根时，每个节点子树内的信息
    def dfs1(u, f):
        for v, w in graph[u]:
            if v != f:
                dfs1(v, u)
                # 累加子树中的乘客数量
                people[u] += people[v]
                # 如果子树中有乘客需要接送
                if people[v] > 0:
                    # 计算接送子树中所有乘客并回到u的代价
                    # 需要走u->v->...->v->u，所以是incost[v] + w*2
                    incost[u] += incost[v] + w * 2
                    # 更新最长链和次长链
                    if inner1[u] < inner1[v] + w:
                        choose[u] = v
                        inner2[u] = inner1[u]
                        inner1[u] = inner1[v] + w
                    elif inner2[u] < inner1[v] + w:
                        inner2[u] = inner1[v] + w
    
    # 第二次DFS：换根DP，计算每个节点作为聚会点时的外部信息
    def dfs2(u, f):
        for v, w in graph[u]:
            if v != f:
                # 如果子树外有乘客需要接送
                if k - people[v] > 0:
                    # 计算v子树外接送所有乘客并回到v的代价
                    if people[v] == 0:
                        # v子树内没有乘客
                        outcost[v] = outcost[u] + incost[u] + w * 2
                    else:
                        # v子树内有乘客
                        outcost[v] = outcost[u] + incost[u] - incost[v]
                    # 更新最长链
                    if v != choose[u]:
                        # 最长链不来自v
                        outer[v] = max(outer[u], inner1[u]) + w
                    else:
                        # 最长链来自v，使用次长链
                        outer[v] = max(outer[u], inner2[u]) + w
                dfs2(v, u)
    
    # 第一次DFS计算以节点1为根时的信息
    dfs1(1, 0)
    # 第二次DFS换根计算所有节点作为聚会点时的外部信息
    dfs2(1, 0)
    
    # 计算并输出每个节点作为聚会点时的答案
    for i in range(1, n + 1):
        # 总代价 - 最长链（因为最长链不需要返回）
        print(incost[i] + outcost[i] - max(inner1[i], outer[i]))

if __name__ == "__main__":
    main()
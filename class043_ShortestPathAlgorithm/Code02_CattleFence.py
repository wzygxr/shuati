# 牛场围栏
# 给定一个长度为n的数组arr, arr[i]代表第i种木棍的长度，每种木棍有无穷多个
# 给定一个正数m，表示你可以把任何一根木棍消去最多m的长度，同一种木棍可以消去不同的长度
# 你可以随意拼接木棍形成一个长度，返回不能拼出来的长度中，最大值是多少
# 如果你可以拼出所有的长度，返回-1
# 如果不能拼出来的长度有无穷多，返回-1
# 1 <= n <= 100
# 1 <= arr[i] <= 3000
# 1 <= m <= 3000
# 测试链接 : https://www.luogu.com.cn/problem/P2662

'''
算法思路：
这道题使用同余最短路算法解决。
通过Dijkstra算法构建模x意义下的最短路图，其中x是所有可能长度中的最小值。
每个点i表示模x余数为i的所有长度中能拼出的最小值。
通过其他木棍长度在不同余数之间建立边，权值为木棍长度。
最后找出不能拼出的最大长度。

时间复杂度：O(x * log x + n)
空间复杂度：O(x)

题目来源：洛谷P2662 牛场围栏 (https://www.luogu.com.cn/problem/P2662)
相关题目：
1. 洛谷P3403 跳楼机 - 同类型同余最短路问题 (https://www.luogu.com.cn/problem/P3403)
2. 洛谷P2371 墨墨的等式 - 同余最短路经典问题 (https://www.luogu.com.cn/problem/P2371)
3. POJ 1061 青蛙的约会 - 数论相关问题 (http://poj.org/problem?id=1061)
4. Codeforces 986F Oppa Funcan Style Remastered - 同余最短路 (https://codeforces.com/problemset/problem/986/F)
5. 洛谷P2421 荒岛野人 - 数论问题 (https://www.luogu.com.cn/problem/P2421)
6. POJ 3250 Bad Hair Day - 单调栈问题 (http://poj.org/problem?id=3250)
7. 洛谷P9140 背包 - 同余最短路应用 (https://www.luogu.com.cn/problem/P9140)
8. 洛谷P1776 数列分段 - 动态规划问题 (https://www.luogu.com.cn/problem/P1776)
9. 洛谷P1948 数学作业 - 同余最短路 (https://www.luogu.com.cn/problem/P1948)
10. POJ 2371 Counting Capacities - 经典同余最短路问题
'''

import heapq
import sys

def main():
    # 读取输入
    line = sys.stdin.readline().split()
    n = int(line[0])
    m = int(line[1])
    
    arr = [0] * (n + 1)
    line = sys.stdin.readline().split()
    x = float('inf')
    
    for i in range(1, n + 1):
        arr[i] = int(line[i - 1])
        x = min(x, max(1, arr[i] - m))
    
    x = int(x)
    
    # 初始化
    set_used = [False] * 3001
    distance = [float('inf')] * x
    visited = [False] * x
    
    # 构建图的邻接表表示
    graph = [[] for _ in range(x)]
    
    # 添加边
    for i in range(1, n + 1):
        for j in range(max(1, arr[i] - m), arr[i] + 1):
            if not set_used[j]:
                set_used[j] = True
                for k in range(x):
                    graph[k].append(((k + j) % x, j))
    
    # Dijkstra算法
    distance[0] = 0
    pq = [(0, 0)]  # (距离, 节点)
    
    while pq:
        d, u = heapq.heappop(pq)
        
        if visited[u]:
            continue
            
        visited[u] = True
        
        for v, w in graph[u]:
            if not visited[v] and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w
                heapq.heappush(pq, (distance[v], v))
    
    # 计算结果
    ans = 0
    if x == 1:
        ans = -1
    else:
        for i in range(1, x):
            if distance[i] == float('inf'):
                ans = -1
                break
            ans = max(ans, distance[i] - x)
    
    print(ans)

if __name__ == "__main__":
    main()
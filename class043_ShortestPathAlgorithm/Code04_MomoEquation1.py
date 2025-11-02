# 墨墨的等式(dijkstra算法)
# 一共有n种正数，每种数可以选择任意个，个数不能是负数
# 那么一定有某些数值可以由这些数字累加得到
# 请问在[l...r]范围上，有多少个数能被累加得到
# 0 <= n <= 12
# 0 <= 数值范围 <= 5 * 10^5
# 1 <= l <= r <= 10^12
# 测试链接 : https://www.luogu.com.cn/problem/P2371

'''
算法思路：
这道题可以转化为图论问题，用Dijkstra算法解决。
选择数组中最小的数作为基准数x，构建模x意义下的最短路图。
每个点i表示模x余数为i的所有数中能被表示的最小值。
通过其他数字在不同余数之间建立边，权值为数字值。
最后统计[l,r]范围内能被表示的数的个数。

时间复杂度：O(x * log x + n)
空间复杂度：O(x)

题目来源：洛谷P2371 墨墨的等式 (https://www.luogu.com.cn/problem/P2371)
相关题目：
1. 洛谷P3403 跳楼机 - 与本题思路相同 (https://www.luogu.com.cn/problem/P3403)
2. POJ 2371 Counting Capacities - 经典同余最短路问题 (http://poj.org/problem?id=2371)
3. Codeforces 1117D Magic Gems - 矩阵快速幂+最短路优化DP (https://codeforces.com/problemset/problem/1117/D)
4. 洛谷P2662 牛场围栏 - 同余最短路应用 (https://www.luogu.com.cn/problem/P2662)
5. POJ 1061 青蛙的约会 - 扩展欧几里得算法 (http://poj.org/problem?id=1061)
6. Codeforces 986F Oppa Funcan Style Remastered - 同余最短路 (https://codeforces.com/problemset/problem/986/F)
7. 洛谷P2421 荒岛野人 - 数论问题 (https://www.luogu.com.cn/problem/P2421)
8. POJ 3250 Bad Hair Day - 单调栈问题 (http://poj.org/problem?id=3250)
9. 洛谷P9140 背包 - 同余最短路应用 (https://www.luogu.com.cn/problem/P9140)
10. 洛谷P1776 数列分段 - 动态规划问题 (https://www.luogu.com.cn/problem/P1776)
11. 洛谷P1948 数学作业 - 同余最短路 (https://www.luogu.com.cn/problem/P1948)
12. LeetCode 743 Network Delay Time - Dijkstra算法应用 (https://leetcode.cn/problems/network-delay-time/)
13. LeetCode 1631 Path With Minimum Effort - Dijkstra算法应用 (https://leetcode.cn/problems/path-with-minimum-effort/)
14. LeetCode 773 Sliding Puzzle - BFS/最短路问题 (https://leetcode.cn/problems/sliding-puzzle/)
15. AtCoder ARC084_B Small Multiple - 01-BFS问题 (https://atcoder.jp/contests/abc077/tasks/arc084_b)
'''

import heapq
import sys

def main():
    # 读取输入
    line = sys.stdin.readline().split()
    n = int(line[0])
    l = int(line[1]) - 1
    r = int(line[2])
    
    # 读取数组元素
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 过滤掉0值
    non_zero_arr = [x for x in arr if x != 0]
    
    if not non_zero_arr:
        print(0)
        return
    
    # 选择最小的数作为基准数x
    x = min(non_zero_arr)
    
    # 初始化距离数组
    distance = [float('inf')] * x
    visited = [False] * x
    
    # 构建图的邻接表表示
    graph = [[] for _ in range(x)]
    for num in non_zero_arr:
        if num != x:  # 不处理基准数本身
            for j in range(x):
                graph[j].append(((j + num) % x, num))
    
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
    for i in range(x):
        if r >= distance[i]:
            ans += (r - distance[i]) // x + 1
        if l >= distance[i]:
            ans -= (l - distance[i]) // x + 1
    
    print(ans)

if __name__ == "__main__":
    main()
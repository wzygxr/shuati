"""
洛谷 P1613 跑路

题目描述：
一共有n个节点，编号1~n，一共有m条有向边，每条边1公里
有一个空间跑路器，每秒你都可以直接移动2^k公里，每秒钟可以随意决定k的值
题目保证1到n之间一定可以到达，返回1到n最少用几秒

解题思路：
这道题是一个结合了倍增思想和最短路径算法的问题。

核心思想：
1. 预处理：使用倍增思想，计算任意两点之间是否存在长度为2^k的路径
2. 最短路径：在预处理的基础上，使用Floyd算法计算最短路径

具体步骤：
1. 初始化：对于每条边(u,v)，标记u到v存在长度为2^0=1的路径
2. 倍增预处理：对于k从1到最大值，如果存在i到j长度为2^(k-1)的路径，
   且存在j到p长度为2^(k-1)的路径，则i到p存在长度为2^k的路径
3. 最短路径计算：使用Floyd算法，在新图上计算1到n的最短路径

时间复杂度：O(n^3 * log(max_distance))
空间复杂度：O(n^2 * log(max_distance))

相关题目：
- Codeforces 835D - Palindromic characteristics (字符串倍增)
- POJ 3253 - Fence Repair (贪心 + 倍增思想)
- HDU 3507 - Print Article (DP斜率优化，也可用倍增思想优化)
"""

import sys
from typing import List

# 最大节点数
MAXN = 61
# 最大幂次
MAXP = 64
# 表示不可达
INF = float('inf')

def run_away(n: int, edges: List[List[int]]) -> int:
    """
    计算从节点1到节点n的最短时间
    
    Args:
        n: 节点数
        edges: 边的列表，每个元素为 [起点, 终点]
    
    Returns:
        从节点1到节点n的最短时间
    """
    # st[i][j][p] : i到j的距离是不是2^p
    st = [[[False for _ in range(MAXP + 1)] for _ in range(MAXN)] for _ in range(MAXN)]
    
    # time[i][j] : i到j的最短时间
    time = [[INF for _ in range(MAXN)] for _ in range(MAXN)]
    
    # 初始化边
    for u, v in edges:
        st[u][v][0] = True
        time[u][v] = 1
    
    # 倍增预处理
    # 先枚举次方
    # 再枚举跳板
    # 最后枚举每一组(i,j)
    for p in range(1, MAXP + 1):
        for jump in range(1, n + 1):
            for i in range(1, n + 1):
                for j in range(1, n + 1):
                    # 如果i到jump有2^(p-1)的路径，jump到j也有2^(p-1)的路径
                    # 那么i到j就有2^p的路径，时间为1秒
                    if st[i][jump][p - 1] and st[jump][j][p - 1]:
                        st[i][j][p] = True
                        time[i][j] = 1
    
    # 如果1到n已经可以直接1秒到达，直接返回
    if time[1][n] != 1:
        # 使用Floyd算法计算最短路径
        # 先枚举跳板
        # 最后枚举每一组(i,j)
        for jump in range(1, n + 1):
            for i in range(1, n + 1):
                for j in range(1, n + 1):
                    # 如果i到jump可达，jump到j也可达
                    # 更新i到j的最短时间
                    if time[i][jump] != INF and time[jump][j] != INF:
                        time[i][j] = min(time[i][j], time[i][jump] + time[jump][j])
    
    return int(time[1][n])


# 测试用例
if __name__ == "__main__":
    # 模拟测试用例
    n = 4
    edges = [[1, 1], [1, 2], [2, 3], [3, 4]]
    
    print("测试用例:")
    print("节点数: 4")
    print("边: 1->1, 1->2, 2->3, 3->4")
    print("最短时间:", run_away(n, edges))  # 期望输出: 1
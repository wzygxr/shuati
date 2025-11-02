#!/usr/bin/env python3

"""
HDU 1285 - 确定比赛名次

题目描述：
有N个比赛队（1<=N<500），编号依次为1，2，3，...，N进行比赛，比赛结束后，
裁判委员会要将所有参赛队伍从前往后依次排名，但现在裁判委员会不能直接获得
每个队的比赛成绩，只知道每场比赛的结果，即P1赢P2，用P1,P2表示，排名时P1在P2之前。
现在请你编程序确定排名。

注意：符合条件的排名可能不是唯一的，此时要求输出时编号小的队伍在前；
输入数据保证是正确的，即输入数据确保一定能有一个符合要求的排名。

解题思路：
这是一道典型的拓扑排序题，但要求输出字典序最小的拓扑序列。
为了实现字典序最小，我们在选择入度为0的节点时，使用优先队列（最小堆），
每次选择编号最小的节点。

算法步骤：
1. 构建图和入度数组
2. 将所有入度为0的节点加入优先队列
3. 不断从优先队列中取出编号最小的节点，加入结果序列
4. 将该节点的所有邻居节点入度减1
5. 如果邻居节点入度变为0，则加入优先队列
6. 重复3-5直到队列为空

时间复杂度：O(V log V + E)，优先队列操作的复杂度
空间复杂度：O(V + E)

测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=1285
"""

import sys
import heapq
from collections import defaultdict

def topological_sort_lexicographically(n, edges):
    """
    字典序最小的拓扑排序
    :param n: 节点数量
    :param edges: 边的列表，每个元素为(winner, loser)表示winner赢了loser
    :return: 拓扑排序结果（字典序最小）
    """
    # 构建邻接表和入度数组
    graph = defaultdict(list)
    in_degree = [0] * (n + 1)
    
    # 建图
    for winner, loser in edges:
        graph[winner].append(loser)
        in_degree[loser] += 1
    
    # 使用优先队列（最小堆）保证每次取编号最小的节点
    heap = []
    
    # 将所有入度为0的节点加入优先队列
    for i in range(1, n + 1):
        if in_degree[i] == 0:
            heapq.heappush(heap, i)
    
    result = []
    
    # Kahn算法进行拓扑排序
    while heap:
        # 取出编号最小的节点
        current = heapq.heappop(heap)
        result.append(current)
        
        # 遍历当前节点的所有邻居
        for neighbor in graph[current]:
            # 将邻居节点的入度减1
            in_degree[neighbor] -= 1
            # 如果邻居节点的入度变为0，则加入队列
            if in_degree[neighbor] == 0:
                heapq.heappush(heap, neighbor)
    
    return result

def main():
    for line in sys.stdin:
        parts = list(map(int, line.strip().split()))
        if not parts:
            continue
            
        n, m = parts[0], parts[1]
        
        edges = []
        # 读取比赛结果
        for _ in range(m):
            winner, loser = map(int, input().split())
            edges.append((winner, loser))
        
        # 拓扑排序（字典序最小）
        result = topological_sort_lexicographically(n, edges)
        
        # 输出结果
        print(' '.join(map(str, result)))

if __name__ == "__main__":
    try:
        main()
    except EOFError:
        pass
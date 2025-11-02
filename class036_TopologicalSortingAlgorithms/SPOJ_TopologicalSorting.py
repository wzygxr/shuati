#!/usr/bin/env python3

"""
SPOJ TOPOSORT - Topological Sorting

题目描述：
给定一个有向无环图，输出其字典序最小的拓扑排序。
如果不存在拓扑排序（图中有环），则输出"Sandro fails."

解题思路：
这道题要求输出字典序最小的拓扑排序，所以我们需要在Kahn算法的基础上做一些修改：
1. 使用优先队列（最小堆）而不是普通队列来存储入度为0的节点
2. 每次从优先队列中取出编号最小的节点
3. 如果最终结果的节点数小于图中节点总数，说明图中有环

算法步骤：
1. 计算每个节点的入度
2. 将所有入度为0的节点加入优先队列
3. 不断从优先队列中取出编号最小的节点，加入结果序列
4. 将该节点的所有邻居节点入度减1
5. 如果邻居节点入度变为0，则加入优先队列
6. 重复3-5直到队列为空
7. 检查结果序列长度是否等于节点总数

时间复杂度：O(V log V + E)
空间复杂度：O(V + E)

测试链接：https://www.spoj.com/problems/TOPOSORT/
"""

import heapq
from collections import defaultdict

def topological_sort_lexicographically(n, edges):
    """
    字典序最小的拓扑排序
    :param n: 节点数量
    :param edges: 边的列表，每个元素为(from, to)
    :return: 拓扑排序结果（字典序最小）
    """
    # 构建邻接表和入度数组
    graph = defaultdict(list)
    in_degree = [0] * (n + 1)
    
    # 建图
    for from_node, to_node in edges:
        graph[from_node].append(to_node)
        in_degree[to_node] += 1
    
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
    # 读取输入
    n, m = map(int, input().split())
    
    edges = []
    # 读取边的信息
    for _ in range(m):
        from_node, to_node = map(int, input().split())
        edges.append((from_node, to_node))
    
    # 拓扑排序（字典序最小）
    result = topological_sort_lexicographically(n, edges)
    
    # 输出结果
    if len(result) != n:
        print("Sandro fails.")
    else:
        print(' '.join(map(str, result)))

if __name__ == "__main__":
    main()
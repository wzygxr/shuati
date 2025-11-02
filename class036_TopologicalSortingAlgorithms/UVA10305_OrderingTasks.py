#!/usr/bin/env python3

"""
UVA 10305 - Ordering Tasks

题目描述：
给定n个任务和m个任务之间的先后顺序关系，要求输出一个满足所有约束条件的任务执行顺序。

解题思路：
这是一道经典的拓扑排序模板题。我们可以使用Kahn算法来解决：
1. 计算每个节点的入度
2. 将所有入度为0的节点加入队列
3. 不断从队列中取出节点，将其加入结果序列，并将其所有邻居节点的入度减1
4. 如果邻居节点的入度变为0，则将其加入队列
5. 重复步骤3-4直到队列为空

时间复杂度：O(V + E)，其中V是节点数，E是边数
空间复杂度：O(V + E)

测试链接：https://vjudge.net/problem/UVA-10305
"""

from collections import deque, defaultdict

def topological_sort(n, edges):
    """
    拓扑排序函数
    :param n: 节点数量
    :param edges: 边的列表，每个元素为(u, v)表示u必须在v之前执行
    :return: 拓扑排序结果
    """
    # 构建邻接表和入度数组
    graph = defaultdict(list)
    in_degree = [0] * (n + 1)
    
    # 建图
    for u, v in edges:
        graph[u].append(v)
        in_degree[v] += 1
    
    # 将所有入度为0的节点加入队列
    queue = deque()
    for i in range(1, n + 1):
        if in_degree[i] == 0:
            queue.append(i)
    
    result = []
    
    # Kahn算法进行拓扑排序
    while queue:
        current = queue.popleft()
        result.append(current)
        
        # 遍历当前节点的所有邻居
        for neighbor in graph[current]:
            # 将邻居节点的入度减1
            in_degree[neighbor] -= 1
            # 如果邻居节点的入度变为0，则加入队列
            if in_degree[neighbor] == 0:
                queue.append(neighbor)
    
    return result

def main():
    while True:
        line = input().strip()
        if not line:
            break
            
        n, m = map(int, line.split())
        
        # 输入结束条件
        if n == 0 and m == 0:
            break
        
        edges = []
        # 读取约束关系
        for _ in range(m):
            u, v = map(int, input().split())
            edges.append((u, v))
        
        # 拓扑排序
        result = topological_sort(n, edges)
        
        # 输出结果
        print(' '.join(map(str, result)))

if __name__ == "__main__":
    try:
        main()
    except EOFError:
        pass
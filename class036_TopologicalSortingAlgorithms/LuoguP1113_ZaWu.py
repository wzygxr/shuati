#!/usr/bin/env python3

"""
洛谷 P1113 杂务

题目描述：
John的农场在给牛奶加工时，要把一些杂务完成。这些杂务可以形成一个有向无环图，
每个杂务都有一个完成时间，某些杂务必须在一些杂务完成之后才能进行。
请你帮John计算一下完成所有杂务需要的最少时间。

解题思路：
这道题是最长路径的拓扑排序问题。每个杂务都有一个执行时间，我们需要计算从开始
到完成所有杂务的最短时间，也就是所有杂务完成时间的最大值。

算法步骤：
1. 使用拓扑排序处理依赖关系
2. 对于每个节点，计算其最早开始时间 = max(所有前驱节点的完成时间)
3. 节点的完成时间 = 最早开始时间 + 执行时间
4. 所有节点完成时间的最大值就是答案

时间复杂度：O(V + E)
空间复杂度：O(V + E)

测试链接：https://www.luogu.com.cn/problem/P1113
"""

from collections import deque, defaultdict

def topological_sort_for_latest_time(n, times, dependencies):
    """
    拓扑排序计算完成所有杂务的最长时间
    :param n: 杂务数量
    :param times: 每个杂务的执行时间
    :param dependencies: 依赖关系，dependencies[i]表示杂务i依赖的所有杂务
    :return: 完成所有杂务的最短时间
    """
    # 构建邻接表和入度数组
    graph = defaultdict(list)
    in_degree = [0] * (n + 1)
    
    # 建图
    for i in range(1, n + 1):
        for dep in dependencies[i]:
            graph[dep].append(i)
            in_degree[i] += 1
    
    # 每个杂务的最早开始时间和完成时间
    earliest_start = [0] * (n + 1)
    finish_time = [0] * (n + 1)
    
    # 将所有入度为0的节点加入队列
    queue = deque()
    for i in range(1, n + 1):
        if in_degree[i] == 0:
            queue.append(i)
    
    max_finish_time = 0
    
    # Kahn算法进行拓扑排序
    while queue:
        # 取出当前节点
        current = queue.popleft()
        
        # 计算当前节点的完成时间
        finish_time[current] = earliest_start[current] + times[current]
        max_finish_time = max(max_finish_time, finish_time[current])
        
        # 遍历当前节点的所有邻居
        for neighbor in graph[current]:
            # 更新邻居节点的最早开始时间
            earliest_start[neighbor] = max(earliest_start[neighbor], finish_time[current])
            
            # 将邻居节点的入度减1
            in_degree[neighbor] -= 1
            # 如果邻居节点的入度变为0，则加入队列
            if in_degree[neighbor] == 0:
                queue.append(neighbor)
    
    return max_finish_time

def main():
    # 读取输入
    n = int(input())
    
    times = [0] * (n + 1)
    dependencies = [[] for _ in range(n + 1)]
    
    # 读取每个杂务的信息
    for _ in range(n):
        line = list(map(int, input().split()))
        id_ = line[0]
        times[id_] = line[1]
        
        # 读取依赖的杂务编号，以0结尾
        i = 2
        while line[i] != 0:
            dependencies[id_].append(line[i])
            i += 1
    
    # 拓扑排序计算最早完成时间
    result = topological_sort_for_latest_time(n, times, dependencies)
    
    # 输出结果
    print(result)

if __name__ == "__main__":
    main()
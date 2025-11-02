#!/usr/bin/env python3

"""
POJ 1094 - Sorting It All Out

题目描述：
给定n个大写字母和m个关系，每个关系形如"A<B"，表示A在B前面。
要求判断在第几个关系后可以唯一确定一个拓扑序列，或者发现矛盾，或者始终无法确定。

解题思路：
这道题需要逐步添加关系并检查状态：
1. 对于每个新添加的关系，检查是否产生矛盾（形成环）
2. 如果产生矛盾，输出在第几个关系发现的矛盾
3. 如果没有矛盾，检查是否能唯一确定拓扑序列
4. 如果能唯一确定，输出序列
5. 如果处理完所有关系仍然无法确定，输出无法确定

关键点：
1. 每次添加关系后都要重新进行拓扑排序
2. 判断唯一性：在拓扑排序过程中，如果某一步有多个入度为0的节点，说明不唯一
3. 判断矛盾：拓扑排序后，如果结果序列长度小于n，说明有环

时间复杂度：O(m * (n + m))，每次都要进行一次拓扑排序
空间复杂度：O(n + m)

测试链接：http://poj.org/problem?id=1094
"""

import sys

def topological_sort(graph, in_degree, n):
    """
    拓扑排序并判断状态
    :param graph: 邻接矩阵
    :param in_degree: 入度数组
    :param n: 节点数量
    :return: (type, result) type: -1表示有矛盾，0表示无法确定，1表示唯一确定
    """
    temp_in_degree = in_degree[:]
    result = []
    
    while len(result) < n:
        # 查找入度为0的节点
        zero_nodes = []
        for i in range(n):
            if temp_in_degree[i] == 0:
                zero_nodes.append(i)
        
        # 如果没有入度为0的节点，说明有环（矛盾）
        if not zero_nodes:
            return (-1, [])
        
        # 如果有多个入度为0的节点，说明无法唯一确定
        if len(zero_nodes) > 1:
            return (0, [])
        
        # 只有一个入度为0的节点，可以确定当前位置
        zero_node = zero_nodes[0]
        result.append(zero_node)
        temp_in_degree[zero_node] = -1  # 标记为已处理
        
        # 更新邻居节点的入度
        for i in range(n):
            if graph[zero_node][i] == 1:
                temp_in_degree[i] -= 1
    
    # 成功生成完整的拓扑序列
    return (1, result)

def main():
    for line in sys.stdin:
        parts = line.strip().split()
        if not parts:
            continue
            
        n, m = int(parts[0]), int(parts[1])
        
        # 输入结束条件
        if n == 0 and m == 0:
            break
        
        # 初始化
        graph = [[0] * n for _ in range(n)]
        in_degree = [0] * n
        
        relations = []
        for _ in range(m):
            relations.append(input().strip())
        
        determined = False    # 是否已经确定顺序
        inconsistent = False  # 是否存在矛盾
        
        # 逐步添加关系并检查状态
        for i in range(m):
            # 添加新关系
            relation = relations[i]
            from_node = ord(relation[0]) - ord('A')
            to_node = ord(relation[2]) - ord('A')
            if graph[from_node][to_node] == 0:  # 避免重复添加
                graph[from_node][to_node] = 1
                in_degree[to_node] += 1
            
            # 检查当前状态
            type_code, result = topological_sort(graph, in_degree, n)
            
            if type_code == -1:  # 发现矛盾
                print(f"Inconsistency found after {i + 1} relations.")
                inconsistent = True
                break
            elif type_code == 1:  # 唯一确定
                sequence = ''.join(chr(x + ord('A')) for x in result)
                print(f"Sorted sequence determined after {i + 1} relations: {sequence}.")
                determined = True
                break
            # type_code == 0 表示还无法确定，继续处理
        
        # 处理完所有关系仍未确定或矛盾
        if not determined and not inconsistent:
            print("Sorted sequence cannot be determined.")

if __name__ == "__main__":
    try:
        main()
    except EOFError:
        pass
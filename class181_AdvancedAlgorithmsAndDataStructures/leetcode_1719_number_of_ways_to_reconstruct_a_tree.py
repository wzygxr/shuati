#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1719 Number Of Ways To Reconstruct A Tree

题目描述：
给你一个数组 pairs ，其中 pairs[i] = [xi, yi] ，并且满足：
- pairs 中没有重复元素
- xi < yi

请你构造一个合法的 rooted tree，使得对于 pairs 中的每个 [xi, yi]，
xi 是 yi 的祖先或者 yi 是 xi 的祖先。

返回以下三者之一：
- 如果不可能构造出这样的树，返回 0。
- 如果可以构造出这样的树，且构造方案唯一，返回 1。
- 如果可以构造出这样的树，且构造方案不唯一，返回 2。

解题思路：
这是一个图论问题，我们需要分析给定的节点对关系来判断是否能构造出合法的树。
1. 构建图并计算每个节点的度数
2. 找到可能的根节点（度数最大的节点）
3. 验证树的构造是否合法
4. 判断构造方案是否唯一

时间复杂度：O(n^2)
空间复杂度：O(n^2)
"""

class Solution:
    def check_ways(self, pairs):
        """
        检查重构树的方案数
        
        Args:
            pairs: 节点对列表，每个元素为[x, y]
            
        Returns:
            0表示无法构造，1表示唯一构造，2表示多种构造方案
        """
        # 找到所有节点
        nodes = set()
        for x, y in pairs:
            nodes.add(x)
            nodes.add(y)
        
        node_count = len(nodes)
        
        # 构建邻接矩阵
        adj = {node: set() for node in nodes}
        
        # 计算每个节点的度数
        degree = {node: 0 for node in nodes}
        
        for x, y in pairs:
            adj[x].add(y)
            adj[y].add(x)
            degree[x] += 1
            degree[y] += 1
        
        # 找到度数最大的节点作为根
        root = max(nodes, key=lambda x: degree[x])
        
        # 检查根节点的度数是否为node_count-1
        if degree[root] != node_count - 1:
            return 0  # 无法构造树
        
        # 验证每个节点
        result = 1  # 假设构造方案唯一
        
        for node in nodes:
            if node == root:
                continue
            
            # 找到节点node的父节点
            parent = -1
            parent_degree = node_count + 1
            
            for neighbor in adj[node]:
                if degree[neighbor] < parent_degree and degree[neighbor] >= degree[node]:
                    parent = neighbor
                    parent_degree = degree[neighbor]
            
            # 检查节点node是否与父节点有直接连接
            if parent == -1:
                return 0  # 无法构造树
            
            # 检查节点node的邻居是否都是父节点的邻居
            for neighbor in adj[node]:
                if neighbor != parent and neighbor not in adj[parent]:
                    return 0  # 无法构造树
            
            # 检查是否有度数相同的节点（可能导致构造方案不唯一）
            if degree[parent] == degree[node]:
                result = 2  # 构造方案不唯一
        
        return result


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    pairs1 = [[1,2],[2,3]]
    print("测试用例1:")
    print("节点对:", pairs1)
    print("结果:", solution.check_ways(pairs1))
    print()
    
    # 测试用例2
    pairs2 = [[1,2],[2,3],[1,3]]
    print("测试用例2:")
    print("节点对:", pairs2)
    print("结果:", solution.check_ways(pairs2))
    print()
    
    # 测试用例3
    pairs3 = [[1,2],[2,3],[2,4],[1,5]]
    print("测试用例3:")
    print("节点对:", pairs3)
    print("结果:", solution.check_ways(pairs3))


if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1494. Parallel Courses II

题目描述：
给你一个整数 n 表示某所大学里课程的数目，编号为 1 到 n，
还给你一个数组 relations，其中 relations[i] = [xi, yi] 表示课程 xi 必须在课程 yi 之前上。
在一个学期中，你最多可以同时上 k 门课，前提是这些课的先修课在之前的学期中都已上过。
请你返回上完所有课最少需要多少个学期。

解题思路：
这个问题可以使用动态规划和位运算来解决。
我们使用状态压缩动态规划，其中 dp[mask] 表示完成课程集合 mask 所需的最少学期数。

时间复杂度：O(2^n * n)
空间复杂度：O(2^n)
"""

class Solution:
    def minNumberOfSemesters(self, n, relations, k):
        """
        计算完成所有课程所需的最少学期数
        
        Args:
            n: 课程总数
            relations: 课程依赖关系列表
            k: 每学期最多可上的课程数
            
        Returns:
            完成所有课程所需的最少学期数
        """
        # 构建先修课程依赖关系
        prerequisites = [0] * n
        for prev, next in relations:
            prerequisites[next-1] |= (1 << (prev-1))  # 转换为0-indexed
        
        # dp[mask] 表示完成课程集合mask所需的最少学期数
        dp = [n] * (1 << n)  # 初始化为最大值
        dp[0] = 0  # 不需要上任何课程时，学期数为0
        
        # 对于每个课程集合
        for mask in range(1 << n):
            if dp[mask] == n:
                continue  # 跳过无法达到的状态
            
            # 计算当前可以学习的课程（先修课程已完成的课程）
            available = 0
            for i in range(n):
                # 如果课程i还没有学过，并且其先修课程都已完成
                if not (mask & (1 << i)) and (prerequisites[i] & mask) == prerequisites[i]:
                    available |= (1 << i)
            
            # 枚举available的所有非空子集作为本学期的学习课程
            subset = available
            while subset > 0:
                # 检查子集大小是否不超过k
                if bin(subset).count('1') <= k:
                    new_mask = mask | subset
                    dp[new_mask] = min(dp[new_mask], dp[mask] + 1)
                subset = (subset - 1) & available
        
        return dp[(1 << n) - 1]  # 返回完成所有课程的最少学期数


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    print("测试用例1:")
    n1 = 4
    relations1 = [[2,1],[3,1],[1,4]]
    k1 = 2
    print(f"课程数: {n1}")
    print(f"依赖关系: {relations1}")
    print(f"每学期最多课程数: {k1}")
    print(f"最少学期数: {solution.minNumberOfSemesters(n1, relations1, k1)}")
    print()
    
    # 测试用例2
    print("测试用例2:")
    n2 = 5
    relations2 = [[2,1],[3,1],[4,1],[1,5]]
    k2 = 2
    print(f"课程数: {n2}")
    print(f"依赖关系: {relations2}")
    print(f"每学期最多课程数: {k2}")
    print(f"最少学期数: {solution.minNumberOfSemesters(n2, relations2, k2)}")
    print()
    
    # 测试用例3
    print("测试用例3:")
    n3 = 11
    relations3 = []
    k3 = 2
    print(f"课程数: {n3}")
    print(f"依赖关系: {relations3}")
    print(f"每学期最多课程数: {k3}")
    print(f"最少学期数: {solution.minNumberOfSemesters(n3, relations3, k3)}")


if __name__ == "__main__":
    main()
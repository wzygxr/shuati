#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
IPO问题 - 贪心算法 + 优先队列

题目描述：
给你n个项目，对于每个项目i，它都有一个纯利润profits[i]和启动该项目需要的最小资本capital[i]。
最初你的资本为w，当你完成一个项目时，你将获得纯利润，添加到你的总资本中。
总而言之，从给定项目中选择最多k个不同项目的列表，以最大化最终资本，并输出最终可获得的最多资本。

解题思路：
1. 使用两个优先队列：
   - 小根堆heap1：按启动资金排序，存储当前无法启动的项目（被锁住的项目）
   - 大根堆heap2：按利润排序，存储当前可以启动的项目（被解锁的项目）
2. 贪心策略：在每一步选择当前可启动项目中利润最大的项目
3. 循环k次，每次：
   - 将heap1中所有可启动的项目转移到heap2
   - 从heap2中选择利润最大的项目执行

算法原理：
- 贪心选择性质：每次选择当前可执行项目中利润最大的项目是最优的
- 优先队列：高效维护可执行项目和不可执行项目的集合

时间复杂度：O(n*logn + k*logn) - 排序和k次操作的时间复杂度
空间复杂度：O(n) - 两个优先队列的空间

相关题目：
- LeetCode 502: https://leetcode.cn/problems/ipo/
- 资源分配问题的经典变种
"""

import heapq

def findMaximizedCapital(k, w, profits, capital):
    """
    计算最终可获得的最多资本
    
    Args:
        k: 最多可完成的项目数
        w: 初始资本
        profits: 项目利润数组
        capital: 项目启动资金数组
    
    Returns:
        最终可获得的最多资本
    """
    n = len(profits)
    
    # 需要的启动金小根堆（存储索引，按启动资金排序）
    # 代表被锁住的项目
    heap1 = [(capital[i], i) for i in range(n)]
    heapq.heapify(heap1)
    
    # 利润大根堆（存储负数模拟大根堆）
    # 代表被解锁的项目
    heap2 = []
    
    # 最多执行k个项目
    for _ in range(k):
        # 将所有当前可启动的项目从heap1转移到heap2
        while heap1 and heap1[0][0] <= w:
            cap, idx = heapq.heappop(heap1)
            heapq.heappush(heap2, -profits[idx])  # 存储负数模拟大根堆
        
        # 如果没有可启动的项目，结束循环
        if not heap2:
            break
        
        # 选择利润最大的项目执行
        w += -heapq.heappop(heap2)  # 取负数恢复原值
    
    return w

# 测试代码
if __name__ == "__main__":
    # 测试用例
    k = 2
    w = 0
    profits = [1, 2, 3]
    capital = [0, 1, 1]
    print(findMaximizedCapital(k, w, profits, capital))  # 应该输出 4
    
    k = 3
    w = 0
    profits = [1, 2, 3]
    capital = [0, 1, 2]
    print(findMaximizedCapital(k, w, profits, capital))  # 应该输出 6
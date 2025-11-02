#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Codeforces 612D. The Union of k-Segments

题目来源：https://codeforces.com/problemset/problem/612/D

题目描述：
给定n条线段和一个整数k，求被至少k条线段覆盖的区间的并集。

输入格式：
第一行包含两个整数n和k(1 ≤ n ≤ 10^6, 1 ≤ k ≤ n)。
接下来n行，每行包含两个整数li和ri(-10^9 ≤ li ≤ ri ≤ 10^9)，表示第i条线段的左右端点。

输出格式：
第一行输出一个整数m，表示结果区间的数量。
接下来m行，每行输出两个整数aj和bj，表示结果区间。

示例输入：
3 2
0 5
-3 2
3 8

示例输出：
2
-3 2
3 5

解题思路：
使用扫描线算法解决线段覆盖问题。核心思想是：
1. 将每个线段的左右端点转换为事件点
2. 对所有事件点按位置排序
3. 扫描所有事件点，维护当前覆盖的线段数量
4. 当覆盖数量从<k变为≥k时开始新区间，从≥k变为<k时结束区间

时间复杂度：O(n log n)，其中 n 是线段的数量
空间复杂度：O(n)

相关题目：
- LeetCode 56. 合并区间
- LeetCode 253. 会议室II
"""

import random
import time

class Solution:
    @staticmethod
    def union_of_k_segments(segments, k):
        """
        线段覆盖问题的扫描线解法
        :param segments: 线段数组，每个线段是 [left, right] 形式
        :param k: 覆盖线段的最小数量
        :return: 被至少k条线段覆盖的区间列表
        """
        if not segments or k <= 0:
            return []
        
        # 创建事件点列表：[位置, 类型]
        # 类型：0表示线段开始，1表示线段结束
        events = []
        
        # 为每个线段创建开始和结束事件
        for left, right in segments:
            events.append([left, 0])   # 开始事件
            events.append([right, 1])  # 结束事件
        
        # 按照位置排序事件点
        # 如果位置相同，结束事件优先于开始事件
        events.sort(key=lambda x: (x[0], -x[1]))
        
        result = []
        coverage_count = 0
        start = 0
        
        # 扫描所有事件点
        for position, event_type in events:
            if event_type == 0:
                # 线段开始事件
                coverage_count += 1
                if coverage_count == k:
                    # 开始新区间
                    start = position
            else:
                # 线段结束事件
                if coverage_count == k:
                    # 结束区间
                    result.append([start, position])
                coverage_count -= 1
        
        return result
    
    @staticmethod
    def test_union_of_k_segments():
        """测试线段覆盖问题解法"""
        print("=== Codeforces 612D. The Union of k-Segments ===")
        
        # 测试用例1
        print("测试用例1:")
        segments1 = [
            [0, 5], [-3, 2], [3, 8]
        ]
        k1 = 2
        result1 = Solution.union_of_k_segments(segments1, k1)
        print(f"输入线段: {segments1}")
        print(f"k = {k1}")
        print(f"输出区间数量: {len(result1)}")
        print("输出区间: ", end="")
        for interval in result1:
            print(f"[{interval[0]},{interval[1]}] ", end="")
        print()
        print("期望: 2个区间，[-3,2] [3,5]")
        print()
        
        # 测试用例2
        print("测试用例2:")
        segments2 = [
            [0, 10], [5, 15], [10, 20]
        ]
        k2 = 3
        result2 = Solution.union_of_k_segments(segments2, k2)
        print(f"输入线段: {segments2}")
        print(f"k = {k2}")
        print(f"输出区间数量: {len(result2)}")
        print("输出区间: ", end="")
        for interval in result2:
            print(f"[{interval[0]},{interval[1]}] ", end="")
        print()
        print("期望: 1个区间，[10,10]")
        print()
        
        # 性能测试
        print("=== 性能测试 ===")
        random.seed(42)
        n = 100000
        segments = []
        
        for _ in range(n):
            left = random.randint(-1000000, 1000000)
            right = left + random.randint(1, 10000)
            segments.append([left, right])
        
        k = 50000
        
        start_time = time.time()
        result = Solution.union_of_k_segments(segments, k)
        end_time = time.time()
        
        print(f"100000个随机线段，k=50000的覆盖计算完成")
        print(f"覆盖区间数量: {len(result)}")
        print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    Solution.test_union_of_k_segments()
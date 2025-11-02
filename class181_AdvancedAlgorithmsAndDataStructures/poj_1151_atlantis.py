#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 1151 Atlantis (矩形面积并)

题目来源：http://poj.org/problem?id=1151

题目描述：
给定一些矩形，求这些矩形的总面积（重叠部分只计算一次）。

输入格式：
输入包含多个测试用例。每个测试用例以一个整数n开始，表示矩形的数量。
接下来n行，每行包含四个实数x1, y1, x2, y2，表示一个矩形的左下角和右上角坐标。
当n=0时输入结束。

输出格式：
对于每个测试用例，输出一行"Test case #k"，其中k是测试用例编号。
然后输出一行"Total explored area: a"，其中a是总面积，保留两位小数。
每个测试用例后输出一个空行。

示例输入：
2
10 10 20 20
15 15 25 25.5
0

示例输出：
Test case #1
Total explored area: 180.00

解题思路：
使用扫描线算法解决矩形面积并问题。核心思想是：
1. 将每个矩形的左右边界转换为垂直扫描线事件
2. 对所有事件按x坐标排序
3. 在每个x区间内，计算y方向的覆盖长度
4. 累加每个区间的面积

时间复杂度：O(n^2 log n)，其中 n 是矩形的数量
空间复杂度：O(n)

相关题目：
- LeetCode 850. 矩形面积II
- LeetCode 218. 天际线问题
"""

import random
import time

class Solution:
    @staticmethod
    def rectangle_area_union(rectangles):
        """
        矩形面积并的扫描线解法
        :param rectangles: 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式
        :return: 覆盖的总面积
        """
        if not rectangles:
            return 0.0
        
        # 创建垂直扫描线事件
        events = []
        y_coordinates = set()
        
        for x1, y1, x2, y2 in rectangles:
            # 添加开始和结束事件
            events.append([x1, 0, y1, y2])  # 开始事件
            events.append([x2, 1, y1, y2])  # 结束事件
            
            # 收集所有y坐标
            y_coordinates.add(y1)
            y_coordinates.add(y2)
        
        # 排序事件
        events.sort()
        
        # 对y坐标排序
        sorted_y = sorted(y_coordinates)
        
        # 用于跟踪当前活动的矩形
        active_intervals = []
        total_area = 0.0
        prev_x = events[0][0]
        
        # 处理每个事件
        for event in events:
            current_x = event[0]
            width = current_x - prev_x
            
            if width > 0:
                # 计算当前活动的y区间总长度
                height = Solution._calculate_active_height(active_intervals, sorted_y)
                
                # 增加面积
                total_area += width * height
            
            # 更新活动区间
            if event[1] == 0:
                active_intervals.append([event[2], event[3]])
            else:
                active_intervals = [interval for interval in active_intervals 
                                  if interval != [event[2], event[3]]]
            
            prev_x = current_x
        
        return total_area
    
    @staticmethod
    def _calculate_active_height(active_intervals, sorted_y):
        """
        计算当前活动的y区间总长度
        :param active_intervals: 活动区间列表
        :param sorted_y: 排序后的y坐标
        :return: 总长度
        """
        if not active_intervals:
            return 0.0
        
        # 合并重叠的y区间
        intervals = sorted(active_intervals)
        
        total_height = 0.0
        current_start = intervals[0][0]
        current_end = intervals[0][1]
        
        for i in range(1, len(intervals)):
            if intervals[i][0] <= current_end:
                # 重叠，合并区间
                current_end = max(current_end, intervals[i][1])
            else:
                # 不重叠，计算长度并更新当前区间
                total_height += current_end - current_start
                current_start = intervals[i][0]
                current_end = intervals[i][1]
        
        # 加上最后一个区间
        total_height += current_end - current_start
        
        return total_height
    
    @staticmethod
    def test_rectangle_area_union():
        """测试矩形面积并解法"""
        print("=== POJ 1151 Atlantis ===")
        
        # 测试用例1
        print("测试用例1:")
        rectangles1 = [
            [10, 10, 20, 20],
            [15, 15, 25, 25.5]
        ]
        result1 = Solution.rectangle_area_union(rectangles1)
        print(f"输入: {rectangles1}")
        print(f"输出: {result1:.2f}")
        print("期望: 180.00")
        print()
        
        # 测试用例2
        print("测试用例2:")
        rectangles2 = [
            [0, 0, 10, 10],
            [5, 5, 15, 15]
        ]
        result2 = Solution.rectangle_area_union(rectangles2)
        print(f"输入: {rectangles2}")
        print(f"输出: {result2:.2f}")
        print("期望: 175.00")
        print()
        
        # 性能测试
        print("=== 性能测试 ===")
        random.seed(42)
        n = 100
        rectangles = []
        
        for _ in range(n):
            x1 = random.random() * 1000
            y1 = random.random() * 1000
            x2 = x1 + random.random() * 100 + 1
            y2 = y1 + random.random() * 100 + 1
            rectangles.append([x1, y1, x2, y2])
        
        start_time = time.time()
        result = Solution.rectangle_area_union(rectangles)
        end_time = time.time()
        
        print(f"100个随机矩形的总面积计算完成")
        print(f"总面积: {result:.2f}")
        print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    Solution.test_rectangle_area_union()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 850. Rectangle Area II 解决方案

题目链接: https://leetcode.com/problems/rectangle-area-ii/
题目描述: 计算多个矩形的总面积（重叠部分只计算一次）
解题思路: 使用扫描线算法处理矩形重叠

时间复杂度: O(n^2 log n) - n个矩形
空间复杂度: O(n)
"""

from typing import List

class Solution:
    def rectangleArea(self, rectangles: List[List[int]]) -> int:
        """
        计算多个矩形的总面积（不重复计算重叠部分）
        
        Args:
            rectangles: 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式
            
        Returns:
            矩形覆盖的总面积
        """
        # 检查输入有效性
        if not rectangles:
            return 0
        
        # 创建扫描线事件
        events = []
        
        # 为每个矩形创建开始和结束事件
        for rect in rectangles:
            x1, y1, x2, y2 = rect
            
            # 添加开始和结束事件
            events.append([x1, 0, y1, y2])  # 开始事件
            events.append([x2, 1, y1, y2])  # 结束事件
        
        # 按照x坐标排序事件
        events.sort()
        
        # 用于跟踪当前活动的y区间
        active_intervals = []
        total_area = 0
        prev_x = events[0][0]
        
        # 处理每个事件
        for event in events:
            current_x, event_type, y1, y2 = event
            
            # 计算当前扫描线和前一条扫描线之间的面积
            if current_x > prev_x:
                # 计算当前活动的y区间的总长度
                active_length = self._calculate_active_length(active_intervals)
                # 面积 = 宽度 * 高度
                total_area += (current_x - prev_x) * active_length
            
            # 更新活动区间
            if event_type == 0:
                # 矩形开始事件，添加y区间
                active_intervals.append([y1, y2])
            else:
                # 矩形结束事件，移除对应的y区间
                active_intervals.remove([y1, y2])
            
            prev_x = current_x
        
        # 返回结果，对10^9 + 7取模
        return total_area % (10**9 + 7)
    
    def _calculate_active_length(self, intervals: List[List[int]]) -> int:
        """
        计算当前活动的y区间总长度
        
        Args:
            intervals: 活动的y区间列表
            
        Returns:
            总长度
        """
        if not intervals:
            return 0
        
        # 对区间按照起始位置排序
        sorted_intervals = sorted(intervals, key=lambda x: x[0])
        
        # 合并重叠的区间
        total_length = 0
        current_start = sorted_intervals[0][0]
        current_end = sorted_intervals[0][1]
        
        for i in range(1, len(sorted_intervals)):
            interval_start, interval_end = sorted_intervals[i]
            if interval_start <= current_end:
                # 重叠，合并区间
                current_end = max(current_end, interval_end)
            else:
                # 不重叠，计算长度并更新当前区间
                total_length += current_end - current_start
                current_start = interval_start
                current_end = interval_end
        
        # 加上最后一个区间
        total_length += current_end - current_start
        
        return total_length
    
    def rectangleAreaCoordinateCompression(self, rectangles: List[List[int]]) -> int:
        """
        使用坐标压缩方法计算矩形面积
        
        Args:
            rectangles: 矩形数组
            
        Returns:
            矩形覆盖的总面积
        """
        # 收集所有x和y坐标
        x_coords = set()
        y_coords = set()
        
        for rect in rectangles:
            x_coords.add(rect[0])
            x_coords.add(rect[2])
            y_coords.add(rect[1])
            y_coords.add(rect[3])
        
        # 排序坐标
        sorted_x = sorted(x_coords)
        sorted_y = sorted(y_coords)
        
        # 创建坐标映射
        x_map = {coord: i for i, coord in enumerate(sorted_x)}
        y_map = {coord: i for i, coord in enumerate(sorted_y)}
        
        # 创建网格标记数组
        grid = [[False for _ in range(len(sorted_y))] for _ in range(len(sorted_x))]
        
        # 标记被矩形覆盖的网格
        for rect in rectangles:
            x1, y1, x2, y2 = rect
            x1_idx = x_map[x1]
            x2_idx = x_map[x2]
            y1_idx = y_map[y1]
            y2_idx = y_map[y2]
            
            for i in range(x1_idx, x2_idx):
                for j in range(y1_idx, y2_idx):
                    grid[i][j] = True
        
        # 计算总面积
        total_area = 0
        for i in range(len(sorted_x) - 1):
            for j in range(len(sorted_y) - 1):
                if grid[i][j]:
                    width = sorted_x[i + 1] - sorted_x[i]
                    height = sorted_y[j + 1] - sorted_y[j]
                    total_area += width * height
        
        return total_area % (10**9 + 7)
    
    @staticmethod
    def test_solution():
        """测试方法"""
        solution = Solution()
        
        # 测试用例1
        rectangles1 = [
            [1, 1, 3, 3],
            [3, 1, 4, 2],
            [3, 2, 4, 4],
            [1, 3, 2, 4],
            [2, 3, 3, 4]
        ]
        
        print("=== 测试用例1 ===")
        print("输入矩形: ")
        for rect in rectangles1:
            print(f"  {rect}")
        
        result1 = solution.rectangleArea(rectangles1)
        print(f"扫描线算法结果: {result1}")
        
        result1_compressed = solution.rectangleAreaCoordinateCompression(rectangles1)
        print(f"坐标压缩算法结果: {result1_compressed}")
        
        # 测试用例2
        rectangles2 = [
            [1, 1, 2, 2],
            [2, 2, 3, 3]
        ]
        
        print("\n=== 测试用例2 ===")
        print("输入矩形: ")
        for rect in rectangles2:
            print(f"  {rect}")
        
        result2 = solution.rectangleArea(rectangles2)
        print(f"扫描线算法结果: {result2}")
        
        result2_compressed = solution.rectangleAreaCoordinateCompression(rectangles2)
        print(f"坐标压缩算法结果: {result2_compressed}")
        
        # 测试用例3：重叠矩形
        rectangles3 = [
            [0, 0, 2, 2],
            [1, 1, 3, 3]
        ]
        
        print("\n=== 测试用例3：重叠矩形 ===")
        print("输入矩形: ")
        for rect in rectangles3:
            print(f"  {rect}")
        
        result3 = solution.rectangleArea(rectangles3)
        print(f"扫描线算法结果: {result3}")
        
        result3_compressed = solution.rectangleAreaCoordinateCompression(rectangles3)
        print(f"坐标压缩算法结果: {result3_compressed}")

if __name__ == "__main__":
    Solution.test_solution()
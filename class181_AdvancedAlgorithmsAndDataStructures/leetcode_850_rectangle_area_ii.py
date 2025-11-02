#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 850. 矩形面积II (Rectangle Area II)

题目来源：https://leetcode.cn/problems/rectangle-area-ii/

题目描述：
我们给出了一个（轴对齐的）二维矩形列表 rectangles 。
对于 rectangle[i] = [x1, y1, x2, y2] ，其中 (x1, y1) 是矩形 i 左下角的坐标， (x2, y2) 是该矩形右上角的坐标。
计算平面中所有 rectangles 所覆盖的总面积。任何被两个或多个矩形覆盖的区域应只计算一次。
返回总面积。因为答案可能太大，返回 10^9 + 7 的模。

示例 1：
输入：rectangles = [[1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]]
输出：6
解释：如图所示，三个矩形代表三个矩形，红色的为绿色的为蓝色的为。它们共同覆盖了总面积为6的区域。

示例 2：
输入：rectangles = [[1,1,2,2],[2,2,3,3]]
输出：1
解释：如图所示，两个矩形之间没有重叠区域。

示例 3：
输入：rectangles = [[0,0,2,2],[1,0,2,3],[1,0,3,1]]
输出：6
解释：如图所示，三个矩形的总面积为6。

提示：
1 <= rectangles.length <= 200
rectangles[i].length == 4
0 <= xi1, yi1, xi2, yi2 <= 10^9

解题思路：
使用扫描线算法解决矩形面积问题。核心思想是：
1. 将每个矩形的左右边界转换为垂直扫描线事件
2. 对所有事件按x坐标排序
3. 在每个x区间内，计算y方向的覆盖长度
4. 累加每个区间的面积

时间复杂度：O(n^2 log n)，其中 n 是矩形的数量
空间复杂度：O(n)

相关题目：
- LeetCode 218. 天际线问题
- LeetCode 391. 完美矩形
"""

import random
import time

class Solution:
    MOD = 1000000007
    
    @staticmethod
    def rectangle_area(rectangles):
        """
        矩形面积II的扫描线解法
        :param rectangles: 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式
        :return: 覆盖的总面积
        """
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
        total_area = 0
        prev_x = events[0][0]
        
        # 处理每个事件
        for event in events:
            current_x = event[0]
            width = current_x - prev_x
            
            if width > 0:
                # 计算当前活动的y区间总长度
                height = Solution._calculate_active_height(active_intervals, sorted_y)
                
                # 增加面积
                total_area = (total_area + width * height) % Solution.MOD
            
            # 更新活动区间
            if event[1] == 0:
                active_intervals.append([event[2], event[3]])
            else:
                active_intervals = [interval for interval in active_intervals 
                                  if interval != [event[2], event[3]]]
            
            prev_x = current_x
        
        return int(total_area)
    
    @staticmethod
    def _calculate_active_height(active_intervals, sorted_y):
        """
        计算当前活动的y区间总长度
        :param active_intervals: 活动区间列表
        :param sorted_y: 排序后的y坐标
        :return: 总长度
        """
        if not active_intervals:
            return 0
        
        # 合并重叠的y区间
        intervals = sorted(active_intervals)
        
        total_height = 0
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
    def test_rectangle_area():
        """测试矩形面积II解法"""
        print("=== LeetCode 850. 矩形面积II ===")
        
        # 测试用例1
        print("测试用例1:")
        rectangles1 = [
            [1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]
        ]
        result1 = Solution.rectangle_area(rectangles1)
        print(f"输入: {rectangles1}")
        print(f"输出: {result1}")
        print("期望: 6")
        print()
        
        # 测试用例2
        print("测试用例2:")
        rectangles2 = [
            [1,1,2,2],[2,2,3,3]
        ]
        result2 = Solution.rectangle_area(rectangles2)
        print(f"输入: {rectangles2}")
        print(f"输出: {result2}")
        print("期望: 1")
        print()
        
        # 测试用例3
        print("测试用例3:")
        rectangles3 = [
            [0,0,2,2],[1,0,2,3],[1,0,3,1]
        ]
        result3 = Solution.rectangle_area(rectangles3)
        print(f"输入: {rectangles3}")
        print(f"输出: {result3}")
        print("期望: 6")
        print()
        
        # 性能测试
        print("=== 性能测试 ===")
        random.seed(42)
        n = 200
        rectangles = []
        
        for _ in range(n):
            x1 = random.randint(0, 1000)
            y1 = random.randint(0, 1000)
            x2 = x1 + random.randint(1, 100)
            y2 = y1 + random.randint(1, 100)
            rectangles.append([x1, y1, x2, y2])
        
        start_time = time.time()
        result = Solution.rectangle_area(rectangles)
        end_time = time.time()
        
        print(f"200个随机矩形的总面积计算完成")
        print(f"总面积: {result}")
        print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    Solution.test_rectangle_area()
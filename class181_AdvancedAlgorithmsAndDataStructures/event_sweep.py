#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
事件排序 - 时间扫描线算法实现 (Python版本)

算法思路：
时间扫描线算法是一种用于解决几何和调度问题的有效技术。
核心思想是将问题中的事件按时间排序，然后按顺序处理这些事件。

应用场景：
1. 计算几何：矩形面积、线段相交
2. 资源调度：会议室安排、任务调度
3. 图形学：可见性分析、遮挡处理

时间复杂度：O(n log n)
空间复杂度：O(n)
"""

import heapq
from collections import defaultdict

class Event:
    """事件类，用于时间扫描线算法"""
    
    def __init__(self, time, event_type, data):
        """
        构造函数
        :param time: 事件发生的时间
        :param event_type: 事件类型：0表示开始，1表示结束
        :param data: 事件关联的数据
        """
        self.time = time
        self.type = event_type
        self.data = data
    
    def __lt__(self, other):
        """用于事件排序"""
        # 首先按照时间排序
        if self.time != other.time:
            return self.time < other.time
        # 时间相同时，结束事件优先处理，避免重复计算
        return self.type > other.type

class EventSweep:
    """时间扫描线算法实现类"""
    
    @staticmethod
    def max_overlap(intervals):
        """
        区间覆盖问题：计算最多有多少个重叠的区间
        :param intervals: 区间数组，每个区间是 [start, end] 形式
        :return: 最大重叠数量
        """
        if not intervals:
            return 0
        
        events = []
        
        # 为每个区间创建开始和结束事件
        for start, end in intervals:
            events.append(Event(start, 0, 1))  # 开始事件
            events.append(Event(end, 1, 1))    # 结束事件
        
        # 按照时间排序事件
        events.sort()
        
        max_overlap = 0
        current_overlap = 0
        
        # 扫描所有事件
        for event in events:
            if event.type == 0:  # 开始事件
                current_overlap += 1
                max_overlap = max(max_overlap, current_overlap)
            else:  # 结束事件
                current_overlap -= 1
        
        return max_overlap
    
    @staticmethod
    def calculate_rectangle_area(rectangles):
        """
        扫描线算法解决矩形面积问题：计算多个矩形的总面积（不重复计算重叠部分）
        :param rectangles: 矩形数组，每个矩形是 [x1, y1, x2, y2] 形式，
                          其中(x1,y1)是左下顶点，(x2,y2)是右上顶点
        :return: 矩形覆盖的总面积
        """
        if not rectangles:
            return 0
        
        # 创建垂直扫描线事件
        events = []
        y_coordinates = set()
        
        for x1, y1, x2, y2 in rectangles:
            # 添加开始和结束事件
            events.append(Event(x1, 0, (y1, y2)))  # 开始事件
            events.append(Event(x2, 1, (y1, y2)))  # 结束事件
            
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
        prev_x = events[0].time
        
        # 处理每个事件
        for event in events:
            current_x = event.time
            width = current_x - prev_x
            
            if width > 0:
                # 计算当前活动的y区间总长度
                height = EventSweep._calculate_active_height(active_intervals, sorted_y)
                
                # 增加面积
                total_area += width * height
            
            # 更新活动区间
            if event.type == 0:
                active_intervals.append(event.data)
            else:
                active_intervals.remove(event.data)
            
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
    def test_event_sweep():
        """测试事件扫描线算法"""
        print("=== 测试事件扫描线算法 ===")
        
        # 测试区间重叠问题
        print("测试区间重叠问题:")
        intervals1 = [
            [1, 4], [2, 5], [3, 6], [7, 9]
        ]
        print(f"最大重叠数量: {EventSweep.max_overlap(intervals1)}")  # 应该是3
        
        intervals2 = [
            [1, 2], [3, 4], [5, 6]
        ]
        print(f"最大重叠数量: {EventSweep.max_overlap(intervals2)}")  # 应该是1
        
        # 测试矩形面积问题
        print("\n测试矩形面积计算:")
        rectangles1 = [
            [0, 0, 2, 2], [1, 1, 3, 3]
        ]
        print(f"矩形覆盖总面积: {EventSweep.calculate_rectangle_area(rectangles1)}")  # 应该是7
        
        rectangles2 = [
            [0, 0, 1, 1], [2, 2, 3, 3], [1, 1, 2, 2]
        ]
        print(f"矩形覆盖总面积: {EventSweep.calculate_rectangle_area(rectangles2)}")  # 应该是3
        
        # 性能测试
        print("\n=== 性能测试 ===")
        import random
        import time
        
        # 生成大量随机区间
        n = 10000
        intervals = []
        for _ in range(n):
            start = random.randint(0, 100000)
            end = start + random.randint(1, 1000)
            intervals.append([start, end])
        
        start_time = time.time()
        max_overlap = EventSweep.max_overlap(intervals)
        end_time = time.time()
        
        print(f"10000个随机区间的最大重叠数量: {max_overlap}")
        print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")
        
        # 生成大量随机矩形
        rectangles = []
        for _ in range(1000):
            x1 = random.randint(0, 1000)
            y1 = random.randint(0, 1000)
            x2 = x1 + random.randint(1, 100)
            y2 = y1 + random.randint(1, 100)
            rectangles.append([x1, y1, x2, y2])
        
        start_time = time.time()
        total_area = EventSweep.calculate_rectangle_area(rectangles)
        end_time = time.time()
        
        print(f"1000个随机矩形的总面积: {total_area}")
        print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    EventSweep.test_event_sweep()
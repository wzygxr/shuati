#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 759. 员工空闲时间 (Employee Free Time)

题目来源：https://leetcode.cn/problems/employee-free-time/

题目描述：
给定员工的 schedule 列表，表示每个员工的工作时间。
每个员工都有一个非重叠的时间段 Intervals 列表，这些时间段已经排好序。
返回表示所有员工的共同，有限时间段的列表，且该时间段需均为空闲时间，同时该时间段需要按升序排列。

示例 1：
输入：schedule = [[[1,2],[5,6]],[[1,3]],[[4,10]]]
输出：[[3,4]]
解释：在所有员工工作时间之外的共同空闲时间是[3,4]

示例 2：
输入：schedule = [[[1,3],[6,7]],[[2,4]],[[2,5],[9,12]]]
输出：[[5,6],[7,9]]

提示：
1 <= schedule.length , schedule[i].length <= 50
0 <= schedule[i].start < schedule[i].end <= 10^8

解题思路：
使用扫描线算法解决员工空闲时间问题。核心思想是：
1. 将所有员工的工作时间转换为事件点
2. 对所有事件点按时间排序
3. 扫描所有事件点，维护当前正在工作的员工数量
4. 当员工数量从大于0变为0时，开始空闲时间；从0变为大于0时，结束空闲时间

时间复杂度：O(n log n)，其中 n 是所有工作时间段的数量
空间复杂度：O(n)

相关题目：
- LeetCode 56. 合并区间
- LeetCode 253. 会议室II
"""

import random
import time

class Interval:
    def __init__(self, start=0, end=0):
        self.start = start
        self.end = end
    
    def __repr__(self):
        return f"[{self.start},{self.end}]"

class Solution:
    @staticmethod
    def employee_free_time(schedule):
        """
        员工空闲时间的扫描线解法
        :param schedule: 员工工作时间安排
        :return: 所有员工的共同空闲时间
        """
        if not schedule:
            return []
        
        # 创建事件点列表：[时间, 类型]
        # 类型：0表示工作开始，1表示工作结束
        events = []
        
        # 为每个工作时间段创建开始和结束事件
        for employee in schedule:
            for interval in employee:
                events.append([interval.start, 0])  # 开始事件
                events.append([interval.end, 1])    # 结束事件
        
        # 按照时间排序事件点
        # 如果时间相同，结束事件优先于开始事件
        events.sort(key=lambda x: (x[0], -x[1]))
        
        result = []
        working_count = 0
        free_start = 0
        
        # 扫描所有事件点
        for time_point, event_type in events:
            if event_type == 0:
                # 工作开始事件
                if working_count == 0 and free_start < time_point:
                    # 结束空闲时间
                    result.append(Interval(free_start, time_point))
                working_count += 1
            else:
                # 工作结束事件
                working_count -= 1
                if working_count == 0:
                    # 开始空闲时间
                    free_start = time_point
        
        return result
    
    @staticmethod
    def test_employee_free_time():
        """测试员工空闲时间解法"""
        print("=== LeetCode 759. 员工空闲时间 ===")
        
        # 测试用例1
        print("测试用例1:")
        schedule1 = [
            [Interval(1, 2), Interval(5, 6)],
            [Interval(1, 3)],
            [Interval(4, 10)]
        ]
        result1 = Solution.employee_free_time(schedule1)
        print(f"输入: {schedule1}")
        print(f"输出: {result1}")
        print("期望: [[3,4]]")
        print()
        
        # 测试用例2
        print("测试用例2:")
        schedule2 = [
            [Interval(1, 3), Interval(6, 7)],
            [Interval(2, 4)],
            [Interval(2, 5), Interval(9, 12)]
        ]
        result2 = Solution.employee_free_time(schedule2)
        print(f"输入: {schedule2}")
        print(f"输出: {result2}")
        print("期望: [[5,6],[7,9]]")
        print()
        
        # 性能测试
        print("=== 性能测试 ===")
        random.seed(42)
        schedule = []
        employee_count = 50
        intervals_per_employee = 50
        
        for i in range(employee_count):
            employee = []
            current_time = 0
            for j in range(intervals_per_employee):
                start = current_time + random.randint(0, 1000)
                end = start + random.randint(1, 1000)
                employee.append(Interval(start, end))
                current_time = end + random.randint(0, 1000)
            schedule.append(employee)
        
        start_time = time.time()
        result = Solution.employee_free_time(schedule)
        end_time = time.time()
        
        print(f"50个员工，每个员工50个工作时间段的空闲时间计算完成")
        print(f"共同空闲时间段数量: {len(result)}")
        print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    Solution.test_employee_free_time()
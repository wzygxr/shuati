#!/usr/bin/env python3
"""
LeetCode 253. 会议室II (Meeting Rooms II) - Python版本

题目来源：https://leetcode.cn/problems/meeting-rooms-ii/

解题思路：
使用扫描线算法解决会议室安排问题

时间复杂度：O(n log n)
空间复杂度：O(n)
"""

import time
import random
from typing import List

class Solution:
    def minMeetingRooms(self, intervals: List[List[int]]) -> int:
        """
        计算所需会议室数量的扫描线解法
        
        Args:
            intervals: 会议时间安排数组
            
        Returns:
            所需的最小会议室数量
        """
        if not intervals:
            return 0
        
        # 创建事件点列表：[时间, 类型]
        # 类型：0表示会议开始，1表示会议结束
        events = []
        
        # 为每个会议创建开始和结束事件
        for start, end in intervals:
            events.append([start, 0])  # 开始事件
            events.append([end, 1])    # 结束事件
        
        # 按照时间排序事件点
        # 如果时间相同，结束事件优先于开始事件
        events.sort(key=lambda x: (x[0], -x[1]))
        
        max_rooms = 0
        current_rooms = 0
        
        # 扫描所有事件点
        for time, event_type in events:
            if event_type == 0:
                # 会议开始事件
                current_rooms += 1
                max_rooms = max(max_rooms, current_rooms)
            else:
                # 会议结束事件
                current_rooms -= 1
        
        return max_rooms

def test_meeting_rooms_ii():
    """测试会议室II解法"""
    solution = Solution()
    
    print("=== LeetCode 253. 会议室II (Python版本) ===")
    
    # 测试用例1
    print("测试用例1:")
    intervals1 = [[0, 30], [5, 10], [15, 20]]
    result1 = solution.minMeetingRooms(intervals1)
    print(f"输入: {intervals1}")
    print(f"输出: {result1}")
    print("期望: 2")
    print()
    
    # 测试用例2
    print("测试用例2:")
    intervals2 = [[7, 10], [2, 4]]
    result2 = solution.minMeetingRooms(intervals2)
    print(f"输入: {intervals2}")
    print(f"输出: {result2}")
    print("期望: 1")
    print()
    
    # 性能测试
    print("=== 性能测试 ===")
    random.seed(42)
    n = 10000
    large_intervals = []
    
    for i in range(n):
        start = random.randint(0, 1000000)
        end = start + random.randint(1, 1000)
        large_intervals.append([start, end])
    
    start_time = time.time()
    large_result = solution.minMeetingRooms(large_intervals)
    end_time = time.time()
    
    print(f"{n}个会议的会议室计算完成")
    print(f"所需会议室数量: {large_result}")
    print(f"运行时间: {(end_time - start_time) * 1000:.2f} ms")
    
    # Python语言特性考量
    print("\n=== Python语言特性考量 ===")
    print("1. 使用列表推导式生成事件点")
    print("2. 使用lambda表达式进行自定义排序")
    print("3. 使用类型注解提高代码可读性")
    print("4. 使用f-string进行格式化输出")
    
    # 算法复杂度分析
    print("\n=== 算法复杂度分析 ===")
    print("时间复杂度: O(n log n)")
    print("  - 排序: O(n log n)")
    print("  - 扫描: O(n)")
    print("空间复杂度: O(n)")
    print("  - 存储事件点: O(n)")

if __name__ == "__main__":
    test_meeting_rooms_ii()
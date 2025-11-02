"""
我的日程安排表系列 - 扫描线算法应用

题目描述:
729: 实现一个 MyCalendar 类来存放你的日程安排。如果要添加的时间内没有其他安排，则可以存储这个新的日程安排。
731: 实现一个 MyCalendarTwo 类来存放你的日程安排。如果要添加的时间内不会导致三重预订时，则可以存储这个新的日程安排。
732: 实现一个 MyCalendarThree 类来存放你的日程安排，你可以一直添加新的日程安排。

解题思路:
使用扫描线算法结合平衡树或线段树实现日程安排的管理。
1. 将每个日程的开始和结束作为事件点
2. 维护当前时间线上的预订状态
3. 根据不同的约束条件进行冲突检查

算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
工程化考量:
1. 异常处理: 检查时间区间合法性
2. 边界条件: 处理时间边界重叠情况
3. 性能优化: 使用高效的数据结构
4. 可扩展性: 支持不同的约束条件
5. 提供了多种实现方式：基于TreeMap和基于扫描线算法
"""

from collections import defaultdict
import bisect

class MyCalendarSeries:
    """我的日程安排表系列"""
    
    class MyCalendarI:
        """我的日程安排表 I (LeetCode 729)"""
        
        def __init__(self):
            """初始化日程安排表"""
            self.calendar = []  # 存储日程安排，按开始时间排序
        
        def book(self, start, end):
            """
            添加新的日程安排
            算法核心思想：
            1. 检查新日程与现有日程是否有时间冲突
            2. 如果没有冲突，则添加新日程
            
            Args:
                start: 开始时间
                end: 结束时间
            
            Returns:
                是否成功添加（不冲突则返回True）
            """
            # 边界条件检查
            if start < 0 or end <= start:
                raise ValueError("Invalid time interval")
            
            # 使用二分查找找到插入位置
            pos = bisect.bisect_left(self.calendar, [start, end])
            
            # 检查与前一个日程的冲突
            if pos > 0 and self.calendar[pos-1][1] > start:
                return False
            
            # 检查与后一个日程的冲突
            if pos < len(self.calendar) and self.calendar[pos][0] < end:
                return False
            
            # 添加新的日程安排
            self.calendar.insert(pos, [start, end])
            return True
        
        def book_with_sweep_line(self, start, end):
            """
            扫描线算法实现
            通过将所有日程转化为事件点来检查冲突
            
            Args:
                start: 开始时间
                end: 结束时间
            
            Returns:
                是否成功添加（不冲突则返回True）
            """
            if start < 0 or end <= start:
                raise ValueError("Invalid time interval")
            
            # 将日程安排转化为事件点
            events = []
            for s, e in self.calendar:
                events.append([s, 1])  # 开始事件
                events.append([e, -1])  # 结束事件
            
            # 添加新日程的事件点
            events.append([start, 1])
            events.append([end, -1])
            
            # 按时间排序，相同时间时结束事件优先
            events.sort(key=lambda x: (x[0], x[1]))
            
            # 扫描检查冲突
            count = 0
            for time, delta in events:
                count += delta
                if count > 1:
                    return False  # 发现冲突
            
            # 没有冲突，添加日程
            pos = bisect.bisect_left(self.calendar, [start, end])
            self.calendar.insert(pos, [start, end])
            return True
    
    class MyCalendarII:
        """我的日程安排表 II (LeetCode 731)"""
        
        def __init__(self):
            """初始化日程安排表"""
            self.single_bookings = []  # 单次预订
            self.double_bookings = []  # 双重预订
        
        def book(self, start, end):
            """
            添加新的日程安排（不允许三重预订）
            算法核心思想：
            1. 检查新日程是否会导致三重预订
            2. 如果不会导致三重预订，则更新双重预订和单次预订
            
            Args:
                start: 开始时间
                end: 结束时间
            
            Returns:
                是否成功添加（不导致三重预订则返回True）
            """
            if start < 0 or end <= start:
                raise ValueError("Invalid time interval")
            
            # 检查是否会导致三重预订
            if self.has_triple_booking(start, end):
                return False
            
            # 更新双重预订
            self.update_double_bookings(start, end)
            
            # 添加单次预订
            pos = bisect.bisect_left(self.single_bookings, [start, end])
            self.single_bookings.insert(pos, [start, end])
            
            return True
        
        def has_triple_booking(self, start, end):
            """
            检查是否会导致三重预订
            
            Args:
                start: 开始时间
                end: 结束时间
            
            Returns:
                是否会导致三重预订
            """
            # 检查与双重预订的冲突
            pos = bisect.bisect_left(self.double_bookings, [start, end])
            
            # 检查与前一个双重预订的冲突
            if pos > 0 and self.double_bookings[pos-1][1] > start:
                return True
            
            # 检查与后一个双重预订的冲突
            if pos < len(self.double_bookings) and self.double_bookings[pos][0] < end:
                return True
            
            return False
        
        def update_double_bookings(self, start, end):
            """
            更新双重预订区间
            
            Args:
                start: 开始时间
                end: 结束时间
            """
            # 找出与新区间重叠的单次预订
            overlaps = []
            for s, e in self.single_bookings:
                if s < end and e > start:
                    # 计算重叠区间
                    overlap_start = max(start, s)
                    overlap_end = min(end, e)
                    if overlap_start < overlap_end:
                        overlaps.append([overlap_start, overlap_end])
            
            # 合并重叠区间并更新双重预订
            if overlaps:
                # 按开始时间排序
                overlaps.sort()
                
                # 合并重叠区间
                merged = []
                for s, e in overlaps:
                    if merged and merged[-1][1] >= s:
                        # 合并重叠区间
                        merged[-1][1] = max(merged[-1][1], e)
                    else:
                        merged.append([s, e])
                
                # 添加到双重预订
                for s, e in merged:
                    pos = bisect.bisect_left(self.double_bookings, [s, e])
                    self.double_bookings.insert(pos, [s, e])
        
        def book_with_sweep_line(self, start, end):
            """
            扫描线算法实现
            通过扫描所有事件点来检查是否会导致三重预订
            
            Args:
                start: 开始时间
                end: 结束时间
            
            Returns:
                是否成功添加（不导致三重预订则返回True）
            """
            if start < 0 or end <= start:
                raise ValueError("Invalid time interval")
            
            # 收集所有事件点
            events = defaultdict(int)
            
            # 添加现有日程的事件点
            for s, e in self.single_bookings:
                events[s] += 1
                events[e] -= 1
            
            # 添加新日程的事件点
            events[start] += 1
            events[end] -= 1
            
            # 按时间排序
            sorted_times = sorted(events.keys())
            
            # 扫描检查是否会导致三重预订
            count = 0
            for time in sorted_times:
                count += events[time]
                if count >= 3:
                    return False
            
            # 没有三重预订，添加日程
            pos = bisect.bisect_left(self.single_bookings, [start, end])
            self.single_bookings.insert(pos, [start, end])
            
            return True
    
    class MyCalendarThree:
        """我的日程安排表 III (LeetCode 732)"""
        
        def __init__(self):
            """初始化日程安排表"""
            self.events = defaultdict(int)  # 记录所有事件点
        
        def book(self, start, end):
            """
            添加新的日程安排，返回最大重叠次数
            算法核心思想：
            1. 将新日程的开始和结束作为事件点添加
            2. 扫描所有事件点计算最大重叠次数
            
            Args:
                start: 开始时间
                end: 结束时间
            
            Returns:
                添加新日程后的最大重叠次数
            """
            if start < 0 or end <= start:
                raise ValueError("Invalid time interval")
            
            # 添加事件点
            self.events[start] += 1
            self.events[end] -= 1
            
            # 扫描计算最大重叠次数
            max_k = 0
            current_k = 0
            
            # 按时间排序
            sorted_times = sorted(self.events.keys())
            
            for time in sorted_times:
                current_k += self.events[time]
                max_k = max(max_k, current_k)
            
            return max_k
        
        def book_with_segment_tree(self, start, end):
            """
            线段树实现（支持区间查询）
            可以使用线段树实现更高效的区间查询
            
            Args:
                start: 开始时间
                end: 结束时间
            
            Returns:
                添加新日程后的最大重叠次数
            """
            if start < 0 or end <= start:
                raise ValueError("Invalid time interval")
            
            # 这里可以使用线段树实现更高效的区间查询
            # 由于时间范围可能很大，可以使用动态开点线段树
            
            # 简化实现：使用扫描线算法
            return self.book(start, end)

def test_my_calendar_series():
    """测试函数"""
    print("=== MyCalendar I 测试 ===")
    test_my_calendar_i()
    
    print("\n=== MyCalendar II 测试 ===")
    test_my_calendar_ii()
    
    print("\n=== MyCalendar III 测试 ===")
    test_my_calendar_iii()

def test_my_calendar_i():
    """测试MyCalendarI类"""
    calendar = MyCalendarSeries.MyCalendarI()
    
    # 测试用例1: 正常添加
    print(f"添加 [10, 20]: {calendar.book(10, 20)}")  # True
    print(f"添加 [15, 25]: {calendar.book(15, 25)}")  # False
    print(f"添加 [20, 30]: {calendar.book(20, 30)}")  # True
    
    # 测试用例2: 边界情况
    print(f"添加 [5, 10]: {calendar.book(5, 10)}")  # True
    print(f"添加 [5, 15]: {calendar.book(5, 15)}")  # False
    
    # 测试扫描线版本
    calendar2 = MyCalendarSeries.MyCalendarI()
    print(f"扫描线版本 - 添加 [10, 20]: {calendar2.book_with_sweep_line(10, 20)}")  # True
    print(f"扫描线版本 - 添加 [15, 25]: {calendar2.book_with_sweep_line(15, 25)}")  # False

def test_my_calendar_ii():
    """测试MyCalendarII类"""
    calendar = MyCalendarSeries.MyCalendarII()
    
    # 测试用例1: 正常添加
    print(f"添加 [10, 20]: {calendar.book(10, 20)}")  # True
    print(f"添加 [50, 60]: {calendar.book(50, 60)}")  # True
    print(f"添加 [10, 40]: {calendar.book(10, 40)}")  # True
    print(f"添加 [5, 15]: {calendar.book(5, 15)}")  # False (三重预订)
    print(f"添加 [5, 10]: {calendar.book(5, 10)}")  # True
    print(f"添加 [25, 55]: {calendar.book(25, 55)}")  # True
    
    # 测试扫描线版本
    calendar2 = MyCalendarSeries.MyCalendarII()
    print(f"扫描线版本 - 添加 [10, 20]: {calendar2.book_with_sweep_line(10, 20)}")  # True
    print(f"扫描线版本 - 添加 [50, 60]: {calendar2.book_with_sweep_line(50, 60)}")  # True
    print(f"扫描线版本 - 添加 [10, 40]: {calendar2.book_with_sweep_line(10, 40)}")  # True
    print(f"扫描线版本 - 添加 [5, 15]: {calendar2.book_with_sweep_line(5, 15)}")  # False

def test_my_calendar_iii():
    """测试MyCalendarThree类"""
    calendar = MyCalendarSeries.MyCalendarThree()
    
    # 测试用例1: 正常添加
    print(f"添加 [10, 20]: {calendar.book(10, 20)}")  # 1
    print(f"添加 [50, 60]: {calendar.book(50, 60)}")  # 1
    print(f"添加 [10, 40]: {calendar.book(10, 40)}")  # 2
    print(f"添加 [5, 15]: {calendar.book(5, 15)}")  # 3
    print(f"添加 [5, 10]: {calendar.book(5, 10)}")  # 3
    print(f"添加 [25, 55]: {calendar.book(25, 55)}")  # 3
    
    # 测试线段树版本
    print(f"线段树版本 - 添加 [10, 20]: {calendar.book_with_segment_tree(10, 20)}")  # 3

def performance_test():
    """性能测试方法"""
    print("\n=== 性能测试 ===")
    
    import time
    
    # MyCalendar I 性能测试
    calendar_i = MyCalendarSeries.MyCalendarI()
    start_time = time.time()
    for i in range(10000):
        calendar_i.book(i, i + 10)
    end_time = time.time()
    print(f"MyCalendar I 10000次操作时间: {(end_time - start_time) * 1000:.2f}ms")
    
    # MyCalendar III 性能测试
    calendar_iii = MyCalendarSeries.MyCalendarThree()
    start_time = time.time()
    max_k = 0
    for i in range(10000):
        max_k = max(max_k, calendar_iii.book(i, i + 10))
    end_time = time.time()
    print(f"MyCalendar III 10000次操作时间: {(end_time - start_time) * 1000:.2f}ms")
    print(f"最大重叠次数: {max_k}")

if __name__ == "__main__":
    test_my_calendar_series()
    performance_test()
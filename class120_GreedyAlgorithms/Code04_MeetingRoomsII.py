"""
会议室II - 贪心算法 + 最小堆解决方案（Python实现）

题目描述：
给你一个会议时间安排的数组 intervals
每个会议时间都会包括开始和结束的时间intervals[i]=[starti, endi]
返回所需会议室的最小数量

测试链接：https://leetcode.cn/problems/meeting-rooms-ii/

算法思想：
贪心算法 + 最小堆（优先队列）
1. 按照会议开始时间对会议进行排序
2. 使用最小堆来存储当前正在进行的会议的结束时间
3. 对于每个会议：
   - 如果堆顶的会议已经结束（结束时间 <= 当前会议开始时间），则弹出堆顶
   - 将当前会议的结束时间加入堆中
   - 更新所需会议室的最大数量

时间复杂度分析：
O(n*logn) - 排序需要O(n*logn)，每个会议进出堆一次需要O(logn)

空间复杂度分析：
O(n) - 最坏情况下所有会议都需要同时进行，堆的大小为n

是否为最优解：
是，这是解决该问题的最优解

工程化考量：
1. 边界条件处理：处理空数组、单个会议等特殊情况
2. 输入验证：检查会议时间是否有效（开始时间 < 结束时间）
3. 异常处理：对非法输入进行检查
4. 可读性：使用清晰的变量命名和详细的注释

贪心策略证明：
每次选择最早结束的会议室来安排新会议，这样可以最大化会议室的使用效率
这种策略可以保证所需会议室数量最少
"""

import heapq
from typing import List

class Code04_MeetingRoomsII:
    
    @staticmethod
    def min_meeting_rooms(intervals: List[List[int]]) -> int:
        """
        计算所需会议室的最小数量
        
        Args:
            intervals: 会议时间数组，每个元素是[starti, endi]
            
        Returns:
            所需会议室的最小数量
            
        算法步骤：
        1. 按照会议开始时间排序
        2. 使用最小堆存储当前会议的结束时间
        3. 遍历每个会议，释放已结束的会议室，分配新的会议室
        4. 跟踪所需会议室的最大数量
        """
        # 输入验证
        if not intervals:
            return 0
        
        n = len(intervals)
        
        # 按照会议开始时间排序
        intervals.sort(key=lambda x: x[0])
        
        # 最小堆，存储当前正在进行的会议的结束时间
        heap = []
        max_rooms = 0  # 所需会议室的最大数量
        
        for i in range(n):
            start, end = intervals[i]
            
            # 验证会议时间有效性
            if start >= end:
                raise ValueError("会议开始时间必须小于结束时间")
            
            # 释放已经结束的会议室（结束时间 <= 当前会议开始时间）
            while heap and heap[0] <= start:
                heapq.heappop(heap)
            
            # 分配新的会议室
            heapq.heappush(heap, end)
            
            # 更新所需会议室的最大数量
            max_rooms = max(max_rooms, len(heap))
        
        return max_rooms
    
    @staticmethod
    def debug_min_meeting_rooms(intervals: List[List[int]]) -> int:
        """
        调试版本：打印计算过程中的中间结果
        
        Args:
            intervals: 会议时间数组
            
        Returns:
            所需会议室的最小数量
        """
        if not intervals:
            print("空数组，不需要会议室")
            return 0
        
        n = len(intervals)
        
        print("原始会议安排:")
        for i in range(n):
            print(f"会议{i}: [{intervals[i][0]}, {intervals[i][1]}]")
        
        # 按照会议开始时间排序
        intervals.sort(key=lambda x: x[0])
        
        print("\n排序后的会议安排:")
        for i in range(n):
            print(f"会议{i}: [{intervals[i][0]}, {intervals[i][1]}]")
        
        heap = []
        max_rooms = 0
        
        print("\n分配过程:")
        for i in range(n):
            start, end = intervals[i]
            
            print(f"\n处理会议{i}: [{start}, {end}]")
            
            # 释放已经结束的会议室
            print("释放会议室: ", end="")
            released = False
            while heap and heap[0] <= start:
                finished = heapq.heappop(heap)
                print(f"结束时间{finished} ", end="")
                released = True
            if not released:
                print("无会议室可释放", end="")
            print()
            
            # 分配新的会议室
            heapq.heappush(heap, end)
            print(f"分配新会议室，当前会议室数量: {len(heap)}")
            
            # 更新最大数量
            if len(heap) > max_rooms:
                max_rooms = len(heap)
                print(f"更新最大会议室数量: {max_rooms}")
        
        print(f"\n最终结果: {max_rooms} 个会议室")
        return max_rooms
    
    @staticmethod
    def test_min_meeting_rooms():
        """
        测试函数：验证会议室分配算法的正确性
        """
        print("会议室II算法测试开始")
        print("===================")
        
        # 测试用例1: [[0,30],[5,10],[15,20]]
        intervals1 = [[0,30], [5,10], [15,20]]
        result1 = Code04_MeetingRoomsII.min_meeting_rooms(intervals1)
        print("输入: [[0,30],[5,10],[15,20]]")
        print("输出:", result1)
        print("预期: 2")
        print("✓ 通过" if result1 == 2 else "✗ 失败")
        print()
        
        # 测试用例2: [[7,10],[2,4]]
        intervals2 = [[7,10], [2,4]]
        result2 = Code04_MeetingRoomsII.min_meeting_rooms(intervals2)
        print("输入: [[7,10],[2,4]]")
        print("输出:", result2)
        print("预期: 1")
        print("✓ 通过" if result2 == 1 else "✗ 失败")
        print()
        
        # 测试用例3: 空数组
        intervals3 = []
        result3 = Code04_MeetingRoomsII.min_meeting_rooms(intervals3)
        print("输入: []")
        print("输出:", result3)
        print("预期: 0")
        print("✓ 通过" if result3 == 0 else "✗ 失败")
        print()
        
        # 测试用例4: [[1,5],[8,9],[8,9]]
        intervals4 = [[1,5], [8,9], [8,9]]
        result4 = Code04_MeetingRoomsII.min_meeting_rooms(intervals4)
        print("输入: [[1,5],[8,9],[8,9]]")
        print("输出:", result4)
        print("预期: 2")
        print("✓ 通过" if result4 == 2 else "✗ 失败")
        print()
        
        print("测试结束")
    
    @staticmethod
    def performance_test():
        """
        性能测试：测试算法在大规模数据下的表现
        """
        import time
        import random
        
        print("性能测试开始")
        print("============")
        
        # 生成大规模测试数据
        n = 10000
        intervals = []
        for i in range(n):
            start = i * 10
            end = start + random.randint(1, 10)
            intervals.append([start, end])
        
        start_time = time.time()
        result = Code04_MeetingRoomsII.min_meeting_rooms(intervals)
        end_time = time.time()
        
        print(f"数据规模: {n} 个会议")
        print(f"执行时间: {end_time - start_time:.4f} 秒")
        print(f"所需会议室数量: {result}")
        print("性能测试结束")
    
    @staticmethod
    def alternative_solution(intervals: List[List[int]]) -> int:
        """
        替代解法：使用双指针方法（事件排序法）
        
        算法思想：
        1. 分别提取所有会议的开始时间和结束时间
        2. 对开始时间和结束时间分别排序
        3. 使用双指针遍历，统计同时进行的会议数量
        
        时间复杂度：O(n*logn)
        空间复杂度：O(n)
        """
        if not intervals:
            return 0
        
        starts = [interval[0] for interval in intervals]
        ends = [interval[1] for interval in intervals]
        
        starts.sort()
        ends.sort()
        
        rooms = 0
        end_ptr = 0
        
        for start in starts:
            if start < ends[end_ptr]:
                rooms += 1
            else:
                end_ptr += 1
        
        return rooms


def main():
    """
    主函数：运行测试
    """
    print("会议室II - 贪心算法 + 最小堆解决方案（Python实现）")
    print("===========================================")
    
    # 运行基础测试
    Code04_MeetingRoomsII.test_min_meeting_rooms()
    
    print("\n调试模式示例:")
    debug_intervals = [[0,30], [5,10], [15,20]]
    print("对测试用例 [[0,30],[5,10],[15,20]] 进行调试跟踪:")
    debug_result = Code04_MeetingRoomsII.debug_min_meeting_rooms(debug_intervals)
    print("最终结果:", debug_result)
    
    print("\n算法分析:")
    print("- 时间复杂度: O(n*logn) - 排序需要O(n*logn)，每个会议进出堆一次需要O(logn)")
    print("- 空间复杂度: O(n) - 最坏情况下所有会议都需要同时进行，堆的大小为n")
    print("- 贪心策略: 每次选择最早结束的会议室来安排新会议")
    print("- 最优性: 这种贪心策略能够得到全局最优解")
    print("- Python特性: 使用heapq模块实现最小堆")
    
    # 可选：运行性能测试
    # print("\n性能测试:")
    # Code04_MeetingRoomsII.performance_test()
    
    # 可选：比较替代解法
    # print("\n替代解法测试:")
    # test_intervals = [[0,30], [5,10], [15,20]]
    # alt_result = Code04_MeetingRoomsII.alternative_solution(test_intervals)
    # print("替代解法结果:", alt_result)


if __name__ == "__main__":
    main()
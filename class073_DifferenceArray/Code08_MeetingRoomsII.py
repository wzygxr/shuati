import sys
from collections import defaultdict

"""
LeetCode 253. 会议室预定 (Meeting Rooms II)

题目描述:
给定一个会议时间安排的数组，每个会议时间都会包括开始和结束的时间 [[s1,e1],[s2,e2],...] (si < ei)，
为避免会议冲突，同时要考虑充分利用会议室资源，请你计算至少需要多少间会议室，才能满足这些会议安排。

示例1:
输入: [[0, 30],[5, 10],[15, 20]]
输出: 2
解释: 需要至少两个会议室，一个会议室举办 [0, 30]，另一个举办 [5, 10] 和 [15, 20]。

示例2:
输入: [[7,10],[2,4]]
输出: 1

提示:
- 所有输入的会议时间都是有效的，并且不会有重叠的时间区间。
- 输入的会议时间可能有任意顺序。

题目链接: https://leetcode.com/problems/meeting-rooms-ii/

解题思路:
方法一：差分数组法
1. 找出所有会议的最早开始时间和最晚结束时间
2. 创建一个差分数组，大小为最晚结束时间减去最早开始时间加1
3. 对于每个会议，在差分数组的开始时间位置加1，在结束时间位置减1
4. 计算差分数组的前缀和，前缀和的最大值就是所需的最少会议室数量

方法二：排序+扫描线法（使用字典实现，更高效地处理离散时间点）
1. 使用字典来记录每个时间点的会议数量变化
2. 对于每个会议，在开始时间加1，在结束时间减1
3. 按时间顺序遍历字典，累加会议数量，记录最大值

时间复杂度: O(n log n) - n是会议数量，主要来自排序时间
空间复杂度: O(n) - 需要存储时间点和对应的变化量

这是最优解，因为我们需要考虑所有会议的时间点，并且需要排序来按时间顺序处理。
"""

class Solution:
    """
    计算最少需要的会议室数量
    
    Args:
        intervals: 会议时间安排的数组，每个元素是[开始时间, 结束时间]的列表
    
    Returns:
        最少需要的会议室数量
    """
    def minMeetingRooms(self, intervals):
        # 边界情况处理
        if not intervals:
            return 0
        if len(intervals) == 1:
            return 1
        
        # 方法二：使用字典实现扫描线算法
        time_point_changes = defaultdict(int)
        
        # 对于每个会议，在开始时间增加1个会议室需求，在结束时间减少1个会议室需求
        for start, end in intervals:
            # 在开始时间增加1个会议室需求
            time_point_changes[start] += 1
            # 在结束时间减少1个会议室需求
            time_point_changes[end] -= 1
        
        # 按时间顺序计算同时进行的会议数量
        current_rooms = 0
        max_rooms = 0
        
        # 按时间顺序遍历
        for time in sorted(time_point_changes.keys()):
            # 更新当前使用的会议室数量
            current_rooms += time_point_changes[time]
            # 更新最大会议室数量
            max_rooms = max(max_rooms, current_rooms)
        
        return max_rooms
    
    """
    方法一：差分数组实现
    注意：此方法适用于时间范围较小的情况
    """
    def minMeetingRoomsWithDiffArray(self, intervals):
        if not intervals:
            return 0
        
        # 找出最早开始时间和最晚结束时间
        earliest_start = sys.maxsize
        latest_end = -sys.maxsize - 1
        
        for start, end in intervals:
            earliest_start = min(earliest_start, start)
            latest_end = max(latest_end, end)
        
        # 创建差分数组
        diff = [0] * (latest_end - earliest_start + 1)
        
        # 对每个会议进行差分标记
        for start, end in intervals:
            start_idx = start - earliest_start
            end_idx = end - earliest_start
            
            diff[start_idx] += 1  # 开始时间增加会议室需求
            if end_idx < len(diff):
                diff[end_idx] -= 1  # 结束时间减少会议室需求
        
        # 计算前缀和并找出最大值
        current_rooms = 0
        max_rooms = 0
        
        for i in range(len(diff)):
            current_rooms += diff[i]
            max_rooms = max(max_rooms, current_rooms)
        
        return max_rooms

# 测试代码
def main():
    solution = Solution()
    
    # 测试用例1
    intervals1 = [[0, 30], [5, 10], [15, 20]]
    result1 = solution.minMeetingRooms(intervals1)
    # 预期输出: 2
    print(f"测试用例1: {result1}")
    print(f"测试用例1 (差分数组): {solution.minMeetingRoomsWithDiffArray(intervals1)}")

    # 测试用例2
    intervals2 = [[7, 10], [2, 4]]
    result2 = solution.minMeetingRooms(intervals2)
    # 预期输出: 1
    print(f"测试用例2: {result2}")
    print(f"测试用例2 (差分数组): {solution.minMeetingRoomsWithDiffArray(intervals2)}")
    
    # 测试用例3
    intervals3 = [[1, 5], [8, 9], [8, 9]]
    result3 = solution.minMeetingRooms(intervals3)
    # 预期输出: 2
    print(f"测试用例3: {result3}")
    print(f"测试用例3 (差分数组): {solution.minMeetingRoomsWithDiffArray(intervals3)}")
    
    # 测试用例4
    intervals4 = [[13, 15], [1, 13], [0, 2]]
    result4 = solution.minMeetingRooms(intervals4)
    # 预期输出: 2
    print(f"测试用例4: {result4}")
    print(f"测试用例4 (差分数组): {solution.minMeetingRoomsWithDiffArray(intervals4)}")

if __name__ == "__main__":
    main()
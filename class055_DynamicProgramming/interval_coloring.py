# -*- coding: utf-8 -*-
"""
区间染色算法（结合贪心）

问题描述：
给定多个区间，每个区间代表一个需要染色的区域，每次可以选择一个区间进行染色，
求最少需要多少次染色才能覆盖所有给定的区间。

贪心策略：
按照区间的右端点进行排序，每次选择能够覆盖当前未染色区域的最右端点的区间。

时间复杂度：O(n log n)，其中n是区间的数量，主要是排序的时间复杂度
空间复杂度：O(1)，只需要常量级的额外空间

相关题目：
1. LeetCode 435. 无重叠区间
2. LeetCode 452. 用最少数量的箭引爆气球
3. LeetCode 253. 会议室 II
"""

def interval_coloring(intervals):
    """
    区间染色算法实现
    
    Args:
        intervals: 区间列表，每个区间是一个元组 (start, end)
    
    Returns:
        int: 最少需要的染色次数
    
    Raises:
        ValueError: 当输入无效时抛出异常
    """
    # 参数验证
    if not intervals:
        return 0
    
    for interval in intervals:
        if len(interval) != 2 or interval[0] > interval[1]:
            raise ValueError("每个区间必须是有效的(start, end)元组，且start <= end")
    
    # 按照区间的右端点进行排序
    sorted_intervals = sorted(intervals, key=lambda x: x[1])
    
    # 贪心选择
    count = 1  # 至少需要一次染色
    end = sorted_intervals[0][1]  # 当前已经覆盖到的最右端点
    
    for i in range(1, len(sorted_intervals)):
        # 如果当前区间的左端点大于已经覆盖到的最右端点，需要新的染色
        if sorted_intervals[i][0] > end:
            count += 1
            end = sorted_intervals[i][1]
    
    return count


def interval_coloring_leetcode_452(points):
    """
    LeetCode 452. 用最少数量的箭引爆气球
    题目链接：https://leetcode-cn.com/problems/minimum-number-of-arrows-to-burst-balloons/
    
    问题描述：
    在二维空间中有许多球形的气球。对于每个气球，提供的输入是水平方向上，气球直径的开始和结束坐标。
    由于它是水平的，所以y坐标并不重要，因此只要知道开始和结束的x坐标就足够了。开始坐标总是小于结束坐标。
    一支弓箭可以沿着x轴从不同点完全垂直地射出。在坐标x处射出一支箭，若有一个气球的直径的开始和结束坐标为x_start，x_end，
    且满足x_start ≤ x ≤ x_end，则该气球会被引爆。可以射出的弓箭的数量没有限制。
    弓箭一旦被射出之后，可以无限地前进。我们想找到使得所有气球全部被引爆，所需的弓箭的最小数量。
    
    解题思路：
    这是区间染色问题的一个变种，相当于求最少需要多少个点才能覆盖所有区间。
    我们可以按照区间的右端点进行排序，然后每次选择当前区间的右端点作为箭的发射位置，
    这样可以尽可能多地引爆后面的气球。
    
    Args:
        points: 气球的坐标列表，每个气球表示为 [x_start, x_end]
    
    Returns:
        int: 所需的最小弓箭数量
    """
    if not points:
        return 0
    
    # 按照右端点排序
    points.sort(key=lambda x: x[1])
    
    arrows = 1
    pos = points[0][1]  # 第一支箭的位置
    
    for i in range(1, len(points)):
        # 如果当前气球的左端点大于箭的位置，需要新的箭
        if points[i][0] > pos:
            arrows += 1
            pos = points[i][1]
    
    return arrows


def interval_coloring_leetcode_435(intervals):
    """
    LeetCode 435. 无重叠区间
    题目链接：https://leetcode-cn.com/problems/non-overlapping-intervals/
    
    问题描述：
    给定一个区间的集合，找到需要移除区间的最小数量，使得剩余区间互不重叠。
    
    解题思路：
    这个问题可以转换为找到最大不重叠区间数，然后用总区间数减去这个值。
    求最大不重叠区间数的方法与区间染色类似，我们按照区间的右端点排序，
    然后贪心选择不重叠的区间。
    
    Args:
        intervals: 区间列表，每个区间是一个列表 [start, end]
    
    Returns:
        int: 需要移除的最小区间数量
    """
    if not intervals:
        return 0
    
    # 按照右端点排序
    intervals.sort(key=lambda x: x[1])
    
    count = 1  # 最大不重叠区间数
    end = intervals[0][1]
    
    for i in range(1, len(intervals)):
        if intervals[i][0] >= end:
            count += 1
            end = intervals[i][1]
    
    return len(intervals) - count


def interval_coloring_leetcode_253(intervals):
    """
    LeetCode 253. 会议室 II
    题目链接：https://leetcode-cn.com/problems/meeting-rooms-ii/
    
    问题描述：
    给定一个会议时间安排的数组，每个会议时间都会包括开始和结束的时间 [[s1,e1],[s2,e2],...] (si < ei)，
    为避免会议冲突，同时要考虑充分利用会议室资源，请你计算至少需要多少间会议室，才能满足这些会议安排。
    
    解题思路：
    我们可以将所有的开始时间和结束时间分别排序，然后使用双指针的方法来计算所需的会议室数量。
    每当一个会议开始时，我们需要一个新的会议室；每当一个会议结束时，我们释放一个会议室。
    我们只需要跟踪当前正在进行的会议数量，最大值即为所需的会议室数量。
    
    Args:
        intervals: 会议时间列表，每个会议是一个列表 [start, end]
    
    Returns:
        int: 所需的最少会议室数量
    """
    if not intervals:
        return 0
    
    # 分别提取开始时间和结束时间并排序
    start_times = sorted([interval[0] for interval in intervals])
    end_times = sorted([interval[1] for interval in intervals])
    
    i = j = 0
    max_rooms = current_rooms = 0
    
    while i < len(start_times) and j < len(end_times):
        # 如果当前会议开始时间小于结束时间，需要一个新的会议室
        if start_times[i] < end_times[j]:
            current_rooms += 1
            max_rooms = max(max_rooms, current_rooms)
            i += 1
        # 否则，释放一个会议室
        else:
            current_rooms -= 1
            j += 1
    
    return max_rooms


# 测试代码
if __name__ == "__main__":
    # 测试区间染色算法
    intervals1 = [(1, 4), (2, 5), (7, 9), (8, 10)]
    print(f"区间染色最少次数: {interval_coloring(intervals1)}")  # 应该输出 2
    
    # 测试LeetCode 452
    points = [[10, 16], [2, 8], [1, 6], [7, 12]]
    print(f"最少需要的箭数量: {interval_coloring_leetcode_452(points)}")  # 应该输出 2
    
    # 测试LeetCode 435
    intervals2 = [[1, 2], [2, 3], [3, 4], [1, 3]]
    print(f"需要移除的最小区间数量: {interval_coloring_leetcode_435(intervals2)}")  # 应该输出 1
    
    # 测试LeetCode 253
    intervals3 = [[0, 30], [5, 10], [15, 20]]
    print(f"最少需要的会议室数量: {interval_coloring_leetcode_253(intervals3)}")  # 应该输出 2
"""
LeetCode 435. Non-overlapping Intervals

题目描述：
给定一个区间的集合 intervals，其中 intervals[i] = [start_i, end_i]。
返回需要移除区间的最小数量，使剩余区间互不重叠。

解题思路：
这是一个经典的贪心算法问题。为了移除最少的区间，我们应该保留尽可能多的不重叠区间。

算法步骤：
1. 将所有区间按结束时间排序
2. 使用贪心策略：总是选择结束时间最早的区间
3. 遍历排序后的区间，统计可以保留的区间数量
4. 返回总区间数减去保留的区间数，即为需要移除的区间数

贪心策略的正确性：
选择结束时间最早的区间可以为后续区间留下更多空间，从而最大化保留的区间数量。

时间复杂度：O(n * log n)
空间复杂度：O(1)

相关题目：
- LeetCode 1353. 最多可以参加的会议数目 (贪心)
- LeetCode 646. 最长数对链 (贪心)
- LeetCode 1235. 最大盈利的工作调度 (动态规划 + 二分查找)
"""

def eraseOverlapIntervals(intervals):
    """
    计算需要移除的最小区间数
    
    Args:
        intervals: 区间列表，每个元素为 [start, end]
    
    Returns:
        需要移除的区间数
    """
    # 边界情况处理
    if not intervals:
        return 0
    
    n = len(intervals)
    
    # 按结束时间排序
    intervals.sort(key=lambda x: x[1])
    
    # 初始化计数器和上一个保留区间的结束时间
    count = 1  # 至少可以保留一个区间
    end = intervals[0][1]  # 第一个区间的结束时间
    
    # 遍历剩余区间
    for i in range(1, n):
        # 如果当前区间的开始时间 >= 上一个保留区间的结束时间
        # 说明不重叠，可以保留
        if intervals[i][0] >= end:
            count += 1
            end = intervals[i][1]  # 更新结束时间
        # 如果重叠，则跳过当前区间（相当于移除）
    
    # 返回需要移除的区间数
    return n - count

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    intervals1 = [[1,2],[2,3],[3,4],[1,3]]
    print("测试用例1:")
    print(f"输入: intervals = {intervals1}")
    print(f"输出: {eraseOverlapIntervals(intervals1)}")  # 期望输出: 1
    
    # 测试用例2
    intervals2 = [[1,2],[1,2],[1,2]]
    print("\n测试用例2:")
    print(f"输入: intervals = {intervals2}")
    print(f"输出: {eraseOverlapIntervals(intervals2)}")  # 期望输出: 2
    
    # 测试用例3
    intervals3 = [[1,2],[2,3]]
    print("\n测试用例3:")
    print(f"输入: intervals = {intervals3}")
    print(f"输出: {eraseOverlapIntervals(intervals3)}")  # 期望输出: 0
    
    # 测试用例4
    intervals4 = []
    print("\n测试用例4:")
    print(f"输入: intervals = {intervals4}")
    print(f"输出: {eraseOverlapIntervals(intervals4)}")  # 期望输出: 0
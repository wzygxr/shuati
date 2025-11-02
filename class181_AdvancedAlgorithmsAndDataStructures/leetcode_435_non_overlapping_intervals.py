#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 435 Non-overlapping Intervals

题目描述：
给定一个区间的集合，找到需要移除区间的最小数量，使剩余区间互不重叠。

解题思路：
这是一个经典的贪心算法问题。我们可以按照区间的结束时间进行排序，
然后贪心地选择结束时间最早的区间，这样可以为后续区间留出更多空间。

算法步骤：
1. 按照区间的结束时间进行排序
2. 贪心地选择不重叠的区间
3. 统计需要移除的区间数量

时间复杂度：O(n log n)
空间复杂度：O(1)
"""

class Solution:
    def erase_overlap_intervals(self, intervals):
        """
        计算需要移除的最小区间数量
        
        Args:
            intervals: 区间列表，每个元素为[start, end]
            
        Returns:
            需要移除的区间数量
        """
        if len(intervals) <= 1:
            return 0
        
        # 按照区间的结束时间进行排序
        intervals.sort(key=lambda x: x[1])
        
        count = 0
        end = intervals[0][1]  # 当前选择区间的结束时间
        
        # 贪心地选择不重叠的区间
        for i in range(1, len(intervals)):
            # 如果当前区间的开始时间小于前一个区间的结束时间，则有重叠
            if intervals[i][0] < end:
                count += 1  # 需要移除一个区间
                # 选择结束时间较早的区间，为后续留出更多空间
                end = min(end, intervals[i][1])
            else:
                # 没有重叠，更新结束时间
                end = intervals[i][1]
        
        return count


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    intervals1 = [[1,2],[2,3],[3,4],[1,3]]
    print("测试用例1:")
    print("区间:", intervals1)
    print("结果:", solution.erase_overlap_intervals(intervals1))
    print()
    
    # 测试用例2
    intervals2 = [[1,2],[1,2],[1,2]]
    print("测试用例2:")
    print("区间:", intervals2)
    print("结果:", solution.erase_overlap_intervals(intervals2))
    print()
    
    # 测试用例3
    intervals3 = [[1,2],[2,3]]
    print("测试用例3:")
    print("区间:", intervals3)
    print("结果:", solution.erase_overlap_intervals(intervals3))


if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1272 Remove Interval

题目描述：
给你一个有序的区间列表 intervals ，其中 intervals[i] = [starti, endi] 表示集合 [starti, endi)。
区间 [a, b) 是一组实数 x，满足 a <= x < b。
给你一个要删除的区间 toBeRemoved 。
返回一组实数，表示 intervals 中删除了 toBeRemoved 的部分。
结果应当是一个有序的、不重叠的区间列表。

解题思路：
我们需要遍历所有区间，对每个区间与要删除的区间进行比较，
根据重叠情况生成新的区间。

时间复杂度：O(n)
空间复杂度：O(1)
"""

class Solution:
    def remove_interval(self, intervals, to_be_removed):
        """
        从区间列表中删除指定区间
        
        Args:
            intervals: 区间列表，每个元素为[start, end]
            to_be_removed: 要删除的区间[start, end]
            
        Returns:
            删除指定区间后的区间列表
        """
        result = []
        remove_start, remove_end = to_be_removed
        
        # 遍历所有区间
        for start, end in intervals:
            # 如果当前区间与要删除的区间没有重叠
            if end <= remove_start or start >= remove_end:
                # 直接添加当前区间
                result.append([start, end])
            else:
                # 如果当前区间与要删除的区间有重叠
                # 添加左侧部分（如果存在）
                if start < remove_start:
                    result.append([start, remove_start])
                
                # 添加右侧部分（如果存在）
                if end > remove_end:
                    result.append([remove_end, end])
        
        return result


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    intervals1 = [[0,2],[3,4],[5,7]]
    to_be_removed1 = [1,6]
    print("测试用例1:")
    print("区间列表:", intervals1)
    print("要删除的区间:", to_be_removed1)
    print("结果:", solution.remove_interval(intervals1, to_be_removed1))
    print()
    
    # 测试用例2
    intervals2 = [[0,5]]
    to_be_removed2 = [2,3]
    print("测试用例2:")
    print("区间列表:", intervals2)
    print("要删除的区间:", to_be_removed2)
    print("结果:", solution.remove_interval(intervals2, to_be_removed2))
    print()
    
    # 测试用例3
    intervals3 = [[-5,-4],[-3,-2],[1,2],[3,5],[8,9]]
    to_be_removed3 = [-1,4]
    print("测试用例3:")
    print("区间列表:", intervals3)
    print("要删除的区间:", to_be_removed3)
    print("结果:", solution.remove_interval(intervals3, to_be_removed3))


if __name__ == "__main__":
    main()
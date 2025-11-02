#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1893 Check If All Integers In A Range Are Covered

题目描述：
给你一个二维整数数组 ranges 和两个整数 left 和 right。
每个 ranges[i] = [starti, endi] 表示一个从 starti 到 endi 的闭区间。
如果闭区间 [left, right] 内每个整数都被 ranges 中至少一个区间覆盖，
那么返回 true，否则返回 false。

解题思路：
我们可以使用差分数组来解决这个问题。
1. 创建一个差分数组，对每个区间进行标记
2. 计算前缀和得到每个点的覆盖次数
3. 检查目标区间内每个点的覆盖次数是否大于0

时间复杂度：O(n + m)，其中n是ranges的长度，m是区间长度
空间复杂度：O(m)
"""

class Solution:
    def is_covered(self, ranges, left, right):
        """
        检查区间是否被完全覆盖
        
        Args:
            ranges: 区间列表，每个元素为[start, end]
            left: 目标区间的左边界
            right: 目标区间的右边界
            
        Returns:
            是否被完全覆盖
        """
        # 创建差分数组，范围足够大以覆盖所有可能的值
        diff = [0] * 52  # 下标0-51，足够覆盖1-50的范围
        
        # 对每个区间进行标记
        for start, end in ranges:
            diff[start] += 1
            diff[end + 1] -= 1
        
        # 计算前缀和得到每个点的覆盖次数
        curr = 0
        for i in range(1, 51):
            curr += diff[i]
            # 检查目标区间内每个点的覆盖次数是否大于0
            if left <= i <= right and curr <= 0:
                return False
        
        return True


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    ranges1 = [[1,2],[3,4],[5,6]]
    left1, right1 = 2, 5
    print("测试用例1:")
    print("区间:", ranges1)
    print("目标区间: [", left1, ",", right1, "]")
    print("结果:", solution.is_covered(ranges1, left1, right1))
    print()
    
    # 测试用例2
    ranges2 = [[1,10],[10,20]]
    left2, right2 = 21, 21
    print("测试用例2:")
    print("区间:", ranges2)
    print("目标区间: [", left2, ",", right2, "]")
    print("结果:", solution.is_covered(ranges2, left2, right2))
    print()
    
    # 测试用例3
    ranges3 = [[1,50]]
    left3, right3 = 1, 50
    print("测试用例3:")
    print("区间:", ranges3)
    print("目标区间: [", left3, ",", right3, "]")
    print("结果:", solution.is_covered(ranges3, left3, right3))


if __name__ == "__main__":
    main()
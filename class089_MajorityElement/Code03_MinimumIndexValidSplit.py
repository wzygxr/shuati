#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
合法分割的最小下标
给定一个下标从 0 开始且全是正整数的数组 nums
如果一个元素在数组中占据主导地位（出现次数严格大于数组长度的一半），则称其为支配元素
一个有效分割是将数组分成 nums[0...i] 和 nums[i+1...n-1] 两部分
要求这两部分的支配元素都存在且等于原数组的支配元素
返回满足条件的最小分割下标 i，如果不存在有效分割，返回 -1

测试链接: https://leetcode.cn/problems/minimum-index-of-a-valid-split/
"""

from typing import List

def minimumIndex(nums: List[int]) -> int:
    """
    题目解析：
    需要找到一个最小的分割点，使得分割后的两部分都有支配元素，且都等于原数组的支配元素
    
    解题思路：
    1. 首先找出原数组的支配元素（使用Boyer-Moore投票算法）
    2. 统计该元素在整个数组中的出现次数
    3. 遍历所有可能的分割点，检查分割后的两部分是否都满足支配元素条件
    
    时间复杂度：O(n) - 需要遍历数组三次（找候选元素、统计次数、检查分割点）
    空间复杂度：O(1) - 只使用了常数级别的额外空间
    
    该解法是最优解，因为：
    1. 时间复杂度已经是最优的，因为至少需要遍历一次数组才能确定每个元素的信息
    2. 空间复杂度也是最优的，只使用了常数级别的额外空间
    """
    
    # 第一步：使用Boyer-Moore投票算法找出候选元素
    candidate = 0
    count = 0
    
    # 投票阶段：找出可能的支配元素
    for num in nums:
        if count == 0:
            candidate = num
            count = 1
        elif num == candidate:
            count += 1
        else:
            count -= 1
    
    # 第二步：统计候选元素在整个数组中的出现次数
    count = 0
    for num in nums:
        if num == candidate:
            count += 1
    
    # 第三步：遍历所有可能的分割点，检查是否满足条件
    n = len(nums)
    leftCount = 0  # 左半部分中候选元素的出现次数
    
    # 遍历所有可能的分割点 i (0 <= i < n-1)
    for i in range(n - 1):
        # 更新左半部分中候选元素的出现次数
        if nums[i] == candidate:
            leftCount += 1
        
        # 计算右半部分中候选元素的出现次数
        rightCount = count - leftCount
        
        # 检查左半部分是否满足支配元素条件
        # 左半部分长度为 i+1，需要候选元素出现次数 > (i+1)/2
        leftValid = leftCount * 2 > (i + 1)
        
        # 检查右半部分是否满足支配元素条件
        # 右半部分长度为 n-i-1，需要候选元素出现次数 > (n-i-1)/2
        rightValid = rightCount * 2 > (n - i - 1)
        
        # 如果两部分都满足条件，则找到了有效分割点
        if leftValid and rightValid:
            return i
    
    # 不存在有效分割点
    return -1


# 测试用例
if __name__ == "__main__":
    # 测试用例1: [1,2,2,2] -> 2
    # 原数组支配元素是2，分割点2处，左半部分[1,2,2]支配元素是2，右半部分[2]支配元素是2
    nums1 = [1, 2, 2, 2]
    print("输入: [1,2,2,2]")
    print("输出: ", minimumIndex(nums1))
    
    # 测试用例2: [2,1,3,1,1,1,7,1,2,1] -> 4
    nums2 = [2, 1, 3, 1, 1, 1, 7, 1, 2, 1]
    print("输入: [2,1,3,1,1,1,7,1,2,1]")
    print("输出: ", minimumIndex(nums2))
    
    # 测试用例3: [3,3,3,3,7,2,2] -> -1
    nums3 = [3, 3, 3, 3, 7, 2, 2]
    print("输入: [3,3,3,3,7,2,2]")
    print("输出: ", minimumIndex(nums3))
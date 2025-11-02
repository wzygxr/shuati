#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
多数元素 II
给定一个大小为 n 的整数数组，找出其中所有出现超过 ⌊ n/3 ⌋ 次的元素。

测试链接: https://leetcode.cn/problems/majority-element-ii/
"""

from typing import List

def majorityElement(nums: List[int]) -> List[int]:
    """
    题目解析：
    需要找出数组中出现次数超过 n/3 的元素
    由于数组中最多只能有2个这样的元素（因为如果3个元素都出现超过n/3次，总数会超过n），我们可以使用扩展的Boyer-Moore投票算法
    
    解题思路：
    1. 使用Boyer-Moore投票算法的扩展版本，维护两个候选元素和它们的计数
    2. 第一遍遍历数组，找出两个候选元素
    3. 第二遍遍历数组，验证候选元素是否真的出现超过 n/3 次
    
    时间复杂度：O(n) - 需要遍历数组两次
    空间复杂度：O(1) - 只使用了常数级别的额外空间
    
    该解法是最优解，因为：
    1. 时间复杂度已经是最优的，因为至少需要遍历一次数组才能确定每个元素的信息
    2. 空间复杂度也是最优的，只使用了常数级别的额外空间
    """
    
    # 初始化两个候选元素和它们的计数
    cand1, cand2 = 0, 0
    count1, count2 = 0, 0
    
    # 第一遍遍历，找出候选元素
    # Boyer-Moore投票算法的核心思想：
    # 1. 如果当前元素等于候选元素，则计数加1
    # 2. 如果当前元素不等于任何候选元素：
    #    a. 如果某个候选元素计数为0，则替换该候选元素为当前元素
    #    b. 否则所有候选元素计数减1（相当于抵消）
    for num in nums:
        if count1 > 0 and num == cand1:
            # 当前元素等于第一个候选元素，计数加1
            count1 += 1
        elif count2 > 0 and num == cand2:
            # 当前元素等于第二个候选元素，计数加1
            count2 += 1
        elif count1 == 0:
            # 第一个候选元素计数为0，替换为当前元素
            cand1 = num
            count1 = 1
        elif count2 == 0:
            # 第二个候选元素计数为0，替换为当前元素
            cand2 = num
            count2 = 1
        else:
            # 当前元素不等于任何候选元素，且两个候选元素计数都大于0
            # 则两个候选元素计数都减1（相当于抵消）
            count1 -= 1
            count2 -= 1
    
    # 第二遍遍历，统计候选元素的真实出现次数
    count1, count2 = 0, 0
    for num in nums:
        if num == cand1:
            count1 += 1
        elif num == cand2:
            count2 += 1
    
    # 构造结果列表
    result = []
    n = len(nums)
    # 验证候选元素是否真的出现超过 n/3 次
    if count1 > n // 3:
        result.append(cand1)
    if count2 > n // 3:
        result.append(cand2)
    
    return result


# 测试用例
if __name__ == "__main__":
    # 测试用例1: [3,2,3] -> [3]
    nums1 = [3, 2, 3]
    print("输入: [3,2,3]")
    print("输出: ", majorityElement(nums1))
    
    # 测试用例2: [1] -> [1]
    nums2 = [1]
    print("输入: [1]")
    print("输出: ", majorityElement(nums2))
    
    # 测试用例3: [1,2] -> [1,2]
    nums3 = [1, 2]
    print("输入: [1,2]")
    print("输出: ", majorityElement(nums3))
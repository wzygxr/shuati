#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
题目: LeetCode 136. Single Number
链接: https://leetcode.cn/problems/single-number/

题目描述:
给定一个非空整数数组，除了某个元素只出现一次以外，其余每个元素均出现两次。
找出那个只出现了一次的元素。

解题思路:
利用异或运算的性质:
1. 任何数与自身异或结果为0 (a ^ a = 0)
2. 任何数与0异或结果为其本身 (a ^ 0 = a)
3. 异或运算满足交换律和结合律

因此，将数组中所有元素进行异或运算，出现两次的元素会相互抵消为0，
最终只剩下出现一次的元素。

时间复杂度: O(n) - 需要遍历数组一次
空间复杂度: O(1) - 只使用常数额外空间
"""


class LeetCode136_SingleNumber:
    def single_number(self, nums):
        """
        找到数组中唯一出现一次的元素
        
        Args:
            nums: 输入数组
            
        Returns:
            只出现一次的元素
        """
        # 利用异或运算的性质，所有出现两次的数会相互抵消
        result = 0
        for num in nums:
            result ^= num
        return result


# 测试代码
if __name__ == "__main__":
    solution = LeetCode136_SingleNumber()
    
    # 测试用例1
    nums1 = [2, 2, 1]
    print(f"输入: {nums1}")
    print(f"输出: {solution.single_number(nums1)}")  # 应该输出 1
    
    # 测试用例2
    nums2 = [4, 1, 2, 1, 2]
    print(f"输入: {nums2}")
    print(f"输出: {solution.single_number(nums2)}")  # 应该输出 4
    
    # 测试用例3
    nums3 = [1]
    print(f"输入: {nums3}")
    print(f"输出: {solution.single_number(nums3)}")  # 应该输出 1
#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
LeetCode 421. 数组中两个数的最大异或值
题目链接：https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/

解题思路：
这道题可以用字典树（前缀树）来实现线性基的功能。我们将每个数的二进制表示从最高位到最低位插入字典树中，
然后对于每个数，在字典树中查找能够与其产生最大异或值的另一个数。

时间复杂度：O(n * 32)，其中n是数组长度，32是整数的二进制位数
空间复杂度：O(n * 32)
'''

class Solution:
    
    def __init__(self):
        self.root = None
        self.HIGH_BIT = 30  # 整数的最高位是第30位（假设是32位整数）
    
    def findMaximumXOR(self, nums):
        """
        找到数组中两个数的最大异或值
        
        Args:
            nums: 输入数组
        
        Returns:
            int: 最大异或值
        """
        # 边界情况处理
        if not nums or len(nums) <= 1:
            return 0
        
        # 创建字典树
        self.root = {}
        max_xor = 0
        
        # 插入第一个数
        self._insert(nums[0])
        
        # 对于每个数，先查询最大异或值，再插入到字典树中
        for i in range(1, len(nums)):
            max_xor = max(max_xor, self._query(nums[i]))
            self._insert(nums[i])
        
        return max_xor
    
    def _insert(self, num):
        """
        将数字插入字典树
        
        Args:
            num: 要插入的数字
        """
        node = self.root
        # 从最高位开始处理每一位
        for i in range(self.HIGH_BIT, -1, -1):
            bit = (num >> i) & 1  # 提取当前位
            if bit not in node:
                node[bit] = {}
            node = node[bit]
    
    def _query(self, num):
        """
        查询与给定数字异或的最大值
        
        Args:
            num: 给定数字
        
        Returns:
            int: 最大异或值
        """
        node = self.root
        xor_sum = 0
        
        for i in range(self.HIGH_BIT, -1, -1):
            bit = (num >> i) & 1
            desired_bit = 1 - bit  # 希望找到相反的位以获得最大异或值
            
            if desired_bit in node:
                # 当前位可以取到1，增加异或和
                xor_sum |= (1 << i)
                node = node[desired_bit]
            else:
                # 只能取相同的位
                node = node[bit]
        
        return xor_sum

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1 = [3, 10, 5, 25, 2, 8]
    print(f"测试用例1结果: {solution.findMaximumXOR(nums1)}")  # 预期输出: 28
    
    # 测试用例2
    nums2 = [14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70]
    print(f"测试用例2结果: {solution.findMaximumXOR(nums2)}")  # 预期输出: 127
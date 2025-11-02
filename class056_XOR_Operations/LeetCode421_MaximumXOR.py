#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
题目: LeetCode 421. Maximum XOR of Two Numbers in an Array
链接: https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/

题目描述:
给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。

解题思路:
使用前缀树(Trie)结构：
1. 将数组中每个数字的二进制表示插入前缀树中
2. 对于每个数字，在前缀树中查找能与之产生最大异或值的路径
3. 贪心策略：对于每一位，尽量寻找相反的位以最大化异或结果

时间复杂度: O(n * 32) = O(n) - 每个数字处理32位
空间复杂度: O(n * 32) = O(n) - 前缀树存储
"""


class TrieNode:
    """前缀树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典，键为0或1，值为TrieNode


class LeetCode421_MaximumXOR:
    def find_maximum_xor(self, nums):
        """
        找到数组中两个数的最大异或值
        
        Args:
            nums: 输入数组
            
        Returns:
            最大异或值
        """
        if not nums:
            return 0
        
        # 构建前缀树
        root = TrieNode()
        
        # 将所有数字插入前缀树
        for num in nums:
            self._insert(root, num)
        
        max_result = 0
        # 对于每个数字，在前缀树中寻找能产生最大异或值的数字
        for num in nums:
            current_max = self._find_max_xor(root, num)
            max_result = max(max_result, current_max)
        
        return max_result
    
    def _insert(self, root, num):
        """
        向前缀树中插入数字
        
        Args:
            root: 前缀树根节点
            num: 待插入的数字
        """
        node = root
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            if bit not in node.children:
                node.children[bit] = TrieNode()
            node = node.children[bit]
    
    def _find_max_xor(self, root, num):
        """
        在前缀树中查找与num异或能得到最大值的数字
        
        Args:
            root: 前缀树根节点
            num: 待查询的数字
            
        Returns:
            最大异或值
        """
        node = root
        max_xor = 0
        # 从最高位开始处理
        for i in range(31, -1, -1):
            bit = (num >> i) & 1
            # 贪心策略：尽量走相反的位
            desired_bit = 1 - bit
            if desired_bit in node.children:
                # 能走相反的位，该位异或结果为1
                max_xor |= (1 << i)
                node = node.children[desired_bit]
            else:
                # 只能走相同的位，该位异或结果为0
                node = node.children[bit] if bit in node.children else None
                if node is None:
                    break
        return max_xor


# 测试代码
if __name__ == "__main__":
    solution = LeetCode421_MaximumXOR()
    
    # 测试用例1
    nums1 = [3, 10, 5, 25, 2, 8]
    print(f"输入: {nums1}")
    print(f"输出: {solution.find_maximum_xor(nums1)}")  # 应该输出 28 (5^25)
    
    # 测试用例2
    nums2 = [0]
    print(f"输入: {nums2}")
    print(f"输出: {solution.find_maximum_xor(nums2)}")  # 应该输出 0
    
    # 测试用例3
    nums3 = [2, 4]
    print(f"输入: {nums3}")
    print(f"输出: {solution.find_maximum_xor(nums3)}")  # 应该输出 6 (2^4)
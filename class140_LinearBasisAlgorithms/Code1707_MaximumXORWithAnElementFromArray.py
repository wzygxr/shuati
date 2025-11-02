#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
LeetCode 1707. 与数组中元素的最大异或值
题目链接：https://leetcode.com/problems/maximum-xor-with-an-element-from-array/

解题思路：
这道题是上一题的扩展，需要处理带约束条件的查询。我们可以采用离线处理的方法：
1. 将nums数组排序
2. 将queries数组排序，并记录原始索引
3. 按照mi从小到大的顺序处理查询，将nums中不超过mi的元素插入字典树
4. 对于每个查询，在字典树中查询最大异或值

时间复杂度：O(n log n + q log q + (n + q) * 32)，其中n是数组长度，q是查询数量
空间复杂度：O(n * 32 + q)
'''

class Solution:
    
    def __init__(self):
        self.root = None
        self.HIGH_BIT = 30  # 整数的最高位是第30位（假设是32位整数）
    
    def maximizeXor(self, nums, queries):
        """
        处理每个查询，返回每个查询的最大异或值
        
        Args:
            nums: 输入数组
            queries: 查询数组，每个查询包含[xi, mi]
        
        Returns:
            list: 每个查询的最大异或值数组
        """
        # 边界情况处理
        if not nums or not queries:
            return []
        
        # 创建字典树
        self.root = {}
        n = len(nums)
        q = len(queries)
        result = [-1] * q
        
        # 对nums数组排序
        sorted_nums = sorted(nums)
        
        # 对queries进行排序，并记录原始索引
        sorted_queries = [(query[1], query[0], i) for i, query in enumerate(queries)]
        sorted_queries.sort()
        
        num_index = 0
        for mi, xi, original_index in sorted_queries:
            # 将nums中不超过mi的元素插入字典树
            while num_index < n and sorted_nums[num_index] <= mi:
                self._insert(sorted_nums[num_index])
                num_index += 1
            
            # 如果字典树不为空，查询最大异或值
            if num_index > 0:
                result[original_index] = self._query_max_xor(xi)
        
        return result
    
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
    
    def _query_max_xor(self, num):
        """
        查询与给定数字异或的最大值
        
        Args:
            num: 给定数字
        
        Returns:
            int: 最大异或值
        """
        node = self.root
        max_xor = 0
        
        for i in range(self.HIGH_BIT, -1, -1):
            bit = (num >> i) & 1
            desired_bit = 1 - bit  # 希望找到相反的位以获得最大异或值
            
            if desired_bit in node:
                # 当前位可以取到1，增加异或和
                max_xor |= (1 << i)
                node = node[desired_bit]
            else:
                # 只能取相同的位
                node = node[bit]
        
        return max_xor

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    nums1 = [0, 1, 2, 3, 4]
    queries1 = [[3, 1], [1, 3], [5, 6]]
    result1 = solution.maximizeXor(nums1, queries1)
    print(f"测试用例1结果: {result1}")  # 预期输出: [3, 3, 7]
    
    # 测试用例2
    nums2 = [5, 2, 4, 6, 6, 3]
    queries2 = [[12, 4], [8, 1], [6, 3]]
    result2 = solution.maximizeXor(nums2, queries2)
    print(f"测试用例2结果: {result2}")  # 预期输出: [15, -1, 5]
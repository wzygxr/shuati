#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1521 Find Value of Partition

题目描述：
给你一个正整数数组 nums 。
同时给你一个长度为 m 的整数数组 queries 。
第 i 个查询中，我们需要将 nums 中的元素划分到两个数组 A 和 B 中，
使得 A 中的元素都小于等于 queries[i]，B 中的元素都大于 queries[i]。
如果某个数组为空，其和视为 0。
返回一个数组 answer，其中 answer[i] 是第 i 个查询的答案。

解题思路：
我们可以使用稀疏表来预处理数组，然后对每个查询使用二分查找找到分割点。
1. 对数组进行排序
2. 使用前缀和预处理
3. 对每个查询使用二分查找找到分割点
4. 计算两个数组的和并返回差值的绝对值

时间复杂度：O(n log n + m log n)
空间复杂度：O(n)
"""

class Solution:
    def find_value_of_partition(self, nums, queries):
        """
        计算每个查询的分区值
        
        Args:
            nums: 输入数组
            queries: 查询数组
            
        Returns:
            每个查询的结果数组
        """
        # 对数组进行排序
        nums.sort()
        
        # 计算前缀和
        prefix_sum = [0]
        for num in nums:
            prefix_sum.append(prefix_sum[-1] + num)
        
        result = []
        
        # 处理每个查询
        for query in queries:
            # 使用二分查找找到分割点
            left, right = 0, len(nums)
            while left < right:
                mid = left + (right - left) // 2
                if nums[mid] <= query:
                    left = mid + 1
                else:
                    right = mid
            
            # 计算两个数组的和
            sum_a = prefix_sum[left]
            sum_b = prefix_sum[-1] - sum_a
            
            # 返回差值的绝对值
            result.append(abs(sum_a - sum_b))
        
        return result


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 3, 2, 4]
    queries1 = [2, 3]
    print("测试用例1:")
    print("数组:", nums1)
    print("查询:", queries1)
    print("结果:", solution.find_value_of_partition(nums1, queries1))
    print()
    
    # 测试用例2
    nums2 = [10, 15, 20, 25]
    queries2 = [12, 18, 25]
    print("测试用例2:")
    print("数组:", nums2)
    print("查询:", queries2)
    print("结果:", solution.find_value_of_partition(nums2, queries2))
    print()
    
    # 测试用例3
    nums3 = [1, 100]
    queries3 = [50]
    print("测试用例3:")
    print("数组:", nums3)
    print("查询:", queries3)
    print("结果:", solution.find_value_of_partition(nums3, queries3))


if __name__ == "__main__":
    main()
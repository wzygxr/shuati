"""
最长连续递增子序列

题目来源：LeetCode 674. 最长连续递增子序列
题目链接：https://leetcode.cn/problems/longest-continuous-increasing-subsequence/
题目描述：给定一个未经排序的整数数组，找到最长且连续递增的子序列，并返回该序列的长度。
连续递增的子序列可以由两个下标 l 和 r（l < r）确定，如果对于每个 l <= i < r，
都有 nums[i] < nums[i + 1] ，那么子序列 [nums[l], nums[l + 1], ..., nums[r - 1], nums[r]] 就是连续递增子序列。

算法思路：
1. 使用滑动窗口或动态规划思想
2. 遍历数组，维护当前连续递增子序列的长度
3. 当遇到不满足递增条件时，重置当前长度
4. 记录遍历过程中的最大长度

时间复杂度：O(n) - 只需遍历一次数组
空间复杂度：O(1) - 只使用常数额外空间
是否最优解：是，这是最优解法

示例：
输入: [1,3,5,4,7]
输出: 3
解释: 最长连续递增子序列是 [1,3,5], 长度为3。
尽管 [1,3,5,7] 也是升序的子序列, 但它不是连续的，因为5和7在原数组里被4隔开。

输入: [2,2,2,2,2]
输出: 1
解释: 最长连续递增子序列是 [2], 长度为1。
"""

from typing import List

class Solution:
    """
    计算最长连续递增子序列的长度
    
    Args:
        nums: 输入的整数数组
        
    Returns:
        最长连续递增子序列的长度
    """
    def findLengthOfLCIS(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        n = len(nums)
        # 当前连续递增子序列的长度
        current_length = 1
        # 最大连续递增子序列的长度
        max_length = 1
        
        # 从第二个元素开始遍历
        for i in range(1, n):
            # 如果当前元素大于前一个元素，连续递增
            if nums[i] > nums[i - 1]:
                current_length += 1
                max_length = max(max_length, current_length)
            else:
                # 不满足递增条件，重置当前长度
                current_length = 1
        
        return max_length

    """
    使用滑动窗口方法计算最长连续递增子序列的长度
    
    算法思路：
    1. 使用双指针维护滑动窗口
    2. 左指针指向当前连续递增子序列的开始位置
    3. 右指针向右扩展，直到不满足递增条件
    4. 更新最大长度，移动左指针到右指针位置
    
    时间复杂度：O(n) - 每个元素最多被访问两次
    空间复杂度：O(1) - 只使用常数额外空间
    是否最优解：是，这是最优解法
    
    Args:
        nums: 输入的整数数组
        
    Returns:
        最长连续递增子序列的长度
    """
    def findLengthOfLCIS2(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        n = len(nums)
        left = 0  # 滑动窗口左边界
        max_length = 1  # 最大长度
        
        # 右指针遍历数组
        for right in range(1, n):
            # 如果当前元素不大于前一个元素，不满足连续递增条件
            if nums[right] <= nums[right - 1]:
                # 更新最大长度
                max_length = max(max_length, right - left)
                # 移动左指针到当前位置
                left = right
        
        # 处理最后一个窗口
        max_length = max(max_length, n - left)
        
        return max_length

def test():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 3, 5, 4, 7]
    print("输入:", nums1)
    print("方法1输出:", solution.findLengthOfLCIS(nums1))
    print("方法2输出:", solution.findLengthOfLCIS2(nums1))
    print("期望: 3")
    print()
    
    # 测试用例2
    nums2 = [2, 2, 2, 2, 2]
    print("输入:", nums2)
    print("方法1输出:", solution.findLengthOfLCIS(nums2))
    print("方法2输出:", solution.findLengthOfLCIS2(nums2))
    print("期望: 1")
    print()
    
    # 测试用例3
    nums3 = [1, 3, 5, 7]
    print("输入:", nums3)
    print("方法1输出:", solution.findLengthOfLCIS(nums3))
    print("方法2输出:", solution.findLengthOfLCIS2(nums3))
    print("期望: 4")
    print()
    
    # 测试用例4
    nums4 = []
    print("输入:", nums4)
    print("方法1输出:", solution.findLengthOfLCIS(nums4))
    print("方法2输出:", solution.findLengthOfLCIS2(nums4))
    print("期望: 0")
    print()
    
    # 测试用例5
    nums5 = [1]
    print("输入:", nums5)
    print("方法1输出:", solution.findLengthOfLCIS(nums5))
    print("方法2输出:", solution.findLengthOfLCIS2(nums5))
    print("期望: 1")
    print()

if __name__ == "__main__":
    test()
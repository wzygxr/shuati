"""
得到山形数组的最少删除次数

题目来源：LeetCode 1671. 得到山形数组的最少删除次数
题目链接：https://leetcode.cn/problems/minimum-number-of-removals-to-make-mountain-array/
题目描述：我们定义 arr 是山形数组当且仅当它满足：
  - arr.length >= 3
  - 存在某个下标 i（0 < i < arr.length - 1）使得：
      arr[0] < arr[1] < ... < arr[i-1] < arr[i]
      arr[i] > arr[i+1] > ... > arr[arr.length - 1]
给你一个整数数组 nums，返回将它变成山形数组的最少删除次数。

算法思路：
1. 山形数组要求存在一个峰值，左侧严格递增，右侧严格递减
2. 对于每个位置i，计算以i为结尾的最长递增子序列长度（左侧）
3. 计算以i为开头的最长递减子序列长度（右侧）
4. 如果左侧长度>1且右侧长度>1，则山形数组长度为left[i] + right[i] - 1
5. 最少删除次数 = n - 最大山形数组长度

时间复杂度：O(n²) - 需要计算两个方向的LIS
空间复杂度：O(n) - 需要两个数组存储左右LIS长度
是否最优解：对于此问题的动态规划解法是标准解法

示例：
输入: nums = [1,3,1]
输出: 0
解释: 数组本身就是山形数组，所以我们不需要删除任何元素。

输入: nums = [2,1,1,5,6,2,3,1]
输出: 3
解释: 一种方法是将下标为 0，1 和 5 的元素删除，剩下元素为 [1,5,6,3,1] ，是山形数组。
"""

from typing import List
import bisect

class Solution:
    """
    计算将数组变成山形数组的最少删除次数
    
    Args:
        nums: 输入的整数数组
        
    Returns:
        最少删除次数
    """
    def minimumMountainRemovals(self, nums: List[int]) -> int:
        n = len(nums)
        if n < 3:
            return 0  # 数组长度小于3，无法形成山形数组
        
        # left[i] 表示以nums[i]结尾的最长严格递增子序列长度
        left = [1] * n
        # right[i] 表示以nums[i]开头的最长严格递减子序列长度
        right = [1] * n
        
        # 计算左侧最长递增子序列
        for i in range(n):
            for j in range(i):
                if nums[j] < nums[i]:
                    left[i] = max(left[i], left[j] + 1)
        
        # 计算右侧最长递减子序列
        for i in range(n-1, -1, -1):
            for j in range(n-1, i, -1):
                if nums[j] < nums[i]:
                    right[i] = max(right[i], right[j] + 1)
        
        # 计算最大山形数组长度
        max_mountain_length = 0
        for i in range(1, n-1):
            # 峰值左右两侧都必须有元素（长度>1）
            if left[i] > 1 and right[i] > 1:
                max_mountain_length = max(max_mountain_length, left[i] + right[i] - 1)
        
        # 最少删除次数 = 总长度 - 最大山形数组长度
        return n - max_mountain_length

    """
    使用贪心+二分查找优化版本
    
    算法思路：
    1. 使用贪心+二分查找优化LIS计算
    2. 分别计算从左到右和从右到左的LIS
    3. 时间复杂度优化到O(n*logn)
    
    时间复杂度：O(n*logn) - 使用二分查找优化
    空间复杂度：O(n) - 需要ends数组存储状态
    是否最优解：是，这是最优解法
    
    Args:
        nums: 输入的整数数组
        
    Returns:
        最少删除次数
    """
    def minimumMountainRemovals2(self, nums: List[int]) -> int:
        n = len(nums)
        if n < 3:
            return 0
        
        # 计算从左到右的LIS
        left = [0] * n
        ends_left = []
        
        for i in range(n):
            # 使用bisect查找插入位置
            pos = bisect.bisect_left(ends_left, nums[i])
            if pos == len(ends_left):
                ends_left.append(nums[i])
                left[i] = len(ends_left)
            else:
                ends_left[pos] = nums[i]
                left[i] = pos + 1
        
        # 计算从右到左的LIS（相当于从左到右的递减序列）
        right = [0] * n
        ends_right = []
        
        for i in range(n-1, -1, -1):
            # 注意：对于从右到左的LDS，我们需要找>=nums[i]的位置
            pos = bisect.bisect_left(ends_right, nums[i])
            if pos == len(ends_right):
                ends_right.append(nums[i])
                right[i] = len(ends_right)
            else:
                ends_right[pos] = nums[i]
                right[i] = pos + 1
        
        # 计算最大山形数组长度
        max_mountain_length = 0
        for i in range(1, n-1):
            if left[i] > 1 and right[i] > 1:
                max_mountain_length = max(max_mountain_length, left[i] + right[i] - 1)
        
        return n - max_mountain_length

def test():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 3, 1]
    print("输入:", nums1)
    print("方法1输出:", solution.minimumMountainRemovals(nums1))
    print("方法2输出:", solution.minimumMountainRemovals2(nums1))
    print("期望: 0")
    print()
    
    # 测试用例2
    nums2 = [2, 1, 1, 5, 6, 2, 3, 1]
    print("输入:", nums2)
    print("方法1输出:", solution.minimumMountainRemovals(nums2))
    print("方法2输出:", solution.minimumMountainRemovals2(nums2))
    print("期望: 3")
    print()
    
    # 测试用例3
    nums3 = [4, 3, 2, 1, 1, 2, 3, 1]
    print("输入:", nums3)
    print("方法1输出:", solution.minimumMountainRemovals(nums3))
    print("方法2输出:", solution.minimumMountainRemovals2(nums3))
    print()
    
    # 测试用例4
    nums4 = [1, 2, 3, 4, 4, 3, 2, 1]
    print("输入:", nums4)
    print("方法1输出:", solution.minimumMountainRemovals(nums4))
    print("方法2输出:", solution.minimumMountainRemovals2(nums4))
    print()

if __name__ == "__main__":
    test()
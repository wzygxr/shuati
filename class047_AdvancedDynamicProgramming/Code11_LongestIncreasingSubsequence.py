"""
最长递增子序列 (Longest Increasing Subsequence) - 线性动态规划 - Python实现

题目描述：
给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。

题目来源：LeetCode 300. 最长递增子序列
测试链接：https://leetcode.cn/problems/longest-increasing-subsequence/

解题思路：
这是一个经典的线性动态规划问题。
设 dp[i] 表示以 nums[i] 结尾的最长递增子序列的长度。
对于每个位置 i，我们需要找到所有 j < i 且 nums[j] < nums[i] 的位置，
然后取 dp[j] 的最大值加1。
状态转移方程：dp[i] = max(dp[j] + 1) for all j < i and nums[j] < nums[i]
边界条件：dp[i] = 1 (每个元素本身构成长度为1的子序列)

算法实现：
1. 动态规划：O(n^2)时间复杂度
2. 二分查找优化：O(n log n)时间复杂度
3. 记忆化搜索：递归计算，使用记忆化避免重复计算

时间复杂度分析：
- 动态规划：O(n^2)
- 二分查找优化：O(n log n)
- 记忆化搜索：O(n^2)

空间复杂度分析：
- 动态规划：O(n)
- 二分查找优化：O(n)
- 记忆化搜索：O(n)
"""

def lengthOfLIS1(nums):
    """
    动态规划解法 O(n^2)
    
    Args:
        nums: 整数数组
        
    Returns:
        int: 最长递增子序列的长度
    """
    if not nums:
        return 0
    
    n = len(nums)
    # dp[i] 表示以 nums[i] 结尾的最长递增子序列的长度
    dp = [1] * n
    
    maxLength = 1
    
    # 状态转移
    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)
        maxLength = max(maxLength, dp[i])
    
    return maxLength

def lengthOfLIS2(nums):
    """
    二分查找优化解法 O(n log n)
    
    Args:
        nums: 整数数组
        
    Returns:
        int: 最长递增子序列的长度
    """
    if not nums:
        return 0
    
    import bisect
    
    # tails[i] 表示长度为 i+1 的递增子序列的最小尾部元素
    tails = []
    
    for num in nums:
        # 使用二分查找找到第一个大于等于 num 的位置
        pos = bisect.bisect_left(tails, num)
        
        # 更新 tails 数组
        if pos == len(tails):
            tails.append(num)
        else:
            tails[pos] = num
    
    return len(tails)

def lengthOfLIS3(nums):
    """
    记忆化搜索解法
    
    Args:
        nums: 整数数组
        
    Returns:
        int: 最长递增子序列的长度
    """
    if not nums:
        return 0
    
    n = len(nums)
    from functools import lru_cache
    
    @lru_cache(maxsize=None)
    def dfs(i):
        # 初始化：至少为1（自身）
        maxLength = 1
        
        # 寻找前面所有较小元素的最长子序列
        for j in range(i):
            if nums[j] < nums[i]:
                maxLength = max(maxLength, dfs(j) + 1)
        
        return maxLength
    
    # 尝试以每个元素作为结尾
    maxLength = 0
    for i in range(n):
        maxLength = max(maxLength, dfs(i))
    
    return maxLength

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    nums1 = [10, 9, 2, 5, 3, 7, 101, 18]
    print("测试用例1:")
    print(f"数组: {nums1}")
    print("方法1结果:", lengthOfLIS1(nums1))
    print("方法2结果:", lengthOfLIS2(nums1))
    print("方法3结果:", lengthOfLIS3(nums1))
    print()
    
    # 测试用例2
    nums2 = [0, 1, 0, 3, 2, 3]
    print("测试用例2:")
    print(f"数组: {nums2}")
    print("方法1结果:", lengthOfLIS1(nums2))
    print("方法2结果:", lengthOfLIS2(nums2))
    print("方法3结果:", lengthOfLIS3(nums2))
    print()
    
    # 测试用例3
    nums3 = [7, 7, 7, 7, 7, 7, 7]
    print("测试用例3:")
    print(f"数组: {nums3}")
    print("方法1结果:", lengthOfLIS1(nums3))
    print("方法2结果:", lengthOfLIS2(nums3))
    print("方法3结果:", lengthOfLIS3(nums3))
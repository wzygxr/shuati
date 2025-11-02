"""
最长递增子序列的个数

题目来源：LeetCode 673. 最长递增子序列的个数
题目链接：https://leetcode.cn/problems/number-of-longest-increasing-subsequence/
题目描述：给定一个未排序的整数数组 nums ，返回最长递增子序列的个数。
注意：这个数列必须是严格递增的。

算法思路：
1. 使用动态规划方法
2. 维护两个数组：
   - dp[i] 表示以 nums[i] 结尾的最长递增子序列的长度
   - cnt[i] 表示以 nums[i] 结尾的最长递增子序列的个数
3. 对于每个位置 i，遍历前面所有位置 j：
   - 如果 nums[j] < nums[i]，可以将 nums[i] 接在以 nums[j] 结尾的递增子序列后面
   - 如果 dp[j] + 1 > dp[i]，更新 dp[i] 和 cnt[i]
   - 如果 dp[j] + 1 == dp[i]，累加 cnt[i]
4. 统计最长长度对应的个数

时间复杂度：O(n^2) - 外层循环n次，内层循环最多n次
空间复杂度：O(n) - 需要dp和cnt数组存储状态
是否最优解：对于此问题的动态规划解法是标准解法

示例：
输入: [1,3,5,4,7]
输出: 2
解释: 有两个最长递增子序列，分别是 [1, 3, 4, 7] 和[1, 3, 5, 7]。

输入: [2,2,2,2,2]
输出: 5
解释: 最长递增子序列的长度是1，并且存在5个子序列，因此输出5。
"""


def findNumberOfLIS(nums):
    """
    计算最长递增子序列的个数
    
    Args:
        nums: 输入的整数数组
        
    Returns:
        最长递增子序列的个数
    """
    n = len(nums)
    if n <= 1:
        return n
    
    # dp[i] 表示以 nums[i] 结尾的最长递增子序列的长度
    dp = [1] * n
    # cnt[i] 表示以 nums[i] 结尾的最长递增子序列的个数
    cnt = [1] * n
    
    maxLen = 1
    
    # 遍历每个位置
    for i in range(1, n):
        # 遍历前面所有位置
        for j in range(i):
            # 如果 nums[j] < nums[i]，可以将 nums[i] 接在以 nums[j] 结尾的递增子序列后面
            if nums[j] < nums[i]:
                # 如果找到了更长的递增子序列
                if dp[j] + 1 > dp[i]:
                    dp[i] = dp[j] + 1
                    cnt[i] = cnt[j]  # 更新个数
                # 如果找到了相同长度的递增子序列
                elif dp[j] + 1 == dp[i]:
                    cnt[i] += cnt[j]  # 累加个数
        # 更新全局最长长度
        maxLen = max(maxLen, dp[i])
    
    # 统计最长长度对应的个数
    result = 0
    for i in range(n):
        if dp[i] == maxLen:
            result += cnt[i]
    
    return result


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 3, 5, 4, 7]
    print("输入: [1,3,5,4,7]")
    print("输出: ", findNumberOfLIS(nums1))
    print("期望: 2")
    print()
    
    # 测试用例2
    nums2 = [2, 2, 2, 2, 2]
    print("输入: [2,2,2,2,2]")
    print("输出: ", findNumberOfLIS(nums2))
    print("期望: 5")
    print()
    
    # 测试用例3
    nums3 = [1, 2, 4, 3, 5, 4, 7, 2]
    print("输入: [1,2,4,3,5,4,7,2]")
    print("输出: ", findNumberOfLIS(nums3))
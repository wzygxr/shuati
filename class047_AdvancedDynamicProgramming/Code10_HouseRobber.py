"""
打家劫舍 (House Robber) - 线性动态规划 - Python实现

题目描述：
你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，
影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，
如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
给定一个代表每个房屋存放金额的非负整数数组，计算你不触动警报装置的情况下，
一夜之内能够偷窃到的最高金额。

题目来源：LeetCode 198. 打家劫舍
测试链接：https://leetcode.cn/problems/house-robber/

解题思路：
这是一个经典的线性动态规划问题。
设 dp[i] 表示偷窃前 i 个房屋能获得的最大金额。
对于第 i 个房屋，有两种选择：
1. 偷窃第 i 个房屋：dp[i] = dp[i-2] + nums[i]
2. 不偷窃第 i 个房屋：dp[i] = dp[i-1]
状态转移方程：dp[i] = max(dp[i-2] + nums[i], dp[i-1])
边界条件：dp[0] = nums[0], dp[1] = max(nums[0], nums[1])

算法实现：
1. 动态规划：使用数组存储每一步的结果
2. 空间优化：只保存前两个状态值
3. 记忆化搜索：递归计算，使用记忆化避免重复计算

时间复杂度分析：
- 动态规划：O(n)
- 空间优化：O(n)
- 记忆化搜索：O(n)

空间复杂度分析：
- 动态规划：O(n)
- 空间优化：O(1)
- 记忆化搜索：O(n)
"""

def rob1(nums):
    """
    动态规划解法
    
    Args:
        nums: 每个房屋存放金额的数组
        
    Returns:
        int: 能够偷窃到的最高金额
    """
    if not nums:
        return 0
    
    n = len(nums)
    if n == 1:
        return nums[0]
    
    if n == 2:
        return max(nums[0], nums[1])
    
    # dp[i] 表示偷窃前 i+1 个房屋能获得的最大金额
    dp = [0] * n
    dp[0] = nums[0]
    dp[1] = max(nums[0], nums[1])
    
    # 状态转移
    for i in range(2, n):
        dp[i] = max(dp[i - 2] + nums[i], dp[i - 1])
    
    return dp[n - 1]

def rob2(nums):
    """
    空间优化的动态规划解法
    
    Args:
        nums: 每个房屋存放金额的数组
        
    Returns:
        int: 能够偷窃到的最高金额
    """
    if not nums:
        return 0
    
    n = len(nums)
    if n == 1:
        return nums[0]
    
    # 只需要保存前两个状态
    prev2 = nums[0]  # dp[i-2]
    prev1 = max(nums[0], nums[1])  # dp[i-1]
    
    if n == 2:
        return prev1
    
    current = 0  # dp[i]
    
    # 状态转移
    for i in range(2, n):
        current = max(prev2 + nums[i], prev1)
        prev2 = prev1
        prev1 = current
    
    return current

def rob3(nums):
    """
    记忆化搜索解法
    
    Args:
        nums: 每个房屋存放金额的数组
        
    Returns:
        int: 能够偷窃到的最高金额
    """
    if not nums:
        return 0
    
    n = len(nums)
    from functools import lru_cache
    
    @lru_cache(maxsize=None)
    def dfs(i):
        # 边界条件
        if i < 0:
            return 0
        
        if i == 0:
            return nums[0]
        
        # 状态转移：偷窃当前房屋或不偷窃当前房屋
        return max(dfs(i - 2) + nums[i], dfs(i - 1))
    
    return dfs(n - 1)

# 测试函数
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1, 2, 3, 1]
    print("测试用例1:")
    print(f"房屋金额: {nums1}")
    print("方法1结果:", rob1(nums1))
    print("方法2结果:", rob2(nums1))
    print("方法3结果:", rob3(nums1))
    print()
    
    # 测试用例2
    nums2 = [2, 7, 9, 3, 1]
    print("测试用例2:")
    print(f"房屋金额: {nums2}")
    print("方法1结果:", rob1(nums2))
    print("方法2结果:", rob2(nums2))
    print("方法3结果:", rob3(nums2))
    print()
    
    # 测试用例3
    nums3 = [5]
    print("测试用例3:")
    print(f"房屋金额: {nums3}")
    print("方法1结果:", rob1(nums3))
    print("方法2结果:", rob2(nums3))
    print("方法3结果:", rob3(nums3))
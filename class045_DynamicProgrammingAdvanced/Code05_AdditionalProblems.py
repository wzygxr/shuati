"""
Class067 补充题目实现 - Python版本
包含各大算法平台的经典动态规划题目实现

题目来源：LeetCode、LintCode、HackerRank、牛客网、洛谷、Codeforces、AtCoder、POJ、HDU等

实现原则：
1. 代码清晰可读，注释详细
2. 包含多种解法（暴力、记忆化、DP、优化DP）
3. 提供完整的测试用例
4. 分析时间复杂度和空间复杂度
5. 考虑工程化需求（异常处理、边界条件等）

Python特性：
- 动态类型，语法简洁
- 列表推导式创建数组
- 内置函数简化代码
- 性能相对较慢但开发效率高
"""

import sys
from typing import List

class Code05_AdditionalProblems:
    
    @staticmethod
    def uniquePaths(m: int, n: int) -> int:
        """
        LeetCode 62. 不同路径
        问题描述：机器人从m×n网格的左上角移动到右下角，每次只能向右或向下移动一步
        求有多少条不同的路径
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n) 可优化为O(min(m,n))
        
        Args:
            m: 网格行数
            n: 网格列数
            
        Returns:
            int: 不同路径的数量
            
        异常处理：检查输入参数合法性
        边界处理：处理单行、单列网格等特殊情况
        """
        # 输入验证
        if m <= 0 or n <= 0:
            return 0
        if m == 1 or n == 1:
            return 1
        
        # 创建DP数组
        dp = [[1] * n for _ in range(m)]
        
        # 状态转移：dp[i][j] = dp[i-1][j] + dp[i][j-1]
        for i in range(1, m):
            for j in range(1, n):
                dp[i][j] = dp[i-1][j] + dp[i][j-1]
        
        return dp[m-1][n-1]
    
    @staticmethod
    def uniquePathsOptimized(m: int, n: int) -> int:
        """
        空间优化版本
        空间复杂度：O(min(m,n))
        """
        if m <= 0 or n <= 0:
            return 0
        if m == 1 or n == 1:
            return 1
        
        # 让n成为较小的维度以优化空间
        if m < n:
            return Code05_AdditionalProblems.uniquePathsOptimized(n, m)
        
        dp = [1] * n
        
        for i in range(1, m):
            for j in range(1, n):
                dp[j] += dp[j-1]
        
        return dp[n-1]
    
    @staticmethod
    def uniquePathsWithObstacles(obstacleGrid: List[List[int]]) -> int:
        """
        LeetCode 63. 不同路径 II（有障碍物）
        问题描述：在62题基础上，网格中有障碍物，障碍物位置不能通过
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n) 可优化为O(n)
        """
        # 输入验证
        if not obstacleGrid or not obstacleGrid[0]:
            return 0
        
        m, n = len(obstacleGrid), len(obstacleGrid[0])
        
        # 如果起点或终点有障碍物，直接返回0
        if obstacleGrid[0][0] == 1 or obstacleGrid[m-1][n-1] == 1:
            return 0
        
        dp = [[0] * n for _ in range(m)]
        
        # 初始化第一行和第一列（遇到障碍物则后面都为0）
        for i in range(m):
            if obstacleGrid[i][0] == 1:
                break
            dp[i][0] = 1
        
        for j in range(n):
            if obstacleGrid[0][j] == 1:
                break
            dp[0][j] = 1
        
        # 状态转移
        for i in range(1, m):
            for j in range(1, n):
                if obstacleGrid[i][j] == 0:
                    dp[i][j] = dp[i-1][j] + dp[i][j-1]
        
        return dp[m-1][n-1]
    
    @staticmethod
    def uniquePathsWithObstaclesOptimized(obstacleGrid: List[List[int]]) -> int:
        """
        空间优化版本
        """
        if not obstacleGrid or not obstacleGrid[0]:
            return 0
        
        m, n = len(obstacleGrid), len(obstacleGrid[0])
        if obstacleGrid[0][0] == 1 or obstacleGrid[m-1][n-1] == 1:
            return 0
        
        dp = [0] * n
        dp[0] = 1
        
        for i in range(m):
            for j in range(n):
                if obstacleGrid[i][j] == 1:
                    dp[j] = 0
                elif j > 0:
                    dp[j] += dp[j-1]
        
        return dp[n-1]
    
    @staticmethod
    def numDistinct(s: str, t: str) -> int:
        """
        LeetCode 115. 不同的子序列
        问题描述：给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n) 可优化为O(n)
        """
        # 输入验证
        if not s or not t:
            return 0
        if len(t) == 0:
            return 1
        if len(s) < len(t):
            return 0
        
        m, n = len(s), len(t)
        # 创建DP数组
        dp = [[0] * (n+1) for _ in range(m+1)]
        
        # 初始化：空字符串是任何字符串的一个子序列
        for i in range(m+1):
            dp[i][0] = 1
        
        # 状态转移
        for i in range(1, m+1):
            for j in range(1, n+1):
                if s[i-1] == t[j-1]:
                    dp[i][j] = dp[i-1][j-1] + dp[i-1][j]
                else:
                    dp[i][j] = dp[i-1][j]
        
        return dp[m][n]
    
    @staticmethod
    def numDistinctOptimized(s: str, t: str) -> int:
        """
        空间优化版本
        """
        # 输入验证
        if not s or not t:
            return 0
        if len(t) == 0:
            return 1
        if len(s) < len(t):
            return 0
        
        n = len(t)
        dp = [0] * (n+1)
        dp[0] = 1
        
        for i in range(1, len(s)+1):
            # 从后往前遍历，避免重复使用更新后的值
            for j in range(min(i, n), 0, -1):
                if s[i-1] == t[j-1]:
                    dp[j] += dp[j-1]
        
        return dp[n]
    
    @staticmethod
    def minDistance(word1: str, word2: str) -> int:
        """
        LeetCode 72. 编辑距离
        问题描述：计算将word1转换成word2所使用的最少操作数
        操作包括插入、删除、替换一个字符
        
        时间复杂度：O(m*n)
        空间复杂度：O(m*n) 可优化为O(min(m,n))
        """
        m, n = len(word1), len(word2)
        
        # 特殊情况处理
        if m == 0:
            return n
        if n == 0:
            return m
        
        # 创建DP数组
        dp = [[0] * (n+1) for _ in range(m+1)]
        
        # 初始化边界
        for i in range(m+1):
            dp[i][0] = i
        for j in range(n+1):
            dp[0][j] = j
        
        # 状态转移
        for i in range(1, m+1):
            for j in range(1, n+1):
                if word1[i-1] == word2[j-1]:
                    # 字符相同，不需要操作
                    dp[i][j] = dp[i-1][j-1]
                else:
                    # 字符不同，取三种操作的最小值 + 1
                    dp[i][j] = min(dp[i-1][j],    # 删除
                                  dp[i][j-1],    # 插入
                                  dp[i-1][j-1])  # 替换
                    dp[i][j] += 1
        
        return dp[m][n]
    
    @staticmethod
    def minDistanceOptimized(word1: str, word2: str) -> int:
        """
        空间优化版本
        """
        m, n = len(word1), len(word2)
        
        if m == 0:
            return n
        if n == 0:
            return m
        
        # 让较短的字符串作为word2以优化空间
        if m < n:
            return Code05_AdditionalProblems.minDistanceOptimized(word2, word1)
        
        dp = list(range(n+1))
        
        for i in range(1, m+1):
            prev = dp[0]  # 保存左上角的值
            dp[0] = i
            
            for j in range(1, n+1):
                temp = dp[j]
                if word1[i-1] == word2[j-1]:
                    dp[j] = prev
                else:
                    dp[j] = min(dp[j], dp[j-1], prev) + 1
                prev = temp
        
        return dp[n]
    
    @staticmethod
    def lengthOfLIS(nums: List[int]) -> int:
        """
        LeetCode 300. 最长递增子序列
        问题描述：找到数组中最长的严格递增子序列的长度
        
        方法1：动态规划 O(n^2)
        """
        if not nums:
            return 0
        
        n = len(nums)
        dp = [1] * n
        max_len = 1
        
        for i in range(1, n):
            for j in range(i):
                if nums[i] > nums[j]:
                    dp[i] = max(dp[i], dp[j] + 1)
            max_len = max(max_len, dp[i])
        
        return max_len
    
    @staticmethod
    def lengthOfLISOptimized(nums: List[int]) -> int:
        """
        方法2：贪心 + 二分查找 O(n log n)
        维护一个tails数组，tails[i]表示长度为i+1的递增子序列的最小结尾值
        """
        if not nums:
            return 0
        
        tails = []
        
        for x in nums:
            # 二分查找第一个大于等于x的位置
            left, right = 0, len(tails)
            while left < right:
                mid = (left + right) // 2
                if tails[mid] < x:
                    left = mid + 1
                else:
                    right = mid
            
            if left == len(tails):
                tails.append(x)
            else:
                tails[left] = x
        
        return len(tails)
    
    @staticmethod
    def coinChange(coins: List[int], amount: int) -> int:
        """
        LeetCode 322. 零钱兑换
        问题描述：给定不同面额的硬币和一个总金额，计算可以凑成总金额的最少硬币数
        如果无法凑成，返回-1
        
        时间复杂度：O(amount * n)
        空间复杂度：O(amount)
        """
        if amount == 0:
            return 0
        if not coins:
            return -1
        
        dp = [amount + 1] * (amount + 1)  # 初始化为一个不可能的大值
        dp[0] = 0
        
        for i in range(1, amount + 1):
            for coin in coins:
                if i >= coin:
                    dp[i] = min(dp[i], dp[i - coin] + 1)
        
        return dp[amount] if dp[amount] <= amount else -1
    
    @staticmethod
    def canPartition(nums: List[int]) -> bool:
        """
        LeetCode 416. 分割等和子集（0-1背包问题）
        问题描述：判断是否可以将数组分割成两个子集，使得两个子集的元素和相等
        
        时间复杂度：O(n * sum)
        空间复杂度：O(sum)
        """
        if len(nums) < 2:
            return False
        
        total_sum = sum(nums)
        
        # 如果和为奇数，不可能分割
        if total_sum % 2 != 0:
            return False
        
        target = total_sum // 2
        dp = [False] * (target + 1)
        dp[0] = True
        
        for num in nums:
            # 逆序遍历避免重复使用同一元素
            for j in range(target, num - 1, -1):
                dp[j] = dp[j] or dp[j - num]
        
        return dp[target]
    
    @staticmethod
    def coinChangeWays(amount: int, coins: List[int]) -> int:
        """
        HackerRank - The Coin Change Problem
        问题描述：给定不同面额的硬币和一个总金额，计算可以凑成总金额的硬币组合数
        
        时间复杂度：O(amount * n)
        空间复杂度：O(amount)
        """
        if amount == 0:
            return 1
        if not coins:
            return 0
        
        dp = [0] * (amount + 1)
        dp[0] = 1
        
        # 注意：这里先遍历硬币，再遍历金额，确保组合数（不是排列数）
        for coin in coins:
            for j in range(coin, amount + 1):
                dp[j] += dp[j - coin]
        
        return dp[amount]
    
    @staticmethod
    def boredomMaxScore(arr: List[int]) -> int:
        """
        Codeforces 455A - Boredom
        问题描述：给定数组，选择元素x获得x分，但不能再选择x-1和x+1，求最大得分
        
        时间复杂度：O(n + maxVal)
        空间复杂度：O(maxVal)
        """
        if not arr:
            return 0
        
        max_val = 100000
        count = [0] * (max_val + 1)
        
        for num in arr:
            count[num] += 1
        
        dp = [0] * (max_val + 1)
        dp[1] = count[1]
        
        for i in range(2, max_val + 1):
            # 选择i：得分 = count[i]*i + dp[i-2]
            # 不选择i：得分 = dp[i-1]
            dp[i] = max(dp[i-1], dp[i-2] + count[i] * i)
        
        return dp[max_val]
    
    @staticmethod
    def herbalMedicine(T: int, M: int, time: List[int], value: List[int]) -> int:
        """
        洛谷 P1048 采药（0-1背包问题）
        问题描述：在时间限制内选择草药，使得总价值最大
        """
        if T <= 0 or M <= 0 or not time or not value or len(time) != M or len(value) != M:
            return 0
        
        dp = [0] * (T + 1)
        
        for i in range(M):
            for j in range(T, time[i] - 1, -1):
                dp[j] = max(dp[j], dp[j - time[i]] + value[i])
        
        return dp[T]
    
    @staticmethod
    def climbStairs(n: int) -> int:
        """
        LeetCode 70. 爬楼梯
        问题描述：每次可以爬1或2个台阶，求爬到第n阶有多少种方法
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if n <= 2:
            return n
        
        prev2, prev1 = 1, 2
        for i in range(3, n + 1):
            current = prev1 + prev2
            prev2, prev1 = prev1, current
        return prev1
    
    @staticmethod
    def rob(nums: List[int]) -> int:
        """
        LeetCode 198. 打家劫舍
        问题描述：不能抢劫相邻的房屋，求能抢劫到的最大金额
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not nums:
            return 0
        if len(nums) == 1:
            return nums[0]
        
        prev2, prev1 = nums[0], max(nums[0], nums[1])
        for i in range(2, len(nums)):
            current = max(prev1, prev2 + nums[i])
            prev2, prev1 = prev1, current
        return prev1
    
    @staticmethod
    def maxSubArray(nums: List[int]) -> int:
        """
        LeetCode 53. 最大子数组和
        问题描述：找到数组中连续子数组的最大和
        
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        if not nums:
            return 0
        
        max_sum = nums[0]
        current_sum = nums[0]
        
        for i in range(1, len(nums)):
            current_sum = max(nums[i], current_sum + nums[i])
            max_sum = max(max_sum, current_sum)
        return max_sum
    
    @staticmethod
    def test():
        """
        测试方法：验证所有算法的正确性
        """
        print("=== Class067 补充题目测试 ===")
        
        # 测试不同路径
        print("不同路径测试:")
        print("3x7网格路径数:", Code05_AdditionalProblems.uniquePaths(3, 7))
        print("空间优化版本:", Code05_AdditionalProblems.uniquePathsOptimized(3, 7))
        print("预期结果: 28")
        print()
        
        # 测试编辑距离
        print("编辑距离测试:")
        print("'horse'到'ros':", Code05_AdditionalProblems.minDistance("horse", "ros"))
        print("空间优化版本:", Code05_AdditionalProblems.minDistanceOptimized("horse", "ros"))
        print("预期结果: 3")
        print()
        
        # 测试不同的子序列
        print("不同的子序列测试:")
        print("'rabbbit'中'rabbit'的个数:", Code05_AdditionalProblems.numDistinct("rabbbit", "rabbit"))
        print("空间优化版本:", Code05_AdditionalProblems.numDistinctOptimized("rabbbit", "rabbit"))
        print("预期结果: 3")
        print()
        
        # 测试最长递增子序列
        print("最长递增子序列测试:")
        nums = [10, 9, 2, 5, 3, 7, 101, 18]
        print("数组:", nums)
        print("DP方法:", Code05_AdditionalProblems.lengthOfLIS(nums))
        print("优化方法:", Code05_AdditionalProblems.lengthOfLISOptimized(nums))
        print("预期结果: 4")
        print()
        
        # 测试零钱兑换
        print("零钱兑换测试:")
        coins = [1, 2, 5]
        print("硬币:", coins, ", 金额: 11")
        print("最少硬币数:", Code05_AdditionalProblems.coinChange(coins, 11))
        print("预期结果: 3")
        print()
        
        # 测试分割等和子集
        print("分割等和子集测试:")
        nums2 = [1, 5, 11, 5]
        print("数组:", nums2)
        print("能否分割:", Code05_AdditionalProblems.canPartition(nums2))
        print("预期结果: True")
        print()
        
        # 测试爬楼梯
        print("爬楼梯测试:")
        print("爬5阶楼梯的方法数:", Code05_AdditionalProblems.climbStairs(5))
        print("预期结果: 8")
        print()
        
        # 测试打家劫舍
        print("打家劫舍测试:")
        nums3 = [2, 7, 9, 3, 1]
        print("房屋金额:", nums3)
        print("最大金额:", Code05_AdditionalProblems.rob(nums3))
        print("预期结果: 12")
        print()
        
        # 测试最大子数组和
        print("最大子数组和测试:")
        nums4 = [-2, 1, -3, 4, -1, 2, 1, -5, 4]
        print("数组:", nums4)
        print("最大子数组和:", Code05_AdditionalProblems.maxSubArray(nums4))
        print("预期结果: 6")
        print()
        
        print("=== 测试完成 ===")

# 主函数：运行测试用例
if __name__ == "__main__":
    Code05_AdditionalProblems.test()
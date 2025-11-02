import sys
from typing import List

class Solution:
    """
    LeetCode 1770. 执行乘法运算的最大分数
    题目链接：https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
    难度：中等
    
    题目描述：
    给你两个长度分别为 n 和 m 的整数数组 nums 和 multipliers。
    你需要执行恰好 m 步操作。在第 i 步操作（从 1 开始计数）中，你需要：
    - 选择数组 nums 开头或者结尾的一个元素 x
    - 获得 multipliers[i] * x 的分数，并将 x 从数组 nums 中移除
    
    解题思路：
    这是一个区间动态规划问题，需要处理从数组两端取元素的情况。
    状态定义：dp[i][j]表示已经取了i个开头元素和j个结尾元素时的最大分数
    状态转移：每次可以选择取开头或结尾的元素
    
    时间复杂度：O(m^2)
    空间复杂度：O(m^2)
    
    工程化考量：
    1. 空间优化：使用滚动数组将空间复杂度优化到O(m)
    2. 边界条件处理：m可能为0的情况
    3. 优化：只考虑必要的状态
    
    相关题目扩展：
    1. LeetCode 1770. 执行乘法运算的最大分数 - https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
    2. LeetCode 486. 预测赢家 - https://leetcode.cn/problems/predict-the-winner/
    3. LeetCode 877. 石子游戏 - https://leetcode.cn/problems/stone-game/
    4. LeetCode 1140. 石子游戏 II - https://leetcode.cn/problems/stone-game-ii/
    5. LeetCode 1406. 石子游戏 III - https://leetcode.cn/problems/stone-game-iii/
    6. LintCode 390. 石子游戏 - https://www.lintcode.com/problem/390/
    7. LintCode 1718. 石子游戏 VI - https://www.lintcode.com/problem/1718/
    8. HackerRank - Game of Stones - https://www.hackerrank.com/challenges/game-of-stones-1/problem
    9. Codeforces 1312C - Add One - https://codeforces.com/problemset/problem/1312/C
    10. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
    """
    
    def maximumScore(self, nums: List[int], multipliers: List[int]) -> int:
        """
        区间动态规划解法
        时间复杂度：O(m^2)
        空间复杂度：O(m^2)
        """
        n = len(nums)
        m = len(multipliers)
        
        # dp[i][j]表示取了i个开头元素和j个结尾元素时的最大分数
        dp = [[-10**9] * (m + 1) for _ in range(m + 1)]
        dp[0][0] = 0
        
        # 动态规划
        for total in range(1, m + 1):
            for left in range(total + 1):
                right = total - left
                
                # 情况1：当前取的是开头元素
                if left > 0:
                    score1 = dp[left - 1][right] + multipliers[total - 1] * nums[left - 1]
                    dp[left][right] = max(dp[left][right], score1)
                
                # 情况2：当前取的是结尾元素
                if right > 0:
                    score2 = dp[left][right - 1] + multipliers[total - 1] * nums[n - right]
                    dp[left][right] = max(dp[left][right], score2)
        
        # 找到最大分数
        max_score = -10**9
        for left in range(m + 1):
            right = m - left
            if dp[left][right] > max_score:
                max_score = dp[left][right]
        
        return max_score
    
    def maximumScoreOptimized(self, nums: List[int], multipliers: List[int]) -> int:
        """
        优化版本：使用一维DP数组
        时间复杂度：O(m^2)
        空间复杂度：O(m)
        """
        n = len(nums)
        m = len(multipliers)
        
        # dp[i]表示取了i个开头元素时的最大分数
        dp = [-10**9] * (m + 1)
        dp[0] = 0
        
        # 动态规划
        for op in range(m):
            next_dp = [-10**9] * (m + 1)
            
            for left in range(op + 2):
                right = op + 1 - left
                
                # 情况1：当前取的是开头元素
                if left > 0:
                    score1 = dp[left - 1] + multipliers[op] * nums[left - 1]
                    next_dp[left] = max(next_dp[left], score1)
                
                # 情况2：当前取的是结尾元素
                if right > 0:
                    score2 = dp[left] + multipliers[op] * nums[n - right]
                    next_dp[left] = max(next_dp[left], score2)
            
            dp = next_dp
        
        # 找到最大分数
        max_score = -10**9
        for score in dp:
            if score > max_score:
                max_score = score
        
        return max_score

def test_maximum_score():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 2, 3]
    multipliers1 = [3, 2, 1]
    print("测试用例1: nums = [1,2,3], multipliers = [3,2,1]")
    print("预期结果: 14")
    result1 = solution.maximumScore(nums1, multipliers1)
    result1_opt = solution.maximumScoreOptimized(nums1, multipliers1)
    print(f"DP解法: {result1}")
    print(f"优化版本: {result1_opt}")
    print()
    
    # 测试用例2
    nums2 = [-5, -3, -3, -2, 7, 1]
    multipliers2 = [-10, -5, 3, 4, 6]
    print("测试用例2: 复杂数组")
    result2 = solution.maximumScore(nums2, multipliers2)
    result2_opt = solution.maximumScoreOptimized(nums2, multipliers2)
    print(f"DP解法: {result2}")
    print(f"优化版本: {result2_opt}")

if __name__ == "__main__":
    test_maximum_score()
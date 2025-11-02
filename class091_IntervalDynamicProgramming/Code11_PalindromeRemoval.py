import sys
from typing import List

class Solution:
    """
    LeetCode 1246. 删除回文子数组
    题目链接：https://leetcode.cn/problems/palindrome-removal/
    难度：困难
    
    题目描述：
    给你一个整数数组 arr，每一次操作你都可以选择并删除它的一个回文子数组。
    注意，每当你删除掉一个子数组后，右侧元素会自动左侧移动以填补空缺。
    请你计算并返回从数组中删除所有数字所需的最少操作次数。
    
    解题思路：
    这是一个区间动态规划问题，需要处理回文子数组的删除。
    状态定义：dp[i][j]表示删除区间[i,j]所需的最少操作次数
    状态转移：
    1. 如果arr[i] == arr[j]，可以一起删除
    2. 枚举分割点k，将区间分成两部分分别删除
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    
    工程化考量：
    1. 边界条件处理：单个元素是回文，操作次数为1
    2. 优化：当arr[i] == arr[j]时，可以利用dp[i+1][j-1]的结果
    3. 特殊情况：整个数组是回文时，只需要1次操作
    
    相关题目扩展：
    1. LeetCode 1246. 删除回文子数组 - https://leetcode.cn/problems/palindrome-removal/
    2. LeetCode 1312. 让字符串成为回文串的最少插入次数 - https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
    3. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
    4. LeetCode 1216. 验证回文字符串 III - https://leetcode.cn/problems/valid-palindrome-iii/
    5. LeetCode 1682. 最长回文子序列 II - https://leetcode.cn/problems/longest-palindromic-subsequence-ii/
    6. LintCode 1419. 最少行程 - https://www.lintcode.com/problem/1419/
    7. LintCode 1797. 模糊坐标 - https://www.lintcode.com/problem/1797/
    8. HackerRank - Palindrome Index - https://www.hackerrank.com/challenges/palindrome-index/problem
    9. Codeforces 1373C - Pluses and Minuses - https://codeforces.com/problemset/problem/1373/C
    10. AtCoder ABC161D - Lunlun Number - https://atcoder.jp/contests/abc161/tasks/abc161_d
    """
    
    def minimumMoves(self, arr: List[int]) -> int:
        """
        区间动态规划解法
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        """
        n = len(arr)
        if n == 0:
            return 0
        
        # dp[i][j]表示删除区间[i,j]所需的最少操作次数
        dp = [[10**9] * n for _ in range(n)]
        
        # 初始化：单个元素需要1次操作
        for i in range(n):
            dp[i][i] = 1
        
        # 两个元素的情况
        for i in range(n - 1):
            if arr[i] == arr[i + 1]:
                dp[i][i + 1] = 1  # 两个相同元素可以一次删除
            else:
                dp[i][i + 1] = 2  # 两个不同元素需要两次删除
        
        # 区间动态规划
        for length in range(3, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                
                # 情况1：如果首尾元素相同，可以一起删除
                if arr[i] == arr[j]:
                    dp[i][j] = dp[i + 1][j - 1]
                
                # 情况2：枚举分割点
                for k in range(i, j):
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j])
        
        return int(dp[0][n - 1])
    
    def minimumMovesOptimized(self, arr: List[int]) -> int:
        """
        优化版本
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        """
        n = len(arr)
        if n == 0:
            return 0
        
        dp = [[10**9] * n for _ in range(n)]
        
        # 初始化
        for i in range(n):
            dp[i][i] = 1
        
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                
                # 默认情况：单独删除第一个元素
                dp[i][j] = dp[i + 1][j] + 1
                
                # 如果arr[i] == arr[k]，可以考虑一起删除
                for k in range(i + 1, j + 1):
                    if arr[i] == arr[k]:
                        left = dp[i + 1][k - 1] if i + 1 <= k - 1 else 0
                        right = dp[k + 1][j] if k + 1 <= j else 0
                        cost = left + right
                        if left == 0 and right == 0:
                            cost = 1
                        dp[i][j] = min(dp[i][j], cost)
        
        return int(dp[0][n - 1])

def test_palindrome_removal():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    arr1 = [1, 2]
    print("测试用例1: arr = [1,2]")
    print("预期结果: 2")
    result1 = solution.minimumMoves(arr1)
    result1_opt = solution.minimumMovesOptimized(arr1)
    print(f"实际结果: {result1}")
    print(f"优化版本: {result1_opt}")
    print()
    
    # 测试用例2
    arr2 = [1, 3, 4, 1, 5]
    print("测试用例2: arr = [1,3,4,1,5]")
    print("预期结果: 3")
    result2 = solution.minimumMoves(arr2)
    result2_opt = solution.minimumMovesOptimized(arr2)
    print(f"实际结果: {result2}")
    print(f"优化版本: {result2_opt}")
    print()
    
    # 测试用例3
    arr3 = [1, 2, 3, 4, 5]
    print("测试用例3: arr = [1,2,3,4,5]")
    print("预期结果: 5")
    result3 = solution.minimumMoves(arr3)
    result3_opt = solution.minimumMovesOptimized(arr3)
    print(f"实际结果: {result3}")
    print(f"优化版本: {result3_opt}")

if __name__ == "__main__":
    test_palindrome_removal()
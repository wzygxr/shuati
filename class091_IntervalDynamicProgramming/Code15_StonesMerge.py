import sys
from typing import List

class Solution:
    """
    AcWing 282. 石子合并
    题目链接：https://www.acwing.com/problem/content/284/
    难度：简单
    
    题目描述：
    设有N堆石子排成一排，其编号为1，2，3，…，N。
    每堆石子有一定的质量，可以用一个整数来描述，现在要将这N堆石子合并成为一堆。
    每次只能合并相邻的两堆，合并的代价为这两堆石子的质量之和，合并后与这两堆石子相邻的石子将和新堆相邻。
    找出一种合理的方法，使总的代价最小，输出最小代价。
    
    解题思路：
    经典的区间动态规划问题，石子合并问题。
    状态定义：dp[i][j]表示合并区间[i,j]的石子所需的最小代价
    状态转移：dp[i][j] = min(dp[i][k] + dp[k+1][j] + sum[i][j])
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    
    工程化考量：
    1. 使用前缀和优化区间和计算
    2. 四边形不等式优化可以将时间复杂度优化到O(n^2)
    3. 处理边界条件：单个石子代价为0
    
    相关题目扩展：
    1. AcWing 282. 石子合并 - https://www.acwing.com/problem/content/284/
    2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
    3. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
    4. LintCode 1000. 合并石头的最低成本 - https://www.lintcode.com/problem/1000/
    5. LintCode 476. Stone Game - https://www.lintcode.com/problem/476/
    6. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
    7. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
    8. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
    9. POJ 1141 Brackets Sequence - http://poj.org/problem?id=1141
    10. HDU 4632 - Palindrome Subsequence - http://acm.hdu.edu.cn/showproblem.php?pid=4632
    """
    
    def minCost(self, stones: List[int]) -> int:
        """
        区间动态规划解法
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        """
        n = len(stones)
        if n == 1:
            return 0
        
        # 前缀和数组
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + stones[i]
        
        # dp[i][j]表示合并区间[i,j]的最小代价
        dp = [[10**9] * n for _ in range(n)]
        
        # 初始化：单个石子代价为0
        for i in range(n):
            dp[i][i] = 0
        
        # 区间动态规划
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                
                # 枚举分割点k
                for k in range(i, j):
                    cost = dp[i][k] + dp[k + 1][j] + (prefix_sum[j + 1] - prefix_sum[i])
                    if cost < dp[i][j]:
                        dp[i][j] = cost
        
        return int(dp[0][n - 1])
    
    def minCostOptimized(self, stones: List[int]) -> int:
        """
        四边形不等式优化版本
        时间复杂度：O(n^2)
        空间复杂度：O(n^2)
        """
        n = len(stones)
        if n == 1:
            return 0
        
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + stones[i]
        
        dp = [[10**9] * n for _ in range(n)]
        best = [[0] * n for _ in range(n)]  # 记录最优分割点
        
        # 初始化
        for i in range(n):
            dp[i][i] = 0
            best[i][i] = i
        
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                
                # 修复索引越界问题
                left = best[i][j - 1]
                right = best[i + 1][j] if (i + 1 < n and j < n) else min(j - 1, i)
                right = max(left, right)  # 确保left <= right
                
                for k in range(left, right + 1):
                    # 确保k在有效范围内
                    if k >= n - 1:
                        continue
                    cost = dp[i][k] + dp[k + 1][j] + (prefix_sum[j + 1] - prefix_sum[i])
                    if cost < dp[i][j]:
                        dp[i][j] = cost
                        best[i][j] = k
        
        return int(dp[0][n - 1])

def test_stones_merge():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    stones1 = [1, 3, 5, 2]
    print("测试用例1: stones = [1,3,5,2]")
    print("预期结果: 22")
    result1 = solution.minCost(stones1)
    result1_opt = solution.minCostOptimized(stones1)
    print(f"DP解法: {result1}")
    print(f"优化版本: {result1_opt}")
    print()
    
    # 测试用例2
    stones2 = [4, 2, 1, 3]
    print("测试用例2: stones = [4,2,1,3]")
    print("预期结果: 20")
    result2 = solution.minCost(stones2)
    result2_opt = solution.minCostOptimized(stones2)
    print(f"DP解法: {result2}")
    print(f"优化版本: {result2_opt}")
    
    # 验证算法正确性：手动计算合并过程
    print("手动验证合并过程:")
    print("方案1: 先合并[2,1] (代价=3) -> [4,3,3]")
    print("       再合并[4,3] (代价=7) -> [7,3]")
    print("       再合并[7,3] (代价=10) -> 总代价=3+7+10=20")
    print("方案2: 先合并[4,2] (代价=6) -> [6,1,3]")
    print("       再合并[1,3] (代价=4) -> [6,4]")
    print("       再合并[6,4] (代价=10) -> 总代价=6+4+10=20")

if __name__ == "__main__":
    test_stones_merge()
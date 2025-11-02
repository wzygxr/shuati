import sys
from typing import List

class Solution:
    """
    HackerRank - Sherlock and Cost
    题目链接：https://www.hackerrank.com/challenges/sherlock-and-cost/problem
    难度：中等
    
    题目描述：
    给定一个数组B，需要构造一个数组A，使得：
    1. 1 <= A[i] <= B[i] 对于所有i
    2. 最大化 S = Σ|A[i] - A[i-1]| (i从1到n-1)
    
    解题思路：
    这是一个动态规划问题，需要处理每个位置取1或B[i]的情况。
    状态定义：dp[i][0]表示第i个位置取1时的最大和，dp[i][1]表示第i个位置取B[i]时的最大和
    
    时间复杂度：O(n)
    空间复杂度：O(n) 可以优化到O(1)
    
    工程化考量：
    1. 空间优化：使用滚动变量代替数组
    2. 边界条件处理：单个元素的情况
    3. 优化：只需要前一个状态的信息
    
    相关题目扩展：
    1. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
    2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
    3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
    4. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
    5. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
    6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
    7. LintCode 1639. K 倍重复项删除 - https://www.lintcode.com/problem/1639/
    8. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
    9. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
    10. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
    """
    
    def cost(self, B: List[int]) -> int:
        """
        动态规划解法
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        n = len(B)
        if n <= 1:
            return 0
        
        # dp[i][0]: 第i个位置取1时的最大和
        # dp[i][1]: 第i个位置取B[i]时的最大和
        dp = [[0] * 2 for _ in range(n)]
        
        for i in range(1, n):
            # 当前取1，前一个取1
            diff1 = dp[i-1][0] + abs(1 - 1)
            # 当前取1，前一个取B[i-1]
            diff2 = dp[i-1][1] + abs(1 - B[i-1])
            dp[i][0] = max(diff1, diff2)
            
            # 当前取B[i]，前一个取1
            diff3 = dp[i-1][0] + abs(B[i] - 1)
            # 当前取B[i]，前一个取B[i-1]
            diff4 = dp[i-1][1] + abs(B[i] - B[i-1])
            dp[i][1] = max(diff3, diff4)
        
        return max(dp[n-1][0], dp[n-1][1])
    
    def costOptimized(self, B: List[int]) -> int:
        """
        空间优化版本
        时间复杂度：O(n)
        空间复杂度：O(1)
        """
        n = len(B)
        if n <= 1:
            return 0
        
        prev_low = 0  # 前一个位置取1时的最大和
        prev_high = 0  # 前一个位置取B[i]时的最大和
        
        for i in range(1, n):
            current_low = max(prev_low, prev_high + abs(1 - B[i-1]))
            current_high = max(prev_low + abs(B[i] - 1), 
                             prev_high + abs(B[i] - B[i-1]))
            
            prev_low = current_low
            prev_high = current_high
        
        return max(prev_low, prev_high)

def test_sherlock_cost():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    B1 = [1, 2, 3]
    print("测试用例1: B = [1,2,3]")
    print("预期结果: 2")
    result1 = solution.cost(B1)
    result1_opt = solution.costOptimized(B1)
    print(f"DP解法: {result1}")
    print(f"优化版本: {result1_opt}")
    print()
    
    # 测试用例2
    B2 = [10, 1, 10, 1, 10]
    print("测试用例2: B = [10,1,10,1,10]")
    print("预期结果: 36")
    result2 = solution.cost(B2)
    result2_opt = solution.costOptimized(B2)
    print(f"DP解法: {result2}")
    print(f"优化版本: {result2_opt}")

if __name__ == "__main__":
    test_sherlock_cost()
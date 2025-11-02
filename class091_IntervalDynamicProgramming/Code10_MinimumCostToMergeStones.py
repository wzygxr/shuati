import sys
from typing import List

class Solution:
    """
    LeetCode 1000. 合并石头的最低成本
    题目链接：https://leetcode.cn/problems/minimum-cost-to-merge-stones/
    难度：困难
    
    题目描述：
    有 N 堆石头排成一排，第 i 堆中有 stones[i] 块石头。
    每次移动（move）需要将连续的 K 堆石头合并为一堆，而这个移动的成本为这 K 堆石头的总数。
    找出把所有石头合并成一堆的最低成本。如果不可能，返回 -1。
    
    解题思路：
    这是一个经典的区间动态规划问题，需要处理K堆合并的特殊情况。
    状态定义：dp[i][j]表示将区间[i,j]的石头合并成若干堆的最小成本
    状态转移：枚举分割点k，将区间分成两部分进行合并
    
    时间复杂度：O(n^3 * K)，其中n为石头堆数
    空间复杂度：O(n^2)
    
    工程化考量：
    1. 边界条件处理：当K=1时直接返回0，当(n-1)%(K-1)!=0时返回-1
    2. 前缀和优化：使用前缀和数组快速计算区间和
    3. 状态压缩：可以优化空间复杂度到O(n)
    
    Python实现注意事项：
    1. 使用动态规划时注意列表索引范围
    2. 使用float('inf')表示无穷大
    3. 注意Python的列表切片操作
    4. 使用前缀和优化区间和计算
    
    相关题目扩展：
    1. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
    2. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
    3. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
    4. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
    5. LintCode 1000. 合并石头的最低成本 - https://www.lintcode.com/problem/1000/
    6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
    7. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
    8. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
    9. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
    10. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
    """
    
    def mergeStones(self, stones: List[int], K: int) -> int:
        """
        合并石头的最低成本 - 区间动态规划解法
        时间复杂度：O(n^3 * K)
        空间复杂度：O(n^2)
        
        Args:
            stones: 石头数组
            K: 每次合并的堆数
            
        Returns:
            int: 最小成本，如果不可能返回-1
        """
        n = len(stones)
        
        # 特殊情况处理
        if n == 1:
            return 0
        if K < 2 or (n - 1) % (K - 1) != 0:
            return -1
        
        # 前缀和数组
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + stones[i]
        
        # DP数组初始化
        # dp[i][j]表示将区间[i,j]合并成若干堆的最小成本
        dp = [[float('inf')] * n for _ in range(n)]
        
        # 单个堆的成本为0
        for i in range(n):
            dp[i][i] = 0
        
        # 按区间长度从小到大进行动态规划
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                
                # 枚举分割点，步长为K-1
                for k in range(i, j, K - 1):
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j])
                
                # 如果当前区间可以合并成一堆，需要加上合并成本
                if (length - 1) % (K - 1) == 0:
                    dp[i][j] += prefix_sum[j + 1] - prefix_sum[i]
        
        return dp[0][n - 1] if dp[0][n - 1] != float('inf') else -1
    
    def mergeStonesOptimized(self, stones: List[int], K: int) -> int:
        """
        优化版本：使用三维DP
        时间复杂度：O(n^3 * K^2)
        空间复杂度：O(n^2 * K)
        
        Args:
            stones: 石头数组
            K: 每次合并的堆数
            
        Returns:
            int: 最小成本，如果不可能返回-1
        """
        n = len(stones)
        if (n - 1) % (K - 1) != 0:
            return -1
        
        # 前缀和数组
        prefix_sum = [0] * (n + 1)
        for i in range(n):
            prefix_sum[i + 1] = prefix_sum[i] + stones[i]
        
        # 三维DP数组：dp[i][j][m]表示区间[i,j]合并成m堆的最小成本
        # 使用嵌套列表推导式创建三维数组
        dp = [[[float('inf')] * (K + 1) for _ in range(n)] for _ in range(n)]
        
        # 初始化
        for i in range(n):
            dp[i][i][1] = 0  # 单个堆合并成1堆成本为0
        
        # 按区间长度从小到大计算
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                
                # 枚举分割点
                for k in range(i, j):
                    for m1 in range(1, K + 1):
                        for m2 in range(1, K + 1):
                            if m1 + m2 <= K:
                                if dp[i][k][m1] != float('inf') and dp[k + 1][j][m2] != float('inf'):
                                    dp[i][j][m1 + m2] = min(
                                        dp[i][j][m1 + m2],
                                        dp[i][k][m1] + dp[k + 1][j][m2]
                                    )
                
                # 如果可以合并成1堆，需要加上合并成本
                if dp[i][j][K] != float('inf'):
                    dp[i][j][1] = min(
                        dp[i][j][1],
                        dp[i][j][K] + prefix_sum[j + 1] - prefix_sum[i]
                    )
        
        return dp[0][n - 1][1] if dp[0][n - 1][1] != float('inf') else -1

def test_merge_stones():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    stones1 = [3, 2, 4, 1]
    K1 = 2
    print("测试用例1: stones = [3,2,4,1], K = 2")
    print("预期结果: 20")
    result1 = solution.mergeStones(stones1, K1)
    result1_opt = solution.mergeStonesOptimized(stones1, K1)
    print(f"实际结果: {result1}")
    print(f"优化版本: {result1_opt}")
    print()
    
    # 测试用例2
    stones2 = [3, 2, 4, 1]
    K2 = 3
    print("测试用例2: stones = [3,2,4,1], K = 3")
    print("预期结果: -1")
    result2 = solution.mergeStones(stones2, K2)
    print(f"实际结果: {result2}")
    print()
    
    # 测试用例3
    stones3 = [3, 5, 1, 2, 6]
    K3 = 3
    print("测试用例3: stones = [3,5,1,2,6], K = 3")
    print("预期结果: 25")
    result3 = solution.mergeStones(stones3, K3)
    result3_opt = solution.mergeStonesOptimized(stones3, K3)
    print(f"实际结果: {result3}")
    print(f"优化版本: {result3_opt}")

if __name__ == "__main__":
    test_merge_stones()
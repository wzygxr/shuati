import sys
from typing import List

class Solution:
    """
    LeetCode 1130. 叶值的最小代价生成树
    题目链接：https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
    难度：中等
    
    题目描述：
    给你一个正整数数组 arr，考虑所有满足以下条件的二叉树：
    1. 每个节点都有 0 个或 2 个子节点
    2. 数组 arr 中的值与树的中序遍历中每个叶节点的值一一对应
    3. 每个非叶节点的值等于其左子树和右子树中叶节点的最大值的乘积
    
    解题思路：
    这是一个区间动态规划问题，需要构建最优二叉树。
    状态定义：dp[i][j]表示区间[i,j]构建二叉树的最小代价
    辅助数组：max[i][j]表示区间[i,j]中的最大值
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    
    工程化考量：
    1. 使用单调栈优化到O(n)时间复杂度
    2. 处理边界条件：单个叶节点的情况
    3. 优化：预处理区间最大值
    
    相关题目扩展：
    1. LeetCode 1130. 叶值的最小代价生成树 - https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
    2. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
    3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
    4. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
    5. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
    6. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
    7. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
    8. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
    9. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
    10. 洛谷 P1880 [NOI1995] 石子合并 - https://www.luogu.com.cn/problem/P1880
    """
    
    def mctFromLeafValues(self, arr: List[int]) -> int:
        """
        区间动态规划解法
        时间复杂度：O(n^3)
        空间复杂度：O(n^2)
        """
        n = len(arr)
        if n == 1:
            return 0
        
        # DP数组和最大值数组
        dp = [[10**9] * n for _ in range(n)]
        max_val = [[0] * n for _ in range(n)]
        
        # 初始化max数组
        for i in range(n):
            max_val[i][i] = arr[i]
            for j in range(i + 1, n):
                max_val[i][j] = max(max_val[i][j - 1], arr[j])
        
        # 初始化dp数组
        for i in range(n):
            dp[i][i] = 0
        
        # 两个叶节点的情况
        for i in range(n - 1):
            dp[i][i + 1] = arr[i] * arr[i + 1]
        
        # 区间动态规划
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                
                for k in range(i, j):
                    cost = dp[i][k] + dp[k + 1][j] + max_val[i][k] * max_val[k + 1][j]
                    dp[i][j] = min(dp[i][j], cost)
        
        return int(dp[0][n - 1])
    
    def mctFromLeafValuesStack(self, arr: List[int]) -> int:
        """
        单调栈优化解法
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        res = 0
        stack = [10**9]  # 哨兵
        
        for num in arr:
            while stack[-1] <= num:
                mid = stack.pop()
                res += mid * min(stack[-1], num)
            stack.append(num)
        
        while len(stack) > 2:
            res += stack.pop() * stack[-1]
        
        return res
    
    def mctFromLeafValuesGreedy(self, arr: List[int]) -> int:
        """
        贪心解法
        时间复杂度：O(n^2)
        空间复杂度：O(n)
        """
        arr_list = arr.copy()
        res = 0
        
        while len(arr_list) > 1:
            min_product = 10**9
            min_index = -1
            
            for i in range(len(arr_list) - 1):
                product = arr_list[i] * arr_list[i + 1]
                if product < min_product:
                    min_product = product
                    min_index = i
            
            res += min_product
            max_val = max(arr_list[min_index], arr_list[min_index + 1])
            arr_list.pop(min_index + 1)
            arr_list[min_index] = max_val
        
        return res

def test_mct():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    arr1 = [6, 2, 4]
    print("测试用例1: arr = [6,2,4]")
    print("预期结果: 32")
    result1 = solution.mctFromLeafValues(arr1)
    result1_stack = solution.mctFromLeafValuesStack(arr1)
    result1_greedy = solution.mctFromLeafValuesGreedy(arr1)
    print(f"DP解法: {result1}")
    print(f"单调栈: {result1_stack}")
    print(f"贪心解法: {result1_greedy}")
    print()
    
    # 测试用例2
    arr2 = [4, 11]
    print("测试用例2: arr = [4,11]")
    print("预期结果: 44")
    result2 = solution.mctFromLeafValues(arr2)
    result2_stack = solution.mctFromLeafValuesStack(arr2)
    result2_greedy = solution.mctFromLeafValuesGreedy(arr2)
    print(f"DP解法: {result2}")
    print(f"单调栈: {result2_stack}")
    print(f"贪心解法: {result2_greedy}")

if __name__ == "__main__":
    test_mct()
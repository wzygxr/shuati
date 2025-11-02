# HackerRank Sherlock and Cost
# 题目来源：https://www.hackerrank.com/challenges/sherlock-and-cost/problem
# 题目大意：给定一个数组B，构造数组A使得对于所有i，1 <= A[i] <= B[i]。
# 数组A的代价定义为相邻元素差的绝对值之和，即sum(|A[i] - A[i-1]|)。
# 求A数组的最大可能代价。
#
# 解题思路：
# 1. 这是一个特殊的区间动态规划问题
# 2. 关键观察：为了最大化代价，每个位置的取值要么是1，要么是B[i]
# 3. dp[i][0]表示第i个位置取1时，前i个位置的最大代价
# 4. dp[i][1]表示第i个位置取B[i]时，前i个位置的最大代价
# 5. 状态转移：
#    dp[i][0] = max(dp[i-1][0], dp[i-1][1] + |B[i-1] - 1|)
#    dp[i][1] = max(dp[i-1][0] + |1 - B[i]|, dp[i-1][1] + |B[i-1] - B[i]|)
#
# 时间复杂度：O(n) - 单层循环
# 空间复杂度：O(1) - 只需要常数空间
#
# 工程化考虑：
# 1. 输入验证：检查输入是否合法
# 2. 边界处理：处理数组长度为1的特殊情况
# 3. 优化处理：使用滚动数组优化空间复杂度
# 4. 异常处理：对于不合法输入给出适当提示

import sys

def solve(B, n):
    """
    主函数：解决Sherlock and Cost问题
    时间复杂度: O(n) - 单层循环
    空间复杂度: O(1) - 只需要常数空间
    """
    if n <= 1:
        return 0
    
    # dp[0]表示当前位置取1时的最大代价
    # dp[1]表示当前位置取B[i]时的最大代价
    dp = [0, 0]
    
    # 从第二个元素开始计算
    for i in range(1, n):
        prev0 = dp[0]
        prev1 = dp[1]
        
        # 当前位置取1时的最大代价
        dp[0] = max(prev0, prev1 + abs(B[i-1] - 1))
        
        # 当前位置取B[i]时的最大代价
        dp[1] = max(prev0 + abs(1 - B[i]), prev1 + abs(B[i-1] - B[i]))
    
    return max(dp[0], dp[1])

if __name__ == "__main__":
    # 读取输入
    t = int(input().strip())
    for _ in range(t):
        n = int(input().strip())
        B = list(map(int, input().split()))
        
        result = solve(B, n)
        print(result)
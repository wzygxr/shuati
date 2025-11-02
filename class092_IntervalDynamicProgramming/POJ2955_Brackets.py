# POJ 2955 Brackets
# 题目来源：http://poj.org/problem?id=2955
# 题目大意：给定一个只包含'('、')'、'['、']'的括号序列，求最长的合法括号子序列的长度。
# 合法括号序列定义：
# 1. 空序列是合法的
# 2. 如果A是合法的，则(A)和[A]都是合法的
# 3. 如果A和B都是合法的，则AB也是合法的
#
# 解题思路：
# 1. 使用区间动态规划，dp[i][j]表示区间[i,j]内最长合法括号子序列的长度
# 2. 状态转移：
#    - 如果s[i]和s[j]匹配，则dp[i][j] = dp[i+1][j-1] + 2
#    - 否则枚举分割点k，dp[i][j] = max(dp[i][k] + dp[k+1][j])
#
# 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
# 空间复杂度：O(n^2) - dp数组占用空间
#
# 工程化考虑：
# 1. 输入验证：检查输入是否为空
# 2. 边界处理：处理长度为0、1的特殊情况
# 3. 匹配判断：正确判断括号是否匹配
# 4. 异常处理：对于不合法输入给出适当提示

import sys

def solve(s):
    """
    主函数：解决最长合法括号子序列问题
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    """
    n = len(s)
    if n == 0:
        return 0
    
    # dp[i][j]表示区间[i,j]内最长合法括号子序列的长度
    dp = [[0] * n for _ in range(n)]
    
    # 初始化：单个字符无法构成合法序列
    for i in range(n):
        dp[i][i] = 0
    
    # 枚举区间长度，从2开始
    for length in range(2, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            # 计算区间终点j
            j = i + length - 1
            
            # 如果两端字符匹配
            if (s[i] == '(' and s[j] == ')') or (s[i] == '[' and s[j] == ']'):
                if length == 2:
                    # 长度为2且匹配
                    dp[i][j] = 2
                else:
                    # 长度大于2且匹配
                    dp[i][j] = dp[i+1][j-1] + 2
            
            # 枚举分割点k，取最大值
            for k in range(i, j):
                dp[i][j] = max(dp[i][j], dp[i][k] + dp[k+1][j])
    
    return dp[0][n-1]

if __name__ == "__main__":
    # 读取输入
    try:
        while True:
            line = input().strip()
            if line == "end":
                break
            result = solve(line)
            print(result)
    except EOFError:
        pass
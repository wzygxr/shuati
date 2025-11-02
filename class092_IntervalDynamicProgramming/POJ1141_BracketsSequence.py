# POJ 1141 Brackets Sequence
# 题目来源：http://poj.org/problem?id=1141
# 题目大意：给定一个括号序列，可能包含'('、')'、'['、']'，要求添加最少的括号使其成为合法的括号序列，
# 并输出字典序最小的合法序列。
#
# 解题思路：
# 1. 使用区间动态规划，dp[i][j]表示使区间[i,j]成为合法括号序列需要添加的最少括号数
# 2. 状态转移：
#    - 如果s[i]和s[j]匹配，则dp[i][j] = dp[i+1][j-1]
#    - 否则枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k+1][j])
# 3. 通过path数组记录路径，用于构造最终的合法序列
#
# 时间复杂度：O(n^3) - 三层循环：区间长度、区间起点、分割点
# 空间复杂度：O(n^2) - dp和path数组占用空间
#
# 工程化考虑：
# 1. 输入验证：检查输入是否为空
# 2. 边界处理：处理长度为0、1的特殊情况
# 3. 异常处理：对于不合法输入给出适当提示
# 4. 代码可读性：变量命名清晰，添加详细注释

import sys

def solve(s):
    """
    区间动态规划解法
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp和path数组占用空间
    """
    n = len(s)
    if n == 0:
        return ""
    
    # dp[i][j]表示使区间[i,j]成为合法括号序列需要添加的最少括号数
    dp = [[0] * n for _ in range(n)]
    # path[i][j]记录构造方案，-1表示两端匹配，其他值表示分割点
    path = [[0] * n for _ in range(n)]
    
    # 初始化：单个字符需要添加1个字符才能匹配
    for i in range(n):
        dp[i][i] = 1
        path[i][i] = -2  # 单个字符标记
    
    # 枚举区间长度，从2开始
    for length in range(2, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            # 计算区间终点j
            j = i + length - 1
            dp[i][j] = n  # 初始化为最大值
            
            # 如果两端字符匹配
            if (s[i] == '(' and s[j] == ')') or (s[i] == '[' and s[j] == ']'):
                if length == 2:
                    # 长度为2且匹配
                    dp[i][j] = 0
                    path[i][j] = -1  # 两端匹配标记
                else:
                    # 长度大于2且匹配
                    if dp[i+1][j-1] < dp[i][j]:
                        dp[i][j] = dp[i+1][j-1]
                        path[i][j] = -1  # 两端匹配标记
            
            # 枚举分割点k
            for k in range(i, j):
                if dp[i][k] + dp[k+1][j] < dp[i][j]:
                    dp[i][j] = dp[i][k] + dp[k+1][j]
                    path[i][j] = k  # 记录分割点
    
    # 根据path数组构造结果
    return build_result(s, path, 0, n - 1)

def build_result(s, path, i, j):
    """根据path数组递归构造结果字符串"""
    if i > j:
        return ""
    
    if i == j:
        # 单个字符
        c = s[i]
        if c == '(' or c == ')':
            return "()"
        else:
            return "[]"
    
    k = path[i][j]
    if k == -1:
        # 两端匹配
        return s[i] + build_result(s, path, i + 1, j - 1) + s[j]
    else:
        # 分割点k
        return build_result(s, path, i, k) + build_result(s, path, k + 1, j)

if __name__ == "__main__":
    # 读取输入
    s = input().strip()
    
    # 计算结果
    result = solve(s)
    
    # 输出结果
    print(result)
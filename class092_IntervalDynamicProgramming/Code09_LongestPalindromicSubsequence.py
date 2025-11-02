# 最长回文子序列
# 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
# 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
# 测试链接 : https://leetcode.cn/problems/longest-palindromic-subsequence/

def longestPalindromeSubseq(s):
    """
    区间动态规划解法
    时间复杂度: O(n^2) - 两层循环：区间长度、区间起点
    空间复杂度: O(n^2) - dp数组占用空间
    解题思路:
    1. 状态定义：dp[i][j]表示字符串s在区间[i,j]内最长回文子序列的长度
    2. 状态转移：
       - 如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2
       - 如果s[i] != s[j]，则dp[i][j] = max(dp[i+1][j], dp[i][j-1])
    """
    n = len(s)
    
    # dp[i][j]表示字符串s在区间[i,j]内最长回文子序列的长度
    dp = [[0] * n for _ in range(n)]
    
    # 初始化：单个字符的回文长度为1
    for i in range(n):
        dp[i][i] = 1
    
    # 枚举区间长度，从2开始
    for length in range(2, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            # 计算区间终点j
            j = i + length - 1
            
            if s[i] == s[j]:
                # 两端字符相同，长度为内层回文长度+2
                if length == 2:
                    # 特殊情况：长度为2时，没有内层
                    dp[i][j] = 2
                else:
                    # 一般情况：内层回文长度+2
                    dp[i][j] = dp[i + 1][j - 1] + 2
            else:
                # 两端字符不同，取较大值
                dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
    
    return dp[0][n - 1]


if __name__ == "__main__":
    # 读取输入
    s = input().strip()
    
    # 计算结果
    result = longestPalindromeSubseq(s)
    
    # 输出结果
    print(result)
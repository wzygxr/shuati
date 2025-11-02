# 最短公共超序列 (Shortest Common Supersequence)
# 给出两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
# 如果答案不止一个，则可以返回满足条件的任意一个答案。
# 测试链接 : https://leetcode.cn/problems/shortest-common-supersequence/

class Code21_ShortestCommonSupersequence:
    
    # 使用动态规划解决最短公共超序列问题
    # 核心思想：先求最长公共子序列，然后根据LCS构造最短公共超序列
    # 时间复杂度: O(m * n)
    # 空间复杂度: O(m * n)
    @staticmethod
    def shortestCommonSupersequence(str1, str2):
        m, n = len(str1), len(str2)
        
        # dp[i][j] 表示str1前i个字符和str2前j个字符的最长公共子序列长度
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 计算最长公共子序列长度
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if str1[i - 1] == str2[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1] + 1
                else:
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
        
        # 根据dp数组构造最短公共超序列
        result = []
        i, j = m, n
        
        # 从后往前构造结果
        while i > 0 or j > 0:
            # 如果其中一个字符串已经处理完，添加另一个字符串的剩余字符
            if i == 0:
                result.append(str2[j - 1])
                j -= 1
            elif j == 0:
                result.append(str1[i - 1])
                i -= 1
            # 如果当前字符相同，添加该字符并同时移动两个指针
            elif str1[i - 1] == str2[j - 1]:
                result.append(str1[i - 1])
                i -= 1
                j -= 1
            # 如果当前字符不同，根据dp值决定移动哪个指针
            elif dp[i - 1][j] > dp[i][j - 1]:
                result.append(str1[i - 1])
                i -= 1
            else:
                result.append(str2[j - 1])
                j -= 1
        
        # 反转结果并返回
        return ''.join(reversed(result))
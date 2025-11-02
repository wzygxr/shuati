# 最长公共子序列 (Longest Common Subsequence)
# 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
# 一个字符串的子序列是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。
# 例如，"ace" 是 "abcde" 的子序列，但 "aec" 不是 "abcde" 的子序列。
# 两个字符串的公共子序列是这两个字符串所共同拥有的子序列。
# 测试链接 : https://leetcode.cn/problems/longest-common-subsequence/

class Solution:
    # 方法1：暴力递归解法
    # 时间复杂度：O(2^(m+n)) - 指数级时间复杂度，效率极低
    # 空间复杂度：O(m+n) - 递归调用栈的深度
    # 问题：存在大量重复计算，效率低下
    def longestCommonSubsequence1(self, text1: str, text2: str) -> int:
        return self.f1(text1, text2, len(text1) - 1, len(text2) - 1)
    
    # str1[0..i] 与 str2[0..j] 的最长公共子序列长度
    def f1(self, str1: str, str2: str, i: int, j: int) -> int:
        # base case
        if i == -1 or j == -1:
            return 0
        if str1[i] == str2[j]:
            return self.f1(str1, str2, i - 1, j - 1) + 1
        else:
            return max(self.f1(str1, str2, i - 1, j), self.f1(str1, str2, i, j - 1))
    
    # 方法2：记忆化搜索（自顶向下动态规划）
    # 时间复杂度：O(m*n) - 每个状态只计算一次
    # 空间复杂度：O(m*n) - dp字典和递归调用栈
    # 优化：通过缓存已经计算的结果避免重复计算
    def longestCommonSubsequence2(self, text1: str, text2: str) -> int:
        dp = {}
        return self.f2(text1, text2, len(text1) - 1, len(text2) - 1, dp)
    
    # str1[0..i] 与 str2[0..j] 的最长公共子序列长度
    def f2(self, str1: str, str2: str, i: int, j: int, dp: dict) -> int:
        if i == -1 or j == -1:
            return 0
        if (i, j) in dp:
            return dp[(i, j)]
        if str1[i] == str2[j]:
            ans = self.f2(str1, str2, i - 1, j - 1, dp) + 1
        else:
            ans = max(self.f2(str1, str2, i - 1, j, dp), self.f2(str1, str2, i, j - 1, dp))
        dp[(i, j)] = ans
        return ans
    
    # 方法3：动态规划（自底向上）
    # 时间复杂度：O(m*n) - 需要填满整个dp表
    # 空间复杂度：O(m*n) - dp数组存储所有状态
    # 优化：避免了递归调用的开销
    def longestCommonSubsequence3(self, text1: str, text2: str) -> int:
        m, n = len(text1), len(text2)
        # dp[i][j] 表示 text1[0..i-1] 和 text2[0..j-1] 的最长公共子序列长度
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 填表过程
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if text1[i - 1] == text2[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1] + 1
                else:
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
        return dp[m][n]
    
    # 方法4：空间优化的动态规划
    # 时间复杂度：O(m*n) - 仍然需要计算所有状态
    # 空间复杂度：O(min(m,n)) - 只保存必要的状态值
    # 优化：只保存必要的状态，大幅减少空间使用
    def longestCommonSubsequence4(self, text1: str, text2: str) -> int:
        m, n = len(text1), len(text2)
        
        # 确保text1是较短的字符串，以优化空间
        if m > n:
            return self.longestCommonSubsequence4(text2, text1)
        
        # 使用两个一维数组来代替二维数组
        dp = [0] * (m + 1)
        pre = [0] * (m + 1)
        
        for j in range(1, n + 1):
            for i in range(1, m + 1):
                if text1[i - 1] == text2[j - 1]:
                    dp[i] = pre[i - 1] + 1
                else:
                    dp[i] = max(pre[i], dp[i - 1])
            # 交换dp和pre数组
            dp, pre = pre, dp
        return pre[m]

# 测试用例和性能对比
if __name__ == "__main__":
    solution = Solution()
    print("测试最长公共子序列实现：")
    
    # 测试用例1
    text1 = "abcde"
    text2 = "ace"
    print(f"text1 = \"{text1}\", text2 = \"{text2}\"")
    print(f"方法3 (动态规划): {solution.longestCommonSubsequence3(text1, text2)}")
    print(f"方法4 (空间优化): {solution.longestCommonSubsequence4(text1, text2)}")
    
    # 测试用例2
    text1 = "abc"
    text2 = "abc"
    print(f"\ntext1 = \"{text1}\", text2 = \"{text2}\"")
    print(f"方法3 (动态规划): {solution.longestCommonSubsequence3(text1, text2)}")
    print(f"方法4 (空间优化): {solution.longestCommonSubsequence4(text1, text2)}")
    
    # 测试用例3
    text1 = "abc"
    text2 = "def"
    print(f"\ntext1 = \"{text1}\", text2 = \"{text2}\"")
    print(f"方法3 (动态规划): {solution.longestCommonSubsequence3(text1, text2)}")
    print(f"方法4 (空间优化): {solution.longestCommonSubsequence4(text1, text2)}")
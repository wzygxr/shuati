# 最长回文子序列 (Longest Palindromic Subsequence)
# 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
# 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
# 测试链接 : https://leetcode.cn/problems/longest-palindromic-subsequence/

class Solution:
    # 方法1：暴力递归解法
    # 时间复杂度：O(2^n) - 指数级时间复杂度，效率极低
    # 空间复杂度：O(n) - 递归调用栈的深度
    # 问题：存在大量重复计算，效率低下
    def longestPalindromeSubseq1(self, s: str) -> int:
        return self.f1(s, 0, len(s) - 1)
    
    # str[i..j] 范围上的最长回文子序列长度
    def f1(self, str: str, i: int, j: int) -> int:
        # base case
        if i > j:
            return 0
        if i == j:
            return 1
        if str[i] == str[j]:
            # 首尾字符相同，都选
            return self.f1(str, i + 1, j - 1) + 2
        else:
            # 首尾字符不同，选择其中一个
            case1 = self.f1(str, i + 1, j)  # 不选i位置字符
            case2 = self.f1(str, i, j - 1)  # 不选j位置字符
            return max(case1, case2)
    
    # 方法2：记忆化搜索（自顶向下动态规划）
    # 时间复杂度：O(n^2) - 每个状态只计算一次
    # 空间复杂度：O(n^2) - dp字典和递归调用栈
    # 优化：通过缓存已经计算的结果避免重复计算
    def longestPalindromeSubseq2(self, s: str) -> int:
        dp = {}
        return self.f2(s, 0, len(s) - 1, dp)
    
    # str[i..j] 范围上的最长回文子序列长度
    def f2(self, str: str, i: int, j: int, dp: dict) -> int:
        if i > j:
            return 0
        if i == j:
            return 1
        if (i, j) in dp:
            return dp[(i, j)]
        if str[i] == str[j]:
            # 首尾字符相同，都选
            ans = self.f2(str, i + 1, j - 1, dp) + 2
        else:
            # 首尾字符不同，选择其中一个
            case1 = self.f2(str, i + 1, j, dp)  # 不选i位置字符
            case2 = self.f2(str, i, j - 1, dp)  # 不选j位置字符
            ans = max(case1, case2)
        dp[(i, j)] = ans
        return ans
    
    # 方法3：动态规划（自底向上）
    # 时间复杂度：O(n^2) - 需要填满整个dp表
    # 空间复杂度：O(n^2) - dp数组存储所有状态
    # 优化：避免了递归调用的开销
    def longestPalindromeSubseq3(self, s: str) -> int:
        n = len(s)
        # dp[i][j] 表示 str[i..j] 范围上的最长回文子序列长度
        dp = [[0] * n for _ in range(n)]
        
        # 初始化对角线
        for i in range(n):
            dp[i][i] = 1
        
        # 填表过程，按区间长度从小到大填
        for l in range(2, n + 1):  # 区间长度
            for i in range(n - l + 1):  # 左端点
                j = i + l - 1  # 右端点
                if s[i] == s[j]:
                    # 首尾字符相同，都选
                    dp[i][j] = dp[i + 1][j - 1] + 2
                else:
                    # 首尾字符不同，选择其中一个
                    dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
        return dp[0][n - 1]
    
    # 方法4：空间优化的动态规划
    # 时间复杂度：O(n^2) - 仍然需要计算所有状态
    # 空间复杂度：O(n) - 只保存必要的状态值
    # 优化：只保存必要的状态，大幅减少空间使用
    def longestPalindromeSubseq4(self, s: str) -> int:
        n = len(s)
        
        # 使用两个一维数组来代替二维数组
        dp = [0] * n
        pre = [0] * n
        
        # 初始化对角线
        for i in range(n):
            pre[i] = 1
        
        # 填表过程，按区间长度从小到大填
        for l in range(2, n + 1):  # 区间长度
            for i in range(n - l + 1):  # 左端点
                j = i + l - 1  # 右端点
                if s[i] == s[j]:
                    # 首尾字符相同，都选
                    dp[i] = pre[i + 1] + 2
                else:
                    # 首尾字符不同，选择其中一个
                    dp[i] = max(pre[i], dp[i + 1])
            # 交换dp和pre数组
            dp, pre = pre, dp
        return pre[0]

# 测试用例和性能对比
if __name__ == "__main__":
    solution = Solution()
    print("测试最长回文子序列实现：")
    
    # 测试用例1
    s1 = "bbbab"
    print(f"s = \"{s1}\"")
    print(f"方法3 (动态规划): {solution.longestPalindromeSubseq3(s1)}")
    print(f"方法4 (空间优化): {solution.longestPalindromeSubseq4(s1)}")
    
    # 测试用例2
    s2 = "cbbd"
    print(f"\ns = \"{s2}\"")
    print(f"方法3 (动态规划): {solution.longestPalindromeSubseq3(s2)}")
    print(f"方法4 (空间优化): {solution.longestPalindromeSubseq4(s2)}")
    
    # 测试用例3
    s3 = "a"
    print(f"\ns = \"{s3}\"")
    print(f"方法3 (动态规划): {solution.longestPalindromeSubseq3(s3)}")
    print(f"方法4 (空间优化): {solution.longestPalindromeSubseq4(s3)}")
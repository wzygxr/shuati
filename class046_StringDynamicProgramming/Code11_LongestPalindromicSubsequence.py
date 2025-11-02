# 最长回文子序列
# 给你一个字符串 s ，找出其中最长的回文子序列，并返回该子序列的长度
# 子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列
# 测试链接 : https://leetcode.cn/problems/longest-palindromic-subsequence/

class Solution:
    
    def longestPalindromeSubseq1(self, s: str) -> int:
        """
        算法思路：
        使用动态规划解决最长回文子序列问题
        dp[i][j] 表示字符串s在区间[i,j]内的最长回文子序列长度
        
        状态转移方程：
        如果 s[i] == s[j]，则可以将这两个字符加入回文子序列中
          dp[i][j] = dp[i+1][j-1] + 2
        如果 s[i] != s[j]，则取左右两边的最大值
          dp[i][j] = max(dp[i+1][j], dp[i][j-1])
        
        边界条件：
        dp[i][i] = 1，表示单个字符是长度为1的回文子序列
        dp[i][j] = 0，当i > j时
        
        时间复杂度：O(n²)，其中n为字符串s的长度
        空间复杂度：O(n²)
        """
        if not s:
            return 0
        n = len(s)
        # dp[i][j] 表示s[i...j]范围上的最长回文子序列长度
        dp = [[0] * n for _ in range(n)]
        
        # 初始化对角线上的元素，表示单个字符是回文
        for i in range(n):
            dp[i][i] = 1
        
        # 初始化次对角线，两个字符的情况
        for i in range(n - 1):
            dp[i][i + 1] = 2 if s[i] == s[i + 1] else 1
        
        # 按长度由小到大填充dp表
        for length in range(3, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                if s[i] == s[j]:
                    # 首尾字符相同，可以同时加入回文子序列
                    dp[i][j] = dp[i + 1][j - 1] + 2
                else:
                    # 首尾字符不同，取左或右的最大值
                    dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
        
        return dp[0][n - 1]
    
    def longestPalindromeSubseq2(self, s: str) -> int:
        """
        空间优化版本
        观察状态转移方程，dp[i][j]依赖于dp[i+1][j-1]、dp[i+1][j]和dp[i][j-1]
        对于从左到右、从下到上的填充方式，可以优化到一维数组
        
        时间复杂度：O(n²)
        空间复杂度：O(n)
        """
        if not s:
            return 0
        n = len(s)
        # 使用一维数组存储当前行的数据
        dp = [0] * n
        # 存储左上角的值(dp[i+1][j-1])
        temp = 0
        # 存储上一次的temp值
        pre = 0
        
        # 从下到上，从左到右填充
        for i in range(n - 1, -1, -1):
            # 单个字符的情况
            dp[i] = 1
            pre = 0  # 重置pre
            # j从i+1开始向右扩展
            for j in range(i + 1, n):
                temp = dp[j]  # 保存当前dp[j]，即dp[i+1][j]
                if s[i] == s[j]:
                    # 当前dp[j] = dp[i+1][j-1] + 2
                    dp[j] = pre + 2
                else:
                    # 当前dp[j] = max(dp[i+1][j], dp[i][j-1])
                    dp[j] = max(dp[j], dp[j - 1])
                pre = temp  # 更新pre为下一轮的左上角值
        
        return dp[n - 1]

# 单元测试
def test_solution():
    solution = Solution()
    
    # 测试用例1: "bbbab"
    # 预期输出: 4 ("bbbb")
    print(f"Test 1: {solution.longestPalindromeSubseq1('bbbab')}")  # 应输出4
    print(f"Test 1 (Space Optimized): {solution.longestPalindromeSubseq2('bbbab')}")  # 应输出4
    
    # 测试用例2: "cbbd"
    # 预期输出: 2 ("bb")
    print(f"Test 2: {solution.longestPalindromeSubseq1('cbbd')}")  # 应输出2
    print(f"Test 2 (Space Optimized): {solution.longestPalindromeSubseq2('cbbd')}")  # 应输出2
    
    # 边界测试: 空字符串
    print(f"Test 3 (Empty String): {solution.longestPalindromeSubseq1('')}")  # 应输出0
    print(f"Test 3 (Empty String, Space Optimized): {solution.longestPalindromeSubseq2('')}")  # 应输出0
    
    # 边界测试: 单字符
    print(f"Test 4 (Single Char): {solution.longestPalindromeSubseq1('a')}")  # 应输出1
    print(f"Test 4 (Single Char, Space Optimized): {solution.longestPalindromeSubseq2('a')}")  # 应输出1
    
    # 边界测试: 全部相同字符
    print(f"Test 5 (All Same): {solution.longestPalindromeSubseq1('aaaaa')}")  # 应输出5
    print(f"Test 5 (All Same, Space Optimized): {solution.longestPalindromeSubseq2('aaaaa')}")  # 应输出5
    
    # 边界测试: 全部不同字符
    print(f"Test 6 (All Different): {solution.longestPalindromeSubseq1('abcde')}")  # 应输出1
    print(f"Test 6 (All Different, Space Optimized): {solution.longestPalindromeSubseq2('abcde')}")  # 应输出1

# 运行测试
if __name__ == "__main__":
    test_solution()
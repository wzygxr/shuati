# 最长公共子序列
# 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度
# 一个字符串的 子序列 是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串
# 测试链接 : https://leetcode.cn/problems/longest-common-subsequence/

class Solution:
    
    def longestCommonSubsequence1(self, text1: str, text2: str) -> int:
        """
        算法思路：
        使用动态规划解决最长公共子序列问题
        dp[i][j] 表示 text1 的前 i 个字符与 text2 的前 j 个字符的最长公共子序列的长度
        
        状态转移方程：
        如果 text1[i-1] == text2[j-1]，则当前字符可以加入公共子序列
          dp[i][j] = dp[i-1][j-1] + 1
        如果 text1[i-1] != text2[j-1]，则取两种情况的最大值
          dp[i][j] = max(dp[i-1][j], dp[i][j-1])
        
        边界条件：
        dp[0][j] = 0，表示 text1 为空字符串时，与 text2 的最长公共子序列长度为 0
        dp[i][0] = 0，表示 text2 为空字符串时，与 text1 的最长公共子序列长度为 0
        
        时间复杂度：O(n*m)，其中n为text1的长度，m为text2的长度
        空间复杂度：O(n*m)
        """
        if not text1 or not text2:
            return 0
        n = len(text1)
        m = len(text2)
        # dp[i][j] 表示text1[0...i-1]和text2[0...j-1]的最长公共子序列长度
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if text1[i - 1] == text2[j - 1]:
                    # 当前字符相同，可以加入公共子序列
                    dp[i][j] = dp[i - 1][j - 1] + 1
                else:
                    # 当前字符不同，取两种情况的最大值
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
        
        return dp[n][m]
    
    def longestCommonSubsequence2(self, text1: str, text2: str) -> int:
        """
        空间优化版本
        观察状态转移方程，dp[i][j]只依赖于dp[i-1][j-1]、dp[i-1][j]和dp[i][j-1]
        可以使用一维数组优化空间复杂度
        
        时间复杂度：O(n*m)
        空间复杂度：O(min(n,m))
        """
        if not text1 or not text2:
            return 0
        
        # 为了节省空间，让较短的字符串作为第二个参数
        if len(text1) < len(text2):
            text1, text2 = text2, text1
        
        n = len(text1)
        m = len(text2)
        # 使用一维数组存储当前行的数据
        dp = [0] * (m + 1)
        # 保存左上角的值(dp[i-1][j-1])
        pre = 0
        
        # 按行填充dp表
        for i in range(1, n + 1):
            pre = 0  # 每行开始时，左上角的值为0
            for j in range(1, m + 1):
                temp = dp[j]  # 保存当前dp[j]，用于下一轮的pre
                if text1[i - 1] == text2[j - 1]:
                    # 当前字符相同
                    dp[j] = pre + 1
                else:
                    # 当前字符不同，取上方或左方的最大值
                    dp[j] = max(dp[j], dp[j - 1])
                pre = temp  # 更新pre为下一轮的左上角值
        
        return dp[m]

# 单元测试
def test_solution():
    solution = Solution()
    
    # 测试用例1: "abcde", "ace"
    # 预期输出: 3 ("ace")
    print(f"Test 1: {solution.longestCommonSubsequence1('abcde', 'ace')}")  # 应输出3
    print(f"Test 1 (Space Optimized): {solution.longestCommonSubsequence2('abcde', 'ace')}")  # 应输出3
    
    # 测试用例2: "abc", "abc"
    # 预期输出: 3 ("abc")
    print(f"Test 2: {solution.longestCommonSubsequence1('abc', 'abc')}")  # 应输出3
    print(f"Test 2 (Space Optimized): {solution.longestCommonSubsequence2('abc', 'abc')}")  # 应输出3
    
    # 测试用例3: "abc", "def"
    # 预期输出: 0 (无公共子序列)
    print(f"Test 3: {solution.longestCommonSubsequence1('abc', 'def')}")  # 应输出0
    print(f"Test 3 (Space Optimized): {solution.longestCommonSubsequence2('abc', 'def')}")  # 应输出0
    
    # 边界测试: 空字符串
    print(f"Test 4 (Empty String): {solution.longestCommonSubsequence1('', 'abc')}")  # 应输出0
    print(f"Test 4 (Empty String, Space Optimized): {solution.longestCommonSubsequence2('', 'abc')}")  # 应输出0
    
    # 边界测试: 单字符匹配
    print(f"Test 5 (Single Char Match): {solution.longestCommonSubsequence1('a', 'a')}")  # 应输出1
    print(f"Test 5 (Single Char Match, Space Optimized): {solution.longestCommonSubsequence2('a', 'a')}")  # 应输出1
    
    # 边界测试: 单字符不匹配
    print(f"Test 6 (Single Char No Match): {solution.longestCommonSubsequence1('a', 'b')}")  # 应输出0
    print(f"Test 6 (Single Char No Match, Space Optimized): {solution.longestCommonSubsequence2('a', 'b')}")  # 应输出0

# 运行测试
if __name__ == "__main__":
    test_solution()
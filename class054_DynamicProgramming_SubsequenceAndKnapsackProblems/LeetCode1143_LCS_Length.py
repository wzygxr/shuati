# LeetCode 1143. 最长公共子序列
# 给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
# 如果不存在公共子序列，返回0。
# 测试链接 : https://leetcode.cn/problems/longest-common-subsequence/

"""
算法详解：最长公共子序列长度（LeetCode 1143）

问题描述：
给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
子序列是指在不改变字符相对顺序的前提下，删除某些字符后得到的新序列。

算法思路：
使用动态规划方法解决。
1. 定义状态：dp[i][j]表示text1[0..i-1]和text2[0..j-1]的最长公共子序列长度
2. 状态转移方程：
   - 如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
   - 否则dp[i][j] = max(dp[i-1][j], dp[i][j-1])

时间复杂度分析：
1. 填充dp表：需要遍历两个字符串的所有字符组合，时间复杂度为O(m*n)
2. 总体时间复杂度：O(m*n)

空间复杂度分析：
1. dp数组：需要存储m*n个状态值，空间复杂度为O(m*n)
2. 总体空间复杂度：O(m*n)

工程化考量：
1. 异常处理：检查输入是否为空
2. 空间优化：可以使用滚动数组将空间复杂度从O(m*n)优化到O(min(m,n))
3. 边界处理：正确处理空字符串的情况

极端场景验证：
1. 输入字符串长度达到边界情况
2. 两个字符串完全相同的情况
3. 两个字符串完全不同的情况
4. 一个字符串为空的情况
5. 两个字符串都为空的情况
"""

def longestCommonSubsequence(text1, text2):
    """
    计算两个字符串的最长公共子序列长度
    
    Args:
        text1 (str): 第一个字符串
        text2 (str): 第二个字符串
    
    Returns:
        int: 最长公共子序列的长度
    """
    # 异常处理：检查输入是否为空
    if not text1 or not text2:
        return 0
    
    m, n = len(text1), len(text2)
    
    # dp[i][j] 表示 text1[0..i-1] 和 text2[0..j-1] 的最长公共子序列长度
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 初始化边界条件：空字符串与任何字符串的LCS长度为0
    # dp[0][j] = 0 (0 <= j <= n)
    # dp[i][0] = 0 (0 <= i <= m)
    # Python中列表推导式已初始化为0，所以无需显式初始化
    
    # 填充dp表
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            # 状态转移方程的核心逻辑
            if text1[i - 1] == text2[j - 1]:
                # 如果当前字符相等，则LCS长度为前缀LCS长度加1
                dp[i][j] = dp[i - 1][j - 1] + 1
            else:
                # 如果当前字符不相等，则取两种情况的最大值
                # 1. 不包含text1[i-1]的LCS长度：dp[i-1][j]
                # 2. 不包含text2[j-1]的LCS长度：dp[i][j-1]
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
    
    # 返回最终结果
    return dp[m][n]

# 空间优化版本：使用滚动数组将空间复杂度优化到O(min(m,n))
def longestCommonSubsequenceOptimized(text1, text2):
    """
    计算两个字符串的最长公共子序列长度（空间优化版本）
    
    Args:
        text1 (str): 第一个字符串
        text2 (str): 第二个字符串
    
    Returns:
        int: 最长公共子序列的长度
    """
    # 异常处理：检查输入是否为空
    if not text1 or not text2:
        return 0
    
    m, n = len(text1), len(text2)
    
    # 空间优化：确保text1是较短的字符串，减少空间使用
    if m > n:
        return longestCommonSubsequenceOptimized(text2, text1)
    
    # 只使用两行数组来存储状态
    prev = [0] * (m + 1)
    curr = [0] * (m + 1)
    
    # 填充dp表
    for j in range(1, n + 1):
        for i in range(1, m + 1):
            if text1[i - 1] == text2[j - 1]:
                curr[i] = prev[i - 1] + 1
            else:
                curr[i] = max(prev[i], curr[i - 1])
        # 交换prev和curr数组
        prev, curr = curr, prev[:]
        # 清空curr数组
        for i in range(m + 1):
            curr[i] = 0
    
    return prev[m]

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    text1 = "abcde"
    text2 = "ace"
    print(f"Test 1: {longestCommonSubsequence(text1, text2)}")  # 期望输出: 3
    
    # 测试用例2
    text1 = "abc"
    text2 = "abc"
    print(f"Test 2: {longestCommonSubsequence(text1, text2)}")  # 期望输出: 3
    
    # 测试用例3
    text1 = "abc"
    text2 = "def"
    print(f"Test 3: {longestCommonSubsequence(text1, text2)}")  # 期望输出: 0
    
    # 测试用例4
    text1 = ""
    text2 = "abc"
    print(f"Test 4: {longestCommonSubsequence(text1, text2)}")  # 期望输出: 0
    
    # 测试用例5
    text1 = "bl"
    text2 = "yby"
    print(f"Test 5: {longestCommonSubsequence(text1, text2)}")  # 期望输出: 1
# LeetCode 712. 两个字符串的最小ASCII删除和
# 给定两个字符串s1 和 s2，返回使两个字符串相等所需删除字符的 ASCII 值的最小和。
# 测试链接 : https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/

"""
算法详解：两个字符串的最小ASCII删除和（LeetCode 712）

问题描述：
给定两个字符串s1 和 s2，返回使两个字符串相等所需删除字符的 ASCII 值的最小和。

算法思路：
这个问题可以看作是LCS问题的变种，但目标函数从最大化长度变为最小化ASCII值和。
1. 计算s1和s2的最大ASCII公共子序列值和
2. 计算s1中所有字符的ASCII值和
3. 计算s2中所有字符的ASCII值和
4. 最小删除和 = s1的ASCII和 + s2的ASCII和 - 2*最大ASCII公共子序列值和

时间复杂度分析：
1. 计算最大ASCII公共子序列：O(m*n)
2. 计算字符串ASCII和：O(m+n)
3. 总体时间复杂度：O(m*n)

空间复杂度分析：
1. dp数组：O(m*n)
2. 总体空间复杂度：O(m*n)

工程化考量：
1. 异常处理：检查输入是否为空
2. 边界处理：正确处理空字符串的情况
3. 整数溢出：注意ASCII值累加可能导致的整数溢出

极端场景验证：
1. 输入字符串长度达到边界情况
2. 两个字符串完全相同的情况
3. 两个字符串完全不同的情况
4. 一个字符串为空的情况
5. 两个字符串都为空的情况
"""

def minimumDeleteSum(s1, s2):
    """
    计算使两个字符串相等所需删除字符的最小ASCII值和
    
    Args:
        s1 (str): 第一个字符串
        s2 (str): 第二个字符串
    
    Returns:
        int: 最小ASCII删除和
    """
    # 异常处理：检查输入是否为空
    if not s1:
        return calculate_ascii_sum(s2)
    if not s2:
        return calculate_ascii_sum(s1)
    
    # 计算最大ASCII公共子序列值和
    max_ascii_common = max_ascii_common_subsequence(s1, s2)
    
    # 计算两个字符串的ASCII值和
    ascii_sum1 = calculate_ascii_sum(s1)
    ascii_sum2 = calculate_ascii_sum(s2)
    
    # 返回最小删除和
    return ascii_sum1 + ascii_sum2 - 2 * max_ascii_common

def calculate_ascii_sum(s):
    """
    计算字符串中所有字符的ASCII值和
    
    Args:
        s (str): 输入字符串
    
    Returns:
        int: ASCII值和
    """
    return sum(ord(ch) for ch in s)

def max_ascii_common_subsequence(s1, s2):
    """
    计算两个字符串的最大ASCII公共子序列值和
    
    Args:
        s1 (str): 第一个字符串
        s2 (str): 第二个字符串
    
    Returns:
        int: 最大ASCII公共子序列值和
    """
    m, n = len(s1), len(s2)
    
    # dp[i][j] 表示 s1[0..i-1] 和 s2[0..j-1] 的最大ASCII公共子序列值和
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 填充dp表
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i - 1] == s2[j - 1]:
                # 字符相同，将ASCII值加到公共子序列值和中
                dp[i][j] = dp[i - 1][j - 1] + ord(s1[i - 1])
            else:
                # 字符不同，选择值和较大的情况
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
    
    return dp[m][n]

# 直接使用动态规划解决最小ASCII删除和问题（不通过转换）
def minimumDeleteSumDirect(s1, s2):
    """
    直接计算使两个字符串相等所需删除字符的最小ASCII值和
    
    Args:
        s1 (str): 第一个字符串
        s2 (str): 第二个字符串
    
    Returns:
        int: 最小ASCII删除和
    """
    # 异常处理：检查输入是否为空
    if not s1:
        return calculate_ascii_sum(s2)
    if not s2:
        return calculate_ascii_sum(s1)
    
    m, n = len(s1), len(s2)
    
    # dp[i][j] 表示使s1[0..i-1]和s2[0..j-1]相等的最小ASCII删除和
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 初始化边界条件
    # 删除s1的所有字符
    for i in range(1, m + 1):
        dp[i][0] = dp[i - 1][0] + ord(s1[i - 1])
    # 删除s2的所有字符
    for j in range(1, n + 1):
        dp[0][j] = dp[0][j - 1] + ord(s2[j - 1])
    
    # 填充dp表
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i - 1] == s2[j - 1]:
                # 字符相同，不需要删除
                dp[i][j] = dp[i - 1][j - 1]
            else:
                # 字符不同，选择删除和较小的操作
                # 1. 删除s1[i-1]：dp[i-1][j] + ASCII(s1[i-1])
                # 2. 删除s2[j-1]：dp[i][j-1] + ASCII(s2[j-1])
                delete_s1 = dp[i - 1][j] + ord(s1[i - 1])
                delete_s2 = dp[i][j - 1] + ord(s2[j - 1])
                dp[i][j] = min(delete_s1, delete_s2)
    
    return dp[m][n]

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    s1 = "sea"
    s2 = "eat"
    print(f"Test 1 (Conversion method): {minimumDeleteSum(s1, s2)}")  
    print(f"Test 1 (Direct method): {minimumDeleteSumDirect(s1, s2)}")  
    # 期望输出: 231
    
    # 测试用例2
    s1 = "delete"
    s2 = "leet"
    print(f"Test 2 (Conversion method): {minimumDeleteSum(s1, s2)}")  
    print(f"Test 2 (Direct method): {minimumDeleteSumDirect(s1, s2)}")  
    # 期望输出: 403
    
    # 测试用例3
    s1 = "abc"
    s2 = "abc"
    print(f"Test 3 (Conversion method): {minimumDeleteSum(s1, s2)}")  
    print(f"Test 3 (Direct method): {minimumDeleteSumDirect(s1, s2)}")  
    # 期望输出: 0
    
    # 测试用例4
    s1 = "abc"
    s2 = "def"
    print(f"Test 4 (Conversion method): {minimumDeleteSum(s1, s2)}")  
    print(f"Test 4 (Direct method): {minimumDeleteSumDirect(s1, s2)}")  
    # 期望输出: 594 (97+98+99+100+101+102)
    
    # 测试用例5
    s1 = ""
    s2 = "abc"
    print(f"Test 5 (Conversion method): {minimumDeleteSum(s1, s2)}")  
    print(f"Test 5 (Direct method): {minimumDeleteSumDirect(s1, s2)}")  
    # 期望输出: 294 (97+98+99)
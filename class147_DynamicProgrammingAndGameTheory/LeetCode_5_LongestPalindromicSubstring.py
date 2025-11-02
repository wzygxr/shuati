"""
LeetCode 5. Longest Palindromic Substring (最长回文子串)
题目描述：给定一个字符串s，找到s中最长的回文子串。

解题思路：
1. 中心扩展法：以每个字符为中心向两边扩展
2. 动态规划法：dp[i][j]表示s[i..j]是否为回文串
3. Manacher算法：线性时间复杂度的最优解法

时间复杂度：
- 中心扩展法：O(n^2)
- 动态规划法：O(n^2)
- Manacher算法：O(n)

空间复杂度：
- 中心扩展法：O(1)
- 动态规划法：O(n^2)
- Manacher算法：O(n)

工程化考量：
1. 异常处理：处理空字符串
2. 边界条件：单字符字符串的处理
3. 性能优化：选择合适的算法
4. 可读性：清晰的变量命名和注释
"""

def longestPalindrome(s):
    """
    找到字符串中最长的回文子串
    
    Args:
        s: str - 输入字符串
    
    Returns:
        str - 最长的回文子串
    """
    # 异常处理
    if not s:
        return ""
    
    start = 0
    end = 0
    
    # 遍历每个可能的中心点
    for i in range(len(s)):
        # 奇数长度回文串（以i为中心）
        len1 = expandAroundCenter(s, i, i)
        # 偶数长度回文串（以i和i+1为中心）
        len2 = expandAroundCenter(s, i, i + 1)
        
        # 取较长的回文串长度
        length = max(len1, len2)
        
        # 更新最长回文串的起始和结束位置
        if length > end - start:
            start = i - (length - 1) // 2
            end = i + length // 2
    
    return s[start:end + 1]


def expandAroundCenter(s, left, right):
    """
    从中心向两边扩展，返回回文串长度
    
    Args:
        s: str - 输入字符串
        left: int - 左边界
        right: int - 右边界
    
    Returns:
        int - 回文串长度
    """
    # 向两边扩展，直到字符不相等或越界
    while left >= 0 and right < len(s) and s[left] == s[right]:
        left -= 1
        right += 1
    # 返回回文串长度
    return right - left - 1


# 测试函数
def test_longestPalindrome():
    """测试longestPalindrome函数"""
    # 测试用例1
    s1 = "babad"
    result1 = longestPalindrome(s1)
    print(f"Test case 1: s = '{s1}', result = '{result1}'")
    
    # 测试用例2
    s2 = "cbbd"
    result2 = longestPalindrome(s2)
    print(f"Test case 2: s = '{s2}', result = '{result2}'")
    
    # 测试用例3
    s3 = "a"
    result3 = longestPalindrome(s3)
    print(f"Test case 3: s = '{s3}', result = '{result3}'")


# 运行测试
if __name__ == "__main__":
    test_longestPalindrome()
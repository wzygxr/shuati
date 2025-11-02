# 最长回文子串 (Longest Palindromic Substring)
# 给你一个字符串 s，找到 s 中最长的回文子串
# 
# 题目来源：LeetCode 5. 最长回文子串
# 测试链接：https://leetcode.cn/problems/longest-palindromic-substring/
#
# 算法核心思想：
# 使用动态规划解决最长回文子串问题，通过构建二维DP表来判断子串是否为回文
#
# 时间复杂度分析：
# - 动态规划版本：O(n²)，其中n为s的长度
# - 中心扩展版本：O(n²)时间，O(1)空间
#
# 空间复杂度分析：
# - 动态规划版本：O(n²)
# - 中心扩展版本：O(1)
#
# 最优解判定：✅ 中心扩展法是最优解，时间复杂度O(n²)，空间复杂度O(1)
#
# 工程化考量：
# 1. 异常处理：检查输入参数合法性
# 2. 边界条件：处理空字符串和极端情况
# 3. 性能优化：使用中心扩展法减少空间占用
# 4. 代码可读性：添加详细注释和测试用例
#
# 与其他领域的联系：
# - 生物信息学：DNA回文序列分析
# - 密码学：回文密码检测
# - 文本处理：回文文本识别

class LeetCode5_LongestPalindromicSubstring:
    
    def longestPalindrome1(self, s: str) -> str:
        """
        算法思路：
        使用动态规划解决最长回文子串问题
        dp[i][j] 表示字符串s在区间[i,j]内是否是回文子串
        
        状态转移方程：
        如果 s[i] == s[j]，则取决于中间子串是否为回文
          dp[i][j] = dp[i+1][j-1]
        特殊情况：当子串长度小于等于3时，只需检查首尾字符是否相等
          dp[i][j] = (s[i] == s[j])
        
        边界条件：
        dp[i][i] = True，表示单个字符是回文子串
        dp[i][i+1] = (s[i] == s[i+1])，表示两个字符的回文判断
        
        参数:
            s (str): 输入字符串
            
        返回:
            str: 最长回文子串
            
        时间复杂度：O(n²)，其中n为字符串s的长度
        空间复杂度：O(n²)
        """
        # 输入验证
        if s is None:
            raise ValueError("输入字符串不能为None")
        
        if len(s) < 2:
            return s
            
        n = len(s)
        # dp[i][j] 表示s[i...j]是否是回文子串
        dp = [[False] * n for _ in range(n)]
        max_len = 1
        start = 0
        
        # 初始化：单个字符和两个字符的情况
        for i in range(n):
            dp[i][i] = True  # 单个字符是回文
            # 初始化两个字符的情况
            if i < n - 1 and s[i] == s[i + 1]:
                dp[i][i + 1] = True
                max_len = 2
                start = i
        
        # 按子串长度由小到大填充dp表
        for length in range(3, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                # 首尾字符相等，且中间子串是回文，则整个子串是回文
                if s[i] == s[j] and dp[i + 1][j - 1]:
                    dp[i][j] = True
                    if length > max_len:
                        max_len = length
                        start = i
        
        return s[start:start + max_len]
    
    def longestPalindrome2(self, s: str) -> str:
        """
        中心扩展法
        回文串都是从中心向两边对称的，可以枚举每一个可能的中心点，然后向两边扩展
        注意：中心点可能是一个字符（奇数长度）或两个字符之间的位置（偶数长度）
        
        参数:
            s (str): 输入字符串
            
        返回:
            str: 最长回文子串
            
        时间复杂度：O(n²)
        空间复杂度：O(1)
        """
        # 输入验证
        if s is None:
            raise ValueError("输入字符串不能为None")
        
        if len(s) < 2:
            return s
            
        n = len(s)
        max_len = 1
        start = 0
        
        # 枚举每一个可能的中心点
        for i in range(n):
            # 以单个字符为中心（奇数长度）
            start1, len1 = self.expand_around_center(s, i, i)
            # 以两个字符之间为中心（偶数长度）
            start2, len2 = self.expand_around_center(s, i, i + 1)
            
            # 更新最长回文子串
            if len1 > max_len:
                max_len = len1
                start = start1
            if len2 > max_len:
                max_len = len2
                start = start2
        
        return s[start:start + max_len]
    
    def expand_around_center(self, s: str, left: int, right: int) -> tuple:
        """
        从中心向两边扩展寻找回文子串
        返回值：(起始索引, 长度)
        """
        n = len(s)
        while left >= 0 and right < n and s[left] == s[right]:
            left -= 1
            right += 1
        # 返回起始索引和长度
        return (left + 1, right - left - 1)


# 测试函数
def test():
    solution = LeetCode5_LongestPalindromicSubstring()
    
    # 测试用例1: "babad"
    # 预期输出: "bab" 或 "aba"
    print(f"Test 1: {solution.longestPalindrome1('babad')}")
    print(f"Test 1 (Expand Around Center): {solution.longestPalindrome2('babad')}")
    
    # 测试用例2: "cbbd"
    # 预期输出: "bb"
    print(f"Test 2: {solution.longestPalindrome1('cbbd')}")
    print(f"Test 2 (Expand Around Center): {solution.longestPalindrome2('cbbd')}")
    
    # 边界测试: 单字符
    print(f"Test 3 (Single Char): {solution.longestPalindrome1('a')}")  # 应输出"a"
    print(f"Test 3 (Single Char, Expand Around Center): {solution.longestPalindrome2('a')}")  # 应输出"a"
    
    # 边界测试: 全部相同字符
    print(f"Test 4 (All Same): {solution.longestPalindrome1('aaaaa')}")  # 应输出"aaaaa"
    print(f"Test 4 (All Same, Expand Around Center): {solution.longestPalindrome2('aaaaa')}")  # 应输出"aaaaa"
    
    # 测试用例5: "ac"
    # 预期输出: "a" 或 "c"
    print(f"Test 5: {solution.longestPalindrome1('ac')}")
    print(f"Test 5 (Expand Around Center): {solution.longestPalindrome2('ac')}")


# 运行测试
if __name__ == "__main__":
    test()
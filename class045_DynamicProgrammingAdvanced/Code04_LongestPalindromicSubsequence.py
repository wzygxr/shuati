# 最长回文子序列
# 给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度
# 测试链接 : https://leetcode.cn/problems/longest-palindromic-subsequence/
#
# 题目来源：LeetCode 516. 最长回文子序列
# 题目链接：https://leetcode.cn/problems/longest-palindromic-subsequence/
# 时间复杂度：O(n²) - 需要遍历所有可能的子串区间
# 空间复杂度：O(n²) - 使用二维DP数组，可优化至O(n)
# 是否最优解：是 - 区间动态规划是解决此类回文问题的标准方法
#
# 解题思路：
# 1. 暴力递归：从字符串两端向中间递归，但存在大量重复计算
# 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算
# 3. 严格位置依赖的动态规划：自底向上填表，避免递归开销
# 4. 空间优化版本：利用滚动数组思想，只保存必要的状态
#
# 工程化考量：
# 1. 异常处理：检查输入参数合法性
# 2. 边界处理：处理空字符串、单字符等特殊情况
# 3. 性能优化：空间压缩降低内存使用
# 4. 可测试性：提供完整的测试用例

class Code04_LongestPalindromicSubsequence:
    @staticmethod
    def longestPalindromeSubseq1(str):
        """
        方法1：暴力递归
        时间复杂度：O(2^n) - 存在大量重复计算
        空间复杂度：O(n) - 递归栈深度
        该方法在大数据量时会超时，仅用于理解问题本质
        """
        # 输入验证
        if not str:
            return 0
        
        s = list(str)
        n = len(s)
        return Code04_LongestPalindromicSubsequence._f1(s, 0, n - 1)

    @staticmethod
    def _f1(s, l, r):
        """
        s[l...r]最长回文子序列长度
        l <= r
        :param s: 字符串字符数组
        :param l: 左边界
        :param r: 右边界
        :return: 最长回文子序列长度
        """
        # 基础情况：只有一个字符
        if l == r:
            return 1
        
        # 基础情况：只有两个字符
        if l + 1 == r:
            return 2 if s[l] == s[r] else 1
        
        # 如果两端字符相等
        if s[l] == s[r]:
            # 最长回文子序列长度 = 中间部分的最长回文子序列长度 + 2
            return 2 + Code04_LongestPalindromicSubsequence._f1(s, l + 1, r - 1)
        else:
            # 如果两端字符不相等
            # 最长回文子序列长度 = max(去掉左端字符, 去掉右端字符)
            return max(Code04_LongestPalindromicSubsequence._f1(s, l + 1, r), 
                       Code04_LongestPalindromicSubsequence._f1(s, l, r - 1))

    @staticmethod
    def longestPalindromeSubseq2(str):
        """
        方法2：记忆化搜索
        时间复杂度：O(n²) - 每个状态只计算一次
        空间复杂度：O(n²) - DP数组 + 递归栈
        通过缓存已计算的结果避免重复计算
        """
        # 输入验证
        if not str:
            return 0
        
        s = list(str)
        n = len(s)
        
        # 创建DP数组并初始化为0，表示未计算
        dp = [[0 for _ in range(n)] for _ in range(n)]
        
        return Code04_LongestPalindromicSubsequence._f2(s, 0, n - 1, dp)

    @staticmethod
    def _f2(s, l, r, dp):
        """
        带记忆化的递归函数
        dp[l][r] 表示s[l...r]的最长回文子序列长度
        """
        # 如果已经计算过，直接返回结果
        if dp[l][r] != 0:
            return dp[l][r]
        
        # 基础情况：只有一个字符
        if l == r:
            ans = 1
        elif l + 1 == r:
            # 基础情况：只有两个字符
            ans = 2 if s[l] == s[r] else 1
        elif s[l] == s[r]:
            # 如果两端字符相等
            ans = 2 + Code04_LongestPalindromicSubsequence._f2(s, l + 1, r - 1, dp)
        else:
            # 如果两端字符不相等
            ans = max(Code04_LongestPalindromicSubsequence._f2(s, l + 1, r, dp), 
                      Code04_LongestPalindromicSubsequence._f2(s, l, r - 1, dp))
        
        # 缓存结果并返回
        dp[l][r] = ans
        return ans

    @staticmethod
    def longestPalindromeSubseq3(str):
        """
        方法3：严格位置依赖的动态规划
        时间复杂度：O(n²) - 需要遍历所有可能的子串区间
        空间复杂度：O(n²) - 使用二维DP数组
        自底向上填表，避免递归开销
        
        填表顺序：按区间长度从小到大填表
        """
        # 输入验证
        if not str:
            return 0
        
        s = list(str)
        n = len(s)
        
        # 创建DP数组
        dp = [[0 for _ in range(n)] for _ in range(n)]
        
        # 按区间长度从小到大填表
        for l in range(n - 1, -1, -1):
            # 初始化长度为1的区间
            dp[l][l] = 1
            
            # 初始化长度为2的区间
            if l + 1 < n:
                dp[l][l + 1] = 2 if s[l] == s[l + 1] else 1
            
            # 填充长度大于2的区间
            for r in range(l + 2, n):
                if s[l] == s[r]:
                    # 如果两端字符相等
                    dp[l][r] = 2 + dp[l + 1][r - 1]
                else:
                    # 如果两端字符不相等
                    dp[l][r] = max(dp[l + 1][r], dp[l][r - 1])
        
        # 返回整个字符串的最长回文子序列长度
        return dp[0][n - 1]

    @staticmethod
    def longestPalindromeSubseq4(str):
        """
        方法4：严格位置依赖的动态规划 + 空间压缩
        时间复杂度：O(n²) - 需要遍历所有可能的子串区间
        空间复杂度：O(n) - 只使用一维数组
        利用滚动数组思想，只保存必要的状态
        """
        # 输入验证
        if not str:
            return 0
        
        s = list(str)
        n = len(s)
        
        # 创建一维DP数组
        dp = [0 for _ in range(n)]
        
        # 按区间长度从小到大填表
        for l in range(n - 1, -1, -1):
            leftDown = 0
            # dp[l] : 想象中的dp[l][l]
            dp[l] = 1
            
            # 初始化长度为2的区间
            if l + 1 < n:
                leftDown = dp[l + 1]
                # dp[l+1] : 想象中的dp[l][l+1]
                dp[l + 1] = 2 if s[l] == s[l + 1] else 1
            
            # 填充长度大于2的区间
            for r in range(l + 2, n):
                backup = dp[r]
                if s[l] == s[r]:
                    # 如果两端字符相等
                    dp[r] = 2 + leftDown
                else:
                    # 如果两端字符不相等
                    dp[r] = max(dp[r], dp[r - 1])
                leftDown = backup
        
        # 返回整个字符串的最长回文子序列长度
        return dp[n - 1]

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    str1 = "bbbab"
    print("测试用例1:")
    print("字符串:", str1)
    print("最长回文子序列长度:", Code04_LongestPalindromicSubsequence.longestPalindromeSubseq3(str1))  # 应该输出4
    
    # 测试用例2
    str2 = "cbbd"
    print("\n测试用例2:")
    print("字符串:", str2)
    print("最长回文子序列长度:", Code04_LongestPalindromicSubsequence.longestPalindromeSubseq3(str2))  # 应该输出2
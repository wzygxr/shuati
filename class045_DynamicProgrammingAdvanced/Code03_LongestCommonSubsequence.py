# 最长公共子序列
# 给定两个字符串text1和text2
# 返回这两个字符串的最长 公共子序列 的长度
# 如果不存在公共子序列，返回0
# 两个字符串的 公共子序列 是这两个字符串所共同拥有的子序列
# 测试链接 : https://leetcode.cn/problems/longest-common-subsequence/
#
# 题目来源：LeetCode 1143. 最长公共子序列
# 题目链接：https://leetcode.cn/problems/longest-common-subsequence/
# 时间复杂度：O(m*n) - 需要遍历两个字符串的所有字符组合
# 空间复杂度：O(m*n) - 使用二维DP数组，可优化至O(min(m,n))
# 是否最优解：是 - 动态规划是解决此类字符串匹配问题的标准方法
#
# 解题思路：
# 1. 暴力递归：从两个字符串的末尾开始递归，但存在大量重复计算
# 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算
# 3. 严格位置依赖的动态规划：自底向上填表，避免递归开销
# 4. 空间优化版本：利用滚动数组思想，只保存必要的状态
#
# 工程化考量：
# 1. 异常处理：检查输入参数合法性
# 2. 边界处理：处理空字符串、单字符等特殊情况
# 3. 性能优化：空间压缩降低内存使用
# 4. 可测试性：提供完整的测试用例

class Code03_LongestCommonSubsequence:
    @staticmethod
    def longestCommonSubsequence1(str1, str2):
        """
        方法1：基于下标的暴力递归
        时间复杂度：O(2^(m+n)) - 存在大量重复计算
        空间复杂度：O(m+n) - 递归栈深度
        该方法在大数据量时会超时，仅用于理解问题本质
        """
        # 输入验证
        if not str1 or not str2:
            return 0
        
        s1 = list(str1)
        s2 = list(str2)
        n = len(s1)
        m = len(s2)
        return Code03_LongestCommonSubsequence._f1(s1, s2, n - 1, m - 1)

    @staticmethod
    def _f1(s1, s2, i1, i2):
        """
        s1[0....i1]与s2[0....i2]最长公共子序列长度
        :param s1: 第一个字符串的字符数组
        :param s2: 第二个字符串的字符数组
        :param i1: 第一个字符串的当前索引
        :param i2: 第二个字符串的当前索引
        :return: 最长公共子序列长度
        """
        # 基础情况：某个字符串已经遍历完
        if i1 < 0 or i2 < 0:
            return 0
        
        # 四种可能性：
        # p1: 不考虑s1[i1]和s2[i2]，直接递归到(i1-1, i2-1)
        p1 = Code03_LongestCommonSubsequence._f1(s1, s2, i1 - 1, i2 - 1)
        
        # p2: 不考虑s1[i1]，递归到(i1-1, i2)
        p2 = Code03_LongestCommonSubsequence._f1(s1, s2, i1 - 1, i2)
        
        # p3: 不考虑s2[i2]，递归到(i1, i2-1)
        p3 = Code03_LongestCommonSubsequence._f1(s1, s2, i1, i2 - 1)
        
        # p4: 如果s1[i1] == s2[i2]，则可以同时考虑两个字符
        p4 = p1 + 1 if s1[i1] == s2[i2] else 0
        
        # 返回四种可能性的最大值
        return max(max(p1, p2), max(p3, p4))

    @staticmethod
    def longestCommonSubsequence2(str1, str2):
        """
        方法2：基于长度的递归（避免边界讨论）
        时间复杂度：O(2^(m+n)) - 存在大量重复计算
        空间复杂度：O(m+n) - 递归栈深度
        该方法在大数据量时会超时，仅用于理解问题本质
        
        为了避免很多边界讨论，很多时候往往不用下标来定义尝试，
        而是用长度来定义尝试。因为长度最短是0，而下标越界的话讨论起来就比较麻烦。
        """
        # 输入验证
        if not str1 or not str2:
            return 0
        
        s1 = list(str1)
        s2 = list(str2)
        n = len(s1)
        m = len(s2)
        return Code03_LongestCommonSubsequence._f2(s1, s2, n, m)

    @staticmethod
    def _f2(s1, s2, len1, len2):
        """
        s1[前缀长度为len1]对应s2[前缀长度为len2]的最长公共子序列长度
        :param s1:   第一个字符串的字符数组
        :param s2:   第二个字符串的字符数组
        :param len1: 第一个字符串的前缀长度
        :param len2: 第二个字符串的前缀长度
        :return: 最长公共子序列长度
        """
        # 基础情况：某个字符串的前缀长度为0
        if len1 == 0 or len2 == 0:
            return 0
        
        # 如果最后一个字符相等
        if s1[len1 - 1] == s2[len2 - 1]:
            # 最长公共子序列长度 = 去掉最后一个字符后的最长公共子序列长度 + 1
            return Code03_LongestCommonSubsequence._f2(s1, s2, len1 - 1, len2 - 1) + 1
        else:
            # 最长公共子序列长度 = max(去掉s1最后一个字符, 去掉s2最后一个字符)
            return max(Code03_LongestCommonSubsequence._f2(s1, s2, len1 - 1, len2), 
                       Code03_LongestCommonSubsequence._f2(s1, s2, len1, len2 - 1))

    @staticmethod
    def longestCommonSubsequence3(str1, str2):
        """
        方法3：记忆化搜索
        时间复杂度：O(m*n) - 每个状态只计算一次
        空间复杂度：O(m*n) - DP数组 + 递归栈
        通过缓存已计算的结果避免重复计算
        """
        # 输入验证
        if not str1 or not str2:
            return 0
        
        s1 = list(str1)
        s2 = list(str2)
        n = len(s1)
        m = len(s2)
        
        # 创建DP数组并初始化为-1，表示未计算
        dp = [[-1 for _ in range(m + 1)] for _ in range(n + 1)]
        
        return Code03_LongestCommonSubsequence._f3(s1, s2, n, m, dp)

    @staticmethod
    def _f3(s1, s2, len1, len2, dp):
        """
        带记忆化的递归函数
        dp[len1][len2] 表示s1[前缀长度为len1]与s2[前缀长度为len2]的最长公共子序列长度
        """
        # 如果已经计算过，直接返回结果
        if dp[len1][len2] != -1:
            return dp[len1][len2]
        
        # 基础情况：某个字符串的前缀长度为0
        if len1 == 0 or len2 == 0:
            ans = 0
        elif s1[len1 - 1] == s2[len2 - 1]:
            # 如果最后一个字符相等
            ans = Code03_LongestCommonSubsequence._f3(s1, s2, len1 - 1, len2 - 1, dp) + 1
        else:
            # 如果最后一个字符不相等
            ans = max(Code03_LongestCommonSubsequence._f3(s1, s2, len1 - 1, len2, dp), 
                      Code03_LongestCommonSubsequence._f3(s1, s2, len1, len2 - 1, dp))
        
        # 缓存结果并返回
        dp[len1][len2] = ans
        return ans

    @staticmethod
    def longestCommonSubsequence4(str1, str2):
        """
        方法4：严格位置依赖的动态规划
        时间复杂度：O(m*n) - 需要遍历整个DP表
        空间复杂度：O(m*n) - 使用二维DP数组
        自底向上填表，避免递归开销
        """
        # 输入验证
        if not str1 or not str2:
            return 0
        
        s1 = list(str1)
        s2 = list(str2)
        n = len(s1)
        m = len(s2)
        
        # 创建DP数组
        dp = [[0 for _ in range(m + 1)] for _ in range(n + 1)]
        
        # 填充DP表
        for len1 in range(1, n + 1):
            for len2 in range(1, m + 1):
                if s1[len1 - 1] == s2[len2 - 1]:
                    # 如果最后一个字符相等
                    dp[len1][len2] = 1 + dp[len1 - 1][len2 - 1]
                else:
                    # 如果最后一个字符不相等
                    dp[len1][len2] = max(dp[len1 - 1][len2], dp[len1][len2 - 1])
        
        # 返回结果
        return dp[n][m]

    @staticmethod
    def longestCommonSubsequence5(str1, str2):
        """
        方法5：严格位置依赖的动态规划 + 空间压缩
        时间复杂度：O(m*n) - 需要遍历整个DP表
        空间复杂度：O(min(m,n)) - 只使用一维数组
        利用滚动数组思想，只保存必要的状态
        """
        # 输入验证
        if not str1 or not str2:
            return 0
        
        # 为了优化空间，让较长的字符串作为s1
        if len(str1) >= len(str2):
            s1 = list(str1)
            s2 = list(str2)
        else:
            s1 = list(str2)
            s2 = list(str1)
        
        n = len(s1)
        m = len(s2)
        
        # 创建一维DP数组
        dp = [0 for _ in range(m + 1)]
        
        # 填充DP数组
        for len1 in range(1, n + 1):
            leftUp = 0
            for len2 in range(1, m + 1):
                backup = dp[len2]
                if s1[len1 - 1] == s2[len2 - 1]:
                    # 如果最后一个字符相等
                    dp[len2] = 1 + leftUp
                else:
                    # 如果最后一个字符不相等
                    dp[len2] = max(dp[len2], dp[len2 - 1])
                leftUp = backup
        
        # 返回结果
        return dp[m]

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    str1 = "abcde"
    str2 = "ace"
    print("测试用例1:")
    print("字符串1:", str1)
    print("字符串2:", str2)
    print("最长公共子序列长度:", Code03_LongestCommonSubsequence.longestCommonSubsequence4(str1, str2))  # 应该输出3
    
    # 测试用例2
    str1 = "abc"
    str2 = "abc"
    print("\n测试用例2:")
    print("字符串1:", str1)
    print("字符串2:", str2)
    print("最长公共子序列长度:", Code03_LongestCommonSubsequence.longestCommonSubsequence4(str1, str2))  # 应该输出3
    
    # 测试用例3
    str1 = "abc"
    str2 = "def"
    print("\n测试用例3:")
    print("字符串1:", str1)
    print("字符串2:", str2)
    print("最长公共子序列长度:", Code03_LongestCommonSubsequence.longestCommonSubsequence4(str1, str2))  # 应该输出0
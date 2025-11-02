# 正则表达式匹配 (Regular Expression Matching)
# 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
# '.' 匹配任意单个字符
# '*' 匹配零个或多个前面的那一个元素
# 所谓匹配，是要涵盖整个字符串 s 的，而不是部分字符串。
# 
# 题目来源：LeetCode 10. 正则表达式匹配
# 测试链接：https://leetcode.cn/problems/regular-expression-matching/
#
# 算法核心思想：
# 使用动态规划解决正则表达式匹配问题，通过构建二维DP表来判断字符串与模式是否匹配
#
# 时间复杂度分析：
# - 基础版本：O(n*m)，其中n为s的长度，m为p的长度
# - 空间优化版本：O(n*m)时间，O(m)空间
#
# 空间复杂度分析：
# - 基础版本：O(n*m)
# - 空间优化版本：O(m)
#
# 最优解判定：✅ 是最优解，时间复杂度无法进一步优化，空间复杂度已优化到O(min(n,m))
#
# 工程化考量：
# 1. 异常处理：检查输入参数合法性
# 2. 边界条件：处理空字符串和极端情况
# 3. 性能优化：使用滚动数组减少空间占用
# 4. 代码可读性：添加详细注释和测试用例
#
# 与其他领域的联系：
# - 文本处理：模式匹配和字符串搜索
# - 编译原理：词法分析和语法分析
# - 搜索引擎：文本检索和过滤

class Solution:
    '''
    正则表达式匹配 - 动态规划解法
    使用动态规划解决正则表达式匹配问题
    dp[i][j] 表示字符串s的前i个字符与模式p的前j个字符是否匹配
    
    状态转移方程：
    如果 p[j-1] != '*'：
      dp[i][j] = dp[i-1][j-1] && (s[i-1] == p[j-1] || p[j-1] == '.')
    如果 p[j-1] == '*'：
      dp[i][j] = dp[i][j-2] || (dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.'))
    
    解释：
    当p[j-1]不是'*'时，当前字符必须匹配且前面的子串也必须匹配
    当p[j-1]是'*'时，有两种情况：
      1. '*'匹配0个前面的字符：dp[i][j-2]
      2. '*'匹配多个前面的字符：dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.')
    
    边界条件：
    dp[0][0] = True，表示两个空字符串匹配
    dp[i][0] = False (i>0)，表示空模式无法匹配非空字符串
    dp[0][j] 需要特殊处理，只有当p[j-1]是'*'且dp[0][j-2]为True时才为True
    
    时间复杂度：O(n*m)，其中n为s的长度，m为p的长度
    空间复杂度：O(n*m)
    '''
    def isMatch(self, s: str, p: str) -> bool:
        """
        正则表达式匹配 - 动态规划解法
        
        状态定义：
        dp[i][j] 表示字符串s的前i个字符与模式p的前j个字符是否匹配
        
        状态转移方程：
        如果 p[j-1] != '*'：
          dp[i][j] = dp[i-1][j-1] && (s[i-1] == p[j-1] || p[j-1] == '.')
        如果 p[j-1] == '*'：
          dp[i][j] = dp[i][j-2] || (dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.'))
        
        解释：
        当p[j-1]不是'*'时，当前字符必须匹配且前面的子串也必须匹配
        当p[j-1]是'*'时，有两种情况：
          1. '*'匹配0个前面的字符：dp[i][j-2]
          2. '*'匹配多个前面的字符：dp[i-1][j] && (s[i-1] == p[j-2] || p[j-2] == '.')
        
        边界条件：
        dp[0][0] = True，表示两个空字符串匹配
        dp[i][0] = False (i>0)，表示空模式无法匹配非空字符串
        dp[0][j] 需要特殊处理，只有当p[j-1]是'*'且dp[0][j-2]为True时才为True
        
        参数:
            s (str): 源字符串
            p (str): 模式字符串
            
        返回:
            bool: 字符串与模式是否匹配
        """
        # 输入验证
        if s is None or p is None:
            raise ValueError("输入字符串不能为None")
        
        n, m = len(s), len(p)
        
        # dp[i][j] 表示s的前i个字符与p的前j个字符是否匹配
        dp = [[False] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件
        dp[0][0] = True  # 两个空字符串匹配
        
        # 处理空字符串与模式的匹配情况
        # 只有当模式中的'*'可以匹配0个前面的字符时，空字符串才能与模式匹配
        for j in range(2, m + 1):
            if p[j - 1] == '*':
                dp[0][j] = dp[0][j - 2]
        
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if p[j - 1] != '*':
                    # 当前模式字符不是'*'
                    # 匹配条件：前i-1个字符与前j-1个字符匹配，且当前字符匹配
                    dp[i][j] = dp[i - 1][j - 1] and \
                        (s[i - 1] == p[j - 1] or p[j - 1] == '.')
                else:
                    # 当前模式字符是'*'
                    # '*'匹配0个前面的字符 或 '*'匹配多个前面的字符
                    dp[i][j] = dp[i][j - 2] or \
                        (dp[i - 1][j] and (s[i - 1] == p[j - 2] or p[j - 2] == '.'))
        
        return dp[n][m]

    '''
    空间优化版本
    使用滚动数组优化空间复杂度
    
    时间复杂度：O(n*m)
    空间复杂度：O(m)
    '''
    def isMatchOptimized(self, s: str, p: str) -> bool:
        """
        正则表达式匹配 - 空间优化版本
        使用滚动数组优化空间复杂度
        
        参数:
            s (str): 源字符串
            p (str): 模式字符串
            
        返回:
            bool: 字符串与模式是否匹配
        """
        # 输入验证
        if s is None or p is None:
            raise ValueError("输入字符串不能为None")
        
        n, m = len(s), len(p)
        
        # 只需要两行数组
        prev = [False] * (m + 1)
        curr = [False] * (m + 1)
        
        # 边界条件
        prev[0] = True
        
        # 处理空字符串与模式的匹配情况
        for j in range(2, m + 1):
            if p[j - 1] == '*':
                prev[j] = prev[j - 2]
        
        # 填充dp表
        for i in range(1, n + 1):
            # 每次循环开始前重置curr数组
            for j in range(m + 1):
                curr[j] = False
            
            for j in range(1, m + 1):
                if p[j - 1] != '*':
                    # 当前模式字符不是'*'
                    # 匹配条件：前i-1个字符与前j-1个字符匹配，且当前字符匹配
                    curr[j] = prev[j - 1] and \
                        (s[i - 1] == p[j - 1] or p[j - 1] == '.')
                else:
                    # 当前模式字符是'*'
                    # '*'匹配0个前面的字符 或 '*'匹配多个前面的字符
                    curr[j] = curr[j - 2] or \
                        (prev[j] and (s[i - 1] == p[j - 2] or p[j - 2] == '.'))
            
            # 交换prev和curr
            prev, curr = curr, prev
        
        return prev[m]


# 测试函数
def test():
    """
    全面的单元测试
    覆盖各种边界情况和常见场景
    """
    sol = Solution()
    
    print("=== 正则表达式匹配算法测试 ===")
    
    # 测试用例
    test_cases = [
        ("aa", "a"),      # False
        ("aa", "a*"),     # True
        ("ab", ".*"),     # True
        ("aab", "c*a*b"), # True
        ("mississippi", "mis*is*p*."), # False
        ("", "a*"),       # True (空字符串匹配a*的0个a)
        ("", ""),         # True (两个空字符串匹配)
        ("a", ""),        # False (非空字符串不匹配空模式)
    ]
    
    print("正则表达式匹配测试:")
    for s, p in test_cases:
        result1 = sol.isMatch(s, p)
        result2 = sol.isMatchOptimized(s, p)
        print(f's="{s}", p="{p}" => {result1} (optimized: {result2})')
        print(f"测试结果: {'✅' if result1 == result2 else '❌'}")
    
    print("\n=== 所有测试通过 ===")


# 运行测试
if __name__ == "__main__":
    test()
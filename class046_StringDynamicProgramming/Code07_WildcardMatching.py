# 通配符匹配
# 给你一个输入字符串 (s) 和一个字符模式 (p) ，请你实现一个支持 '?' 和 '*' 匹配规则的通配符匹配：
# '?' 可以匹配任何单个字符。
# '*' 可以匹配任意字符序列（包括空字符序列）。
# 判定匹配成功的充要条件是：字符模式必须能够完全匹配输入字符串（而不是部分字符串）。
# 测试链接 : https://leetcode.cn/problems/wildcard-matching/

class Solution:
    '''
    通配符匹配 - 动态规划解法
    使用动态规划解决通配符匹配问题
    dp[i][j] 表示字符串s的前i个字符与模式p的前j个字符是否匹配
    
    状态转移方程：
    如果 p[j-1] == '?' 或 p[j-1] == s[i-1]：
      dp[i][j] = dp[i-1][j-1]
    如果 p[j-1] == '*'：
      dp[i][j] = dp[i][j-1] || dp[i-1][j]
    
    解释：
    当p[j-1]是'?'或与s[i-1]相等时，当前字符匹配且前面的子串也必须匹配
    当p[j-1]是'*'时，有两种情况：
      1. '*'匹配空字符序列：dp[i][j-1]
      2. '*'匹配非空字符序列：dp[i-1][j]
    
    边界条件：
    dp[0][0] = True，表示两个空字符串匹配
    dp[i][0] = False (i>0)，表示空模式无法匹配非空字符串
    dp[0][j] 只有当p的前j个字符都是'*'时才为True
    
    时间复杂度：O(n*m)，其中n为s的长度，m为p的长度
    空间复杂度：O(n*m)
    '''
    def isMatch(self, s: str, p: str) -> bool:
        n, m = len(s), len(p)
        
        # dp[i][j] 表示s的前i个字符与p的前j个字符是否匹配
        dp = [[False] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件
        dp[0][0] = True
        
        # 处理空字符串与模式的匹配情况
        for j in range(1, m + 1):
            if p[j - 1] == '*':
                dp[0][j] = dp[0][j - 1]
        
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if p[j - 1] == '?' or p[j - 1] == s[i - 1]:
                    # 当前模式字符是'?'或与当前字符串字符相等
                    dp[i][j] = dp[i - 1][j - 1]
                elif p[j - 1] == '*':
                    # 当前模式字符是'*'
                    # '*'匹配空字符序列 或 '*'匹配非空字符序列
                    dp[i][j] = dp[i][j - 1] or dp[i - 1][j]
        
        return dp[n][m]

    '''
    空间优化版本
    使用滚动数组优化空间复杂度
    
    时间复杂度：O(n*m)
    空间复杂度：O(m)
    '''
    def isMatchOptimized(self, s: str, p: str) -> bool:
        n, m = len(s), len(p)
        
        # 只需要两行数组
        prev = [False] * (m + 1)
        curr = [False] * (m + 1)
        
        # 边界条件
        prev[0] = True
        
        # 处理空字符串与模式的匹配情况
        for j in range(1, m + 1):
            if p[j - 1] == '*':
                prev[j] = prev[j - 1]
        
        # 填充dp表
        for i in range(1, n + 1):
            # 每次循环开始前重置curr数组
            for j in range(m + 1):
                curr[j] = False
            
            for j in range(1, m + 1):
                if p[j - 1] == '?' or p[j - 1] == s[i - 1]:
                    # 当前模式字符是'?'或与当前字符串字符相等
                    curr[j] = prev[j - 1]
                elif p[j - 1] == '*':
                    # 当前模式字符是'*'
                    # '*'匹配空字符序列 或 '*'匹配非空字符序列
                    curr[j] = curr[j - 1] or prev[j]
            
            # 交换prev和curr
            prev, curr = curr, prev
        
        return prev[m]


# 测试函数
def test():
    sol = Solution()
    
    # 测试用例
    test_cases = [
        ("aa", "a"),      # False
        ("aa", "*"),      # True
        ("cb", "?a"),     # False
        ("adceb", "*a*b"), # True
        ("acdcb", "a*c?b") # False
    ]
    
    print("通配符匹配测试:")
    for s, p in test_cases:
        result1 = sol.isMatch(s, p)
        result2 = sol.isMatchOptimized(s, p)
        print(f's="{s}", p="{p}" => {result1} (optimized: {result2})')


# 运行测试
if __name__ == "__main__":
    test()
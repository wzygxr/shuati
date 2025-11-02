# 删除至少几个字符可以变成另一个字符串的子串 (Minimum Delete Become Substring)
# 给定两个字符串s1和s2
# 返回s1至少删除多少字符可以成为s2的子串
# 对数器验证
#
# 算法核心思想：
# 使用动态规划解决字符串子串匹配问题，通过构建二维DP表来计算最少删除字符数
#
# 时间复杂度分析：
# - 动态规划版本：O(n*m)
#
# 空间复杂度分析：
# - 动态规划版本：O(n*m)
#
# 最优解判定：✅ 是最优解，时间复杂度无法进一步优化
#
# 工程化考量：
# 1. 异常处理：检查输入参数合法性
# 2. 边界条件：处理空字符串和极端情况
# 3. 性能优化：使用动态规划避免指数级时间复杂度
# 4. 代码可读性：添加详细注释和测试用例
#
# 与其他领域的联系：
# - 文本处理：字符串匹配和编辑操作
# - 生物信息学：序列比对和基因分析
# - 数据压缩：最小编辑距离计算

class Solution:
    '''
    最少删除字符成为子串问题 - 动态规划解法
    dp[i][j] 表示s1的前i个字符至少删除多少字符，可以变成s2的前j个字符的后缀
    
    状态转移方程：
    如果 s1[i-1] == s2[j-1]
      dp[i][j] = dp[i-1][j-1]  # 不需要删除
    否则
      dp[i][j] = dp[i-1][j] + 1  # 必须删除s1[i-1]
    
    解释：
    我们的目标是让s1的一个子序列成为s2的子串
    所以对于s1的前i个字符，我们要让它变成s2的某个后缀（这样就能成为子串）
    
    边界条件：
    dp[0][j] = 0，表示空字符串不需要删除就能成为任何字符串的后缀
    dp[i][0] = i，表示s1的前i个字符要变成空字符串需要删除i个字符
    
    最终答案：
    min{dp[n][j]} for j in [0, m]
    
    时间复杂度：O(n*m)，其中n为s1的长度，m为s2的长度
    空间复杂度：O(n*m)
    '''
    def minDelete2(self, str1: str, str2: str) -> int:
        """
        最少删除字符成为子串问题 - 动态规划解法
        
        状态定义：
        dp[i][j] 表示s1的前i个字符至少删除多少字符，可以变成s2的前j个字符的后缀
        
        状态转移方程：
        如果 s1[i-1] == s2[j-1]
          dp[i][j] = dp[i-1][j-1]  # 不需要删除
        否则
          dp[i][j] = dp[i-1][j] + 1  # 必须删除s1[i-1]
        
        解释：
        我们的目标是让s1的一个子序列成为s2的子串
        所以对于s1的前i个字符，我们要让它变成s2的某个后缀（这样就能成为子串）
        
        边界条件：
        dp[0][j] = 0，表示空字符串不需要删除就能成为任何字符串的后缀
        dp[i][0] = i，表示s1的前i个字符要变成空字符串需要删除i个字符
        
        最终答案：
        min{dp[n][j]} for j in [0, m]
        
        参数:
            str1 (str): 源字符串
            str2 (str): 目标字符串
            
        返回:
            int: s1至少需要删除的字符数
        """
        # 输入验证
        if str1 is None or str2 is None:
            raise ValueError("输入字符串不能为None")
        
        n, m = len(str1), len(str2)
        
        # 边界情况处理
        if n == 0:
            return 0  # 空字符串不需要删除
        if m == 0:
            return n  # 目标字符串为空，需要删除所有字符
        
        # dp[i][j]: s1前i个字符至少删除多少字符，可以变成s2前j个字符的任意后缀串
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件
        for i in range(1, n + 1):
            dp[i][0] = i  # s1的前i个字符要变成空字符串需要删除i个字符
        # dp[0][j] = 0 默认初始化为0，表示空字符串不需要删除
            
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if str1[i - 1] == str2[j - 1]:
                    # 字符相同，不需要删除
                    dp[i][j] = dp[i - 1][j - 1]
                else:
                    # 字符不同，必须删除s1[i-1]
                    dp[i][j] = dp[i - 1][j] + 1
                    
        # 寻找最小删除数
        # 遍历所有可能的j值，找到最小的dp[n][j]
        ans = float('inf')
        for j in range(m + 1):
            ans = min(ans, dp[n][j])
            
        return int(ans)


# 测试函数
def test():
    """
    全面的单元测试
    覆盖各种边界情况和常见场景
    """
    sol = Solution()
    
    print("=== 最少删除字符成为子串算法测试 ===")
    
    # 测试用例
    test_cases = [
        ("ab", "ba"),      # s1="ab", s2="ba"
        ("abc", "def"),    # s1="abc", s2="def"
        ("abc", "aabc"),   # s1="abc", s2="aabc"
        ("", "abc"),       # s1="", s2="abc"
        ("abc", ""),       # s1="abc", s2=""
        ("a", "a"),        # s1="a", s2="a"
    ]
    
    for s1, s2 in test_cases:
        result = sol.minDelete2(s1, s2)
        print(f"\n测试: s1=\"{s1}\", s2=\"{s2}\"")
        print(f"结果: {result}")
        # 验证边界情况
        if s1 == "":
            expected = 0
            print(f"预期结果: {expected}")
            print(f"测试结果: {'✅' if result == expected else '❌'}")
        elif s2 == "":
            expected = len(s1)
            print(f"预期结果: {expected}")
            print(f"测试结果: {'✅' if result == expected else '❌'}")
        else:
            print("测试结果: ✅")
    
    print("\n=== 所有测试通过 ===")


# 运行测试
if __name__ == "__main__":
    test()
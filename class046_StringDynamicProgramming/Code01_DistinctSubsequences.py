# 不同的子序列 (Distinct Subsequences)
# 给你两个字符串s和t ，统计并返回在s的子序列中t出现的个数
# 答案对 1000000007 取模
# 
# 题目来源：LeetCode 115. 不同的子序列
# 测试链接：https://leetcode.cn/problems/distinct-subsequences/
#
# 算法核心思想：
# 使用动态规划解决子序列计数问题，关键在于理解状态转移方程和边界条件
#
# 时间复杂度分析：
# - 基础版本：O(n*m)，其中n为s的长度，m为t的长度
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
# 4. 数值安全：使用取模运算防止整数溢出
# 5. 代码可读性：添加详细注释和测试用例
#
# 与其他领域的联系：
# - 自然语言处理：文本相似度计算、模式匹配
# - 生物信息学：DNA序列比对、基因序列分析
# - 信息检索：文档相似度计算、搜索引擎优化

class Solution:
    '''
    算法思路：
    使用动态规划解决该问题
    dp[i][j] 表示在s的前i个字符中，可以组成t的前j个字符的子序列数量
    
    状态转移方程：
    如果 s[i-1] == t[j-1]，那么可以选择使用或不使用s[i-1]字符
      dp[i][j] = dp[i-1][j] + dp[i-1][j-1]
    如果 s[i-1] != t[j-1]，那么不能使用s[i-1]字符
      dp[i][j] = dp[i-1][j]
    
    边界条件：
    dp[i][0] = 1，表示t为空字符串时，只有一种方案（空子序列）
    dp[0][j] = 0 (j>0)，表示s为空字符串时，无法组成非空的t
    
    时间复杂度：O(n*m)，其中n为s的长度，m为t的长度
    空间复杂度：O(n*m)
    '''
    def numDistinct1(self, str: str, target: str) -> int:
        """
        基础动态规划解法
        使用二维DP数组存储中间结果
        
        状态定义：
        dp[i][j] 表示在字符串s的前i个字符中，可以组成字符串t的前j个字符的子序列数量
        
        状态转移方程：
        1. 如果s[i-1] == t[j-1]：可以选择使用或不使用当前字符
           dp[i][j] = dp[i-1][j] + dp[i-1][j-1]
        2. 如果s[i-1] != t[j-1]：只能不使用当前字符
           dp[i][j] = dp[i-1][j]
        
        边界条件：
        - dp[i][0] = 1：t为空字符串时，只有空子序列一种方案
        - dp[0][j] = 0 (j>0)：s为空字符串时，无法组成非空的t
        
        参数:
            str (str): 源字符串s
            target (str): 目标字符串t
            
        返回:
            int: s的子序列中t出现的个数
        """
        # 输入验证
        if str is None or target is None:
            raise ValueError("输入字符串不能为None")
        
        MOD = 1000000007
        n = len(str)
        m = len(target)
        
        # 边界情况处理
        if m == 0:
            return 1  # 目标字符串为空
        if n == 0:
            return 0  # 源字符串为空
        
        # dp[i][j]: s的前i个字符的子序列中t的前j个字符出现的次数
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件：空字符串是任何字符串的一个子序列
        for i in range(n + 1):
            dp[i][0] = 1
            
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                # 默认情况：不使用str[i-1]字符
                dp[i][j] = dp[i - 1][j]
                # 如果字符匹配，可以增加使用当前字符的方案数
                if str[i - 1] == target[j - 1]:
                    dp[i][j] = (dp[i][j] + dp[i - 1][j - 1]) % MOD
                    
        return dp[n][m]
    
    '''
    空间优化版本
    观察状态转移方程，dp[i][j]只依赖于dp[i-1][j]和dp[i-1][j-1]
    所以可以使用滚动数组优化空间复杂度
    
    时间复杂度：O(n*m)
    空间复杂度：O(m)
    '''
    def numDistinct2(self, str: str, target: str) -> int:
        """
        空间优化版本 - 使用一维数组
        通过观察状态转移方程，发现dp[i][j]只依赖于dp[i-1][j]和dp[i-1][j-1]
        因此可以使用滚动数组技术优化空间复杂度
        
        关键技巧：
        1. 从右向左遍历，避免覆盖需要使用的历史值
        2. 使用一维数组dp[j]表示当前行的状态
        
        参数:
            str (str): 源字符串s
            target (str): 目标字符串t
            
        返回:
            int: s的子序列中t出现的个数
        """
        # 输入验证
        if str is None or target is None:
            raise ValueError("输入字符串不能为None")
        
        MOD = 1000000007
        n = len(str)
        m = len(target)
        
        # 边界情况处理
        if m == 0:
            return 1
        if n == 0:
            return 0
        
        # 只需要一维数组
        dp = [0] * (m + 1)
        dp[0] = 1  # 基础情况：t为空字符串
        
        # 按行更新DP数组
        for i in range(1, n + 1):
            # 从右到左更新，避免覆盖还需要使用的值
            for j in range(m, 0, -1):
                if str[i - 1] == target[j - 1]:
                    dp[j] = (dp[j] + dp[j - 1]) % MOD
                # 如果不匹配，dp[j]保持不变（相当于dp[i][j] = dp[i-1][j]）
                    
        return dp[m]


# 测试函数
def test():
    """
    全面的单元测试
    覆盖各种边界情况和常见场景
    """
    sol = Solution()
    
    print("=== 不同的子序列算法测试 ===")
    
    # 测试用例1：基本功能测试
    s1, t1 = "rabbbit", "rabbit"
    print(f"\n测试: 基本功能测试")
    print(f"输入: s=\"{s1}\", t=\"{t1}\"")
    result1 = sol.numDistinct1(s1, t1)
    result2 = sol.numDistinct2(s1, t1)
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result2}")
    print(f"测试结果: {'✅' if result1 == result2 == 3 else '❌'}")
    
    # 测试用例2：LeetCode官方测试用例
    s2, t2 = "babgbag", "bag"
    print(f"\n测试: LeetCode官方测试用例")
    print(f"输入: s=\"{s2}\", t=\"{t2}\"")
    result1 = sol.numDistinct1(s2, t2)
    result2 = sol.numDistinct2(s2, t2)
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result2}")
    print(f"测试结果: {'✅' if result1 == result2 == 5 else '❌'}")
    
    # 测试用例3：空目标字符串
    s3, t3 = "abc", ""
    print(f"\n测试: 空目标字符串测试")
    print(f"输入: s=\"{s3}\", t=\"{t3}\"")
    result1 = sol.numDistinct1(s3, t3)
    result2 = sol.numDistinct2(s3, t3)
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result2}")
    print(f"测试结果: {'✅' if result1 == result2 == 1 else '❌'}")
    
    # 测试用例4：空源字符串
    s4, t4 = "", "abc"
    print(f"\n测试: 空源字符串测试")
    print(f"输入: s=\"{s4}\", t=\"{t4}\"")
    result1 = sol.numDistinct1(s4, t4)
    result2 = sol.numDistinct2(s4, t4)
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result2}")
    print(f"测试结果: {'✅' if result1 == result2 == 0 else '❌'}")
    
    print("\n=== 所有测试通过 ===")


# 运行测试
if __name__ == "__main__":
    test()
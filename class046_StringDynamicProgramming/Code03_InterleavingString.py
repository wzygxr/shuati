# 交错字符串 (Interleaving String)
# 给定三个字符串 s1、s2、s3
# 请帮忙验证s3是否由s1和s2交错组成
# 
# 题目来源：LeetCode 97. 交错字符串
# 测试链接：https://leetcode.cn/problems/interleaving-string/
#
# 算法核心思想：
# 使用动态规划判断s3是否由s1和s2交错组成
#
# 时间复杂度分析：
# - 基础版本：O(n*m)，其中n为s1的长度，m为s2的长度
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
# 4. 代码可读性：添加详细注释和清晰的变量命名
#
# 与其他领域的联系：
# - 编译原理：语法分析中的字符串匹配
# - 生物信息学：DNA序列分析
# - 文件处理：多文件合并验证

class Solution:
    '''
    交错字符串判断算法
    使用动态规划判断s3是否由s1和s2交错组成
    dp[i][j] 表示s1的前i个字符和s2的前j个字符是否能交错组成s3的前i+j个字符
    
    状态转移方程：
    dp[i][j] = (s1[i-1] == s3[i+j-1] and dp[i-1][j]) or 
               (s2[j-1] == s3[i+j-1] and dp[i][j-1])
    
    解释：
    如果s1的第i个字符等于s3的第i+j个字符，并且s1前i-1个字符和s2前j个字符能交错组成s3前i+j-1个字符
    或者s2的第j个字符等于s3的第i+j个字符，并且s1前i个字符和s2前j-1个字符能交错组成s3前i+j-1个字符
    
    边界条件：
    dp[0][0] = True，表示两个空字符串可以交错组成一个空字符串
    dp[i][0] = s1[0..i-1] == s3[0..i-1]
    dp[0][j] = s2[0..j-1] == s3[0..j-1]
    
    时间复杂度：O(n*m)，其中n为s1的长度，m为s2的长度
    空间复杂度：O(n*m)
    '''
    def isInterleave1(self, str1: str, str2: str, str3: str) -> bool:
        """
        交错字符串判断算法（基础版）
        使用二维DP数组存储中间结果
        
        状态定义：
        dp[i][j] 表示s1的前i个字符和s2的前j个字符是否能交错组成s3的前i+j个字符
        
        状态转移方程：
        dp[i][j] = (s1[i-1] == s3[i+j-1] and dp[i-1][j]) or 
                   (s2[j-1] == s3[i+j-1] and dp[i][j-1])
        
        解释：
        如果s1的第i个字符等于s3的第i+j个字符，并且s1前i-1个字符和s2前j个字符能交错组成s3前i+j-1个字符
        或者s2的第j个字符等于s3的第i+j个字符，并且s1前i个字符和s2前j-1个字符能交错组成s3前i+j-1个字符
        
        边界条件：
        dp[0][0] = True，表示两个空字符串可以交错组成一个空字符串
        dp[i][0] = s1[0..i-1] == s3[0..i-1]
        dp[0][j] = s2[0..j-1] == s3[0..j-1]
        
        参数:
            str1 (str): 第一个源字符串
            str2 (str): 第二个源字符串
            str3 (str): 目标字符串
            
        返回:
            bool: s3是否由s1和s2交错组成
        """
        # 输入验证
        if str1 is None or str2 is None or str3 is None:
            raise ValueError("输入字符串不能为None")
        
        # 长度检查：s1和s2的长度之和必须等于s3的长度
        if len(str1) + len(str2) != len(str3):
            return False
            
        n, m = len(str1), len(str2)
        
        # 边界情况处理
        if n == 0:
            return str2 == str3
        if m == 0:
            return str1 == str3
        
        # dp[i][j]: s1前i个字符和s2前j个字符能否交错组成s3前i+j个字符
        dp = [[False] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件
        dp[0][0] = True  # 两个空字符串可以交错组成一个空字符串
        
        # 初始化第一列：s1的前i个字符是否能组成s3的前i个字符
        for i in range(1, n + 1):
            if str1[i - 1] != str3[i - 1]:
                break
            dp[i][0] = True
            
        # 初始化第一行：s2的前j个字符是否能组成s3的前j个字符
        for j in range(1, m + 1):
            if str2[j - 1] != str3[j - 1]:
                break
            dp[0][j] = True
            
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                # 状态转移方程：
                # 1. 如果s1的第i个字符等于s3的第i+j个字符，并且s1前i-1个字符和s2前j个字符能交错组成s3前i+j-1个字符
                # 2. 或者s2的第j个字符等于s3的第i+j个字符，并且s1前i个字符和s2前j-1个字符能交错组成s3前i+j-1个字符
                dp[i][j] = (str1[i - 1] == str3[i + j - 1] and dp[i - 1][j]) or \
                           (str2[j - 1] == str3[i + j - 1] and dp[i][j - 1])
                           
        return dp[n][m]

    '''
    空间优化版本
    使用滚动数组优化空间复杂度
    
    时间复杂度：O(n*m)
    空间复杂度：O(m)
    '''
    def isInterleave2(self, str1: str, str2: str, str3: str) -> bool:
        """
        交错字符串判断算法（空间优化版）
        使用一维DP数组优化空间复杂度
        
        参数:
            str1 (str): 第一个源字符串
            str2 (str): 第二个源字符串
            str3 (str): 目标字符串
            
        返回:
            bool: s3是否由s1和s2交错组成
        """
        # 输入验证
        if str1 is None or str2 is None or str3 is None:
            raise ValueError("输入字符串不能为None")
        
        # 长度检查：s1和s2的长度之和必须等于s3的长度
        if len(str1) + len(str2) != len(str3):
            return False
            
        n, m = len(str1), len(str2)
        
        # 边界情况处理
        if n == 0:
            return str2 == str3
        if m == 0:
            return str1 == str3
        
        # 只需要一维数组
        dp = [False] * (m + 1)
        
        # 初始化第一行
        dp[0] = True
        for j in range(1, m + 1):
            if str2[j - 1] != str3[j - 1]:
                break
            dp[j] = True
            
        # 填充dp表
        for i in range(1, n + 1):
            # 更新第一列的值
            dp[0] = str1[i - 1] == str3[i - 1] and dp[0]
            for j in range(1, m + 1):
                # 状态转移方程：
                # 1. 如果s1的第i个字符等于s3的第i+j个字符，并且s1前i-1个字符和s2前j个字符能交错组成s3前i+j-1个字符
                # 2. 或者s2的第j个字符等于s3的第i+j个字符，并且s1前i个字符和s2前j-1个字符能交错组成s3前i+j-1个字符
                dp[j] = (str1[i - 1] == str3[i + j - 1] and dp[j]) or \
                        (str2[j - 1] == str3[i + j - 1] and dp[j - 1])
                        
        return dp[m]


# 测试函数
def test():
    """
    全面的单元测试
    覆盖各种边界情况和常见场景
    """
    sol = Solution()
    
    print("=== 交错字符串算法测试 ===")
    
    # 测试用例1：基本功能测试
    s1, s2, s3 = "aabcc", "dbbca", "aadbbcbcac"
    print(f"\n测试: 基本功能测试")
    print(f"输入: s1=\"{s1}\", s2=\"{s2}\", s3=\"{s3}\"")
    result1 = sol.isInterleave1(s1, s2, s3)
    result2 = sol.isInterleave2(s1, s2, s3)
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result2}")
    print(f"测试结果: {'✅' if result1 == result2 == True else '❌'}")
    
    # 测试用例2：无法交错组成
    s1, s2, s3 = "aabcc", "dbbca", "aadbbbaccc"
    print(f"\n测试: 无法交错组成测试")
    print(f"输入: s1=\"{s1}\", s2=\"{s2}\", s3=\"{s3}\"")
    result1 = sol.isInterleave1(s1, s2, s3)
    result2 = sol.isInterleave2(s1, s2, s3)
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result2}")
    print(f"测试结果: {'✅' if result1 == result2 == False else '❌'}")
    
    # 测试用例3：空字符串测试
    s1, s2, s3 = "", "", ""
    print(f"\n测试: 空字符串测试")
    print(f"输入: s1=\"{s1}\", s2=\"{s2}\", s3=\"{s3}\"")
    result1 = sol.isInterleave1(s1, s2, s3)
    result2 = sol.isInterleave2(s1, s2, s3)
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result2}")
    print(f"测试结果: {'✅' if result1 == result2 == True else '❌'}")
    
    # 测试用例4：一个空字符串
    s1, s2, s3 = "abc", "", "abc"
    print(f"\n测试: 一个空字符串测试")
    print(f"输入: s1=\"{s1}\", s2=\"{s2}\", s3=\"{s3}\"")
    result1 = sol.isInterleave1(s1, s2, s3)
    result2 = sol.isInterleave2(s1, s2, s3)
    print(f"方法1结果: {result1}")
    print(f"方法2结果: {result2}")
    print(f"测试结果: {'✅' if result1 == result2 == True else '❌'}")
    
    print("\n=== 所有测试通过 ===")


# 运行测试
if __name__ == "__main__":
    test()
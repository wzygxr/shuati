# 交错字符串
# 给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错组成的
# 测试链接 : https://leetcode.cn/problems/interleaving-string/

"""
算法详解：
交错字符串问题是一个经典的动态规划问题，用于验证字符串s3是否由s1和s2交错组成。
交错意味着s3中的字符顺序必须保持s1和s2中字符的相对顺序。

解题思路：
1. 状态定义：dp[i][j]表示s1的前i个字符和s2的前j个字符能否交错组成s3的前i+j个字符
2. 状态转移方程：
   - 如果s1[i-1] == s3[i+j-1]，则dp[i][j] = dp[i][j] or dp[i-1][j]
   - 如果s2[j-1] == s3[i+j-1]，则dp[i][j] = dp[i][j] or dp[i][j-1]
3. 初始化：
   - dp[0][0] = True（两个空字符串可以组成空字符串）
   - dp[i][0] = (s1的前i个字符等于s3的前i个字符)
   - dp[0][j] = (s2的前j个字符等于s3的前j个字符)

时间复杂度分析：
设s1长度为m，s2长度为n
1. 动态规划计算：O(m * n)
总时间复杂度：O(m * n)

空间复杂度分析：
1. 二维DP数组：O(m * n)
2. 空间优化后：O(min(m, n))

相关题目扩展：
1. LeetCode 97. 交错字符串（本题）
2. LeetCode 115. 不同的子序列
3. LeetCode 1143. 最长公共子序列
4. LeetCode 72. 编辑距离
5. LeetCode 10. 正则表达式匹配

工程化考量：
1. 输入验证：检查输入参数的有效性
2. 异常处理：处理空字符串、长度不匹配等边界情况
3. 可配置性：可以将字符串比较逻辑作为参数传入
4. 单元测试：为isInterleave方法编写测试用例
5. 性能优化：对于长字符串，使用空间优化版本
"""

def isInterleave(s1: str, s2: str, s3: str) -> bool:
    """
    标准二维DP版本
    """
    m, n, length = len(s1), len(s2), len(s3)
    
    # 长度检查
    if m + n != length:
        return False
    
    # 创建DP数组
    dp = [[False] * (n + 1) for _ in range(m + 1)]
    
    # 初始化
    dp[0][0] = True
    
    # 初始化第一列：只使用s1
    for i in range(1, m + 1):
        dp[i][0] = dp[i - 1][0] and (s1[i - 1] == s3[i - 1])
    
    # 初始化第一行：只使用s2
    for j in range(1, n + 1):
        dp[0][j] = dp[0][j - 1] and (s2[j - 1] == s3[j - 1])
    
    # 填充DP数组
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            c3 = s3[i + j - 1]
            
            # 检查是否可以从s1取字符
            if s1[i - 1] == c3:
                dp[i][j] = dp[i][j] or dp[i - 1][j]
            
            # 检查是否可以从s2取字符
            if s2[j - 1] == c3:
                dp[i][j] = dp[i][j] or dp[i][j - 1]
    
    return dp[m][n]

def isInterleaveOptimized(s1: str, s2: str, s3: str) -> bool:
    """
    空间优化版本（使用一维数组）
    """
    m, n, length = len(s1), len(s2), len(s3)
    
    # 长度检查
    if m + n != length:
        return False
    
    # 为了节省空间，让s2作为较短的字符串
    if m < n:
        return isInterleaveOptimized(s2, s1, s3)
    
    # 使用一维DP数组
    dp = [False] * (n + 1)
    
    # 初始化
    dp[0] = True
    
    # 初始化第一行：只使用s2
    for j in range(1, n + 1):
        dp[j] = dp[j - 1] and (s2[j - 1] == s3[j - 1])
    
    # 填充DP数组
    for i in range(1, m + 1):
        # 更新第一列：只使用s1
        dp[0] = dp[0] and (s1[i - 1] == s3[i - 1])
        
        for j in range(1, n + 1):
            c3 = s3[i + j - 1]
            result = False
            
            # 检查是否可以从s1取字符
            if s1[i - 1] == c3:
                result = result or dp[j]  # 相当于dp[i-1][j]
            
            # 检查是否可以从s2取字符
            if s2[j - 1] == c3:
                result = result or dp[j - 1]  # 相当于dp[i][j-1]
            
            dp[j] = result
    
    return dp[n]

def test_interleaving_string():
    """测试函数"""
    # 测试用例1
    s1_1, s2_1, s3_1 = "aabcc", "dbbca", "aadbbcbcac"
    print("测试用例1:")
    print(f"标准版本: {isInterleave(s1_1, s2_1, s3_1)}")
    print(f"优化版本: {isInterleaveOptimized(s1_1, s2_1, s3_1)}")
    print("预期结果: True")
    print()
    
    # 测试用例2
    s1_2, s2_2, s3_2 = "aabcc", "dbbca", "aadbbbaccc"
    print("测试用例2:")
    print(f"标准版本: {isInterleave(s1_2, s2_2, s3_2)}")
    print(f"优化版本: {isInterleaveOptimized(s1_2, s2_2, s3_2)}")
    print("预期结果: False")
    print()
    
    # 测试用例3：边界情况
    s1_3, s2_3, s3_3 = "", "", ""
    print("测试用例3（空字符串）:")
    print(f"标准版本: {isInterleave(s1_3, s2_3, s3_3)}")
    print(f"优化版本: {isInterleaveOptimized(s1_3, s2_3, s3_3)}")
    print("预期结果: True")
    print()
    
    # 测试用例4：长度不匹配
    s1_4, s2_4, s3_4 = "abc", "def", "abcd"
    print("测试用例4（长度不匹配）:")
    print(f"标准版本: {isInterleave(s1_4, s2_4, s3_4)}")
    print(f"优化版本: {isInterleaveOptimized(s1_4, s2_4, s3_4)}")
    print("预期结果: False")

if __name__ == "__main__":
    test_interleaving_string()

"""
=============================================================================================
补充题目：LeetCode 115. 不同的子序列（Python实现）
题目链接：https://leetcode.cn/problems/distinct-subsequences/

Python实现：
def numDistinct(s: str, t: str) -> int:
    m, n = len(s), len(t)
    
    # 快速判断特殊情况
    if m < n:
        return 0
    if m == n:
        return 1 if s == t else 0
    
    # 创建DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 初始化
    for i in range(m + 1):
        dp[i][0] = 1
    
    # 填充DP数组
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s[i - 1] == t[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j]
            else:
                dp[i][j] = dp[i - 1][j]
    
    return dp[m][n]

工程化考量：
1. 使用类型注解提高代码可读性
2. 添加详细的文档字符串
3. 使用单元测试框架进行测试
4. 添加性能分析工具

优化思路：
1. 使用numpy数组加速计算
2. 使用缓存装饰器进行记忆化
3. 使用生成器表达式减少内存使用
4. 使用多进程并行计算
"""
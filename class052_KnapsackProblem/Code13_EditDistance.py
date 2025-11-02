# 编辑距离
# 给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数
# 你可以对一个单词进行如下三种操作：
# 1. 插入一个字符
# 2. 删除一个字符
# 3. 替换一个字符
# 测试链接 : https://leetcode.cn/problems/edit-distance/

"""
算法详解：
编辑距离是动态规划的经典问题，用于衡量两个字符串之间的相似度。
通过插入、删除、替换操作将一个字符串转换为另一个字符串所需的最少操作次数。

解题思路：
1. 状态定义：dp[i][j]表示将word1的前i个字符转换成word2的前j个字符所需的最少操作数
2. 状态转移方程：
   - 如果word1[i-1] == word2[j-1]，则dp[i][j] = dp[i-1][j-1]（不需要操作）
   - 否则，dp[i][j] = min(dp[i-1][j-1], dp[i-1][j], dp[i][j-1]) + 1
      其中：dp[i-1][j-1] + 1表示替换操作
           dp[i-1][j] + 1表示删除操作
           dp[i][j-1] + 1表示插入操作
3. 初始化：
   - dp[i][0] = i（删除i个字符）
   - dp[0][j] = j（插入j个字符）

时间复杂度分析：
设word1长度为m，word2长度为n
1. 动态规划计算：O(m * n)
总时间复杂度：O(m * n)

空间复杂度分析：
1. 二维DP数组：O(m * n)
2. 空间优化后：O(min(m, n))

相关题目扩展：
1. LeetCode 72. 编辑距离（本题）
2. LeetCode 1143. 最长公共子序列
3. LeetCode 97. 交错字符串
4. LeetCode 115. 不同的子序列
5. LeetCode 583. 两个字符串的删除操作
6. LeetCode 712. 两个字符串的最小ASCII删除和

工程化考量：
1. 输入验证：检查输入参数的有效性
2. 异常处理：处理空字符串、非法字符等边界情况
3. 可配置性：可以将操作代价作为配置参数传入
4. 单元测试：为minDistance方法编写测试用例
5. 性能优化：对于长字符串，使用空间优化版本

语言特性差异：
1. Python：使用列表推导式简洁但性能较低
2. 动态类型：无需声明变量类型，但运行效率相对较低
3. 丰富库支持：有丰富的标准库和第三方库支持

调试技巧：
1. 打印dp数组中间状态，观察状态转移过程
2. 使用断言验证边界条件
3. 构造小规模测试用例手动验证结果
"""

def minDistance(word1: str, word2: str) -> int:
    """
    标准二维DP版本
    """
    m, n = len(word1), len(word2)
    
    # 边界情况处理
    if m == 0:
        return n
    if n == 0:
        return m
    if word1 == word2:
        return 0
    
    # 创建DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 初始化边界条件
    for i in range(m + 1):
        dp[i][0] = i  # 将word1的前i个字符转换为空字符串需要i次删除操作
    
    for j in range(n + 1):
        dp[0][j] = j  # 将空字符串转换为word2的前j个字符需要j次插入操作
    
    # 填充DP数组
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if word1[i - 1] == word2[j - 1]:
                # 字符相同，不需要操作
                dp[i][j] = dp[i - 1][j - 1]
            else:
                # 字符不同，取三种操作的最小值加1
                dp[i][j] = min(
                    dp[i - 1][j - 1],  # 替换操作
                    dp[i - 1][j],      # 删除操作
                    dp[i][j - 1]       # 插入操作
                ) + 1
    
    return dp[m][n]

def minDistanceOptimized(word1: str, word2: str) -> int:
    """
    空间优化版本（使用一维数组）
    """
    m, n = len(word1), len(word2)
    
    # 边界情况处理
    if m == 0:
        return n
    if n == 0:
        return m
    if word1 == word2:
        return 0
    
    # 为了节省空间，让word2作为较短的字符串
    if m < n:
        return minDistanceOptimized(word2, word1)
    
    # 使用一维DP数组
    dp = [0] * (n + 1)
    
    # 初始化第一行
    for j in range(n + 1):
        dp[j] = j
    
    # 填充DP数组
    for i in range(1, m + 1):
        prev = dp[0]  # 保存左上角的值
        dp[0] = i     # 更新第一列
        
        for j in range(1, n + 1):
            temp = dp[j]  # 保存当前值，用于下一轮计算
            
            if word1[i - 1] == word2[j - 1]:
                dp[j] = prev  # 字符相同，直接继承左上角的值
            else:
                dp[j] = min(
                    prev,        # 替换操作（左上角）
                    dp[j],       # 删除操作（上方）
                    dp[j - 1]    # 插入操作（左方）
                ) + 1
            
            prev = temp  # 更新左上角的值
    
    return dp[n]

def test_edit_distance():
    """测试函数"""
    # 测试用例1
    word1_1, word2_1 = "horse", "ros"
    print("测试用例1:")
    print(f"标准版本: {minDistance(word1_1, word2_1)}")
    print(f"优化版本: {minDistanceOptimized(word1_1, word2_1)}")
    print("预期结果: 3")
    print()
    
    # 测试用例2
    word1_2, word2_2 = "intention", "execution"
    print("测试用例2:")
    print(f"标准版本: {minDistance(word1_2, word2_2)}")
    print(f"优化版本: {minDistanceOptimized(word1_2, word2_2)}")
    print("预期结果: 5")
    print()
    
    # 测试用例3：边界情况
    word1_3, word2_3 = "", "abc"
    print("测试用例3（空字符串）:")
    print(f"标准版本: {minDistance(word1_3, word2_3)}")
    print(f"优化版本: {minDistanceOptimized(word1_3, word2_3)}")
    print("预期结果: 3")
    print()
    
    # 测试用例4：相同字符串
    word1_4, word2_4 = "abc", "abc"
    print("测试用例4（相同字符串）:")
    print(f"标准版本: {minDistance(word1_4, word2_4)}")
    print(f"优化版本: {minDistanceOptimized(word1_4, word2_4)}")
    print("预期结果: 0")

if __name__ == "__main__":
    test_edit_distance()

"""
=============================================================================================
补充题目：LeetCode 1143. 最长公共子序列（Python实现）
题目链接：https://leetcode.cn/problems/longest-common-subsequence/

Python实现：
def longestCommonSubsequence(text1: str, text2: str) -> int:
    m, n = len(text1), len(text2)
    
    # 创建DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 填充DP数组
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if text1[i - 1] == text2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
    
    return dp[m][n]

def longestCommonSubsequenceOptimized(text1: str, text2: str) -> int:
    m, n = len(text1), len(text2)
    
    # 为了节省空间，让text2作为较短的字符串
    if m < n:
        return longestCommonSubsequenceOptimized(text2, text1)
    
    # 使用一维DP数组
    dp = [0] * (n + 1)
    
    for i in range(1, m + 1):
        prev = 0
        for j in range(1, n + 1):
            temp = dp[j]
            if text1[i - 1] == text2[j - 1]:
                dp[j] = prev + 1
            else:
                dp[j] = max(dp[j], dp[j - 1])
            prev = temp
    
    return dp[n]

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
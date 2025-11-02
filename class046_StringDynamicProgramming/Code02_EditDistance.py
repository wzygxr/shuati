# 编辑距离 (Edit Distance)
# 给你两个单词 word1 和 word2
# 请返回将 word1 转换成 word2 所使用的最少代价
# 你可以对一个单词进行如下三种操作：
# 插入一个字符，代价a
# 删除一个字符，代价b
# 替换一个字符，代价c
# 
# 题目来源：LeetCode 72. 编辑距离
# 测试链接：https://leetcode.cn/problems/edit-distance/
#
# 算法核心思想：
# 使用动态规划解决字符串编辑距离问题，通过构建二维DP表来计算最小编辑操作次数
#
# 时间复杂度分析：
# - 基础版本：O(n*m)，其中n为word1的长度，m为word2的长度
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
# - 自然语言处理：文本相似度计算、拼写检查
# - 生物信息学：DNA序列比对、基因序列分析
# - 信息检索：文档相似度计算、搜索引擎优化
# - 版本控制：Git等版本控制系统中的diff算法

class Solution:
    '''
    编辑距离算法（基础版）
    使用动态规划解决字符串编辑距离问题
    dp[i][j] 表示将str1的前i个字符转换为str2的前j个字符所需的最小代价
    
    状态转移方程：
    如果 s1[i-1] == s2[j-1]，不需要额外操作
      dp[i][j] = dp[i-1][j-1]
    否则，可以选择以下三种操作中的最小值：
      1. 替换：dp[i-1][j-1] + c
      2. 删除：dp[i-1][j] + b
      3. 插入：dp[i][j-1] + a
    
    边界条件：
    dp[i][0] = i * b，表示将str1前i个字符删除为空字符串的代价
    dp[0][j] = j * a，表示将空字符串插入j个字符得到str2前j个字符的代价
    
    时间复杂度：O(n*m)，其中n为str1的长度，m为str2的长度
    空间复杂度：O(n*m)
    '''
    def editDistance1(self, str1: str, str2: str, a: int, b: int, c: int) -> int:
        """
        基础动态规划解法
        使用二维DP数组存储中间结果
        
        状态定义：
        dp[i][j] 表示将str1的前i个字符转换为str2的前j个字符所需的最小代价
        
        状态转移方程：
        1. 如果s1[i-1] == s2[j-1]：不需要额外操作
           dp[i][j] = dp[i-1][j-1]
        2. 否则，可以选择以下三种操作中的最小值：
           - 替换：dp[i-1][j-1] + c
           - 删除：dp[i-1][j] + b
           - 插入：dp[i][j-1] + a
        
        边界条件：
        - dp[i][0] = i * b：将str1前i个字符删除为空字符串的代价
        - dp[0][j] = j * a：将空字符串插入j个字符得到str2前j个字符的代价
        
        参数:
            str1 (str): 源字符串
            str2 (str): 目标字符串
            a (int): 插入一个字符的代价
            b (int): 删除一个字符的代价
            c (int): 替换一个字符的代价
            
        返回:
            int: 最小编辑代价
        """
        # 输入验证
        if str1 is None or str2 is None:
            raise ValueError("输入字符串不能为None")
        
        n = len(str1)
        m = len(str2)
        
        # 边界情况处理
        if n == 0:
            return m * a  # str1为空，需要插入m个字符
        if m == 0:
            return n * b  # str2为空，需要删除n个字符
        
        # dp[i][j]: str1前i个字符转换为str2前j个字符的最小代价
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件
        for i in range(1, n + 1):
            dp[i][0] = i * b  # 删除前i个字符的代价
        for j in range(1, m + 1):
            dp[0][j] = j * a  # 插入前j个字符的代价
            
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                p1 = float('inf')
                if str1[i - 1] == str2[j - 1]:
                    p1 = dp[i - 1][j - 1]  # 字符相同，无需操作
                p2 = float('inf')
                if str1[i - 1] != str2[j - 1]:
                    p2 = dp[i - 1][j - 1] + c  # 替换操作
                p3 = dp[i][j - 1] + a  # 插入操作
                p4 = dp[i - 1][j] + b  # 删除操作
                dp[i][j] = int(min(min(p1, p2), min(p3, p4)))
                
        return dp[n][m]

    '''
    编辑距离算法（优化版）
    对基础版本进行逻辑优化，减少不必要的判断
    
    时间复杂度：O(n*m)
    空间复杂度：O(n*m)
    '''
    def editDistance2(self, str1: str, str2: str, a: int, b: int, c: int) -> int:
        """
        编辑距离算法（优化版）
        对基础版本进行逻辑优化，减少不必要的判断
        
        参数:
            str1 (str): 源字符串
            str2 (str): 目标字符串
            a (int): 插入一个字符的代价
            b (int): 删除一个字符的代价
            c (int): 替换一个字符的代价
            
        返回:
            int: 最小编辑代价
        """
        # 输入验证
        if str1 is None or str2 is None:
            raise ValueError("输入字符串不能为None")
        
        n = len(str1)
        m = len(str2)
        
        # 边界情况处理
        if n == 0:
            return m * a
        if m == 0:
            return n * b
        
        # dp[i][j]: str1前i个字符转换为str2前j个字符的最小代价
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件
        for i in range(1, n + 1):
            dp[i][0] = i * b
        for j in range(1, m + 1):
            dp[0][j] = j * a
            
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if str1[i - 1] == str2[j - 1]:
                    # 字符相同，无需操作
                    dp[i][j] = dp[i - 1][j - 1]
                else:
                    # 字符不同，选择三种操作中代价最小的
                    dp[i][j] = int(min(
                        min(dp[i - 1][j] + b,    # 删除操作
                            dp[i][j - 1] + a),    # 插入操作
                            dp[i - 1][j - 1] + c)) # 替换操作
                    
        return dp[n][m]

    '''
    空间优化版本
    使用滚动数组优化空间复杂度
    
    时间复杂度：O(n*m)
    空间复杂度：O(m)
    '''
    def editDistance3(self, str1: str, str2: str, a: int, b: int, c: int) -> int:
        """
        空间优化版本
        使用滚动数组优化空间复杂度
        
        参数:
            str1 (str): 源字符串
            str2 (str): 目标字符串
            a (int): 插入一个字符的代价
            b (int): 删除一个字符的代价
            c (int): 替换一个字符的代价
            
        返回:
            int: 最小编辑代价
        """
        # 输入验证
        if str1 is None or str2 is None:
            raise ValueError("输入字符串不能为None")
        
        n = len(str1)
        m = len(str2)
        
        # 边界情况处理
        if n == 0:
            return m * a
        if m == 0:
            return n * b
        
        # 只需要一维数组
        dp = [0] * (m + 1)
        
        # 初始化第一行
        for j in range(1, m + 1):
            dp[j] = j * a
            
        # 填充dp表
        for i in range(1, n + 1):
            leftUp = (i - 1) * b  # 保存dp[i-1][0]的值
            dp[0] = i * b         # 更新dp[i][0]的值
            for j in range(1, m + 1):
                backUp = dp[j]    # 保存当前dp[j]的值，用于下一次迭代
                if str1[i - 1] == str2[j - 1]:
                    # 字符相同，无需操作
                    dp[j] = leftUp
                else:
                    # 字符不同，选择三种操作中代价最小的
                    dp[j] = int(min(
                        min(dp[j] + b,      # 删除操作（对应dp[i-1][j] + b）
                            dp[j - 1] + a),  # 插入操作（对应dp[i][j-1] + a）
                            leftUp + c))     # 替换操作（对应dp[i-1][j-1] + c）
                leftUp = backUp   # 更新leftUp为原来的dp[j]，用于下一次迭代
                
        return dp[m]
    
    # 默认参数版本
    def minDistance(self, word1: str, word2: str) -> int:
        """
        默认参数版本的编辑距离计算方法
        所有操作的代价都设为1
        
        参数:
            word1 (str): 源字符串
            word2 (str): 目标字符串
            
        返回:
            int: 最小编辑操作次数
        """
        return self.editDistance2(word1, word2, 1, 1, 1)


# 测试函数
def test():
    """
    全面的单元测试
    覆盖各种边界情况和常见场景
    """
    sol = Solution()
    
    print("=== 编辑距离算法测试 ===")
    
    # 测试用例1：基本功能测试
    word1, word2 = "horse", "ros"
    print(f"\n测试: 基本功能测试")
    print(f"输入: word1=\"{word1}\", word2=\"{word2}\"")
    result1 = sol.minDistance(word1, word2)
    result2 = sol.editDistance1(word1, word2, 1, 1, 1)
    result3 = sol.editDistance2(word1, word2, 1, 1, 1)
    result4 = sol.editDistance3(word1, word2, 1, 1, 1)
    print(f"默认方法结果: {result1}")
    print(f"方法1结果: {result2}")
    print(f"方法2结果: {result3}")
    print(f"方法3结果: {result4}")
    print(f"测试结果: {'✅' if result1 == result2 == result3 == result4 == 3 else '❌'}")
    
    # 测试用例2：LeetCode官方测试用例
    word1, word2 = "intention", "execution"
    print(f"\n测试: LeetCode官方测试用例")
    print(f"输入: word1=\"{word1}\", word2=\"{word2}\"")
    result1 = sol.minDistance(word1, word2)
    result2 = sol.editDistance1(word1, word2, 1, 1, 1)
    result3 = sol.editDistance2(word1, word2, 1, 1, 1)
    result4 = sol.editDistance3(word1, word2, 1, 1, 1)
    print(f"默认方法结果: {result1}")
    print(f"方法1结果: {result2}")
    print(f"方法2结果: {result3}")
    print(f"方法3结果: {result4}")
    print(f"测试结果: {'✅' if result1 == result2 == result3 == result4 == 5 else '❌'}")
    
    # 测试用例3：空字符串测试
    word1, word2 = "", "abc"
    print(f"\n测试: 空源字符串测试")
    print(f"输入: word1=\"{word1}\", word2=\"{word2}\"")
    result1 = sol.minDistance(word1, word2)
    result2 = sol.editDistance1(word1, word2, 1, 1, 1)
    result3 = sol.editDistance2(word1, word2, 1, 1, 1)
    result4 = sol.editDistance3(word1, word2, 1, 1, 1)
    print(f"默认方法结果: {result1}")
    print(f"方法1结果: {result2}")
    print(f"方法2结果: {result3}")
    print(f"方法3结果: {result4}")
    print(f"测试结果: {'✅' if result1 == result2 == result3 == result4 == 3 else '❌'}")
    
    print("\n=== 所有测试通过 ===")


# 运行测试
if __name__ == "__main__":
    test()
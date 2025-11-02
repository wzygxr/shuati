# 编辑距离 (Edit Distance)
# 给你两个单词 word1 和 word2，请你计算出将 word1 转换成 word2 所使用的最少操作数。
# 你可以对一个单词进行如下三种操作：
# 插入一个字符
# 删除一个字符
# 替换一个字符
# 测试链接 : https://leetcode.cn/problems/edit-distance/

class Solution:
    # 方法1：暴力递归解法
    # 时间复杂度：O(3^(m+n)) - 指数级时间复杂度，效率极低
    # 空间复杂度：O(m+n) - 递归调用栈的深度
    # 问题：存在大量重复计算，效率低下
    def minDistance1(self, word1: str, word2: str) -> int:
        return self.f1(word1, word2, len(word1) - 1, len(word2) - 1)
    
    # str1[0..i] 与 str2[0..j] 的编辑距离
    def f1(self, str1: str, str2: str, i: int, j: int) -> int:
        # base case
        if i == -1:
            return j + 1  # str1为空，需要插入j+1个字符
        if j == -1:
            return i + 1  # str2为空，需要删除i+1个字符
        if str1[i] == str2[j]:
            return self.f1(str1, str2, i - 1, j - 1)  # 字符相同，不需要操作
        else:
            # 字符不同，三种操作中取最小值
            replace = self.f1(str1, str2, i - 1, j - 1) + 1  # 替换
            delete = self.f1(str1, str2, i - 1, j) + 1  # 删除
            insert = self.f1(str1, str2, i, j - 1) + 1  # 插入
            return min(replace, delete, insert)
    
    # 方法2：记忆化搜索（自顶向下动态规划）
    # 时间复杂度：O(m*n) - 每个状态只计算一次
    # 空间复杂度：O(m*n) - dp字典和递归调用栈
    # 优化：通过缓存已经计算的结果避免重复计算
    def minDistance2(self, word1: str, word2: str) -> int:
        dp = {}
        return self.f2(word1, word2, len(word1) - 1, len(word2) - 1, dp)
    
    # str1[0..i] 与 str2[0..j] 的编辑距离
    def f2(self, str1: str, str2: str, i: int, j: int, dp: dict) -> int:
        if i == -1:
            return j + 1
        if j == -1:
            return i + 1
        if (i, j) in dp:
            return dp[(i, j)]
        if str1[i] == str2[j]:
            ans = self.f2(str1, str2, i - 1, j - 1, dp)
        else:
            replace = self.f2(str1, str2, i - 1, j - 1, dp) + 1
            delete = self.f2(str1, str2, i - 1, j, dp) + 1
            insert = self.f2(str1, str2, i, j - 1, dp) + 1
            ans = min(replace, delete, insert)
        dp[(i, j)] = ans
        return ans
    
    # 方法3：动态规划（自底向上）
    # 时间复杂度：O(m*n) - 需要填满整个dp表
    # 空间复杂度：O(m*n) - dp数组存储所有状态
    # 优化：避免了递归调用的开销
    def minDistance3(self, word1: str, word2: str) -> int:
        m, n = len(word1), len(word2)
        # dp[i][j] 表示 word1[0..i-1] 和 word2[0..j-1] 的编辑距离
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 初始化边界条件
        for i in range(m + 1):
            dp[i][0] = i  # word2为空，需要删除i个字符
        for j in range(n + 1):
            dp[0][j] = j  # word1为空，需要插入j个字符
        
        # 填表过程
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if word1[i - 1] == word2[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1]  # 字符相同，不需要操作
                else:
                    # 字符不同，三种操作中取最小值
                    replace = dp[i - 1][j - 1] + 1  # 替换
                    delete = dp[i - 1][j] + 1  # 删除
                    insert = dp[i][j - 1] + 1  # 插入
                    dp[i][j] = min(replace, delete, insert)
        return dp[m][n]
    
    # 方法4：空间优化的动态规划
    # 时间复杂度：O(m*n) - 仍然需要计算所有状态
    # 空间复杂度：O(min(m,n)) - 只保存必要的状态值
    # 优化：只保存必要的状态，大幅减少空间使用
    def minDistance4(self, word1: str, word2: str) -> int:
        m, n = len(word1), len(word2)
        
        # 确保word1是较短的字符串，以优化空间
        if m > n:
            return self.minDistance4(word2, word1)
        
        # 使用两个一维数组来代替二维数组
        dp = [0] * (m + 1)
        pre = [0] * (m + 1)
        
        # 初始化边界条件
        for i in range(m + 1):
            pre[i] = i
        
        for j in range(1, n + 1):
            dp[0] = j  # word1为空，需要插入j个字符
            for i in range(1, m + 1):
                if word1[i - 1] == word2[j - 1]:
                    dp[i] = pre[i - 1]  # 字符相同，不需要操作
                else:
                    # 字符不同，三种操作中取最小值
                    replace = pre[i - 1] + 1  # 替换
                    delete = pre[i] + 1  # 删除
                    insert = dp[i - 1] + 1  # 插入
                    dp[i] = min(replace, delete, insert)
            # 交换dp和pre数组
            dp, pre = pre, dp
        return pre[m]

# 测试用例和性能对比
if __name__ == "__main__":
    solution = Solution()
    print("测试编辑距离实现：")
    
    # 测试用例1
    word1 = "horse"
    word2 = "ros"
    print(f"word1 = \"{word1}\", word2 = \"{word2}\"")
    print(f"方法3 (动态规划): {solution.minDistance3(word1, word2)}")
    print(f"方法4 (空间优化): {solution.minDistance4(word1, word2)}")
    
    # 测试用例2
    word1 = "intention"
    word2 = "execution"
    print(f"\nword1 = \"{word1}\", word2 = \"{word2}\"")
    print(f"方法3 (动态规划): {solution.minDistance3(word1, word2)}")
    print(f"方法4 (空间优化): {solution.minDistance4(word1, word2)}")
    
    # 测试用例3
    word1 = "a"
    word2 = "b"
    print(f"\nword1 = \"{word1}\", word2 = \"{word2}\"")
    print(f"方法3 (动态规划): {solution.minDistance3(word1, word2)}")
    print(f"方法4 (空间优化): {solution.minDistance4(word1, word2)}")
# 两个字符串的删除操作
# 给定两个单词 word1 和 word2 ，返回使得 word1 和 word2 相同所需的最小步数。
# 每步可以删除任意一个字符串中的一个字符。
# 测试链接 : https://leetcode.cn/problems/delete-operation-for-two-strings/

class Solution:
    '''
    两个字符串的删除操作 - 动态规划解法
    使用动态规划解决两个字符串的删除操作问题
    dp[i][j] 表示使字符串word1的前i个字符与字符串word2的前j个字符相同所需的最小删除步数
    
    状态转移方程：
    如果 word1[i-1] == word2[j-1]：
      dp[i][j] = dp[i-1][j-1]
    否则：
      dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + 1
    
    解释：
    当当前字符相等时，不需要删除操作，结果等于前面子串的删除步数
    当当前字符不相等时，可以选择删除word1的字符或删除word2的字符，取较小值加1
    
    边界条件：
    dp[i][0] = i，表示将word1的前i个字符删除为空字符串需要i步
    dp[0][j] = j，表示将空字符串变为word2的前j个字符需要j步
    
    时间复杂度：O(n*m)，其中n为word1的长度，m为word2的长度
    空间复杂度：O(n*m)
    '''
    def minDistance(self, word1: str, word2: str) -> int:
        n, m = len(word1), len(word2)
        
        # dp[i][j] 表示使word1的前i个字符与word2的前j个字符相同所需的最小删除步数
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件
        for i in range(1, n + 1):
            dp[i][0] = i
        for j in range(1, m + 1):
            dp[0][j] = j
        
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if word1[i - 1] == word2[j - 1]:
                    # 当前字符相等，不需要删除操作
                    dp[i][j] = dp[i - 1][j - 1]
                else:
                    # 当前字符不相等，选择删除word1或word2的字符
                    dp[i][j] = min(dp[i - 1][j], dp[i][j - 1]) + 1
        
        return dp[n][m]

    '''
    空间优化版本
    使用滚动数组优化空间复杂度
    
    时间复杂度：O(n*m)
    空间复杂度：O(m)
    '''
    def minDistanceOptimized(self, word1: str, word2: str) -> int:
        n, m = len(word1), len(word2)
        
        # 只需要一行数组
        dp = [0] * (m + 1)
        
        # 初始化第一行
        for j in range(1, m + 1):
            dp[j] = j
        
        # 填充dp表
        for i in range(1, n + 1):
            prev = dp[0]  # 保存dp[i-1][j-1]的值
            dp[0] = i     # 更新dp[i][0]的值
            
            for j in range(1, m + 1):
                temp = dp[j]  # 保存当前dp[j]的值，用于下一次循环
                
                if word1[i - 1] == word2[j - 1]:
                    # 当前字符相等，不需要删除操作
                    dp[j] = prev
                else:
                    # 当前字符不相等，选择删除word1或word2的字符
                    dp[j] = min(dp[j], dp[j - 1]) + 1
                
                prev = temp  # 更新prev为原来的dp[j]值
        
        return dp[m]


# 测试函数
def test():
    sol = Solution()
    
    # 测试用例
    test_cases = [
        ("sea", "eat"),     # 2
        ("leetcode", "etco"), # 4
        ("", "a"),          # 1
        ("a", ""),          # 1
        ("", "")            # 0
    ]
    
    print("两个字符串的删除操作测试:")
    for word1, word2 in test_cases:
        result1 = sol.minDistance(word1, word2)
        result2 = sol.minDistanceOptimized(word1, word2)
        print(f'word1="{word1}", word2="{word2}" => {result1} (optimized: {result2})')


# 运行测试
if __name__ == "__main__":
    test()
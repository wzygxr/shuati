# 两个字符串的最小ASCII删除和
# 给定两个字符串s1 和 s2，返回使两个字符串相等所需删除字符的 ASCII 值的最小和。
# 测试链接 : https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/

class Solution:
    '''
    两个字符串的最小ASCII删除和 - 动态规划解法
    使用动态规划解决两个字符串的最小ASCII删除和问题
    dp[i][j] 表示使字符串s1的前i个字符与字符串s2的前j个字符相同所需删除字符的ASCII值的最小和
    
    状态转移方程：
    如果 s1[i-1] == s2[j-1]：
      dp[i][j] = dp[i-1][j-1]
    否则：
      dp[i][j] = min(dp[i-1][j] + ord(s1[i-1]), dp[i][j-1] + ord(s2[j-1]))
    
    解释：
    当当前字符相等时，不需要删除操作，结果等于前面子串的删除ASCII和
    当当前字符不相等时，可以选择删除s1的字符或删除s2的字符，取ASCII值较小的方案
    
    边界条件：
    dp[i][0] = dp[i-1][0] + ord(s1[i-1])，表示将s1的前i个字符删除所需的ASCII和
    dp[0][j] = dp[0][j-1] + ord(s2[j-1])，表示将s2的前j个字符删除所需的ASCII和
    
    时间复杂度：O(n*m)，其中n为s1的长度，m为s2的长度
    空间复杂度：O(n*m)
    '''
    def minimumDeleteSum(self, s1: str, s2: str) -> int:
        n, m = len(s1), len(s2)
        
        # dp[i][j] 表示使s1的前i个字符与s2的前j个字符相同所需删除字符的ASCII值的最小和
        dp = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 边界条件
        for i in range(1, n + 1):
            dp[i][0] = dp[i - 1][0] + ord(s1[i - 1])
        for j in range(1, m + 1):
            dp[0][j] = dp[0][j - 1] + ord(s2[j - 1])
        
        # 填充dp表
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if s1[i - 1] == s2[j - 1]:
                    # 当前字符相等，不需要删除操作
                    dp[i][j] = dp[i - 1][j - 1]
                else:
                    # 当前字符不相等，选择删除ASCII值较小的字符
                    dp[i][j] = min(dp[i - 1][j] + ord(s1[i - 1]), dp[i][j - 1] + ord(s2[j - 1]))
        
        return dp[n][m]

    '''
    空间优化版本
    使用滚动数组优化空间复杂度
    
    时间复杂度：O(n*m)
    空间复杂度：O(m)
    '''
    def minimumDeleteSumOptimized(self, s1: str, s2: str) -> int:
        n, m = len(s1), len(s2)
        
        # 只需要一行数组
        dp = [0] * (m + 1)
        
        # 初始化第一行
        for j in range(1, m + 1):
            dp[j] = dp[j - 1] + ord(s2[j - 1])
        
        # 填充dp表
        for i in range(1, n + 1):
            prev = dp[0]  # 保存dp[i-1][j-1]的值
            dp[0] = dp[0] + ord(s1[i - 1])  # 更新dp[i][0]的值
            
            for j in range(1, m + 1):
                temp = dp[j]  # 保存当前dp[j]的值，用于下一次循环
                
                if s1[i - 1] == s2[j - 1]:
                    # 当前字符相等，不需要删除操作
                    dp[j] = prev
                else:
                    # 当前字符不相等，选择删除ASCII值较小的字符
                    dp[j] = min(temp + ord(s1[i - 1]), dp[j - 1] + ord(s2[j - 1]))
                
                prev = temp  # 更新prev为原来的dp[j]值
        
        return dp[m]


# 测试函数
def test():
    sol = Solution()
    
    # 测试用例
    test_cases = [
        ("sea", "eat"),     # 231
        ("delete", "leet"), # 403
        ("", "a"),          # 97
        ("a", ""),          # 97
        ("", "")            # 0
    ]
    
    print("两个字符串的最小ASCII删除和测试:")
    for s1, s2 in test_cases:
        result1 = sol.minimumDeleteSum(s1, s2)
        result2 = sol.minimumDeleteSumOptimized(s1, s2)
        print(f's1="{s1}", s2="{s2}" => {result1} (optimized: {result2})')


# 运行测试
if __name__ == "__main__":
    test()
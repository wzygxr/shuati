# 最长有效括号
# 给你一个只包含 '(' 和 ')' 的字符串
# 找出最长有效（格式正确且连续）括号子串的长度。
# 测试链接 : https://leetcode.cn/problems/longest-valid-parentheses/

class Solution:
    # 时间复杂度O(n)，n是str字符串的长度
    # 空间复杂度O(n)，dp数组存储以每个位置结尾的最长有效括号长度
    # 核心思想：动态规划，dp[i]表示以i位置字符结尾的最长有效括号长度
    def longestValidParentheses(self, s: str) -> int:
        if not s:
            return 0
            
        # dp[0...n-1]
        # dp[i] : 子串必须以i位置的字符结尾的情况下，往左整体有效的最大长度
        dp = [0] * len(s)
        ans = 0
        for i in range(1, len(s)):
            if s[i] == ')':
                p = i - dp[i - 1] - 1
                #  ?         )
                #  p         i
                if p >= 0 and s[p] == '(':
                    dp[i] = dp[i - 1] + 2 + (dp[p - 1] if p - 1 >= 0 else 0)
            ans = max(ans, dp[i])
        return ans

# 测试用例
if __name__ == "__main__":
    solution = Solution()
    print("测试最长有效括号问题：")
    
    # 测试用例1
    s1 = "(()"
    print(f"s = \"{s1}\"")
    print(f"最长有效括号长度: {solution.longestValidParentheses(s1)}")
    
    # 测试用例2
    s2 = ")()())"
    print(f"s = \"{s2}\"")
    print(f"最长有效括号长度: {solution.longestValidParentheses(s2)}")
    
    # 测试用例3
    s3 = ""
    print(f"s = \"{s3}\"")
    print(f"最长有效括号长度: {solution.longestValidParentheses(s3)}")
    
    # 测试用例4
    s4 = "()(()"
    print(f"s = \"{s4}\"")
    print(f"最长有效括号长度: {solution.longestValidParentheses(s4)}")
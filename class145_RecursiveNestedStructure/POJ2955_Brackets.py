# POJ 2955 Brackets (最长括号匹配子序列)
# 测试链接 : http://poj.org/problem?id=2955

class POJ2955_Brackets:
    def longestValidParentheses(self, s: str) -> int:
        n = len(s)
        if n == 0:
            return 0
        
        # dp[i][j] 表示区间[i,j]内最长的有效括号长度
        dp = [[0] * n for _ in range(n)]
        
        # 填充dp表
        for length in range(2, n + 1):  # 区间长度从2开始
            for i in range(n - length + 1):
                j = i + length - 1
                
                # 如果首尾字符匹配
                if (s[i] == '(' and s[j] == ')') or (s[i] == '[' and s[j] == ']'):
                    dp[i][j] = dp[i + 1][j - 1] + 2
                
                # 尝试分割区间
                for k in range(i, j):
                    dp[i][j] = max(dp[i][j], dp[i][k] + dp[k + 1][j])
        
        return dp[0][n - 1] if n > 0 else 0

# 测试用例
def main():
    solution = POJ2955_Brackets()
    
    # 测试用例1
    s1 = "((()))"
    print(f"输入: {s1}")
    print(f"输出: {solution.longestValidParentheses(s1)}")
    print(f"期望: 6\n")
    
    # 测试用例2
    s2 = "()()()"
    print(f"输入: {s2}")
    print(f"输出: {solution.longestValidParentheses(s2)}")
    print(f"期望: 6\n")

if __name__ == "__main__":
    main()
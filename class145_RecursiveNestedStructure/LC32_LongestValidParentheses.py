# LeetCode 32. Longest Valid Parentheses (最长有效括号)
# 测试链接 : https://leetcode.cn/problems/longest-valid-parentheses/

class LC32_LongestValidParentheses:
    def longestValidParentheses(self, s: str) -> int:
        stack = [-1]  # 初始化栈底为-1
        max_len = 0
        
        for i, c in enumerate(s):
            if c == '(':
                stack.append(i)  # 遇到左括号，压入索引
            else:
                stack.pop()  # 遇到右括号，弹出栈顶
                if not stack:
                    # 栈为空，压入当前索引作为新的基准
                    stack.append(i)
                else:
                    # 计算当前有效括号长度
                    max_len = max(max_len, i - stack[-1])
        
        return max_len

# 测试用例
def main():
    solution = LC32_LongestValidParentheses()
    
    # 测试用例1
    s1 = "(()"
    print(f"输入: {s1}")
    print(f"输出: {solution.longestValidParentheses(s1)}")
    print(f"期望: 2\n")
    
    # 测试用例2
    s2 = ")()())"
    print(f"输入: {s2}")
    print(f"输出: {solution.longestValidParentheses(s2)}")
    print(f"期望: 4\n")
    
    # 测试用例3
    s3 = ""
    print(f"输入: {s3}")
    print(f"输出: {solution.longestValidParentheses(s3)}")
    print(f"期望: 0\n")

if __name__ == "__main__":
    main()
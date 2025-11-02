# LeetCode 856. Score of Parentheses (括号的分数)
# 测试链接 : https://leetcode.cn/problems/score-of-parentheses/

class LC856_ScoreOfParentheses:
    def scoreOfParentheses(self, s: str) -> int:
        stack = [0]  # 初始化栈底为0
        
        for c in s:
            if c == '(':
                stack.append(0)  # 遇到左括号，压入0
            else:
                v = stack.pop()  # 弹出当前值
                w = stack.pop()  # 弹出前一个值
                # 计算当前括号对的分数并加到前一个值上
                stack.append(w + max(2 * v, 1))
        
        return stack.pop()  # 返回最终结果

# 测试用例
def main():
    solution = LC856_ScoreOfParentheses()
    
    # 测试用例1
    s1 = "()"
    print(f"输入: {s1}")
    print(f"输出: {solution.scoreOfParentheses(s1)}")
    print(f"期望: 1\n")
    
    # 测试用例2
    s2 = "(())"
    print(f"输入: {s2}")
    print(f"输出: {solution.scoreOfParentheses(s2)}")
    print(f"期望: 2\n")
    
    # 测试用例3
    s3 = "()()"
    print(f"输入: {s3}")
    print(f"输出: {solution.scoreOfParentheses(s3)}")
    print(f"期望: 2\n")
    
    # 测试用例4
    s4 = "(()(()))"
    print(f"输入: {s4}")
    print(f"输出: {solution.scoreOfParentheses(s4)}")
    print(f"期望: 6\n")

if __name__ == "__main__":
    main()
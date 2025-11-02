# LeetCode 20. Valid Parentheses (有效的括号)
# 测试链接 : https://leetcode.cn/problems/valid-parentheses/

class LC20_ValidParentheses:
    def isValid(self, s: str) -> bool:
        stack = []
        
        # 定义括号匹配关系
        mapping = {')': '(', ']': '[', '}': '{'}
        
        for c in s:
            if c in '([{':
                stack.append(c)  # 遇到左括号入栈
            else:
                if not stack:  # 栈为空但遇到右括号
                    return False
                
                top = stack.pop()  # 弹出栈顶元素
                # 检查括号是否匹配
                if mapping[c] != top:
                    return False
        
        return not stack  # 栈为空表示所有括号都匹配

# 测试用例
def main():
    solution = LC20_ValidParentheses()
    
    # 测试用例1
    s1 = "()"
    print(f"输入: {s1}")
    print(f"输出: {solution.isValid(s1)}")
    print(f"期望: true\n")
    
    # 测试用例2
    s2 = "()[]{}"
    print(f"输入: {s2}")
    print(f"输出: {solution.isValid(s2)}")
    print(f"期望: true\n")
    
    # 测试用例3
    s3 = "(]"
    print(f"输入: {s3}")
    print(f"输出: {solution.isValid(s3)}")
    print(f"期望: false\n")

if __name__ == "__main__":
    main()
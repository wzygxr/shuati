# UVA 551 Nesting a Bunch of Brackets (多种类型括号匹配)
# 测试链接 : https://onlinejudge.org/external/5/551.pdf

class UVA551_NestingBrackets:
    def checkBrackets(self, s: str) -> str:
        stack = []
        positions = []
        
        for i, c in enumerate(s):
            if c in '([{<':
                stack.append(c)
                positions.append(i + 1)  # 位置从1开始计数
            elif c in ')]}>':
                if not stack:
                    return f"NO {i + 1}"  # 不匹配的位置
                
                top = stack.pop()
                positions.pop()
                
                # 检查括号类型是否匹配
                if not self.isMatchingPair(top, c):
                    return f"NO {i + 1}"  # 不匹配的位置
        
        if stack:
            return f"NO {positions[-1]}"  # 未匹配的括号位置
        
        return "YES"
    
    def isMatchingPair(self, open_char: str, close_char: str) -> bool:
        return (open_char == '(' and close_char == ')') or \
               (open_char == '[' and close_char == ']') or \
               (open_char == '{' and close_char == '}') or \
               (open_char == '<' and close_char == '>')

# 测试用例
def main():
    solution = UVA551_NestingBrackets()
    
    # 测试用例1
    s1 = "([]){}"
    print(f"输入: {s1}")
    print(f"输出: {solution.checkBrackets(s1)}")
    print(f"期望: YES\n")
    
    # 测试用例2
    s2 = "([)]"
    print(f"输入: {s2}")
    print(f"输出: {solution.checkBrackets(s2)}")
    print(f"期望: NO 3\n")

if __name__ == "__main__":
    main()
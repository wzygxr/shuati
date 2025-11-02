# UVA 551 Nesting a Bunch of Brackets
# 题目链接 : https://onlinejudge.org/external/5/551.pdf
#
# 题目描述:
# 在这个问题中，我们考虑包含括号的表达式，这些括号是正确嵌套的。
# 这些表达式是通过并置获得的，即通过将表达式的有限序列一个接一个地写下来。
# 每个表达式可以是单个字符，也可以是用一对匹配的括号括起来的表达式序列。
# 有几种括号对：()、[]、{}、<>。
# 在这个表达式中，除了括号之外，还有几种括号对，所以我们要对表达式施加第二个条件：
# 匹配的括号应该是同一种类的。
#
# 示例:
# 输入：([)]
# 输出：No
# 解释: 括号没有正确匹配
#
# 输入：([])
# 输出：Yes
# 解释: 括号正确匹配
#
# 解题思路:
# 使用栈来验证括号是否正确匹配
# 遍历字符串中的每个字符：
# 1. 如果是左括号，入栈
# 2. 如果是右括号，检查栈顶是否为对应的左括号
# 3. 如果匹配，弹出栈顶元素；如果不匹配，返回错误
# 4. 遍历结束后，如果栈为空，则括号正确匹配
#
# 时间复杂度: O(n)，其中n是字符串的长度，需要遍历字符串一次
# 空间复杂度: O(n)，栈的空间复杂度最坏情况下为O(n)

def check_brackets(s: str) -> str:
    # 使用列表模拟栈存储左括号
    stack = []
    
    # 定义括号匹配关系
    pairs = {')': '(', ']': '[', '}': '{', '>': '<'}
    
    # 遍历字符串中的每个字符
    for c in s:
        # 如果是左括号，入栈
        if c in '([{<':
            stack.append(c)
        # 如果是右括号，检查匹配
        elif c in ')]}>':
            # 如果栈为空，说明没有对应的左括号
            if not stack:
                return "No"
            
            # 弹出栈顶元素
            top = stack.pop()
            
            # 检查是否匹配
            if pairs[c] != top:
                return "No"
    
    # 如果栈为空，说明所有括号都正确匹配
    return "Yes" if not stack else "No"

# 测试用例
def main():
    # 测试用例1
    s1 = "([])"
    print(f"输入: {s1}")
    print(f"输出: {check_brackets(s1)}")
    print(f"期望: Yes\n")
    
    # 测试用例2
    s2 = "([)]"
    print(f"输入: {s2}")
    print(f"输出: {check_brackets(s2)}")
    print(f"期望: No\n")
    
    # 测试用例3
    s3 = "([]{})"
    print(f"输入: {s3}")
    print(f"输出: {check_brackets(s3)}")
    print(f"期望: Yes\n")
    
    # 测试用例4
    s4 = "([{}])"
    print(f"输入: {s4}")
    print(f"输出: {check_brackets(s4)}")
    print(f"期望: Yes\n")

if __name__ == "__main__":
    main()
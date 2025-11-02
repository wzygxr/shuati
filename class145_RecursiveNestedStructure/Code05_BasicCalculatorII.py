# LeetCode 227. Basic Calculator II (基本计算器 II)
# 测试链接 : https://leetcode.cn/problems/basic-calculator-ii/
#
# 题目描述:
# 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
# 整数除法仅保留整数部分。
# 你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
# 注意：不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
# 表达式中的所有整数都是非负整数，且在范围 [0, 2^31 - 1] 内
# 题目数据保证答案是一个 32-bit 整数
#
# 示例:
# 输入：s = "3+2*2"
# 输出：7
#
# 输入：s = " 3/2 "
# 输出：1
#
# 输入：s = " 3+5 / 2 "
# 输出：5
#
# 解题思路:
# 使用栈来处理运算符优先级，乘除法优先级高于加减法
# 遇到数字时，根据前一个运算符决定如何处理：
# 1. 如果是+或-，直接入栈（-号入栈负数）
# 2. 如果是*或/，与栈顶元素计算后入栈
# 最后将栈中所有元素相加得到结果
#
# 时间复杂度: O(n)，其中n是字符串的长度，需要遍历字符串一次
# 空间复杂度: O(n)，栈的空间复杂度最坏情况下为O(n)

class Solution:
    def calculate(self, s: str) -> int:
        # 使用列表模拟栈存储中间结果
        stack = []
        # 记录前一个运算符，初始为'+'
        pre_op = '+'
        # 记录当前正在构建的数字
        cur_num = 0
        
        # 遍历字符串
        for i in range(len(s)):
            c = s[i]
            
            # 如果是数字，构建当前数字
            if c.isdigit():
                cur_num = cur_num * 10 + int(c)
            
            # 如果是运算符或者到达字符串末尾，根据前一个运算符处理
            if c in '+-*/' or i == len(s) - 1:
                # 根据前一个运算符处理当前数字
                if pre_op == '+':
                    stack.append(cur_num)
                elif pre_op == '-':
                    stack.append(-cur_num)
                elif pre_op == '*':
                    # 与栈顶元素相乘后替换栈顶元素
                    stack[-1] = stack[-1] * cur_num
                elif pre_op == '/':
                    # 与栈顶元素相除后替换栈顶元素
                    # 注意Python中负数除法的处理
                    if stack[-1] < 0:
                        stack[-1] = -(-stack[-1] // cur_num)
                    else:
                        stack[-1] = stack[-1] // cur_num
                
                # 更新前一个运算符
                pre_op = c
                # 重置当前数字
                cur_num = 0
        
        # 将栈中所有元素相加得到最终结果
        return sum(stack)

# 测试用例
def main():
    solution = Solution()
    
    # 测试用例1
    s1 = "3+2*2"
    print(f"输入: {s1}")
    print(f"输出: {solution.calculate(s1)}")
    print(f"期望: 7\n")
    
    # 测试用例2
    s2 = " 3/2 "
    print(f"输入: {s2}")
    print(f"输出: {solution.calculate(s2)}")
    print(f"期望: 1\n")
    
    # 测试用例3
    s3 = " 3+5 / 2 "
    print(f"输入: {s3}")
    print(f"输出: {solution.calculate(s3)}")
    print(f"期望: 5\n")

if __name__ == "__main__":
    main()
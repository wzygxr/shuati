# LeetCode 772. Basic Calculator III (基本计算器 III)
# 测试链接 : https://leetcode.cn/problems/basic-calculator-iii/
#
# 题目描述:
# 实现一个基本计算器来计算并返回给定字符串表达式的值。
# 整数除法仅保留整数部分。
# 你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
# 注意：不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
# 表达式中的所有整数都是非负整数，且在范围 [0, 2^31 - 1] 内
# 答案保证是32位整数
#
# 示例:
# 输入：s = "1+1"
# 输出：2
#
# 输入：s = "6-4/2"
# 输出：4
#
# 输入：s = "2*(5+5*2)/3+(6/2+8)"
# 输出：21
#
# 解题思路:
# 使用递归处理嵌套结构，遇到左括号时递归处理括号内的表达式
# 使用两个列表分别存储数字和操作符
# 乘除法优先级高于加减法，需要特殊处理
#
# 时间复杂度: O(n)，其中n是字符串的长度
# 空间复杂度: O(n)，递归调用栈的深度和存储数字操作符的额外空间

class Solution:
    def __init__(self):
        self.where = 0
    
    def calculate(self, s: str) -> int:
        self.where = 0
        return self.f(list(s), 0)
    
    # s[i....]开始计算，遇到字符串终止 或者 遇到)停止
    # 返回 : 自己负责的这一段，计算的结果
    # 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    def f(self, s: list, i: int) -> int:
        cur = 0
        numbers = []
        ops = []
        while i < len(s) and s[i] != ')':
            if s[i].isdigit():
                cur = cur * 10 + int(s[i])
                i += 1
            elif s[i] != '(':
                # 遇到了运算符 + - * /
                self.push(numbers, ops, cur, s[i])
                cur = 0
                i += 1
            else:
                # i (.....)
                # 遇到了左括号！
                cur = self.f(s, i + 1)
                i = self.where + 1
        self.push(numbers, ops, cur, '+')
        self.where = i
        return self.compute(numbers, ops)
    
    # 根据操作符处理数字，乘除法优先级高需要特殊处理
    def push(self, numbers: list, ops: list, cur: int, op: str) -> None:
        n = len(numbers)
        if n == 0 or ops[n - 1] in ['+', '-']:
            numbers.append(cur)
            ops.append(op)
        else:
            top_number = numbers[n - 1]
            top_op = ops[n - 1]
            if top_op == '*':
                numbers[n - 1] = top_number * cur
            else:
                numbers[n - 1] = int(top_number / cur)  # 使用int()确保截断 toward zero
            ops[n - 1] = op
    
    # 计算最终结果，只处理加减法
    def compute(self, numbers: list, ops: list) -> int:
        n = len(numbers)
        ans = numbers[0]
        for i in range(1, n):
            ans += numbers[i] if ops[i - 1] == '+' else -numbers[i]
        return ans

# 测试用例
def main():
    solution = Solution()
    
    # 测试用例1
    s1 = "1+1"
    print(f"输入: {s1}")
    print(f"输出: {solution.calculate(s1)}")
    print(f"期望: 2\n")
    
    # 测试用例2
    s2 = "6-4/2"
    print(f"输入: {s2}")
    print(f"输出: {solution.calculate(s2)}")
    print(f"期望: 4\n")
    
    # 测试用例3
    s3 = "2*(5+5*2)/3+(6/2+8)"
    print(f"输入: {s3}")
    print(f"输出: {solution.calculate(s3)}")
    print(f"期望: 21\n")

if __name__ == "__main__":
    main()
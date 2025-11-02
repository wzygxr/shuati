# LeetCode 224. Basic Calculator (基本计算器)
# 测试链接 : https://leetcode.cn/problems/basic-calculator/
#
# 题目描述:
# 给你一个字符串表达式 s ，请你实现一个基本计算器来计算并返回它的值。
# 整数除法仅保留整数部分。
# 你可以假设给定的表达式总是有效的。所有中间结果将在 [-2^31, 2^31 - 1] 的范围内。
# 注意：不允许使用任何将字符串作为数学表达式计算的内置函数，比如 eval() 。
#
# 示例:
# 输入：s = "1 + 1"
# 输出：2
#
# 输入：s = " 2-1 + 2 "
# 输出：3
#
# 输入：s = "(1+(4+5+2)-3)+(6+8)"
# 输出：23
#
# 解题思路:
# 使用递归处理嵌套结构，遇到左括号时递归处理括号内的表达式
# 使用两个列表分别存储数字和操作符
# 只包含加减法和括号，不需要考虑运算符优先级
#
# 时间复杂度: O(n)，其中n是字符串的长度
# 空间复杂度: O(n)，递归调用栈的深度和存储数字操作符的额外空间

class Solution:
    def __init__(self):
        self.where = 0
    
    def calculate(self, s: str) -> int:
        self.where = 0
        return self.f(s, 0)
    
    # s[i....]开始计算，遇到字符串终止 或者 遇到)停止
    # 返回 : 自己负责的这一段，计算的结果
    # 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    def f(self, s: str, i: int) -> int:
        cur = 0
        numbers = []
        ops = []
        # 用于处理正负号
        is_positive = True
        
        while i < len(s) and s[i] != ')':
            if s[i].isdigit():
                cur = cur * 10 + int(s[i])
                i += 1
            elif s[i] == '+' or s[i] == '-':
                # 遇到了运算符 + -
                # 根据符号处理数字
                if not is_positive:
                    cur = -cur
                    is_positive = True
                numbers.append(cur)
                ops.append(s[i])
                cur = 0
                i += 1
            elif s[i] == '(':
                # i (.....)
                # 遇到了左括号！
                cur = self.f(s, i + 1)
                i = self.where + 1
            elif s[i] == ' ':
                # 跳过空格
                i += 1
            else:
                i += 1
        
        # 处理最后一个数字
        if not is_positive:
            cur = -cur
        numbers.append(cur)
        self.where = i
        return self.compute(numbers, ops)
    
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
    s1 = "1 + 1"
    print(f"输入: {s1}")
    print(f"输出: {solution.calculate(s1)}")
    print(f"期望: 2\n")
    
    # 测试用例2
    s2 = " 2-1 + 2 "
    print(f"输入: {s2}")
    print(f"输出: {solution.calculate(s2)}")
    print(f"期望: 3\n")
    
    # 测试用例3
    s3 = "(1+(4+5+2)-3)+(6+8)"
    print(f"输入: {s3}")
    print(f"输出: {solution.calculate(s3)}")
    print(f"期望: 23\n")

if __name__ == "__main__":
    main()
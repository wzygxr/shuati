# 含有嵌套的表达式求值
# 力扣上本题为会员题，所以额外提供了牛客网的测试链接
# 如果在牛客网上提交，请将函数名从calculate改为solve
# 测试链接 : https://leetcode.cn/problems/basic-calculator-iii/
# 测试链接 : https://www.nowcoder.com/practice/c215ba61c8b1443b996351df929dc4d4
#
# 相关题目:
# 1. LeetCode 224. Basic Calculator (基本计算器)
#    链接: https://leetcode.cn/problems/basic-calculator/
#    区别: 只包含加减法和括号
#
# 2. LeetCode 227. Basic Calculator II (基本计算器 II)
#    链接: https://leetcode.cn/problems/basic-calculator-ii/
#    区别: 包含加减乘除，但不包含括号
#
# 3. LeetCode 772. Basic Calculator III (基本计算器 III)
#    链接: https://leetcode.cn/problems/basic-calculator-iii/
#    区别: 包含加减乘除和括号，是这三题中最复杂的
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
        return self.f(s, 0)
    
    # s[i....]开始计算，遇到字符串终止 或者 遇到)停止
    # 返回 : 自己负责的这一段，计算的结果
    # 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    def f(self, s: str, i: int) -> int:
        cur = 0
        numbers = []
        ops = []
        while i < len(s) and s[i] != ')':
            if '0' <= s[i] <= '9':
                cur = cur * 10 + int(s[i])
                i += 1
            elif s[i] != '(':
                # 遇到了运算符 + - * /
                self.push(numbers, ops, cur, s[i])
                i += 1
                cur = 0
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
        if n == 0 or ops[n - 1] == '+' or ops[n - 1] == '-':
            numbers.append(cur)
            ops.append(op)
        else:
            topNumber = numbers[n - 1]
            topOp = ops[n - 1]
            if topOp == '*':
                numbers[n - 1] = topNumber * cur
            else:
                numbers[n - 1] = topNumber // cur
            ops[n - 1] = op
    
    # 计算最终结果，只处理加减法
    def compute(self, numbers: list, ops: list) -> int:
        n = len(numbers)
        ans = numbers[0]
        for i in range(1, n):
            ans += numbers[i] if ops[i - 1] == '+' else -numbers[i]
        return ans

# 测试函数
"""
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: "1 + 1"
    print("Test 1: 1+1 =", solution.calculate("1+1"))
    
    # 测试用例2: "2-1 + 2"
    print("Test 2: 2-1+2 =", solution.calculate("2-1+2"))
    
    # 测试用例3: "(1+(4+5+2)-3)+(6+8)"
    print("Test 3: (1+(4+5+2)-3)+(6+8) =", solution.calculate("(1+(4+5+2)-3)+(6+8)"))
    
    # 测试用例4: "2*(5+5*2)/3+(6/2+8)"
    print("Test 4: 2*(5+5*2)/3+(6/2+8) =", solution.calculate("2*(5+5*2)/3+(6/2+8)"))
"""
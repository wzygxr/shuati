"""
移掉K位数字

题目描述：
给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，
使得剩下的数字最小。请你以字符串形式返回这个最小的数字。

测试链接：https://leetcode.cn/problems/remove-k-digits/

解题思路：
使用单调栈来解决这个问题。维护一个单调递增的栈，从左到右遍历数字字符串：
1. 如果当前数字比栈顶数字小，且还有可移除的位数(k>0)，则弹出栈顶数字并减少k
2. 将当前数字入栈
3. 如果遍历完还有剩余的k，从栈顶移除k个数字
4. 处理前导零并返回结果

具体步骤：
1. 创建一个栈用于存储结果数字
2. 遍历字符串中的每个字符
3. 当栈不为空、当前字符小于栈顶字符且k>0时，弹出栈顶元素并减少k
4. 将当前字符入栈（注意避免前导零）
5. 如果遍历完还有剩余的k，从栈顶移除k个数字
6. 将栈中元素构造成字符串并处理特殊情况

时间复杂度分析：
O(n) - 每个元素最多入栈和出栈各一次，n为字符串长度

空间复杂度分析：
O(n) - 栈的空间最多为n

是否为最优解：
是，这是解决该问题的最优解之一
"""


def removeKdigits(num, k):
    """
    移除k位数字使得剩下的数字最小

    Args:
        num: str - 表示非负整数的字符串
        k: int - 要移除的数字位数

    Returns:
        str - 移除k位数字后最小的数字字符串
    """
    # 边界条件检查
    if k >= len(num):
        return "0"
    
    # 使用栈存储结果数字
    stack = []
    
    # 遍历字符串中的每个字符
    for digit in num:
        # 当栈不为空、当前字符小于栈顶字符且还有可移除的位数时
        while stack and k > 0 and stack[-1] > digit:
            stack.pop()  # 弹出栈顶元素
            k -= 1       # 减少可移除位数
        
        # 避免前导零：如果栈为空且当前字符是'0'，则不入栈
        if stack or digit != '0':
            stack.append(digit)
    
    # 如果遍历完还有剩余的k，从栈顶移除k个数字
    while k > 0 and stack:
        stack.pop()
        k -= 1
    
    # 构造结果字符串
    result = ''.join(stack)
    
    # 处理空结果或全零情况
    return result if result else "0"


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    num1 = "1432219"
    k1 = 3
    print(f"测试用例1: num={num1}, k={k1}")
    print(f"输出: {removeKdigits(num1, k1)}")  # 期望输出: "1219"
    
    # 测试用例2
    num2 = "10200"
    k2 = 1
    print(f"测试用例2: num={num2}, k={k2}")
    print(f"输出: {removeKdigits(num2, k2)}")  # 期望输出: "200"
    
    # 测试用例3
    num3 = "10"
    k3 = 2
    print(f"测试用例3: num={num3}, k={k3}")
    print(f"输出: {removeKdigits(num3, k3)}")  # 期望输出: "0"
    
    # 测试用例4
    num4 = "112"
    k4 = 1
    print(f"测试用例4: num={num4}, k={k4}")
    print(f"输出: {removeKdigits(num4, k4)}")  # 期望输出: "11"
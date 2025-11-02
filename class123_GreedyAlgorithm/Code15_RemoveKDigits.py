# 移掉K位数字
# 给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，
# 使得剩下的数字最小。请你以字符串形式返回这个最小的数字。
# 测试链接 : https://leetcode.cn/problems/remove-k-digits/


class Solution:
    """
    贪心算法 + 单调栈解法

    核心思想：
    1. 使用贪心策略，每次尽可能选择小的数字放在高位
    2. 使用单调栈维护当前已选择的数字
    3. 遍历数字，对于每个数字，如果它比栈顶元素小，且还有删除次数，则弹出栈顶元素
    4. 最后如果还有删除次数，从栈顶删除剩余的数字

    时间复杂度：O(n) - n是字符串的长度，每个字符最多入栈和出栈一次
    空间复杂度：O(n) - 单调栈的空间复杂度

    为什么这是最优解？
    1. 贪心策略保证了高位尽可能小，从而得到全局最小的数字
    2. 单调栈的使用使得我们能够高效地维护当前的最优选择
    3. 无法在更少的时间内完成，因为需要处理每个数字

    工程化考虑：
    1. 边界条件处理：空字符串、k=0、k等于字符串长度等
    2. 前导零处理：移除结果字符串开头的零
    3. 空结果处理：如果结果为空，返回"0"

    算法调试技巧：
    1. 可以打印每一步栈的状态来观察算法执行过程
    2. 注意处理各种边界情况
    """

    def removeKdigits(self, num: str, k: int) -> str:
        # 边界条件：如果字符串为空或者k为0，直接返回原字符串
        if not num or k == 0:
            return num

        # 边界条件：如果k等于或大于字符串长度，返回"0"
        if k >= len(num):
            return "0"

        # 使用列表实现单调栈
        stack = []

        # 遍历每个数字
        for digit in num:
            # 当栈不为空，当前数字比栈顶元素小，且还有删除次数时，弹出栈顶元素
            while stack and digit < stack[-1] and k > 0:
                stack.pop()
                k -= 1

            # 将当前数字入栈
            stack.append(digit)

        # 如果还有剩余的删除次数，从栈顶删除
        while k > 0 and stack:
            stack.pop()
            k -= 1

        # 移除前导零
        start_index = 0
        while start_index < len(stack) and stack[start_index] == '0':
            start_index += 1

        # 如果结果为空，返回"0"
        if start_index == len(stack):
            return "0"

        # 构建结果字符串
        return ''.join(stack[start_index:])

    # 优化版本，更简洁的实现
    def removeKdigitsOptimized(self, num: str, k: int) -> str:
        # 边界条件处理
        if k >= len(num):
            return "0"
        if k == 0:
            return num

        # 使用列表作为栈
        stack = []

        # 遍历每个数字
        for c in num:
            # 贪心策略：移除较大的高位数字
            while stack and stack[-1] > c and k > 0:
                stack.pop()
                k -= 1
            stack.append(c)

        # 移除剩余需要删除的数字（从末尾）
        stack = stack[:-k] if k > 0 else stack

        # 移除前导零并处理空结果的情况
        result = ''.join(stack).lstrip('0')
        return result if result else "0"


# 测试函数
def test():
    solution = Solution()

    # 测试用例1: num = "1432219", k = 3 -> "1219"
    num1 = "1432219"
    k1 = 3
    print(f"测试用例1: num = {num1}, k = {k1}")
    print(f"预期结果: 1219, 实际结果: {solution.removeKdigits(num1, k1)}")
    print(f"优化版本结果: {solution.removeKdigitsOptimized(num1, k1)}")
    print()

    # 测试用例2: num = "10200", k = 1 -> "200"
    num2 = "10200"
    k2 = 1
    print(f"测试用例2: num = {num2}, k = {k2}")
    print(f"预期结果: 200, 实际结果: {solution.removeKdigits(num2, k2)}")
    print(f"优化版本结果: {solution.removeKdigitsOptimized(num2, k2)}")
    print()

    # 测试用例3: num = "10", k = 2 -> "0"
    num3 = "10"
    k3 = 2
    print(f"测试用例3: num = {num3}, k = {k3}")
    print(f"预期结果: 0, 实际结果: {solution.removeKdigits(num3, k3)}")
    print(f"优化版本结果: {solution.removeKdigitsOptimized(num3, k3)}")
    print()

    # 测试用例4: num = "10", k = 1 -> "0"
    num4 = "10"
    k4 = 1
    print(f"测试用例4: num = {num4}, k = {k4}")
    print(f"预期结果: 0, 实际结果: {solution.removeKdigits(num4, k4)}")
    print(f"优化版本结果: {solution.removeKdigitsOptimized(num4, k4)}")
    print()

    # 测试用例5: num = "112", k = 1 -> "11"
    num5 = "112"
    k5 = 1
    print(f"测试用例5: num = {num5}, k = {k5}")
    print(f"预期结果: 11, 实际结果: {solution.removeKdigits(num5, k5)}")
    print(f"优化版本结果: {solution.removeKdigitsOptimized(num5, k5)}")
    print()

    # 测试用例6: k=0
    num6 = "12345"
    k6 = 0
    print(f"测试用例6: num = {num6}, k = {k6}")
    print(f"预期结果: 12345, 实际结果: {solution.removeKdigits(num6, k6)}")
    print(f"优化版本结果: {solution.removeKdigitsOptimized(num6, k6)}")


# 运行测试
if __name__ == "__main__":
    test()
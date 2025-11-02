# 移掉K位数字（Remove K Digits）
# 题目来源：LeetCode 402
# 题目链接：https://leetcode.cn/problems/remove-k-digits/
# 
# 问题描述：
# 给定一个以字符串表示的非负整数num，移除这个数中的k位数字，使得剩下的数字最小。
# 
# 算法思路：
# 使用贪心策略，结合单调栈：
# 1. 遍历数字字符串，维护一个单调递增栈
# 2. 当遇到比栈顶小的数字时，弹出栈顶元素（移除数字）
# 3. 直到移除k个数字或栈为空
# 4. 如果遍历完成后还没有移除k个数字，从栈顶继续移除
# 5. 处理前导零和空栈情况
# 
# 时间复杂度：O(n) - 每个元素最多入栈出栈一次
# 空间复杂度：O(n) - 栈的空间
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 数字最小化问题
# 2. 字符串处理问题
# 
# 异常处理：
# 1. 处理k大于等于数字长度的情况
# 2. 处理前导零情况
# 3. 处理空字符串情况
# 
# 工程化考量：
# 1. 输入验证：检查参数是否合法
# 2. 边界条件：处理各种边界情况
# 3. 性能优化：使用列表模拟栈提高效率
# 
# 相关题目：
# 1. LeetCode 321. 拼接最大数 - 类似数字拼接问题
# 2. LeetCode 316. 去除重复字母 - 类似字符处理问题
# 3. LeetCode 738. 单调递增的数字 - 数字单调性问题
# 4. 牛客网 NC140 排序 - 各种排序算法实现
# 5. LintCode 1254. 移除K位数字 - 与本题相同
# 6. HackerRank - Largest Permutation - 最大排列问题
# 7. CodeChef - DIGITREM - 数字移除问题
# 8. AtCoder ABC155D - Pairs - 数字配对问题
# 9. Codeforces 1324C - Frog Jumps - 贪心跳跃问题
# 10. POJ 1700 - Crossing River - 经典过河问题

class Solution:
    """
    移除k位数字使得剩下的数字最小
    
    Args:
        num: str - 数字字符串
        k: int - 要移除的数字个数
    
    Returns:
        str - 移除后的最小数字字符串
    """
    def removeKdigits(self, num: str, k: int) -> str:
        # 边界条件检查
        if not num or k >= len(num):
            return "0"
        
        if k == 0:
            return num  # 不需要移除任何数字
        
        # 使用列表模拟栈
        stack = []
        
        for char in num:
            # 当栈不为空，且k>0，且当前数字小于栈顶数字时，弹出栈顶
            while stack and k > 0 and char < stack[-1]:
                stack.pop()
                k -= 1
            
            # 将当前数字压入栈中
            stack.append(char)
        
        # 如果还有k个数字需要移除，从栈顶移除（因为栈是单调递增的）
        if k > 0:
            stack = stack[:-k]
        
        # 构建结果字符串
        result = ''.join(stack)
        
        # 处理前导零
        result = result.lstrip('0')
        
        # 如果所有数字都是0，返回"0"
        if not result:
            return "0"
        
        return result


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况
    num1 = "1432219"
    k1 = 3
    result1 = solution.removeKdigits(num1, k1)
    print("测试用例1:")
    print(f"输入数字: {num1}")
    print(f"移除位数: {k1}")
    print(f"最小结果: {result1}")
    print("期望输出: 1219")
    print()
    
    # 测试用例2: 简单情况
    num2 = "10200"
    k2 = 1
    result2 = solution.removeKdigits(num2, k2)
    print("测试用例2:")
    print(f"输入数字: {num2}")
    print(f"移除位数: {k2}")
    print(f"最小结果: {result2}")
    print("期望输出: 200")
    print()
    
    # 测试用例3: 复杂情况
    num3 = "10"
    k3 = 2
    result3 = solution.removeKdigits(num3, k3)
    print("测试用例3:")
    print(f"输入数字: {num3}")
    print(f"移除位数: {k3}")
    print(f"最小结果: {result3}")
    print("期望输出: 0")
    print()
    
    # 测试用例4: 边界情况 - 移除所有数字
    num4 = "12345"
    k4 = 5
    result4 = solution.removeKdigits(num4, k4)
    print("测试用例4:")
    print(f"输入数字: {num4}")
    print(f"移除位数: {k4}")
    print(f"最小结果: {result4}")
    print("期望输出: 0")
    print()
    
    # 测试用例5: 边界情况 - 不移除数字
    num5 = "12345"
    k5 = 0
    result5 = solution.removeKdigits(num5, k5)
    print("测试用例5:")
    print(f"输入数字: {num5}")
    print(f"移除位数: {k5}")
    print(f"最小结果: {result5}")
    print("期望输出: 12345")
    print()
    
    # 测试用例6: 复杂情况 - 前导零处理
    num6 = "100200"
    k6 = 1
    result6 = solution.removeKdigits(num6, k6)
    print("测试用例6:")
    print(f"输入数字: {num6}")
    print(f"移除位数: {k6}")
    print(f"最小结果: {result6}")
    print("期望输出: 200")


if __name__ == "__main__":
    main()
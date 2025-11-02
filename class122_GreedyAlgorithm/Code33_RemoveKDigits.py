import time
import random
from typing import List

class Code33_RemoveKDigits:
    """
    移掉K位数字
    
    题目描述：
    给你一个以字符串表示的非负整数 num 和一个整数 k ，移除这个数中的 k 位数字，使得剩下的数字最小。
    请你以字符串形式返回这个最小的数字。
    
    来源：LeetCode 402
    链接：https://leetcode.cn/problems/remove-k-digits/
    
    算法思路：
    使用贪心算法 + 单调栈：
    1. 使用栈来保存最终结果
    2. 遍历字符串中的每个字符：
        - 当栈非空且栈顶元素大于当前字符且还有删除次数时，弹出栈顶元素
        - 将当前字符入栈
    3. 如果遍历完成后还有剩余的删除次数，从栈顶删除（因为栈是单调递增的）
    4. 处理前导零并返回结果
    
    时间复杂度：O(n) - 每个字符最多入栈出栈一次
    空间复杂度：O(n) - 栈的空间
    
    关键点分析：
    - 贪心策略：移除高位较大的数字可以最大化减少数值
    - 单调栈：维护一个单调递增的栈
    - 边界处理：处理前导零和空结果
    
    工程化考量：
    - 输入验证：检查字符串和k的有效性
    - 性能优化：使用列表而非字符串拼接
    - 可读性：清晰的变量命名和注释
    """
    
    @staticmethod
    def remove_k_digits(num: str, k: int) -> str:
        """
        移除k位数字使得剩下的数字最小
        
        Args:
            num: 输入数字字符串
            k: 要移除的数字个数
            
        Returns:
            str: 最小的数字字符串
        """
        # 输入验证
        if not num:
            return "0"
        if k < 0:
            raise ValueError("k必须是非负整数")
        if k >= len(num):
            return "0"
        
        # 使用栈来保存结果
        stack = []
        
        for char in num:
            # 当栈非空且栈顶元素大于当前字符且还有删除次数时，弹出栈顶元素
            while stack and k > 0 and stack[-1] > char:
                stack.pop()
                k -= 1
            
            # 将当前字符入栈（避免前导零）
            if stack or char != '0':
                stack.append(char)
        
        # 如果还有剩余的删除次数，从栈顶删除（因为栈是单调递增的）
        if k > 0:
            stack = stack[:-k]
        
        # 处理空栈的情况
        if not stack:
            return "0"
        
        # 构建结果字符串
        return ''.join(stack)
    
    @staticmethod
    def remove_k_digits_optimized(num: str, k: int) -> str:
        """
        优化版本：使用列表模拟栈，避免字符串拼接
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if not num:
            return "0"
        if k < 0:
            raise ValueError("k必须是非负整数")
        if k >= len(num):
            return "0"
        
        # 使用列表模拟栈
        stack = []
        remaining_k = k
        
        for char in num:
            while stack and remaining_k > 0 and stack[-1] > char:
                stack.pop()
                remaining_k -= 1
            stack.append(char)
        
        # 处理剩余的删除次数
        if remaining_k > 0:
            stack = stack[:len(stack) - remaining_k]
        
        # 去除前导零
        result = ''.join(stack).lstrip('0')
        return result if result else "0"
    
    @staticmethod
    def remove_k_digits_recursive(num: str, k: int) -> str:
        """
        递归解法（用于理解思路）
        时间复杂度：O(C(n, k)) - 组合数，效率较低
        空间复杂度：O(n) - 递归栈深度
        """
        if not num:
            return "0"
        if k < 0:
            raise ValueError("k必须是非负整数")
        if k >= len(num):
            return "0"
        if k == 0:
            # 去除前导零
            result = num.lstrip('0')
            return result if result else "0"
        
        min_num = num
        
        # 尝试移除每一位数字
        for i in range(len(num)):
            # 移除第i位数字
            new_num = num[:i] + num[i+1:]
            result = Code33_RemoveKDigits.remove_k_digits_recursive(new_num, k - 1)
            
            # 比较大小
            if Code33_RemoveKDigits._compare_numeric(result, min_num) < 0:
                min_num = result
        
        return min_num
    
    @staticmethod
    def _compare_numeric(num1: str, num2: str) -> int:
        """
        比较两个数字字符串的大小
        
        Args:
            num1: 第一个数字字符串
            num2: 第二个数字字符串
            
        Returns:
            int: -1表示num1 < num2，0表示相等，1表示num1 > num2
        """
        # 去除前导零
        s1 = num1.lstrip('0') or "0"
        s2 = num2.lstrip('0') or "0"
        
        if len(s1) != len(s2):
            return -1 if len(s1) < len(s2) else 1
        
        if s1 < s2:
            return -1
        elif s1 > s2:
            return 1
        else:
            return 0
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 移掉K位数字测试 ===")
        
        # 测试用例1: num = "1432219", k = 3 -> "1219"
        num1 = "1432219"
        k1 = 3
        print(f"测试用例1: num = \"{num1}\", k = {k1}")
        result1 = Code33_RemoveKDigits.remove_k_digits(num1, k1)
        result2 = Code33_RemoveKDigits.remove_k_digits_optimized(num1, k1)
        print(f"方法1结果: \"{result1}\"")  # "1219"
        print(f"方法2结果: \"{result2}\"")  # "1219"
        
        # 测试用例2: num = "10200", k = 1 -> "200"
        num2 = "10200"
        k2 = 1
        print(f"\n测试用例2: num = \"{num2}\", k = {k2}")
        result1 = Code33_RemoveKDigits.remove_k_digits(num2, k2)
        result2 = Code33_RemoveKDigits.remove_k_digits_optimized(num2, k2)
        print(f"方法1结果: \"{result1}\"")  # "200"
        print(f"方法2结果: \"{result2}\"")  # "200"
        
        # 测试用例3: num = "10", k = 2 -> "0"
        num3 = "10"
        k3 = 2
        print(f"\n测试用例3: num = \"{num3}\", k = {k3}")
        result1 = Code33_RemoveKDigits.remove_k_digits(num3, k3)
        result2 = Code33_RemoveKDigits.remove_k_digits_optimized(num3, k3)
        print(f"方法1结果: \"{result1}\"")  # "0"
        print(f"方法2结果: \"{result2}\"")  # "0"
        
        # 测试用例4: num = "9", k = 1 -> "0"
        num4 = "9"
        k4 = 1
        print(f"\n测试用例4: num = \"{num4}\", k = {k4}")
        result1 = Code33_RemoveKDigits.remove_k_digits(num4, k4)
        result2 = Code33_RemoveKDigits.remove_k_digits_optimized(num4, k4)
        print(f"方法1结果: \"{result1}\"")  # "0"
        print(f"方法2结果: \"{result2}\"")  # "0"
        
        # 测试用例5: num = "123456", k = 3 -> "123"
        num5 = "123456"
        k5 = 3
        print(f"\n测试用例5: num = \"{num5}\", k = {k5}")
        result1 = Code33_RemoveKDigits.remove_k_digits(num5, k5)
        result2 = Code33_RemoveKDigits.remove_k_digits_optimized(num5, k5)
        print(f"方法1结果: \"{result1}\"")  # "123"
        print(f"方法2结果: \"{result2}\"")  # "123"
        
        # 边界测试：k = 0
        num6 = "123"
        k6 = 0
        print(f"\n测试用例6: num = \"{num6}\", k = {k6}")
        result1 = Code33_RemoveKDigits.remove_k_digits(num6, k6)
        result2 = Code33_RemoveKDigits.remove_k_digits_optimized(num6, k6)
        print(f"方法1结果: \"{result1}\"")  # "123"
        print(f"方法2结果: \"{result2}\"")  # "123"
        
        # 边界测试：空字符串
        num7 = ""
        k7 = 0
        print(f"\n测试用例7: num = \"{num7}\", k = {k7}")
        result1 = Code33_RemoveKDigits.remove_k_digits(num7, k7)
        result2 = Code33_RemoveKDigits.remove_k_digits_optimized(num7, k7)
        print(f"方法1结果: \"{result1}\"")  # "0"
        print(f"方法2结果: \"{result2}\"")  # "0"
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        large_num = ''.join(str(random.randint(0, 9)) for _ in range(10000))
        k = 500
        
        print("\n=== 性能测试 ===")
        
        start_time1 = time.time()
        result1 = Code33_RemoveKDigits.remove_k_digits(large_num, k)
        end_time1 = time.time()
        print(f"方法1执行时间: {(end_time1 - start_time1) * 1000:.2f} 毫秒")
        print(f"结果长度: {len(result1)}")
        
        start_time2 = time.time()
        result2 = Code33_RemoveKDigits.remove_k_digits_optimized(large_num, k)
        end_time2 = time.time()
        print(f"方法2执行时间: {(end_time2 - start_time2) * 1000:.2f} 毫秒")
        print(f"结果长度: {len(result2)}")
        
        # 验证结果一致性
        print(f"结果一致性: {result1 == result2}")
    
    @staticmethod
    def analyze_complexity():
        """算法复杂度分析"""
        print("\n=== 算法复杂度分析 ===")
        print("方法1（单调栈）:")
        print("- 时间复杂度: O(n)")
        print("  - 每个字符最多入栈出栈一次")
        print("  - 总体线性时间复杂度")
        print("- 空间复杂度: O(n)")
        print("  - 栈的空间: O(n)")
        
        print("\n方法2（优化版本）:")
        print("- 时间复杂度: O(n)")
        print("  - 每个字符处理一次")
        print("  - 列表操作效率高")
        print("- 空间复杂度: O(n)")
        print("  - 列表空间: O(n)")
        
        print("\n方法3（递归）:")
        print("- 时间复杂度: O(C(n, k))")
        print("  - 组合数复杂度，指数级")
        print("  - 仅用于理解思路，不适用于实际应用")
        print("- 空间复杂度: O(n)")
        print("  - 递归栈深度: O(n)")
        
        print("\n贪心策略证明:")
        print("1. 高位数字对数值影响更大，优先移除高位较大的数字")
        print("2. 单调栈确保每次移除的都是当前最优选择")
        print("3. 数学归纳法证明贪心选择性质")
        
        print("\n工程化考量:")
        print("1. 输入验证：处理非法输入和边界情况")
        print("2. 性能优化：选择高效的数据结构")
        print("3. 可读性：清晰的算法逻辑和注释")
        print("4. 测试覆盖：全面的测试用例设计")

def main():
    """主函数"""
    Code33_RemoveKDigits.run_tests()
    Code33_RemoveKDigits.performance_test()
    Code33_RemoveKDigits.analyze_complexity()

if __name__ == "__main__":
    main()
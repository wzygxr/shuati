import time
import random
from typing import List

class Code18_MinimumRemoveToMakeValidParentheses:
    """
    使括号有效的最少删除 - Python实现
    
    题目描述：
    给你一个由 '('、')' 和小写字母组成的字符串 s。
    你需要从字符串中删除最少数目的 '(' 或者 ')'（可以删除任意位置的括号)，使得剩下的「括号字符串」有效。
    请返回任意一个合法字符串。
    
    测试链接：https://leetcode.cn/problems/minimum-remove-to-make-valid-parentheses/
    题目来源：LeetCode
    难度：中等
    
    核心算法：栈 + 标记删除
    
    解题思路：
    1. 使用栈来记录左括号的位置
    2. 遍历字符串，遇到左括号入栈，遇到右括号时：
        - 如果栈不为空，弹出栈顶（匹配成功）
        - 如果栈为空，标记这个右括号需要删除
    3. 遍历结束后，栈中剩余的左括号都需要删除
    4. 根据标记构建结果字符串
    
    时间复杂度分析：
    O(n) - 需要遍历字符串两次，n为字符串长度
    
    空间复杂度分析：
    O(n) - 栈的空间最多为n，标记数组需要n空间
    
    Python语言特性：
    - 使用列表模拟栈操作
    - 使用列表推导式构建结果字符串
    - 使用生成器表达式提高内存效率
    - 使用类型注解提高代码可读性
    """
    
    @staticmethod
    def min_remove_to_make_valid(s: str) -> str:
        """
        主解法：使用栈和标记数组
        
        Args:
            s: 输入字符串
            
        Returns:
            有效的括号字符串
        """
        if not s:
            return s
        
        n = len(s)
        # 标记数组：True表示保留，False表示删除
        keep = [True] * n
        
        # 使用栈记录左括号的位置
        stack = []
        
        # 第一次遍历：标记需要删除的括号
        for i, char in enumerate(s):
            if char == '(':
                # 左括号入栈，暂时标记为保留
                stack.append(i)
            elif char == ')':
                if stack:
                    # 有匹配的左括号，弹出栈顶
                    stack.pop()
                else:
                    # 没有匹配的左括号，这个右括号需要删除
                    keep[i] = False
            # 字母字符保持保留状态
        
        # 栈中剩余的左括号都需要删除
        for index in stack:
            keep[index] = False
        
        # 构建结果字符串
        result = ''.join(s[i] for i in range(n) if keep[i])
        return result
    
    @staticmethod
    def min_remove_to_make_valid_optimized(s: str) -> str:
        """
        优化解法：两次遍历法（空间优化）
        第一次遍历：删除多余的右括号
        第二次遍历：删除多余的左括号
        
        Args:
            s: 输入字符串
            
        Returns:
            有效的括号字符串
        """
        if not s:
            return s
        
        # 第一次遍历：删除多余的右括号
        first_pass = []
        balance = 0  # 括号平衡计数器
        
        for char in s:
            if char == '(':
                balance += 1
                first_pass.append(char)
            elif char == ')':
                if balance > 0:
                    balance -= 1
                    first_pass.append(char)
                # 如果balance <= 0，不添加这个右括号
            else:
                first_pass.append(char)
        
        # 第二次遍历：删除多余的左括号（从右向左）
        result = []
        remove_left = balance  # 需要删除的左括号数量
        
        for i in range(len(first_pass) - 1, -1, -1):
            char = first_pass[i]
            if char == '(' and remove_left > 0:
                remove_left -= 1
            else:
                result.append(char)
        
        # 反转结果字符串
        result.reverse()
        return ''.join(result)
    
    @staticmethod
    def test_min_remove_to_make_valid():
        """单元测试函数"""
        print("=== 使括号有效的最少删除单元测试 ===")
        
        # 测试用例1：常规情况
        s1 = "lee(t(c)o)de)"
        result1 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid(s1)
        print(f"测试用例1: {s1}")
        print(f"输出: {result1}")
        print("期望: lee(t(c)o)de 或 lee(t(co)de) 或 lee(t(c)ode)")
        
        # 测试用例2：需要删除多个括号
        s2 = "a)b(c)d"
        result2 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid(s2)
        print(f"\n测试用例2: {s2}")
        print(f"输出: {result2}")
        print("期望: ab(c)d")
        
        # 测试用例3：删除所有括号
        s3 = "))(("
        result3 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid(s3)
        print(f"\n测试用例3: {s3}")
        print(f"输出: {result3}")
        print("期望: \"\" (空字符串)")
        
        # 测试用例4：已经是有效括号
        s4 = "(a(b(c)d))"
        result4 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid(s4)
        print(f"\n测试用例4: {s4}")
        print(f"输出: {result4}")
        print("期望: (a(b(c)d))")
        
        # 测试用例5：纯字母字符串
        s5 = "abcdefg"
        result5 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid(s5)
        print(f"\n测试用例5: {s5}")
        print(f"输出: {result5}")
        print("期望: abcdefg")
        
        # 测试用例6：边界情况 - 空字符串
        s6 = ""
        result6 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid(s6)
        print(f"\n测试用例6: 空字符串")
        print(f"输出: {result6}")
        print("期望: \"\"")
    
    @staticmethod
    def performance_comparison():
        """性能对比测试：标记数组法 vs 两次遍历法"""
        print("\n=== 性能对比测试 ===")
        
        # 生成测试数据
        n = 100000
        test_data = []
        
        # 生成包含括号和字母的混合字符串
        for _ in range(n):
            char_type = random.randint(0, 2)
            if char_type == 0:
                test_data.append('(')
            elif char_type == 1:
                test_data.append(')')
            else:
                test_data.append(chr(ord('a') + random.randint(0, 25)))
        
        test_string = ''.join(test_data)
        
        # 测试标记数组法
        start_time1 = time.time()
        result1 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid(test_string)
        end_time1 = time.time()
        
        # 测试两次遍历法
        start_time2 = time.time()
        result2 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid_optimized(test_string)
        end_time2 = time.time()
        
        time1 = (end_time1 - start_time1) * 1000
        time2 = (end_time2 - start_time2) * 1000
        
        print(f"数据规模: {n}个字符")
        print(f"标记数组法执行时间: {time1:.2f}ms")
        print(f"两次遍历法执行时间: {time2:.2f}ms")
        print(f"结果长度对比: {len(result1)} vs {len(result2)}")
        print(f"结果是否相等: {result1 == result2}")
    
    @staticmethod
    def correctness_verification():
        """正确性验证：验证两种解法结果是否一致"""
        print("\n=== 正确性验证 ===")
        
        test_cases = [
            "lee(t(c)o)de)",
            "a)b(c)d",
            "))((",
            "(a(b(c)d))",
            "abcdefg",
            "",
            "((a)(b)((c)))d))",
            "()()()()",
            "(((((())))))",
            "))))((((()"
        ]
        
        all_passed = True
        for i, test_case in enumerate(test_cases):
            result1 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid(test_case)
            result2 = Code18_MinimumRemoveToMakeValidParentheses.min_remove_to_make_valid_optimized(test_case)
            
            is_valid1 = Code18_MinimumRemoveToMakeValidParentheses.is_valid_parentheses(result1)
            is_valid2 = Code18_MinimumRemoveToMakeValidParentheses.is_valid_parentheses(result2)
            results_equal = (result1 == result2)
            
            if not is_valid1 or not is_valid2 or not results_equal:
                print(f"测试用例 {i} 失败:")
                print(f"输入: {test_case}")
                print(f"解法1结果: {result1} (有效: {is_valid1})")
                print(f"解法2结果: {result2} (有效: {is_valid2})")
                print(f"结果相等: {results_equal}")
                all_passed = False
        
        if all_passed:
            print("所有测试用例通过！")
    
    @staticmethod
    def is_valid_parentheses(s: str) -> bool:
        """
        验证括号字符串是否有效
        
        Args:
            s: 要验证的字符串
            
        Returns:
            如果括号有效返回True，否则返回False
        """
        balance = 0
        for char in s:
            if char == '(':
                balance += 1
            elif char == ')':
                balance -= 1
                if balance < 0:
                    return False
        return balance == 0
    
    @staticmethod
    def run():
        """主运行函数"""
        # 运行单元测试
        Code18_MinimumRemoveToMakeValidParentheses.test_min_remove_to_make_valid()
        
        # 运行性能对比测试
        Code18_MinimumRemoveToMakeValidParentheses.performance_comparison()
        
        # 运行正确性验证
        Code18_MinimumRemoveToMakeValidParentheses.correctness_verification()
        
        print("\n=== 算法验证完成 ===")


# 程序入口点
if __name__ == "__main__":
    Code18_MinimumRemoveToMakeValidParentheses.run()
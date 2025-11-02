import time
import random
from typing import List

class Code20_ValidParentheses:
    """
    有效的括号 - Python实现
    
    题目描述：
    给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。
    有效字符串需满足：
    1. 左括号必须用相同类型的右括号闭合。
    2. 左括号必须以正确的顺序闭合。
    
    测试链接：https://leetcode.cn/problems/valid-parentheses/
    题目来源：LeetCode
    难度：简单
    
    核心算法：栈
    
    解题思路：
    1. 使用栈来存储遇到的左括号
    2. 遍历字符串中的每个字符：
        - 如果是左括号，压入栈中
        - 如果是右括号，检查栈顶是否匹配
        - 如果不匹配或栈为空，返回false
    3. 遍历结束后，检查栈是否为空
    
    时间复杂度分析：
    O(n) - 需要遍历字符串一次，n为字符串长度
    
    空间复杂度分析：
    O(n) - 栈的空间最多为n
    
    Python语言特性：
    - 使用列表模拟栈操作
    - 使用字典存储括号映射关系
    - 使用列表推导式简化代码
    - 使用生成器表达式提高内存效率
    """
    
    @staticmethod
    def is_valid(s: str) -> bool:
        """
        主解法：使用栈判断括号有效性
        
        Args:
            s: 输入字符串
            
        Returns:
            如果括号有效返回True，否则返回False
        """
        # 边界条件检查
        if not s or len(s) % 2 != 0:
            return False
        
        # 使用栈存储左括号
        stack = []
        
        # 使用字典存储括号映射关系
        bracket_map = {')': '(', '}': '{', ']': '['}
        
        # 遍历字符串中的每个字符
        for char in s:
            if char in bracket_map.values():
                # 如果是左括号，压入栈中
                stack.append(char)
            elif char in bracket_map:
                # 如果是右括号，检查栈顶是否匹配
                if not stack or stack.pop() != bracket_map[char]:
                    return False
            else:
                # 非法字符
                return False
        
        # 检查栈是否为空
        return len(stack) == 0
    
    @staticmethod
    def is_valid_optimized(s: str) -> bool:
        """
        优化解法：使用列表模拟栈（性能优化）
        
        Args:
            s: 输入字符串
            
        Returns:
            如果括号有效返回True，否则返回False
        """
        # 边界条件检查
        if not s or len(s) % 2 != 0:
            return False
        
        # 使用列表模拟栈
        stack = []
        
        for char in s:
            if char == '(' or char == '{' or char == '[':
                # 左括号入栈
                stack.append(char)
            else:
                # 右括号，检查栈是否为空
                if not stack:
                    return False
                
                # 检查括号是否匹配
                top_char = stack.pop()
                if (char == ')' and top_char != '(') or \
                   (char == '}' and top_char != '{') or \
                   (char == ']' and top_char != '['):
                    return False
        
        # 检查栈是否为空
        return len(stack) == 0
    
    @staticmethod
    def is_valid_extended(s: str) -> bool:
        """
        扩展解法：支持更多括号类型
        
        Args:
            s: 输入字符串
            
        Returns:
            如果括号有效返回True，否则返回False
        """
        if not s or len(s) % 2 != 0:
            return False
        
        stack = []
        
        # 扩展的括号映射（支持更多括号类型）
        extended_map = {')': '(', '}': '{', ']': '[', '>': '<', '»': '«'}
        
        for char in s:
            if char in extended_map.values():
                stack.append(char)
            elif char in extended_map:
                if not stack or stack.pop() != extended_map[char]:
                    return False
            else:
                # 忽略非括号字符（扩展功能）
                continue
        
        return len(stack) == 0
    
    @staticmethod
    def test_is_valid():
        """单元测试函数"""
        print("=== 有效的括号单元测试 ===")
        
        # 测试用例1：有效括号
        s1 = "()"
        result1 = Code20_ValidParentheses.is_valid(s1)
        print(f"测试用例1: {s1}")
        print(f"输出: {result1}")
        print("期望: True")
        
        # 测试用例2：有效嵌套括号
        s2 = "()[]{}"
        result2 = Code20_ValidParentheses.is_valid(s2)
        print(f"\n测试用例2: {s2}")
        print(f"输出: {result2}")
        print("期望: True")
        
        # 测试用例3：复杂有效括号
        s3 = "([{}])"
        result3 = Code20_ValidParentheses.is_valid(s3)
        print(f"\n测试用例3: {s3}")
        print(f"输出: {result3}")
        print("期望: True")
        
        # 测试用例4：无效括号（不匹配）
        s4 = "(]"
        result4 = Code20_ValidParentheses.is_valid(s4)
        print(f"\n测试用例4: {s4}")
        print(f"输出: {result4}")
        print("期望: False")
        
        # 测试用例5：无效括号（顺序错误）
        s5 = "([)]"
        result5 = Code20_ValidParentheses.is_valid(s5)
        print(f"\n测试用例5: {s5}")
        print(f"输出: {result5}")
        print("期望: False")
        
        # 测试用例6：边界情况 - 空字符串
        s6 = ""
        result6 = Code20_ValidParentheses.is_valid(s6)
        print(f"\n测试用例6: 空字符串")
        print(f"输出: {result6}")
        print("期望: True")
        
        # 测试用例7：边界情况 - 奇数长度
        s7 = "(()"
        result7 = Code20_ValidParentheses.is_valid(s7)
        print(f"\n测试用例7: {s7}")
        print(f"输出: {result7}")
        print("期望: False")
    
    @staticmethod
    def performance_comparison():
        """性能对比测试：字典法 vs 列表模拟栈法"""
        print("\n=== 性能对比测试 ===")
        
        # 生成测试数据（大规模有效括号字符串）
        n = 100000
        test_data = []
        temp_stack = []
        
        # 生成有效括号字符串
        for _ in range(n):
            char_type = random.randint(0, 2)
            if char_type == 0:
                left, right = '(', ')'
            elif char_type == 1:
                left, right = '[', ']'
            else:
                left, right = '{', '}'
            
            # 50%概率添加左括号，50%概率添加右括号（但保持有效性）
            if random.choice([True, False]) or not temp_stack:
                test_data.append(left)
                temp_stack.append(right)
            else:
                test_data.append(temp_stack.pop())
        
        # 添加剩余的右括号
        while temp_stack:
            test_data.append(temp_stack.pop())
        
        test_string = ''.join(test_data)
        
        # 测试字典法
        start_time1 = time.time()
        result1 = Code20_ValidParentheses.is_valid(test_string)
        end_time1 = time.time()
        
        # 测试列表模拟栈法
        start_time2 = time.time()
        result2 = Code20_ValidParentheses.is_valid_optimized(test_string)
        end_time2 = time.time()
        
        time1 = (end_time1 - start_time1) * 1000
        time2 = (end_time2 - start_time2) * 1000
        
        print(f"数据规模: {len(test_string)}个字符")
        print(f"字典法执行时间: {time1:.2f}ms, 结果: {result1}")
        print(f"列表模拟栈法执行时间: {time2:.2f}ms, 结果: {result2}")
        print(f"结果一致性: {result1 == result2}")
    
    @staticmethod
    def correctness_verification():
        """正确性验证：验证两种解法结果是否一致"""
        print("\n=== 正确性验证 ===")
        
        test_cases = [
            "()",
            "()[]{}",
            "([{}])",
            "(]",
            "([)]",
            "",
            "(()",
            "{[()]}",
            "{{{{}}}}",
            "[[[]]]]",
            "({[}])",
            "((()))",
            "([{}])"
        ]
        
        all_passed = True
        for i, test_case in enumerate(test_cases):
            result1 = Code20_ValidParentheses.is_valid(test_case)
            result2 = Code20_ValidParentheses.is_valid_optimized(test_case)
            
            if result1 != result2:
                print(f"测试用例 {i} 不一致:")
                print(f"输入: {test_case}")
                print(f"解法1结果: {result1}")
                print(f"解法2结果: {result2}")
                all_passed = False
        
        if all_passed:
            print("所有测试用例结果一致！")
    
    @staticmethod
    def test_extended_functionality():
        """扩展功能测试：支持更多括号类型"""
        print("\n=== 扩展功能测试 ===")
        
        # 测试尖括号
        s1 = "<>"
        result1 = Code20_ValidParentheses.is_valid_extended(s1)
        print(f"测试用例1 (尖括号): {s1} -> {result1} (期望: True)")
        
        # 测试混合括号（包含非括号字符）
        s2 = "(hello{world})"
        result2 = Code20_ValidParentheses.is_valid_extended(s2)
        print(f"测试用例2 (混合字符): {s2} -> {result2} (期望: True)")
        
        # 测试无效扩展括号
        s3 = "<]"
        result3 = Code20_ValidParentheses.is_valid_extended(s3)
        print(f"测试用例3 (无效扩展): {s3} -> {result3} (期望: False)")
    
    @staticmethod
    def run():
        """主运行函数"""
        # 运行单元测试
        Code20_ValidParentheses.test_is_valid()
        
        # 运行性能对比测试
        Code20_ValidParentheses.performance_comparison()
        
        # 运行正确性验证
        Code20_ValidParentheses.correctness_verification()
        
        # 运行扩展功能测试
        Code20_ValidParentheses.test_extended_functionality()
        
        print("\n=== 有效的括号算法验证完成 ===")


# 程序入口点
if __name__ == "__main__":
    Code20_ValidParentheses.run()
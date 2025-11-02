"""
链表相加及相关题目扩展 - Python实现
包含LeetCode、LintCode、牛客网、剑指Offer等多个平台的相关题目
每个题目都提供详细的解题思路、复杂度分析、多种解法

主要题目：
1. LeetCode 2. 两数相加 (基础题)
2. LeetCode 445. 两数相加 II (进阶题)
3. LeetCode 369. 给单链表加一 (变种题)
4. LeetCode 66. 加一 (数组形式)
5. LeetCode 989. 数组形式的整数加法
6. LeetCode 415. 字符串相加
7. LeetCode 67. 二进制求和
8. 牛客网 BM86 大数加法
9. 牛客网 NC40 链表相加（二）
10. LintCode 165. 合并两个排序链表
11. 剑指Offer 06. 从尾到头打印链表
12. HackerRank BigInteger Addition
13. LeetCode 43. 字符串相乘
14. LeetCode 371. 两整数之和
15. LeetCode 258. 各位相加
"""

from typing import List, Optional

# 链表节点定义
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

# 工具函数：创建链表
def create_list(arr):
    if not arr:
        return None
    head = ListNode(arr[0])
    cur = head
    for i in range(1, len(arr)):
        cur.next = ListNode(arr[i])
        cur = cur.next
    return head

# 工具函数：打印链表
def print_list(head):
    cur = head
    result = []
    while cur:
        result.append(str(cur.val))
        cur = cur.next
    print(" -> ".join(result) if result else "")

class AddTwoNumbersSolution:
    """
    题目1: LeetCode 2. 两数相加 (Add Two Numbers)
    来源: LeetCode
    链接: https://leetcode.cn/problems/add-two-numbers/
    难度: Medium
    
    时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
    空间复杂度: O(1) - 不考虑返回结果的空间
    是否最优解: 是
    
    解题思路：
    1. 同时遍历两个链表，逐位相加
    2. 使用carry变量记录进位
    3. 处理不同长度链表
    4. 处理最后的进位
    """
    
    @staticmethod
    def add_two_numbers(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
        dummy = ListNode(0)
        current = dummy
        carry = 0
        
        while l1 or l2:
            x = l1.val if l1 else 0
            y = l2.val if l2 else 0
            
            sum_val = x + y + carry
            carry = sum_val // 10
            
            current.next = ListNode(sum_val % 10)
            current = current.next
            
            if l1:
                l1 = l1.next
            if l2:
                l2 = l2.next
        
        if carry > 0:
            current.next = ListNode(carry)
        
        return dummy.next
    
    @staticmethod
    def test():
        print("=== 两数相加测试 ===")
        
        l1 = create_list([2, 4, 3])
        l2 = create_list([5, 6, 4])
        print("链表1 (342): ", end="")
        print_list(l1)
        print("链表2 (465): ", end="")
        print_list(l2)
        
        result = AddTwoNumbersSolution.add_two_numbers(l1, l2)
        print("结果 (807): ", end="")
        print_list(result)
        print()

class AddTwoNumbersIISolution:
    """
    题目2: LeetCode 445. 两数相加 II (Add Two Numbers II)
    来源: LeetCode
    链接: https://leetcode.cn/problems/add-two-numbers-ii/
    难度: Medium
    
    时间复杂度: O(max(m,n))
    空间复杂度: O(m+n) - 栈的空间
    是否最优解: 是（如果不允许修改原链表）
    
    解题思路：
    1. 使用两个栈存储链表的值
    2. 从栈顶开始相加，处理进位
    3. 使用头插法构建结果链表
    """
    
    @staticmethod
    def add_two_numbers(l1: Optional[ListNode], l2: Optional[ListNode]) -> Optional[ListNode]:
        stack1, stack2 = [], []
        
        while l1:
            stack1.append(l1.val)
            l1 = l1.next
        
        while l2:
            stack2.append(l2.val)
            l2 = l2.next
        
        head = None
        carry = 0
        
        while stack1 or stack2 or carry:
            x = stack1.pop() if stack1 else 0
            y = stack2.pop() if stack2 else 0
            
            sum_val = x + y + carry
            carry = sum_val // 10
            
            node = ListNode(sum_val % 10)
            node.next = head
            head = node
        
        return head
    
    @staticmethod
    def test():
        print("=== 两数相加 II 测试 ===")
        
        l1 = create_list([7, 2, 4, 3])
        l2 = create_list([5, 6, 4])
        print("链表1 (7243): ", end="")
        print_list(l1)
        print("链表2 (564): ", end="")
        print_list(l2)
        
        result = AddTwoNumbersIISolution.add_two_numbers(l1, l2)
        print("结果 (7807): ", end="")
        print_list(result)
        print()

class PlusOneSolution:
    """
    题目4: LeetCode 66. 加一 (Plus One)
    来源: LeetCode
    链接: https://leetcode.cn/problems/plus-one/
    难度: Easy
    
    时间复杂度: O(n)
    空间复杂度: O(1) 或 O(n) - 最坏情况需要创建新数组
    是否最优解: 是
    
    解题思路：
    1. 从数组末尾开始加一
    2. 处理进位
    3. 如果最高位还有进位，创建新数组
    """
    
    @staticmethod
    def plus_one(digits: List[int]) -> List[int]:
        for i in range(len(digits) - 1, -1, -1):
            digits[i] += 1
            
            if digits[i] < 10:
                return digits
            
            digits[i] = 0
        
        return [1] + digits
    
    @staticmethod
    def test():
        print("=== 加一测试 ===")
        
        digits = [1, 2, 3]
        print(f"数组: {digits}")
        result = PlusOneSolution.plus_one(digits[:])
        print(f"结果: {result}")
        print()

class AddStringsSolution:
    """
    题目6: LeetCode 415. 字符串相加 (Add Strings)
    来源: LeetCode
    链接: https://leetcode.cn/problems/add-strings/
    难度: Easy
    
    时间复杂度: O(max(m,n))
    空间复杂度: O(max(m,n))
    是否最优解: 是
    
    解题思路：
    1. 从字符串末尾开始相加
    2. 处理进位
    3. 反转结果字符串
    """
    
    @staticmethod
    def add_strings(num1: str, num2: str) -> str:
        result = []
        carry = 0
        i, j = len(num1) - 1, len(num2) - 1
        
        while i >= 0 or j >= 0 or carry:
            x = int(num1[i]) if i >= 0 else 0
            y = int(num2[j]) if j >= 0 else 0
            
            sum_val = x + y + carry
            carry = sum_val // 10
            
            result.append(str(sum_val % 10))
            
            i -= 1
            j -= 1
        
        return ''.join(reversed(result))
    
    @staticmethod
    def test():
        print("=== 字符串相加测试 ===")
        
        num1, num2 = "11", "123"
        print(f"字符串1: {num1}, 字符串2: {num2}")
        result = AddStringsSolution.add_strings(num1, num2)
        print(f"结果: {result}")
        print()

class AddBinarySolution:
    """
    题目7: LeetCode 67. 二进制求和 (Add Binary)
    来源: LeetCode
    链接: https://leetcode.cn/problems/add-binary/
    难度: Easy
    
    时间复杂度: O(max(m,n))
    空间复杂度: O(max(m,n))
    是否最优解: 是
    
    解题思路：
    1. 从字符串末尾开始相加
    2. 处理进位（逢二进一）
    3. 反转结果字符串
    """
    
    @staticmethod
    def add_binary(a: str, b: str) -> str:
        result = []
        carry = 0
        i, j = len(a) - 1, len(b) - 1
        
        while i >= 0 or j >= 0 or carry:
            x = int(a[i]) if i >= 0 else 0
            y = int(b[j]) if j >= 0 else 0
            
            sum_val = x + y + carry
            carry = sum_val // 2
            
            result.append(str(sum_val % 2))
            
            i -= 1
            j -= 1
        
        return ''.join(reversed(result))
    
    @staticmethod
    def test():
        print("=== 二进制求和测试 ===")
        
        a, b = "11", "1"
        print(f"二进制1: {a}, 二进制2: {b}")
        result = AddBinarySolution.add_binary(a, b)
        print(f"结果: {result}")
        print()

class MultiplyStringsSolution:
    """
    题目13: LeetCode 43. 字符串相乘 (Multiply Strings)
    来源: LeetCode
    链接: https://leetcode.cn/problems/multiply-strings/
    难度: Medium
    
    时间复杂度: O(m*n)
    空间复杂度: O(m+n)
    是否最优解: 是
    
    解题思路：
    1. num1[i] * num2[j] 的结果位于 result[i+j] 和 result[i+j+1]
    2. 从右往左逐位相乘，累加到对应位置
    3. 处理进位
    4. 去除前导零
    """
    
    @staticmethod
    def multiply(num1: str, num2: str) -> str:
        if num1 == "0" or num2 == "0":
            return "0"
        
        m, n = len(num1), len(num2)
        result = [0] * (m + n)
        
        for i in range(m - 1, -1, -1):
            digit1 = int(num1[i])
            for j in range(n - 1, -1, -1):
                digit2 = int(num2[j])
                product = digit1 * digit2
                sum_val = product + result[i + j + 1]
                result[i + j + 1] = sum_val % 10
                result[i + j] += sum_val // 10
        
        # 去除前导零
        result_str = ''.join(map(str, result))
        return result_str.lstrip('0') or '0'
    
    @staticmethod
    def test():
        print("=== 字符串相乘测试 ===")
        
        num1, num2 = "123", "456"
        print(f"字符串1: {num1}, 字符串2: {num2}")
        result = MultiplyStringsSolution.multiply(num1, num2)
        print(f"结果: {result}")
        print()

class SumOfTwoIntegersSolution:
    """
    题目14: LeetCode 371. 两整数之和 (Sum of Two Integers)
    来源: LeetCode
    链接: https://leetcode.cn/problems/sum-of-two-integers/
    难度: Medium
    
    时间复杂度: O(1)
    空间复杂度: O(1)
    是否最优解: 是
    
    解题思路：
    1. 使用异或运算得到不含进位的和
    2. 使用与运算和左移得到进位
    3. 重复直到进位为0
    
    注意：Python的整数是无限精度的，需要特殊处理
    """
    
    @staticmethod
    def get_sum(a: int, b: int) -> int:
        # Python整数无限精度，需要使用掩码限制在32位
        mask = 0xFFFFFFFF
        
        while b != 0:
            # 计算不含进位的和
            sum_val = (a ^ b) & mask
            # 计算进位
            carry = ((a & b) << 1) & mask
            a = sum_val
            b = carry
        
        # 如果a的最高位是1，说明是负数，需要转换
        return a if a <= 0x7FFFFFFF else ~(a ^ mask)
    
    @staticmethod
    def test():
        print("=== 两整数之和测试 ===")
        
        a1, b1 = 1, 2
        print(f"{a1} + {b1} = {SumOfTwoIntegersSolution.get_sum(a1, b1)}")
        
        a2, b2 = 5, 3
        print(f"{a2} + {b2} = {SumOfTwoIntegersSolution.get_sum(a2, b2)}")
        print()

class AddDigitsSolution:
    """
    题目15: LeetCode 258. 各位相加 (Add Digits)
    来源: LeetCode
    链接: https://leetcode.cn/problems/add-digits/
    难度: Easy
    
    时间复杂度: O(1) - 使用数学公式
    空间复杂度: O(1)
    是否最优解: 是
    
    解题思路：
    1. 模拟法：不断计算各位数字之和，直到结果为一位数
    2. 数学法：使用数根公式 dr(n) = 1 + ((n - 1) % 9)
    
    数学原理：
    数根（digital root）的性质：一个数对9取余的结果等于它各位数字之和对9取余的结果
    """
    
    @staticmethod
    def add_digits(num: int) -> int:
        """模拟法"""
        while num >= 10:
            sum_val = 0
            while num > 0:
                sum_val += num % 10
                num //= 10
            num = sum_val
        return num
    
    @staticmethod
    def add_digits_optimal(num: int) -> int:
        """数学法（数根公式）- 最优解"""
        return 0 if num == 0 else 1 + (num - 1) % 9
    
    @staticmethod
    def test():
        print("=== 各位相加测试 ===")
        
        num = 38
        print(f"模拟法: {num} -> {AddDigitsSolution.add_digits(num)}")
        print(f"数学法: {num} -> {AddDigitsSolution.add_digits_optimal(num)}")
        print()

class AdditiveNumberSolution:
    """
    题目16: LeetCode 306. 累加数 (Additive Number)
    来源: LeetCode
    链接: https://leetcode.cn/problems/additive-number/
    难度: Medium
    
    时间复杂度: O(n^3)
    空间复杂度: O(n)
    是否最优解: 是
    """
    
    @staticmethod
    def is_additive_number(num: str) -> bool:
        n = len(num)
        for i in range(1, n // 2 + 1):
            if num[0] == '0' and i > 1:
                break
            for j in range(i + 1, n):
                if num[i] == '0' and j - i > 1:
                    break
                if n - j < max(i, j - i):
                    break
                if AdditiveNumberSolution._is_valid(num[:i], num[i:j], j, num):
                    return True
        return False
    
    @staticmethod
    def _is_valid(num1: str, num2: str, start: int, num: str) -> bool:
        if start == len(num):
            return True
        sum_str = AddStringsSolution.add_strings(num1, num2)
        if not num.startswith(sum_str, start):
            return False
        return AdditiveNumberSolution._is_valid(num2, sum_str, start + len(sum_str), num)
    
    @staticmethod
    def test():
        print("=== 累加数测试 ===")
        
        num1 = "112358"
        print(f"字符串: {num1} -> {AdditiveNumberSolution.is_additive_number(num1)}")
        
        num2 = "199100199"
        print(f"字符串: {num2} -> {AdditiveNumberSolution.is_additive_number(num2)}")
        print()

class DoubleLinkedListNumberSolution:
    """
    题目17: LeetCode 2816. 翻倍以链表形式表示的数字
    来源: LeetCode
    链接: https://leetcode.cn/problems/double-a-number-represented-as-a-linked-list/
    难度: Medium
    
    时间复杂度: O(n)
    空间复杂度: O(1)
    是否最优解: 是
    """
    
    @staticmethod
    def double_it(head: Optional[ListNode]) -> Optional[ListNode]:
        # 反转链表
        head = DoubleLinkedListNumberSolution._reverse(head)
        
        # 翻倍并处理进位
        cur = head
        carry = 0
        prev = None
        
        while cur:
            doubled = cur.val * 2 + carry
            cur.val = doubled % 10
            carry = doubled // 10
            prev = cur
            cur = cur.next
        
        # 处理最后的进位
        if carry > 0 and prev is not None:
            prev.next = ListNode(carry)
        
        # 再次反转链表
        return DoubleLinkedListNumberSolution._reverse(head)
    
    @staticmethod
    def _reverse(head: Optional[ListNode]) -> Optional[ListNode]:
        prev = None
        cur = head
        while cur:
            next_node = cur.next
            cur.next = prev
            prev = cur
            cur = next_node
        return prev
    
    @staticmethod
    def test():
        print("=== 翻倍链表数字测试 ===")
        
        head1 = create_list([1, 8, 9])
        print("链表 (189): ", end="")
        print_list(head1)
        
        result1 = DoubleLinkedListNumberSolution.double_it(head1)
        print("结果 (378): ", end="")
        print_list(result1)
        print()

class GoodArraySolution:
    """
    题目18: Codeforces 1077C - Good Array
    来源: Codeforces
    链接: https://codeforces.com/problemset/problem/1077/C
    难度: Easy
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    是否最优解: 是
    """
    
    @staticmethod
    def good_array(arr: List[int]) -> List[int]:
        result = []
        if not arr:
            return result
        
        # 计算总和
        total_sum = sum(arr)
        
        # 找到最大值和次大值
        max1 = float('-inf')
        max2 = float('-inf')
        
        for num in arr:
            if num > max1:
                max2 = max1
                max1 = num
            elif num > max2:
                max2 = num
        
        # 检查每个元素
        for i, num in enumerate(arr):
            remaining_sum = total_sum - num
            max_element = max2 if num == max1 else max1
            
            if remaining_sum == 2 * max_element:
                result.append(i + 1)  # 1-based索引
        
        return result
    
    @staticmethod
    def test():
        print("=== Codeforces 1077C - Good Array 测试 ===")
        
        arr1 = [2, 1, 3]
        print(f"数组: {arr1}")
        result1 = GoodArraySolution.good_array(arr1)
        print(f"结果索引: {result1}")
        print()

class MyCowAteMyHomeworkSolution:
    """
    题目19: USACO 2017 December Contest, Silver Problem 1. My Cow Ate My Homework
    来源: USACO
    链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=762
    难度: Easy
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    是否最优解: 是
    """
    
    @staticmethod
    def find_best_k(scores: List[int]) -> List[int]:
        result = []
        if len(scores) < 3:
            return result
        
        n = len(scores)
        suffix_sum = [0] * (n + 1)
        suffix_min = [float('inf')] * (n + 1)
        
        # 计算后缀和和后缀最小值
        for i in range(n - 1, -1, -1):
            suffix_sum[i] = suffix_sum[i + 1] + scores[i]
            suffix_min[i] = min(suffix_min[i + 1], scores[i])
        
        max_avg = 0
        
        # 遍历k值
        for k in range(1, n - 1):
            total = suffix_sum[k] - suffix_min[k]
            count = n - k - 1
            
            if count > 0:
                avg = total / count
                
                if avg > max_avg:
                    max_avg = avg
                    result = [k]
                elif abs(avg - max_avg) < 1e-9:
                    result.append(k)
        
        return result
    
    @staticmethod
    def test():
        print("=== USACO 2017 December Contest, Silver Problem 1 测试 ===")
        
        scores1 = [3, 1, 9, 2, 7]
        print(f"成绩数组: {scores1}")
        result1 = MyCowAteMyHomeworkSolution.find_best_k(scores1)
        print(f"最优k值: {result1}")
        print()

class LuoguP1001Solution:
    """
    题目20: 洛谷 P1001 A+B Problem
    来源: 洛谷
    链接: https://www.luogu.com.cn/problem/P1001
    难度: 入门
    
    时间复杂度: O(max(m,n))
    空间复杂度: O(max(m,n))
    是否最优解: 是
    """
    
    @staticmethod
    def add(a: str, b: str) -> str:
        result = []
        carry = 0
        i, j = len(a) - 1, len(b) - 1
        
        while i >= 0 or j >= 0 or carry:
            digit_a = int(a[i]) if i >= 0 else 0
            digit_b = int(b[j]) if j >= 0 else 0
            total = digit_a + digit_b + carry
            carry = total // 10
            result.append(str(total % 10))
            i -= 1
            j -= 1
        
        return ''.join(reversed(result))
    
    @staticmethod
    def test():
        print("=== 洛谷 P1001 A+B Problem 测试 ===")
        
        a1, b1 = "1", "2"
        print(f"{a1} + {b1} = {LuoguP1001Solution.add(a1, b1)}")
        
        a2, b2 = "123456789", "987654321"
        print(f"{a2} + {b2} = {LuoguP1001Solution.add(a2, b2)}")
        print()

class CodeChefFLOW001Solution:
    """
    题目21: CodeChef FLOW001 - Add Two Numbers
    来源: CodeChef
    链接: https://www.codechef.com/problems/FLOW001
    难度: Beginner
    
    时间复杂度: O(1)
    空间复杂度: O(1)
    是否最优解: 是
    """
    
    @staticmethod
    def add(a: int, b: int) -> int:
        return a + b
    
    @staticmethod
    def test():
        print("=== CodeChef FLOW001 - Add Two Numbers 测试 ===")
        
        print(f"1 + 2 = {CodeChefFLOW001Solution.add(1, 2)}")
        print(f"100 + 200 = {CodeChefFLOW001Solution.add(100, 200)}")
        print()

class SPOJADDREVSolution:
    """
    题目22: SPOJ ADDREV - Adding Reversed Numbers
    来源: SPOJ
    链接: http://www.spoj.com/problems/ADDREV/
    难度: Easy
    
    时间复杂度: O(log n)
    空间复杂度: O(1)
    是否最优解: 是
    """
    
    @staticmethod
    def add_reversed(a: int, b: int) -> int:
        reversed_a = SPOJADDREVSolution._reverse_number(a)
        reversed_b = SPOJADDREVSolution._reverse_number(b)
        sum_val = reversed_a + reversed_b
        return SPOJADDREVSolution._reverse_number(sum_val)
    
    @staticmethod
    def _reverse_number(n: int) -> int:
        reversed_num = 0
        while n > 0:
            reversed_num = reversed_num * 10 + n % 10
            n //= 10
        return reversed_num
    
    @staticmethod
    def test():
        print("=== SPOJ ADDREV - Adding Reversed Numbers 测试 ===")
        
        print(f"24 + 1 = {SPOJADDREVSolution.add_reversed(24, 1)}")
        print(f"4358 + 754 = {SPOJADDREVSolution.add_reversed(4358, 754)}")
        print()

class ProjectEulerProblem13Solution:
    """
    题目23: Project Euler Problem 13: Large sum
    来源: Project Euler
    链接: https://projecteuler.net/problem=13
    难度: Easy
    
    时间复杂度: O(n*m)
    空间复杂度: O(m)
    是否最优解: 是
    """
    
    @staticmethod
    def large_sum(numbers: List[str]) -> str:
        result = "0"
        for num in numbers:
            result = ProjectEulerProblem13Solution._add_big_numbers(result, num)
        return result[:10]  # 返回前10位
    
    @staticmethod
    def _add_big_numbers(a: str, b: str) -> str:
        result = []
        carry = 0
        i, j = len(a) - 1, len(b) - 1
        
        while i >= 0 or j >= 0 or carry:
            digit_a = int(a[i]) if i >= 0 else 0
            digit_b = int(b[j]) if j >= 0 else 0
            total = digit_a + digit_b + carry
            carry = total // 10
            result.append(str(total % 10))
            i -= 1
            j -= 1
        
        return ''.join(reversed(result))
    
    @staticmethod
    def test():
        print("=== Project Euler Problem 13: Large sum 测试 ===")
        
        test_numbers = [
            "37107287533902102798797998220837590246510135740250",
            "46376937677490009712648124896970078050417018260538"
        ]
        
        result = ProjectEulerProblem13Solution.large_sum(test_numbers)
        print(f"前10位和: {result}")
        print()

def run_all_tests():
    """运行所有测试"""
    AddTwoNumbersSolution.test()
    AddTwoNumbersIISolution.test()
    PlusOneSolution.test()
    AddStringsSolution.test()
    AddBinarySolution.test()
    MultiplyStringsSolution.test()
    SumOfTwoIntegersSolution.test()
    AddDigitsSolution.test()
    AdditiveNumberSolution.test()
    DoubleLinkedListNumberSolution.test()
    GoodArraySolution.test()
    MyCowAteMyHomeworkSolution.test()
    LuoguP1001Solution.test()
    CodeChefFLOW001Solution.test()
    SPOJADDREVSolution.test()
    ProjectEulerProblem13Solution.test()

if __name__ == "__main__":
    run_all_tests()

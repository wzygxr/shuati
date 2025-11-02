#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
LeetCode 125. 验证回文串 (Valid Palindrome)

题目描述:
如果在将所有大写字符转换为小写字符、并移除所有非字母数字字符之后，短语正着读和反着读都一样，则可以认为该短语是一个 回文串 。
字母和数字都属于字母数字字符。
给你一个字符串 s，如果它是 回文串 ，返回 true ；否则，返回 false 。

示例1:
输入: s = "A man, a plan, a canal: Panama"
输出: true
解释: "amanaplanacanalpanama" 是回文串。

示例2:
输入: s = "race a car"
输出: false
解释: "raceacar" 不是回文串。

示例3:
输入: s = " "
输出: true
解释: 在移除非字母数字字符后，s 变为 "" 。由于空字符串正着反着读都一样，所以是回文串。

提示:
1 <= s.length <= 2 * 10^5
s 仅由可打印的 ASCII 字符组成

题目链接: https://leetcode.cn/problems/valid-palindrome/

解题思路:
这道题可以使用双指针的方法来解决：

方法一（双指针 + 字符处理）：
1. 使用两个指针 left 和 right 分别指向字符串的首尾
2. 跳过非字母数字字符，只比较字母数字字符
3. 比较左右指针指向的字符（忽略大小写）
4. 如果所有字符都匹配，则返回true，否则返回false

时间复杂度: O(n)，n为字符串长度
空间复杂度: O(1)
是否最优解：是
"""

class Solution:
    """
    验证回文串解决方案类
    """
    
    @staticmethod
    def is_palindrome(s: str) -> bool:
        """
        解法一: 双指针（最优解）
        
        Args:
            s: 输入字符串
            
        Returns:
            bool: 是否为回文串
            
        时间复杂度: O(n) - 每个字符最多被访问一次
        空间复杂度: O(1) - 只使用常数级别的额外空间
        """
        if s is None:
            return False
            
        left, right = 0, len(s) - 1
        
        while left < right:
            # 跳过非字母数字字符（左指针）
            while left < right and not s[left].isalnum():
                left += 1
                
            # 跳过非字母数字字符（右指针）
            while left < right and not s[right].isalnum():
                right -= 1
                
            # 比较字符（忽略大小写）
            if s[left].lower() != s[right].lower():
                return False
                
            left += 1
            right -= 1
            
        return True
    
    @staticmethod
    def is_palindrome_string(s: str) -> bool:
        """
        解法二: 使用字符串反转比较
        
        Args:
            s: 输入字符串
            
        Returns:
            bool: 是否为回文串
            
        时间复杂度: O(n) - 需要遍历字符串两次
        空间复杂度: O(n) - 需要额外的字符串存储空间
        """
        if s is None:
            return False
            
        # 过滤非字母数字字符并转换为小写
        filtered = ''.join(char.lower() for char in s if char.isalnum())
        
        # 比较原字符串和反转后的字符串
        return filtered == filtered[::-1]
    
    @staticmethod
    def is_palindrome_optimized(s: str) -> bool:
        """
        解法三: 优化的双指针实现（避免重复计算）
        
        Args:
            s: 输入字符串
            
        Returns:
            bool: 是否为回文串
            
        时间复杂度: O(n)
        空间复杂度: O(1)
        """
        if s is None:
            return False
            
        left, right = 0, len(s) - 1
        
        while left < right:
            left_char = s[left]
            right_char = s[right]
            
            # 如果左字符不是字母数字，跳过
            if not Solution._is_alphanumeric(left_char):
                left += 1
                continue
                
            # 如果右字符不是字母数字，跳过
            if not Solution._is_alphanumeric(right_char):
                right -= 1
                continue
                
            # 比较字符（忽略大小写）
            if Solution._to_lower(left_char) != Solution._to_lower(right_char):
                return False
                
            left += 1
            right -= 1
            
        return True
    
    @staticmethod
    def _is_alphanumeric(c: str) -> bool:
        """
        判断字符是否为字母或数字
        
        Args:
            c: 字符
            
        Returns:
            bool: 是否为字母数字
        """
        return ('a' <= c <= 'z') or ('A' <= c <= 'Z') or ('0' <= c <= '9')
    
    @staticmethod
    def _to_lower(c: str) -> str:
        """
        将字符转换为小写（自定义实现）
        
        Args:
            c: 字符
            
        Returns:
            str: 小写字符
        """
        if 'A' <= c <= 'Z':
            return chr(ord(c) - ord('A') + ord('a'))
        return c

def test():
    """
    测试函数
    """
    solution = Solution()
    
    # 测试用例1
    s1 = "A man, a plan, a canal: Panama"
    expected1 = True
    print("测试用例1:")
    print(f"输入: \"{s1}\"")
    print(f"解法一结果: {solution.is_palindrome(s1)}")
    print(f"解法二结果: {solution.is_palindrome_string(s1)}")
    print(f"解法三结果: {solution.is_palindrome_optimized(s1)}")
    print(f"期望: {expected1}")
    print()
    
    # 测试用例2
    s2 = "race a car"
    expected2 = False
    print("测试用例2:")
    print(f"输入: \"{s2}\"")
    print(f"解法一结果: {solution.is_palindrome(s2)}")
    print(f"解法二结果: {solution.is_palindrome_string(s2)}")
    print(f"解法三结果: {solution.is_palindrome_optimized(s2)}")
    print(f"期望: {expected2}")
    print()
    
    # 测试用例3
    s3 = " "
    expected3 = True
    print("测试用例3:")
    print(f"输入: \"{s3}\"")
    print(f"解法一结果: {solution.is_palindrome(s3)}")
    print(f"解法二结果: {solution.is_palindrome_string(s3)}")
    print(f"解法三结果: {solution.is_palindrome_optimized(s3)}")
    print(f"期望: {expected3}")
    print()
    
    # 测试用例4 - 边界情况：空字符串
    s4 = ""
    expected4 = True
    print("测试用例4（空字符串）:")
    print(f"输入: \"{s4}\"")
    print(f"解法一结果: {solution.is_palindrome(s4)}")
    print(f"解法二结果: {solution.is_palindrome_string(s4)}")
    print(f"解法三结果: {solution.is_palindrome_optimized(s4)}")
    print(f"期望: {expected4}")
    print()
    
    # 测试用例5 - 边界情况：纯数字
    s5 = "12321"
    expected5 = True
    print("测试用例5（纯数字）:")
    print(f"输入: \"{s5}\"")
    print(f"解法一结果: {solution.is_palindrome(s5)}")
    print(f"解法二结果: {solution.is_palindrome_string(s5)}")
    print(f"解法三结果: {solution.is_palindrome_optimized(s5)}")
    print(f"期望: {expected5}")
    print()
    
    # 测试用例6 - 边界情况：混合字符
    s6 = "0P"
    expected6 = False
    print("测试用例6（混合字符）:")
    print(f"输入: \"{s6}\"")
    print(f"解法一结果: {solution.is_palindrome(s6)}")
    print(f"解法二结果: {solution.is_palindrome_string(s6)}")
    print(f"解法三结果: {solution.is_palindrome_optimized(s6)}")
    print(f"期望: {expected6}")
    print()

def performance_test():
    """
    性能测试函数
    """
    import time
    solution = Solution()
    
    # 创建长字符串进行性能测试
    import random
    chars = "!@#$%^&*() abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789"
    long_string = ''.join(random.choice(chars) for _ in range(100000))
    
    # 测试解法一的性能
    start_time = time.time()
    result1 = solution.is_palindrome(long_string)
    end_time = time.time()
    duration1 = (end_time - start_time) * 1000  # 转换为毫秒
    print(f"解法一（双指针）耗时: {duration1:.2f}ms, 结果: {result1}")
    
    # 测试解法二的性能
    start_time = time.time()
    result2 = solution.is_palindrome_string(long_string)
    end_time = time.time()
    duration2 = (end_time - start_time) * 1000
    print(f"解法二（字符串反转）耗时: {duration2:.2f}ms, 结果: {result2}")
    
    # 测试解法三的性能
    start_time = time.time()
    result3 = solution.is_palindrome_optimized(long_string)
    end_time = time.time()
    duration3 = (end_time - start_time) * 1000
    print(f"解法三（优化双指针）耗时: {duration3:.2f}ms, 结果: {result3}")
    
    # 验证结果一致性
    print(f"所有解法结果一致: {result1 == result2 == result3}")

def boundary_test():
    """
    边界条件测试函数
    """
    solution = Solution()
    
    # 测试None输入
    try:
        result = solution.is_palindrome(None)
        print("边界测试失败：None输入没有抛出异常")
    except Exception as e:
        print(f"边界测试通过：None输入正确抛出异常: {e}")
    
    # 测试极端长字符串
    extreme_string = 'a' * 1000000
    
    import time
    start_time = time.time()
    result = solution.is_palindrome(extreme_string)
    end_time = time.time()
    duration = (end_time - start_time) * 1000
    print(f"极端长字符串测试耗时: {duration:.2f}ms, 结果: {result}")

def algorithm_analysis():
    """
    算法分析函数
    """
    print("=== 算法分析 ===")
    print("1. 解法一（双指针）")
    print("   - 时间复杂度: O(n) - 每个字符最多被访问一次")
    print("   - 空间复杂度: O(1) - 只使用常数级别的额外空间")
    print("   - 优点: 原地操作，空间效率高")
    print("   - 缺点: 需要处理字符过滤逻辑")
    print()
    
    print("2. 解法二（字符串反转）")
    print("   - 时间复杂度: O(n) - 需要遍历字符串两次")
    print("   - 空间复杂度: O(n) - 需要额外的字符串存储空间")
    print("   - 优点: 实现简单，易于理解")
    print("   - 缺点: 空间效率较低")
    print()
    
    print("3. 解法三（优化双指针）")
    print("   - 时间复杂度: O(n)")
    print("   - 空间复杂度: O(1)")
    print("   - 优点: 避免重复字符检查，效率最高")
    print("   - 缺点: 实现相对复杂")
    print()
    
    print("推荐使用解法一作为通用解决方案")

if __name__ == "__main__":
    print("=== 验证回文串 算法实现 ===")
    print()
    
    print("=== 测试用例 ===")
    test()
    
    print("=== 性能测试 ===")
    performance_test()
    
    print("=== 边界条件测试 ===")
    boundary_test()
    
    print("=== 算法分析 ===")
    algorithm_analysis()
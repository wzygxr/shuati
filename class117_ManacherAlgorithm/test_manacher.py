#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
测试Manacher算法实现
"""

# 导入我们实现的模块
import Code08_P3805_Manacher
import Code09_POJ3974_Palindrome
import Code10_HDU3068_LongestPalindrome

def test_p3805():
    """测试洛谷P3805题目"""
    print("Testing P3805 Manacher:")
    
    # 测试用例1
    s1 = "abcba"
    result1 = Code08_P3805_Manacher.longest_palindrome(s1)
    print(f"Input: {s1} -> Output: {result1} (Expected: 5)")
    
    # 测试用例2
    s2 = "abccba"
    result2 = Code08_P3805_Manacher.longest_palindrome(s2)
    print(f"Input: {s2} -> Output: {result2} (Expected: 6)")
    
    # 测试用例3
    s3 = "abacabad"
    result3 = Code08_P3805_Manacher.longest_palindrome(s3)
    print(f"Input: {s3} -> Output: {result3} (Expected: 7)")
    # "abacabad"中"abacaba"是长度为7的最长回文子串

def test_poj3974():
    """测试POJ3974题目"""
    print("\nTesting POJ3974 Palindrome:")
    
    # 测试用例1
    s1 = "abcbabcbabcba"
    result1 = Code09_POJ3974_Palindrome.longest_palindrome(s1)
    print(f"Input: {s1} -> Output: {result1} (Expected: 13)")
    
    # 测试用例2
    s2 = "abacacbaaaab"
    result2 = Code09_POJ3974_Palindrome.longest_palindrome(s2)
    print(f"Input: {s2} -> Output: {result2} (Expected: 6)")

def test_hdu3068():
    """测试HDU3068题目"""
    print("\nTesting HDU3068 Longest Palindrome:")
    
    # 测试用例1
    s1 = "aaaa"
    result1 = Code10_HDU3068_LongestPalindrome.longest_palindrome(s1)
    print(f"Input: {s1} -> Output: {result1} (Expected: 4)")
    
    # 测试用例2
    s2 = "abab"
    result2 = Code10_HDU3068_LongestPalindrome.longest_palindrome(s2)
    print(f"Input: {s2} -> Output: {result2} (Expected: 3)")
    
    # 测试用例3
    s3 = "abcd"
    result3 = Code10_HDU3068_LongestPalindrome.longest_palindrome(s3)
    print(f"Input: {s3} -> Output: {result3} (Expected: 1)")

if __name__ == "__main__":
    test_p3805()
    test_poj3974()
    test_hdu3068()
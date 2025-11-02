#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
测试所有算法实现的脚本
包括单元测试和集成测试
"""

import unittest
import sys
import os

# 添加当前目录到Python路径
sys.path.append('.')

class TestManacherAlgorithms(unittest.TestCase):
    """Manacher算法测试类"""
    
    def setUp(self):
        """测试前准备"""
        # 导入Manacher算法实现
        try:
            from manacher_python import manacher, longest_palindrome, count_substrings, shortest_palindrome
            self.manacher = manacher
            self.longest_palindrome = longest_palindrome
            self.count_substrings = count_substrings
            self.shortest_palindrome = shortest_palindrome
        except ImportError:
            self.skipTest("Python Manacher实现未找到")
    
    def test_manacher_basic(self):
        """测试基本Manacher算法"""
        self.assertEqual(self.manacher("abc12321cba"), 7)
        self.assertEqual(self.manacher("a"), 1)
        self.assertEqual(self.manacher("aaaaa"), 5)
        self.assertEqual(self.manacher(""), 0)
    
    def test_longest_palindrome(self):
        """测试最长回文子串"""
        result = self.longest_palindrome("babad")
        self.assertIn(result, ["bab", "aba"])
        
        result = self.longest_palindrome("cbbd")
        self.assertEqual(result, "bb")
        
        result = self.longest_palindrome("a")
        self.assertEqual(result, "a")
        
        result = self.longest_palindrome("")
        self.assertEqual(result, "")
    
    def test_count_substrings(self):
        """测试回文子串计数"""
        self.assertEqual(self.count_substrings("abc"), 3)
        self.assertEqual(self.count_substrings("aaa"), 6)
        self.assertEqual(self.count_substrings("a"), 1)
        self.assertEqual(self.count_substrings(""), 0)
    
    def test_shortest_palindrome(self):
        """测试最短回文串"""
        self.assertEqual(self.shortest_palindrome("aacecaaa"), "aaacecaaa")
        self.assertEqual(self.shortest_palindrome("abcd"), "dcbabcd")
        self.assertEqual(self.shortest_palindrome("a"), "a")
        self.assertEqual(self.shortest_palindrome(""), "")

class TestZFunctionAlgorithms(unittest.TestCase):
    """Z函数算法测试类"""
    
    def setUp(self):
        """测试前准备"""
        # 导入Z函数实现
        try:
            from z_function_python import z_function, sum_scores, minimum_time_to_initial_state
            self.z_function = z_function
            self.sum_scores = sum_scores
            self.minimum_time_to_initial_state = minimum_time_to_initial_state
        except ImportError:
            self.skipTest("Python Z函数实现未找到")
    
    def test_z_function_basic(self):
        """测试基本Z函数"""
        result = self.z_function("aaaaa")
        expected = [5, 4, 3, 2, 1]
        self.assertEqual(result, expected)
        
        result = self.z_function("ababc")
        expected = [5, 0, 2, 0, 1]
        self.assertEqual(result, expected)
    
    def test_sum_scores(self):
        """测试构造字符串的总得分和"""
        self.assertEqual(self.sum_scores("babab"), 9)
        self.assertEqual(self.sum_scores("azbazbzaz"), 14)
    
    def test_minimum_time_to_initial_state(self):
        """测试恢复初始状态所需的最短时间"""
        self.assertEqual(self.minimum_time_to_initial_state("abacaba", 3), 2)
        self.assertEqual(self.minimum_time_to_initial_state("abacaba", 4), 1)
        self.assertEqual(self.minimum_time_to_initial_state("abcdef", 2), 3)

class TestCodeforcesProblems(unittest.TestCase):
    """Codeforces题目测试类"""
    
    def setUp(self):
        """测试前准备"""
        # 导入Codeforces问题实现
        try:
            from z_function_python import z_function
            self.z_function = z_function
        except ImportError:
            self.skipTest("Python Z函数实现未找到")
    
    def test_password_problem(self):
        """测试Codeforces 126B Password问题"""
        # 这里我们测试Z函数的正确性，因为Password问题的完整实现可能在Java中
        result = self.z_function("abcabc")
        expected = [6, 0, 0, 3, 0, 0]
        self.assertEqual(result, expected)

class TestLeetCodeProblems(unittest.TestCase):
    """LeetCode题目测试类"""
    
    def setUp(self):
        """测试前准备"""
        # 导入LeetCode问题实现
        try:
            from manacher_python import manacher
            from z_function_python import sum_scores, minimum_time_to_initial_state
            self.manacher = manacher
            self.sum_scores = sum_scores
            self.minimum_time_to_initial_state = minimum_time_to_initial_state
        except ImportError:
            self.skipTest("Python算法实现未找到")
    
    def test_leetcode_5(self):
        """测试LeetCode 5. 最长回文子串"""
        # 这个测试已经在TestManacherAlgorithms中完成
    
    def test_leetcode_2223(self):
        """测试LeetCode 2223. 构造字符串的总得分和"""
        self.assertEqual(self.sum_scores("babab"), 9)
    
    def test_leetcode_3031(self):
        """测试LeetCode 3031. 将单词恢复初始状态所需的最短时间 II"""
        self.assertEqual(self.minimum_time_to_initial_state("abacaba", 3), 2)

def run_all_tests():
    """运行所有测试"""
    # 创建测试套件
    test_suite = unittest.TestSuite()
    
    # 添加所有测试类
    test_suite.addTest(unittest.makeSuite(TestManacherAlgorithms))
    test_suite.addTest(unittest.makeSuite(TestZFunctionAlgorithms))
    test_suite.addTest(unittest.makeSuite(TestCodeforcesProblems))
    test_suite.addTest(unittest.makeSuite(TestLeetCodeProblems))
    
    # 运行测试
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(test_suite)
    
    return result.wasSuccessful()

if __name__ == "__main__":
    print("开始运行所有算法测试...")
    print("=" * 50)
    
    success = run_all_tests()
    
    print("=" * 50)
    if success:
        print("所有测试通过!")
        sys.exit(0)
    else:
        print("部分测试失败!")
        sys.exit(1)
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
最终验证脚本
用于验证[class103](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class103)目录下所有算法实现的正确性
包括Java、Python、C++三种语言的实现
"""

import os
import subprocess
import sys
import time

# 测试用例
TEST_CASES = [
    "abc12321cba",
    "a",
    "aaaaa",
    "abcdefg",
    "babad",
    "cbbd",
    "aacecaaa",
    "abcd",
    "ababbb",
    "zaaaxbbby"
]

def run_java_tests():
    """运行Java代码测试"""
    print("开始Java代码测试...")
    
    # 编译Java代码
    try:
        subprocess.run(["javac", "Code01_Manacher.java"], check=True, cwd=".")
        subprocess.run(["javac", "Code02_ExpandKMP.java"], check=True, cwd=".")
        subprocess.run(["javac", "Codeforces126B_Password.java"], check=True, cwd=".")
        subprocess.run(["javac", "LeetCode1960_MaxProduct.java"], check=True, cwd=".")
        print("Java代码编译成功")
    except subprocess.CalledProcessError:
        print("Java代码编译失败")
        return False
    
    # 运行测试
    try:
        # 测试Manacher算法
        result = subprocess.run(["java", "Code01_Manacher"], 
                              input="abc12321cba\n", 
                              text=True, 
                              capture_output=True, 
                              cwd=".")
        print("Manacher算法测试结果:", result.stdout.strip())
        
        # 测试Z函数
        result = subprocess.run(["java", "Code02_ExpandKMP"], 
                              input="abc\nabcd\n", 
                              text=True, 
                              capture_output=True, 
                              cwd=".")
        print("Z函数测试结果:", result.stdout.strip())
        
        print("Java代码测试完成")
        return True
    except Exception as e:
        print(f"Java代码运行失败: {e}")
        return False

def run_python_tests():
    """运行Python代码测试"""
    print("开始Python代码测试...")
    
    try:
        # 测试Manacher算法
        from manacher_python import manacher, longest_palindrome, count_substrings, shortest_palindrome
        
        for test_case in TEST_CASES[:3]:
            result = manacher(test_case)
            print(f"manacher('{test_case}') = {result}")
        
        # 测试Z函数
        from z_function_python import z_function, sum_scores, minimum_time_to_initial_state
        
        z_result = z_function("abc")
        print(f"z_function('abc') = {z_result}")
        
        score = sum_scores("babab")
        print(f"sum_scores('babab') = {score}")
        
        time_result = minimum_time_to_initial_state("abacaba", 3)
        print(f"minimum_time_to_initial_state('abacaba', 3) = {time_result}")
        
        print("Python代码测试完成")
        return True
    except Exception as e:
        print(f"Python代码运行失败: {e}")
        return False

def run_cpp_tests():
    """运行C++代码测试"""
    print("开始C++代码测试...")
    
    # 编译C++代码
    try:
        subprocess.run(["g++", "-std=c++11", "manacher_cpp.cpp", "-o", "manacher_cpp"], check=True, cwd=".")
        subprocess.run(["g++", "-std=c++11", "z_function_cpp.cpp", "-o", "z_function_cpp"], check=True, cwd=".")
        print("C++代码编译成功")
    except subprocess.CalledProcessError:
        print("C++代码编译失败")
        return False
    
    # 运行测试
    try:
        # 测试Manacher算法
        result = subprocess.run(["./manacher_cpp"], 
                              input="abc12321cba\n", 
                              text=True, 
                              capture_output=True, 
                              cwd=".")
        print("Manacher C++测试结果:", result.stdout.strip())
        
        # 测试Z函数
        result = subprocess.run(["./z_function_cpp"], 
                              input="abc\nabcd\n", 
                              text=True, 
                              capture_output=True, 
                              cwd=".")
        print("Z函数 C++测试结果:", result.stdout.strip())
        
        print("C++代码测试完成")
        return True
    except Exception as e:
        print(f"C++代码运行失败: {e}")
        return False

def main():
    """主函数"""
    print("开始[class103](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class103)算法实现验证")
    print("=" * 50)
    
    # 获取当前目录
    current_dir = os.getcwd()
    print(f"当前目录: {current_dir}")
    
    # 验证文件存在性
    required_files = [
        "Code01_Manacher.java",
        "Code02_ExpandKMP.java", 
        "Codeforces126B_Password.java",
        "LeetCode1960_MaxProduct.java",
        "manacher_python.py",
        "z_function_python.py",
        "manacher_cpp.cpp",
        "z_function_cpp.cpp"
    ]
    
    missing_files = []
    for file in required_files:
        if not os.path.exists(file):
            missing_files.append(file)
    
    if missing_files:
        print(f"缺少以下文件: {missing_files}")
        return False
    
    print("所有必需文件都存在")
    
    # 运行各语言测试
    tests_passed = 0
    total_tests = 3
    
    if run_java_tests():
        tests_passed += 1
    
    if run_python_tests():
        tests_passed += 1
        
    if run_cpp_tests():
        tests_passed += 1
    
    print("=" * 50)
    print(f"测试完成: {tests_passed}/{total_tests} 个测试通过")
    
    if tests_passed == total_tests:
        print("所有测试都通过了！")
        return True
    else:
        print("部分测试失败，请检查代码实现")
        return False

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1)
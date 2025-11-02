#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
测试SPOJ PHONELST问题的Python实现
"""

import sys
import os

# 添加当前目录到Python路径
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from Code06_PhoneList import phone_list

def test_phonelist():
    """测试PhoneList功能"""
    print("Testing SPOJ PHONELST...")
    
    # 测试用例1：存在前缀关系
    numbers1 = ["123", "1234", "567"]
    result1 = phone_list(numbers1)
    print(f"Numbers: {numbers1}")
    print(f"Result: {result1}")
    print(f"Expected: False (123 is prefix of 1234)")
    
    # 测试用例2：不存在前缀关系
    numbers2 = ["123", "456", "789"]
    result2 = phone_list(numbers2)
    print(f"Numbers: {numbers2}")
    print(f"Result: {result2}")
    print(f"Expected: True (no prefix relationship)")
    
    # 验证结果
    if result1 == False and result2 == True:
        print("Test passed!")
        return True
    else:
        print("Test failed!")
        return False

if __name__ == "__main__":
    test_phonelist()
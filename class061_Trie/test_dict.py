#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
测试SPOJ DICT问题的Python实现
"""

import sys
import os

# 添加当前目录到Python路径
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from Code05_Dict import dict_words

def test_dict():
    """测试Dict功能"""
    print("Testing SPOJ DICT...")
    
    words = ["hello", "world", "help", "held", "he"]
    prefix = "hel"
    
    result = dict_words(words, prefix)
    print(f"Words: {words}")
    print(f"Prefix: {prefix}")
    print(f"Result: {result}")
    
    # 验证结果 - 按字典序排序后比较
    expected = ["held", "help", "hello"]
    result_sorted = sorted(result)
    expected_sorted = sorted(expected)
    
    if result_sorted == expected_sorted:
        print("Test passed!")
        return True
    else:
        print("Test failed!")
        return False

if __name__ == "__main__":
    test_dict()
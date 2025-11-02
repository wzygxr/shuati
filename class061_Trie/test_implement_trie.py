#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
测试LintCode实现前缀树问题的Python实现
"""

import sys
import os

# 添加当前目录到Python路径
sys.path.append(os.path.dirname(os.path.abspath(__file__)))

from Code07_ImplementTrie import Trie

def test_implement_trie():
    """测试ImplementTrie功能"""
    print("Testing LintCode Implement Trie...")
    
    trie = Trie()
    
    # 测试插入
    trie.insert("apple")
    print("Inserted 'apple'")
    
    # 测试搜索
    result1 = trie.search("apple")   # 应该返回True
    result2 = trie.search("app")     # 应该返回False
    print(f"Search 'apple': {result1}")
    print(f"Search 'app': {result2}")
    
    # 测试前缀
    result3 = trie.startsWith("app") # 应该返回True
    result4 = trie.startsWith("apr") # 应该返回False
    print(f"StartsWith 'app': {result3}")
    print(f"StartsWith 'apr': {result4}")
    
    # 插入更多单词
    trie.insert("app")
    print("Inserted 'app'")
    
    # 再次测试
    result5 = trie.search("app")     # 应该返回True
    print(f"Search 'app' after insert: {result5}")
    
    # 验证结果
    if result1 == True and result2 == False and result3 == True and result4 == False and result5 == True:
        print("Test passed!")
        return True
    else:
        print("Test failed!")
        return False

if __name__ == "__main__":
    test_implement_trie()
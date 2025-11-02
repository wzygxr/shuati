#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
压缩算法综合测试脚本
用于测试所有Python实现的压缩算法功能
"""

import sys
import os

def test_arithmetic_coding():
    """测试算术编码"""
    print("=== 测试算术编码 ===")
    try:
        # 更新导入路径以适应新的目录结构
        arithmetic_coding_path = os.path.join(os.path.dirname(__file__), "arithmetic_coding")
        if arithmetic_coding_path not in sys.path:
            sys.path.append(arithmetic_coding_path)
        
        import arithmetic_coding
        test_string = "ABRACADABRA"
        print(f"原始字符串: {test_string}")
        
        # 编码
        ac = arithmetic_coding.ArithmeticCoding(test_string)
        encoded = ac.encode(test_string)
        print(f"编码结果: {encoded}")
        
        # 解码
        decoded = ac.decode(encoded, len(test_string) + 1)
        print(f"解码结果: {decoded}")
        
        # 验证
        success = test_string == decoded
        print(f"编码解码是否正确: {success}")
        
        if success:
            print("✓ 算术编码测试通过")
            return True
        else:
            print("✗ 算术编码测试失败")
            return False
    except Exception as e:
        print(f"✗ 算术编码测试出错: {e}")
        return False

def test_lzw_encoding():
    """测试LZW编码"""
    print("\n=== 测试LZW编码 ===")
    try:
        # 更新导入路径以适应新的目录结构
        lzw_encoding_path = os.path.join(os.path.dirname(__file__), "lzw_encoding")
        if lzw_encoding_path not in sys.path:
            sys.path.append(lzw_encoding_path)
        
        import lzw_encoding
        test_string = "ABABABA"
        print(f"原始字符串: {test_string}")
        
        # 编码
        encoded = lzw_encoding.lzw_encode(test_string)
        print(f"编码结果: {encoded}")
        
        # 解码
        decoded = lzw_encoding.lzw_decode(encoded)
        print(f"解码结果: {decoded}")
        
        # 验证
        success = test_string == decoded
        print(f"编码解码是否正确: {success}")
        
        if success:
            print("✓ LZW编码测试通过")
            return True
        else:
            print("✗ LZW编码测试失败")
            return False
    except Exception as e:
        print(f"✗ LZW编码测试出错: {e}")
        return False

def test_huffman_encoding():
    """测试Huffman编码"""
    print("\n=== 测试Huffman编码 ===")
    try:
        # 更新导入路径以适应新的目录结构
        huffman_encoding_path = os.path.join(os.path.dirname(__file__), "huffman_encoding")
        if huffman_encoding_path not in sys.path:
            sys.path.append(huffman_encoding_path)
        
        import huffman_encoding
        test_string = "ABRACADABRA"
        print(f"原始字符串: {test_string}")
        
        # 编码
        result = huffman_encoding.encode(test_string)
        print(f"Huffman编码表:")
        for char, code in result.huffman_codes.items():
            print(f"  {char}: {code}")
        print(f"编码结果: {result.encoded_data}")
        
        # 解码
        decoded = huffman_encoding.decode(result.encoded_data, result.huffman_codes)
        print(f"解码结果: {decoded}")
        
        # 验证
        success = test_string == decoded
        print(f"编码解码是否正确: {success}")
        
        if success:
            print("✓ Huffman编码测试通过")
            return True
        else:
            print("✗ Huffman编码测试失败")
            return False
    except Exception as e:
        print(f"✗ Huffman编码测试出错: {e}")
        return False

def main():
    """主测试函数"""
    print("========================================")
    print("     压缩算法综合测试脚本")
    print("========================================")
    
    # 切换到当前目录
    script_dir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(script_dir)
    
    # 统计测试结果
    passed_tests = 0
    total_tests = 3
    
    # 测试算术编码
    if test_arithmetic_coding():
        passed_tests += 1
    
    # 测试LZW编码
    if test_lzw_encoding():
        passed_tests += 1
    
    # 测试Huffman编码
    if test_huffman_encoding():
        passed_tests += 1
    
    # 输出测试结果
    print("\n========================================")
    print(f"测试完成: {passed_tests}/{total_tests} 个测试通过")
    
    if passed_tests == total_tests:
        print("🎉 所有测试通过!")
        return 0
    else:
        print("❌ 部分测试失败!")
        return 1

if __name__ == "__main__":
    sys.exit(main())
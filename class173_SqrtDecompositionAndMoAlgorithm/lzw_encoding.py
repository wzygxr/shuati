#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LZW字典编码实现 (Python版本)

LZW（Lempel-Ziv-Welch）是一种无损数据压缩算法，属于字典编码的一种。

算法原理：
1. 初始化字典，包含所有可能的单字符
2. 读取输入字符串，查找字典中最长的匹配字符串
3. 输出匹配字符串对应的编码
4. 将匹配字符串加上下一个字符组成的新字符串添加到字典中
5. 重复步骤2-4直到处理完所有输入

时间复杂度：O(n)，其中n是输入字符串长度
空间复杂度：O(d)，其中d是字典中条目的数量

优势：
1. 实现相对简单
2. 压缩效果好，特别适合重复模式较多的数据
3. 不需要预先知道数据的统计特性
4. 编码和解码过程对称

劣势：
1. 需要维护字典，占用内存
2. 对于随机数据压缩效果不佳
3. 字典可能会变得很大

应用场景：
1. GIF图像格式
2. TIFF图像格式
3. Unix系统的compress工具
"""


def lzw_encode(input_string):
    """
    LZW编码
    :param input_string: 输入字符串
    :return: 编码结果（整数列表）
    """
    # 初始化字典，包含所有ASCII字符
    dictionary = {chr(i): i for i in range(256)}
    dict_size = 256
    
    result = []
    current = ""
    
    for char in input_string:
        combined = current + char
        
        # 如果组合字符串在字典中，继续扩展
        if combined in dictionary:
            current = combined
        else:
            # 输出当前字符串的编码
            result.append(dictionary[current])
            
            # 将新字符串添加到字典
            dictionary[combined] = dict_size
            dict_size += 1
            
            # 重新开始
            current = char
    
    # 输出最后一个字符串
    if current:
        result.append(dictionary[current])
    
    return result


def lzw_decode(encoded):
    """
    LZW解码
    :param encoded: 编码结果（整数列表）
    :return: 解码结果（字符串）
    """
    # 初始化字典，包含所有ASCII字符
    dictionary = {i: chr(i) for i in range(256)}
    dict_size = 256
    
    result = []
    current = ""
    
    for code in encoded:
        if code in dictionary:
            entry = dictionary[code]
        elif code == dict_size:
            # 特殊情况：处理字符串+首字符的重复情况
            entry = current + current[0]
        else:
            raise ValueError(f"无效的编码: {code}")
        
        result.append(entry)
        
        # 将新字符串添加到字典
        if current:
            dictionary[dict_size] = current + entry[0]
            dict_size += 1
        
        current = entry
    
    return ''.join(result)


def calculate_compression_ratio(original_size, compressed_size):
    """
    计算压缩率
    :param original_size: 原始数据大小（字节）
    :param compressed_size: 压缩后数据大小（字节）
    :return: 压缩率（百分比）
    """
    if original_size == 0:
        return 0
    return (1.0 - compressed_size / original_size) * 100


def main():
    """
    测试方法
    """
    # 测试用例1：包含重复模式的字符串
    test1 = "ABABABA"
    print(f"原始字符串: {test1}")
    print(f"原始长度: {len(test1)} 字符")
    
    encoded1 = lzw_encode(test1)
    print(f"编码结果: {encoded1}")
    print(f"编码长度: {len(encoded1)} 个整数")
    
    decoded1 = lzw_decode(encoded1)
    print(f"解码结果: {decoded1}")
    print(f"编码解码是否正确: {test1 == decoded1}")
    print(f"压缩率: {calculate_compression_ratio(len(test1) * 2, len(encoded1) * 2):.2f}%")  # 假设每个整数占2字节
    print()
    
    # 测试用例2：更复杂的字符串
    test2 = "ABCABCABCABCABC"
    print(f"原始字符串: {test2}")
    print(f"原始长度: {len(test2)} 字符")
    
    encoded2 = lzw_encode(test2)
    print(f"编码结果: {encoded2}")
    print(f"编码长度: {len(encoded2)} 个整数")
    
    decoded2 = lzw_decode(encoded2)
    print(f"解码结果: {decoded2}")
    print(f"编码解码是否正确: {test2 == decoded2}")
    print(f"压缩率: {calculate_compression_ratio(len(test2) * 2, len(encoded2) * 2):.2f}%")
    print()
    
    # 测试用例3：无重复的字符串
    test3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    print(f"原始字符串: {test3}")
    print(f"原始长度: {len(test3)} 字符")
    
    encoded3 = lzw_encode(test3)
    print(f"编码结果: {encoded3}")
    print(f"编码长度: {len(encoded3)} 个整数")
    
    decoded3 = lzw_decode(encoded3)
    print(f"解码结果: {decoded3}")
    print(f"编码解码是否正确: {test3 == decoded3}")
    print(f"压缩率: {calculate_compression_ratio(len(test3) * 2, len(encoded3) * 2):.2f}%")


if __name__ == "__main__":
    main()
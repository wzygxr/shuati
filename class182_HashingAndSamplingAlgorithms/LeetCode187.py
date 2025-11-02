#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 187. 重复的DNA序列

题目描述：
所有DNA都由一系列缩写为'A'，'C'，'G'和'T'的核苷酸组成，例如：'ACGAATTCCG'。
在研究DNA时，识别DNA中的重复序列有时会对研究非常有帮助。
编写一个函数来找出DNA分子中所有出现不止一次的长度为10的序列（子串）。

解题思路：
使用滑动窗口和字符串哈希。维护一个长度为10的滑动窗口，计算每个子串的哈希值，
用哈希表统计出现次数。由于DNA序列只包含4个字符，可以使用4进制编码优化。

时间复杂度：O(n)，其中n是字符串长度
空间复杂度：O(n)，用于存储哈希表
"""

def findRepeatedDnaSequences(s):
    """
    找出DNA中所有重复的长度为10的序列
    
    Args:
        s (str): DNA序列字符串
        
    Returns:
        List[str]: 所有重复的序列列表
    """
    result = []
    
    # 如果字符串长度小于10，不可能有长度为10的子串
    if len(s) < 10:
        return result
    
    # 使用字典统计每个哈希值出现的次数
    hash_count = {}
    
    # 将字符映射为数字
    char_map = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
    
    # 计算4^9，用于滑动窗口时的哈希值更新
    base = 4
    power = base ** 9
    
    # 计算第一个长度为10的子串的哈希值
    hash_val = 0
    for i in range(10):
        hash_val = hash_val * base + char_map[s[i]]
    hash_count[hash_val] = 1
    
    # 滑动窗口，计算后续子串的哈希值
    for i in range(10, len(s)):
        # 移除最左边的字符，添加新字符
        hash_val = hash_val - char_map[s[i - 10]] * power
        hash_val = hash_val * base + char_map[s[i]]
        
        # 统计哈希值出现次数
        count = hash_count.get(hash_val, 0)
        hash_count[hash_val] = count + 1
        
        # 如果某个哈希值出现次数达到2，说明找到了重复序列
        # 只在第一次发现重复时添加到结果中，避免重复添加
        if count == 1:
            result.append(s[i - 9: i + 1])
    
    return result


def main():
    """测试方法"""
    # 测试用例1
    s1 = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
    print(f"输入: {s1}")
    print(f"输出: {findRepeatedDnaSequences(s1)}")
    # 预期输出: ["AAAAACCCCC","CCCCCAAAAA"]
    
    # 测试用例2
    s2 = "AAAAAAAAAAAAA"
    print(f"输入: {s2}")
    print(f"输出: {findRepeatedDnaSequences(s2)}")
    # 预期输出: ["AAAAAAAAAA"]
    
    # 测试用例3
    s3 = "AAAAAAAAAAA"
    print(f"输入: {s3}")
    print(f"输出: {findRepeatedDnaSequences(s3)}")
    # 预期输出: ["AAAAAAAAAA"]


if __name__ == "__main__":
    main()
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
洛谷 P3370 【模板】字符串哈希

题目描述：
如题，给定 N 个字符串（第 i 个字符串长度为 M_i，字符串内包含数字、大小写字母，大小写敏感），
请求出 N 个字符串中共有多少个不同的字符串。

解题思路：
这是字符串哈希的模板题。通过将每个字符串映射为一个整数（哈希值），我们可以快速比较两个字符串是否相等。
对于每个字符串，我们计算其哈希值并存储在集合中，最后集合的大小即为不同字符串的个数。

字符串哈希原理：
字符串哈希通过将字符串看作一个P进制数来计算哈希值，公式为：
hash(s) = (s[0]*P^(n-1) + s[1]*P^(n-2) + ... + s[n-1]*P^0) mod M
其中P通常选择一个质数（如31、131等），M也是一个大质数。

时间复杂度：O(N*M)，其中N是字符串个数，M是字符串平均长度
空间复杂度：O(N)，用于存储哈希集合
"""

def compute_hash(s):
    """
    计算字符串的哈希值
    
    Args:
        s (str): 输入字符串
        
    Returns:
        int: 字符串的哈希值
    """
    MOD = 1000000007  # 10^9 + 7
    BASE = 31         # 哈希基数
    
    hash_val = 0
    pow_val = 1
    
    # 从右到左计算哈希值
    for i in range(len(s) - 1, -1, -1):
        hash_val = (hash_val + (ord(s[i]) - ord('a') + 1) * pow_val) % MOD
        pow_val = (pow_val * BASE) % MOD
    
    return hash_val


def count_distinct_strings(strings):
    """
    计算不同字符串的个数
    
    Args:
        strings (List[str]): 字符串数组
        
    Returns:
        int: 不同字符串的个数
    """
    # 使用集合存储不同的哈希值
    hash_set = set()
    
    # 计算每个字符串的哈希值并加入集合
    for s in strings:
        hash_val = compute_hash(s)
        hash_set.add(hash_val)
    
    # 集合大小即为不同字符串的个数
    return len(hash_set)


def main():
    """测试方法"""
    # 测试用例
    strings = [
        "aaaa",
        "abc",
        "abcc",
        "abc",
        "12345"
    ]
    
    print("输入字符串:")
    for s in strings:
        print(s)
    
    result = count_distinct_strings(strings)
    print("不同字符串的个数:", result)
    # 预期输出: 4


if __name__ == "__main__":
    main()
"""
Codeforces 861D - Polycarp's phone book

题目描述：
给定n个长度为9的数字字符串，对于每个字符串，找到最短的特有子串（即该子串只在这个字符串中出现）。

解题思路：
1. 使用字典统计所有子串的出现次数
2. 对于每个字符串，枚举其所有子串，在字典中查找出现次数为1的最短子串

时间复杂度：O(N * L^3)，其中N是字符串数量，L是字符串长度
空间复杂度：O(N * L^3)
"""

import sys
from collections import defaultdict

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    n = int(input_lines[0])  # 字符串数量
    strings = input_lines[1:n+1]  # 所有字符串
    
    # 统计所有子串的出现次数
    substring_count = defaultdict(int)
    
    # 将所有子串插入字典
    for string in strings:
        # 枚举所有子串
        for i in range(len(string)):
            for j in range(i + 1, len(string) + 1):
                substring_count[string[i:j]] += 1
    
    # 对于每个字符串，找到最短的特有子串
    for string in strings:
        result = string  # 默认结果为整个字符串
        
        # 枚举所有子串，按长度递增
        found = False
        for length in range(1, len(string) + 1):
            if found:
                break
            for i in range(len(string) - length + 1):
                substr = string[i:i + length]
                # 如果该子串只出现一次，说明是特有子串
                if substring_count[substr] == 1:
                    result = substr
                    found = True
                    break
        
        print(result)

if __name__ == "__main__":
    main()
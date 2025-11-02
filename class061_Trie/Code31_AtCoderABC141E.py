"""
AtCoder ABC141 E - Who Says a Pun?

题目描述：
给定一个字符串，求最长的出现至少两次的不重叠子串长度。

解题思路：
1. 使用二分答案，对于每个长度，检查是否存在出现至少两次的不重叠子串
2. 使用前缀树或哈希来检查是否存在重复子串
3. 对于每个长度，枚举所有子串并在前缀树中查找是否已存在

时间复杂度：O(N^2 * log N)，其中N是字符串长度
空间复杂度：O(N^2)
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}      # 子节点字典
        self.positions = []     # 存储子串出现的位置

class Trie:
    """Trie树类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert_and_check(self, substr, pos):
        """
        向Trie树中插入子串并检查是否已存在不重叠的子串
        :param substr: 子串
        :param pos: 子串起始位置
        :return: 是否存在不重叠的重复子串
        """
        node = self.root
        for char in substr:
            # 如果子节点不存在，创建新节点
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        
        # 检查是否存在不重叠的子串
        for prev_pos in node.positions:
            if abs(pos - prev_pos) >= len(substr):
                return True  # 找到不重叠的重复子串
        
        # 记录当前位置
        node.positions.append(pos)
        return False

def has_duplicate_non_overlapping_substring(string, length):
    """
    检查是否存在长度为length的重复不重叠子串
    :param string: 字符串
    :param length: 子串长度
    :return: 是否存在重复不重叠子串
    """
    trie = Trie()  # 创建Trie树
    
    # 枚举所有长度为length的子串
    for i in range(len(string) - length + 1):
        substr = string[i:i + length]
        if trie.insert_and_check(substr, i):
            return True
    
    return False

def find_longest_duplicate_non_overlapping_substring(string):
    """
    二分查找最长的重复不重叠子串长度
    :param string: 字符串
    :return: 最长重复不重叠子串长度
    """
    left = 0
    right = len(string) // 2
    result = 0
    
    # 二分答案
    while left <= right:
        mid = (left + right) // 2
        if has_duplicate_non_overlapping_substring(string, mid):
            result = mid
            left = mid + 1
        else:
            right = mid - 1
    
    return result

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    n = int(input_lines[0])     # 字符串长度
    string = input_lines[1]     # 字符串
    
    result = find_longest_duplicate_non_overlapping_substring(string)
    print(result)

if __name__ == "__main__":
    main()
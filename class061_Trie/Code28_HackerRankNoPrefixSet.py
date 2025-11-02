"""
HackerRank No Prefix Set

题目描述：
给定一个字符串集合，判断是否存在一个字符串是另一个字符串的前缀。
如果存在，输出"BAD SET"和第一个违反规则的字符串；否则输出"GOOD SET"。

解题思路：
1. 使用前缀树存储字符串
2. 在插入过程中检查是否存在前缀关系
3. 如果在插入一个字符串时，发现路径上有已经标记为完整字符串的节点，
   或者插入完成后当前节点有子节点，说明存在前缀关系

时间复杂度：O(N*M)，其中N是字符串数量，M是平均字符串长度
空间复杂度：O(N*M)
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典
        self.is_end = False  # 标记是否为一个完整字符串的结尾

class Trie:
    """Trie树类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert(self, string):
        """
        向Trie树中插入一个字符串，并检查是否存在前缀关系
        :param string: 要插入的字符串
        :return: 如果存在前缀关系返回True，否则返回False
        """
        if not string:
            return False
        
        node = self.root
        for char in string:
            # 如果子节点不存在，创建新节点
            if char not in node.children:
                node.children[char] = TrieNode()
            
            node = node.children[char]
            
            # 如果当前节点已经是某个完整字符串的结尾，说明当前字符串是另一个字符串的前缀
            if node.is_end:
                return True
        
        # 标记当前节点为完整字符串的结尾
        node.is_end = True
        
        # 检查当前节点是否有子节点，如果有说明存在前缀关系
        if node.children:
            return True
        
        return False

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    n = int(input_lines[0])  # 字符串数量
    strings = input_lines[1:n+1]  # 所有字符串
    
    # 初始化Trie树
    trie = Trie()
    
    good_set = True
    bad_string = ""
    
    for string in strings:
        if trie.insert(string):
            good_set = False
            bad_string = string
            break
    
    if good_set:
        print("GOOD SET")
    else:
        print("BAD SET")
        print(bad_string)

if __name__ == "__main__":
    main()
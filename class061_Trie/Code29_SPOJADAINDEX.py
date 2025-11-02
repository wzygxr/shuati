"""
SPOJ ADAINDEX - Ada and Indexing

题目描述：
给定一个字符串列表和多个查询，每个查询给出一个前缀，要求统计以该前缀开头的字符串数量。

解题思路：
1. 使用前缀树存储所有字符串，每个节点记录经过该节点的字符串数量
2. 对于每个查询，在前缀树中找到对应前缀的节点，返回该节点记录的数量

时间复杂度：
- 构建：O(∑len(strings[i]))
- 查询：O(len(prefix))
空间复杂度：O(∑len(strings[i]))
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典
        self.count = 0      # 经过该节点的字符串数量

class Trie:
    """Trie树类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert(self, string):
        """
        向Trie树中插入一个字符串
        :param string: 要插入的字符串
        """
        if not string:
            return
        
        node = self.root
        for char in string:
            # 如果子节点不存在，创建新节点
            if char not in node.children:
                node.children[char] = TrieNode()
            
            node = node.children[char]
            node.count += 1  # 增加经过该节点的字符串数量
    
    def count_prefix(self, prefix):
        """
        查询以指定前缀开头的字符串数量
        :param prefix: 前缀
        :return: 字符串数量
        """
        if not prefix:
            return 0
        
        node = self.root
        for char in prefix:
            # 如果子节点不存在，说明没有以该前缀开头的字符串
            if char not in node.children:
                return 0
            
            node = node.children[char]
        
        # 返回经过该节点的字符串数量
        return node.count

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    # 解析输入
    n, q = map(int, input_lines[0].split())  # 字符串数量和查询数量
    strings = input_lines[1:n+1]  # 所有字符串
    prefixes = input_lines[n+1:n+1+q]  # 所有前缀查询
    
    # 初始化Trie树
    trie = Trie()
    
    # 插入所有字符串
    for string in strings:
        trie.insert(string)
    
    # 处理所有查询
    for prefix in prefixes:
        count = trie.count_prefix(prefix)
        print(count)

if __name__ == "__main__":
    main()
"""
POJ 2001 Shortest Prefixes

题目描述：
给定一组单词，为每个单词找出最短的唯一前缀。如果整个单词都不是其他任何单词的前缀，
则输出该单词本身。

解题思路：
1. 使用Trie树存储所有单词，在每个节点记录经过该节点的单词数量
2. 对于每个单词，在Trie树中查找第一个节点计数为1的位置，该位置即为最短唯一前缀

时间复杂度：O(N*M)，其中N是单词数量，M是平均单词长度
空间复杂度：O(N*M)
"""

import sys
from collections import defaultdict

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典
        self.count = 0      # 经过该节点的单词数量

class Trie:
    """Trie树类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert(self, word):
        """
        向Trie树中插入一个单词
        :param word: 要插入的单词
        """
        if not word:
            return
        
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.count += 1  # 增加经过该节点的单词数量
    
    def find_shortest_prefix(self, word):
        """
        查找单词的最短唯一前缀
        :param word: 要查找的单词
        :return: 最短唯一前缀
        """
        if not word:
            return ""
        
        node = self.root
        prefix = ""
        
        for char in word:
            node = node.children[char]
            prefix += char
            
            # 如果经过该节点的单词数量为1，说明这是唯一前缀
            if node.count == 1:
                return prefix
        
        # 如果整个单词都不是其他单词的前缀，返回整个单词
        return word

def main():
    # 读取所有单词
    words = []
    for line in sys.stdin:
        line = line.strip()
        if line:
            words.append(line)
    
    # 构建Trie树
    trie = Trie()
    for word in words:
        trie.insert(word)
    
    # 输出每个单词的最短唯一前缀
    for word in words:
        prefix = trie.find_shortest_prefix(word)
        print(f"{word} {prefix}")

if __name__ == "__main__":
    main()
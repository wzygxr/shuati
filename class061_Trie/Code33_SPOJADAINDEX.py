"""
SPOJ ADAINDEX - Ada and Indexing

题目描述：
给定一个字符串列表和多个查询，每个查询给出一个前缀，要求统计以该前缀开头的字符串数量。

解题思路：
这是一个标准的Trie树应用问题。我们可以：
1. 使用Trie树存储所有字符串，在每个节点记录经过该节点的字符串数量
2. 对于每个查询，在Trie树中查找前缀对应的节点
3. 返回该节点记录的字符串数量

时间复杂度：
- 构建Trie树：O(∑len(strings[i]))
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
            node.count += 1  # 增加经过该节点的字符串数量
    
    def count_prefix(self, prefix):
        """
        统计以指定前缀开头的字符串数量
        :param prefix: 要查询的前缀
        :return: 以该前缀开头的字符串数量
        """
        if not prefix:
            return 0
        
        node = self.root
        # 遍历前缀中的每个字符
        for char in prefix:
            if char not in node.children:
                return 0  # 前缀不存在
            node = node.children[char]
        
        # 返回经过该节点的字符串数量
        return node.count

def main():
    """主函数"""
    # 读取输入
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    if not input_lines:
        return
    
    # 解析输入
    line_idx = 0
    while line_idx < len(input_lines):
        if not input_lines[line_idx]:
            line_idx += 1
            continue
            
        parts = input_lines[line_idx].split()
        if len(parts) < 2:
            line_idx += 1
            continue
            
        n, m = int(parts[0]), int(parts[1])
        line_idx += 1
        
        # 构建Trie树
        trie = Trie()
        
        # 插入所有字符串
        for i in range(n):
            if line_idx < len(input_lines):
                word = input_lines[line_idx]
                trie.insert(word)
                line_idx += 1
        
        # 处理所有查询
        for i in range(m):
            if line_idx < len(input_lines):
                prefix = input_lines[line_idx]
                count = trie.count_prefix(prefix)
                print(count)
                line_idx += 1

if __name__ == "__main__":
    main()
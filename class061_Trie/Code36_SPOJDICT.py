"""
SPOJ DICT - Search in the dictionary!

题目描述：
给定一个字典（字符串列表）和多个查询，每个查询给出一个前缀，要求找出字典中所有以该前缀开头的单词，
并按字典序输出。

解题思路：
这是一个标准的Trie树应用问题：
1. 使用Trie树存储字典中的所有单词
2. 对于每个查询，在Trie树中查找前缀对应的节点
3. 从该节点开始深度优先搜索，收集所有单词并按字典序排序输出

时间复杂度：
- 构建Trie树：O(∑len(strings[i]))
- 查询：O(len(prefix) + ∑len(results))
空间复杂度：O(∑len(strings[i]))
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}      # 子节点字典
        self.is_end = False     # 标记是否为单词结尾
        self.word = ""          # 如果是单词结尾，存储完整的单词

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
        
        # 标记单词结尾并存储完整单词
        node.is_end = True
        node.word = word
    
    def find_words_with_prefix(self, prefix):
        """
        查找所有以指定前缀开头的单词
        :param prefix: 要查询的前缀
        :return: 以该前缀开头的所有单词列表（按字典序排序）
        """
        if not prefix:
            return []
        
        # 查找前缀对应的节点
        node = self.root
        for char in prefix:
            if char not in node.children:
                return []  # 前缀不存在
            node = node.children[char]
        
        # 从该节点开始深度优先搜索，收集所有单词
        words = []
        self._dfs_collect_words(node, words)
        return sorted(words)  # 按字典序排序
    
    def _dfs_collect_words(self, node, words):
        """
        深度优先搜索收集所有单词
        :param node: 当前节点
        :param words: 存储单词的列表
        """
        if node.is_end:
            words.append(node.word)
        
        # 按字典序遍历子节点
        for char in sorted(node.children.keys()):
            self._dfs_collect_words(node.children[char], words)

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
    case_num = 1
    
    while line_idx < len(input_lines):
        if not input_lines[line_idx]:
            line_idx += 1
            continue
        
        # 读取字典大小
        n = int(input_lines[line_idx])
        line_idx += 1
        
        # 构建Trie树
        trie = Trie()
        
        # 插入所有单词
        for i in range(n):
            if line_idx < len(input_lines):
                word = input_lines[line_idx]
                trie.insert(word)
                line_idx += 1
        
        # 读取查询数量
        m = int(input_lines[line_idx])
        line_idx += 1
        
        # 处理所有查询
        for i in range(m):
            if line_idx < len(input_lines):
                prefix = input_lines[line_idx]
                words = trie.find_words_with_prefix(prefix)
                
                print(f"Case #{case_num}:")
                if not words:
                    print("No match.")
                else:
                    for word in words:
                        print(word)
                
                case_num += 1
                line_idx += 1

if __name__ == "__main__":
    main()
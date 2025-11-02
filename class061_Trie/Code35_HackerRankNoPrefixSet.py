"""
HackerRank No Prefix Set

题目描述：
给定N个字符串，每个字符串只包含小写字母a-j（包含）。
如果字符串集合中没有字符串是另一个字符串的前缀，则称该字符串集合为GOOD SET。
否则，打印BAD SET，并在下一行打印正在检查的字符串。

注意：如果两个字符串相同，它们互为前缀。

解题思路：
这是一个经典的Trie树应用问题，用于检测前缀关系：
1. 使用Trie树存储字符串
2. 在插入每个字符串时检查前缀关系
3. 如果发现前缀关系，立即返回BAD SET

检测前缀关系的方法：
1. 在插入过程中，如果到达一个已经是单词结尾的节点，说明当前字符串是某个已插入字符串的前缀
2. 如果插入完成后，当前节点还有子节点，说明某个已插入字符串是当前字符串的前缀

时间复杂度：O(∑len(strings[i]))
空间复杂度：O(∑len(strings[i]))
"""

import sys

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}     # 子节点字典
        self.is_end = False    # 标记是否为单词结尾

class Trie:
    """Trie树类"""
    def __init__(self):
        self.root = TrieNode()  # 根节点
    
    def insert_and_check(self, word):
        """
        向Trie树中插入单词并检查前缀关系
        :param word: 要插入的单词
        :return: (success, message) 如果成功返回(True, "")，否则返回(False, 冲突的单词)
        """
        if not word:
            return True, ""
        
        node = self.root
        for i, char in enumerate(word):
            # 如果子节点不存在，创建新节点
            if char not in node.children:
                node.children[char] = TrieNode()
            
            node = node.children[char]
            
            # 如果当前节点已经是某个单词的结尾，说明当前单词是另一个单词的前缀
            if node.is_end:
                return False, word
        
        # 标记当前节点为单词结尾
        node.is_end = True
        
        # 检查当前节点是否有子节点，如果有说明某个单词是当前单词的前缀
        if node.children:
            return False, word
        
        return True, ""

def main():
    """主函数"""
    # 读取输入
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line.strip())
    
    if not input_lines:
        return
    
    # 解析输入
    n = int(input_lines[0])
    
    # 初始化Trie树
    trie = Trie()
    
    # 处理每个字符串
    for i in range(1, n + 1):
        if i >= len(input_lines):
            break
        word = input_lines[i]
        success, conflict_word = trie.insert_and_check(word)
        if not success:
            print("BAD SET")
            print(conflict_word)
            return
    
    print("GOOD SET")

if __name__ == "__main__":
    main()
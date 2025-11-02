"""
LeetCode 1032. 字符流

题目描述：
实现一个数据结构，支持查询字符流的后缀是否为给定字符串数组中的某个字符串。

解题思路：
1. 使用前缀树存储所有单词的逆序
2. 维护字符流的逆序缓冲区
3. 查询时在前缀树中查找字符流后缀的逆序

时间复杂度：
- 初始化：O(∑len(words[i]))
- 查询：O(max(len(query)))
空间复杂度：O(∑len(words[i]))
"""

class TrieNode:
    """Trie树节点类"""
    def __init__(self):
        self.children = {}  # 子节点字典
        self.is_end = False  # 是否为单词结尾

class Code27_LeetCode1032:
    """字符流检查器类"""
    
    def __init__(self, words):
        """
        构造函数
        :param words: 单词数组
        """
        self.root = TrieNode()
        self.stream = []  # 字符流缓冲区
        
        # 将所有单词的逆序插入Trie树
        for word in words:
            self._insert(word[::-1])  # 逆序插入
    
    def _insert(self, word):
        """
        向Trie树中插入一个单词
        :param word: 要插入的单词
        """
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True  # 标记为单词结尾
    
    def query(self, letter):
        """
        查询字符流的后缀是否为给定单词
        :param letter: 新加入的字符
        :return: 是否存在匹配的单词
        """
        # 将新字符添加到字符流缓冲区
        self.stream.append(letter)
        
        # 在Trie树中查找字符流后缀的逆序
        node = self.root
        for i in range(len(self.stream) - 1, -1, -1):
            char = self.stream[i]
            
            # 如果字符不存在，返回False
            if char not in node.children:
                return False
            
            node = node.children[char]
            
            # 如果找到单词结尾，返回True
            if node.is_end:
                return True
        
        return False

def main():
    words = ["cd", "f", "kl"]
    stream_checker = Code27_LeetCode1032(words)
    
    letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l']
    for letter in letters:
        result = stream_checker.query(letter)
        print(f"Query '{letter}': {result}")

if __name__ == "__main__":
    main()
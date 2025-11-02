# -*- coding: utf-8 -*-

'''
题目5: POJ 1451 T9
题目来源：POJ
题目链接：http://poj.org/problem?id=1451

题目描述：
模拟手机T9输入法。手机键盘上每个数字键对应多个字母：
2: abc, 3: def, 4: ghi, 5: jkl, 6: mno, 7: pqrs, 8: tuv, 9: wxyz
给定一些单词及其频率，然后给出按键序列，要求按频率从高到低输出匹配的单词。

解题思路：
1. 构建Trie树存储所有单词及其频率
2. 对于每个节点，维护以该节点为前缀的所有单词中频率最高的单词
3. 对于给定的按键序列，找到对应的Trie树节点，输出该节点存储的最高频率单词

时间复杂度分析：
1. 构建Trie树：O(∑len(s))，其中∑len(s)是所有单词长度之和
2. 查询过程：O(m)，其中m是按键序列长度
3. 总体时间复杂度：O(∑len(s) + ∑m)

空间复杂度分析：
1. Trie树空间：O(∑len(s) * 26)
2. 总体空间复杂度：O(∑len(s))

是否为最优解：是，使用Trie树可以高效地存储和查询单词

工程化考量：
1. 异常处理：输入为空或单词为空的情况
2. 边界情况：相同单词不同频率的情况
3. 极端输入：大量单词或长单词的情况
4. 鲁棒性：处理非法字符的情况

语言特性差异：
Java：使用引用类型，有垃圾回收机制，HashMap实现动态子节点
C++：需要手动管理内存，可以使用数组或指针数组实现
Python：动态类型语言，字典实现自然，但性能不如编译型语言

与实际应用的联系：
1. 输入法：T9输入法预测
2. 搜索引擎：关键词预测
3. 自动补全：代码编辑器中的自动补全功能
'''


class T9TrieNode:
    """
    T9 Trie树节点类
    """
    def __init__(self):
        # 以该节点为前缀的所有单词中的最大频率
        self.max_freq = 0
        # 对应最大频率的单词
        self.max_word = ""
        # 子节点映射
        self.children = {}


class T9Trie:
    """
    T9 Trie树类
    """
    def __init__(self):
        # 根节点
        self.root = T9TrieNode()
        # 数字到字母的映射
        self.digit_to_letters = {
            '2': "abc",
            '3': "def",
            '4': "ghi",
            '5': "jkl",
            '6': "mno",
            '7': "pqrs",
            '8': "tuv",
            '9': "wxyz"
        }
    
    def insert(self, word, freq):
        """
        插入单词及其频率
        
        时间复杂度：O(len(word))
        空间复杂度：O(len(word))
        
        :param word: 单词
        :param freq: 频率
        """
        node = self.root
        # 更新根节点的信息
        if freq > node.max_freq:
            node.max_freq = freq
            node.max_word = word
        
        for ch in word:
            # 如果字符不在当前节点的子节点中，则创建新节点
            if ch not in node.children:
                node.children[ch] = T9TrieNode()
            node = node.children[ch]
            # 更新当前节点的信息
            if freq > node.max_freq:
                node.max_freq = freq
                node.max_word = word
    
    def find_most_likely_word(self, digits):
        """
        根据按键序列查找最可能的单词
        
        时间复杂度：O(len(digits))
        空间复杂度：O(len(digits))
        
        :param digits: 按键序列
        :return: 最可能的单词
        """
        node = self.root
        result = []
        
        for digit in digits:
            if digit not in self.digit_to_letters:
                # 非法数字，直接返回空字符串
                return ""
            
            letters = self.digit_to_letters[digit]
            # 找到第一个匹配的子节点
            next_node = None
            for letter in letters:
                if letter in node.children:
                    next_node = node.children[letter]
                    result.append(letter)
                    break
            
            if next_node is None:
                # 没有匹配的子节点，返回当前找到的部分
                break
            node = next_node
        
        return "".join(result)
    
    def get_most_frequent_word(self, prefix):
        """
        获取指定前缀下的最高频率单词
        
        时间复杂度：O(len(prefix))
        空间复杂度：O(1)
        
        :param prefix: 前缀
        :return: 最高频率单词
        """
        node = self.root
        for ch in prefix:
            if ch not in node.children:
                return ""
            node = node.children[ch]
        return node.max_word


def t9_input(words, frequencies, digits):
    """
    T9输入法模拟
    
    算法思路：
    1. 构建Trie树存储所有单词及其频率
    2. 对于每个节点，维护以该节点为前缀的所有单词中频率最高的单词
    3. 对于给定的按键序列，找到对应的Trie树节点，输出该节点存储的最高频率单词
    
    时间复杂度：O(∑len(s) + ∑m)
    空间复杂度：O(∑len(s))
    
    :param words: 单词数组
    :param frequencies: 频率数组
    :param digits: 按键序列
    :return: 最可能的单词
    """
    trie = T9Trie()
    
    # 构建Trie树
    for i in range(len(words)):
        trie.insert(words[i], frequencies[i])
    
    # 查找最可能的单词
    return trie.find_most_likely_word(digits)


# 测试方法
if __name__ == "__main__":
    # 测试用例
    words = ["apple", "application", "apply", "banana", "band", "bandana"]
    frequencies = [50, 30, 20, 40, 25, 15]
    digits = "27753"  # 对应apple
    
    result = t9_input(words, frequencies, digits)
    print(f"输入按键序列 {digits} 最可能的单词是: {result}")
    
    # 另一个测试用例
    digits2 = "226"  # 对应banana
    result2 = t9_input(words, frequencies, digits2)
    print(f"输入按键序列 {digits2} 最可能的单词是: {result2}")
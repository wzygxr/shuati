# -*- coding: utf-8 -*-

"""
LeetCode 1032. Stream of Characters
题目链接：https://leetcode.com/problems/stream-of-characters/
题目描述：设计一个算法，接收一个字符流，并检查这些字符的后缀是否是字符串数组words中的一个字符串

算法详解：
这是一道典型的AC自动机应用题。由于需要检查字符流的后缀是否匹配模式串，
我们可以将模式串反转后构建AC自动机，然后在字符流中进行匹配。

算法核心思想：
1. 将所有模式串反转后插入到Trie树中
2. 构建失配指针（fail指针）
3. 在字符流中进行匹配，每次匹配当前字符，利用fail指针避免回溯

时间复杂度分析：
1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
2. 构建fail指针：O(∑|Pi|)
3. 查询：O(|T|)，其中T是文本串
总时间复杂度：O(∑|Pi| + |T|)

空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小

适用场景：
1. 字符流匹配
2. 后缀匹配问题

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 性能优化：使用字典代替列表提高访问速度
3. 内存优化：合理使用Python的数据结构

与机器学习的联系：
1. 在自然语言处理中用于实时文本分析
2. 在网络安全中用于实时恶意代码检测
"""

from collections import deque

class StreamChecker:
    def __init__(self, words):
        """
        初始化StreamChecker
        :param words: 模式串列表
        """
        self.words = words
        self.root = {}
        self.current = self.root
        
        # 构建Trie树
        self.build_trie()
        
        # 构建AC自动机
        self.build_ac_automation()
    
    def build_trie(self):
        """
        构建Trie树
        """
        for word in self.words:
            node = self.root
            # 反转字符串插入Trie树
            for i in range(len(word) - 1, -1, -1):
                char = word[i]
                if char not in node:
                    node[char] = {}
                node = node[char]
            node['#'] = True  # 标记单词结尾
    
    def build_ac_automation(self):
        """
        构建AC自动机
        """
        # 初始化根节点的失配指针
        self.root['fail'] = self.root
        
        queue = deque()
        # 处理根节点的子节点
        for char in self.root:
            if char != 'fail' and char != '#':
                child = self.root[char]
                child['fail'] = self.root
                queue.append(child)
        
        # BFS构建失配指针
        while queue:
            node = queue.popleft()
            
            for char in node:
                if char != 'fail' and char != '#':
                    child = node[char]
                    queue.append(child)
                    
                    # 查找失配指针
                    fail_node = node['fail']
                    while char not in fail_node and fail_node != self.root:
                        fail_node = fail_node['fail']
                    
                    if char in fail_node:
                        child['fail'] = fail_node[char]
                    else:
                        child['fail'] = self.root
    
    def query(self, letter):
        """
        查询字符流中是否有匹配的后缀
        :param letter: 新加入的字符
        :return: 是否有匹配的后缀
        """
        # 根据失配指针跳转
        while letter not in self.current and self.current != self.root:
            self.current = self.current['fail']
        
        if letter in self.current:
            self.current = self.current[letter]
        else:
            self.current = self.root
        
        # 检查是否有匹配的模式串
        temp = self.current
        while temp != self.root:
            if '#' in temp:
                return True
            temp = temp['fail']
        
        return False

# 测试方法
def main():
    words = ["cd", "f", "kl"]
    stream_checker = StreamChecker(words)
    
    letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l']
    for letter in letters:
        result = stream_checker.query(letter)
        print(f"Query '{letter}': {result}")

if __name__ == "__main__":
    main()
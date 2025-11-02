# -*- coding: utf-8 -*-

"""
HDU 2222 Keywords Search
题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2222
题目描述：给定一些单词和一个字符串，求有多少单词在字符串中出现过

算法详解：
这是一道经典的AC自动机模板题。需要在文本中查找多个模式串的出现次数。

算法核心思想：
1. 将所有模式串插入到Trie树中
2. 构建失配指针（fail指针）
3. 在文本中进行匹配，统计每个模式串的出现次数

时间复杂度分析：
1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个模式串
2. 构建fail指针：O(∑|Pi|)
3. 匹配：O(|T|)，其中T是文本串
总时间复杂度：O(∑|Pi| + |T|)

空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小

适用场景：
1. 多模式串匹配
2. 关键词搜索

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 性能优化：使用字典代替列表提高访问速度
3. 内存优化：合理使用Python的数据结构

与机器学习的联系：
1. 在自然语言处理中用于关键词提取
2. 在网络安全中用于恶意代码检测
"""

from collections import deque

class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False
        self.fail = None  # type: ignore
        self.count = 0  # 匹配次数
        self.word_id = -1  # 单词编号

class KeywordsSearch:
    def __init__(self):
        self.root = TrieNode()
        self.word_count = []  # 每个单词的出现次数
    
    def build_trie(self, patterns):
        """
        构建Trie树
        :param patterns: 模式串列表
        """
        for i, pattern in enumerate(patterns):
            node = self.root
            for char in pattern:
                if char not in node.children:
                    node.children[char] = TrieNode()
                node = node.children[char]
            node.is_end = True
            node.word_id = i
    
    def build_ac_automation(self):
        """
        构建AC自动机
        """
        # 初始化根节点的失配指针
        self.root.fail = self.root  # type: ignore
        
        queue = deque()
        # 处理根节点的子节点
        for char in self.root.children:
            child = self.root.children[char]
            child.fail = self.root
            queue.append(child)
        
        # BFS构建失配指针
        while queue:
            node = queue.popleft()
            
            for char in node.children:
                child = node.children[char]
                queue.append(child)
                
                # 查找失配指针
                fail_node = node.fail
                while char not in fail_node.children and fail_node != self.root:
                    fail_node = fail_node.fail  # type: ignore
                
                if char in fail_node.children:
                    child.fail = fail_node.children[char]
                else:
                    child.fail = self.root
    
    def match_text(self, text):
        """
        匹配文本
        :param text: 文本串
        :return: 总匹配次数
        """
        current = self.root  # type: TrieNode
        total_matches = 0
        
        for char in text:
            index = char
            
            # 根据失配指针跳转
            while index not in current.children and current != self.root:
                current = current.fail  # type: ignore
            
            if index in current.children:
                current = current.children[index]
            else:
                current = self.root
            
            # 检查是否有匹配的模式串
            temp = current  # type: TrieNode
            while temp != self.root:
                if temp.is_end:
                    temp.count += 1
                    total_matches += 1
                temp = temp.fail  # type: ignore
        
        return total_matches
    
    def count_words(self, patterns):
        """
        统计每个单词的出现次数
        :param patterns: 模式串列表
        """
        self.word_count = [0] * len(patterns)
        
        # 使用BFS遍历Trie树，将匹配次数传递给父节点
        queue = deque()
        queue.append(self.root)
        
        while queue:
            node = queue.popleft()
            
            # 将当前节点的匹配次数传递给fail节点
            if node != self.root and node.fail is not None:
                node.fail.count += node.count
            
            # 将子节点加入队列
            for char in node.children:
                queue.append(node.children[char])
            
            # 如果是单词结尾，记录匹配次数
            if node.is_end and node.word_id != -1:
                self.word_count[node.word_id] = node.count

def main():
    # 示例输入（实际应用中需要从标准输入读取）
    patterns = ["she", "he", "say", "shr", "her"]
    text = "yasherhs"
    
    searcher = KeywordsSearch()
    
    # 构建Trie树
    searcher.build_trie(patterns)
    
    # 构建AC自动机
    searcher.build_ac_automation()
    
    # 匹配文本
    total_matches = searcher.match_text(text)
    
    # 统计每个单词的出现次数
    searcher.count_words(patterns)
    
    print(f"总匹配次数: {total_matches}")
    for i, pattern in enumerate(patterns):
        print(f"{pattern}: {searcher.word_count[i]}")

if __name__ == "__main__":
    main()
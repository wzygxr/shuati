#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
AC自动机实现 (Aho-Corasick Automaton) (Python版本)

AC自动机是一种多模式字符串匹配算法，能够在一次扫描中同时匹配多个模式串。

算法原理：
1. 构建Trie树：将所有模式串插入到Trie树中
2. 构建fail指针：为Trie树中的每个节点构建失败指针
3. 匹配过程：在文本串中进行匹配，利用fail指针避免重复匹配

时间复杂度：
- 预处理：O(∑|Pi|)，其中∑|Pi|是所有模式串长度之和
- 匹配：O(n + z)，其中n是文本串长度，z是匹配次数

空间复杂度：O(∑|Pi| × |Σ|)，其中|Σ|是字符集大小

优势：
1. 支持多模式匹配
2. 匹配效率高
3. 适合处理大量模式串的场景

劣势：
1. 实现复杂度较高
2. 空间消耗较大
3. 对于少量模式串可能不如KMP算法高效

应用场景：
1. 关键词过滤
2. 病毒特征码检测
3. 生物信息学中的序列匹配
4. 网络入侵检测
"""

from collections import deque
from typing import List, Optional

class ACAutomaton:
    """AC自动机实现"""
    
    class Node:
        """Trie树节点"""
        
        def __init__(self):
            self.children = {}  # 子节点字典
            self.fail: Optional['ACAutomaton.Node'] = None    # 失败指针
            self.output = []    # 输出列表，存储以该节点结尾的模式串索引
            self.is_end = False # 是否为某个模式串的结尾
    
    def __init__(self):
        """构造函数"""
        self.root = self.Node()
        self.patterns = []  # 模式串列表
    
    def add_pattern(self, pattern: str) -> None:
        """
        添加模式串
        :param pattern: 模式串
        """
        self.patterns.append(pattern)
        current = self.root
        
        # 将模式串插入到Trie树中
        for char in pattern:
            if char not in current.children:
                current.children[char] = self.Node()
            current = current.children[char]
        
        # 标记该节点为模式串结尾
        current.is_end = True
        current.output.append(len(self.patterns) - 1)
    
    def build_fail_pointer(self) -> None:
        """构建失败指针"""
        queue = deque()
        
        # 初始化根节点的子节点的失败指针
        for char in self.root.children:
            child = self.root.children[char]
            child.fail = self.root
            queue.append(child)
        
        # BFS构建失败指针
        while queue:
            current = queue.popleft()
            
            for char, child in current.children.items():
                fail_node = current.fail
                
                # 找到失败指针指向的节点
                while fail_node is not None and fail_node != self.root and char not in fail_node.children:
                    fail_node = fail_node.fail
                
                if fail_node is not None and char in fail_node.children and fail_node.children[char] != child:
                    child.fail = fail_node.children[char]
                else:
                    child.fail = self.root
                
                # 合并输出列表
                if child.fail is not None and child.fail.is_end:
                    child.output.extend(child.fail.output)
                
                queue.append(child)
    
    def search(self, text: str) -> List['ACAutomaton.MatchResult']:
        """
        在文本中查找所有模式串
        :param text: 文本串
        :return: 匹配结果列表，每个元素包含模式串索引和在文本中的位置
        """
        results = []
        current: Optional['ACAutomaton.Node'] = self.root
        
        for i, char in enumerate(text):
            # 如果当前节点没有对应的子节点，则沿着失败指针查找
            while current is not None and current != self.root and char not in current.children:
                current = current.fail
            
            # 移动到下一个节点
            if current is not None and char in current.children:
                current = current.children[char]
            
            # 检查是否有模式串匹配
            if current is not None and (current.is_end or current.output):
                for pattern_index in current.output:
                    pattern = self.patterns[pattern_index]
                    position = i - len(pattern) + 1
                    results.append(self.MatchResult(pattern_index, pattern, position))
                
                # 更新current以避免None错误
                if current is None:
                    current = self.root
        
        return results
    
    def search_optimized(self, text: str) -> List['ACAutomaton.MatchResult']:
        """
        在文本中查找所有模式串（优化版本）
        :param text: 文本串
        :return: 匹配结果列表，每个元素包含模式串索引和在文本中的位置
        """
        results = []
        current: Optional['ACAutomaton.Node'] = self.root
        
        for i, char in enumerate(text):
            # 沿着失败指针查找直到找到匹配的子节点或回到根节点
            while current is not None and current != self.root and char not in current.children:
                current = current.fail
            
            # 移动到下一个节点
            if current is not None and char in current.children:
                current = current.children[char]
            
            # 沿着失败指针收集所有匹配结果
            temp = current
            while temp is not None and temp != self.root:
                if temp.is_end:
                    for pattern_index in temp.output:
                        pattern = self.patterns[pattern_index]
                        position = i - len(pattern) + 1
                        results.append(self.MatchResult(pattern_index, pattern, position))
                temp = temp.fail
        
        return results
    
    def get_pattern_count(self) -> int:
        """
        获取模式串数量
        :return: 模式串数量
        """
        return len(self.patterns)
    
    def get_patterns(self) -> List[str]:
        """
        获取所有模式串
        :return: 模式串列表
        """
        return self.patterns[:]
    
    class MatchResult:
        """匹配结果类"""
        
        def __init__(self, pattern_index: int, pattern: str, position: int):
            self.pattern_index = pattern_index  # 模式串索引
            self.pattern = pattern              # 模式串
            self.position = position            # 在文本中的位置
        
        def __str__(self) -> str:
            return f"Pattern[{self.pattern_index}]='{self.pattern}' at position {self.position}"
        
        def __repr__(self) -> str:
            return self.__str__()


def main():
    """测试方法"""
    # 创建AC自动机
    ac = ACAutomaton()
    
    # 添加模式串
    ac.add_pattern("he")
    ac.add_pattern("she")
    ac.add_pattern("his")
    ac.add_pattern("hers")
    
    # 构建失败指针
    ac.build_fail_pointer()
    
    # 测试文本
    text = "ushers"
    print(f"文本: {text}")
    print(f"模式串: {ac.get_patterns()}")
    
    # 搜索匹配
    results = ac.search_optimized(text)
    print("\n匹配结果:")
    for result in results:
        print(result)
    
    # 更复杂的测试
    print("\n=== 复杂测试 ===")
    ac2 = ACAutomaton()
    ac2.add_pattern("abc")
    ac2.add_pattern("bcd")
    ac2.add_pattern("cde")
    ac2.add_pattern("abcdef")
    ac2.build_fail_pointer()
    
    text2 = "abcdefg"
    print(f"文本: {text2}")
    print(f"模式串: {ac2.get_patterns()}")
    
    results2 = ac2.search_optimized(text2)
    print("\n匹配结果:")
    for result in results2:
        print(result)


if __name__ == "__main__":
    main()
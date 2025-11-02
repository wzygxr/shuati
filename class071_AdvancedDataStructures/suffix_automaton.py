#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
后缀自动机（Suffix Automaton）实现
后缀自动机是一个能够表示一个字符串的所有子串的最小DFA
主要特性：
1. 状态数和转移边数都是O(n)级别
2. 每个状态代表一个endpos等价类
3. 支持高效查询子串、不同子串计数等操作
时间复杂度：构建O(n)，查询O(m)
空间复杂度：O(n)
"""

from typing import Dict, List, Optional

class SuffixAutomaton:
    """
    后缀自动机类
    用于高效处理字符串的子串查询、计数等操作
    """
    
    class State:
        """
        后缀自动机的状态节点
        """
        def __init__(self, length: int):
            self.next: Dict[str, int] = {}  # 转移函数
            self.length = length           # 该状态能接受的最长子串长度
            self.link = -1                 # 后缀链接（suffix link）
            self.endpos_size = 0           # endpos集合的大小
            self.is_clone = False          # 是否是克隆节点
    
    def __init__(self, text: str):
        """
        构造函数，构建后缀自动机
        
        Args:
            text (str): 输入文本
            
        Raises:
            ValueError: 如果输入文本为None
        """
        if text is None:
            raise ValueError("输入文本不能为None")
        
        self.text = text
        self.states: List[SuffixAutomaton.State] = []
        self.last = 0          # 上一个状态的索引
        self.size = 1          # 当前状态数量
        
        # 创建初始状态
        self.states.append(SuffixAutomaton.State(0))
        
        # 逐个字符构建自动机
        for char in text:
            self._extend(char)
        
        # 计算endpos集合大小
        self._calculate_endpos_size()
    
    def _extend(self, char: str) -> None:
        """
        扩展后缀自动机，添加一个字符
        
        Args:
            char (str): 要添加的字符
        """
        # 创建新状态cur
        cur = self.size
        self.size += 1
        self.states.append(SuffixAutomaton.State(self.states[self.last].length + 1))
        self.states[cur].endpos_size = 1  # 新状态的endpos大小为1
        
        # 从last开始，沿着后缀链接回溯，添加转移
        p = self.last
        while p >= 0 and char not in self.states[p].next:
            self.states[p].next[char] = cur
            p = self.states[p].link
        
        if p == -1:
            # 如果没有找到含有char转移的状态，将cur的后缀链接指向初始状态
            self.states[cur].link = 0
        else:
            q = self.states[p].next[char]
            if self.states[p].length + 1 == self.states[q].length:
                # 如果q已经是p通过char转移后的正确状态
                self.states[cur].link = q
            else:
                # 需要分裂状态q
                clone = self.size
                self.size += 1
                self.states.append(SuffixAutomaton.State(self.states[p].length + 1))
                self.states[clone].next = self.states[q].next.copy()  # 复制转移
                self.states[clone].link = self.states[q].link        # 复制后缀链接
                self.states[clone].is_clone = True                   # 标记为克隆节点
                
                # 更新q和cur的后缀链接
                self.states[q].link = clone
                self.states[cur].link = clone
                
                # 从p开始，沿着后缀链接回溯，更新转移
                while p >= 0 and self.states[p].next.get(char) == q:
                    self.states[p].next[char] = clone
                    p = self.states[p].link
        
        # 更新last为新状态
        self.last = cur
    
    def _calculate_endpos_size(self) -> None:
        """
        计算每个状态的endpos集合大小
        基于后缀链接树进行后序遍历累加
        """
        # 根据length对状态进行排序（用于后序遍历后缀链接树）
        order = list(range(self.size))
        order.sort(key=lambda x: -self.states[x].length)
        
        # 后序遍历，累加endpos大小
        for v in order:
            if self.states[v].link != -1 and not self.states[v].is_clone:
                self.states[self.states[v].link].endpos_size += self.states[v].endpos_size
    
    def contains(self, s: str) -> bool:
        """
        检查字符串s是否是原始文本的子串
        
        Args:
            s (str): 要检查的字符串
            
        Returns:
            bool: 如果是子串返回True，否则返回False
            
        Raises:
            ValueError: 如果查询字符串为None
        """
        if s is None:
            raise ValueError("查询字符串不能为None")
        
        state = 0  # 从初始状态开始
        for char in s:
            if char not in self.states[state].next:
                return False  # 没有对应的转移，不是子串
            state = self.states[state].next[char]
        return True  # 成功匹配所有字符
    
    def count_distinct_substrings(self) -> int:
        """
        计算不同子串的数量
        利用性质：不同子串数量 = Σ (length[state] - length[link[state]])
        
        Returns:
            int: 不同子串的数量
        """
        count = 0
        for i in range(1, self.size):  # 跳过初始状态
            count += self.states[i].length - self.states[self.states[i].link].length
        return count
    
    def count_occurrences(self, s: str) -> int:
        """
        计算子串s在原文本中出现的次数
        
        Args:
            s (str): 要查询的子串
            
        Returns:
            int: 出现次数
            
        Raises:
            ValueError: 如果查询字符串为None
        """
        if s is None:
            raise ValueError("查询字符串不能为None")
        
        # 找到对应s的状态
        state = 0
        for char in s:
            if char not in self.states[state].next:
                return 0  # 不是子串，出现次数为0
            state = self.states[state].next[char]
        
        return self.states[state].endpos_size
    
    def find_longest_substring_with_k_occurrences(self, k: int) -> str:
        """
        找出所有出现次数至少为k次的子串中，最长的那个
        
        Args:
            k (int): 最小出现次数
            
        Returns:
            str: 最长的满足条件的子串
            
        Raises:
            ValueError: 如果k不为正整数
        """
        if k <= 0:
            raise ValueError("k必须为正整数")
        
        max_length = 0
        
        # 遍历所有状态，找到endpos_size >= k的状态，且length最大
        for i in range(1, self.size):
            if self.states[i].endpos_size >= k and self.states[i].length > max_length:
                max_length = self.states[i].length
        
        if max_length == 0:
            return ""
        
        # 找到对应的子串
        current = []
        return self._find_substring_by_length(0, max_length, current)
    
    def _find_substring_by_length(self, state: int, target_length: int, current: list) -> Optional[str]:
        """
        递归查找指定长度的子串
        
        Args:
            state (int): 当前状态
            target_length (int): 目标长度
            current (list): 当前构建的字符串列表
            
        Returns:
            Optional[str]: 找到的子串或None
        """
        if self.states[state].length == target_length:
            return ''.join(current)
        
        for char, next_state in self.states[state].next.items():
            if self.states[next_state].length <= target_length:
                current.append(char)
                result = self._find_substring_by_length(next_state, target_length, current)
                if result is not None:
                    return result
                current.pop()
        
        return None
    
    def find_longest_repeated_substring(self) -> str:
        """
        找出文本的最长重复子串
        
        Returns:
            str: 最长重复子串
        """
        return self.find_longest_substring_with_k_occurrences(2)
    
    def get_state_count(self) -> int:
        """
        获取后缀自动机的状态数量
        
        Returns:
            int: 状态数量
        """
        return self.size
    
    def __str__(self) -> str:
        """
        获取后缀自动机的信息
        
        Returns:
            str: 状态和转移信息的字符串表示
        """
        result = []
        result.append("后缀自动机状态信息：")
        result.append(f"文本: {self.text}")
        result.append(f"状态数量: {self.size}")
        
        for i in range(self.size):
            state = self.states[i]
            state_info = f"状态 {i}: length={state.length}, link={state.link}, endpos_size={state.endpos_size}"
            if state.is_clone:
                state_info += " (克隆)"
            result.append(state_info)
            
            transitions = "  转移: "
            for char, next_state in state.next.items():
                transitions += f"[{char} -> {next_state}] "
            result.append(transitions)
        
        return '\n'.join(result)

# 测试代码
if __name__ == "__main__":
    # 测试用例1：基本功能测试
    text1 = "banana"
    sam1 = SuffixAutomaton(text1)
    print("=== 测试用例1 ===")
    print(f"文本: {text1}")
    print(f"状态数量: {sam1.get_state_count()}")
    print(f"不同子串数量: {sam1.count_distinct_substrings()}")  # 应该是15
    
    # 测试子串检查
    print(f"包含'an': {sam1.contains('an')}")  # True
    print(f"包含'na': {sam1.contains('na')}")  # True
    print(f"包含'xyz': {sam1.contains('xyz')}")  # False
    
    # 测试出现次数
    print(f"'a'出现次数: {sam1.count_occurrences('a')}")  # 3
    print(f"'na'出现次数: {sam1.count_occurrences('na')}")  # 2
    
    # 测试最长重复子串
    print(f"最长重复子串: {sam1.find_longest_repeated_substring()}")  # "ana"
    
    # 测试用例2：边界情况
    text2 = "aaa"
    sam2 = SuffixAutomaton(text2)
    print("\n=== 测试用例2 ===")
    print(f"文本: {text2}")
    print(f"不同子串数量: {sam2.count_distinct_substrings()}")  # 3
    print(f"最长重复子串: {sam2.find_longest_repeated_substring()}")  # "aa"
    
    # 测试用例3：更长文本
    text3 = "mississippi"
    sam3 = SuffixAutomaton(text3)
    print("\n=== 测试用例3 ===")
    print(f"文本: {text3}")
    print(f"状态数量: {sam3.get_state_count()}")
    print(f"包含'issi': {sam3.contains('issi')}")  # True
    print(f"'issi'出现次数: {sam3.count_occurrences('issi')}")  # 2
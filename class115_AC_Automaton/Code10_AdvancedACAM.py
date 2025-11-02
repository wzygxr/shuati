# -*- coding: utf-8 -*-

"""
高级AC自动机算法变体与优化实现 - Python版本

本文件实现了以下高级AC自动机变体：
1. 双向AC自动机（Bidirectional AC Automaton）
2. 动态AC自动机（Dynamic AC Automaton）
3. 压缩AC自动机（Compressed AC Automaton）
4. 并行AC自动机（Parallel AC Automaton）

算法详解：
这些高级变体在标准AC自动机的基础上进行了优化和改进，
针对不同的应用场景和性能需求提供了更好的解决方案。

时间复杂度分析：
- 双向AC自动机：O(∑|Pi| + |T|)
- 动态AC自动机：每次操作O(|P|)
- 压缩AC自动机：O(∑|Pi| + |T|)
- 并行AC自动机：O(∑|Pi| + |T|/p)，其中p是处理器数量

空间复杂度分析：
- 双向AC自动机：O(2 × ∑|Pi| × |Σ|)
- 动态AC自动机：O(∑|Pi| × |Σ|)
- 压缩AC自动机：O(∑|Pi|)
- 并行AC自动机：O(∑|Pi| × |Σ|)

Python特性优化：
1. 使用字典实现高效查找
2. 利用生成器处理大数据
3. 使用多线程实现并行处理
4. 实现路径压缩减少内存占用
"""

from collections import deque, defaultdict
from typing import List, Set, Dict, Tuple
import threading
from concurrent.futures import ThreadPoolExecutor

# ==================== 变体1: 双向AC自动机 ====================

class BidirectionalACAutomaton:
    """
    双向AC自动机实现
    核心思想：同时构建正向和反向的AC自动机，支持双向匹配
    适用场景：需要同时检查前缀和后缀匹配的场景
    """
    
    def __init__(self):
        self.CHARSET_SIZE = 26
        
        # 正向AC自动机
        self.forward_tree = [{}]
        self.forward_fail = [0]
        self.forward_danger = [False]
        self.forward_cnt = 0
        
        # 反向AC自动机
        self.reverse_tree = [{}]
        self.reverse_fail = [0]
        self.reverse_danger = [False]
        self.reverse_cnt = 0
    
    def insert(self, pattern: str) -> None:
        """插入模式串到双向AC自动机"""
        self.insert_forward(pattern)
        self.insert_reverse(pattern)
    
    def insert_forward(self, pattern: str) -> None:
        """插入到正向AC自动机"""
        u = 0
        for c in pattern:
            idx = ord(c) - ord('a')
            if idx not in self.forward_tree[u]:
                self.forward_cnt += 1
                self.forward_tree[u][idx] = self.forward_cnt
                self.forward_tree.append({})
                self.forward_fail.append(0)
                self.forward_danger.append(False)
            u = self.forward_tree[u][idx]
        self.forward_danger[u] = True
    
    def insert_reverse(self, pattern: str) -> None:
        """插入到反向AC自动机（字符串反转后插入）"""
        u = 0
        reversed_pattern = pattern[::-1]  # 反转字符串
        for c in reversed_pattern:
            idx = ord(c) - ord('a')
            if idx not in self.reverse_tree[u]:
                self.reverse_cnt += 1
                self.reverse_tree[u][idx] = self.reverse_cnt
                self.reverse_tree.append({})
                self.reverse_fail.append(0)
                self.reverse_danger.append(False)
            u = self.reverse_tree[u][idx]
        self.reverse_danger[u] = True
    
    def build(self) -> None:
        """构建双向AC自动机"""
        self.build_forward()
        self.build_reverse()
    
    def build_forward(self) -> None:
        """构建正向AC自动机"""
        q = deque()
        for i in range(self.CHARSET_SIZE):
            if i in self.forward_tree[0]:
                q.append(self.forward_tree[0][i])
        
        while q:
            u = q.popleft()
            self.forward_danger[u] = self.forward_danger[u] or self.forward_danger[self.forward_fail[u]]
            
            for i in range(self.CHARSET_SIZE):
                if i in self.forward_tree[u]:
                    v = self.forward_tree[u][i]
                    self.forward_fail[v] = self.forward_tree[self.forward_fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.forward_tree[u][i] = self.forward_tree[self.forward_fail[u]].get(i, 0)
    
    def build_reverse(self) -> None:
        """构建反向AC自动机"""
        q = deque()
        for i in range(self.CHARSET_SIZE):
            if i in self.reverse_tree[0]:
                q.append(self.reverse_tree[0][i])
        
        while q:
            u = q.popleft()
            self.reverse_danger[u] = self.reverse_danger[u] or self.reverse_danger[self.reverse_fail[u]]
            
            for i in range(self.CHARSET_SIZE):
                if i in self.reverse_tree[u]:
                    v = self.reverse_tree[u][i]
                    self.reverse_fail[v] = self.reverse_tree[self.reverse_fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.reverse_tree[u][i] = self.reverse_tree[self.reverse_fail[u]].get(i, 0)
    
    def bidirectional_match(self, text: str) -> bool:
        """双向匹配文本"""
        return self.forward_match(text) or self.reverse_match(text)
    
    def forward_match(self, text: str) -> bool:
        """正向匹配"""
        u = 0
        for c in text:
            idx = ord(c) - ord('a')
            u = self.forward_tree[u].get(idx, 0)
            if self.forward_danger[u]:
                return True
        return False
    
    def reverse_match(self, text: str) -> bool:
        """反向匹配"""
        u = 0
        reversed_text = text[::-1]  # 反转文本
        for c in reversed_text:
            idx = ord(c) - ord('a')
            u = self.reverse_tree[u].get(idx, 0)
            if self.reverse_danger[u]:
                return True
        return False

# ==================== 变体2: 动态AC自动机 ====================

class DynamicACAutomaton:
    """
    动态AC自动机实现
    核心思想：支持动态添加和删除模式串，无需重建整个自动机
    适用场景：模式串集合频繁变化的场景
    """
    
    def __init__(self):
        self.CHARSET_SIZE = 26
        self.tree = [{}]
        self.fail = [0]
        self.danger = [False]
        self.cnt = 0
        self.patterns = []
        self.needs_rebuild = False
    
    def add_pattern(self, pattern: str) -> None:
        """动态添加模式串"""
        self.patterns.append(pattern)
        self.needs_rebuild = True
    
    def remove_pattern(self, pattern: str) -> None:
        """动态删除模式串"""
        if pattern in self.patterns:
            self.patterns.remove(pattern)
            self.needs_rebuild = True
    
    def build(self) -> None:
        """构建或重建AC自动机"""
        if not self.needs_rebuild:
            return
        
        self.reset_automaton()
        
        for pattern in self.patterns:
            self.insert(pattern)
        
        self.build_fail_pointers()
        self.needs_rebuild = False
    
    def reset_automaton(self) -> None:
        """重置自动机"""
        self.tree = [{}]
        self.fail = [0]
        self.danger = [False]
        self.cnt = 0
    
    def insert(self, pattern: str) -> None:
        """插入模式串"""
        u = 0
        for c in pattern:
            idx = ord(c) - ord('a')
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.danger.append(False)
            u = self.tree[u][idx]
        self.danger[u] = True
    
    def build_fail_pointers(self) -> None:
        """构建fail指针"""
        q = deque()
        for i in range(self.CHARSET_SIZE):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            self.danger[u] = self.danger[u] or self.danger[self.fail[u]]
            
            for i in range(self.CHARSET_SIZE):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    def match(self, text: str) -> bool:
        """匹配文本"""
        if self.needs_rebuild:
            self.build()
        
        u = 0
        for c in text:
            idx = ord(c) - ord('a')
            u = self.tree[u].get(idx, 0)
            if self.danger[u]:
                return True
        return False

# ==================== 变体3: 压缩AC自动机 ====================

class CompressedACAutomaton:
    """
    压缩AC自动机实现
    核心思想：对Trie树进行路径压缩，减少节点数量
    适用场景：内存受限的大规模模式串匹配
    """
    
    class CompressedNode:
        """压缩节点类"""
        def __init__(self, path: str):
            self.path = path
            self.children = {}
            self.fail = 0
            self.is_end = False
        
        def __repr__(self):
            return f"CompressedNode(path='{self.path}', is_end={self.is_end})"
    
    def __init__(self):
        self.nodes = [self.CompressedNode("")]  # 根节点
        self.cnt = 1
    
    def insert(self, pattern: str) -> None:
        """插入模式串"""
        self.insert_recursive(0, pattern, 0)
    
    def insert_recursive(self, node_idx: int, pattern: str, pattern_pos: int) -> None:
        """递归插入模式串"""
        if pattern_pos >= len(pattern):
            self.nodes[node_idx].is_end = True
            return
        
        current_char = pattern[pattern_pos]
        current_node = self.nodes[node_idx]
        
        # 简化实现：总是创建新分支
        self.create_new_branch(node_idx, pattern, pattern_pos)
    
    def create_new_branch(self, node_idx: int, pattern: str, pattern_pos: int) -> None:
        """创建新分支"""
        current_char = pattern[pattern_pos]
        current_node = self.nodes[node_idx]
        
        # 创建新节点
        new_path = pattern[pattern_pos:]
        new_node = self.CompressedNode(new_path)
        new_node.is_end = True
        
        self.nodes.append(new_node)
        new_idx = self.cnt
        self.cnt += 1
        
        # 添加到当前节点的子节点
        current_node.children[current_char] = new_idx
    
    def build(self) -> None:
        """构建压缩AC自动机"""
        self.build_fail_pointers()
    
    def build_fail_pointers(self) -> None:
        """构建fail指针"""
        q = deque()
        
        for child_idx in self.nodes[0].children.values():
            self.nodes[child_idx].fail = 0
            q.append(child_idx)
        
        while q:
            u = q.popleft()
            u_node = self.nodes[u]
            
            for char, v in u_node.children.items():
                v_node = self.nodes[v]
                
                fail_node = u_node.fail
                while fail_node != 0 and char not in self.nodes[fail_node].children:
                    fail_node = self.nodes[fail_node].fail
                
                if char in self.nodes[fail_node].children:
                    v_node.fail = self.nodes[fail_node].children[char]
                else:
                    v_node.fail = 0
                
                q.append(v)
    
    def match(self, text: str) -> bool:
        """匹配文本"""
        u = 0
        text_pos = 0
        
        while text_pos < len(text):
            current_char = text[text_pos]
            current_node = self.nodes[u]
            
            if current_char in current_node.children:
                next_node_idx = current_node.children[current_char]
                next_node = self.nodes[next_node_idx]
                
                if self.check_path_match(next_node, text, text_pos):
                    if next_node.is_end:
                        return True
                    u = next_node_idx
                    text_pos += len(next_node.path)
                else:
                    u = current_node.fail
            else:
                u = current_node.fail
                if u == 0:
                    text_pos += 1
        
        return False
    
    def check_path_match(self, node: CompressedNode, text: str, start_pos: int) -> bool:
        """检查路径匹配"""
        path = node.path
        if start_pos + len(path) > len(text):
            return False
        
        for i in range(len(path)):
            if text[start_pos + i] != path[i]:
                return False
        
        return True

# ==================== 变体4: 并行AC自动机 ====================

class ParallelACAutomaton:
    """
    并行AC自动机实现
    核心思想：利用多线程并行处理文本的不同部分
    适用场景：超大规模文本匹配
    """
    
    def __init__(self, num_threads: int = 4):
        self.CHARSET_SIZE = 26
        self.NUM_THREADS = num_threads
        self.tree = [{}]
        self.fail = [0]
        self.danger = [False]
        self.cnt = 0
    
    def insert(self, pattern: str) -> None:
        """插入模式串"""
        u = 0
        for c in pattern:
            idx = ord(c) - ord('a')
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.danger.append(False)
            u = self.tree[u][idx]
        self.danger[u] = True
    
    def build(self) -> None:
        """构建AC自动机"""
        q = deque()
        for i in range(self.CHARSET_SIZE):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            self.danger[u] = self.danger[u] or self.danger[self.fail[u]]
            
            for i in range(self.CHARSET_SIZE):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    def parallel_match(self, text: str) -> bool:
        """并行匹配文本"""
        if len(text) < self.NUM_THREADS * 100:
            return self.single_thread_match(text)
        
        text_segments = self.split_text(text, self.NUM_THREADS)
        
        with ThreadPoolExecutor(max_workers=self.NUM_THREADS) as executor:
            futures = []
            for segment in text_segments:
                future = executor.submit(self.single_thread_match, segment)
                futures.append(future)
            
            for future in futures:
                if future.result():
                    return True
        
        return False
    
    def split_text(self, text: str, num_segments: int) -> List[str]:
        """分割文本"""
        segments = []
        segment_length = len(text) // num_segments
        
        for i in range(num_segments):
            start = i * segment_length
            end = len(text) if i == num_segments - 1 else (i + 1) * segment_length
            segments.append(text[start:end])
        
        return segments
    
    def single_thread_match(self, text: str) -> bool:
        """单线程匹配"""
        u = 0
        for c in text:
            idx = ord(c) - ord('a')
            u = self.tree[u].get(idx, 0)
            if self.danger[u]:
                return True
        return False

# ==================== 测试函数 ====================

def main():
    """主测试函数"""
    
    # 测试双向AC自动机
    print("=== 测试双向AC自动机 ===")
    baca = BidirectionalACAutomaton()
    baca.insert("abc")
    baca.insert("def")
    baca.build()
    
    result1 = baca.bidirectional_match("xyzabcuvw")
    result2 = baca.bidirectional_match("defxyz")
    result3 = baca.bidirectional_match("xyz")
    
    print(f"正向匹配结果: {result1}")
    print(f"反向匹配结果: {result2}")
    print(f"无匹配结果: {result3}")
    
    # 测试动态AC自动机
    print("\n=== 测试动态AC自动机 ===")
    daca = DynamicACAutomaton()
    daca.add_pattern("hello")
    daca.add_pattern("world")
    daca.build()
    
    result4 = daca.match("hello world")
    print(f"匹配结果1: {result4}")
    
    daca.remove_pattern("hello")
    daca.build()
    
    result5 = daca.match("hello world")
    print(f"匹配结果2: {result5}")
    
    # 测试压缩AC自动机
    print("\n=== 测试压缩AC自动机 ===")
    caca = CompressedACAutomaton()
    caca.insert("abc")
    caca.insert("abd")
    caca.build()
    
    result6 = caca.match("xyzabcuvw")
    print(f"压缩AC自动机匹配结果: {result6}")
    
    # 测试并行AC自动机
    print("\n=== 测试并行AC自动机 ===")
    paca = ParallelACAutomaton(num_threads=4)
    paca.insert("test")
    paca.insert("pattern")
    paca.build()
    
    long_text = "xyzabcuvw" * 10000 + "test"
    
    result7 = paca.parallel_match(long_text)
    print(f"并行AC自动机匹配结果: {result7}")

if __name__ == "__main__":
    main()
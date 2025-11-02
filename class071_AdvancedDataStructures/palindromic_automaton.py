#!/usr/bin/env python3
"""
回文自动机（Palindromic Automaton, PAM）实现
也称为Eertree，是一种能够高效处理字符串中所有回文子串的数据结构

主要特性：
1. 每个节点表示一个唯一的回文子串
2. 支持动态添加字符并维护回文子串信息
3. 可以高效统计不同回文子串数量、每个回文子串出现次数等

时间复杂度：构建O(n)，查询O(1)或O(n)
空间复杂度：O(n)
"""

class Node:
    """回文自动机节点类"""
    
    def __init__(self, length):
        self.next = {}      # 转移函数
        self.len = length   # 回文子串的长度
        self.link = -1      # 后缀链接
        self.count = 0      # 该回文子串在当前字符串中的出现次数
        self.occur_count = 0  # 该回文子串在原字符串中的总出现次数


class PalindromicAutomaton:
    """回文自动机实现"""
    
    def __init__(self, text=""):
        # 创建两个特殊根节点
        self.tree = [
            Node(-1),  # root1: 长度为-1的虚拟回文
            Node(0)    # root2: 长度为0的空回文
        ]
        self.text = text
        self.size = 2
        self.last = 1
        
        # 如果提供了初始文本，则构建自动机
        if text:
            for c in text:
                self.extend(c)
            self.calculate_occur_count()
    
    def get_fail(self, p, pos, c):
        """
        找到当前节点的后缀链接中，其对应的回文子串前添加字符c后仍是回文的节点
        
        Args:
            p: 当前节点
            pos: 当前字符位置
            c: 当前字符
            
        Returns:
            找到的节点索引
        """
        while True:
            length = self.tree[p].len
            if pos - length - 1 >= 0 and self.text[pos - length - 1] == c:
                break
            p = self.tree[p].link
        return p
    
    def extend(self, c):
        """
        扩展回文自动机，添加一个字符
        
        Args:
            c: 要添加的字符
        """
        self.text += c
        pos = len(self.text) - 1
        
        p = self.get_fail(self.last, pos, c)
        
        if c not in self.tree[p].next:
            # 创建新节点
            new_node = self.size
            self.size += 1
            self.tree.append(Node(self.tree[p].len + 2))
            
            # 设置新节点的后缀链接
            if self.tree[new_node].len == 1:
                self.tree[new_node].link = 1
            else:
                fail_node = self.get_fail(self.tree[p].link, pos, c)
                self.tree[new_node].link = self.tree[fail_node].next.get(c, 0)
            
            self.tree[p].next[c] = new_node
        
        self.last = self.tree[p].next[c]
        self.tree[self.last].count += 1
    
    def calculate_occur_count(self):
        """计算每个回文子串在原字符串中的总出现次数"""
        # 按照节点长度降序处理
        order = list(range(2, self.size))
        order.sort(key=lambda x: self.tree[x].len, reverse=True)
        
        # 将count累加到后缀链接指向的节点
        for node in order:
            self.tree[self.tree[node].link].occur_count += self.tree[node].count
        
        # 加上原始的count，得到总出现次数
        for i in range(2, self.size):
            self.tree[i].occur_count += self.tree[i].count
    
    def get_distinct_palindrome_count(self):
        """获取不同回文子串的数量"""
        return self.size - 2  # 减去两个根节点
    
    def get_longest_palindrome(self):
        """获取最长回文子串"""
        max_len = 0
        max_node = -1
        
        for i in range(2, self.size):
            if self.tree[i].len > max_len:
                max_len = self.tree[i].len
                max_node = i
        
        if max_node != -1:
            return self.reconstruct_palindrome(max_node)
        return ""
    
    def reconstruct_palindrome(self, node):
        """重建回文子串"""
        if node == 0 or node == 1:
            return ""
        
        current = node
        parent = self.tree[current].link
        
        # 找出第一个字符
        first_char = None
        for char, next_node in self.tree[parent].next.items():
            if next_node == current:
                first_char = char
                break
        
        if first_char is None:
            return ""
        
        if self.tree[current].len == 1:
            return first_char
        
        # 递归重建父回文，然后在两边添加字符
        parent_palindrome = self.reconstruct_palindrome(parent)
        return first_char + parent_palindrome + first_char
    
    def get_total_palindrome_occurrences(self):
        """获取所有回文子串的总出现次数"""
        total = 0
        for i in range(2, self.size):
            total += self.tree[i].occur_count
        return total
    
    def get_node_count(self):
        """获取回文自动机的节点数量"""
        return self.size
    
    def get_text(self):
        """获取文本"""
        return self.text


class LPSSolver:
    """最长回文子串求解器"""
    
    def __init__(self, text):
        self.pam = PalindromicAutomaton(text)
    
    def get_longest_palindrome(self):
        """获取最长回文子串"""
        return self.pam.get_longest_palindrome()
    
    def get_longest_palindrome_length(self):
        """获取最长回文子串长度"""
        return len(self.pam.get_longest_palindrome())


class NumberOfPalindromes:
    """回文数量求解器"""
    
    def __init__(self, text):
        self.pam = PalindromicAutomaton(text)
    
    def get_distinct_count(self):
        """获取不同回文子串数量"""
        return self.pam.get_distinct_palindrome_count()
    
    def get_total_occurrences(self):
        """获取回文子串总出现次数"""
        return self.pam.get_total_palindrome_occurrences()


def test_palindromic_automaton():
    """测试函数"""
    print("=== 测试回文自动机 ===")
    
    # 测试最长回文子串
    print("\n=== 测试最长回文子串 ===")
    lps = LPSSolver("abacabad")
    print(f"文本: abacabad")
    print(f"最长回文子串: {lps.get_longest_palindrome()}")
    print(f"最长回文子串长度: {lps.get_longest_palindrome_length()}")
    
    # 测试回文数量
    print("\n=== 测试回文数量 ===")
    nop = NumberOfPalindromes("aabaa")
    print(f"文本: aabaa")
    print(f"不同回文子串数量: {nop.get_distinct_count()}")
    print(f"回文子串总出现次数: {nop.get_total_occurrences()}")
    
    # 测试更多字符串
    print("\n=== 测试更多字符串 ===")
    test_strings = ["racecar", "abba", "abcba", "a", ""]
    
    for s in test_strings:
        pam = PalindromicAutomaton(s)
        print(f"文本: '{s}'")
        print(f"  不同回文数: {pam.get_distinct_palindrome_count()}")
        print(f"  最长回文: '{pam.get_longest_palindrome()}'")
        print(f"  总出现次数: {pam.get_total_palindrome_occurrences()}")
        print()


if __name__ == "__main__":
    test_palindromic_automaton()
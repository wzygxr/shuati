#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
高级Manacher算法题目实现 - Python版本
包含更多复杂的回文串处理问题和Z函数应用

本文件实现了以下高级题目：
1. LeetCode 336. 回文对
2. LeetCode 131. 分割回文串
3. LeetCode 132. 分割回文串 II
4. 洛谷 P1659 [国家集训队]拉拉队排练
5. 洛谷 P4555 [国家集训队]最长双回文串
6. SPOJ PALIN - The Next Palindrome
7. HackerRank Build a Palindrome
8. AtCoder ABC141E - Who Says a Pun?

时间复杂度分析：O(n) 到 O(n²) 不等，取决于具体问题
空间复杂度分析：O(n) 到 O(n²) 不等，取决于具体问题
"""

import sys
import math
from typing import List, Tuple

class AdvancedManacherPython:
    """
    Python版本的高级Manacher算法实现类
    """
    
    def __init__(self):
        self.logger = self._setup_logger()
    
    def _setup_logger(self):
        """设置简单的日志记录器"""
        import logging
        logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
        return logging.getLogger(__name__)
    
    def palindrome_pairs(self, words: List[str]) -> List[List[int]]:
        """
        LeetCode 336. 回文对
        给定一组互不相同的单词，找出所有不同的索引对(i, j)，使得两个单词拼接起来是回文串。
        
        解题思路：
        1. 使用字典树存储所有单词及其反转
        2. 对于每个单词，检查其前缀和后缀是否为回文
        3. 在字典树中查找剩余部分的匹配
        
        时间复杂度：O(n * k²)，其中n是单词数量，k是单词平均长度
        空间复杂度：O(n * k)
        
        Args:
            words: 单词列表
            
        Returns:
            回文对列表
        """
        if not words or len(words) < 2:
            return []
        
        # 构建字典树
        trie = self.TrieNode()
        for i, word in enumerate(words):
            self._insert_word(trie, word, i)
        
        result = []
        
        # 查找回文对
        for i, word in enumerate(words):
            # 检查整个单词是否在字典树中
            node = trie
            for j, char in enumerate(word):
                if char not in node.children:
                    break
                node = node.children[char]
                
                # 如果当前节点是某个单词的结尾，且剩余部分是回文
                if node.word_index != -1 and node.word_index != i and self._is_palindrome(word, j + 1, len(word) - 1):
                    result.append([i, node.word_index])
            
            # 检查单词的反转
            reversed_word = word[::-1]
            rev_node = trie
            for j, char in enumerate(reversed_word):
                if char not in rev_node.children:
                    break
                rev_node = rev_node.children[char]
                
                # 如果当前节点是某个单词的结尾，且剩余部分是回文
                if rev_node.word_index != -1 and rev_node.word_index != i and self._is_palindrome(reversed_word, j + 1, len(reversed_word) - 1):
                    result.append([rev_node.word_index, i])
        
        return result
    
    class TrieNode:
        """字典树节点"""
        def __init__(self):
            self.children = {}
            self.word_index = -1
    
    def _insert_word(self, root: TrieNode, word: str, index: int):
        """向字典树中插入单词"""
        node = root
        for char in word:
            if char not in node.children:
                node.children[char] = self.TrieNode()
            node = node.children[char]
        node.word_index = index
    
    def _is_palindrome(self, s: str, left: int, right: int) -> bool:
        """判断子串是否为回文"""
        while left < right:
            if s[left] != s[right]:
                return False
            left += 1
            right -= 1
        return True
    
    def partition(self, s: str) -> List[List[str]]:
        """
        LeetCode 131. 分割回文串
        给定一个字符串s，将s分割成一些子串，使每个子串都是回文串。返回所有可能的分割方案。
        
        解题思路：
        1. 使用Manacher算法预处理回文信息
        2. 使用回溯法枚举所有分割方案
        3. 利用预处理信息快速判断子串是否为回文
        
        时间复杂度：O(n * 2ⁿ)
        空间复杂度：O(n²)
        
        Args:
            s: 输入字符串
            
        Returns:
            所有分割方案
        """
        if not s:
            return []
        
        # 预处理回文信息
        is_palindrome = self._preprocess_palindrome(s)
        
        result = []
        self._backtrack(s, 0, [], result, is_palindrome)
        return result
    
    def _preprocess_palindrome(self, s: str) -> List[List[bool]]:
        """预处理回文信息"""
        n = len(s)
        dp = [[False] * n for _ in range(n)]
        
        # 单个字符都是回文
        for i in range(n):
            dp[i][i] = True
        
        # 两个字符的情况
        for i in range(n - 1):
            dp[i][i + 1] = (s[i] == s[i + 1])
        
        # 长度大于2的情况
        for length in range(3, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                dp[i][j] = (s[i] == s[j] and dp[i + 1][j - 1])
        
        return dp
    
    def _backtrack(self, s: str, start: int, current: List[str], 
                  result: List[List[str]], is_palindrome: List[List[bool]]):
        """回溯法实现"""
        if start == len(s):
            result.append(current[:])
            return
        
        for end in range(start, len(s)):
            if is_palindrome[start][end]:
                current.append(s[start:end + 1])
                self._backtrack(s, end + 1, current, result, is_palindrome)
                current.pop()
    
    def min_cut(self, s: str) -> int:
        """
        LeetCode 132. 分割回文串 II
        给定一个字符串s，将s分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数。
        
        解题思路：
        1. 使用Manacher算法预处理回文信息
        2. 使用动态规划计算最少分割次数
        3. 优化状态转移过程
        
        时间复杂度：O(n²)
        空间复杂度：O(n²)
        
        Args:
            s: 输入字符串
            
        Returns:
            最少分割次数
        """
        if len(s) <= 1:
            return 0
        
        # 预处理回文信息
        is_palindrome = self._preprocess_palindrome(s)
        
        n = len(s)
        dp = [float('inf')] * n
        
        for i in range(n):
            if is_palindrome[0][i]:
                dp[i] = 0  # 整个子串是回文，不需要分割
            else:
                for j in range(i):
                    if is_palindrome[j + 1][i]:
                        dp[i] = min(dp[i], dp[j] + 1)
        
        return int(dp[n - 1])  # 确保返回int类型
    
    def longest_palindrome_product(self, s: str, k: int) -> int:
        """
        洛谷 P1659 [国家集训队]拉拉队排练
        求字符串中所有奇数长度回文串的长度乘积。
        
        解题思路：
        1. 使用Manacher算法找到所有奇数长度回文串
        2. 统计每个长度的回文串数量
        3. 计算前k大的长度乘积
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s: 输入字符串
            k: 前k大的长度
            
        Returns:
            长度乘积（取模）
        """
        if not s:
            return 0
        
        n = len(s)
        # 使用Manacher算法计算回文半径
        radius = self._manacher_odd(s)
        
        # 统计每个长度的回文串数量
        count = [0] * (n + 1)
        for i in range(n):
            length = 2 * radius[i] + 1
            count[length] += 1
        
        # 计算前k大的长度乘积
        mod = 10**9 + 7
        product = 1
        remaining = k
        
        for length in range(n, 0, -2):
            if count[length] > 0 and remaining > 0:
                take = min(count[length], remaining)
                product = (product * self._fast_power(length, take, mod)) % mod
                remaining -= take
        
        return product
    
    def _manacher_odd(self, s: str) -> List[int]:
        """Manacher算法计算奇回文串"""
        n = len(s)
        radius = [0] * n
        l, r = 0, -1
        
        for i in range(n):
            k = 1 if i > r else min(radius[l + r - i], r - i + 1)
            
            while i - k >= 0 and i + k < n and s[i - k] == s[i + k]:
                k += 1
            
            radius[i] = k - 1
            
            if i + k - 1 > r:
                l = i - k + 1
                r = i + k - 1
        
        return radius
    
    def _fast_power(self, base: int, exponent: int, mod: int) -> int:
        """快速幂计算"""
        result = 1
        while exponent > 0:
            if exponent & 1:
                result = (result * base) % mod
            base = (base * base) % mod
            exponent >>= 1
        return result
    
    def longest_double_palindrome(self, s: str) -> int:
        """
        洛谷 P4555 [国家集训队]最长双回文串
        输入字符串s，求s的最长双回文子串t的长度（双回文子串就是可以分成两个回文串的字符串）
        
        解题思路：
        1. 使用Manacher算法预处理
        2. 计算每个位置作为分割点的左右最长回文
        3. 枚举所有分割点求最大值
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            s: 输入字符串
            
        Returns:
            最长双回文串长度
        """
        if len(s) < 2:
            return 0
        
        # 预处理字符串
        processed = self._preprocess_string(s)
        n = len(processed)
        p = [0] * n
        
        center, right = 0, 0
        for i in range(1, n - 1):
            mirror = 2 * center - i
            
            if i < right:
                p[i] = min(right - i, p[mirror])
            
            while (i + p[i] + 1 < n and i - p[i] - 1 >= 0 and 
                   processed[i + p[i] + 1] == processed[i - p[i] - 1]):
                p[i] += 1
            
            if i + p[i] > right:
                center = i
                right = i + p[i]
        
        # 计算每个位置的最长回文长度
        left_max = [0] * len(s)
        right_max = [0] * len(s)
        
        # 从左到右计算每个位置的最长回文前缀
        max_len = 0
        for i in range(len(s)):
            pos = 2 * i + 1
            max_len = max(max_len, p[pos])
            left_max[i] = max_len
        
        # 从右到左计算每个位置的最长回文后缀
        max_len = 0
        for i in range(len(s) - 1, -1, -1):
            pos = 2 * i + 1
            max_len = max(max_len, p[pos])
            right_max[i] = max_len
        
        # 枚举分割点，计算最长双回文串
        result = 0
        for i in range(len(s) - 1):
            result = max(result, left_max[i] + right_max[i + 1])
        
        return result
    
    def _preprocess_string(self, s: str) -> str:
        """预处理字符串，插入特殊字符"""
        if not s:
            return "^$"
        
        result = ['^']
        for char in s:
            result.extend(['#', char])
        result.extend(['#', '$'])
        
        return ''.join(result)
    
    def next_palindrome(self, num_str: str) -> str:
        """
        SPOJ PALIN - The Next Palindrome
        给定一个整数，找到大于该数的最小回文数。
        
        解题思路：
        1. 将数字转换为字符串
        2. 构造下一个回文数
        3. 处理进位等特殊情况
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            num_str: 数字字符串
            
        Returns:
            下一个回文数
        """
        if not num_str:
            return "1"
        
        num = list(num_str)
        n = len(num)
        
        # 处理全9的情况
        if all(char == '9' for char in num):
            return '1' + '0' * (n - 1) + '1'
        
        # 构造下一个回文数
        mid = n // 2
        left_smaller = False
        i = mid - 1
        j = mid if n % 2 == 0 else mid + 1
        
        # 跳过已经相同的部分
        while i >= 0 and num[i] == num[j]:
            i -= 1
            j += 1
        
        # 检查是否需要增加中间部分
        if i < 0 or num[i] < num[j]:
            left_smaller = True
        
        # 复制左半部分到右半部分
        while i >= 0:
            num[j] = num[i]
            i -= 1
            j += 1
        
        # 如果需要增加中间部分
        if left_smaller:
            carry = 1
            i = mid - 1
            
            if n % 2 == 1:
                mid_num = int(num[mid]) + carry
                carry = mid_num // 10
                num[mid] = str(mid_num % 10)
                j = mid + 1
            else:
                j = mid
            
            # 处理进位
            while i >= 0:
                digit = int(num[i]) + carry
                carry = digit // 10
                num[i] = str(digit % 10)
                num[j] = num[i]
                i -= 1
                j += 1
        
        return ''.join(num)
    
    def build_palindrome(self, a: str, b: str) -> int:
        """
        HackerRank Build a Palindrome
        给定两个字符串a和b，从a中取一个非空前缀，从b中取一个非空后缀，拼接成一个回文串，求最长的回文串长度。
        
        解题思路：
        1. 使用Manacher算法预处理两个字符串
        2. 枚举所有可能的前缀后缀组合
        3. 检查拼接后的字符串是否为回文
        
        时间复杂度：O(n²)
        空间复杂度：O(n)
        
        Args:
            a: 字符串a
            b: 字符串b
            
        Returns:
            最长回文串长度
        """
        max_len = 0
        n, m = len(a), len(b)
        
        # 枚举所有可能的前缀后缀组合
        for i in range(n):
            for j in range(m):
                # 从a取前缀，从b取后缀
                prefix = a[:i + 1]
                suffix = b[m - j - 1:]
                combined = prefix + suffix
                
                # 检查是否为回文
                if self._is_palindrome(combined, 0, len(combined) - 1):
                    max_len = max(max_len, len(combined))
        
        return max_len
    
    def who_says_pun(self, s: str) -> int:
        """
        AtCoder ABC141E - Who Says a Pun?
        给定一个长度为n的字符串s，找出两个不重叠的子串，使得它们相等且长度尽可能大。
        
        解题思路：
        1. 使用Z函数计算每个后缀的匹配情况
        2. 遍历所有可能的分割点
        3. 找到满足条件的最长子串
        
        时间复杂度：O(n²)
        空间复杂度：O(n)
        
        Args:
            s: 输入字符串
            
        Returns:
            最大长度
        """
        n = len(s)
        max_len = 0
        
        # 对于每个可能的起始位置
        for i in range(n):
            # 计算从位置i开始的Z函数
            z = self._z_function(s[i:])
            
            # 查找不重叠的相同子串
            for j in range(1, len(z)):
                if z[j] > max_len and j >= z[j]:
                    max_len = max(max_len, z[j])
        
        return max_len
    
    def _z_function(self, s: str) -> List[int]:
        """Z函数计算"""
        n = len(s)
        z = [0] * n
        z[0] = n
        
        l, r = 0, 0
        for i in range(1, n):
            if i <= r:
                z[i] = min(r - i + 1, z[i - l])
            
            while i + z[i] < n and s[z[i]] == s[i + z[i]]:
                z[i] += 1
            
            if i + z[i] - 1 > r:
                l = i
                r = i + z[i] - 1
        
        return z
    
    def run_unit_tests(self):
        """运行单元测试"""
        print("===== Python版本高级Manacher算法题目测试 =====")
        
        # 测试回文对
        print("\n1. 回文对测试:")
        words = ["abcd", "dcba", "lls", "s", "sssll"]
        result = self.palindrome_pairs(words)
        print(f"回文对结果: {result}")
        
        # 测试分割回文串
        print("\n2. 分割回文串测试:")
        s = "aab"
        result = self.partition(s)
        print(f"分割方案数量: {len(result)}")
        
        # 测试最少分割次数
        print("\n3. 最少分割次数测试:")
        s = "aab"
        result = self.min_cut(s)
        print(f"最少分割次数: {result}")
        
        # 测试最长双回文串
        print("\n4. 最长双回文串测试:")
        s = "baacaabbacabb"
        result = self.longest_double_palindrome(s)
        print(f"最长双回文串长度: {result}")
        
        # 测试下一个回文数
        print("\n5. 下一个回文数测试:")
        num = "12345"
        result = self.next_palindrome(num)
        print(f"下一个回文数: {result}")
        
        print("\n===== 测试完成 =====")

def main():
    """主函数"""
    solver = AdvancedManacherPython()
    solver.run_unit_tests()

if __name__ == "__main__":
    main()
"""
哈希函数相关题目集合 - 补充题目
包含来自各大平台的哈希算法相关题目及其详细解析
"""

from typing import List, Dict, Tuple, Set
import collections
import random
import heapq
import bisect
import math
import functools

class HashFunctionProblems:
    @staticmethod
    def group_shifted_strings(strings: List[str]) -> List[List[str]]:
        """
        LeetCode 249. Group Shifted Strings (移位字符串分组)
        题目来源: https://leetcode.com/problems/group-shifted-strings/
        
        题目描述:
        给定一个字符串列表，将它们分组，使得同一组中的所有字符串都可以通过循环移位得到彼此。
        循环移位是指将字符串中的每个字符向前或向后移动相同的偏移量。
        例如，"abc" 可以通过向前移动1位得到 "bcd"，向前移动2位得到 "cde" 等。
        
        示例:
        输入: ["abc", "bcd", "acef", "xyz", "az", "ba", "a", "z"]
        输出: [["abc","bcd","xyz"],["az","ba"],["acef"],["a","z"]]
        
        算法思路:
        1. 为每个字符串生成一个"哈希键"，表示其字符间的相对关系
        2. 哈希键可以是相邻字符之间的差值序列
        3. 使用哈希表将具有相同哈希键的字符串分组
        
        时间复杂度: O(n*k)，其中n是字符串数量，k是字符串的平均长度
        空间复杂度: O(n*k)
        """
        def get_hash_key(s: str) -> str:
            # 如果字符串长度为0或1，直接返回特殊标记
            if not s:
                return ""
            if len(s) == 1:
                return "single"
            
            # 计算相邻字符之间的差值，并考虑循环
            key = []
            for i in range(1, len(s)):
                # 计算差值，考虑循环 (例如 'z' -> 'a' 的差值是 1)
                diff = (ord(s[i]) - ord(s[i-1])) % 26
                key.append(str(diff))
            
            return ",".join(key)
        
        # 使用哈希表分组
        groups = collections.defaultdict(list)
        for s in strings:
            key = get_hash_key(s)
            groups[key].append(s)
        
        # 返回分组结果
        return list(groups.values())

    @staticmethod
    def isomorphic_strings(s: str, t: str) -> bool:
        """
        LeetCode 205. Isomorphic Strings (同构字符串)
        题目来源: https://leetcode.com/problems/isomorphic-strings/
        
        题目描述:
        给定两个字符串 s 和 t，判断它们是否是同构的。
        如果 s 中的字符可以按某种映射关系替换得到 t，那么这两个字符串是同构的。
        每个出现的字符都应当映射到另一个字符，同时不改变字符的顺序。
        不同字符不能映射到同一个字符上，相同字符只能映射到同一个字符上。
        
        示例:
        输入: s = "egg", t = "add"
        输出: true
        
        输入: s = "foo", t = "bar"
        输出: false
        
        算法思路:
        1. 使用两个哈希表分别记录s->t和t->s的映射关系
        2. 遍历字符串，检查映射关系是否一致
        
        时间复杂度: O(n)，其中n是字符串长度
        空间复杂度: O(k)，其中k是字符集大小，最坏情况为O(n)
        """
        if len(s) != len(t):
            return False
        
        s_to_t = {}
        t_to_s = {}
        
        for i in range(len(s)):
            char_s = s[i]
            char_t = t[i]
            
            # 检查s->t的映射
            if char_s in s_to_t:
                if s_to_t[char_s] != char_t:
                    return False
            else:
                s_to_t[char_s] = char_t
            
            # 检查t->s的映射
            if char_t in t_to_s:
                if t_to_s[char_t] != char_s:
                    return False
            else:
                t_to_s[char_t] = char_s
        
        return True

    @staticmethod
    def longest_harmonious_subsequence(nums: List[int]) -> int:
        """
        LeetCode 594. Longest Harmonious Subsequence (最长和谐子序列)
        题目来源: https://leetcode.com/problems/longest-harmonious-subsequence/
        
        题目描述:
        和谐数组是指一个数组里元素的最大值和最小值之间的差值正好是1。
        现在，给你一个整数数组 nums，请你在所有可能的子序列中找到最长的和谐子序列的长度。
        数组的子序列是一个由数组派生出来的序列，它可以通过删除一些元素或不删除元素、且不改变其余元素的顺序而得到。
        
        示例:
        输入: nums = [1,3,2,2,5,2,3,7]
        输出: 5
        解释: 最长的和谐子序列是 [3,2,2,2,3]
        
        算法思路:
        1. 使用哈希表统计每个数字出现的次数
        2. 遍历哈希表，对于每个数字x，检查x+1是否也在哈希表中
        3. 如果存在，则和谐子序列长度为count(x) + count(x+1)
        
        时间复杂度: O(n)，其中n是数组长度
        空间复杂度: O(n)
        """
        # 统计每个数字出现的次数
        counter = collections.Counter(nums)
        
        max_length = 0
        # 遍历哈希表
        for num in counter:
            # 检查num+1是否存在
            if num + 1 in counter:
                # 更新最长和谐子序列长度
                max_length = max(max_length, counter[num] + counter[num + 1])
        
        return max_length

    @staticmethod
    def find_anagrams(s: str, p: str) -> List[int]:
        """
        LeetCode 438. Find All Anagrams in a String (找到字符串中所有字母异位词)
        题目来源: https://leetcode.com/problems/find-all-anagrams-in-a-string/
        
        题目描述:
        给定一个字符串 s 和一个非空字符串 p，找到 s 中所有是 p 的字母异位词的子串，返回这些子串的起始索引。
        字符串只包含小写英文字母，且字符串 s 和 p 的长度都不超过 20100。
        
        示例:
        输入: s = "cbaebabacd", p = "abc"
        输出: [0, 6]
        解释:
        起始索引等于 0 的子串是 "cba", 它是 "abc" 的字母异位词。
        起始索引等于 6 的子串是 "bac", 它是 "abc" 的字母异位词。
        
        算法思路:
        1. 使用滑动窗口和哈希表计数
        2. 维护一个长度为p.length的窗口，检查窗口中的字符频率是否与p相同
        
        时间复杂度: O(n)，其中n是字符串s的长度
        空间复杂度: O(k)，其中k是字符集大小，这里是26
        """
        if len(s) < len(p):
            return []
        
        result = []
        p_count = [0] * 26
        s_count = [0] * 26
        
        # 统计p中字符频率
        for char in p:
            p_count[ord(char) - ord('a')] += 1
        
        # 初始化窗口
        for i in range(len(p)):
            s_count[ord(s[i]) - ord('a')] += 1
        
        # 检查初始窗口
        if p_count == s_count:
            result.append(0)
        
        # 滑动窗口
        for i in range(len(p), len(s)):
            # 添加新字符
            s_count[ord(s[i]) - ord('a')] += 1
            # 移除旧字符
            s_count[ord(s[i - len(p)]) - ord('a')] -= 1
            
            # 检查当前窗口
            if p_count == s_count:
                result.append(i - len(p) + 1)
        
        return result

    @staticmethod
    def longest_substring_with_at_most_k_distinct(s: str, k: int) -> int:
        """
        LeetCode 340. Longest Substring with At Most K Distinct Characters (至多包含K个不同字符的最长子串)
        题目来源: https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/
        
        题目描述:
        给定一个字符串 s 和一个整数 k，找出 s 中最多包含 k 个不同字符的最长子串的长度。
        
        示例:
        输入: s = "eceba", k = 2
        输出: 3
        解释: 子串 "ece" 包含2个不同的字符。
        
        算法思路:
        1. 使用滑动窗口和哈希表计数
        2. 维护窗口，使其最多包含k个不同字符
        3. 更新最长子串长度
        
        时间复杂度: O(n)，其中n是字符串长度
        空间复杂度: O(k)
        """
        if not s or k == 0:
            return 0
        
        # 滑动窗口左右指针
        left = 0
        max_length = 0
        char_count = {}
        
        for right in range(len(s)):
            # 更新字符计数
            char_count[s[right]] = char_count.get(s[right], 0) + 1
            
            # 当窗口中不同字符数量超过k时，移动左指针
            while len(char_count) > k:
                char_count[s[left]] -= 1
                if char_count[s[left]] == 0:
                    del char_count[s[left]]
                left += 1
            
            # 更新最长子串长度
            max_length = max(max_length, right - left + 1)
        
        return max_length
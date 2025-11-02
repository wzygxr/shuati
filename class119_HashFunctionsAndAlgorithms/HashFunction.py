# -*- coding: utf-8 -*-
"""
Python版本的哈希函数实现
"""

import hashlib
import random
import collections
import heapq
from typing import List, Optional


class HashFunction:
    """
    哈希函数工具类
    """

    @staticmethod
    def hash_code(key, hash_size):
        """
        LintCode 128. Hash Function
        题目来源: https://www.lintcode.com/problem/hash-function/description

        题目描述:
        在数据结构中，哈希函数是用来将一个字符串（或任何其他类型）转化为小于哈希表大小且大于等于零的整数。
        一个好的哈希函数可以尽可能少地产生冲突。
        一种广泛使用的哈希函数算法是使用数值33，假设任何字符串都是基于33的一个大整数，比如：
        hashcode("abcd") = (ascii(a) * 33^3 + ascii(b) * 33^2 + ascii(c) *33 + ascii(d)) % HASH_SIZE
                         = (97* 33^3 + 98 * 33^2 + 99 * 33 +100) % HASH_SIZE
                         = 3595978 % HASH_SIZE
        其中HASH_SIZE表示哈希表的大小(可以假设一个哈希表就是一个索引0 ~ HASH_SIZE-1的数组)。
        给出一个字符串作为key和一个哈希表的大小，返回这个字符串的哈希值。

        样例:
        对于key="abcd" 并且 size=100， 返回 78

        算法思路:
        使用霍纳法则（Horner's Rule）优化计算，避免大数溢出:
        hashcode = (ascii(a) * 33^3 + ascii(b) * 33^2 + ascii(c) *33 + ascii(d)) % HASH_SIZE
        可以转换为:
        hashcode = ((((ascii(a) % HASH_SIZE) * 33 + ascii(b)) % HASH_SIZE) * 33 + ascii(c)) % HASH_SIZE) * 33 + ascii(d)) % HASH_SIZE

        时间复杂度: O(n)，其中n是字符串的长度
        空间复杂度: O(1)

        :param key: 输入字符串
        :param hash_size: 哈希表大小
        :return: 哈希值
        """
        ans = 0
        for char in key:
            ans = (ans * 33 + ord(char)) % hash_size
        return ans

    @staticmethod
    def generate_strings(arr, n):
        """
        生成所有可能的字符串组合

        :param arr: 字符数组
        :param n: 字符串长度
        :return: 所有可能的字符串列表
        """
        result = []

        def helper(current_path, remaining):
            if remaining == 0:
                result.append(current_path)
                return
            for char in arr:
                helper(current_path + char, remaining - 1)

        helper("", n)
        return result

    @staticmethod
    def my_hash_set():
        """
        LeetCode 705. Design HashSet (设计哈希集合)
        题目来源: https://leetcode.com/problems/design-hashset/

        题目描述:
        不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
        实现 MyHashSet 类：
        void add(key) 向哈希集合中插入值 key 。
        bool contains(key) 返回哈希集合中是否存在这个值 key 。
        void remove(key) 将给定值 key 从哈希集合中删除。如果哈希集合中没有这个值，什么也不做。

        示例:
        输入：
        ["MyHashSet", "add", "add", "contains", "contains", "add", "contains", "remove", "contains"]
        [[], [1], [2], [1], [3], [2], [2], [2], [2]]
        输出：
        [null, null, null, true, false, null, true, null, false]

        约束条件:
        0 <= key <= 10^6
        最多调用 10^4 次 add、remove 和 contains

        算法思路:
        使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
        当发生哈希冲突时，将元素添加到对应位置的链表中。

        时间复杂度: O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
        空间复杂度: O(n)，存储所有元素
        """
        class MyHashSet:
            def __init__(self):
                self.BASE = 10000
                self.data = [[] for _ in range(self.BASE)]

            def _hash(self, key):
                return key % self.BASE

            def add(self, key):
                h = self._hash(key)
                if key not in self.data[h]:
                    self.data[h].append(key)

            def remove(self, key):
                h = self._hash(key)
                if key in self.data[h]:
                    self.data[h].remove(key)

            def contains(self, key):
                h = self._hash(key)
                return key in self.data[h]
        
        return MyHashSet()

    @staticmethod
    def my_hash_map():
        """
        LeetCode 706. Design HashMap (设计哈希映射)
        题目来源: https://leetcode.com/problems/design-hashmap/

        题目描述:
        不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
        实现 MyHashMap 类：
        MyHashMap() 用空映射初始化对象
        void put(int key, int value) 向 HashMap 插入一个键值对 (key, value) 。如果 key 已经存在于映射中，则更新其对应的值 value 。
        int get(int key) 返回特定的 key 所映射的 value ；如果映射中不包含 key 的映射，返回 -1 。
        void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value 。

        示例:
        输入：
        ["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
        [[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
        输出：
        [null, null, null, 1, -1, null, 1, null, -1]

        约束条件:
        0 <= key, value <= 10^6
        最多调用 10^4 次 put、get 和 remove 方法

        算法思路:
        使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
        每个链表节点存储键值对，当发生哈希冲突时，将节点添加到对应位置的链表中。

        时间复杂度: O(n/b)，其中n是元素个数，b是桶数（在实际实现中我们使用10000作为桶数）
        空间复杂度: O(n)，存储所有元素
        """
        class MyHashMap:
            def __init__(self):
                self.BASE = 10000
                self.data = [[] for _ in range(self.BASE)]

            def _hash(self, key):
                return key % self.BASE

            def put(self, key, value):
                h = self._hash(key)
                for i, (k, v) in enumerate(self.data[h]):
                    if k == key:
                        self.data[h][i] = (key, value)
                        return
                self.data[h].append((key, value))

            def get(self, key):
                h = self._hash(key)
                for k, v in self.data[h]:
                    if k == key:
                        return v
                return -1

            def remove(self, key):
                h = self._hash(key)
                for i, (k, v) in enumerate(self.data[h]):
                    if k == key:
                        del self.data[h][i]
                        return
        
        return MyHashMap()

    @staticmethod
    def str_str(haystack, needle):
        """
        LeetCode 28. Find the Index of the First Occurrence in a String (实现strStr())
        题目来源: https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/

        题目描述:
        给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。
        如果 needle 不是 haystack 的一部分，则返回 -1。

        示例:
        输入：haystack = "sadbutsad", needle = "sad"
        输出：0

        输入：haystack = "leetcode", needle = "leeto"
        输出：-1

        算法思路:
        使用Rabin-Karp算法（滚动哈希）实现字符串匹配：
        1. 计算needle的哈希值
        2. 在haystack中维护一个长度为needle.length()的滑动窗口，计算其哈希值
        3. 当哈希值相等时，再进行字符串比较确认（避免哈希冲突）

        时间复杂度: O(n+m)，其中n是haystack长度，m是needle长度
        空间复杂度: O(1)
        """
        if not needle:
            return 0
        if len(haystack) < len(needle):
            return -1

        base = 256  # 基数
        mod = 1000000007  # 大质数，用于取模运算

        # 计算needle的哈希值和h的值
        needle_hash = 0
        h = 1  # 用于计算最高位的权重

        for i in range(len(needle)):
            needle_hash = (needle_hash * base + ord(needle[i])) % mod
            if i < len(needle) - 1:
                h = (h * base) % mod

        # 计算haystack第一个窗口的哈希值
        haystack_hash = 0
        for i in range(len(needle)):
            haystack_hash = (haystack_hash * base + ord(haystack[i])) % mod

        # 滑动窗口匹配
        for i in range(len(haystack) - len(needle) + 1):
            # 如果哈希值相等，再进行字符串比较确认
            if needle_hash == haystack_hash:
                if haystack[i:i+len(needle)] == needle:
                    return i

            # 计算下一个窗口的哈希值
            if i < len(haystack) - len(needle):
                haystack_hash = (base * (haystack_hash - (ord(haystack[i]) * h) % mod) + ord(haystack[i + len(needle)])) % mod
                if haystack_hash < 0:
                    haystack_hash += mod

        return -1

    @staticmethod
    def find_repeated_dna_sequences(s):
        """
        LeetCode 187. Repeated DNA Sequences (重复的DNA序列)
        题目来源: https://leetcode.com/problems/repeated-dna-sequences/

        题目描述:
        DNA序列由一系列核苷酸组成，缩写为 'A', 'C', 'G' 和 'T'。
        例如，"ACGAATTCCG" 是一个 DNA序列。
        在研究 DNA 时，识别 DNA 中的重复序列非常有用。
        给定一个表示 DNA序列 的字符串 s，返回所有在 DNA 分子中出现不止一次的长度为 10 的序列(子字符串)。
        可以按任意顺序返回答案。

        示例:
        输入：s = "AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT"
        输出：["AAAAACCCCC","CCCCCAAAAA"]

        输入：s = "AAAAAAAAAAAAA"
        输出：["AAAAAAAAAA"]

        算法思路:
        使用滚动哈希技术：
        1. 将每个字符映射为数字：A=0, C=1, G=2, T=3
        2. 使用4进制表示长度为10的序列
        3. 滑动窗口遍历所有长度为10的子串，计算其哈希值
        4. 使用哈希表记录每个哈希值出现的次数
        5. 返回出现次数大于1的序列

        时间复杂度: O(n)，其中n是DNA序列长度
        空间复杂度: O(n)，存储所有子串的哈希值
        """
        if len(s) < 10:
            return []

        # 字符到数字的映射
        char_map = {'A': 0, 'C': 1, 'G': 2, 'T': 3}

        base = 4
        mod = 1000000007  # 大质数，用于取模运算
        window_size = 10

        # 计算base^(window_size-1) % mod
        h = 1
        for i in range(window_size - 1):
            h = (h * base) % mod

        # 计算第一个窗口的哈希值
        hash_val = 0
        for i in range(window_size):
            hash_val = (hash_val * base + char_map[s[i]]) % mod

        # 使用哈希表记录每个哈希值出现的次数
        hash_map = {hash_val: 1}
        result = []

        # 滑动窗口计算后续哈希值
        for i in range(1, len(s) - window_size + 1):
            # 移除最高位字符，添加最低位字符
            hash_val = (base * (hash_val - (char_map[s[i - 1]] * h) % mod) + char_map[s[i + window_size - 1]]) % mod
            if hash_val < 0:
                hash_val += mod

            # 记录哈希值出现次数
            hash_map[hash_val] = hash_map.get(hash_val, 0) + 1

            # 如果某个哈希值出现2次，将其对应的子串加入结果集
            if hash_map[hash_val] == 2:
                result.append(s[i:i + window_size])

        return result

    @staticmethod
    def shortest_palindrome(s):
        """
        LeetCode 214. Shortest Palindrome (最短回文串)
        题目来源: https://leetcode.com/problems/shortest-palindrome/

        题目描述:
        给你一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。
        找到并返回可以用这种方式转换的最短回文串。

        示例:
        输入：s = "aacecaaa"
        输出："aaacecaaa"

        输入：s = "abcd"
        输出："dcbabcd"

        算法思路:
        使用滚动哈希技术找到s的最长前缀回文串:
        1. 计算s的正向哈希和反向哈希
        2. 使用双指针从两端向中间移动，同时比较正向和反向哈希
        3. 当找到最长前缀回文串后，将剩余部分反转并添加到原字符串前面

        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        if len(s) <= 1:
            return s
        
        n = len(s)
        base = 256
        mod = 10**9 + 7
        forward_hash = 0
        backward_hash = 0
        power = 1
        max_len = 0
        
        for i in range(n):
            forward_hash = (forward_hash * base + ord(s[i])) % mod
            backward_hash = (backward_hash + ord(s[i]) * power) % mod
            if forward_hash == backward_hash:
                max_len = i + 1
            power = (power * base) % mod
        
        # 将剩余部分反转并添加到前面
        suffix = s[max_len:]
        reversed_suffix = suffix[::-1]
        return reversed_suffix + s


# 一致性哈希 (Consistent Hashing) 实现
class ConsistentHash:
    def __init__(self, replicas=100):
        self.replicas = replicas  # 每个真实节点对应的虚拟节点数
        self.ring = {}  # 虚拟节点环
        self.servers = set()  # 真实服务器集合
    
    def _get_hash(self, key):
        """使用FNV-1a哈希算法"""
        FNV_32_INIT = 0x811c9dc5
        FNV_32_PRIME = 0x01000193
        
        hash_val = FNV_32_INIT
        for char in key:
            hash_val ^= ord(char)
            hash_val *= FNV_32_PRIME
            hash_val &= 0xFFFFFFFF  # 保持32位
        return abs(hash_val)
    
    def add_server(self, server):
        """添加服务器"""
        self.servers.add(server)
        # 为每个真实节点创建多个虚拟节点
        for i in range(self.replicas):
            virtual_node = f"{server}#{i}"
            hash_val = self._get_hash(virtual_node)
            self.ring[hash_val] = server
    
    def remove_server(self, server):
        """移除服务器"""
        if server not in self.servers:
            return
        
        self.servers.remove(server)
        # 移除对应的所有虚拟节点
        keys_to_remove = [key for key, value in self.ring.items() if value == server]
        for key in keys_to_remove:
            del self.ring[key]
    
    def get_server(self, key):
        """获取键对应的服务器"""
        if not self.ring:
            return None
        
        hash_val = self._get_hash(key)
        # 找到顺时针方向的第一个服务器
        sorted_keys = sorted(self.ring.keys())
        # 使用二分查找找到第一个大于等于hash_val的键
        import bisect
        index = bisect.bisect_left(sorted_keys, hash_val)
        # 如果没有找到，则使用第一个键
        if index == len(sorted_keys):
            index = 0
        
        return self.ring[sorted_keys[index]]
    
    def get_servers(self):
        """获取服务器列表"""
        return list(self.servers)

# 布隆过滤器 (Bloom Filter) 实现
class BloomFilter:
    def __init__(self, size=10000, hash_functions=7):
        self.size = size
        self.bits = [False] * size
        self.hash_functions = hash_functions
        # 初始化哈希函数种子
        self.seeds = [i * 100 + 31 for i in range(hash_functions)]
    
    def _get_hash(self, element, seed):
        """哈希函数"""
        hash_val = 0
        for char in element:
            hash_val = seed * hash_val + ord(char)
        return abs(hash_val % self.size)
    
    def add(self, element):
        """添加元素"""
        for seed in self.seeds:
            hash_val = self._get_hash(element, seed)
            self.bits[hash_val] = True
    
    def might_contain(self, element):
        """判断元素是否可能存在"""
        for seed in self.seeds:
            hash_val = self._get_hash(element, seed)
            if not self.bits[hash_val]:
                return False  # 只要有一个位置为0，元素一定不存在
        return True  # 所有位置都为1，元素可能存在

# 双重哈希 (Double Hashing) 实现的哈希表
class DoubleHashTable:
    DEFAULT_SIZE = 16
    LOAD_FACTOR = 0.75
    
    def __init__(self):
        self.size = self.DEFAULT_SIZE
        self.keys = [None] * self.size
        self.values = [None] * self.size
        self.occupied = [False] * self.size
        self.count = 0
    
    def _hash1(self, key):
        """第一个哈希函数"""
        return abs(hash(key) % self.size)
    
    def _hash2(self, key):
        """第二个哈希函数，用于计算步长"""
        return 1 + abs(hash(key) % (self.size - 1))
    
    def _find_insertion_index(self, key):
        """查找插入位置"""
        h1 = self._hash1(key)
        h2 = self._hash2(key)
        index = h1
        step = 1
        
        # 查找空位置或相同的键
        while self.occupied[index]:
            if self.keys[index] == key:
                return index  # 键已存在，返回该位置以更新值
            index = (h1 + step * h2) % self.size
            step += 1
        
        return index
    
    def _find_index(self, key):
        """查找键的索引"""
        h1 = self._hash1(key)
        h2 = self._hash2(key)
        index = h1
        step = 1
        
        # 查找键
        while self.occupied[index]:
            if self.keys[index] == key:
                return index  # 找到键
            index = (h1 + step * h2) % self.size
            step += 1
            # 避免无限循环
            if step > self.size:
                break
        
        return -1  # 未找到键
    
    def _rehash(self):
        """扩容"""
        old_keys = self.keys.copy()
        old_values = self.values.copy()
        old_occupied = self.occupied.copy()
        
        # 扩大为原来的两倍
        self.size *= 2
        self.keys = [None] * self.size
        self.values = [None] * self.size
        self.occupied = [False] * self.size
        self.count = 0
        
        # 重新插入所有键值对
        for i in range(len(old_occupied)):
            if old_occupied[i]:
                self.put(old_keys[i], old_values[i])
    
    def put(self, key, value):
        """插入键值对"""
        # 检查是否需要扩容
        if self.count / self.size >= self.LOAD_FACTOR:
            self._rehash()
        
        index = self._find_insertion_index(key)
        self.keys[index] = key
        self.values[index] = value
        if not self.occupied[index]:
            self.occupied[index] = True
            self.count += 1
    
    def get(self, key):
        """获取值"""
        index = self._find_index(key)
        return self.values[index] if index != -1 else None
    
    def remove(self, key):
        """删除键值对"""
        index = self._find_index(key)
        if index != -1:
            self.occupied[index] = False
            self.count -= 1
    
    def get_size(self):
        """获取大小"""
        return self.count

# 更多LeetCode哈希相关题目实现

def two_sum(nums: List[int], target: int) -> List[int]:
    """
    LeetCode 1. Two Sum (两数之和)
    题目来源: https://leetcode.com/problems/two-sum/
    
    题目描述:
    给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。
    你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
    你可以按任意顺序返回答案。
    
    示例:
    输入：nums = [2,7,11,15], target = 9
    输出：[0,1]
    解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
    
    算法思路:
    使用哈希表存储每个数字及其对应的索引，遍历数组时检查target - nums[i]是否在哈希表中
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    num_map = {}
    for i, num in enumerate(nums):
        complement = target - num
        if complement in num_map:
            return [num_map[complement], i]
        num_map[num] = i
    return [-1, -1]

def group_anagrams(strs: List[str]) -> List[List[str]]:
    """
    LeetCode 49. Group Anagrams (字母异位词分组)
    题目来源: https://leetcode.com/problems/group-anagrams/
    
    题目描述:
    给你一个字符串数组，请你将字母异位词组合在一起。可以按任意顺序返回结果列表。
    字母异位词是由重新排列源单词的所有字母得到的一个新单词。
    
    示例:
    输入: strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
    输出: [["bat"],["nat","tan"],["ate","eat","tea"]]
    
    算法思路:
    使用排序后的字符串作为哈希表的键，将具有相同排序字符串的单词分组
    
    时间复杂度: O(n * k log k)，其中n是字符串数量，k是字符串最大长度
    空间复杂度: O(n * k)
    """
    anagram_map = {}
    for s in strs:
        sorted_s = ''.join(sorted(s))
        if sorted_s not in anagram_map:
            anagram_map[sorted_s] = []
        anagram_map[sorted_s].append(s)
    return list(anagram_map.values())

def is_anagram(s: str, t: str) -> bool:
    """
    LeetCode 242. Valid Anagram (有效的字母异位词)
    题目来源: https://leetcode.com/problems/valid-anagram/
    
    题目描述:
    给定两个字符串 s 和 t ，编写一个函数来判断 t 是否是 s 的字母异位词。
    注意：若 s 和 t 中每个字符出现的次数都相同，则称 s 和 t 互为字母异位词。
    
    示例:
    输入: s = "anagram", t = "nagaram"
    输出: true
    
    算法思路:
    使用哈希表统计每个字符出现的次数，然后比较两个字符串的字符频率
    
    时间复杂度: O(n)
    空间复杂度: O(1)，因为字符集大小固定为26
    """
    if len(s) != len(t):
        return False
    
    count = [0] * 26
    for char in s:
        count[ord(char) - ord('a')] += 1
    for char in t:
        count[ord(char) - ord('a')] -= 1
        if count[ord(char) - ord('a')] < 0:
            return False
    return True

def length_of_longest_substring(s: str) -> int:
    """
    LeetCode 3. Longest Substring Without Repeating Characters (无重复字符的最长子串)
    题目来源: https://leetcode.com/problems/longest-substring-without-repeating-characters/
    
    题目描述:
    给定一个字符串 s ，请你找出其中不含有重复字符的最长子串的长度。
    
    示例:
    输入: s = "abcabcbb"
    输出: 3
    解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
    
    算法思路:
    使用滑动窗口和哈希表记录字符最后出现的位置
    当遇到重复字符时，移动窗口左边界到重复字符的下一个位置
    
    时间复杂度: O(n)
    空间复杂度: O(min(m, n))，其中m是字符集大小
    """
    char_map = {}
    max_length = 0
    left = 0
    
    for right, char in enumerate(s):
        if char in char_map and char_map[char] >= left:
            left = char_map[char] + 1
        char_map[char] = right
        max_length = max(max_length, right - left + 1)
    
    return max_length

def min_window(s: str, t: str) -> str:
    """
    LeetCode 76. Minimum Window Substring (最小覆盖子串)
    题目来源: https://leetcode.com/problems/minimum-window-substring/
    
    题目描述:
    给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
    
    示例:
    输入：s = "ADOBECODEBANC", t = "ABC"
    输出："BANC"
    解释：最小覆盖子串 "BANC" 包含来自字符串 t 的 'A'、'B' 和 'C'。
    
    算法思路:
    使用滑动窗口和哈希表统计字符频率
    维护一个计数器记录还需要匹配的字符数量
    
    时间复杂度: O(m + n)
    空间复杂度: O(m + n)
    """
    if len(s) < len(t):
        return ""
    
    target = collections.Counter(t)
    window = {}
    left = 0
    right = 0
    required = len(target)
    formed = 0
    min_length = float('inf')
    min_left = 0
    min_right = 0
    
    while right < len(s):
        char = s[right]
        window[char] = window.get(char, 0) + 1
        
        if char in target and window[char] == target[char]:
            formed += 1
        
        while left <= right and formed == required:
            char = s[left]
            
            if right - left + 1 < min_length:
                min_length = right - left + 1
                min_left = left
                min_right = right
            
            window[char] -= 1
            if char in target and window[char] < target[char]:
                formed -= 1
            left += 1
        
        right += 1
    
    return "" if min_length == float('inf') else s[min_left:min_right+1]

def subarray_sum(nums: List[int], k: int) -> int:
    """
    LeetCode 560. Subarray Sum Equals K (和为K的子数组)
    题目来源: https://leetcode.com/problems/subarray-sum-equals-k/
    
    题目描述:
    给你一个整数数组 nums 和一个整数 k ，请你统计并返回该数组中和为 k 的连续子数组的个数。
    
    示例:
    输入：nums = [1,1,1], k = 2
    输出：2
    
    算法思路:
    使用前缀和和哈希表，记录每个前缀和出现的次数
    当prefixSum - k在哈希表中存在时，说明存在和为k的子数组
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    prefix_sum_count = {0: 1}
    prefix_sum = 0
    count = 0
    
    for num in nums:
        prefix_sum += num
        if prefix_sum - k in prefix_sum_count:
            count += prefix_sum_count[prefix_sum - k]
        prefix_sum_count[prefix_sum] = prefix_sum_count.get(prefix_sum, 0) + 1
    
    return count

def top_k_frequent(nums: List[int], k: int) -> List[int]:
    """
    LeetCode 347. Top K Frequent Elements (前K个高频元素)
    题目来源: https://leetcode.com/problems/top-k-frequent-elements/
    
    题目描述:
    给你一个整数数组 nums 和一个整数 k ，请你返回其中出现频率前 k 高的元素。你可以按任意顺序返回答案。
    
    示例:
    输入: nums = [1,1,1,2,2,3], k = 2
    输出: [1,2]
    
    算法思路:
    使用哈希表统计频率，然后使用桶排序或优先队列找出前k个高频元素
    
    时间复杂度: O(n log k)
    空间复杂度: O(n)
    """
    frequency_map = collections.Counter(nums)
    
    # 使用桶排序
    buckets = [[] for _ in range(len(nums) + 1)]
    for num, freq in frequency_map.items():
        buckets[freq].append(num)
    
    result = []
    for i in range(len(buckets) - 1, 0, -1):
        if buckets[i]:
            result.extend(buckets[i])
        if len(result) >= k:
            break
    
    return result[:k]

class RandomizedSet:
    """
    LeetCode 380. Insert Delete GetRandom O(1) (常数时间插入、删除和获取随机元素)
    题目来源: https://leetcode.com/problems/insert-delete-getrandom-o1/
    
    题目描述:
    实现RandomizedSet类：
    RandomizedSet() 初始化 RandomizedSet 对象
    bool insert(int val) 当元素 val 不存在时，向集合中插入该项，并返回 true ；否则，返回 false 。
    bool remove(int val) 当元素 val 存在时，从集合中移除该项，并返回 true ；否则，返回 false 。
    int getRandom() 随机返回现有集合中的一项（测试用例保证调用此方法时集合中至少存在一个元素）。每个元素应该有相同的概率被返回。
    
    算法思路:
    使用哈希表存储值和索引的映射，使用动态数组存储值
    删除时将要删除的元素与最后一个元素交换，然后删除最后一个元素
    
    时间复杂度: O(1) 平均时间复杂度
    空间复杂度: O(n)
    """
    def __init__(self):
        self.value_to_index = {}
        self.values = []
    
    def insert(self, val: int) -> bool:
        if val in self.value_to_index:
            return False
        self.value_to_index[val] = len(self.values)
        self.values.append(val)
        return True
    
    def remove(self, val: int) -> bool:
        if val not in self.value_to_index:
            return False
        
        index = self.value_to_index[val]
        last_element = self.values[-1]
        
        # 将要删除的元素与最后一个元素交换
        self.values[index] = last_element
        self.value_to_index[last_element] = index
        
        # 删除最后一个元素
        self.values.pop()
        del self.value_to_index[val]
        
        return True
    
    def get_random(self) -> int:
        return random.choice(self.values)

class LRUCache:
    """
    LeetCode 146. LRU Cache (LRU缓存)
    题目来源: https://leetcode.com/problems/lru-cache/
    
    题目描述:
    请你设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。
    实现 LRUCache 类：
    LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
    int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
    void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value ；如果不存在，则向缓存中插入该组 key-value 。
    如果插入操作导致关键字数量超过 capacity ，则应该逐出最久未使用的关键字。
    
    算法思路:
    使用哈希表+双向链表实现
    哈希表提供O(1)的查找，双向链表维护访问顺序
    
    时间复杂度: O(1)
    空间复杂度: O(capacity)
    """
    class DLinkedNode:
        def __init__(self, key=0, value=0):
            self.key = key
            self.value = value
            self.prev = None
            self.next = None
    
    def __init__(self, capacity: int):
        self.capacity = capacity
        self.size = 0
        self.cache = {}
        self.head = self.DLinkedNode()
        self.tail = self.DLinkedNode()
        self.head.next = self.tail
        self.tail.prev = self.head
    
    def _add_node(self, node):
        """在头部添加节点"""
        node.prev = self.head
        node.next = self.head.next
        if self.head.next:
            self.head.next.prev = node
        self.head.next = node
    
    def _remove_node(self, node):
        """移除节点"""
        if node.prev:
            node.prev.next = node.next
        if node.next:
            node.next.prev = node.prev
    
    def _move_to_head(self, node):
        """移动节点到头部"""
        self._remove_node(node)
        self._add_node(node)
    
    def _pop_tail(self):
        """弹出尾部节点"""
        if self.tail.prev != self.head:
            node = self.tail.prev
            self._remove_node(node)
            return node
        return None
    
    def get(self, key: int) -> int:
        if key not in self.cache:
            return -1
        node = self.cache[key]
        self._move_to_head(node)
        return node.value
    
    def put(self, key: int, value: int) -> None:
        if key in self.cache:
            node = self.cache[key]
            node.value = value
            self._move_to_head(node)
        else:
            new_node = self.DLinkedNode(key, value)
            self.cache[key] = new_node
            self._add_node(new_node)
            self.size += 1
            
            if self.size > self.capacity:
                tail = self._pop_tail()
                if tail:
                    del self.cache[tail.key]
                    self.size -= 1

def main():
    print("=== LintCode 128. Hash Function ===")
    key = "abcd"
    hash_size = 100
    result = HashFunction.hash_code(key, hash_size)
    print(f"Key: {key}, HASH_SIZE: {hash_size}, Result: {result}")
    print()

    # 测试MD5哈希
    print("=== MD5 Hash Examples ===")
    str1 = "zuochengyunzuochengyunzuochengyun1"
    str2 = "zuochengyunzuochengyunzuochengyun2"
    str3 = "zuochengyunzuochengyunzuochengyun3"

    print(f"String 1 MD5: {hashlib.md5(str1.encode()).hexdigest()}")
    print(f"String 2 MD5: {hashlib.md5(str2.encode()).hexdigest()}")
    print(f"String 3 MD5: {hashlib.md5(str3.encode()).hexdigest()}")
    print()

    # 测试生成字符串
    arr = ['a', 'b']
    n = 5  # 使用较小的n值避免输出过多
    print(f"生成长度为{n}，字符来自arr，所有可能的字符串:")
    strs = HashFunction.generate_strings(arr, n)
    print(f"不同字符串的数量 : {len(strs)}")

    hash_set = set()
    for s in strs:
        hash_set.add(hashlib.md5(s.encode()).hexdigest())
    print(f"不同哈希值的数量 : {len(hash_set)}")
    print()

    # 测试哈希分布
    m = 7
    cnts = [0] * m
    print(f"现在看看这些哈希值，% {m} 之后的余数分布情况")
    for hash_code in hash_set:
        # 简化处理，取前几位字符的ASCII值求模
        total = sum(ord(c) for c in hash_code[:4])
        remainder = total % m
        cnts[remainder] += 1
    for i in range(m):
        print(f"余数 {i} 出现了 {cnts[i]} 次")

    # 测试LeetCode 705. Design HashSet
    print("\n=== LeetCode 705. Design HashSet ===")
    my_hash_set = HashFunction.my_hash_set()
    my_hash_set.add(1)      # set = [1]
    my_hash_set.add(2)      # set = [1, 2]
    print(my_hash_set.contains(1))  # 返回 True
    print(my_hash_set.contains(3))  # 返回 False （未找到）
    my_hash_set.add(2)      # set = [1, 2]
    print(my_hash_set.contains(2))  # 返回 True
    my_hash_set.remove(2)   # set = [1]
    print(my_hash_set.contains(2))  # 返回 False （已移除）

    # 测试LeetCode 706. Design HashMap
    print("\n=== LeetCode 706. Design HashMap ===")
    my_hash_map = HashFunction.my_hash_map()
    my_hash_map.put(1, 1)  # myHashMap 现在为 [[1,1]]
    my_hash_map.put(2, 2)  # myHashMap 现在为 [[1,1], [2,2]]
    print(my_hash_map.get(1))     # 返回 1 ，myHashMap 现在为 [[1,1], [2,2]]
    print(my_hash_map.get(3))     # 返回 -1（未找到），myHashMap 现在为 [[1,1], [2,2]]
    my_hash_map.put(2, 1)  # myHashMap 现在为 [[1,1], [2,1]]（更新已有的值）
    print(my_hash_map.get(2))     # 返回 1 ，myHashMap 现在为 [[1,1], [2,1]]
    my_hash_map.remove(2)  # 删除键为 2 的数据，myHashMap 现在为 [[1,1]]
    print(my_hash_map.get(2))     # 返回 -1（未找到），myHashMap 现在为 [[1,1]]

    # 测试LeetCode 28. Find the Index of the First Occurrence in a String
    print("\n=== LeetCode 28. Find the Index of the First Occurrence in a String ===")
    print(HashFunction.str_str("sadbutsad", "sad"))   # 返回 0
    print(HashFunction.str_str("leetcode", "leeto"))  # 返回 -1

    # 测试LeetCode 187. Repeated DNA Sequences
    print("\n=== LeetCode 187. Repeated DNA Sequences ===")
    dna_result = HashFunction.find_repeated_dna_sequences("AAAAACCCCCAAAAACCCCCCAAAAAGGGTTT")
    print(dna_result)  # ["AAAAACCCCC","CCCCCAAAAA"]
    
    # 测试LeetCode 214题
    print("\n=== LeetCode 214. Shortest Palindrome ===")
    print(f"shortest_palindrome('aacecaaa'): {HashFunction.shortest_palindrome('aacecaaa')}")
    print(f"shortest_palindrome('abcd'): {HashFunction.shortest_palindrome('abcd')}")
    print()
    
    # 测试一致性哈希
    print("=== 一致性哈希 (Consistent Hashing) ===")
    consistent_hash = ConsistentHash(replicas=100)
    consistent_hash.add_server("Server1")
    consistent_hash.add_server("Server2")
    consistent_hash.add_server("Server3")
    
    print(f"键 'user1' 分配到的服务器: {consistent_hash.get_server('user1')}")
    print(f"键 'user2' 分配到的服务器: {consistent_hash.get_server('user2')}")
    print(f"键 'user3' 分配到的服务器: {consistent_hash.get_server('user3')}")
    
    # 移除一个服务器后，观察键的重新分配情况
    print("\n移除 Server2 后:")
    consistent_hash.remove_server("Server2")
    print(f"键 'user1' 分配到的服务器: {consistent_hash.get_server('user1')}")
    print(f"键 'user2' 分配到的服务器: {consistent_hash.get_server('user2')}")
    print(f"键 'user3' 分配到的服务器: {consistent_hash.get_server('user3')}")
    print()
    
    # 测试布隆过滤器
    print("=== 布隆过滤器 (Bloom Filter) ===")
    bloom_filter = BloomFilter(size=10000, hash_functions=7)
    bloom_filter.add("apple")
    bloom_filter.add("banana")
    bloom_filter.add("orange")
    
    print(f"'apple' 可能在集合中: {bloom_filter.might_contain('apple')}")
    print(f"'banana' 可能在集合中: {bloom_filter.might_contain('banana')}")
    print(f"'orange' 可能在集合中: {bloom_filter.might_contain('orange')}")
    print(f"'pear' 可能在集合中: {bloom_filter.might_contain('pear')}")
    print(f"'grape' 可能在集合中: {bloom_filter.might_contain('grape')}")
    print()
    
    # 测试双重哈希表
    print("=== 双重哈希 (Double Hashing) ===")
    double_hash_table = DoubleHashTable()
    double_hash_table.put("apple", 100)
    double_hash_table.put("banana", 200)
    double_hash_table.put("orange", 300)
    
    print(f"'apple' 的值: {double_hash_table.get('apple')}")
    print(f"'banana' 的值: {double_hash_table.get('banana')}")
    print(f"'orange' 的值: {double_hash_table.get('orange')}")
    print(f"'pear' 的值: {double_hash_table.get('pear')}")
    
    double_hash_table.remove("banana")
    print(f"移除 'banana' 后的值: {double_hash_table.get('banana')}")
    print(f"哈希表大小: {double_hash_table.get_size()}")
    
    # 测试更多LeetCode哈希相关题目
    print("\n=== 更多LeetCode哈希相关题目测试 ===")
    
    # 测试LeetCode 1. Two Sum
    print("\n=== LeetCode 1. Two Sum ===")
    nums = [2, 7, 11, 15]
    target = 9
    result = two_sum(nums, target)
    print(f"nums: {nums}, target: {target}, result: {result}")
    
    # 测试LeetCode 49. Group Anagrams
    print("\n=== LeetCode 49. Group Anagrams ===")
    strs = ["eat", "tea", "tan", "ate", "nat", "bat"]
    result = group_anagrams(strs)
    print(f"strs: {strs}")
    print(f"grouped anagrams: {result}")
    
    # 测试LeetCode 242. Valid Anagram
    print("\n=== LeetCode 242. Valid Anagram ===")
    s1, s2 = "anagram", "nagaram"
    result = is_anagram(s1, s2)
    print(f"'{s1}' and '{s2}' are anagrams: {result}")
    
    # 测试LeetCode 3. Longest Substring Without Repeating Characters
    print("\n=== LeetCode 3. Longest Substring Without Repeating Characters ===")
    s = "abcabcbb"
    result = length_of_longest_substring(s)
    print(f"String: '{s}', longest substring length: {result}")
    
    # 测试LeetCode 76. Minimum Window Substring
    print("\n=== LeetCode 76. Minimum Window Substring ===")
    s = "ADOBECODEBANC"
    t = "ABC"
    result = min_window(s, t)
    print(f"s: '{s}', t: '{t}', min window: '{result}'")
    
    # 测试LeetCode 560. Subarray Sum Equals K
    print("\n=== LeetCode 560. Subarray Sum Equals K ===")
    nums = [1, 1, 1]
    k = 2
    result = subarray_sum(nums, k)
    print(f"nums: {nums}, k: {k}, subarray count: {result}")
    
    # 测试LeetCode 347. Top K Frequent Elements
    print("\n=== LeetCode 347. Top K Frequent Elements ===")
    nums = [1, 1, 1, 2, 2, 3]
    k = 2
    result = top_k_frequent(nums, k)
    print(f"nums: {nums}, k: {k}, top k frequent: {result}")
    
    # 测试LeetCode 380. Insert Delete GetRandom O(1)
    print("\n=== LeetCode 380. Insert Delete GetRandom O(1) ===")
    randomized_set = RandomizedSet()
    print(f"Insert 1: {randomized_set.insert(1)}")
    print(f"Insert 2: {randomized_set.insert(2)}")
    print(f"Insert 1 again: {randomized_set.insert(1)}")
    print(f"Get random: {randomized_set.get_random()}")
    print(f"Remove 2: {randomized_set.remove(2)}")
    print(f"Remove 3: {randomized_set.remove(3)}")
    print(f"Get random: {randomized_set.get_random()}")
    
    # 测试LeetCode 146. LRU Cache
    print("\n=== LeetCode 146. LRU Cache ===")
    lru_cache = LRUCache(2)
    lru_cache.put(1, 1)
    lru_cache.put(2, 2)
    print(f"Get 1: {lru_cache.get(1)}")
    lru_cache.put(3, 3)  # 这会使得键2被移除
    print(f"Get 2: {lru_cache.get(2)}")
    print(f"Get 3: {lru_cache.get(3)}")


if __name__ == "__main__":
    main()
# -*- coding: utf-8 -*-

"""
Trie树扩展题目合集 - Python版本
从各大算法平台收集的Trie树相关题目
"""

class ExtendedTrieProblems:
    """
    题目1: LeetCode 745. 前缀和后缀搜索
    题目来源：LeetCode
    题目链接：https://leetcode.cn/problems/prefix-and-suffix-search/
    
    解题思路：
    1. 使用Trie树存储所有单词，每个节点记录经过该节点的最大下标
    2. 对于每个单词，将其所有后缀+分隔符+单词本身插入Trie树
    3. 查询时，将后缀+分隔符+前缀作为查询字符串
    
    时间复杂度分析：
    1. 构造函数：O(N*L^2)，其中N是单词数量，L是单词最大长度
    2. f函数：O(P+S)，其中P是前缀长度，S是后缀长度
    空间复杂度分析：
    1. O(N*L^2)，需要存储所有单词的所有后缀组合
    是否为最优解：是，这是解决此类问题的经典方法
    """
    class WordFilter:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.weight = 0  # 存储经过该节点的最大下标
        
        def __init__(self, words):
            self.root = self.TrieNode()
            for weight, word in enumerate(words):
                # 对于每个单词，插入所有后缀+分隔符+单词的组合
                for i in range(len(word) + 1):
                    key = word[i:] + "{" + word
                    node = self.root
                    for c in key:
                        if c not in node.children:
                            node.children[c] = self.TrieNode()
                        node = node.children[c]
                        node.weight = weight  # 更新最大下标
        
        def f(self, prefix, suffix):
            key = suffix + "{" + prefix
            node = self.root
            for c in key:
                if c not in node.children:
                    return -1
                node = node.children[c]
            return node.weight

    """
    题目2: LeetCode 336. 回文对
    题目来源：LeetCode
    题目链接：https://leetcode.cn/problems/palindrome-pairs/
    
    解题思路：
    1. 使用Trie树存储所有单词的逆序
    2. 对于每个单词，在Trie树中查找能与之形成回文串的单词
    3. 分情况讨论：当前单词是较长部分、当前单词是较短部分
    
    时间复杂度分析：
    1. 构建Trie树：O(N*L)，其中N是单词数量，L是单词平均长度
    2. 查询过程：O(N*L^2)，需要检查每个单词的所有前缀和后缀
    空间复杂度分析：
    1. O(N*L)，Trie树存储空间
    是否为最优解：是，Trie树是解决此类问题的高效方法
    """
    class PalindromePairs:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.index = -1  # 单词在数组中的下标
                self.list = []   # 存储经过该节点且剩余部分是回文的单词下标
        
        def is_palindrome(self, word, i, j):
            """检查子串word[i:j+1]是否是回文"""
            while i < j:
                if word[i] != word[j]:
                    return False
                i += 1
                j -= 1
            return True
        
        def add_word(self, root, word, index):
            """逆序插入单词到Trie树"""
            node = root
            # 逆序插入单词
            for i in range(len(word)-1, -1, -1):
                c = word[i]
                if c not in node.children:
                    node.children[c] = self.TrieNode()
                node = node.children[c]
                # 如果单词的前缀是回文，记录当前下标
                if self.is_palindrome(word, 0, i):
                    node.list.append(index)
            node.list.append(index)
            node.index = index
        
        def search(self, words, i, node, result):
            """在Trie树中搜索能与words[i]形成回文对的单词"""
            # 正序匹配单词
            for j in range(len(words[i])):
                if node.index >= 0 and node.index != i and self.is_palindrome(words[i], j, len(words[i])-1):
                    result.append([i, node.index])
                
                c = words[i][j]
                if c not in node.children:
                    return
                node = node.children[c]
            
            # 处理Trie树中剩余的匹配
            for j in node.list:
                if i == j:
                    continue
                result.append([i, j])
        
        def palindrome_pairs(self, words):
            result = []
            root = self.TrieNode()
            
            # 构建Trie树，存储单词的逆序
            for i, word in enumerate(words):
                self.add_word(root, word, i)
            
            # 对于每个单词，在Trie树中查找匹配
            for i in range(len(words)):
                self.search(words, i, root, result)
            
            return result

    """
    题目3: POJ 2001 Shortest Prefixes
    题目来源：POJ
    题目链接：http://poj.org/problem?id=2001
    
    解题思路：
    1. 使用Trie树存储所有单词
    2. 记录每个节点被经过的次数
    3. 对于每个单词，找到第一个出现次数为1的节点，该节点之前的前缀就是最短唯一前缀
    
    时间复杂度分析：
    1. 构建Trie树：O(∑len(s))
    2. 查询过程：O(∑len(s))
    空间复杂度分析：
    1. O(∑len(s))
    是否为最优解：是，Trie树是解决此类问题的最优方法
    """
    class ShortestPrefixes:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.count = 0  # 经过该节点的单词数量
        
        def find_shortest_prefixes(self, words):
            result = {}
            root = self.TrieNode()
            
            # 构建Trie树
            for word in words:
                node = root
                for c in word:
                    if c not in node.children:
                        node.children[c] = self.TrieNode()
                    node = node.children[c]
                    node.count += 1
            
            # 为每个单词寻找最短唯一前缀
            for word in words:
                node = root
                prefix = ""
                for c in word:
                    prefix += c
                    node = node.children[c]
                    # 如果当前节点只被当前单词经过，则找到最短唯一前缀
                    if node.count == 1:
                        break
                result[word] = prefix
            
            return result

    """
    题目4: HDU 1247 Hat's Words
    题目来源：HDU
    题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1247
    
    解题思路：
    1. 使用Trie树存储所有单词
    2. 对于每个单词，检查它是否能被拆分成两个都在字典中的单词
    3. 使用Trie树快速检查每个前缀和后缀是否在字典中
    
    时间复杂度分析：
    1. 构建Trie树：O(∑len(s))
    2. 检查过程：O(N*L^2)，其中N是单词数量，L是单词最大长度
    空间复杂度分析：
    1. O(∑len(s))
    是否为最优解：是，Trie树提供高效的字符串查找
    """
    class HatsWords:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.is_end = False
        
        def insert(self, root, word):
            """插入单词到Trie树"""
            node = root
            for c in word:
                if c not in node.children:
                    node.children[c] = self.TrieNode()
                node = node.children[c]
            node.is_end = True
        
        def search(self, root, word):
            """在Trie树中搜索单词"""
            node = root
            for c in word:
                if c not in node.children:
                    return False
                node = node.children[c]
            return node.is_end
        
        def is_hats_word(self, root, word):
            """检查单词是否是hat's word"""
            # 尝试所有可能的分割点
            for i in range(1, len(word)):
                prefix = word[:i]
                suffix = word[i:]
                if self.search(root, prefix) and self.search(root, suffix):
                    return True
            return False
        
        def find_hats_words(self, words):
            result = []
            root = self.TrieNode()
            
            # 构建Trie树
            for word in words:
                self.insert(root, word)
            
            # 检查每个单词是否是hat's word
            for word in words:
                if self.is_hats_word(root, word):
                    result.append(word)
            
            return result

    """
    题目5: 牛客网 最长公共前缀
    题目来源：牛客网
    题目链接：https://www.nowcoder.com/practice/28eb3175488f4434a4a6207f6f484f47
    
    解题思路：
    1. 使用Trie树存储所有字符串
    2. 从根节点开始，找到第一个有多个子节点的节点
    3. 该节点之前的前缀就是最长公共前缀
    
    时间复杂度分析：
    1. 构建Trie树：O(∑len(s))
    2. 查找过程：O(min(len(s)))
    空间复杂度分析：
    1. O(∑len(s))
    是否为最优解：是，Trie树提供直观的解决方案
    """
    class LongestCommonPrefix:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.count = 0  # 经过该节点的字符串数量
        
        def longest_common_prefix(self, strs):
            if not strs:
                return ""
            if len(strs) == 1:
                return strs[0]
            
            # 直接比较法，避免Trie树的内存问题
            prefix = ""
            min_len = min(len(s) for s in strs)
            
            for i in range(min_len):
                char = strs[0][i]
                for j in range(1, len(strs)):
                    if strs[j][i] != char:
                        return prefix
                prefix += char
            
            return prefix

    """
    题目6: 洛谷 P2580 点名系统
    题目来源：洛谷
    题目链接：https://www.luogu.com.cn/problem/P2580
    
    解题思路：
    1. 使用Trie树存储所有学生姓名
    2. 每个节点记录点名状态
    3. 根据点名状态输出相应结果
    
    时间复杂度分析：
    1. 构建Trie树：O(∑len(s))
    2. 查询过程：O(∑len(s))
    空间复杂度分析：
    1. O(∑len(s))
    是否为最优解：是，Trie树提供高效的姓名查找
    """
    class RollCallSystem:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.status = 0  # 0: 未点名, 1: 已点名
        
        def __init__(self, names):
            self.root = self.TrieNode()
            # 构建Trie树，插入所有学生姓名
            for name in names:
                self.insert(name)
        
        def insert(self, name):
            """插入学生姓名"""
            node = self.root
            for c in name:
                if c not in node.children:
                    node.children[c] = self.TrieNode()
                node = node.children[c]
            node.status = 0  # 初始状态为未点名
        
        def call(self, name):
            """点名"""
            node = self.root
            for c in name:
                if c not in node.children:
                    return "WRONG"  # 姓名不存在
                node = node.children[c]
            
            if node.status == 0:
                node.status = 1  # 标记为已点名
                return "OK"
            elif node.status == 1:
                return "REPEAT"
            else:
                return "WRONG"

    """
    题目7: CodeChef DICT - Dictionary
    题目来源：CodeChef
    题目链接：https://www.codechef.com/problems/DICT
    
    解题思路：
    1. 使用Trie树存储字典中的所有单词
    2. 每个节点维护以该节点为前缀的所有单词
    3. 查询时找到前缀对应的节点，输出该节点存储的所有单词
    
    时间复杂度分析：
    1. 构建Trie树：O(∑len(s))
    2. 查询过程：O(P + K)，其中P是前缀长度，K是输出单词数量
    空间复杂度分析：
    1. O(∑len(s))
    是否为最优解：是，Trie树是解决前缀查询的高效方法
    """
    class DictionarySearch:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.words = []  # 存储以该节点为前缀的所有单词
        
        def __init__(self, dictionary):
            self.root = self.TrieNode()
            # 构建Trie树
            for word in dictionary:
                self.insert(word)
        
        def insert(self, word):
            """插入单词"""
            node = self.root
            for c in word:
                if c not in node.children:
                    node.children[c] = self.TrieNode()
                node = node.children[c]
                node.words.append(word)
        
        def search(self, prefix):
            """搜索前缀"""
            node = self.root
            for c in prefix:
                if c not in node.children:
                    return []  # 前缀不存在
                node = node.children[c]
            # 返回该前缀对应的所有单词，按字典序排序
            return sorted(node.words)

    """
    题目8: SPOJ PHONELST - Phone List
    题目来源：SPOJ
    题目链接：https://www.spoj.com/problems/PHONELIST/
    
    解题思路：
    1. 使用Trie树存储所有电话号码
    2. 在插入过程中检查前缀关系
    3. 优化实现，提高效率
    
    时间复杂度分析：
    1. O(∑len(s))
    空间复杂度分析：
    1. O(∑len(s))
    是否为最优解：是
    """
    class SPOJPhoneList:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.is_end = False
        
        def has_consistent_list(self, phone_numbers):
            # 按长度排序，先插入短的
            phone_numbers.sort(key=len)
            root = self.TrieNode()
            
            for phone in phone_numbers:
                node = root
                created_new = False
                
                for i in range(len(phone)):
                    digit = phone[i]
                    if digit not in node.children:
                        node.children[digit] = self.TrieNode()
                        created_new = True
                    
                    node = node.children[digit]
                    
                    # 如果在插入过程中遇到已标记的结尾，说明存在前缀关系
                    if node.is_end:
                        return False
                
                # 如果当前节点有子节点，说明当前号码是其他号码的前缀
                if not created_new:
                    return False
                
                node.is_end = True
            
            return True

    """
    题目9: 剑指Offer 45. 把数组排成最小的数
    题目来源：剑指Offer
    题目链接：https://leetcode.cn/problems/ba-shu-zu-pai-cheng-zui-xiao-de-shu-lcof/
    
    解题思路：
    1. 使用自定义比较器进行字符串排序
    2. 比较a+b和b+a的大小
    3. 按特定顺序拼接字符串
    
    时间复杂度分析：
    1. O(NlogN)
    空间复杂度分析：
    1. O(N)
    是否为最优解：是
    """
    class MinNumber:
        def min_number(self, nums):
            # 将数字转换为字符串
            str_nums = [str(num) for num in nums]
            
            # 自定义排序：比较a+b和b+a的大小
            from functools import cmp_to_key
            def compare(x, y):
                if x + y < y + x:
                    return -1
                elif x + y > y + x:
                    return 1
                else:
                    return 0
            
            str_nums.sort(key=cmp_to_key(compare))
            
            # 拼接结果
            return ''.join(str_nums)

    """
    题目10: 杭电OJ 1251 统计难题
    题目来源：杭电OJ
    题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1251
    
    解题思路：
    1. 使用Trie树存储所有单词
    2. 每个节点记录经过该节点的单词数量
    3. 查询时找到前缀对应的节点，返回该节点的计数
    
    时间复杂度分析：
    1. 构建Trie树：O(∑len(s))
    2. 查询过程：O(P)，其中P是前缀长度
    空间复杂度分析：
    1. O(∑len(s))
    是否为最优解：是
    """
    class StatisticalProblem:
        class TrieNode:
            def __init__(self):
                self.children = {}
                self.count = 0  # 经过该节点的单词数量
        
        def __init__(self, words):
            self.root = self.TrieNode()
            for word in words:
                self.insert(word)
        
        def insert(self, word):
            """插入单词"""
            node = self.root
            for c in word:
                if c not in node.children:
                    node.children[c] = self.TrieNode()
                node = node.children[c]
                node.count += 1
        
        def prefix_count(self, prefix):
            """统计前缀数量"""
            node = self.root
            for c in prefix:
                if c not in node.children:
                    return 0
                node = node.children[c]
            return node.count


def test():
    """测试所有题目"""
    solution = ExtendedTrieProblems()
    
    print("=== 测试WordFilter ===")
    words1 = ["apple", "application", "apply"]
    wf = solution.WordFilter(words1)
    print(f"f('a', 'e'): {wf.f('a', 'e')}")  # 应该返回2（apply的下标）
    
    print("\n=== 测试PalindromePairs ===")
    words2 = ["abcd", "dcba", "lls", "s", "sssll"]
    pp = solution.PalindromePairs()
    pairs = pp.palindrome_pairs(words2)
    print(f"回文对数量: {len(pairs)}")
    
    print("\n=== 测试ShortestPrefixes ===")
    words3 = ["z", "dog", "duck", "dove"]
    sp = solution.ShortestPrefixes()
    prefixes = sp.find_shortest_prefixes(words3)
    print(f"最短唯一前缀: {prefixes}")
    
    print("\n=== 测试HatsWords ===")
    words4 = ["a", "hat", "hats", "word", "words", "hatword"]
    hw = solution.HatsWords()
    hats_words = hw.find_hats_words(words4)
    print(f"Hat's words: {hats_words}")
    
    print("\n=== 测试LongestCommonPrefix ===")
    words5 = ["flower", "flow", "flight"]
    lcp = solution.LongestCommonPrefix()
    common_prefix = lcp.longest_common_prefix(words5)
    print(f"最长公共前缀: '{common_prefix}'")
    
    print("\n=== 测试RollCallSystem ===")
    names = ["alice", "bob", "charlie"]
    rcs = solution.RollCallSystem(names)
    print(f"点名alice: {rcs.call('alice')}")  # OK
    print(f"点名alice: {rcs.call('alice')}")  # REPEAT
    print(f"点名david: {rcs.call('david')}")  # WRONG
    
    print("\n=== 测试DictionarySearch ===")
    dictionary = ["apple", "application", "apply", "banana", "band"]
    ds = solution.DictionarySearch(dictionary)
    results = ds.search("app")
    print(f"前缀'app'的单词: {results}")
    
    print("\n=== 测试SPOJPhoneList ===")
    phones1 = ["911", "97625999", "91125426"]
    phones2 = ["113", "12340", "123440", "12345", "98346"]
    spl = solution.SPOJPhoneList()
    print(f"电话号码列表1是否一致: {spl.has_consistent_list(phones1)}")  # False
    print(f"电话号码列表2是否一致: {spl.has_consistent_list(phones2)}")  # True
    
    print("\n=== 测试MinNumber ===")
    nums = [3, 30, 34, 5, 9]
    mn = solution.MinNumber()
    min_num = mn.min_number(nums)
    print(f"最小数字: {min_num}")
    
    print("\n=== 测试StatisticalProblem ===")
    words6 = ["banana", "band", "bee", "absolute", "acm"]
    stat = solution.StatisticalProblem(words6)
    print(f"前缀'ba'的数量: {stat.prefix_count('ba')}")  # 2
    print(f"前缀'b'的数量: {stat.prefix_count('b')}")   # 3
    print(f"前缀'abc'的数量: {stat.prefix_count('abc')}") # 0


if __name__ == "__main__":
    test()
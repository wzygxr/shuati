# LeetCode 211. 添加与搜索单词 - 数据结构设计 - Python实现
# 
# 题目描述：
# 请你设计一个数据结构，支持添加新单词和查找字符串是否与任何先前添加的字符串匹配。
# 实现词典类 WordDictionary：
# - WordDictionary() 初始化词典对象
# - void addWord(word) 将 word 添加到数据结构中，之后可以对它进行匹配
# - bool search(word) 如果数据结构中存在字符串与 word 匹配，则返回 true；否则返回 false。
# word 中可能包含一些 '.'，每个 '.' 都可以表示任何一个字母。
# 
# 测试链接：https://leetcode.cn/problems/design-add-and-search-words-data-structure/
# 
# 算法思路：
# 1. 使用字典实现前缀树存储所有添加的单词
# 2. 对于普通字符的搜索，按照标准前缀树搜索进行
# 3. 对于包含 '.' 的搜索，使用深度优先搜索（DFS）尝试所有可能的字符路径
# 
# 核心优化：
# 使用前缀树存储单词，对于包含通配符 '.' 的搜索使用DFS遍历所有可能路径
# 
# 时间复杂度分析：
# - 添加单词：O(L)，其中L是单词长度
# - 搜索单词：O(26^L)，其中L是单词长度（最坏情况，所有字符都是 '.'）
# - 总体时间复杂度：O(L) 添加，O(26^L) 搜索
# 
# 空间复杂度分析：
# - 前缀树空间：O(N*L)，其中N是插入的单词数量，L是平均单词长度
# - 递归栈空间：O(L)，其中L是最长单词的长度
# - 总体空间复杂度：O(N*L + L)
# 
# 是否最优解：是
# 理由：使用前缀树可以高效地存储和查询单词，对于通配符搜索使用DFS是标准解法
# 
# 工程化考虑：
# 1. 异常处理：输入为空或单词为空的情况
# 2. 边界情况：单词只包含 '.' 或不包含 '.' 的情况
# 3. 极端输入：大量单词或单词很长的情况
# 4. 鲁棒性：处理特殊字符和重复添加
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现前缀树，性能较高但空间固定
# C++：可使用指针实现前缀树节点，更节省空间
# 
# 相关题目扩展：
# 1. LeetCode 211. 添加与搜索单词 - 数据结构设计 (本题)
# 2. LeetCode 208. 实现 Trie (前缀树)
# 3. LeetCode 212. 单词搜索 II
# 4. LintCode 473. 单词的添加与查找
# 5. 牛客网 NC138. 添加与搜索单词
# 6. HackerRank - Word Search with Wildcards
# 7. CodeChef - WILDCARD
# 8. SPOJ - WSEARCH
# 9. AtCoder - Wildcard Matching

class TrieNode:
    """
    前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持任意字符集
    包含单词结尾标记
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        # 子节点字典，键为字符，值为TrieNode
        self.children = {}
        # 标记该节点是否是单词结尾
        self.is_end = False

class WordDictionary:
    """
    单词词典类
    
    算法思路：
    使用TrieNode构建树结构，支持单词的添加和搜索（包括通配符搜索）
    
    时间复杂度分析：
    - 添加：O(L)，L为单词长度
    - 搜索：O(26^L)，L为单词长度（最坏情况）
    
    空间复杂度分析：
    - 总体：O(N*L)，N为单词数，L为平均长度
    """
    
    def __init__(self):
        """
        初始化词典对象
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = TrieNode()
    
    def addWord(self, word: str) -> None:
        """
        向词典中添加单词
        
        算法步骤：
        1. 从根节点开始遍历单词的每个字符
        2. 对于每个字符，如果子节点不存在则创建
        3. 移动到子节点继续处理下一个字符
        4. 单词遍历完成后标记当前节点为单词结尾
        
        时间复杂度：O(L)，其中L是单词长度
        空间复杂度：O(L)，最坏情况下需要创建新节点
        
        :param word: 待添加的单词
        """
        if not word:
            return  # 空字符串不添加
        
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
    
    def search(self, word: str) -> bool:
        """
        搜索单词是否存在于词典中
        
        算法步骤：
        1. 调用DFS方法从根节点开始搜索
        
        时间复杂度：O(26^L)，其中L是单词长度（最坏情况）
        空间复杂度：O(L)，递归栈空间
        
        :param word: 待搜索的单词（可能包含 '.' 通配符）
        :return: 如果单词存在返回True，否则返回False
        """
        if not word:
            return False  # 空字符串不存在
        
        return self._dfs(word, 0, self.root)
    
    def _dfs(self, word: str, index: int, node: TrieNode) -> bool:
        """
        使用深度优先搜索查找单词
        
        算法步骤：
        1. 递归终止条件：已处理完所有字符
           a. 如果当前节点是单词结尾，返回True
           b. 否则返回False
        2. 处理当前字符：
           a. 如果是普通字符，检查对应子节点是否存在
           b. 如果是通配符 '.'，尝试所有可能的子节点
        3. 递归处理剩余字符
        
        时间复杂度：O(26^L)，其中L是单词长度（最坏情况）
        空间复杂度：O(L)，递归栈空间
        
        :param word: 待搜索的单词
        :param index: 当前处理的字符索引
        :param node: 当前前缀树节点
        :return: 如果能找到匹配的单词返回True，否则返回False
        """
        # 递归终止条件：已处理完所有字符
        if index == len(word):
            return node.is_end
        
        char = word[index]
        
        # 处理当前字符
        if char == '.':
            # 通配符 '.'，尝试所有可能的子节点
            for child_node in node.children.values():
                if self._dfs(word, index + 1, child_node):
                    return True
            return False
        else:
            # 普通字符，检查对应子节点是否存在
            if char not in node.children:
                return False
            return self._dfs(word, index + 1, node.children[char])

def test_word_dictionary():
    """
    单元测试函数
    
    测试用例设计：
    1. 正常添加和搜索：验证基本功能正确性
    2. 通配符搜索测试：验证 '.' 字符的处理
    3. 空字符串处理：验证边界条件处理
    4. 重复添加处理：验证重复操作的正确性
    5. 不存在的单词搜索：验证错误情况处理
    """
    word_dict = WordDictionary()
    
    # 测试用例1：正常添加和搜索
    word_dict.addWord("bad")
    word_dict.addWord("dad")
    word_dict.addWord("mad")
    
    assert not word_dict.search("pad")  # 应该返回False
    assert word_dict.search("bad")     # 应该返回True
    assert word_dict.search(".ad")     # 应该返回True
    assert word_dict.search("b..")     # 应该返回True
    
    # 测试用例2：空字符串处理
    assert not word_dict.search("")     # 空字符串应该返回False
    
    # 测试用例3：不存在的单词
    assert not word_dict.search("b...") # 不存在的单词应该返回False
    
    # 测试用例4：重复添加
    word_dict.addWord("bad")
    assert word_dict.search("bad")     # 重复添加后搜索应该仍然返回True
    
    print("LeetCode 211 所有测试用例通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 添加大量单词：测试添加操作的性能
    2. 搜索操作性能：测试普通搜索和通配符搜索的性能
    """
    import time
    
    word_dict = WordDictionary()
    
    # 添加性能测试
    start_time = time.time()
    
    # 添加10000个单词
    for i in range(10000):
        word_dict.addWord(f"word{i}")
    
    insert_time = time.time() - start_time
    print(f"添加10000个单词耗时: {insert_time:.3f}秒")
    
    # 普通搜索性能测试
    start_time = time.time()
    
    # 普通搜索1000次
    for i in range(1000):
        word_dict.search(f"word{i}")
    
    search_time = time.time() - start_time
    print(f"普通搜索1000次耗时: {search_time:.3f}秒")
    
    # 通配符搜索性能测试
    start_time = time.time()
    
    # 通配符搜索100次
    for i in range(100):
        word_dict.search(f"w.rd{i}")
    
    wildcard_time = time.time() - start_time
    print(f"通配符搜索100次耗时: {wildcard_time:.3f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_word_dictionary()
    
    # 运行性能测试
    performance_test()
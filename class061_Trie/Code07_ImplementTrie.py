# LintCode 442. Implement Trie (Prefix Tree)
# 题目描述：
# 实现一个Trie（前缀树），包含insert, search, 和startsWith这三个操作。
# 测试链接：https://www.lintcode.com/problem/442/
#
# 相关题目扩展：
# 1. LeetCode 208. 实现 Trie (前缀树) (本题与LeetCode 208相同)
# 2. LeetCode 212. 单词搜索 II
# 3. LeetCode 421. 数组中两个数的最大异或值
# 4. HackerRank Contacts
# 5. SPOJ DICT
# 6. SPOJ PHONELST
# 7. LintCode 442. 实现 Trie (前缀树)
# 8. 牛客网 NC105. 二分查找-II
# 9. 牛客网 NC138. 字符串匹配
# 10. CodeChef - ANAGRAMS

class TrieNode:
    """
    前缀树节点类
    """
    def __init__(self):
        # 子节点字典
        self.children = {}
        # 单词结尾标记
        self.is_end = False

class Trie:
    """
    实现Trie（前缀树）
    
    算法思路：
    1. 设计TrieNode类，包含子节点字典和单词结尾标记
    2. 实现insert方法：逐个字符遍历单词，创建节点并建立连接
    3. 实现search方法：逐个字符遍历单词，查找节点并检查是否为单词结尾
    4. 实现startsWith方法：逐个字符遍历前缀，查找节点
    
    时间复杂度分析：
    - insert操作：O(len(word))，其中len(word)是单词长度
    - search操作：O(len(word))，其中len(word)是单词长度
    - startsWith操作：O(len(prefix))，其中len(prefix)是前缀长度
    
    空间复杂度分析：
    - Trie空间：O(∑len(words))，用于存储所有插入的单词
    - 总体空间复杂度：O(∑len(words))
    
    是否最优解：是
    理由：使用前缀树可以高效地处理字符串的插入、搜索和前缀匹配操作
    
    工程化考虑：
    1. 异常处理：输入为空或字符串包含非法字符的情况
    2. 边界情况：空字符串的情况
    3. 极端输入：大量操作或极长字符串的情况
    4. 鲁棒性：处理大小写敏感和特殊字符
    
    语言特性差异：
    Java：使用二维数组实现前缀树，利用字符减法计算路径索引
    C++：可使用指针实现前缀树节点，更节省空间
    Python：可使用字典实现前缀树，代码更简洁
    """
    
    def __init__(self):
        """
        初始化前缀树
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        # 根节点
        self.root = TrieNode()
    
    def insert(self, word):
        """
        向前缀树中插入单词
        
        算法思路：
        1. 从根节点开始遍历单词
        2. 对于每个字符，如果子节点不存在则创建新节点
        3. 移动到子节点，继续遍历
        4. 遍历完成后，标记单词结尾
        
        时间复杂度：O(len(word))，其中len(word)是单词长度
        空间复杂度：O(len(word))，最坏情况下需要创建新节点
        
        :param word: 待插入的单词
        """
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
    
    def search(self, word):
        """
        在前缀树中搜索单词
        
        算法思路：
        1. 从根节点开始遍历单词
        2. 对于每个字符，如果子节点不存在则返回False
        3. 移动到子节点，继续遍历
        4. 遍历完成后，检查当前节点是否为单词结尾
        
        时间复杂度：O(len(word))，其中len(word)是单词长度
        空间复杂度：O(1)
        
        :param word: 待搜索的单词
        :return: 如果单词存在返回True，否则返回False
        """
        node = self.root
        for char in word:
            if char not in node.children:
                return False
            node = node.children[char]
        return node.is_end
    
    def startsWith(self, prefix):
        """
        检查前缀树中是否存在以prefix为前缀的单词
        
        算法思路：
        1. 从根节点开始遍历前缀
        2. 对于每个字符，如果子节点不存在则返回False
        3. 移动到子节点，继续遍历
        4. 遍历完成后，返回True
        
        时间复杂度：O(len(prefix))，其中len(prefix)是前缀长度
        空间复杂度：O(1)
        
        :param prefix: 待检查的前缀
        :return: 如果存在以prefix为前缀的单词返回True，否则返回False
        """
        node = self.root
        for char in prefix:
            if char not in node.children:
                return False
            node = node.children[char]
        return True
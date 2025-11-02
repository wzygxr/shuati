# SPOJ DICT
# 题目描述：
# 给定一个字典和一个前缀，找出字典中所有以该前缀开头的单词，并按字典序输出。
# 测试链接：https://www.spoj.com/problems/DICT/
#
# 相关题目扩展：
# 1. LeetCode 208. 实现 Trie (前缀树)
# 2. LeetCode 212. 单词搜索 II
# 3. LeetCode 421. 数组中两个数的最大异或值
# 4. HackerRank Contacts
# 5. SPOJ DICT (本题)
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
        self.word = None

class Trie:
    """
    前缀树类
    """
    def __init__(self):
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
        node.word = word

def dict_words(words, prefix):
    """
    在字典中查找具有特定前缀的单词
    
    算法思路：
    1. 构建前缀树，将字典中的所有单词插入前缀树
    2. 查找前缀在前缀树中的位置
    3. 从该位置开始，深度优先搜索所有可能的单词
    4. 按字典序收集所有匹配的单词
    
    时间复杂度分析：
    - 构建前缀树：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
    - 查找过程：O(len(prefix) + ∑len(results))，其中∑len(results)是所有结果单词长度之和
    - 总体时间复杂度：O(∑len(words[i]) + len(prefix) + ∑len(results))
    
    空间复杂度分析：
    - 前缀树空间：O(∑len(words[i]))，用于存储所有单词
    - 递归栈空间：O(max(len(words[i])))，其中max(len(words[i]))是最长单词的长度
    - 结果空间：O(∑len(results))，用于存储结果单词
    - 总体空间复杂度：O(∑len(words[i]) + max(len(words[i])) + ∑len(results))
    
    是否最优解：是
    理由：使用前缀树可以高效地存储和查询单词，避免了重复搜索
    
    工程化考虑：
    1. 异常处理：输入为空或字典为空的情况
    2. 边界情况：前缀为空或不存在匹配单词的情况
    3. 极端输入：大量单词或极长单词的情况
    4. 鲁棒性：处理重复单词和特殊字符
    
    语言特性差异：
    Java：使用二维数组实现前缀树，利用字符减法计算路径索引
    C++：可使用指针实现前缀树节点，更节省空间
    Python：可使用字典实现前缀树，代码更简洁
    
    :param words: 字典单词数组
    :param prefix: 查找前缀
    :return: 匹配的单词列表
    """
    # 构建前缀树
    trie = Trie()
    for word in words:
        trie.insert(word)
    
    # 查找前缀节点
    node = trie.root
    for char in prefix:
        if char not in node.children:
            return []  # 前缀不存在
        node = node.children[char]
    
    # 深度优先搜索查找所有匹配单词
    result = []
    
    def dfs(current_node, current_path):
        """
        深度优先搜索查找所有匹配单词
        
        算法思路：
        1. 从当前节点开始，遍历所有子节点
        2. 对于每个子节点，如果它是单词结尾，则将当前路径加入结果
        3. 递归搜索子节点的子树
        4. 回溯时删除添加的字符
        
        时间复杂度：O(∑len(results))，其中∑len(results)是所有结果单词长度之和
        空间复杂度：O(max(len(results)))，递归栈空间
        """
        # 如果当前节点是单词结尾，将路径加入结果
        if current_node.word:
            result.append(current_node.word)
        
        # 按字典序遍历所有子节点
        for char in sorted(current_node.children.keys()):
            dfs(current_node.children[char], current_path + char)
    
    dfs(node, prefix)
    return result
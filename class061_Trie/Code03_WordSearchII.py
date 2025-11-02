# 在二维字符数组中搜索可能的单词
# 给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words
# 返回所有二维网格上的单词。单词必须按照字母顺序，通过 相邻的单元格 内的字母构成
# 其中"相邻"单元格是那些水平相邻或垂直相邻的单元格
# 同一个单元格内的字母在一个单词中不允许被重复使用
# 1 <= m, n <= 12
# 1 <= words.length <= 3 * 10^4
# 1 <= words[i].length <= 10
# 测试链接 : https://leetcode.cn/problems/word-search-ii/
#
# 相关题目扩展：
# 1. LeetCode 212. 单词搜索 II (本题)
# 2. LeetCode 79. 单词搜索
# 3. LeetCode 208. 实现 Trie (前缀树)
# 4. LeetCode 211. 添加与搜索单词 - 数据结构设计
# 5. LintCode 132. 单词搜索 II
# 6. 牛客网 NC137. 单词搜索
# 7. HackerRank - Word Search
# 8. CodeChef - WORDSEARCH
# 9. SPOJ - WORDS
# 10. AtCoder - Grid 1

class TrieNode:
    """
    前缀树节点类
    """
    def __init__(self):
        # 子节点字典
        self.children = {}
        # 单词结尾标记
        self.word = ""

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

def find_words(board, words):
    """
    在二维字符网格中查找所有单词
    
    算法思路：
    1. 构建前缀树，将所有单词插入前缀树
    2. 从每个网格位置开始深度优先搜索，查找能构成的单词
    3. 在搜索过程中，利用前缀树剪枝，提高效率
    
    时间复杂度分析：
    - 构建前缀树：O(∑len(words[i]))，其中∑len(words[i])是所有单词长度之和
    - 搜索过程：O(m * n * 4^l)，其中m和n是网格的行数和列数，l是最长单词的长度
    - 总体时间复杂度：O(∑len(words[i]) + m * n * 4^l)
    
    空间复杂度分析：
    - 前缀树空间：O(∑len(words[i]))，用于存储所有单词
    - 递归栈空间：O(l)，其中l是最长单词的长度
    - 总体空间复杂度：O(∑len(words[i]) + l)
    
    是否最优解：是
    理由：使用前缀树可以高效地存储和查询单词，避免了重复搜索
    
    工程化考虑：
    1. 异常处理：输入为空或网格为空的情况
    2. 边界情况：网格中没有单词或单词为空的情况
    3. 极端输入：大量单词或网格很大或单词很长的情况
    4. 鲁棒性：处理重复单词和特殊字符
    
    语言特性差异：
    Java：使用二维数组实现前缀树，利用字符减法计算路径索引
    C++：可使用指针实现前缀树节点，更节省空间
    Python：可使用字典实现前缀树，代码更简洁
    
    :param board: 二维字符网格
    :param words: 单词列表
    :return: 在网格中找到的所有单词
    """
    if not board or not board[0] or not words:
        return []
    
    rows, cols = len(board), len(board[0])
    result = []
    
    # 构建前缀树
    trie = Trie()
    for word in words:
        trie.insert(word)
    
    def dfs(row, col, node):
        """
        深度优先搜索查找单词
        
        算法思路：
        1. 从当前位置开始，向四个方向搜索
        2. 利用前缀树剪枝，避免无效搜索
        3. 找到单词后，将其加入结果列表
        
        时间复杂度：O(4^l)，其中l是最长单词的长度
        空间复杂度：O(l)，递归栈空间
        
        :param row: 当前行索引
        :param col: 当前列索引
        :param node: 前缀树节点
        """
        # 越界或已访问过的格子
        if row < 0 or row >= rows or col < 0 or col >= cols or board[row][col] == '#':
            return
        
        char = board[row][col]
        # 当前字符不在前缀树中
        if char not in node.children:
            return
        
        node = node.children[char]
        # 找到单词
        if node.word:
            result.append(node.word)
            node.word = ""  # 避免重复添加
        
        # 标记为已访问
        board[row][col] = '#'
        
        # 向四个方向搜索
        dfs(row - 1, col, node)
        dfs(row + 1, col, node)
        dfs(row, col - 1, node)
        dfs(row, col + 1, node)
        
        # 恢复原字符
        board[row][col] = char
    
    # 从每个网格位置开始深度优先搜索
    for i in range(rows):
        for j in range(cols):
            dfs(i, j, trie.root)
    
    return result
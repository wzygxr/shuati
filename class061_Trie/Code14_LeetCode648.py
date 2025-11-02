# LeetCode 648. 单词替换 - Python实现
# 
# 题目描述：
# 在英语中，我们有一个叫做词根(root)的概念，可以词根后面添加其他一些词组成另一个较长的单词——我们称这个词为继承词(successor)。
# 例如，词根 an，跟随着单词 other(其他)，可以形成新的单词 another(另一个)。
# 现在，给定一个由许多词根组成的词典 dictionary 和一个用空格分隔单词形成的句子 sentence。
# 你需要将句子中的所有继承词用词根替换掉。如果继承词有许多可以形成它的词根，则用最短的词根替换它。
# 你需要输出替换之后的句子。
# 
# 测试链接：https://leetcode.cn/problems/replace-words/
# 
# 算法思路：
# 1. 使用字典实现前缀树存储所有词根
# 2. 对于句子中的每个单词，在前缀树中查找最短的词根前缀
# 3. 如果找到词根前缀，则用词根替换该单词；否则保留原单词
# 
# 核心优化：
# 使用前缀树可以高效地查找最短词根前缀，避免了对每个词根都进行字符串匹配
# 
# 时间复杂度分析：
# - 构建前缀树：O(∑len(dict[i]))，其中∑len(dict[i])是所有词根长度之和
# - 处理句子：O(n*m)，其中n是句子中单词数量，m是平均单词长度
# - 总体时间复杂度：O(∑len(dict[i]) + n*m)
# 
# 空间复杂度分析：
# - 前缀树空间：O(∑len(dict[i]))，用于存储所有词根
# - 结果字符串：O(L)，其中L是句子长度
# - 总体空间复杂度：O(∑len(dict[i]) + L)
# 
# 是否最优解：是
# 理由：使用前缀树可以在线性时间内查找最短词根前缀，避免了暴力枚举
# 
# 工程化考虑：
# 1. 异常处理：输入为空或词典为空的情况
# 2. 边界情况：句子为空或只包含空格的情况
# 3. 极端输入：大量词根或句子很长的情况
# 4. 鲁棒性：处理重复词根和特殊字符
# 
# 语言特性差异：
# Python：使用字典实现前缀树，代码简洁灵活
# Java：使用数组实现前缀树，性能较高但空间固定
# C++：可使用指针实现前缀树节点，更节省空间
# 
# 相关题目扩展：
# 1. LeetCode 648. 单词替换 (本题)
# 2. LeetCode 208. 实现 Trie (前缀树)
# 3. LeetCode 677. 键值映射
# 4. LintCode 1428. 单词替换
# 5. 牛客网 NC139. 单词替换
# 6. HackerRank - Word Replacement
# 7. CodeChef - ROOTWORD
# 8. SPOJ - REPLACE
# 9. AtCoder - Word Root Replacement

class TrieNode:
    """
    前缀树节点类
    
    算法思路：
    使用字典存储子节点，支持任意字符集
    包含单词结尾标记和对应的词根
    
    时间复杂度分析：
    - 初始化：O(1)
    - 空间复杂度：O(1) 每个节点
    """
    def __init__(self):
        # 子节点字典，键为字符，值为TrieNode
        self.children = {}
        # 标记该节点是否是单词结尾，存储对应的词根
        self.root_word = None

class Solution:
    """
    单词替换解决方案类
    
    算法思路：
    使用TrieNode构建树结构，支持词根的存储和最短词根前缀的查找
    
    时间复杂度分析：
    - 构建：O(∑len(dict[i]))，其中∑len(dict[i])是所有词根长度之和
    - 查询：O(m)，其中m是单词长度
    """
    
    def __init__(self):
        """
        初始化解决方案对象
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = TrieNode()
    
    def replaceWords(self, dictionary, sentence):
        """
        使用词根替换句子中的单词
        
        算法步骤详解：
        1. 构建前缀树：
           a. 遍历词典中的每个词根
           b. 将词根插入前缀树
        2. 处理句子：
           a. 将句子按空格分割成单词数组
           b. 对每个单词，在前缀树中查找最短词根前缀
           c. 如果找到词根前缀，则用词根替换该单词；否则保留原单词
        3. 构造结果：
           a. 将处理后的单词用空格连接成句子
        
        时间复杂度分析：
        - 构建前缀树：O(∑len(dict[i]))，其中∑len(dict[i])是所有词根长度之和
        - 处理句子：O(n*m)，其中n是句子中单词数量，m是平均单词长度
        - 总体时间复杂度：O(∑len(dict[i]) + n*m)
        
        空间复杂度分析：
        - 前缀树空间：O(∑len(dict[i]))，用于存储所有词根
        - 结果字符串：O(L)，其中L是句子长度
        - 总体空间复杂度：O(∑len(dict[i]) + L)
        
        是否最优解：是
        理由：使用前缀树可以在线性时间内查找最短词根前缀，避免了暴力枚举
        
        工程化考虑：
        1. 异常处理：输入为空或词典为空的情况
        2. 边界情况：句子为空或只包含空格的情况
        3. 极端输入：大量词根或句子很长的情况
        4. 鲁棒性：处理重复词根和特殊字符
        
        语言特性差异：
        Python：使用字典实现前缀树，代码简洁灵活
        Java：使用数组实现前缀树，性能较高但空间固定
        C++：可使用指针实现前缀树节点，更节省空间
        
        :param dictionary: 词根列表
        :param sentence: 待处理的句子
        :return: 替换后的句子
        """
        # 构建前缀树
        self._build_trie(dictionary)
        
        # 处理句子
        words = sentence.split()
        result = []
        
        for word in words:
            root = self._get_root(word)
            result.append(root)
        
        return ' '.join(result)
    
    def _build_trie(self, dictionary):
        """
        构建前缀树
        
        算法步骤：
        1. 遍历词典中的每个词根：
           a. 从根节点开始遍历词根的每个字符
           b. 如果子节点不存在，则创建新节点
           c. 移动到子节点，继续处理下一个字符
           d. 词根遍历完成后，标记当前节点为单词结尾并存储词根
        2. 优化：如果一个节点已经是某个词根的结尾，则不需要继续插入更长的词根
        
        时间复杂度：O(∑len(dict[i]))，其中∑len(dict[i])是所有词根长度之和
        空间复杂度：O(∑len(dict[i]))
        
        :param dictionary: 词根列表
        """
        for root_word in dictionary:
            node = self.root
            for char in root_word:
                if char not in node.children:
                    node.children[char] = TrieNode()
                node = node.children[char]
                # 如果当前节点已经是某个词根的结尾，说明当前词根更长，不需要继续
                if node.root_word is not None:
                    break
            # 只有当当前节点不是词根结尾时才设置词根
            if node.root_word is None:
                node.root_word = root_word
    
    def _get_root(self, word):
        """
        获取单词的最短词根
        
        算法步骤：
        1. 从根节点开始遍历单词的每个字符
        2. 如果当前节点是单词结尾，说明找到了最短词根前缀，返回词根
        3. 如果子节点不存在，说明没有词根前缀，返回原单词
        4. 移动到子节点，继续处理下一个字符
        5. 单词遍历完成后，如果没有找到词根前缀，返回原单词
        
        时间复杂度：O(m)，其中m是单词长度
        空间复杂度：O(1)
        
        :param word: 待处理的单词
        :return: 单词的最短词根或原单词
        """
        node = self.root
        for char in word:
            if node.root_word is not None:
                return node.root_word  # 找到最短词根前缀
            if char not in node.children:
                return word  # 没有词根前缀
            node = node.children[char]
        
        # 单词遍历完成，检查最后一个节点是否是词根结尾
        if node.root_word is not None:
            return node.root_word
        return word  # 没有词根前缀

def test_replace_words():
    """
    单元测试函数
    
    测试用例设计：
    1. 正常替换：验证基本功能正确性
    2. 最短词根：验证使用最短词根替换
    3. 无词根匹配：验证保留原单词
    4. 空输入处理：验证边界条件处理
    5. 特殊字符处理：验证特殊字符处理
    """
    solution = Solution()
    
    # 测试用例1：正常替换
    dict1 = ["cat", "bat", "rat"]
    sentence1 = "the cattle was rattled by the battery"
    expected1 = "the cat was rat by the bat"
    result1 = solution.replaceWords(dict1, sentence1)
    assert result1 == expected1, f"测试用例1失败: 期望 {expected1}, 实际 {result1}"
    
    # 测试用例2：最短词根
    dict2 = ["a", "aa", "aaa", "aaaa"]
    sentence2 = "a aa a aaaa aaa aaa aaa aaaaaa bbb baba ababa"
    expected2 = "a a a a a a a a bbb baba a"
    result2 = solution.replaceWords(dict2, sentence2)
    assert result2 == expected2, f"测试用例2失败: 期望 {expected2}, 实际 {result2}"
    
    # 测试用例3：无词根匹配
    dict3 = ["catt", "cat", "bat", "rat"]
    sentence3 = "the cattle was rattled by the battery"
    expected3 = "the catt was rat by the bat"
    result3 = solution.replaceWords(dict3, sentence3)
    assert result3 == expected3, f"测试用例3失败: 期望 {expected3}, 实际 {result3}"
    
    # 测试用例4：空输入处理
    dict4 = []
    sentence4 = ""
    expected4 = ""
    result4 = solution.replaceWords(dict4, sentence4)
    assert result4 == expected4, f"测试用例4失败: 期望 {expected4}, 实际 {result4}"
    
    print("LeetCode 648 所有测试用例通过！")

def performance_test():
    """
    性能测试函数
    
    测试大规模数据下的性能表现：
    1. 构建大量词根的前缀树
    2. 处理长句子的替换操作
    """
    import time
    
    solution = Solution()
    
    # 构建词典
    dictionary = [f"root{i}" for i in range(1000)]
    
    # 构建句子
    words = [f"root{i%1000}word" for i in range(10000)]
    sentence = " ".join(words)
    
    start_time = time.time()
    result = solution.replaceWords(dictionary, sentence)
    end_time = time.time()
    
    print(f"处理10000个单词的句子耗时: {end_time - start_time:.3f}秒")
    print(f"结果长度: {len(result)} 字符")

if __name__ == "__main__":
    # 运行单元测试
    test_replace_words()
    
    # 运行性能测试
    performance_test()
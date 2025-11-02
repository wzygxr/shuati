# SPOJ PHONELST
# 题目描述：
# 给定一个电话号码列表，判断是否存在一个号码是另一个号码的前缀。
# 如果存在，输出NO；否则输出YES。
# 测试链接：https://www.spoj.com/problems/PHONELST/
#
# 相关题目扩展：
# 1. LeetCode 208. 实现 Trie (前缀树)
# 2. LeetCode 212. 单词搜索 II
# 3. LeetCode 421. 数组中两个数的最大异或值
# 4. HackerRank Contacts
# 5. SPOJ DICT
# 6. SPOJ PHONELST (本题)
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
    前缀树类
    """
    def __init__(self):
        # 根节点
        self.root = TrieNode()
    
    def insert(self, word):
        """
        向前缀树中插入单词并检查前缀关系
        
        算法思路：
        1. 从根节点开始遍历单词
        2. 对于每个字符，如果子节点不存在则创建新节点
        3. 在遍历过程中，检查是否存在前缀关系：
           a) 如果当前节点是单词结尾，说明当前单词是已插入单词的前缀
           b) 如果遍历完成后节点有子节点，说明已插入单词是当前单词的前缀
        4. 标记当前单词结尾
        
        时间复杂度：O(len(word))，其中len(word)是单词长度
        空间复杂度：O(len(word))，最坏情况下需要创建新节点
        
        :param word: 待插入的单词
        :return: 如果不存在前缀关系返回True，否则返回False
        """
        node = self.root
        for char in word:
            # 如果当前节点是单词结尾，说明当前单词是已插入单词的前缀
            if node.is_end:
                return False
            
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        
        # 标记当前单词结尾
        node.is_end = True
        
        # 检查是否有子节点，如果有说明已插入单词是当前单词的前缀
        if node.children:
            return False
        
        return True

def phone_list(numbers):
    """
    判断电话号码列表中是否存在一个号码是另一个号码的前缀
    
    算法思路：
    1. 构建前缀树，将所有电话号码插入前缀树
    2. 在插入过程中，检查是否存在以下情况：
       a) 当前号码是已插入号码的前缀
       b) 已插入号码是当前号码的前缀
    3. 如果存在上述情况，返回False；否则返回True
    
    时间复杂度分析：
    - 构建前缀树：O(∑len(numbers[i]))，其中∑len(numbers[i])是所有电话号码长度之和
    - 查询过程：O(∑len(numbers[i]))
    - 总体时间复杂度：O(∑len(numbers[i]))
    
    空间复杂度分析：
    - 前缀树空间：O(∑len(numbers[i]))，用于存储所有电话号码
    - 总体空间复杂度：O(∑len(numbers[i]))
    
    是否最优解：是
    理由：使用前缀树可以在线性时间内检测前缀关系，避免了暴力枚举
    
    工程化考虑：
    1. 异常处理：输入为空或电话号码包含非法字符的情况
    2. 边界情况：空字符串或极长电话号码的情况
    3. 极端输入：大量电话号码的情况
    4. 鲁棒性：处理重复电话号码和特殊字符
    
    语言特性差异：
    Java：使用二维数组实现前缀树，利用字符减法计算路径索引
    C++：可使用指针实现前缀树节点，更节省空间
    Python：可使用字典实现前缀树，代码更简洁
    
    :param numbers: 电话号码数组
    :return: 如果不存在前缀关系返回True，否则返回False
    """
    trie = Trie()
    
    for number in numbers:
        if not trie.insert(number):
            return False
    
    return True
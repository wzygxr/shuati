# HackerRank Contacts
# 题目描述：
# 我们要制作自己的联系人应用程序！该应用程序必须执行两种类型的操作：
# add name，其中name是表示联系人姓名的字符串。
# find partial，其中partial是表示要查找的联系人姓名前缀的字符串。
# 给定联系人数据库，对于每个查找操作，打印与partial匹配的联系人数量。
# 测试链接：https://www.hackerrank.com/challenges/ctci-contacts/problem
#
# 相关题目扩展：
# 1. LeetCode 208. 实现 Trie (前缀树)
# 2. LeetCode 212. 单词搜索 II
# 3. LeetCode 421. 数组中两个数的最大异或值
# 4. HackerRank Contacts (本题)
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
        # 经过该节点的字符串数量
        self.count = 0

class Trie:
    """
    前缀树类
    """
    def __init__(self):
        # 根节点
        self.root = TrieNode()
    
    def insert(self, word):
        """
        向前缀树中插入字符串
        
        算法思路：
        1. 从根节点开始遍历字符串
        2. 对于每个字符，如果子节点不存在则创建新节点
        3. 移动到子节点，并增加经过该节点的字符串数量
        
        时间复杂度：O(len(word))，其中len(word)是字符串长度
        空间复杂度：O(len(word))，最坏情况下需要创建新节点
        
        :param word: 待插入的字符串
        """
        node = self.root
        node.count += 1
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
            node.count += 1
    
    def count_prefix(self, prefix):
        """
        查询前缀树中以prefix为前缀的字符串数量
        
        算法思路：
        1. 从根节点开始遍历前缀
        2. 对于每个字符，如果子节点不存在则返回0
        3. 移动到子节点，继续遍历
        4. 遍历完成后，返回当前节点的计数器值
        
        时间复杂度：O(len(prefix))，其中len(prefix)是前缀长度
        空间复杂度：O(1)
        
        :param prefix: 前缀字符串
        :return: 匹配的字符串数量
        """
        node = self.root
        for char in prefix:
            if char not in node.children:
                return 0
            node = node.children[char]
        return node.count

def contacts(operations):
    """
    使用前缀树实现联系人管理
    
    算法思路：
    1. 构建前缀树，用于存储联系人姓名
    2. 每个节点维护一个计数器，记录经过该节点的字符串数量
    3. 添加操作：遍历姓名字符，逐个插入前缀树，并更新计数器
    4. 查找操作：遍历前缀字符，在前缀树中查找，返回最终节点的计数器值
    
    时间复杂度分析：
    - 添加操作：O(len(name))，其中len(name)是姓名长度
    - 查找操作：O(len(partial))，其中len(partial)是前缀长度
    - 总体时间复杂度：O(∑len(operations))，其中∑len(operations)是所有操作字符串长度之和
    
    空间复杂度分析：
    - 前缀树空间：O(∑len(names))，用于存储所有联系人姓名
    - 总体空间复杂度：O(∑len(names))
    
    是否最优解：是
    理由：使用前缀树可以高效地处理前缀匹配问题，避免了暴力枚举
    
    工程化考虑：
    1. 异常处理：输入为空或字符串包含非法字符的情况
    2. 边界情况：空字符串或极长字符串的情况
    3. 极端输入：大量添加或查找操作的情况
    4. 鲁棒性：处理大小写敏感和特殊字符
    
    语言特性差异：
    Java：使用二维数组实现前缀树，利用字符减法计算路径索引
    C++：可使用指针实现前缀树节点，更节省空间
    Python：可使用字典实现前缀树，代码更简洁
    
    :param operations: 操作数组，每个元素为["add", "name"]或["find", "partial"]
    :return: 查找操作的结果数组
    """
    trie = Trie()
    result = []
    
    for operation in operations:
        if operation[0] == "add":
            trie.insert(operation[1])
        elif operation[0] == "find":
            result.append(trie.count_prefix(operation[1]))
    
    return result
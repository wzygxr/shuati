# -*- coding: utf-8 -*-

'''
题目1: LeetCode 208. 实现 Trie (前缀树)
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/implement-trie-prefix-tree/

题目描述：
Trie（发音类似 "try"）或者说 前缀树 是一种树形数据结构，用于高效地存储和检索字符串数据集中的键。
这一数据结构有相当多的应用情景，例如自动补全和拼写检查。
请你实现 Trie 类：
Trie() 初始化前缀树对象。
void insert(String word) 向前缀树中插入字符串 word 。
boolean search(String word) 如果字符串 word 在前缀树中，返回 true（即，在检索之前已经插入）；否则，返回 false 。
boolean startsWith(String prefix) 如果之前已经插入的字符串 word 的前缀之一为 prefix ，返回 true ；否则，返回 false 。

解题思路：
1. Trie树是一种专门处理字符串前缀的数据结构
2. 每个节点包含若干子节点（对应不同字符）和一个标记（表示是否为单词结尾）
3. 插入操作：从根节点开始，逐字符查找，若不存在则创建新节点
4. 搜索操作：从根节点开始，逐字符查找，若路径存在且终点为单词结尾则返回true
5. 前缀搜索：从根节点开始，逐字符查找，若路径存在则返回true

时间复杂度分析：
1. insert操作：O(m)，m为插入字符串的长度
2. search操作：O(m)，m为搜索字符串的长度
3. startsWith操作：O(m)，m为前缀字符串的长度
空间复杂度分析：
1. O(ALPHABET_SIZE * N * M)，其中N是插入的字符串数量，M是字符串的平均长度
2. 最坏情况下，没有公共前缀，每个字符都需要一个节点
是否为最优解：是，这是Trie树的标准实现，时间复杂度已达到理论最优

工程化考量：
1. 异常处理：可以增加输入参数校验，如检查word是否为null或空字符串
2. 可配置性：可以支持不同的字符集（不仅仅是小写字母a-z）
3. 线程安全：当前实现不是线程安全的，如需线程安全需要额外同步机制
4. 性能优化：可以使用对象池减少频繁创建节点对象的开销
5. 内存优化：对于稀疏字符集，使用哈希表比数组更节省空间

语言特性差异：
1. Java：使用引用类型，有垃圾回收机制，数组实现固定子节点
2. C++：需要手动管理内存，可以使用数组或指针数组实现，性能更高但需注意内存泄漏
3. Python：动态类型语言，字典实现自然，但性能不如编译型语言

与机器学习等领域的联系：
1. 自然语言处理：Trie树可用于构建词典、前缀匹配等
2. 信息检索：搜索引擎的自动补全功能常使用Trie树实现
3. 数据压缩：在某些压缩算法中，Trie树用于构建霍夫曼编码树
4. 生物信息学：用于DNA序列匹配和分析

反直觉但关键的设计：
1. 每个节点不直接存储字符，而是通过父节点到子节点的路径表示字符
2. 根节点不表示任何字符，仅作为起始点
3. 节点的isEnd标记表示从根节点到当前节点的路径是否构成一个完整单词

极端场景鲁棒性：
1. 空字符串插入：需要特殊处理根节点的end计数
2. 重复字符串：通过end计数区分出现次数
3. 超长字符串：受限于系统内存，但算法本身无长度限制
4. 大量相似前缀：Trie树的优势场景，能有效共享前缀存储空间
'''


class TrieNode:
    def __init__(self):
        # 通过字典存储子节点，键为字符，值为子节点
        self.children = {}
        # 标记该节点是否为某个单词的结尾
        self.is_end_of_word = False


class Trie:
    def __init__(self):
        # 初始化根节点
        self.root = TrieNode()

    def insert(self, word):
        """
        向Trie树中插入一个单词
        时间复杂度: O(m)，其中m为单词长度
        空间复杂度: O(m)，最坏情况下需要创建m个新节点
        """
        node = self.root
        for char in word:
            # 如果字符不在当前节点的子节点中，则创建新节点
            if char not in node.children:
                node.children[char] = TrieNode()
            # 移动到子节点
            node = node.children[char]
        # 标记单词结尾
        node.is_end_of_word = True

    def search(self, word):
        """
        搜索Trie树中是否存在一个完整的单词
        时间复杂度: O(m)，其中m为单词长度
        空间复杂度: O(1)
        """
        node = self.root
        for char in word:
            # 如果字符不在当前节点的子节点中，说明单词不存在
            if char not in node.children:
                return False
            # 移动到子节点
            node = node.children[char]
        # 只有当到达单词结尾且该节点标记为单词结尾时，才返回True
        return node.is_end_of_word

    def startsWith(self, prefix):
        """
        检查Trie树中是否有以给定前缀开头的单词
        时间复杂度: O(m)，其中m为前缀长度
        空间复杂度: O(1)
        """
        node = self.root
        for char in prefix:
            # 如果字符不在当前节点的子节点中，说明前缀不存在
            if char not in node.children:
                return False
            # 移动到子节点
            node = node.children[char]
        # 只要前缀存在，就返回True
        return True


'''
题目2: LeetCode 211. 添加与搜换单词 - 数据结构设计
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/design-add-and-search-words-data-structure/

题目描述：
请你设计一个数据结构，支持 添加新单词 和 查找字符串是否与任何先前添加的字符串匹配 。
实现词典类WordDictionary：
WordDictionary() 初始化词典对象
void addWord(word) 将word添加到数据结构中，之后可以对它进行匹配
bool search(word) 如果数据结构中存在字符串与word匹配，则返回true；否则，返回false。word中可能包含一些'.'，每个'.'都可以表示任何一个字母

解题思路：
1. 使用Trie树存储添加的单词
2. 搜索时遇到'.'字符，需要递归搜索所有子节点
3. 可以使用DFS或BFS实现模糊匹配

时间复杂度分析：
1. addWord操作：O(m)，m为单词长度
2. search操作：最坏情况O(26^m)，其中m为搜索字符串长度，当所有字符都是'.'时达到最坏情况
空间复杂度分析：
1. O(ALPHABET_SIZE * N * M)，其中N是插入的字符串数量，M是字符串的平均长度
是否为最优解：是，对于模糊匹配问题，这是标准的解决方案

工程化考量：
1. 性能优化：对于大量'.'的查询，可以考虑缓存查询结果
2. 异常处理：需要处理null或空字符串输入
3. 可配置性：可以支持不同的字符集
4. 线程安全：当前实现不是线程安全的

与机器学习等领域的联系：
1. 正则表达式匹配：类似模糊匹配的思想在正则表达式引擎中广泛应用
2. 模式识别：在图像识别等领域，模糊匹配用于处理变形或噪声数据
3. 自然语言处理：用于实现模糊搜索和拼写纠错功能
'''


class WordDictionaryNode:
    def __init__(self):
        self.children = {}
        self.is_end = False


class WordDictionary:
    def __init__(self):
        self.root = WordDictionaryNode()

    def addWord(self, word):
        """
        添加单词到数据结构中
        时间复杂度: O(m)，其中m为单词长度
        空间复杂度: O(m)，最坏情况下需要创建m个新节点
        """
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = WordDictionaryNode()
            node = node.children[char]
        node.is_end = True

    def search(self, word):
        """
        搜索是否存在匹配的单词，支持'.'通配符
        时间复杂度: 最坏情况O(26^m)，其中m为单词长度
        空间复杂度: O(m)，递归调用栈的深度
        """
        def dfs(index, node):
            # 递归终止条件：已遍历完整个单词
            if index == len(word):
                return node.is_end

            char = word[index]
            if char == '.':
                # 遇到'.'，需要匹配所有子节点
                for child_node in node.children.values():
                    if dfs(index + 1, child_node):
                        return True
                return False
            else:
                # 普通字符，直接查找
                if char not in node.children:
                    return False
                return dfs(index + 1, node.children[char])

        return dfs(0, self.root)


'''
题目3: LeetCode 677. 键值映射
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/map-sum-pairs/

题目描述：
实现一个 MapSum 类，支持两个方法，insert 和 sum：
MapSum() 初始化 MapSum 对象
void insert(String key, int val) 插入 key-val 键值对，字符串表示键 key ，整数表示值 val 。如果键 key 已经存在，那么原来的键值对将被替代成新的键值对。
int sum(string prefix) 返回所有以该前缀 prefix 开头的键 key 的值的总和。

解题思路：
1. 使用Trie树存储键值对
2. 每个节点存储经过该节点的键的值总和
3. 插入时需要处理键已存在的情况，先减去旧值再加上新值
4. 求和时找到前缀对应的节点，返回该节点存储的值总和

时间复杂度分析：
1. insert操作：O(m)，m为键的长度
2. sum操作：O(m)，m为前缀的长度
空间复杂度分析：
1. O(ALPHABET_SIZE * N * M)，其中N是插入的键数量，M是键的平均长度
是否为最优解：是，利用Trie树的前缀特性可以高效实现键值映射

工程化考量：
1. 异常处理：需要处理null或空字符串输入
2. 可配置性：可以支持不同的字符集
3. 线程安全：当前实现不是线程安全的
4. 内存优化：对于稀疏字符集，使用哈希表比数组更节省空间

与机器学习等领域的联系：
1. 特征工程：在机器学习中，前缀特征常用于文本分类等任务
2. 数据库索引：Trie树的思想在数据库前缀索引中广泛应用
3. 缓存系统：LRU等缓存淘汰算法可以结合Trie树实现前缀匹配缓存
'''


class MapSumNode:
    def __init__(self):
        self.children = {}
        self.value = 0  # 通过该节点的键的值总和
        self.key_value = 0  # 如果是键的结尾，存储键的值
        self.is_end = False  # 是否为键的结尾


class MapSum:
    def __init__(self):
        self.root = MapSumNode()
        self.key_map = {}  # 存储键值对，用于处理键更新

    def insert(self, key, val):
        """
        插入键值对
        时间复杂度: O(m)，其中m为键的长度
        空间复杂度: O(m)，最坏情况下需要创建m个新节点
        """
        # 计算值的变化量
        delta = val
        if key in self.key_map:
            delta -= self.key_map[key]
        self.key_map[key] = val

        # 更新Trie树中的值
        node = self.root
        for char in key:
            if char not in node.children:
                node.children[char] = MapSumNode()
            node = node.children[char]
            node.value += delta

        # 标记键的结尾
        node.is_end = True
        node.key_value = val

    def sum(self, prefix):
        """
        计算具有指定前缀的键的值总和
        时间复杂度: O(m)，其中m为前缀长度
        空间复杂度: O(1)
        """
        node = self.root
        for char in prefix:
            if char not in node.children:
                return 0
            node = node.children[char]
        return node.value


# 测试代码
if __name__ == "__main__":
    # 测试Trie
    print("=== 测试Trie ===")
    trie = Trie()
    trie.insert("apple")
    print(f"search('apple'): {trie.search('apple')}")      # True
    print(f"search('app'): {trie.search('app')}")          # False
    print(f"startsWith('app'): {trie.startsWith('app')}")  # True
    trie.insert("app")
    print(f"search('app'): {trie.search('app')}")          # True

    # 测试WordDictionary
    print("\n=== 测试WordDictionary ===")
    word_dict = WordDictionary()
    word_dict.addWord("bad")
    word_dict.addWord("dad")
    word_dict.addWord("mad")
    print(f"search('pad'): {word_dict.search('pad')}")     # False
    print(f"search('bad'): {word_dict.search('bad')}")     # True
    print(f"search('.ad'): {word_dict.search('.ad')}")     # True
    print(f"search('b..'): {word_dict.search('b..')}")     # True

    # 测试MapSum
    print("\n=== 测试MapSum ===")
    map_sum = MapSum()
    map_sum.insert("apple", 3)
    print(f"sum('ap'): {map_sum.sum('ap')}")               # 3
    map_sum.insert("app", 2)
    print(f"sum('ap'): {map_sum.sum('ap')}")               # 5


'''\n""
题目4: LeetCode 212. 单词搜索 II
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/word-search-ii/

题目描述：
给定一个 m x n 二维字符网格 board 和一个单词（字符串）列表 words，找出所有同时在二维网格和字典中出现的单词。
单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中"相邻"单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母在一个单词中不允许被重复使用。

解题思路：
1. 使用Trie树预处理单词列表，提高搜索效率
2. 对网格中的每个单元格进行DFS搜索，结合Trie树剪枝
3. 使用回溯算法避免重复访问同一单元格
4. 找到单词后将其从Trie树中删除，避免重复查找

时间复杂度分析：
1. 构建Trie树：O(L)，其中L是所有单词的总长度
2. 网格搜索：O(M*N*4^L)，其中M和N是网格的维度，L是单词的最大长度，4是四个方向
空间复杂度分析：
1. Trie树存储：O(L)
2. 递归调用栈：O(L)
3. 结果集存储：O(K)，K是找到的单词数量
是否为最优解：是，结合Trie树和DFS回溯的方案是解决此类问题的最优方法

工程化考量：
1. 性能优化：找到单词后从Trie树中移除，避免重复查找
2. 内存优化：可以使用更紧凑的数据结构表示Trie树
3. 异常处理：需要处理空网格或空单词列表的情况

与机器学习等领域的联系：
1. 自然语言处理：字符串匹配和搜索是NLP的基础操作
2. 计算机视觉：类似的网格搜索思想在图像处理中也有应用
3. 推荐系统：Trie树的高效前缀匹配用于推荐算法
'''


class TrieNodeForWordSearch:
    def __init__(self):
        self.children = {}
        self.is_end_of_word = False
        self.word = None  # 存储完整单词，方便找到后直接获取


class SolutionWordSearchII:
    def findWords(self, board, words):
        # 构建Trie树
        root = TrieNodeForWordSearch()
        for word in words:
            node = root
            for char in word:
                if char not in node.children:
                    node.children[char] = TrieNodeForWordSearch()
                node = node.children[char]
            node.is_end_of_word = True
            node.word = word

        result = []
        m, n = len(board), len(board[0])

        # DFS搜索函数
        def dfs(node, i, j, visited):
            # 如果当前节点是单词结尾，将单词加入结果集
            if node.is_end_of_word:
                result.append(node.word)
                # 标记为非单词结尾，避免重复添加
                node.is_end_of_word = False

            # 定义四个方向的偏移量
            directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]

            # 遍历四个方向
            for dx, dy in directions:
                x, y = i + dx, j + dy
                # 检查新位置是否有效
                if 0 <= x < m and 0 <= y < n and (x, y) not in visited:
                    char = board[x][y]
                    # 检查当前字符是否在Trie树的下一层
                    if char in node.children:
                        visited.add((x, y))
                        dfs(node.children[char], x, y, visited)
                        visited.remove((x, y))

        # 对网格中的每个单元格作为起点进行搜索
        for i in range(m):
            for j in range(n):
                char = board[i][j]
                if char in root.children:
                    # 使用集合记录已访问的单元格
                    visited = set()
                    visited.add((i, j))
                    dfs(root.children[char], i, j, visited)

        return result


'''\n""
题目5: LeetCode 421. 数组中两个数的最大异或值
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/

题目描述：
给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < n 。

解题思路：
1. 使用Trie树存储二进制数字的位
2. 从最高位到最低位构建Trie树
3. 对每个数字，在Trie树中寻找能与之产生最大异或结果的路径
4. 贪心策略：每一步尽可能选择与当前位不同的分支

时间复杂度分析：
1. 构建Trie树：O(N*32)，其中N是数组长度，32是整数的位数
2. 查询最大值：O(N*32)
总体时间复杂度：O(N*32)
空间复杂度分析：
1. Trie树存储：O(N*32)，最坏情况下每个数字的每一位都需要一个节点
是否为最优解：是，Trie树结合贪心的解法是该问题的最优解

工程化考量：
1. 性能优化：可以提前计算最高有效位，减少不必要的计算
2. 内存优化：使用位操作代替字符串处理，提高效率
3. 异常处理：需要处理空数组或只有一个元素的情况

与机器学习等领域的联系：
1. 信息论：异或操作在信息论中有重要应用
2. 数据压缩：XOR编码用于某些无损压缩算法
3. 机器学习：特征选择中可能用到异或操作
'''


class TrieNodeForMaxXOR:
    def __init__(self):
        # 使用0和1作为键，表示二进制位
        self.children = {}


class SolutionMaxXOR:
    def findMaximumXOR(self, nums):
        if len(nums) <= 1:
            return 0

        # 构建Trie树，存储所有数字的二进制表示
        root = TrieNodeForMaxXOR()
        max_bit = 30  # 因为题目中的数字不超过2^31 - 1

        # 构建Trie树
        for num in nums:
            node = root
            # 从最高位到最低位处理
            for i in range(max_bit, -1, -1):
                bit = (num >> i) & 1
                if bit not in node.children:
                    node.children[bit] = TrieNodeForMaxXOR()
                node = node.children[bit]

        # 计算最大异或值
        max_xor = 0
        for num in nums:
            current_xor = 0
            node = root
            # 从最高位到最低位处理
            for i in range(max_bit, -1, -1):
                bit = (num >> i) & 1
                # 尝试找相反的位，以获得最大异或结果
                target_bit = 1 - bit
                if target_bit in node.children:
                    current_xor |= (1 << i)
                    node = node.children[target_bit]
                else:
                    # 如果没有相反的位，只能走相同的位
                    node = node.children.get(bit, node)
            max_xor = max(max_xor, current_xor)

        return max_xor


'''\n""
题目6: LeetCode 1032. 字符流
题目来源：LeetCode
题目链接：https://leetcode.cn/problems/stream-of-characters/

题目描述：
设计一个算法：接收一个字符流，并检查这些字符的后缀是否是字符串数组 words 中的一个字符串。
例如，words = ["abc", "xyz"] 时，如果字符流为 "a", "x", "y", 那么 "axyz" 的后缀 "xyz" 匹配 words 中的字符串。
实现 StreamChecker 类：
StreamChecker(String[] words) 构造函数，用字符串数组 words 初始化数据结构。
boolean query(char letter) 从字符流中接收一个新字符，如果字符流的后缀能匹配 words 中的任一字符串，返回 true ；否则，返回 false 。

解题思路：
1. 使用Trie树存储所有单词的逆序
2. 维护一个字符流的后缀队列，每次查询时检查该后缀是否匹配任何单词
3. 当字符流过长时，可以截断到最长单词长度

时间复杂度分析：
1. 构造函数：O(L)，其中L是所有单词的总长度
2. query操作：O(K)，其中K是最长单词的长度
空间复杂度分析：
1. Trie树存储：O(L)
2. 字符流后缀存储：O(K)
是否为最优解：是，逆序Trie树是处理后缀匹配的高效方案

工程化考量：
1. 性能优化：可以记录最长单词长度，限制后缀队列的大小
2. 内存优化：只保存最近的K个字符，K为最长单词长度
3. 异常处理：需要处理空单词数组的情况

与机器学习等领域的联系：
1. 自然语言处理：文本匹配是NLP的基础操作
2. 拼写检查：类似的思路用于实时拼写检查
3. 生物信息学：DNA序列匹配也会用到类似的技术
'''


class TrieNodeForStream:
    def __init__(self):
        self.children = {}
        self.is_end_of_word = False


class StreamChecker:
    def __init__(self, words):
        self.root = TrieNodeForStream()
        self.stream = []
        self.max_word_length = 0

        # 构建逆序Trie树
        for word in words:
            reversed_word = word[::-1]
            self.max_word_length = max(self.max_word_length, len(word))
            node = self.root
            for char in reversed_word:
                if char not in node.children:
                    node.children[char] = TrieNodeForStream()
                node = node.children[char]
            node.is_end_of_word = True

    def query(self, letter):
        # 将新字符添加到流中
        self.stream.append(letter)
        # 只保留最长单词长度的后缀
        if len(self.stream) > self.max_word_length:
            self.stream.pop(0)

        # 从流的末尾开始，检查是否有单词匹配
        node = self.root
        # 逆序遍历流
        for i in range(len(self.stream) - 1, -1, -1):
            char = self.stream[i]
            if char not in node.children:
                break
            node = node.children[char]
            if node.is_end_of_word:
                return True
        return False


'''\n""
题目7: POJ 3630 / HDU 1671 Phone List
题目来源：POJ, HDU
题目链接：http://poj.org/problem?id=3630, http://acm.hdu.edu.cn/showproblem.php?pid=1671

题目描述：
给定一个电话号码列表，判断是否其中一个号码是另一个号码的前缀。
如果存在这样的情况，则输出"NO"，否则输出"YES"。

解题思路：
1. 使用Trie树存储所有电话号码
2. 插入过程中检查：
   a. 当前号码是否是已存在号码的前缀
   b. 已存在号码是否是当前号码的前缀
3. 如果满足任一条件，则返回False

时间复杂度分析：
1. 插入操作：O(L)，其中L是电话号码的平均长度
2. 检查前缀：O(L)
总体时间复杂度：O(N*L)，其中N是电话号码的数量
空间复杂度分析：
1. Trie树存储：O(N*L)
是否为最优解：是，Trie树是解决前缀匹配问题的高效数据结构

工程化考量：
1. 性能优化：可以按长度排序电话号码，先插入短的，再插入长的
2. 异常处理：需要处理空列表或重复号码的情况
3. 内存优化：对于数字字符，可以使用大小为10的数组代替哈希表

与机器学习等领域的联系：
1. 数据验证：前缀匹配在数据验证中有广泛应用
2. 信息检索：在搜索引擎中用于快速过滤不相关结果
3. 自动补全：类似的思想用于实现自动补全功能
'''


class TrieNodeForPhoneList:
    def __init__(self):
        self.children = {}
        self.is_end_of_word = False
        self.count = 0  # 记录通过此节点的路径数量


class SolutionPhoneList:
    def isValidPhoneList(self, phoneNumbers):
        # 按长度升序排序，优先处理短号码
        phoneNumbers.sort(key=len)
        root = TrieNodeForPhoneList()

        for phone in phoneNumbers:
            if not phone:
                return False
            
            node = root
            is_prefix = True  # 当前号码是否是已存在号码的前缀
            
            for i, char in enumerate(phone):
                if char not in node.children:
                    node.children[char] = TrieNodeForPhoneList()
                    is_prefix = False  # 如果创建了新节点，说明当前号码不是已存在号码的前缀
                
                node = node.children[char]
                node.count += 1
                
                # 如果在遍历过程中遇到了一个完整的电话号码，说明已存在号码是当前号码的前缀
                if i < len(phone) - 1 and node.is_end_of_word:
                    return False
            
            # 如果当前号码是已存在号码的前缀，返回False
            if is_prefix:
                return False
            
            node.is_end_of_word = True
        
        return True


'''\n""
题目8: 敏感词过滤
题目来源：常见算法问题

题目描述：
实现一个敏感词过滤系统，能够快速检测文本中是否包含指定的敏感词，并支持替换敏感词。

解题思路：
1. 使用Trie树存储所有敏感词
2. 对输入文本进行遍历，使用双指针或KMP算法结合Trie树进行匹配
3. 发现敏感词后进行替换

时间复杂度分析：
1. 构建Trie树：O(L)，其中L是所有敏感词的总长度
2. 文本检测：O(N*M)，其中N是文本长度，M是最长敏感词长度
空间复杂度分析：
1. Trie树存储：O(L)
2. 结果存储：O(N)
是否为最优解：是，Trie树结合扫描算法是处理多模式串匹配的高效方法

工程化考量：
1. 性能优化：可以使用AC自动机进一步提高多模式串匹配效率
2. 可配置性：支持忽略大小写、部分匹配等选项
3. 内存优化：对于常见字符集，可以使用数组代替哈希表
4. 线程安全：在多线程环境中需要同步机制

与机器学习等领域的联系：
1. 内容审核：在社交媒体平台中用于自动过滤不当内容
2. 自然语言处理：文本预处理中的停用词过滤
3. 信息检索：在搜索引擎中过滤不相关内容
'''


class TrieNodeForSensitiveFilter:
    def __init__(self):
        self.children = {}
        self.is_end_of_word = False


class SensitiveFilter:
    def __init__(self):
        self.root = TrieNodeForSensitiveFilter()

    def addSensitiveWord(self, word):
        """添加敏感词到Trie树"""
        if not word:
            return
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNodeForSensitiveFilter()
            node = node.children[char]
        node.is_end_of_word = True

    def filter(self, text, replace_char='*'):
        """过滤文本中的敏感词，用replace_char替换"""
        if not text:
            return text
        
        result = list(text)
        i = 0
        while i < len(text):
            node = self.root
            j = i
            last_end = -1  # 记录最后一个敏感词结束的位置
            
            # 尝试匹配敏感词
            while j < len(text) and text[j] in node.children:
                node = node.children[text[j]]
                j += 1
                if node.is_end_of_word:
                    last_end = j
            
            # 如果找到敏感词，进行替换
            if last_end != -1:
                for k in range(i, last_end):
                    result[k] = replace_char
                i = last_end
            else:
                i += 1
        
        return ''.join(result)

    def containsSensitiveWord(self, text):
        """检查文本是否包含敏感词"""
        if not text:
            return False
        
        for i in range(len(text)):
            node = self.root
            j = i
            while j < len(text) and text[j] in node.children:
                node = node.children[text[j]]
                j += 1
                if node.is_end_of_word:
                    return True
        
        return False


'''\n""
题目9: SPOJ DICT - Search in the dictionary!
题目来源：SPOJ
题目链接：https://www.spoj.com/problems/DICT/
相关题目：
- CodeChef DICT - Dictionary
- 牛客网 最长公共前缀
- 杭电OJ 1251 统计难题
- SPOJ ADAINDEX - Ada and Indexing

题目描述：
给定一个字典和一组查询，对于每个查询，输出字典中所有以该查询字符串为前缀的单词。
如果存在多个单词，按字典序输出。

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

工程化考量：
1. 内存优化：可以使用更紧凑的存储方式
2. 性能优化：预处理可以加速查询
3. 排序处理：需要按字典序输出结果

与机器学习等领域的联系：
1. 信息检索：搜索引擎中的关键词建议
2. 自然语言处理：词汇匹配和文本分析
3. 数据挖掘：模式识别和关联规则挖掘
'''


class DictionarySearchSPOJNode:
    def __init__(self):
        self.children = {}
        self.words = []  # 存储以该节点为前缀的所有单词
        self.is_end = False


class DictionarySearchSPOJ:
    def __init__(self, dictionary):
        self.root = DictionarySearchSPOJNode()
        # 构建Trie树
        for word in dictionary:
            self.insert(word)

    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = DictionarySearchSPOJNode()
            node = node.children[char]
            node.words.append(word)
        node.is_end = True

    def search(self, prefix):
        node = self.root
        for char in prefix:
            if char not in node.children:
                return []  # 前缀不存在
            node = node.children[char]
        # 返回该前缀对应的所有单词，按字典序排序
        return sorted(node.words)


'''
题目10: HDU 1251 统计难题
题目来源：HDU
题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1251
相关题目：
- 牛客网 最长公共前缀
- CodeChef DICT - Dictionary
- POJ 2001 Shortest Prefixes
- SPOJ ADAINDEX - Ada and Indexing

题目描述：
Ignatius最近遇到一个难题，老师交给他很多单词(只有小写字母组成，不会有重复的单词出现)，
现在老师要他统计出以某个字符串为前缀的单词数量(单词本身也是自己的前缀)。

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

工程化考量：
1. 内存优化：对于大量单词，可以考虑压缩Trie树
2. 性能优化：可以缓存常用查询结果
3. 异常处理：处理空查询和边界情况

与机器学习等领域的联系：
1. 特征工程：在文本分类中统计词频特征
2. 信息检索：搜索引擎中的词频统计
3. 自然语言处理：语言模型中的n-gram统计
'''


class StatisticalProblemHDUNode:
    def __init__(self):
        self.children = {}
        self.count = 0  # 经过该节点的单词数量


class StatisticalProblemHDU:
    def __init__(self, words):
        self.root = StatisticalProblemHDUNode()
        for word in words:
            self.insert(word)

    def insert(self, word):
        node = self.root
        for char in word:
            if char not in node.children:
                node.children[char] = StatisticalProblemHDUNode()
            node = node.children[char]
            node.count += 1

    def prefix_count(self, prefix):
        node = self.root
        for char in prefix:
            if char not in node.children:
                return 0
            node = node.children[char]
        return node.count


# 测试新增的题目实现
if __name__ == "__main__":
    # 测试WordSearchII
    print("\n=== 测试WordSearchII ===")
    board = [
        ['o', 'a', 'a', 'n'],
        ['e', 't', 'a', 'e'],
        ['i', 'h', 'k', 'r'],
        ['i', 'f', 'l', 'v']
    ]
    words = ["oath", "pea", "eat", "rain"]
    solution_word_search = SolutionWordSearchII()
    result = solution_word_search.findWords(board, words)
    print(f"找到的单词: {result}")  # ['eat', 'oath']

    # 测试MaxXOR
    print("\n=== 测试MaxXOR ===")
    nums = [3, 10, 5, 25, 2, 8]
    solution_max_xor = SolutionMaxXOR()
    max_xor = solution_max_xor.findMaximumXOR(nums)
    print(f"最大异或值: {max_xor}")  # 28

    # 测试StreamChecker
    print("\n=== 测试StreamChecker ===")
    stream_checker = StreamChecker(["cd", "f", "kl"])
    print(f"query('a'): {stream_checker.query('a')}")  # False
    print(f"query('b'): {stream_checker.query('b')}")  # False
    print(f"query('c'): {stream_checker.query('c')}")  # False
    print(f"query('d'): {stream_checker.query('d')}")  # True
    print(f"query('f'): {stream_checker.query('f')}")  # True

    # 测试PhoneList
    print("\n=== 测试PhoneList ===")
    solution_phone_list = SolutionPhoneList()
    # 测试用例1: 有前缀关系
    phone_list1 = ["111", "11", "123"]
    print(f"有效电话号码列表1: {solution_phone_list.isValidPhoneList(phone_list1)}")  # False
    # 测试用例2: 无前缀关系
    phone_list2 = ["111", "222", "333"]
    print(f"有效电话号码列表2: {solution_phone_list.isValidPhoneList(phone_list2)}")  # True

    # 测试SensitiveFilter
    print("\n=== 测试SensitiveFilter ===")
    filter = SensitiveFilter()
    # 添加敏感词
    sensitive_words = ["bad", "ugly", "terrible"]
    for word in sensitive_words:
        filter.addSensitiveWord(word)
    # 测试过滤
    text = "This is a bad example with some terrible words."
    filtered_text = filter.filter(text)
    print(f"原始文本: {text}")
    print(f"过滤后文本: {filtered_text}")
    print(f"包含敏感词: {filter.containsSensitiveWord(text)}")  # True
    
    # 测试DictionarySearchSPOJ
    print("\n=== 测试DictionarySearchSPOJ ===")
    dictionary = ["abc", "abcd", "abcde", "bcd", "bcde"]
    dict_spoj = DictionarySearchSPOJ(dictionary)
    results = dict_spoj.search("abc")
    print(f"前缀'abc'的单词: {results}")
    
    # 测试StatisticalProblemHDU
    print("\n=== 测试StatisticalProblemHDU ===")
    words_hdu = ["abc", "abcde", "abcdef", "bcd", "bcde"]
    stat_hdu = StatisticalProblemHDU(words_hdu)
    print(f"前缀'abc'的数量: {stat_hdu.prefix_count('abc')}")  # 3
    print(f"前缀'bc'的数量: {stat_hdu.prefix_count('bc')}")   # 2
    print(f"前缀'xyz'的数量: {stat_hdu.prefix_count('xyz')}")  # 0

'''\n""
Trie树核心思想与应用场景总结：

核心思想：
1. Trie树是一种专门用于处理字符串前缀的数据结构
2. 通过共享公共前缀来节省空间
3. 每个节点代表一个字符串前缀，从根节点到某一节点的路径表示一个完整字符串

应用场景：
1. 字符串检索：高效查找字典中的单词
2. 前缀匹配：自动补全、拼写检查、前缀搜索
3. 字符串排序：按字典序排序字符串
4. 文本处理：敏感词过滤、垃圾邮件识别
5. 网络路由：最长前缀匹配算法
6. DNA序列分析：生物信息学中的序列匹配

设计要点：
1. 节点结构：通常包含子节点引用和单词结束标记
2. 实现方式：可以使用数组或哈希表存储子节点
3. 优化策略：
   - 压缩路径（Compressed Trie）：合并只有一个子节点的节点
   - 双数组Trie（Double-Array Trie）：空间效率更高的实现
   - 后缀链接（Suffix Links）：用于AC自动机等高级应用

复杂度分析：
1. 时间复杂度：
   - 插入/查找：O(m)，m为字符串长度
   - 前缀匹配：O(m)
2. 空间复杂度：O(ALPHABET_SIZE * N * M)，其中N是字符串数量，M是平均长度

语言实现差异：
1. Java：使用类和引用，可使用数组或HashMap实现子节点
2. C++：使用结构体和指针，内存管理更灵活
3. Python：使用字典和类，实现简洁但性能略低

工程化考量：
1. 线程安全：需要考虑并发访问的同步问题
2. 内存优化：根据字符集大小选择合适的子节点存储方式
3. 异常处理：处理非法输入和边界情况
4. 性能调优：根据实际应用场景选择合适的Trie树变体
'''

